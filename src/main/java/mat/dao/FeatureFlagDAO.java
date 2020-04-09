package mat.dao;

import java.util.List;
import java.util.Map;

import mat.model.FeatureFlag;

public interface FeatureFlagDAO extends IDAO<FeatureFlag, String>  {
    List<FeatureFlag> findAllFeatureFlags();
    Map<String, Boolean> findFeatureFlags();
}
