package mat.client.codelist;

import java.util.List;

import mat.client.shared.search.SearchResults;
import mat.model.CodeListSearchDTO;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ManageCodeListSearchModel.
 */
public class ManageCodeListSearchModel implements SearchResults<CodeListSearchDTO>,
		IsSerializable {
	//US538 --adding new column Last Modified in the value set workspace
	//US190, US192
	
	
	// Clone Column removed for User Story MAT-2372 : Remove Value Set Creation.
	/** The headers. */
	private String[] headers = new String[] { "Name","Last Modified","Steward","Category","Code System", "History","Export"};
	
	/** The sortable. */
	private boolean[] sortable = new boolean[]{true,false,true, true, true, false,false,false}; //US 385
	
	/** The widths. */
	private String[] widths = new String[] { "25%","15%","20%", "10%", "10%", "5%","5%"};
	

	/**
	 * Gets the headers.
	 * 
	 * @return the headers
	 */
	public String[] getHeaders() {
		return headers;
	}
	
	/**
	 * Sets the headers.
	 * 
	 * @param headers
	 *            the new headers
	 */
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
	
	/**
	 * Gets the sortable.
	 * 
	 * @return the sortable
	 */
	public boolean[] getSortable() {
		return sortable;
	}
	
	/**
	 * Sets the sortable.
	 * 
	 * @param sortable
	 *            the new sortable
	 */
	public void setSortable(boolean[] sortable) {
		this.sortable = sortable;
	}
	
	/**
	 * Gets the widths.
	 * 
	 * @return the widths
	 */
	public String[] getWidths() {
		return widths;
	}
	
	/**
	 * Sets the widths.
	 * 
	 * @param widths
	 *            the new widths
	 */
	public void setWidths(String[] widths) {
		this.widths = widths;
	}
	
	/** The data. */
	private List<CodeListSearchDTO> data;
	
	/** The start index. */
	private int startIndex;
	
	/** The results total. */
	private int resultsTotal;
	
	/** The page count. */
	private int pageCount;
	
	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(List<CodeListSearchDTO> data) {
		this.data = data;
	}
	
	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	public List<CodeListSearchDTO> getData(){
		return data;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSortable(int)
	 */
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return sortable[columnIndex];
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfColumns()
	 */
	@Override
	public int getNumberOfColumns() {
		return headers.length;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfRows()
	 */
	@Override
	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnHeader(int)
	 */
	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnWidth(int)
	 */
	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getValue(int, int)
	 */
	@Override
	public Widget getValue(int row, int column){
		return null;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getStartIndex()
	 */
	@Override
	public int getStartIndex() {
		return startIndex;
	}
	
	/**
	 * Sets the start index.
	 * 
	 * @param startIndex
	 *            the new start index
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getResultsTotal()
	 */
	@Override
	public int getResultsTotal() {
		return resultsTotal;
	}
	
	/**
	 * Sets the results total.
	 * 
	 * @param resultsTotal
	 *            the new results total
	 */
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getKey(int)
	 */
	@Override
	public String getKey(int row) {
		return data.get(row).getId();
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#get(int)
	 */
	@Override
	public CodeListSearchDTO get(int row) {
		return data.get(row);
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return columnIndex == 0;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}
	
	/**
	 * Gets the page count.
	 * 
	 * @return the page count
	 */
	public int getPageCount() {
		return pageCount;
	}
	
	/**
	 * Sets the page count.
	 * 
	 * @param pageCount
	 *            the new page count
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	
}
