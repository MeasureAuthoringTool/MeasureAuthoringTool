package mat.client.measure.service;

import java.util.List;

import mat.model.CQLValueSetTransferObject;
import mat.model.clause.CQLData;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;

// TODO: Auto-generated Javadoc
/**
 * The Interface CQLService.
 */
public interface CQLService {

	/**
	 * Save cql.
	 *
	 * @param measureId the measure id
	 * @return the boolean
	 */
	// Boolean saveCQL(CQLModel cqlDataModel);

	/**
	 * Get cql.
	 * 
	 * @param boolean
	 * @return cqlDataModel the cql data model
	 */
	CQLData getCQL(String measureId);

    /**
     * Parses the cql.
     *
     * @param cqlBuilder the cql builder
     * @return the CQL model
     */
    CQLModel parseCQL(String cqlBuilder);

	/**
	 * Gets the CQL data.
	 *
	 * @param measureId the measure id
	 * @return the CQL data
	 */
	SaveUpdateCQLResult getCQLData(String xmlString);

	/**
	 * Save and modify cql general info.
	 *
	 * @param currentMeasureId the current measure id
	 * @param context the context
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(
			String currentMeasureId, String context);
	
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
			List<CQLFunctions> functionsList);
	
	/**
	 * Save and modify parameters.
	 *
	 * @param measureId the measure id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyParameters(String measureId, CQLParameter toBeModifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList);
	
	/**
	 * Save and modify definitions.
	 *
	 * @param measureId the measure id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyDefinitions(String measureId, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList);
	
	/**
	 * Delete definition
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @return the save and update result
	 */
	SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList);
	
	/**
	 * Delete functions
	 * 
	 * @param measureId the measure id
	 * @param toBeDeltedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param functionsList the functions list
	 * @return the save and update result
	 */
	SaveUpdateCQLResult deleteFunctions(String measureId, CQLFunctions toBeDeltedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList);
	
	/**
	 * Delete parameter
	 * 
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @return the save and update result
	 */
	SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList);
	
	/**
	 * Gets the CQL data type list.
	 *
	 * @return the CQL data type list
	 */
	CQLKeywords getCQLKeyWords();
	
	/**
	 * Gets the CQL file data.
	 *
	 * @param measureId the measure id
	 * @return the CQL file data
	 */
	SaveUpdateCQLResult getCQLFileData(String xmlString);

	String createParametersXML(CQLParameter parameter);

	String getJSONObjectFromXML();

	String createDefinitionsXML(CQLDefinition definition);

	String getSupplementalDefinitions();

	StringBuilder getCqlString(CQLModel cqlModel);

	String getDefaultCodeSystems();
	
	String getDefaultCodes();

	GetUsedCQLArtifactsResult getUsedCQlArtifacts(String measureId);

	SaveUpdateCQLResult parseCQLStringForError(String cqlFileString);

	CQLQualityDataModelWrapper getCQLValusets(String measureID, CQLQualityDataModelWrapper cqlQualityDataModelWrapper);

	SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject);

	SaveUpdateCQLResult saveCQLUserDefinedValueset(CQLValueSetTransferObject matValueSetTransferObject);

	SaveUpdateCQLResult modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject);

	SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String measureId, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList);

	SaveUpdateCQLResult deleteInclude(String currentMeasureId,
			CQLIncludeLibrary toBeModifiedIncludeObj,
			CQLIncludeLibrary cqlLibObject,
			List<CQLIncludeLibrary> viewIncludeLibrarys);

	void saveCQLAssociation(CQLIncludeLibrary currentObj, String measureId);

	void deleteCQLAssociation(CQLIncludeLibrary currentObj, String measureId);

	SaveUpdateCQLResult updateCQLLookUpTag(String xml, CQLQualityDataSetDTO modifyWithDTO,
			CQLQualityDataSetDTO modifyDTO);

	SaveUpdateCQLResult deleteValueSet(String xml, String toBeDelValueSetId);

	int countNumberOfAssociation(String associatedWithId);





}
