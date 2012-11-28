package mat.reportmodel;


@SuppressWarnings("serial")
public class MeasurementTerm extends Decision {
	private String quantity;
	private String unit;
	private String id;
	private String decisionId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDecisionId() {
		return decisionId;
	}

	public void setDecisionId(String decisionId) {
		this.decisionId = decisionId;
	}

	public MeasurementTerm() {
	}
	
	public MeasurementTerm(String quantity, String unit) {
		this.quantity = quantity;
		this.unit = unit;
	}
	
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MeasurementTerm");
		sb.append("\n");
		sb.append(quantity).append(" ").append(unit);
		return sb.toString();
	}	
}
