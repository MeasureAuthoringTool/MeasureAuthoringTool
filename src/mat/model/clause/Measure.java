package mat.model.clause;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import mat.model.User;

public class Measure {

	private String id;

	private User owner;

	private String aBBRName;

	private String description;

	private String version;
	
	/** The Revision Number. */
	private String revisionNumber;
	
	private String measureScoring;
	
	private Date exportedDate;
	
	private Set<MeasureShare> shares;
	
	private Timestamp lockedOutDate;
	
	private User lockedUser;

	private boolean draft;
	
	private MeasureSet measureSet;
	
	private Timestamp finalizedDate;

	private Timestamp valueSetDate;
	
	private int eMeasureId;
	
	private boolean isPrivate;
	
	private String releaseVersion;
	
	private boolean isPatientBased;
	
	private Timestamp lastModifiedOn; 
	
	private User lastModifiedBy;

	public Timestamp getValueSetDate() {
		return valueSetDate;
	}

	public void setValueSetDate(Timestamp valueSetDate) {
		this.valueSetDate = valueSetDate;
	}

	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	public MeasureSet getMeasureSet() {
		if(measureSet == null) {
			measureSet= new MeasureSet();
		}
		return measureSet;
	}

	public void setMeasureSet(MeasureSet measureSet) {
		this.measureSet = measureSet;
	}

	public Timestamp getFinalizedDate() {
		return finalizedDate;
	}

	public void setFinalizedDate(Timestamp finalizedDate) {
		this.finalizedDate = finalizedDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getaBBRName() {
		return aBBRName;
	}

	public void setaBBRName(String aBBRName) {
		this.aBBRName = aBBRName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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

	public String getMinorVersionStr(){
		return getMinorVersionInt() + "";
	}

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

	public String getMajorVersionStr(){
		return getMajorVersionInt() + "";
	}

	public double getVersionNumber(){
		double versionNumber = 0;
		if ((version != null) && !version.isEmpty()) {
			versionNumber = Double.valueOf(version).doubleValue();
		}
		return versionNumber;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMeasureScoring() {
		return measureScoring;
	}

	public void setMeasureScoring(String measureScoring) {
		this.measureScoring = measureScoring;
	}

	public User getOwner() {
		return owner;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<MeasureShare> getShares() {
		return shares;
	}

	public void setShares(Set<MeasureShare> shares) {
		this.shares = shares;
	}

	public Date getExportedDate() {
		return exportedDate;
	}

	public void setExportedDate(Date exportedDate) {
		this.exportedDate = exportedDate;
	}

	public Timestamp getLockedOutDate() {
		return lockedOutDate;
	}

	public void setLockedOutDate(Timestamp lockedOutDate) {
		this.lockedOutDate = lockedOutDate;
	}

	public User getLockedUser() {
		return lockedUser;
	}
	
	public void setLockedUser(User lockedUser) {
		this.lockedUser = lockedUser;
	}

	public int geteMeasureId() {
		return eMeasureId;
	}

	public void seteMeasureId(int eMeasureId) {
		this.eMeasureId = eMeasureId;
	}
	
	public boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getRevisionNumber() {
		return revisionNumber;
	}
	
	public void setRevisionNumber(String revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public String getReleaseVersion() {
		return releaseVersion;
	}

	public void setReleaseVersion(String releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

	public boolean isPatientBased() {
		return isPatientBased;
	}

	public void setPatientBased(boolean isPatientBased) {
		this.isPatientBased = isPatientBased;
	}

	public Timestamp getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Timestamp lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}
