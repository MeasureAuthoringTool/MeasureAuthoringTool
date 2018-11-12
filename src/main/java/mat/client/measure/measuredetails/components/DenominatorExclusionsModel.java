package mat.client.measure.measuredetails.components;

public class DenominatorExclusionsModel implements MeasureDetailsComponentModel{
	private String denominatorExclusion;

	public String getDenominatorExclusion() {
		return denominatorExclusion;
	}

	public void setDenominatorExclusion(String denominatorExclusion) {
		this.denominatorExclusion = denominatorExclusion;
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
