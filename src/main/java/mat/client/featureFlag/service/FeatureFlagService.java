package mat.client.featureFlag.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.model.FeatureFlag;

import java.util.List;

@RemoteServiceRelativePath("featureFlag")
public interface FeatureFlagService extends RemoteService {

	List<FeatureFlag> findFeatureFlag();
}
