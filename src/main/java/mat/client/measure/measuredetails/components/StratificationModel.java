package mat.client.measure.measuredetails.components;

public class StratificationModel implements MeasureDetailsComponentModel{
	private String stratification;

	public String getStratification() {
		return stratification;
	}

	public void setStratification(String stratification) {
		this.stratification = stratification;
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
