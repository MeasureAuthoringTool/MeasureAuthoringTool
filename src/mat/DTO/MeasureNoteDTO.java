package mat.DTO;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.User;


public class MeasureNoteDTO implements IsSerializable{
	private String id;	
	private String noteTitle;
	private String noteDesc;
	private String lastModifiedByEmailAddress;
	private Date lastModifiedDate;
	private String measureId;

	public MeasureNoteDTO(){
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNoteTitle() {
		return noteTitle;
	}
	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}
	public String getNoteDesc() {
		return noteDesc;
	}
	public void setNoteDesc(String noteDesc) {
		this.noteDesc = noteDesc;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedByEmailAddress() {
		return lastModifiedByEmailAddress;
	}

	public void setLastModifiedByEmailAddress(String lastModifiedByEmailAddress) {
		this.lastModifiedByEmailAddress = lastModifiedByEmailAddress;
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
}
