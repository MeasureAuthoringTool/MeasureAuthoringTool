package mat.server;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.cql.CQLDataModel;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLDefinitionsWrapper;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLFunctionsWrapper;
import mat.model.cql.CQLLibraryModel;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLParametersWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class CQLUtilityClass {
	
	/** The Constant PATIENT. */
	private static final String PATIENT = "Patient";
	
	/** The Constant POPULATION. */
	private static final String POPULATION = "Population";
	
	private static StringBuilder toBeInsertedAtEnd;
	
	
	public static StringBuilder getStrToBeInserted(){
		return toBeInsertedAtEnd;
	}

	public static StringBuilder getCqlString(CQLModel cqlModel, String toBeInserted) {

		StringBuilder cqlStr = new StringBuilder();
		toBeInsertedAtEnd = new  StringBuilder();
		// library Name and Using 
		if (cqlModel.getLibrary() != null) {
			cqlStr = cqlStr.append("library "
					+ cqlModel.getLibrary().getLibraryName());
			if (cqlModel.getLibrary().getVersionUsed() != null) {
				cqlStr = cqlStr.append("version "
						+ cqlModel.getLibrary().getVersionUsed());
			}
			cqlStr = cqlStr.append("\n\n");
			
			cqlStr = cqlStr.append("using QDM");
			cqlStr = cqlStr.append("\n\n");
		}

		
		//Valuesets
		List<CQLQualityDataSetDTO> valueSetList = cqlModel.getValueSetList();
		if (valueSetList != null) {
			for (CQLQualityDataSetDTO valueset : valueSetList) {
				cqlStr = cqlStr.append("valueset "
						+'"'+ valueset.getCodeListName() +'"'+ ": "
						+"'"+ valueset.getOid()+"'"
						);

				cqlStr = cqlStr.append("\n\n");
			}
		}

		// parameters
		List<CQLParameter> paramList = cqlModel.getCqlParameters();
		if (paramList != null) {
			for (CQLParameter parameter : paramList) {
				if(parameter.getParameterName().equalsIgnoreCase(toBeInserted)){
					toBeInsertedAtEnd = toBeInsertedAtEnd.append("parameter "
							+ "\""+parameter.getParameterName()+ "\"" + " "
							+ parameter.getParameterLogic());
					//cqlStr = cqlStr.append("\n\n");
				} else {
					cqlStr = cqlStr.append("parameter "
							+ "\""+parameter.getParameterName()+ "\"" + " "
							+ parameter.getParameterLogic());
					cqlStr = cqlStr.append("\n\n");
				}
			}
		}

		// Definitions and Functions by Context
		cqlStr = getDefineAndFunctionsByContext(cqlModel.getDefinitionList(),
				cqlModel.getCqlFunctions(), cqlStr, toBeInserted);
		//cqlModel.setLines(countLines(cqlStr.toString()));
		
		/*if(!toBeInsertedAtEnd.toString().isEmpty()){
			cqlStr = cqlStr.append(toBeInsertedAtEnd.toString());
		}*/

		return cqlStr;

	}
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CQLUtilityClass.class);
	
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
			StringBuilder cqlStr, String toBeInserted) {
		
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
					contextPatFuncList, PATIENT, cqlStr, toBeInserted);
		}
		
		if ((contextPopDefineList.size() > 0) || (contextPopFuncList.size() > 0)) {
			
			getDefineAndFunctionsByContext(contextPopDefineList,
					contextPopFuncList, POPULATION, cqlStr, toBeInserted);
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
			StringBuilder cqlStr, String toBeInserted) {
		cqlStr = cqlStr.append("context").append(" " + context).append("\n\n");
		for (CQLDefinition definition : definitionList) {
			if(definition.getDefinitionName().equalsIgnoreCase(toBeInserted)){
				toBeInsertedAtEnd = toBeInsertedAtEnd.append("define " + "\""+ definition.getDefinitionName() + "\""
						+ ": ");
				toBeInsertedAtEnd = toBeInsertedAtEnd.append(definition.getDefinitionLogic());
				//toBeInsertedAtEnd = toBeInsertedAtEnd.append("\n\n");
			} else {
				cqlStr = cqlStr.append("define " + "\""+ definition.getDefinitionName() + "\""
						+ ": ");
				cqlStr = cqlStr.append(definition.getDefinitionLogic());
				cqlStr = cqlStr.append("\n\n");
			}
			
		}
		
		for (CQLFunctions function : functionsList) {
			
			if(function.getFunctionName().equalsIgnoreCase(toBeInserted)){
				toBeInsertedAtEnd = toBeInsertedAtEnd.append("define function "
						+ "\""+ function.getFunctionName() + "\"" + "(");
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
					toBeInsertedAtEnd = toBeInsertedAtEnd.append("\""+ argument.getArgumentName() + "\" "
							+ argumentType + ", ");
				}
				toBeInsertedAtEnd.deleteCharAt(toBeInsertedAtEnd.length() - 2);
			}
				
				toBeInsertedAtEnd = toBeInsertedAtEnd.append("): " + function.getFunctionLogic());
				//toBeInsertedAtEnd = toBeInsertedAtEnd.append("\n\n");
			} else {
				cqlStr = cqlStr.append("define function "
						+ "\""+ function.getFunctionName() + "\"" + "(");
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
					cqlStr = cqlStr.append("\""+ argument.getArgumentName() + "\" "
							+ argumentType + ", ");
				}
				cqlStr.deleteCharAt(cqlStr.length() - 2);
			}
				
				cqlStr = cqlStr.append("): " + function.getFunctionLogic());
				cqlStr = cqlStr.append("\n\n");
			}
 			
			
			
		}
		
		return cqlStr;
	}


	public static CQLModel getCQLStringFromMeasureXML(String measureXML,String measureId){
		
		CQLModel cqlModel = new CQLModel();
		XmlProcessor measureXMLProcessor = new XmlProcessor(measureXML);
		String cqlLookUpXMLString = measureXMLProcessor.getXmlByTagName("cqlLookUp");
		
		if(StringUtils.isNotBlank(cqlLookUpXMLString)){
			getCQLGeneralInfo(cqlModel, measureXMLProcessor);
			getValueSet(cqlModel, cqlLookUpXMLString);
			getCQLDefinitionsInfo(cqlModel, cqlLookUpXMLString);
			getCQLParametersInfo(cqlModel,cqlLookUpXMLString);
			getCQLFunctionsInfo(cqlModel, cqlLookUpXMLString);
		}
		
		return cqlModel;
	}
	
	private static void getValueSet(CQLModel cqlModel, String cqlLookUpXMLString){
		QualityDataModelWrapper valuesetWrapper;
		try {			 

			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("ValueSetsMapping.xml"));
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setClass(QualityDataModelWrapper.class);
			unmarshaller.setWhitespacePreserve(true);

			valuesetWrapper = (QualityDataModelWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			if(!valuesetWrapper.getQualityDataDTO().isEmpty()){
				cqlModel.setValueSetList(convertToCQLQualityDataSetDTO(valuesetWrapper.getQualityDataDTO()));
			}
		} catch (Exception e) {
			logger.info("Error while getting valueset :" +e.getMessage());
		}

	}
	
	private static void getCQLGeneralInfo(CQLModel cqlModel, XmlProcessor measureXMLProcessor) {
		
		String libraryNameStr = "";
		String usingModelStr = "";
		CQLLibraryModel libraryModel = new CQLLibraryModel();
		CQLDataModel usingModel = new CQLDataModel();
		
		
		if (measureXMLProcessor != null) {
					
			String XPATH_EXPRESSION_CQLLOOKUP_lIBRARY = "/measure/cqlLookUp/library/text()";
			String XPATH_EXPRESSION_CQLLOOKUP_USING = "/measure/cqlLookUp/usingModel/text()";
			
			try {
				
				Node nodeCQLLibrary = measureXMLProcessor.findNode(
						measureXMLProcessor.getOriginalDoc(),
						XPATH_EXPRESSION_CQLLOOKUP_lIBRARY);
				Node nodeCQLUsingModel = measureXMLProcessor.findNode(
						measureXMLProcessor.getOriginalDoc(),
						XPATH_EXPRESSION_CQLLOOKUP_USING);
				
				if (nodeCQLLibrary != null) {
					libraryNameStr = nodeCQLLibrary.getTextContent();
					libraryModel.setLibraryName(libraryNameStr);
				}
				
				if (nodeCQLUsingModel != null) {
					usingModelStr = nodeCQLUsingModel.getTextContent();
					usingModel.setName(usingModelStr);
				}
				
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
		}
		
		cqlModel.setLibrary(libraryModel);
		cqlModel.setUsedModel(usingModel);
		
	}
	
	private static void getCQLDefinitionsInfo(CQLModel cqlModel, String cqlLookUpXMLString) {
		CQLDefinitionsWrapper details = null;
		
		try {			 
			
			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLDefinitionModelMapping.xml"));
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setClass(CQLDefinitionsWrapper.class);
			unmarshaller.setWhitespacePreserve(true);
			
			details = (CQLDefinitionsWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			cqlModel.setDefinitionList(details.getCqlDefinitions());
			
		} catch (Exception e) {
			logger.info("Error while getting cql definition :" +e.getMessage());
		}
		
	}

	private static void getCQLParametersInfo(CQLModel cqlModel, String cqlLookUpXMLString) {
		
		CQLParametersWrapper details = null;
		try {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLParameterModelMapping.xml"));
				Unmarshaller unmarshaller = new Unmarshaller(mapping);
				unmarshaller.setClass(CQLParametersWrapper.class);
				unmarshaller.setWhitespacePreserve(true);
			
				details = (CQLParametersWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
				cqlModel.setCqlParameters(details.getCqlParameterList());
		} catch (Exception e) {
			logger.info("Error while getting cql parameters :" +e.getMessage());
		}
		
	}

	private static void getCQLFunctionsInfo(CQLModel cqlModel, String cqlLookUpXMLString) {
		
		CQLFunctionsWrapper details = null;
		try {
			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLFunctionModelMapping.xml"));
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setClass(CQLFunctionsWrapper.class);
			unmarshaller.setWhitespacePreserve(true);
			details = (CQLFunctionsWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			cqlModel.setCqlFunctions(details.getCqlFunctionsList());
		} catch (Exception e) {
			logger.info("Error while getting cql functions :" +e.getMessage());
		}
	}
	
	private static List<CQLQualityDataSetDTO> convertToCQLQualityDataSetDTO(List<QualityDataSetDTO> qualityDataSetDTO){
		List<CQLQualityDataSetDTO> convertedCQLDataSetList = new ArrayList<CQLQualityDataSetDTO>();
			for (QualityDataSetDTO tempDataSet : qualityDataSetDTO) {
				CQLQualityDataSetDTO convertedCQLDataSet = new CQLQualityDataSetDTO();
				if(!tempDataSet.getDataType().equalsIgnoreCase("Patient characteristic Birthdate") && !tempDataSet.getDataType().equalsIgnoreCase("Patient characteristic Expired")){
					convertedCQLDataSet.setCodeListName(tempDataSet.getCodeListName());
					convertedCQLDataSet.setCodeSystemName(tempDataSet.getCodeSystemName());
					convertedCQLDataSet.setDataType(tempDataSet.getDataType());
					convertedCQLDataSet.setId(tempDataSet.getId());
					convertedCQLDataSet.setOid(tempDataSet.getOid());
					convertedCQLDataSet.setSuppDataElement(tempDataSet.isSuppDataElement());
					convertedCQLDataSet.setTaxonomy(tempDataSet.getTaxonomy());
					convertedCQLDataSet.setType(tempDataSet.getType());
					convertedCQLDataSet.setUuid(tempDataSet.getUuid());
					convertedCQLDataSet.setVersion(tempDataSet.getVersion());
					convertedCQLDataSetList.add(convertedCQLDataSet);
				}
				
			}
		return convertedCQLDataSetList;
		
	}
	
	public static int countLines(String str) {
	    if(str == null || str.isEmpty())
	    {
	        return 0;
	    }
	    int lines = 1;
	    int pos = 0;
	    while ((pos = str.indexOf("\n\n", pos) + 1) != 0) {
	        lines = lines + 2;
	    }
	    return lines;
	}
	
}
