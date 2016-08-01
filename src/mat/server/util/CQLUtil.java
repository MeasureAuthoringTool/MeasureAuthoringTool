package mat.server.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.model.cql.parser.CQLBaseStatementInterface;
import mat.model.cql.parser.CQLDefinitionModelObject;
import mat.model.cql.parser.CQLFileObject;
import mat.model.cql.parser.CQLFunctionModelObject;
import mat.model.cql.parser.CQLParameterModelObject;
import mat.model.cql.parser.CQLValueSetModelObject;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CQLUtil {
	
	/** The Constant xPath. */
	static final javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();

	public static CQLArtifactHolder getUsedCQLArtifacts(Document originalDoc, CQLFileObject cqlFileObject) throws XPathExpressionException {
					
		CQLArtifactHolder cqlArtifactHolder = CQLUtil.getCQLArtifactsReferredByPoplns(originalDoc, cqlFileObject);
		CQLUtil cqlUtil = new CQLUtil();
		CQLArtifactHolder usedCQLArtifactHolder = cqlUtil.new CQLArtifactHolder();
		
		usedCQLArtifactHolder.getCqlDefinitionUUIDSet().addAll(cqlArtifactHolder.getCqlDefinitionUUIDSet());
		usedCQLArtifactHolder.getCqlFunctionUUIDSet().addAll(cqlArtifactHolder.getCqlFunctionUUIDSet());
		
		for(String cqlDefnUUID: cqlArtifactHolder.getCqlDefinitionUUIDSet()){
			String xPathCQLDef = "/measure/cqlLookUp/definitions/definition[@id='" + cqlDefnUUID +"']";
			Node cqlDefinition = (Node) xPath.evaluate(xPathCQLDef, 
					originalDoc.getDocumentElement(), XPathConstants.NODE);
			
			String cqlDefnName = "\"" + cqlDefinition.getAttributes().getNamedItem("name").getNodeValue() + "\"";
			CQLDefinitionModelObject cqlDefinitionModelObject = cqlFileObject.getDefinitionsMap().get(cqlDefnName);
			
			CQLUtil.collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlDefinitionModelObject, usedCQLArtifactHolder);
		}
		
		for(String cqlDefnUUID: cqlArtifactHolder.getCqlFunctionUUIDSet()){
			String xPathCQLDef = "/measure/cqlLookUp/functions/function[@id='" + cqlDefnUUID +"']";
			Node cqlFunction = (Node) xPath.evaluate(xPathCQLDef, 
					originalDoc.getDocumentElement(), XPathConstants.NODE);
			
			String cqlFuncName = "\"" + cqlFunction.getAttributes().getNamedItem("name").getNodeValue() + "\"";
			CQLFunctionModelObject cqlFunctionModelObject = cqlFileObject.getFunctionsMap().get(cqlFuncName);
			
			CQLUtil.collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlFunctionModelObject, usedCQLArtifactHolder);
		}
			
		return usedCQLArtifactHolder;
	}
	
	/**
	 * Loop through all of the cqlDefinitionsModels in the list and get all referred to definitions and functions. 
	 * On each definition and function, recursively find all of the referred to definitions and functions for them. 
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
		 for(CQLValueSetModelObject cqlValueSetModelObject: cqlValueSetModelObjects){
			 usedCQLArtifactHolder.addValuesetIdentifier(cqlValueSetModelObject.getIdentifier().replace("\"", ""));
		 }
		 
		 // get parameters used by current artifact
		 List<CQLParameterModelObject> cqlParameterModelObjects = cqlBaseStatementInterface.getReferredToParameters(); 
		 for(CQLParameterModelObject cqlParameterModelObject : cqlParameterModelObjects) {
			 usedCQLArtifactHolder.addParameterIdentifier(cqlParameterModelObject.getIdentifier());
		 }
		
		 // get all definitions which current artifact refer to, recursively find all artifacts which could be nested. 
		 List<CQLDefinitionModelObject> cqlDefinitionModelObjectList = cqlBaseStatementInterface.getReferredToDefinitions();
		 for(CQLDefinitionModelObject cqlDefinitionModelObject: cqlDefinitionModelObjectList){
			 usedCQLArtifactHolder.addDefinitionUUID(CQLUtil.getCQLDefinitionUUID(originalDoc, cqlDefinitionModelObject, true));
			 
			 collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlDefinitionModelObject, 
					 usedCQLArtifactHolder);
			 
		 }
		 
		 // get all functions which current artifact refer to, recursively find all artifacts which could be nested. 
		 List<CQLFunctionModelObject> cqlFunctionModelObjectList = cqlBaseStatementInterface.getReferredToFunctions();
		 for(CQLFunctionModelObject cqlFunctionModelObject:cqlFunctionModelObjectList){
			 usedCQLArtifactHolder.addFunctionUUID(CQLUtil.getCQLDefinitionUUID(originalDoc, cqlFunctionModelObject, false));
			 
			 collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlFunctionModelObject, 
					 usedCQLArtifactHolder);
		 }
	}
	
	

	/**
	 * Given a CqlDefinitionModelObject, find out the <definition> node and return its "uuid" 
	 * attribute value.
	 * 
	 * @param originalDoc
	 * @param cqlModelObject
	 * @return
	 * @throws XPathExpressionException
	 */
	public static String getCQLDefinitionUUID(Document originalDoc,
			CQLBaseStatementInterface cqlModelObject, boolean isDefinition) throws XPathExpressionException {
		 
		 System.out.println("cql defn name bfor:"+cqlModelObject.getIdentifier());
		 String defnName = cqlModelObject.getIdentifier().replaceAll("\"", "");
		 System.out.println("cql defn name aftr:"+defnName);
		 
		 String cqlArtifactName = "definitions//definition";
		 if(!isDefinition){
			 cqlArtifactName = "functions//function";
		 }
		 
		 String xPathForDefinitions = "/measure/cqlLookUp/"+cqlArtifactName+"[@name='"+defnName+"']/@id";
		 System.out.println(xPathForDefinitions);
		 Node cqlDefinitionUUID = (Node) xPath.evaluate(xPathForDefinitions, originalDoc.getDocumentElement(), XPathConstants.NODE);
		 return cqlDefinitionUUID.getNodeValue();
	}

	/**
	 * This method will find out all the CQLDefinitions referred to by populations defined in a measure.
	 * @param originalDoc
	 * @param cqlFileObject
	 * @return
	 */
	public static CQLArtifactHolder getCQLArtifactsReferredByPoplns(Document originalDoc, CQLFileObject cqlFileObject) {
		CQLUtil cqlUtil = new CQLUtil();
		CQLUtil.CQLArtifactHolder cqlArtifactHolder = cqlUtil.new CQLArtifactHolder();
		
		String xPathForDefinitions = "/measure//cqldefinition/@uuid";
		String xPathForFunctions = "/measure/measureObservations//cqlfunction/@uuid";
		try {
			NodeList cqlDefinitions = (NodeList) xPath.evaluate(xPathForDefinitions, 
												originalDoc.getDocumentElement(), XPathConstants.NODESET);
			
			for(int i = 0; i < cqlDefinitions.getLength(); i++) {
				String uuid = cqlDefinitions.item(i).getNodeValue(); 
				cqlArtifactHolder.addDefinitionUUID(uuid);
			}
			
			NodeList cqlFunctions = (NodeList) xPath.evaluate(xPathForFunctions, originalDoc.getDocumentElement(), XPathConstants.NODESET); 
			
			for(int i = 0; i < cqlFunctions.getLength(); i++) {
				String uuid = cqlFunctions.item(i).getNodeValue(); 
				cqlArtifactHolder.addFunctionUUID(uuid);
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return cqlArtifactHolder;
	}
	
	/**
	 * Removes all unused cql definitions from the simple xml file. Iterates through the usedcqldefinitions set, 
	 * adds them to the xpath string, and then removes all nodes that are not a part of the xpath string. 
	 * @param originalDoc
	 * 	the simple xml document
	 * @param usedCQLDefinitions
	 * 	the usedcqldefinitions
	 * @throws XPathExpressionException
	 */
	public static void removeUnusedCQLDefinitions(Document originalDoc, Set<String> usedCQLDefinitions) throws XPathExpressionException {
		
		String idXPathString = ""; 
		for (String id:usedCQLDefinitions) {
			idXPathString += "[@id !='" + id + "']"; 
		}
		
		String xPathForUnusedDefinitions = "/measure/cqlLookUp//definition" + idXPathString; 
		System.out.println(xPathForUnusedDefinitions);
		
		NodeList unusedCQLDefNodeList = (NodeList) xPath.evaluate(xPathForUnusedDefinitions, originalDoc.getDocumentElement(), XPathConstants.NODESET); 
		
		for (int i = 0; i < unusedCQLDefNodeList.getLength(); i++) {
			Node current = unusedCQLDefNodeList.item(i); 
			
			Node parent = current.getParentNode(); 
			parent.removeChild(current); 
		}
		
	}
	
	/**
	 * Removes all unused cql functions from the simple xml file. Iterates through the usedcqlfunctions set, 
	 * adds them to the xpath string, and then removes all nodes that are not a part of the xpath string. 
	 * @param originalDoc
	 * 	the simple xml document
	 * @param usedCQLFunctions
	 * 	the usedcqlfunctions
	 * @throws XPathExpressionException
	 */
	public static void removeUnusedCQLFunctions(Document originalDoc, Set<String> usedCQLFunctions) throws XPathExpressionException {
		String idXPathString = ""; 
		for(String id: usedCQLFunctions) {
			idXPathString += "[@id !='" + id + "']";
		}
		
		String xPathForUnusedFunctions = "/measure/cqlLookUp//function" + idXPathString; 
		System.out.println(xPathForUnusedFunctions);
		
		NodeList unusedCqlFuncNodeList = (NodeList) xPath.evaluate(xPathForUnusedFunctions, originalDoc.getDocumentElement(), XPathConstants.NODESET);
		for(int i = 0; i < unusedCqlFuncNodeList.getLength(); i++) {
			Node current = unusedCqlFuncNodeList.item(i); 
			
			Node parent = current.getParentNode(); 
			parent.removeChild(current);
		}
	}
	
	/**
	 * Removes all unused cql valuesets from the simple xml file. Iterates through the usedcqlvaluesets set, 
	 * adds them to the xpath string, and then removes all nodes that are not a part of the xpath string. 
	 * @param originalDoc
	 * 	the simple xml document
	 * @param cqlValuesetIdentifierSet
	 * 	the usevaluesets
	 * @throws XPathExpressionException
	 */
	public static void removeUnusedValuesets(Document originalDoc, Set<String> cqlValuesetIdentifierSet) throws XPathExpressionException {
		String nameXPathString = ""; 
		for(String name : cqlValuesetIdentifierSet) {
			nameXPathString += "[@name !='" + name + "']";
		}
		
		String xPathForUnusedValuesets= "/measure/cqlLookUp//valueset" + nameXPathString; 
		System.out.println(xPathForUnusedValuesets);
		
		NodeList unusedCqlValuesetNodeList = (NodeList) xPath.evaluate(xPathForUnusedValuesets, originalDoc.getDocumentElement(), XPathConstants.NODESET);
		for(int i = 0; i < unusedCqlValuesetNodeList.getLength(); i++) {
			Node current = unusedCqlValuesetNodeList.item(i); 
			
			Node parent = current.getParentNode(); 
			parent.removeChild(current);
		}
	}
	
	/**
	 * Removes all unused cql parameters from the simple xml file. Iterates through the usedcqlparameters set, 
	 * adds them to the xpath string, and then removes all nodes that are not a part of the xpath string. 
	 * @param originalDoc
	 * 	the simple xml document
	 * @param cqlParameterIdentifierSet
	 * 	the used parameters
	 * @throws XPathExpressionException
	 */
	public static void removeUnusedParameters(Document originalDoc, Set<String> cqlParameterIdentifierSet) throws XPathExpressionException {
		String nameXPathString = ""; 
		for(String name : cqlParameterIdentifierSet) {
			name = name.replaceAll("\"", "");
			nameXPathString += "[@name !='" + name + "']";
		}
		
		String xPathForUnusedParameters = "/measure/cqlLookUp//parameter" + nameXPathString; 
		System.out.println(xPathForUnusedParameters);
		
		NodeList unusedCqlParameterNodeList = (NodeList) xPath.evaluate(xPathForUnusedParameters, originalDoc.getDocumentElement(), XPathConstants.NODESET); 
		for (int i = 0; i < unusedCqlParameterNodeList.getLength(); i++) {
			Node current = unusedCqlParameterNodeList.item(i); 
			
			Node parent = current.getParentNode(); 
			parent.removeChild(current);
		}
	}
	
	public class CQLArtifactHolder{
		
		private Set<String> cqlDefinitionUUIDSet = new HashSet<String>();
		private Set<String> cqlFunctionUUIDSet = new HashSet<String>();
		private Set<String> cqlValuesetIdentifierSet = new HashSet<String>(); 
		private Set<String> cqlParameterIdentifierSet = new HashSet<String>(); 
		
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
		public void addDefinitionUUID(String uuid){
			cqlDefinitionUUIDSet.add(uuid);
		}
		public void addFunctionUUID(String uuid){
			cqlFunctionUUIDSet.add(uuid);
		}
		public void addValuesetIdentifier(String identifier) {
			cqlValuesetIdentifierSet.add(identifier);
		}
		public void addParameterIdentifier(String identifier) {
			cqlParameterIdentifierSet.add(identifier);
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
	}	
}

