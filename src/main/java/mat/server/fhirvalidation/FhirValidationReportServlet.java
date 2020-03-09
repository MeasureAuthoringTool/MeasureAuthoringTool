package mat.server.fhirvalidation;

import mat.server.service.FhirValidationReportService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FhirValidationReportServlet extends HttpServlet {

    private static final String ID_PARAM = "id";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final Log logger = LogFactory.getLog(FhirValidationReportServlet.class);

    private ApplicationContext applicationContext;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        String id = req.getParameter(ID_PARAM);
        logger.info("Generating validation report for measure id: " + id);
        FhirValidationReportService fhirValidationReportService = getValidationReportService();
        String validationReport = "";
        try {
            validationReport = fhirValidationReportService.getFhirConversionReportForMeasure(id);
        } catch (Exception e) {
            logger.error("Exception occurred while generation FHIR conversion report:", e);
            validationReport = "An error occurred while validating the FHIR conversion. Please try again later.";
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        response.setHeader(CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
        response.getOutputStream().write(validationReport.getBytes());
    }

    /**
     * Getter for FhirValidationReportService
     * @return an instance of FhirValidationReportService
     */
    private FhirValidationReportService getValidationReportService() {
     return applicationContext.getBean(FhirValidationReportService.class);
    }
}
