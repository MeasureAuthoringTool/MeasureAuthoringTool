package mat.client.shared.search;

import com.google.gwt.user.client.ui.Widget;

import java.util.List;

//
// takes the whole results collection and then mimics
// paging on it
//
/**
 * The Class PagingFacade.
 * 
 * @param <T>
 *            the generic type
 */
public abstract class PagingFacade<T> implements SearchResults<T> {

	/** The current page. */
	private int currentPage = 1;
	
	/** The page size. */
	private int pageSize;
	
	/** The total pages. */
	private int totalPages;
	
	/** The results. */
	private List<T> results;
	
	/**
	 * Instantiates a new paging facade.
	 * 
	 * @param results
	 *            the results
	 */
	public PagingFacade(List<T> results) {
		this.results = results;
	}
	
	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	protected List<T> getData() {
		return results;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getStartIndex()
	 */
	@Override
	public final int getStartIndex() {
		return pageSize * (currentPage - 1) + 1;
	}
	
	/**
	 * Sets the current page.
	 * 
	 * @param i
	 *            the new current page
	 */
	public final void setCurrentPage(int i) {
		currentPage = i;
	}
	
	/**
	 * Sets the page size.
	 * 
	 * @param i
	 *            the new page size
	 */
	public final void setPageSize(int i) {
		pageSize = i;
	}	
	
	/**
	 * Gets the totalpages.
	 * 
	 * @return the totalpages
	 */
	public final int getTotalpages() {
		return totalPages;
	}

	/**
	 * Sets the total pages.
	 * 
	 * @param i
	 *            the new total pages
	 */
	public final void setTotalPages(int i) {
		totalPages = i;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getResultsTotal()
	 */
	@Override
	public final int getResultsTotal() {
		return results.size();
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfColumns()
	 */
	@Override
	public abstract int getNumberOfColumns();

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfRows()
	 */
	@Override
	public final int getNumberOfRows() {
		if(results != null){
			return Math.min(pageSize, results.size() - getStartIndex() + 1);
		}else{
			return this.pageSize;
		}
	}

	/**
	 * Convert row index to data index.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @return the int
	 */
	protected final int convertRowIndexToDataIndex(int rowIndex) {
		return rowIndex + getStartIndex() - 1;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#get(int)
	 */
	@Override
	public final T get(int rowIndex) {
		return results.get(convertRowIndexToDataIndex(rowIndex));
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getKey(int)
	 */
	@Override
	public final String getKey(int rowIndex) {
		return getKey(get(rowIndex));
	}
	
	/**
	 * Gets the key.
	 * 
	 * @param dataObject
	 *            the data object
	 * @return the key
	 */
	public abstract String getKey(T dataObject);

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnHeader(int)
	 */
	@Override
	public abstract String getColumnHeader(int columnIndex);

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSortable(int)
	 */
	@Override
	public abstract boolean isColumnSortable(int columnIndex);

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnFiresSelection(int)
	 */
	@Override
	public abstract boolean isColumnFiresSelection(int columnIndex);

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnWidth(int)
	 */
	@Override
	public abstract String getColumnWidth(int columnIndex);

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getValue(int, int)
	 */
	@Override
	public final Widget getValue(int rowIndex, int column) {
		return getValueImpl(get(rowIndex), column);
	}
	
	
	
	/**
	 * Gets the value impl.
	 * 
	 * @param dataObject
	 *            the data object
	 * @param column
	 *            the column
	 * @return the value impl
	 */
	public abstract Widget getValueImpl(T dataObject, int column);

}
