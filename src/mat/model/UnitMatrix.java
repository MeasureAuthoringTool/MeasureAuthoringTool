package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UnitMatrix implements IsSerializable{
	private String id;
	private String unitTypeId;
	private String unitId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUnitTypeId() {
		return unitTypeId;
	}
	public void setUnitTypeId(String unitTypeId) {
		this.unitTypeId = unitTypeId;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
}
