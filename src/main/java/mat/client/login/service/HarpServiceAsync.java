package mat.client.login.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HarpServiceAsync extends AsynchronousService {

    void revoke(String accessToken, AsyncCallback<Boolean> async);

    void getHarpUrl(AsyncCallback<String> async);

    void getHarpBaseUrl(AsyncCallback<String> async);

    void getHarpClientId(AsyncCallback<String> async);
}
