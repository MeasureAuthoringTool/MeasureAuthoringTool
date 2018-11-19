package mat.client.measure.measuredetails.components;

public class MeasurePopulationExclusionsModel implements MeasureDetailsComponentModel{
	private String measurePopulationExclusions;

	public String getMeasurePopulationExclusions() {
		return measurePopulationExclusions;
	}

	public void setMeasurePopulationExclusions(String measurePopulationExclusions) {
		this.measurePopulationExclusions = measurePopulationExclusions;
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
