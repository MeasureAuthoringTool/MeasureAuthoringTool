package mat.reportmodel;


public class AttributeDetails {

	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String name) {
		this.attrName = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeSystem() {
		return codeSystem;
	}
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}
	public String getCodeSystemName() {
		return codeSystemName;
	}
	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String id;
	private String attrName;
	private String code;
	private String codeSystem;
	private String codeSystemName;
	private String mode;
	private String typeCode;
	
}
