package mat.server.logging;

import mat.server.LoggedInUserUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RequestResponseLoggingFilter implements Filter {
    private static final String HEADER_TEMPLATE = "%s:\"%s\"";
    public static ThreadLocal<String> logHeaders = new ThreadLocal<>();
    private static final Log log = LogFactory.getLog(RequestResponseLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        //Empty impl
    }

    @Override
    public void destroy() {
        //Empty impl
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest req = new BufferingInputFilter.NonBufferingRequestWrapper((HttpServletRequest) request);
        HttpServletResponse res = (HttpServletResponse) response;
        setupMDC(req);
        logRequest(req);
        chain.doFilter(req, res);
        logResponse(res, System.currentTimeMillis() - start);
        clearMDC();
    }

    public void logRequest(HttpServletRequest request) {
        if (log.isDebugEnabled()) {
            String body = "";

            try {
                body = IOUtils.toString(request.getInputStream(), "utf-8");
            } catch (IOException e) {
                log.error("Cannot find body", e);
            }

            String headers = processRequestHeaders(request);
            ServletLogging.logIncomingRequest(request.getRequestURI(),
                    request.getQueryString(),
                    request.getMethod(),
                    headers,
                    body);
        }
    }

    private String processRequestHeaders(HttpServletRequest request) {
        List<String> headers = new ArrayList<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            headers.add(String.format(HEADER_TEMPLATE, name, value));
        }

        return String.join(", ", headers);
    }

    private void logResponse(HttpServletResponse response, long executionTime) {
        if (log.isDebugEnabled()) {
            HttpStatus httpStatus = HttpStatus.resolve(response.getStatus());
            String statusText = httpStatus == null ? "" : httpStatus.getReasonPhrase();
            String status = response.getStatus() + " " + statusText;

            String headers = processResponseHeadersForLog(response);
            String body = ThreadLocalBody.getBody();

            ServletLogging.logIncomingResponse(status, executionTime, headers, body);
        }
    }

    private String processResponseHeadersForLog(HttpServletResponse response) {
        return response.getHeaderNames().stream()
                .map(name -> String.format(HEADER_TEMPLATE, name, response.getHeader(name)))
                .collect(Collectors.joining(", "));
    }

    private void setupMDC(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        String userId = LoggedInUserUtil.getLoggedInUserId();
        String sessionId = session != null ? session.getId() : null;
        String transactionId = UUID.randomUUID().toString();
        String requestId = UUID.randomUUID().toString();

        if (StringUtils.isNotBlank(userId)) {
            MDC.put("userId", userId);
        }
        if (StringUtils.isNotBlank(sessionId)) {
            MDC.put("sessionId", sessionId);
        }
        MDC.put("transactionId", transactionId);
        MDC.put("requestId", requestId);

        StringBuilder b = new StringBuilder();
        org.jboss.logging.MDC.getMap().forEach((k, v) -> b.append(" ").append(k).append("=").append(v));
        b.append(" ");
        logHeaders.set(b.toString());
    }

    private void clearMDC() {
        MDC.clear();
        logHeaders.remove();
    }
}
