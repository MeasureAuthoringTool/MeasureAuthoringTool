package mat.model;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The Class MatValueSetTransferObject.
 */
public class MatValueSetTransferObject implements IsSerializable {
	
	/** The applied qdm list. */
	List<QualityDataSetDTO> appliedQDMList;
	
	/** The code list search dto. */
	CodeListSearchDTO codeListSearchDTO;
	
	/** The datatype. */
	String datatype;
	
	/** The effective date. */
	String effectiveDate;
	
	/** The is specific occurrence. */
	boolean isSpecificOccurrence;
	
	/** The mat value set. */
	MatValueSet matValueSet;
	
	/** The measure id. */
	String measureId;
	
	/** The quality data set dto. */
	QualityDataSetDTO qualityDataSetDTO;
	
	/** The user defined text. */
	String userDefinedText;
	
	/** The version. */
	String version;
	
	/**
	 * Gets the applied qdm list.
	 * 
	 * @return the appliedQDMList
	 */
	public List<QualityDataSetDTO> getAppliedQDMList() {
		return appliedQDMList;
	}
	
	/**
	 * Gets the code list search dto.
	 * 
	 * @return the codeListSearchDTO
	 */
	public CodeListSearchDTO getCodeListSearchDTO() {
		return codeListSearchDTO;
	}
	
	/**
	 * Gets the datatype.
	 * 
	 * @return the datatype
	 */
	public String getDatatype() {
		return datatype;
	}
	
	/**
	 * Gets the effective date.
	 * 
	 * @return the effectiveDate
	 */
	public String getEffectiveDate() {
		return effectiveDate;
	}
	
	/**
	 * Gets the mat value set.
	 * 
	 * @return the matValueSet
	 */
	public MatValueSet getMatValueSet() {
		return matValueSet;
	}
	
	/**
	 * Gets the measure id.
	 * 
	 * @return the measureId
	 */
	public String getMeasureId() {
		return measureId;
	}
	
	/**
	 * Gets the quality data set dto.
	 * 
	 * @return the qualityDataSetDTO
	 */
	public QualityDataSetDTO getQualityDataSetDTO() {
		return qualityDataSetDTO;
	}
	
	/**
	 * Gets the user defined text.
	 * 
	 * @return the userDefinedText
	 */
	public String getUserDefinedText() {
		return userDefinedText;
	}
	
	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Checks if is specific occurrence.
	 * 
	 * @return the isSpecificOccurrence
	 */
	public boolean isSpecificOccurrence() {
		return isSpecificOccurrence;
	}
	
	/**
	 * Sets the applied qdm list.
	 * 
	 * @param appliedQDMList
	 *            the appliedQDMList to set
	 */
	public void setAppliedQDMList(List<QualityDataSetDTO> appliedQDMList) {
		this.appliedQDMList = appliedQDMList;
	}
	
	/**
	 * Sets the code list search dto.
	 * 
	 * @param codeListSearchDTO
	 *            the codeListSearchDTO to set
	 */
	public void setCodeListSearchDTO(CodeListSearchDTO codeListSearchDTO) {
		this.codeListSearchDTO = codeListSearchDTO;
	}
	
	/**
	 * Sets the datatype.
	 * 
	 * @param datatype
	 *            the datatype to set
	 */
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	
	/**
	 * Sets the effective date.
	 * 
	 * @param effectiveDate
	 *            the effectiveDate to set
	 */
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	/**
	 * Sets the mat value set.
	 * 
	 * @param matValueSet
	 *            the matValueSet to set
	 */
	public void setMatValueSet(MatValueSet matValueSet) {
		this.matValueSet = matValueSet;
	}
	
	/**
	 * Sets the measure id.
	 * 
	 * @param measureId
	 *            the measureId to set
	 */
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	
	/**
	 * Sets the quality data set dto.
	 * 
	 * @param qualityDataSetDTO
	 *            the qualityDataSetDTO to set
	 */
	public void setQualityDataSetDTO(QualityDataSetDTO qualityDataSetDTO) {
		this.qualityDataSetDTO = qualityDataSetDTO;
	}
	
	/**
	 * Sets the specific occurrence.
	 * 
	 * @param isSpecificOccurrence
	 *            the isSpecificOccurrence to set
	 */
	public void setSpecificOccurrence(boolean isSpecificOccurrence) {
		this.isSpecificOccurrence = isSpecificOccurrence;
	}
	
	/**
	 * Sets the user defined text.
	 * 
	 * @param userDefinedText
	 *            the userDefinedText to set
	 */
	public void setUserDefinedText(String userDefinedText) {
		this.userDefinedText = userDefinedText;
	}
	
	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
}
