package mat.client.expressionbuilder.constant;

public enum TimingOperator implements ExpressionBuilderType {
	
	BEGINNING_NODE(null),
	DONE_NODE(null),
	STARTS("starts"),
	ENDS("ends"),
	OCCURS("occurs"),
	SAME("same"),
	PROPERLY("properly"),
	INCLUDES("includes"),
	AS("as"),
	START_OF("start of"),
	END_OF("end of"),
	DURING("during"),
	INCLUDED_IN("included in"),
	WITHIN("within"),
	MEETS("meets"),
	BEFORE("before"),
	AFTER("after"),
	
	// DATE TIME PRECISION
	YEAR("year"),
	MONTH("month"),
	WEEK("week"),
	DAY("day"),
	HOUR("hour"),
	MINUTE("minute"),
	SECOND("second"),
	MILISECOND("millisecond"),
	
	// DATE TIME PRECISION SPECIFIER
	YEAR_OF("year of"),
	MONTH_OF("month of"),
	WEEK_OF("week of"),
	DAY_OF("day of"),
	HOUR_OF("hour of"),
	MINUTE_OF("minute of"),
	SECOND_OF("second of"),
	MILISECOND_OF("millisecond of"),
	
	// relative qualifier
	OR_BEFORE("or before"),
	OR_AFTER("or after"),
	
	// offset relative qualifier
	OR_MORE("or more"),
	OR_LESS("or less"),
	
	// temporal relationship
	ON_OR_BEFORE("on or before"),
	ON_OR_AFTER("on or after"),
	BEFORE_OR_ON("before or on"),
	AFTER_OR_ON("after or on"),
	
	QUANTITY_OF("(quantity) of"),
	OVERLAPS("overlaps"),
	
	// quantity offset
	QUANTITY_OR_MORE("(quantity) or more"),
	QUANTITY_OR_LESS("(quantity) or less"),
	MORE_THAN_QUANTITY("more than (quantity)"),
	LESS_THAN_QUANTITY("less than (quantity)");


	private String displayName;

	private TimingOperator(String displayName) {
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
