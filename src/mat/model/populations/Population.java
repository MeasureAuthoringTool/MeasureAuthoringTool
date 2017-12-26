package mat.model.populations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Population {
	
	private String displayName;

	List<Clause> clauses;
		
	public String getDisplayName() {
		return displayName;
	}

	@XmlAttribute
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
	
}
