package mat.client.featureFlag.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.client.login.service.AsynchronousService;
import mat.model.FeatureFlag;

import java.util.List;

public interface FeatureFlagServiceAsync extends AsynchronousService{

	void findFeatureFlags(AsyncCallback<List<FeatureFlag>> callback);
}
