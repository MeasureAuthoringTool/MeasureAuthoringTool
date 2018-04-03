package mat.server;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;
import mat.model.cql.CQLCode;
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

public final class CQLUtilityClass {

	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CQLUtilityClass.class);

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
		toBeInsertedAtEnd = new StringBuilder();
		// library Name and Using
		cqlStr.append(CQLUtilityClass.createLibraryNameSection(cqlModel));

		//includes
		cqlStr.append(CQLUtilityClass.createIncludesSection(cqlModel.getCqlIncludeLibrarys()));

		//CodeSystems
		cqlStr.append(CQLUtilityClass.createCodeSystemsSection(cqlModel.getCodeList()));

		//Valuesets
		cqlStr.append(CQLUtilityClass.createValueSetsSection(cqlModel.getValueSetList()));		

		//Codes
		cqlStr.append(CQLUtilityClass.createCodesSection(cqlModel.getCodeList()));

		// parameters
		CQLUtilityClass.createParameterSection(cqlModel.getCqlParameters(), cqlStr, toBeInserted);

		// Definitions and Functions by Context
		if(!cqlModel.getDefinitionList().isEmpty() || !cqlModel.getCqlFunctions().isEmpty()){
			getDefineAndFunctionsByContext(cqlModel.getDefinitionList(),
					cqlModel.getCqlFunctions(), cqlStr, toBeInserted);
		} else {
			cqlStr.append("context").append(" " + PATIENT).append("\n\n");
		}


		return cqlStr.toString();

	}

	private static String createLibraryNameSection(CQLModel cqlModel) {
		StringBuilder sb = new StringBuilder();

		if (cqlModel.getLibraryName() != null) {

			sb.append("library ").append(cqlModel.getLibraryName());
			sb.append(" version ").append("'" + cqlModel.getVersionUsed()).append("'");
			sb.append("\n\n");

			sb.append("using QDM version ");			
			sb.append("'").append(cqlModel.getQdmVersion()).append("'");
			sb.append("\n\n");			
		}

		return sb.toString();		
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

		if ((!contextPatDefineList.isEmpty()) || (!contextPatFuncList.isEmpty())) {

			getDefineAndFunctionsByContext(contextPatDefineList,
					contextPatFuncList, PATIENT, cqlStr, toBeInserted);
		}

		if ((!contextPopDefineList.isEmpty()) || (!contextPopFuncList.isEmpty())) {

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
			if(def.equalsIgnoreCase(toBeInserted)) {
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
					if (argument.getArgumentType().equalsIgnoreCase("QDM Datatype")) {
						argumentType = argumentType.append("\"").append(argument.getQdmDataType());
						if (argument.getAttributeName() != null) {
							argumentType = argumentType.append(".").append(argument.getAttributeName());
						}
						argumentType = argumentType.append("\"");
					} else if (argument.getArgumentType().equalsIgnoreCase(
							CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
						argumentType = argumentType.append(argument.getOtherType());
					} else {
						argumentType = argumentType.append(argument.getArgumentType());
					}
					cqlStr = cqlStr.append( argument.getArgumentName()+ " " + argumentType + ", ");
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
		if(StringUtils.isBlank(str))
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

		cqlQualityDataSetDTOs.sort((c1, c2) -> c1.getCodeListName().compareToIgnoreCase(c2.getCodeListName()));
		return cqlQualityDataSetDTOs;
	}

	public static List<CQLCode> sortCQLCodeDTO(List<CQLCode> cqlCodes){

		cqlCodes.sort((c1, c2) -> c1.getCodeName().compareToIgnoreCase(c2.getCodeName()));
		return cqlCodes;
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

	private static String createIncludesSection(List<CQLIncludeLibrary> includeLibList) {
		StringBuilder sb = new StringBuilder();
		if(includeLibList != null){
			for(CQLIncludeLibrary includeLib : includeLibList){
				sb.append("include ").append(includeLib.getCqlLibraryName());
				sb.append(" version ").append("'").append(includeLib.getVersion()).append("' ");
				sb.append("called ").append(includeLib.getAliasName());
				sb.append("\n\n");
			}
		}
		return sb.toString();
	}

	private static String createCodeSystemsSection(List<CQLCode> codeSystemList) {

		StringBuilder sb = new StringBuilder();

		List<String> codeSystemAlreadyUsed = new ArrayList<>();

		if(codeSystemList != null){

			for(CQLCode codes : codeSystemList){

				String codeSysStr = codes.getCodeSystemName();
				String codeSysVersion = "";

				if(codes.isIsCodeSystemVersionIncluded()) {
					codeSysStr = codeSysStr + ":" + codes.getCodeSystemVersion().replaceAll(" ", "%20");
					codeSysVersion = "version 'urn:hl7:version:" + codes.getCodeSystemVersion() + "'";
				}

				if(!codeSystemAlreadyUsed.contains(codeSysStr)){
					sb.append("codesystem \"").append(codeSysStr).append('"').append(": ");
					sb.append("'urn:oid:").append(codes.getCodeSystemOID()).append("' ");
					sb.append(codeSysVersion);
					sb.append("\n");

					codeSystemAlreadyUsed.add(codeSysStr);
				}

			}

			sb.append("\n");
		}

		return sb.toString();
	}

	private static String createValueSetsSection(List<CQLQualityDataSetDTO> valueSetList) {

		StringBuilder sb = new StringBuilder();

		List<String> valueSetAlreadyUsed = new ArrayList<>();

		if (valueSetList != null) {

			for (CQLQualityDataSetDTO valueset : valueSetList) {

				if(!valueSetAlreadyUsed.contains(valueset.getCodeListName())){

					String version = valueset.getVersion().replaceAll(" ", "%20");
					sb.append("valueset ").append('"').append(valueset.getCodeListName()).append('"');
					sb.append(": 'urn:oid:").append(valueset.getOid()).append("' ");
					//Check if QDM has expansion identifier or not.
					if(!version.equals("1.0") || StringUtils.isNotBlank(version)){
						sb.append("version 'urn:hl7:version:").append(version).append("' ");
					}
					sb.append("\n");
					valueSetAlreadyUsed.add(valueset.getCodeListName());
				}
			}

			sb.append("\n");
		}

		return sb.toString();
	}

	private static String createCodesSection(List<CQLCode> codeList) {

		StringBuilder sb = new StringBuilder();

		List<String> codesAlreadyUsed = new ArrayList<String>();

		if(codeList != null){

			for(CQLCode codes : codeList){

				String codesStr = '"' + codes.getDisplayName() + '"' + ": " + "'" + codes.getCodeOID() + "'";
				String codeSysStr = codes.getCodeSystemName();
				if(codes.isIsCodeSystemVersionIncluded()) {
					codeSysStr = codeSysStr + ":" + codes.getCodeSystemVersion().replaceAll(" ", "%20");	
				}

				if(!codesAlreadyUsed.contains(codesStr)){
					sb.append("code ").append(codesStr).append(" ").append("from ");
					sb.append('"').append(codeSysStr).append('"').append(" ");
					sb.append("display " +"'" +codes.getCodeName().replaceAll("'", "\\\\'")+"'");
					sb.append("\n");
					codesAlreadyUsed.add(codesStr);
				}
			}

			sb.append("\n");
		}

		return sb.toString();
	}

	private static StringBuilder createParameterSection(List<CQLParameter> paramList, StringBuilder cqlStr, String toBeInserted) {
		
		if (paramList != null) {

			for (CQLParameter parameter : paramList) {

				String param = "parameter " + "\"" + parameter.getParameterName() + "\"";

				if(StringUtils.isNotBlank(parameter.getCommentString())) {
					cqlStr.append("/*").append(parameter.getCommentString()).append("*/");
					cqlStr.append("\n");
				}
				
				cqlStr.append(param + " " + parameter.getParameterLogic());
				cqlStr.append("\n");

				// if the the param we just appended is the current one, then
				// find the size of the file at that time.
				// This will give us the end line of the parameter we are trying to insert.
				if(param.equalsIgnoreCase(toBeInserted)) {
					size = getEndLine(cqlStr.toString());
				}

			}

			cqlStr.append("\n");
		}

		return cqlStr;

	}

	private CQLUtilityClass() {
		throw new IllegalStateException("CQL Utility class");
	}
}
