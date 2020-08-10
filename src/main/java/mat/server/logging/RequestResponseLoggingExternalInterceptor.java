package mat.server.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;

@Slf4j
public class RequestResponseLoggingExternalInterceptor extends RequestResponseLoggingInterceptor {
    @Override
    protected void processHeaders(HttpRequest request) {
        // No op
        log.trace("No header processing required");
    }
}

