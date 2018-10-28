package mat.server.humanreadable.cql;

import java.util.List;
import java.util.Random;

public class HumanReadablePopulationCriteriaModel {
	String name; 
	String id; 
	List<HumanReadablePopulationModel> populations;
	
	public HumanReadablePopulationCriteriaModel(String name, List<HumanReadablePopulationModel> nameLogicModel) {
		this.populations = nameLogicModel;
		this.name = name; 
		this.id = this.name.replaceAll(" ", "_") + " " + new Random().nextInt();
	}

	public List<HumanReadablePopulationModel> getPopulations() {
		return populations;
	}

	public void setPopulations(List<HumanReadablePopulationModel> nameLogicModel) {
		this.populations = nameLogicModel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}	
}
