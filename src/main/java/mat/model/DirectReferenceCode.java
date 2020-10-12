package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.ToString;

// TODO: Auto-generated Javadoc
/**
 * The Class DirectReferenceCode.
 */
@ToString
public class DirectReferenceCode implements IsSerializable{

	/** The code descriptor. */
	private String codeDescriptor;
	
	/** The code system name. */
	private String codeSystemName;
	
	/** The code system version. */
	private String codeSystemVersion;
	
	private String codeSystemOid;
	
	/** The code. */
	private String code;
	
	/**
	 * Gets the code descriptor.
	 *
	 * @return the code descriptor
	 */
	public String getCodeDescriptor() {
		return codeDescriptor;
	}
	
	/**
	 * Sets the code descriptor.
	 *
	 * @param codeDescriptor the new code descriptor
	 */
	public void setCodeDescriptor(String codeDescriptor) {
		this.codeDescriptor = codeDescriptor;
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
	 * @param code the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeSystemOid() {
		return codeSystemOid;
	}

	public void setCodeSystemOid(String codeSystemOid) {
		this.codeSystemOid = codeSystemOid;
	}
	
}
