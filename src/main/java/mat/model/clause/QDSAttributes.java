package mat.model.clause;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "QDM_ATTRIBUTES")
public class QDSAttributes implements IsSerializable {
	

	private String id;
	
	private String name;
	
	private String dataTypeId;
	
	private String qDSAttributeType;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "NAME", nullable = false, length = 32)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "DATA_TYPE_ID", nullable = false, length = 32)
	public String getDataTypeId() {
		return dataTypeId;
	}
	
	public void setDataTypeId(String dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	
	@Column(name = "QDM_ATTRIBUTE_TYPE", nullable = false, length = 32)
	public String getqDSAttributeType() {
		return qDSAttributeType;
	}

	public void setqDSAttributeType(String qDSAttributeType) {
		this.qDSAttributeType = qDSAttributeType;
	}
}
