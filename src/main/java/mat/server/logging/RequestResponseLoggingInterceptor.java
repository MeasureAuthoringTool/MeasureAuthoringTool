package mat.server.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Log log = LogFactory.getLog(RequestResponseLoggingInterceptor.class);
    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        long start = System.currentTimeMillis();
        processHeaders(request);
        logRequest(request, new String(body, StandardCharsets.UTF_8));

        ClientHttpResponse response = execution.execute(request, body);

        logResponse(response, start);
        return response;
    }

    protected  void processHeaders(HttpRequest request) {
    }

    private void logResponse(ClientHttpResponse response, long start) throws IOException {
        if (log.isDebugEnabled()) {
            long executionTime = System.currentTimeMillis() - start;
            String body =  StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());

            String headers = "";
            if (!response.getHeaders().isEmpty()) {
                headers = response.getHeaders().toString();
            }

            ServletLogging.logOutgoingResponse(response.getStatusCode().toString(),
                    response.getStatusText(),
                    executionTime,
                    headers,
                    body);
        }
    }

    private void logRequest(HttpRequest request, String body) {
        if (log.isDebugEnabled()) {

            String headers = "";
            if (!request.getHeaders().isEmpty()) {
                headers = request.getHeaders().toString();
            }

            String method = "";
            if (request.getMethod() != null) {
                method = request.getMethod().name();
            }

            ServletLogging.logOutgoingRequest(request.getURI(),
                    method,
                    headers,
                    body);

        }
    }
}