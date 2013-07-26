package mat.client.codelist.service;

import java.util.ArrayList;

import mat.client.codelist.ManageCodeListDetailModel;
import mat.client.shared.GenericResult;
import mat.model.QualityDataSetDTO;

public class SaveUpdateCodeListResult extends GenericResult {
	public static final int NOT_UNIQUE = 1;
	public static final int CODES_NOT_UNIQUE = 2;
	public static final int OID_NOT_UNIQUE = 3;
	public static final int SERVER_SIDE_VALIDATION = 4;
	public static final int INVALID_LAST_MODIFIED_DATE = 5;
	public static final int LAST_MODIFIED_DATE_DUPLICATE = 6;
	public static final int ALREADY_EXISTS=7;
	private String id;
	private String codeId;
	private String codeListName;
	private boolean isDuplicateExists;
	private boolean isAllCodesDups;
	private String occurrenceMessage;
	private String lastModifiedDate;
	private String xmlString;
	ArrayList<QualityDataSetDTO> appliedQDMList ;
	QualityDataSetDTO dataSetDTO;
	
	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}

	private ManageCodeListDetailModel codeListDetailModel;
	
	private int duplicateCount;
	
	public String getOccurrenceMessage() {
		return occurrenceMessage;
	}

	public void setOccurrenceMessage(String occurrenceMessage) {
		this.occurrenceMessage = occurrenceMessage;
	}

	public boolean isDuplicateExists() {
		return isDuplicateExists;
	}

	public void setDuplicateExists(boolean isDuplicateExists) {
		this.isDuplicateExists = isDuplicateExists;
	}

	public int getDuplicateCount() {
		return duplicateCount;
	}

	public void setDuplicateCount(int duplicateCount) {
		this.duplicateCount = duplicateCount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getCodeListName() {
		return codeListName;
	}

	public void setCodeListName(String codeListName) {
		this.codeListName = codeListName;
	}
	
	public boolean isAllCodesDups() {
		return isAllCodesDups;
	}

	public void setAllCodesDups(boolean isAllCodesDups) {
		this.isAllCodesDups = isAllCodesDups;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public ManageCodeListDetailModel getCodeListDetailModel() {
		return codeListDetailModel;
	}

	public void setCodeListDetailModel(ManageCodeListDetailModel codeListDetailModel) {
		this.codeListDetailModel = codeListDetailModel;
	}

	public ArrayList<QualityDataSetDTO> getAppliedQDMList() {
		return appliedQDMList;
	}

	public void setAppliedQDMList(ArrayList<QualityDataSetDTO> appliedQDMList) {
		this.appliedQDMList = appliedQDMList;
	}

	public QualityDataSetDTO getDataSetDTO() {
		return dataSetDTO;
	}

	public void setDataSetDTO(QualityDataSetDTO dataSetDTO) {
		this.dataSetDTO = dataSetDTO;
	}

	
}
