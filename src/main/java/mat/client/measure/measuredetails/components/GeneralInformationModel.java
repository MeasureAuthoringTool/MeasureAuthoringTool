package mat.client.measure.measuredetails.components;

public class GeneralInformationModel implements MeasureDetailsComponentModel{
	private String measureName;
	private String finalizedDate;
	private boolean patientBased;
	private String guid;
	private String eCQMAbbreviatedTitle;
	private String eCQMVersionNumber;
	private String compositeScoringMethod;
	
	public String getCompositeScoringMethod() {
		return compositeScoringMethod;
	}
	public void setCompositeScoringMethod(String compositeScoringMethod) {
		this.compositeScoringMethod = compositeScoringMethod;
	}
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
		GeneralInformationModel originalModel = (GeneralInformationModel) model;
		return (
				((originalModel.getMeasureName() == null && getMeasureName() == null) ||
				(originalModel.getMeasureName() != null && originalModel.getMeasureName().equals(getMeasureName()))) &&
				
				((originalModel.getFinalizedDate() == null && getFinalizedDate() == null) ||
				(originalModel.getFinalizedDate() != null && originalModel.getFinalizedDate().equals(getFinalizedDate()))) &&
				
				originalModel.isPatientBased() == isPatientBased() && 
				
				((originalModel.getGuid() == null && getGuid() == null) ||
				(originalModel.getGuid() != null && originalModel.getGuid().equals(getGuid()))) &&
				
				((originalModel.geteCQMAbbreviatedTitle() == null && geteCQMAbbreviatedTitle() == null) ||
				(originalModel.geteCQMAbbreviatedTitle().equals(geteCQMAbbreviatedTitle()))) &&
				
				(originalModel.geteCQMVersionNumber() == null && geteCQMVersionNumber() == null) ||
				(originalModel.geteCQMVersionNumber().equals(geteCQMVersionNumber())) &&
				
				(originalModel.getCompositeScoringMethod() == null && getCompositeScoringMethod() == null) ||
				(originalModel.getCompositeScoringMethod() != null && getCompositeScoringMethod().contentEquals(getCompositeScoringMethod())));
	}
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getMeasureName() {
		return measureName;
	}
	
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}
}
