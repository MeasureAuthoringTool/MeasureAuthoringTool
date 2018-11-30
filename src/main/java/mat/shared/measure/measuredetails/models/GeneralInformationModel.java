package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GeneralInformationModel implements MeasureDetailsComponentModel, IsSerializable {
	private String measureName;
	private String finalizedDate;
	private boolean patientBased;
	private String guid;
	private String eCQMAbbreviatedTitle;
	private String eCQMVersionNumber;
	private String compositeScoringMethod;
	private String scoringMethod;
	private int eMeasureId;
	
	public GeneralInformationModel() {
	}
	
	public GeneralInformationModel(GeneralInformationModel model) {
		this.measureName = model.getMeasureName();
		this.finalizedDate = model.getFinalizedDate();
		this.patientBased = model.isPatientBased();
		this.guid = model.getGuid();
		this.eCQMAbbreviatedTitle = model.geteCQMAbbreviatedTitle();
		this.eCQMVersionNumber = model.geteCQMVersionNumber();
		this.compositeScoringMethod = model.getCompositeScoringMethod();
		this.scoringMethod = model.getScoringMethod();
	}

	public int geteMeasureId() {
		return eMeasureId;
	}
	public void seteMeasureId(int eMeasureId) {
		this.eMeasureId = eMeasureId;
	}
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
		boolean isEqual = ((originalModel.getMeasureName() == null && getMeasureName() == null) ||
				(originalModel.getMeasureName() != null && originalModel.getMeasureName().equals(getMeasureName())) &&
				((originalModel.getFinalizedDate() == null && getFinalizedDate() == null) ||
						(originalModel.getFinalizedDate() != null && originalModel.getFinalizedDate().equals(getFinalizedDate()))) &&
				originalModel.isPatientBased() == isPatientBased()  &&
				((originalModel.getGuid() == null && getGuid() == null) ||
				(originalModel.getGuid() != null && originalModel.getGuid().equals(getGuid())))  &&
				((originalModel.geteCQMAbbreviatedTitle() == null && geteCQMAbbreviatedTitle() == null) ||
				(originalModel.geteCQMAbbreviatedTitle().equals(geteCQMAbbreviatedTitle()))) &&
				(originalModel.geteCQMVersionNumber() == null && geteCQMVersionNumber() == null) ||
				(originalModel.geteCQMVersionNumber().equals(geteCQMVersionNumber())) &&
				((originalModel.getCompositeScoringMethod() == null && getCompositeScoringMethod() == null) ||
				(originalModel.getCompositeScoringMethod() != null && originalModel.getCompositeScoringMethod().equals(getCompositeScoringMethod()))) && 
				((originalModel.getScoringMethod() == null && getScoringMethod() == null) ||
				(originalModel.getScoringMethod() != null && originalModel.getScoringMethod().equals(getScoringMethod())))
				);
		return isEqual;
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
	
	public String getScoringMethod() {
		return scoringMethod;
	}
	public void setScoringMethod(String scoringMethod) {
		this.scoringMethod = scoringMethod;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("measureName: " + measureName);
		sb.append(", finalizedDate: " + finalizedDate);
		sb.append(", patientBased: " + patientBased);
		sb.append(", guid: " + guid);
		sb.append(", eCQMAbbreviatedTitle: " + eCQMAbbreviatedTitle);
		sb.append(", eCQMVersionNumber: " + eCQMVersionNumber);
		sb.append(", compositeScoringMethod: " + compositeScoringMethod);
		sb.append(", scoringMethod: " + scoringMethod);
		return sb.toString();
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
