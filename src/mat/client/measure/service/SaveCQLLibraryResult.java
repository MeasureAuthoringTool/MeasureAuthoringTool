package mat.client.measure.service;

import java.util.List;

import mat.client.shared.GenericResult;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.ConstantMessages;

public class SaveCQLLibraryResult extends GenericResult {
	/** The id. */
	private String id;
	
	private String cqlLibraryName;
	
	/** The version str. */
	private String versionStr;
	
	private boolean isEditable;
	
	private boolean isLocked;
	
	public static final int INVALID_DATA = ConstantMessages.INVALID_DATA;
	public static final int INVALID_USER = 1;
	public static final int INVALID_CQL = 2;
	
	/** The Constant REACHED_MAXIMUM_VERSION. */
	public static final int REACHED_MAXIMUM_VERSION = ConstantMessages.REACHED_MAXIMUM_VERSION;
	
	/** The Constant REACHED_MAXIMUM_MAJOR_VERSION. */
	public static final int REACHED_MAXIMUM_MAJOR_VERSION = ConstantMessages.REACHED_MAXIMUM_MAJOR_VERSION;
	
	/** The Constant REACHED_MAXIMUM_MINOR_VERSION. */
	public static final int REACHED_MAXIMUM_MINOR_VERSION = ConstantMessages.REACHED_MAXIMUM_MINOR_VERSION;
	
	private List<CQLLibraryDataSetObject> cqlLibraryDataSetObjects;
	
	/** The results total. */
	private int resultsTotal;

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

	public List<CQLLibraryDataSetObject> getCqlLibraryDataSetObjects() {
		return cqlLibraryDataSetObjects;
	}

	public void setCqlLibraryDataSetObjects(List<CQLLibraryDataSetObject> cqlLibraryDataSetObjects) {
		this.cqlLibraryDataSetObjects = cqlLibraryDataSetObjects;
	}

	public int getResultsTotal() {
		return resultsTotal;
	}

	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	
}
