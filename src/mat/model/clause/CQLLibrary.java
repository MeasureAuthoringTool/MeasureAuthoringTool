package mat.model.clause;

import java.sql.Timestamp;
import com.mysql.jdbc.Blob;

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
	private String ownerId; 
	
	/**
	 * The set id
	 */
	private String setId; 
	
	/**
	 * The version
	 */
	private String version; 
	
	/**
	 * The draft
	 */
	private int draft;
	
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
	private String lockedUserId; 
	
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

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getSetId() {
		return setId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getDraft() {
		return draft;
	}

	public void setDraft(int draft) {
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

	public void setCqlXML(Blob cqlXML) {
		this.cqlXML = cqlXML;
	}

	public String getReleaseVersion() {
		return releaseVersion;
	}

	public void setReleaseVersion(String releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

	public String getLockedUserId() {
		return lockedUserId;
	}

	public void setLockedUserId(String lockedUserId) {
		this.lockedUserId = lockedUserId;
	}

	public Timestamp getLockedOutDate() {
		return lockedOutDate;
	}

	public void setLockedOutDate(Timestamp lockedOutDate) {
		this.lockedOutDate = lockedOutDate;
	} 
	
	

}
