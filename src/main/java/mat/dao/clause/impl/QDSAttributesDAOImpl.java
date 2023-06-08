package mat.dao.clause.impl;

import mat.client.shared.MatContext;
import mat.dao.DataTypeDAO;
import mat.dao.clause.QDSAttributesDAO;
import mat.dao.search.GenericDAO;
import mat.model.DataType;
import mat.model.clause.QDSAttributes;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository("qDSAttributesDAO")
public class QDSAttributesDAOImpl extends GenericDAO<QDSAttributes, String> implements QDSAttributesDAO {

	private static final String DATA_TYPE_ID = "dataTypeId";
	
	public QDSAttributesDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<QDSAttributes> findByDataType(String qdmname, ApplicationContext context) {
		
		qdmname = MatContext.get().getTextSansOid(qdmname);
		final DataTypeDAO dataTypeDAO = context.getBean(DataTypeDAO.class);
		final DataType dataType = getDataTypeFromQDMName(qdmname, dataTypeDAO);

		return (dataType == null) ? new ArrayList<>() : getQDSAttributesListByParameter(DATA_TYPE_ID, dataType.getId());
	}
	
	@Override
	public List<QDSAttributes> findByDataTypeName(String dataTypeName, ApplicationContext context){
		final DataTypeDAO dataTypeDAO = context.getBean(DataTypeDAO.class);
		final DataType dataType = getDataTypeFromName(dataTypeName, dataTypeDAO);
		
		return (dataType == null) ? new ArrayList<>() : getQDSAttributesListByParameter(DATA_TYPE_ID, dataType.getId());
	}
	
	@Override
	public List<QDSAttributes> getAllDataFlowAttributeName() {
		return getQDSAttributesListByParameter("qDSAttributeType", "Data Flow");
	}
	
	private List<QDSAttributes> getQDSAttributesListByParameter(String name, String parameter){
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<QDSAttributes> query = cb.createQuery(QDSAttributes.class);
		final Root<QDSAttributes> root = query.from(QDSAttributes.class);
		
		query.select(root).where(cb.equal(root.get(name), parameter));
		
		return session.createQuery(query).getResultList();
	}
	
	/**
	 * attributeName alone does not uniquely identify a QDSAttributes record
	 * while attributeName and dataTypeName do there should not be a need to
	 * search by attributeName alone.
	 * 
	 * @param attributeName
	 *            the attribute name
	 * @param dataTypeName
	 *            the data type name
	 * @return the qDS attributes
	 */
	@Override
	public QDSAttributes findByNameAndDataType(String attributeName, String dataTypeName) {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<QDSAttributes> query = cb.createQuery(QDSAttributes.class);
		final Root<QDSAttributes> root = query.from(QDSAttributes.class);
		
		final Predicate pred = getPredicateByNameAndDataType(attributeName, dataTypeName, cb, root);
		
		query.select(root).where(pred);		
		query.orderBy(cb.asc(root.get("name")));
		
		final List<QDSAttributes> qdsAttributesList = session.createQuery(query).getResultList();
		
		return CollectionUtils.isNotEmpty(qdsAttributesList) ? qdsAttributesList.get(0) : null;
	}
	
	private Predicate getPredicateByNameAndDataType(String attributeName, String dataTypeName, CriteriaBuilder cb, Root<QDSAttributes> root) {
		final Predicate p1 = cb.equal(root.get("name"), attributeName);
		Predicate p2 = null;
		if(addDataTypeRestriction(attributeName, dataTypeName)) {
			p2 = cb.equal(root.get(DATA_TYPE_ID), dataTypeName);
		}
		return (p2 != null) ? cb.and(p1, p2) : p1 ;
	}
	
	private boolean addDataTypeRestriction(String attributeName, String dataTypeName) {
		return dataTypeName == null ? false :
			
			attributeName.equalsIgnoreCase("Recorder") ? false :
				attributeName.equalsIgnoreCase("Source") ? false :
					
					//retaining the next four lines to handle existing data
					attributeName.equalsIgnoreCase("Source - Device") ? false :
						attributeName.equalsIgnoreCase("Source - Informant") ? false :
							attributeName.equalsIgnoreCase("Recorder - Informant") ? false :
								attributeName.equalsIgnoreCase("Recorder - Device") ? false :
							
									attributeName.equalsIgnoreCase("Setting") ? false :
										attributeName.equalsIgnoreCase("Health Record Field") ? false :
											true;
	}
	
	/**
	 * Gets the data type from qdm name.
	 * 
	 * @param qdmname
	 *            could be of the form: <<value set name>>:<<data type>> where
	 *            <<value set name>> and <<data type>> could contain one or more
	 *            ':' so... s(1):...:s(n-2):s(n-1):s(n):
	 * 
	 *            begin with s(n)
	 * @param dataTypeDAO
	 *            the data type dao
	 * @return the data type from qdm name
	 */
	@Override
	public DataType getDataTypeFromQDMName(String qdmname, DataTypeDAO dataTypeDAO){
		final String[] namePieces = qdmname.split(":");
		final int len = namePieces.length;
		int idx = len - 1;
		
		DataType dataType = null;
		if(idx >=0){
			String dataTypeName = namePieces[idx].trim();
			//get datatype from table
			dataType = dataTypeDAO.findByDataTypeName(dataTypeName);
			idx--;
			while((idx >= 0) && (dataType == null)){
				dataTypeName = namePieces[idx].trim() + ": " + dataTypeName;
				dataType = dataTypeDAO.findByDataTypeName(dataTypeName);
				idx --;	
			}
		}
		return dataType;
	}
	
	/**
	 * Gets the data type from name.
	 * 
	 * @param dataTypeName
	 *            the data type name
	 * @param dataTypeDAO
	 *            the data type dao
	 * @return the data type from name
	 */
	private DataType getDataTypeFromName(String dataTypeName,DataTypeDAO dataTypeDAO){
		return dataTypeDAO.findByDataTypeName(dataTypeName);
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.clause.QDSAttributesDAO#getAllAttributes()
	 */
	@Override
	public List<String> getAllAttributes() {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<String> query = cb.createQuery(String.class);
		final Root<QDSAttributes> root = query.from(QDSAttributes.class);
		
		query.select(root.get("name")).distinct(true);
		query.orderBy(cb.asc(root.get("name")));
		
		return session.createQuery(query).getResultList();
	}
}
