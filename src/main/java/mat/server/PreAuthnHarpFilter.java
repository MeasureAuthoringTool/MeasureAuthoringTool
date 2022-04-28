package mat.server;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class PreAuthnHarpFilter extends AbstractPreAuthenticatedProcessingFilter {
    private static final Logger logger = LoggerFactory.getLogger(PreAuthnHarpFilter.class);

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        try {
            if(request.getMethod().equals("POST") && request.getRequestURI().contains("/Mat.html")) {
                String token = IOUtils.toString(request.getReader());
                return token.split("=")[1];
            }
        }catch (IOException e) {
            logger.error("getPreAuthenticatedPrincipal", e);
        }
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return null;
    }
}
