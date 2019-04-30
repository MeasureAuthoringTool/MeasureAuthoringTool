package mat.server.cqlparser;

import mat.model.cql.CQLModel;

public class CQLLinterConfig {

	private String libraryName;
	private String libraryVersion;
	private String qdmVersion;
	
	private CQLModel previousCQLModel;
	
	public CQLLinterConfig() {
		
	}

	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

	public String getLibraryVersion() {
		return libraryVersion;
	}

	public void setLibraryVersion(String version) {
		this.libraryVersion = version;
	}

	public String getQdmVersion() {
		return qdmVersion;
	}

	public void setQdmVersion(String qdmVersion) {
		this.qdmVersion = qdmVersion;
	}

	public CQLModel getPreviousCQLModel() {
		return previousCQLModel;
	}

	public void setPreviousCQLModel(CQLModel previousCQLModel) {
		this.previousCQLModel = previousCQLModel;
	}
}
