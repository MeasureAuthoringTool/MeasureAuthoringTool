package mat.client.featureFlag.service;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("featureFlag")
public interface FeatureFlagRemoteService extends RemoteService {

    Map<String, Boolean> findFeatureFlags();
}
