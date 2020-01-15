package mat.server.service.impl;

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
import mat.server.service.MeasureCloningService;
import mat.server.service.MeasureLibraryService;

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
    public FhirConvertResultResponse convert(ManageMeasureSearchModel.Result sourceMeasure, String loggedinUserId) throws MatException {
        if (!sourceMeasure.isFhirConvertible()) {
            throw new MatException("Measure cannot be converted to FHIR");
        }
        FhirConvertResultResponse fhirConvertResultResponse = new FhirConvertResultResponse();
        fhirConvertResultResponse.setSourceMeasure(sourceMeasure);
        measureLibraryService.recordRecentMeasureActivity(sourceMeasure.getId(), loggedinUserId);

        FhirValidationStatus sourceValidationStatus = validateSourceMeasureForFhirConversion(sourceMeasure);

        if (!sourceValidationStatus.isValidationPassed()) {
            fhirConvertResultResponse.setValidationStatus(sourceValidationStatus);
        } else {
            ManageMeasureDetailModel sourceMeasureDetails = loadMeasureAsDetailsForCloning(sourceMeasure);
            dropFhirMeasureIfExists(sourceMeasureDetails);
            ManageMeasureSearchModel.Result fhirMeasure = cloneSourceToFhir(sourceMeasureDetails);
            fhirConvertResultResponse.setFhirMeasure(fhirMeasure);

            // We convert source measure instead of fhir measure, because we want to convert a versioned measure under the original measure id (SOURCE_MEASURE_ID)
            // If we use a fhir measure id, then it will be abandoned on FHIR server end, when re-converted
            ConversionResultDto fhirConvertResult = fhirOrchestrationGatewayService.convert(sourceMeasure.getId(), sourceMeasure.isDraft());
            fhirConvertResultResponse.setValidationStatus(createValidationStatus(fhirConvertResult));
            measureLibraryService.recordRecentMeasureActivity(fhirMeasure.getId(), loggedinUserId);
        }

        return fhirConvertResultResponse;
    }

    private FhirValidationStatus validateSourceMeasureForFhirConversion(ManageMeasureSearchModel.Result sourceMeasure) {
        ConversionResultDto sourceValidationResult = fhirOrchestrationGatewayService.validate(sourceMeasure.getId(), sourceMeasure.isDraft());
        return createValidationStatus(sourceValidationResult);
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

    private ManageMeasureSearchModel.Result cloneSourceToFhir(ManageMeasureDetailModel currentDetails) {
        return transactionTemplate.execute(status -> {
            try {
                return measureCloningService.cloneForFhir(currentDetails);
            } catch (MatException e) {
                throw new MatRuntimeException(e);
            }
        });
    }

    private void dropFhirMeasureIfExists(ManageMeasureDetailModel currentDetails) {
        if (currentDetails.getFhirMeasureId() != null && !currentDetails.getFhirMeasureId().isEmpty()) {
            logger.debug("Removing existing fhir measure " + currentDetails.getFhirMeasureId());
            transactionTemplate.execute(status -> {
                Measure existingFhirMeasure = measureDAO.find(currentDetails.getFhirMeasureId());
                measureDAO.delete(existingFhirMeasure);
                return null;
            });
        }
    }

}
