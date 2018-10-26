package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MAT_FLAG")
public class MatFlag implements java.io.Serializable {
	private static final long serialVersionUID = -3203401079752345678L;
	private String id;
	private String flag;

	public MatFlag() {
	}

	public MatFlag(String id) {
		this.id = id;
	}

	@Id
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@Column(name="FLAG")
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
