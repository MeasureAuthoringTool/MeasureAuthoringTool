package mat.client.expressionbuilder.constant;

public enum OperatorType implements ExpressionBuilderType {
		
	AND("And"),
	OR("Or"),
	INTERSECT("Intersect"),
	UNION("Union"),
	EXCEPT("Except");
	
	private String displayName;
	
	OperatorType(String displayName) {
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
