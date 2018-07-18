package mat.client.measure.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.client.clause.clauseworkspace.model.MeasureDetailResult;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.TransferOwnerShipModel;
import mat.client.shared.MatException;
import mat.client.umls.service.VsacApiResult;
import mat.model.CQLValueSetTransferObject;
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
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.MeasureSearchModel;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

/**
 * The Interface MeasureService.
 */
@RemoteServiceRelativePath("measureLibrary")
public interface MeasureService extends RemoteService {
	
	/**
	 * Append and save node.
	 * 
	 * @param measureXmlModel
	 *            the measure xml model
	 * @param nodeName
	 *            the node name
	 * @param measureXmlModel
	 *            the new measure xml model
	 * @param nodeName
	 *            the new node name
	 */
	void appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName);
	
	/**
	 * Clone measure xml.
	 *
	 * @param creatingDraft            the creating draft	 * @param oldMeasureId
	 *            the old measure id
	 * @param oldMeasureId the old measure id
	 * @param clonedMeasureId            the cloned measure id
	 */
	void cloneMeasureXml(boolean creatingDraft, String oldMeasureId, String clonedMeasureId);
	
	/**
	 * Creates the and save element look up.
	 *
	 * @param list            the list
	 * @param measureID            the measure id
	 * @param expProfileToAllQDM the exp profile to all qdm
	 */
	void createAndSaveElementLookUp(List<QualityDataSetDTO> list,
			String measureID, String expProfileToAllQDM);
	
	void createAndSaveCQLLookUp(List<QualityDataSetDTO> list, String measureID, String expProfileToAllQDM);
	
	/**
	 * Generate and save max emeasure id.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the int
	 */
	int generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId);
	
	
	/** Gets the all recent measure for user.
	 * 
	 * @param userId the user id
	 * @return the all recent measure for user */
	ManageMeasureSearchModel getAllRecentMeasureForUser(String userId);
	
	/**
	 * Gets the applied qdm from measure xml.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param checkForSupplementData
	 *            the check for supplement data
	 * @return the applied qdm from measure xml
	 */
	//	List<QualityDataSetDTO> getAppliedQDMFromMeasureXml(String measureId,
	//			boolean checkForSupplementData);
	
	QualityDataModelWrapper getAppliedQDMFromMeasureXml(String measureId,
			boolean checkForSupplementData);
	
	CQLQualityDataModelWrapper getCQLAppliedQDMFromMeasureXml(String measureId,
			boolean checkForSupplementData);
	
	/**
	 * Gets the max e measure id.
	 * 
	 * @return the max e measure id
	 */
	int getMaxEMeasureId();
	
	/**
	 * Gets the measure.
	 * 
	 * @param key
	 *            the key
	 * @return the measure
	 */
	ManageMeasureDetailModel getMeasure(String key);
	
	/**
	 * Gets the measure and logs in this measure as recently used measure in recent measure activity log.
	 *
	 * @param measureId the measure id
	 * @param userId the user id
	 * @return the measure and log recent measure
	 */
	ManageMeasureDetailModel getMeasureAndLogRecentMeasure(String measureId, String userId);
	
	/**
	 * Gets the measure xml for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure xml for measure
	 */
	MeasureXmlModel getMeasureXmlForMeasure(String measureId);
	
	/**
	 * Gets recently used measures by the given user ID in the descending order of time from recent measure activity log.
	 *
	 * @param userId the user id
	 * @return the recent measure activity log
	 */
	List<RecentMSRActivityLog> getRecentMeasureActivityLog(String userId);
	
	/**
	 * Gets the users for share.
	 * 
	 * @param userName
	 *            the user name
	 * @param measureId
	 *            the measure id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the users for share
	 */
	ManageMeasureShareModel getUsersForShare(String userName, String measureId, int startIndex, int pageSize);
	
	/**
	 * Checks if is measure locked.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is measure locked
	 */
	boolean isMeasureLocked(String id);
	
	/**
	 * Reset locked date.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param userId
	 *            the user id
	 * @return the save measure result
	 */
	SaveMeasureResult resetLockedDate(String measureId,String userId);
	
	/**
	 * Save.
	 * 
	 * @param model
	 *            the model
	 * @return the save measure result
	 */
	SaveMeasureResult save(ManageMeasureDetailModel model);
	
	/**
	 * Save Called To update Revision Number at Create New Package button Click.
	 * @param model -ManageMeasureDetailModel.
	 * @return - SaveMeasureResult.
	 */
	SaveMeasureResult saveMeasureAtPackage(ManageMeasureDetailModel model);
	
	/**
	 * Save and delete measure.
	 * 
	 * @param measureID
	 *            the measure id
	 *            
	 * @param loginUserId
	 *            the loginUser id           
	 */
	void saveAndDeleteMeasure(String measureID, String loginUserId);
	
	/**
	 * Save finalized version.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param isMajor
	 *            the is major
	 * @param version
	 *            the version
	 * @return the save measure result
	 */
	SaveMeasureResult saveFinalizedVersion(String measureId, boolean isMajor,String version, boolean shouldPackage, boolean ignoreUnusedLibraries) ;
	
	/**
	 * Save measure details.
	 * 
	 * @param model
	 *            the model
	 * @return the save measure result
	 */
	SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model);

	/**
	 * Save measure xml.
	 * 
	 * @param measureXmlModel
	 *            the measure xml model
	 */
	void saveMeasureXml(MeasureXmlModel measureXmlModel);
	
	/**
	 * Search.
	 * 
	 * @param searchText
	 *            the search text
	 * @param advancedSearchModel
	 * 			the model that the search method uses to search
	 * @return the manage measure search model
	 */
	ManageMeasureSearchModel search(MeasureSearchModel advancedSearchModel);
	
	/**
	 * Search users.
	 *
	 * @param searchText the search text
	 * @param startIndex the start index
	 * @param pageSize the page size
	 * @return the transfer measure owner ship model
	 */
	TransferOwnerShipModel searchUsers(String searchText, int startIndex, int pageSize);
	
	/**
	 * Transfer owner ship to user.
	 * 
	 * @param list
	 *            the list
	 * @param toEmail
	 *            the to email
	 */
	void transferOwnerShipToUser(List<String> list, String toEmail);
	
	/**
	 * Update locked date.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param userId
	 *            the user id
	 * @return the save measure result
	 */
	SaveMeasureResult updateLockedDate(String measureId,String userId);
	
	/**
	 * Update measure xml.
	 * 
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param modifyDTO
	 *            the modify dto
	 * @param measureId
	 *            the measure id
	 */
	void updateMeasureXML(QualityDataSetDTO modifyWithDTO,
			QualityDataSetDTO modifyDTO, String measureId);
	
	/**
	 * Update private column in measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param isPrivate
	 *            the is private
	 */
	void updatePrivateColumnInMeasure(String measureId, boolean isPrivate);
	
	/**
	 * Update users share.
	 * 
	 * @param model
	 *            the model
	 */
	void updateUsersShare(ManageMeasureShareModel model);
	/**
	 * Validate measure for export.
	 * 
	 * @param key
	 *            the key
	 * @param matValueSetList
	 *            the mat value set list
	 * @return the validate measure result
	 * @throws MatException
	 *             the mat exception
	 */
	ValidateMeasureResult validateMeasureForExport(String key,
			List<MatValueSet> matValueSetList) throws MatException;
	
	/**
	 * Save sub tree in measure xml.
	 *
	 * @param measureXmlModel the measure xml model
	 * @param nodeName the node name
	 * @param nodeUUID the node uuid
	 * @return the sorted clause map result
	 */
	SortedClauseMapResult saveSubTreeInMeasureXml(MeasureXmlModel measureXmlModel,
			String nodeName, String nodeUUID);
	
	/**
	 * Check and delete sub tree.
	 *
	 * @param measureId the measure id
	 * @param subTreeUUID the sub tree uuid
	 * @return true, if successful
	 */
	HashMap<String, String> checkAndDeleteSubTree(String measureId, String subTreeUUID);
	
	/**
	 * Checks if is sub tree referred in logic.
	 *
	 * @param measureId the measure id
	 * @param subTreeUUID the sub tree uuid
	 * @return true, if is sub tree referred in logic
	 */
	boolean isSubTreeReferredInLogic(String measureId, String subTreeUUID);
	
	/**
	 * Gets the human readable for node.
	 *
	 * @param measureId the measure id
	 * @param populationSubXML the population sub xml
	 * @return the human readable for node
	 */
	String getHumanReadableForNode(String measureId, String populationSubXML);
	
	/**
	 * Gets the component measures.
	 *
	 * @param measureIds the measure ids
	 * @return the component measures
	 */
	ManageMeasureSearchModel getComponentMeasures(List<String> measureIds);
	
	/**
	 * Validate package grouping.
	 *
	 * @param model the model
	 * @return true, if successful
	 */
	ValidateMeasureResult validatePackageGrouping(ManageMeasureDetailModel model);
	
	/**
	 * Validate measure xmlinpopulation workspace.
	 *
	 * @param measureXmlModel the measure xml model
	 * @return true, if successful
	 */
	ValidateMeasureResult validateMeasureXmlinpopulationWorkspace(
			MeasureXmlModel measureXmlModel);
	
	/**
	 * Validate for group.
	 *
	 * @param model the model
	 * @return the validate measure result
	 */
	ValidateMeasureResult validateForGroup(ManageMeasureDetailModel model);
	
	/**
	 * Validates and packages the measure when the user versions or packages a measure
	 * @param mode the measure details
	 */
	SaveMeasureResult validateAndPackageMeasure(ManageMeasureDetailModel mode);
	
	/**
	 * Gets the all measure types.
	 *
	 * @return the all measure types
	 */
	List<MeasureType> getAllMeasureTypes();
	
	/**
	 * Gets the all add edit authors.
	 *
	 * @return the all add edit authors
	 */
	List<Organization> getAllOrganizations();
	
	/**
	 * Save sub tree occurrence.
	 *
	 * @param measureXmlModel the measure xml model
	 * @param nodeName the node name
	 * @param nodeUUID the node uuid
	 * @return the sorted clause map result
	 */
	SortedClauseMapResult saveSubTreeOccurrence(MeasureXmlModel measureXmlModel, String nodeName, String nodeUUID);
	
	/**
	 * Checks if is QDM variable enabled.
	 *
	 * @param measureId the measure id
	 * @param subTreeUUID the sub tree uuid
	 * @return true, if is QDM variable enabled
	 */
	boolean isQDMVariableEnabled(String measureId, String subTreeUUID);
	
	/**
	 * Gets the sorted clause map.
	 *
	 * @param measureId the measure id
	 * @return the sorted clause map
	 */
	LinkedHashMap<String, String> getSortedClauseMap(String measureId);
	
	/**
	 * Gets the measure xml for measure and sorted sub tree map.
	 *
	 * @param currentMeasureId the current measure id
	 * @return the measure xml for measure and sorted sub tree map
	 */
	SortedClauseMapResult getMeasureXmlForMeasureAndSortedSubTreeMap(
			String currentMeasureId);
	
	/**
	 * Gets the used steward and developers list.
	 *
	 * @param measureId the measure id
	 * @return the used steward and developers list
	 */
	MeasureDetailResult getUsedStewardAndDevelopersList(String measureId);
	
	/**
	 * Update measure xml for deleted component measure and org.
	 *
	 * @param id the id
	 */
	void updateMeasureXmlForDeletedComponentMeasureAndOrg(String id);
	
	/**
	 * Update measure xml for expansion identifier.
	 *
	 * @param modifyWithDTO the modify with dto
	 * @param measureId the measure id
	 * @param expansionProfile the expansion profile
	 */
	void updateMeasureXMLForExpansionIdentifier(List<QualityDataSetDTO> modifyWithDTO, String measureId, String expansionProfile);
	
	/**
	 * Update measure xml for expansion identifier.
	 *
	 * @param modifyWithDTO the modify with dto
	 * @param measureId the measure id
	 * @param expansionProfile the expansion profile
	 */
	void updateCQLMeasureXMLForExpansionProfile(List<CQLQualityDataSetDTO> modifyWithDTO, String measureId, String expansionProfile);
	
	/**
	 * Method to Get Default 4 Supplemental Data Elements for give Measure.
	 *
	 * @param measureId the measure id
	 * @return QualityDataModelWrapper
	 */
	QualityDataModelWrapper getDefaultSDEFromMeasureXml(String measureId);
	
	/**
	 * Method to Get Default 4 Supplemental Data Elements for give Measure.
	 *
	 * @param measureId the measure id
	 * @return CQLQualityDataModelWrapper
	 */
	CQLQualityDataModelWrapper getDefaultCQLSDEFromMeasureXml(String measureId);
	
	/**
	 * Parses the cql.
	 *
	 * @param cqlBuilder the cql builder
	 * @return the CQL model
	 */
	CQLModel parseCQL(String cqlBuilder);
	
	//Boolean saveCQLData(CQLModel cqlDataModel);
	
	/**
	 * Gets the CQL data.
	 *
	 * @param measureId the measure id
	 * @return the CQL data
	 */
