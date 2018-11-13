package mat.client.measure.measuredetails.components;

public class DenominatorExceptionsModel implements MeasureDetailsComponentModel{
	private String denominatorExceptions;

	public String getDenominatorExceptions() {
		return denominatorExceptions;
	}

	public void setDenominatorExceptions(String denominatorExceptions) {
		this.denominatorExceptions = denominatorExceptions;
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
