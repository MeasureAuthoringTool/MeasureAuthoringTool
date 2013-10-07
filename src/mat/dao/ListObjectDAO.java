package mat.dao;

import java.sql.Timestamp;
import java.util.List;

import mat.client.codelist.ManageCodeListDetailModel;
import mat.model.CodeListSearchDTO;
import mat.model.ListObject;
import mat.model.User;
import mat.model.clause.Measure;

public interface ListObjectDAO extends IDAO<ListObject, String> {
	public int countSearchResultsByUser(String searchText, String userId,
			boolean defaultCodeList);

	public List<CodeListSearchDTO> searchByUser(String searchText,
			String userId, int startIndex, int pageSize, String sortColumn,
			boolean isAsc, boolean defaultCodeList);

	public int countSearchResultsByMeasure(String searchText, Measure measure,
			boolean defaultCodeList);

	public List<CodeListSearchDTO> searchByMeasureOwner(String searchText,
			String ownerId, int startIndex, int pageSize, String sortColumn,
			boolean isAsc, boolean defaultCodeList);

	// US 413. Added parameter for Steward Other
	public ListObject getListObject(ManageCodeListDetailModel currentDetails,
			String userid);

	public ListObject getListObject(String name, String steward,
			String categoryCd, String userid);

	public List<ListObject> loadAllCodeListPerUser(String user);

	public ListObject findMostRecentValueSet(ListObject loFamily,
			Timestamp vsPackageDate);

	public List<ListObject> getListObject(String Oid);

	public String generateUniqueOid(User user);

	/**
	 * 
	 * @param oid
	 * @param id
	 * @return count(*) from list_object l where l.oid = oid and not l.id = id
	 */
	public int countListObjectsByOidAndNotId(String oid, String id);

	public List<ListObject> getListObjectsByMeasure(Measure measure);

	public List<ListObject> getListObjectsToDraft();

	public void updateFamilyOid(String oldOID, String newOID);

	public boolean hasDraft(String oid);

	public List<ListObject> getListObject(
			ManageCodeListDetailModel currentDetails, Timestamp ts);

	public List<ListObject> getSupplimentalCodeList();

	public boolean isMyValueSet(String id, String ownerId);

	public List<CodeListSearchDTO> searchWithFilter(String searchText,
			String loggedInUserid, int i, int pageSize, String sortColumn,
			boolean isAsc, boolean defaultCodeList, int filter);

	String generateUniqueName(String name, User u);

	List<ListObject> getElementCodeListByOID(List<String> elementOIDList);
}
