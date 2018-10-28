package mat.server.simplexml;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import freemarker.template.TemplateException;
import mat.client.shared.MatContext;
import mat.dao.clause.CQLLibraryDAO;
import mat.server.humanreadable.cql.HumanReadableExpressionModel;
import mat.server.humanreadable.cql.HumanReadableModel;
import mat.server.humanreadable.cql.HumanReadablePopulationCriteriaModel;
import mat.server.humanreadable.cql.HumanReadablePopulationModel;
import mat.server.humanreadable.cql.NewHumanReadableGenerator;
import mat.server.simplexml.cql.CQLHumanReadableGenerator;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;

@Component
public class HumanReadableGenerator {
	
	@Autowired NewHumanReadableGenerator humanReadableGenerator; 
	
	public String generateHTMLForPopulationOrSubtree(String measureId,
			String subXML, String measureXML, CQLLibraryDAO cqlLibraryDAO) {
		
		XmlProcessor subXMLProcessor = new XmlProcessor(subXML);
		String html = "";
		
		if(subXMLProcessor.getOriginalDoc().getDocumentElement().hasChildNodes()){			
			String firstNodeName = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild().getNodeName();
			System.out.println("firstNodeName:"+firstNodeName);
			
			if("cqldefinition".equals(firstNodeName) || "cqlfunction".equals(firstNodeName) || "cqlaggfunction".equals(firstNodeName)){
				html = CQLHumanReadableGenerator.generateHTMLForPopulation(measureId, subXMLProcessor, measureXML, cqlLibraryDAO);
			}else{
				html = HQMFHumanReadableGenerator.generateHTMLForPopulationOrSubtree(measureId,subXML,measureXML);
			}
		}else{
			return "<html></html>";
		}
		
		return html;
	}
	
	public String generateHTMLForMeasure(String measureId,String simpleXmlStr, String measureReleaseVersion, CQLLibraryDAO cqlLibraryDAO){
		
		String html = "";
		System.out.println("Generating human readable for ver:"+measureReleaseVersion);
		if(MatContext.get().isCQLMeasure(measureReleaseVersion)){
			try {
				XmlProcessor processor = new XmlProcessor(simpleXmlStr);
				Mapping mapping = new Mapping(); 
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("SimpleXMLHumanReadableModelMapping.xml"));
				Unmarshaller unmarshaller = new Unmarshaller(mapping);
				unmarshaller.setClass(HumanReadableModel.class);
				unmarshaller.setWhitespacePreserve(true);
				HumanReadableModel model = (HumanReadableModel) unmarshaller.unmarshal(new InputSource(new StringReader(simpleXmlStr)));
				model.setPopulationCriterias(getPopulationCriteriaModels(processor));
				model.setSupplementalDataElements(getSupplementalDataElements(processor));
				model.setRiskAdjustmentVariables(getRiskAdjustmentVariables(processor));
				html = humanReadableGenerator.generate(model);
			} catch (IOException | TemplateException | MappingException | MarshalException | ValidationException | XPathExpressionException e) {
				e.printStackTrace();
			}
		} else {
			html = HQMFHumanReadableGenerator.generateHTMLForMeasure(measureId,simpleXmlStr);
		}
		
