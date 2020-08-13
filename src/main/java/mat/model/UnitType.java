package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "UNIT_TYPE")
public class UnitType implements java.io.Serializable {

	private static final long serialVersionUID = -8202686098236431056L;
	private String id;
	private String name;
	private Set<UnitMatrix> unitTypeMatrixes = new HashSet<>(0);

	public UnitType() {
	}

	public UnitType(String id) {
		this.id = id;
	}

	public UnitType(String id, String name, Set<UnitMatrix> unitTypeMatrixes) {
		this.id = id;
		this.name = name;
		this.unitTypeMatrixes = unitTypeMatrixes;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
	public Set<UnitMatrix> getUnitTypeMatrixes() {
		return this.unitTypeMatrixes;
	}

	public void setUnitTypeMatrixes(Set<UnitMatrix> unitTypeMatrixes) {
		this.unitTypeMatrixes = unitTypeMatrixes;
	}

}
