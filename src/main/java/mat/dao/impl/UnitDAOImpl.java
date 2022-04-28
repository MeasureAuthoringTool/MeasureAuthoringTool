package mat.dao.impl;

import mat.dao.UnitDAO;
import mat.dao.search.GenericDAO;
import mat.dto.UnitDTO;
import mat.model.Unit;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("unitDAO")
public class UnitDAOImpl extends GenericDAO<Unit, String> implements UnitDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(UnitDAOImpl.class);
	
	public UnitDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<UnitDTO> getAllUnits(){
		logger.debug("Getting all the rows from the Unit table");
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<UnitDTO> query = cb.createQuery(UnitDTO.class);
		final Root<Unit> root = query.from(Unit.class);

		query.select(cb.construct(
						UnitDTO.class, 
						root.get("id"),
						root.get("name"),
						root.get("cqlUnit"),
						root.get("sortOrder")));
		
		query.orderBy(cb.asc(root.get("sortOrder")));
		
		return session.createQuery(query).getResultList();
	}
}
