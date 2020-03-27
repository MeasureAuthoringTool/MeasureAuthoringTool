package mat.dao.impl;

import mat.dao.FeatureFlagDAO;
import mat.dao.search.GenericDAO;
import mat.model.FeatureFlag;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository("FeatureFlagDAO")
public class FeatureFlagDAOImpl extends GenericDAO<FeatureFlag, String> implements FeatureFlagDAO {

    public FeatureFlagDAOImpl(@Autowired SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    @Override
    @Cacheable("featureFlags")
    public List<FeatureFlag> findAllFeatureFlags() {
        final List<FeatureFlag> dataTypeList = find();
        return CollectionUtils.isNotEmpty(dataTypeList) ? dataTypeList : Collections.emptyList();
    }
}
