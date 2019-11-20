package mat.client.featureFlag.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import mat.client.login.service.AsynchronousService;
import mat.model.FeatureFlag;

public interface FeatureFlagServiceAsync extends AsynchronousService{

	
	void findFeatureFlag(String flagName, AsyncCallback<FeatureFlag> callback);
}
