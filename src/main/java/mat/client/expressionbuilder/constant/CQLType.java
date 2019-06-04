package mat.client.expressionbuilder.constant;

public enum CQLType {
	ANY("Any"),
	LIST("List"),
	CODE("Code"),
	BOOLEAN("Boolean"),
	QUANTITY("Quantity"),
	DECIMAL("Decimal"),
	INTEGER("Integer"),
	STRING("String"),
	INTERVAL("Interval"),
	INTERVAL_NUMBER("Interval<Integer>"),
	INTERVAL_DECIMAL("Interval<Decimal>"),
	INTERVAL_DATETIME("Interval<DateTime>"),
	INTERVAL_DATE("Interval<Date>"),
	INTERVAL_TIME("Interval<Time>"),
	INTERVAL_QUANTITY("Interval<Quantity>"),
	TIME("Time"),
	DATE("Date"),
	DATETIME("DateTime"),
	RATIO("Ratio"),
	QDM("QDM"),
	LIST_QDM("List<QDM>"), 
	LIST_CODE("List<Code>"), 
	LIST_BOOLEAN("List<Boolean>"),
	LIST_DATE("List<Date>"),
	LIST_DATETIME("List<DateTime>"),
	LIST_DECIMAL("List<Decimal>"),
	LIST_INTEGER("List<Integer>"),
	LIST_QUANTITY("List<Quantity>"),
	LIST_STRING("List<String>"),
	LIST_TIME("List<Time>");
	
	private String name;

	CQLType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
