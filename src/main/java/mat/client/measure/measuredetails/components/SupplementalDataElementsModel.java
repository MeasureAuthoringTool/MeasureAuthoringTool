package mat.client.measure.measuredetails.components;

public class SupplementalDataElementsModel implements MeasureDetailsComponentModel{
	private String supplementalDataElements;

	public String getSupplementalDataElements() {
		return supplementalDataElements;
	}

	public void setSupplementalDataElements(String supplementalDataElements) {
		this.supplementalDataElements = supplementalDataElements;
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
