package mat.client.shared;

import java.util.ArrayList;
import java.util.List;

import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GlobalCopyPasteObject implements IsSerializable{
	
	private List<QualityDataSetDTO> copiedQDMList = new ArrayList<QualityDataSetDTO>();
	
	private String currentMeasureId;

	/**
	 * @return the copiedQDMList
	 */
	public List<QualityDataSetDTO> getCopiedQDMList() {
		return copiedQDMList;
	}

	/**
	 * @param copiedQDMList the copiedQDMList to set
	 */
	public void setCopiedQDMList(List<QualityDataSetDTO> copiedQDMList) {
		this.copiedQDMList = copiedQDMList;
	}

	/**
	 * @return the currentMeasureId
	 */
	public String getCurrentMeasureId() {
		return currentMeasureId;
	}

	/**
	 * @param currentMeasureId the currentMeasureId to set
	 */
	public void setCurrentMeasureId(String currentMeasureId) {
		this.currentMeasureId = currentMeasureId;
	}

}
