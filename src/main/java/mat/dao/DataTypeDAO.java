package mat.dao;

import java.util.List;

import mat.dto.DataTypeDTO;
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
	DataType findByDataTypeName(String dataTypeName);
	
	/**
	 * Find data type for supplemental code list.
	 * 
	 * @param dataTypeName
	 *            the data type name
	 * @param categoryId
	 *            the category id
	 * @return the data type
	 */
	DataType findDataTypeForSupplimentalCodeList(String dataTypeName,String categoryId);
	
	/**
	 * Find all data type.
	 * 
	 * @return the list
	 */
	List<DataTypeDTO> findAllDataType();
}
