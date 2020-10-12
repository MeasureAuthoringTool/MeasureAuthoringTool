package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class QualityDataModelWrapper.
 */
public class QualityDataModelWrapper implements IsSerializable{
	
	/** The quality data dto list. */
	private List<QualityDataSetDTO> qualityDataDTOList;
	
	/** The risk adj var dto list. */
	private List<RiskAdjustmentDTO> riskAdjVarDTOList;
	
	/** The mat.vsac profile. */
	private String vsacExpIdentifier;
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

	/**
	 * Gets the risk adj var dto list.
	 *
	 * @return the risk adj var dto list
	 */
	public List<RiskAdjustmentDTO> getRiskAdjVarDTO() {
		return riskAdjVarDTOList;
	}

	/**
	 * Sets the risk adj var dto list.
	 *
	 * @param riskAdjVarDTOList the new risk adj var dto list
	 */
	public void setRiskAdjVarDTO(List<RiskAdjustmentDTO> riskAdjVarDTOList) {
		this.riskAdjVarDTOList = riskAdjVarDTOList;
	}

	/**
	 * Gets the mat.vsac profile.
	 *
	 * @return the mat.vsac profile
	 */
	public String getVsacExpIdentifier() {
		return vsacExpIdentifier;
	}

	/**
	 * Sets the mat.vsac identifier.
	 *
	 * @param vsac Identifier the new mat.vsac profile
	 */
	public void setVsacExpIdentifier(String vsacExpIdentifier) {
		this.vsacExpIdentifier = vsacExpIdentifier;
	}

}
