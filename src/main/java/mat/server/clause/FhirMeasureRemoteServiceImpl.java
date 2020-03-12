package mat.server.clause;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.FhirMeasureRemoteService;
import mat.client.shared.MatException;
import mat.client.umls.service.VsacTicketInformation;
import mat.server.LoggedInUserUtil;
import mat.server.SpringRemoteServiceServlet;
import mat.server.service.FhirMeasureService;
import mat.server.service.VSACApiService;

import static mat.client.measure.ManageMeasureSearchModel.Result;

public class FhirMeasureRemoteServiceImpl extends SpringRemoteServiceServlet implements FhirMeasureRemoteService {

    private static final long serialVersionUID = 2280421300224680146L;

    private static final Log logger = LogFactory.getLog(FhirMeasureRemoteServiceImpl.class);

    @Autowired
    private FhirMeasureService fhirMeasureService;
    @Autowired
    private VSACApiService vsacApiService;

    @Override
    public FhirConvertResultResponse convert(Result sourceMeasure) throws MatException {
        logger.info("Converting  measureId: " + sourceMeasure.getId());

        String sessionId = getThreadLocalRequest().getSession().getId();
        VsacTicketInformation vsacTicketInformation = this.vsacApiService.getTicketGrantingTicket(sessionId);
        if (vsacTicketInformation == null) {
            throw new MatException("Cannot get a granting ticket");
        }
        try {
            return fhirMeasureService.convert(sourceMeasure, vsacTicketInformation.getTicket(), LoggedInUserUtil.getLoggedInUser());
        } catch (MatException e) {
            logger.error("Error calling fhirMeasureService.convert",e);
            throw e;
        } catch (Exception e) {
            logger.error("Error calling fhirMeasureService.convert",e);
            throw new MatException(e.getMessage(),e);
        }
    }

}
