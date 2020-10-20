package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.dto.MeasureTypeDTO;
import mat.model.MeasureType;
import mat.model.clause.MeasureTypeAssociation;
import mat.server.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


@Repository("measureTypeDAO")
public class MeasureTypeDAOImpl extends GenericDAO<MeasureType, String> implements mat.dao.MeasureTypeDAO {
	
	private static final Log logger = LogFactory.getLog(MeasureTypeDAOImpl.class);
	
	public MeasureTypeDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<MeasureTypeDTO> getAllMeasureTypes(){
		logger.debug("Getting all the rows from the MEASURE_TYPES table");
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<MeasureTypeDTO> query = cb.createQuery(MeasureTypeDTO.class);
		final Root<MeasureType> root = query.from(MeasureType.class);

		query.select(cb.construct(
				MeasureTypeDTO.class, 
						root.get("id"),
						root.get("description"),
						root.get("abbrName")));
		
		return session.createQuery(query).getResultList();
	}
	
	@Override
	public MeasureType getMeasureTypeByName(String name) {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<MeasureType> query = cb.createQuery(MeasureType.class);
        final Root<MeasureType> root = query.from(MeasureType.class);

        query.select(root).where(cb.equal(root.get("description"), name));
        
        return session.createQuery(query).getResultList().get(0);
 }

	@Override
	public void deleteAllMeasureTypeAssociationsByMeasureId(String measureId) {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaDelete<MeasureTypeAssociation> deleteQuery = cb.createCriteriaDelete(MeasureTypeAssociation.class);
		final Root<MeasureTypeAssociation> root = deleteQuery.from(MeasureTypeAssociation.class);
		
		
		deleteQuery.where(cb.equal(root.get("measure").get("id"), measureId));
		
		session.createQuery(deleteQuery).executeUpdate();
	}
}
