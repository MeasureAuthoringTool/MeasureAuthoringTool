package mat.client.measure.measuredetails.components;

public class ClinicalRecommendationModel implements MeasureDetailsComponentModel{
	private String clinicalRecommendation;

	public String getClinicalRecommendation() {
		return clinicalRecommendation;
	}

	public void setClinicalRecommendation(String clinicalRecommendation) {
		this.clinicalRecommendation = clinicalRecommendation;
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
