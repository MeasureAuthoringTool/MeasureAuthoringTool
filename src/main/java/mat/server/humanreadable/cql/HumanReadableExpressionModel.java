package mat.server.humanreadable.cql;

import java.util.Random;

public class HumanReadableExpressionModel {
	private String name; 
	private String logic;
	private String id; 

	public HumanReadableExpressionModel(String name, String logic) {
		this.name = name;
		this.logic = logic;
		this.id = name.replaceAll(" ", "_") + "_" + Math.abs(new Random().nextInt());
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
	public String getId() {
		return id;
	}	
}
