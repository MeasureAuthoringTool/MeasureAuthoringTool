package mat.model;

import java.util.Date;

public class MeasureNotes{

	private String id;
	private String measure_id;
	private String noteTitle;
	private String noteDesc;
	private User createUser;
	private User modifyUser;
	private Date lastModifiedDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getMeasure_id() {
		return measure_id;
	}
	public void setMeasure_id(String measure_id) {
		this.measure_id = measure_id;
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
	public User getCreateUser() {
		return createUser;
	}
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}
	public User getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
}