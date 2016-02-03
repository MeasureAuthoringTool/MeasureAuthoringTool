package mat.model.cql;

public class CQLContextModelObject extends CQLBaseModelObject{
	
	@Override
	/**
	 * Override this to do nothing, since Context definitions don't have a 'version' 
	 * attribute.
	 */
	public String getVersion() {
		return "";
	}
	
	@Override
	/**
	 * Override this to do nothing, since Context definitions don't have a 'version' 
	 * attribute.
	 */
	public void setVersion(String version) {
	}
}
