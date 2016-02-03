package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLBaseModelObject implements IsSerializable {
	
	private String identifier;
	private String version;
	
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
}
