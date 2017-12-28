package mat.model.populations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Strata {
	
	private String displayName;
	
	List<Stratification> stratifications;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<Stratification> getStratifications() {
		return stratifications;
	}
	
	@XmlElement(name="stratification")
	public void setStratifications(List<Stratification> stratifications) {
		this.stratifications = stratifications;
	}
	
	public void add(Stratification stratification) {
		if(this.stratifications == null) {
			this.stratifications = new ArrayList<Stratification>();			
		}
		this.stratifications.add(stratification);
	}
	
}
