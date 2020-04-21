package mat.client.login.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.client.shared.MatException;

public interface HarpServiceAsync extends AsynchronousService {

    void revoke(String accessToken, AsyncCallback<Boolean> async);

    void getHarpUrl(AsyncCallback<String> async);

    void getHarpBaseUrl(AsyncCallback<String> async);

    void getHarpClientId(AsyncCallback<String> async);
}
