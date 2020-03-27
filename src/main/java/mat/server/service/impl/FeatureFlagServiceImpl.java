package mat.server.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mat.client.util.FeatureFlagConstant;
import mat.dao.FeatureFlagDAO;
import mat.model.FeatureFlag;
import mat.server.service.FeatureFlagService;

@Service
public class FeatureFlagServiceImpl implements FeatureFlagService {

    @Autowired
    private FeatureFlagDAO featureFlagDAO;

    @Override
    public Map<String, Boolean> findFeatureFlags() {
        List<FeatureFlag> featureFlagList = featureFlagDAO.findAllFeatureFlags();
        return featureFlagList.stream()
                .collect(Collectors.toMap(FeatureFlag::getFlagName, FeatureFlag::isFlagOn));
    }

    public boolean isFhirEditEnabled() {
        return findFeatureFlags().getOrDefault(FeatureFlagConstant.FHIR_EDIT, false);
    }

}
