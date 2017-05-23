package mat.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.client.measurepackage.MeasurePackageDetail;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.clause.MeasureXML;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.util.CQLUtil;
import mat.server.util.XmlProcessor;
import mat.shared.CQLExpressionObject;
import mat.shared.SaveUpdateCQLResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

public class PatientBasedValidator {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(PatientBasedValidator.class);
	
	private static final String SYSTEM_QUANTITY = "System.Quantity";

	private static final String SYSTEM_DECIMAL = "System.Decimal";

	private static final String SYSTEM_INTEGER = "System.Integer";

	private static final String SYSTEM_BOOLEAN = "System.Boolean";

	private static final String CQLAGGFUNCTION = "cqlaggfunction";

	private static final String DISPLAY_NAME = "displayName";

	private static final String MEASURE_OBSERVATION = "measureObservation";

	private static final String XPATH_FOR_MEASURE_SCORING = "/measure/measureDetails/scoring";

	private static final String XPATH_FOR_PATIENT_BASED_INDICATOR = "/measure/measureDetails/patientBasedIndicator";

	//Indicator for CQL return types of Integer,Decimal, or Quantity.
	private static final String CQL_RETURN_TYPE_NUMERIC = "NUMERIC";

	//Indicator for CQL return types of Boolean.
	private static final String CQL_RETURN_TYPE_BOOLEAN = "BOOLEAN";	
	
	
	public static List<String> checkPatientBasedValidations(MeasureXML measureXML, MeasurePackageDetail detail, CQLLibraryDAO cqlLibraryDAO) throws XPathExpressionException {
		
		List<String> errorMessages = new ArrayList<String>();
		
		CQLModel cqlModel = CQLUtilityClass.getCQLStringFromXML(measureXML.getMeasureXMLAsString());
		XmlProcessor xmlProcessor = new XmlProcessor(measureXML.getMeasureXMLAsString());

		Node patientBasedIndicatorNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), XPATH_FOR_PATIENT_BASED_INDICATOR);
		String patientBasedIndicator = patientBasedIndicatorNode.getTextContent();
		boolean isPatientBasedIndicator = patientBasedIndicator.equals("true");
		
		Node scoringNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), XPATH_FOR_MEASURE_SCORING);
		String scoringType = scoringNode.getTextContent();
		
		List<String> exprList = new ArrayList<String>();
		List<String> msrObsFunctionList = new ArrayList<String>();
		
		List<MeasurePackageClauseDetail> packageClauses =  detail.getPackageClauses();
		
		for(MeasurePackageClauseDetail measurePackageClauseDetail : packageClauses){
			String populationUUID = measurePackageClauseDetail.getId();
			String type = measurePackageClauseDetail.getType();
			//ignore "stratification" nodes.
			if(type.equals("stratification")){
				continue;
			}
			
			Node clauseNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure//clause[@uuid='"+populationUUID+"']");
					
			if(type.equals(MEASURE_OBSERVATION)){
			
				//find the cqlfunction here
				Node firstChildNode = clauseNode.getFirstChild();
				
				if(firstChildNode.getNodeName().equals(CQLAGGFUNCTION)){
					if(firstChildNode.hasChildNodes()){
						firstChildNode = firstChildNode.getFirstChild();
					}else{
						continue;
					}
				}				
				msrObsFunctionList.add(firstChildNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue());
				
			}else{
				
				//find cqldefinition here
				Node firstChildNode = clauseNode.getFirstChild();
				
				String definitionName = firstChildNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
				
				if(!exprList.contains(definitionName)){
					exprList.add(definitionName);
				}
			}
		}
		
		List<String> usedExprList = new ArrayList<String>();
		usedExprList.addAll(exprList);
		usedExprList.addAll(msrObsFunctionList);
		SaveUpdateCQLResult cqlResult = CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, usedExprList);
		
		List<CQLExpressionObject> expressions = cqlResult.getCqlObject().getCqlDefinitionObjectList();
		List<CQLExpressionObject> expressionsToBeChecked = new ArrayList<CQLExpressionObject>();
		
		for(CQLExpressionObject cqlExpressionObject : expressions){
			String name = cqlExpressionObject.getName();
			
			if(exprList.contains(name)){
				expressionsToBeChecked.add(cqlExpressionObject);
			}
		}
		
		if(isPatientBasedIndicator){
			
			//Check for MAT-8606 validations.			
			List<String> messages = checkReturnType(expressionsToBeChecked, CQL_RETURN_TYPE_BOOLEAN);
			if(messages.size() > 0){
				errorMessages.addAll(messages);
			}
			
		}else{
			
			//Check for MAT-8608 validations.			
			List<String> messages = checkSimilarReturnTypes(expressionsToBeChecked);
			if(messages.size() > 0){
				errorMessages.addAll(messages);
			}
			
		}
		
		//check for MAT-8627 validations for functions attached to Measure Observations.
		List<CQLExpressionObject> functions = cqlResult.getCqlObject().getCqlFunctionObjectList();
		List<CQLExpressionObject> functionsToBeChecked = new ArrayList<CQLExpressionObject>();
		
		for(CQLExpressionObject cqlExpressionObject : functions){
			String name = cqlExpressionObject.getName();
		
			if(msrObsFunctionList.contains(name)){
				functionsToBeChecked.add(cqlExpressionObject);
			}
		}
		List<String> messages = checkReturnType(functionsToBeChecked, CQL_RETURN_TYPE_NUMERIC);
		if(messages.size() > 0){
			errorMessages.addAll(messages);
		}
		
		return errorMessages;
	}

	private static List<String> checkSimilarReturnTypes(
			List<CQLExpressionObject> expressionsToBeChecked) {
		
		List<String> returnMessages = new ArrayList<String>();
		
		String returnType = null;
		
		for(CQLExpressionObject cqlExpressionObject : expressionsToBeChecked){
			
			logger.info("Return type for "+cqlExpressionObject.getName()+" is "+cqlExpressionObject.getReturnType());
			String expressionReturnType = cqlExpressionObject.getReturnType();
			
			if(returnType == null){
				returnType = expressionReturnType;
			}else if(!returnType.equals(expressionReturnType)){
				returnMessages.add("Return types for all definitions in a group should be similar.");
			}
		}
		
		return returnMessages;
	}

	private static List<String> checkReturnType(
			List<CQLExpressionObject> expressionsToBeChecked, String returnTypeCheck) {
		
		List<String> returnMessages = new ArrayList<String>();
		
		for(CQLExpressionObject cqlExpressionObject : expressionsToBeChecked){
			
			/*
			 * Angular brackets "<" and ">" are filtered out by Javascript/mark-up hack validators.
			 * Replace angular brackets "<" and ">" by square brackets "[" and "]".
			*/ 
			String returnType = cqlExpressionObject.getReturnType();
			returnType = returnType.replaceAll("<", "[");
			returnType = returnType.replaceAll(">", "]");
			
			//check for return type to be "System.Boolean"
			if(returnTypeCheck.equals(CQL_RETURN_TYPE_BOOLEAN)){
				
				if(!cqlExpressionObject.getReturnType().equals(SYSTEM_BOOLEAN)){
					returnMessages.add("CQL "+cqlExpressionObject.getType() + " " +cqlExpressionObject.getName() + " has return type as "+returnType+ ". Must be System.Boolean.");
				}
				
			}else if(returnTypeCheck.equals(CQL_RETURN_TYPE_NUMERIC)){
				String exprReturnType = cqlExpressionObject.getReturnType();
				
				if(!exprReturnType.equals(SYSTEM_INTEGER) && 
						!exprReturnType.equals(SYSTEM_DECIMAL) && 
							!exprReturnType.equals(SYSTEM_QUANTITY)){
					
					returnMessages.add("CQL "+cqlExpressionObject.getType() + " " +cqlExpressionObject.getName() + " has return type as "+returnType+ ". Must be System.Integer or System.Decimal or System.Quantity.");
				}
			}
		}		
		return returnMessages;
	}

}
