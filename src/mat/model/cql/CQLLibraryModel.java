package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.LockedUserInfo;

public class CQLLibraryModel implements IsSerializable{
	private String libraryName;
	private String versionUsed;
	private String aliasName;
	private boolean isInclude;
	private String id;
	private boolean isLocked;
	private LockedUserInfo lockedUserInfo;
	private String setId;
	private boolean isFamily;
	private String releaseVersion;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLibraryName() {
		return libraryName;
	}
	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}
	public String getVersionUsed() {
		return versionUsed;
	}
	public void setVersionUsed(String versionUsed) {
		this.versionUsed = versionUsed;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public boolean isInclude() {
		return isInclude;
	}
	public void setInclude(boolean isInclude) {
		this.isInclude = isInclude;
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
	public void setLockedUserInfo(LockedUserInfo lockedUserInfo) {
		this.lockedUserInfo = lockedUserInfo;
	}
	public String getSetId() {
		return setId;
	}
	public void setSetId(String setId) {
		this.setId = setId;
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

}
