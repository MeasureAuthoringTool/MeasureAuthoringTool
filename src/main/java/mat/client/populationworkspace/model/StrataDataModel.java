package mat.client.populationworkspace.model;

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
		int lastSequenceNumber = 0;
		if(stratificationObjectList.size() > 0) {
			stratificationObjectList.sort((StratificationsObject so1, StratificationsObject so2)->so1.getSequenceNumber().compareTo(so2.getSequenceNumber()));
			lastSequenceNumber = stratificationObjectList.get(stratificationObjectList.size() - 1).getSequenceNumber();
		}
		return lastSequenceNumber;
	}
	
	
	public String toXML(List<StratificationsObject> stratificationObjectList) {
		StringBuilder sb = new StringBuilder();
		sb.append("<strata displayName=\"Stratification\">");
		for(StratificationsObject s : stratificationObjectList) {
		
			sb.append("<").append(s.getPopulationName());
			sb.append(" displayName=").append("\"");
			sb.append(s.getDisplayName()).append("\"");
			sb.append(" uuid=").append("\"");
			sb.append(s.getUuid());
			sb.append("\"").append(">");
			
			for(PopulationClauseObject pco : s.getPopulationClauseObjectList()) {
				sb.append(pco.toXML());
			}
			
			sb.append("</").append(s.getPopulationName()).append(">");
			
		}
		sb.append("</strata>");

		return sb.toString();
				
	}
	
}
