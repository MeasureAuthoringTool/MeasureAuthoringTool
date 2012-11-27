package org.ifmc.mat.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.DataType;

public class DataTypeDAO extends GenericDAO<DataType, String> implements org.ifmc.mat.dao.DataTypeDAO {
	
	public DataType findByDataTypeName(String dataTypeName) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(DataType.class);
		criteria.add(Restrictions.eq("description", dataTypeName));
		if(criteria.list().isEmpty()) return null;
		return (DataType)criteria.list().get(0);
	}
	
	public DataType findDataTypeForSupplimentalCodeList(String dataTypeName,String categoryId){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(DataType.class);
		criteria.add(Restrictions.eq("description",dataTypeName));
		criteria.add(Restrictions.eq("categoryId",categoryId));
		if(criteria.list().isEmpty()) return null;
		return (DataType)criteria.list().get(0);
	}
}
