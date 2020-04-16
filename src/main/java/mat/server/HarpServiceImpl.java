package mat.server;

import mat.client.login.service.HarpService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class HarpServiceImpl extends SpringRemoteServiceServlet implements HarpService {
    private static final Log logger = LogFactory.getLog(HarpServiceImpl.class);

    @Value("https://dev-120913.okta.com/oauth2/v1")
    private String harpUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean logout(String idToken) {
        logger.info("logout::idToken::"+idToken);
//        ResponseEntity<Void> response = restTemplate.exchange("https://dev-120913.okta.com/oauth2/v1" + "/logout?id_token_hint={idToken}",
//                HttpMethod.GET, null, Void.class,
//                Map.of("idToken", idToken));

        ResponseEntity<Void> getResonse = restTemplate.getForEntity(
                "https://dev-120913.okta.com/oauth2/v1" + "/logout?id_token_hint={idToken}",
                Void.class, Map.of("idToken", idToken));
        logger.info("logout::statusCode::"+getResonse.getStatusCode().toString());
        return getResonse.getStatusCode().is2xxSuccessful();
    }

    @Override
    public boolean revoke(String accessToken) {
        return false;
    }
}
