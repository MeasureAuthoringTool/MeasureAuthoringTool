package mat.server.cqlparser;

import mat.model.cql.CQLModel;

public class CQLLinterConfig {

	private String libraryName;
	private String libraryVersion;
	private String modelIdentifier;
	private String modelVersion;
	
	private CQLModel previousCQLModel;
	
	public CQLLinterConfig() {
		
	}
	
	public CQLLinterConfig(String libraryName, String libraryVersion, String modelIdentifier, String modelVersion) {
		super();
		this.libraryName = libraryName;
		this.libraryVersion = libraryVersion;
		this.modelIdentifier = modelIdentifier;
		this.modelVersion = modelVersion;
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

	public String getModelIdentifier() {
		return modelIdentifier;
	}

	public void setModelIdentifier(String modelIdentifier) {
		this.modelIdentifier = modelIdentifier;
	}

	public String getModelVersion() {
		return modelVersion;
	}

	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}

	public CQLModel getPreviousCQLModel() {
		return previousCQLModel;
	}

	public void setPreviousCQLModel(CQLModel previousCQLModel) {
		this.previousCQLModel = previousCQLModel;
	}
}
