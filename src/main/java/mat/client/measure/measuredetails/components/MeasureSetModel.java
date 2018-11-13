package mat.client.measure.measuredetails.components;

public class MeasureSetModel implements MeasureDetailsComponentModel{
	private String measureSet;

	public String getMeasureSet() {
		return measureSet;
	}

	public void setMeasureSet(String measureSet) {
		this.measureSet = measureSet;
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
