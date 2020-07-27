package mat.model.clause;


import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.Organization;

import java.io.Serializable;

@Entity
@Table(name = "MEASURE_DEVELOPER_ASSOCIATION")
public class MeasureDeveloperAssociation implements IsSerializable, Serializable {

	private Integer id;
	private Measure measure;
	private Organization organization;

	public MeasureDeveloperAssociation() {
	}

	public MeasureDeveloperAssociation(Measure measure) {
		this.measure = measure;
	}

	public MeasureDeveloperAssociation(Measure measure, Organization organization) {
		this.measure = measure;
		this.organization = organization;
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
	@JoinColumn(name = "MEASURE_DEVELOPER_ID")
	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
