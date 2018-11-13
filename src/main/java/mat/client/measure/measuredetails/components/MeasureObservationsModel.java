package mat.client.measure.measuredetails.components;

public class MeasureObservationsModel implements MeasureDetailsComponentModel{
	private String measureObservations;

	public String getMeasureObservations() {
		return measureObservations;
	}

	public void setMeasureObservations(String measureObservations) {
		this.measureObservations = measureObservations;
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
