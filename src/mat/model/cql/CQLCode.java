package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLCodeSystem.
 */
public class CQLCode implements IsSerializable {

	/** The id. */
	private String id;
	
	/** The code system. */
	private String codeName;
	
	/** The code system name. */
	private String codeSystemName;
	
	/** The code system version. */
	private String codeSystemVersion;
	
	/** The OID. */
	private String codeOID;
	
	/** The Display Name. */
	private String displayName;
	
	private String codeIdentifier;

	private boolean isUsed;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the code name.
	 *
	 * @return the code name
	 */
	public String getCodeName() {
		return codeName;
	}

	/**
	 * Sets the code name.
	 *
	 * @param codeName the new code system
	 */
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	/**
	 * Gets the code system name.
	 *
	 * @return the code system name
	 */
	public String getCodeSystemName() {
		return codeSystemName;
	}

	/**
	 * Sets the code system name.
	 *
	 * @param codeSystemName the new code system name
	 */
	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}

	/**
	 * Gets the code system version.
	 *
	 * @return the code system version
	 */
	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}

	/**
	 * Sets the code system version.
	 *
	 * @param codeSystemVersion the new code system version
	 */
	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}

	public String getCodeOID() {
		return codeOID;
	}

	public void setCodeOID(String codeOID) {
		this.codeOID = codeOID;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public String getCodeIdentifier() {
		return codeIdentifier;
	}

	public void setCodeIdentifier(String codeIdentifier) {
		this.codeIdentifier = codeIdentifier;
	}

	
	
}
