package mat.client.measure.service;

import java.io.IOException;
import java.util.List;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import mat.model.CQLValueSetTransferObject;
import mat.model.MatCodeTransferObject;
import mat.model.clause.CQLLibrary;
import mat.model.clause.CQLLibraryHistory;
import mat.model.clause.Measure;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLCodeWrapper;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryAssociation;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.cqlparser.CQLLinterConfig;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.cql.error.InvalidLibraryException;

/**
 * The Interface CQLService.
 */
public interface CQLService {

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
			String currentMeasureId, String context, String libraryComment);

	/**
	 * Save and modify functions.
	 *
	 * @param measureId the measure id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param functionsList the functions list
	 * @param isFormatable flag to determine if the function should be formatted on save
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyFunctions(String measureId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList, boolean isFormatable, String modelType);

	/**
	 * Save and modify parameters.
	 *
	 * @param measureId the measure id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param parameterList the parameter list
	 * @param isFormatable flag to determine if the parameter should be formatted on save

	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyParameters(String measureId, CQLParameter toBeModifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList, boolean isFormatable, String modelType);

	/**
	 * Save and modify definitions.
	 *
	 * @param measureId the measure id
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 * @param definitionList the definition list
	 * @param isFormatable flag to determine if the definition should be formatted on save
	 * @return the save update cql result
	 */
	SaveUpdateCQLResult saveAndModifyDefinitions(String xml, CQLDefinition toBeModifiedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList, boolean isFormatable, String modelType);

	/**
	 * Delete definition
	 *
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @return the save and update result
	 */
	SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj);

	/**
	 * Delete functions
	 *
	 * @param measureId the measure id
	 * @param toBeDeltedObj the to be deleted obj
	 * @return the save and update result
	 */
	SaveUpdateCQLResult deleteFunction(String measureId, CQLFunctions toBeDeltedObj);

	/**
	 * Delete parameter
	 *
	 * @param measureId the measure id
	 * @param toBeDeletedObj the to be deleted obj
	 * @return the save and update result
	 */
	SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj);

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

	String getCqlString(CQLModel cqlModel);

	String getDefaultCodeSystems();

	SaveUpdateCQLResult generateParsedCqlObject(String cqlValidationResponse, CQLModel cqlModel);

	GetUsedCQLArtifactsResult getUsedCQlArtifacts(String measureId);

	GetUsedCQLArtifactsResult generateUsedCqlArtifactsResult(CQLModel cqlModel, String xml, SaveUpdateCQLResult cqlResult);

	SaveUpdateCQLResult parseCQLStringForError(String cqlFileString);

	CQLQualityDataModelWrapper getCQLValusets(String measureID, CQLQualityDataModelWrapper cqlQualityDataModelWrapper);

	SaveUpdateCQLResult saveCQLValueset(String xml, CQLValueSetTransferObject valueSetTransferObject);

	SaveUpdateCQLResult saveAndModifyIncludeLibrayInCQLLookUp(String xml, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList,  String modelType) throws InvalidLibraryException;

	SaveUpdateCQLResult deleteInclude(String currentMeasureId, CQLIncludeLibrary toBeModifiedIncludeObj);

	void saveCQLAssociation(CQLIncludeLibrary currentObj, String measureId);

	void deleteCQLAssociation(CQLIncludeLibrary currentObj, String measureId);

	SaveUpdateCQLResult updateCQLLookUpTag(String xml, CQLQualityDataSetDTO modifyWithDTO,
			CQLQualityDataSetDTO modifyDTO);

	SaveUpdateCQLResult deleteValueSet(String xml, String toBeDelValueSetId);

	int countNumberOfAssociation(String associatedWithId);

	SaveUpdateCQLResult parseCQLLibraryForErrors(CQLModel cqlModel);

	List<CQLLibraryAssociation> getAssociations(String id);

	SaveUpdateCQLResult saveCQLCodes(String xml , MatCodeTransferObject codeTransferObject);

	CQLCodeWrapper getCQLCodes(String xmlString);

	SaveUpdateCQLResult deleteCode(String xml, String toBeDeletedCodeId);

	SaveUpdateCQLResult saveCQLCodeSystem(String xml, CQLCodeSystem codeSystem);

	SaveUpdateCQLResult getCQLLibraryData(String xmlString, String modelType);

	SaveUpdateCQLResult getCQLDataForLoad(String xmlString);

	String createIncludeLibraryXML(CQLIncludeLibrary includeLibrary) throws MarshalException, ValidationException, IOException, MappingException;

	SaveUpdateCQLResult saveCQLFile(String xml, String cql, CQLLinterConfig config, String modelType);

	List<CQLLibraryHistory> createCQLLibraryHistory(List<CQLLibraryHistory> exsistingLibraryHistory, String CQLLibraryString, CQLLibrary cqlLibrary, Measure measure);

	boolean checkIfLibraryNameExists(String libraryName, String setId);
}
