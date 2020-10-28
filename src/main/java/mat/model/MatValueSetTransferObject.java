package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.vsacmodel.ValueSet;

import java.util.List;


/**
 * The Class ValueSetTransferObject.
 */
public class MatValueSetTransferObject implements IsSerializable , BaseModel{
	
	/** The applied qdm list. */
	List<QualityDataSetDTO> appliedQDMList;
	
	/** The code list search dto. */
	CodeListSearchDTO codeListSearchDTO;
	
	/** The datatype. */
	String datatype;
	
	/** The is effective date. */
	boolean isEffectiveDate;
	
	/** The is specific occurrence. */
	boolean isSpecificOccurrence;
	
	/** The is version date. */
	boolean isVersionDate;
	
	boolean isExpansionProfile;
	
	/** The mat value set. */
	ValueSet ValueSet;
	
	/** The measure id. */
	String measureId;
	
	/** The quality data set dto. */
	QualityDataSetDTO qualityDataSetDTO;
	
	/** The Query date. */
	String queryDate;
	
	/** The user defined text. */
	String userDefinedText;
	
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
	 * Gets the mat value set.
	 * 
	 * @return the ValueSet
	 */
	public ValueSet getValueSet() {
		return ValueSet;
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
	 * Gets the Query date.
	 * 
	 * @return the queryDate
	 */
	public String getQueryDate() {
		return queryDate;
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
	 * Checks if is effective date.
	 * 
	 * @return the isEffectiveDate
	 */
	public boolean isEffectiveDate() {
		return isEffectiveDate;
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
	 * Checks if is version date.
	 * 
	 * @return the isVersionDate
	 */
	public boolean isVersionDate() {
		return isVersionDate;
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
	 * @param isEffectiveDate
	 *            the isEffectiveDate to set
	 */
	public void setEffectiveDate(boolean isEffectiveDate) {
		this.isEffectiveDate = isEffectiveDate;
	}
	
	/**
	 * Sets the mat value set.
	 * 
	 * @param ValueSet
	 *            the ValueSet to set
	 */
	public void setValueSet(ValueSet ValueSet) {
		this.ValueSet = ValueSet;
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
	 * Sets the Query date.
	 * 
	 * @param queryDate
	 *            the queryDate to set
	 */
	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
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
	 * Sets the version date.
	 * 
	 * @param isVersionDate
	 *            the isVersionDate to set
	 */
	public void setVersionDate(boolean isVersionDate) {
		this.isVersionDate = isVersionDate;
	}
	
	public boolean isExpansionProfile() {
		return isExpansionProfile;
	}
	
	public void setExpansionProfile(boolean isExpansionProfile) {
		this.isExpansionProfile = isExpansionProfile;
	}
	
	@Override
	public void scrubForMarkUp() {
		String markupRegExp = "<[^>]+>";
		if ((this.getUserDefinedText() != null) && !this.getUserDefinedText().isEmpty()) {
			String noMarkupText = this.getUserDefinedText().trim().replaceAll(markupRegExp, "");
			System.out.println("QDM User defined name:" + noMarkupText);
			if (this.getUserDefinedText().trim().length() > noMarkupText.length()) {
				this.setUserDefinedText(noMarkupText);
			}
		}
		if (this.getValueSet() != null) {
			String noMarkupText = this.getValueSet().getDisplayName().trim().replaceAll(markupRegExp, "");
			System.out.println("QDM VSAC Value set name:" + noMarkupText);
			if (this.getValueSet().getDisplayName().trim().length() > noMarkupText.length()) {
				this.getValueSet().setDisplayName(noMarkupText);
			}
		}
		if (this.getQualityDataSetDTO() != null) {
			String noMarkupText = this.getQualityDataSetDTO().getCodeListName().trim().replaceAll(markupRegExp, "");
			System.out.println("QDM To Be Modified VSAC Value set name:" + noMarkupText);
			if (this.getQualityDataSetDTO().getCodeListName().trim().length() > noMarkupText.length()) {
				this.getQualityDataSetDTO().setCodeListName(noMarkupText);
			}
		}
		if (this.getCodeListSearchDTO() != null) {
			String noMarkupText = this.getCodeListSearchDTO().getName().trim().replaceAll(markupRegExp, "");
			System.out.println("QDM To Be Modified USD Value set name:" + noMarkupText);
			if (this.getCodeListSearchDTO().getName().trim().length() > noMarkupText.length()) {
				this.getCodeListSearchDTO().setName(noMarkupText);
			}
		}
	}
	
}
