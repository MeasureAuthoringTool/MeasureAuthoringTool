package mat.model.cql.parser;

public class CQLCodeSystemModelObject extends CQLBaseModelDefinitionObject {
	
	private String identifier;
	private String codeSystemId;
	private String versionSpecifier;
	
	public String getCodeSystemId() {
		return codeSystemId;
	}
	
	public void setCodeSystemId(String codeSystemId) {
		this.codeSystemId = codeSystemId;
	}

	public String getVersionSpecifier() {
		return versionSpecifier;
	}

	public void setVersionSpecifier(String versionSpecifier) {
		this.versionSpecifier = versionSpecifier;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
