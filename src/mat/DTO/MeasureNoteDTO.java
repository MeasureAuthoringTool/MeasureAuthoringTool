package mat.DTO;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.User;


public class MeasureNoteDTO implements IsSerializable{
	private String id;
	private String noteTitle;
	private String noteDesc;
	private String createUserEmailAddress;
	private String modifyUserEmailAddress;
	private Date lastModifiedDate;

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

	public String getCreateUserEmailAddress() {
		return createUserEmailAddress;
	}

	public void setCreateUserEmailAddress(String createUserEmailAddress) {
		this.createUserEmailAddress = createUserEmailAddress;
	}

	public String getModifyUserEmailAddress() {
		return modifyUserEmailAddress;
	}

	public void setModifyUserEmailAddress(String modifyUserEmailAddress) {
		this.modifyUserEmailAddress = modifyUserEmailAddress;
	}
}
