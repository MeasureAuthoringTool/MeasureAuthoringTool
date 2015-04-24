package mat.client.codelist;

import java.util.ArrayList;
import java.util.List;

import mat.model.CodeListSearchDTO;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class AdminManageCodeListSearchModel.
 */
public class AdminManageCodeListSearchModel extends ManageCodeListSearchModel implements 
		IsSerializable {
	//US538 --adding new column Last Modified in the value set workspace
	//US190, US192
	/** The headers. */
	private static String[] headers = new String[] { "Name","Last Modified","Steward","Category","Code System","History","TransferClear"};
	
	/** The sortable. */
	private static boolean[] sortable = new boolean[]{true,false,true, true, true,false,false}; //US 385
	
	/** The widths. */
	private static String[] widths = new String[] { "25%","15%","20%", "10%", "10%","5%","5%"};
	
	/** The data. */
	private List<CodeListSearchDTO> data;
	
	/** The start index. */
	private int startIndex;
	
	/** The results total. */
	private int resultsTotal;
	
	/** The page count. */
	private int pageCount;
	
	/** The transfer value set i ds. */
	private ArrayList<CodeListSearchDTO> transferValueSetIDs ;
	
	/** The lis object id. */
	private ArrayList <String> lisObjectId ;
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#setData(java.util.List)
	 */
	public void setData(List<CodeListSearchDTO> data) {
		this.data = data;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#getData()
	 */
	public List<CodeListSearchDTO> getData(){
		return data;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#isColumnSortable(int)
	 */
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return sortable[columnIndex];
	}


	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#getNumberOfColumns()
	 */
	@Override
	public int getNumberOfColumns() {
		return headers.length;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#getNumberOfRows()
	 */
	@Override
	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#getColumnHeader(int)
	 */
	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#getColumnWidth(int)
	 */
	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#getValue(int, int)
	 */
	@Override
	public Widget getValue(int row, int column){
		return null;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#getStartIndex()
	 */
	@Override
	public int getStartIndex() {
		return startIndex;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#setStartIndex(int)
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#getResultsTotal()
	 */
	@Override
	public int getResultsTotal() {
		return resultsTotal;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#setResultsTotal(int)
	 */
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}


	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#getKey(int)
	 */
	@Override
	public String getKey(int row) {
		return data.get(row).getId();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#get(int)
	 */
	@Override
	public CodeListSearchDTO get(int row) {
		return data.get(row);
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return columnIndex == 0;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#isColumnSelectAll(int)
	 */
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#getPageCount()
	 */
	public int getPageCount() {
		return pageCount;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchModel#setPageCount(int)
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	/**
	 * Sets the transfer value set i ds.
	 * 
	 * @param transferValueSetIDs
	 *            the transferValueSetIDs to set
	 */
	public void setTransferValueSetIDs(ArrayList<CodeListSearchDTO> transferValueSetIDs) {
		this.transferValueSetIDs = transferValueSetIDs;
	}
	
	/**
	 * Gets the transfer value set i ds.
	 * 
	 * @return the transferValueSetIDs
	 */
	public ArrayList<CodeListSearchDTO> getTransferValueSetIDs() {
		return transferValueSetIDs;
	}
	
	/**
	 * Sets the lis object id.
	 * 
	 * @param lisObjectId
	 *            the lisObjectId to set
	 */
	public void setLisObjectId(ArrayList <String> lisObjectId) {
		this.lisObjectId = lisObjectId;
	}
	
	/**
	 * Gets the lis object id.
	 * 
	 * @return the lisObjectId
	 */
	public ArrayList <String> getLisObjectId() {
		return lisObjectId;
	}
	
	
}
