/**
 * This class is supposed to be a superclass for parameter,codesystem & valueset definitions.
 */
package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLBaseModelDefinitionObject extends CQLBaseModelObject implements IsSerializable {
	
	private String accessModifier = "";
	private String typeSpecifier = "";
	private String defaultExpression = "";
	

	public String getAccessModifier() {
		return accessModifier;
	}

	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}

	public String getTypeSpecifier() {
		return typeSpecifier;
	}

	public void setTypeSpecifier(String typeSpecifier) {
		this.typeSpecifier = typeSpecifier;
	}

	public String getDefaultExpression() {
		return defaultExpression;
	}

	public void setDefaultExpression(String defaultExpression) {
		this.defaultExpression = defaultExpression;
	}
	
	
	
}
