package mat.model.populations;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

import com.google.gwt.user.client.rpc.IsSerializable;

@XmlRootElement(name="clause")
public class Clause implements IsSerializable{
	
	private String type;
	
	private String uuid;
	
	private String displayName;
	
	private CQLDefinition cqldefinition;
	
	private CQLFunction cqlFunction;

	private CQLAggFunction cqlAggFunction; 
	
	public String getType() {
		return type;
	}

	@XmlAttribute
	public void setType(String type) {
		this.type = type;
	}

	public String getUuid() {
		return uuid;
	}

	@XmlAttribute
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDisplayName() {
		return displayName;
	}

	@XmlAttribute
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public CQLDefinition getCqldefinition() {
		return cqldefinition;
	}

	@XmlElement(name="cqldefinition")
	public void setCqldefinition(CQLDefinition cqldefinition) {
		this.cqldefinition = cqldefinition;
	}

	public CQLFunction getCqlFunction() {
		return cqlFunction;
	}
	
	@XmlElement(name="cqlfunction")
	public void setCqlFunction(CQLFunction cqlFunction) {
		this.cqlFunction = cqlFunction;
	}

	public CQLAggFunction getCqlAggFunction() {
		return cqlAggFunction;
	}

	@XmlElement(name="cqlaggfunction")
	public void setCqlAggFunction(CQLAggFunction cqlAggFunction) {
		this.cqlAggFunction = cqlAggFunction;
	}

	public Clause(String type, String uuid, String displayName) {
		super();
		this.type = type;
		if(StringUtils.isBlank(uuid)) {
			uuid = UUID.randomUUID().toString();
		}
		this.uuid = uuid;
		this.displayName = displayName;
	}

	public Clause() {
		super();
	}
	
	
}
