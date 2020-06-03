package mat.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
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
import java.util.UUID;
import java.util.stream.Collectors;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mat.CQLFormatter;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.service.CQLService;
import mat.client.shared.MatException;
import mat.cql.CqlParser;
import mat.cql.CqlToMatXml;
import mat.cql.CqlVisitorFactory;
import mat.dao.UserDAO;
import mat.dao.clause.CQLLibraryAssociationDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.CQLValueSetTransferObject;
import mat.model.MatCodeTransferObject;
import mat.model.MatValueSet;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.CQLLibraryHistory;
import mat.model.clause.Measure;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLCodeWrapper;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLDefinitionsWrapper;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
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
import mat.server.cqlparser.CQLLinter;
import mat.server.cqlparser.CQLLinterConfig;
import mat.server.cqlparser.ReverseEngineerListener;
import mat.server.service.MeasurePackageService;
import mat.server.service.impl.XMLMarshalUtil;
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
@Service
public class CQLServiceImpl implements CQLService {

    private static final Log logger = LogFactory.getLog(CQLServiceImpl.class);

    private static final int COMMENTS_MAX_LENGTH = 2500;

    /**
     * The cql supplemental definition XML string.
     */
    private static final String CQL_SUPPLEMENTAL_DEFINITION_XML_STRING =

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

    /**
     * The cql default code system XML string.
     */
    private static final String CQL_DEFAULT_CODE_SYSTEM_XML_STRING =

            "<codeSystems>"

                    + "<codeSystem codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" "
                    + "codeSystemVersion=\"2.46\" id=\"777\" " + "/>"

                    + "<codeSystem codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMEDCT\" "
                    + "codeSystemVersion=\"2016-03\" id=\"777\" " + "/>"

                    + "</codeSystems>";

    @Autowired
    private CQLLibraryDAO cqlLibraryDAO;

    @Autowired
    private CQLLibraryAssociationDAO cqlLibraryAssociationDAO;

    @Autowired
    private MeasurePackageService measurePackageService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CqlValidatorRemoteCallService cqlValidatorRemoteCallService;

    @Autowired
    private CqlVisitorFactory visitorFactory;

    @Autowired
    private CqlParser cqlParser;

    @Autowired
    private FhirCQLResultParser fhirCQLResultParser;

