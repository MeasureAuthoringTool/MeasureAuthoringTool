package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * The Class CQLQualityDataSetDTO.
 */
public class CQLQualityDataModelWrapper implements IsSerializable{
	
	/** The quality data dto list. */
	private List<CQLQualityDataSetDTO> qualityDataDTOList;
	
	/** The vsac profile. */
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
	 * Gets the vsac profile.
	 *
	 * @return the vsac profile
	 */
	/*public String getVsacExpIdentifier() {
		return vsacExpIdentifier;
	}*/

	/**
	 * Sets the vsac identifier.
	 *
	 * @param vsac Identifier the new vsac profile
	 */
	/*public void setVsacExpIdentifier(String vsacExpIdentifier) {
		this.vsacExpIdentifier = vsacExpIdentifier;
	}*/

}
