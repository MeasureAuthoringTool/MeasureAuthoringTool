package mat.client.measure.measuredetails.components;

public class RiskAdjustmentModel implements MeasureDetailsComponentModel{
	private String riskAdjustment;

	public String getRiskAdjustment() {
		return riskAdjustment;
	}

	public void setRiskAdjustment(String riskAdjustment) {
		this.riskAdjustment = riskAdjustment;
	}

	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
}
