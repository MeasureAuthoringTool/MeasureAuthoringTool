package mat.dao;

import java.util.List;

import mat.model.ListObject;

/**
 * The Interface ListObjectDAO.
 */
public interface ListObjectDAO extends IDAO<ListObject, String> {
	
	/**
	 * Count search results by user.
	 * 
	 * @param searchText
	 *            the search text
	 * @param userId
	 *            the user id
	 * @param defaultCodeList
	 *            the default code list
	 * @return the int
	 */
	/*public int countSearchResultsByUser(String searchText, String userId,
			boolean defaultCodeList);*/

	/**
	 * Search by user.
	 * 
	 * @param searchText
	 *            the search text
	 * @param userId
	 *            the user id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param sortColumn
	 *            the sort column
	 * @param isAsc
	 *            the is asc
	 * @param defaultCodeList
	 *            the default code list
	 * @return the list
	 */
	/*public List<CodeListSearchDTO> searchByUser(String searchText,
			String userId, int startIndex, int pageSize, String sortColumn,
			boolean isAsc, boolean defaultCodeList);
*/
	/**
	 * Count search results by measure.
	 * 
	 * @param searchText
	 *            the search text
	 * @param measure
	 *            the measure
	 * @param defaultCodeList
	 *            the default code list
	 * @return the int
	 */
	/*public int countSearchResultsByMeasure(String searchText, Measure measure,
			boolean defaultCodeList);
*/
	/**
	 * Search by measure owner.
	 * 
	 * @param searchText
	 *            the search text
	 * @param ownerId
	 *            the owner id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param sortColumn
	 *            the sort column
	 * @param isAsc
	 *            the is asc
	 * @param defaultCodeList
	 *            the default code list
	 * @return the list
	 */
	/*public List<CodeListSearchDTO> searchByMeasureOwner(String searchText,
			String ownerId, int startIndex, int pageSize, String sortColumn,
			boolean isAsc, boolean defaultCodeList);*/

	// US 413. Added parameter for Steward Other
	/**
	 * Gets the list object.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @param userid
	 *            the userid
	 * @return the list object
	 */
	/*public ListObject getListObject(ManageCodeListDetailModel currentDetails,
			String userid);
*/
	/**
	 * Gets the list object.
	 * 
	 * @param name
	 *            the name
	 * @param steward
	 *            the steward
	 * @param categoryCd
	 *            the category cd
	 * @param userid
	 *            the userid
	 * @return the list object
	 */
	/*public ListObject getListObject(String name, String steward,
			String categoryCd, String userid);
*/
	/**
	 * Load all code list per user.
	 * 
	 * @param user
	 *            the user
	 * @return the list
	 */
	/*public List<ListObject> loadAllCodeListPerUser(String user);*/

	/**
	 * Find most recent value set.
	 * 
	 * @param loFamily
	 *            the lo family
	 * @param vsPackageDate
	 *            the vs package date
	 * @return the list object
	 */
	/*public ListObject findMostRecentValueSet(ListObject loFamily,
			Timestamp vsPackageDate);*/

	/**
	 * Gets the list object.
	 * 
	 * @param Oid
	 *            the oid
	 * @return the list object
	 */
	/*public List<ListObject> getListObject(String Oid);*/

	/**
	 * Generate unique oid.
	 * 
	 * @param user
	 *            the user
	 * @return the string
	 */
	/*public String generateUniqueOid(User user);*/

	/**
	 * Count list objects by oid and not id.
	 * 
	 * @param oid
	 *            the oid
	 * @param id
	 *            the id
	 * @return count(*) from list_object l where l.oid = oid and not l.id = id
	 */
	//public int countListObjectsByOidAndNotId(String oid, String id);

	/**
	 * Gets the list objects by measure.
	 * 
	 * @param measure
	 *            the measure
	 * @return the list objects by measure
	 */
//	public List<ListObject> getListObjectsByMeasure(Measure measure);

	/**
	 * Gets the list objects to draft.
	 * 
	 * @return the list objects to draft
	 */
	//public List<ListObject> getListObjectsToDraft();

	/**
	 * Update family oid.
	 * 
	 * @param oldOID
	 *            the old oid
	 * @param newOID
	 *            the new oid
	 */
	//public void updateFamilyOid(String oldOID, String newOID);

	/**
	 * Checks for draft.
	 * 
	 * @param oid
	 *            the oid
	 * @return true, if successful
	 */
	//public boolean hasDraft(String oid);

	/**
	 * Gets the list object.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @param ts
	 *            the ts
	 * @return the list object
	 */
//	public List<ListObject> getListObject(
	//		ManageCodeListDetailModel currentDetails, Timestamp ts);

	/**
	 * Gets the supplimental code list.
	 * 
	 * @return the supplimental code list
	 */
	public List<ListObject> getSupplimentalCodeList();

	/**
	 * Checks if is my value set.
	 * 
	 * @param id
	 *            the id
	 * @param ownerId
	 *            the owner id
	 * @return true, if is my value set
	 */
//	public boolean isMyValueSet(String id, String ownerId);

	/**
	 * Search with filter.
	 * 
	 * @param searchText
	 *            the search text
	 * @param loggedInUserid
	 *            the logged in userid
	 * @param i
	 *            the i
	 * @param pageSize
	 *            the page size
	 * @param sortColumn
	 *            the sort column
	 * @param isAsc
	 *            the is asc
	 * @param defaultCodeList
	 *            the default code list
	 * @param filter
	 *            the filter
	 * @return the list
	 */
//	public List<CodeListSearchDTO> searchWithFilter(String searchText,
//			String loggedInUserid, int i, int pageSize, String sortColumn,
//			boolean isAsc, boolean defaultCodeList, int filter);

	/**
	 * Generate unique name.
	 * 
	 * @param name
	 *            the name
	 * @param u
	 *            the u
	 * @return the string
	 */
	//String generateUniqueName(String name, User u);

	/**
	 * Gets the element code list by oid.
	 * 
	 * @param elementOIDList
	 *            the element oid list
	 * @return the element code list by oid
	 */
	List<ListObject> getElementCodeListByOID(List<String> elementOIDList);
}
