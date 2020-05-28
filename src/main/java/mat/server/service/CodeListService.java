package mat.server.service;

import java.util.List;

import mat.dto.DataTypeDTO;
import mat.dto.OperatorDTO;
import mat.dto.UnitDTO;
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
	
}
