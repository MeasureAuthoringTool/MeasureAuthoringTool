package mat.model.clause;

import java.sql.Timestamp;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.LockedUserInfo;

public class MeasureShareDTO implements IsSerializable {

    private String userId;

    private String measureId;

    private String measureName;

    private String measureModel;

    private String scoringType;

    private String firstName;

    private String lastName;

    private String shareLevel;

    private String status;

    private String ownerUserId;

    private boolean packaged;

    private boolean locked;

    private String shortName;

    private LockedUserInfo lockedUserInfo;

    private String organizationName;

    private int eMeasureId;

    private boolean draft;

    private Timestamp finalizedDate;

    private String version;

    private String releaseVersion;

    private String qdmVersion;

    private String revisionNumber;

    private String measureSetId;

    private boolean isPrivateMeasure;

    private boolean isDraftable;

    private boolean isVersionable;

    private Boolean isPatientBased;

    public MeasureShareDTO(String measureId, String shareLevel, String ownerUserId) {
        super();
        this.measureId = measureId;
        this.shareLevel = shareLevel;
        this.ownerUserId = ownerUserId;
    }

    public MeasureShareDTO() {

    }

    public String getScoringType() {
        return scoringType;
    }

    public void setScoringType(String scoringType) {
        this.scoringType = scoringType;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShareLevel() {
        return shareLevel;
    }

    public void setShareLevel(String shareLevel) {
        this.shareLevel = shareLevel;
    }

    public String getMeasureModel() {
        return measureModel;
    }

    public void setMeasureModel(String measureModel) {
        this.measureModel = measureModel;
    }

    public String getMeasureName() {
        return measureName;
    }

    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPackaged() {
        return packaged;
    }

    public void setPackaged(boolean packaged) {
        this.packaged = packaged;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public LockedUserInfo getLockedUserInfo() {
        return lockedUserInfo;
    }

    public void setLockedUserInfo(LockedUserInfo lockedUserInfo) {
        this.lockedUserInfo = lockedUserInfo;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReleaseVersion() {
        return releaseVersion;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public String getQdmVersion() {
        return qdmVersion;
    }

    public void setQdmVersion(String qdmVersion) {
        this.qdmVersion = qdmVersion;
    }

    public String getMeasureSetId() {
        return measureSetId;
    }

    public void setMeasureSetId(String measureSetId) {
        this.measureSetId = measureSetId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public boolean isPrivateMeasure() {
        return isPrivateMeasure;
    }

    public void setPrivateMeasure(boolean isPrivateMeasure) {
        this.isPrivateMeasure = isPrivateMeasure;
    }

    public int geteMeasureId() {
        return eMeasureId;
    }

    public void seteMeasureId(int eMeasureId) {
        this.eMeasureId = eMeasureId;
    }

    public String getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(String revisionNumber) {
        this.revisionNumber = revisionNumber;
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

    public Boolean isPatientBased() {
        return isPatientBased;
    }

    public void setPatientBased(Boolean isPatientBased) {
        this.isPatientBased = isPatientBased;
    }

}
