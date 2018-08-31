package mat.model.clause;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COMPONENT_MEASURES")
public class ComponentMeasure {

	private int id;
	
	private Measure compositeMeasure;
	
	private Measure componentMeasure;
	

	private String alias;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Measure getCompositeMeasure() {
		return compositeMeasure;
	}

	public void setCompositeMeasure(Measure compositeMeasure) {
		this.compositeMeasure = compositeMeasure;
	}

	public Measure getComponentMeasure() {
		return componentMeasure;
	}

	public void setComponentMeasure(Measure componentMeasure) {
		this.componentMeasure = componentMeasure;
	}

	public ComponentMeasure() {
		super();
	}
	
	public ComponentMeasure(Measure compositeMeasure, Measure componentMeasure, String alias) {
		super();
		this.compositeMeasure = compositeMeasure;
		this.componentMeasure = componentMeasure;
		this.alias = alias;
	}

	
}
