package mat.client.clause.cqlworkspace.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class StrataDataModel
 *
 */

public class StrataDataModel {
	
	/**
	 * List of StratificationsObject.
	 */
	private List<StratificationsObject> stratificationObjectList = new ArrayList<StratificationsObject> ();
	
	/**
	 * Constructor
	 */
	public StrataDataModel() {
		
	}
	
	public List<StratificationsObject> getStratificationObjectList() {
		return stratificationObjectList;
	}
	
	public void setStratificationObjectList(List<StratificationsObject> stratificationObjectList) {
		this.stratificationObjectList = stratificationObjectList;
	}
	
	public void addStratification(StratificationsObject stratificationsObject) {
		this.stratificationObjectList.add(stratificationsObject);
	}
}
