package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UNIT")
public class Unit implements java.io.Serializable {

	private static final long serialVersionUID = -4747568228077545164L;
	private String id;
	private String name;
	private int sortOrder;
	private String cqlUnit;

	public Unit() {
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

	@Column(name = "SORT_ORDER", nullable = false)
	public int getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Column(name = "CQL_UNIT", length = 60)
	public String getCqlUnit() {
		return this.cqlUnit;
	}

	public void setCqlUnit(String cqlUnit) {
		this.cqlUnit = cqlUnit;
	}

}
