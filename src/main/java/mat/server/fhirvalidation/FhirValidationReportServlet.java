package mat.server.fhirvalidation;

import mat.client.shared.MatException;
import org.slf4j.LoggerFactory;
import mat.server.service.VSACApiService;
import mat.server.service.fhirvalidationreport.CqlLibraryValidationReportImpl;
import mat.server.service.fhirvalidationreport.FhirValidationReport;
import mat.server.service.fhirvalidationreport.MeasureValidationReportImpl;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FhirValidationReportServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(FhirValidationReportServlet.class);
    private static final String ID_PARAM = "id";
    private static final String CONVERTED_PARAM = "converted";
    private static final String TYPE_PARAM = "type";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String SHOW_STACK_TRACE_PARAM = "showStackTrace";
    private static final String CQL_LIBRARY = "Library";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            var sessionId = request.getSession().getId();
            var apiKey = getVsacApiKey(sessionId);

            var converted = isConverted(request);
            var id = request.getParameter(ID_PARAM);

            var validationReportService = getFhirValidationReportService(request);
            var validationReport = validationReportService.generateReport(id, apiKey, converted);

            response.setHeader(CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
            response.getOutputStream().write(validationReport.getBytes());
        } catch (Exception e) {
            var errorResponse = toErrorResponse(request, e);
            response.setHeader(CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getOutputStream().write(errorResponse.getBytes());
        }


    }

    /**
     * Given sessionId, delegates to VsacApiService to fetch the VSAC ticket
     * @param sessionId
     * @return mat.vsac Ticket
     * @throws MatException
     */
    private String getVsacApiKey(String sessionId) throws MatException {
    		var vsacTicketInformation = getVascApiService().getVsacInformation(sessionId);
        if (vsacTicketInformation == null) {
        	throw new MatException("Cannot get apiKey");
        }
        return vsacTicketInformation.getApiKey();
    }

    private String toErrorResponse(HttpServletRequest request, Exception e) {
        var tmpMsg1 = "An error occurred while validating the FHIR conversion. ";
        logger.error(tmpMsg1, e);

        var errMsg = new StringBuilder(tmpMsg1)
                .append("Please try again later. ")
                .append("If this continues, please contact the mat help desk.");

        if (isShowStackTrace(request)) {
            errMsg.append(" Error ")
                .append(System.lineSeparator())
                .append(ExceptionUtils.getRootCauseMessage(e))
                .append(ExceptionUtils.getStackTrace(e));
        }

        return errMsg.toString();
    }

    private boolean isShowStackTrace(HttpServletRequest req) {
        return Boolean.parseBoolean(req.getParameter(SHOW_STACK_TRACE_PARAM));
    }

    private boolean isConverted(HttpServletRequest req) {
        return Boolean.parseBoolean(req.getParameter(CONVERTED_PARAM));
    }

    private FhirValidationReport getFhirValidationReportService(HttpServletRequest request) {
        var type = request.getParameter(TYPE_PARAM);
        var id = request.getParameter(ID_PARAM);

        if (CQL_LIBRARY.equalsIgnoreCase(type)) {
            logger.debug("Generating validation report for CQL library id: " + id);
            return getLibraryValidationReportService();
        } else {
            logger.debug("Generating validation report for measure id: " + id);
            return getMeasureValidationReportService();
        }
    }


    private WebApplicationContext getApplicationContext() {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

    private FhirValidationReport getLibraryValidationReportService() {
        return getApplicationContext().getBean(CqlLibraryValidationReportImpl.class);
    }
    private FhirValidationReport getMeasureValidationReportService() {
        return getApplicationContext().getBean(MeasureValidationReportImpl.class);
    }

    private VSACApiService getVascApiService() {
        return getApplicationContext().getBean(VSACApiService.class);
    }

}
