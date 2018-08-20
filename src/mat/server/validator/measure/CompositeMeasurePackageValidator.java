package mat.server.validator.measure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

@Component
public class CompositeMeasurePackageValidator {
	
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
	
	private static final Log logger = LogFactory.getLog(CompositeMeasurePackageValidator.class);
		
	private CompositeMeasurePackageValidationResult result = new CompositeMeasurePackageValidationResult(); 
	
	public CompositeMeasurePackageValidationResult validate(String simpleXML) {
		result.getMessages().clear();
		try {	
			ManageCompositeMeasureDetailModel model = getCompositeMeasureDetailModel(simpleXML);
			validateMeasureDetails(model);
			validatePresenceOfComponentSupplementalDataElementDefinitionsInComposite(model, simpleXML);
			validatePresenceOfComponentRiskAdjustmentVariableDefinitionsInComposite(model, simpleXML);
			validateAllSupplementalDataElementsWithSameNameHaveSameType(simpleXML);
			validateAllRiskAdjustmentVariablesWithSameNameHaveSameType(simpleXML);
		} catch (Exception e) {
			result.getMessages().add(MessageDelegate.GENERIC_ERROR_MESSAGE);
			e.printStackTrace();
			logger.error(e);
		}
				
		return result; 
	}
	
	private ManageCompositeMeasureDetailModel getCompositeMeasureDetailModel(String simpleXML) throws MarshalException, ValidationException, XPathExpressionException, IOException, MappingException {
		ManageCompositeMeasureDetailModel tempModel = compositeMeasureDetailUtil.covertXMLIntoCompositeMeasureDetailModel(simpleXML);
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
			for(int i = 0; i < model.getAppliedComponentMeasures().size(); i++) {
				if(!usedComponentIds.contains(model.getAppliedComponentMeasures().get(i).getId())) {
					model.getAppliedComponentMeasures().remove(i);
				}
			}		
		}
	}
			
	private void replaceDashesInModelForMeasureIds(ManageCompositeMeasureDetailModel model) {
		model.setId(model.getId().replace("-", ""));
		for(Result component : model.getAppliedComponentMeasures()) {
			component.setId(component.getId().replace("-", ""));
		}
	}
	
	private void validateMeasureDetails(ManageCompositeMeasureDetailModel model) {
		result.getMessages().addAll(compositeMeasureValidator.validateCompositeMeasure(model).getMessages());
	}
	
	private void validatePresenceOfComponentSupplementalDataElementDefinitionsInComposite(ManageCompositeMeasureDetailModel model, String simpleXML) throws XPathExpressionException {
		Set<String> supplementalDataElementDefinitionNamesFromComposite = getSupplementalDataElementsFromSimpleXML(simpleXML);
		
		for(Result component : model.getAppliedComponentMeasures()) {
			String componentSimpleXML = measureExportDAO.findByMeasureId(component.getId()).getSimpleXML();
			Set<String> supplementalDataElementDefinitionNamesFromComponent = getSupplementalDataElementsFromSimpleXML(componentSimpleXML);		
			if(!isSetASubset(supplementalDataElementDefinitionNamesFromComponent, supplementalDataElementDefinitionNamesFromComposite)) {
				result.getMessages().add("Every SDE present in a component measure must also be present in the composite measure.");
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
				result.getMessages().add("Every Risk Adjustment Variable in a component measure must also be present in the composite measure.");
				return; 
			}
		}
	}
	
	private void validateAllSupplementalDataElementsWithSameNameHaveSameType(String simpleXML) throws XPathExpressionException {
		if(!isDefinitionPresentInCompositeHaveSameReturnTypeInComponent(simpleXML, getSupplementalDataElementsFromSimpleXML(simpleXML))) {
			result.getMessages().add("A Supplemental Data Element that has the same name as a Supplemental Data Element in a component measure must return the same type.");
		}
	}
	
	private void validateAllRiskAdjustmentVariablesWithSameNameHaveSameType(String simpleXML) throws XPathExpressionException {
		if(!isDefinitionPresentInCompositeHaveSameReturnTypeInComponent(simpleXML, getRiskAdjustmentVariablesFromSimpleXML(simpleXML))) {
			result.getMessages().add("A Risk Adjustment Variable that has the same name as a Risk Adjustment Variable in a component measure must return the same type.");
		}
	}
	
	private boolean isDefinitionPresentInCompositeHaveSameReturnTypeInComponent(String simpleXML, Set<String> definitions) {
		CQLModel model = CQLUtilityClass.getCQLModelFromXML(simpleXML);
		SaveUpdateCQLResult result = CQLUtil.parseCQLLibraryForErrors(model, cqlLibraryDAO, new ArrayList<>());			
		List<Map<String, String>> componentMeasureReturnTypeMaps = new ArrayList<>(); 
		for(CQLIncludeLibrary library : model.getCqlIncludeLibrarys()) {
			if("true".equals(library.getIsComponent())) {
				componentMeasureReturnTypeMaps.add(result.getUsedCQLArtifacts().getNameToReturnTypeMap().get(library.getCqlLibraryName() + "-" + library.getVersion()));
			}
		}
		
		for(String definition : definitions) {
			String compositeReturnType = result.getUsedCQLArtifacts().getNameToReturnTypeMap().get(model.getLibraryName() + "-" +  model.getVersionUsed()).get(definition);
			
			for(Map<String, String> componentReturnTypeMap : componentMeasureReturnTypeMaps) {
				String componentReturnType = componentReturnTypeMap.get(definition);
				if(componentReturnType != null && !componentReturnType.equals(compositeReturnType)) {
					return false; 
				}
			}
		}
		
		return true; 
	}
	
	private Set<String> getSupplementalDataElementsFromSimpleXML(String simpleXML) throws XPathExpressionException {
		String supplementalDataElementXpath = "/measure/supplementalDataElements/cqldefinition";
		return getDefinitionNames(simpleXML, supplementalDataElementXpath);
	}
	
	private Set<String> getRiskAdjustmentVariablesFromSimpleXML(String simpleXML) throws XPathExpressionException {
		String riskAdjustmentVariablesXPath = "/measure/riskAdjustmentVariables/cqldefinition";
		return getDefinitionNames(simpleXML, riskAdjustmentVariablesXPath);
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
}
