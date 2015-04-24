package mat.client.codelist;


import java.util.List;

import mat.client.shared.search.SearchResults;
import mat.model.GroupedCodeListDTO;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ManageGroupedCodeListsSummaryModel.
 */
public class ManageGroupedCodeListsSummaryModel implements SearchResults<GroupedCodeListDTO> ,IsSerializable{

	/** The headers. */
	private static String[] headers = new String[] {"Value Set","Descriptor"};
	
	/** The widths. */
	private static String[] widths = new String[] {"30%", "50%"};
	
	/** The data. */
	private List<GroupedCodeListDTO> data;

	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(List<GroupedCodeListDTO> data) {
		this.data = data;
	}
	
	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	public List<GroupedCodeListDTO> getData(){
		return data;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getStartIndex()
	 */
	@Override
	public int getStartIndex() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getResultsTotal()
	 */
	@Override
	public int getResultsTotal() {
		return 0;
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
	 * @see mat.client.shared.search.SearchResults#get(int)
	 */
	@Override
	public GroupedCodeListDTO get(int row) {
		return null;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getKey(int)
	 */
	@Override
	public String getKey(int row) {
		return null;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnHeader(int)
	 */
	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSortable(int)
	 */
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
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
	public Widget getValue(int row, int column) {
		Widget value;
		switch(column) {
		case 0:
			value = new Label(data.get(row).getName());
			break;
		case 1:
			value = new Label(data.get(row).getDescription());
			break;
		default:
			value = new Label("Unknown Column Index");
		}
		return value;
	}

}
