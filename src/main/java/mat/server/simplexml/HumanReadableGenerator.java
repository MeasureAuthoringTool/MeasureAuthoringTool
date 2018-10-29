package mat.server.simplexml;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import freemarker.template.TemplateException;
import mat.client.shared.MatContext;
import mat.dao.clause.CQLLibraryDAO;
import mat.server.humanreadable.cql.HumanReadableCodeModel;
import mat.server.humanreadable.cql.HumanReadableExpressionModel;
import mat.server.humanreadable.cql.HumanReadableModel;
import mat.server.humanreadable.cql.HumanReadablePopulationCriteriaModel;
import mat.server.humanreadable.cql.HumanReadablePopulationModel;
import mat.server.humanreadable.cql.HumanReadableValuesetModel;
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
				model.setValuesetDataCriteriaList(getValuesetDataCriteria(processor));
				model.setCodeDataCriteriaList(getCodeDataCriteria(processor));
				model.setValuesetTerminologyList(getValuesetTerminology(processor));
				model.setCodeTerminologyList(getCodeTerminology(processor));
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
	
	private List<HumanReadableValuesetModel> getValuesetDataCriteria(XmlProcessor processor) throws XPathExpressionException {
		List<HumanReadableValuesetModel> valuesets = new ArrayList<>(); 
		NodeList elements = processor.findNodeList(processor.getOriginalDoc(), "/measure/elementLookUp/qdm[@code='false'][@datatype]");
		
		for(int i = 0; i < elements.getLength(); i++) {
			
			String datatype = elements.item(i).getAttributes().getNamedItem("datatype").getNodeValue(); 
			String name = elements.item(i).getAttributes().getNamedItem("name").getNodeValue(); 
			String oid = elements.item(i).getAttributes().getNamedItem("oid").getNodeValue(); 

			String version = "";
			if(elements.item(i).getAttributes().getNamedItem("version") != null){
				version = elements.item(i).getAttributes().getNamedItem("version").getNodeValue();
			}

			HumanReadableValuesetModel valueset = new HumanReadableValuesetModel(name, oid, version, datatype);
			valuesets.add(valueset);
		}
		
		valuesets.sort(Comparator.comparing(HumanReadableValuesetModel::getDataCriteriaDisplay));
		return valuesets; 
	}
	
	private List<HumanReadableCodeModel> getCodeDataCriteria(XmlProcessor processor) throws XPathExpressionException {
		List<HumanReadableCodeModel> codes = new ArrayList<>(); 
		NodeList elements = processor.findNodeList(processor.getOriginalDoc(),  "/measure/elementLookUp/qdm[@code='true'][@datatype]");
		
		for(int i = 0; i < elements.getLength(); i++) {
			NamedNodeMap attributeMap = elements.item(i).getAttributes();
			String datatype = attributeMap.getNamedItem("datatype").getNodeValue(); 
			if("attribute".equals(datatype)){
				datatype = "Attribute";
			}
			
			String name = attributeMap.getNamedItem("name").getNodeValue(); 
			String oid = attributeMap.getNamedItem("oid").getNodeValue(); 
			String codesystemVersion = attributeMap.getNamedItem("codeSystemVersion").getNodeValue();					
			String codesystemName = attributeMap.getNamedItem("taxonomy").getNodeValue();
			boolean isCodeSystemVersionIncluded = true;
			Node isCodeSystemVersionIncludedNode = attributeMap.getNamedItem("isCodeSystemVersionIncluded");
			if(isCodeSystemVersionIncludedNode != null) {
				isCodeSystemVersionIncluded = Boolean.parseBoolean(isCodeSystemVersionIncludedNode.getNodeValue());
			} 				 
			
			HumanReadableCodeModel code = new HumanReadableCodeModel(name, oid, codesystemName, isCodeSystemVersionIncluded, codesystemVersion, datatype);
			codes.add(code);
		}
		
		codes.sort(Comparator.comparing(HumanReadableCodeModel::getDataCriteriaDisplay));
		return codes; 
	}
	
	private List<HumanReadableValuesetModel> getValuesetTerminology(XmlProcessor processor) throws XPathExpressionException {
		List<HumanReadableValuesetModel> valuesets = new ArrayList<>(); 
		NodeList elements = processor.findNodeList(processor.getOriginalDoc(), "/measure/elementLookUp/qdm[@code=\"false\"]");
		
		for(int i = 0; i < elements.getLength(); i++) {
			Node current = elements.item(i);
			String name = current.getAttributes().getNamedItem("name").getNodeValue();
			String oid = current.getAttributes().getNamedItem("oid").getNodeValue();  
			String version = current.getAttributes().getNamedItem("version").getNodeValue(); 
			HumanReadableValuesetModel valueset = new HumanReadableValuesetModel(name, oid, version, "");
			valuesets.add(valueset);
		}
		
		valuesets.sort(Comparator.comparing(HumanReadableValuesetModel::getTerminologyDisplay));
		return valuesets;
	}
	
	private List<HumanReadableCodeModel> getCodeTerminology(XmlProcessor processor) throws XPathExpressionException {
		List<HumanReadableCodeModel> codes = new ArrayList<>(); 
		NodeList elements = processor.findNodeList(processor.getOriginalDoc(), "/measure/elementLookUp/qdm[@code=\"true\"]");
		for(int i = 0; i < elements.getLength(); i++) {
			Node current = elements.item(i);
			String name = current.getAttributes().getNamedItem("name").getNodeValue(); 
			String oid = current.getAttributes().getNamedItem("oid").getNodeValue();
			String codesystemName = current.getAttributes().getNamedItem("taxonomy").getNodeValue(); 
			String codesystemVersion = "";

			Node isCodeSystemVersionIncludedNode = current.getAttributes().getNamedItem("isCodeSystemVersionIncluded");
			boolean isCodeSystemVersionIncluded = true; // by default the code system should be included if the isCodeSystemIncluded tag does not exist
			if(isCodeSystemVersionIncludedNode != null) {
				isCodeSystemVersionIncluded = Boolean.parseBoolean(isCodeSystemVersionIncludedNode.getNodeValue());
			} 
			
			if(isCodeSystemVersionIncluded) {
				codesystemVersion = current.getAttributes().getNamedItem("codeSystemVersion").getNodeValue();
			}
			
			HumanReadableCodeModel model = new HumanReadableCodeModel(name, oid, codesystemName, isCodeSystemVersionIncluded, codesystemVersion, "");
			codes.add(model);
		}  
		
		codes.sort(Comparator.comparing(HumanReadableCodeModel::getTerminologyDisplay));
		return codes; 
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
