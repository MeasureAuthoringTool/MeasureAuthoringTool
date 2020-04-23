package mat.server;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class HarpFilterAuthorizationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (harpService.validateToken(authentication.getPrincipal().toString())) {
            authentication.setAuthenticated(true);
        }
        return authentication;
    }
}
