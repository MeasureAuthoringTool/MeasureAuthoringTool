package mat.model.cql;

public class CQLParameterModelObject extends CQLBaseModelDefinitionObject {
	
	private String id;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	/**
	 * Override this to do nothing, since Parameter definitions dont have a 'version' 
	 * attribute.
	 */
	public String getVersion() {
		return "";
	}
	
	@Override
	/**
	 * Override this to do nothing, since Parameter definitions dont have a 'version' 
	 * attribute.
	 */
	public void setVersion(String version) {
	}

}
