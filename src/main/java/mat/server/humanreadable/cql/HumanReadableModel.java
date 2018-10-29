package mat.server.humanreadable.cql;

import java.util.List;

public class HumanReadableModel {
	
	private HumanReadableMeasureInformationModel measureInformation; 
	private List<HumanReadablePopulationCriteriaModel> populationCriterias; 
	private List<HumanReadableExpressionModel> supplementalDataElements; 
	private List<HumanReadableExpressionModel> riskAdjustmentVariables; 
	private List<HumanReadableCodeModel> codeTerminologyList;
	private List<HumanReadableValuesetModel> valuesetTerminologyList; 
	private List<HumanReadableCodeModel> codeDataCriteriaList; 
	private List<HumanReadableValuesetModel> valuesetDataCriteriaList; 
	
	private int numberOfGroups = 1; 
	
	public HumanReadableModel(HumanReadableMeasureInformationModel measureInformationModel, List<HumanReadablePopulationCriteriaModel> populations, 
			List<HumanReadableValuesetModel> valuesetTerminologyList, List<HumanReadableCodeModel> codeTerminologyList,
			List<HumanReadableValuesetModel> valuesetDataCriteriaList, List<HumanReadableCodeModel> codeDataCriteriaList,
			List<HumanReadableExpressionModel> supplementalDataElements,  List<HumanReadableExpressionModel> riskAdjustmentVariables) {
		this.measureInformation = measureInformationModel;
		this.populationCriterias = populations; 
		this.codeTerminologyList = codeTerminologyList;
		this.valuesetTerminologyList = valuesetTerminologyList;
		this.valuesetDataCriteriaList = valuesetDataCriteriaList;
		this.codeDataCriteriaList = codeDataCriteriaList;
		this.supplementalDataElements = supplementalDataElements;
		this.riskAdjustmentVariables = riskAdjustmentVariables;		
		this.setNumberOfGroups(populationCriterias.size());
	}

	public HumanReadableModel() {
	}

	public HumanReadableMeasureInformationModel getMeasureInformation() {
		return measureInformation;
	}

	public void setMeasureInformation(HumanReadableMeasureInformationModel measureInformation) {
		this.measureInformation = measureInformation;
	}

	public List<HumanReadablePopulationCriteriaModel> getPopulationCriterias() {
		return populationCriterias;
	}

	public void setPopulationCriterias(List<HumanReadablePopulationCriteriaModel> groups) {
		this.populationCriterias = groups;
		this.setNumberOfGroups(groups.size());
	}

	public int getNumberOfGroups() {
		return populationCriterias.size();
	}

	public void setNumberOfGroups(int numberOfGroups) {
		this.numberOfGroups = numberOfGroups;
	}

	public List<HumanReadableExpressionModel> getSupplementalDataElements() {
		return supplementalDataElements;
	}

	public void setSupplementalDataElements(List<HumanReadableExpressionModel> supplementalDataElements) {
		this.supplementalDataElements = supplementalDataElements;
	}

	public List<HumanReadableExpressionModel> getRiskAdjustmentVariables() {
		return riskAdjustmentVariables;
	}

	public void setRiskAdjustmentVariables(List<HumanReadableExpressionModel> riskAdjustmentVariables) {
		this.riskAdjustmentVariables = riskAdjustmentVariables;
	}

	public List<HumanReadableCodeModel> getCodeTerminologyList() {
		return codeTerminologyList;
	}

	public void setCodeTerminologyList(List<HumanReadableCodeModel> codeTerminologyList) {
		this.codeTerminologyList = codeTerminologyList;
	}

	public List<HumanReadableValuesetModel> getValuesetTerminologyList() {
		return valuesetTerminologyList;
	}

	public void setValuesetTerminologyList(List<HumanReadableValuesetModel> valuesetTerminologyList) {
		this.valuesetTerminologyList = valuesetTerminologyList;
	}

	public List<HumanReadableCodeModel> getCodeDataCriteriaList() {
		return codeDataCriteriaList;
	}

	public void setCodeDataCriteriaList(List<HumanReadableCodeModel> codeDataCriteriaList) {
		this.codeDataCriteriaList = codeDataCriteriaList;
	}

	public List<HumanReadableValuesetModel> getValuesetDataCriteriaList() {
		return valuesetDataCriteriaList;
	}

	public void setValuesetDataCriteriaList(List<HumanReadableValuesetModel> valuesetDataCriteriaList) {
		this.valuesetDataCriteriaList = valuesetDataCriteriaList;
	}
}
