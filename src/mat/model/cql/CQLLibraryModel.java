package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLLibraryModel implements IsSerializable{
	private String libraryName;
	private String versionUsed;
	private String aliasName;
	private boolean isInclude;
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

}
