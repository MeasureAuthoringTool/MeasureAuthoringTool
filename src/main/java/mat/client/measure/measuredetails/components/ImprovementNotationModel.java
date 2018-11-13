package mat.client.measure.measuredetails.components;

public class ImprovementNotationModel implements MeasureDetailsComponentModel{
	private String improvementNotation;

	public String getImprovementNotation() {
		return improvementNotation;
	}

	public void setImprovementNotation(String improvementNotation) {
		this.improvementNotation = improvementNotation;
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
