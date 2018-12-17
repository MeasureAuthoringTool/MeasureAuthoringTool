package mat.shared.measure.measuredetails.models;

import java.util.List;

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
	private String nqfId;
	private Boolean endorseByNQF;

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
		this.eMeasureId = model.geteMeasureId();
		this.endorseByNQF = model.getEndorseByNQF();
		this.nqfId = model.nqfId;
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
				(originalModel.getScoringMethod() != null && originalModel.getScoringMethod().equals(getScoringMethod()))) &&
				(originalModel.geteMeasureId() == geteMeasureId()) &&
				((originalModel.getNqfId() == null && getNqfId() == null) || 
				(originalModel.getNqfId() != null && originalModel.getNqfId().equals(getNqfId()))) &&
				((originalModel.getEndorseByNQF() == null && getEndorseByNQF() == null) || 
				(originalModel.getEndorseByNQF() != null &&  originalModel.getEndorseByNQF().equals(getEndorseByNQF())))
				);
		return isEqual;
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
	
	public String getNqfId() {
		return nqfId;
	}

	public void setNqfId(String nqfId) {
		this.nqfId = nqfId;
	}
	
	public Boolean getEndorseByNQF() {
		return endorseByNQF;
	}

	public void setEndorseByNQF(Boolean endorseByNQF) {
		this.endorseByNQF = endorseByNQF;
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
		sb.append(", eMeasureId: " + eMeasureId);
		sb.append(", endorsedByNQF: " + endorseByNQF);
		sb.append(", nqfId: " + nqfId);
		return sb.toString();
	}
	
	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.updateModel(this);
	}
	
	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.validateModel(this);
	}

	@Override
	public boolean isDirty(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.isDirty(this);
	}
}
