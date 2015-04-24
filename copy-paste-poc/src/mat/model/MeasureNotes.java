package mat.model;

import java.util.Date;

/**
 * The Class MeasureNotes.
 */
public class MeasureNotes implements Cloneable {

	/** The id. */
	private String id;
	
	/** The measure_id. */
	private String measure_id;
	
	/** The note title. */
	private String noteTitle;
	
	/** The note desc. */
	private String noteDesc;
	
	/** The create user. */
	private User createUser;
	
	/** The modify user. */
	private User modifyUser;
	
	/** The last modified date. */
	private Date lastModifiedDate;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the measure_id.
	 * 
	 * @return the measure_id
	 */
	public String getMeasure_id() {
		return measure_id;
	}

	/**
	 * Sets the measure_id.
	 * 
	 * @param measure_id
	 *            the new measure_id
	 */
	public void setMeasure_id(String measure_id) {
		this.measure_id = measure_id;
	}

	/**
	 * Gets the note title.
	 * 
	 * @return the note title
	 */
	public String getNoteTitle() {
		return noteTitle;
	}

	/**
	 * Sets the note title.
	 * 
	 * @param noteTitle
	 *            the new note title
	 */
	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}

	/**
	 * Gets the note desc.
	 * 
	 * @return the note desc
	 */
	public String getNoteDesc() {
		return noteDesc;
	}

	/**
	 * Sets the note desc.
	 * 
	 * @param noteDesc
	 *            the new note desc
	 */
	public void setNoteDesc(String noteDesc) {
		this.noteDesc = noteDesc;
	}

	/**
	 * Gets the creates the user.
	 * 
	 * @return the creates the user
	 */
	public User getCreateUser() {
		return createUser;
	}

	/**
	 * Sets the creates the user.
	 * 
	 * @param createUser
	 *            the new creates the user
	 */
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	/**
	 * Gets the modify user.
	 * 
	 * @return the modify user
	 */
	public User getModifyUser() {
		return modifyUser;
	}

	/**
	 * Sets the modify user.
	 * 
	 * @param modifyUser
	 *            the new modify user
	 */
	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}

	/**
	 * Gets the last modified date.
	 * 
	 * @return the last modified date
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * Sets the last modified date.
	 * 
	 * @param lastModifiedDate
	 *            the new last modified date
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public MeasureNotes clone() {
		MeasureNotes measureNotesClone = new MeasureNotes();
		measureNotesClone.setNoteTitle(this.getNoteTitle());
		measureNotesClone.setNoteDesc(this.getNoteDesc());
		measureNotesClone.setCreateUser(this.getCreateUser());
		measureNotesClone.setModifyUser(this.getModifyUser());
		measureNotesClone.setLastModifiedDate(this.getLastModifiedDate());
		return measureNotesClone;
	}

}