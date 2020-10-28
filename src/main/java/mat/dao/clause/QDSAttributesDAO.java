package mat.dao.clause;

import mat.dao.DataTypeDAO;
import mat.dao.IDAO;
import mat.model.DataType;
import mat.model.clause.QDSAttributes;
import org.springframework.context.ApplicationContext;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface QDSAttributesDAO.
 */
public interface QDSAttributesDAO extends IDAO<QDSAttributes, String> {
	
	/**
	 * Find by data type.
	 * 
	 * @param dataTypeName
	 *            the data type name
	 * @param context
	 *            the context
	 * @return the list
	 */
	public List<QDSAttributes> findByDataType(String dataTypeName, ApplicationContext context);
	
	/**
	 * Gets the all data flow attribute name.
	 * 
	 * @return the all data flow attribute name
	 */
	public List<QDSAttributes> getAllDataFlowAttributeName();
	
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
	public QDSAttributes findByNameAndDataType(String attributeName, String dataTypeName);
	
	/**
	 * Gets the data type from qdm name.
	 * 
	 * @param qdmname
	 *            the qdmname
	 * @param dataTypeDAO
	 *            the data type dao
	 * @return the data type from qdm name
	 */
	public DataType getDataTypeFromQDMName(String qdmname, DataTypeDAO dataTypeDAO);
	
	/**
	 * Find by data type name.
	 * 
	 * @param dataTypeName
	 *            the data type name
	 * @param context
	 *            the context
	 * @return the list
	 */
	public List<QDSAttributes> findByDataTypeName(String dataTypeName, ApplicationContext context);

	/**
	 * Gets the all attributes.
	 *
	 * @return the all attributes
	 */
	public List<String> getAllAttributes();
}
