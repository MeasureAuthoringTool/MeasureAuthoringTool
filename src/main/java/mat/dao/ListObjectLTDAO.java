package mat.dao;

import mat.model.ListObjectLT;

/**
 * Lightweight (no codes) ListObject data access data structure.
 * 
 * @author aschmidt
 */
public interface ListObjectLTDAO extends IDAO<ListObjectLT, String> {
	
	/**
	 * Gets the list objects to draft.
	 * 
	 * @return the list objects to draft
	 */
	//public List<ListObjectLT> getListObjectsToDraft();
	
	/**
	 * Count search results with filter.
	 * 
	 * @param searchText
	 *            the search text
	 * @param userid
	 *            the userid
	 * @param defaultCodeList
	 *            the default code list
	 * @param filter
	 *            the filter
	 * @return the int
	 */
	//public int countSearchResultsWithFilter(String searchText, String userid, boolean defaultCodeList, int filter);
}
