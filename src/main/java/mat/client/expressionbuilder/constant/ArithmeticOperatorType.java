package mat.client.expressionbuilder.constant;

public enum ArithmeticOperatorType {
	
	ADDITION("+ (add)", "+"),
	SUBTRACTION("- (subtract)", "-"),
	MULTIPLICATION("* (times)", "*"),
	DIVISION("/ (divide by)", "/"),
	EXPONENT("^ (to the power of)", "^"),
	TRUNCATED_DIVISION("div (truncated divide by)", "div"),
	MODULO("mod (the remainder of)", "mod");
	
	private String displayName;
	private String value; 
	
	ArithmeticOperatorType(String displayName, String value) {
		this.displayName = displayName;
		this.value = value;
	}
	
	public String getDisplay() {
		return displayName;
	}
	
	public String getValue() {
		return value;
	}

}
