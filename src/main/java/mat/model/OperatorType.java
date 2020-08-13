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
@Table(name = "OPERATOR_TYPE")
public class OperatorType implements java.io.Serializable {

	private static final long serialVersionUID = -3200249108277873539L;
	private String id;
	private String name;
	private Set<Operator> operators = new HashSet<>(0);

	public OperatorType() {
	}

	public OperatorType(String id) {
		this.id = id;
	}

	public OperatorType(String id, String name, Set<Operator> operators) {
		this.id = id;
		this.name = name;
		this.operators = operators;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "operatorType")
	public Set<Operator> getOperators() {
		return this.operators;
	}

	public void setOperators(Set<Operator> operators) {
		this.operators = operators;
	}

}
