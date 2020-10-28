package mat.model.clause;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "COMPONENT_MEASURES")
public class ComponentMeasure {

	private int id;
	
	private Measure compositeMeasure;
	
	private Measure componentMeasure;
	

	private String alias;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "ALIAS", length = 45)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COMPOSITE_MEASURE_ID", nullable = false)
	public Measure getCompositeMeasure() {
		return compositeMeasure;
	}

	public void setCompositeMeasure(Measure compositeMeasure) {
		this.compositeMeasure = compositeMeasure;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COMPONENT_MEASURE_ID", nullable = false)
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
