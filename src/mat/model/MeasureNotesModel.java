package mat.model;

import java.util.Date;
import java.util.List;
import mat.client.shared.search.SearchResults;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;

public class MeasureNotesModel implements IsSerializable, SearchResults<MeasureNotesModel.Result> {
	public static class Result implements IsSerializable {
		private String noteId;
		private String measureId;
		private String title;
		private String description;
		private String createUser;
		private Date creationDate;
		private String creatorEmailAddr;
		public String getNoteId() {
			return noteId;
		}
		public void setNoteId(String noteId) {
			this.noteId = noteId;
		}
		public String getMeasureId() {
			return measureId;
		}
		public void setMeasureId(String measureId) {
			this.measureId = measureId;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getCreateUser() {
			return createUser;
		}
		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}
		public Date getCreationDate() {
			return creationDate;
		}
		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}
		public String getCreatorEmailAddr() {
			return creatorEmailAddr;
		}
		public void setCreatorEmailAddr(String creatorEmailAddr) {
			this.creatorEmailAddr = creatorEmailAddr;
		}	
		
		
	}
	private String noteId;
	private String measureId;
	private String title;
	private String description;
	private String createUser;
	private String modifyUser;
	private Date creationDate;
	private Date lastModifiedDate;
	
	private List<Result> data;
	private int startIndex;
	private int resultsTotal;
	private int pageCount;
	
	
	public String getNoteId() {
		return noteId;
	}
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}
	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public void setData(List<Result> data) {
		this.data = data;
	}
	
    public List<Result> getData(){
    	return data;
    }

    @Override
	public int getStartIndex() {
		// TODO Auto-generated method stub
		return startIndex;
	}    
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
		public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}
	
	public int getPageCount() {
			return pageCount;
		}
		public void setPageCount(int pageCount) {
			this.pageCount = pageCount;
		}
	@Override
	public int getResultsTotal() {
		// TODO Auto-generated method stub
		return resultsTotal;
	}
	@Override
	public int getNumberOfColumns() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getNumberOfRows() {
		// TODO Auto-generated method stub
		return data != null ? data.size() : 0;
	}
	@Override
	public Result get(int row) {
		// TODO Auto-generated method stub
		return data.get(row);
	}
	@Override
	public String getKey(int row) {
		// TODO Auto-generated method stub
		return data.get(row).getNoteId();
	}
	@Override
	public String getColumnHeader(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isColumnSortable(int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getColumnWidth(int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Widget getValue(int row, int column) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
