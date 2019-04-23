package mat.client.expressionbuilder.constant;

public enum ComparisonOperatorType {
	
	EQUALS("= (equals)", "="),
	NOT_EQUALS("!= (not equals)", "!="),
	EQUIVALENT("~ (equivalent)", "~"),
	NOT_EQUIVALENT("!~ (not equivalent)", "!~"),
	GREATER_THAN("> (greater than)", ">"),
	LESS_THAN("< (less than)", "<"),
	GREATER_THAN_OR_EQUAL_TO(">= (greater than or equal to)", ">="),
	LESS_THAN_OR_EQUAL_TO("<= (less than or equal to)", "<=");
	
	private String displayName;
	private String value; 
	
	ComparisonOperatorType(String displayName, String value) {
		this.displayName = displayName;
		this.value = value;
	}
	
	public String getDisplay() {
		return this.displayName;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String getTitle() {
		return this.displayName;
	}
}
