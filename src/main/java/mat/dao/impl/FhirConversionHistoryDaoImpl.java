package mat.dao.impl;

import mat.dao.FhirConversionHistoryDao;
import mat.dao.search.GenericDAO;
import mat.model.MeasureType;
import mat.model.clause.FhirConversionHistory;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class FhirConversionHistoryDaoImpl extends GenericDAO<FhirConversionHistory, String> implements FhirConversionHistoryDao {
    @Override
    public FhirConversionHistory lookupByQdmSetId(String qdmSetId) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<FhirConversionHistory> query = cb.createQuery(FhirConversionHistory.class);
        Root<FhirConversionHistory> root = query.from(FhirConversionHistory.class);

        query.select(root).where(cb.equal(root.get("qdmSetId"), qdmSetId));

        return session.createQuery(query).uniqueResult();
    }
}
