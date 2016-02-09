package mat.model.cql;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLDefinitionsWrapper implements IsSerializable{

	
	private List<CQLDefinition> cqlDefinitions;

	public List<CQLDefinition> getCqlDefinitions() {
		return cqlDefinitions;
	}

	public void setCqlDefinitions(List<CQLDefinition> cqlDefinitions) {
		this.cqlDefinitions = cqlDefinitions;
	}
}
