package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.model.DataType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class DataTypeDAO extends GenericDAO<DataType, String> implements mat.dao.DataTypeDAO {
	
	@Override
	public DataType findByDataTypeName(String dataTypeName) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(DataType.class);
		criteria.add(Restrictions.eq("description", dataTypeName));
		if(criteria.list().isEmpty()) return null;
		return (DataType)criteria.list().get(0);
	}
	
	@Override
	public DataType findDataTypeForSupplimentalCodeList(String dataTypeName,String categoryId){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(DataType.class);
		criteria.add(Restrictions.eq("description",dataTypeName));
		criteria.add(Restrictions.eq("categoryId",categoryId));
		if(criteria.list().isEmpty()) return null;
		return (DataType)criteria.list().get(0);
	}
}
