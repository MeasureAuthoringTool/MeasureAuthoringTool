package mat.server.service.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import mat.cql.CqlToMatXml;
import mat.dao.clause.MeasureXMLDAO;
import mat.model.clause.MeasureXML;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.util.XmlProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import gov.cms.mat.fhir.rest.dto.ConversionOutcome;
import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.FhirValidationStatus;
import mat.client.shared.MatException;
import mat.client.shared.MatRuntimeException;
import mat.dao.clause.MeasureDAO;
import mat.model.clause.Measure;
import mat.server.service.FhirMeasureService;
import mat.server.service.FhirOrchestrationGatewayService;
import mat.server.service.MeasureCloningService;
import mat.server.service.MeasureLibraryService;
import mat.shared.SaveUpdateCQLResult;

@Service
public class FhirMeasureServiceImpl implements FhirMeasureService {
    public static boolean TEST_MODE = false;
    private static final Log logger = LogFactory.getLog(FhirMeasureServiceImpl.class);

    private final FhirOrchestrationGatewayService fhirOrchestrationGatewayService;

    private final MeasureLibraryService measureLibraryService;

    private final MeasureCloningService measureCloningService;

    private final MeasureDAO measureDAO;

    private final MeasureXMLDAO measureXMLDAO;

    private final TransactionTemplate transactionTemplate;

    private final XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();

    public FhirMeasureServiceImpl(FhirOrchestrationGatewayService fhirOrchestrationGatewayService, MeasureLibraryService measureLibraryService, MeasureCloningService measureCloningService, MeasureDAO measureDAO, MeasureXMLDAO measureXMLDAO, PlatformTransactionManager txManager) {
        this.fhirOrchestrationGatewayService = fhirOrchestrationGatewayService;
        this.measureLibraryService = measureLibraryService;
        this.measureCloningService = measureCloningService;
        this.measureDAO = measureDAO;
        this.measureXMLDAO = measureXMLDAO;
        this.transactionTemplate = new TransactionTemplate(txManager);
    }

    @Override
    public FhirConvertResultResponse convert(ManageMeasureSearchModel.Result sourceMeasure, String vsacGrantingTicket, String loggedinUserId) throws MatException {
        if (!sourceMeasure.isFhirConvertible()) {
            throw new MatException("Measure cannot be converted to FHIR");
        }
        FhirConvertResultResponse fhirConvertResultResponse = new FhirConvertResultResponse();
        fhirConvertResultResponse.setSourceMeasure(sourceMeasure);
        measureLibraryService.recordRecentMeasureActivity(sourceMeasure.getId(), loggedinUserId);

        ManageMeasureDetailModel sourceMeasureDetails = loadMeasureAsDetailsForCloning(sourceMeasure);
        dropFhirMeasureIfExists(sourceMeasureDetails);

        ConversionResultDto conversionResult = fhirOrchestrationGatewayService.convert(sourceMeasure.getId(), vsacGrantingTicket, sourceMeasure.isDraft());
        Optional<String> fhirCqlOpt = getFhirCql(conversionResult);

        FhirValidationStatus validationStatus = createValidationStatus(conversionResult);
        fhirConvertResultResponse.setValidationStatus(validationStatus);

        if (!fhirCqlOpt.isPresent()) {
            // If there is no FHIR CQL, then we don't persist the measure. FHIR measure cannot be created.
            throw new MatException("Your measure cannot be converted to FHIR. Outcome: " + validationStatus.getOutcome() + " Error Reason: " + validationStatus.getErrorReason());
        } else {
            persistFhirMeasure(loggedinUserId, fhirConvertResultResponse, sourceMeasureDetails, fhirCqlOpt.get());
        }

        return fhirConvertResultResponse;
    }

    /**
     * The only purpose of this is to make it mockable.
     * setMeasureXMLAsByteArray has Hibernate LOB creation calls on the current session inside of it.
     * This makes it difficult to mock away in tests.
     */
    public void saveMeasureXml(MeasureXML existingMeasureXml, String newXml) {
        if (!TEST_MODE) {
            existingMeasureXml.setMeasureXMLAsByteArray(newXml);
            measureXMLDAO.save(existingMeasureXml);
        }
    }


