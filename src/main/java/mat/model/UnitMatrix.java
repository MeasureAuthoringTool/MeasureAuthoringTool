package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UNIT_TYPE_MATRIX")
public class UnitMatrix implements IsSerializable{
	
	private String id;
	
	private String unitTypeId;
	
	private String unitId;
	
    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
    @Column(name = "FK_UNIT_ID", nullable = false)
	public String getUnitTypeId() {
		return unitTypeId;
	}
	
	public void setUnitTypeId(String unitTypeId) {
		this.unitTypeId = unitTypeId;
	}
	
    @Column(name = "FK_UNIT_TYPE_ID", nullable = false)
	public String getUnitId() {
		return unitId;
	}

    public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
}
