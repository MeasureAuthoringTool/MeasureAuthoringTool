package mat.model.populations;

import javax.xml.bind.annotation.XmlAttribute;

public class CQLFunction {

	private String displayName;
	
	private String uuid;
	
	@XmlAttribute
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@XmlAttribute
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public CQLFunction(String displayName, String uuid) {
		super();
		this.displayName = displayName;
		this.uuid = uuid;
	}

	public CQLFunction() {
		super();
	}
	
	
}
