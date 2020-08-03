package mat.server.service;

import mat.client.measure.service.CheckForConversionResult;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.FhirLibraryPackageResult;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.MatException;
import mat.client.umls.service.VsacApiResult;
import mat.model.CQLLibraryOwnerReportDTO;
import mat.model.CQLValueSetTransferObject;
import mat.model.MatCodeTransferObject;
import mat.model.clause.CQLLibrary;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryAssociation;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.util.XmlProcessor;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.LibrarySearchModel;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.cql.error.InvalidLibraryException;
import mat.shared.error.AuthenticationException;

import java.util.List;

public interface CQLLibraryServiceInterface {

    SaveCQLLibraryResult search(LibrarySearchModel librarySearchModel);

    void save(CQLLibrary cqlLibrary);

    CQLLibraryDataSetObject extractCQLLibraryDataObject(CQLLibrary cqlLibrary);

    CQLLibraryDataSetObject findCQLLibraryByID(String cqlLibraryId);

    SaveCQLLibraryResult saveLibrary(CQLLibraryDataSetObject cqlLibraryDataSetObject);

    String createCQLLookUpTag(String libraryName, String version, boolean isFhir);

    XmlProcessor loadCQLXmlTemplateFile(boolean isFhir);

    SaveUpdateCQLResult getCQLData(String id);

    boolean isLibraryLocked(String id);

    SaveCQLLibraryResult resetLockedDate(String currentLibraryId, String userId);

    SaveCQLLibraryResult updateLockedDate(String currentLibraryId, String userId);

    SaveCQLLibraryResult getAllRecentCQLLibrariesForUser(String userId);

    void isLibraryAvailableAndLogRecentActivity(String libraryid, String userId);

    String getCQLLookUpXml(String libraryName, String versionText, XmlProcessor xmlProcessor, String mainXPath, boolean isFhir);

    SaveCQLLibraryResult saveFinalizedVersion(String libraryId, boolean isMajor, String version, boolean ignoreUnusedLibraries);

    SaveCQLLibraryResult draftExistingCQLLibrary(String libraryId, String libraryName) throws MatException;

    SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String libraryId, String context, String libraryComment, String description, String stewardId, boolean isExperimental);

    SaveUpdateCQLResult getLibraryCQLFileData(String libraryId);

    SaveCQLLibraryResult getUserShareInfo(String cqlId, String searchText);

    SaveCQLLibraryResult searchForIncludes(String setId, String libraryName, String searchText, String modelType);

    void updateUsersShare(SaveCQLLibraryResult result);

    SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String libraryId, CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) throws InvalidLibraryException;

    SaveUpdateCQLResult deleteInclude(String libraryId, CQLIncludeLibrary toBeModifiedIncludeObj);

    GetUsedCQLArtifactsResult getUsedCqlArtifacts(String libraryId);

    int countNumberOfAssociation(String id);

    SaveUpdateCQLResult saveCQLFile(String libraryId, String cql);

    SaveUpdateCQLResult saveAndModifyDefinitions(String libraryId, CQLDefinition toBeModifiedObj,
                                                 CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable);

    SaveUpdateCQLResult saveAndModifyFunctions(String libraryId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
                                               List<CQLFunctions> functionsList, boolean isFormatable);

    SaveUpdateCQLResult saveAndModifyParameters(String libraryId, CQLParameter toBeModifiedObj, CQLParameter currentObj,
                                                List<CQLParameter> parameterList, boolean isFormatable);

    SaveUpdateCQLResult deleteDefinition(String libraryId, CQLDefinition toBeDeletedObj);

    SaveUpdateCQLResult deleteFunction(String libraryId, CQLFunctions toBeDeletedObj);

    SaveUpdateCQLResult deleteParameter(String libraryId, CQLParameter toBeDeletedObj);

    SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject);

    SaveUpdateCQLResult deleteValueSet(String toBeDelValueSetId, String libraryId);

    VsacApiResult updateCQLVSACValueSets(String cqlLibraryId, String expansionId, String sessionId);

    CQLKeywords getCQLKeywordsLists();

    List<CQLLibraryAssociation> getAssociations(String Id);

    void transferLibraryOwnerShipToUser(List<String> list, String toEmail);

    List<CQLLibraryOwnerReportDTO> getCQLLibrariesForOwner();

    SaveUpdateCQLResult saveCQLCodestoCQLLibrary(MatCodeTransferObject transferObject);

    SaveUpdateCQLResult saveCQLCodeListToCQLLibrary(List<CQLCode> codeList, String libraryId);

    SaveUpdateCQLResult deleteCode(String toBeDeletedId, String libraryId);

    void deleteCQLLibrary(String cqllibId, String loginUserId) throws AuthenticationException;

    SaveUpdateCQLResult getCQLLibraryFileData(String libraryId);

    SaveCQLLibraryResult searchForReplaceLibraries(String setId);

    SaveUpdateCQLResult getCQLDataForLoad(String id);

    CQLQualityDataModelWrapper saveValueSetList(List<CQLValueSetTransferObject> transferObjectList,
                                                List<CQLQualityDataSetDTO> appliedValueSetList, String cqlLibraryId);

    void saveQdmCQLLibraryExport(CQLLibrary cqlLibrary, String cqlXML);

    void saveFhirCQLLibraryExport(CQLLibrary cqlLibrary, FhirLibraryPackageResult fhirPackageResults);

    CheckForConversionResult checkLibraryForConversion(CQLLibraryDataSetObject object);

    FhirConvertResultResponse convertCqlLibrary(CQLLibraryDataSetObject object) throws MatException;

    int deleteDraftFhirLibrariesBySetId(String setId);
}
