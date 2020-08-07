package mat.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import mat.dao.FeatureFlagDAO;
import mat.dao.search.GenericDAO;
import mat.model.FeatureFlag;

@Repository("FeatureFlagDAO")
public class FeatureFlagDAOImpl extends GenericDAO<FeatureFlag, String> implements FeatureFlagDAO {

    private static final Log logger = LogFactory.getLog(FeatureFlagDAOImpl.class);

    public FeatureFlagDAOImpl(@Autowired SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    @Override
    @Cacheable("featureFlags")
    public Map<String, Boolean> findFeatureFlags() {
        final List<FeatureFlag> dataTypeList = find();
        List<FeatureFlag> featureFlags = CollectionUtils.isNotEmpty(dataTypeList) ? dataTypeList : Collections.emptyList();
        logger.info("FeatureFlagServiceImpl::findFeatureFlags Successfully retrieved feature flags");
        return featureFlags.stream().collect(Collectors.toMap(FeatureFlag::getFlagName, FeatureFlag::isFlagOn));
    }

}
