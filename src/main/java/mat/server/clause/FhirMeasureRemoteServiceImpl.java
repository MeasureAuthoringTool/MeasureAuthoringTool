package mat.server.clause;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.FhirMeasureRemoteService;
import mat.client.shared.MatException;
import mat.server.SpringRemoteServiceServlet;
import mat.server.service.FhirMeasureService;

import static mat.client.measure.ManageMeasureSearchModel.Result;

public class FhirMeasureRemoteServiceImpl extends SpringRemoteServiceServlet implements FhirMeasureRemoteService {

    private static final long serialVersionUID = 2280421300224680146L;

    private static final Log logger = LogFactory.getLog(FhirMeasureRemoteServiceImpl.class);

    @Autowired
    private FhirMeasureService fhirMeasureService;

    @Override
    public FhirConvertResultResponse convert(Result sourceMeasure) throws MatException {
        logger.info("Converting  measureId: " + sourceMeasure.getId());
        try {
            return fhirMeasureService.convert(sourceMeasure);
        } catch (MatException e) {
            logger.error(e);
            throw e;
        } catch (Exception e) {
            logger.error(e);
            throw new MatException(e);
        }
    }

}
