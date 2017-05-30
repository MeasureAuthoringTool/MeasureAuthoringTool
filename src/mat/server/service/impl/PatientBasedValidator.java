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
import mat.shared.CQLExpressionOprandObject;
import mat.shared.SaveUpdateCQLResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

public class PatientBasedValidator {
	
	private static final String SCORING_CONTINUOUS_VARIABLE = "Continuous Variable";

	private static final String SCORING_RATIO = "Ratio";

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
		List<String> moAssociatedPopUsedExpression = new ArrayList<String>();
		List<MeasurePackageClauseDetail> packageClauses =  detail.getPackageClauses();
		
		for(MeasurePackageClauseDetail measurePackageClauseDetail : packageClauses){
			String populationUUID = measurePackageClauseDetail.getId();
			String type = measurePackageClauseDetail.getType();
			//ignore "stratification" nodes.
			if(type.equalsIgnoreCase("stratification")){
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
				String associatedClauseNodeUUID  = null;
				if(scoringType.equalsIgnoreCase(SCORING_CONTINUOUS_VARIABLE)){
					associatedClauseNodeUUID = findMeasurePopInGrouping(packageClauses);
				} else if(scoringType.equalsIgnoreCase(SCORING_RATIO)){
					associatedClauseNodeUUID = measurePackageClauseDetail.getAssociatedPopulationUUID();
				}
				if(associatedClauseNodeUUID !=null){
					Node associatedPopNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "/measure//clause[@uuid='"+associatedClauseNodeUUID+"']");
					String definitionName = associatedPopNode.getFirstChild().getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
					moAssociatedPopUsedExpression.add(definitionName);
				}
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
		usedExprList.addAll(moAssociatedPopUsedExpression);
		
		SaveUpdateCQLResult cqlResult = CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, usedExprList);
		
		List<CQLExpressionObject> expressions = cqlResult.getCqlObject().getCqlDefinitionObjectList();
		List<CQLExpressionObject> expressionsToBeChecked = new ArrayList<CQLExpressionObject>();
		List<CQLExpressionObject> expressionsToBeCheckedForMO = new ArrayList<CQLExpressionObject>();
		
		for(CQLExpressionObject cqlExpressionObject : expressions){
			String name = cqlExpressionObject.getName();
			
			if(exprList.contains(name)){
				expressionsToBeChecked.add(cqlExpressionObject);
			}
			
			if(moAssociatedPopUsedExpression.contains(name)) {
				expressionsToBeCheckedForMO.add(cqlExpressionObject);
			}
		}
		
		if(isPatientBasedIndicator){
			
			//Check for MAT-8606 validations.			
			List<String> messages = checkReturnType(expressionsToBeChecked, CQL_RETURN_TYPE_BOOLEAN);
			if(messages.size() > 0){
				errorMessages.addAll(messages);
			}
			//Check for MAT-8622 Measure Observation and Patient-based Measure Indicator in Ratio scoring type.
			if(msrObsFunctionList.size() >0 && scoringType.equalsIgnoreCase(SCORING_RATIO) ){
				String message ="For Ratio Measure, Measure Observation can only be added to grouping if the measure is not Patient-based.";
				errorMessages.add(message);
			}
			
		}else{
			
			//Check for MAT-8608 validations.			
			List<String> messages = checkSimilarReturnTypes(expressionsToBeChecked);
			if(messages.size() > 0){
				errorMessages.addAll(messages);
			}
			
		}
		//check for MAT-8627 validations for functions attached to Measure Observations.
		if(!isPatientBasedIndicator || !scoringType.equalsIgnoreCase(SCORING_RATIO)){
			List<CQLExpressionObject> functions = cqlResult.getCqlObject().getCqlFunctionObjectList();
			List<CQLExpressionObject> functionsToBeChecked = new ArrayList<CQLExpressionObject>();
			
			for(CQLExpressionObject cqlExpressionObject : functions){
				String name = cqlExpressionObject.getName();
			
				if(msrObsFunctionList.contains(name)){
					functionsToBeChecked.add(cqlExpressionObject);
				}
			}
			//MAT-8624 Single Argument Required for Measure Observation User-defined Function .
			List<String> moArgumentMessage = checkForMOFunctionArgumentCount(functionsToBeChecked, expressionsToBeCheckedForMO);
			if(moArgumentMessage.size() > 0){
				errorMessages.addAll(moArgumentMessage);
			}
			
			List<String> messages = checkReturnType(functionsToBeChecked, CQL_RETURN_TYPE_NUMERIC);
			if(messages.size() > 0){
				errorMessages.addAll(messages);
			}
			
		}
		
		return errorMessages;
	}
	private static  String findMeasurePopInGrouping(List<MeasurePackageClauseDetail> packageClauses) {
		String measurePopUUID = null;
		for(MeasurePackageClauseDetail measurePackageClauseDetail : packageClauses){
			String type = measurePackageClauseDetail.getType();
			if(type.equalsIgnoreCase("measurePopulation")){
				measurePopUUID = measurePackageClauseDetail.getId();
			}
		}
		return measurePopUUID;
	}
	//MAT-8624 Single Argument Required for Measure Observation User-defined Function .
	private static List<String> checkForMOFunctionArgumentCount(List<CQLExpressionObject> functionsToBeChecked , List<CQLExpressionObject> associatedPopExpressionTobeChecked) {
		List<String> returnMessages = new ArrayList<String>();
		for(CQLExpressionObject cqlExpressionObject : functionsToBeChecked){
			List<CQLExpressionOprandObject> argumentList =  cqlExpressionObject.getOprandList();
			if(argumentList.isEmpty() || argumentList.size() > 1){
				returnMessages.add("For an added measure observation, the user-defined function "+ cqlExpressionObject.getName()+" must have exactly 1 argument in the argument list.");
				returnMessages.add("For an added measure observation, the argument in the user-defined function must match the return type"
						+ " of the definition directly applied to the Associated Population.");
			} else {
				String funcArgumentReturnType = argumentList.get(0).getReturnType();
				for(CQLExpressionObject expressionObject : associatedPopExpressionTobeChecked){
					if(!expressionObject.getReturnType().equalsIgnoreCase(funcArgumentReturnType)){
						returnMessages.add("For an added measure observation, the argument in the user-defined function must match the return type"
								+ " of the definition directly applied to the Associated Population.");
						break;
					}
				}
			}
			
		}
		return returnMessages;
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
