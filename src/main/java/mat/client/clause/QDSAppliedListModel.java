package mat.client.clause;

import mat.model.QualityDataSetDTO;

import java.util.List;

/**
 * The Class QDSAppliedListModel.
 */
public class QDSAppliedListModel {
	
	/** The applied qd ms. */
	private List<QualityDataSetDTO> appliedQDMs;
	//private HashSet<QualityDataSetDTO> removeQDMs;
	/** The last selected. */
	private QualityDataSetDTO lastSelected;
	
	/**
	 * Gets the applied qd ms.
	 * 
	 * @return the appliedQDMs
	 */
	public List<QualityDataSetDTO> getAppliedQDMs() {
		return appliedQDMs;
	}
	
	/**
	 * Sets the last selected.
	 * 
	 * @param lastSelected
	 *            the new last selected
	 *//*
	public void setRemoveQDMs(Set<QualityDataSetDTO> set) {
		this.removeQDMs = (HashSet<QualityDataSetDTO>) set;
	}

	  *//**
	  * @return the removeQDMs
	  *//*
	public HashSet<QualityDataSetDTO> getRemoveQDMs() {
		return removeQDMs;
	}*/
	
	

	/**
	 * Gets the last selected.
	 * 
	 * @return the lastSelected
	 */
	public QualityDataSetDTO getLastSelected() {
		return lastSelected;
	}
	
	/**
	 * Sets the applied qd ms.
	 * 
	 * @param appliedQDMs
	 *            the appliedQDMs to set
	 */
	public void setAppliedQDMs(List<QualityDataSetDTO> appliedQDMs) {
		this.appliedQDMs = appliedQDMs;
	}
	
	/**
	 * @param lastSelected the lastSelected to set
	 */
	public void setLastSelected(QualityDataSetDTO lastSelected) {
		this.lastSelected = lastSelected;
	}
	
}