//	SaveUpdateCQLResult getCQLData(String measureId, String fromTable);
	
	SaveUpdateCQLResult getMeasureCQLData(String measureId);
	
	/**
	 * Gets the CQL file data.
	 *
	 * @param measureId the measure id
	 * @return the CQL file data
	 */
//	SaveUpdateCQLResult getCQLFileData(String measureId);
	
	/**
	 * Save and modify definitions.
	 *
	 * @param measureId the measure id
	 * @param toBemodifiedObj the to bemodified obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @param isFormtable flag for if the definition should be formatted on save
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyDefinitions(String measureId,
			CQLDefinition toBemodifiedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList, boolean isFormatable);
	
	/**
	 * Save and modify parameters.
	 *
	 * @param measureId the measure id
	 * @param toBemodifiedObj the to bemodified obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @param isFormtable flag for if the parameter should be formatted on save
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyParameters(String measureId,
			CQLParameter toBemodifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList, boolean isFormatable);
	
	/**
	 * Save and modify cql general info.
	 *
	 * @param currentMeasureId the current measure id
	 * @param context the context
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String currentMeasureId,
			String context, String comments);
	
	/**
	 * Save and modify functions.
	 *
	 * @param measureId the measure id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param functionsList the functions list
	 * @param isFormtable flag for if the function should be formatted on save
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyFunctions(String measureId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList, boolean isFormatable);
	
	/**
	 * Delete definition
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj, 
			List<CQLDefinition> definitionList);
	
	/**
	 * Delete functions
	 *  
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param functionsList the functions list
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult deleteFunctions(String measureId, CQLFunctions toBeDeletedObj,  
			List<CQLFunctions> functionsList); 
	
	/**
	 * Delete parameter
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj,  
			List<CQLParameter> parameterList); 
	
	
	/**
	 * Gets the CQL data type list.
	 *
	 * @return the CQL data type list
	 */
	CQLKeywords getCQLKeywordsList();

	String getJSONObjectFromXML();
	
	GetUsedCQLArtifactsResult getUsedCQLArtifacts(String measureId);

	SaveUpdateCQLResult parseCQLStringForError(String cqlFileString);

	SaveUpdateCQLResult deleteValueSet(String toBeDeletedValueSetId, String measureID);	
	CQLQualityDataModelWrapper getCQLValusets(String measureID);

	SaveUpdateCQLResult saveCQLValuesettoMeasure(CQLValueSetTransferObject valueSetTransferObject);

	SaveUpdateCQLResult saveCQLUserDefinedValuesettoMeasure(CQLValueSetTransferObject valueSetTransferObject);

	SaveUpdateCQLResult updateCQLValuesetsToMeasure(CQLValueSetTransferObject matValueSetTransferObject);

	SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String measureId, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList);

	SaveUpdateCQLResult getMeasureCQLFileData(String measureId);

	SaveUpdateCQLResult deleteInclude(String currentMeasureId,
			CQLIncludeLibrary toBeModifiedIncludeObj,
			List<CQLIncludeLibrary> viewIncludeLibrarys);
	
	VsacApiResult updateCQLVSACValueSets(String currentMeasureId, String expansionId);

	SaveUpdateCQLResult saveCQLCodestoMeasure(MatCodeTransferObject transferObject);
	
	SaveUpdateCQLResult saveCQLCodeListToMeasure(List<CQLCode> codeList, String measureId);
	
	SaveUpdateCQLResult modifyCQLCodeInMeasure(CQLCode modifyCQLCode, CQLCode refCode, String measureId);

	CQLCodeWrapper getCQLCodes(String measureID);

	SaveUpdateCQLResult deleteCode(String toBeDeletedId, String measureID);

	SaveUpdateCQLResult getMeasureCQLLibraryData(String measureId);

	SaveUpdateCQLResult getMeasureCQLDataForLoad(String measureId);

	CQLQualityDataModelWrapper saveValueSetList(List<CQLValueSetTransferObject> transferObjectList,
			List<CQLQualityDataSetDTO> appliedValueSetList, String measureId);
}
