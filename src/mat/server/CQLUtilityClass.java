package mat.server;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.xml.xpath.XPathExpressionException;

import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLCodeSystemWrapper;
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
import mat.shared.ConstantMessages;

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
	
	private static int size; 
	
	public static int getSize() {
		return size; 
	}
	
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
			cqlStr = cqlStr.append(" version "
					+ "'" + cqlModel.getLibrary().getVersionUsed());
			cqlStr = cqlStr.append("'");
			
			cqlStr = cqlStr.append("\n\n");
			
			cqlStr = cqlStr.append("using QDM");
			
			cqlStr = cqlStr.append(" version ");
			cqlStr = cqlStr.append("'");
			cqlStr = cqlStr.append(cqlModel.getUsedModel().getQdmVersion());
			cqlStr = cqlStr.append("'");
			cqlStr = cqlStr.append("\n\n");
		}

		//CodeSystems
		List<CQLCodeSystem> codeSystemList = cqlModel.getCodeSystemList();
		List<String> codeSystemAlreadyUsed = new ArrayList<String>();
		if(codeSystemList != null){
			for(CQLCodeSystem codeSystem : codeSystemList){
				String codeSysStr = codeSystem.getCodeSystemName() + ":" 
			                          + codeSystem.getCodeSystemVersion();
				String version = codeSystem.getCodeSystemVersion().replaceAll(" ", "%20");
				if(!codeSystemAlreadyUsed.contains(codeSysStr)){
					cqlStr = cqlStr.append("codesystem \"" + codeSysStr+'"').append(": ")
							.append("'urn:oid:" + codeSystem.getCodeSystem() +"' ");
					cqlStr = cqlStr.append("version 'urn:hl7:version:" + version +"'");
					cqlStr = cqlStr.append("\n\n");
					codeSystemAlreadyUsed.add(codeSysStr);
				}
				
			}
		}
		
		//Valuesets
		List<CQLQualityDataSetDTO> valueSetList = cqlModel.getValueSetList();
		List<String> valueSetAlreadyUsed = new ArrayList<String>();
		if (valueSetList != null) {
			for (CQLQualityDataSetDTO valueset : valueSetList) {
				if(!valueSetAlreadyUsed.contains(valueset.getCodeListName())){
					String expIdentifier = "";
					String version = valueset.getVersion().replaceAll(" ", "%20");
					if(valueset.getExpansionIdentifier() != null){
						expIdentifier = valueset.getExpansionIdentifier().replaceAll(" ", "%20");
					}
					cqlStr = cqlStr.append("valueset "
							+'"'+ valueset.getCodeListName() +'"'+ ": "
							+"'urn:oid:"+ valueset.getOid()+"' "
							);
					List<String> codeSysName = getCodeSysName(valueset.getOid(),cqlModel);
					
					//Check if QDM has expansionidentifier or not.
					if(expIdentifier.equalsIgnoreCase("")){
						if(!version.equalsIgnoreCase("1.0")){
							cqlStr = cqlStr.append("version 'urn:hl7:version:" + version +"' ");
						}
					}
					else{
						cqlStr = cqlStr.append("version 'urn:hl7:profile:" + expIdentifier +"' ");
					}
					/*if(!valueset.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)){
						cqlStr = cqlStr.append("codesystems {"+'"');
						Iterator<String> codeSysNameIterator = codeSysName.iterator();
						while (codeSysNameIterator.hasNext()) {
							cqlStr = cqlStr.append(codeSysNameIterator.next());
							if(codeSysNameIterator.hasNext()){
								cqlStr = cqlStr.append('"'+", "+'"');
							}
						}
						cqlStr = cqlStr.append('"'+"}");
					}*/
					cqlStr = cqlStr.append("\n\n");
					valueSetAlreadyUsed.add(valueset.getCodeListName());
				}
			}
		}

		//code
				List<CQLDefinition> definitionList = cqlModel.getDefinitionList();
				List<CQLFunctions> functionList = cqlModel.getCqlFunctions();
				//initialize to false to display only once.
				boolean birthDateEntry = false;
				boolean deadEntry = false;
				if (definitionList != null) {
					for (int i = 0; i < definitionList.size(); i++) {
						if (definitionList.get(i).getDefinitionLogic().matches(".*\\b"+ConstantMessages.BIRTHDATE+"\\b.*") && birthDateEntry == false) {
							cqlStr = cqlStr.append("code "+'"'+"Birthdate"+'"'+": '21112-8' from "+'"'+"LOINC:2.46"+'"'+" display 'Birthdate'");
							cqlStr = cqlStr.append("\n\n");
							birthDateEntry = true;
						} else if (definitionList.get(i).getDefinitionLogic().matches(".*\\b"+ConstantMessages.DEAD+"\\b.*") && deadEntry == false) {
							cqlStr = cqlStr.append("code "+'"'+"Dead"+'"'+": '419099009' from "+'"'+"SNOMEDCT:2016-03"+'"'+" display 'Dead'");
							cqlStr = cqlStr.append("\n\n");
							deadEntry = true;
						} 
					}
				}
				if (functionList != null) {
					for (int i = 0; i < functionList.size(); i++) {
						if (functionList.get(i).getFunctionLogic().matches(".*\\b"+ConstantMessages.BIRTHDATE+"\\b.*") && birthDateEntry == false) {
							cqlStr = cqlStr.append("code "+'"'+"Birthdate"+'"'+": '21112-8' from "+'"'+"LOINC:2.46"+'"'+" display 'Birth date'");
							cqlStr = cqlStr.append("\n\n");
							birthDateEntry = true;
						} else if (functionList.get(i).getFunctionLogic().matches(".*\\b"+ConstantMessages.DEAD+"\\b.*") && deadEntry == false){
							cqlStr = cqlStr.append("code "+'"'+"Dead"+'"'+": '419099009' from "+'"'+"SNOMEDCT:2016-03"+'"'+" display 'Dead'");
							cqlStr = cqlStr.append("\n\n");
							deadEntry = true;
						} 
					}
				}
		
		// parameters
		List<CQLParameter> paramList = cqlModel.getCqlParameters();
		if (paramList != null) {
			for (CQLParameter parameter : paramList) {
				System.out.println(parameter.getParameterName());
					
				String param = "parameter "
						+ "\""+parameter.getParameterName()+ "\"";
				
				
				
					cqlStr = cqlStr.append(param + " "
							+ parameter.getParameterLogic());
					cqlStr = cqlStr.append("\n\n");
					
					// if the the param we just appended is the current one, then
					// find the size of the file at that time. 
					// This will give us the end line of the parameter we are trying to insert. 
					if(param.equalsIgnoreCase(toBeInserted)) {
						size = getEndLine(cqlStr.toString());
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
	

	private static List<String> getCodeSysName(String oid, CQLModel cqlModel) {
		//CodeSystems
		List<String> endresult = new ArrayList<String>();
		List<CQLCodeSystem> codeSystemList = cqlModel.getCodeSystemList();
		if(oid != null && codeSystemList!=null){
			for (CQLCodeSystem existingList : codeSystemList) {
				if(existingList.getValueSetOID()!=null && existingList.getValueSetOID().equalsIgnoreCase(oid)){
					endresult.add(existingList.getCodeSystemName()+":"+existingList.getCodeSystemVersion());
				}
			}
		}
		return endresult;
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
			
				String def = "define " + "\""+ definition.getDefinitionName() + "\"";
			
				cqlStr = cqlStr.append(def + ": ");
				cqlStr = cqlStr.append(definition.getDefinitionLogic());
				cqlStr = cqlStr.append("\n\n");	
				
				// if the the def we just appended is the current one, then
				// find the size of the file at that time. ;-
				// This will give us the end line of the definition we are trying to insert. 
				if(def.equalsIgnoreCase(toBeInserted.toString())) {
					size = getEndLine(cqlStr.toString());
				}
				
		}
		
		for (CQLFunctions function : functionsList) {
				String func = "define function "
						+ "\""+ function.getFunctionName() + "\"";
			
			
				cqlStr = cqlStr.append(func + "(");
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
				
				// if the the func we just appended is the current one, then
				// find the size of the file at that time. 
				// This will give us the end line of the function we are trying to insert. 
				if(func.equalsIgnoreCase(toBeInserted)) {
					size = getEndLine(cqlStr.toString());
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
			getCodeSystems(cqlModel, cqlLookUpXMLString);
			getValueSet(cqlModel, cqlLookUpXMLString);
			getCQLDefinitionsInfo(cqlModel, cqlLookUpXMLString);
			getCQLParametersInfo(cqlModel,cqlLookUpXMLString);
			getCQLFunctionsInfo(cqlModel, cqlLookUpXMLString);
		}
		
		return cqlModel;
	}
	
	private static void getCodeSystems(CQLModel cqlModel, String cqlLookUpXMLString) {
		CQLCodeSystemWrapper codeSystemWrapper;
		try {			 

			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CodeSystemsMapping.xml"));
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setClass(CQLCodeSystemWrapper.class);
			unmarshaller.setWhitespacePreserve(true);

			codeSystemWrapper = (CQLCodeSystemWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			if(!codeSystemWrapper.getCqlCodeSystemList().isEmpty()){
				cqlModel.setCodeSystemList(codeSystemWrapper.getCqlCodeSystemList());
			}
		} catch (Exception e) {
			logger.info("Error while getting valueset :" +e.getMessage());
		}
		
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
		String usingModelVer = "";
		String versionStr = "";
		CQLLibraryModel libraryModel = new CQLLibraryModel();
		CQLDataModel usingModel = new CQLDataModel();
		
		
		if (measureXMLProcessor != null) {
					
			String XPATH_EXPRESSION_CQLLOOKUP_lIBRARY = "/measure/cqlLookUp/library/text()";
			String XPATH_EXPRESSION_CQLLOOKUP_USING = "/measure/cqlLookUp/usingModel/text()";
			String XPATH_EXPRESSION_CQLLOOKUP_USING_VERSION = "/measure/cqlLookUp/usingModelVersion/text()";
			String XPATH_EXPRESSION_CQLLOOKUP_VERSION = "/measure/measureDetails/version/text()";
			
			try {
				
				Node nodeCQLLibrary = measureXMLProcessor.findNode(
						measureXMLProcessor.getOriginalDoc(),
						XPATH_EXPRESSION_CQLLOOKUP_lIBRARY);
				Node nodeCQLUsingModel = measureXMLProcessor.findNode(
						measureXMLProcessor.getOriginalDoc(),
						XPATH_EXPRESSION_CQLLOOKUP_USING);
				Node nodeCQLUsingModelVersion = measureXMLProcessor.findNode(
						measureXMLProcessor.getOriginalDoc(),
						XPATH_EXPRESSION_CQLLOOKUP_USING_VERSION);
				Node nodeCQLVersion = measureXMLProcessor.findNode(
						measureXMLProcessor.getOriginalDoc(),
						XPATH_EXPRESSION_CQLLOOKUP_VERSION);
				
				if (nodeCQLLibrary != null) {
					libraryNameStr = nodeCQLLibrary.getTextContent();
					libraryModel.setLibraryName(libraryNameStr);
				}
				
				if (nodeCQLUsingModel != null) {
					usingModelStr = nodeCQLUsingModel.getTextContent();
					usingModel.setName(usingModelStr);
				}
				
				if (nodeCQLUsingModelVersion != null) {
					usingModelVer = nodeCQLUsingModelVersion.getTextContent();
					usingModel.setQdmVersion(usingModelVer);
				}
				
				if (nodeCQLVersion != null) {
					versionStr = nodeCQLVersion.getTextContent();
					libraryModel.setVersionUsed(versionStr);
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
					convertedCQLDataSet.setDataTypeHasRemoved(tempDataSet.isDataTypeHasRemoved());
					convertedCQLDataSet.setExpansionIdentifier(tempDataSet.getExpansionIdentifier());
					convertedCQLDataSet.setVsacExpIdentifier(tempDataSet.getVsacExpIdentifier());
					convertedCQLDataSetList.add(convertedCQLDataSet);
				}
				
			}
		return convertedCQLDataSetList;
		
	}
	
	private static int getEndLine(String cqlString) {
		System.out.println("Get end line");
		Scanner scanner = new Scanner(cqlString);
		
		int endLine = -1; 
		while(scanner.hasNextLine()) {
			endLine++;
			scanner.nextLine(); 
		}
		
		
		return endLine; 
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
