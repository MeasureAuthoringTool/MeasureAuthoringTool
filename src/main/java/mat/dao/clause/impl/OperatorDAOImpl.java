package mat.dao.clause.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.DTO.OperatorDTO;
import mat.dao.clause.OperatorDAO;
import mat.dao.search.GenericDAO;
import mat.model.Operator;

@Repository
public class OperatorDAOImpl extends GenericDAO<Operator, String> implements OperatorDAO{

	private static final Log logger = LogFactory.getLog(OperatorDAOImpl.class);
	
	public OperatorDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	public List<OperatorDTO> getLogicalOperators(){
		List<OperatorDTO> OperatorDTOList = new ArrayList<OperatorDTO>();
		logger.info("Getting all the Logical Operators from the Operator table");
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Operator.class);
		criteria.add(Restrictions.eq("operatorType.id", "1"));
		criteria.addOrder(Order.asc("longName"));
		@SuppressWarnings("unchecked")
		List<Operator> OperatorList = criteria.list();
		
		for(Operator Operator: OperatorList){
			OperatorDTO OperatorDTO =  new OperatorDTO();
			OperatorDTO.setId(Operator.getShortName());//Long Name goes as List Box Text and Short Name goes as value.
			OperatorDTO.setOperator(Operator.getLongName());
			OperatorDTO.setOperatorType(Operator.getOperatorType().getId());
			OperatorDTOList.add(OperatorDTO);
		}
		return OperatorDTOList;
	}
	
	public List<OperatorDTO> getRelTimingperators(){
		List<OperatorDTO> OperatorDTOList = new ArrayList<OperatorDTO>();
		logger.info("Getting all Relative Timing Operators from the Operator table");
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Operator.class);
		criteria.add(Restrictions.eq("operatorType.id", "2"));
		criteria.addOrder(Order.asc("longName"));
		@SuppressWarnings("unchecked")
		List<Operator> OperatorList = criteria.list();
		
		for(Operator Operator: OperatorList){
			OperatorDTO OperatorDTO =  new OperatorDTO();
			OperatorDTO.setId(Operator.getShortName());//Long Name goes as List Box Text and Short Name goes as value.
			OperatorDTO.setOperator(Operator.getLongName());
			OperatorDTO.setOperatorType(Operator.getOperatorType().getId());
			OperatorDTOList.add(OperatorDTO);
		}
		return OperatorDTOList;
	}
	
	public List<OperatorDTO> getRelAssociationsOperators(){
		List<OperatorDTO> OperatorDTOList = new ArrayList<OperatorDTO>();
		logger.info("Getting all Relative Associations Operators from the Operator table");
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Operator.class);
		criteria.add(Restrictions.eq("operatorType.id", "3"));
		criteria.addOrder(Order.asc("longName"));
		@SuppressWarnings("unchecked")
		List<Operator> OperatorList = criteria.list();
		
		for(Operator Operator: OperatorList){
			OperatorDTO OperatorDTO =  new OperatorDTO();
			OperatorDTO.setId(Operator.getShortName());//Long Name goes as List Box Text and Short Name goes as value.
			OperatorDTO.setOperator(Operator.getLongName());
			OperatorDTO.setOperatorType(Operator.getOperatorType().getId());
			OperatorDTOList.add(OperatorDTO);
		}
		return OperatorDTOList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OperatorDTO> getAllOperators() {
		List<OperatorDTO> OperatorDTOList = new ArrayList<OperatorDTO>();
		logger.info("Getting all  Operators from the Operator table");
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Operator.class);	
		criteria.addOrder(Order.asc("longName"));
		List<Operator> OperatorList = criteria.list();
		for (Operator operator : OperatorList) {
			OperatorDTO OperatorDTO =  new OperatorDTO();
			OperatorDTO.setId(operator.getShortName());//Long Name goes as List Box Text and Short Name goes as value.
			OperatorDTO.setOperator(operator.getLongName());
			OperatorDTO.setOperatorType(operator.getOperatorType().getId());
			OperatorDTOList.add(OperatorDTO);
		}
		return OperatorDTOList;
	}
}
