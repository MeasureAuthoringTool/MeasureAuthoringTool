package mat.client.shared;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.DTO.UnitDTO;
import mat.client.codelist.HasListBox;
import mat.model.cql.CQLKeywords;

public class CQLConstantContainer implements IsSerializable {
	
	private List<String> cqlAttributeList; 
	
	private List<String> cqlDatatypeList; 
	
	private List<String> qdmDatatypeList; 
		
	private List<UnitDTO> cqlUnitDTOList; 
	
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
	
	

}
