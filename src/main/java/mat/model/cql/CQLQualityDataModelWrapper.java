package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * The Class CQLQualityDataSetDTO.
 */
public class CQLQualityDataModelWrapper implements IsSerializable{
	
	/** The quality data dto list. */
	private List<CQLQualityDataSetDTO> qualityDataDTOList;
	
	/** The mat.vsac profile. */
	//private String vsacExpIdentifier;
	/**
	 * Gets the quality data dto.
	 * 
	 * @return the quality data dto
	 */
	public List<CQLQualityDataSetDTO> getQualityDataDTO() {
		return qualityDataDTOList;
	}

	/**
	 * Sets the quality data dto.
	 * 
	 * @param qualityDataDTOList
	 *            the new quality data dto
	 */
	public void setQualityDataDTO(List<CQLQualityDataSetDTO> qualityDataDTOList) {
		this.qualityDataDTOList = qualityDataDTOList;
	}

	/**
	 * Gets the mat.vsac profile.
	 *
	 * @return the mat.vsac profile
	 */
	/*public String getVsacExpIdentifier() {
		return vsacExpIdentifier;
	}*/

	/**
	 * Sets the mat.vsac identifier.
	 *
	 * @param mat.vsac Identifier the new mat.vsac profile
	 */
	/*public void setVsacExpIdentifier(String vsacExpIdentifier) {
		this.vsacExpIdentifier = vsacExpIdentifier;
	}*/

}
