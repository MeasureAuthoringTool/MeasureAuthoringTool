package mat.client.measure.measuredetails.components;

public class GeneralInformationModel implements MeasureDetailsComponentModel{
	private String finalizedDate;
	private boolean patientBased;
	private String guid;
	private String eCQMAbbreviatedTitle;
	private String eCQMVersionNumber;
	public String getFinalizedDate() {
		return finalizedDate;
	}
	public void setFinalizedDate(String finalizedDate) {
		this.finalizedDate = finalizedDate;
	}
	public boolean isPatientBased() {
		return patientBased;
	}
	public void setPatientBased(boolean patientBased) {
		this.patientBased = patientBased;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String geteCQMAbbreviatedTitle() {
		return eCQMAbbreviatedTitle;
	}
	public void seteCQMAbbreviatedTitle(String eCQMAbbreviatedTitle) {
		this.eCQMAbbreviatedTitle = eCQMAbbreviatedTitle;
	}
	public String geteCQMVersionNumber() {
		return eCQMVersionNumber;
	}
	public void seteCQMVersionNumber(String eCQMVersionNumber) {
		this.eCQMVersionNumber = eCQMVersionNumber;
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
