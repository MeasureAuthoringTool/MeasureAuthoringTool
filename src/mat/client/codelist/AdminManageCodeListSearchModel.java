package mat.client.codelist;

import java.util.List;

import mat.model.CodeListSearchDTO;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;

public class AdminManageCodeListSearchModel extends ManageCodeListSearchModel implements 
		IsSerializable {
	//US538 --adding new column Last Modified in the value set workspace
	//US190, US192
	private static String[] headers = new String[] { "Name","Last Modified","Steward","Category","Code System","History","Transfer"};
	private static boolean[] sortable = new boolean[]{true,false,true, true, true,false,false}; //US 385
	private static String[] widths = new String[] { "25%","15%","20%", "10%", "10%","5%","5%"};
	
	private List<CodeListSearchDTO> data;
	private int startIndex;
	private int resultsTotal;
	private int pageCount;
	
	public void setData(List<CodeListSearchDTO> data) {
		this.data = data;
	}
	public List<CodeListSearchDTO> getData(){
		return data;
	}
	
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return sortable[columnIndex];
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
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}

	@Override
	public Widget getValue(int row, int column){
		return null;
	}
	
	@Override
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	@Override
	public int getResultsTotal() {
		return resultsTotal;
	}
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}


	@Override
	public String getKey(int row) {
		return data.get(row).getId();
	}

	@Override
	public CodeListSearchDTO get(int row) {
		return data.get(row);
	}

	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return columnIndex == 0;
	}
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	
}
