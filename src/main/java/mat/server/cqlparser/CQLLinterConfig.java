package mat.server.cqlparser;

public class CQLLinterConfig {

	private String libraryName;
	private String libraryVersion;
	
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
}
