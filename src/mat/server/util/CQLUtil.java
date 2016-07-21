package mat.server.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mat.model.cql.parser.CQLBaseStatementInterface;
import mat.model.cql.parser.CQLDefinitionModelObject;
import mat.model.cql.parser.CQLFileObject;
import mat.model.cql.parser.CQLFunctionModelObject;
import mat.model.cql.parser.CQLValueSetModelObject;

public class CQLUtil {
	
	/** The Constant xPath. */
	static final javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();

	public static CQLArtifactHolder getUsedCQLArtifacts(Document originalDoc, CQLFileObject cqlFileObject) throws XPathExpressionException {
					
		CQLArtifactHolder cqlArtifactHolder = CQLUtil.getCQLArtifactsReferredByPoplns(originalDoc, cqlFileObject);
		CQLUtil cqlUtil = new CQLUtil();
		CQLArtifactHolder usedCQLArtifactHolder = cqlUtil.new CQLArtifactHolder();
		
		usedCQLArtifactHolder.setCqlDefinitionUUIDSet(cqlArtifactHolder.getCqlDefinitionUUIDSet());
		usedCQLArtifactHolder.setCqlFunctionUUIDSet(cqlArtifactHolder.getCqlFunctionUUIDSet());
		
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
		
		 List<CQLDefinitionModelObject> cqlDefinitionModelObjectList = cqlBaseStatementInterface
				 															.getReferredToDefinitions();
		 
		 for(CQLDefinitionModelObject cqlDefinitionModelObject: cqlDefinitionModelObjectList){
			 usedCQLArtifactHolder.addDefinitionUUID(CQLUtil.getCQLDefinitionUUID(originalDoc, cqlDefinitionModelObject, true));
			 
			 collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlDefinitionModelObject, 
					 usedCQLArtifactHolder);
			 
		 }
		 
		 List<CQLFunctionModelObject> cqlFunctionModelObjectList = cqlBaseStatementInterface
				 														.getReferredToFunctions();
		 for(CQLFunctionModelObject cqlFunctionModelObject:cqlFunctionModelObjectList){
			 usedCQLArtifactHolder.addFunctionUUID(CQLUtil.getCQLDefinitionUUID(originalDoc, cqlFunctionModelObject, false));
			 
			 collectUsedCQLArtifacts(originalDoc, cqlFileObject, cqlFunctionModelObject, 
					 usedCQLArtifactHolder);
		 }
		 
		 List<CQLValueSetModelObject> cqlValueSetModelObjects = cqlBaseStatementInterface.getReferredToValueSets();
		 for(CQLValueSetModelObject cqlValueSetModelObject: cqlValueSetModelObjects){
			 System.out.println(cqlValueSetModelObject.getIdentifier());
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
		 Node cqlDefinitionUUID = (Node) xPath.evaluate(xPathForDefinitions, 
					originalDoc.getDocumentElement(), XPathConstants.NODE);
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
	
	public static void removeUnusedCQLFunctions(Document originalDoc, Set<String> usedCQLFunctions) throws XPathExpressionException {
		String idXPathString = ""; 
		for(String id: usedCQLFunctions) {
			idXPathString += "[@id !='" + id + "']";
		}
		
		String xPathForUnusedFunctions = "measure/cqlLookUp//function" + idXPathString; 
		System.out.println(xPathForUnusedFunctions);
		
		NodeList unusedCqlFuncNodeList = (NodeList) xPath.evaluate(xPathForUnusedFunctions, originalDoc.getDocumentElement(), XPathConstants.NODESET);
		for(int i = 0; i < unusedCqlFuncNodeList.getLength(); i++) {
			Node current = unusedCqlFuncNodeList.item(i); 
			
			Node parent = current.getParentNode(); 
			parent.removeChild(current);
		}
	}
	
	public class CQLArtifactHolder{
		
		private Set<String> cqlDefinitionUUIDSet = new HashSet<String>();
		private Set<String> cqlFunctionUUIDSet = new HashSet<String>();
		
		public Set<String> getCqlDefinitionUUIDSet() {
			return cqlDefinitionUUIDSet;
		}
		public void addDefinitionUUID(String uuid){
			cqlDefinitionUUIDSet.add(uuid);
		}
		public Set<String> getCqlFunctionUUIDSet() {
			return cqlFunctionUUIDSet;
		}
		public void addFunctionUUID(String uuid){
			cqlFunctionUUIDSet.add(uuid);
		}
		public void setCqlDefinitionUUIDSet(Set<String> cqlDefinitionUUIDSet) {
			this.cqlDefinitionUUIDSet = cqlDefinitionUUIDSet;
		}
		public void setCqlFunctionUUIDSet(Set<String> cqlFunctionUUIDSet) {
			this.cqlFunctionUUIDSet = cqlFunctionUUIDSet;
		}
	}
	
}

