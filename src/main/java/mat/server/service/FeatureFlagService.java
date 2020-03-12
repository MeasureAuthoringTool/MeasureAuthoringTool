package mat.server.service;

import java.util.Map;

public interface FeatureFlagService {
    Map<String, Boolean> findFeatureFlags();
    boolean isFhirEditEnabled();
}
