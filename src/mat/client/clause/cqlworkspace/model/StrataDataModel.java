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
	
	public int getLastPopulationSequenceNumber() {
		int lastSequenceNumber = 1;
		if(stratificationObjectList.size() > 1) {
			stratificationObjectList.sort((StratificationsObject so1, StratificationsObject so2)->so1.getSequenceNumber().compareTo(so2.getSequenceNumber()));
		}
		lastSequenceNumber = stratificationObjectList.get(stratificationObjectList.size() - 1).getSequenceNumber();
		return lastSequenceNumber;
	}
}
