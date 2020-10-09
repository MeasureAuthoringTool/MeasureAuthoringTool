package mat.vsac.model;


import java.io.Serializable;

/**
 * The Class MatConcept.
 */
public class MatConcept  implements Serializable {

	/** The code. */
	private String code;
	
	/** The code system. */
	private String codeSystem;
	
	/** The code system name. */
	private String codeSystemName;
	
	/** The code system version. */
	private String codeSystemVersion;
	
	/** The display name. */
	private String displayName;

	public String createKey() {
		return codeSystem + "-" + codeSystemVersion;
	}
	
	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Sets the code.
	 * 
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @param codeSystem
	 *            the new code system
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
	 * @param codeSystemName
	 *            the new code system name
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
	 * @param codeSystemVersion
	 *            the new code system version
	 */
	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}
	
	/**
	 * Gets the display name.
	 * 
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Sets the display name.
	 * 
	 * @param displayName
	 *            the new display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
