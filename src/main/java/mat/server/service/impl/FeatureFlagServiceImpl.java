package mat.server.service.impl;

import mat.dao.FeatureFlagDAO;
import mat.server.logging.LogFactory;
import mat.server.service.FeatureFlagService;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FeatureFlagServiceImpl implements FeatureFlagService {

    private static final Log logger = LogFactory.getLog(FeatureFlagServiceImpl.class);

    @Autowired
    private FeatureFlagDAO featureFlagDAO;

    @Override
    public Map<String, Boolean> findFeatureFlags() {
        return featureFlagDAO.findFeatureFlags();
    }
}
