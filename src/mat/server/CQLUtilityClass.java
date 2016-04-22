package mat.server;

import java.util.ArrayList;
import java.util.List;

import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;

public class CQLUtilityClass {
	
	/** The Constant PATIENT. */
	private static final String PATIENT = "patient";
	
	/** The Constant POPULATION. */
	private static final String POPULATION = "population";

	public static StringBuilder getCqlString(CQLModel cqlModel) {

		StringBuilder cqlStr = new StringBuilder();

		// library Name
		if (cqlModel.getLibrary() != null) {
			cqlStr = cqlStr.append("library "
					+ cqlModel.getLibrary().getLibraryName());
			if (cqlModel.getLibrary().getVersionUsed() != null) {
				cqlStr = cqlStr.append("version "
						+ cqlModel.getLibrary().getVersionUsed());
			}
			cqlStr = cqlStr.append("\n\n");
		}

		// Using
		cqlStr = cqlStr.append("using QDM");
		cqlStr = cqlStr.append("\n\n");


		//Valuesets
		List<CQLQualityDataSetDTO> valueSetList = cqlModel.getValueSetList();
		if (valueSetList != null) {
			for (CQLQualityDataSetDTO valueset : valueSetList) {
				cqlStr = cqlStr.append("valueset "
						+'"'+ valueset.getCodeListName() +'"'+ ":"
						+"'"+ valueset.getOid()+"'");

				cqlStr = cqlStr.append("\n\n");
			}
		}

		// parameters
		List<CQLParameter> paramList = cqlModel.getCqlParameters();
		if (paramList != null) {
			for (CQLParameter parameter : paramList) {
				cqlStr = cqlStr.append("parameter "
						+ parameter.getParameterName() + " "
						+ parameter.getParameterLogic());
				cqlStr = cqlStr.append("\n\n");
			}
		}

		// Definitions and Functions by Context
		cqlStr = getDefineAndFunctionsByContext(cqlModel.getDefinitionList(),
				cqlModel.getCqlFunctions(), cqlStr);

		return cqlStr;

	}
	
	/**
	 * Gets the define and funcs by context.
	 * 
	 * @param defineList
	 *            the define list
	 * @param functionsList
	 *            the functions list
	 * @param cqlStr
	 *            the cql str
	 * @return the define and funcs by context
	 */
	private static StringBuilder getDefineAndFunctionsByContext(
			List<CQLDefinition> defineList, List<CQLFunctions> functionsList,
			StringBuilder cqlStr) {
		
		List<CQLDefinition> contextPatDefineList = new ArrayList<CQLDefinition>();
		List<CQLDefinition> contextPopDefineList = new ArrayList<CQLDefinition>();
		List<CQLFunctions> contextPatFuncList = new ArrayList<CQLFunctions>();
		List<CQLFunctions> contextPopFuncList = new ArrayList<CQLFunctions>();
		
		if (defineList != null) {
			for (int i = 0; i < defineList.size(); i++) {
				if (defineList.get(i).getContext().equalsIgnoreCase(PATIENT)) {
					contextPatDefineList.add(defineList.get(i));
				} else {
					contextPopDefineList.add(defineList.get(i));
				}
			}
		}
		if (functionsList != null) {
			for (int i = 0; i < functionsList.size(); i++) {
				if (functionsList.get(i).getContext().equalsIgnoreCase(PATIENT)) {
					contextPatFuncList.add(functionsList.get(i));
				} else {
					contextPopFuncList.add(functionsList.get(i));
				}
			}
		}
		
		if ((contextPatDefineList.size() > 0) || (contextPatFuncList.size() > 0)) {
			
			getDefineAndFunctionsByContext(contextPatDefineList,
					contextPatFuncList, PATIENT, cqlStr);
		}
		
		if ((contextPopDefineList.size() > 0) || (contextPopFuncList.size() > 0)) {
			
			getDefineAndFunctionsByContext(contextPopDefineList,
					contextPopFuncList, POPULATION, cqlStr);
		}
		
		return cqlStr;
		
	}
	
	/**
	 * Gets the define and functions by context.
	 * 
	 * @param definitionList
	 *            the definition list
	 * @param functionsList
	 *            the functions list
	 * @param context
	 *            the context
	 * @param cqlStr
	 *            the cql str
	 * @return the define and functions by context
	 */
	private static StringBuilder getDefineAndFunctionsByContext(
			List<CQLDefinition> definitionList,
			List<CQLFunctions> functionsList, String context,
			StringBuilder cqlStr) {
		
		cqlStr = cqlStr.append("context").append(" " + context).append("\n\n");
		for (CQLDefinition definition : definitionList) {
			cqlStr = cqlStr.append("define " + definition.getDefinitionName()
					+ ": ");
			cqlStr = cqlStr.append(definition.getDefinitionLogic());
			cqlStr = cqlStr.append("\n\n");
		}
		
		for (CQLFunctions function : functionsList) {
			cqlStr = cqlStr.append("define function "
					+ function.getFunctionName() + "(");
			if(function.getArgumentList()!=null) {
			for (CQLFunctionArgument argument : function.getArgumentList()) {
				StringBuilder argumentType = new StringBuilder();
				if (argument.getArgumentType().toString()
						.equalsIgnoreCase("QDM Datatype")) {
					argumentType = argumentType.append("\"").append(
							argument.getQdmDataType());
					if (argument.getAttributeName() != null) {
						argumentType = argumentType.append(".")
								.append(argument.getAttributeName());
					}
					argumentType = argumentType.append("\"");
				} else if (argument
						.getArgumentType()
						.toString()
						.equalsIgnoreCase(
								CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
					argumentType = argumentType.append(argument.getOtherType());
				} else {
					argumentType = argumentType.append(argument
							.getArgumentType());
				}
				cqlStr = cqlStr.append(argument.getArgumentName() + " "
						+ argumentType + ", ");
			}
			cqlStr.deleteCharAt(cqlStr.length() - 2);
		}
			
			cqlStr = cqlStr.append("): " + function.getFunctionLogic());
			cqlStr = cqlStr.append("\n\n");
		}
		
		return cqlStr;
	}

}
