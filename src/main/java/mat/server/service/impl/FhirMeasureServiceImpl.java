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
        fhirConvertResultResponse.setValidationStatus(createValidationStatus(conversionResult));

        Optional<String> fhirCqlOpt = Optional.ofNullable(conversionResult.getLibraryConversionResults()).stream()
                .flatMap(libConvRes -> libConvRes.stream())
                .map(cqlLibRes -> cqlLibRes.getCqlConversionResult())
                .filter(el -> el != null)
                .map(el -> el.getFhirCql())
                .filter(StringUtils::isNotBlank)
                .findFirst();

        if (!fhirCqlOpt.isPresent()) {
            fhirConvertResultResponse.setSuccess(false);
        } else {
            ManageMeasureSearchModel.Result fhirMeasure = cloneSourceToFhir(sourceMeasureDetails);
            fhirConvertResultResponse.setFhirMeasure(fhirMeasure);
            SaveUpdateCQLResult cqlResult = measureLibraryService.saveCQLFile(fhirMeasure.getId(), fhirCqlOpt.get());
            fhirConvertResultResponse.setSuccess(cqlResult.isSuccess());

            measureLibraryService.recordRecentMeasureActivity(fhirMeasure.getId(), loggedinUserId);
        }

        return fhirConvertResultResponse;
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
