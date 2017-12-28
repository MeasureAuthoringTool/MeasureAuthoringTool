package mat.model.populations;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class CQLAggFunction {
	private String displayName;
	
	private CQLFunction cqlFunction;

	@XmlAttribute
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public CQLFunction getCqlFunction() {
		return cqlFunction;
	}

	@XmlElement(name="cqlfunction")
	public void setCqlFunction(CQLFunction cqlFunction) {
		this.cqlFunction = cqlFunction;
	}
	
	public CQLAggFunction(String displayName) {
		super();
		this.displayName = displayName;
	}
	
}
