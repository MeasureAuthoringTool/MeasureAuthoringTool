package mat.dao;

import mat.model.FeatureFlag;

public interface FeatureFlagDAO extends IDAO<FeatureFlag, String>  {
    public FeatureFlag findFlagByName(String flagName);
}
