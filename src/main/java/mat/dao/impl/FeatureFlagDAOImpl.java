package mat.dao.impl;

import mat.dao.FeatureFlagDAO;
import mat.dao.search.GenericDAO;
import mat.model.FeatureFlag;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("FeatureFlagDAO")
public class FeatureFlagDAOImpl extends GenericDAO<FeatureFlag, String> implements FeatureFlagDAO {

    private static final String FLAG_NAME = "flagName";

    public FeatureFlagDAOImpl(@Autowired SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    @Override
    public List<FeatureFlag> findAllFeatureFlags() {
        final List<FeatureFlag> dataTypeList = find();
        return CollectionUtils.isNotEmpty(dataTypeList) ? dataTypeList : null;
    }
}
