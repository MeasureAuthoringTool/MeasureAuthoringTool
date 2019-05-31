package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLIdentifierObject implements IsSerializable {
	private String aliasName; 
	private String id;
	private String identifier;
	private String returnType;

	public CQLIdentifierObject(String aliasName, String identifier, String id) {
		this.aliasName = aliasName;
		this.identifier = identifier;
		this.id = id;
	}
	
	public CQLIdentifierObject(String aliasName, String identifier) {
		this.aliasName = aliasName;
		this.identifier = identifier;
	}
	
	public CQLIdentifierObject() {
		
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}


	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	} 
	
	public String getDisplay() {
		if(aliasName != null && !aliasName.isEmpty()) {
			return aliasName + "." + identifier;
		} else {
			return identifier;
		}
	}
	
	@Override
	public String toString() {
		if(aliasName != null && !aliasName.isEmpty()) {
			return aliasName + "." + "\"" + identifier + "\"";
		} else {
			return "\"" + identifier + "\"";
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
}
