package mat.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import mat.CQLFormatter;
import mat.CQLtoELM;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.service.CQLService;
import mat.client.shared.MatException;
import mat.client.shared.ValueSetNameInputValidator;
import mat.dao.clause.CQLLibraryAssociationDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.CQLValueSetTransferObject;
import mat.model.MatCodeTransferObject;
import mat.model.MatValueSet;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLCodeSystemWrapper;
import mat.model.cql.CQLCodeWrapper;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLDefinitionsWrapper;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLFunctionsWrapper;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLIncludeLibraryWrapper;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryAssociation;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLParametersWrapper;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.model.cql.validator.CQLIncludeLibraryValidator;
import mat.server.cqlparser.ReverseEngineerListener;
import mat.server.service.MeasurePackageService;
import mat.server.util.CQLLibraryWrapperMappingUtil;
import mat.server.util.CQLUtil;
import mat.server.util.CQLUtil.CQLArtifactHolder;
import mat.server.util.CQLValidationUtil;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;
import mat.shared.CQLError;
import mat.shared.CQLExpressionObject;
import mat.shared.CQLModelValidator;
import mat.shared.CQLObject;
import mat.shared.ConstantMessages;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.LibHolderObject;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.cql.error.InvalidLibraryException;

/**
 * The Class CQLServiceImpl.
 */
public class CQLServiceImpl implements CQLService {

	private static final Log logger = LogFactory.getLog(CQLServiceImpl.class);

	private static final int COMMENTS_MAX_LENGTH = 2500;

	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;
	@Autowired
	private CQLLibraryAssociationDAO cqlLibraryAssociationDAO;
	@Autowired
	private MeasurePackageService measurePackageService;

	/** The cql supplemental definition XML string. */
	private String cqlSupplementalDefinitionXMLString =

			"<supplementalDefinitions>"

					+ "<definition context=\"Patient\" name=\"SDE Ethnicity\" "
					+ " supplDataElement=\"true\" popDefinition=\"false\" id=\"999\"> "
					+ "<logic>[\"Patient Characteristic Ethnicity\": \"Ethnicity\"]</logic> " + "</definition>"

					+ "<definition context=\"Patient\" name=\"SDE Payer\" "
					+ " supplDataElement=\"true\" popDefinition=\"false\" id=\"999\">  "
					+ "<logic>[\"Patient Characteristic Payer\": \"Payer\"]</logic>" + "</definition>"

					+ "<definition context=\"Patient\" name=\"SDE Race\" "
					+ " supplDataElement=\"true\" popDefinition=\"false\"  id=\"999\"> "
					+ "<logic>[\"Patient Characteristic Race\": \"Race\"]</logic>" + "</definition>"

					+ "<definition context=\"Patient\" name=\"SDE Sex\" "
					+ " supplDataElement=\"true\"  popDefinition=\"false\" id=\"999\">  "
					+ "<logic>[\"Patient Characteristic Sex\": \"ONC Administrative Sex\"]</logic>" + "</definition>"

					+ "</supplementalDefinitions>";

	/** The cql default code system XML string. */
	private String cqlDefaultCodeSystemXMLString =

			"<codeSystems>"

					+ "<codeSystem codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" "
					+ "codeSystemVersion=\"2.46\" id=\"777\" " + "/>"

					+ "<codeSystem codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMEDCT\" "
					+ "codeSystemVersion=\"2016-03\" id=\"777\" " + "/>"

					+ "</codeSystems>";

