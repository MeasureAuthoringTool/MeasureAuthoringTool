package mat.client.measure.service;

import mat.client.shared.GenericResult;
import mat.shared.ConstantMessages;

public class SaveCQLLibraryResult extends GenericResult {
	/** The id. */
	private String id;
	
	private String cqlLibraryName;
	
	/** The version str. */
	private String versionStr;
	
	public static final int INVALID_DATA = ConstantMessages.INVALID_DATA;
	public static final int INVALID_USER = 1;
	public static final int INVALID_CQL = 2;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersionStr() {
		return versionStr;
	}

	public void setVersionStr(String versionStr) {
		this.versionStr = versionStr;
	}

	public String getCqlLibraryName() {
		return cqlLibraryName;
	}

	public void setCqlLibraryName(String cqlLibraryName) {
		this.cqlLibraryName = cqlLibraryName;
	}
	
	
}
