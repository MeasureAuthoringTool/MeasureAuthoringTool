package mat.DTO;

import mat.model.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The Class MeasureNoteDTO.
 */
public class MeasureNoteDTO implements IsSerializable , BaseModel{
	
	/** The id. */
	private String id;
	
	/** The note title. */
	private String noteTitle;
	
	/** The note desc. */
	private String noteDesc;
	
	/** The last modified by email address. */
	private String lastModifiedByEmailAddress;
	
	/** The last modified date. */
	private String lastModifiedDate;
	
	/** The measure id. */
	private String measureId;
	
	/**
	 * Instantiates a new measure note dto.
	 */
	public MeasureNoteDTO(){
		
	}
	
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
	 * Gets the last modified date.
	 * 
	 * @return the last modified date
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	
	/**
	 * Sets the last modified date.
	 * 
	 * @param lastModifiedDate
	 *            the new last modified date
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	/**
	 * Gets the last modified by email address.
	 * 
	 * @return the last modified by email address
	 */
	public String getLastModifiedByEmailAddress() {
		return lastModifiedByEmailAddress;
	}
	
	/**
	 * Sets the last modified by email address.
	 * 
	 * @param lastModifiedByEmailAddress
	 *            the new last modified by email address
	 */
	public void setLastModifiedByEmailAddress(String lastModifiedByEmailAddress) {
		this.lastModifiedByEmailAddress = lastModifiedByEmailAddress;
	}
	
	/**
	 * Gets the measure id.
	 * 
	 * @return the measure id
	 */
	public String getMeasureId() {
		return measureId;
	}
	
	/**
	 * Sets the measure id.
	 * 
	 * @param measureId
	 *            the new measure id
	 */
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	@Override
	public void scrubForMarkUp() {
		String markupRegExp = "<[^>]+>";
		String noMarkupText = this.getNoteTitle().trim().replaceAll(markupRegExp, "");
		System.out.println("MeasureNote Title:" + noMarkupText);
		if (this.getNoteTitle().trim().length() > noMarkupText.length()) {
			this.setNoteTitle(noMarkupText);
		}
		
		/*noMarkupText = this.getNoteDesc().trim().replaceAll(markupRegExp, "");
		System.out.println("MeasureNote Note Desc:" + noMarkupText);
		if (this.getNoteDesc().trim().length() > noMarkupText.length()) {
			this.setNoteDesc(noMarkupText);
		}*/
	}
}
