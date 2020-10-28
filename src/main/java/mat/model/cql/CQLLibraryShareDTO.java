package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.LockedUserInfo;

import java.sql.Timestamp;

public class CQLLibraryShareDTO implements IsSerializable {
	
	/** The user id. */
	private String userId;
	
	/** The cqlLibrary id. */
	private String cqlLibraryId;
	
	/** The cqlLibrary name. */
	private String cqlLibraryName;
	
	/** The first name. */
	private String firstName;
	
	/** The last name. */
	private String lastName;
	
	/** The share level. */
	private String shareLevel;
	
	/** The status. */
	private String status;
	
	/** The owner user id. */
	private String ownerUserId;
	
	/** The packaged. */
	private boolean packaged;
	
	/** The locked. */
	private boolean locked;
	
	/** The locked user info. */
	private LockedUserInfo lockedUserInfo;
	
	/** The organization name. */
	private String organizationName;
	
	/** The draft. */
	private boolean draft;
	
	/** The finalized date. */
	private Timestamp finalizedDate;
	
	/** The version. */
	private String version;
	
	/** The Revision. */
	private String revisionNumber;
	
	/** The measure set id. */
	private String cqlLibrarySetId;

	private boolean isDraftable;
	
	
	private boolean isVersionable;

	private String libraryModelType;

	private String releaseVersion;

	private String qdmVersion;

	private String fhirVersion;

	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * Sets the user id.
	 * 
	 * @param id
	 *            the new user id
	 */
	public void setUserId(String id) {
		this.userId = id;
	}
	
	/**
	 * Gets the first name.
	 * 
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Sets the first name.
	 * 
	 * @param firstName
	 *            the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Gets the last name.
	 * 
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Sets the last name.
	 * 
	 * @param lastName
	 *            the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Gets the share level.
	 * 
	 * @return the share level
	 */
	public String getShareLevel() {
		return shareLevel;
	}
	
	/**
	 * Sets the share level.
	 * 
	 * @param shareLevel
	 *            the new share level
	 */
	public void setShareLevel(String shareLevel) {
		this.shareLevel = shareLevel;
	}
	
	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Checks if is packaged.
	 * 
	 * @return true, if is packaged
	 */
	public boolean isPackaged() {
		return packaged;
	}
	
	/**
	 * Sets the packaged.
	 * 
	 * @param packaged
	 *            the new packaged
	 */
	public void setPackaged(boolean packaged) {
		this.packaged = packaged;
	}
	
	/**
	 * Gets the owner user id.
	 * 
	 * @return the owner user id
	 */
	public String getOwnerUserId() {
		return ownerUserId;
	}
	
	/**
	 * Sets the owner user id.
	 * 
	 * @param ownerUserId
	 *            the new owner user id
	 */
	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}
	
	/**
	 * Checks if is locked.
	 * 
	 * @return true, if is locked
	 */
	public boolean isLocked() {
		return locked;
	}
	
	/**
	 * Sets the locked.
	 * 
	 * @param locked
	 *            the new locked
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	/**
	 * Gets the locked user info.
	 * 
	 * @return the locked user info
	 */
	public LockedUserInfo getLockedUserInfo() {
		return lockedUserInfo;
	}
	
	/**
	 * Sets the locked user info.
	 * 
	 * @param lockedUserInfo
	 *            the new locked user info
	 */
	public void setLockedUserInfo(LockedUserInfo lockedUserInfo) {
		this.lockedUserInfo = lockedUserInfo;
	}
	
	/*US501*/
	/**
	 * Checks if is draft.
	 * 
	 * @return true, if is draft
	 */
	public boolean isDraft() {
		return draft;
	}
	
	/**
	 * Sets the draft.
	 * 
	 * @param draft
	 *            the new draft
	 */
	public void setDraft(boolean draft) {
		this.draft = draft;
	}
	
	/**
	 * Gets the finalized date.
	 * 
	 * @return the finalized date
	 */
	public Timestamp getFinalizedDate() {
		return finalizedDate;
	}
	
	/**
	 * Sets the finalized date.
	 * 
	 * @param finalizedDate
	 *            the new finalized date
	 */
	public void setFinalizedDate(Timestamp finalizedDate) {
		this.finalizedDate = finalizedDate;
	}
	
	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	
	/**
	 * Gets the organization name.
	 * 
	 * @return the organizationName
	 */
	public String getOrganizationName() {
		return organizationName;
	}
	
	/**
	 * Sets the organization name.
	 * 
	 * @param organizationName
	 *            the organizationName to set
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	
	
	public boolean isDraftable() {
		return isDraftable;
	}

	public void setDraftable(boolean isDraftable) {
		this.isDraftable = isDraftable;
	}

	public boolean isVersionable() {
		return isVersionable;
	}

	public void setVersionable(boolean isVersionable) {
		this.isVersionable = isVersionable;
	}

	/**
	 * @return the revisionNumber
	 */
	public String getRevisionNumber() {
		return revisionNumber;
	}

	/**
	 * @param revisionNumber the revisionNumber to set
	 */
	public void setRevisionNumber(String revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public String getCqlLibraryId() {
		return cqlLibraryId;
	}

	public void setCqlLibraryId(String cqlLibraryId) {
		this.cqlLibraryId = cqlLibraryId;
	}

	public String getCqlLibraryName() {
		return cqlLibraryName;
	}

	public void setCqlLibraryName(String cqlLibraryName) {
		this.cqlLibraryName = cqlLibraryName;
	}

	public String getCqlLibrarySetId() {
		return cqlLibrarySetId;
	}

	public void setCqlLibrarySetId(String cqlLibrarySetId) {
		this.cqlLibrarySetId = cqlLibrarySetId;
	}

	public String getLibraryModelType() {
		return libraryModelType;
	}

	public void setLibraryModelType(String libraryModelType) {
		this.libraryModelType = libraryModelType;
	}


	public void setReleaseVersion(String releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

	public String getReleaseVersion() {
		return releaseVersion;
	}

	public void setQdmVersion(String qdmVersion) {
		this.qdmVersion = qdmVersion;
	}

	public String getQdmVersion() {
		return qdmVersion;
	}

    public String getFhirVersion() {
        return fhirVersion;
    }

    public void setFhirVersion(String fhirVersion) {
        this.fhirVersion = fhirVersion;
    }
}
