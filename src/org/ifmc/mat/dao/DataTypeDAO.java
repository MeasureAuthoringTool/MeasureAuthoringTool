package org.ifmc.mat.dao;

import org.ifmc.mat.model.DataType;

public interface DataTypeDAO extends IDAO<DataType, String> {
	public DataType findByDataTypeName(String dataTypeName);
	public DataType findDataTypeForSupplimentalCodeList(String dataTypeName,String categoryId);
}
