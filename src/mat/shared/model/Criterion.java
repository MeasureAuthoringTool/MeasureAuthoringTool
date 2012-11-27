package mat.shared.model;

public class Criterion {
	public String name;
	public Conditional conditional;
	
	public Conditional getConditional() {
		return conditional;
	}
	public void setConditional(Conditional conditional) {
		this.conditional = conditional;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
