package mat.client.measure.service;

import java.util.List;

import mat.model.clause.CQLData;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLDefinitionsWrapper;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
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
	SaveUpdateCQLResult getCQLData(String measureId);

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
	SaveUpdateCQLResult getCQLFileData(String measureId);

	String createParametersXML(CQLParameter parameter);

	String getJSONObjectFromXML();
}
