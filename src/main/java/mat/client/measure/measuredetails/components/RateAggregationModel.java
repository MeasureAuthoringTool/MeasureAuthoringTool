package mat.client.measure.measuredetails.components;

public class RateAggregationModel implements MeasureDetailsComponentModel{
	private String rateAggregation;

	public String getRateAggregation() {
		return rateAggregation;
	}

	public void setRateAggregation(String rateAggregation) {
		this.rateAggregation = rateAggregation;
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
