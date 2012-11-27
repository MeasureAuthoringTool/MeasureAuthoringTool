package org.ifmc.mat.dao.impl.clause;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ifmc.mat.DTO.OperatorDTO;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.Operator;

public class OperatorDAO extends GenericDAO<Operator, String> implements org.ifmc.mat.dao.clause.OperatorDAO{
private static final Log logger = LogFactory.getLog(OperatorDAO.class);
	
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
}
