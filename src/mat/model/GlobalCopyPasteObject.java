package mat.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLQualityDataSetDTO;

public class GlobalCopyPasteObject implements IsSerializable{
	
	//private List<QualityDataSetDTO> copiedQDMList = new ArrayList<QualityDataSetDTO>();
	
	private List<CQLQualityDataSetDTO> copiedValueSetList = new ArrayList<CQLQualityDataSetDTO>();
	
	private List<CQLCode> copiedCodeList = new ArrayList<CQLCode>();
	
	private List<CQLCodeSystem> copiedCodeSystemList = new  ArrayList<CQLCodeSystem>();
	
	private String currentMeasureId;
	
	private String currentLibraryId;
	
//	private List<MatValueSetTransferObject> matValueSetList;
	/**
	 * @return the copiedQDMList
	 */
	/*public List<QualityDataSetDTO> getCopiedQDMList() {
		return copiedQDMList;
	}
	
	*//**
	 * @param copiedQDMList the copiedQDMList to set
	 *//*
	public void setCopiedQDMList(List<QualityDataSetDTO> copiedQDMList) {
		this.copiedQDMList = copiedQDMList;
	}*/
	/**
	 * @return the currentMeasureId
	 */
	public String getCurrentMeasureId() {
		return currentMeasureId;
	}
	/**
	 * @param currentMeasureId the currentMeasureId to set
	 */
	public void setCurrentMeasureId(String currentMeasureId) {
		this.currentMeasureId = currentMeasureId;
	}
	/*public List<MatValueSetTransferObject> getMatValueSetList() {
		return matValueSetList;
	}
	public void setMatValueSetList(List<MatValueSetTransferObject> matValueSetList) {
		this.matValueSetList = matValueSetList;
	}*/
	/**
	 * 
	 */
	/*public void setMatValueSetListFromQDS(String defaultExpansionProfile) {
		matValueSetList = new ArrayList<MatValueSetTransferObject>();
		for (QualityDataSetDTO dataSetDTO : copiedQDMList) {
			MatValueSetTransferObject mvsto = new MatValueSetTransferObject();
			mvsto.setDatatype(dataSetDTO.getDataType());
			if (!dataSetDTO.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
				MatValueSet currentMatValueSet = new MatValueSet();
				currentMatValueSet.setDisplayName(dataSetDTO.getCodeListName());
				if (!((defaultExpansionProfile != null) && (defaultExpansionProfile != ""))) {
					if ((dataSetDTO.getExpansionIdentifier() != null) && (dataSetDTO.getExpansionIdentifier() != "")) {
						mvsto.setExpansionProfile(true);
						currentMatValueSet.setExpansionProfile(dataSetDTO.getExpansionIdentifier());
					} else {
						mvsto.setExpansionProfile(false);
					}
					if (dataSetDTO.getVersion().equalsIgnoreCase("1.0")) {
						mvsto.setVersionDate(false);
					} else {
						mvsto.setVersionDate(true);
						currentMatValueSet.setVersion(dataSetDTO.getVersion());
					}
				} else {
					mvsto.setExpansionProfile(true);
					currentMatValueSet.setExpansionProfile(defaultExpansionProfile);
				}
				if (dataSetDTO.getTaxonomy().equalsIgnoreCase(ConstantMessages.GROUPING_CODE_SYSTEM)) {
					currentMatValueSet.setType(ConstantMessages.GROUPING_CODE_SYSTEM);
				} else {
					currentMatValueSet.setType(dataSetDTO.getTaxonomy());
					MatConceptList conceptList = new MatConceptList();
					MatConcept concept = new MatConcept();
					concept.setCodeSystemName(dataSetDTO.getTaxonomy());
					List<MatConcept> concepts = new ArrayList<MatConcept>();
					concepts.add(concept);
					conceptList.setConceptList(concepts);
					currentMatValueSet.setConceptList(conceptList);
				}
				currentMatValueSet.setID(dataSetDTO.getOid());
				mvsto.setMatValueSet(currentMatValueSet);
				mvsto.setMeasureId(MatContext.get().getCurrentMeasureId());
				if (dataSetDTO.getOccurrenceText() != null) {
					mvsto.setSpecificOccurrence(true);
				}
			} else {
				mvsto.setUserDefinedText(dataSetDTO.getCodeListName());
			}
			matValueSetList.add(mvsto);
		}
	}*/

	public List<CQLQualityDataSetDTO> getCopiedValueSetList() {
		return copiedValueSetList;
	}

	public void setCopiedValueSetList(List<CQLQualityDataSetDTO> copiedValueSetList) {
		this.copiedValueSetList = copiedValueSetList;
	}

	public List<CQLCode> getCopiedCodeList() {
		return copiedCodeList;
	}

	public void setCopiedCodeList(List<CQLCode> copiedCodeList) {
		this.copiedCodeList = copiedCodeList;
	}

	public List<CQLCodeSystem> getCopiedCodeSystemList() {
		return copiedCodeSystemList;
	}

	public void setCopiedCodeSystemList(List<CQLCodeSystem> copiedCodeSystemList) {
		this.copiedCodeSystemList = copiedCodeSystemList;
	}

	public String getCurrentLibraryId() {
		return currentLibraryId;
	}

	public void setCurrentLibraryId(String currentLibraryId) {
		this.currentLibraryId = currentLibraryId;
	}

}
