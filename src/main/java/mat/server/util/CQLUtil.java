package mat.server.util;

import cqltoelm.CQLtoELM;
import cqltoelm.MATCQLFilter;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.clause.CQLLibrary;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.server.CQLUtilityClass;
import mat.server.cqlparser.CQLLinter;
import mat.server.cqlparser.CQLLinterConfig;
import mat.server.cqlparser.FhirCQLLinter;
import mat.server.cqlparser.QdmCQLLinter;
import mat.server.logging.LogFactory;
import mat.shared.CQLError;
import mat.shared.CQLExpressionObject;
import mat.shared.CQLExpressionOprandObject;
import mat.shared.CQLObject;
import mat.shared.ConstantMessages;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.LibHolderObject;
import mat.shared.SaveUpdateCQLResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.hl7.elm.r1.FunctionDef;
import org.hl7.elm.r1.OperandDef;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * The Class CQLUtil.
 */
public class CQLUtil {

    /**
     * The Constant xPath.
     */
    static final javax.xml.xpath.XPath XPATH = XPathFactory.newInstance().newXPath();

    /**
     * The Constant logger.
     */
    private static final Log logger = LogFactory.getLog(CQLUtil.class);


    /**
     * This method will find out all the CQLDefinitions referred to by
     * populations defined in a measure.
     *
     * @param originalDoc the original doc
     * @return the CQL artifacts referred by poplns
     */
    public static CQLArtifactHolder getCQLArtifactsReferredByPoplns(Document originalDoc) {
        CQLUtil cqlUtil = new CQLUtil();
        CQLUtil.CQLArtifactHolder cqlArtifactHolder = cqlUtil.new CQLArtifactHolder();

        String xPathForDefinitions = "//cqldefinition";
        String xPathForFunctions = "//cqlfunction";

        try {
            NodeList cqlDefinitions = (NodeList) XPATH.evaluate(xPathForDefinitions, originalDoc.getDocumentElement(),
                    XPathConstants.NODESET);

            for (int i = 0; i < cqlDefinitions.getLength(); i++) {
                String uuid = cqlDefinitions.item(i).getAttributes().getNamedItem("uuid").getNodeValue();
                String name = cqlDefinitions.item(i).getAttributes().getNamedItem("displayName").getNodeValue();

                cqlArtifactHolder.addDefinitionUUID(uuid);
                cqlArtifactHolder.addDefinitionIdentifier(name.replaceAll("\"", ""));
            }

            NodeList cqlFunctions = (NodeList) XPATH.evaluate(xPathForFunctions, originalDoc.getDocumentElement(),
                    XPathConstants.NODESET);

            for (int i = 0; i < cqlFunctions.getLength(); i++) {
                String uuid = cqlFunctions.item(i).getAttributes().getNamedItem("uuid").getNodeValue();
                String name = cqlFunctions.item(i).getAttributes().getNamedItem("displayName").getNodeValue();

                cqlArtifactHolder.addFunctionUUID(uuid);
                cqlArtifactHolder.addFunctionIdentifier(name.replaceAll("\"", ""));
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return cqlArtifactHolder;
    }

    /**
     * Checks a CQL Model for to determine if valid datatype valueset/code combinations are being used
     *
     * @param model               the cql model to check
     * @param valuesetDatatypeMap the map of valueset names to datatypes
     * @param codeDatatypeMap     the map of code names to datatypes
     * @return true if all combinations are valid, false otherwise
     */
    public static boolean validateDatatypeCombinations(CQLModel model, Map<String, List<String>> valuesetDatatypeMap, Map<String, List<String>> codeDatatypeMap) {
        Iterator<Entry<String, List<String>>> iterator;
        if (valuesetDatatypeMap != null && !valuesetDatatypeMap.isEmpty()) {
            boolean isValidValuesetDatatypeComboUsed = true;
            iterator = valuesetDatatypeMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> pair = iterator.next();
                List<String> datatypes = pair.getValue();
                isValidValuesetDatatypeComboUsed = isValidDataTypeCombination(datatypes);

                if (!isValidValuesetDatatypeComboUsed) {
                    return false;
                }
            }
        }
        if (codeDatatypeMap != null && !codeDatatypeMap.isEmpty()) {
            boolean isValidCodeDatatypeComboUsed = true;
            iterator = codeDatatypeMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> pair = iterator.next();
                String codeName = pair.getKey();
                List<String> datatypes = pair.getValue();
                CQLCode code = model.getCodeByName(codeName);
                if (code != null) {
                    isValidCodeDatatypeComboUsed = isValidDataTypeCombination(code.getCodeOID(), code.getCodeSystemOID(), datatypes);
                }
                if (!isValidCodeDatatypeComboUsed) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the datatype list contains "Patient Characteristic Birthdate or Patient Characteristic Dead.
     * <p>
     * If it does, return false, otherwise return true
     *
     * @param dataTypeList
     * @return
     */
    private static boolean isValidDataTypeCombination(List<String> dataTypeList) {
        return !(dataTypeList.contains(ConstantMessages.PATIENT_CHARACTERISTIC_BIRTHDATE) || dataTypeList.contains(ConstantMessages.PATIENT_CHARACTERISTIC_EXPIRED));
    }


    /**
     * Checks if the valueset/code has a valid datatype combination.
     *
     * @return true if it valid, false if it is not.
     */
    public static boolean isValidDataTypeCombination(String codeOID, String codesystemOID, List<String> dataTypeList) {
        // check if the birthdate valueset is being used with something other
        // than then Patient Characteristic Birthdate datatype
        if (codeOID.equals(ConstantMessages.BIRTHDATE_OID) && codesystemOID.equals(ConstantMessages.BIRTHDATE_CODE_SYSTEM_OID)) {
            for (String dataType : dataTypeList) {
                if (!dataType.equalsIgnoreCase(ConstantMessages.PATIENT_CHARACTERISTIC_BIRTHDATE)) {
                    return false;
                }
            }
        }

        // check if the dead valueset is being used with something other than
        // the Patient Characteristic Expired datatype
        else if (codeOID.equals(ConstantMessages.DEAD_OID) && codesystemOID.equals(ConstantMessages.DEAD_CODE_SYSTEM_OID)) {
            for (String dataType : dataTypeList) {
                if (!dataType.equalsIgnoreCase(ConstantMessages.PATIENT_CHARACTERISTIC_EXPIRED)) {
                    return false;
                }
            }
        }

        // if not birthdate or dead, check if the datatype list contains Patient
        // Characteristic Birthdate or Dead, any non Birthdate or Dead code
        // cannot use these datatypes.
        else {
            return isValidDataTypeCombination(dataTypeList);
        }


        return true;
    }

    /**
     * Removes all unused cql definitions from the simple xml file. Iterates
     * through the usedcqldefinitions set, adds them to the xpath string, and
     * then removes all nodes that are not a part of the xpath string.
     *
     * @param originalDoc         the simple xml document
     * @param usedDefinitionsList the usedcqldefinitions
     * @throws XPathExpressionException the x path expression exception
     */
    public static void removeUnusedCQLDefinitions(Document originalDoc, List<String> usedDefinitionsList)
            throws XPathExpressionException {

        String idXPathString = "";
        for (String name : usedDefinitionsList) {
            idXPathString += "[@name !='" + name + "']";
        }

        String xPathForUnusedDefinitions = "//cqlLookUp//definition" + idXPathString;

        NodeList unusedCQLDefNodeList = (NodeList) XPATH.evaluate(xPathForUnusedDefinitions,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);

        for (int i = 0; i < unusedCQLDefNodeList.getLength(); i++) {
            Node current = unusedCQLDefNodeList.item(i);

            Node parent = current.getParentNode();
            parent.removeChild(current);
        }

    }

    /**
     * Removes all unused cql functions from the simple xml file. Iterates
     * through the usedcqlfunctions set, adds them to the xpath string, and then
     * removes all nodes that are not a part of the xpath string.
     *
     * @param originalDoc       the simple xml document
     * @param usedFunctionsList the used functions list
     * @throws XPathExpressionException the x path expression exception
     */
    public static void removeUnusedCQLFunctions(Document originalDoc, List<String> usedFunctionsList)
            throws XPathExpressionException {
        String idXPathString = "";
        for (String name : usedFunctionsList) {
            idXPathString += "[@name !='" + name + "']";
        }

        String xPathForUnusedFunctions = "//cqlLookUp//function" + idXPathString;

        NodeList unusedCqlFuncNodeList = (NodeList) XPATH.evaluate(xPathForUnusedFunctions,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);
        for (int i = 0; i < unusedCqlFuncNodeList.getLength(); i++) {
            Node current = unusedCqlFuncNodeList.item(i);

            Node parent = current.getParentNode();
            parent.removeChild(current);
        }
    }

    /**
     * Removes all unused cql valuesets from the simple xml file. Iterates
     * through the usedcqlvaluesets set, adds them to the xpath string, and then
     * removes all nodes that are not a part of the xpath string.
     *
     * @param originalDoc  the simple xml document
     * @param valueSetList the usevaluesets
     * @throws XPathExpressionException the x path expression exception
     */
    public static void removeUnusedValuesets(Document originalDoc, List<String> valueSetList)
            throws XPathExpressionException {
        String nameXPathString = "";

        for (String name : valueSetList) {
            nameXPathString += "[@name !=\"" + name + "\"]";
        }

        String xPathForUnusedValuesets = "//cqlLookUp//valueset" + nameXPathString;

        NodeList unusedCqlValuesetNodeList = (NodeList) XPATH.evaluate(xPathForUnusedValuesets,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);

        for (int i = 0; i < unusedCqlValuesetNodeList.getLength(); i++) {
            Node current = unusedCqlValuesetNodeList.item(i);
            Node parent = current.getParentNode();
            parent.removeChild(current);
        }
    }

    /**
     * Removes all unused cql codes from the simple xml file. Iterates through
     * the usedcodes set, adds them to the xpath string, and then removes all
     * nodes that are not a part of the xpath string.
     *
     * @param originalDoc            the simple xml document
     * @param cqlCodesIdentifierList the usevaluesets
     * @throws XPathExpressionException the x path expression exception
     */
    public static void removeUnusedCodes(Document originalDoc, List<String> cqlCodesIdentifierList)
            throws XPathExpressionException {

        String nameXPathString = "";
        for (String codeName : cqlCodesIdentifierList) {
            nameXPathString += "[@displayName !=\"" + codeName + "\"]";
        }

        String xPathForUnusedCodes = "//cqlLookUp//code" + nameXPathString;

        NodeList unusedCqlCodesNodeList = (NodeList) XPATH.evaluate(xPathForUnusedCodes, originalDoc.getDocumentElement(), XPathConstants.NODESET);
        for (int i = 0; i < unusedCqlCodesNodeList.getLength(); i++) {
            Node current = unusedCqlCodesNodeList.item(i);
            Node parent = current.getParentNode();
            parent.removeChild(current);
        }
        removeUnsedCodeSystems(originalDoc);
    }

    /**
     * Removes the unsed code systems.
     *
     * @param originalDoc the original doc
     * @throws XPathExpressionException the x path expression exception
     */
    private static void removeUnsedCodeSystems(Document originalDoc) throws XPathExpressionException {

        // find all used codeSystemNames
        String xPathForCodesystemNames = "//cqlLookUp/codes/code";
        NodeList codeSystemNameList = (NodeList) XPATH.evaluate(xPathForCodesystemNames,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);

        String nameXPathString = "";
        for (int i = 0; i < codeSystemNameList.getLength(); i++) {
            String name = codeSystemNameList.item(i).getAttributes().getNamedItem("codeSystemName").getNodeValue();
            String version = codeSystemNameList.item(i).getAttributes().getNamedItem("codeSystemVersion").getNodeValue();

            // if the code system doesn't match the name or the version, we'll want to remove it
            nameXPathString += "[@codeSystemName !=\"" + name + "\" or @codeSystemVersion !=\"" + version + "\"]";
        }

        String xPathForUnusedCodeSystems = "//cqlLookUp/codeSystems/codeSystem" + nameXPathString;
        NodeList unusedCqlCodeSystemNodeList = (NodeList) XPATH.evaluate(xPathForUnusedCodeSystems,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);

        for (int i = 0; i < unusedCqlCodeSystemNodeList.getLength(); i++) {
            Node current = unusedCqlCodeSystemNodeList.item(i);
            Node parent = current.getParentNode();
            parent.removeChild(current);
        }

    }

    /**
     * Removes all unused cql parameters from the simple xml file. Iterates
     * through the usedcqlparameters set, adds them to the xpath string, and
     * then removes all nodes that are not a part of the xpath string.
     *
     * @param originalDoc       the simple xml document
     * @param usedParameterList the used parameters
     * @throws XPathExpressionException the x path expression exception
     */
    public static void removeUnusedParameters(Document originalDoc, List<String> usedParameterList)
            throws XPathExpressionException {
        String nameXPathString = "";
        for (String name : usedParameterList) {
            nameXPathString += "[@name !='" + name + "']";
        }

        String xPathForUnusedParameters = "//cqlLookUp//parameter" + nameXPathString;

        NodeList unusedCqlParameterNodeList = (NodeList) XPATH.evaluate(xPathForUnusedParameters,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);
        for (int i = 0; i < unusedCqlParameterNodeList.getLength(); i++) {
            Node current = unusedCqlParameterNodeList.item(i);

            Node parent = current.getParentNode();
            parent.removeChild(current);
        }
    }

    public static boolean checkForUnusedIncludes(String xml, List<String> usedLibraries) {
        XmlProcessor processor = new XmlProcessor(xml);
        Document originalDoc = processor.getOriginalDoc();
        String includeLibrariesXPath = "//cqlLookUp/includeLibrarys";

        try {
            Node node = (Node) XPATH.evaluate(includeLibrariesXPath, originalDoc.getDocumentElement(), XPathConstants.NODE);
            if (node != null) {
                NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    String refName = null;
                    String alias = null;
                    String version = null;
                    if (childNodes.item(i).getAttributes().getNamedItem("cqlLibRefName") != null) {
                        refName = childNodes.item(i).getAttributes().getNamedItem("cqlLibRefName").getNodeValue();
                    }
                    if (childNodes.item(i).getAttributes().getNamedItem("name") != null) {
                        alias = childNodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
                    }
                    if (childNodes.item(i).getAttributes().getNamedItem("cqlVersion") != null) {
                        version = childNodes.item(i).getAttributes().getNamedItem("cqlVersion").getNodeValue();
                    }
                    String libraryKey = refName + "-" + version + "|" + alias;
                    if (!usedLibraries.contains(libraryKey)) {
                        return true;
                    }
                }
            }
        } catch (XPathExpressionException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());
        }

        return false;
    }


    /**
     * Removes all unused cql includes from the simple xml file. Iterates
     * through the usedCQLLibraries set, adds them to the xpath string, and then
     * removes all nodes that are not a part of the xpath string.
     *
     * @param originalDoc the simple xml document
     * @param usedLibList the used includes
     * @param cqlModel    the cql model
     * @throws XPathExpressionException the x path expression exception
     */
    public static void removeUnusedIncludes(Document originalDoc, List<String> usedLibList, CQLModel cqlModel)
            throws XPathExpressionException {

        String nameXPathString = "";
        for (String libName : usedLibList) {
            String[] libArr = libName.split(Pattern.quote("|"));
            String libAliasName = libArr[1];

            String libPathAndVersion = libArr[0];
            String[] libPathArr = libPathAndVersion.split(Pattern.quote("-"));
            String libPath = libPathArr[0];
            String libVer = libPathArr[1];

            nameXPathString += "[@name != '" + libAliasName + "' or @cqlVersion != '" + libVer
                    + "' or @cqlLibRefName != '" + libPath + "']";
        }

        String xPathForUnusedIncludes = "//cqlLookUp//includeLibrarys/includeLibrary" + nameXPathString;
        NodeList unusedCqlIncludeNodeList = (NodeList) XPATH.evaluate(xPathForUnusedIncludes,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);
        for (int i = 0; i < unusedCqlIncludeNodeList.getLength(); i++) {
            Node current = unusedCqlIncludeNodeList.item(i);
            Node parent = current.getParentNode();
            parent.removeChild(current);
        }
    }

    /**
     * Generate ELM.
     *
     * @param cqlModel      the cql model
     * @param cqlLibraryDAO the cql library DAO
     * @return the save update CQL result
     */
    public static SaveUpdateCQLResult generateELM(CQLModel cqlModel, CQLLibraryDAO cqlLibraryDAO) {
        return parseQDMCQLLibraryForErrors(cqlModel, cqlLibraryDAO, null, true);
    }

    /**
     * Parses the CQL library for errors.
     *
     * @param cqlModel      the cql model
     * @param cqlLibraryDAO the cql library DAO
     * @param exprList      the expr list
     * @return the save update CQL result
     */
    public static SaveUpdateCQLResult parseQDMCQLLibraryForErrors(CQLModel cqlModel, CQLLibraryDAO cqlLibraryDAO,
                                                                  List<String> exprList) {
        return parseQDMCQLLibraryForErrors(cqlModel, cqlLibraryDAO, exprList, false);
    }

    public static CQLLinter lint(String cql, CQLLinterConfig config) {
        try {
            if (StringUtils.equals("FHIR", config.getModelIdentifier())) {
                return new FhirCQLLinter(cql, config);
            } else {
                return new QdmCQLLinter(cql, config);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Parses the CQL library for errors.
     *
     * @param cqlModel      the cql model
     * @param cqlLibraryDAO the cql library DAO
     * @param exprList      the expr list
     * @param generateELM   the generate ELM
     * @return the save update CQL result
     */
    public static SaveUpdateCQLResult parseQDMCQLLibraryForErrors(CQLModel cqlModel, CQLLibraryDAO cqlLibraryDAO,
                                                                  List<String> exprList, boolean generateELM) {

        SaveUpdateCQLResult parsedCQL = new SaveUpdateCQLResult();

        Map<String, LibHolderObject> cqlLibNameMap = new HashMap<>();

        Map<CQLIncludeLibrary, CQLModel> cqlIncludeModelMap = new HashMap<>();

        getCQLIncludeMaps(cqlModel, cqlLibNameMap, cqlIncludeModelMap, cqlLibraryDAO);

        cqlModel.setIncludedCQLLibXMLMap(cqlLibNameMap);
        cqlModel.setIncludedLibrarys(cqlIncludeModelMap);

        setIncludedCQLExpressions(cqlModel);

        validateCQLWithIncludes(cqlModel, cqlLibNameMap, parsedCQL, exprList, generateELM);

        return parsedCQL;
    }


    /**
     * Gets the CQL include lib map.
     *
     * @param cqlModel           the cql model
     * @param cqlLibNameMap      the cql lib name map
     * @param cqlIncludeModelMap
     * @param cqlLibraryDAO      the cql library DAO
     * @return the CQL include lib map
     */
    public static void getCQLIncludeMaps(CQLModel cqlModel, Map<String, LibHolderObject> cqlLibNameMap,
                                         Map<CQLIncludeLibrary, CQLModel> cqlIncludeModelMap, CQLLibraryDAO cqlLibraryDAO) {

        List<CQLIncludeLibrary> cqlIncludeLibraries = cqlModel.getCqlIncludeLibrarys();
        if (cqlIncludeLibraries == null) {
            return;
        }

        for (CQLIncludeLibrary cqlIncludeLibrary : cqlIncludeLibraries) {
            CQLModel includeCqlModel = generateCQLIncludeModelMap(cqlIncludeLibrary, cqlLibNameMap, cqlIncludeModelMap, cqlLibraryDAO);
            if (includeCqlModel == null) {
                continue;
            }
            getCQLIncludeMaps(includeCqlModel, cqlLibNameMap, includeCqlModel.getIncludedLibrarys(), cqlLibraryDAO, cqlIncludeLibrary);
        }
    }

    private static void getCQLIncludeMaps(CQLModel cqlModel, Map<String, LibHolderObject> cqlLibNameMap,
                                          Map<CQLIncludeLibrary, CQLModel> cqlIncludeModelMap, CQLLibraryDAO cqlLibraryDAO, CQLIncludeLibrary parentCQLIncludeLibrary) {
        List<CQLIncludeLibrary> cqlIncludeLibraries = cqlModel.getCqlIncludeLibrarys();
        if (cqlIncludeLibraries == null) {
            return;
        }

        for (CQLIncludeLibrary cqlIncludeLibrary : cqlIncludeLibraries) {
            cqlIncludeLibrary.setIsComponent(parentCQLIncludeLibrary.getIsComponent());
            CQLModel includeCqlModel = generateCQLIncludeModelMap(cqlIncludeLibrary, cqlLibNameMap, cqlIncludeModelMap, cqlLibraryDAO);
            if (includeCqlModel == null) {
                continue;
            }
            getCQLIncludeMaps(includeCqlModel, cqlLibNameMap, includeCqlModel.getIncludedLibrarys(), cqlLibraryDAO, cqlIncludeLibrary);
        }
    }

    private static CQLModel generateCQLIncludeModelMap(CQLIncludeLibrary cqlIncludeLibrary, Map<String, LibHolderObject> cqlLibNameMap,
                                                       Map<CQLIncludeLibrary, CQLModel> cqlIncludeModelMap, CQLLibraryDAO cqlLibraryDAO) {
        CQLLibrary cqlLibrary = cqlLibraryDAO.find(cqlIncludeLibrary.getCqlLibraryId());

        if (cqlLibrary == null) {
            logger.debug("Could not find included library:" + cqlIncludeLibrary.getAliasName());
            return null;
        }

        String includeCqlXMLString = new String(cqlLibrary.getCQLByteArray());

        CQLModel includeCqlModel = CQLUtilityClass.getCQLModelFromXML(includeCqlXMLString);
        cqlLibNameMap.put(cqlIncludeLibrary.getCqlLibraryName() + "-" + MeasureUtility.formatVersionText(cqlIncludeLibrary.getVersion()) + "|" + cqlIncludeLibrary.getAliasName(),
                new LibHolderObject(includeCqlXMLString, cqlIncludeLibrary));
        cqlIncludeModelMap.put(cqlIncludeLibrary, includeCqlModel);
        return includeCqlModel;
    }

    /**
     * Validate CQL with includes.
     *
     * @param cqlModel      the cql model
     * @param cqlLibNameMap the cql lib name map
     * @param parsedCQL     the parsed CQL
     * @param exprList      the expr list
     * @param generateELM   the generate ELM
     */
    private static void validateCQLWithIncludes(CQLModel cqlModel, Map<String, LibHolderObject> cqlLibNameMap,
                                                SaveUpdateCQLResult parsedCQL, List<String> exprList, boolean generateELM) {

        Map<String, String> libraryMap = new HashMap<>();

        // get the strings for parsing
        String parentCQLString = CQLUtilityClass.getCqlString(cqlModel, "").getLeft();
        String parentLibraryName = cqlModel.getLibraryName() + "-" + cqlModel.getVersionUsed();
        libraryMap.put(parentLibraryName, parentCQLString);
        for (String cqlLibName : cqlLibNameMap.keySet()) {
            CQLModel includedCQLModel = CQLUtilityClass.getCQLModelFromXML(cqlLibNameMap.get(cqlLibName).getMeasureXML());

            LibHolderObject libHolderObject = cqlLibNameMap.get(cqlLibName);
            String includedCQLString = CQLUtilityClass.getCqlString(includedCQLModel, "").getLeft();
            libraryMap.put(libHolderObject.getCqlLibrary().getCqlLibraryName() + "-" + libHolderObject.getCqlLibrary().getVersion(), includedCQLString);
        }

        // do the parsing
        CQLtoELM cqlToELM = new CQLtoELM(parentCQLString, libraryMap);
        cqlToELM.doTranslation(true);

        // do the filtering
        if (exprList != null) {
            filterCQLArtifacts(cqlModel, parsedCQL, cqlToELM, exprList);
        }

        // set the elm string
        if (generateELM) {
            parsedCQL.setElmString(cqlToELM.getElmString());
            parsedCQL.setJsonString(cqlToELM.getParentJsonString());
        }

        // add in the errors, if any
        List<CQLError> errors = new ArrayList<>();
        Map<String, List<CQLError>> libraryNameErrorsMap = new HashMap<>();
        Map<String, List<CQLError>> libraryNameWarningsMap = new HashMap<>();


        if (cqlToELM.getErrors().isEmpty()) {
            validateMeasurementPeriodReturnType(cqlToELM, parentLibraryName, errors, libraryNameErrorsMap);
        }

        for (CqlTranslatorException cte : cqlToELM.getErrors()) {
            setCQLErrors(parentLibraryName, errors, libraryNameErrorsMap, cte);
        }

        List<CQLError> warnings = new ArrayList<>();
        for (CqlTranslatorException cte : cqlToELM.getWarnings()) {
            setCQLErrors(parentLibraryName, warnings, libraryNameWarningsMap, cte);
        }

        parsedCQL.setCqlModel(cqlModel);
        parsedCQL.setCqlErrors(errors);
        parsedCQL.setCqlWarnings(warnings);
        parsedCQL.setLibraryNameErrorsMap(libraryNameErrorsMap);
        parsedCQL.setLibraryNameWarningsMap(libraryNameWarningsMap);
    }

    private static void validateMeasurementPeriodReturnType(CQLtoELM cqlToELM, String libraryName, List<CQLError> errors, Map<String, List<CQLError>> libraryNameErrorsMap) {
        if (!"interval<System.DateTime>".equals(cqlToELM.getExpressionReturnType("Measurement Period"))) {
            TrackBack trackback = cqlToELM.getTrackBackMap().get("Measurement Period");
            if (trackback != null) {
                CQLError error = new CQLError();
                error.setStartErrorInLine(trackback.getStartLine());
                error.setErrorInLine(trackback.getStartLine());
                error.setErrorAtOffset(trackback.getStartChar());
                error.setEndErrorInLine(trackback.getEndLine());
                error.setEndErrorAtOffset(trackback.getEndChar());
                error.setErrorMessage("A Parameter titled \"Measurement Period\" must return an Interval<DateTime>");
                error.setSeverity(CqlTranslatorException.ErrorSeverity.Error.toString());
                libraryNameErrorsMap.put(libraryName, Arrays.asList(error));
                errors.add(error);
            }
        }
    }

    private static void setCQLErrors(String parentLibraryName, List<CQLError> errors, Map<String, List<CQLError>> libraryToErrorsMap, CqlTranslatorException cte) {
        CQLError cqlError = new CQLError();

        String libraryName = "";
        if (cte != null && cte.getLocator() != null) {
            cqlError.setStartErrorInLine(cte.getLocator().getStartLine());
            cqlError.setErrorInLine(cte.getLocator().getStartLine());
            cqlError.setErrorAtOffset(cte.getLocator().getStartChar());
            cqlError.setEndErrorInLine(cte.getLocator().getEndLine());
            cqlError.setEndErrorAtOffset(cte.getLocator().getEndChar());

            if (cte.getLocator().getLibrary() != null && !"unknown".equals(cte.getLocator().getLibrary().getId())) {
                libraryName = cte.getLocator().getLibrary().getId() + "-" + cte.getLocator().getLibrary().getVersion();
            } else {
                libraryName = parentLibraryName;
            }

        }

        cqlError.setErrorMessage(cte.getMessage());
        cqlError.setSeverity(cte.getSeverity().toString());

        libraryToErrorsMap.computeIfAbsent(libraryName, k -> new ArrayList<>());
        libraryToErrorsMap.get(libraryName).add(cqlError);
        errors.add(cqlError);
    }

    /**
     * Filter CQL artifacts.
     *
     * @param cqlModel  the cql model
     * @param parsedCQL the parsed CQL
     * @param cqlToElm  the cql to elm
     * @param exprList  the expr list
     */

    public static void filterCQLArtifacts(CQLModel cqlModel, SaveUpdateCQLResult parsedCQL, CQLtoELM cqlToElm, List<String> exprList) {
        CQLObject cqlObject = new CQLObject();
        if (cqlToElm != null && cqlToElm.getErrors().isEmpty()) {
            String parentLibraryString = cqlToElm.getParentCQLLibraryString();
            // expression List => population/sde/rav defines and functions
            MATCQLFilter cqlFilter = new MATCQLFilter(parentLibraryString, cqlToElm.getCqlLibraryMapping(), exprList, cqlToElm.getTranslator(), cqlToElm.getTranslatedLibraries());

            try {
                cqlFilter.filter();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (CQLDefinition definition : cqlModel.getDefinitionList()) {
                String definitionName = definition.getName();
                CQLExpressionObject expression = new CQLExpressionObject("Definition", definitionName);
                expression.setReturnType(cqlToElm.getExpression(definitionName).getResultType().toString());
                expression.setCodeDataTypeMap(mapSetValueToListValue(cqlFilter.getExpressionNameToCodeDataTypeMap().get(definitionName)));
                expression.setValueSetDataTypeMap(mapSetValueToListValue(cqlFilter.getExpressionNameToValuesetDataTypeMap().get(definitionName)));
                cqlObject.getCqlDefinitionObjectList().add(expression);
            }

            for (CQLFunctions function : cqlModel.getCqlFunctions()) {
                String functionName = function.getName();
                FunctionDef functionDef = (FunctionDef) cqlToElm.getExpression(functionName);

                CQLExpressionObject expression = new CQLExpressionObject("Function", functionName);
                expression.setReturnType(cqlToElm.getExpressionReturnType(functionName));
                expression.setCodeDataTypeMap(mapSetValueToListValue(cqlFilter.getExpressionNameToCodeDataTypeMap().get(functionName)));
                expression.setValueSetDataTypeMap(mapSetValueToListValue(cqlFilter.getExpressionNameToValuesetDataTypeMap().get(functionName)));

                for (OperandDef expressionDef : functionDef.getOperand()) {
                    CQLExpressionOprandObject cqlExpressionOprandObject = new CQLExpressionOprandObject();
                    cqlExpressionOprandObject.setName(expressionDef.getName());
                    cqlExpressionOprandObject.setReturnType(expressionDef.getResultType().toString());
                    expression.getOprandList().add(cqlExpressionOprandObject);
                }

                cqlObject.getCqlFunctionObjectList().add(expression);
            }

            for (CQLParameter parameter : cqlModel.getCqlParameters()) {
                String parameterName = parameter.getName();
                CQLExpressionObject expression = new CQLExpressionObject("Parameter", parameterName);
                cqlObject.getCqlParameterObjectList().add(expression);
            }

            GetUsedCQLArtifactsResult usedArtifacts = new GetUsedCQLArtifactsResult();
            usedArtifacts.setUsedCQLcodes(cqlFilter.getUsedCodes());
            usedArtifacts.setUsedCQLcodeSystems(cqlFilter.getUsedCodeSystems());
            usedArtifacts.setUsedCQLDefinitions(cqlFilter.getUsedDefinitions());
            usedArtifacts.setUsedCQLFunctions(cqlFilter.getUsedFunctions());
            usedArtifacts.setUsedCQLParameters(cqlFilter.getUsedParameters());
            usedArtifacts.setUsedCQLValueSets(cqlFilter.getUsedValuesets());
            usedArtifacts.setUsedCQLLibraries(cqlFilter.getUsedLibraries());
            usedArtifacts.setValueSetDataTypeMap(cqlFilter.getValuesetDataTypeMap());
            usedArtifacts.setCodeDataTypeMap(cqlFilter.getCodeDataTypeMap());
            usedArtifacts.setExpressionNameToValuesetDataTypeMap(cqlFilter.getExpressionNameToValuesetDataTypeMap());
            usedArtifacts.setExpressionNameToCodeDataTypeMap(cqlFilter.getExpressionNameToCodeDataTypeMap());

            Map<String, CQLIncludeLibrary> includedLibraries = new HashMap<>();

            for (String library : cqlFilter.getUsedLibraries()) {
                includedLibraries.put(library, cqlModel.getIncludedCQLLibXMLMap().get(library).getCqlLibrary());
            }

            usedArtifacts.setIncludeLibMap(includedLibraries);
            usedArtifacts.setNameToReturnTypeMap(cqlFilter.getAllNamesToReturnTypeMap());
            usedArtifacts.setExpressionToReturnTypeMap(cqlFilter.getExpressionToReturnTypeMap());
            parsedCQL.setUsedCQLArtifacts(usedArtifacts);
        }

        parsedCQL.setCqlObject(cqlObject);
    }

    public static Map<String, List<String>> mapSetValueToListValue(Map<String, Set<String>> mapWithSet) {
        Map<String, List<String>> mapWithList = new HashMap<>();
        if (mapWithSet == null) {
            return mapWithList;
        }

        for (Entry<String, Set<String>> entry : mapWithSet.entrySet()) {
            List<String> newList = new ArrayList<>(entry.getValue());
            mapWithList.put(entry.getKey(), newList);
        }

        return mapWithList;
    }

    /**
     * This method will extract all the CQL expressions (Definitions, Functions, ValueSets, Parameters) from included
     * child (only child, not grand child) CQL Libs and set them inside CQLModel object.
     * <p>
     * This extracted list will then be used by UI CQL Workspace.
     *
     * @param cqlModel the new included CQL expressions
     */
    public static void setIncludedCQLExpressions(CQLModel cqlModel) {

        List<CQLIncludeLibrary> cqlIncludeLibraries = cqlModel.getCqlIncludeLibrarys();
        if (cqlIncludeLibraries == null) {
            return;
        }

        for (CQLIncludeLibrary cqlIncludeLibrary : cqlIncludeLibraries) {

            LibHolderObject libHolderObject = cqlModel.getIncludedCQLLibXMLMap().get(cqlIncludeLibrary.getCqlLibraryName() + "-" + cqlIncludeLibrary.getVersion() + "|" + cqlIncludeLibrary.getAliasName());
            if (libHolderObject != null) {
                cqlIncludeLibrary.getAliasName();
                libHolderObject.getMeasureXML();
            }
        }
    }

    public class CQLArtifactHolder {

        private Set<String> cqlDefinitionUUIDSet = new HashSet<>();

        /**
         * The cql function UUID set.
         */
        private Set<String> cqlFunctionUUIDSet = new HashSet<>();

        /**
         * The cql valueset identifier set.
         */
        private Set<String> cqlValuesetIdentifierSet = new HashSet<>();

        /**
         * The cql parameter identifier set.
         */
        private Set<String> cqlParameterIdentifierSet = new HashSet<>();

        /**
         * The cql codes set.
         */
        private Set<String> cqlCodesSet = new HashSet<>();

        /**
         * The cql def from pop set.
         */
        private Set<String> cqlDefFromPopSet = new HashSet<>();

        /**
         * The cql func from pop set.
         */
        private Set<String> cqlFuncFromPopSet = new HashSet<>();

        /**
         * Gets the cql definition UUID set.
         *
         * @return the cql definition UUID set
         */
        public Set<String> getCqlDefinitionUUIDSet() {
            return cqlDefinitionUUIDSet;
        }

        /**
         * Gets the cql function UUID set.
         *
         * @return the cql function UUID set
         */
        public Set<String> getCqlFunctionUUIDSet() {
            return cqlFunctionUUIDSet;
        }

        /**
         * Gets the cql valueset identifier set.
         *
         * @return the cql valueset identifier set
         */
        public Set<String> getCqlValuesetIdentifierSet() {
            return cqlValuesetIdentifierSet;
        }

        /**
         * Gets the cql parameter identifier set.
         *
         * @return the cql parameter identifier set
         */
        public Set<String> getCqlParameterIdentifierSet() {
            return cqlParameterIdentifierSet;
        }

        /**
         * Adds the definition UUID.
         *
         * @param uuid the uuid
         */
        public void addDefinitionUUID(String uuid) {
            cqlDefinitionUUIDSet.add(uuid);
        }

        /**
         * Adds the function UUID.
         *
         * @param uuid the uuid
         */
        public void addFunctionUUID(String uuid) {
            cqlFunctionUUIDSet.add(uuid);
        }

        /**
         * Adds the valueset identifier.
         *
         * @param identifier the identifier
         */
        public void addValuesetIdentifier(String identifier) {
            cqlValuesetIdentifierSet.add(identifier);
        }

        /**
         * Adds the parameter identifier.
         *
         * @param identifier the identifier
         */
        public void addParameterIdentifier(String identifier) {
            cqlParameterIdentifierSet.add(identifier);
        }

        /**
         * Adds the definition identifier.
         *
         * @param identifier the identifier
         */
        public void addDefinitionIdentifier(String identifier) {
            cqlDefFromPopSet.add(identifier);
        }

        /**
         * Adds the function identifier.
         *
         * @param identifier the identifier
         */
        public void addFunctionIdentifier(String identifier) {
            cqlFuncFromPopSet.add(identifier);
        }

        /**
         * Sets the cql definition UUID set.
         *
         * @param cqlDefinitionUUIDSet the new cql definition UUID set
         */
        public void setCqlDefinitionUUIDSet(Set<String> cqlDefinitionUUIDSet) {
            this.cqlDefinitionUUIDSet = cqlDefinitionUUIDSet;
        }

        /**
         * Sets the cql function UUID set.
         *
         * @param cqlFunctionUUIDSet the new cql function UUID set
         */
        public void setCqlFunctionUUIDSet(Set<String> cqlFunctionUUIDSet) {
            this.cqlFunctionUUIDSet = cqlFunctionUUIDSet;
        }

        /**
         * Sets the cql valueset identifier set.
         *
         * @param cqlValuesetIdentifierSet the new cql valueset identifier set
         */
        public void setCqlValuesetIdentifierSet(Set<String> cqlValuesetIdentifierSet) {
            this.cqlValuesetIdentifierSet = cqlValuesetIdentifierSet;
        }

        /**
         * Sets the cql parameter identifier set.
         *
         * @param cqlParameterIdentifierSet the new cql parameter identifier set
         */
        public void setCqlParameterIdentifierSet(Set<String> cqlParameterIdentifierSet) {
            this.cqlParameterIdentifierSet = cqlParameterIdentifierSet;
        }

        /**
         * Gets the cql codes set.
         *
         * @return the cql codes set
         */
        public Set<String> getCqlCodesSet() {
            return cqlCodesSet;
        }

        /**
         * Sets the cql codes set.
         *
         * @param cqlCodesSet the new cql codes set
         */
        public void setCqlCodesSet(Set<String> cqlCodesSet) {
            this.cqlCodesSet = cqlCodesSet;
        }

        /**
         * Adds the CQL code.
         *
         * @param identifier the identifier
         */
        public void addCQLCode(String identifier) {
            this.cqlCodesSet.add(identifier);
        }

        /**
         * Gets the cql def from pop set.
         *
         * @return the cql def from pop set
         */
        public Set<String> getCqlDefFromPopSet() {
            return cqlDefFromPopSet;
        }

        /**
         * Sets the cql def from pop set.
         *
         * @param cqlDefFromPopSet the new cql def from pop set
         */
        public void setCqlDefFromPopSet(Set<String> cqlDefFromPopSet) {
            this.cqlDefFromPopSet = cqlDefFromPopSet;
        }

        /**
         * Gets the cql func from pop set.
         *
         * @return the cql func from pop set
         */
        public Set<String> getCqlFuncFromPopSet() {
            return cqlFuncFromPopSet;
        }

        /**
         * Sets the cql func from pop set.
         *
         * @param cqlFuncFromPopSet the new cql func from pop set
         */
        public void setCqlFuncFromPopSet(Set<String> cqlFuncFromPopSet) {
            this.cqlFuncFromPopSet = cqlFuncFromPopSet;
        }
    }

    /**
     * Adds the used CQL libsto simple XML.
     *
     * @param originalDoc   the original doc
     * @param includeLibMap the include lib map
     * @param cqlLibraryDAO
     */
    public static void addUsedCQLLibstoSimpleXML(Document originalDoc, Map<String, CQLIncludeLibrary> includeLibMap, CQLLibraryDAO cqlLibraryDAO) {
        Node allUsedLibsNode = originalDoc.createElement("allUsedCQLLibs");
        originalDoc.getFirstChild().appendChild(allUsedLibsNode);

        for (String libName : includeLibMap.keySet()) {
            CQLIncludeLibrary cqlLibrary = includeLibMap.get(libName);

            Element libNode = originalDoc.createElement("lib");
            libNode.setAttribute("id", cqlLibrary.getCqlLibraryId());
            libNode.setAttribute("alias", cqlLibrary.getAliasName());
            libNode.setAttribute("name", cqlLibrary.getCqlLibraryName());
            libNode.setAttribute("version", cqlLibrary.getVersion());
            libNode.setAttribute("setId", cqlLibraryDAO.find(cqlLibrary.getCqlLibraryId()).getSetId());
            libNode.setAttribute("isUnUsedGrandChild", "false");
            //Marking if the library is a component measure
            libNode.setAttribute("isComponent", cqlLibrary.getIsComponent());

            allUsedLibsNode.appendChild(libNode);
        }
    }

    /**
     * Adds the unused grand children to simple XML.
     *
     * @param originalDoc the original doc
     * @param result      the result
     * @param cqlModel    the cql model
     * @throws XPathExpressionException the x path expression exception
     */
    public static void addUnUsedGrandChildrentoSimpleXML(Document originalDoc, SaveUpdateCQLResult result, CQLModel cqlModel, CQLLibraryDAO cqlLibraryDAO) throws XPathExpressionException {

        String allUsedCQLLibsXPath = "//allUsedCQLLibs";

        XmlProcessor xmlProcessor = new XmlProcessor("<test></test>");
        xmlProcessor.setOriginalDoc(originalDoc);

        Node allUsedLibsNode = xmlProcessor.findNode(originalDoc, allUsedCQLLibsXPath);

        if (allUsedLibsNode != null) {

            List<CQLIncludeLibrary> cqlChildLibs = cqlModel.getCqlIncludeLibrarys();

            for (CQLIncludeLibrary library : cqlChildLibs) {
                String libAlias = library.getAliasName();
                String libName = library.getCqlLibraryName();
                String libVersion = library.getVersion();

                Collection<CQLIncludeLibrary> childs = result.getUsedCQLArtifacts().getIncludeLibMap().values();
                boolean found = childs.contains(library);

                if (found) {
                    LibHolderObject libHolderObject = cqlModel.getIncludedCQLLibXMLMap().
                            get(libName + "-" + libVersion + "|" + libAlias);

                    if (libHolderObject != null) {
                        String xml = libHolderObject.getMeasureXML();
                        CQLModel childCQLModel = CQLUtilityClass.getCQLModelFromXML(xml);

                        List<CQLIncludeLibrary> cqlGrandChildLibs = childCQLModel.getCqlIncludeLibrarys();

                        for (CQLIncludeLibrary grandChildLib : cqlGrandChildLibs) {

                            boolean gcFound = childs.contains(grandChildLib);

                            if (!gcFound) {
                                Element libNode = originalDoc.createElement("lib");
                                libNode.setAttribute("id", grandChildLib.getCqlLibraryId());
                                libNode.setAttribute("alias", grandChildLib.getAliasName());
                                libNode.setAttribute("name", grandChildLib.getCqlLibraryName());
                                libNode.setAttribute("version", grandChildLib.getVersion());
                                libNode.setAttribute("setId", cqlLibraryDAO.find(grandChildLib.getCqlLibraryId()).getSetId());
                                libNode.setAttribute("isUnUsedGrandChild", "true");
                                //Marking if the library is part of a component measure
                                libNode.setAttribute("isComponent", library.getIsComponent());
                                allUsedLibsNode.appendChild(libNode);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Gets the included CQL expressions.
     *
     * @param cqlModel      the cql model
     * @param cqlLibraryDAO the cql library DAO
     * @return the included CQL expressions
     */
    public static void getIncludedCQLExpressions(CQLModel cqlModel, CQLLibraryDAO cqlLibraryDAO) {

        Map<String, LibHolderObject> cqlLibNameMap = new HashMap<>();
        Map<CQLIncludeLibrary, CQLModel> cqlIncludeModelMap = new HashMap<>();
        getCQLIncludeMaps(cqlModel, cqlLibNameMap, cqlIncludeModelMap, cqlLibraryDAO);
        cqlModel.setIncludedCQLLibXMLMap(cqlLibNameMap);
        cqlModel.setIncludedLibrarys(cqlIncludeModelMap);
        setIncludedCQLExpressions(cqlModel);
    }
}

