package mat.server.service.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.client.measurepackage.service.MeasurePackageSaveResult;
import mat.client.shared.MatContext;
import mat.dao.clause.MeasureXMLDAO;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.RiskAdjustmentDTO;
import mat.model.clause.MeasureXML;
import mat.server.service.PackagerService;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;
import mat.shared.ConstantMessages;
import mat.shared.MeasurePackageClauseValidator;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
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

// TODO: Auto-generated Javadoc
/**
 * The Class PackagerServiceImpl.
 */
public class PackagerServiceImpl implements PackagerService {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(PackagerServiceImpl.class);
	
	/** The Constant MEASURE. */
	private static final String MEASURE = "measure";
	
	/** The Constant SUPPLEMENT_DATA_ELEMENTS. */
	private static final String SUPPLEMENT_DATA_ELEMENTS = "supplementalDataElements";
	
	/** The Constant RISK_ADJUSTMENT_VARIABLES. */
	private static final String RISK_ADJUSTMENT_VARIABLES = "riskAdjustmentVariables";
	
	/** The Constant XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_ELEMENTREF. */
	private static final String XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_ELEMENTREF = "/measure/supplementalDataElements/elementRef/@id";
	
	/** The Constant XPATH_MEASURE_RISK_ADJ_VARIABLES. */
	private static final String XPATH_MEASURE_RISK_ADJ_VARIABLES = "/measure/riskAdjustmentVariables/subTreeRef/@id";
	
	/** The Constant XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_EXPRESSION. */
	private static final String XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_EXPRESSION = "/measure/supplementalDataElements/elementRef[@id";
	
	/** The Constant XPATH_MEASURE_RISK_ADJ_VARIABLES_EXPRESSION. */
	private static final String XPATH_MEASURE_RISK_ADJ_VARIABLES_EXPRESSION = "/measure/riskAdjustmentVariables/subTreeRef[@id";
	/** The Constant XPATH_MEASURE_ELEMENT_LOOKUP_QDM. */
	private static final String XPATH_MEASURE_ELEMENT_LOOKUP_QDM = "/measure/elementLookUp/qdm";
	
	/** The Constant XPATH_MEASURE_SUBTREE_LOOKUP_CLAUSE. */
	private static final String XPATH_MEASURE_SUBTREE_LOOKUP_CLAUSE = "/measure/subTreeLookUp/subTree";
	
	/** The Constant XPATH_MEASURE_RISK_ADJSUTMENT_VARIABLE. */
	private static final String XPATH_MEASURE_RISK_ADJSUTMENT_VARIABLE="/measure/riskAdjustmentVariables/subTreeRef";	
	/** The Constant XPATH_SD_ELEMENTS_ELEMENTREF. */
	private static final String XPATH_SD_ELEMENTS_ELEMENTREF = "/measure/supplementalDataElements/elementRef";
	
	/** The Constant INSTANCE. */
	private static final String INSTANCE = "instance";
	
	/** The Constant UUID. */
	private static final String UUID_STRING = "uuid";
	
	/** The measure xmldao. */
	@Autowired
	private MeasureXMLDAO measureXMLDAO;
	
