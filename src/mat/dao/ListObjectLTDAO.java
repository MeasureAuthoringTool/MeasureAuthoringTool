package mat.dao;

import java.util.List;

import mat.model.ListObjectLT;

/**
 * Lightweight (no codes) ListObject data access data structure
 * @author aschmidt
 *
 */
public interface ListObjectLTDAO extends IDAO<ListObjectLT, String> {
	public List<ListObjectLT> getListObjectsToDraft();
	public int countSearchResultsWithFilter(String searchText, String userid, boolean defaultCodeList, int filter);
}
