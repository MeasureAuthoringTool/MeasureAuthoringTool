package mat.server.service.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.client.clause.clauseworkspace.presenter.ClauseConstants;
import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.dao.clause.MeasureXMLDAO;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.clause.MeasureXML;
import mat.server.service.PackagerService;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PackagerServiceImpl implements PackagerService {

	private static final Log logger = LogFactory.getLog(PackagerServiceImpl.class);
	private static final String SUPPLEMENT_DATA_ELEMENTS = "supplementalDataElements";
	private static final String XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_ELEMENTREF = "/measure/supplementalDataElements/elementRef/@id";
	private static final String XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_EXPRESSION = "/measure/supplementalDataElements/elementRef[@id";
	private static final String MEASURE ="measure";
	@Autowired 
	private MeasureXMLDAO measureXMLDAO;
	
	/**
	 * Creates measureGrouping XML chunk from MeasurePackageDetail using castor and "MeasurePackageClauseDetail.xml" mapping file.
	 * Finds the Group Node in the Measure_Xml using the sequence number from MeasurePackageDetail
	 * if the Group present deletes that Group in Measure_Xml and appends the new Group from measureGrouping XML to the measureGrouping node in Measure_Xml
	 * if the Group not Present appends the new Group from measureGrouping XML to the parent measureGrouping node in Measure_XML
	 * Finally Save the Measure_xml
	 */
	@Override
	public void save(MeasurePackageDetail detail) {
		
		MeasureXML measureXML = measureXMLDAO.findForMeasure(detail.getMeasureId());
		XmlProcessor  processor = new XmlProcessor(measureXML.getMeasureXMLAsString());
		Node groupNode = null;
		Node measureGroupingNode = null;
		try {
			//fetches the Group node from Measure_XML with the sequence number from MeasurePackageDetail
			groupNode = processor.findNode(processor.getOriginalDoc(), XmlProcessor.XPATH_GROUP_SEQ_START + detail.getSequence() +  XmlProcessor.XPATH_GROUP_SEQ_END);			
			//fetches the MeasureGrouping node from the Measure_xml
			measureGroupingNode = processor.findNode(processor.getOriginalDoc(), XmlProcessor.XPATH_MEASURE_GROUPING);
		} catch (XPathExpressionException e) {
			logger.info("Xpath Expression is incorrect" + e);
		}
		if(null != groupNode && groupNode.hasChildNodes()){
			logger.info("Removing Group with seq number" + detail.getSequence());
			measureGroupingNode.removeChild(groupNode);
		}
		String measureGroupingXml = createGroupingXml(detail);//Converts MeasurePackageDetail to measureGroupingXml 
		XmlProcessor measureGrpProcessor = new XmlProcessor(measureGroupingXml);
		Node newGroupNode = measureGrpProcessor.getOriginalDoc().getElementsByTagName("measureGrouping").item(0).getFirstChild();
		measureGroupingNode.appendChild(processor.getOriginalDoc().importNode(newGroupNode, true));
		logger.info("new Group appended");
		String xml = measureGrpProcessor.transform(processor.getOriginalDoc());
		measureXML.setMeasureXMLAsByteArray(xml);
		measureXMLDAO.save(measureXML);
	}

	
	private String createGroupingXml(
			MeasurePackageDetail detail) {
		Collections.sort(detail.getPackageClauses());
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("MeasurePackageClauseDetail.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
	        marshaller.marshal(detail);
	        logger.info("Marshalling of MeasurePackageDetail is successful.." + stream.toString());
		} catch (Exception e) {
			if(e instanceof IOException){
				logger.info("Failed to load MeasurePackageClauseDetail.xml" + e);
			}else if(e instanceof MappingException){
				logger.info("Mapping Failed" + e);
			}else if(e instanceof MarshalException){
				logger.info("Unmarshalling Failed" + e);
			}else if(e instanceof ValidationException){
				logger.info("Validation Exception" + e);
			}else{
				logger.info("Other Exception" + e);
			}
		} 
		return stream.toString();
	}
	

	/**
	 * 1) Loads the MeasureXml from DB and converts into Xml Document Object
	 * 2) XPATH retrieves all Clause nodes in Measure_Xml except for  Clause type "stratum"
	 * 3) Creates a list of MeasurePackageClauseDetail object with the attributes from Clause Nodes, which is used to show on the Measure Packager Screen Clause Box on left
	 * 4) XPATH finds the group nodes that are not matching with the the Clause nodes but comparing the uuids, the found group nodes are deleted from MeasureXml
	 * 5) XPATH finds the remaining groups in MeasureXml, converted into list of MeasurePackageDetail object using the groups child node attributes. this list is used to display the top Groupings with seq number on Page 
	 * 6) The MeasurePackageClauseDetail list and MeasurePackageDetail list is set into MeasurePackageOverview object and returned to Page,
	 * 
	 */
	@Override
	public MeasurePackageOverview buildOverviewForMeasure(String measureId) {
		
		MeasurePackageOverview overview = new MeasurePackageOverview();
		
		List<MeasurePackageClauseDetail> clauses = new ArrayList<MeasurePackageClauseDetail>();
		List<MeasurePackageDetail> pkgs = new ArrayList<MeasurePackageDetail>();
		// Load Measure Xml
		MeasureXML measureXML = measureXMLDAO.findForMeasure(measureId);
		XmlProcessor  processor = new XmlProcessor(measureXML.getMeasureXMLAsString());
		boolean isGroupRemoved = false;
		try {
			NodeList measureClauses = processor.findNodeList(processor.getOriginalDoc(), XmlProcessor.XPATH_MEASURE_CLAUSE);
			if(null != measureClauses && measureClauses.getLength() > 0){
				String xpathGrpUuid = XmlProcessor.XPATH_FIND_GROUP_CLAUSE;
				for (int i = 0; i < measureClauses.getLength(); i++) {
					NamedNodeMap namedNodeMap = measureClauses.item(i).getAttributes();
					Node uuidNode = namedNodeMap.getNamedItem(ClauseConstants.UUID);
					Node displayNameNode = namedNodeMap.getNamedItem(ClauseConstants.DISPLAY_NAME);
					Node typeNode = namedNodeMap.getNamedItem(ClauseConstants.TYPE);
					clauses.add(createMeasurePackageClauseDetail(uuidNode.getNodeValue(), displayNameNode.getNodeValue(), typeNode.getNodeValue()));	
					xpathGrpUuid = xpathGrpUuid + "@uuid != '" + uuidNode.getNodeValue() + "' and";
				}
				xpathGrpUuid = xpathGrpUuid.substring(0, xpathGrpUuid.lastIndexOf(" and")).concat("]]");
				// delete groups which doesn't have the measure clauses.
				 NodeList toRemoveGroups = processor.findNodeList(processor.getOriginalDoc(), xpathGrpUuid);
				 if(toRemoveGroups != null && toRemoveGroups.getLength() > 0){
					 Node measureGroupingNode = toRemoveGroups.item(0).getParentNode();
					 for (int i = 0; i < toRemoveGroups.getLength(); i++) {
						measureGroupingNode.removeChild(toRemoveGroups.item(i));
						isGroupRemoved = true;
					}
				 }
			}
			
			NodeList measureGroups = processor.findNodeList(processor.getOriginalDoc(), XmlProcessor.XPATH_MEASURE_GROUPING_GROUP);
			Map<Integer, MeasurePackageDetail> seqDetailMap = 
				new HashMap<Integer, MeasurePackageDetail>();
			
			if(measureGroups != null && measureGroups.getLength() > 0){
				for (int i = 0; i < measureGroups.getLength(); i++) {
					NamedNodeMap groupAttrs = measureGroups.item(i).getAttributes();
					Integer seq = Integer.parseInt(groupAttrs.getNamedItem("sequence").getNodeValue());
					MeasurePackageDetail detail = seqDetailMap.get(seq);
					if(detail == null) {
						detail = new MeasurePackageDetail();
						detail.setSequence(Integer.toString(seq));
						detail.setMeasureId(measureId);
						seqDetailMap.put(seq, detail);
						pkgs.add(detail);
					}
					NodeList pkgClauses = measureGroups.item(i).getChildNodes();
					for (int j = 0; j < pkgClauses.getLength(); j++) {
						NamedNodeMap pkgClauseMap = pkgClauses.item(j).getAttributes();
						detail.getPackageClauses().add(createMeasurePackageClauseDetail(pkgClauseMap.getNamedItem(ClauseConstants.UUID).getNodeValue()
								, pkgClauseMap.getNamedItem("name").getNodeValue(), pkgClauseMap.getNamedItem(ClauseConstants.TYPE).getNodeValue()));
					}
				}	
			}
		} catch (XPathExpressionException e) {
			logger.info("Xpath Expression is incorrect" + e);
		}
		
		Collections.sort(pkgs);
		overview.setClauses(clauses);
		overview.setPackages(pkgs);
		Map<String,ArrayList<QualityDataSetDTO>> finalMap = getIntersectionOfQDMAndSDE(measureId);	
		overview.setQdmElements(finalMap.get("QDM"));
		overview.setSuppDataElements(finalMap.get("SDE"));
		if(isGroupRemoved){
			measureXML.setMeasureXMLAsByteArray(processor.transform(processor.getOriginalDoc()));
			measureXMLDAO.save(measureXML);	
		}
		return overview;
	}

	private Map<String,ArrayList<QualityDataSetDTO>> getIntersectionOfQDMAndSDE(String measureId){
		MeasureXML measureXML = measureXMLDAO.findForMeasure(measureId);
		Map<String,ArrayList<QualityDataSetDTO>> finalMap = new HashMap<String,ArrayList<QualityDataSetDTO>>();
		XmlProcessor processor = new XmlProcessor(measureXML.getMeasureXMLAsString());
		finalMap = processor.sortSDEAndQDMsForMeasurePackager();
		logger.info("finalMap()of QualityDataSetDTO ::"+ finalMap.size());
		return finalMap;
		
	}


	private MeasurePackageClauseDetail createMeasurePackageClauseDetail(String id, String name, String type) {
		MeasurePackageClauseDetail detail = new MeasurePackageClauseDetail();
		detail.setId(id);
		detail.setName(name);
		detail.setType(type);
		return detail;
	}
	

	@Override
	public void delete(MeasurePackageDetail detail) {
		MeasureXML measureXML = measureXMLDAO.findForMeasure(detail.getMeasureId());
		XmlProcessor  processor = new XmlProcessor(measureXML.getMeasureXMLAsString());
		Node groupNode = null;
		try {
			groupNode = processor.findNode(processor.getOriginalDoc(), XmlProcessor.XPATH_GROUP_SEQ_START + detail.getSequence() +  XmlProcessor.XPATH_GROUP_SEQ_END);			
		} catch (XPathExpressionException e) {
			logger.info("Xpath Expression is incorrect" + e);
		}
		if(groupNode != null){
			Node measureGroupingNode = groupNode.getParentNode();
			measureGroupingNode.removeChild(groupNode);
		}
		String xml = processor.transform(processor.getOriginalDoc());
		measureXML.setMeasureXMLAsByteArray(xml);
		measureXMLDAO.save(measureXML);
	}

	
	@Override
	public void saveQDMData(MeasurePackageDetail detail) {
		ArrayList<QualityDataSetDTO> supplementDataElementsAll = (ArrayList<QualityDataSetDTO>) detail.getSuppDataElements();
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		wrapper.setQualityDataDTO(supplementDataElementsAll);
		ByteArrayOutputStream stream = convertQDMOToSuppleDataXML(wrapper);
		MeasureXML measureXML = measureXMLDAO.findForMeasure(detail.getMeasureId());
		XmlProcessor  processor = new XmlProcessor(measureXML.getMeasureXMLAsString());
		if(supplementDataElementsAll.size() >0){
			processor.replaceNode(stream.toString(), SUPPLEMENT_DATA_ELEMENTS, MEASURE);
		}else{
			
			try {
				// In case all elements from SupplementDataElements are moved to QDM, this will remove all.
				javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
				NodeList nodesSupplementalData = (NodeList) xPath.evaluate(XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_ELEMENTREF, processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODESET);
				for(int i=0 ;i<nodesSupplementalData.getLength();i++){
					String xPathString = XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_EXPRESSION.concat("='").concat(nodesSupplementalData.item(i).getNodeValue().toString()).concat("']");
					Node newNode = processor.findNode(processor.getOriginalDoc(), xPathString);
					Node parentNode = newNode.getParentNode();
					parentNode.removeChild(newNode);
				}
			} catch (XPathExpressionException e) {
				
				e.printStackTrace();
			}
		}
		measureXML.setMeasureXMLAsByteArray(processor.transform(processor.getOriginalDoc()));
		measureXMLDAO.save(measureXML);	
	}
	
	 /**
     * Method to create XML from QualityDataModelWrapper object for supplementalDataElement .
     * */
    private ByteArrayOutputStream convertQDMOToSuppleDataXML(QualityDataModelWrapper qualityDataSetDTO) {
		logger.info("In PackagerServiceImpl.convertQDMOToSuppleDataXML()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("QDMToSupplementDataMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
	        marshaller.marshal(qualityDataSetDTO);
	        logger.info("Marshalling of QualityDataSetDTO is successful in convertQDMOToSuppleDataXML()" + stream.toString());
		} catch (Exception e) {
			if(e instanceof IOException){
				logger.info("Failed to load QualityDataModelMapping.xml in convertQDMOToSuppleDataXML()" + e);
			}else if(e instanceof MappingException){
				logger.info("Mapping Failed in convertQDMOToSuppleDataXML()" + e);
			}else if(e instanceof MarshalException){
				logger.info("Unmarshalling Failed in convertQDMOToSuppleDataXML()" + e);
			}else if(e instanceof ValidationException){
				logger.info("Validation Exception in convertQDMOToSuppleDataXML()" + e);
			}else{
				logger.info("Other Exception in convertQDMOToSuppleDataXML()" + e);
			}
		} 
		logger.info("Exiting PackagerServiceImpl.convertQDMOToSuppleDataXML");
		return stream;
	}
	
}
