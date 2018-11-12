package mat.client.measure.measuredetails.components;

public class DenominatorModel implements MeasureDetailsComponentModel{
	private String denominator;

	public String getDenominator() {
		return denominator;
	}

	public void setDenominator(String denominator) {
		this.denominator = denominator;
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
