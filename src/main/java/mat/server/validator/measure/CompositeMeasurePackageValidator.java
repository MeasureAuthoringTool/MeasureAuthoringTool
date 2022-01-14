package mat.server.validator.measure;

import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.MessageDelegate;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.service.MeasureLibraryService;
import mat.server.util.CQLUtil;
import mat.server.util.CompositeMeasureDetailUtil;
import mat.server.util.XmlProcessor;
import mat.shared.CompositeMeasurePackageValidationResult;
import mat.shared.SaveUpdateCQLResult;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class CompositeMeasurePackageValidator {
	
	private static final String EVERY_RISK_ADJUSTMENT_VARIABLE_PRESENT_IN_COMPOSITE_ERROR = "Every Risk Adjustment Variable in a component measure must also be present in the composite measure.";

	private static final String EVERY_SUPPLEMENTAL_DATA_ELEMENT_PRESENT_IN_COMPOSITE_ERROR = "Every Supplemental Data Element present in a component measure must also be present in the composite measure.";

	public static final String SUPPLEMENTAL_DATA_ELEMENT_TYPE_ERROR = "A Supplemental Data Element that has the same name as a Supplemental Data Element in a component measure must return the same type.";

	public static final String RISK_ADJUSTMENT_VARIABLE_TYPE_ERROR = "A Risk Adjustment Variable that has the same name as a Risk Adjustment Variable in a component measure must return the same type.";

	private static final String MEASURE_RISK_ADJUSTMENT_VARIABLES_CQLDEFINITION_XPATH = "/measure/riskAdjustmentVariables/cqldefinition";

	private static final String MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_CQLDEFINITION_XPATH = "/measure/supplementalDataElements/cqldefinition";

	@Autowired
	private CompositeMeasureValidator compositeMeasureValidator; 
	
	@Autowired
	private MeasureExportDAO measureExportDAO; 
	
	@Autowired
	private MeasureLibraryService measureLibraryService; 
	
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO; 
	
	@Autowired
	private CompositeMeasureDetailUtil compositeMeasureDetailUtil;
	
	private static final Logger logger = LoggerFactory.getLogger(CompositeMeasurePackageValidator.class);
		
	private CompositeMeasurePackageValidationResult result = new CompositeMeasurePackageValidationResult(); 

	public CompositeMeasurePackageValidationResult validate(String simpleXML) {
		result.getMessages().clear();
		try {	
			ManageCompositeMeasureDetailModel model = getCompositeMeasureDetailModel(simpleXML);
			validateMeasureDetails(model);
			validatePresenceOfComponentSupplementalDataElementDefinitionsInComposite(model, simpleXML);
			validatePresenceOfComponentRiskAdjustmentVariableDefinitionsInComposite(model, simpleXML);
			validateAllSupplementalDataElementsWithSameNameHaveSameType(model, simpleXML);
			validateAllRiskAdjustmentVariablesWithSameNameHaveSameType(model, simpleXML);
		} catch (Exception e) {
			result.getMessages().add(MessageDelegate.GENERIC_ERROR_MESSAGE);
			e.printStackTrace();
			logger.error("CompositeMeasurePackageValidationResult", e);
		}
				
		return result; 
	}
	
	private ManageCompositeMeasureDetailModel getCompositeMeasureDetailModel(String simpleXML) throws MarshalException, ValidationException, XPathExpressionException, IOException, MappingException {
		ManageCompositeMeasureDetailModel tempModel = compositeMeasureDetailUtil.convertXMLIntoCompositeMeasureDetailModel(simpleXML);
		replaceDashesInModelForMeasureIds(tempModel);
		ManageCompositeMeasureDetailModel model = measureLibraryService.getCompositeMeasure(tempModel.getId(), simpleXML);
		removeUnusedComponentsFromModel(model, tempModel);
		replaceDashesInModelForMeasureIds(model);
		return model; 

	}
	
	private void removeUnusedComponentsFromModel(ManageCompositeMeasureDetailModel model, ManageCompositeMeasureDetailModel modelFromSimpleXML) {
		Set<String> usedComponentIds = new HashSet<>(); 
		for(Result component : modelFromSimpleXML.getAppliedComponentMeasures()) {
			usedComponentIds.add(component.getId());
		}
		
		if(model.getAppliedComponentMeasures() != null) {
			model.getAppliedComponentMeasures().removeIf(m -> !usedComponentIds.contains(m.getId()));
		}
	}
			
	private void replaceDashesInModelForMeasureIds(ManageCompositeMeasureDetailModel model) {
		model.setId(model.getId().replace("-", ""));
		for(Result component : model.getAppliedComponentMeasures()) {
			component.setId(component.getId().replace("-", ""));
		}
	}
	
	private void validateMeasureDetails(ManageCompositeMeasureDetailModel model) {
		result.getMessages().addAll(compositeMeasureValidator.validateCompositeMeasureOnPackage(model).getMessages());
	}
	
	private void validatePresenceOfComponentSupplementalDataElementDefinitionsInComposite(ManageCompositeMeasureDetailModel model, String simpleXML) throws XPathExpressionException {
		Set<String> supplementalDataElementDefinitionNamesFromComposite = getSupplementalDataElementsFromSimpleXML(simpleXML);
	
		for(Result component : model.getAppliedComponentMeasures()) {
			String componentSimpleXML = measureExportDAO.findByMeasureId(component.getId()).getSimpleXML();
			Set<String> supplementalDataElementDefinitionNamesFromComponent = getSupplementalDataElementsFromSimpleXML(componentSimpleXML);		
			if(!isSetASubset(supplementalDataElementDefinitionNamesFromComponent, supplementalDataElementDefinitionNamesFromComposite)) {
				result.getMessages().add(EVERY_SUPPLEMENTAL_DATA_ELEMENT_PRESENT_IN_COMPOSITE_ERROR);
				return; 
			}
		}
	}
	
	private void validatePresenceOfComponentRiskAdjustmentVariableDefinitionsInComposite(ManageCompositeMeasureDetailModel model, String simpleXML) throws XPathExpressionException {
		Set<String> riskAdjustmentVariableDefinitionNamesFromComposite = getRiskAdjustmentVariablesFromSimpleXML(simpleXML);
		
		for(Result component : model.getAppliedComponentMeasures()) {
			String componentSimpleXML = measureExportDAO.findByMeasureId(component.getId()).getSimpleXML();
			Set<String> supplementalDataElementDefinitionNamesFromComponent = getRiskAdjustmentVariablesFromSimpleXML(componentSimpleXML);		
			if(!isSetASubset(supplementalDataElementDefinitionNamesFromComponent, riskAdjustmentVariableDefinitionNamesFromComposite)) {
				result.getMessages().add(EVERY_RISK_ADJUSTMENT_VARIABLE_PRESENT_IN_COMPOSITE_ERROR);
				return; 
			}
		}
	}
	
	public void validateAllSupplementalDataElementsWithSameNameHaveSameType(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel, String simpleXML) throws XPathExpressionException {
		CQLModel model = CQLUtilityClass.getCQLModelFromXML(simpleXML);
		SaveUpdateCQLResult cqlSaveUpdateResult = CQLUtil.parseQDMCQLLibraryForErrors(model, cqlLibraryDAO, new ArrayList<>());
		
		for(Result componentMeasure : manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
			String componentSimpleXML = measureExportDAO.findByMeasureId(componentMeasure.getId()).getSimpleXML();
			Set<String> compositeSDEDefinitionNames = getSupplementalDataElementsFromSimpleXML(simpleXML);
			Set<String> componentSDEDefinitionNames = getSupplementalDataElementsFromSimpleXML(componentSimpleXML);
			String componentMeasureId = componentMeasure.getId();
			
			if(!isDefinitionPresentInCompositeHaveSameReturnTypeInComponent(compositeSDEDefinitionNames, componentSDEDefinitionNames, componentMeasureId, model, cqlSaveUpdateResult)) {
				result.getMessages().add(SUPPLEMENTAL_DATA_ELEMENT_TYPE_ERROR);
				return; 
			}
		}
	}
	
	public void validateAllRiskAdjustmentVariablesWithSameNameHaveSameType(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel, String simpleXML) throws XPathExpressionException {
		CQLModel model = CQLUtilityClass.getCQLModelFromXML(simpleXML);
		SaveUpdateCQLResult cqlSaveUpdateResult = CQLUtil.parseQDMCQLLibraryForErrors(model, cqlLibraryDAO, new ArrayList<>());
		
		for(Result componentMeasure : manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
			String componentSimpleXML = measureExportDAO.findByMeasureId(componentMeasure.getId()).getSimpleXML();
			Set<String> compositeRAVDefinitionNames = getRiskAdjustmentVariablesFromSimpleXML(simpleXML);
			Set<String> componentRAVDefinitionNames = getRiskAdjustmentVariablesFromSimpleXML(componentSimpleXML);
			String componentMeasureId = componentMeasure.getId();
			
			if(!isDefinitionPresentInCompositeHaveSameReturnTypeInComponent(compositeRAVDefinitionNames, componentRAVDefinitionNames, componentMeasureId, model, cqlSaveUpdateResult)) {
				result.getMessages().add(RISK_ADJUSTMENT_VARIABLE_TYPE_ERROR);
				return; 
			}
		}
	}
	
	private boolean isDefinitionPresentInCompositeHaveSameReturnTypeInComponent(Set<String> compositeDefinitions, Set<String> componentDefinitions, String componentMeasureId, CQLModel model, SaveUpdateCQLResult result) {			
		CQLIncludeLibrary libraryToCheckReturnTypes = findLibraryAssociatedWithMeasureId(componentMeasureId, model);
		
		// if the library does not exist to check against because it was filtered out, or for some other reason, return true. 
		if(libraryToCheckReturnTypes == null) {
			return true; 
		}
		 		
		return isReturnTypesTheSame(compositeDefinitions, model, result, getReturnTypesForDefinitions(componentDefinitions, result, libraryToCheckReturnTypes)); 
	}

	private boolean isReturnTypesTheSame(Set<String> compositeDefinitions, CQLModel model, SaveUpdateCQLResult result, Map<String, String> componentMeasureDefinitionReturnTypes) {
		// go through all of the composite definitions and check if the return type in the component measure is equivalent if there happens to be one with the same name
		for(String compositeDefinition : compositeDefinitions) {
			String compositeDefinitionReturnType = result.getUsedCQLArtifacts().getNameToReturnTypeMap().get(model.getLibraryName() + "-" +  model.getVersionUsed()).get(compositeDefinition);
			String componentDefinitionReturnType = componentMeasureDefinitionReturnTypes.get(compositeDefinition);
			if(componentDefinitionReturnType != null && !compositeDefinitionReturnType.equals(componentDefinitionReturnType)) {
				return false; 
			}
		}
		
		return true;
	}

	private Map<String, String> getReturnTypesForDefinitions(Set<String> componentDefinitions, SaveUpdateCQLResult result, CQLIncludeLibrary libraryToCheckReturnTypes) {
		Map<String, String> componentMeasureReturnTypes = result.getUsedCQLArtifacts().getNameToReturnTypeMap().get(libraryToCheckReturnTypes.getCqlLibraryName() + "-" + libraryToCheckReturnTypes.getVersion());
		Map<String, String> componentMeasureDefinitionReturnTypes = new HashMap<>(); 
		for(String componentDefinition : componentDefinitions) {
			componentMeasureDefinitionReturnTypes.put(componentDefinition, componentMeasureReturnTypes.get(componentDefinition));
		}
		return componentMeasureDefinitionReturnTypes;
	}

	private CQLIncludeLibrary findLibraryAssociatedWithMeasureId(String componentMeasureId, CQLModel model) {
		for(CQLIncludeLibrary library : model.getCqlIncludeLibrarys()) {
			if(componentMeasureId.equals(library.getMeasureId()) && "true".equals(library.getIsComponent())) {
				return library; 
			}
		}
		
		return null;
	}
	
	private Set<String> getSupplementalDataElementsFromSimpleXML(String simpleXML) throws XPathExpressionException {
		return getDefinitionNames(simpleXML, MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_CQLDEFINITION_XPATH);
	}
	
	private Set<String> getRiskAdjustmentVariablesFromSimpleXML(String simpleXML) throws XPathExpressionException {
		return getDefinitionNames(simpleXML, MEASURE_RISK_ADJUSTMENT_VARIABLES_CQLDEFINITION_XPATH);
	}
	
	private Set<String> getDefinitionNames(String simpleXML, String xPath) throws XPathExpressionException {
		Set<String> definitionNames = new HashSet<>() ;
		XmlProcessor processor = new XmlProcessor(simpleXML);
		NodeList supplementalDataElementNodes = processor.findNodeList(processor.getOriginalDoc(), xPath);
		
		for(int i = 0; i < supplementalDataElementNodes.getLength(); i++) {
			Node current = supplementalDataElementNodes.item(i);
			String name = current.getAttributes().getNamedItem("displayName").getNodeValue();
			definitionNames.add(name);
		}
		
		return definitionNames;
	}
	
	/**
	 * Checks if set 1 is a subset of list 2. 
	 * @return true if list 1 is a subset of list 2, false otherwise. 
	 */
	private boolean isSetASubset(Set<String> set1, Set<String> set2) {
		// if set 1 is bigger than set 2, there is no way it can be a subset. 
		if(set1.size() > set2.size()) {
			return false; 
		}
		
		// for every value in set1, check if it is set 2. If it is not in set2, then it is not a subset. 
		for(String s : set1) {
			if(!set2.contains(s)) {
				return false; 
			}
		}
		
		return true; 
	}
	
	
	public CompositeMeasurePackageValidationResult getResult() {
		return result;
	}

	public void setResult(CompositeMeasurePackageValidationResult result) {
		this.result = result;
	}
}
