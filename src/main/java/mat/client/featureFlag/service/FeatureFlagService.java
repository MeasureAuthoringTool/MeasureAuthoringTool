package mat.client.featureFlag.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.model.FeatureFlag;

@RemoteServiceRelativePath("featureFlag")
public interface FeatureFlagService extends RemoteService {

	
	FeatureFlag findFeatureFlag(String flagName);
}