    @Override
    public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String xml, String libraryName, String libraryComment) {

        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        CQLModel cqlModel = new CQLModel();
        result.setCqlModel(cqlModel);

        CQLModelValidator validator = new CQLModelValidator();
        if (validator.isLibraryNameMoreThan500Characters(libraryName)) {
            result.setSuccess(false);
            result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
            return result;
        }

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

    public SaveUpdateCQLResult saveCQLFile(String xml, String cql, CQLLinterConfig config, String modelType) {
        CQLModel newModel = new CQLModel();
        List<CQLError> errors = new ArrayList<>();
        try {
            //Now parse it into a new CQLModel
            switch (modelType) {
                case "QDM":
                    // Use ReverseEngineerListener for QDM.
                    // This code overwrites some of the users changes int he CQL that are not allowed.
                    ReverseEngineerListener listener = new ReverseEngineerListener(cql, config.getPreviousCQLModel());
                    newModel = listener.getCQLModel();
                    errors = listener.getSyntaxErrors();
                    break;
                case "FHIR":
                    // Use the CqlToMatXml parser for FHIR.
                    CqlToMatXml converter = visitorFactory.getCqlToMatXmlVisitor();
                    converter.setSourceModel(config.getPreviousCQLModel());
                    try {
                        cqlParser.parse(cql, converter);
                        newModel = converter.getDestinationModel();

                        //Overwrite fields the user is not allowed to change for FHIR.
                        newModel.setLibraryName(config.getPreviousCQLModel().getLibraryName());
                        newModel.setUsingModelVersion(config.getPreviousCQLModel().getUsingModelVersion());
                        newModel.setUsingModel(config.getPreviousCQLModel().getUsingModel());
                        newModel.setVersionUsed(config.getPreviousCQLModel().getVersionUsed());
                    } catch (MatException me) {
                        newModel.setLibraryName("");
                        newModel.setVersionUsed("");
                        errors = Collections.singletonList(new CQLError());
                        errors.get(0).setErrorMessage(me.getMessage());
                        errors.get(0).setErrorInLine(1);
                        errors.get(0).setErrorAtOffeset(1);
                        errors.get(0).setStartErrorInLine(1);
                        errors.get(0).setEndErrorInLine(1);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected modelType " + modelType);
            }

            //Parser errors.
            if (!errors.isEmpty()) {
                SaveUpdateCQLResult r = new SaveUpdateCQLResult();
                r.setXml(xml); // retain the old xml if there are syntax errors (essentially not saving)
                r.setCqlString(cql);
                r.setCqlModel(newModel);
                r.setSuccess(false);
                r.setCqlErrors(errors);
                r.setLibraryNameErrorsMap(new HashMap<>());
                r.setLibraryNameWarningsMap(new HashMap<>());
                r.setFailureReason(StringUtils.equals(modelType, "FHIR") ? SaveUpdateCQLResult.CUSTOM : SaveUpdateCQLResult.SYNTAX_ERRORS);
                return r;
            }

            // The model could have altered due to places the user is not allowed to change.
            // The cql needs to be regenerated from the model to catch these cases.
            // At one point formatting was being done, but the formatter doesn't work that well with
            // fhir cql and it was causing errors.
            String scrubbedCql = CQLUtilityClass.getCqlString(newModel, "").getLeft();

            // Validation.
            SaveUpdateCQLResult parsedResult;
            if (ModelTypeHelper.FHIR.equalsIgnoreCase(modelType)) {
                parsedResult = parseFhirCqlLibraryForErrors(newModel, scrubbedCql);
            } else {
                parsedResult = parseCQLLibraryForErrors(newModel);
            }

            // Duplicate identifiers.
            if (CQLValidationUtil.doesModelHaveDuplicateIdentifierOrIdentifierAsKeyword(newModel)) {
                parsedResult.setXml(xml); // retain the old xml if there are duplicate identifiers (essentially not saving)
                parsedResult.setCqlString(cql);
                parsedResult.setSuccess(false);
                parsedResult.setFailureReason(SaveUpdateCQLResult.DUPLICATE_CQL_KEYWORD);
                return parsedResult;
            }

            //Gen the new mat xml.
            String newLibXml = marshallCQLModel(newModel);

            //Lint.
            CQLLinter linter = CQLUtil.lint(cql, config); //lint with original cql
            SaveUpdateCQLResult result = getCQLDataForLoad(newLibXml);
            result.setCqlErrors(parsedResult.getCqlErrors());
            result.setCqlWarnings(parsedResult.getCqlWarnings());
            result.setLibraryNameErrorsMap(parsedResult.getLibraryNameErrorsMap());
            result.setLibraryNameWarningsMap(parsedResult.getLibraryNameWarningsMap());
            result.setCqlString(scrubbedCql);
            result.setXml(newLibXml);
            result.getLinterErrors().addAll(linter.getErrors());
            result.getLinterWarningMessages().addAll(linter.getWarningMessages());
            result.getLinterErrorMessages().addAll(linter.getErrorMessages());
            result.setDatatypeUsedCorrectly(CQLUtil.validateDatatypeCombinations(result.getCqlModel(),
                    result.getUsedCQLArtifacts().getValueSetDataTypeMap(),
                    result.getUsedCQLArtifacts().getCodeDataTypeMap()));
            result.setSuccess(true);

            return result;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public List<CQLLibraryHistory> createCQLLibraryHistory(List<CQLLibraryHistory> existingLibraryHistoryList, String CQLLibraryString, CQLLibrary cqlLibrary, Measure measure) {
        CQLLibraryHistory cqlLibraryHistory = new CQLLibraryHistory();

        cqlLibraryHistory.setMeasure(measure);
        cqlLibraryHistory.setCqlLibrary(cqlLibrary);
        cqlLibraryHistory.setFreeTextEditorUsed(true);
        cqlLibraryHistory.setCqlLibraryString(CQLLibraryString);
        String loggedinUserId = LoggedInUserUtil.getLoggedInUser();
        User user = userDAO.find(loggedinUserId);
        cqlLibraryHistory.setLastModifiedBy(user);
        if (existingLibraryHistoryList == null) {
            existingLibraryHistoryList = new ArrayList<>();
        }
        existingLibraryHistoryList.add(cqlLibraryHistory);
        return existingLibraryHistoryList;
    }

    private String marshallCQLModel(CQLModel cqlModel) {
        return CQLUtilityClass.getXMLFromCQLModel(cqlModel);
    }

    private CQLModel reverseEngineerCQLModel(String cql, CQLModel previousModel) throws IOException {
        ReverseEngineerListener listener = new ReverseEngineerListener(cql, previousModel);
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
                                                      List<CQLFunctions> functionsList, boolean isFormatable, String modelType) {

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
                    isValidArgumentName = validator.isValidQDMName(argument.getArgumentName());
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
                parseCQLExpressionForErrors(result, xml, cqlExpressionName, functionWithOriginalContent.getLogic(),
                        functionWithOriginalContent.getName(), "Function", modelType);
                if (!ModelTypeHelper.FHIR.equalsIgnoreCase(modelType) && result.getUsedCQLArtifacts().getUsedCQLFunctions().contains(functionWithOriginalContent.getName())) {
                    functionWithEdits.setContext(functionWithOriginalContent.getContext());
                }
            }

            // update the the information the model
            cqlModel.getCqlFunctions().removeIf(f -> f.getId().equals(functionWithEdits.getId()));
            cqlModel.getCqlFunctions().add(functionWithEdits);


            String cqlExpressionName = "define function" + " \"" + functionWithEdits.getName() + "\"";
            parseCQLExpressionForErrors(result, CQLUtilityClass.getXMLFromCQLModel(cqlModel), cqlExpressionName,
                    functionWithEdits.getLogic(), functionWithEdits.getName(), "Definition", modelType);

            // do some processing if the are no errors in the CQL
            if (!ModelTypeHelper.FHIR.equalsIgnoreCase(modelType) && result.getCqlErrors().isEmpty()) {
                Optional<CQLExpressionObject> expressionObject = findExpressionObject(functionWithEdits.getName(), result.getCqlObject().getCqlFunctionObjectList());
                if (expressionObject.isPresent()) {
                    functionWithEdits.setReturnType(expressionObject.get().getReturnType());
                }

                if (isFormatable) {
                    functionWithEdits.setLogic(formatFunction(functionWithEdits));
                    cqlModel.getCqlFunctions().removeIf(f -> f.getId().equals(functionWithEdits.getId()));
                    cqlModel.getCqlFunctions().add(functionWithEdits);
                }
            }

            result.setXml(CQLUtilityClass.getXMLFromCQLModel(cqlModel));
            result.setFunction(functionWithEdits);
            result.setCqlModel(cqlModel);
            result.getCqlModel().setCqlFunctions(sortFunctionList(result.getCqlModel().getCqlFunctions()));
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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

    @Override
    public SaveUpdateCQLResult saveAndModifyParameters(String xml, CQLParameter parameterWithOriginalContent,
                                                       CQLParameter parameterWithEdits, List<CQLParameter> parameterList, boolean isFormatable, String modelType) {
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
                    parameterWithEdits.getLogic(), parameterWithEdits.getName(), "Parameter", modelType);

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
            logger.error(e.getMessage(), e);
            result.setSuccess(false);
        }

        return result;
    }

    /**
     * Builds a temporary parameter expressions string, formats the parameter, and
     * returns the formatted logic
     *
     * @param parameter the parameter
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

    @Override
    public SaveUpdateCQLResult saveAndModifyDefinitions(String xml, CQLDefinition definitionWithOriginalContent,
                                                        CQLDefinition definitionWithEdits, List<CQLDefinition> definitionList, boolean isFormatable, String modelType) {
        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        result.setXml(xml); // if any failure, it will use this xml, which is the original

        try {

            if (StringUtils.isEmpty(xml)) {
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

            // if the definition already exists in the model, then get the id, set the definition with edits id,
            // remove the current definition from the model, then add the one with edits to the model.
            Optional<CQLDefinition> definitionToBeEditedFromModel = cqlModel.getDefinitionList().stream().filter(d -> d.getId().equals(definitionWithEdits.getId())).findFirst();
            if (definitionToBeEditedFromModel.isPresent()) {
                definitionWithEdits.setId(definitionToBeEditedFromModel.get().getId());
                cqlModel.getDefinitionList().removeIf(d -> d.getId().equals(definitionWithEdits.getId()));
            }

            cqlModel.getDefinitionList().add(definitionWithEdits);
            String cqlExpressionName = "define" + " \"" + definitionWithEdits.getName() + "\"";
            parseCQLExpressionForErrors(result, CQLUtilityClass.getXMLFromCQLModel(cqlModel), cqlExpressionName,
                    definitionWithEdits.getLogic(), definitionWithEdits.getName(), "Definition", modelType);

            // do some processing if the are no errors in the CQL
            if (!ModelTypeHelper.FHIR.equalsIgnoreCase(modelType) && result.getCqlErrors().isEmpty()) {
                Optional<CQLExpressionObject> expressionObject = findExpressionObject(definitionWithEdits.getName(), result.getCqlObject().getCqlDefinitionObjectList());
                if (expressionObject.isPresent()) {
                    definitionWithEdits.setReturnType(expressionObject.get().getReturnType());
                }

                // if the definition is able to be formatted, format it, remove it from the existing model, and add back the formatted object
                if (isFormatable) {
                    definitionWithEdits.setLogic(formatDefinition(definitionWithEdits));
                    cqlModel.getDefinitionList().removeIf(d -> d.getId().equals(definitionWithEdits.getId()));
                    cqlModel.getDefinitionList().add(definitionWithEdits);
                }
            }

            result.setXml(CQLUtilityClass.getXMLFromCQLModel(cqlModel));
            result.setDefinition(definitionWithEdits);
            result.setCqlModel(cqlModel);
            result.getCqlModel().setDefinitionList(sortDefinitionsList(result.getCqlModel().getDefinitionList()));
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
        String tempDefinitionString = definitionStatement + "\n" + definition.getLogic();

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
        // expression body and then trim whitespace
        String expressionBodyString = cqlExpressionString.replace(expressionDefinitionString, "").trim();
        return CQLUtilityClass.replaceFirstWhitespaceInLineForExpression(expressionBodyString);
    }

    @Override
    public SaveUpdateCQLResult saveAndModifyIncludeLibrayInCQLLookUp(String xml, CQLIncludeLibrary includedLibraryWithOriginalContent,
                                                                     CQLIncludeLibrary includedLibraryWithEdits, List<CQLIncludeLibrary> includedLibraryList, String modelType) throws InvalidLibraryException {

        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        result.setXml(xml); // if any failure, it will use this xml, which is the original

        if (StringUtils.isEmpty(xml)) {
            return result;
        }


        CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(xml);
        if (includedLibraryWithOriginalContent != null) {
            includedLibraryWithEdits.setId(includedLibraryWithOriginalContent.getId());
        } else {
            includedLibraryWithEdits.setId(UUID.randomUUID().toString());
        }


        Optional<CQLIncludeLibrary> includedLibraryToBeEditedFromModel = cqlModel.getCqlIncludeLibrarys().stream().filter(l -> includedLibraryWithEdits.getId().equals(l.getId())).findFirst();
        if (includedLibraryToBeEditedFromModel.isPresent()) {
            includedLibraryToBeEditedFromModel.get().setAliasName(includedLibraryWithEdits.getAliasName());
            includedLibraryToBeEditedFromModel.get().setCqlLibraryId(includedLibraryWithEdits.getCqlLibraryId());
            includedLibraryToBeEditedFromModel.get().setCqlLibraryName(includedLibraryWithEdits.getCqlLibraryName());
            includedLibraryToBeEditedFromModel.get().setMeasureId(includedLibraryWithEdits.getMeasureId());
            includedLibraryToBeEditedFromModel.get().setQdmVersion(includedLibraryWithEdits.getQdmVersion());
            includedLibraryToBeEditedFromModel.get().setSetId(includedLibraryWithEdits.getSetId());
            includedLibraryToBeEditedFromModel.get().setVersion(includedLibraryWithEdits.getVersion());
        } else {
            CQLIncludeLibraryValidator libraryValidator = new CQLIncludeLibraryValidator();
            libraryValidator.validate(includedLibraryWithEdits, cqlModel, modelType);

            if (!libraryValidator.isValid()) {
                throw new InvalidLibraryException(libraryValidator.getMessages());
            }

            cqlModel.getCqlIncludeLibrarys().add(includedLibraryWithEdits);
        }


        result.setSuccess(true);
        CQLUtil.getIncludedCQLExpressions(cqlModel, cqlLibraryDAO);
        result.setXml(CQLUtilityClass.getXMLFromCQLModel(cqlModel));
        result.setCqlModel(cqlModel);
        result.setIncludeLibrary(includedLibraryWithEdits);
        result.setUsedCQLArtifacts(getUsedCQlArtifacts(result.getXml()));


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
    public SaveUpdateCQLResult deleteDefinition(String xml, CQLDefinition toBeDeletedObj) {
        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(xml);
        result.setCqlModel(cqlModel);
        cqlModel.getDefinitionList().removeIf(d -> d.getId().equals(toBeDeletedObj.getId()));
        result.setSuccess(true);
        result.setCqlModel(cqlModel);
        result.getCqlModel().setDefinitionList(sortDefinitionsList(cqlModel.getDefinitionList()));
        result.setXml(CQLUtilityClass.getXMLFromCQLModel(cqlModel));

        return result;
    }

    @Override
    public SaveUpdateCQLResult deleteValueSet(String xml, String toBeDelValueSetId) {
        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        CQLModel model = CQLUtilityClass.getCQLModelFromXML(xml);

        Optional<CQLQualityDataSetDTO> valuesetToDelete = model.getValueSetList().stream().filter(v -> v.getId().equals(toBeDelValueSetId)).findFirst();

        if (valuesetToDelete.isPresent()) {
            model.getValueSetList().removeIf(v -> v.getId().equals(toBeDelValueSetId));
            result.setXml(CQLUtilityClass.getXMLFromCQLModel(model));
            result.setCqlModel(model);
            result.setSuccess(true);
            result.setCqlAppliedQDMList(model.getValueSetList());
            result.setCqlQualityDataSetDTO(valuesetToDelete.get());
        } else {
            result.setSuccess(false);
        }

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
                result.setCqlModel(CQLUtilityClass.getCQLModelFromXML(result.getXml()));
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

    @Override
    public SaveUpdateCQLResult deleteFunction(String xml, CQLFunctions toBeDeletedObj) {
        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(xml);
        result.setCqlModel(cqlModel);
        cqlModel.getCqlFunctions().removeIf(f -> f.getId().equals(toBeDeletedObj.getId()));
        result.setCqlModel(cqlModel);
        result.setXml(CQLUtilityClass.getXMLFromCQLModel(cqlModel));
        result.setSuccess(true);
        result.getCqlModel().setCqlFunctions(sortFunctionList(result.getCqlModel().getCqlFunctions()));

        return result;
    }

    @Override
    public SaveUpdateCQLResult deleteParameter(String xml, CQLParameter toBeDeletedObj) {
        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(xml);
        cqlModel.getCqlParameters().removeIf(p -> p.getId().equals(toBeDeletedObj.getId()));
        result.setXml(CQLUtilityClass.getXMLFromCQLModel(cqlModel));
        result.setCqlModel(cqlModel);
        result.setSuccess(true);
        result.getCqlModel().setCqlParameters(sortParametersList(result.getCqlModel().getCqlParameters()));

        return result;
    }

    @Override
    public SaveUpdateCQLResult getCQLData(String xmlString) {
        CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(xmlString);
        String cqlString = CQLUtilityClass.getCqlString(cqlModel, "").getLeft();

        SaveUpdateCQLResult result;
        if (ModelTypeHelper.isFhir(cqlModel.getUsingModel())) {
            result = parseFhirCqlLibraryForErrors(cqlModel, cqlString);
        } else {
            result = parseCQLLibraryForErrors(cqlModel);
        }

        if (result.getCqlErrors().isEmpty() && !StringUtils.equals(cqlModel.getUsingModel(),"FHIR")) {
            result.setUsedCQLArtifacts(getUsedCQlArtifacts(xmlString));
            setUsedValuesets(result, cqlModel);
            setUsedCodes(result, cqlModel);
            boolean isValid = CQLUtil.validateDatatypeCombinations(cqlModel,
                    result.getUsedCQLArtifacts().getValueSetDataTypeMap(),
                    result.getUsedCQLArtifacts().getCodeDataTypeMap());
            result.setDatatypeUsedCorrectly(isValid);

        }

        result.setCqlModel(cqlModel);
        result.setCqlString(cqlString);

        return result;
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
        result.setUsedCQLArtifacts(getUsedCQlArtifacts(xmlString));

        return result;
    }

    @Override
    public SaveUpdateCQLResult getCQLLibraryData(String xmlString, String modelType) {
        CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(xmlString);
        String cqlString = CQLUtilityClass.getCqlString(cqlModel, "").getLeft();

        HashMap<String, LibHolderObject> cqlLibNameMap = new HashMap<>();
        Map<CQLIncludeLibrary, CQLModel> cqlIncludeModelMap = new HashMap<>();
        String parentLibraryName = cqlModel.getLibraryName();
        CQLUtil.getCQLIncludeMaps(cqlModel, cqlLibNameMap, cqlIncludeModelMap, getCqlLibraryDAO());
        cqlModel.setIncludedCQLLibXMLMap(cqlLibNameMap);
        cqlModel.setIncludedLibrarys(cqlIncludeModelMap);

        SaveUpdateCQLResult result;
        if (ModelTypeHelper.isFhir(modelType)) {
            result = parseFhirCqlLibraryForErrors(cqlModel, cqlString);
        } else {
            // QDM
            List<String> expressionList = cqlModel.getExpressionListFromCqlModel();
            result = CQLUtil.parseQDMCQLLibraryForErrors(cqlModel, cqlLibraryDAO, expressionList);

            Iterator<CQLIncludeLibrary> libraryIter = cqlModel.getIncludedLibrarys().keySet().iterator();
            while (libraryIter.hasNext()) {
                CQLIncludeLibrary curLibrary = libraryIter.next();
                if (!cqlModel.getUsingModelVersion().equals(curLibrary.getQdmVersion())) {
                    result.setQDMVersionMatching(false);
                }
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
                result.setCqlString(formatter.format(cqlString));
            } catch (IOException e) {
                result.setCqlString(cqlString);
            }
        } else {
            result.setCqlString(cqlString);
        }

        result.setCqlString(cqlString);
        result.setLibraryName(parentLibraryName);

        return result;
    }

    private SaveUpdateCQLResult parseFhirCqlLibraryForErrors(CQLModel cqlModel, String cqlString) {
        SaveUpdateCQLResult result;
        String cqlValidationResponse = cqlValidatorRemoteCallService.validateCqlExpression(cqlString);
        result = generateParsedCqlObject(cqlValidationResponse, cqlModel);
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
        return CQLUtil.parseQDMCQLLibraryForErrors(cqlModel, getCqlLibraryDAO(), null);
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
        if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
            xml = new XmlProcessor(xmlModel.getXml()).getXmlByTagName("cqlLookUp");
        }

        try {
            if (xml != null) {
                XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
                details = (CQLDefinitionsWrapper) xmlMarshalUtil.convertXMLToObject("CQLDefinitionModelMapping.xml", xml, CQLDefinitionsWrapper.class);
            }

        } catch (MarshalException | ValidationException | MappingException | IOException e) {
            logger.error("Failed to load CQLDefinitionModelMapping.xml " + e.getMessage(), e);
        }
        return details;
    }

    @Override
    public String createParametersXML(CQLParameter parameter) {
        logger.info("In CQLServiceImpl.createParametersXML");
        CQLParametersWrapper wrapper = new CQLParametersWrapper();
        List<CQLParameter> paramList = new ArrayList<>();
        paramList.add(parameter);
        wrapper.setCqlParameterList(paramList);

        return createNewXML("CQLParameterModelMapping.xml", wrapper);
    }

    @Override
    public String createDefinitionsXML(CQLDefinition definition) {
        logger.info("In CQLServiceImpl.createDefinitionsXML");
        CQLDefinitionsWrapper wrapper = new CQLDefinitionsWrapper();
        List<CQLDefinition> definitionList = new ArrayList<>();
        definitionList.add(definition);
        wrapper.setCqlDefinitions(definitionList);

        return createNewXML("CQLDefinitionModelMapping.xml", wrapper);
    }

    private String createNewXML(String mapping, Object object) {
        String stream = null;
        try {
            final XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
            stream = xmlMarshalUtil.convertObjectToXML(mapping, object);

        } catch (MarshalException | ValidationException | IOException | MappingException e) {
            logger.info("Exception in createExpressionXML: " + e, e);
        }

        logger.info("Exiting ManageCodeListServiceImpl.createXml()");
        return stream;
    }

    @Override
    public SaveUpdateCQLResult getCQLFileData(String xmlString) {
        SaveUpdateCQLResult result = getCQLData(xmlString);
        String cqlString = getCqlString(result.getCqlModel());
        result.setSuccess(cqlString != null);
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

        return CQLUtilityClass.getCqlString(cqlModel, "").getLeft();
    }

    @Override
    public CQLKeywords getCQLKeyWords() {
        return CQLKeywordsUtil.getCQLKeywords();
    }

    /**
     * Sort definitions list.
     *
     * @param defineList the define list
     * @return the list
     */
    private List<CQLDefinition> sortDefinitionsList(List<CQLDefinition> defineList) {

        Collections.sort(defineList, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        return defineList;
    }

    /**
     * Sort parameters list.
     *
     * @param paramList the param list
     * @return the list
     */
    private List<CQLParameter> sortParametersList(List<CQLParameter> paramList) {

        Collections.sort(paramList, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        return paramList;
    }

    /**
     * Sort Functions list.
     *
     * @param funcList the Function list
     * @return the list
     */
    private List<CQLFunctions> sortFunctionList(List<CQLFunctions> funcList) {

        Collections.sort(funcList, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        return funcList;
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
                logger.error(e1.getMessage(), e1);
            }
            fr = new FileReader(xmlFile);
            BufferedReader br = new BufferedReader(fr);

            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line.trim());
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return sb.toString();
    }

    /*
     * {@inheritDoc}
     */
    @Override
    public String getSupplementalDefinitions() {
        return CQL_SUPPLEMENTAL_DEFINITION_XML_STRING;
    }

    /*
     * {@inheritDoc}
     */
    @Override
    public String getDefaultCodeSystems() {
        return CQL_DEFAULT_CODE_SYSTEM_XML_STRING;
    }

    private SaveUpdateCQLResult parseCQLExpressionForErrors(SaveUpdateCQLResult result, String xml,
                                                            String cqlExpressionName, String logic, String expressionName, String expressionType, String modelType) {

        SaveUpdateCQLResult parsedCQL;
        String cqlValidationResponse;

        CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(xml);

        Pair<String, Integer> cqlRes = CQLUtilityClass.getCqlString(cqlModel, cqlExpressionName);

        String cqlFileString = cqlRes.getLeft();

        cqlModel.setLines(countLines(cqlFileString));

        String wholeDef = "";
        int size = 0;
        int startLine = 0;
        int endLine = cqlRes.getRight();

        if (expressionType.equalsIgnoreCase("parameter")) {
            endLine = endLine + 1; // for parameters, the size is actually 1 more than reported.
            wholeDef = cqlExpressionName + " " + logic;
            size = countLines(wholeDef);

            if (size > 1) {
                startLine = endLine - size;
            } else {
                startLine = endLine;
            }
        } else {
            wholeDef = cqlExpressionName + " :\n" + logic;
            size = countLines(wholeDef);
            startLine = endLine - size + 1; // the start line is really 1 more than endLine - size because
            // the definition logic starts on the next line.
        }

        result.setStartLine(startLine);
        result.setEndLine(endLine);

        String formattedName = cqlModel.getFormattedName();

        List<CQLError> expressionErrors = new ArrayList<>();
        List<CQLError> expressionWarnings = new ArrayList<>();

        if (ModelTypeHelper.FHIR.equalsIgnoreCase(modelType)) {
            cqlValidationResponse = cqlValidatorRemoteCallService.validateCqlExpression(cqlFileString);
            parsedCQL = generateParsedCqlObject(cqlValidationResponse, cqlModel);
        } else {
            List<String> expressionList = cqlModel.getExpressionListFromCqlModel();
            parsedCQL = CQLUtil.parseQDMCQLLibraryForErrors(cqlModel, cqlLibraryDAO, expressionList);
        }

        if (CollectionUtils.isNotEmpty(parsedCQL.getLibraryNameErrorsMap().get(formattedName))) {
            List<CQLError> libraryErrors = parsedCQL.getLibraryNameErrorsMap().get(formattedName);
            result.setValidCQLWhileSavingExpression(false);
            buildExpressionExceptionList(startLine, endLine, libraryErrors, expressionErrors);
        }

        if (CollectionUtils.isNotEmpty(parsedCQL.getLibraryNameWarningsMap().get(formattedName))) {
            List<CQLError> libraryWarnings = parsedCQL.getLibraryNameWarningsMap().get(formattedName);
            buildExpressionExceptionList(startLine, endLine, libraryWarnings, expressionWarnings);
        }

        if (expressionErrors.isEmpty() && !ModelTypeHelper.FHIR.equalsIgnoreCase(modelType)) {
            result.setDatatypeUsedCorrectly(findValidDataTypeUsage(cqlModel, expressionName, expressionType, parsedCQL));
            if (result.isDatatypeUsedCorrectly()) {
                XmlProcessor xmlProcessor = new XmlProcessor(xml);
                CQLArtifactHolder cqlArtifactHolder = CQLUtil.getCQLArtifactsReferredByPoplns(xmlProcessor.getOriginalDoc());
                parsedCQL.getUsedCQLArtifacts().getUsedCQLDefinitions().addAll(cqlArtifactHolder.getCqlDefFromPopSet());
                parsedCQL.getUsedCQLArtifacts().getUsedCQLFunctions().addAll(cqlArtifactHolder.getCqlFuncFromPopSet());
            }
        }

        result.setCqlObject(parsedCQL.getCqlObject());
        result.setCqlModel(cqlModel);
        result.setCqlErrors(expressionErrors);
        result.setCqlWarnings(expressionWarnings);
        result.setUsedCQLArtifacts(parsedCQL.getUsedCQLArtifacts());
        return result;
    }

    @Override
    public SaveUpdateCQLResult generateParsedCqlObject(String cqlValidationResponse, CQLModel cqlModel) {
        return fhirCQLResultParser.generateParsedCqlObject(cqlValidationResponse, cqlModel);
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

        SaveUpdateCQLResult cqlResult = CQLUtil.parseQDMCQLLibraryForErrors(cqlModel, getCqlLibraryDAO(), exprList);
        return generateUsedCqlArtifactsResult(cqlModel, xml, cqlResult);
    }

    @Override
    public GetUsedCQLArtifactsResult generateUsedCqlArtifactsResult(CQLModel cqlModel, String xml, SaveUpdateCQLResult cqlResult) {
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

    @Override
    public SaveUpdateCQLResult saveCQLValueset(String xml, CQLValueSetTransferObject valueSetTransferObject) {
        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        CQLModel model = CQLUtilityClass.getCQLModelFromXML(xml);

        if (!valueSetTransferObject.validateModel()) {
            result.setSuccess(false);
            result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
            return result;
        }

        CQLQualityDataSetDTO qds = convertValueSetTransferObjectToQualityDataSetDTO(valueSetTransferObject);
        Optional<CQLQualityDataSetDTO> existingValueset = model.getValueSetList().stream().filter(v -> v.getUuid().equals(valueSetTransferObject.getCqlQualityDataSetDTO().getUuid())).findFirst();
        // if this is an edit, make sure the UUID and ID are the same as the valueset being edited
        // and remove it from the list
        if (existingValueset.isPresent()) {
            qds.setUuid(existingValueset.get().getUuid());
            qds.setId(existingValueset.get().getId());
            model.getValueSetList().remove(existingValueset.get());
        } else {
            qds.setUuid(UUID.randomUUID().toString());
            qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            if (model.getValueSetList().stream().filter(v -> v.getName().equals(qds.getName())).count() > 0) {
                result.setSuccess(false);
                result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
                result.setCqlQualityDataSetDTO(qds);
                return result;
            }
        }

        model.getValueSetList().add(qds);


        result.setSuccess(true);
        result.setCqlAppliedQDMList(sortQualityDataSetList(model.getValueSetList()));
        result.setXml(CQLUtilityClass.getXMLFromCQLModel(model));
        return result;
    }


    private CQLQualityDataSetDTO convertValueSetTransferObjectToQualityDataSetDTO(CQLValueSetTransferObject valueSetTransferObject) {
        CQLQualityDataSetDTO qds = new CQLQualityDataSetDTO();
        MatValueSet matValueSet = valueSetTransferObject.getMatValueSet();
        qds.setName(valueSetTransferObject.getCqlQualityDataSetDTO().getName());
        qds.setSuffix(valueSetTransferObject.getCqlQualityDataSetDTO().getSuffix());
        qds.setOriginalCodeListName(valueSetTransferObject.getCqlQualityDataSetDTO().getOriginalCodeListName());
        qds.setDataType("");
        // if the oid is empty, that means it is a user defined valueset and we need to save some special data
        // or if the oid is the user defined oid
        if (StringUtils.isEmpty(valueSetTransferObject.getCqlQualityDataSetDTO().getOid()) ||
                valueSetTransferObject.getCqlQualityDataSetDTO().getOid().equals(ConstantMessages.USER_DEFINED_QDM_OID)) {
            qds.setOid(ConstantMessages.USER_DEFINED_QDM_OID);
            qds.setTaxonomy(ConstantMessages.USER_DEFINED_QDM_NAME);
            qds.setValueSetType(StringUtils.EMPTY);
            qds.setVersion("1.0");
            qds.setRelease("");
            qds.setProgram("");
        } else {
            qds.setOid(matValueSet.getID());
            qds.setRelease(valueSetTransferObject.getCqlQualityDataSetDTO().getRelease());
            qds.setProgram(valueSetTransferObject.getCqlQualityDataSetDTO().getProgram());
            qds.setVersion("");
            qds.setValueSetType(matValueSet.getType());
            if (matValueSet.isGrouping()) {
                qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
            } else {
                qds.setTaxonomy(matValueSet.getCodeSystemName());
            }
        }
        return qds;
    }

    private List<CQLQualityDataSetDTO> sortQualityDataSetList(final List<CQLQualityDataSetDTO> finalList) {
        Collections.sort(finalList, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        return finalList;
    }

    @Override
    public SaveUpdateCQLResult saveCQLCodes(String xml, MatCodeTransferObject codeTransferObject) {
        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        codeTransferObject.scrubForMarkUp();
        CQLModel model = CQLUtilityClass.getCQLModelFromXML(xml);

        if (codeTransferObject.isValidModel()) {

            CQLCode appliedCode = codeTransferObject.getCqlCode();

            Optional<CQLCode> existingCode = model.getCodeList().stream().filter(c -> c.getId().equals(appliedCode.getId())).findFirst();
            if (existingCode.isPresent()) {
                appliedCode.setId(existingCode.get().getId());
                model.getCodeList().removeIf(c -> c.getId().equals(existingCode.get().getId()));
                model.getCodeList().add(appliedCode);
            } else {
                if (model.getCodeList().stream().filter(c -> c.getDisplayName().equals(appliedCode.getDisplayName())).count() > 0) {
                    result.setSuccess(false);
                    result.setFailureReason(result.getDuplicateCode());
                    return result;
                }

                appliedCode.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                model.getCodeList().add(appliedCode);
            }
        }

        List<CQLCode> codesList = model.getCodeList().stream().sorted(Comparator.comparing(CQLCode::getDisplayName, String.CASE_INSENSITIVE_ORDER)).collect(Collectors.toList());
        model.setCodeList(codesList);

        result.setCqlCodeList(model.getCodeList());
        result.setSuccess(true);
        result.setCqlModel(model);
        result.setXml(CQLUtilityClass.getXMLFromCQLModel(model));


        return result;
    }

    @Override
    public SaveUpdateCQLResult saveCQLCodeSystem(String xml, CQLCodeSystem codeSystem) {
        logger.info("::: CQLServiceImpl saveCQLCodeSystem Start :::");
        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        CQLModel model = CQLUtilityClass.getCQLModelFromXML(xml);

        Optional<CQLCodeSystem> existingCodesystem = model.getCodeSystemList().stream().filter(cs -> cs.getId().equals(codeSystem.getId())).findFirst();

        if (existingCodesystem.isPresent()) {
            // edit codesystem
            codeSystem.setId(existingCodesystem.get().getId());
            model.getCodeSystemList().remove(existingCodesystem.get());
            model.getCodeSystemList().add(codeSystem);
        } else {
            // new code system

            if (model.getCodeSystemList().stream().filter(cs -> (cs.getCodeSystemName().equals(codeSystem.getCodeSystemName())
                    && cs.getCodeSystemVersion().equals(codeSystem.getCodeSystemVersion()))).count() > 0) {
                logger.info("::: CodeSystem Already added :::");
                result.setSuccess(false);
                return result;
            }

            codeSystem.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            model.getCodeSystemList().add(codeSystem);
        }

        result.setCqlModel(model);
        result.setSuccess(true);
        result.setXml(CQLUtilityClass.getXMLFromCQLModel(model));

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

    @Override
    public CQLQualityDataModelWrapper getCQLValusets(String measureId,
                                                     CQLQualityDataModelWrapper cqlQualityDataModelWrapper) {
        MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureId);
        String xmlString = model.getXml();
        SaveUpdateCQLResult cqlDataResult = getCQLData(xmlString);
        List<CQLQualityDataSetDTO> cqlQualityDataSetDTOs = CQLUtilityClass
                .sortCQLQualityDataSetDto(cqlDataResult.getCqlModel().getAllValueSetAndCodeList());
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
    public SaveUpdateCQLResult deleteInclude(String xml, CQLIncludeLibrary toBeModifiedIncludeObj) {
        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        result.setXml(xml);

        CQLModel model = CQLUtilityClass.getCQLModelFromXML(xml);
        model.getCqlIncludeLibrarys().removeIf(m -> m.getId().equals(toBeModifiedIncludeObj.getId()));
        result.setSuccess(true);

        if (result.isSuccess()) {
            result.setXml(CQLUtilityClass.getXMLFromCQLModel(model));
            result.setCqlModel(model);
            CQLUtil.getIncludedCQLExpressions(model, cqlLibraryDAO);
            result.setUsedCQLArtifacts(getUsedCQlArtifacts(result.getXml()));
        }

        return result;
    }

    @Override
    public void deleteCQLAssociation(CQLIncludeLibrary currentObj, String associatedWithId) {
        CQLLibraryAssociation cqlLibraryAssociation = new CQLLibraryAssociation();
        cqlLibraryAssociation.setCqlLibraryId(currentObj.getCqlLibraryId());
        cqlLibraryAssociation.setAssociationId(associatedWithId);
        cqlLibraryAssociationDAO.deleteAssociation(cqlLibraryAssociation);
    }

    private Map<String, List<CQLError>> getCQLErrorsPerExpressions(CQLModel cqlModel, List<CQLError> cqlErrors) {

        Map<String, List<CQLError>> expressionMapWithError = new HashMap<>();
        List<CQLExpressionObject> cqlExpressionObjects = getCQLExpressionObjectListFromCQLModel(cqlModel);

        for (CQLExpressionObject expressionObject : cqlExpressionObjects) {
            int fileStartLine = -1;
            int fileEndLine = -1;
            int size = 0;
            String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, expressionObject.getName()).getLeft();

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
    public boolean checkIfLibraryNameExists(String libraryName, String setId) {
        return StringUtils.isNotBlank(libraryName) && cqlLibraryDAO.isLibraryNameExists(libraryName, setId);
    }

}
