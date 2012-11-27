package mat.dao;

import mat.model.DataType;

public interface DataTypeDAO extends IDAO<DataType, String> {
	public DataType findByDataTypeName(String dataTypeName);
	public DataType findDataTypeForSupplimentalCodeList(String dataTypeName,String categoryId);
}
