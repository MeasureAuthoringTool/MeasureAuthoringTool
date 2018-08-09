package mat.model.clause;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COMPONENT_MEASURES")
public class ComponentMeasure {

	private int id;
	
	private String compositeMeasureId;
	
	private String componentMeasureId;
	
	private String alias;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompositeMeasureId() {
		return compositeMeasureId;
	}

	public void setCompositeMeasureId(String compositeMeasureId) {
		this.compositeMeasureId = compositeMeasureId;
	}

	public String getComponentMeasureId() {
		return componentMeasureId;
	}

	public void setComponentMeasureId(String componentMeasureId) {
		this.componentMeasureId = componentMeasureId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public ComponentMeasure() {
		super();
	}

	public ComponentMeasure(String compositeMeasureId, String componentMeasureId, String alias) {
		super();
		this.compositeMeasureId = compositeMeasureId;
		this.componentMeasureId = componentMeasureId;
		this.alias = alias;
	}
	
}
