package mat.server.humanreadable.cql;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public class HumanReadablePopulationModel {
	
	private String id; 
	private String name; 
	private String logic; 
	private String expressionName; 
	private String expressionUUID;
	private String aggregateFunction;
	private boolean inGroup; 
	private String associatedPopulationName; 
	private String display; 
	
	public HumanReadablePopulationModel(String name, String logic, String expressionName, String expressionUUID, String aggregateFunction, String associatedPopulationName, boolean isInGroup) {
		this.name = name;
		this.logic = logic;
		this.aggregateFunction = aggregateFunction;
		this.inGroup = isInGroup;
		this.expressionName = expressionName; 
		this.expressionUUID = expressionUUID; 
		this.associatedPopulationName = associatedPopulationName;
		this.id = name.replaceAll(" ", "_") + " " +  new Random().nextInt();
		createDisplay();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogic() {
		return logic;
	}
	public void setLogic(String logic) {
		this.logic = logic;
	}
	public String getAggregateFunction() {
		return aggregateFunction;
	}
	public void setAggregateFunction(String aggregateFunction) {
		this.aggregateFunction = aggregateFunction;
	}

	public boolean getInGroup() {
		return inGroup;
	}

	public void setInGroup(boolean isInGroup) {
		this.inGroup = isInGroup;
	}

	public String getExpressionName() {
		return expressionName;
	}

	public void setExpressionName(String expressionName) {
		this.expressionName = expressionName;
	}

	public String getExpressionUUID() {
		return expressionUUID;
	}

	public void setExpressionUUID(String expressionUUID) {
		this.expressionUUID = expressionUUID;
	}

	public String getId() {
		return id;
	}
	
	private void createDisplay() {
		String display = this.name;
		if(!StringUtils.isEmpty(associatedPopulationName)) {
			display = display + "    (Association: " + associatedPopulationName + ")"; 
		}
		
		this.display = display; 
	}

	public String getAssociatedPopulationName() {
		return associatedPopulationName;
	}

	public void setAssociatedPopulationName(String associatedPopulationName) {
		this.associatedPopulationName = associatedPopulationName;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
}
