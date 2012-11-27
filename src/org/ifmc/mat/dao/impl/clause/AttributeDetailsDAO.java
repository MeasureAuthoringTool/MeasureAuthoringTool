package org.ifmc.mat.dao.impl.clause;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.clause.AttributeDetails;

public class AttributeDetailsDAO extends GenericDAO<AttributeDetails, String> implements org.ifmc.mat.dao.clause.AttributeDetailsDAO {


	public AttributeDetails findByName(String attrName) {
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(AttributeDetails.class);
		criteria.add(Restrictions.eq("attrName", attrName));
		List<AttributeDetails> attributeDetailsList = criteria.list(); 
		if(attributeDetailsList.isEmpty())
			return null;
		return attributeDetailsList.get(0);
	}
	
}
