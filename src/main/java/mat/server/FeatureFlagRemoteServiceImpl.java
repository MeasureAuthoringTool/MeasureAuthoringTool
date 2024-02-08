package mat.server;


import mat.client.featureFlag.service.FeatureFlagRemoteService;
import mat.server.service.FeatureFlagService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class FeatureFlagRemoteServiceImpl extends SpringRemoteServiceServlet implements FeatureFlagRemoteService {

    private static final long serialVersionUID = 1L;

    @Autowired
    private FeatureFlagService flagService;

    @Override
    public Map<String, Boolean> findFeatureFlags() {
        return flagService.findFeatureFlags();
    }
}
