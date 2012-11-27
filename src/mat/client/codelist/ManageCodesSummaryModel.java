package mat.client.codelist;


import java.util.List;

import mat.client.shared.search.SearchResults;
import mat.model.Code;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ManageCodesSummaryModel implements SearchResults<Code> ,IsSerializable{

	private static String[] headers = new String[] {"Code","Descriptor"};
	private static String[] widths = new String[] {"30%", "50%"};
	
	private List<Code> data;

	public void setData(List<Code> data) {
		this.data = data;
	}
	public List<Code> getData(){
		return data;
	}
	@Override
	public int getStartIndex() {
		return 1;
	}

	@Override
	public int getResultsTotal() {
		return 0;
	}

	@Override
	public int getNumberOfColumns() {
		return headers.length;
	}

	@Override
	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
	}

	@Override
	public Code get(int row) {
		return null;
	}

	@Override
	public String getKey(int row) {
		return null;
	}

	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}

	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}

	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
	}

	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}

	@Override
	public Widget getValue(int row, int column) {
		Widget value;
		switch(column) {
		case 0:
			value = new Label(data.get(row).getCode());
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
