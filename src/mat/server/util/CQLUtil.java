package mat.server.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.dao.clause.CQLLibraryDAO;
import mat.model.clause.CQLLibrary;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.parser.CQLBaseStatementInterface;
import mat.model.cql.parser.CQLCodeModelObject;
import mat.model.cql.parser.CQLDefinitionModelObject;
import mat.model.cql.parser.CQLFileObject;
import mat.model.cql.parser.CQLFunctionModelObject;
import mat.model.cql.parser.CQLParameterModelObject;
import mat.model.cql.parser.CQLValueSetModelObject;
import mat.server.CQLUtilityClass;
import mat.server.cqlparser.CQLFilter;
import mat.shared.CQLErrors;
import mat.shared.CQLObject;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.LibHolderObject;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.UUIDUtilClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cqframework.cql.cql2elm.CQLtoELM;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CQLUtil {

	/** The Constant xPath. */
	static final javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	private static final Log logger = LogFactory.getLog(CQLUtil.class);

	public static CQLArtifactHolder getUsedCQLArtifacts(Document originalDoc, CQLFileObject cqlFileObject)
			throws XPathExpressionException {

		CQLArtifactHolder cqlArtifactHolder = CQLUtil.getCQLArtifactsReferredByPoplns(originalDoc);
		CQLUtil cqlUtil = new CQLUtil();
		CQLArtifactHolder usedCQLArtifactHolder = cqlUtil.new CQLArtifactHolder();

		usedCQLArtifactHolder.getCqlDefinitionUUIDSet().addAll(cqlArtifactHolder.getCqlDefinitionUUIDSet());
		usedCQLArtifactHolder.getCqlFunctionUUIDSet().addAll(cqlArtifactHolder.getCqlFunctionUUIDSet());

		for (String cqlDefnUUID : cqlArtifactHolder.getCqlDefinitionUUIDSet()) {
			String xPathCQLDef = "//cqlLookUp/definitions/definition[@id='" + cqlDefnUUID + "']";
			Node cqlDefinition = (Node) xPath.evaluate(xPathCQLDef, originalDoc.getDocumentElement(),
					XPathConstants.NODE);

			String cqlDefnName = "\"" + cqlDefinition.getAttributes().getNamedItem("name").getNodeValue() + "\"";
			CQLDefinitionModelObject cqlDefinitionModelObject = cqlFileObject.getDefinitionsMap().get(cqlDefnName);

			CQLUtil.collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlDefinitionModelObject,
					usedCQLArtifactHolder);
		}

		for (String cqlFuncUUID : cqlArtifactHolder.getCqlFunctionUUIDSet()) {
			String xPathCQLDef = "//cqlLookUp/functions/function[@id='" + cqlFuncUUID + "']";
			Node cqlFunction = (Node) xPath.evaluate(xPathCQLDef, originalDoc.getDocumentElement(),
					XPathConstants.NODE);

			String cqlFuncName = "\"" + cqlFunction.getAttributes().getNamedItem("name").getNodeValue() + "\"";
			CQLFunctionModelObject cqlFunctionModelObject = cqlFileObject.getFunctionsMap().get(cqlFuncName);

			CQLUtil.collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlFunctionModelObject, usedCQLArtifactHolder);
		}

		return usedCQLArtifactHolder;
	}

	public static CQLArtifactHolder getViewCQLUsedCQLArtifacts(Document originalDoc, CQLFileObject cqlFileObject)
			throws XPathExpressionException {

		CQLArtifactHolder cqlArtifactHolder = CQLUtil.getCQLArtifactsReferredByViewCQL(originalDoc, cqlFileObject);
		CQLUtil cqlUtil = new CQLUtil();
		CQLArtifactHolder usedCQLArtifactHolder = cqlUtil.new CQLArtifactHolder();

		usedCQLArtifactHolder.getCqlParameterIdentifierSet().addAll(cqlArtifactHolder.getCqlParameterIdentifierSet());
		usedCQLArtifactHolder.getCqlDefinitionUUIDSet().addAll(cqlArtifactHolder.getCqlDefinitionUUIDSet());
		usedCQLArtifactHolder.getCqlFunctionUUIDSet().addAll(cqlArtifactHolder.getCqlFunctionUUIDSet());

		for (String cqlParamUUID : cqlArtifactHolder.getCqlParameterIdentifierSet()) {
			String xPathCQLParam = "//cqlLookUp/definitions/parameter[@id='" + cqlParamUUID + "']";
			Node cqlParam = (Node) xPath.evaluate(xPathCQLParam, originalDoc.getDocumentElement(), XPathConstants.NODE);

			String cqlParamName = "\"" + cqlParam.getAttributes().getNamedItem("name").getNodeValue() + "\"";
			CQLParameterModelObject cqlParameterModelObject = cqlFileObject.getParametersMap().get(cqlParamName);

			CQLUtil.collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlParameterModelObject, usedCQLArtifactHolder);
		}

		for (String cqlDefnUUID : cqlArtifactHolder.getCqlDefinitionUUIDSet()) {
			String xPathCQLDef = "//cqlLookUp/definitions/definition[@id='" + cqlDefnUUID + "']";
			Node cqlDefinition = (Node) xPath.evaluate(xPathCQLDef, originalDoc.getDocumentElement(),
					XPathConstants.NODE);

			String cqlDefnName = "\"" + cqlDefinition.getAttributes().getNamedItem("name").getNodeValue() + "\"";
			CQLDefinitionModelObject cqlDefinitionModelObject = cqlFileObject.getDefinitionsMap().get(cqlDefnName);

			CQLUtil.collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlDefinitionModelObject,
					usedCQLArtifactHolder);
		}

		for (String cqlFuncUUID : cqlArtifactHolder.getCqlFunctionUUIDSet()) {
			String xPathCQLDef = "//cqlLookUp/functions/function[@id='" + cqlFuncUUID + "']";
			Node cqlFunction = (Node) xPath.evaluate(xPathCQLDef, originalDoc.getDocumentElement(),
					XPathConstants.NODE);

			String cqlFuncName = "\"" + cqlFunction.getAttributes().getNamedItem("name").getNodeValue() + "\"";
			CQLFunctionModelObject cqlFunctionModelObject = cqlFileObject.getFunctionsMap().get(cqlFuncName);

			CQLUtil.collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlFunctionModelObject, usedCQLArtifactHolder);
		}

		return usedCQLArtifactHolder;
	}

	/**
	 * Loop through all of the cqlDefinitionsModels in the list and get all
	 * referred to definitions and functions. On each definition and function,
	 * recursively find all of the referred to definitions and functions for
	 * them.
	 * 
	 * @param originalDoc
	 * @param cqlFileObject
	 * @param cqlBaseStatementInterface
	 * @param usedCQLArtifactHolder
	 * @throws XPathExpressionException
	 */
	public static void collectUsedCQLArtifacts(Document originalDoc, CQLFileObject cqlFileObject,
			CQLBaseStatementInterface cqlBaseStatementInterface, CQLArtifactHolder usedCQLArtifactHolder)
			throws XPathExpressionException {

		// get valuesets used by current artifact
		List<CQLValueSetModelObject> cqlValueSetModelObjects = cqlBaseStatementInterface.getReferredToValueSets();
		for (CQLValueSetModelObject cqlValueSetModelObject : cqlValueSetModelObjects) {
			usedCQLArtifactHolder.addValuesetIdentifier(cqlValueSetModelObject.getIdentifier().replace("\"", ""));
		}

		// get codes used by currect artifact
		List<CQLCodeModelObject> cqlCodeModelObjects = cqlBaseStatementInterface.getReferredToCodes();
		for (CQLCodeModelObject cqlCodeModelObject : cqlCodeModelObjects) {
			usedCQLArtifactHolder.addCQLCode(cqlCodeModelObject.getCodeIdentifier().replace("\"", ""));
		}

		// get parameters used by current artifact
		List<CQLParameterModelObject> cqlParameterModelObjects = cqlBaseStatementInterface.getReferredToParameters();
		for (CQLParameterModelObject cqlParameterModelObject : cqlParameterModelObjects) {
			usedCQLArtifactHolder.addParameterIdentifier(cqlParameterModelObject.getIdentifier().replace("\"", ""));
		}

		// get all definitions which current artifact refer to, recursively find
		// all artifacts which could be nested.
		List<CQLDefinitionModelObject> cqlDefinitionModelObjectList = cqlBaseStatementInterface
				.getReferredToDefinitions();
		for (CQLDefinitionModelObject cqlDefinitionModelObject : cqlDefinitionModelObjectList) {
			usedCQLArtifactHolder
					.addDefinitionUUID(CQLUtil.getCQLDefinitionUUID(originalDoc, cqlDefinitionModelObject, true));

			collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlDefinitionModelObject, usedCQLArtifactHolder);

		}

		// get all functions which current artifact refer to, recursively find
		// all artifacts which could be nested.
		List<CQLFunctionModelObject> cqlFunctionModelObjectList = cqlBaseStatementInterface.getReferredToFunctions();
		for (CQLFunctionModelObject cqlFunctionModelObject : cqlFunctionModelObjectList) {
			usedCQLArtifactHolder
					.addFunctionUUID(CQLUtil.getCQLDefinitionUUID(originalDoc, cqlFunctionModelObject, false));

			collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlFunctionModelObject, usedCQLArtifactHolder);
		}
	}

	/**
	 * Given a CqlDefinitionModelObject, find out the <definition> node and
	 * return its "uuid" attribute value.
	 * 
	 * @param originalDoc
	 * @param cqlModelObject
	 * @return
	 * @throws XPathExpressionException
	 */
	public static String getCQLDefinitionUUID(Document originalDoc, CQLBaseStatementInterface cqlModelObject,
			boolean isDefinition) throws XPathExpressionException {

		System.out.println("cql defn name bfor:" + cqlModelObject.getIdentifier());
		String defnName = cqlModelObject.getIdentifier().replaceAll("\"", "");
		System.out.println("cql defn name aftr:" + defnName);

		String cqlArtifactName = "definitions//definition";
		if (!isDefinition) {
			cqlArtifactName = "functions//function";
		}

		String xPathForDefinitions = "//cqlLookUp/" + cqlArtifactName + "[@name='" + defnName + "']/@id";
		System.out.println(xPathForDefinitions);
		Node cqlDefinitionUUID = (Node) xPath.evaluate(xPathForDefinitions, originalDoc.getDocumentElement(),
				XPathConstants.NODE);
		return cqlDefinitionUUID.getNodeValue();
	}

	/**
	 * This method will find out all the CQLDefinitions referred to by
	 * populations defined in a measure.
	 * 
	 * @param originalDoc
	 * @param cqlFileObject
	 * @return
	 */
	public static CQLArtifactHolder getCQLArtifactsReferredByPoplns(Document originalDoc) {
		CQLUtil cqlUtil = new CQLUtil();
		CQLUtil.CQLArtifactHolder cqlArtifactHolder = cqlUtil.new CQLArtifactHolder();

		String xPathForDefinitions = "//cqldefinition";
		String xPathForFunctions = "//cqlfunction";

		try {
			NodeList cqlDefinitions = (NodeList) xPath.evaluate(xPathForDefinitions, originalDoc.getDocumentElement(),
					XPathConstants.NODESET);

			for (int i = 0; i < cqlDefinitions.getLength(); i++) {
				String uuid = cqlDefinitions.item(i).getAttributes().getNamedItem("uuid").getNodeValue();
				String name = cqlDefinitions.item(i).getAttributes().getNamedItem("displayName").getNodeValue();

				cqlArtifactHolder.addDefinitionUUID(uuid);
				cqlArtifactHolder.addDefinitionIdentifier(name.replaceAll("\"", ""));
			}

			NodeList cqlFunctions = (NodeList) xPath.evaluate(xPathForFunctions, originalDoc.getDocumentElement(),
					XPathConstants.NODESET);

			for (int i = 0; i < cqlFunctions.getLength(); i++) {
				String uuid = cqlFunctions.item(i).getAttributes().getNamedItem("uuid").getNodeValue();
				String name = cqlFunctions.item(i).getAttributes().getNamedItem("displayName").getNodeValue();

				cqlArtifactHolder.addFunctionUUID(uuid);
				cqlArtifactHolder.addFunctionIdentifier(name.replaceAll("\"", ""));
			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cqlArtifactHolder;
	}

	/**
	 * This method will find out all the CQLDefinitions/Functions/Parameters
	 * referred in viewCQL.
	 * 
	 * @param originalDoc
	 * @param cqlFileObject
	 * @return
	 */
	private static CQLArtifactHolder getCQLArtifactsReferredByViewCQL(Document originalDoc,
			CQLFileObject cqlFileObject) {
		CQLUtil cqlUtil = new CQLUtil();
		CQLUtil.CQLArtifactHolder cqlArtifactHolder = cqlUtil.new CQLArtifactHolder();

		return cqlArtifactHolder;
	}

	public static CQLArtifactHolder getUsedCQLValuesets(CQLFileObject cqlFileObject) throws XPathExpressionException {

		CQLUtil cqlUtil = new CQLUtil();
		CQLArtifactHolder usedCQLArtifactHolder = cqlUtil.new CQLArtifactHolder();

		Map<String, CQLDefinitionModelObject> defnMap = cqlFileObject.getDefinitionsMap();
		Collection<CQLDefinitionModelObject> cqlDefinitionModelObjects = defnMap.values();

		for (CQLDefinitionModelObject cqlDefinitionModelObject : cqlDefinitionModelObjects) {

			// get valuesets used by current artifact
			List<CQLValueSetModelObject> cqlValueSetModelObjects = cqlDefinitionModelObject.getReferredToValueSets();
			for (CQLValueSetModelObject cqlValueSetModelObject : cqlValueSetModelObjects) {
				usedCQLArtifactHolder.addValuesetIdentifier(cqlValueSetModelObject.getIdentifier().replace("\"", ""));
			}
		}

		Map<String, CQLFunctionModelObject> funcMap = cqlFileObject.getFunctionsMap();
		Collection<CQLFunctionModelObject> cqlFunctionModelObjects = funcMap.values();

		for (CQLFunctionModelObject cqlFunctionModelObject : cqlFunctionModelObjects) {
			// get valuesets used by current artifact
			List<CQLValueSetModelObject> cqlValueSetModelObjects = cqlFunctionModelObject.getReferredToValueSets();
			for (CQLValueSetModelObject cqlValueSetModelObject : cqlValueSetModelObjects) {
				usedCQLArtifactHolder.addValuesetIdentifier(cqlValueSetModelObject.getIdentifier().replace("\"", ""));
			}
		}

		Map<String, CQLParameterModelObject> paramMap = cqlFileObject.getParametersMap();
		Collection<CQLParameterModelObject> cqlParameterModelObjects = paramMap.values();

		for (CQLParameterModelObject cqlParameterModelObject : cqlParameterModelObjects) {
			// get valuesets used by current artifact
			List<CQLValueSetModelObject> cqlValueSetModelObjects = cqlParameterModelObject.getReferredToValueSets();
			for (CQLValueSetModelObject cqlValueSetModelObject : cqlValueSetModelObjects) {
				usedCQLArtifactHolder.addValuesetIdentifier(cqlValueSetModelObject.getIdentifier().replace("\"", ""));
			}
		}

		return usedCQLArtifactHolder;
	}

	/**
	 * Removes all unused cql definitions from the simple xml file. Iterates
	 * through the usedcqldefinitions set, adds them to the xpath string, and
	 * then removes all nodes that are not a part of the xpath string.
	 * 
	 * @param originalDoc
	 *            the simple xml document
	 * @param usedDefinitionsList
	 *            the usedcqldefinitions
	 * @throws XPathExpressionException
	 */
	public static void removeUnusedCQLDefinitions(Document originalDoc, List<String> usedDefinitionsList)
			throws XPathExpressionException {

		String idXPathString = "";
		for (String name : usedDefinitionsList) {
			idXPathString += "[@name !='" + name + "']";
		}

		String xPathForUnusedDefinitions = "//cqlLookUp//definition" + idXPathString;
		System.out.println(xPathForUnusedDefinitions);

		NodeList unusedCQLDefNodeList = (NodeList) xPath.evaluate(xPathForUnusedDefinitions,
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
	 * @param originalDoc
	 *            the simple xml document
	 * @param list
	 *            the usedcqlfunctions
	 * @throws XPathExpressionException
	 */
	public static void removeUnusedCQLFunctions(Document originalDoc, List<String> usedFunctionsList)
			throws XPathExpressionException {
		String idXPathString = "";
		for (String name : usedFunctionsList) {
			idXPathString += "[@name !='" + name + "']";
		}

		String xPathForUnusedFunctions = "//cqlLookUp//function" + idXPathString;
		System.out.println(xPathForUnusedFunctions);

		NodeList unusedCqlFuncNodeList = (NodeList) xPath.evaluate(xPathForUnusedFunctions,
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
	 * @param originalDoc
	 *            the simple xml document
	 * @param cqlValuesetIdentifierSet
	 *            the usevaluesets
	 * @throws XPathExpressionException
	 */
	public static void removeUnusedValuesets(Document originalDoc, Set<String> cqlValuesetIdentifierSet)
			throws XPathExpressionException {
		String nameXPathString = "";
		Set<String> cqlCodes = new HashSet<String>();

		for (String name : cqlValuesetIdentifierSet) {
			if (name.equals("Birthdate") || name.equals("Dead")) {
				cqlCodes.add(name);
			} else {
				nameXPathString += "[@name !='" + name + "']";
			}
		}	
			
		String xPathForUnusedValuesets = "//cqlLookUp//valueset" + nameXPathString;
		System.out.println(xPathForUnusedValuesets);

		NodeList unusedCqlValuesetNodeList = (NodeList) xPath.evaluate(xPathForUnusedValuesets,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		
		for (int i = 0; i < unusedCqlValuesetNodeList.getLength(); i++) {
			Node current = unusedCqlValuesetNodeList.item(i);
			Node parent = current.getParentNode();
			parent.removeChild(current);
		}
		
		removeUnusedCodes(originalDoc, cqlCodes);
		removeUnsedCodeSystems(originalDoc);
	}

	/**
	 * Removes all unused cql codes from the simple xml file. Iterates through
	 * the usedcodes set, adds them to the xpath string, and then removes all
	 * nodes that are not a part of the xpath string.
	 * 
	 * @param originalDoc
	 *            the simple xml document
	 * @param cqlCodesIdentifierSet
	 *            the usevaluesets
	 * @throws XPathExpressionException
	 */
	public static void removeUnusedCodes(Document originalDoc, Set<String> cqlCodesIdentifierSet)
			throws XPathExpressionException {
		String nameXPathString = "";
		for (String codeName : cqlCodesIdentifierSet) {
			nameXPathString += "[@codeName !='" + codeName + "']";
		}

		String xPathForUnusedCodes = "//cqlLookUp//code" + nameXPathString;
		System.out.println(xPathForUnusedCodes);

		NodeList unusedCqlCodesNodeList = (NodeList) xPath.evaluate(xPathForUnusedCodes,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		for (int i = 0; i < unusedCqlCodesNodeList.getLength(); i++) {
			Node current = unusedCqlCodesNodeList.item(i);
			Node parent = current.getParentNode();
			parent.removeChild(current);
		}
	}

	/**
	 * Removes the unsed code systems.
	 *
	 * @param originalDoc
	 *            the original doc
	 * @throws XPathExpressionException
	 */
	private static void removeUnsedCodeSystems(Document originalDoc) throws XPathExpressionException {

		// find all used codeSystemNames
		String xPathForCodesystemNames = "//cqlLookUp/codes/code/@codeSystemName";
		NodeList codeSystemNameList = (NodeList) xPath.evaluate(xPathForCodesystemNames,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);

		String nameXPathString = "";
		for (int i = 0; i < codeSystemNameList.getLength(); i++) {
			String name = codeSystemNameList.item(i).getNodeValue();
			nameXPathString += "[@codeSystemName !='" + name + "']";
		}
		String xPathForUnusedCodeSystems = "//cqlLookUp/codeSystems/codeSystem" + nameXPathString;

		NodeList unusedCqlCodeSystemNodeList = (NodeList) xPath.evaluate(xPathForUnusedCodeSystems,
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
	 * @param originalDoc
	 *            the simple xml document
	 * @param usedParameterList
	 *            the used parameters
	 * @throws XPathExpressionException
	 */
	public static void removeUnusedParameters(Document originalDoc, List<String> usedParameterList)
			throws XPathExpressionException {
		String nameXPathString = "";
		for (String name : usedParameterList) {
			nameXPathString += "[@name !='" + name + "']";
		}

		String xPathForUnusedParameters = "//cqlLookUp//parameter" + nameXPathString;
		System.out.println(xPathForUnusedParameters);

		NodeList unusedCqlParameterNodeList = (NodeList) xPath.evaluate(xPathForUnusedParameters,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		for (int i = 0; i < unusedCqlParameterNodeList.getLength(); i++) {
			Node current = unusedCqlParameterNodeList.item(i);

			Node parent = current.getParentNode();
			parent.removeChild(current);
		}
	}

	/**
	 * Removes all unused cql includes from the simple xml file. Iterates
	 * through the usedCQLLibraries set, adds them to the xpath string, and then
	 * removes all nodes that are not a part of the xpath string.
	 * 
	 * @param originalDoc
	 *            the simple xml document
	 * @param usedLibList
	 *            the used includes
	 * @throws XPathExpressionException
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
		System.out.println("xPathForUnusedIncludes");
		System.out.println(xPathForUnusedIncludes);
		NodeList unusedCqlIncludeNodeList = (NodeList) xPath.evaluate(xPathForUnusedIncludes,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		for (int i = 0; i < unusedCqlIncludeNodeList.getLength(); i++) {
			Node current = unusedCqlIncludeNodeList.item(i);
			Node parent = current.getParentNode();
			parent.removeChild(current);
		}
	}

	public static SaveUpdateCQLResult generateELM(CQLModel cqlModel, CQLLibraryDAO cqlLibraryDAO) {
		return parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, null, true);
	}
	
	public static SaveUpdateCQLResult parseCQLLibraryForErrors(CQLModel cqlModel, CQLLibraryDAO cqlLibraryDAO,
			List<String> exprList) {
		return parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, exprList, false);
	}

	private static SaveUpdateCQLResult parseCQLLibraryForErrors(CQLModel cqlModel, CQLLibraryDAO cqlLibraryDAO,
			List<String> exprList, boolean generateELM) {

		SaveUpdateCQLResult parsedCQL = new SaveUpdateCQLResult();

		Map<String, LibHolderObject> cqlLibNameMap = new HashMap<String, LibHolderObject>();

		getCQLIncludeLibMap(cqlModel, cqlLibNameMap, cqlLibraryDAO);

		cqlModel.setIncludedCQLLibXMLMap(cqlLibNameMap);

		validateCQLWithIncludes(cqlModel, cqlLibNameMap, parsedCQL, exprList, generateELM);

		return parsedCQL;
	}

	private static void getCQLIncludeLibMap(CQLModel cqlModel, Map<String, LibHolderObject> cqlLibNameMap,
			CQLLibraryDAO cqlLibraryDAO) {

		List<CQLIncludeLibrary> cqlIncludeLibraries = cqlModel.getCqlIncludeLibrarys();
		if (cqlIncludeLibraries == null) {
			return;
		}

		for (CQLIncludeLibrary cqlIncludeLibrary : cqlIncludeLibraries) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.find(cqlIncludeLibrary.getCqlLibraryId());

			if (cqlLibrary == null) {
				logger.info("Could not find included library:" + cqlIncludeLibrary.getAliasName());
				continue;
			}

			String includeCqlXMLString = new String(cqlLibrary.getCQLByteArray());

			CQLModel includeCqlModel = CQLUtilityClass.getCQLStringFromXML(includeCqlXMLString);
			System.out.println("Include lib version for " + cqlIncludeLibrary.getCqlLibraryName() + " is:"
					+ cqlIncludeLibrary.getVersion());
			cqlLibNameMap.put(cqlIncludeLibrary.getCqlLibraryName() + "-" + cqlIncludeLibrary.getVersion() + "|" + cqlIncludeLibrary.getAliasName(),
					new LibHolderObject(includeCqlXMLString, cqlIncludeLibrary));
			getCQLIncludeLibMap(includeCqlModel, cqlLibNameMap, cqlLibraryDAO);
		}
	}

	private static void validateCQLWithIncludes(CQLModel cqlModel, Map<String, LibHolderObject> cqlLibNameMap,
			SaveUpdateCQLResult parsedCQL, List<String> exprList, boolean generateELM) {

		List<File> fileList = new ArrayList<File>();
		List<CqlTranslatorException> cqlTranslatorExceptions = new ArrayList<CqlTranslatorException>();
		String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, "").toString();

		try {
			File test = File.createTempFile(UUIDUtilClient.uuid(), null);
			File tempDir = test.getParentFile();

			File folder = new File(tempDir.getAbsolutePath() + File.separator + UUIDUtilClient.uuid());
			folder.mkdir();
			File mainCQLFile = createCQLTempFile(cqlFileString, UUIDUtilClient.uuid(), folder);
			fileList.add(mainCQLFile);

			for (String cqlLibName : cqlLibNameMap.keySet()) {
				
				CQLModel includeCqlModel = CQLUtilityClass
						.getCQLStringFromXML(cqlLibNameMap.get(cqlLibName).getMeasureXML());
				
				LibHolderObject libHolderObject = cqlLibNameMap.get(cqlLibName);
				
				String cqlString = CQLUtilityClass.getCqlString(includeCqlModel, "").toString();
				System.out.println("Creating file:"+libHolderObject.getCqlLibrary().getCqlLibraryName() + "-" + libHolderObject.getCqlLibrary().getVersion());
				File cqlIncludedFile = createCQLTempFile(cqlString, libHolderObject.getCqlLibrary().getCqlLibraryName() + "-" + libHolderObject.getCqlLibrary().getVersion(), folder);
				fileList.add(cqlIncludedFile);
				
			}

			CQLtoELM cqlToElm = new CQLtoELM(mainCQLFile);
			cqlToElm.doTranslation(!generateELM, false, generateELM);

			if (generateELM) {
				String elmString = cqlToElm.getElmString();
				parsedCQL.setElmString(elmString);
			}

			cqlTranslatorExceptions = cqlToElm.getErrors();

			fileList.add(test);
			fileList.add(folder);
			
			if(exprList != null){
								
				filterCQLArtifacts(cqlModel, parsedCQL, folder, cqlToElm, exprList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			for (File file : fileList) {
				file.delete();
			}
		}

		List<CQLErrors> errors = new ArrayList<CQLErrors>();

		for (CqlTranslatorException cte : cqlTranslatorExceptions) {
			CQLErrors cqlErrors = new CQLErrors();

			cqlErrors.setStartErrorInLine(cte.getLocator().getStartLine());

			cqlErrors.setErrorInLine(cte.getLocator().getStartLine());
			cqlErrors.setErrorAtOffeset(cte.getLocator().getStartChar());

			cqlErrors.setEndErrorInLine(cte.getLocator().getEndLine());
			cqlErrors.setEndErrorAtOffset(cte.getLocator().getEndChar());

			cqlErrors.setErrorMessage(cte.getMessage());
			errors.add(cqlErrors);
		}

		parsedCQL.setCqlErrors(errors);
	}

	/**
	 * @param parsedCQL
	 * @param forHumanReadable 
	 */
	
	private static void filterCQLArtifacts(CQLModel cqlModel, SaveUpdateCQLResult parsedCQL, File folder,
			CQLtoELM cqlToElm, List<String> exprList) {
		if (cqlToElm != null) {

			CQLFilter cqlFilter = new CQLFilter(cqlToElm.getLibrary(), exprList, folder.getAbsolutePath(), cqlModel);
			cqlFilter.findUsedExpressions();
			CQLObject cqlObject = cqlFilter.getCqlObject();
			GetUsedCQLArtifactsResult usedArtifacts = new GetUsedCQLArtifactsResult();
			usedArtifacts.setUsedCQLcodes(cqlFilter.getUsedCodes());
			usedArtifacts.setUsedCQLcodeSystems(cqlFilter.getUsedCodeSystems());
			usedArtifacts.setUsedCQLDefinitions(cqlFilter.getUsedExpressions());
			usedArtifacts.setUsedCQLFunctions(cqlFilter.getUsedFunctions());
			usedArtifacts.setUsedCQLParameters(cqlFilter.getUsedParameters());
			usedArtifacts.setUsedCQLValueSets(cqlFilter.getUsedValuesets());
			usedArtifacts.setUsedCQLLibraries(cqlFilter.getUsedLibraries());
			usedArtifacts.setValueSetDataTypeMap(cqlFilter.getValueSetDataTypeMap());
			usedArtifacts.setIncludeLibMap(cqlFilter.getUsedLibrariesMap());
			parsedCQL.setUsedCQLArtifacts(usedArtifacts);
			parsedCQL.setCqlObject(cqlObject);
			
			System.out.println("Def to Def:"+cqlFilter.getDefinitionToDefinitionMap());
			usedArtifacts.setDefinitionToDefinitionMap(cqlFilter.getDefinitionToDefinitionMap());
						
			System.out.println("Def to Func:"+cqlFilter.getDefinitionToFunctionMap());
			usedArtifacts.setDefinitionToFunctionMap(cqlFilter.getDefinitionToFunctionMap());
			
			System.out.println("Func to Def:"+cqlFilter.getFunctionToDefinitionMap());
			usedArtifacts.setFunctionToDefinitionMap(cqlFilter.getFunctionToDefinitionMap());
			
			System.out.println("Func to Func:"+cqlFilter.getFunctionToFunctionMap());
			usedArtifacts.setFunctionToFunctionMap(cqlFilter.getFunctionToFunctionMap());
		}
	}

	private static File createCQLTempFile(String cqlFileString, String name, File parentFolder) throws IOException {
		File cqlFile = new File(parentFolder, name + ".cql");
		FileWriter fw = new FileWriter(cqlFile);
		fw.write(cqlFileString);
		fw.close();
		return cqlFile;
	}

	public class CQLArtifactHolder {

		private Set<String> cqlDefinitionUUIDSet = new HashSet<String>();
		private Set<String> cqlFunctionUUIDSet = new HashSet<String>();
		private Set<String> cqlValuesetIdentifierSet = new HashSet<String>();
		private Set<String> cqlParameterIdentifierSet = new HashSet<String>();
		private Set<String> cqlCodesSet = new HashSet<String>();

		private Set<String> cqlDefFromPopSet = new HashSet<String>();
		private Set<String> cqlFuncFromPopSet = new HashSet<String>();

		public Set<String> getCqlDefinitionUUIDSet() {
			return cqlDefinitionUUIDSet;
		}

		public Set<String> getCqlFunctionUUIDSet() {
			return cqlFunctionUUIDSet;
		}

		public Set<String> getCqlValuesetIdentifierSet() {
			return cqlValuesetIdentifierSet;
		}

		public Set<String> getCqlParameterIdentifierSet() {
			return cqlParameterIdentifierSet;
		}

		public void addDefinitionUUID(String uuid) {
			cqlDefinitionUUIDSet.add(uuid);
		}

		public void addFunctionUUID(String uuid) {
			cqlFunctionUUIDSet.add(uuid);
		}

		public void addValuesetIdentifier(String identifier) {
			cqlValuesetIdentifierSet.add(identifier);
		}

		public void addParameterIdentifier(String identifier) {
			cqlParameterIdentifierSet.add(identifier);
		}

		public void addDefinitionIdentifier(String identifier) {
			cqlDefFromPopSet.add(identifier);
		}

		public void addFunctionIdentifier(String identifier) {
			cqlFuncFromPopSet.add(identifier);
		}

		public void setCqlDefinitionUUIDSet(Set<String> cqlDefinitionUUIDSet) {
			this.cqlDefinitionUUIDSet = cqlDefinitionUUIDSet;
		}

		public void setCqlFunctionUUIDSet(Set<String> cqlFunctionUUIDSet) {
			this.cqlFunctionUUIDSet = cqlFunctionUUIDSet;
		}

		public void setCqlValuesetIdentifierSet(Set<String> cqlValuesetIdentifierSet) {
			this.cqlValuesetIdentifierSet = cqlValuesetIdentifierSet;
		}

		public void setCqlParameterIdentifierSet(Set<String> cqlParameterIdentifierSet) {
			this.cqlParameterIdentifierSet = cqlParameterIdentifierSet;
		}

		public Set<String> getCqlCodesSet() {
			return cqlCodesSet;
		}

		public void setCqlCodesSet(Set<String> cqlCodesSet) {
			this.cqlCodesSet = cqlCodesSet;
		}

		public void addCQLCode(String identifier) {
			this.cqlCodesSet.add(identifier);
		}

		public Set<String> getCqlDefFromPopSet() {
			return cqlDefFromPopSet;
		}

		public void setCqlDefFromPopSet(Set<String> cqlDefFromPopSet) {
			this.cqlDefFromPopSet = cqlDefFromPopSet;
		}

		public Set<String> getCqlFuncFromPopSet() {
			return cqlFuncFromPopSet;
		}

		public void setCqlFuncFromPopSet(Set<String> cqlFuncFromPopSet) {
			this.cqlFuncFromPopSet = cqlFuncFromPopSet;
		}
	}

	public static void addUsedCQLLibstoSimpleXML(Document originalDoc, Map<String, CQLIncludeLibrary> includeLibMap) {

		Node allUsedLibsNode = originalDoc.createElement("allUsedCQLLibs");
		originalDoc.getFirstChild().appendChild(allUsedLibsNode);

		for (String libName : includeLibMap.keySet()) {
			CQLIncludeLibrary cqlLibrary = includeLibMap.get(libName);

			Element libNode = originalDoc.createElement("lib");
			libNode.setAttribute("id", cqlLibrary.getCqlLibraryId());
			libNode.setAttribute("alias", cqlLibrary.getAliasName());
			libNode.setAttribute("name", cqlLibrary.getCqlLibraryName());
			libNode.setAttribute("version", cqlLibrary.getVersion());
			libNode.setAttribute("isUnUsedGrandChild", "false");

			allUsedLibsNode.appendChild(libNode);
		}

	}
	
	public static void addUnUsedGrandChildrentoSimpleXML(Document originalDoc, SaveUpdateCQLResult result, CQLModel cqlModel) throws XPathExpressionException {

		String allUsedCQLLibsXPath = "//allUsedCQLLibs";
		
		XmlProcessor xmlProcessor = new XmlProcessor("<test></test>");
		xmlProcessor.setOriginalDoc(originalDoc);
		
		Node allUsedLibsNode = xmlProcessor.findNode(originalDoc, allUsedCQLLibsXPath);
		
		if(allUsedLibsNode != null){
			
			List<CQLIncludeLibrary> cqlChildLibs = cqlModel.getCqlIncludeLibrarys();
			
			for(CQLIncludeLibrary library:cqlChildLibs){
				String libId = library.getCqlLibraryId();
				String libAlias = library.getAliasName();
				String libName = library.getCqlLibraryName();
				String libVersion = library.getVersion();				
				
				Collection<CQLIncludeLibrary> childs = result.getUsedCQLArtifacts().getIncludeLibMap().values();
				boolean found = childs.contains(library);
				
				if(found){
					LibHolderObject libHolderObject = cqlModel.getIncludedCQLLibXMLMap().
							get(libName + "-" + libVersion + "|" + libAlias);
					
					if(libHolderObject != null){
						String xml = libHolderObject.getMeasureXML();
						CQLModel childCQLModel = CQLUtilityClass.getCQLStringFromXML(xml);
						List<CQLIncludeLibrary> cqlGrandChildLibs = childCQLModel.getCqlIncludeLibrarys();
						
						for(CQLIncludeLibrary grandChildLib : cqlGrandChildLibs){
							
							boolean gcFound = childs.contains(grandChildLib);// foundCQLLib(childs, grandChildLib);
							
							if(!gcFound){
								Element libNode = originalDoc.createElement("lib");
								libNode.setAttribute("id", grandChildLib.getCqlLibraryId());
								libNode.setAttribute("alias", grandChildLib.getAliasName());
								libNode.setAttribute("name", grandChildLib.getCqlLibraryName());
								libNode.setAttribute("version", grandChildLib.getVersion());
								libNode.setAttribute("isUnUsedGrandChild", "true");
								allUsedLibsNode.appendChild(libNode);
							}
							
						}
					}
					
				}
			}
		}
	}
	
//	private static boolean foundCQLLib(Collection<CQLIncludeLibrary> libs, CQLIncludeLibrary library){
//		boolean returnVal = false;
//		
//		String libString = library.getCqlLibraryName() + "-" + library.getVersion() + "|" + library.getAliasName();
//		System.out.println(libString);
//		
//		for(CQLIncludeLibrary lib : libs){
//			
//			if(lib.equals(library)){
//				returnVal = true;
//				break;
//			}
//			
//		}
//		
//		return returnVal;
//	}
}
