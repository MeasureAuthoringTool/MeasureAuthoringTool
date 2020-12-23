package mat.client.login.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Map;

public interface HarpServiceAsync extends AsynchronousService {

    void revoke(String accessToken, AsyncCallback<Boolean> async);

    void getHarpUrl(AsyncCallback<String> async);

    void getHarpBaseUrl(AsyncCallback<String> async);

    void getHarpClientId(AsyncCallback<String> async);

    void validateToken(String token, AsyncCallback<Boolean> async);

    void validateUserAndInitSession(String accessToken, AsyncCallback<Void> async);

    void generateUserInfoFromAccessToken(String accessToken, AsyncCallback<Map<String, String>> async);
}
