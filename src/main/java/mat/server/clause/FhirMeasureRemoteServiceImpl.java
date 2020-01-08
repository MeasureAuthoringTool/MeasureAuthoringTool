package mat.server.clause;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.service.FhirMeasureRemoteService;
import mat.client.measure.service.MeasureCloningRemoteService;
import mat.client.shared.MatException;
import mat.client.shared.MatRuntimeException;
import mat.dao.impl.AuthorDAOImpl;
import mat.server.SpringRemoteServiceServlet;
import mat.server.service.FhirOrchestrationGatewayService;
import mat.server.service.MeasureLibraryService;

import static mat.client.measure.ManageMeasureSearchModel.Result;

public class FhirMeasureRemoteServiceImpl extends SpringRemoteServiceServlet implements FhirMeasureRemoteService {

    private static final Log logger = LogFactory.getLog(AuthorDAOImpl.class);

    @Autowired
    private FhirOrchestrationGatewayService fhirOrchestrationGatewayService;
    @Autowired
    private MeasureLibraryService measureLibraryService;

    @Autowired
    private MeasureCloningRemoteService measureCloningRemoteService;

    @Override
    public Result convert(Result currentMeasure) throws MatException {
        if (!currentMeasure.isFhirConvertible()) {
            throw new MatException("Measure cannot be converted to FHIR");
        }

        ManageMeasureDetailModel currentDetails = measureLibraryService.getMeasure(currentMeasure.getId());
        Result result = measureCloningRemoteService.cloneExistingMeasure(currentDetails);

        ConversionResultDto convertResult;
        try {
            convertResult = fhirOrchestrationGatewayService.convert(result.getId());
            logger.info(convertResult);
        } catch (MatRuntimeException e) {
            logger.error(e);
            throw new MatException(e);
        }
        if (hasErrors(convertResult)) {
            MatException e = new MatException("Error while converting measure to FHIR");
            logger.error(e);
            throw e;
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
