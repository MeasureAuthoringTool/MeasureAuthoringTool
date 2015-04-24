package mat.client.measurepackage;

// TODO: Auto-generated Javadoc
/**
 * The Enum MeasureGroupingOrder.
 */
public enum MeasureGroupingOrder {
	
	/** The initial patient population. */
	initialPopulation("1"),
	
	/** The denominator. */
	denominator("2"),
	
	/** The denominator exclusions. */
	denominatorExclusions("3"),
	
	/** The denominator exceptions. */
	denominatorExceptions("4"),
	
	/** The numerator. */
	numerator("5"),
	
	/** The numerator exclusions. */
	numeratorExclusions("6"),
	
	/** The measure population. */
	measurePopulation("7"),
	
	/** The measure population exclusions. */
	measurePopulationExclusions("8"),
	
	/** The measure observation. */
	measureObservation("9"),
	
	/** The stratification. */
	stratification("10");
	
	
	/** The status code. */
	private String statusCode;
	
	/**
	 * Instantiates a new measure grouping order.
	 * 
	 * @param status
	 *            the status
	 */
	private MeasureGroupingOrder(String status) {
		statusCode = status;
	}
	
	/**
	 * Gets the status code.
	 * 
	 * @return the status code
	 */
	public String getStatusCode() {
		return statusCode;
	}
	
}
