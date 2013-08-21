package mat.client.measure;

import java.util.Date;
import java.util.List;
import mat.client.shared.search.SearchResults;
import mat.model.MeasureNotes;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;

public class MeasureNotesModel {
	/*public static class Result implements IsSerializable {
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
		
		
	}*/
	private List<MeasureNotes> data;

	public List<MeasureNotes> getData() {
		return data;
	}

	public void setData(List<MeasureNotes> data) {
		this.data = data;
	}
		
}
