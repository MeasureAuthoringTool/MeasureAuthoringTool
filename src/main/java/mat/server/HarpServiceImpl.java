package mat.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mat.client.login.service.HarpService;
import mat.client.shared.MatException;
import mat.server.util.ServerConstants;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;

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
}
