package mat.server.service.impl;

import mat.dao.FeatureFlagDAO;
import org.slf4j.LoggerFactory;
import mat.server.service.FeatureFlagService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FeatureFlagServiceImpl implements FeatureFlagService {

    private static final Logger logger = LoggerFactory.getLogger(FeatureFlagServiceImpl.class);

    @Autowired
    private FeatureFlagDAO featureFlagDAO;

    @Override
    public Map<String, Boolean> findFeatureFlags() {
        return featureFlagDAO.findFeatureFlags();
    }
}
