package mat.client.expressionbuilder.constant;

public enum ExpressionType implements ExpressionBuilderType {

	RETRIEVE("Data element or Retrieve");
	
	private String displayName;
	
	ExpressionType(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public String getDisplayName() {
		return this.displayName;
	}
	
	@Override
	public String getValue() {
		return this.toString();
	}
}
