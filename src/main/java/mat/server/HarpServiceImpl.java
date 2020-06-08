package mat.server;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import mat.shared.HarpConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mat.client.login.service.HarpService;
import mat.server.util.ServerConstants;

import static mat.shared.HarpConstants.HARP_FAMILY_NAME;
import static mat.shared.HarpConstants.HARP_FULLNAME;
import static mat.shared.HarpConstants.HARP_GIVEN_NAME;
import static mat.shared.HarpConstants.HARP_MIDDLE_NAME;
import static mat.shared.HarpConstants.HARP_PRIMARY_EMAIL_ID;

public class HarpServiceImpl extends SpringRemoteServiceServlet implements HarpService {
    private static final Log logger = LogFactory.getLog(HarpServiceImpl.class);

    private RestTemplate restTemplate;


    @Override
    public boolean revoke(String accessToken) {
        logger.debug("Revoking Token::" + accessToken);

        HttpHeaders headers = new HttpHeaders();
        List<MediaType> acceptList = new ArrayList<>();
        acceptList.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptList);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("clientId", getHarpClientId());
        uriVariables.put("hint", "access_token");
        uriVariables.put("token", accessToken);
        ResponseEntity<Void> response = null;
        try {
            response = getRestTemplate().exchange(getHarpUrl() + "/revoke?client_id={clientId}&token_type_hint={hint}&token={token}",
                    HttpMethod.POST,
                    request,
                    Void.class,
                    uriVariables);
        } catch (RestClientResponseException e) {
            logger.error("Error in revoke:" + e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            logger.error("Error in revoke:" + e.getMessage(), e);
        }
        return response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public boolean validateToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> acceptList = new ArrayList<>();
        acceptList.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptList);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<TokenIntrospect> request = new HttpEntity<>(headers);
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("clientId", getHarpClientId());
        uriVariables.put("hint", "access_token");
        uriVariables.put("token", token);
        ResponseEntity<TokenIntrospect> response = null;
        try {
            response = getRestTemplate().exchange(getHarpUrl() + "/introspect?client_id={clientId}&token_type_hint={hint}&token={token}",
                    HttpMethod.POST,
                    request,
                    TokenIntrospect.class,
                    uriVariables);
        } catch (RestClientResponseException e) {
            logger.error("Error in validateToken:" + e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            logger.error("Error in validateToken:" + e.getMessage(), e);
        }
        return response.getBody().isActive();
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

    @Override
    public Map<String, String> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> acceptList = new ArrayList<>();
        acceptList.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptList);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<TokenIntrospect> request = new HttpEntity<>(headers);
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("clientId", getHarpClientId());
        ResponseEntity<UserInfo> response = null;
        try {
            response = getRestTemplate().exchange(getHarpUrl() + "/userinfo?client_id={clientId}",
                    HttpMethod.POST,
                    request,
                    UserInfo.class,
                    uriVariables);
        } catch (RestClientResponseException e) {
            logger.error("Error in validateToken:" + e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            logger.error("Error in validateToken:" + e.getMessage(), e);
        }
        UserInfo userinfo = response.getBody();
        Map<String, String> harpUserInfo = new HashMap<>();
        harpUserInfo.put(HarpConstants.HARP_GIVEN_NAME, userinfo.getGivenName());
        harpUserInfo.put(HarpConstants.HARP_MIDDLE_NAME, userinfo.getMiddleName());
        harpUserInfo.put(HarpConstants.HARP_FAMILY_NAME, userinfo.getFamilyName());
        harpUserInfo.put(HarpConstants.HARP_FULLNAME, userinfo.getName());
        harpUserInfo.put(HarpConstants.HARP_PRIMARY_EMAIL_ID, userinfo.getEmail());
        harpUserInfo.values().forEach(logger::debug);
        return harpUserInfo;
    }

    public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setSSLSocketFactory(csf)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);
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
