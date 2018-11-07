package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dao.DataTypeDAO;
import mat.dao.search.GenericDAO;
import mat.model.DataType;

@Repository("dataTypeDAO")
public class DataTypeDAOImpl extends GenericDAO<DataType, String> implements DataTypeDAO {
	
	public DataTypeDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
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
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DataType> findAllDataType(){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(DataType.class);
		if(criteria.list().isEmpty()) return null;
		return (ArrayList<DataType>)criteria.list();
	}
	
}
