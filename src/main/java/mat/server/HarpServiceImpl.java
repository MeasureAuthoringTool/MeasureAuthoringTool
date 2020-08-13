package mat.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mat.client.login.service.HarpService;
import mat.client.shared.MatException;
import mat.server.logging.LogFactory;
import mat.server.util.ServerConstants;
import org.apache.commons.logging.Log;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static mat.shared.HarpConstants.HARP_FAMILY_NAME;
import static mat.shared.HarpConstants.HARP_FULLNAME;
import static mat.shared.HarpConstants.HARP_GIVEN_NAME;
import static mat.shared.HarpConstants.HARP_MIDDLE_NAME;
import static mat.shared.HarpConstants.HARP_PRIMARY_EMAIL_ID;


@Service
public class HarpServiceImpl extends SpringRemoteServiceServlet implements HarpService {
    private static final Log logger = LogFactory.getLog(HarpServiceImpl.class);

    private WebClient harpOtkaClient;

    @Override
    public boolean revoke(String accessToken) {
        logger.debug("Revoking Token::" + accessToken);
        ClientResponse response = revokeToken(accessToken);
        logger.debug(response.statusCode().toString());
        return response.statusCode().is2xxSuccessful();
    }

    @Override
    public boolean validateToken(String token) {
        TokenIntrospect introspect = validate(token);
        return introspect.isActive();
    }

    @Override
    public Map<String, String> getUserInfo(String accessToken) {
        UserInfo userinfo = userinfo(accessToken);
        Map<String, String> harpUserInfo = new HashMap<>();
        harpUserInfo.put(HARP_GIVEN_NAME, userinfo.getGivenName());
        harpUserInfo.put(HARP_MIDDLE_NAME, userinfo.getMiddleName());
        harpUserInfo.put(HARP_FAMILY_NAME, userinfo.getFamilyName());
        harpUserInfo.put(HARP_FULLNAME, userinfo.getName());
        harpUserInfo.put(HARP_PRIMARY_EMAIL_ID, userinfo.getEmail());
        harpUserInfo.values().forEach(logger::debug);
        return harpUserInfo;
    }

    @Override
    public String getHarpUrl() {
        return ServerConstants.getHarpUrl();
    }

    @Override
    public String getHarpBaseUrl() {
        return ServerConstants.getHarpBaseUrl();
    }

    @Override
    public String getHarpClientId() {
        logger.debug("getHarpClientId::" + ServerConstants.getHarpClientId());
        return ServerConstants.getHarpClientId();
    }

    private TokenIntrospect validate(String token) {
        return getClient()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/introspect")
                        .queryParam("token", token)
                        .queryParam("client_id", ServerConstants.getHarpClientId())
                        .queryParam("token_type_hint", "id_token")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .bodyToMono(TokenIntrospect.class).block();
    }

    private ClientResponse revokeToken(String token) {
        return getClient()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/revoke")
                        .queryParam("token", token)
                        .queryParam("client_id", getHarpClientId())
                        .queryParam("token_type_hint", "access_token")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .exchange()
                .onErrorResume(e -> Mono.error(new MatException("Unable to revoke token.", e)))
                .block();
    }

    private UserInfo userinfo(String token) {
        return getClient()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/userinfo")
                        .queryParam("client_id", getHarpClientId())
                        .build())
                .headers(h -> h.setBearerAuth(token))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .bodyToMono(UserInfo.class)
                .onErrorResume(e -> Mono.error(new MatException("Unable to retrieve userinfo.", e)))
                .block();
    }

    private WebClient getClient() {
        if (isNull(harpOtkaClient)) {
            this.harpOtkaClient = WebClient.create(getHarpUrl());
        }
        return harpOtkaClient;
    }

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class TokenIntrospect {
        private boolean isActive;
        private String username;
        private String name;
        private String email;
    }

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    static class UserInfo {
        private String email;
        private String familyName;
        private String givenName;
        private String middleName;
        private String name;
    }
}
