package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLDefinition implements IsSerializable{
	private String id;
	private String definitionName;
	private String definitionLogic;
	private String context;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDefinitionName() {
		return definitionName;
	}
	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}
	public String getDefinitionLogic() {
		return definitionLogic;
	}
	public void setDefinitionLogic(String definitionLogic) {
		this.definitionLogic = definitionLogic;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
}
