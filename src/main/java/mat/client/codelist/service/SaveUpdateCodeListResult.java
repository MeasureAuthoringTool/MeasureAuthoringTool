package mat.client.codelist.service;

import mat.client.shared.GenericResult;
import mat.model.QualityDataSetDTO;
import mat.model.cql.CQLQualityDataSetDTO;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveUpdateCodeListResult.
 */
public class SaveUpdateCodeListResult extends GenericResult {
	
	/** The Constant NOT_UNIQUE. */
	public static final int NOT_UNIQUE = 1;
	
	/** The Constant CODES_NOT_UNIQUE. */
	public static final int CODES_NOT_UNIQUE = 2;
	
	/** The Constant OID_NOT_UNIQUE. */
	public static final int OID_NOT_UNIQUE = 3;
	
	/** The Constant SERVER_SIDE_VALIDATION. */
	public static final int SERVER_SIDE_VALIDATION = 4;
	
	/** The Constant INVALID_LAST_MODIFIED_DATE. */
	public static final int INVALID_LAST_MODIFIED_DATE = 5;
	
	/** The Constant LAST_MODIFIED_DATE_DUPLICATE. */
	public static final int LAST_MODIFIED_DATE_DUPLICATE = 6;
	
	/** The Constant ALREADY_EXISTS. */
	public static final int ALREADY_EXISTS=7;
	
	/** The id. */
	private String id;
	
	/** The code id. */
	private String codeId;
	
	/** The code list name. */
	private String codeListName;
	
	/** The is duplicate exists. */
	private boolean isDuplicateExists;
	
	/** The is all codes dups. */
	private boolean isAllCodesDups;
	
	/** The occurrence message. */
	private String occurrenceMessage;
	
	/** The last modified date. */
	private String lastModifiedDate;
	
	/** The xml string. */
	private String xmlString;
	
	/** The xml string. */
	private String newXmlString;
	
	/** The applied qdm list. */
	List<QualityDataSetDTO> appliedQDMList ;
	
	/** The cql applied QDM list. */
	List<CQLQualityDataSetDTO> cqlAppliedQDMList ;
	
	/** The data set dto. */
	QualityDataSetDTO dataSetDTO;
	
	CQLQualityDataSetDTO cqlQualityDataSetDTO;
	
	/** The all oi ds updated. */
	boolean allOIDsUpdated;


	/**
	 * Gets the xml string.
	 * 
	 * @return the xml string
	 */
	public String getXmlString() {
		return xmlString;
	}

	/**
	 * Sets the new xml string.
	 * 
	 * @param newXmlString
	 *            the new xml string
	 */
	public void setnewXmlString(String newXmlString) {
		this.newXmlString = newXmlString;
	}
	
	/**
	 * Gets the new xml string.
	 * 
	 * @return the new xml string
	 */
	public String getnewXmlString() {
		return newXmlString;
	}

