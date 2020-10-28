package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLCodeSystem.
 */
public class CQLCodeSystem implements IsSerializable {

	/** The id. */
	private String id;
	
	/** The code system. */
	private String codeSystem;
	
	/** The code system name. */
	private String codeSystemName;
	
	/** The code system version. */
	private String codeSystemVersion;
	
	/** The value set OID. */
	private String valueSetOID;

	/**
	 * stores off the version uri. example:
	 * codesystem "SNOMEDCT:2017-09": 'http://snomed.info/sct/731000124108' version 'http://snomed.info/sct/731000124108/version/201709'
	 */
	 private String versionUri;

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
	 * Gets the code system.
	 *
	 * @return the code system
	 */
	public String getCodeSystem() {
		return codeSystem;
	}

	/**
	 * Sets the code system.
	 *
	 * @param codeSystem the new code system
	 */
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
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

	public String getVersionUri() {
		return versionUri;
	}

	public void setVersionUri(String versionUri) {
		this.versionUri = versionUri;
	}

	public String getValueSetOID() {
		return valueSetOID;
	}

	public void setValueSetOID(String valueSetOID) {
		this.valueSetOID = valueSetOID;
	}
}
