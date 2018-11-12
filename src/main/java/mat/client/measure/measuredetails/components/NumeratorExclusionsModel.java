package mat.client.measure.measuredetails.components;

public class NumeratorExclusionsModel implements MeasureDetailsComponentModel{
	private String numeratorExclusions;

	public String getNumeratorExclusions() {
		return numeratorExclusions;
	}

	public void setNumeratorExclusions(String numeratorExclusions) {
		this.numeratorExclusions = numeratorExclusions;
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