    private void persistFhirMeasure(String loggedinUserId,
                                    FhirConvertResultResponse fhirConvertResultResponse,
                                    ManageMeasureDetailModel sourceMeasureDetails,
                                    String convertedCql) {
        // Just to make sure the change is atomic and performed within the same single transaction.
        transactionTemplate.executeWithoutResult(status -> {
            try {
                ManageMeasureSearchModel.Result fhirMeasure = measureCloningService.cloneForFhir(sourceMeasureDetails);
                fhirConvertResultResponse.setFhirMeasure(fhirMeasure);

                //Update the MAT xml.
                convertXml(fhirMeasure.getId(), convertedCql);

                measureLibraryService.recordRecentMeasureActivity(fhirMeasure.getId(), loggedinUserId);
            } catch (MatException | MatRuntimeException e) {
                logger.error("persistFhirMeasure error",e);
                throw new MatRuntimeException("Mat cannot persist converted FHIR measure CQL file.");
            }
        });
    }

    private void convertXml(String measureId, String cql) throws MatException {
        try {
            MeasureXML measureXml = measureXMLDAO.findForMeasure(measureId);
            String sourceMeasureXml = measureXml.getMeasureXMLAsString();

            XmlProcessor processor = new XmlProcessor(sourceMeasureXml);
            String cqlModelXmlFrag = processor.getXmlByTagName("cqlLookUp");
            CQLModel sourceCqlModel = (CQLModel) xmlMarshalUtil.convertXMLToObject("CQLModelMapping.xml",
                    cqlModelXmlFrag,
                    CQLModel.class);

            CqlToMatXml converter = new CqlToMatXml(sourceCqlModel, cql);
            CQLModel destinationCqlModel = converter.convert();

            String newCqlModelXmlFrag = CQLUtilityClass.getXMLFromCQLModel(destinationCqlModel);
            String destinationMeasureXml = processor.replaceNode(newCqlModelXmlFrag, "cqlLookUp", "measure");
            saveMeasureXml(measureXml, destinationMeasureXml);
        } catch (IOException | MappingException | MarshalException | ValidationException e) {
            throw new MatException("Error converting mat xml", e);
        }
    }

    private Optional<String> getFhirCql(ConversionResultDto conversionResult) {
        return Optional.ofNullable(conversionResult.getLibraryConversionResults()).stream()
                .flatMap(libConvRes -> libConvRes.stream())
                .map(cqlLibRes -> cqlLibRes.getCqlConversionResult())
                .filter(el -> el != null)
                .map(el -> el.getFhirCql())
                .filter(StringUtils::isNotBlank)
                .findFirst();
    }

    private ConversionResultDto validateSourceMeasureForFhirConversion(ManageMeasureSearchModel.Result sourceMeasure, String vsacGrantingTicket) {
        return fhirOrchestrationGatewayService.validate(sourceMeasure.getId(), vsacGrantingTicket, sourceMeasure.isDraft());
    }

    private ManageMeasureDetailModel loadMeasureAsDetailsForCloning(ManageMeasureSearchModel.Result sourceMeasure) {
        return measureLibraryService.getMeasure(sourceMeasure.getId());
    }

    private FhirValidationStatus createValidationStatus(ConversionResultDto convertResult) {
        FhirValidationStatus validationSummary = new FhirValidationStatus();
        validationSummary.setErrorReason(convertResult.getErrorReason());
        validationSummary.setOutcome(convertResult.getOutcome() != null ? convertResult.getOutcome().toString() : null);
        validationSummary.setValidationPassed(ConversionOutcome.SUCCESS.equals(convertResult.getOutcome()));
        return validationSummary;
    }

    private void dropFhirMeasureIfExists(ManageMeasureDetailModel currentDetails) {
        transactionTemplate.execute(status -> {
            Measure currentSourceMeasure = measureDAO.find(currentDetails.getId());
            Collection<Measure> fhirMeasures = currentSourceMeasure.getFhirMeasures();
            fhirMeasures.stream().forEach(fhirMeasure ->
                    logger.debug("Removing existing fhir measure " + fhirMeasure.getId())
            );
            // removeOrphan = true should remove the records
            currentSourceMeasure.getFhirMeasures().clear();
            return null;
        });
    }

}
