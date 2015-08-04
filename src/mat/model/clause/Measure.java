package mat.model.clause;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import mat.model.User;

/**
 * The Class Measure.
 */
public class Measure {
	
	/** The id. */
	private String id;
	
	/** The owner. */
	private User owner;
	
	/** The a bbr name. */
	private String aBBRName;
	
	/** The description. */
	private String description;
	
	/** The version. */
	private String version;
	
	/** The Revision Number. */
	private String revisionNumber;
	
	/** The measure status. */
	/*private String measureStatus;*/
	
	/** The measure scoring. */
	private String measureScoring;
	
	/** The exported date. */
	private Date exportedDate;
	
	/** The shares. */
	private Set<MeasureShare> shares;
	
	/** The locked out date. */
	private Timestamp lockedOutDate;
	
	/** The locked user. */
	private User lockedUser;
	//Sprint 16 additions
	/** The draft. */
	private boolean draft;
	
	/** The measure set. */
	private MeasureSet measureSet;
	
	/** The finalized date. */
	private Timestamp finalizedDate;
	//Sprint17
	/** The value set date. */
	private Timestamp valueSetDate;
	
	/** The e measure id. */
	private int eMeasureId;
	
	//private String deleted;
	/** The is private. */
	private boolean isPrivate;
	
	private String releaseVersion;
	
	/**
	 * Gets the value set date.
	 * 
	 * @return the value set date
	 */
	public Timestamp getValueSetDate() {
		return valueSetDate;
	}
	
	/**
	 * Sets the value set date.
	 * 
	 * @param valueSetDate
	 *            the new value set date
	 */
	public void setValueSetDate(Timestamp valueSetDate) {
		this.valueSetDate = valueSetDate;
	}
	
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
	 * Gets the measure set.
	 * 
	 * @return the measure set
	 */
	public MeasureSet getMeasureSet() {
		if(measureSet == null) {
			measureSet= new MeasureSet();
		}
		return measureSet;
	}
	
	/**
	 * Sets the measure set.
	 * 
	 * @param measureSet
	 *            the new measure set
	 */
	public void setMeasureSet(MeasureSet measureSet) {
		this.measureSet = measureSet;
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
	 * Gets the a bbr name.
	 * 
	 * @return the a bbr name
	 */
	public String getaBBRName() {
		return aBBRName;
	}
	
	/**
	 * Sets the a bbr name.
	 * 
	 * @param aBBRName
	 *            the new a bbr name
	 */
	public void setaBBRName(String aBBRName) {
		this.aBBRName = aBBRName;
	}
	
	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the minor version int.
	 * 
	 * @return the minor version int
	 */
	public int getMinorVersionInt(){
		int minVersion = 0;
		if ((version != null) && !version.isEmpty()) {
			int decimalIndex = version.indexOf('.');
			if (decimalIndex < 0) {
				minVersion = 0;
			} else {
				minVersion = Integer.valueOf(version.substring(decimalIndex+1)).intValue();
			}
		}
		return minVersion;
	}
	
	/**
	 * Gets the minor version str.
	 * 
	 * @return the minor version str
	 */
	public String getMinorVersionStr(){
		return getMinorVersionInt() + "";
	}
	
	/**
	 * Gets the major version int.
	 * 
	 * @return the major version int
	 */
	public int getMajorVersionInt(){
		int maxVersion = 0;
		if ((version != null) && !version.isEmpty()) {
			int decimalIndex = version.indexOf('.');
			if (decimalIndex < 0) {
				maxVersion = Integer.valueOf(version).intValue();
			} else {
				maxVersion = Integer.valueOf(version.substring(0, decimalIndex)).intValue();
			}
		}
		return maxVersion;
	}
	
	/**
	 * Gets the major version str.
	 * 
	 * @return the major version str
	 */
	public String getMajorVersionStr(){
		return getMajorVersionInt() + "";
	}
	
	/**
	 * Gets the version number.
	 * 
	 * @return the version number
	 */
	public double getVersionNumber(){
		double versionNumber = 0;
		if ((version != null) && !version.isEmpty()) {
			versionNumber = Double.valueOf(version).doubleValue();
		}
		return versionNumber;
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
	 * Gets the measure scoring.
	 * 
	 * @return the measure scoring
	 */
	public String getMeasureScoring() {
		return measureScoring;
	}
	
	/**
	 * Sets the measure scoring.
	 * 
	 * @param measureScoring
	 *            the new measure scoring
	 */
	public void setMeasureScoring(String measureScoring) {
		this.measureScoring = measureScoring;
	}
	
	/**
	 * Gets the measure status.
	 * 
	 * @return the measure status
	 */
	/*public String getMeasureStatus() {
		return measureStatus;
	}
	
	 *//**
	 * Sets the measure status.
	 * 
	 * @param measureStatus
	 *            the new measure status
	 *//*
	public void setMeasureStatus(String measureStatus) {
		this.measureStatus = measureStatus;
	}*/
	
	/**
	 * Gets the owner.
	 * 
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}
	
	/**
	 * Sets the owner.
	 * 
	 * @param owner
	 *            the new owner
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	/**
	 * Gets the shares.
	 * 
	 * @return the shares
	 */
	public Set<MeasureShare> getShares() {
		return shares;
	}
	
	/**
	 * Sets the shares.
	 * 
	 * @param shares
	 *            the new shares
	 */
	public void setShares(Set<MeasureShare> shares) {
		this.shares = shares;
	}
	
	/**
	 * Gets the exported date.
	 * 
	 * @return the exported date
	 */
	public Date getExportedDate() {
		return exportedDate;
	}
	
	/**
	 * Sets the exported date.
	 * 
	 * @param exportedDate
	 *            the new exported date
	 */
	public void setExportedDate(Date exportedDate) {
		this.exportedDate = exportedDate;
	}
	
	/**
	 * Gets the locked out date.
	 * 
	 * @return the locked out date
	 */
	public Timestamp getLockedOutDate() {
		return lockedOutDate;
	}
	
	/**
	 * Sets the locked out date.
	 * 
	 * @param lockedOutDate
	 *            the new locked out date
	 */
	public void setLockedOutDate(Timestamp lockedOutDate) {
		this.lockedOutDate = lockedOutDate;
	}
	
	/**
	 * Gets the locked user.
	 * 
	 * @return the locked user
	 */
	public User getLockedUser() {
		return lockedUser;
	}
	
	/**
	 * Sets the locked user.
	 * 
	 * @param lockedUser
	 *            the new locked user
	 */
	public void setLockedUser(User lockedUser) {
		this.lockedUser = lockedUser;
	}
	
	/**
	 * Gets the e measure id.
	 * 
	 * @return the e measure id
	 */
	public int geteMeasureId() {
		return eMeasureId;
	}
	
	/**
	 * Sets the e measure id.
	 * 
	 * @param eMeasureId
	 *            the new e measure id
	 */
	public void seteMeasureId(int eMeasureId) {
		this.eMeasureId = eMeasureId;
	}
	
	/*public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}*/
	/**
	 * Gets the checks if is private.
	 * 
	 * @return the checks if is private
	 */
	public boolean getIsPrivate() {
		return isPrivate;
	}
	
	/**
	 * Sets the checks if is private.
	 * 
	 * @param isPrivate
	 *            the new checks if is private
	 */
	public void setIsPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
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

	/**
	 * @return the releaseVersion
	 */
	public String getReleaseVersion() {
		return releaseVersion;
	}

	/**
	 * @param releaseVersion the releaseVersion to set
	 */
	public void setReleaseVersion(String releaseVersion) {
		this.releaseVersion = releaseVersion;
	}
}