	@Override
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String xml, String libraryName, String libraryComment) {

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);

		if (xml != null) {
			XmlProcessor processor = new XmlProcessor(xml);

			if (StringUtils.isNotBlank(libraryName)) {
				updateLibraryName(processor, libraryName);
			}

			updateLibraryComment(processor, libraryComment);

			result.setXml(processor.transform(processor.getOriginalDoc()));
			result.setSuccess(true);
			result.getCqlModel().setLibraryName(libraryName);
			result.getCqlModel().setLibraryComment(libraryComment);
		} else {
			result.setSuccess(false);
		}
		return result;
	}

	public SaveUpdateCQLResult saveCQLFile(String xml, String cql) {
		XmlProcessor processor = new XmlProcessor(xml);
		try {
			ReverseEngineerListener listener = new ReverseEngineerListener(cql);
			CQLModel reversedEngineeredCQLModel = listener.getCQLModel();			
			String reverseEngineeredCQLLookup = marshallCQLModel(reversedEngineeredCQLModel);
			processor.replaceNode(reverseEngineeredCQLLookup, "cqlLookUp", "measure");
			SaveUpdateCQLResult parsedResult = parseCQLLibraryForErrors(reversedEngineeredCQLModel);
						
			if(listener.hasSyntaxErrors()) {
				parsedResult.setXml(xml); // retain the old xml if there are syntax errors (essentially not saving)
				parsedResult.setCqlString(cql);
				parsedResult.setSuccess(false);
				parsedResult.setCqlErrors(listener.getSyntaxErrors());
				parsedResult.setFailureReason(SaveUpdateCQLResult.SYNTAX_ERRORS);				
				return parsedResult;
			}

			if (parsedResult.getCqlErrors().isEmpty()) {
				CQLFormatter formatter = new CQLFormatter();
				String formattedCQL = formatter.format(CQLUtilityClass.getCqlString(reversedEngineeredCQLModel, ""));
				CQLModel formattedReversedEngineeredCQLModel = reverseEngineerCQLModel(formattedCQL);
				String formattedCQLLookup = marshallCQLModel(formattedReversedEngineeredCQLModel);
				parsedResult.setXml(formattedCQLLookup);
				parsedResult.setCqlString(formattedCQL);
			} else {
				parsedResult.setXml(reverseEngineeredCQLLookup);
				parsedResult.setCqlString(cql);
			}
			parsedResult.setSuccess(true);				
			return parsedResult;

		} catch (IOException | MappingException | MarshalException | ValidationException e) {
			e.printStackTrace();
			return null;
		}

	}

	private String marshallCQLModel(CQLModel cqlModel)
			throws IOException, MappingException, MarshalException, ValidationException {
		return CQLUtilityClass.getXMLFromCQLModel(cqlModel);
	}

	private CQLModel reverseEngineerCQLModel(String cql) throws IOException {
		ReverseEngineerListener listener = new ReverseEngineerListener(cql);
		CQLModel reversedEngineeredCQLModel = listener.getCQLModel();	
		return reversedEngineeredCQLModel;
	}

	private void updateLibraryName(XmlProcessor processor, String libraryName) {

		String xPathForCQLLibraryName = "//cqlLookUp/library";
		try {
			Node libraryNode = processor.findNode(processor.getOriginalDoc(), xPathForCQLLibraryName);
			if (libraryNode != null) {
				libraryNode.setTextContent(libraryName);
			}
		} catch (XPathExpressionException e) {
			logger.error("updateLibraryName:" + e.getMessage());
		}

	}

	private void updateLibraryComment(XmlProcessor processor, String libraryComment) {
		libraryComment = StringUtils.left(libraryComment, COMMENTS_MAX_LENGTH);
		String xPathForCQLLibraryComment = "//cqlLookUp/libraryComment";
		try {
			Node commentsNode = processor.findNode(processor.getOriginalDoc(), xPathForCQLLibraryComment);
			if (commentsNode == null) {
				String xPathForCQLUsingModel = "//cqlLookUp/usingModel";
				Node usingModelNode = processor.findNode(processor.getOriginalDoc(), xPathForCQLUsingModel);

				Element commentsElem = processor.getOriginalDoc().createElement("libraryComment");
				commentsElem.setTextContent(libraryComment);

				Node cqlNode = processor.findNode(processor.getOriginalDoc(), "//cqlLookUp");
				cqlNode.insertBefore(commentsElem, usingModelNode);

			} else {
				commentsNode.setTextContent(libraryComment);
			}

		} catch (XPathExpressionException e) {
			logger.error("updateLibraryComment:" + e.getMessage());
		}
	}

	@Override
	public SaveUpdateCQLResult saveAndModifyFunctions(String xml, CQLFunctions functionWithOriginalContent, CQLFunctions functionWithEdits,
			List<CQLFunctions> functionsList, boolean isFormatable) {
		
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		result.setXml(xml); // if any failure, it will use this xml, which is the original

		try {

			if (StringUtils.isEmpty(xml)) {
				return result;
			}

			CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(xml);
			CQLModelValidator validator = new CQLModelValidator();

			if (functionWithOriginalContent != null) {
				functionWithEdits.setId(functionWithOriginalContent.getId());
			} else {
				functionWithEdits.setId(UUID.randomUUID().toString());
			}

			// validate the new of the identifier, unless it was equal to the function
			// that was being edited
			// (this flow should always happen for new functions since
			// functionWithOriginalContent will be null)
			if (functionWithOriginalContent == null
					|| !functionWithOriginalContent.getName().equalsIgnoreCase(functionWithEdits.getName())) {
				if (validator.hasSpecialCharacter(functionWithEdits.getName())) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NO_SPECIAL_CHAR);
					return result;
				}

				if (CQLValidationUtil.isDuplicateIdentifierName(functionWithEdits.getName(), cqlModel)) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NAME_NOT_UNIQUE);
					return result;
				}
			}
			
			boolean isValidArgumentName = true;
			if (functionWithEdits.getArgumentList() != null && functionWithEdits.getArgumentList().size() > 0) {
				for (CQLFunctionArgument argument : functionWithEdits.getArgumentList()) {
					isValidArgumentName = validator.doesAliasNameFollowCQLAliasNamingConvention(argument.getArgumentName());
					if (!isValidArgumentName) {
						break;
					}
				}
			}
			
			

			if (validator.isCommentTooLongOrContainsInvalidText(functionWithEdits.getCommentString())) {
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateCQLResult.COMMENT_INVALID);
				return result;
			}

			// validate that the context does not change if the expression is used
			if (functionWithOriginalContent != null && !functionWithOriginalContent.getContext().equalsIgnoreCase(functionWithEdits.getContext())) {
				String cqlExpressionName = "define function" + " \"" + functionWithOriginalContent.getName() + "\"";
				parseCQLExpressionForErrors(result, xml,cqlExpressionName, functionWithOriginalContent.getLogic(), 
						functionWithOriginalContent.getName(), "Function");
				if (result.getUsedCQLArtifacts().getUsedCQLFunctions().contains(functionWithOriginalContent.getName())) {
					functionWithEdits.setContext(functionWithOriginalContent.getContext());
				}
			}

			// update the the information the model
			cqlModel.getCqlFunctions().removeIf(f -> f.getId().equals(functionWithEdits.getId()));
			cqlModel.getCqlFunctions().add(functionWithEdits);


			String cqlExpressionName = "define function" + " \"" + functionWithEdits.getName() + "\"";
			parseCQLExpressionForErrors(result, CQLUtilityClass.getXMLFromCQLModel(cqlModel), cqlExpressionName,
					functionWithEdits.getLogic(), functionWithEdits.getName(), "Definition");

			// do some processing if the are no errors in the CQL
			if (result.getCqlErrors().isEmpty()) {
				Optional<CQLExpressionObject> expressionObject = findExpressionObject(functionWithEdits.getName(), result.getCqlObject().getCqlDefinitionObjectList());
				if (expressionObject.isPresent()) {
					functionWithEdits.setReturnType(expressionObject.get().getReturnType());
				}

				if (isFormatable) {
					functionWithEdits.setLogic(formatFunction(functionWithEdits));
				}
			}

			result.setXml(CQLUtilityClass.getXMLFromCQLModel(cqlModel));
			result.setFunction(functionWithEdits);
			result.getCqlModel().setCqlFunctions(sortFunctionssList(result.getCqlModel().getCqlFunctions()));
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}

	/**
	 * Builds a temporary function exprsesion string by taking the name, arguments,
	 * and logic, then formats the logic, and returns the formatted logic.
	 * 
	 * @param function the function to format
	 * @return the formatted function logic
	 * @throws IOException
	 */
	private String formatFunction(CQLFunctions function) throws IOException {
		// create argument string and then format the cql expression

		StringBuilder argumentBuilder = new StringBuilder();

		argumentBuilder.append("(");
		for (int i = 0; i < function.getArgumentList().size(); i++) {
			CQLFunctionArgument argument = function.getArgumentList().get(i);

			argumentBuilder.append(argument.getArgumentName() + " " + argument.getArgumentType() + ", ");
		}
		argumentBuilder.append(")");

		String definitionStatement = "define" + " \"" + function.getName() + argumentBuilder.toString() + "\":";
		String tempFunctionString = "define" + " \"" + function.getName() + argumentBuilder.toString() + "\":\n"
				+ function.getLogic();

		String functionLogic = "";
		if (function.getLogic() != null && !function.getLogic().isEmpty()) {
			CQLFormatter formatter = new CQLFormatter();
			String formattedFunction = formatter.format(tempFunctionString);
			functionLogic = parseOutBody(formattedFunction, definitionStatement, true, 2);
		}

		return functionLogic;

	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyParameters(String xml, CQLParameter parameterWithOriginalContent,
			CQLParameter parameterWithEdits, List<CQLParameter> parameterList, boolean isFormatable) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		result.setXml(xml); // if any failure, it will use this xml, which is the original

		try {

			if (StringUtils.isEmpty(xml)) {
				return result;
			}

			CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(xml);
			CQLModelValidator validator = new CQLModelValidator();

			if (parameterWithOriginalContent != null) {
				parameterWithEdits.setId(parameterWithOriginalContent.getId());
			} else {
				parameterWithEdits.setId(UUID.randomUUID().toString());
			}

			// validate the new of the identifier, unless it was equal to the parameter
			// that was being edited
			// (this flow should always happen for new parameter since
			// parameterWithOriginalContent will be null)
			if (parameterWithOriginalContent == null
					|| !parameterWithOriginalContent.getName().equalsIgnoreCase(parameterWithEdits.getName())) {
				if (validator.hasSpecialCharacter(parameterWithEdits.getName())) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NO_SPECIAL_CHAR);
					return result;
				}

				if (CQLValidationUtil.isDuplicateIdentifierName(parameterWithEdits.getName(), cqlModel)) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NAME_NOT_UNIQUE);
					return result;
				}
			}

			if (validator.isCommentTooLongOrContainsInvalidText(parameterWithEdits.getCommentString())) {
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateCQLResult.COMMENT_INVALID);
				return result;
			}

			// update the the information the model
			Optional<CQLParameter> parameterToBeEditedFromModel = cqlModel.getCqlParameters().stream().filter(p -> p.getId().equals(parameterWithEdits.getId())).findFirst();
			if (parameterToBeEditedFromModel.isPresent()) {
				parameterToBeEditedFromModel.get().setName(parameterWithEdits.getName());
				parameterToBeEditedFromModel.get().setCommentString(parameterWithEdits.getCommentString());
				parameterToBeEditedFromModel.get().setParameterLogic(parameterWithEdits.getParameterLogic());
			} else {
				cqlModel.getCqlParameters().add(parameterWithEdits);
			}

			String cqlExpressionName = "parameter" + " \"" + parameterWithEdits.getName() + "\"";
			parseCQLExpressionForErrors(result, CQLUtilityClass.getXMLFromCQLModel(cqlModel), cqlExpressionName,
					parameterWithEdits.getLogic(), parameterWithEdits.getName(), "Parameter");

			// do some processing if the are no errors in the CQL
			if (result.getCqlErrors().isEmpty()) {
				if (isFormatable) {
					parameterWithEdits.setLogic(formatParameter(parameterWithEdits));
				}
			}

			result.setXml(CQLUtilityClass.getXMLFromCQLModel(cqlModel));
			result.setParameter(parameterWithEdits);
			result.getCqlModel().setCqlParameters(sortParametersList(result.getCqlModel().getCqlParameters()));
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}

		return result;
	}

	/**
	 * Builds a temporary parameter expressions string, formats the parameter, and
	 * returns the formatted logic
	 * 
	 * @param parameterExpressionString the
	 * @return the formatted parameter logic
	 * @throws IOException
	 */
	private String formatParameter(CQLParameter parameter) throws IOException {

		// format the cql parameter
		String tempParameterString = "parameter" + " \"" + parameter.getName() + "\" " + parameter.getLogic();

		String parameterLogic = "";
		if (parameter.getLogic() != null && !parameter.getLogic().isEmpty()) {
			CQLFormatter formatter = new CQLFormatter();
			String formattedParameter = formatter.format(tempParameterString);
			parameterLogic = parseOutParameterBody(formattedParameter, parameter.getName());
		}

		return parameterLogic;
	}

	/**
	 * Parses the body from the parameter expression. Removes the parameter
	 * definition statement from the logic and formats nicely for ace editor.
	 * 
	 * @param cqlExpressionString the parameter expression string in the format of
	 *                            `parameter "ParamName" paramlogic`
	 * @param parameterName       the parameter name
	 * @return the parameter body
	 */
	private String parseOutParameterBody(String cqlExpressionString, String parameterName) {

		String parameterDefinitionStatement = "parameter \"" + parameterName + "\"";

		// remove the parameter definition statement
		String expressionBodyString = cqlExpressionString.replace(parameterDefinitionStatement, "");
		expressionBodyString = expressionBodyString.trim();

		return expressionBodyString;

	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyDefinitions(String xml, CQLDefinition definitionWithOriginalContent,
			CQLDefinition definitionWithEdits, List<CQLDefinition> definitionList, boolean isFormatable) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		result.setXml(xml); // if any failure, it will use this xml, which is the original

		try {

			if (StringUtils.isEmpty(xml) || (definitionWithOriginalContent != null && definitionWithOriginalContent.isSupplDataElement())) {
				return result;
			}

			CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(xml);
			CQLModelValidator validator = new CQLModelValidator();

			if (definitionWithOriginalContent != null) {
				definitionWithEdits.setId(definitionWithOriginalContent.getId());
			} else {
				definitionWithEdits.setId(UUID.randomUUID().toString());
			}

			// validate the new of the identifier, unless it was equal to the definition
			// that was being edited
			// (this flow should always happen for new definitions since
			// definitionWithOriginalContent will be null)
			if (definitionWithOriginalContent == null
					|| !definitionWithOriginalContent.getName().equalsIgnoreCase(definitionWithEdits.getName())) {
				if (validator.hasSpecialCharacter(definitionWithEdits.getName())) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NO_SPECIAL_CHAR);
					return result;
				}

				if (CQLValidationUtil.isDuplicateIdentifierName(definitionWithEdits.getName(), cqlModel)) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NAME_NOT_UNIQUE);
					return result;
				}
			}

			if (validator.isCommentTooLongOrContainsInvalidText(definitionWithEdits.getCommentString())) {
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateCQLResult.COMMENT_INVALID);
				return result;
			}

			// validate that the context does not change if the expression is used
			if (definitionWithOriginalContent != null && !definitionWithOriginalContent.getContext().equalsIgnoreCase(definitionWithEdits.getContext())) {
				parseCQLExpressionForErrors(result, xml,
						"define" + " \"" + definitionWithOriginalContent.getName() + "\"",
						definitionWithOriginalContent.getLogic(), definitionWithOriginalContent.getName(),
						"Definition");
				if (result.getUsedCQLArtifacts().getUsedCQLDefinitions().contains(definitionWithOriginalContent.getName())) {
					definitionWithEdits.setContext(definitionWithOriginalContent.getContext());
				}
			}

			// update the the information the model
			Optional<CQLDefinition> definitionToBeEditedFromModel = cqlModel.getDefinitionList().stream().filter(d -> d.getId().equals(definitionWithEdits.getId())).findFirst();
			if (definitionToBeEditedFromModel.isPresent()) {
				definitionToBeEditedFromModel.get().setName(definitionWithEdits.getName());
				definitionToBeEditedFromModel.get().setCommentString(definitionWithEdits.getCommentString());
				definitionToBeEditedFromModel.get().setContext(definitionWithEdits.getContext());
				definitionToBeEditedFromModel.get().setDefinitionLogic(definitionWithEdits.getDefinitionLogic());
			} else {
				cqlModel.getDefinitionList().add(definitionWithEdits);
			}

			String cqlExpressionName = "define" + " \"" + definitionWithEdits.getName() + "\"";
			parseCQLExpressionForErrors(result, CQLUtilityClass.getXMLFromCQLModel(cqlModel), cqlExpressionName,
					definitionWithEdits.getLogic(), definitionWithEdits.getName(), "Definition");

			// do some processing if the are no errors in the CQL
			if (result.getCqlErrors().isEmpty()) {
				Optional<CQLExpressionObject> expressionObject = findExpressionObject(definitionWithEdits.getName(), result.getCqlObject().getCqlDefinitionObjectList());
				if (expressionObject.isPresent()) {
					definitionWithEdits.setReturnType(expressionObject.get().getReturnType());
				}

				if (isFormatable) {
					definitionWithEdits.setLogic(formatDefinition(definitionWithEdits));
				}
			}

			result.setXml(CQLUtilityClass.getXMLFromCQLModel(cqlModel));
			result.setDefinition(definitionWithEdits);
			result.getCqlModel().setDefinitionList(sortDefinitionsList(result.getCqlModel().getDefinitionList()));
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}

	/**
	 * Creates a temporary string for the definition, formats it, and returns the
	 * formatted definition logic.
	 * 
	 * @param definition the definition to format
	 * @return the formatted definition logic
	 * @throws IOException
	 */
	private String formatDefinition(CQLDefinition definition) throws IOException {

		String definitionStatement = "define" + " \"" + definition.getName() + "\":";
		String tempDefinitionString = definitionStatement + "\n\t" + definition.getLogic();

		String definitionLogic = "";
		if (definition.getLogic() != null && !definition.getLogic().isEmpty()) {
			CQLFormatter formatter = new CQLFormatter();
			String formattedDefinition = formatter.format(tempDefinitionString);
			definitionLogic = parseOutBody(formattedDefinition, definitionStatement, true, 2);
		}

		return definitionLogic;
	}

	/**
	 * Parses the body from the cql definition or function. Remove the define
	 * statement for the expression and formats it nicely for ace editor by removing
	 * the first tab on each line.
	 * 
	 * @param cqlExpressionString        the cql expression string
	 * @param expressionDefinitionString the definition string in the format of
	 *                                   define "ExpressionName": logic or define
	 *                                   function "FunctionName"(arg1 Boolean, arg2
	 *                                   Boolean...): logic
	 * @return the body of the cql expression
	 */
	private String parseOutBody(String cqlExpressionString, String expressionDefinitionString, boolean isSpaces,
			int indentSize) {

		// remove the definition statement from the expressions string to make the
		// epxerssion body and then trim whitespace
		String expressionBodyString = cqlExpressionString.replace(expressionDefinitionString, "").trim();

		Scanner scanner = new Scanner(expressionBodyString);
		StringBuilder builder = new StringBuilder();

		// go through and rebuild the the format
		// this will remove the first whitespace in a line so
		// it properly displays in the ace editor.
		// without doing this, the the ace editor display
		// would be indented one too many
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			if (!line.isEmpty()) {
				line = line.replaceFirst(CQLUtilityClass.getWhiteSpaceString(isSpaces, indentSize), "");
			}

			builder.append(line + "\n");
		}

		scanner.close();
		return builder.toString();
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyIncludeLibrayInCQLLookUp(String xml, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> includedLibraryList) throws InvalidLibraryException {

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLIncludeLibraryWrapper wrapper = new CQLIncludeLibraryWrapper();
		if (xml != null && !xml.isEmpty()) {
			XmlProcessor processor = new XmlProcessor(xml);
			checkAndAppendIncludeLibraryParentNode(processor);
			if (toBeModifiedObj != null) { // this is a part of Modify
				result = modifyIncludedLibrary(toBeModifiedObj, currentObj, includedLibraryList, wrapper, processor);
			} else {
				result = createNewIncludedLibrary(xml, currentObj, includedLibraryList, wrapper);
			}
		}

		if (result.isSuccess()) {
			CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(result.getXml());
			result.setCqlModel(cqlModel);
			CQLUtil.getIncludedCQLExpressions(cqlModel, cqlLibraryDAO);
		}

		return result;
	}

	private SaveUpdateCQLResult createNewIncludedLibrary(String xml, CQLIncludeLibrary currentObj,
			List<CQLIncludeLibrary> includedLibraryList, CQLIncludeLibraryWrapper wrapper)
			throws InvalidLibraryException {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		XmlProcessor processor = new XmlProcessor(xml);
		CQLModel modelBeforeSave = CQLUtilityClass.getCQLModelFromXML(xml);
		currentObj.setId(UUID.randomUUID().toString());

		CQLIncludeLibraryValidator libraryValidator = new CQLIncludeLibraryValidator();
		libraryValidator.validate(currentObj, modelBeforeSave);

		if (!libraryValidator.isValid()) {
			throw new InvalidLibraryException(libraryValidator.getMessages());
		}

		try {
			String XPATH_EXPRESSION_INCLUDES = "//cqlLookUp/includeLibrarys";
			String cqlString = createIncludeLibraryXML(currentObj);

			processor.appendNode(cqlString, "includeLibrary", XPATH_EXPRESSION_INCLUDES);
			processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
			String finalUpdatedXml = processor.transform(processor.getOriginalDoc());
			result.setXml(finalUpdatedXml);
			result.setSuccess(true);
			result.setIncludeLibrary(currentObj);
			includedLibraryList.add(currentObj);
			wrapper.setCqlIncludeLibrary(includedLibraryList);
		} catch (Exception e) {
			logger.error("Failed to save CQL Included Library: " + e.getMessage());
		}

		return result;
	}

	private SaveUpdateCQLResult modifyIncludedLibrary(CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj,
			List<CQLIncludeLibrary> incLibraryList, CQLIncludeLibraryWrapper wrapper, XmlProcessor processor) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		currentObj.setId(toBeModifiedObj.getId());
		currentObj.setAliasName(toBeModifiedObj.getAliasName());
		String XPATH_EXPRESSION_INCLUDES = "//includeLibrary[@cqlLibRefId='" + toBeModifiedObj.getCqlLibraryId() + "']";
		try {
			Node nodeIncludes = processor.findNode(processor.getOriginalDoc(), XPATH_EXPRESSION_INCLUDES);
			String cqlLibraryXML = createIncludeLibraryXML(currentObj);
			String XPATH_EXPRESSION_INCLUDELIBRARYS = "//cqlLookUp/includeLibrarys";
			processor.removeFromParent(nodeIncludes);
			processor.appendNode(cqlLibraryXML, "includeLibrary", XPATH_EXPRESSION_INCLUDELIBRARYS);
			processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
			String finalUpdatedXml = processor.transform(processor.getOriginalDoc());
			result.setXml(finalUpdatedXml);
			result.setSuccess(true);
			result.setIncludeLibrary(currentObj);
			wrapper.setCqlIncludeLibrary(modifyIncludesList(toBeModifiedObj, currentObj, incLibraryList));
		} catch (XPathExpressionException | SAXException | IOException | MarshalException | ValidationException
				| MappingException | NullPointerException e) {
			logger.error("Failed to replace CQL included Library: " + e.getMessage());
		}

		return result;
	}

	/**
	 * Save CQL association.
	 *
	 * @param currentObj the current obj
	 * @param measureId  the measure id
	 */
	@Override
	public void saveCQLAssociation(CQLIncludeLibrary currentObj, String measureId) {
		CQLLibraryAssociation cqlLibraryAssociation = new CQLLibraryAssociation();
		cqlLibraryAssociation.setCqlLibraryId(currentObj.getCqlLibraryId());
		cqlLibraryAssociation.setAssociationId(measureId);
		cqlLibraryAssociationDAO.save(cqlLibraryAssociation);
	}

	@Override
	public int countNumberOfAssociation(String associatedWithId) {
		return cqlLibraryAssociationDAO.findAssociationCount(associatedWithId);
	}

	@Override
	public List<CQLLibraryAssociation> getAssociations(String associatedWithId) {
		return cqlLibraryAssociationDAO.getAssociations(associatedWithId);
	}

	/**
	 * Check and append include library parent node.
	 *
	 * @param processor the processor
	 */
	private void checkAndAppendIncludeLibraryParentNode(XmlProcessor processor) {

		try {
			Node cqlNode = processor.findNode(processor.getOriginalDoc(), "//cqlLookUp");
			if (cqlNode != null) {

				Node cqlIncludeLibNode = processor.findNode(processor.getOriginalDoc(), "//cqlLookUp/includeLibrarys");
				if (cqlIncludeLibNode == null) {
					Element includesChildElem = processor.getOriginalDoc().createElement("includeLibrarys");
					cqlNode.appendChild(includesChildElem);
				}

			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates the include library XML.
	 *
	 * @param includeLibrary the include library
	 * @return the string
	 * @throws MappingException
	 * @throws IOException
	 * @throws ValidationException
	 * @throws MarshalException
	 */
	@Override
	public String createIncludeLibraryXML(CQLIncludeLibrary includeLibrary)
			throws MarshalException, ValidationException, IOException, MappingException {
		logger.info("In CQLServiceImpl.createIncludeLibraryXML");
		CQLIncludeLibraryWrapper wrapper = new CQLIncludeLibraryWrapper();
		List<CQLIncludeLibrary> includeLibraryList = new ArrayList<>();
		includeLibraryList.add(includeLibrary);
		wrapper.setCqlIncludeLibrary(includeLibraryList);
		String includeLibraryXML = CQLLibraryWrapperMappingUtil.convertCQLIncludeLibraryWrapperToXML(wrapper);
		logger.info("Marshalling of CQLIncludeLibrary is successful..");
		logger.info("Exiting CQLServiceImpl.createIncludeLibraryXML()");
		return includeLibraryXML;
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public SaveUpdateCQLResult deleteDefinition(String xml, CQLDefinition toBeDeletedObj,
			List<CQLDefinition> definitionList) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLDefinitionsWrapper wrapper = new CQLDefinitionsWrapper();

		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);

		if (toBeDeletedObj.isSupplDataElement()) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
			return result;
		}

		GetUsedCQLArtifactsResult artifactsResult = getUsedCQlArtifacts(xml);
		if (artifactsResult.getCqlErrors().isEmpty()
				&& artifactsResult.getUsedCQLDefinitions().contains(toBeDeletedObj.getName())) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
		} else {
			XmlProcessor processor = new XmlProcessor(xml);

			if (xml != null && !xml.isEmpty()) {
				String XPATH_EXPRESSION_CQLLOOKUP_DEFINITION = "//cqlLookUp//definition[@id='" + toBeDeletedObj.getId()
						+ "']";
				try {
					Node definitionNode = processor.findNode(processor.getOriginalDoc(),
							XPATH_EXPRESSION_CQLLOOKUP_DEFINITION);

					if (definitionNode != null) {
						// remove from xml
						definitionNode.getParentNode().removeChild(definitionNode);
						processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
						result.setXml(processor.getOriginalXml());

						// remove from definition list
						definitionList.remove(toBeDeletedObj);
						wrapper.setCqlDefinitions(definitionList);
						result.setSuccess(true);
						result.setDefinition(toBeDeletedObj);
					}

					else {
						result.setSuccess(false);
						result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
					}

				} catch (XPathExpressionException e) {
					result.setSuccess(false);
					e.printStackTrace();
				}

			}
		}
		if (result.isSuccess() && (wrapper.getCqlDefinitions().size() > 0)) {
			result.getCqlModel().setDefinitionList(sortDefinitionsList(wrapper.getCqlDefinitions()));
		}

		return result;
	}

	@Override
	public SaveUpdateCQLResult deleteValueSet(String xml, String toBeDelValueSetId) {
		logger.info("Start deleteValueSet : ");
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		XmlProcessor xmlProcessor = new XmlProcessor(xml);
		try {
			String xpathforValueSet = "//cqlLookUp//valueset[@id='" + toBeDelValueSetId + "']";
			Node valueSetElements = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xpathforValueSet);

			if (valueSetElements != null) {
				String valueSetName = valueSetElements.getAttributes().getNamedItem("name").getNodeValue();
				CQLQualityDataSetDTO valueSet = new CQLQualityDataSetDTO();
				valueSet.setName(valueSetName);
				Node parentNode = valueSetElements.getParentNode();
				parentNode.removeChild(valueSetElements);
				result.setSuccess(true);
				result.setXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
				result.setCqlQualityDataSetDTO(valueSet);

			} else {
				logger.info("Unable to find the selected valueset element with id in deleteValueSet : "
						+ toBeDelValueSetId);
				result.setSuccess(false);
			}
		} catch (XPathExpressionException e) {
			result.setSuccess(false);
			logger.info("Error in method deleteValueSet: " + e.getMessage());
		}

		logger.info("END deleteValueSet : ");
		return result;

	}

	@Override
	public SaveUpdateCQLResult deleteCode(String xml, String toBeDeletedCodeId) {
		logger.info("Start deleteCode : ");
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		XmlProcessor xmlProcessor = new XmlProcessor(xml);
		try {
			String xpathforCodeNode = "//cqlLookUp//code[@id='" + toBeDeletedCodeId + "']";
			Node codeNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xpathforCodeNode);

			if (codeNode != null) {
				String cqlOID = codeNode.getAttributes().getNamedItem("codeOID").getNodeValue();
				String cqlCodeName = codeNode.getAttributes().getNamedItem("codeName").getNodeValue();
				String codeSystemOID = codeNode.getAttributes().getNamedItem("codeSystemOID").getNodeValue();
				String codeSystemVersion = codeNode.getAttributes().getNamedItem("codeSystemVersion").getNodeValue();
				if (!isCodeSystemUsedByAnotherCode(codeSystemOID, codeSystemVersion, toBeDeletedCodeId, xmlProcessor)) {
					deleteCodeSystem(xmlProcessor, codeSystemOID, codeSystemVersion);
				}

				CQLCode cqlCode = new CQLCode();
				cqlCode.setName(cqlCodeName);
				cqlCode.setCodeOID(cqlOID);

				Node parentNode = codeNode.getParentNode();
				parentNode.removeChild(codeNode);
				result.setSuccess(true);
				result.setXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
				result.setCqlCodeList(getCQLCodes(result.getXml()).getCqlCodeList());
				result.setCqlCode(cqlCode);
			} else {
				logger.info("Unable to find the selected Code element with id in deleteCode : " + toBeDeletedCodeId);
				result.setSuccess(false);
			}
		} catch (XPathExpressionException e) {
			result.setSuccess(false);
			logger.info("Error in method deleteCode: " + e.getMessage());
		} catch (MatException e) {
			result.setSuccess(false);
			logger.info("Error in method deleteCode: " + e.getMessage());
		}

		logger.info("END deleteCode : ");

		return result;
	}

	private void deleteCodeSystem(XmlProcessor xmlProcessor, String codeSystemOID, String codeSystemVersion)
			throws XPathExpressionException, MatException {
		String xpathforCodeSystemNode = "//cqlLookUp//codeSystem[@codeSystem='" + codeSystemOID
				+ "' and @codeSystemVersion='" + codeSystemVersion + "']";
		Node codeSystemNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xpathforCodeSystemNode);
		if (codeSystemNode != null) {
			Node codeSystemParentNode = codeSystemNode.getParentNode();
			codeSystemParentNode.removeChild(codeSystemNode);
		} else {
			throw new MatException("Unable to find the selected CodeSystem element with id:" + codeSystemOID
					+ " and version: " + codeSystemVersion);
		}
	}

	private boolean isCodeSystemUsedByAnotherCode(String codeSystemOID, String codeSystemVersion,
			String toBeDeletedCodeId, XmlProcessor xmlProcessor) throws XPathExpressionException {
		boolean isCodeSystemUsed = false;
		String xpathforCodeNodes = "//cqlLookUp//code[@id!='" + toBeDeletedCodeId + "']";
		NodeList nodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xpathforCodeNodes);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node curCodeNode = nodeList.item(i);
			if (curCodeNode != null) {
				String curCodeSystemOID = curCodeNode.getAttributes().getNamedItem("codeSystemOID").getNodeValue();
				String curCodeSystemVersion = curCodeNode.getAttributes().getNamedItem("codeSystemVersion")
						.getNodeValue();
				if (codeSystemOID.equalsIgnoreCase(curCodeSystemOID)
						&& curCodeSystemVersion.equals(codeSystemVersion)) {
					isCodeSystemUsed = true;
					break;
				}
			}
		}

		return isCodeSystemUsed;
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public SaveUpdateCQLResult deleteFunctions(String xml, CQLFunctions toBeDeletedObj,
			List<CQLFunctions> functionsList) {

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLFunctionsWrapper wrapper = new CQLFunctionsWrapper();

		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);

		GetUsedCQLArtifactsResult artifactsResult = getUsedCQlArtifacts(xml);
		if (artifactsResult.getCqlErrors().isEmpty()
				&& artifactsResult.getUsedCQLFunctions().contains(toBeDeletedObj.getName())) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
		} else {

			XmlProcessor processor = new XmlProcessor(xml);

			if (xml != null && !xml.isEmpty()) {
				String XPATH_EXPRESSION_CQLLOOKUP_FUNCTION = "//cqlLookUp//function[@id='" + toBeDeletedObj.getId()
						+ "']";
				try {
					Node functionNode = processor.findNode(processor.getOriginalDoc(),
							XPATH_EXPRESSION_CQLLOOKUP_FUNCTION);

					if (functionNode != null) {

						// remove from xml
						functionNode.getParentNode().removeChild(functionNode);
						processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
						result.setXml(processor.getOriginalXml());

						// remove from function list
						functionsList.remove(toBeDeletedObj);
						wrapper.setCqlFunctionsList(functionsList);
						result.setSuccess(true);
						result.setFunction(toBeDeletedObj);
					}

					else {
						result.setSuccess(false);
						result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
					}

				} catch (XPathExpressionException e) {
					result.setSuccess(false);
					e.printStackTrace();
				}
			}
		}
		if (result.isSuccess() && (wrapper.getCqlFunctionsList().size() > 0)) {
			result.getCqlModel().setCqlFunctions(sortFunctionssList(wrapper.getCqlFunctionsList()));
		}

		return result;
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public SaveUpdateCQLResult deleteParameter(String xml, CQLParameter toBeDeletedObj,
			List<CQLParameter> parameterList) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLParametersWrapper wrapper = new CQLParametersWrapper();

		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);

		if (toBeDeletedObj.isReadOnly()) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
			return result;
		}

		GetUsedCQLArtifactsResult artifactsResult = getUsedCQlArtifacts(xml);
		if (artifactsResult.getCqlErrors().isEmpty()
				&& artifactsResult.getUsedCQLParameters().contains(toBeDeletedObj.getName())) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
		} else {
			XmlProcessor processor = new XmlProcessor(xml);

			if (xml != null) {
				String XPATH_EXPRESSION_CQLLOOKUP_PARAMETER = "//cqlLookUp//parameter[@id='" + toBeDeletedObj.getId()
						+ "']";
				try {
					Node parameterNode = processor.findNode(processor.getOriginalDoc(),
							XPATH_EXPRESSION_CQLLOOKUP_PARAMETER);

					if (parameterNode != null) {

						// remove from xml
						parameterNode.getParentNode().removeChild(parameterNode);
						processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
						result.setXml(processor.getOriginalXml());

						// remove from parameter list
						parameterList.remove(toBeDeletedObj);
						wrapper.setCqlParameterList(parameterList);
						result.setSuccess(true);
						result.setParameter(toBeDeletedObj);
					}

					else {
						result.setSuccess(false);
						result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
					}

				} catch (XPathExpressionException e) {
					result.setSuccess(false);
					e.printStackTrace();
				}

			}
		}
		if (result.isSuccess() && (wrapper.getCqlParameterList().size() > 0)) {
			result.getCqlModel().setCqlParameters(sortParametersList(wrapper.getCqlParameterList()));
		}

		return result;
	}

	/**
	 * Update risk adjustment variables.
	 *
	 * @param processor       the processor
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj      the current obj
	 */




	/*
	 * {@inheritDoc}
	 */
	@Override
	public SaveUpdateCQLResult getCQLData(String xmlString) {
		CQLModel cqlModel = new CQLModel();
		cqlModel = CQLUtilityClass.getCQLModelFromXML(xmlString);

		SaveUpdateCQLResult parsedCQL = parseCQLLibraryForErrors(cqlModel);

		if (parsedCQL.getCqlErrors().isEmpty()) {
			parsedCQL.setUsedCQLArtifacts(getUsedCQlArtifacts(xmlString));
			setUsedValuesets(parsedCQL, cqlModel);
			setUsedCodes(parsedCQL, cqlModel);
			boolean isValid = CQLUtil.validateDatatypeCombinations(cqlModel,
					parsedCQL.getUsedCQLArtifacts().getValueSetDataTypeMap(),
					parsedCQL.getUsedCQLArtifacts().getCodeDataTypeMap());
			parsedCQL.setDatatypeUsedCorrectly(isValid);

		}

		parsedCQL.setCqlModel(cqlModel);

		return parsedCQL;
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public SaveUpdateCQLResult getCQLDataForLoad(String xmlString) {
		CQLModel cqlModel = new CQLModel();

		cqlModel = CQLUtilityClass.getCQLModelFromXML(xmlString);

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		Map<String, LibHolderObject> cqlLibNameMap = new HashMap<>();
		Map<CQLIncludeLibrary, CQLModel> cqlIncludeModelMap = new HashMap<>();
		CQLUtil.getCQLIncludeMaps(cqlModel, cqlLibNameMap, cqlIncludeModelMap, cqlLibraryDAO);
		cqlModel.setIncludedCQLLibXMLMap(cqlLibNameMap);
		cqlModel.setIncludedLibrarys(cqlIncludeModelMap);
		CQLUtil.setIncludedCQLExpressions(cqlModel);
		result.setCqlModel(cqlModel);

		return result;
	}

	@Override
	public SaveUpdateCQLResult getCQLLibraryData(String xmlString) {
		CQLModel cqlModel = new CQLModel();
		cqlModel = CQLUtilityClass.getCQLModelFromXML(xmlString);

		HashMap<String, LibHolderObject> cqlLibNameMap = new HashMap<>();
		Map<CQLIncludeLibrary, CQLModel> cqlIncludeModelMap = new HashMap<>();
		String parentLibraryName = cqlModel.getLibraryName();
		CQLUtil.getCQLIncludeMaps(cqlModel, cqlLibNameMap, cqlIncludeModelMap, getCqlLibraryDAO());
		cqlModel.setIncludedCQLLibXMLMap(cqlLibNameMap);
		cqlModel.setIncludedLibrarys(cqlIncludeModelMap);

		// get the strings for parsing
		String parentCQLString = CQLUtilityClass.getCqlString(cqlModel, "").toString();
		List<String> expressionList = cqlModel.getExpressionListFromCqlModel();
		SaveUpdateCQLResult result = CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, expressionList);

		Iterator<CQLIncludeLibrary> libraryIter = cqlModel.getIncludedLibrarys().keySet().iterator();
		while (libraryIter.hasNext()) {
			CQLIncludeLibrary curLibrary = libraryIter.next();
			if (!cqlModel.getQdmVersion().equals(curLibrary.getQdmVersion())) {
				result.setQDMVersionMatching(false);
			}
		}

		setUsedValuesets(result, cqlModel);
		setUsedCodes(result, cqlModel);
		boolean isValid = CQLUtil.validateDatatypeCombinations(cqlModel,
				result.getUsedCQLArtifacts().getValueSetDataTypeMap(),
				result.getUsedCQLArtifacts().getCodeDataTypeMap());
		result.setDatatypeUsedCorrectly(isValid);

		// if there is no cql errors
		if (result.getCqlErrors().isEmpty()) {
			try {
				CQLFormatter formatter = new CQLFormatter();
				result.setCqlString(formatter.format(parentCQLString));
			} catch (IOException e) {
				result.setCqlString(parentCQLString);
			}
		} else {
			result.setCqlString(parentCQLString);
		}

		result.setCqlString(parentCQLString);
		result.setLibraryName(parentLibraryName);

		return result;
	}

	private void setReturnTypes(SaveUpdateCQLResult result, CQLModel cqlModel) {
		CQLObject cqlObject = result.getCqlObject();
		if (cqlObject == null || cqlModel == null) {
			return;

		}
		if (cqlModel.getDefinitionList() != null) {
			for (CQLDefinition definition : cqlModel.getDefinitionList()) {
				Optional<CQLExpressionObject> obj = findExpressionObject(definition.getName(),
						cqlObject.getCqlDefinitionObjectList());
				if (obj.isPresent()) {
					result.getUsedCQLArtifacts().getExpressionReturnTypeMap().put(obj.get().getName(),
							obj.get().getReturnType());
				}

			}

		}
		if (cqlModel.getCqlFunctions() != null) {
			for (CQLFunctions functions : cqlModel.getCqlFunctions()) {
				Optional<CQLExpressionObject> obj = findExpressionObject(functions.getName(),
						cqlObject.getCqlFunctionObjectList());
				if (obj.isPresent()) {
					result.getUsedCQLArtifacts().getExpressionReturnTypeMap().put(obj.get().getName(),
							obj.get().getReturnType());
				}

			}
		}
	}

	private void setUsedCodes(SaveUpdateCQLResult parsedCQL, CQLModel cqlModel) {

		List<String> usedCodes = parsedCQL.getUsedCQLArtifacts().getUsedCQLcodes();
		for (CQLCode cqlCode : cqlModel.getCodeList()) {
			boolean isUsed = usedCodes.contains(cqlCode.getDisplayName());
			cqlCode.setUsed(isUsed);
		}
	}

	private void setUsedValuesets(SaveUpdateCQLResult parsedCQL, CQLModel cqlModel) {

		List<String> usedValuesets = parsedCQL.getUsedCQLArtifacts().getUsedCQLValueSets();

		for (CQLQualityDataSetDTO valueset : cqlModel.getAllValueSetAndCodeList()) {
			boolean isUsed = usedValuesets.contains(valueset.getName());
			valueset.setUsed(isUsed);
		}
	}

	@Override
	public SaveUpdateCQLResult parseCQLLibraryForErrors(CQLModel cqlModel) {
		return CQLUtil.parseCQLLibraryForErrors(cqlModel, getCqlLibraryDAO(), null);
	}

	/**
	 * Gets the CQL definitions from measure xml.
	 *
	 * @param measureId the measure id
	 * @return the CQL definitions from measure xml
	 */
	public CQLDefinitionsWrapper getCQLDefinitionsFromMeasureXML(String measureId) {

		MeasureXmlModel measureXmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
		CQLDefinitionsWrapper wrapper = null;
		if (measureXmlModel != null) {
			wrapper = convertXmltoCQLDefinitionModel(measureXmlModel);
			if ((wrapper.getCqlDefinitions() != null) && (wrapper.getCqlDefinitions().size() > 0)) {
				sortDefinitionsList(wrapper.getCqlDefinitions());
			}
		}
		return wrapper;
	}

	/**
	 * Convert xmlto cql definition model.
	 *
	 * @param xmlModel the xml model
	 * @return the CQL definitions wrapper
	 */
	private CQLDefinitionsWrapper convertXmltoCQLDefinitionModel(final MeasureXmlModel xmlModel) {
		logger.info("In CQLServiceImpl.convertXmltoCQLDefinitionModel()");
		CQLDefinitionsWrapper details = null;
		String xml = null;
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			xml = new XmlProcessor(xmlModel.getXml()).getXmlByTagName("cqlLookUp");
		}
		try {
			if (xml == null) {// TODO: This Check should be replaced when the
				// DataConversion is complete.
				logger.info("xml is null or xml doesn't contain cqlLookUp tag");

			} else {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLDefinitionModelMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(CQLDefinitionsWrapper.class);
				unmar.setWhitespacePreserve(true);
				details = (CQLDefinitionsWrapper) unmar.unmarshal(new InputSource(new StringReader(xml)));

			}

		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLDefinitionModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		return details;
	}

	/**
	 * Creates the Parameters xml.
	 *
	 * @param parameter the CQLParameter
	 * @return the string
	 */
	@Override
	public String createParametersXML(CQLParameter parameter) {

		logger.info("In CQLServiceImpl.createParametersXML");
		Mapping mapping = new Mapping();
		CQLParametersWrapper wrapper = new CQLParametersWrapper();
		List<CQLParameter> paramList = new ArrayList<>();

		paramList.add(parameter);
		wrapper.setCqlParameterList(paramList);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLParameterModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(wrapper);
			logger.info("Marshalling of CQLParameter is successful..");
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLParameterModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				logger.info("Other Exception" + e.getMessage());
				e.printStackTrace();
			}
		}
		logger.info("Exiting CQLServiceImpl.createParametersXML()");
		return stream.toString();

	}

	/**
	 * Creates the definitions xml.
	 *
	 * @param definition the definition
	 * @return the string
	 */
	@Override
	public String createDefinitionsXML(CQLDefinition definition) {

		logger.info("In CQLServiceImpl.createDefinitionsXML");
		Mapping mapping = new Mapping();
		CQLDefinitionsWrapper wrapper = new CQLDefinitionsWrapper();
		List<CQLDefinition> definitionList = new ArrayList<>();
		definitionList.add(definition);
		wrapper.setCqlDefinitions(definitionList);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLDefinitionModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(wrapper);
			logger.info("Marshalling of CQLDefinition is successful..");
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLDefinitionModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				e.printStackTrace();
			}
		}
		logger.info("Exiting CQLServiceImpl.createDefinitionsXML()");
		return stream.toString();
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public SaveUpdateCQLResult getCQLFileData(String xmlString) {

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		result = getCQLData(xmlString);
		String cqlString = getCqlString(result.getCqlModel());
		if (cqlString != null) {
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}
		result.setCqlString(cqlString);
		return result;
	}

	/**
	 * Gets the cql string.
	 *
	 * @param cqlModel - CQLModel
	 * @return the cql string
	 */
	@Override
	public String getCqlString(CQLModel cqlModel) {

		return CQLUtilityClass.getCqlString(cqlModel, "");
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public CQLKeywords getCQLKeyWords() {
		return CQLKeywordsUtil.getCQLKeywords();
	}

	private List<CQLIncludeLibrary> modifyIncludesList(CQLIncludeLibrary toBeModified, CQLIncludeLibrary currentObj,
			List<CQLIncludeLibrary> incLibraryList) {
		Iterator<CQLIncludeLibrary> iterator = incLibraryList.iterator();
		while (iterator.hasNext()) {
			CQLIncludeLibrary cqlParam = iterator.next();
			if (cqlParam.getId().equals(toBeModified.getId())) {

				iterator.remove();
				break;
			}
		}
		incLibraryList.add(currentObj);
		return incLibraryList;
	}

	/**
	 * Sort definitions list.
	 *
	 * @param defineList the define list
	 * @return the list
	 */
	private List<CQLDefinition> sortDefinitionsList(List<CQLDefinition> defineList) {

		Collections.sort(defineList, new Comparator<CQLDefinition>() {
			@Override
			public int compare(final CQLDefinition o1, final CQLDefinition o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		return defineList;
	}

	/**
	 * Sort parameters list.
	 *
	 * @param paramList the param list
	 * @return the list
	 */
	private List<CQLParameter> sortParametersList(List<CQLParameter> paramList) {

		Collections.sort(paramList, new Comparator<CQLParameter>() {
			@Override
			public int compare(final CQLParameter o1, final CQLParameter o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		return paramList;
	}

	/**
	 * Sort Functions list.
	 *
	 * @param funcList the Function list
	 * @return the list
	 */
	private List<CQLFunctions> sortFunctionssList(List<CQLFunctions> funcList) {

		Collections.sort(funcList, new Comparator<CQLFunctions>() {
			@Override
			public int compare(final CQLFunctions o1, final CQLFunctions o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		return funcList;
	}

	/**
	 * Sort include lib list.
	 *
	 * @param IncLibList the inc lib list
	 * @return the list
	 */
	private List<CQLIncludeLibrary> sortIncludeLibList(List<CQLIncludeLibrary> IncLibList) {

		Collections.sort(IncLibList, new Comparator<CQLIncludeLibrary>() {
			@Override
			public int compare(final CQLIncludeLibrary o1, final CQLIncludeLibrary o2) {
				return o1.getAliasName().compareToIgnoreCase(o2.getAliasName());
			}
		});

		return IncLibList;
	}

	@Override
	public String getJSONObjectFromXML() {
		String result = null;
		try {
			JSONObject jsonObject = XML.toJSONObject(convertXmlToString());
			result = jsonObject.toString(4);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Convert xml to string.
	 *
	 * @return the string
	 */
	private String convertXmlToString() {
		String fileName = "CQLTimingExpressions.xml";
		URL templateFileUrl = new ResourceLoader().getResourceAsURL(fileName);
		File xmlFile = null;
		FileReader fr;
		String line = "";
		StringBuilder sb = new StringBuilder();
		try {
			try {
				xmlFile = new File(templateFileUrl.toURI());
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
			fr = new FileReader(xmlFile);
			BufferedReader br = new BufferedReader(fr);

			try {
				while ((line = br.readLine()) != null) {
					sb.append(line.trim());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public String getSupplementalDefinitions() {
		return cqlSupplementalDefinitionXMLString;
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultCodeSystems() {
		return cqlDefaultCodeSystemXMLString;
	}

	private SaveUpdateCQLResult parseCQLExpressionForErrors(SaveUpdateCQLResult result, String xml,
			String cqlExpressionName, String logic, String expressionName, String expressionType) {

		CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(xml);

		String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, cqlExpressionName);

		cqlModel.setLines(countLines(cqlFileString));

		String wholeDef = "";
		int size = 0;
		int startLine = 0;
		int endLine = CQLUtilityClass.getSize();

		if (expressionType.equalsIgnoreCase("parameter")) {
			endLine = endLine + 1; // for parameters, the size is actually 1 more than reported.
			wholeDef = cqlExpressionName + " " + logic;
			size = countLines(wholeDef);

			if (size > 1) {
				startLine = endLine - size;
			} else {
				startLine = endLine;
			}
		}

		else {
			wholeDef = cqlExpressionName + " :\n" + logic;
			size = countLines(wholeDef);
			startLine = endLine - size + 1; // the start line is really 1 more than endLine - size because
											// the definition logic starts on the next line.
		}

		result.setStartLine(startLine);
		result.setEndLine(endLine);

		List<String> expressionList = cqlModel.getExpressionListFromCqlModel();
		SaveUpdateCQLResult parsedCQL = CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, expressionList);

		String formattedName = cqlModel.getFormattedName();
		List<CQLError> libraryErrors = parsedCQL.getLibraryNameErrorsMap().get(formattedName);
		List<CQLError> expressionErrors = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(libraryErrors)) {
			result.setValidCQLWhileSavingExpression(false);
			buildExpressionExceptionList(startLine, endLine, libraryErrors, expressionErrors);
		}

		List<CQLError> libraryWarnings = parsedCQL.getLibraryNameWarningsMap().get(formattedName);
		List<CQLError> expressionWarnings = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(libraryWarnings)) {
			buildExpressionExceptionList(startLine, endLine, libraryWarnings, expressionWarnings);
		}

		if (expressionErrors.isEmpty()) {
			result.setCqlObject(parsedCQL.getCqlObject());
			result.setDatatypeUsedCorrectly(findValidDataTypeUsage(cqlModel, expressionName, expressionType, parsedCQL));
			if (result.isDatatypeUsedCorrectly()) {
				XmlProcessor xmlProcessor = new XmlProcessor(xml);
				CQLArtifactHolder cqlArtifactHolder = CQLUtil.getCQLArtifactsReferredByPoplns(xmlProcessor.getOriginalDoc());
				parsedCQL.getUsedCQLArtifacts().getUsedCQLDefinitions().addAll(cqlArtifactHolder.getCqlDefFromPopSet());
				parsedCQL.getUsedCQLArtifacts().getUsedCQLFunctions().addAll(cqlArtifactHolder.getCqlFuncFromPopSet());
			}
		}

		result.setCqlModel(cqlModel);
		result.setCqlErrors(expressionErrors);
		result.setCqlWarnings(expressionWarnings);
		result.setUsedCQLArtifacts(parsedCQL.getUsedCQLArtifacts());
		return result;
	}

	private List<CQLError> buildExpressionExceptionList(int startLine, int endLine, List<CQLError> cqlErrors,
			List<CQLError> expressionErrors) {
		for (CQLError cqlError : cqlErrors) {
			int errorStartLine = cqlError.getStartErrorInLine();
			if ((errorStartLine >= startLine && errorStartLine <= endLine)) {
				if (cqlError.getStartErrorInLine() == startLine) {
					cqlError.setStartErrorInLine(cqlError.getStartErrorInLine() - startLine);
				} else {
					cqlError.setStartErrorInLine(cqlError.getStartErrorInLine() - startLine - 1);
				}

				if (cqlError.getEndErrorInLine() == startLine) {
					cqlError.setEndErrorInLine(cqlError.getEndErrorInLine() - startLine);
				} else {
					cqlError.setEndErrorInLine(cqlError.getEndErrorInLine() - startLine - 1);
				}

				cqlError.setErrorMessage(cqlError.getErrorMessage());
				expressionErrors.add(cqlError);
			}
		}
		return expressionErrors;
	}

	private boolean findValidDataTypeUsage(CQLModel model, String expressionName, String expressionType,
			SaveUpdateCQLResult parsedCQL) {
		List<CQLExpressionObject> expressionList = null;
		switch (expressionType) {
		case "Function":
			expressionList = parsedCQL.getCqlObject().getCqlFunctionObjectList();
			break;
		case "Definition":
			expressionList = parsedCQL.getCqlObject().getCqlDefinitionObjectList();
			break;
		case "Parameter":
			expressionList = parsedCQL.getCqlObject().getCqlParameterObjectList();
			break;

		}

		Optional<CQLExpressionObject> cqlExpressionObject = findExpressionObject(expressionName, expressionList);
		boolean isValid = true;
		if (cqlExpressionObject.isPresent()) {
			isValid = CQLUtil.validateDatatypeCombinations(model, cqlExpressionObject.get().getValueSetDataTypeMap(),
					cqlExpressionObject.get().getCodeDataTypeMap());
		}
		return isValid;
	}

	private Optional<CQLExpressionObject> findExpressionObject(String expressionName,
			List<CQLExpressionObject> expressionList) {
		return expressionList.stream().filter(e -> e.getName().equalsIgnoreCase(expressionName)).findFirst();
	}

	/**
	 * Count lines.
	 *
	 * @param str the str
	 * @return the int
	 */
	public static int countLines(String str) {
		if (str == null || str.isEmpty()) {
			return 0;
		}
		int lines = 1;
		int pos = 0;
		while ((pos = str.indexOf("\n", pos) + 1) != 0) {
			lines = lines + 1;
		}
		return lines;
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public GetUsedCQLArtifactsResult getUsedCQlArtifacts(String xml) {
		CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(xml);

		List<String> exprList = new ArrayList<>();

		for (CQLDefinition cqlDefinition : cqlModel.getDefinitionList()) {
			logger.info("name:" + cqlDefinition.getName());
			exprList.add(cqlDefinition.getName());
		}

		for (CQLFunctions cqlFunction : cqlModel.getCqlFunctions()) {
			logger.info("name:" + cqlFunction.getName());
			exprList.add(cqlFunction.getName());
		}

		for (CQLParameter cqlParameter : cqlModel.getCqlParameters()) {
			logger.info("name:" + cqlParameter.getName());
			exprList.add(cqlParameter.getName());
		}

		SaveUpdateCQLResult cqlResult = CQLUtil.parseCQLLibraryForErrors(cqlModel, getCqlLibraryDAO(), exprList);
		String formattedName = cqlModel.getFormattedName();
		// if there are no errors in the cql library, get the used cql artifacts
		if (CollectionUtils.isEmpty(cqlResult.getCqlErrors())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xml);
			CQLArtifactHolder cqlArtifactHolder = CQLUtil
					.getCQLArtifactsReferredByPoplns(xmlProcessor.getOriginalDoc());
			cqlResult.getUsedCQLArtifacts().getUsedCQLDefinitions().addAll(cqlArtifactHolder.getCqlDefFromPopSet());
			cqlResult.getUsedCQLArtifacts().getUsedCQLFunctions().addAll(cqlArtifactHolder.getCqlFuncFromPopSet());
			setReturnTypes(cqlResult, cqlModel);

		} else {
			cqlResult.getUsedCQLArtifacts().setLibraryNameErrorsMap(cqlResult.getLibraryNameErrorsMap());
			cqlResult.getUsedCQLArtifacts().setLibraryNameWarningsMap(cqlResult.getLibraryNameWarningsMap());
			cqlResult.getUsedCQLArtifacts().setCqlErrors(cqlResult.getCqlErrors());
			cqlResult.getUsedCQLArtifacts().setLibraryNameErrorsMap(cqlResult.getLibraryNameErrorsMap());
			cqlResult.getUsedCQLArtifacts().setCqlErrorsPerExpression(
					getCQLErrorsPerExpressions(cqlModel, cqlResult.getLibraryNameErrorsMap().get(formattedName)));
		}

		if (CollectionUtils.isNotEmpty(cqlResult.getCqlWarnings())) {
			cqlResult.getUsedCQLArtifacts().setLibraryNameWarningsMap(cqlResult.getLibraryNameWarningsMap());
			cqlResult.getUsedCQLArtifacts().setCqlWarningsPerExpression(
					getCQLErrorsPerExpressions(cqlModel, cqlResult.getLibraryNameWarningsMap().get(formattedName)));
		}

		return cqlResult.getUsedCQLArtifacts();
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public SaveUpdateCQLResult parseCQLStringForError(String cqlFileString) {
		return null;
	}

	/**
	 * Save QD sto measure.
	 *
	 * @param valueSetTransferObject the value set transfer object
	 * @return the save update code list result
	 */
	@Override
	public final SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		ArrayList<CQLQualityDataSetDTO> qdsList = new ArrayList<>();
		wrapper.setQualityDataDTO(qdsList);
		valueSetTransferObject.scrubForMarkUp();

		if (!valueSetTransferObject.validateModel()) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
			return result;
		}

		CQLQualityDataSetDTO qds = new CQLQualityDataSetDTO();
		MatValueSet matValueSet = valueSetTransferObject.getMatValueSet();
		qds.setOid(matValueSet.getID());
		qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		qds.setName(valueSetTransferObject.getCqlQualityDataSetDTO().getName());
		qds.setSuffix(valueSetTransferObject.getCqlQualityDataSetDTO().getSuffix());
		qds.setOriginalCodeListName(valueSetTransferObject.getCqlQualityDataSetDTO().getOriginalCodeListName());
		qds.setRelease(valueSetTransferObject.getCqlQualityDataSetDTO().getRelease());
		qds.setProgram(valueSetTransferObject.getCqlQualityDataSetDTO().getProgram());
		qds.setVersion("");
		qds.setValueSetType(matValueSet.getType());
		if (matValueSet.isGrouping()) {
			qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
		} else {
			qds.setTaxonomy(matValueSet.getCodeSystemName());
		}
		qds.setUuid(UUID.randomUUID().toString());

		ArrayList<CQLQualityDataSetDTO> qualityDataSetDTOs = (ArrayList<CQLQualityDataSetDTO>) valueSetTransferObject
				.getAppliedQDMList();

		// Treat as regular QDM
		if (!isDuplicate(valueSetTransferObject, true)) {
			wrapper.getQualityDataDTO().add(qds);
			String xmlString = generateXmlForAppliedValueset(wrapper);
			result.setSuccess(true);
			qualityDataSetDTOs.add(qds);
			result.setCqlAppliedQDMList(sortQualityDataSetList(qualityDataSetDTOs));
			result.setXml(xmlString);
		} else {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
		}

		return result;
	}

	/**
	 * Sort quality data set list.
	 *
	 * @param finalList the final list
	 * @return the list
	 */
	private List<CQLQualityDataSetDTO> sortQualityDataSetList(final List<CQLQualityDataSetDTO> finalList) {

		Collections.sort(finalList, new Comparator<CQLQualityDataSetDTO>() {
			@Override
			public int compare(final CQLQualityDataSetDTO o1, final CQLQualityDataSetDTO o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		return finalList;

	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public SaveUpdateCQLResult saveCQLUserDefinedValueset(CQLValueSetTransferObject matValueSetTransferObject) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		matValueSetTransferObject.scrubForMarkUp();

		if (!matValueSetTransferObject.validateModel()) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
			return result;
		}

		ValueSetNameInputValidator validator = new ValueSetNameInputValidator();
		String errorMessage = validator.validate(matValueSetTransferObject);
		if (errorMessage.isEmpty()) {
			ArrayList<CQLQualityDataSetDTO> qdsList = new ArrayList<>();

			wrapper.setQualityDataDTO(qdsList);
			CQLQualityDataSetDTO qds = new CQLQualityDataSetDTO();
			qds.setOid(ConstantMessages.USER_DEFINED_QDM_OID);
			qds.setId(UUID.randomUUID().toString());
			qds.setName(matValueSetTransferObject.getCqlQualityDataSetDTO().getName());
			qds.setSuffix(matValueSetTransferObject.getCqlQualityDataSetDTO().getSuffix());
			qds.setOriginalCodeListName(matValueSetTransferObject.getCqlQualityDataSetDTO().getOriginalCodeListName());
			qds.setTaxonomy(ConstantMessages.USER_DEFINED_QDM_NAME);
			qds.setValueSetType(StringUtils.EMPTY);
			qds.setUuid(UUID.randomUUID().toString());
			qds.setVersion("1.0");
			qds.setRelease("");
			qds.setProgram("");
			wrapper.getQualityDataDTO().add(qds);

			String qdmXMLString = generateXmlForAppliedValueset(wrapper);
			result.setSuccess(true);
			result.setCqlAppliedQDMList(sortQualityDataSetList(wrapper.getQualityDataDTO()));
			result.setXml(qdmXMLString);

		} else {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
		}
		return result;
	}

	@Override
	public SaveUpdateCQLResult saveCQLCodes(String xml, MatCodeTransferObject codeTransferObject) {
		logger.info("::: CQLServiceImpl saveCQLCodes Start :::");
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		codeTransferObject.scrubForMarkUp();
		if (codeTransferObject.isValidModel()) {

			XmlProcessor xmlProcessor = new XmlProcessor(xml);
			CQLCode appliedCode = codeTransferObject.getCqlCode();
			appliedCode.setId(UUID.randomUUID().toString().replaceAll("-", ""));

			try {
				Node existingCodeList = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
						"//cqlLookUp/codes/code[@displayName=\"" + appliedCode.getDisplayName() + "\" ]");
				if (existingCodeList != null) {
					logger.info("::: Duplicate Code :::");
					result.setSuccess(false);
					result.setFailureReason(result.getDuplicateCode());
				} else {
					CQLCodeWrapper wrapper = new CQLCodeWrapper();
					ArrayList<CQLCode> codeList = new ArrayList<>();
					wrapper.setCqlCodeList(codeList);
					wrapper.getCqlCodeList().add(codeTransferObject.getCqlCode());
					String codeXMLString = generateXmlForAppliedCode(wrapper);
					result.setSuccess(true);
					result.setXml(codeXMLString);
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		logger.info("::: CQLServiceImpl saveCQLCodes End :::");
		return result;
	}

	@Override
	public SaveUpdateCQLResult saveCQLCodeSystem(String xml, CQLCodeSystem codeSystem) {
		logger.info("::: CQLServiceImpl saveCQLCodeSystem Start :::");
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		XmlProcessor xmlProcessor = new XmlProcessor(xml);

		codeSystem.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			Node existingCodeSystemList = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
					"//cqlLookUp/codeSystems/codeSystem[@codeSystemName='" + codeSystem.getCodeSystemName()
							+ "' and @codeSystemVersion='" + codeSystem.getCodeSystemVersion() + "' ]");

			if (existingCodeSystemList != null) {
				logger.info("::: CodeSystem Already added :::");
				result.setSuccess(false);
			} else {
				CQLCodeSystemWrapper wrapper = new CQLCodeSystemWrapper();
				ArrayList<CQLCodeSystem> codeSystemList = new ArrayList<>();
				wrapper.setCqlCodeSystemList(codeSystemList);
				wrapper.getCqlCodeSystemList().add(codeSystem);
				String codeSystemXMLString = generateXmlForAppliedCodeSystem(wrapper);
				result.setSuccess(true);
				result.setXml(codeSystemXMLString);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		logger.info("::: CQLServiceImpl saveCQLCodeSystem End :::");
		return result;
	}

	/**
	 * Checks if is duplicate.
	 *
	 * @param matValueSetTransferObject the mat value set transfer object
	 * @param isVSACValueSet            the is VSAC value set
	 * @return true, if is duplicate
	 */
	private boolean isDuplicate(CQLValueSetTransferObject matValueSetTransferObject, boolean isVSACValueSet) {
		logger.info(" checkForDuplicates Method Call Start.");
		boolean isQDSExist = false;

		String qdmCompareName = matValueSetTransferObject.getCqlQualityDataSetDTO().getName();

		List<CQLQualityDataSetDTO> existingQDSList = matValueSetTransferObject.getAppliedQDMList();
		for (CQLQualityDataSetDTO dataSetDTO : existingQDSList) {

			String codeListName = dataSetDTO.getName();

			if (codeListName.equalsIgnoreCase(qdmCompareName)) {
				isQDSExist = true;
				break;
			}
		}
		logger.info("checkForDuplicates Method Call End.Check resulted in :" + (isQDSExist));
		return isQDSExist;
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public final SaveUpdateCQLResult modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		matValueSetTransferObject.scrubForMarkUp();
		if (!matValueSetTransferObject.validateModel()) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
			return result;
		}
		if (matValueSetTransferObject.getMatValueSet() != null) {
			result = modifyVSACValueSetInCQLLookUp(matValueSetTransferObject);
		} else if (matValueSetTransferObject.getCodeListSearchDTO() != null) {
			result = modifyUserDefineValuesetInCQLLookUp(matValueSetTransferObject);
		}
		return result;
	}

	/**
	 * Update VSAC value set in element look up.
	 *
	 * @param matValueSetTransferObject the mat value set transfer object
	 * @return the save update code list result
	 */
	private SaveUpdateCQLResult modifyVSACValueSetInCQLLookUp(CQLValueSetTransferObject matValueSetTransferObject) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLQualityDataSetDTO oldQdm = new CQLQualityDataSetDTO();
		populatedOldQDM(oldQdm, matValueSetTransferObject.getCqlQualityDataSetDTO());
		// Treat as regular QDM
		List<CQLQualityDataSetDTO> origAppliedQDMList = matValueSetTransferObject.getAppliedQDMList();
		List<CQLQualityDataSetDTO> tempAppliedQDMList = new ArrayList<>();
		tempAppliedQDMList.addAll(matValueSetTransferObject.getAppliedQDMList());
		// Removing the QDS that is being modified from the
		// tempAppliedQDMList.
		Iterator<CQLQualityDataSetDTO> iterator = tempAppliedQDMList.iterator();
		while (iterator.hasNext()) {
			CQLQualityDataSetDTO qualityDataSetDTO = iterator.next();
			if (qualityDataSetDTO.getUuid().equals(matValueSetTransferObject.getCqlQualityDataSetDTO().getUuid())) {
				iterator.remove();
				break;
			}
		}
		matValueSetTransferObject.setAppliedQDMList(tempAppliedQDMList);

		if (!isDuplicate(matValueSetTransferObject, true)) {
			matValueSetTransferObject.setAppliedQDMList(origAppliedQDMList);
			CQLQualityDataSetDTO qds = matValueSetTransferObject.getCqlQualityDataSetDTO();
			MatValueSet matValueSet = matValueSetTransferObject.getMatValueSet();
			qds.setOid(matValueSet.getID());
			qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			qds.setName(matValueSet.getDisplayName());
			qds.setValueSetType(matValueSet.getType());
			if (matValueSet.isGrouping()) {
				qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
			} else {
				qds.setTaxonomy(matValueSet.getCodeSystemName());
			}

			CQLQualityDataModelWrapper wrapper = modifyAppliedElementList(qds,
					(ArrayList<CQLQualityDataSetDTO>) matValueSetTransferObject.getAppliedQDMList());

			result.setSuccess(true);
			result.setCqlAppliedQDMList(sortQualityDataSetList(wrapper.getQualityDataDTO()));
			result.setCqlQualityDataSetDTO(qds);
		} else {
			matValueSetTransferObject.setAppliedQDMList(origAppliedQDMList);
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
		}
		return result;
	}

	@Override
	public SaveUpdateCQLResult updateCQLLookUpTag(String xml, CQLQualityDataSetDTO modifyWithDTO,
			final CQLQualityDataSetDTO modifyDTO) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		XmlProcessor processor = new XmlProcessor(xml);

		// XPath Expression to find all elementRefs in elementLookUp for to
		// be modified QDM.
		String XPATH_EXPRESSION_VALUESETS = "//cqlLookUp/valuesets/valueset[@uuid='" + modifyDTO.getUuid() + "']";
		try {
			NodeList nodesValuesets = processor.findNodeList(processor.getOriginalDoc(), XPATH_EXPRESSION_VALUESETS);
			for (int i = 0; i < nodesValuesets.getLength(); i++) {
				Node newNode = nodesValuesets.item(i);
				newNode.getAttributes().getNamedItem("originalName")
						.setNodeValue(modifyWithDTO.getOriginalCodeListName());
				newNode.getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO.getOriginalCodeListName());
				newNode.getAttributes().getNamedItem("id").setNodeValue(modifyWithDTO.getId());
				if ((newNode.getAttributes().getNamedItem("codeSystemName") == null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					Attr attrNode = processor.getOriginalDoc().createAttribute("codeSystemName");
					attrNode.setNodeValue(modifyWithDTO.getCodeSystemName());
					newNode.getAttributes().setNamedItem(attrNode);
				} else if ((newNode.getAttributes().getNamedItem("codeSystemName") != null)
						&& (modifyWithDTO.getCodeSystemName() == null)) {
					newNode.getAttributes().getNamedItem("codeSystemName").setNodeValue(null);
				} else if ((newNode.getAttributes().getNamedItem("codeSystemName") != null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					newNode.getAttributes().getNamedItem("codeSystemName")
							.setNodeValue(modifyWithDTO.getCodeSystemName());
				}
				newNode.getAttributes().getNamedItem("oid").setNodeValue(modifyWithDTO.getOid());
				newNode.getAttributes().getNamedItem("taxonomy").setNodeValue(modifyWithDTO.getTaxonomy());
				newNode.getAttributes().getNamedItem("version").setNodeValue("");
				if (newNode.getAttributes().getNamedItem("release") != null) {
					newNode.getAttributes().getNamedItem("release").setNodeValue(modifyWithDTO.getRelease());
					newNode.getAttributes().getNamedItem("program").setNodeValue(modifyWithDTO.getProgram());
				}

				if (modifyWithDTO.isSuppDataElement()) {
					newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("true");
				} else {
					newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("false");
				}
				if ((newNode.getAttributes().getNamedItem("suffix") == null)) {
					if (modifyDTO.getSuffix() != null && !modifyDTO.getSuffix().isEmpty()) {
						Attr attrNode = processor.getOriginalDoc().createAttribute("suffix");
						attrNode.setNodeValue(modifyWithDTO.getSuffix());
						newNode.getAttributes().setNamedItem(attrNode);
						newNode.getAttributes().getNamedItem("name").setNodeValue(
								modifyWithDTO.getOriginalCodeListName() + " (" + modifyWithDTO.getSuffix() + ")");
					}

				} else {
					if (modifyDTO.getSuffix() != null && !modifyDTO.getSuffix().isEmpty()) {
						newNode.getAttributes().getNamedItem("suffix").setNodeValue(modifyDTO.getSuffix());
						newNode.getAttributes().getNamedItem("name").setNodeValue(
								modifyWithDTO.getOriginalCodeListName() + " (" + modifyWithDTO.getSuffix() + ")");
					} else {
						if (newNode.getAttributes().getNamedItem("suffix") != null && modifyDTO.getSuffix() == null) {
							newNode.getAttributes().removeNamedItem("suffix");
						}
					}
				}

				if (newNode.getAttributes().getNamedItem("type") == null) {
					Attr attrNode = processor.getOriginalDoc().createAttribute("type");
					attrNode.setNodeValue(modifyWithDTO.getValueSetType());
					newNode.getAttributes().setNamedItem(attrNode);
				} else {
					newNode.getAttributes().getNamedItem("type").setNodeValue(modifyDTO.getValueSetType());
				}
			}
			result.setSuccess(true);
			result.setXml(processor.transform(processor.getOriginalDoc()));
		} catch (XPathExpressionException e) {
			result.setSuccess(false);
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Update user define QDM in element look up.
	 *
	 * @param matValueSetTransferObject the mat value set transfer object
	 * @return the save update code list result
	 */
	private SaveUpdateCQLResult modifyUserDefineValuesetInCQLLookUp(
			CQLValueSetTransferObject matValueSetTransferObject) {
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator();
		List<String> messageList = new ArrayList<>();
		validator.validate(matValueSetTransferObject);
		if (messageList.isEmpty()) {
			if (!isDuplicate(matValueSetTransferObject, false)) {
				ArrayList<CQLQualityDataSetDTO> qdsList = new ArrayList<>();
				wrapper.setQualityDataDTO(qdsList);
				CQLQualityDataSetDTO qds = matValueSetTransferObject.getCqlQualityDataSetDTO();
				qds.setOid(ConstantMessages.USER_DEFINED_QDM_OID);
				qds.setId(UUID.randomUUID().toString());
				qds.setName(matValueSetTransferObject.getCodeListSearchDTO().getName());
				qds.setTaxonomy(ConstantMessages.USER_DEFINED_QDM_NAME);
				qds.setValueSetType(StringUtils.EMPTY);
				qds.setVersion("1.0");
				wrapper = modifyAppliedElementList(qds,
						(ArrayList<CQLQualityDataSetDTO>) matValueSetTransferObject.getAppliedQDMList());
				String qdmXMLString = generateXmlForAppliedValueset(wrapper);
				result.setSuccess(true);
				result.setCqlAppliedQDMList(sortQualityDataSetList(wrapper.getQualityDataDTO()));
				result.setXml(qdmXMLString);
				result.setCqlQualityDataSetDTO(qds);
			} else {
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
		}
		return result;
	}

	/**
	 * Adds the new applied QDM in measure XML.
	 *
	 * @param qualityDataSetDTOWrapper the quality data set DTO wrapper
	 * @return the string
	 */
	private String generateXmlForAppliedValueset(final CQLQualityDataModelWrapper qualityDataSetDTOWrapper) {
		logger.info("addNewAppliedQDMInMeasureXML Method Call Start.");
		ByteArrayOutputStream stream = createNewXML(qualityDataSetDTOWrapper);
		int startIndex = stream.toString().indexOf("<valueset ", 0);
		int lastIndex = stream.toString().indexOf("/>", startIndex);
		String xmlString = stream.toString().substring(startIndex, lastIndex + 2);
		logger.debug("addNewAppliedQDMInMeasureXML Method Call xmlString :: " + xmlString);
		return xmlString;
	}

	private String generateXmlForAppliedCode(CQLCodeWrapper wrapper) {
		logger.info("generateXmlForAppliedCode Method Call Start.");
		ByteArrayOutputStream stream = createNewCodeXML(wrapper);
		int startIndex = stream.toString().indexOf("<code ", 0);
		int lastIndex = stream.toString().indexOf("/>", startIndex);
		String xmlString = stream.toString().substring(startIndex, lastIndex + 2);
		logger.debug("generateXmlForAppliedCode Method Call xmlString :: " + xmlString);
		return xmlString;
	}

	private String generateXmlForAppliedCodeSystem(CQLCodeSystemWrapper wrapper) {
		logger.info("generateXmlForAppliedCodeSystem Method Call Start.");
		ByteArrayOutputStream stream = createNewCodeSystemXML(wrapper);
		int startIndex = stream.toString().indexOf("<codeSystem ", 0);
		int lastIndex = stream.toString().indexOf("/>", startIndex);
		String xmlString = stream.toString().substring(startIndex, lastIndex + 2);
		logger.debug("generateXmlForAppliedCodeSystem Method Call xmlString :: " + xmlString);
		return xmlString;
	}

	/**
	 * Creates the new XML.
	 *
	 * @param qualityDataSetDTO the quality data set DTO
	 * @return the byte array output stream
	 */
	private ByteArrayOutputStream createNewXML(final CQLQualityDataModelWrapper qualityDataSetDTO) {
		logger.info("In CQLServiceImpl.createXml()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("ValueSetsMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(qualityDataSetDTO);
			logger.debug("Marshalling of QualityDataSetDTO is successful.." + stream.toString());
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load ValueSetsMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		logger.info("Exiting CQLServiceImpl.createXml()");
		return stream;
	}

	private ByteArrayOutputStream createNewCodeXML(final CQLCodeWrapper wrapper) {
		logger.info("In CQLServiceImpl.createXml()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CodeMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(wrapper);
			logger.debug("Marshalling of CQLCode is successful.." + stream.toString());
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		logger.info("Exiting CQLServiceImpl.createNewCodeXml()");
		return stream;
	}

	private ByteArrayOutputStream createNewCodeSystemXML(final CQLCodeSystemWrapper wrapper) {
		logger.info("In CQLServiceImpl.createNewCodeSystemXML()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CodeSystemsMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(wrapper);
			logger.debug("Marshalling of CQLCode is successful.." + stream.toString());
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		logger.info("Exiting CQLServiceImpl.createNewCodeSystemXML()");
		return stream;
	}

	/**
	 * Modify applied element list.
	 *
	 * @param dataSetDTO the data set DTO
	 * @param appliedQDM the applied QDM
	 * @return the CQL quality data model wrapper
	 */
	private CQLQualityDataModelWrapper modifyAppliedElementList(final CQLQualityDataSetDTO dataSetDTO,
			final ArrayList<CQLQualityDataSetDTO> appliedQDM) {
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		Iterator<CQLQualityDataSetDTO> iterator = appliedQDM.iterator();
		while (iterator.hasNext()) {
			CQLQualityDataSetDTO qualityDataSetDTO = iterator.next();
			if (qualityDataSetDTO.getUuid().equals(dataSetDTO.getUuid())) {
				iterator.remove();
				break;
			}
		}
		appliedQDM.add(dataSetDTO);
		wrapper.setQualityDataDTO(appliedQDM);
		return wrapper;
	}

	/**
	 * Populated old QDM.
	 *
	 * @param oldQdm            the old qdm
	 * @param qualityDataSetDTO the quality data set DTO
	 */
	private void populatedOldQDM(CQLQualityDataSetDTO oldQdm, CQLQualityDataSetDTO qualityDataSetDTO) {
		oldQdm.setName(qualityDataSetDTO.getName());
		oldQdm.setSuffix(qualityDataSetDTO.getSuffix());
		oldQdm.setOriginalCodeListName(qualityDataSetDTO.getOriginalCodeListName());
		oldQdm.setOid(qualityDataSetDTO.getOid());
		oldQdm.setUuid(qualityDataSetDTO.getUuid());
		oldQdm.setVersion(qualityDataSetDTO.getVersion());
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public CQLQualityDataModelWrapper getCQLValusets(String measureId,
			CQLQualityDataModelWrapper cqlQualityDataModelWrapper) {
		MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureId);
		String xmlString = model.getXml();
		List<CQLQualityDataSetDTO> cqlQualityDataSetDTOs = CQLUtilityClass
				.sortCQLQualityDataSetDto(getCQLData(xmlString).getCqlModel().getAllValueSetAndCodeList());
		cqlQualityDataModelWrapper.setQualityDataDTO(cqlQualityDataSetDTOs);

		return cqlQualityDataModelWrapper;
	}

	@Override
	public CQLCodeWrapper getCQLCodes(String xmlString) {
		CQLCodeWrapper cqlCodeWrapper = new CQLCodeWrapper();
		if (xmlString != null && !xmlString.isEmpty()) {
			SaveUpdateCQLResult parsedResult = getCQLData(xmlString);
			CQLModel cqlModel = parsedResult.getCqlModel();
			List<CQLCode> allCodes = cqlModel.getCodeList();
			if (parsedResult.getCqlErrors().isEmpty()) {
				GetUsedCQLArtifactsResult artifactsResult = parsedResult.getUsedCQLArtifacts();
				List<String> usedCodes = artifactsResult.getUsedCQLcodes();
				for (CQLCode code : allCodes) {
					if (usedCodes.contains(code.getDisplayName())) {
						code.setUsed(true);
					}
				}
			} else {
				for (CQLCode code : allCodes) {
					code.setUsed(false);
				}
			}
			List<CQLCode> cqlCodeDtos = CQLUtilityClass.sortCQLCodeDTO(allCodes);
			cqlCodeWrapper.setCqlCodeList(cqlCodeDtos);
		}

		return cqlCodeWrapper;
	}

	public CQLLibraryDAO getCqlLibraryDAO() {
		return cqlLibraryDAO;
	}

	public void setCqlLibraryDAO(CQLLibraryDAO cqlLibraryDAO) {
		this.cqlLibraryDAO = cqlLibraryDAO;
	}

	@Override
	public SaveUpdateCQLResult deleteInclude(String xml, CQLIncludeLibrary toBeModifiedIncludeObj,
			List<CQLIncludeLibrary> viewIncludeLibrarys) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLIncludeLibraryWrapper wrapper = new CQLIncludeLibraryWrapper();
		XmlProcessor processor = new XmlProcessor(xml);

		if (xml != null) {
			String XPATH_EXPRESSION_CQLLOOKUP_INCLUDE = "//cqlLookUp//includeLibrary[@id='"
					+ toBeModifiedIncludeObj.getId() + "']";
			logger.info("XPATH: " + XPATH_EXPRESSION_CQLLOOKUP_INCLUDE);
			try {
				Node includeNode = processor.findNode(processor.getOriginalDoc(), XPATH_EXPRESSION_CQLLOOKUP_INCLUDE);

				if (includeNode != null) {
					logger.info("FOUND NODE");

					// remove from xml
					Node deletedNode = includeNode.getParentNode().removeChild(includeNode);
					logger.debug(deletedNode.getAttributes().getNamedItem("name").toString());
					processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
					result.setXml(processor.getOriginalXml());

					// remove from library list
					viewIncludeLibrarys.remove(toBeModifiedIncludeObj);
					wrapper.setCqlIncludeLibrary(viewIncludeLibrarys);
					result.setSuccess(true);
					result.setIncludeLibrary(toBeModifiedIncludeObj);
				}

				else {
					logger.info("NOT FOUND NODE");
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
				}

			} catch (XPathExpressionException e) {
				result.setSuccess(false);
				logger.error("deleteInclude" + e.getMessage());
			}
		}

		if (result.isSuccess()) {
			CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(result.getXml());
			result.setCqlModel(cqlModel);
			CQLUtil.getIncludedCQLExpressions(cqlModel, cqlLibraryDAO);
			logger.info(result.getXml());
			logger.info(result.isSuccess());
		}

		return result;
	}

	/**
	 * Delete CQL Association.
	 *
	 * @param currentObj       the current obj
	 * @param associatedWithId the measure id
	 */
	@Override
	public void deleteCQLAssociation(CQLIncludeLibrary currentObj, String associatedWithId) {
		CQLLibraryAssociation cqlLibraryAssociation = new CQLLibraryAssociation();
		cqlLibraryAssociation.setCqlLibraryId(currentObj.getCqlLibraryId());
		cqlLibraryAssociation.setAssociationId(associatedWithId);
		cqlLibraryAssociationDAO.deleteAssociation(cqlLibraryAssociation);
	}

	/**
	 * Find CQL Parsing Errors per CQL Expression.
	 * 
	 * @param cqlModel
	 * @param parsedCQL
	 * @return Map.
	 */
	private Map<String, List<CQLError>> getCQLErrorsPerExpressions(CQLModel cqlModel, List<CQLError> cqlErrors) {

		Map<String, List<CQLError>> expressionMapWithError = new HashMap<>();
		List<CQLExpressionObject> cqlExpressionObjects = getCQLExpressionObjectListFromCQLModel(cqlModel);

		for (CQLExpressionObject expressionObject : cqlExpressionObjects) {
			int fileStartLine = -1;
			int fileEndLine = -1;
			int size = 0;
			String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, expressionObject.getName());

			String wholeDef = "";
			String expressionToFind = null;

			switch (expressionObject.getType()) {
			case "Parameter":
				expressionToFind = "parameter \"" + expressionObject.getName() + "\"";
				fileStartLine = findStartLineForCQLExpressionInCQLFile(cqlFileString, expressionToFind);
				wholeDef = expressionObject.getName() + " " + expressionObject.getLogic();
				size = countLines(wholeDef);
				fileEndLine = fileStartLine + size - 1;
				break;
			case "Definition":
				expressionToFind = "define \"" + expressionObject.getName() + "\"";
				wholeDef = expressionObject.getName() + " :\n" + expressionObject.getLogic();
				fileStartLine = findStartLineForCQLExpressionInCQLFile(cqlFileString, expressionToFind);
				size = countLines(wholeDef);
				fileEndLine = fileStartLine + size - 1;
				break;
			case "Function":
				expressionToFind = "define function \"" + expressionObject.getName() + "\"";
				wholeDef = expressionObject.getName() + " :\n" + expressionObject.getLogic();
				fileStartLine = findStartLineForCQLExpressionInCQLFile(cqlFileString, expressionToFind);
				size = countLines(wholeDef);
				fileEndLine = fileStartLine + size - 1;
				break;

			default:
				break;
			}

			logger.debug("fileStartLine of expression ===== " + fileStartLine);
			logger.debug("fileEndLine of expression ===== " + fileEndLine);

			List<CQLError> errors = new ArrayList<>();
			if (cqlErrors != null) {
				for (CQLError cqlError : cqlErrors) {
					int errorStartLine = cqlError.getStartErrorInLine();

					if (errorStartLine >= fileStartLine && errorStartLine <= fileEndLine) {
						cqlError.setStartErrorInLine(errorStartLine - fileStartLine - 1);
						cqlError.setEndErrorInLine(cqlError.getEndErrorInLine() - fileStartLine - 1);
						cqlError.setErrorMessage(StringUtils.trimToEmpty(cqlError.getErrorMessage()));
						if (cqlError.getStartErrorInLine() == -1) {
							cqlError.setStartErrorInLine(0);
						}
						errors.add(cqlError);
					}
				}
				expressionMapWithError.put(expressionObject.getName(), errors);

			}
		}

		return expressionMapWithError;
	}

	/**
	 * This method finds the start line of each expression in CQL File.
	 * 
	 * @param fileStartLine
	 * @param cqlFileString
	 * @param expressionToFind
	 * @return integer value.
	 */
	private int findStartLineForCQLExpressionInCQLFile(String cqlFileString, String expressionToFind) {
		int fileStartLine = -1;
		try (LineNumberReader rdr = new LineNumberReader(new StringReader(cqlFileString))) {
			String line = null;
			logger.debug("Expression to Find :: " + expressionToFind);
			while ((line = rdr.readLine()) != null) {
				if (line.indexOf(expressionToFind) >= 0) {
					fileStartLine = rdr.getLineNumber();
					break;
				}
			}
		} catch (IOException e) {
			logger.error("findStartLineForCQLExpressionInCQLFile" + e.getMessage());
		}
		return fileStartLine;
	}

	/**
	 * This method iterates through CQLModel object and generates
	 * CQLExpressionObject with type, Name and Logic.
	 * 
	 * @param cqlModel
	 * @return List<CQLExpressionObject>.
	 */
	private List<CQLExpressionObject> getCQLExpressionObjectListFromCQLModel(CQLModel cqlModel) {

		List<CQLExpressionObject> cqlExpressionObjects = new ArrayList<>();

		for (CQLParameter cqlParameter : cqlModel.getCqlParameters()) {
			CQLExpressionObject cqlExpressionObject = new CQLExpressionObject("Parameter", cqlParameter.getName(),
					cqlParameter.getLogic());
			cqlExpressionObjects.add(cqlExpressionObject);
		}
		for (CQLDefinition cqlDefinition : cqlModel.getDefinitionList()) {
			CQLExpressionObject cqlExpressionObject = new CQLExpressionObject("Definition", cqlDefinition.getName(),
					cqlDefinition.getLogic());
			cqlExpressionObjects.add(cqlExpressionObject);
		}

		for (CQLFunctions cqlFunction : cqlModel.getCqlFunctions()) {
			CQLExpressionObject cqlExpressionObject = new CQLExpressionObject("Function", cqlFunction.getName(),
					cqlFunction.getLogic());
			cqlExpressionObjects.add(cqlExpressionObject);
		}

		return cqlExpressionObjects;
	}

	@Override
	public CQLModel parseCQL(String cqlBuilder) {
		CQLModel cqlModel = new CQLModel();
		return cqlModel;
	}
}