package mat.shared.measure.measuredetails.models;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.shared.StringUtility;

public class GeneralInformationModel implements MeasureDetailsComponentModel, IsSerializable {
	private String measureName;
	private String measureModel;
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
	private boolean isCalendarYear;
	private String measureFromPeriod;
	private String measureToPeriod;
	private boolean isExperimental;

	public GeneralInformationModel() {
	}
	
	public GeneralInformationModel(GeneralInformationModel model) {
		this.measureName = model.getMeasureName();
		this.measureModel = model.getMeasureModel();
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
		this.setCalendarYear(model.isCalendarYear());
		this.setMeasureFromPeriod(model.getMeasureFromPeriod());
		this.setMeasureToPeriod(model.getMeasureToPeriod());
		this.isExperimental = model.isExperimental;
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
		return (modelValuesAreEqual(originalModel.getMeasureName(), getMeasureName()) &&
				modelValuesAreEqual(originalModel.getMeasureModel(), getMeasureModel()) &&
				modelValuesAreEqual(originalModel.getFinalizedDate(), getFinalizedDate()) && 
				modelValuesAreEqual(originalModel.isPatientBased(), isPatientBased()) &&
				modelValuesAreEqual(originalModel.getGuid(), getGuid()) &&
				modelValuesAreEqual(originalModel.geteCQMAbbreviatedTitle(), geteCQMAbbreviatedTitle()) &&
				modelValuesAreEqual(originalModel.geteCQMVersionNumber(), geteCQMVersionNumber()) && 
				modelValuesAreEqual(originalModel.getCompositeScoringMethod(), getCompositeScoringMethod()) &&
				modelValuesAreEqual(originalModel.getScoringMethod(), getScoringMethod()) &&
				modelValuesAreEqual(originalModel.getNqfId(), getNqfId()) && 
				modelValuesAreEqual(originalModel.getEndorseByNQF(), getEndorseByNQF()) &&
				modelValuesAreEqual(originalModel.isCalendarYear(), isCalendarYear) &&
				modelValuesAreEqual(originalModel.getMeasureFromPeriod(), getMeasureFromPeriod()) &&
				modelValuesAreEqual(originalModel.getMeasureToPeriod(), getMeasureToPeriod()) &&
				modelValuesAreEqual(originalModel.isExperimental(), isExperimental()));
	}
	

	public boolean modelValuesAreEqual(String originalModelValue, String newModelValue) {
		return (StringUtility.isEmptyOrNull(originalModelValue) && StringUtility.isEmptyOrNull(newModelValue) ||
		(originalModelValue != null && originalModelValue.equals(newModelValue)));
	}
	
	public boolean modelValuesAreEqual(Boolean originalModelValue, Boolean newModelValue) {
		return (originalModelValue == null && newModelValue == null) ||  (originalModelValue == newModelValue);
	}
	
	public String getMeasureName() {
		return measureName;
	}
	
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public String getMeasureModel() {
		return measureModel;
	}

	public void setMeasureModel(String measureModel) {
		this.measureModel = measureModel;
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
		sb.append("measureModel: " + measureModel);
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
		sb.append(", isCalendarYear: " + isCalendarYear);
		sb.append(", measureFromPeriod: " + measureFromPeriod);
		sb.append(", measureToperiod: " + measureToPeriod);
		sb.append(", isExperimental: " + isExperimental);
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
	
	public boolean isCalendarYear() {
		return isCalendarYear;
	}
	
	public void setCalendarYear(boolean isCalenderYear) {
		this.isCalendarYear = isCalenderYear;
	}
	
	public String getMeasureFromPeriod() {
		return measureFromPeriod;
	}
	
	public void setMeasureFromPeriod(String measFromPeriod) {
		this.measureFromPeriod = StringUtility.doTrim(measFromPeriod);
	}
	
	public String getMeasureToPeriod() {
		return measureToPeriod;
	}
	
	public void setMeasureToPeriod(String measToPeriod) {
		this.measureToPeriod = StringUtility.doTrim(measToPeriod);
	}

	public boolean isExperimental() {
		return isExperimental;
	}

	public void setExperimental(boolean experimental) {
		isExperimental = experimental;
	}
}
