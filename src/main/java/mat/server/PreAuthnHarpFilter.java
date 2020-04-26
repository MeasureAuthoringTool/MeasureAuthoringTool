package mat.server;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class PreAuthnHarpFilter extends AbstractPreAuthenticatedProcessingFilter {
    private static final Log logger = LogFactory.getLog(PreAuthnHarpFilter.class);

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        try {
            if(request.getMethod().equals("POST") && request.getRequestURI().contains("/Mat.html")) {
                String token = IOUtils.toString(request.getReader());
                return token.split("=")[1];
            }
        }catch (IOException e) {
            logger.error(e);
        }
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return null;
    }
}
