package mat.model.cql;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.LockedUserInfo;
import mat.shared.CQLErrors;

public class CQLLibraryDataSetObject implements IsSerializable{
	private String id;
	private String cqlName;
	private String version;
	private boolean isDraft;
	private Timestamp finalizedDate;
	private String measureSetId;
	private boolean isLocked;
	private LockedUserInfo lockedUserInfo;
	private String ownerEmailAddress;
	private boolean isFamily;
	private String releaseVersion;
	private String ownerFirstName;
	private String ownerLastName;
	private String cqlText;
	private String measureId;
	private boolean isSelected;
	private CQLModel cqlModel;
	/** The cql errors. */
	private List<CQLErrors> cqlErrors = new ArrayList<CQLErrors>();
	
	public boolean isLocked() {
		return isLocked;
	}
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	public LockedUserInfo getLockedUserInfo() {
		return lockedUserInfo;
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
	public List<CQLErrors> getCqlErrors() {
		return cqlErrors;
	}
	public void setCqlErrors(List<CQLErrors> cqlErrors) {
		this.cqlErrors = cqlErrors;
	}
}
