package mat.client.login.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HarpServiceAsync extends AsynchronousService {
    void logout(String idToken, AsyncCallback<Boolean> async);

    void revoke(String accessToken, AsyncCallback<Boolean> async);
}
