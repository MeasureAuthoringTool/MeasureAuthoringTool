package mat.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class QualityDataModelWrapper.
 */
public class QualityDataModelWrapper implements IsSerializable{
	
	/** The quality data dto list. */
	private List<QualityDataSetDTO> qualityDataDTOList;

	/**
	 * Gets the quality data dto.
	 * 
	 * @return the quality data dto
	 */
	public List<QualityDataSetDTO> getQualityDataDTO() {
		return qualityDataDTOList;
	}

	/**
	 * Sets the quality data dto.
	 * 
	 * @param qualityDataDTOList
	 *            the new quality data dto
	 */
	public void setQualityDataDTO(List<QualityDataSetDTO> qualityDataDTOList) {
		this.qualityDataDTOList = qualityDataDTOList;
	}

}
