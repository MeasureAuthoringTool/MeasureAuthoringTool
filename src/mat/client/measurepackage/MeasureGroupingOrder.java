package mat.client.measurepackage;

public enum MeasureGroupingOrder {
	initialPatientPopulation("1"), 
	denominator("2"), 
	denominatorExclusions("3"),
	denominatorExceptions("4"), 
	numerator("5"),
	numeratorExclusions("6"),
	measurePopulation("7"),
	measureObservation("8");
	 
	private String statusCode;
 
	private MeasureGroupingOrder(String status) {
		statusCode = status;
	}
 
	public String getStatusCode() {
		return statusCode;
	}
 
}
