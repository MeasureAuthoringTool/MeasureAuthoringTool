package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ComponentMeasureTabObject implements IsSerializable{
	private String measureName;
	private String libraryAlias; 
	private String libraryName;
	private String ownerName;
	private String libraryContent;
	private String componentId;
	private String version;
	
	public ComponentMeasureTabObject() {
		
	}
	
	public ComponentMeasureTabObject(String measureName, String libraryAlias, String libraryName, String version, String ownerName, String content, String componentId) {
		this.setMeasureName(measureName);
		this.setLibraryAlias(libraryAlias); 
		this.libraryName = libraryName;
		this.ownerName = ownerName;
		this.libraryContent = content;
		this.componentId = componentId;
		this.version = version; 
	}
	
	public String getAlias() {
		return libraryAlias;
	}
	
	public String getLibraryName() {
		return libraryName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public String getLibraryContent() {
		return libraryContent;
	}

	public String getComponentId() {
		return componentId;
	}

	public String getLibraryAlias() {
		return libraryAlias;
	}

	public void setLibraryAlias(String libraryAlias) {
		this.libraryAlias = libraryAlias;
	}

	public String getMeasureName() {
		return measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}