package mat.dao;

import mat.model.ListObject;

import java.util.List;

/**
 * The Interface ListObjectDAO.
 */
public interface ListObjectDAO extends IDAO<ListObject, String> {

	List<ListObject> getSupplimentalCodeList();

	List<ListObject> getElementCodeListByOID(List<String> elementOIDList);
}
