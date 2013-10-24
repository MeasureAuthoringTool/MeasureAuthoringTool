package mat.server.service;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.OperatorDTO;
import mat.DTO.UnitDTO;
import mat.client.codelist.HasListBox;
import mat.client.codelist.ManageCodeListDetailModel;
import mat.client.codelist.ManageValueSetSearchModel;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.model.Code;
import mat.model.CodeListSearchDTO;
import mat.model.ListObject;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.model.User;
import mat.server.exception.ExcelParsingException;

public interface CodeListService {
	int countSearchResultsWithFilter(String searchText, boolean defaultCodeList, int filter);

	List<CodeListSearchDTO> search(String searchText, int startIndex, int pageSize, String sortColumn, boolean isAsc,
			boolean defaultCodeList, int filter);

	List<CodeListSearchDTO> search(String searchText, int startIndex, int pageSize, String sortColumn, boolean isAsc,
			boolean defaultCodeList, int filter, String categoryId);

	List<? extends HasListBox> getCodeListsForCategory(String category);

	List<Code> getCodes(String codeListId, int startIndex, int pageSize);

	ManageCodeListDetailModel getCodeList(String key);

	ManageCodeListDetailModel deleteCodes(String key, List<Code> Codes);

	ManageCodeListDetailModel getGroupedCodeList(String key);

	mat.client.codelist.service.CodeListService.ListBoxData getListBoxData();

	SaveUpdateCodeListResult saveorUpdateCodeList(ManageCodeListDetailModel currentDetails) throws CodeListNotUniqueException,
			CodeListOidNotUniqueException, ExcelParsingException, InvalidLastModifiedDateException,
			ValueSetLastModifiedDateNotUniqueException;

	SaveUpdateCodeListResult saveDefaultCodeList(User user) throws CodeListNotUniqueException;

	SaveUpdateCodeListResult saveorUpdateGroupedCodeList(ManageCodeListDetailModel currentDetails) throws CodeListNotUniqueException,
			CodeListOidNotUniqueException, InvalidLastModifiedDateException, ValueSetLastModifiedDateNotUniqueException;

	List<ListObject> getSupplimentalCodeList();

	List<? extends HasListBox> getCodeSystemsForCategory(String category);

	List<? extends HasListBox> getQDSDataTypeForCategory(String category);

	List<QualityDataSetDTO> getQDSElements(String measureId, String verision);

	String generateUniqueOid(ManageCodeListDetailModel currentDetails);

	String getGroupedCodeListCodeSystemsForCategory(String categoryId);

	ManageValueSetSearchModel searchValueSetsForDraft(int startIndex, int pageSize);

	ManageValueSetSearchModel createDraft(String id, String oid);

	boolean isCodeAlreadyExists(String codeListId, Code code);

	// US193
	ManageValueSetSearchModel createClone(String id);

	void transferOwnerShipToUser(List<String> list, String toEmail);

	List<OperatorDTO> getTimingOperators();

	List<UnitDTO> getAllUnits();

	List<OperatorDTO> getRelAssociationsOperators();

	List<OperatorDTO> getAllOperators();

	List<? extends HasListBox> getAllDataTypes();

	SaveUpdateCodeListResult saveUserDefinedQDStoMeasure(String measureId, String dataType, String codeListName,
			ArrayList<QualityDataSetDTO> appliedQDM);

	SaveUpdateCodeListResult saveQDStoMeasure(String measureId, String dataType, MatValueSet matValueSet, boolean isSpecificOccurrence,
			String version, ArrayList<QualityDataSetDTO> appliedQDM);

	SaveUpdateCodeListResult updateQDStoMeasure(String dataType, MatValueSet matValueSet, CodeListSearchDTO codeList,
			QualityDataSetDTO qualityDataSetDTO, boolean isSpecificOccurrence, String version, ArrayList<QualityDataSetDTO> appliedQDM);
}
