package mat.server.logging;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;

import java.net.URI;

public final class ServletLogging {
    private static final Log log = LogFactory.getLog(ServletLogging.class);

    private ServletLogging() {
    }

    public static void logIncomingRequest(String requestURI, String queryString, String method, String headers, String body) {
//        if (log.isInfoEnabled()) {
//            StringBuilder builder = new StringBuilder();
//            builder.append("\nMAT request URI=" + requestURI + " METHOD=" + method + "\n" +
//                    "\tHeaders=\n" + headers + "\n" +
//                    "\tBody=\n " + StringUtils.defaultString(body));
//            log.info(builder.toString());
//        }
    }

    public static void logIncomingResponse(String status, long executionTime, String headers, String body) {
//        if (log.isInfoEnabled()) {
//            StringBuilder builder = new StringBuilder();
//            builder.append("\nMAT response. Time=" + executionTime + "ms Status=" + status + "\n" +
//                    "\tHeaders=" + headers + "\n" +
//                    "\tBody=\n" + StringUtils.defaultString(body));
//            log.info(builder.toString());
//        }
    }

    public static void logOutgoingRequest(URI requestURI, String method, String headers, String body) {
//        if (log.isInfoEnabled()) {
//            StringBuilder builder = new StringBuilder();
//            builder.append("\nExternal request. URI=" + requestURI + " METHOD=" + method + "\n" +
//                    "\tHeaders=\n" + headers + "\n" +
//                    "\tBody=\n " + StringUtils.defaultString(body));
//            log.info(builder.toString());
//        }
    }

    public static void logOutgoingResponse(String statusCode, String statusText, long executionTime, String headers, String body) {
//        if (log.isInfoEnabled()) {
//            StringBuilder builder = new StringBuilder();
//            builder.append("\nExternal response. Time="+ executionTime +"ms Status=" + statusCode + " StatusText=" + StringUtils.defaultString(statusText) + "\n" +
//                    "\tHeaders=\n" + headers + "\n" +
//                    "\tBody=\n" + StringUtils.defaultString(body));
//            log.info(builder.toString());
//        }
    }
}
