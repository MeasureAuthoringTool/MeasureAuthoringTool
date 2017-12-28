package mat.model.populations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Stratification {

	private String displayName;
	
	private String uuid;
	
	List<Clause> clauses;
	
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

	public List<Clause> getClauses() {
		return clauses;
	}

	@XmlElement(name="clause")
	public void setClauses(List<Clause> clauses) {
		this.clauses = clauses;
	}
	
	public void add(Clause clause) {
		if(this.clauses == null) {
			this.clauses = new ArrayList<Clause>();			
		}
		this.clauses.add(clause);
	}

	
	public Stratification(String displayName, String uuid) {
		super();
		this.displayName = displayName;
		this.uuid = uuid;
	}

	public Stratification() {
		super();
	}
	
}
