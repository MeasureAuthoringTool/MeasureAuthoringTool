package org.ifmc.mat.client.shared.search;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

//
// takes the whole results collection and then mimics
// paging on it
//
public abstract class PagingFacade<T> implements SearchResults<T> {

	private int currentPage = 1;
	private int pageSize;
	private int totalPages;
	private List<T> results;
	
	public PagingFacade(List<T> results) {
		this.results = results;
	}
	
	protected List<T> getData() {
		return results;
	}
	@Override
	public final int getStartIndex() {
		return pageSize * (currentPage - 1) + 1;
	}
	public final void setCurrentPage(int i) {
		currentPage = i;
	}
	public final void setPageSize(int i) {
		pageSize = i;
	}	
	
	public final int getTotalpages() {
		return totalPages;
	}

	public final void setTotalPages(int i) {
		totalPages = i;
	}
	
	@Override
	public final int getResultsTotal() {
		return results.size();
	}

	@Override
	public abstract int getNumberOfColumns();

	@Override
	public final int getNumberOfRows() {
		if(results != null){
			return Math.min(pageSize, results.size() - getStartIndex() + 1);
		}else{
			return this.pageSize;
		}
	}

	protected final int convertRowIndexToDataIndex(int rowIndex) {
		return rowIndex + getStartIndex() - 1;
	}
	
	@Override
	public final T get(int rowIndex) {
		return results.get(convertRowIndexToDataIndex(rowIndex));
	}

	@Override
	public final String getKey(int rowIndex) {
		return getKey(get(rowIndex));
	}
	
	public abstract String getKey(T dataObject);

	@Override
	public abstract String getColumnHeader(int columnIndex);

	@Override
	public abstract boolean isColumnSortable(int columnIndex);

	@Override
	public abstract boolean isColumnFiresSelection(int columnIndex);

	@Override
	public abstract String getColumnWidth(int columnIndex);

	@Override
	public final Widget getValue(int rowIndex, int column) {
		return getValueImpl(get(rowIndex), column);
	}
	
	
	
	public abstract Widget getValueImpl(T dataObject, int column);

}
