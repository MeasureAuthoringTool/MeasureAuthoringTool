package mat.server;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
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

	public static String getCqlString(CQLModel cqlModel, String toBeInserted) {

		StringBuilder cqlStr = new StringBuilder();
		toBeInsertedAtEnd = new  StringBuilder();
		// library Name and Using
		if (cqlModel.getLibraryName() != null) {
			cqlStr = cqlStr.append("library "
					+ cqlModel.getLibraryName());
			cqlStr = cqlStr.append(" version "
					+ "'" + cqlModel.getVersionUsed());
			cqlStr = cqlStr.append("'");

			cqlStr = cqlStr.append("\n\n");

			cqlStr = cqlStr.append("using QDM");

			cqlStr = cqlStr.append(" version ");
			cqlStr = cqlStr.append("'");
			cqlStr = cqlStr.append(cqlModel.getQdmVersion());
			cqlStr = cqlStr.append("'");
			cqlStr = cqlStr.append("\n\n");
		}

		//includes
		List<CQLIncludeLibrary> includeLibList = cqlModel.getCqlIncludeLibrarys();
		if(includeLibList != null){
			for(CQLIncludeLibrary includeLib : includeLibList){
				cqlStr = cqlStr.append("include ").append(includeLib.getCqlLibraryName());
				cqlStr = cqlStr.append(" version ").append("'").append(includeLib.getVersion()).append("' ");
				cqlStr = cqlStr.append("called ").append(includeLib.getAliasName());
				cqlStr = cqlStr.append("\n\n");
			}
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
					cqlStr = cqlStr.append("\n");
					codeSystemAlreadyUsed.add(codeSysStr);
				}

			}

			cqlStr = cqlStr.append("\n");
		}

		//Valuesets
		List<CQLQualityDataSetDTO> valueSetList = cqlModel.getValueSetList();
		List<String> valueSetAlreadyUsed = new ArrayList<String>();
		if (valueSetList != null) {
			for (CQLQualityDataSetDTO valueset : valueSetList) {
				if(!valueSetAlreadyUsed.contains(valueset.getCodeListName())){
					//String expIdentifier = "";
					String version = valueset.getVersion().replaceAll(" ", "%20");
					/*if(valueset.getExpansionIdentifier() != null){
						expIdentifier = valueset.getExpansionIdentifier().replaceAll(" ", "%20");
					}*/
					cqlStr = cqlStr.append("valueset "
							+'"'+ valueset.getCodeListName() +'"'+ ": "
							+"'urn:oid:"+ valueset.getOid()+"' "
							);
					List<String> codeSysName = getCodeSysName(valueset.getOid(),cqlModel);

					//Check if QDM has expansionidentifier or not.
					//if(expIdentifier.equalsIgnoreCase("")){
						if(!version.equalsIgnoreCase("1.0")){
							cqlStr = cqlStr.append("version 'urn:hl7:version:" + version +"' ");
						}

					cqlStr = cqlStr.append("\n");
					valueSetAlreadyUsed.add(valueset.getCodeListName());
				}
			}

			cqlStr = cqlStr.append("\n");
		}

		//Codes
		List<CQLCode> codeList = cqlModel.getCodeList();
		List<String> codesAlreadyUsed = new ArrayList<String>();
		if(codeList != null){
			for(CQLCode codes : codeList){
				String codesStr = '"'+codes.getDisplayName()+'"'+ ": "
			                          +"'" +codes.getCodeOID()+"'";
				String codeSysStr = codes.getCodeSystemName() + ":"
                          + codes.getCodeSystemVersion().replaceAll(" ", "%20");
				//boolean containsSearchStr = codesAlreadyUsed.stream().filter(s -> s.equalsIgnoreCase(codesStr)).findFirst().isPresent();
				//System.out.println("Code is :: "+ codesStr + ": " + containsSearchStr);
				if(!codesAlreadyUsed.contains(codesStr)){
					cqlStr = cqlStr.append("code " + codesStr).append(" ")
							.append("from " + '"' + codeSysStr + '"' +" ");
					cqlStr = cqlStr.append("display " +"'" +codes.getCodeName().replaceAll("'", "\\\\'")+"'");
					cqlStr = cqlStr.append("\n");
					codesAlreadyUsed.add(codesStr);
				}

			}

			cqlStr = cqlStr.append("\n");
		}

		// parameters
		List<CQLParameter> paramList = cqlModel.getCqlParameters();
		if (paramList != null) {
			for (CQLParameter parameter : paramList) {
				System.out.println(parameter.getParameterName());

				String param = "parameter "
						+ "\""+parameter.getParameterName()+ "\"";


					String commentString = parameter.getCommentString();
					if(commentString != null && commentString.trim().length() > 0){
						commentString = "/*" + commentString + "*/" + "\n";
						cqlStr.append(commentString);
					}

					cqlStr = cqlStr.append(param + " "
							+ parameter.getParameterLogic());
					cqlStr = cqlStr.append("\n");

					// if the the param we just appended is the current one, then
					// find the size of the file at that time.
					// This will give us the end line of the parameter we are trying to insert.
					if(param.equalsIgnoreCase(toBeInserted)) {
						size = getEndLine(cqlStr.toString());
					}

				}

			cqlStr.append("\n");
		}

		// Definitions and Functions by Context
		if(cqlModel.getDefinitionList().size() > 0 || cqlModel.getCqlFunctions().size() > 0){
			cqlStr = getDefineAndFunctionsByContext(cqlModel.getDefinitionList(),
					cqlModel.getCqlFunctions(), cqlStr, toBeInserted);
		} else {
			cqlStr = cqlStr.append("context").append(" " + PATIENT).append("\n\n");
		}

		//cqlModel.setLines(countLines(cqlStr.toString()));

		/*if(!toBeInsertedAtEnd.toString().isEmpty()){
			cqlStr = cqlStr.append(toBeInsertedAtEnd.toString());
		}*/

		return cqlStr.toString();

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

				String definitionComment = definition.getCommentString();
				if(definitionComment != null && definitionComment.trim().length() > 0){
					definitionComment = "/*" + definitionComment + "*/" + "\n";
					cqlStr = cqlStr.append(definitionComment);
				}

				String def = "define " + "\""+ definition.getDefinitionName() + "\"";

				cqlStr = cqlStr.append(def + ":\n");
				cqlStr = cqlStr.append("\t" + definition.getDefinitionLogic().replaceAll("\\n", "\n\t"));
				cqlStr = cqlStr.append("\n\n");

				// if the the def we just appended is the current one, then
				// find the size of the file at that time. ;-
				// This will give us the end line of the definition we are trying to insert.
				if(def.equalsIgnoreCase(toBeInserted.toString())) {
					size = getEndLine(cqlStr.toString());
				}

		}

		for (CQLFunctions function : functionsList) {

			String functionComment = function.getCommentString();
			if(functionComment != null && functionComment.trim().length() > 0){
				functionComment = "/*" + functionComment + "*/" + "\n";
				cqlStr = cqlStr.append(functionComment);
			}

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
					cqlStr = cqlStr.append( argument.getArgumentName()+ " "
							+ argumentType + ", ");
				}
				cqlStr.deleteCharAt(cqlStr.length() - 2);
			}

				cqlStr = cqlStr.append("):\n" + "\t" + function.getFunctionLogic().replaceAll("\\n", "\n\t"));
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


	public static CQLModel getCQLStringFromXML(String xmlString) {
		CQLModel cqlModel = new CQLModel();
		XmlProcessor measureXMLProcessor = new XmlProcessor(xmlString);
		String cqlLookUpXMLString = measureXMLProcessor.getXmlByTagName("cqlLookUp");

		if (StringUtils.isNotBlank(cqlLookUpXMLString)) {
			try {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLModelMapping.xml"));
				Unmarshaller unmarshaller = new Unmarshaller(mapping);
				unmarshaller.setClass(CQLModel.class);
				unmarshaller.setWhitespacePreserve(true);
				unmarshaller.setValidation(false);
				cqlModel = (CQLModel) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			} catch (Exception e) {
				logger.info("Error while getting codesystems :" + e.getMessage());
			}
		}
		if(!cqlModel.getValueSetList().isEmpty()){
			List<CQLQualityDataSetDTO> filterVS  = filterValuesets(cqlModel.getValueSetList());
			cqlModel.setValueSetList(filterVS);
			ArrayList<CQLQualityDataSetDTO> valueSetsList = new ArrayList<CQLQualityDataSetDTO>();
			valueSetsList.addAll(cqlModel.getValueSetList());
			// sorting out CQL all Value sets and codes
			sortCQLQualityDataSetDto(valueSetsList);
			cqlModel.setAllValueSetList(valueSetsList);
		}
		sortCQLCodeDTO(cqlModel.getCodeList());
		if(!cqlModel.getCodeList().isEmpty()){
			//Combine Codes and Value sets in allValueSetList for UI
			List<CQLQualityDataSetDTO> dtoList = convertCodesToQualityDataSetDTO(cqlModel.getCodeList());
			if(!dtoList.isEmpty()){
				cqlModel.getAllValueSetList().addAll(dtoList);
			}
		}
		return cqlModel;
	}


	public static void getValueSet(CQLModel cqlModel, String cqlLookUpXMLString){
		CQLQualityDataModelWrapper valuesetWrapper;
		try {

			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("ValueSetsMapping.xml"));
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setClass(CQLQualityDataModelWrapper.class);
			unmarshaller.setWhitespacePreserve(true);
			unmarshaller.setValidation(false);
			valuesetWrapper = (CQLQualityDataModelWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			if(!valuesetWrapper.getQualityDataDTO().isEmpty()){
				cqlModel.setValueSetList(filterValuesets(valuesetWrapper.getQualityDataDTO()));
			}
		} catch (Exception e) {
			logger.info("Error while getting valueset :" +e.getMessage());
		}

	}


	private static List<CQLQualityDataSetDTO> convertCodesToQualityDataSetDTO(List<CQLCode> codeList){
		List<CQLQualityDataSetDTO> convertedCQLDataSetList = new ArrayList<CQLQualityDataSetDTO>();
			for (CQLCode tempDataSet : codeList) {
				CQLQualityDataSetDTO convertedCQLDataSet = new CQLQualityDataSetDTO();
					convertedCQLDataSet.setCodeListName(tempDataSet.getCodeName());
					convertedCQLDataSet.setCodeSystemName(tempDataSet.getCodeSystemName());
					convertedCQLDataSet.setCodeSystemOID(tempDataSet.getCodeSystemOID());
					
					convertedCQLDataSet.setCodeIdentifier(tempDataSet.getCodeIdentifier());
					convertedCQLDataSet.setId(tempDataSet.getId());
					convertedCQLDataSet.setOid(tempDataSet.getCodeOID());
					convertedCQLDataSet.setVersion(tempDataSet.getCodeSystemVersion());
					convertedCQLDataSet.setDisplayName(tempDataSet.getDisplayName());
					convertedCQLDataSet.setSuffix(tempDataSet.getSuffix());

					convertedCQLDataSet.setReadOnly(tempDataSet.isReadOnly());
					
					convertedCQLDataSet.setType("code");
					convertedCQLDataSetList.add(convertedCQLDataSet);


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

		scanner.close();
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

	public static List<CQLQualityDataSetDTO> sortCQLQualityDataSetDto(List<CQLQualityDataSetDTO> cqlQualityDataSetDTOs){
		Collections.sort(cqlQualityDataSetDTOs, new Comparator<CQLQualityDataSetDTO>() {
			@Override
			public int compare(final CQLQualityDataSetDTO o1, final CQLQualityDataSetDTO o2) {
				return o1.getCodeListName().compareToIgnoreCase(o2.getCodeListName());
			}
		});

		return cqlQualityDataSetDTOs;
	}

	public static List<CQLCode> sortCQLCodeDTO(List<CQLCode> cqlCode){
		Collections.sort(cqlCode, new Comparator<CQLCode>() {
			@Override
			public int compare(final CQLCode o1, final CQLCode o2) {
				return o1.getCodeName().compareToIgnoreCase(o2.getCodeName());
			}
		});

		return cqlCode;
	}

	private static List<CQLQualityDataSetDTO> filterValuesets(List<CQLQualityDataSetDTO> cqlValuesets){

		List<CQLQualityDataSetDTO> filteredValuesets = new ArrayList<CQLQualityDataSetDTO>();

		for(int i=0; i<cqlValuesets.size(); i++){
			CQLQualityDataSetDTO cqlQualityDataSetDTO = cqlValuesets.get(i);
			if(cqlQualityDataSetDTO.getDataType()!= null){
				if(!cqlQualityDataSetDTO.getDataType().equalsIgnoreCase("Patient characteristic Birthdate")
						&& !cqlQualityDataSetDTO.getDataType().equalsIgnoreCase("Patient characteristic Expired")){
					filteredValuesets.add(cqlQualityDataSetDTO);
				}
			} else {
				filteredValuesets.add(cqlQualityDataSetDTO);
			}
		}

		return filteredValuesets;

	}

	/*
	private static void getCodeSystems(CQLModel cqlModel, String cqlLookUpXMLString) {
		CQLCodeSystemWrapper codeSystemWrapper;
		try {

			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CodeSystemsMapping.xml"));
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setClass(CQLCodeSystemWrapper.class);
			unmarshaller.setWhitespacePreserve(true);
			unmarshaller.setValidation(false);
			codeSystemWrapper = (CQLCodeSystemWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			if(!codeSystemWrapper.getCqlCodeSystemList().isEmpty()){
				cqlModel.setCodeSystemList(codeSystemWrapper.getCqlCodeSystemList());
			}
		} catch (Exception e) {
			logger.info("Error while getting codesystems :" +e.getMessage());
		}

	}

	private static void getCodes(CQLModel cqlModel, String cqlLookUpXMLString) {
		CQLCodeWrapper codeWrapper;
		try {

			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CodeMapping.xml"));
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setClass(CQLCodeWrapper.class);
			unmarshaller.setWhitespacePreserve(true);
			unmarshaller.setValidation(false);
			codeWrapper = (CQLCodeWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			if(!codeWrapper.getCqlCodeList().isEmpty()){
				cqlModel.setCodeList(codeWrapper.getCqlCodeList());
				//Combine Codes and Value sets in allValueSetList for UI
				List<CQLQualityDataSetDTO> dtoList = convertCodesToQualityDataSetDTO(codeWrapper.getCqlCodeList());
				if(!dtoList.isEmpty()){
					cqlModel.getAllValueSetList().addAll(dtoList);
				}
			}
		} catch (Exception e) {
			logger.info("Error while getting codes :" +e.getMessage());
		}

	}*/

	/*private static void getCQLGeneralInfo(CQLModel cqlModel, XmlProcessor measureXMLProcessor) {

		String libraryNameStr = "";
		String usingModelStr = "";
		String usingModelVer = "";
		String versionStr = "";
		//CQLLibraryModel libraryModel = new CQLLibraryModel();
		//CQLDataModel usingModel = new CQLDataModel();


		if (measureXMLProcessor != null) {

			String XPATH_EXPRESSION_CQLLOOKUP_lIBRARY = "//cqlLookUp/library/text()";
			String XPATH_EXPRESSION_CQLLOOKUP_USING = "//cqlLookUp/usingModel/text()";
			String XPATH_EXPRESSION_CQLLOOKUP_USING_VERSION = "//cqlLookUp/usingModelVersion/text()";
			String XPATH_EXPRESSION_CQLLOOKUP_VERSION = "//cqlLookUp/version/text()";

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
					cqlModel.setLibraryName(libraryNameStr);
				}

				if (nodeCQLUsingModel != null) {
					usingModelStr = nodeCQLUsingModel.getTextContent();
					cqlModel.setName(usingModelStr);
				}

				if (nodeCQLUsingModelVersion != null) {
					usingModelVer = nodeCQLUsingModelVersion.getTextContent();
					cqlModel.setQdmVersion(usingModelVer);
				}

				if (nodeCQLVersion != null) {
					versionStr = nodeCQLVersion.getTextContent();
					cqlModel.setVersionUsed(versionStr);
				}

			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

		}

		//cqlModel.setLibrary(libraryModel);
		//cqlModel.setUsedModel(usingModel);

	}*/

	/*private static void getCQLDefinitionsInfo(CQLModel cqlModel, String cqlLookUpXMLString) {
		CQLDefinitionsWrapper details = null;

		try {

			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLDefinitionModelMapping.xml"));
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setClass(CQLDefinitionsWrapper.class);
			unmarshaller.setWhitespacePreserve(true);
			unmarshaller.setValidation(false);
			details = (CQLDefinitionsWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			cqlModel.setDefinitionList(details.getCqlDefinitions());

		} catch (Exception e) {
			logger.info("Error while getting cql definition :" +e.getMessage());
		}

	}*/

	/*private static void getCQLParametersInfo(CQLModel cqlModel, String cqlLookUpXMLString) {

		CQLParametersWrapper details = null;
		try {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLParameterModelMapping.xml"));
				Unmarshaller unmarshaller = new Unmarshaller(mapping);
				unmarshaller.setClass(CQLParametersWrapper.class);
				unmarshaller.setWhitespacePreserve(true);
				unmarshaller.setValidation(false);
				details = (CQLParametersWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
				cqlModel.setCqlParameters(details.getCqlParameterList());
		} catch (Exception e) {
			logger.info("Error while getting cql parameters :" +e.getMessage());
		}

	}*/

	/*private static void getCQLFunctionsInfo(CQLModel cqlModel, String cqlLookUpXMLString) {

		CQLFunctionsWrapper details = null;
		try {
			Mapping mapping = new Mapping();

			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLFunctionModelMapping.xml"));
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setClass(CQLFunctionsWrapper.class);
			unmarshaller.setWhitespacePreserve(true);

			unmarshaller.setValidation(false);
			details = (CQLFunctionsWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			cqlModel.setCqlFunctions(details.getCqlFunctionsList());
		} catch (Exception e) {
			logger.info("Error while getting cql functions :" +e.getMessage());
		}
	}*/

/*	private static List<CQLQualityDataSetDTO> convertToCQLQualityDataSetDTO(List<CQLQualityDataSetDTO> qualityDataSetDTO){
		List<CQLQualityDataSetDTO> convertedCQLDataSetList = new ArrayList<CQLQualityDataSetDTO>();
			for (CQLQualityDataSetDTO tempDataSet : qualityDataSetDTO) {
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

	}*/



	/*private static void getCQLIncludeLibrarysInfo(CQLModel cqlModel, String cqlLookUpXMLString) {
		CQLIncludeLibraryWrapper details = null;

		try {

			Mapping mapping = new Mapping();
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLIncludeLibrayMapping.xml"));
			Unmarshaller unmarshaller = new Unmarshaller(mapping);
			unmarshaller.setClass(CQLIncludeLibraryWrapper.class);
			unmarshaller.setValidation(false);
			unmarshaller.setWhitespacePreserve(true);

			details = (CQLIncludeLibraryWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(cqlLookUpXMLString)));
			cqlModel.setCqlIncludeLibrarys(details.getCqlIncludeLibrary());

		} catch (Exception e) {
			logger.info("Error while getting cql definition :" +e.getMessage());
		}

	}*/



}
