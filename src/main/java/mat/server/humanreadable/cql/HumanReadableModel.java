package mat.server.humanreadable.cql;

import java.util.List;

public class HumanReadableModel {
	
	private HumanReadableMeasureInformationModel measureInformation; 
	private List<HumanReadablePopulationCriteriaModel> populationCriterias; 
	private int numberOfGroups = 1; 
	
	public HumanReadableModel(HumanReadableMeasureInformationModel measureInformationModel, List<HumanReadablePopulationCriteriaModel> populations) {
		this.measureInformation = measureInformationModel;
		this.populationCriterias = populations; 
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
}
