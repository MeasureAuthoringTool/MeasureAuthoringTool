package mat.server.service;

import java.util.List;
import java.util.Map;

import mat.dto.DataTypeDTO;
import mat.dto.OperatorDTO;
import mat.dto.UnitDTO;
import mat.dto.VSACCodeSystemDTO;
import mat.client.codelist.HasListBox;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.model.CodeListSearchDTO;
import mat.model.MatValueSetTransferObject;
import mat.model.QualityDataSetDTO;

public interface CodeListService {
	
	int countSearchResultsWithFilter(String searchText, boolean defaultCodeList, int filter);
	
	List<DataTypeDTO> getAllDataTypes();
	
	List<OperatorDTO> getAllOperators();

	List<UnitDTO> getAllUnits();
	
	List<? extends HasListBox> getCodeListsForCategory(String category);
	
	List<? extends HasListBox> getCodeSystemsForCategory(String category);

	mat.client.codelist.service.CodeListService.ListBoxData getListBoxData();
	
	List<? extends HasListBox> getQDSDataTypeForCategory(String category);
	
	List<QualityDataSetDTO> getQDSElements(String measureId, String verision);

	SaveUpdateCodeListResult saveQDStoMeasure(MatValueSetTransferObject valueSetTransferObject);

	SaveUpdateCodeListResult saveUserDefinedQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject);
	
	List<CodeListSearchDTO> search(String searchText, int startIndex, int pageSize, String sortColumn, boolean isAsc,
			boolean defaultCodeList, int filter);
	
	SaveUpdateCodeListResult updateQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject);

	/**
	 * @return Returns a hash map keyed by oid, e.g. urn:oid:2.16.840.1.113883.12.292, and valued by VSACCodeSystemDTO with
	 * fhir model 4.0.1 url, e.g. http://hl7.org/fhir/sid/cvx, and default VSAC version.
	 */
	public Map<String, VSACCodeSystemDTO> getOidToVsacCodeSystemMap();
}
