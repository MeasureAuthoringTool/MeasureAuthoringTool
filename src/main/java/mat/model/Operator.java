package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "OPERATOR")
public class Operator implements java.io.Serializable {

	private static final long serialVersionUID = 7487116012389615656L;
	private String id;
	private OperatorType operatorType;
	private String longName;
	private String shortName;

	public Operator() {
	}

	public Operator(String id, OperatorType operatorType) {
		this.id = id;
		this.operatorType = operatorType;
	}

	public Operator(String id, OperatorType operatorType, String longName, String shortName) {
		this.id = id;
		this.operatorType = operatorType;
		this.longName = longName;
		this.shortName = shortName;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_OPERATOR_TYPE", nullable = false)
	public OperatorType getOperatorType() {
		return this.operatorType;
	}

	public void setOperatorType(OperatorType operatorType) {
		this.operatorType = operatorType;
	}

	@Column(name = "LONG_NAME", length = 45)
	public String getLongName() {
		return this.longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	@Column(name = "SHORT_NAME", length = 45)
	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
