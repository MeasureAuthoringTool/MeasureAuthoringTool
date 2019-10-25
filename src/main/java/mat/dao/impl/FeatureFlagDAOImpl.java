package mat.dao.impl;

import mat.dao.FeatureFlagDAO;
import mat.dao.search.GenericDAO;
import mat.model.FeatureFlag;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("FeatureFlagDAO")
public class FeatureFlagDAOImpl extends GenericDAO<FeatureFlag, String> implements FeatureFlagDAO {

    private static final String FLAG_NAME = "flagName";

    public FeatureFlagDAOImpl(@Autowired SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    @Override
    public FeatureFlag findFlagByName(String flagName) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<FeatureFlag> query = cb.createQuery(FeatureFlag.class);
        final Root<FeatureFlag> root = query.from(FeatureFlag.class);

        query.select(root).where(cb.equal(root.get(FLAG_NAME), flagName));

        final List<FeatureFlag> dataTypeList = session.createQuery(query).getResultList();

        return CollectionUtils.isNotEmpty(dataTypeList) ? dataTypeList.get(0) : null;
    }
}
