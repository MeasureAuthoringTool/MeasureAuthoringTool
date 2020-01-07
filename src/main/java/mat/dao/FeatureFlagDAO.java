package mat.dao;

import mat.model.FeatureFlag;

import java.util.List;

public interface FeatureFlagDAO extends IDAO<FeatureFlag, String>  {
    List<FeatureFlag> findAllFeatureFlags();
    FeatureFlag findFeatureFlagByName(String flagName);
}
