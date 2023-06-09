package mat.dao.impl;

import mat.dao.DataTypeDAO;
import mat.dao.search.GenericDAO;
import mat.dto.DataTypeDTO;
import mat.model.DataType;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("dataTypeDAO")
public class DataTypeDAOImpl extends GenericDAO<DataType, String> implements DataTypeDAO {
	
	private static final String DESCRIPTION = "description";
	
	public DataTypeDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public DataType findByDataTypeName(String dataTypeName) {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<DataType> query = cb.createQuery(DataType.class);
		final Root<DataType> root = query.from(DataType.class);

		query.select(root).where(cb.equal(root.get(DESCRIPTION), dataTypeName));
		
		final List<DataType> dataTypeList = session.createQuery(query).getResultList();
		
		return CollectionUtils.isNotEmpty(dataTypeList) ? dataTypeList.get(0) : null;
	}
	
	@Override
	public DataType findDataTypeForSupplimentalCodeList(String dataTypeName, String categoryId){
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<DataType> query = cb.createQuery(DataType.class);
		final Root<DataType> root = query.from(DataType.class);

		query.select(root).where(cb.and(cb.equal(root.get(DESCRIPTION), dataTypeName), cb.equal(root.get("category").get("id"), categoryId)));
		
		final List<DataType> dataTypeList = session.createQuery(query).getResultList();
		
		return CollectionUtils.isNotEmpty(dataTypeList) ? dataTypeList.get(0) : null;
	}
	
	@Override
	public List<DataTypeDTO> findAllDataType(){
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<DataTypeDTO> query = cb.createQuery(DataTypeDTO.class);
		final Root<DataType> root = query.from(DataType.class);
		// Filter Timing Element Category as this will be added by default to measure at create time.
		query.select(cb.construct(
						DataTypeDTO.class, 
						root.get("id"),
						root.get(DESCRIPTION)));
		
		query.where(cb.notEqual(root.get("category").get("id"), "22"));

		return session.createQuery(query).getResultList();
	}
	
}
