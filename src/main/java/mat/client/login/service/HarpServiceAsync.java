package mat.client.login.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Map;

public interface HarpServiceAsync extends AsynchronousService {

    void revoke(String accessToken, AsyncCallback<Boolean> async);

    void getHarpUrl(AsyncCallback<String> async);

    void getHarpBaseUrl(AsyncCallback<String> async);

    void getHarpClientId(AsyncCallback<String> async);

    void validateToken(String token, AsyncCallback<Boolean> async);

    void getUserInfo(String accessToken, AsyncCallback<Map<String, String>> async);
}
