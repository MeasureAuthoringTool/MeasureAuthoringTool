package mat.server.clause;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import mat.client.measure.service.FhirMeasureRemoteService;
import mat.client.shared.MatException;
import mat.client.shared.MatRuntimeException;
import mat.dao.clause.MeasureDAO;
import mat.dao.impl.AuthorDAOImpl;
import mat.server.SpringRemoteServiceServlet;
import mat.server.service.FhirOrchestrationGatewayService;

import static mat.client.measure.ManageMeasureSearchModel.Result;

public class FhirMeasureRemoteServiceImpl extends SpringRemoteServiceServlet implements FhirMeasureRemoteService {

    private static final Log logger = LogFactory.getLog(AuthorDAOImpl.class);

    @Autowired
    private FhirOrchestrationGatewayService fhirOrchestrationGatewayService;
    @Autowired
    private MeasureDAO measureDAO;

    @Override
    public Result convert(Result currentMeasure) throws MatException {
        if (!currentMeasure.isFhirConvertible()) {
            throw new MatException("Measure cannot be converted to FHIR");
        }
        ConversionResultDto convertResult;
        try {
            convertResult = fhirOrchestrationGatewayService.convert(currentMeasure.getId());
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
