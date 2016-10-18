package mat.model.cql.parser;

public class CQLCodeModelObject extends CQLValueSetModelObject {
	
	private String codeIdentifier;
	private String codeId;
	private String codeSystemIdentifier;
	private String displayClause;
	
	private CQLCodeSystemModelObject cqlCodeSystemModelObject;
	
	
	public String getCodeIdentifier() {
		return codeIdentifier;
	}
	public void setCodeIdentifier(String codeIdentifier) {
		this.codeIdentifier = codeIdentifier;
	}
	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
	public String getCodeSystemIdentifier() {
		return codeSystemIdentifier;
	}
	public void setCodeSystemIdentifier(String codeSystemIdentifier) {
		this.codeSystemIdentifier = codeSystemIdentifier;
	}
	public String getDisplayClause() {
		return displayClause;
	}
	public void setDisplayClause(String displayClause) {
		this.displayClause = displayClause;
	}
	public CQLCodeSystemModelObject getCqlCodeSystemModelObject() {
		return cqlCodeSystemModelObject;
	}
	public void setCqlCodeSystemModelObject(CQLCodeSystemModelObject cqlCodeSystemModelObject) {
		this.cqlCodeSystemModelObject = cqlCodeSystemModelObject;
	}

}
