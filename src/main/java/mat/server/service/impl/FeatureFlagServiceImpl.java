package mat.server.service.impl;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import mat.client.util.FeatureFlagConstant;
import mat.dao.FeatureFlagDAO;
import mat.server.service.FeatureFlagService;

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
