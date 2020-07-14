package mat.client.measure.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.client.clause.clauseworkspace.model.MeasureDetailResult;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.TransferOwnerShipModel;
import mat.client.shared.GenericResult;
import mat.client.umls.service.VsacApiResult;
import mat.model.CQLValueSetTransferObject;
import mat.model.ComponentMeasureTabObject;
import mat.model.MatCodeTransferObject;
import mat.model.MatValueSet;
import mat.model.MeasureType;
import mat.model.Organization;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.RecentMSRActivityLog;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeWrapper;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CompositeMeasureValidationResult;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.MeasureSearchModel;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.measure.measuredetails.models.MeasureDetailsModel;


public interface MeasureServiceAsync {
	
	void appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName, AsyncCallback<Void> callback);
		
	/**
	 * Creates the and save element look up.
	 *
	 * @param list            the list
	 * @param measureID            the measure id
	 * @param expProfileToAllQDM the exp profile to all qdm
	 * @param callback            the callback
	 */
	void createAndSaveElementLookUp(List<QualityDataSetDTO> list,
			String measureID, String expProfileToAllQDM, AsyncCallback<Void> callback);
	void deleteValueSet(String toBeDeletedValueSetId, String measureID,
			 AsyncCallback<SaveUpdateCQLResult> callback);
	
	/**
	 * Creates the and save cql look up.
	 *
	 * @param list            the list
	 * @param measureID            the measure id
	 * @param expProfileToAllQDM the exp profile to all qdm
	 * @param callback            the callback
	 */
	void createAndSaveCQLLookUp(List<QualityDataSetDTO> list,
			String measureID, String expProfileToAllQDM, AsyncCallback<Void> callback);
	
	
	/** Gets the all recent measure for user.
	 * 
	 * @param userId the user id
	 * @param callback the callback
	 * @return the all recent measure for user */
	void getAllRecentMeasureForUser(String userId, AsyncCallback<ManageMeasureSearchModel> callback);
	
	/**
	 * Gets the applied qdm from measure xml.
	 *
	 * @param measureId            the measure id
	 * @param checkForSupplementData            the check for supplement data
	 * @param asyncCallback the async callback
	 * @return the applied qdm from measure xml
	 */
	void getAppliedQDMFromMeasureXml(String measureId,
			boolean checkForSupplementData,
			AsyncCallback<QualityDataModelWrapper> asyncCallback);
	
	void getCQLAppliedQDMFromMeasureXml(String measureId,
			boolean checkForSupplementData,
			AsyncCallback<CQLQualityDataModelWrapper> asyncCallback);
	
	/**
	 * Gets the max e measure id.
	 * 
	 * @param callback
	 *            the callback
	 * @return the max e measure id
	 */
	void getMaxEMeasureId(AsyncCallback<Integer> callback);
	
	/**
	 * Gets the measure.
	 * 
	 * @param key
	 *            the key
	 * @param callback
	 *            the callback
	 * @return the measure
	 */
	void getMeasure(String key, AsyncCallback<ManageMeasureDetailModel> callback);
	
	void getCompositeMeasure(String measureId, AsyncCallback<ManageCompositeMeasureDetailModel> callback);
	
	/**
	 * Gets the measure and logs in this measure as recently used measure in recent measure activity log.
	 *
	 * @param measureId the measure id
	 * @param userId the user id
	 * @param asyncCallback the callback
	 * @return the measure and log recent measure
	 */
	void getMeasureDetailsAndLogRecentMeasure(String measureId, String userId, AsyncCallback<MeasureDetailsModel> asyncCallback);
	
	/**
	 * Gets the measure xml for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param callback
	 *            the callback
	 * @return the measure xml for measure
	 */
	void getMeasureXmlForMeasure(String measureId,
			AsyncCallback<MeasureXmlModel> callback);
	
	/**
	 * Gets recently used measures by the given user ID in the descending order of time from recent measure activity log.
	 *
	 * @param userId the user id
	 * @param callback the callback
	 * @return the recent measure activity log
	 */
	void getRecentMeasureActivityLog(String userId, AsyncCallback<List<RecentMSRActivityLog>> callback);
	
	/**
	 * Gets the users for share.
	 * 
	 * @param userName
	 *            the user name entered for search 
	 * @param measureId
	 *            the measure id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param callback
	 *            the callback
	 * @return the users for share
	 */
	void getUsersForShare(String userName, String measureId, int startIndex, int pageSize, AsyncCallback<ManageMeasureShareModel> callback);
	
	/**
	 * Checks if is measure locked.
	 * 
	 * @param id
	 *            the id
	 * @param isLocked
	 *            the is locked
	 */
	void isMeasureLocked(String id, AsyncCallback<Boolean> isLocked);
	
	/**
	 * Reset locked date.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param userId
	 *            the user id
	 * @param callback
	 *            the callback
	 */
	void resetLockedDate(String measureId,String userId, AsyncCallback<SaveMeasureResult> callback);
	
	/**
	 * Save.
	 * 
	 * @param model
	 *            the model
	 * @param callback
	 *            the callback
	 */
	void saveNewMeasure(ManageMeasureDetailModel model, AsyncCallback<SaveMeasureResult> callback);
	
	void saveCompositeMeasure(ManageCompositeMeasureDetailModel model, AsyncCallback<SaveMeasureResult> callback);
	
	void deleteMeasure(String measureId, String loggedInUserId, AsyncCallback<Void> callback);
	
	/**
	 * Save finalized version.
	 * 
	 * @param measureid
	 *            the measureid
	 * @param isMajor
	 *            the is major
	 * @param version
	 *            the version
	 * @param callback
	 *            the callback
	 */
	void saveFinalizedVersion(String measureid, boolean isMajor,String version, boolean shouldPackage, boolean ignoreUnusedLibraries, AsyncCallback<SaveMeasureResult> callback);
	
	/**
	 * Save measure details.
	 * 
	 * @param model
	 *            the model
	 * @param callback
	 *            the callback
	 */
	void saveMeasureDetails(ManageMeasureDetailModel model,AsyncCallback<SaveMeasureResult> callback);
	
	/**
	 * Save measure xml.
	 * 
	 * @param measureXmlModel
	 *            the measure xml model
	 * @param callback
	 *            the callback
	 */
	void saveMeasureXml(MeasureXmlModel measureXmlModel, String measureId, boolean isFhir,
			AsyncCallback<Void> callback);
	
	/**
	 * Search.
	 * 
	 * @param advancedSearchModel
	 * 			the model the search is performed off of
	 * @param callback
	 *          the callback
	 */
	void search(MeasureSearchModel advancedSearchModel, AsyncCallback<ManageMeasureSearchModel> callback);
	
	/**
	 * Search users.
	 *
	 * @param searchText the search text
	 * @param startIndex the start index
	 * @param pageSize the page size
	 * @param callback the callback
	 */
	void searchUsers(String searchText, int startIndex, int pageSize,
			AsyncCallback<TransferOwnerShipModel> callback);
	
	/**
	 * Transfer owner ship to user.
	 * 
	 * @param list
	 *            the list
	 * @param toEmail
	 *            the to email
	 * @param callback
	 *            the callback
	 */
	void transferOwnerShipToUser(List<String> list, String toEmail,
			AsyncCallback<Void> callback);
	
	/**
	 * Update locked date.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param userId
	 *            the user id
	 * @param callback
	 *            the callback
	 */
	void updateLockedDate(String measureId,String userId, AsyncCallback<SaveMeasureResult> callback);
	
	/**
	 * Update measure xml.
	 * 
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param modifyDTO
	 *            the modify dto
	 * @param measureId
	 *            the measure id
	 * @param callback
	 *            the callback
	 */
	void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
			QualityDataSetDTO modifyDTO, String measureId,
			AsyncCallback<Void> callback);
	
	/**
	 * Update private column in measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param isPrivate
	 *            the is private
	 * @param callback
	 *            the callback
	 */
	void updatePrivateColumnInMeasure(String measureId, boolean isPrivate,
			AsyncCallback<Void> callback);
	
	/**
	 * Update users share.
	 * 
	 * @param model
	 *            the model
	 * @param callback
	 *            the callback
	 */
	void updateUsersShare(ManageMeasureShareModel model, AsyncCallback<Void> callback);
	/**
	 * Validate measure for export.
	 * 
	 * @param key
	 *            the key
	 * @param matValueSetList
	 *            the mat value set list
	 * @param callback
	 *            the callback
	 */
	void createExports(String key, List<MatValueSet> matValueSetList, boolean shouldCreateArtifacts, AsyncCallback<ValidateMeasureResult> callback);
	
	void validateExports(String keys,  AsyncCallback<ValidateMeasureResult> callback);
	
	/**
	 * Save Called To update Revision Number at Create New Package button Click.
	 *
	 * @param model -ManageMeasureDetailModel.
	 * @param callback the callback
	 */
	void saveMeasureAtPackage(ManageMeasureDetailModel model, AsyncCallback<SaveMeasureResult> callback);
	
	/**
	 * Save sub tree in measure xml.
	 *
	 * @param measureXmlModel the measure xml model
	 * @param nodeName the node name
	 * @param nodeUUID the node uuid
	 * @param callback the callback
	 */
	void saveSubTreeInMeasureXml(MeasureXmlModel measureXmlModel, String nodeName, String nodeUUID,
			AsyncCallback<SortedClauseMapResult> callback);
	
	/**
	 * Check and delete sub tree.
	 *
	 * @param measureId the measure id
	 * @param subTreeUUID the sub tree uuid
	 * @param callback the callback
	 */
	void checkAndDeleteSubTree(String measureId, String subTreeUUID, AsyncCallback<HashMap<String, String>> callback);
	
	/**
	 * Checks if is sub tree referred in logic.
	 *
	 * @param measureId the measure id
	 * @param subTreeUUID the sub tree uuid
	 * @param callback the callback
	 */
	void isSubTreeReferredInLogic(String measureId, String subTreeUUID,
			AsyncCallback<Boolean> callback);
	
	/**
	 * Gets the human readable for node.
	 *
	 * @param measureId the measure id
	 * @param populationSubXML the population sub xml
	 * @param callback the callback
	 * @return the human readable for node
	 */
	void getHumanReadableForNode(String measureId, String populationSubXML,
			AsyncCallback<String> callback);
	
	/**
	 * Gets the component measures.
	 *
	 * @param measureIds the measure ids
	 * @param callback the callback
	 * @return the component measures
	 */
	void getComponentMeasures(String measureId, AsyncCallback<ManageMeasureSearchModel> callback);
	
	/**
	 * Validate package grouping.
	 *
	 * @param model the model
	 * @param asyncCallback the async callback
	 */
	void validatePackageGrouping(ManageMeasureDetailModel model,
			AsyncCallback<ValidateMeasureResult> asyncCallback);
	
	/**
	 * Validate measure xmlinpopulation workspace.
	 *
	 * @param measureXmlModel the measure xml model
	 * @param asyncCallback the async callback
	 */
	void validateMeasureXmlinpopulationWorkspace(MeasureXmlModel measureXmlModel, AsyncCallback<ValidateMeasureResult> asyncCallback);
	
	
	/**
	 * Validate for group.
	 *
	 * @param model the model
	 * @param asyncCallback the async callback
	 */
	void validateForGroup(ManageMeasureDetailModel model,
			AsyncCallback<ValidateMeasureResult> asyncCallback);
	
	/**
	 * Validates and packages the measure when the user versions or packages a measure
	 * @param mode the measure details
	 * @param asyncCallback
	 */
	void validateAndPackageMeasure(ManageMeasureDetailModel mode, AsyncCallback<SaveMeasureResult> asyncCallback);
	
	/**
	 * Gets the all measure types.
	 *
	 * @param asyncCallback the async callback
	 * @return the all measure types
	 */
	void getAllMeasureTypes(AsyncCallback<List<MeasureType>> asyncCallback);
	
	/**
	 * Gets the all add edit authors.
	 *
	 * @param asyncCallback the async callback
	 * @return the all add edit authors
	 */
	void getAllOrganizations(AsyncCallback<List<Organization>> asyncCallback);
	
	/**
	 * Save sub tree occurrence.
	 *
	 * @param measureXmlModel the measure xml model
	 * @param nodeName the node name
	 * @param nodeUUID the node uuid
	 * @param callback the callback
	 */
	void saveSubTreeOccurrence(MeasureXmlModel measureXmlModel, String nodeName, String nodeUUID,
			AsyncCallback<SortedClauseMapResult> callback);
	
	/**
	 * Checks if is qdm variable enabled.
	 *
	 * @param measureId the measure id
	 * @param subTreeUUID the sub tree uuid
	 * @param callback the callback
	 */
	void isQDMVariableEnabled(String measureId, String subTreeUUID, AsyncCallback<Boolean> callback);
	
	/**
	 * Gets the sorted clause map.
	 *
	 * @param measureId the measure id
	 * @param callback the callback
	 * @return the sorted clause map
	 */
	void getSortedClauseMap(String measureId,
			AsyncCallback<LinkedHashMap<String, String>> callback);
	
	/**
	 * Gets the measure xml for measure and sorted sub tree map.
	 *
	 * @param currentMeasureId the current measure id
	 * @param Callback the callback
	 * @return the measure xml for measure and sorted sub tree map
	 */
	void getMeasureXmlForMeasureAndSortedSubTreeMap(
			String currentMeasureId,
			AsyncCallback<SortedClauseMapResult> Callback);
	
	/**
	 * Gets the used steward and developers list.
	 *
	 * @param measureId the measure id
	 * @param asyncCallback the async callback
	 * @return the used steward and developers list
	 */
	void getUsedStewardAndDevelopersList(String measureId,
			AsyncCallback<MeasureDetailResult> asyncCallback);
	
	/**
	 * Update measure xml for expansion identifier.
	 *
	 * @param list the list
	 * @param measureId the measure id
	 * @param expansionProfile the expansion profile
	 * @param callback the callback
	 */
	void updateMeasureXMLForExpansionIdentifier(List<QualityDataSetDTO> list, String measureId, String expansionProfile,
			AsyncCallback<Void> callback);
	
	/**
	 * Update CQL measure xml for expansion identifier.
	 *
	 * @param list the list
	 * @param measureId the measure id
	 * @param expansionProfile the expansion profile
	 * @param callback the callback
	 */
	void updateCQLMeasureXMLForExpansionProfile(List<CQLQualityDataSetDTO> list, String measureId, String expansionProfile,
			AsyncCallback<Void> callback);
	
	/**
	 * Gets the default sde from measure xml.
	 *
	 * @param measureId the measure id
	 * @param callback the callback
	 * @return the default sde from measure xml
	 */
	void getDefaultSDEFromMeasureXml(String measureId, AsyncCallback<QualityDataModelWrapper> callback);
	
	/**
	 * Gets the default sde from measure xml.
	 *
	 * @param measureId the measure id
	 * @param callback the callback
	 * @return the default sde from measure xml
	 */
	void getDefaultCQLSDEFromMeasureXml(String measureId, AsyncCallback<CQLQualityDataModelWrapper> callback);

	void saveCQLFile(String measureId, String cql, AsyncCallback<SaveUpdateCQLResult> asyncCallback);
	
	/**
	 * Save and modify definitions.
	 *
	 * @param measureId the measure id
	 * @param toBemodifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @param isFormatable flag for if the definition should be formatted on save
	 * @param callback the callback
	 */
	void saveAndModifyDefinitions(String measureId,
			CQLDefinition toBemodifiedObj, CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable,
			AsyncCallback<SaveUpdateCQLResult> callback);
	
	/**
	 * Save and modify parameters.
	 *
	 * @param measureId the measure id
	 * @param toBemodifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @param isFormtable flag for if the parameter should be formatted on save
	 * @param callback the callback
	 */
	void saveAndModifyParameters(String measureId, CQLParameter toBemodifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList, boolean isFormatable, AsyncCallback<SaveUpdateCQLResult> callback);
	
	/**
	 * Save and modify cql general info.
	 *
	 * @param currentMeasureId the current measure id
	 * @param context the context
	 * @param asyncCallback the async callback
	 */
	void saveAndModifyCQLGeneralInfo(String currentMeasureId, String context, String comments,
			AsyncCallback<SaveUpdateCQLResult> asyncCallback);
	
	
	/**
	 * Save and modify functions.
	 *
	 * @param measureId the measure id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param functionsList the functions list
	 * @param isFormtable flag for if the function should be formatted on save
	 * @param callback the callback
	 */
	void saveAndModifyFunctions(String measureId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList, boolean isFormatable, AsyncCallback<SaveUpdateCQLResult> callback);
	
	/**
	 * Delete definition
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param callback the callback
	 */
	void deleteDefinition(String measureId, CQLDefinition toBeDeletedObj, AsyncCallback<SaveUpdateCQLResult> callback); 	
	
	/**
	 * Delete functions 
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param callback the callback
	 */
	void deleteFunction(String measureId, CQLFunctions toBeDeletedObj, AsyncCallback<SaveUpdateCQLResult> callback);
	
	/**
	 * Delete parameter
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param callback the callback
	 */
	void deleteParameter(String measureId, CQLParameter toBeDeletedObj, AsyncCallback<SaveUpdateCQLResult> callback);
	
	void getUsedCQLArtifacts(String currentMeasureId, AsyncCallback<GetUsedCQLArtifactsResult> asyncCallback);

	/**
	 * Gets the CQL data type list.
	 *
	 * @param callback the callback
	 * @return the CQL data type list
	 */
	void getCQLKeywordsList(AsyncCallback<CQLKeywords> callback);
	
	void getJSONObjectFromXML(AsyncCallback<String> asyncCallback);

	void getCQLValusets(String measureID, AsyncCallback<CQLQualityDataModelWrapper> callback);
	
	void saveCQLValuesettoMeasure(CQLValueSetTransferObject valueSetTransferObject, 
			AsyncCallback<SaveUpdateCQLResult> callback);

	void saveIncludeLibrayInCQLLookUp(String measureId, CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj,
			List<CQLIncludeLibrary> incLibraryList, AsyncCallback<SaveUpdateCQLResult> callback);

	void getMeasureCQLData(String measureId, AsyncCallback<SaveUpdateCQLResult> callback);

	void getMeasureCQLFileData(String measureId, AsyncCallback<SaveUpdateCQLResult> callback);

	void deleteInclude(String currentMeasureId, CQLIncludeLibrary toBeModifiedIncludeObj, AsyncCallback<SaveUpdateCQLResult> asyncCallback);

	void updateCQLVSACValueSets(String currentMeasureId, String expansionId,
			AsyncCallback<VsacApiResult> asyncCallback);

	void saveCQLCodestoMeasure(MatCodeTransferObject transferObject, AsyncCallback<SaveUpdateCQLResult> callback);

	void saveCQLCodeListToMeasure(List<CQLCode> codeList, String measureId, AsyncCallback<SaveUpdateCQLResult> callback);
	
	void getCQLCodes(String measureID, AsyncCallback<CQLCodeWrapper> callback);

	void deleteCode(String toBeDeletedId, String measureID, AsyncCallback<SaveUpdateCQLResult> callback);

	void getMeasureCQLLibraryData(String measureId, AsyncCallback<SaveUpdateCQLResult> callback);
	
	void getMeasureCQLDataForLoad(String measureId, AsyncCallback<SaveUpdateCQLResult> callback);

	void saveValueSetList(List<CQLValueSetTransferObject> transferObjectList,
			List<CQLQualityDataSetDTO> appliedValueSetList, String measureId,
			AsyncCallback<CQLQualityDataModelWrapper> callback);

	void searchComponentMeasures(MeasureSearchModel searchModel, AsyncCallback<ManageMeasureSearchModel> asyncCallback);
	
	void buildCompositeMeasure(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel, AsyncCallback<ManageCompositeMeasureDetailModel> callback);

	void validateCompositeMeasure(ManageCompositeMeasureDetailModel currentCompositeMeasureDetails,
			AsyncCallback<CompositeMeasureValidationResult> asyncCallback);
	
	void getCQLLibraryInformationForComponentMeasure(String compositeMeasureId, AsyncCallback<List<ComponentMeasureTabObject>> callback);

	void checkIfMeasureIsUsedAsComponentMeasure(String currentMeasureId, AsyncCallback<GenericResult> asyncCallback);

	void isCompositeMeasure(String currentMeasureId, AsyncCallback<Boolean> compositeMeasureCallBack);

	void generateAndSaveMaxEmeasureId(boolean isEditable, String measureId, AsyncCallback<Integer> asyncCallback);

	void getHumanReadableForMeasureDetails(String currentMeasureId, String measureModel, AsyncCallback<String> asyncCallback);
	
    void checkIfLibraryNameExists(String libraryName, String setId, AsyncCallback<Boolean> callback);
}
