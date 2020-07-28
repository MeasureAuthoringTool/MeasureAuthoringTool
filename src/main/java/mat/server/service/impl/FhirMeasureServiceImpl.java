package mat.server.service.impl;

import mat.client.measure.FhirMeasurePackageResult;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.CQLService;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.FhirValidationStatus;
import mat.client.shared.MatException;
import mat.client.shared.MatRuntimeException;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dto.fhirconversion.ConversionOutcome;
import mat.dto.fhirconversion.ConversionResultDto;
import mat.model.clause.Measure;
import mat.model.clause.MeasureXML;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.service.FhirMeasureRemoteCall;
import mat.server.service.FhirMeasureService;
import mat.server.service.MeasureAuditService;
import mat.server.service.MeasureCloningService;
import mat.server.service.MeasureLibraryService;
import mat.server.service.cql.FhirCqlParser;
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

import java.io.IOException;
import java.util.Optional;

@Service
public class FhirMeasureServiceImpl implements FhirMeasureService {
    public static boolean TEST_MODE = false;
    private static final Log logger = LogFactory.getLog(FhirMeasureServiceImpl.class);

    private final FhirMeasureRemoteCall fhirMeasureRemote;

    private final MeasureLibraryService measureLibraryService;

    private final MeasureCloningService measureCloningService;

    private final MeasureDAO measureDAO;

    private final MeasureXMLDAO measureXMLDAO;

    private final TransactionTemplate transactionTemplate;

    private final XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();

    private final CQLService cqlService;

    private final FhirCqlParser cqlParser;

    private final MeasureAuditService auditService;

    public FhirMeasureServiceImpl(FhirMeasureRemoteCall fhirOrchestrationGatewayService,
                                  MeasureLibraryService measureLibraryService,
                                  MeasureCloningService measureCloningService,
                                  MeasureDAO measureDAO,
                                  MeasureXMLDAO measureXMLDAO,
                                  PlatformTransactionManager txManager,
                                  CQLService cqlService,
                                  FhirCqlParser cqlParser,
                                  MeasureAuditService auditService) {
        this.fhirMeasureRemote = fhirOrchestrationGatewayService;
        this.measureLibraryService = measureLibraryService;
        this.measureCloningService = measureCloningService;
        this.measureDAO = measureDAO;
        this.measureXMLDAO = measureXMLDAO;
        this.transactionTemplate = new TransactionTemplate(txManager);
        this.cqlService = cqlService;
        this.cqlParser = cqlParser;
        this.auditService = auditService;
    }

    @Override
    public FhirConvertResultResponse convert(ManageMeasureSearchModel.Result sourceMeasure,
                                                    String vsacGrantingTicket,
                                                    String loggedinUserId,
                                                    boolean isUpdatingMatDB) throws MatException {
        if (!sourceMeasure.isFhirConvertible()) {
            throw new MatException("Measure cannot be converted to FHIR");
        }
        FhirConvertResultResponse fhirConvertResultResponse = new FhirConvertResultResponse();
        if (isUpdatingMatDB) {
            measureLibraryService.recordRecentMeasureActivity(sourceMeasure.getId(), loggedinUserId);
        }

        ManageMeasureDetailModel sourceMeasureDetails = loadMeasureAsDetailsForCloning(sourceMeasure);
        if (isUpdatingMatDB) {
            deleteFhirMeasuresInSet(sourceMeasureDetails.getMeasureSetId());
        }

        ConversionResultDto conversionResult = fhirMeasureRemote.convert(sourceMeasure.getId(), vsacGrantingTicket, sourceMeasure.isDraft());
        Optional<String> fhirCqlOpt = getFhirCql(conversionResult);

        FhirValidationStatus validationStatus = createValidationStatus(conversionResult);
        fhirConvertResultResponse.setValidationStatus(validationStatus);

        if (!fhirCqlOpt.isPresent()) {
            // If there is no FHIR CQL, then we don't persist the measure. FHIR measure cannot be created.
            throw new MatException("Your measure cannot be converted to FHIR. Outcome: " + validationStatus.getOutcome() + " Error Reason: " + validationStatus.getErrorReason());
        } else {
            fhirConvertResultResponse.setFhirCql(fhirCqlOpt.get());
            if (isUpdatingMatDB) {
                persistFhirMeasure(loggedinUserId, fhirConvertResultResponse, sourceMeasureDetails, fhirCqlOpt.get());
            }
        }

        return fhirConvertResultResponse;
    }

    @Override
    public String push(String measureId) {
        return fhirMeasureRemote.push(measureId);
    }


    @Override
    public FhirMeasurePackageResult packageMeasure(String measureId) {
        return fhirMeasureRemote.packageMeasure(measureId);
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

                //Update the MAT xml.
                convertXml(sourceMeasureDetails.getId(),
                        fhirMeasure.getId(),
                        fhirConvertResultResponse,
                        convertedCql);

                measureLibraryService.recordRecentMeasureActivity(fhirMeasure.getId(), loggedinUserId);

                auditService.recordMeasureEvent(fhirMeasure.getId(),
                        "Converted from QDM/CQL to FHIR",
                        "QDM measure " + sourceMeasureDetails.getCQLLibraryName(),
                        false);
            } catch (MatException | MatRuntimeException e) {
                logger.error("persistFhirMeasure error", e);
                throw new MatRuntimeException("Mat cannot persist converted FHIR measure CQL file.");
            }
        });
    }

    private void convertXml(String sourceMeasureId, String fhirMeasureId, FhirConvertResultResponse fhirConvertResultResponse, String cql) throws MatException {
        try {
            MeasureXML sourceMeasureXml = measureXMLDAO.findForMeasure(sourceMeasureId);
            String sourceMeasureXmlString = sourceMeasureXml.getMeasureXMLAsString();

            Measure fhirMeasure = measureDAO.find(fhirMeasureId);
            MeasureXML fhirMeasureXml = measureXMLDAO.findForMeasure(fhirMeasureId);

            XmlProcessor processor = new XmlProcessor(sourceMeasureXmlString);
            String cqlModelXmlFrag = processor.getXmlByTagName("cqlLookUp");
            CQLModel sourceCqlModel = (CQLModel) xmlMarshalUtil.convertXMLToObject("CQLModelMapping.xml",
                    cqlModelXmlFrag,
                    CQLModel.class);

            var destModel = cqlParser.parse(cql, sourceCqlModel).getCqlModel();

            String newCqlModelXmlFrag = CQLUtilityClass.getXMLFromCQLModel(destModel);
            String destinationMeasureXml = processor.replaceNode(newCqlModelXmlFrag, "cqlLookUp", "measure");
            saveMeasureXml(fhirMeasureXml, destinationMeasureXml);

            destModel.getCqlIncludeLibrarys().forEach(i -> {
                //Currently only includes global libs and they should all be there.
                //Just add them in.
                if (StringUtils.isNotBlank(i.getCqlLibraryId())) {
                    cqlService.saveCQLAssociation(i, fhirMeasureId);
                }
            });

            //All converted measures default to increase.
            fhirMeasure.getMeasureDetails().setImprovementNotation("increase");
            measureDAO.saveandReturnMaxEMeasureId(fhirMeasure);
            fhirConvertResultResponse.setFhirMeasureId(fhirMeasure.getId());
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

    private void deleteFhirMeasuresInSet(String setId) {
        logger.debug("deleteFhirMeasureIfExists : setId = " + setId);
        int removed = transactionTemplate.execute(status ->
                measureDAO.deleteFhirMeasuresBySetId(setId)
        );
        logger.debug("deleteFhirMeasureIfExists : removed " + removed);
    }
}