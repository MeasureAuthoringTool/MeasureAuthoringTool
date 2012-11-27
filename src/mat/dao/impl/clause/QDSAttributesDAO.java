package mat.dao.impl.clause;

import java.util.ArrayList;
import java.util.List;

import mat.client.shared.MatContext;
import mat.dao.DataTypeDAO;
import mat.dao.search.GenericDAO;
import mat.model.DataType;
import mat.model.clause.QDSAttributes;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;

public class QDSAttributesDAO extends GenericDAO<QDSAttributes, String> implements mat.dao.clause.QDSAttributesDAO {


	public List<QDSAttributes> findByDataType(String qdmname, ApplicationContext context) {
		
		qdmname = MatContext.get().getTextSansOid(qdmname);
		DataTypeDAO dataTypeDAO = (DataTypeDAO)context.getBean("dataTypeDAO");
		DataType dataType = getDataTypeFromQDMName(qdmname, dataTypeDAO);
		
		if(dataType == null){
			return new ArrayList<QDSAttributes>();
		}
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(QDSAttributes.class);
		criteria.add(Restrictions.eq("dataTypeId", dataType.getId()));
		return criteria.list();
	}
	
	public List<QDSAttributes> getAllDataFlowAttributeName() {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(QDSAttributes.class);
		criteria.add(Restrictions.eq("qDSAttributeType", "Data Flow"));
		return criteria.list();
	}
	/**
	 * attributeName alone does not uniquely identify a QDSAttributes record
	 * while attributeName and dataTypeName do
	 * there should not be a need to search by attributeName alone 
	 * @param attributeName
	 * @param dataTypeName
	 * @return
	 */
	@Override
	public QDSAttributes findByNameAndDataType(String attributeName, String dataTypeName) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(QDSAttributes.class);
		criteria.add(Restrictions.eq("name", attributeName));
		boolean addDataTypeRestriction = dataTypeName == null ? false :
			
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
						
		if(addDataTypeRestriction)
			criteria.add(Restrictions.eq("dataTypeId", dataTypeName));
		if (criteria.list().size()>0) {
			return (QDSAttributes)criteria.list().get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @param qdmname could be of the form: 
	 * <<value set name>>:<<data type>>
	 * where <<value set name>> and <<data type>> could contain one or more ':'
	 * so...
	 * s(1):...:s(n-2):s(n-1):s(n):
	 * 
	 * begin with s(n)
	 * @return
	 */
	public DataType getDataTypeFromQDMName(String qdmname, DataTypeDAO dataTypeDAO){
		String[] namePieces = qdmname.split(":");
		int len = namePieces.length;
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
}
