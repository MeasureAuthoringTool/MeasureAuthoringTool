package mat.client.measure.measuredetails.components;

public class DescriptionModel implements MeasureDetailsComponentModel{
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
