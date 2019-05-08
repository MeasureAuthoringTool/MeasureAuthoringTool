package mat.client.expressionbuilder.constant;

import java.util.HashMap;
import java.util.Map;

public enum ExpressionBuilderUserAssistText {

	ATTRIBUTE("Attribute", "This expression type is used when needing to return an attribute’s value(s) for a QDM Element(s). For Example: \"First Inpatient Intensive Care Unit\"(Encounter).locationPeriod"),

	COMPARISON("Comparison", "A Comparison takes two expressions and evaluates them against each other. For Example: AgeInYearsAt (start of \"Measurement Period\") > 18, is a function (AgeInYearsAt (start of \"Measurement Period)) compared to a quantity (18) to identify if the function returns a number greater than the quantity(>)."),

	COMPUTATION("Computation", "A Computation performs a mathematical calculation between two expression types. For Example: \"Measurement Period\" - 5 years takes the parameter expression (\"Measurement Period\") and subtracts {-} a quantity (5 years)."),

	RETRIEVE("Data Element or Retrieve", "A Retrieve is simply a call for a datatype. For Example: Encounter, Performed. A Data Element is when you connect that datatype to a value set or directly referenced code within your measure. For Example: [\"Encounter, Performed\": \"Office Visit\"]  where Encounter, Performed is the datatype and \"Office Visit\" is the value set chosen to go with it."),

	DATE_TIME("Date/Time", "Date / Time is used to indicate a date, a time, or a date and time combination. For Example: @2019-02-28T13:35:00.000"),

	EXISTS("Exists", "An Exists statement is used when you want to take an expression that returns a list and make it return a true or false instead. For Example:  [Encounter, Performed] will return a list of all of the encounters; whereas, exists [Encounter, Performed] will tell you if there was an encounter performed."),

	FUNCTION("Function", "A function statement is used when needing to return the results of a calculation. For Example: AgeInYearsAt (start of \"Measurement Period\")"),

	INTERVAL("Interval", "An Interval is a way to create a range of things, such as a range of dates or quantities. For Example: Interval[3 'mg/dL', 6 'mg/dL']"), 

	IN("Membership (In)", "A Membership statement will help you to determine if one item can be found within another item. For Example: \"First Procedure In Year\".code in \"Laparoscopy\""),

	NOT("Negation (not)", "A Negation statement will let you express that you are looking to see if something is not present. For Example: not \"Last Blood Pressure Within One Year High\""),

	IS_NULL_NOT_NULL("Null (is null/not null)", "A Null statement allows you to check if something is or is not empty or null. For Example: \"Most Recent Prostate Cancer Staging Tumor Size T1a to T2a\" is not null"),

	QUANTITY("Quantity", "A quantity is an integer or a decimal and can be followed by a unit if desired. For Example 5mg/dL or 3 days"),

	QUERY("Query", ""),
	
	QUERY_SOURCE("Source", "When building a query you first need to designate what data you want to start with. This is your query source. This source also needs to have an alias assigned that will allow you refer back to the source within the rest of the query. For Example: [Encounter, Performed : \"Office Visit\"] visit."),

	QUERY_FILTER("Filter", "The Filter statement (also known as the where statement) in the query is a way for you to narrow down the results returned from the source data. For Example: where visit.relevantPeriod starts during \"Measurement Period\"."),

	QUERY_SORT("Sort (Optional)", "A Query Sort indicates in what order you would like the data from your query returned. For Example: sort by start of relevantPeriod asc"),

	QUERY_REVIEW("Review Query", "This screen gives you one last opportunity to review the query you have built before applying it into your expression. Clicking on the Apply button will accept this query and place it into your expression. Clicking on the Exit Query button here will cancel everything that has been built in this query and take you back to the screen you were on before choosing Query."),

	TIME_BOUNDARY("Start of / End of", "A Start of / End of statement is used when you need to reference either the beginning or the end of a period or interval. For Example: start of \"Measurement Period\""),

	TIMING("Timing", "A timing is built with two expressions and a timing phrase between them. Example: [first expression] starts before start of [second expression]."),

	TIMING_PHRASE("Timing Phrase", "The timing phrase screen will continuously show you options based on previous choices made. This allows you to either use a simple timing such as includes or to continue to add and end up with includes year of start of."),

	IS_TRUE_FALSE("True/False (is true/false)", "Use a True/False statement when you want to indicate if something is accurate or not accurate. For Example: \"Refused Medical Attention\" is false");

	private static final Map<String, ExpressionBuilderUserAssistText> labelIndex = new HashMap<>(ExpressionBuilderUserAssistText.values().length);
	static {
		for (final ExpressionBuilderUserAssistText myEnum : values()) {
			labelIndex.put(myEnum.getName(), myEnum);
		}
	}

	private String title;

	private String label;

	private ExpressionBuilderUserAssistText(String name, String value) {
		title = name;
		label = value;
	}

	public String getName() {
		return title;
	}

	public String getValue() {
		return label;
	}

	public static ExpressionBuilderUserAssistText getEnumByTitle(String name) {
		return labelIndex.get(name);
	}
	
	public static boolean isKeyPresent(String title) {
		return labelIndex.containsKey(title);
	}
}