	/**
	 * 1) Loads the MeasureXml from DB and converts into Xml Document Object
	 * 2) XPATH retrieves all Clause nodes in Measure_Xml except for  Clause type "stratum"
	 * 3) Creates a list of MeasurePackageClauseDetail object with the attributes from Clause Nodes,
	 * which is used to show on the Measure Packager Screen Clause Box on left
	 * 4) XPATH finds the group nodes that are not matching with the the Clause
	 * nodes but comparing the uuids, the found group nodes are deleted from MeasureXml
	 * 5) XPATH finds the remaining groups in MeasureXml, converted into list of
	 * MeasurePackageDetail object using the groups child node attributes.
	 * this list is used to display the top Groupings with seq number on Page
	 * 6) The MeasurePackageClauseDetail list and MeasurePackageDetail list is
	 * set into MeasurePackageOverview object and returned to Page,
	 * @param measureId - {@link String}.
	 * @return {@link MeasurePackageOverview}.
	 */
	@Override
	public MeasurePackageOverview buildOverviewForMeasure(String measureId) {
		
		MeasurePackageOverview overview = new MeasurePackageOverview();
		
		List<MeasurePackageClauseDetail> clauses = new ArrayList<MeasurePackageClauseDetail>();
		List<MeasurePackageDetail> pkgs = new ArrayList<MeasurePackageDetail>();
		//get all the list of allowed populations at package
		List<String> allowedPopulationsInPackage = MatContext.get().getAllowedPopulationsInPackage();
		// Load Measure Xml
		MeasureXML measureXML = measureXMLDAO.findForMeasure(measureId);
		XmlProcessor  processor = new XmlProcessor(measureXML.getMeasureXMLAsString());
		boolean isGroupRemoved = false;
		List<QualityDataSetDTO> qdmSelectedList;
		try {
			// get all CLAUSE type nodes except for Stratum.
			NodeList measureClauses = processor.findNodeList(processor.getOriginalDoc(),
					XmlProcessor.XPATH_MEASURE_CLAUSE);
			if ((null != measureClauses) && (measureClauses.getLength() > 0)) {
				qdmSelectedList = new ArrayList<QualityDataSetDTO>();
				// find the GROUP/PACKAGECLAUSES that are not in the main CLAUSE nodes using the clause node UUID
				String xpathGrpUuid = XmlProcessor.XPATH_FIND_GROUP_CLAUSE;
				for (int i = 0; i < measureClauses.getLength(); i++) {
					NamedNodeMap namedNodeMap = measureClauses.item(i).getAttributes();
					Node uuidNode = namedNodeMap.getNamedItem(PopulationWorkSpaceConstants.UUID);
					Node displayNameNode = namedNodeMap.getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME);
					Node typeNode = namedNodeMap.getNamedItem(PopulationWorkSpaceConstants.TYPE);
					Node associatedClauseUUIDNode = namedNodeMap.getNamedItem("associatedPopulationUUID");
					
					String associatedClauseUUID = null;
					if(associatedClauseUUIDNode != null){
						associatedClauseUUID = associatedClauseUUIDNode.getNodeValue();
					}
					
					if(typeNode == null)
					{
						clauses.add(createMeasurePackageClauseDetail(
								uuidNode.getNodeValue(), displayNameNode.getNodeValue(), XmlProcessor.STRATIFICATION,
								associatedClauseUUID,qdmSelectedList));
						
					} else if(allowedPopulationsInPackage.contains(typeNode.getNodeValue())){//filter unAllowed populations in package
						clauses.add(createMeasurePackageClauseDetail(
								uuidNode.getNodeValue(), displayNameNode.getNodeValue(), typeNode.getNodeValue(),
								associatedClauseUUID,qdmSelectedList));
					}
					//adding all Clause type uuid's
					xpathGrpUuid = xpathGrpUuid + "@uuid != '" + uuidNode.getNodeValue() + "' and";
				}
				xpathGrpUuid = xpathGrpUuid.substring(0, xpathGrpUuid.lastIndexOf(" and")).concat("]]");
				// delete groups which doesn't have the measure clauses.
				NodeList toRemoveGroups = processor.findNodeList(processor.getOriginalDoc(), xpathGrpUuid);
				// if the UUID's of Clause nodes does not match the UUID's
				//of Group/Package Clause, remove the Grouping completely
				if ((toRemoveGroups != null) && (toRemoveGroups.getLength() > 0)) {
					Node measureGroupingNode = toRemoveGroups.item(0).getParentNode();
					for (int i = 0; i < toRemoveGroups.getLength(); i++) {
						measureGroupingNode.removeChild(toRemoveGroups.item(i));
						isGroupRemoved = true;
					}
				}
			}
			
			NodeList measureGroups = processor.findNodeList(processor.getOriginalDoc(),
					XmlProcessor.XPATH_MEASURE_GROUPING_GROUP); // XPath to get all Group
			Map<Integer, MeasurePackageDetail> seqDetailMap =
					new HashMap<Integer, MeasurePackageDetail>();
			// iterate through the measure groupings and get the sequence number
			//attribute and insert in a map with sequence as key and MeasurePackageDetail as value
			if ((measureGroups != null) && (measureGroups.getLength() > 0)) {
				for (int i = 0; i < measureGroups.getLength(); i++) {
					NamedNodeMap groupAttrs = measureGroups.item(i).getAttributes();
					Integer seq = Integer.parseInt(groupAttrs.getNamedItem("sequence").getNodeValue());
					MeasurePackageDetail detail = seqDetailMap.get(seq);
					if (detail == null) {
						detail = new MeasurePackageDetail();
						detail.setSequence(Integer.toString(seq));
						detail.setMeasureId(measureId);
						seqDetailMap.put(seq, detail);
						pkgs.add(detail);
					}
					NodeList pkgClauses = measureGroups.item(i).getChildNodes();
					//Iterate through the PACKAGECLAUSE nodes and  convert it into
					//MeasurePackageClauseDetail add it to the list in MeasurePackageDetail
					for (int j = 0; j < pkgClauses.getLength(); j++) {
						qdmSelectedList = new ArrayList<QualityDataSetDTO>();
						if (!PopulationWorkSpaceConstants.PACKAGE_CLAUSE_NODE.equals(
								pkgClauses.item(j).getNodeName())) {
							// group node can contain tab or new lines
							// which can be counted as it's child.Those should be filtered.
							continue;
						}
						
						NodeList itemCountNodeList = pkgClauses.item(j).getChildNodes();
						for(int k = 0; k < itemCountNodeList.getLength(); k++){
							if(itemCountNodeList.item(k).getNodeName().equals("itemCount")){
								NodeList elementRefNode = itemCountNodeList.item(k).getChildNodes();
								for(int l = 0; l < elementRefNode.getLength(); l++){
									QualityDataSetDTO qdmSet = new QualityDataSetDTO();
									Node newNode = elementRefNode.item(l);
									qdmSet.setCodeListName(newNode.getAttributes().getNamedItem("name").getNodeValue());
									qdmSet.setDataType(newNode.getAttributes().getNamedItem("dataType").getNodeValue());
									qdmSet.setUuid(newNode.getAttributes().getNamedItem("id").getNodeValue());
									qdmSet.setOid(newNode.getAttributes().getNamedItem("oid").getNodeValue());
									if(newNode.getAttributes().getNamedItem("instance")!=null){
										qdmSet.setOccurrenceText(newNode.getAttributes().getNamedItem("instance").getNodeValue());
									}
									qdmSelectedList.add(qdmSet);
								}
							}
						}
						
						NamedNodeMap pkgClauseMap = pkgClauses.item(j).getAttributes();
						Node associatedClauseNode = pkgClauseMap.getNamedItem("associatedPopulationUUID");
						String associatedClauseNodeUuid = null;
						if(associatedClauseNode != null) {
							associatedClauseNodeUuid = associatedClauseNode.getNodeValue();
						}
						detail.getPackageClauses().add(
								createMeasurePackageClauseDetail(
										pkgClauseMap.getNamedItem(
												PopulationWorkSpaceConstants.UUID).getNodeValue()
												, pkgClauseMap.getNamedItem("name").
												getNodeValue(), pkgClauseMap.getNamedItem(
														PopulationWorkSpaceConstants.TYPE).getNodeValue(),
														associatedClauseNodeUuid,
														qdmSelectedList));
					}
				}
			}
		} catch (XPathExpressionException e) {
			logger.info("Xpath Expression is incorrect" + e);
		}
		Collections.sort(pkgs);
		overview.setClauses(clauses);
		overview.setPackages(pkgs);
		Map<String, ArrayList<QualityDataSetDTO>> finalMap = getIntersectionOfQDMAndSDE(processor, measureId);
		Map<String, ArrayList<RiskAdjustmentDTO>> clauseMap = getAllClauseList(processor, measureId);
		overview.setQdmElements(finalMap.get("QDM"));
		overview.setSuppDataElements(finalMap.get("SDE"));
		overview.setSubTreeClauseList(clauseMap.get("SUBTREEREF"));
		overview.setRiskAdjList(clauseMap.get("RISKADJ"));
		if (isGroupRemoved) {
			measureXML.setMeasureXMLAsByteArray(processor.transform(processor.getOriginalDoc()));
			measureXMLDAO.save(measureXML);
		}
		return overview;
	}
	/**
	 * Method to create XML from QualityDataModelWrapper object for
	 * supplementalDataElement .
	 * 
	 * @param qualityDataSetDTO
	 *            the quality data set dto
	 * @return the byte array output stream
	 */
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
			if (e instanceof IOException) {
				logger.info("Failed to load QualityDataModelMapping.xml in convertQDMOToSuppleDataXML()" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed in convertQDMOToSuppleDataXML()" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed in convertQDMOToSuppleDataXML()" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception in convertQDMOToSuppleDataXML()" + e);
			} else {
				logger.info("Other Exception in convertQDMOToSuppleDataXML()" + e);
			}
		}
		logger.info("Exiting PackagerServiceImpl.convertQDMOToSuppleDataXML");
		return stream;
	}
	
	/**
	 * Convertclause to risk adj var xml.
	 *
	 * @param riskAdjVarDTO the risk adj var dto
	 * @return the byte array output stream
	 */
	private ByteArrayOutputStream convertclauseToRiskAdjVarXML(QualityDataModelWrapper riskAdjVarDTO){

		logger.info("In PackagerServiceImpl.convertclauseToRiskAdjVarXML()");
		Mapping mapping = new Mapping();
		org.apache.commons.io.output.ByteArrayOutputStream stream = new org.apache.commons.io.output.ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader()
			.getResourceAsURL("SubTreeToRiskAdjustmentVarMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(
					stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(riskAdjVarDTO);
			logger.debug("Marshalling of SubTreeToRiskAdjustmentVarMapping is successful in convertclauseToRiskAdjVarXML()"
					+ stream.toString());
		}catch(IOException e) {
			logger.info("Failed to load SubTreeToRiskAdjustmentVarMapping.xml in convertclauseToRiskAdjVarXML()"
						+ e, e);
		}catch(MappingException e){
			logger.info("Mapping Failed in convertclauseToRiskAdjVarXML()"
						+ e, e);
		}catch(MarshalException e) {
			logger.info("Unmarshalling Failed in convertclauseToRiskAdjVarXML()"
						+ e, e);
		}catch(ValidationException e) {
			logger.info("Validation Exception in convertclauseToRiskAdjVarXML()"
						+ e, e);
		}catch(Exception e) {
			logger.info("Other Exception in convertclauseToRiskAdjVarXML()"
						+ e, e);
		}
		logger.info("Exiting PackagerServiceImpl.convertclauseToRiskAdjVarXML()");
		return stream;
	
		
	}
	
	
	/**
	 * Creates the grouping xml.
	 * 
	 * @param detail
	 *            the detail
	 * @return the string
	 */
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
			if (e instanceof IOException) {
				logger.info("Failed to load MeasurePackageClauseDetail.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		return stream.toString();
	}
	
	/**
	 * Creates the measure package clause detail.
	 *
	 * @param id the id
	 * @param name the name
	 * @param type the type
	 * @param associatedPopulationUUID the associated population uuid
	 * @param itemCountList the item count list
	 * @return the measure package clause detail
	 */
	private MeasurePackageClauseDetail createMeasurePackageClauseDetail(String id, String name, String type,
			String associatedPopulationUUID, List<QualityDataSetDTO> itemCountList) {
		MeasurePackageClauseDetail detail = new MeasurePackageClauseDetail();
		detail.setId(id);
		detail.setName(name);
		detail.setType(type);
		detail.setAssociatedPopulationUUID(associatedPopulationUUID);
		detail.setItemCountList(itemCountList);
		return detail;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.PackagerService#delete(mat.client.measurepackage.MeasurePackageDetail)
	 */
	@Override
	public void delete(MeasurePackageDetail detail) {
		MeasureXML measureXML = measureXMLDAO.findForMeasure(detail.getMeasureId());
		XmlProcessor  processor = new XmlProcessor(measureXML.getMeasureXMLAsString());
		Node groupNode = null;
		try {
			groupNode = processor.findNode(processor.getOriginalDoc(), XmlProcessor.XPATH_GROUP_SEQ_START
					+ detail.getSequence() +  XmlProcessor.XPATH_GROUP_SEQ_END);
		} catch (XPathExpressionException e) {
			logger.info("Xpath Expression is incorrect" + e);
		}
		if (groupNode != null) {
			Node measureGroupingNode = groupNode.getParentNode();
			measureGroupingNode.removeChild(groupNode);
		}
		String xml = processor.transform(processor.getOriginalDoc());
		measureXML.setMeasureXMLAsByteArray(xml);
		measureXMLDAO.save(measureXML);
	}
	
	/**
	 * Gets the all clause list.
	 *
	 * @param processor the processor
	 * @param measureId the measure id
	 * @return the all clause list
	 */
	private Map<String, ArrayList<RiskAdjustmentDTO>> getAllClauseList(XmlProcessor  processor, String measureId){
		Map<String, ArrayList<RiskAdjustmentDTO>> finalClauseMap = new HashMap<String, ArrayList<RiskAdjustmentDTO>>();
		finalClauseMap = getRiskAdjVariablesForMeasurePackager(processor);
		logger.info("finalMap()of RiskAdjClause ::" + finalClauseMap.size());
		return finalClauseMap;
	}
	
	
	/**
	 * Gets the risk adj variables for measure packager.
	 *
	 * @param processor the processor
	 * @return the risk adj variables for measure packager
	 */
	public Map<String, ArrayList<RiskAdjustmentDTO>> getRiskAdjVariablesForMeasurePackager(XmlProcessor  processor){
		Map<String, ArrayList<RiskAdjustmentDTO>> riskMap = new HashMap<String, ArrayList<RiskAdjustmentDTO>>();
		ArrayList<RiskAdjustmentDTO> subTreeList = new ArrayList<RiskAdjustmentDTO>();
		ArrayList<RiskAdjustmentDTO> riskAdkVariableList = new ArrayList<RiskAdjustmentDTO>();
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		if (processor.getOriginalDoc() == null) {
			return riskMap;
		}
		try{
		
		NodeList riskAdjustmentVarNodeList = (NodeList) xPath.evaluate(XPATH_MEASURE_RISK_ADJSUTMENT_VARIABLE,
				processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODESET);
		for(int j=0; j<riskAdjustmentVarNodeList.getLength();j++){
			Node newNode = riskAdjustmentVarNodeList.item(j);					
			RiskAdjustmentDTO riskDTO = new RiskAdjustmentDTO();
			riskDTO.setName(newNode.getAttributes().getNamedItem("displayName").getNodeValue());
			riskDTO.setUuid(newNode.getAttributes().getNamedItem("id").getNodeValue());
			riskAdkVariableList.add(riskDTO);
		}
		String uuidXPathString = "";
		for(int m=0;m<riskAdkVariableList.size();m++){
			uuidXPathString += "@uuid != '"+riskAdkVariableList.
					get(m).getUuid() + "' and";
		}
		String xpathStringForSubTree = "";
		if(!uuidXPathString.isEmpty()){
			uuidXPathString = uuidXPathString.substring(0,uuidXPathString.lastIndexOf(" and"));
			xpathStringForSubTree= XPATH_MEASURE_SUBTREE_LOOKUP_CLAUSE+"["+uuidXPathString +"]" +
				"[@qdmVariable='false']";
		} else {
			xpathStringForSubTree= XPATH_MEASURE_SUBTREE_LOOKUP_CLAUSE +
				"[@qdmVariable='false']";
		}
		NodeList nodesSubTreeLookUpAll = (NodeList) xPath.evaluate(xpathStringForSubTree,
				processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODESET);
		for(int i=0;i<nodesSubTreeLookUpAll.getLength();i++){
			Node newNode = nodesSubTreeLookUpAll.item(i);					
			RiskAdjustmentDTO riskDTO = new RiskAdjustmentDTO();
			riskDTO.setName(newNode.getAttributes().getNamedItem("displayName").getNodeValue());
			riskDTO.setUuid(newNode.getAttributes().getNamedItem("uuid").getNodeValue());
			subTreeList.add(riskDTO);
		}
		
		riskMap.put("RISKADJ", riskAdkVariableList);
		riskMap.put("SUBTREEREF", subTreeList);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return riskMap;
	}
	
	/**
	 * Gets the intersection of qdm and sde.
	 *
	 * @param processor the processor
	 * @param measureId            the measure id
	 * @return the intersection of qdm and sde
	 */
	private Map<String, ArrayList<QualityDataSetDTO>> getIntersectionOfQDMAndSDE(XmlProcessor  processor, String measureId) {
		Map<String, ArrayList<QualityDataSetDTO>> finalMap = new HashMap<String, ArrayList<QualityDataSetDTO>>();
		finalMap = sortSDEAndQDMsForMeasurePackager(processor);
		logger.info("finalMap()of QualityDataSetDTO ::" + finalMap.size());
		return finalMap;
		
	}
	
	/**
	 * Sort sde and qd ms for measure packager.
	 *
	 * @param processor the processor
	 * @return the map
	 */
	public Map<String, ArrayList<QualityDataSetDTO>> sortSDEAndQDMsForMeasurePackager(XmlProcessor  processor) {
		Map<String, ArrayList<QualityDataSetDTO>> map = new HashMap<String, ArrayList<QualityDataSetDTO>>();
		ArrayList<QualityDataSetDTO> qdmList = new ArrayList<QualityDataSetDTO>();
		ArrayList<QualityDataSetDTO> masterList = new ArrayList<QualityDataSetDTO>();
		ArrayList<QualityDataSetDTO> supplementalDataList = new ArrayList<QualityDataSetDTO>();
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		if (processor.getOriginalDoc() == null) {
			return map;
		}
		try {
			NodeList nodesElementLookUpAll = (NodeList) xPath.evaluate(
					XPATH_MEASURE_ELEMENT_LOOKUP_QDM,
					processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODESET);
			// Master List of Element Look Up QDM's. This list is used to
			// populate QDM properties in SDE and QDM List.
			for (int i = 0; i < nodesElementLookUpAll.getLength(); i++) {
				Node newNode = nodesElementLookUpAll.item(i);
				QualityDataSetDTO dataSetDTO = new QualityDataSetDTO();
				dataSetDTO.setId(newNode.getAttributes().getNamedItem("id")
						.getNodeValue().toString());
				dataSetDTO.setDataType(newNode.getAttributes()
						.getNamedItem("datatype").getNodeValue().toString());
				if (newNode.getAttributes().getNamedItem(INSTANCE) != null) {
					dataSetDTO
					.setOccurrenceText(newNode.getAttributes()
							.getNamedItem(INSTANCE).getNodeValue()
							.toString());
				} else {
					dataSetDTO.setOccurrenceText("");
				}
				dataSetDTO.setCodeListName(newNode.getAttributes()
						.getNamedItem("name").getNodeValue().toString());
				dataSetDTO.setOid(newNode.getAttributes().getNamedItem("oid")
						.getNodeValue().toString());
				dataSetDTO.setTaxonomy(newNode.getAttributes()
						.getNamedItem("taxonomy").getNodeValue().toString());
				dataSetDTO.setUuid(newNode.getAttributes().getNamedItem(UUID_STRING)
						.getNodeValue().toString());
				dataSetDTO.setVersion(newNode.getAttributes()
						.getNamedItem("version").getNodeValue().toString());
				if ((newNode.getAttributes().getNamedItem("suppDataElement")
						.getNodeValue().toString()).equalsIgnoreCase("true")) {
					dataSetDTO.setSuppDataElement(true);
				} else {
					dataSetDTO.setSuppDataElement(false);
				}
				masterList.add(dataSetDTO);
			}
			NodeList nodesSupplementalData = (NodeList) xPath.evaluate(
					XPATH_SD_ELEMENTS_ELEMENTREF,
					processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODESET);
			// If SupplementDataElement contains elementRef, intersection of QDM
			// and SupplementDataElement is evaluated.
			if (nodesSupplementalData.getLength() > 0) {
				StringBuilder expression = new StringBuilder(
						XPATH_MEASURE_ELEMENT_LOOKUP_QDM.concat("["));
				// populate supplementDataElement List and create XPATH
				// expression to find intersection of QDM and SDE.
				for (int i = 0; i < nodesSupplementalData.getLength(); i++) {
					Node newNode = nodesSupplementalData.item(i);
					String nodeID = newNode.getAttributes().getNamedItem("id")
							.getNodeValue();
					expression = expression.append("@uuid!= '").append(nodeID)
							.append("'").append(" and ");
					for (QualityDataSetDTO dataSetDTO : masterList) {
						if (dataSetDTO.getUuid().equalsIgnoreCase(nodeID)) {
							supplementalDataList.add(dataSetDTO);
							break;
						}
					}
				}
				String xpathUniqueQDM = expression.toString();
				// Final XPath Expression.
				xpathUniqueQDM = xpathUniqueQDM.substring(0,
						xpathUniqueQDM.lastIndexOf(" and")).concat("]");
				XPathExpression expr = xPath.compile(xpathUniqueQDM);
				// Intersection List of QDM and SDE. Elements which are
				// referenced in SDE are filtered out.
				NodeList nodesFinal = (NodeList) expr.evaluate(
						processor.getOriginalDoc().getDocumentElement(),
						XPathConstants.NODESET);
				// populate QDM List
				for (int i = 0; i < nodesFinal.getLength(); i++) {
					Node newNode = nodesFinal.item(i);
					String nodeID = newNode.getAttributes()
							.getNamedItem(UUID_STRING).getNodeValue();
					String dataType = newNode.getAttributes()
							.getNamedItem("datatype").getNodeValue();
					String oid = newNode.getAttributes()
							.getNamedItem("oid").getNodeValue();
					boolean isOccurrenceText = false;
					if (newNode.getAttributes().getNamedItem(INSTANCE) != null) {
						isOccurrenceText = true;
					}
					// Check to Filter Occurrences and to filter Attributes, Timing, BirtDate and Expired data types.
					if (!isOccurrenceText && (!dataType
						.equalsIgnoreCase(ConstantMessages.TIMING_ELEMENT)
						&& !dataType
						.equalsIgnoreCase(ConstantMessages.ATTRIBUTE) 
						&& !oid
						.equalsIgnoreCase(ConstantMessages.EXPIRED_OID)
						&& !oid
						.equalsIgnoreCase(ConstantMessages.BIRTHDATE_OID))) {
						for (QualityDataSetDTO dataSetDTO : masterList) {
							if (dataSetDTO.getUuid().equalsIgnoreCase(
									nodeID)
									&& StringUtils.isBlank(dataSetDTO
											.getOccurrenceText())) {
								qdmList.add(dataSetDTO);
								break;
							}
						}
					}
				}
			} else {
				for (int i = 0; i < nodesElementLookUpAll.getLength(); i++) {
					Node newNode = nodesElementLookUpAll.item(i);
					String nodeID = newNode.getAttributes()
							.getNamedItem(UUID_STRING).getNodeValue();
					String dataType = newNode.getAttributes()
							.getNamedItem("datatype").getNodeValue();
					String oid = newNode.getAttributes()
							.getNamedItem("oid").getNodeValue();
					boolean isOccurrenceText = false;
					if (newNode.getAttributes().getNamedItem(INSTANCE) != null) {
						isOccurrenceText = true;
					}
					// Check to Filter Occurrences and to filter Attributes, Timing, BirtDate and Expired data types.
					if (!isOccurrenceText && (!dataType
						.equalsIgnoreCase(ConstantMessages.TIMING_ELEMENT)
						&& !dataType
						.equalsIgnoreCase(ConstantMessages.ATTRIBUTE)
						&& !oid
						.equalsIgnoreCase(ConstantMessages.EXPIRED_OID)
						&& !oid
						.equalsIgnoreCase(ConstantMessages.BIRTHDATE_OID))) {
						for (QualityDataSetDTO dataSetDTO : masterList) {
							if (dataSetDTO.getUuid().equalsIgnoreCase(
									nodeID)
									&& StringUtils.isBlank(dataSetDTO
											.getOccurrenceText())) {
								qdmList.add(dataSetDTO);
								break;
							}
						}
					}
				}
			}
			map.put("QDM", qdmList);
			map.put("SDE", supplementalDataList);
			map.put("MASTER", masterList);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * Creates measureGrouping XML chunk from MeasurePackageDetail using castor
	 * and "MeasurePackageClauseDetail.xml" mapping file. Finds the Group Node
	 * in the Measure_Xml using the sequence number from MeasurePackageDetail if
	 * the Group present deletes that Group in Measure_Xml and appends the new
	 * Group from measureGrouping XML to the measureGrouping node in Measure_Xml
	 * if the Group not Present appends the new Group from measureGrouping XML
	 * to the parent measureGrouping node in Measure_XML Finally Save the
	 * Measure_xml
	 *
	 * @param detail the detail
	 * @return the measure package save result
	 */
	@Override
	public MeasurePackageSaveResult save(MeasurePackageDetail detail) {
		MeasurePackageClauseValidator clauseValidator = new MeasurePackageClauseValidator();
		List<String> messages = clauseValidator.isValidMeasurePackage(detail.getPackageClauses());
		MeasurePackageSaveResult result = new MeasurePackageSaveResult();
		if (messages.size() == 0) {
			result.setSuccess(true);
			MeasureXML measureXML = measureXMLDAO.findForMeasure(detail.getMeasureId());
			XmlProcessor  processor = new XmlProcessor(measureXML.getMeasureXMLAsString());
			Node groupNode = null;
			Node measureGroupingNode = null;
			try {
				//fetches the Group node from Measure_XML with the sequence number from MeasurePackageDetail
				groupNode = processor.findNode(processor.getOriginalDoc(), XmlProcessor.XPATH_GROUP_SEQ_START
						+ detail.getSequence() +  XmlProcessor.XPATH_GROUP_SEQ_END);
				//fetches the MeasureGrouping node from the Measure_xml
				measureGroupingNode = processor.findNode(processor.getOriginalDoc(),
						XmlProcessor.XPATH_MEASURE_GROUPING); // get the MEASUREGROUPING node
			} catch (XPathExpressionException e) {
				logger.info("Xpath Expression is incorrect" + e);
			}
			if ((null != groupNode) && groupNode.hasChildNodes()) { //if Same sequence , remove and update.
				logger.info("Removing Group with seq number" + detail.getSequence());
				measureGroupingNode.removeChild(groupNode);
			}
			//Converts MeasurePackageDetail to measureGroupingXml through castor.
			String measureGroupingXml = createGroupingXml(detail);
			XmlProcessor measureGrpProcessor = new XmlProcessor(measureGroupingXml);
			// get the converted XML's first child and appends it the Measure Grouping.
			Node newGroupNode = measureGrpProcessor.getOriginalDoc()
					.getElementsByTagName("measureGrouping").item(0).getFirstChild();
			measureGroupingNode.appendChild(processor.getOriginalDoc().importNode(newGroupNode, true));
			logger.info("new Group appended");
			String xml = measureGrpProcessor.transform(processor.getOriginalDoc());
			measureXML.setMeasureXMLAsByteArray(xml);
			measureXMLDAO.save(measureXML);
		} else {
			for (String message: messages) {
				logger.info("Server-Side Validation failed for MeasurePackageClauseValidator for Login ID: "
						+ MatContext.get().getLoggedinLoginId() + " And failure Message is :" + message);
			}
			result.setSuccess(false);
			result.setMessages(messages);
			result.setFailureReason(MeasurePackageSaveResult.SERVER_SIDE_VALIDATION);
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.PackagerService#saveQDMData(mat.client.measurepackage.MeasurePackageDetail)
	 */
	@Override
	public void saveQDMData(MeasurePackageDetail detail) {
		ArrayList<QualityDataSetDTO> supplementDataElementsAll = (ArrayList<QualityDataSetDTO>) detail.getSuppDataElements();
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		wrapper.setQualityDataDTO(supplementDataElementsAll);
		ByteArrayOutputStream stream = convertQDMOToSuppleDataXML(wrapper);
		MeasureXML measureXML = measureXMLDAO.findForMeasure(detail.getMeasureId());
		XmlProcessor  processor = new XmlProcessor(measureXML.getMeasureXMLAsString());
		if (supplementDataElementsAll.size() > 0) {
			processor.replaceNode(stream.toString(), SUPPLEMENT_DATA_ELEMENTS, MEASURE);
			//try {
			//	setSupplementalDataForQDMs(processor.getOriginalDoc(), detail.getSuppDataElements(), detail.getQdmElements());
			//} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
		} else {
			
			try {
				// In case all elements from SupplementDataElements are moved to QDM, this will remove all.
				javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
				NodeList nodesSupplementalData = (NodeList) xPath.evaluate(XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_ELEMENTREF,
						processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODESET);
				for (int i = 0; i < nodesSupplementalData.getLength(); i++) {
					String xPathString = XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_EXPRESSION.concat("='")
							.concat(nodesSupplementalData.item(i).getNodeValue().toString()).concat("']");
					Node newNode = processor.findNode(processor.getOriginalDoc(), xPathString);
					Node parentNode = newNode.getParentNode();
					parentNode.removeChild(newNode);
				}
				//setSupplementalDataForQDMs(processor.getOriginalDoc(), detail.getSuppDataElements(), detail.getQdmElements());
			} catch (XPathExpressionException e) {
				
				e.printStackTrace();
			}
		}
		measureXML.setMeasureXMLAsByteArray(processor.transform(processor.getOriginalDoc()));
		measureXMLDAO.save(measureXML);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.PackagerService#saveRiskAdjVariables(mat.client.measurepackage.MeasurePackageDetail)
	 */
	@Override
	public void saveRiskAdjVariables(MeasurePackageDetail detail) {
		ArrayList<RiskAdjustmentDTO> allRiskAdjVars = (ArrayList<RiskAdjustmentDTO>) detail
				.getRiskAdjVars();
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		wrapper.setRiskAdjVarDTO(allRiskAdjVars);
		ByteArrayOutputStream stream = convertclauseToRiskAdjVarXML(wrapper);
		MeasureXML measureXML = measureXMLDAO.findForMeasure(detail
				.getMeasureId());
		XmlProcessor processor = new XmlProcessor(
				measureXML.getMeasureXMLAsString());
		if (allRiskAdjVars.size() > 0) {
			processor.replaceNode(stream.toString(), RISK_ADJUSTMENT_VARIABLES,
					MEASURE);
		} else {

			try {
				javax.xml.xpath.XPath xPath = XPathFactory.newInstance()
						.newXPath();
				NodeList nodesSupplementalData = (NodeList) xPath.evaluate(
						XPATH_MEASURE_RISK_ADJ_VARIABLES,
						processor.getOriginalDoc().getDocumentElement(),
						XPathConstants.NODESET);
				for (int i = 0; i < nodesSupplementalData.getLength(); i++) {
					String xPathString = XPATH_MEASURE_RISK_ADJ_VARIABLES_EXPRESSION
							.concat("='")
							.concat(nodesSupplementalData.item(i)
									.getNodeValue().toString()).concat("']");
					Node newNode = processor.findNode(
							processor.getOriginalDoc(), xPathString);
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
	 * Sets the supplemental data for qd ms.
	 *
	 * @param originalDoc the new supplemental data for qd ms
	 * @param supplementalDataElemnts the supplemental data elemnts
	 * @param qdmElemnts the qdm elemnts
	 * @throws XPathExpressionException the x path expression exception
	 *///commented Out
	//	private void setSupplementalDataForQDMs(Document originalDoc, List<QualityDataSetDTO> supplementalDataElemnts,
	//			List<QualityDataSetDTO> qdmElemnts) throws XPathExpressionException {
	//
	//		//to set QDM's that are used in Supplemental Data ELements tab.
	//		for(int i = 0; i<supplementalDataElemnts.size(); i++){
	//			javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	//			Node nodeSupplementalDataNode = (Node) xPath.evaluate(XPATH_MEASURE_ELEMENT_LOOK_UP_EXPRESSION +supplementalDataElemnts.get(i).getUuid()+"']",
	//					originalDoc.getDocumentElement(), XPathConstants.NODE);
	//			nodeSupplementalDataNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("true");
	//		}
	//
	//		//to set QDM's that are used in QDM Elements Tab
	//
	//		for(int j = 0; j<qdmElemnts.size(); j++){
	//			javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	//			Node nodeSupplementalDataNode = (Node) xPath.evaluate(XPATH_MEASURE_ELEMENT_LOOK_UP_EXPRESSION +qdmElemnts.get(j).getUuid()+"']",
	//					originalDoc.getDocumentElement(), XPathConstants.NODE);
	//			nodeSupplementalDataNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("false");
	//		}
	//
	//	}
	
}
