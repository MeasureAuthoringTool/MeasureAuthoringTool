package mat.client.shared.search;

import com.google.gwt.user.client.ui.Widget;

public interface SearchResults<T> {
	int getStartIndex();
	int getResultsTotal();
	int getNumberOfColumns();
	int getNumberOfRows();
	T get(int row);
	String getKey(int row);
	String getColumnHeader(int columnIndex);
	boolean isColumnSortable(int columnIndex);
	boolean isColumnSelectAll(int columnIndex);
	boolean isColumnFiresSelection(int columnIndex);
	String getColumnWidth(int columnIndex);
	Widget getValue(int row, int column);
}
