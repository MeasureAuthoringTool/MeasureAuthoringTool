package mat.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ComponentMeasureTabObject implements IsSerializable{
	private String measureAlias;
	private String libName;
	private String libOwner;
	private String libContent;
	private String componentId;
	
	public ComponentMeasureTabObject(String measureAlias, String name, String owner, String content, String componentId) {
		this.measureAlias = measureAlias;
		this.libName = name;
		this.libOwner = owner;
		this.libContent = content;
		this.componentId = componentId;
	}
	
	public String getAlias() {
		return measureAlias;
	}
	
	public String getLibName() {
		return libName;
	}

	public String getLibOwner() {
		return libOwner;
	}

	public String getLibContent() {
		return libContent;
	}

	public String getComponentId() {
		return componentId;
	}
	
}