		return html;
	}
	
	private List<HumanReadableExpressionModel> getSupplementalDataElements(XmlProcessor processor) throws XPathExpressionException {
		List<HumanReadableExpressionModel> supplementalDataElements = new ArrayList<>(); 
		if(processor.findNode(processor.getOriginalDoc(), "/measure/supplementalDataElements") != null) {
			NodeList supplementalDataElementNodes = processor.findNodeList(processor.getOriginalDoc(), "/measure/supplementalDataElements/cqldefinition");
			
			for(int i = 0; i < supplementalDataElementNodes.getLength(); i++) {
				Node sde = supplementalDataElementNodes.item(i);
				supplementalDataElements.add(getExpressionModel(processor, sde));
			}
		}
		return supplementalDataElements;
	}

	private HumanReadableExpressionModel getExpressionModel(XmlProcessor processor, Node sde)
			throws XPathExpressionException {
		String uuid = sde.getAttributes().getNamedItem("uuid").getNodeValue();
		String name = sde.getAttributes().getNamedItem("displayName").getNodeValue();
		String logic = processor.findNode(processor.getOriginalDoc(), "/measure/cqlLookUp//definition[@id='"+ uuid +"']/logic").getTextContent();
		HumanReadableExpressionModel expression = new HumanReadableExpressionModel(name, logic);
		return expression;
	}
	
	private List<HumanReadableExpressionModel> getRiskAdjustmentVariables(XmlProcessor processor) throws XPathExpressionException {
		List<HumanReadableExpressionModel> riskAdjustmentVariables = new ArrayList<>(); 
		
		if(processor.findNode(processor.getOriginalDoc(), "/measure/riskAdjustmentVariables") != null) {
			NodeList riskAdjustmentVariableNodes = processor.findNodeList(processor.getOriginalDoc(), "/measure/riskAdjustmentVariables/cqldefinition");
			for(int i = 0; i < riskAdjustmentVariableNodes.getLength(); i++) {
				Node rav = riskAdjustmentVariableNodes.item(i);
				riskAdjustmentVariables.add(getExpressionModel(processor, rav));
			}
		}
		
		return riskAdjustmentVariables;
	}
	
	
	
	private List<HumanReadablePopulationCriteriaModel> getPopulationCriteriaModels(XmlProcessor processor) throws XPathExpressionException {
		List<HumanReadablePopulationCriteriaModel> groups = new ArrayList<>();
		
		NodeList groupNodes = processor.findNodeList(processor.getOriginalDoc(), "/measure/measureGrouping/group");
		
		for(int i = 0; i < groupNodes.getLength(); i++) {
			Node group = groupNodes.item(i);
			
			List<HumanReadablePopulationModel> populations = new ArrayList<>();
			
			NodeList populationNodes = group.getChildNodes();
			for(int j = 0; j < populationNodes.getLength(); j++) {
				Node populationNode = populationNodes.item(j);
				String expressionName = "";
				String expressionUUID = "";
				String logic = "";
				String aggregation = null; 
				boolean isInGroup = true; 
				if(populationNode.getAttributes().getNamedItem("isInGrouping") != null) {
					isInGroup= Boolean.parseBoolean(populationNode.getAttributes().getNamedItem("isInGrouping").getNodeValue());
				}
						


				String populationName = populationNode.getAttributes().getNamedItem("displayName").getNodeValue();
				if(populationName.contains("Measure Observation")) {
					
					Node functionNode = null;
					Node aggregationNode = null;
					if("cqlaggfunction".equals(populationNode.getFirstChild().getNodeName())) {
						aggregationNode = populationNode.getFirstChild();
						aggregation = aggregationNode.getAttributes().getNamedItem("displayName").getNodeValue();
						functionNode = aggregationNode.getFirstChild();
					} else {
						functionNode = populationNode.getFirstChild();
					}
					
					expressionName = functionNode.getAttributes().getNamedItem("displayName").getNodeValue();
					expressionUUID = functionNode.getAttributes().getNamedItem("uuid").getNodeValue();
					logic = processor.findNode(processor.getOriginalDoc(), "/measure/cqlLookUp//function[@id='"+ expressionUUID +"']/logic").getTextContent();
					
					if(aggregation != null) {
						logic = aggregation + " (\n  " + logic + "\n)"; // for measures, add in the aggregation
					}
				} else  {
					Node definitionNode = populationNode.getFirstChild();
					if(populationName.contains("stratum")) {
						if(populationName.equals("stratum")) {
							populationName = populationName + " 1"; // we need to enumerate the the stratum, and if there is only one stratum, it doesn't have a number in the name.... 
						}
						
						populationName = populationName.replace("stratum", "Stratification"); // for some reason the simple xml uses "stratum" but it really should be "Stratification"
					}

					if(definitionNode != null) {
						
						expressionName = definitionNode.getAttributes().getNamedItem("displayName").getNodeValue();
						expressionUUID = definitionNode.getAttributes().getNamedItem("uuid").getNodeValue();
						logic = processor.findNode(processor.getOriginalDoc(), "/measure/cqlLookUp//definition[@id='"+ expressionUUID +"']/logic").getTextContent();
					}
				}
				
				HumanReadablePopulationModel population = new HumanReadablePopulationModel(populationName, logic, expressionName, expressionUUID, aggregation, isInGroup);
				populations.add(population);
			}
			
			HumanReadablePopulationCriteriaModel populationCriteria = new HumanReadablePopulationCriteriaModel("Population Criteria " + (i + 1), populations);
			groups.add(populationCriteria);
		}
		
		return groups; 
	}
	
}
