package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.model.ListObjectLT;

/**
 * Lightweight (no codes) ListObject data access data structure
 * @author aschmidt
 *
 */
public interface ListObjectLTDAO extends IDAO<ListObjectLT, String> {
	public List<ListObjectLT> getListObjectsToDraft();
	public int countSearchResultsWithFilter(String searchText, String userid, boolean defaultCodeList, int filter);
}
