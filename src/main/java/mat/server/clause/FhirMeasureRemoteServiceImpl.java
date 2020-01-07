package mat.server.clause;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import mat.client.measure.service.FhirMeasureRemoteService;
import mat.client.shared.MatException;
import mat.dao.clause.MeasureDAO;
import mat.dao.impl.AuthorDAOImpl;
import mat.server.SpringRemoteServiceServlet;
import mat.server.service.FhirConvertServerSideService;

import static mat.client.measure.ManageMeasureSearchModel.Result;

public class FhirMeasureRemoteServiceImpl extends SpringRemoteServiceServlet implements FhirMeasureRemoteService {

    private static final Log logger = LogFactory.getLog(AuthorDAOImpl.class);

    @Autowired
    private FhirConvertServerSideService fhirConvertServerSideService;
    @Autowired
    private MeasureDAO measureDAO;

    @Override
    public Result convert(Result currentMeasure) throws MatException {
        ConversionResultDto convertResult = fhirConvertServerSideService.convert(currentMeasure.getId());
        boolean hasValueSetErrors = convertResult.getValueSetConversionResults().getValueSetResults().stream().anyMatch(valueSetResult -> !Boolean.TRUE.equals(valueSetResult.getSuccess()));
        boolean hasLibraryErrors = !convertResult.getLibraryConversionResults().getLibraryFhirValidationErrors().isEmpty();
        boolean hasMeasureErrors = !convertResult.getMeasureConversionResults().getMeasureFhirValidationErrors().isEmpty();
        logger.info(convertResult);
        if (hasValueSetErrors || hasLibraryErrors || hasMeasureErrors) {
            MatException e = new MatException("FHIR conversion has errors");
            logger.error(e);
            throw e;
        }
        return currentMeasure;
    }

}
