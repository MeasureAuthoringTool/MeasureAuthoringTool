package mat.dao;

import java.util.Map;

import mat.model.FeatureFlag;

public interface FeatureFlagDAO extends IDAO<FeatureFlag, String>  {
    Map<String, Boolean> findFeatureFlags();
}
