package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class CQLCode.
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
	
	private String codeSystemOID;
	
	/** The OID. */
	private String codeOID;
	
	/** The Display Name. */
	private String displayName;
	
	
	private String codeIdentifier;

	private boolean isUsed;
	
	private boolean readOnly; 
	
	private String suffix;

	private boolean isCodeSystemVersionIncluded;

	public boolean isIsCodeSystemVersionIncluded() {
		return isCodeSystemVersionIncluded;
	}



	public void setIsCodeSystemVersionIncluded(boolean isCodeSystemVersionIncluded) {
		this.isCodeSystemVersionIncluded = isCodeSystemVersionIncluded;
	}

	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeIdentifier == null) ? 0 : codeIdentifier.hashCode());
		result = prime * result + ((codeName == null) ? 0 : codeName.hashCode());
		result = prime * result + ((codeOID == null) ? 0 : codeOID.hashCode());
		result = prime * result + ((codeSystemName == null) ? 0 : codeSystemName.hashCode());
		result = prime * result + ((codeSystemOID == null) ? 0 : codeSystemOID.hashCode());
		result = prime * result + ((codeSystemVersion == null) ? 0 : codeSystemVersion.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CQLCode other = (CQLCode) obj;
		if (codeIdentifier == null) {
			if (other.codeIdentifier != null)
				return false;
		} else if (!codeIdentifier.equals(other.codeIdentifier))
			return false;
		if (codeName == null) {
			if (other.codeName != null)
				return false;
		} else if (!codeName.equals(other.codeName))
			return false;
		if (codeOID == null) {
			if (other.codeOID != null)
				return false;
		} else if (!codeOID.equals(other.codeOID))
			return false;
		if (codeSystemName == null) {
			if (other.codeSystemName != null)
				return false;
		} else if (!codeSystemName.equals(other.codeSystemName))
			return false;
		if (codeSystemOID == null) {
			if (other.codeSystemOID != null)
				return false;
		} else if (!codeSystemOID.equals(other.codeSystemOID))
			return false;
		if (codeSystemVersion == null) {
			if (other.codeSystemVersion != null)
				return false;
		} else if (!codeSystemVersion.equals(other.codeSystemVersion))
			return false;
		return true;
	}



	public String getCodeIdentifier() {
		return codeIdentifier;
	}

	public void setCodeIdentifier(String codeIdentifier) {
		this.codeIdentifier = codeIdentifier;
	}

	public String getCodeSystemOID() {
		return codeSystemOID;
	}

	public void setCodeSystemOID(String codeSystemOID) {
		this.codeSystemOID = codeSystemOID;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
}
