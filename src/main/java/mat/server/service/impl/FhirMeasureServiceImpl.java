package mat.server.service.impl;

import java.util.Collection;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log logger = LogFactory.getLog(FhirMeasureServiceImpl.class);

    private final FhirOrchestrationGatewayService fhirOrchestrationGatewayService;

    private final MeasureLibraryService measureLibraryService;

    private final MeasureCloningService measureCloningService;

    private final MeasureDAO measureDAO;

    private final TransactionTemplate transactionTemplate;

    public FhirMeasureServiceImpl(FhirOrchestrationGatewayService fhirOrchestrationGatewayService, MeasureLibraryService measureLibraryService, MeasureCloningService measureCloningService, MeasureDAO measureDAO, PlatformTransactionManager txManager) {
        this.fhirOrchestrationGatewayService = fhirOrchestrationGatewayService;
        this.measureLibraryService = measureLibraryService;
        this.measureCloningService = measureCloningService;
        this.measureDAO = measureDAO;
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

        ConversionResultDto conversionResult = validateSourceMeasureForFhirConversion(sourceMeasure, vsacGrantingTicket);
        FhirValidationStatus validationStatus = createValidationStatus(conversionResult);
        fhirConvertResultResponse.setValidationStatus(validationStatus);

        Optional<String> fhirCqlOpt = getFhirCql(conversionResult);

        if (!fhirCqlOpt.isPresent()) {
            // If there is no FHIR CQL, then we don't persist the measure. FHIR measure cannot be created.
            throw new MatException("Your measure cannot be converted to FHIR. Outcome: " + validationStatus.getOutcome() + " Error Reason: " + validationStatus.getErrorReason());
        } else {
            persistFhirMeasure(loggedinUserId, fhirConvertResultResponse, sourceMeasureDetails, fhirCqlOpt);
        }


        return fhirConvertResultResponse;
    }

    private void persistFhirMeasure(String loggedinUserId, FhirConvertResultResponse fhirConvertResultResponse, ManageMeasureDetailModel sourceMeasureDetails, Optional<String> fhirCqlOpt) {
        // Just to make sure the change is atomic and performed within the same single transaction.
        transactionTemplate.executeWithoutResult(status -> {
            try {
                ManageMeasureSearchModel.Result fhirMeasure = measureCloningService.cloneForFhir(sourceMeasureDetails);
                fhirConvertResultResponse.setFhirMeasure(fhirMeasure);
                SaveUpdateCQLResult cqlResult = measureLibraryService.saveCQLFile(fhirMeasure.getId(), fhirCqlOpt.get());
                if (!cqlResult.isSuccess()) {
                    throw new MatRuntimeException("Mat cannot persist converted FHIR measure CQL file.");
                }
                measureLibraryService.recordRecentMeasureActivity(fhirMeasure.getId(), loggedinUserId);
            } catch (MatException e) {
                throw new MatRuntimeException(e);
            }
        });
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
