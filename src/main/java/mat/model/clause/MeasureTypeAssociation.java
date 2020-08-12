package mat.model.clause;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.MeasureType;

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
@Table(name = "MEASURE_TYPE_ASSOCIATION")
public class MeasureTypeAssociation implements IsSerializable {

	private Integer id;
	private Measure measure;
	private MeasureType measureTypes;

	public MeasureTypeAssociation() {
	}

	public MeasureTypeAssociation(Measure measure, MeasureType measureTypes) {
		this.measure = measure;
		this.measureTypes = measureTypes;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEASURE_ID", nullable = false)
	public Measure getMeasure() {
		return this.measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEASURE_TYPE_ID", nullable = false)
	public MeasureType getMeasureTypes() {
		return this.measureTypes;
	}

	public void setMeasureTypes(MeasureType measureTypes) {
		this.measureTypes = measureTypes;
	}

}
