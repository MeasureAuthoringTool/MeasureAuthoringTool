package mat.model.clause;

import java.sql.Timestamp;

import mat.model.LockedUserInfo;
import mat.model.User;

import java.sql.Blob;

public class CQLLibrary {
	
	/**
	 * The cql library id
	 */
	private String id; 
	
	/**
	 * The name
	 */
	private String name; 
	
	/**
	 * The measure id
	 */
	private String measureId; 
	
	/**
	 * The owner id
	 */
	private User ownerId; 
	
	/**
	 * The measure set id
	 */
	private MeasureSet measureSetId;
	
	/**
	 * The cql set id
	 */
	private String cqlSetId; 
	
	/**
	 * The version
	 */
	private String version; 
	
	/**
	 * The draft
	 */
	private boolean draft;
	
	/**
	 * The finalized date
	 */
	private Timestamp finalizedDate; 
	
	/**
	 * The release version
	 */
	private String releaseVersion; 
		
	/**
	 * The locked user id
	 */
	private LockedUserInfo lockedUserId; 
	
	/**
	 * The locked out date
	 */
	private Timestamp lockedOutDate; 
	
	/**
	 * The cql xml
	 */
	private Blob cqlXML;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	public User getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(User ownerId) {
		this.ownerId = ownerId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	public Timestamp getFinalizedDate() {
		return finalizedDate;
	}

	public void setFinalizedDate(Timestamp finalizedDate) {
		this.finalizedDate = finalizedDate;
	}

	public Blob getCqlXML() {
		return cqlXML;
	}

	public void setCqlXML(java.sql.Blob cqlBlob) {
		this.cqlXML = cqlBlob;
	}

	public String getReleaseVersion() {
		return releaseVersion;
	}

	public void setReleaseVersion(String releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

	public LockedUserInfo getLockedUserId() {
		return lockedUserId;
	}

	public void setLockedUserId(LockedUserInfo lockedUserId) {
		this.lockedUserId = lockedUserId;
	}

	public Timestamp getLockedOutDate() {
		return lockedOutDate;
	}

	public void setLockedOutDate(Timestamp lockedOutDate) {
		this.lockedOutDate = lockedOutDate;
	}

	public MeasureSet getMeasureSetId() {
		return measureSetId;
	}

	public void setMeasureSetId(MeasureSet measureSetId) {
		this.measureSetId = measureSetId;
	}

	public String getCqlSetId() {
		return cqlSetId;
	}

	public void setCqlSetId(String cqlSetId) {
		this.cqlSetId = cqlSetId;
	} 
	
	

}
