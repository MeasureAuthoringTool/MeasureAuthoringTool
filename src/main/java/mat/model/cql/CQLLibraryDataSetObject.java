package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.BaseModel;
import mat.model.LockedUserInfo;
import mat.model.clause.ModelTypeHelper;
import mat.shared.CQLError;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CQLLibraryDataSetObject implements IsSerializable, BaseModel {
    private String id;
    private String cqlName;
    private String version = "0.0";
    private boolean isDraft;
    private Timestamp finalizedDate;
    private String measureSetId;
    private boolean isLocked;
    private LockedUserInfo lockedUserInfo;
    private String ownerEmailAddress;
    private boolean isFamily;
    private String releaseVersion;
    private String qdmVersion;
    private String fhirVersion;
    private String ownerFirstName;
    private String ownerLastName;
    private String cqlText;
    private String measureId;
    private boolean isSelected;
    private CQLModel cqlModel;
    private boolean isSharable;
    private String revisionNumber = "000";
    private String ownerId;
    private String cqlSetId;
    private boolean isEditable;
    private boolean isTransferable;
    private boolean isDraftable;
    private boolean isVersionable;
    private boolean isDeletable;
    private String libraryModelType;
    private boolean fhirConvertible;
    private transient int clickCount;

    private List<CQLError> cqlErrors = new ArrayList<>();

    public boolean isFhir() {
        return ModelTypeHelper.isFhir(libraryModelType);
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public LockedUserInfo getLockedUserInfo() {
        return lockedUserInfo;
    }

    public String getLockedUserId(LockedUserInfo info) {
        if (info != null)
            return info.getUserId();
        else
            return "";
    }

    public String getLockedUserEmail(LockedUserInfo info) {
        if (info != null)
            return info.getEmailAddress();
        else
            return "";
    }

    public String getLockedUserName(LockedUserInfo info) {
        if (info != null)
            return info.getFirstName() + " " + info.getLastName();
        else
            return "";
    }

    public void setLockedUserInfo(LockedUserInfo lockedUserInfo) {
        this.lockedUserInfo = lockedUserInfo;
    }

    public boolean isFamily() {
        return isFamily;
    }

    public void setFamily(boolean isFamily) {
        this.isFamily = isFamily;
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

    public String getFhirVersion() {
        return fhirVersion;
    }

    public void setFhirVersion(String fhirVersion) {
        this.fhirVersion = fhirVersion;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean isDraft) {
        this.isDraft = isDraft;
    }

    public Timestamp getFinalizedDate() {
        return finalizedDate;
    }

    public void setFinalizedDate(Timestamp finalizedDate) {
        this.finalizedDate = finalizedDate;
    }

    public String getMeasureSetId() {
        return measureSetId;
    }

    public void setMeasureSetId(String measureSetId) {
        this.measureSetId = measureSetId;
    }

    public String getOwnerEmailAddress() {
        return ownerEmailAddress;
    }

    public void setOwnerEmailAddress(String ownerEmailAddress) {
        this.ownerEmailAddress = ownerEmailAddress;
    }

    public String getCqlText() {
        return cqlText;
    }

    public void setCqlText(String cqlText) {
        this.cqlText = cqlText;
    }

    public String getCqlName() {
        return cqlName;
    }

    public void setCqlName(String cqlName) {
        this.cqlName = cqlName;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public CQLModel getCqlModel() {
        return cqlModel;
    }

    public void setCqlModel(CQLModel cqlModel) {
        this.cqlModel = cqlModel;
    }

    public List<CQLError> getCqlErrors() {
        return cqlErrors;
    }

    public void setCqlErrors(List<CQLError> cqlErrors) {
        this.cqlErrors = cqlErrors;
    }

    public boolean isSharable() {
        return isSharable;
    }

    public void setSharable(boolean isSharable) {
        this.isSharable = isSharable;
    }

    public String getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(String revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCqlSetId() {
        return cqlSetId;
    }

    public void setCqlSetId(String cqlSetId) {
        this.cqlSetId = cqlSetId;
    }

    @Override
    public void scrubForMarkUp() {
        String markupRegExp = "<[^>]+>";
        if (this.getCqlName() != null) {
            String noMarkupText = this.getCqlName().trim().replaceAll(markupRegExp, "");
            System.out.println("CQL Library name:" + noMarkupText);
            if (this.getCqlName().trim().length() > noMarkupText.length()) {
                this.setCqlName(noMarkupText);
            }
        }
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public boolean isTransferable() {
        return isTransferable;
    }

    public void setTransferable(boolean isTransferable) {
        this.isTransferable = isTransferable;
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

    public boolean isDeletable() {
        return isDeletable;
    }

    public void setDeletable(boolean isDeletable) {
        this.isDeletable = isDeletable;
    }

    public String getLibraryModelType() {
        return libraryModelType;
    }

    public void setLibraryModelType(String libraryModelType) {
        this.libraryModelType = libraryModelType;
    }

    public boolean isFhirConvertible() {
        return fhirConvertible;
    }

    public void setFhirConvertible(boolean fhirConvertible) {
        this.fhirConvertible = fhirConvertible;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }
}
