package mat.client.measure.measuredetails.components;

public class MeasurePopulationExclusionsModel implements MeasureDetailsComponentModel{
	private String measurePopulation;

	public String getMeasurePopulation() {
		return measurePopulation;
	}

	public void setMeasurePopulation(String measurePopulation) {
		this.measurePopulation = measurePopulation;
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
