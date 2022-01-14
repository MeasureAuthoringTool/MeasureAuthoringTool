package mat.dao.clause.impl;

import mat.dao.clause.OperatorDAO;
import mat.dao.search.GenericDAO;
import mat.dto.OperatorDTO;
import mat.model.Operator;
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

@Repository("operatorDAO")
public class OperatorDAOImpl extends GenericDAO<Operator, String> implements OperatorDAO{

	private static final String LOGICAL_OPERATORS = "1";

	private static final String RELATIVE_TIMINGS_OPERATORS = "2";
	
	private static final String RELATIVE_ASSOCIATIONS_OPERATORS = "3";
	
	private static final Logger logger = LoggerFactory.getLogger(OperatorDAOImpl.class);
	
	public OperatorDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public List<OperatorDTO> getLogicalOperators(){
		logger.debug("Getting all the Logical Operators from the Operator table");
		return getOperatorsListBasedOnOperatorType(LOGICAL_OPERATORS);
	}

	@Override
	public List<OperatorDTO> getRelTimingperators(){
		logger.debug("Getting all Relative Timing Operators from the Operator table");
		return getOperatorsListBasedOnOperatorType(RELATIVE_TIMINGS_OPERATORS);
	}

	@Override
	public List<OperatorDTO> getRelAssociationsOperators(){
		logger.debug("Getting all Relative Associations Operators from the Operator table");
		return getOperatorsListBasedOnOperatorType(RELATIVE_ASSOCIATIONS_OPERATORS);
	}

	@Override
	public List<OperatorDTO> getAllOperators() {
		logger.debug("Getting all  Operators from the Operator table");
		return getOperatorsListBasedOnOperatorType(null);
	}

	private List<OperatorDTO> getOperatorsListBasedOnOperatorType(String operatorTypeId) {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<OperatorDTO> query = cb.createQuery(OperatorDTO.class);
		final Root<Operator> root = query.from(Operator.class);

		query.select(cb.construct(
				OperatorDTO.class, 
				root.get("shortName"),
				root.get("longName"),
				root.get("operatorType").get("id")));

		if(operatorTypeId != null) {
			query.where(cb.equal(root.get("operatorType").get("id"), operatorTypeId));	
		}

		query.orderBy(cb.asc(root.get("longName")));

		return session.createQuery(query).getResultList();
	}

}
