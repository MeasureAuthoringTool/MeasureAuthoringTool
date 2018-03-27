package mat.client.shared.search;

import com.google.gwt.user.client.ui.Widget;

/**
 * The Interface SearchResults.
 * 
 * @param <T>
 *            the generic type
 */
public interface SearchResults<T> {
	
	/**
	 * Gets the start index.
	 * 
	 * @return the start index
	 */
	int getStartIndex();
	
	/**
	 * Gets the results total.
	 * 
	 * @return the results total
	 */
	int getResultsTotal();
	
	/**
	 * Gets the number of columns.
	 * 
	 * @return the number of columns
	 */
	int getNumberOfColumns();
	
	/**
	 * Gets the number of rows.
	 * 
	 * @return the number of rows
	 */
	int getNumberOfRows();
	
	/**
	 * Gets the.
	 * 
	 * @param row
	 *            the row
	 * @return the t
	 */
	T get(int row);
	
	/**
	 * Gets the key.
	 * 
	 * @param row
	 *            the row
	 * @return the key
	 */
	String getKey(int row);
	
	/**
	 * Gets the column header.
	 * 
	 * @param columnIndex
	 *            the column index
	 * @return the column header
	 */
	String getColumnHeader(int columnIndex);
	
	/**
	 * Checks if is column sortable.
	 * 
	 * @param columnIndex
	 *            the column index
	 * @return true, if is column sortable
	 */
	boolean isColumnSortable(int columnIndex);
	
	/**
	 * Checks if is column select all.
	 * 
	 * @param columnIndex
	 *            the column index
	 * @return true, if is column select all
	 */
	boolean isColumnSelectAll(int columnIndex);
	
	/**
	 * Checks if is column fires selection.
	 * 
	 * @param columnIndex
	 *            the column index
	 * @return true, if is column fires selection
	 */
	boolean isColumnFiresSelection(int columnIndex);
	
	/**
	 * Gets the column width.
	 * 
	 * @param columnIndex
	 *            the column index
	 * @return the column width
	 */
	String getColumnWidth(int columnIndex);
	
	/**
	 * Gets the value.
	 * 
	 * @param row
	 *            the row
	 * @param column
	 *            the column
	 * @return the value
	 */
	Widget getValue(int row, int column);


}
