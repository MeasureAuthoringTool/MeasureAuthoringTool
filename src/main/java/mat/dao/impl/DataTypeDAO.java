package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mat.dao.search.GenericDAO;
import mat.model.DataType;

/**
 * The Class DataTypeDAO.
 */
public class DataTypeDAO extends GenericDAO<DataType, String> implements mat.dao.DataTypeDAO {
	
	/* (non-Javadoc)
	 * @see mat.dao.DataTypeDAO#findByDataTypeName(java.lang.String)
	 */
	@Override
	public DataType findByDataTypeName(String dataTypeName) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(DataType.class);
		criteria.add(Restrictions.eq("description", dataTypeName));
		if(criteria.list().isEmpty()) return null;
		return (DataType)criteria.list().get(0);
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.DataTypeDAO#findDataTypeForSupplimentalCodeList(java.lang.String, java.lang.String)
	 */
	@Override
	public DataType findDataTypeForSupplimentalCodeList(String dataTypeName,String categoryId){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(DataType.class);
		criteria.add(Restrictions.eq("description",dataTypeName));
		criteria.add(Restrictions.eq("categoryId",categoryId));
		if(criteria.list().isEmpty()) return null;
		return (DataType)criteria.list().get(0);
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.DataTypeDAO#findAllDataType()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DataType> findAllDataType(){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(DataType.class);
		if(criteria.list().isEmpty()) return null;
		return (ArrayList<DataType>)criteria.list();
	}
	
}
