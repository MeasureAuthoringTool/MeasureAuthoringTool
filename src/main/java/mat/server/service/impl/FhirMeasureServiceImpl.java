package mat.server.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
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
    public ManageMeasureSearchModel.Result convert(ManageMeasureSearchModel.Result currentMeasure) throws MatException {
        if (!currentMeasure.isFhirConvertible()) {
            throw new MatException("Measure cannot be converted to FHIR");
        }
        ConversionResultDto convertResult;
        ManageMeasureDetailModel currentDetails = measureLibraryService.getMeasure(currentMeasure.getId());
        if (currentDetails.getFhirMeasureId() != null && !currentDetails.getFhirMeasureId().isEmpty()) {
            logger.debug("Removing existing fhir measure " + currentDetails.getFhirMeasureId());
            transactionTemplate.execute(status -> {
                Measure fhirMeasure = measureDAO.find(currentDetails.getFhirMeasureId());
                measureDAO.delete(fhirMeasure);
                return null;
            });
        }
        ManageMeasureSearchModel.Result result = transactionTemplate.execute(status -> {
            try {
                return measureCloningService.cloneForFhir(currentDetails);
            } catch (MatException e) {
                throw new MatRuntimeException(e);
            }
        });
        convertResult = fhirOrchestrationGatewayService.convert(result.getId());
        logger.info(convertResult);

        if (hasErrors(convertResult)) {
            throw new MatException("Error while converting measure to FHIR. Measure has validation errors.");
        }
        return currentMeasure;
    }

    private boolean hasErrors(ConversionResultDto convertResult) {
        boolean hasValueSetErrors = convertResult.getValueSetConversionResults().getValueSetResults().stream()
                .anyMatch(valueSetResult -> !Boolean.TRUE.equals(valueSetResult.getSuccess()));
        boolean hasLibraryErrors = !convertResult.getLibraryConversionResults().getLibraryFhirValidationErrors().isEmpty();
        boolean hasMeasureErrors = !convertResult.getMeasureConversionResults().getMeasureFhirValidationErrors().isEmpty();
        return hasValueSetErrors || hasLibraryErrors || hasMeasureErrors;
    }

}
