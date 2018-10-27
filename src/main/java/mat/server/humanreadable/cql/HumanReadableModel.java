package mat.server.humanreadable.cql;

public class HumanReadableModel {
	
	private HumanReadableMeasureInformationModel measureInformation; 
	
	public HumanReadableModel(HumanReadableMeasureInformationModel measureInformationModel) {
		this.setMeasureInformation(measureInformationModel);
	}

	public HumanReadableModel() {
	}

	public HumanReadableMeasureInformationModel getMeasureInformation() {
		return measureInformation;
	}

	public void setMeasureInformation(HumanReadableMeasureInformationModel measureInformation) {
		this.measureInformation = measureInformation;
	}
}
