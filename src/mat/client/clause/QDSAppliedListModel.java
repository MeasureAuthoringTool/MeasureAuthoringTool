package mat.client.clause;

import java.util.List;

import mat.model.QualityDataSetDTO;

public class QDSAppliedListModel {
	
	private List<QualityDataSetDTO> appliedQDMs;
	//private HashSet<QualityDataSetDTO> removeQDMs;
	private QualityDataSetDTO lastSelected;
	/**
	 * @param appliedQDMs the appliedQDMs to set
	 */
	public void setAppliedQDMs(List<QualityDataSetDTO> appliedQDMs) {
		this.appliedQDMs = appliedQDMs;
	}

	/**
	 * @return the appliedQDMs
	 */
	public List<QualityDataSetDTO> getAppliedQDMs() {
		return appliedQDMs;
	}

	/**
	 * @param set the removeQDMs to set
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
	 * @param lastSelected the lastSelected to set
	 */
	public void setLastSelected(QualityDataSetDTO lastSelected) {
		this.lastSelected = lastSelected;
	}

	/**
	 * @return the lastSelected
	 */
	public QualityDataSetDTO getLastSelected() {
		return lastSelected;
	}

	

}
