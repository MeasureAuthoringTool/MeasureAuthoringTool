package mat.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.DTO.MeasureTypeDTO;
import mat.dao.search.GenericDAO;
import mat.model.MeasureType;


@Repository("measureTypeDAO")
public class MeasureTypeDAOImpl extends GenericDAO<MeasureType, String> implements mat.dao.MeasureTypeDAO {
	
	private static final Log logger = LogFactory.getLog(MeasureTypeDAOImpl.class);
	
	public MeasureTypeDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<MeasureTypeDTO> getAllMeasureTypes(){
		logger.info("Getting all the rows from the MEASURE_TYPES table");
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
}
