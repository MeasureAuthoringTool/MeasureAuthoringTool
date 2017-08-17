package mat.client.shared;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.DTO.UnitDTO;
import mat.model.cql.CQLKeywords;

public class CQLConstantContainer implements IsSerializable {
	private String currentQDMVersion;
	private String currentReleaseVersion;
	private List<String> cqlAttributeList; 
	
	private List<String> cqlDatatypeList; 
	
	private List<String> qdmDatatypeList; 
		
	private List<UnitDTO> cqlUnitDTOList; 
	
	private List<String> cqlTimingList; 
	
	private Map<String, String> cqlUnitMap;
	
	private CQLKeywords cqlKeywordList; 

	public List<String> getCqlAttributeList() {
		return cqlAttributeList;
	}

	public void setCqlAttributeList(List<String> cqlAttributeList) {
		this.cqlAttributeList = cqlAttributeList;
	}

	public List<String> getCqlDatatypeList() {
		return cqlDatatypeList;
	}

	public void setCqlDatatypeList(List<String> cqlDatatypeList) {
		this.cqlDatatypeList = cqlDatatypeList;
	}

	public Map<String, String> getCqlUnitMap() {
		return cqlUnitMap;
	}

	public void setCqlUnitMap(Map<String, String> cqlUnitMap) {
		this.cqlUnitMap = cqlUnitMap;
	}

	public List<UnitDTO> getCqlUnitDTOList() {
		return cqlUnitDTOList;
	}

	public void setCqlUnitDTOList(List<UnitDTO> cqlUnitDTOList) {
		this.cqlUnitDTOList = cqlUnitDTOList;
	}

	public List<String> getQdmDatatypeList() {
		return qdmDatatypeList;
	}

	public void setQdmDatatypeList(List<String> qdmDatatypeList) {
		this.qdmDatatypeList = qdmDatatypeList;
	}

	public CQLKeywords getCqlKeywordList() {
		return cqlKeywordList;
	}

	public void setCqlKeywordList(CQLKeywords keywordList) {
		this.cqlKeywordList = keywordList;
	}

	public List<String> getCqlTimingList() {
		return cqlTimingList;
	}

	public void setCqlTimingList(List<String> cqlTimingList) {
		this.cqlTimingList = cqlTimingList;
	}

	public String getCurrentQDMVersion() {
		return currentQDMVersion;
	}

	public void setCurrentQDMVersion(String currentQDMVersion) {
		this.currentQDMVersion = currentQDMVersion;
	}

	public String getCurrentReleaseVersion() {
		return currentReleaseVersion;
	}

	public void setCurrentReleaseVersion(String currentReleaseVersion) {
		this.currentReleaseVersion = currentReleaseVersion;
	} 
}