	/**
	 * Sets the xml string.
	 * 
	 * @param xmlString
	 *            the new xml string
	 */
	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}

	/** The code list detail model. */
	//private ManageCodeListDetailModel codeListDetailModel;
	
	/** The duplicate count. */
	private int duplicateCount;
	
	/**
	 * Gets the occurrence message.
	 * 
	 * @return the occurrence message
	 */
	public String getOccurrenceMessage() {
		return occurrenceMessage;
	}

	/**
	 * Sets the occurrence message.
	 * 
	 * @param occurrenceMessage
	 *            the new occurrence message
	 */
	public void setOccurrenceMessage(String occurrenceMessage) {
		this.occurrenceMessage = occurrenceMessage;
	}

	/**
	 * Checks if is duplicate exists.
	 * 
	 * @return true, if is duplicate exists
	 */
	public boolean isDuplicateExists() {
		return isDuplicateExists;
	}

	/**
	 * Sets the duplicate exists.
	 * 
	 * @param isDuplicateExists
	 *            the new duplicate exists
	 */
	public void setDuplicateExists(boolean isDuplicateExists) {
		this.isDuplicateExists = isDuplicateExists;
	}

	/**
	 * Gets the duplicate count.
	 * 
	 * @return the duplicate count
	 */
	public int getDuplicateCount() {
		return duplicateCount;
	}

	/**
	 * Sets the duplicate count.
	 * 
	 * @param duplicateCount
	 *            the new duplicate count
	 */
	public void setDuplicateCount(int duplicateCount) {
		this.duplicateCount = duplicateCount;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the code id.
	 * 
	 * @return the code id
	 */
	public String getCodeId() {
		return codeId;
	}

	/**
	 * Sets the code id.
	 * 
	 * @param codeId
	 *            the new code id
	 */
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	/**
	 * Gets the code list name.
	 * 
	 * @return the code list name
	 */
	public String getCodeListName() {
		return codeListName;
	}

	/**
	 * Sets the code list name.
	 * 
	 * @param codeListName
	 *            the new code list name
	 */
	public void setCodeListName(String codeListName) {
		this.codeListName = codeListName;
	}
	
	/**
	 * Checks if is all codes dups.
	 * 
	 * @return true, if is all codes dups
	 */
	public boolean isAllCodesDups() {
		return isAllCodesDups;
	}

	/**
	 * Sets the all codes dups.
	 * 
	 * @param isAllCodesDups
	 *            the new all codes dups
	 */
	public void setAllCodesDups(boolean isAllCodesDups) {
		this.isAllCodesDups = isAllCodesDups;
	}

	/**
	 * Gets the last modified date.
	 * 
	 * @return the last modified date
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * Sets the last modified date.
	 * 
	 * @param lastModifiedDate
	 *            the new last modified date
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * Gets the code list detail model.
	 * 
	 * @return the code list detail model
	 */
	/*public ManageCodeListDetailModel getCodeListDetailModel() {
		return codeListDetailModel;
	}*/

	/**
	 * Sets the code list detail model.
	 * 
	 * @param codeListDetailModel
	 *            the new code list detail model
	 */
	/*public void setCodeListDetailModel(ManageCodeListDetailModel codeListDetailModel) {
		this.codeListDetailModel = codeListDetailModel;
	}
*/
	/**
	 * Gets the applied qdm list.
	 * 
	 * @return the applied qdm list
	 */
	public List<QualityDataSetDTO> getAppliedQDMList() {
		return appliedQDMList;
	}

	/**
	 * Sets the applied qdm list.
	 * 
	 * @param appliedQDMList
	 *            the new applied qdm list
	 */
	public void setAppliedQDMList(List<QualityDataSetDTO> appliedQDMList) {
		this.appliedQDMList = appliedQDMList;
	}

	/**
	 * Gets the data set dto.
	 * 
	 * @return the data set dto
	 */
	public QualityDataSetDTO getDataSetDTO() {
		return dataSetDTO;
	}

	/**
	 * Sets the data set dto.
	 * 
	 * @param dataSetDTO
	 *            the new data set dto
	 */
	public void setDataSetDTO(QualityDataSetDTO dataSetDTO) {
		this.dataSetDTO = dataSetDTO;
	}
	
	/**
	 * Checks if is all oi ds updated.
	 *
	 * @return true, if is all oi ds updated
	 */
	public boolean isAllOIDsUpdated() {
		return allOIDsUpdated;
	}

	/**
	 * Sets the all oi ds updated.
	 *
	 * @param allOIDsUpdated the new all oi ds updated
	 */
	public void setAllOIDsUpdated(boolean allOIDsUpdated) {
		this.allOIDsUpdated = allOIDsUpdated;
	}

	/**
	 * Gets the cql applied QDM list.
	 *
	 * @return the cql applied QDM list
	 */
	public List<CQLQualityDataSetDTO> getCqlAppliedQDMList() {
		return cqlAppliedQDMList;
	}

	/**
	 * Sets the cql applied QDM list.
	 *
	 * @param cqlAppliedQDMList the new cql applied QDM list
	 */
	public void setCqlAppliedQDMList(List<CQLQualityDataSetDTO> cqlAppliedQDMList) {
		this.cqlAppliedQDMList = cqlAppliedQDMList;
	}

	public CQLQualityDataSetDTO getCqlQualityDataSetDTO() {
		return cqlQualityDataSetDTO;
	}

	public void setCqlQualityDataSetDTO(CQLQualityDataSetDTO cqlQualityDataSetDTO) {
		this.cqlQualityDataSetDTO = cqlQualityDataSetDTO;
	}
	
}
