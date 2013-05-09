package mat.client.measurepackage;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasurePackageClauseDetail implements IsSerializable, Comparable<MeasurePackageClauseDetail> {

	private String id;
	private String name;
	private String type;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int compareTo(MeasurePackageClauseDetail oth) {
		Integer groupingOrder = Integer.parseInt(MeasureGroupingOrder.valueOf(type).getStatusCode());
		Integer otherGroupingOrder = Integer.parseInt(MeasureGroupingOrder.valueOf(oth.type).getStatusCode());
		return groupingOrder.compareTo(otherGroupingOrder);
	}
	
	
	
}
