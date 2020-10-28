package mat.dao;

import mat.model.FeatureFlag;

import java.util.Map;

public interface FeatureFlagDAO extends IDAO<FeatureFlag, String>  {
    Map<String, Boolean> findFeatureFlags();
}
