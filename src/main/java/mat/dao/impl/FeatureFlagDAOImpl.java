package mat.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import mat.dao.FeatureFlagDAO;
import mat.dao.search.GenericDAO;
import mat.model.FeatureFlag;

@Repository("FeatureFlagDAO")
public class FeatureFlagDAOImpl extends GenericDAO<FeatureFlag, String> implements FeatureFlagDAO {

    public FeatureFlagDAOImpl(@Autowired SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    @Override
    public List<FeatureFlag> findAllFeatureFlags() {
        final List<FeatureFlag> dataTypeList = find();
        return CollectionUtils.isNotEmpty(dataTypeList) ? dataTypeList : Collections.emptyList();
    }

    @Override
    @Cacheable("featureFlags")
    public Map<String, Boolean> findFeatureFlags() {
        final List<FeatureFlag> dataTypeList = findAllFeatureFlags();
        return dataTypeList.stream().collect(Collectors.toMap(FeatureFlag::getFlagName, FeatureFlag::isFlagOn));
    }

}
