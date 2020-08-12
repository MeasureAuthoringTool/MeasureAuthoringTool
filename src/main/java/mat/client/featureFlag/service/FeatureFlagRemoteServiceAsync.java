package mat.client.featureFlag.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.client.login.service.AsynchronousService;

import java.util.Map;

public interface FeatureFlagRemoteServiceAsync extends AsynchronousService {

    void findFeatureFlags(AsyncCallback<Map<String, Boolean>> callback);
}
