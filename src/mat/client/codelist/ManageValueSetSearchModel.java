package mat.client.codelist;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import mat.client.shared.search.SearchResults;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author aschmidt
 *
 */
public class ManageValueSetSearchModel implements IsSerializable, SearchResults<ManageValueSetSearchModel.Result>{
	public static class Result implements IsSerializable {
		private String id;
		private String oid;
		private String name;
		private Timestamp lastModified;
		private boolean draft;
		private boolean isGrouped;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getOid() {
			return oid;
		}
		public void setOid(String oid) {
			this.oid = oid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Timestamp getLastModified() {
			return lastModified;
		}
		public void setLastModified(Timestamp lastModified) {
			this.lastModified = lastModified;
		}
		public boolean isDraft() {
			return draft;
		}
		public void setDraft(boolean draft) {
			this.draft = draft;
		}
		public boolean isGrouped() {
			return isGrouped;
		}
		public void setGrouped(boolean isGrouped) {
			this.isGrouped = isGrouped;
		}	
		
	}
	
	private List<Result> data;
	private int startIndex;
	private int resultsTotal;
	private int pageCount;
	
	public void setData(List<Result> data) {
		this.data = data;
	}
    public List<Result>  getData(){
    	if(data == null)
    		data = new ArrayList<Result>();
    	return data;
    }
	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getResultsTotal() {
		return resultsTotal;
	}
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}
	public String getKey(int row) {
		return data.get(row).getId();
	}
	public Result get(int row) {
		return data.get(row);
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	@Override
	public int getNumberOfColumns() {
		return 0;
	}
	@Override
	public String getColumnHeader(int columnIndex) {
	return null;
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
		return null;
	}
	@Override
	public Widget getValue(int row, int column) {
		return null;
	}
	
}

