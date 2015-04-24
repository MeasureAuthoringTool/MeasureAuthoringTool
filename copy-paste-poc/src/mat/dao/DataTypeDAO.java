package mat.dao;

import java.util.List;

import mat.model.DataType;

/**
 * The Interface DataTypeDAO.
 */
public interface DataTypeDAO extends IDAO<DataType, String> {
	
	/**
	 * Find by data type name.
	 * 
	 * @param dataTypeName
	 *            the data type name
	 * @return the data type
	 */
	public DataType findByDataTypeName(String dataTypeName);
	
	/**
	 * Find data type for supplimental code list.
	 * 
	 * @param dataTypeName
	 *            the data type name
	 * @param categoryId
	 *            the category id
	 * @return the data type
	 */
	public DataType findDataTypeForSupplimentalCodeList(String dataTypeName,String categoryId);
	
	/**
	 * Find all data type.
	 * 
	 * @return the list
	 */
	public List<DataType> findAllDataType();
}
