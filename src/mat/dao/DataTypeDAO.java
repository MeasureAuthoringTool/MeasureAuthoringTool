package mat.dao;

import java.util.List;

import mat.model.DataType;

public interface DataTypeDAO extends IDAO<DataType, String> {
	public DataType findByDataTypeName(String dataTypeName);
	public DataType findDataTypeForSupplimentalCodeList(String dataTypeName,String categoryId);
	public List<DataType> findAllDataType();
}
