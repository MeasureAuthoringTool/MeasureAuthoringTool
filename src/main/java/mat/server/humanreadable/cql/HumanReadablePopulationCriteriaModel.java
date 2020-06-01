package mat.server.humanreadable.cql;

import java.util.List;
import java.util.Random;

public class HumanReadablePopulationCriteriaModel {
	private String name; 
	private String id; 
	private int sequence; 
	private List<HumanReadablePopulationModel> populations;
	
	public HumanReadablePopulationCriteriaModel(String name, List<HumanReadablePopulationModel> nameLogicModel, int sequence) {
		this.populations = nameLogicModel;
		this.name = name; 
		this.sequence = sequence; 
		this.id = this.name.replaceAll(" ", "_") + "_" + Math.abs(new Random().nextInt());
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

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
}
