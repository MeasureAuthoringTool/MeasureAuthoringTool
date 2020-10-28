package mat.client.featureFlag.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Map;

@RemoteServiceRelativePath("featureFlag")
public interface FeatureFlagRemoteService extends RemoteService {

    Map<String, Boolean> findFeatureFlags();
}
