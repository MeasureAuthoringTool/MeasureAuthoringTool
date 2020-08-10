package mat.server.logging;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;

@Slf4j
public final class ServletLogging {
    private ServletLogging() {
    }

    public static void logOutgoingRequest(URI requestURI, String method, String headers, String body) {
        log.info("Outgoing Request Uri: {}", requestURI);
        log.info("Method: {}", method);
        log.info("Headers: {}", headers);

        if (StringUtils.isNotBlank(body)) {
            log.info("Request body : {}", body);
        }
    }

    public static void logOutgoingResponse(String statusCode, String statusText, long executionTime, String headers, String body) {
        log.info("Outgoing Response Status: {}", statusCode);

        if (StringUtils.isNotBlank(statusText)) {
            log.info("Status Text: {}", statusText);
        }

        log.info("Exec Time ms: {}", executionTime);
        log.info("Headers: {}", headers);
        log.info("Response body: {}", body);
    }
}
