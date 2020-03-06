package mat.server.fhirvalidation;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import mat.client.shared.MatException;
import mat.client.umls.service.VsacTicketInformation;
import mat.server.service.FhirValidationReportService;
import mat.server.service.VSACApiService;

public class FhirValidationReportServlet extends HttpServlet {

    private static final String ID_PARAM = "id";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final Log logger = LogFactory.getLog(FhirValidationReportServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String sessionId = req.getSession().getId();
        String id = req.getParameter(ID_PARAM);
        logger.info("Generating validation report for measure id: " + id);
        String validationReport = "";
        try {
            VsacTicketInformation vsacTicketInformation = getVascApiService().getTicketGrantingTicket(sessionId);
            if (vsacTicketInformation == null) {
                throw new MatException("Cannot get a granting ticket");
            }
            validationReport = getValidationReportService().getFhirConversionReportForMeasure(id, vsacTicketInformation.getTicket());
        } catch (Exception e) {
            logger.error("Exception occurred while generation FHIR conversion report:", e);
            validationReport = "An error occurred while validating the FHIR conversion. Please try again later. " +
                    "If this continues please contact the mat help desk.";
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        response.setHeader(CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
        response.getOutputStream().write(validationReport.getBytes());
    }

    private WebApplicationContext getApplicationContext() {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

    private FhirValidationReportService getValidationReportService() {
        return getApplicationContext().getBean(FhirValidationReportService.class);
    }

    private VSACApiService getVascApiService() {
        return getApplicationContext().getBean(VSACApiService.class);
    }

}
