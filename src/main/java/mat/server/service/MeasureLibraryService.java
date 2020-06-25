package mat.server.service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import mat.client.clause.clauseworkspace.model.MeasureDetailResult;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.TransferOwnerShipModel;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.GenericResult;
import mat.client.shared.MatException;
import mat.client.umls.service.VsacApiResult;
import mat.model.CQLValueSetTransferObject;
import mat.model.ComponentMeasureTabObject;
import mat.model.MatCodeTransferObject;
import mat.model.MatValueSet;
import mat.model.MeasureOwnerReportDTO;
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
import mat.server.util.XmlProcessor;
import mat.shared.CQLValidationResult;
import mat.shared.CompositeMeasureValidationResult;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.MeasureSearchModel;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.cql.error.InvalidLibraryException;
import mat.shared.error.AuthenticationException;
import mat.shared.error.measure.DeleteMeasureException;

/**
 * The Interface MeasureLibraryService.
 */
public interface MeasureLibraryService {
	
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
	 * @return 
	 */
	String appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName);
	
	/**
	 * Check for timing elements and append.
	 * 
	 * @param xmlProcessor
	 *            the xml processor
	 */
	void checkForTimingElementsAndAppend(XmlProcessor xmlProcessor);
	
	/**
	 * Creates the and save element look up.
	 *
	 * @param list            the list
	 * @param measureID            the measure id
	 * @param expProfileToAllQDM the exp profile to all qdm
	 */
	void createAndSaveElementLookUp(List<QualityDataSetDTO> list,
			String measureID, String expProfileToAllQDM);
	
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
	QualityDataModelWrapper getAppliedQDMFromMeasureXml(String measureId,
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
	
	ManageCompositeMeasureDetailModel getCompositeMeasure(String key);
	
	/**
	 * Gets the measure xml for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure xml for measure
	 */
	public MeasureXmlModel getMeasureXmlForMeasure(String measureId);
	
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
	ManageMeasureShareModel getUsersForShare(String userName, String measureId,
			int startIndex, int pageSize);
	
	/**
	 * Checks if is measure locked.
	 * 
	 * @param id
	 *            the id
	 * @return true, if is measure locked
	 */
	boolean isMeasureLocked(String id);
	
	/**
	 * Record measure in recent measure activity log for the given measure ID and user ID.
	 *
	 * @param measureId the measure id
	 * @param userId the user id
	 */
	void recordRecentMeasureActivity(String measureId, String userId);
	
	/**
	 * Reset locked date.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param userId
	 *            the user id
	 * @return the save measure result
	 */
	SaveMeasureResult resetLockedDate(String measureId, String userId);
	
	/**
	 * Save.
	 * 
	 * @param model
	 *            the model
	 * @return the save measure result
	 * @throws MatException 
	 */
	SaveMeasureResult saveOrUpdateMeasure(ManageMeasureDetailModel model);
	
	SaveMeasureResult saveCompositeMeasure(ManageCompositeMeasureDetailModel model) throws MatException;
	
	void deleteMeasure(String measureId, String loggedInUserId) throws DeleteMeasureException, AuthenticationException;
	
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
	SaveMeasureResult saveFinalizedVersion(String measureId, boolean isMajor, String version, boolean shouldPackage, boolean ignoreUnusedLibraries);
	
	/**
	 * Save measure details.
	 * 
	 * @param model
	 *            the model
	 * @return the save measure result
	 * @throws MatException 
	 */
	SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model) throws MatException;
	
	/**
	 * Save measure xml.
	 * 
	 * @param measureXmlModel
	 *            the measure xml model
	 */
	void saveMeasureXml(MeasureXmlModel measureXmlModel, String measureId);
	
	/**
	 * Search.
	 * 
	 * @param searchText
	 *            the search text
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param filter
	 *            the filter
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
	TransferOwnerShipModel searchUsers(String searchText, int startIndex,
			int pageSize);
	
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
	SaveMeasureResult updateLockedDate(String measureId, String userId);

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
	ValidateMeasureResult createExports(String key,
			List<MatValueSet> matValueSetList, boolean shouldCreateArtifacts) throws MatException;
		
	/**
	 * Save measure at package.
	 *
	 * @param model the model
	 * @return the save measure result
	 */
	SaveMeasureResult saveMeasureAtPackage(ManageMeasureDetailModel model);
	
	/**
	 * Save SubTree - Append new node if new else update exisiting subTreeNode in measureXml.
	 *
	 * @param measureXmlModel the measure xml model
	 * @param nodeName the node name
	 * @param nodeUUID the node uuid
	 * @return the sorted clause map result
	 */
	SortedClauseMapResult saveSubTreeInMeasureXml(MeasureXmlModel measureXmlModel, String nodeName, String nodeUUID);
	
	/**
	 * Check and delete sub tree.
	 *
	 * @param measureId the measure id
	 * @param subTreeUUID the sub tree uuid
	 * @return true, if successful
	 */
	HashMap<String, String> checkAndDeleteSubTree(String measureId, String subTreeUUID);
	
	/**
	 * Gets the formatted release date.
	 *
	 * @param releaseDate the release date
	 * @return the formatted release date
	 */
	Date getFormattedReleaseDate(String releaseDate);
	
	
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
	 * @param measureId the measure ids
	 * @return the component measures
	 */
	ManageMeasureSearchModel getComponentMeasures(String measureId);
	
	
	
	/**
	 * Validate package grouping.
	 *
	 * @param model the model
	 * @return the string
	 */
	ValidateMeasureResult validatePackageGrouping(ManageMeasureDetailModel model);
	
	/**
	 * Validate measure xmlinpopulation workspace.
	 *
	 * @param measureXmlModel the measure xml model
	 * @return the object
	 */
	ValidateMeasureResult validateMeasureXmlAtCreateMeasurePackager(
			MeasureXmlModel measureXmlModel);
	
	/**
	 * Validate for group.
	 *
	 * @param model the model
	 * @return the validate measure result
	 */
	ValidateMeasureResult validateForGroup(ManageMeasureDetailModel model);
	
	/**
	 * Gets the all measure types.
	 *
	 * @return the all measure types
	 */
	List<MeasureType> getAllMeasureTypes();
	
	/**
	 * Gets the all authors.
	 *
	 * @return the all authors
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
	 * @param measureId the measure id
	 * @return the measure xml for measure and sorted sub tree map
	 */
	SortedClauseMapResult getMeasureXmlForMeasureAndSortedSubTreeMap(
			String measureId);
	
	/**
	 * Method to get User Steward and Developers List for measure.
	 *
	 * @param measureId the measure id
	 * @return the used steward and developers list
	 */
	MeasureDetailResult getUsedStewardAndDevelopersList(String measureId);
	
	/**
	 * Service to Update Expansion Profile in Measure Xml.
	 *
	 * @param modifyWithDTO the modify with dto
	 * @param measureId the measure id
	 * @param expansionProfile the expansion profile
	 */
	void updateMeasureXMLForExpansionIdentifier(
			List<QualityDataSetDTO> modifyWithDTO, String measureId,
			String expansionProfile);
	
	
	/**
	 * Method to Get Default 4 Supplemental Data Elements for give Measure.
	 *
	 * @param measureId the measure id
	 * @return QualityDataModelWrapper
	 */
	QualityDataModelWrapper getDefaultSDEFromMeasureXml(String measureId);
	
	/**
	 * Gets the measures for measure owner.
	 *
	 * @return the measures for measure owner
	 * @throws XPathExpressionException the x path expression exception
	 */
	List<MeasureOwnerReportDTO> getMeasuresForMeasureOwner() throws XPathExpressionException;
	
	/**
	 * Gets the default expansion identifier.
	 *
	 * @param measureId the measure id
	 * @return the default expansion identifier
	 */
	//String getDefaultExpansionIdentifier(String measureId);
	
	/**
	 * Gets the current release version.
	 *
	 * @return the current release version
	 */
	String getCurrentReleaseVersion();
	
	/**
	 * Sets the current release version.
	 *
	 * @param releaseVersion the new current release version
	 */
	void setCurrentReleaseVersion(String releaseVersion);
	
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
	//SaveUpdateCQLResult getCQLData(String measureId,String fromTable);
	
	SaveUpdateCQLResult getMeasureCQLData(String measureId);
	
	/**
	 * Gets the CQL file data.
	 *
	 * @param measureId the measure id
	 * @return the CQL file data
	 */
	//SaveUpdateCQLResult getCQLFileData(String xmlString);
	
	/**
	 * Validate cql.
	 *
	 * @param cqlModel the cql model
	 * @return the CQL validation result
	 */
	CQLValidationResult validateCQL(CQLModel cqlModel);
	
	/**
	 * Save and modify definitions.
	 *
	 * @param measureId the measure id
	 * @param toBemodifiedObj the to bemodified obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyDefinitions(String measureId,
			CQLDefinition toBemodifiedObj, CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable);
		
	/**
	 * Save and modify parameters.
	 *
	 * @param measureId the measure id
	 * @param toBemodifiedObj the to bemodified obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyParameters(String measureId, CQLParameter toBemodifiedObj, CQLParameter currentObj,
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
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyFunctions(String measureId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList, boolean isFormatable);
	/**
	 * Delete definition
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @return the save and update cql result
	 */
	SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj);

	/**
	 * Delete functions
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @return the save and update cql result
	 */
	SaveUpdateCQLResult deleteFunction(String measureId, CQLFunctions toBeDeletedObj);

	/**
	 * Delete parameter
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @return the save and update cql result
	 */
	SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj);
	
	/**
	 * Gets the CQL data type list.
	 *
	 * @return the CQL data type list
	 */
	CQLKeywords getCQLKeywordsLists();

	String getJSONObjectFromXML();

	/**
	 * Creates the and save cql look up.
	 *
	 * @param list            the list
	 * @param measureID            the measure id
	 * @param expProfileToAllQDM the exp profile to all qdm
	 */
	void createAndSaveCQLLookUp(List<QualityDataSetDTO> list, String measureID, String expProfileToAllQDM);

	GetUsedCQLArtifactsResult getUsedCqlArtifacts(String measureId);
	
	SaveUpdateCQLResult deleteValueSet(String toBeDeletedValueSetId, String measureID);

	CQLQualityDataModelWrapper getCQLAppliedQDMFromMeasureXml(String measureId, boolean checkForSupplementData);

	CQLQualityDataModelWrapper getDefaultCQLSDEFromMeasureXml(String measureId);

	SaveUpdateCQLResult parseCQLStringForError(String cqlFileString);

	CQLQualityDataModelWrapper getCQLValusets(String measureID);

	SaveUpdateCQLResult saveCQLValuesettoMeasure(CQLValueSetTransferObject valueSetTransferObject);

	void updateCQLLookUpTagWithModifiedValueSet(CQLQualityDataSetDTO modifyWithDTO, CQLQualityDataSetDTO modifyDTO,
			String measureId);
	SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String measureId, CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) throws InvalidLibraryException;

	SaveUpdateCQLResult getMeasureCQLFileData(String measureId);

	void updateCQLMeasureXMLForExpansionIdentifier(List<CQLQualityDataSetDTO> modifyWithDTOList, String measureId,
			String expansionProfile);

	SaveUpdateCQLResult deleteInclude(String currentMeasureId, CQLIncludeLibrary toBeModifiedIncludeObj);

	VsacApiResult updateCQLVSACValueSets(String currentMeasureId, String expansionId, String sessionId);

	SaveUpdateCQLResult saveCQLCodestoMeasure(MatCodeTransferObject transferObject);
	
	SaveUpdateCQLResult saveCQLCodeListToMeasure(List<CQLCode> codeList, String measureId);

	CQLCodeWrapper getCQLCodes(String measureID);

	SaveUpdateCQLResult deleteCode(String toBeDeletedId, String measureID);

	SaveUpdateCQLResult getMeasureCQLLibraryData(String measureId);

	SaveUpdateCQLResult getMeasureCQLDataForLoad(String measureId);

	CQLQualityDataModelWrapper saveValueSetList(List<CQLValueSetTransferObject> transferObjectList,
			List<CQLQualityDataSetDTO> appliedValueSetList, String measureId);

	SaveMeasureResult validateAndPackage(ManageMeasureDetailModel model, boolean shouldCreateArtifacts);

	ManageMeasureSearchModel searchComponentMeasures(MeasureSearchModel searchModel);

	ManageCompositeMeasureDetailModel buildCompositeMeasure(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel);

	CompositeMeasureValidationResult validateCompositeMeasure(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel);

	List<ComponentMeasureTabObject> getCQLLibraryInformationForComponentMeasure(String measureId);

	ManageCompositeMeasureDetailModel getCompositeMeasure(String measureId, String simpleXML);
	
	GenericResult checkIfMeasureIsUsedAsComponentMeasure(String currentMeasureId);

	ValidateMeasureResult validateExports(String measureId) throws Exception;

	Boolean isCompositeMeasure(String currentMeasureId);

	int generateAndSaveMaxEmeasureId(boolean isEditable, String measureId);

	String getHumanReadableForMeasureDetails(String measureId);

	SaveUpdateCQLResult saveCQLFile(String measureId, String cql);
	
	boolean libraryNameExists(String libraryName, String setId);
}
