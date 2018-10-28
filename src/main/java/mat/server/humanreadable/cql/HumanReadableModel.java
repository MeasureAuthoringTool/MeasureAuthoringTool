package mat.server.humanreadable.cql;

import java.util.List;

public class HumanReadableModel {
	
	private HumanReadableMeasureInformationModel measureInformation; 
	private List<HumanReadablePopulationCriteriaModel> populationCriterias; 
	private List<HumanReadableExpressionModel> supplementalDataElements; 
	private List<HumanReadableExpressionModel> riskAdjustmentVariables; 
	
	private int numberOfGroups = 1; 
	
	public HumanReadableModel(HumanReadableMeasureInformationModel measureInformationModel, List<HumanReadablePopulationCriteriaModel> populations, 
			List<HumanReadableExpressionModel> supplementalDataElements, List<HumanReadableExpressionModel> riskAdjustmentVariables) {
		this.measureInformation = measureInformationModel;
		this.populationCriterias = populations; 
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
		return numberOfGroups;
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
}
