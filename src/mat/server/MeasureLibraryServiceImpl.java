package mat.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.DTO.MeasureNoteDTO;
import mat.DTO.MeasureTypeDTO;
import mat.DTO.OperatorDTO;
import mat.client.clause.clauseworkspace.model.MeasureDetailResult;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.MeasureNotesModel;
import mat.client.measure.NqfModel;
import mat.client.measure.PeriodModel;
import mat.client.measure.TransferMeasureOwnerShipModel;
import mat.client.measure.service.CQLService;
import mat.client.measure.service.SaveMeasureNotesResult;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.ManageMeasureModelValidator;
import mat.client.shared.ManageMeasureNotesModelValidator;
import mat.client.shared.MatContext;
import mat.client.shared.MatException;
import mat.dao.AuthorDAO;
import mat.dao.DataTypeDAO;
import mat.dao.MeasureNotesDAO;
import mat.dao.MeasureTypeDAO;
import mat.dao.OrganizationDAO;
import mat.dao.RecentMSRActivityLogDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dao.clause.OperatorDAO;
import mat.dao.clause.QDSAttributesDAO;
import mat.dao.impl.clause.MeasureExportDAO;
import mat.model.Author;
import mat.model.CQLValueSetTransferObject;
import mat.model.DataType;
import mat.model.LockedUserInfo;
import mat.model.MatValueSet;
import mat.model.MeasureNotes;
import mat.model.MeasureOwnerReportDTO;
import mat.model.MeasureSteward;
import mat.model.MeasureType;
import mat.model.Organization;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.RecentMSRActivityLog;
import mat.model.SecurityRole;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.MeasureXML;
import mat.model.clause.QDSAttributes;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.model.cql.parser.CQLFileObject;
import mat.server.cqlparser.MATCQLParser;
import mat.server.model.MatUserDetails;
import mat.server.service.InvalidValueSetDateException;
import mat.server.service.MeasureLibraryService;
import mat.server.service.MeasureNotesService;
import mat.server.service.MeasurePackageService;
import mat.server.service.UserService;
import mat.server.service.impl.MatContextServiceUtil;
import mat.server.util.ExportSimpleXML;
import mat.server.util.MATPropertiesService;
import mat.server.util.MeasureUtility;
import mat.server.util.ResourceLoader;
import mat.server.util.UuidUtility;
import mat.server.util.XmlProcessor;
import mat.shared.CQLErrors;
import mat.shared.CQLValidationResult;
import mat.shared.ConstantMessages;
import mat.shared.DateStringValidator;
import mat.shared.DateUtility;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.UUIDUtilClient;
import mat.shared.model.util.MeasureDetailsUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cqframework.cql.cql2elm.CQLtoELM;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasureLibraryServiceImpl.
 */
public class MeasureLibraryServiceImpl implements MeasureLibraryService {
	
	/**
	 * Nested Clause allowed depth.
	 */
	private static final int NESTED_CLAUSE_DEPTH = 10;
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(MeasureLibraryServiceImpl.class);
	
	/** The Constant MEASURE. */
	private static final String MEASURE = "measure";
	
	/** The Constant MEASURE_DETAILS. */
	private static final String MEASURE_DETAILS = "measureDetails";
	
	/**
	 * Constant XPATH Expression for Component measure.
	 */
	private static final String XPATH_EXPRESSION_COMPONENT_MEASURES = "/measure//measureDetails//componentMeasures";
	/**
	 * Constant XPATH Expression for steward.
	 */
	private static final String XPATH_EXPRESSION_STEWARD = "/measure//measureDetails//steward";
	/**
	 * Constant XPATH Expression for Developers.
	 */
	private static final String XPATH_EXPRESSION_DEVELOPERS = "/measure//measureDetails//developers";
	
	/** The current release version. */
	private String currentReleaseVersion;
	
	/** The is measure created. */
	private boolean isMeasureCreated;
	
	
	/**
	 * Comparator.
	 * **/
	private Comparator<QDSAttributes> attributeComparator = new Comparator<QDSAttributes>() {
		@Override
		public int compare(final QDSAttributes arg0, final QDSAttributes arg1) {
			return arg0.getName().toLowerCase().compareTo(arg1.getName().toLowerCase());
		}
	};
	
	/** The context. */
	@Autowired
	private ApplicationContext context;
	
	/** The measure package service. */
	@Autowired
	private MeasurePackageService measurePackageService;
	
	/** The measure dao. */
	@Autowired
	private MeasureDAO measureDAO;
	
	/** The qds attributes dao. */
	@Autowired
	private QDSAttributesDAO qDSAttributesDAO;
	
	/** The recent msr activity log dao. */
	@Autowired
	private RecentMSRActivityLogDAO recentMSRActivityLogDAO;
	
	/** The user service. */
	@Autowired
	private UserService userService;
	
	/** The measure type dao. */
	@Autowired
	private MeasureTypeDAO measureTypeDAO;
	
	/** The author dao. */
	@Autowired
	private AuthorDAO authorDAO;
	
	/** The operator dao. */
	@Autowired
	private OperatorDAO operatorDAO;
	
	/** The organization dao. */
	@Autowired
	private OrganizationDAO organizationDAO;
	
	/**
	 * The cql library dao
	 */
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO; 
	
	@Autowired
	private CQLLibraryService cqlLibraryService; 
		
	/** The x path. */
	javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	
	/** The cql service. */
	private CQLService cqlService;
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#appendAndSaveNode(mat.client.clause.clauseworkspace.model.MeasureXmlModel, java.lang.String)
	 */
	@Override
	public final void appendAndSaveNode(final MeasureXmlModel measureXmlModel, final String nodeName) {
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		if ((xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml()))
				&& (nodeName != null && StringUtils.isNotBlank(nodeName))) {
			String result = callAppendNode(xmlModel, measureXmlModel.getXml(), nodeName, measureXmlModel.getParentNode());
			xmlModel.setXml(result);
			getService().saveMeasureXml(xmlModel);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#checkAndDeleteSubTree(java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String, String> checkAndDeleteSubTree(String measureId , String subTreeUUID){
		logger.info("Inside checkAndDeleteSubTree Method for measure Id " + measureId);
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureId);
		HashMap<String,String> removeUUIDMap= new HashMap<String,String> ();
		if(MatContextServiceUtil.get().isCurrentMeasureEditable(getMeasureDAO(),measureId)){
			if (((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml()))) {
				XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
				try {
					NodeList subTreeRefNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), "//subTreeRef[@id='"+subTreeUUID+"']");
					if(subTreeRefNodeList.getLength() > 0){
						xmlModel.setXml(null);
						return removeUUIDMap;
					}

					Node subTreeNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc()
							, "/measure/subTreeLookUp/subTree[@uuid='"+subTreeUUID+"']");
					NodeList subTreeOccNode = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc()
							, "/measure/subTreeLookUp/subTree[@instanceOf='"+subTreeUUID+"']");
					if (subTreeNode != null) {
						Node parentNode = subTreeNode.getParentNode();
						String name = subTreeNode.getAttributes().getNamedItem("displayName").getNodeValue();
						String uuid = subTreeNode.getAttributes().getNamedItem("uuid").getNodeValue();
						String mapValue = name + "~" + uuid;
						removeUUIDMap.put(uuid,mapValue);
						parentNode.removeChild(subTreeNode);
					}
					if (subTreeOccNode.getLength() > 0) {
						Set<Node> targetOccurenceElements = new HashSet<Node>();
						for (int i = 0; i < subTreeOccNode.getLength(); i++) {
							Node node = subTreeOccNode.item(i);
							targetOccurenceElements.add(node);
						}

						for (Node occNode : targetOccurenceElements) {
							String name = "Occurrence " + occNode.getAttributes().getNamedItem("instance").getNodeValue() + " of ";
							name = name + occNode.getAttributes().getNamedItem("displayName").getNodeValue();
							String uuid = occNode.getAttributes().getNamedItem("uuid").getNodeValue();
							String mapValue = name + "~" + uuid;
							removeUUIDMap.put(uuid,mapValue);
							occNode.getParentNode().removeChild(occNode);
						}
					}
					xmlModel.setXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
					getService().saveMeasureXml(xmlModel);
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return removeUUIDMap;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#isSubTreeReferredInLogic(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isSubTreeReferredInLogic(String measureId, String subTreeUUID){
		logger.info("Inside isSubTreeReferredInLogic Method for measure Id " + measureId);
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureId);
		if (((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml()))) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			try {
				NodeList subTreeRefNodeList = xmlProcessor.findNodeList(
						xmlProcessor.getOriginalDoc(), "//subTreeRef[@id='" + subTreeUUID + "']");
				NodeList subTreeOccNodeList = xmlProcessor.findNodeList(
						xmlProcessor.getOriginalDoc(), "//subTree[@instanceOf='" + subTreeUUID + "']");
				boolean isOccurrenceUsed = false;
				for (int i = 0; i < subTreeOccNodeList.getLength(); i++) {
					Node node = subTreeOccNodeList.item(i);
					if (node.hasAttributes()) {
						String occNodeUUID = node.getAttributes().getNamedItem("uuid").getNodeValue();
						NodeList subTreeOccRefNodeList = xmlProcessor.findNodeList(
								xmlProcessor.getOriginalDoc(), "//subTreeRef[@id='" + occNodeUUID + "']");
						if (subTreeOccRefNodeList.getLength() > 0) {
							isOccurrenceUsed = true;
							break;
						}
					}
				}
				if ((subTreeRefNodeList.getLength() > 0) || isOccurrenceUsed) {
					return true;
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#isQDMVariableEnabled(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isQDMVariableEnabled(String measureId, String subTreeUUID){
		logger.info("Inside isQDMVariableEnabled Method for measure Id " + measureId);
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureId);
		if (((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml()))) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			try {
				NodeList subTreeOccNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), "//subTree[@instanceOf='"+subTreeUUID+"']");
				if((subTreeOccNodeList.getLength() >0)){
					return true;
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveSubTreeInMeasureXml(mat.client.clause.clauseworkspace.model.MeasureXmlModel, java.lang.String)
	 */
	@Override
	public SortedClauseMapResult saveSubTreeInMeasureXml(MeasureXmlModel measureXmlModel, String nodeNameWithSpaces, String nodeUUID) {
		
		// change multiple spaces into one space for the nodeName
		String nodeName = nodeNameWithSpaces.replaceAll("( )+", " ");
		
		logger.info("Inside saveSubTreeInMeasureXml Method for measure Id " + measureXmlModel.getMeasureId() + " .");
		SortedClauseMapResult clauseMapResult = new SortedClauseMapResult();
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		
		if (((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml()))) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			try {
				// take the spaces out of the xml for displayNme xml attribute
				String normalizedXml = XmlProcessor.normalizeNodeForSpaces(measureXmlModel.getXml(), "//subTree/@displayName");
				measureXmlModel.setXml(normalizedXml);
				
				Node subTreeLookUpNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc()
						, measureXmlModel.getParentNode());
				// Add subTreeLookUp node if not available in MeasureXml.
				if (subTreeLookUpNode == null) {
					logger.info("Adding subTreeNodeLookUp Node for measure Id "
							+ measureXmlModel.getMeasureId() + " .");
					String xPathSupplementalDataElement = "/measure/supplementalDataElements";
					Node supplementaDataElementsElement = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
							xPathSupplementalDataElement);
					String tagNameSubTeeLookUp = "subTreeLookUp";
					Element subTreeLookUpElement = xmlProcessor.getOriginalDoc()
							.createElement(tagNameSubTeeLookUp);
					((Element) supplementaDataElementsElement.getParentNode())
					.insertBefore(subTreeLookUpElement,
							supplementaDataElementsElement.getNextSibling());
					xmlProcessor.setOriginalXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
				}
				// If Node already exist's and its a update then existing node will be removed from Parent Node
				// and updated node will be added.
				String xPathForSubTree = "/measure/subTreeLookUp/subTree[@uuid='"+nodeUUID+"']";
				NodeList subTreeNodeForUUID = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForSubTree);
				if(subTreeNodeForUUID.getLength() == 0){
					xmlProcessor.appendNode(measureXmlModel.getXml(), measureXmlModel.getToReplaceNode(), measureXmlModel.getParentNode());
				}else{
					Node newNode = subTreeNodeForUUID.item(0);
					if (newNode.getAttributes().getNamedItem("uuid").getNodeValue().equals(nodeUUID)) {
						logger.info("Replacing SubTreeNode for UUID " + nodeUUID + " .");
						xmlProcessor.removeFromParent(newNode);
						xmlProcessor.appendNode(measureXmlModel.getXml(), measureXmlModel.getToReplaceNode(), measureXmlModel.getParentNode());
						
						//In case the name of the subTree has changed we need to make sure to find all the subTreeRef tags and change the name in them as well.
						NodeList subTreeRefNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), "//subTreeRef[@id='"+nodeUUID+"']");
						NodeList subTreeOccRefNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), "//subTreeRef[@instanceOf='"+nodeUUID+"']");
						String xPathForSubTreeOcc = "/measure/subTreeLookUp/subTree[@instanceOf='"+nodeUUID+"']";
						NodeList subTreeOccNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForSubTreeOcc);
						if(subTreeRefNodeList.getLength() > 0){
							for(int k=0;k<subTreeRefNodeList.getLength();k++){
								Node subTreeRefNode = subTreeRefNodeList.item(k);
								subTreeRefNode.getAttributes().getNamedItem("displayName").setNodeValue(nodeName);
							}
						}
						if(subTreeOccRefNodeList.getLength() > 0){
							for(int k=0;k<subTreeOccRefNodeList.getLength();k++){
								Node subTreeRefOccNode = subTreeOccRefNodeList.item(k);
								subTreeRefOccNode.getAttributes().getNamedItem("displayName").setNodeValue(nodeName);
							}
						}
						
						if(subTreeOccNodeList.getLength() > 0){
							for(int k=0;k<subTreeOccNodeList.getLength();k++){
								Node subTreeOccNode = subTreeOccNodeList.item(k);
								subTreeOccNode.getAttributes().getNamedItem("displayName").setNodeValue(nodeName);
							}
						}
					}
				}
				xmlProcessor.setOriginalXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
				
				measureXmlModel.setXml(xmlProcessor.getOriginalXml());
				clauseMapResult.setMeasureXmlModel(measureXmlModel);
				getService().saveMeasureXml(measureXmlModel);
			} catch (XPathExpressionException exception) {
				exception.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.info("End saveSubTreeInMeasureXml Method for measure Id " + measureXmlModel.getMeasureId() + " .");
		clauseMapResult.setClauseMap(getSortedClauseMap(measureXmlModel.getMeasureId()));
		return clauseMapResult;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveSubTreeOccurrence(mat.client.clause.clauseworkspace.model.MeasureXmlModel, java.lang.String, java.lang.String)
	 */
	@Override
	public SortedClauseMapResult saveSubTreeOccurrence(MeasureXmlModel measureXmlModel, String nodeName, String nodeUUID){
		logger.info("Inside saveSubTreeOccurrence Method for measure Id " + measureXmlModel.getMeasureId() + " .");
		SortedClauseMapResult sortedClauseMapResult = new SortedClauseMapResult();
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		int ASCII_START = 65;
		int ASCII_END = 90;
		int occurrenceCount = ASCII_START;
		if (((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml()))) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			try {
				String xPathForSubTree = "/measure/subTreeLookUp/subTree[@instanceOf='" + nodeUUID + "']";
				NodeList subTreeNodeForUUID = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForSubTree);
				if (subTreeNodeForUUID.getLength() == 0) {
					XmlProcessor processor = new XmlProcessor(measureXmlModel.getXml());
					Node subTreeNode = processor.findNode(processor.getOriginalDoc(), "/subTree");
					Attr instanceAttrNode = processor.getOriginalDoc().createAttribute("instance");
					instanceAttrNode.setNodeValue("" + (char) occurrenceCount);
					subTreeNode.getAttributes().setNamedItem(instanceAttrNode);
					Attr instanceAttrNodeOfAttrNode = processor.getOriginalDoc().createAttribute("instanceOf");
					instanceAttrNodeOfAttrNode.setNodeValue(nodeUUID);
					subTreeNode.getAttributes().setNamedItem(instanceAttrNodeOfAttrNode);
					measureXmlModel.setXml(processor.transform(subTreeNode));
					xmlProcessor.appendNode(measureXmlModel.getXml(),
							measureXmlModel.getToReplaceNode(), measureXmlModel.getParentNode());
				} else {
					for (int i = 0; i < subTreeNodeForUUID.getLength(); i++) {
						Node node = subTreeNodeForUUID.item(i);
						String instanceValue = node.getAttributes().getNamedItem("instance").getNodeValue();
						Character text = instanceValue.charAt(0);
						int newOcc = (text);
						if (newOcc >= occurrenceCount) {
							occurrenceCount = ++newOcc;
						}
					}
					if (occurrenceCount < ASCII_END) {
						XmlProcessor processor = new XmlProcessor(measureXmlModel.getXml());
						Node subTreeNode = processor.findNode(processor.getOriginalDoc(), "/subTree");
						Attr instanceAttrNode = processor.getOriginalDoc().createAttribute("instance");
						instanceAttrNode.setNodeValue("" + (char) occurrenceCount);
						subTreeNode.getAttributes().setNamedItem(instanceAttrNode);
						Attr instanceAttrNodeOfAttrNode = processor.getOriginalDoc().createAttribute("instanceOf");
						instanceAttrNodeOfAttrNode.setNodeValue(nodeUUID);
						subTreeNode.getAttributes().setNamedItem(instanceAttrNodeOfAttrNode);
						measureXmlModel.setXml(processor.transform(subTreeNode));
						xmlProcessor.appendNode(measureXmlModel.getXml(),
								measureXmlModel.getToReplaceNode(), measureXmlModel.getParentNode());
					}
				}
				xmlProcessor.setOriginalXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
				measureXmlModel.setXml(xmlProcessor.getOriginalXml());
				sortedClauseMapResult.setMeasureXmlModel(measureXmlModel);
				getService().saveMeasureXml(measureXmlModel);
			} catch (XPathExpressionException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			} catch (SAXException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			} catch (IOException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}
		}
		sortedClauseMapResult.setClauseMap(getSortedClauseMap(measureXmlModel.getMeasureId()));
		return sortedClauseMapResult;
	}
	/**
	 * Method to call XMLProcessor appendNode method to append new xml nodes
	 * into existing xml.
	 * 
	 * @param measureXmlModel
	 *            the measure xml model
	 * @param newXml
	 *            the new xml
	 * @param nodeName
	 *            the node name
	 * @param parentNodeName
	 *            the parent node name
	 * @return the string
	 */
	private String callAppendNode(final MeasureXmlModel measureXmlModel, final String newXml, final String nodeName,
			final String parentNodeName) {
		XmlProcessor xmlProcessor = new XmlProcessor(measureXmlModel.getXml());
		String result = null;
		try {
			result = xmlProcessor.appendNode(newXml, nodeName, parentNodeName);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * This method will use XMLProcessor to determine if which of the 3 Timing
	 * Elements are missing from the measure xml 'elementLookUp' tag. Based on
	 * which one is missing it will fetch it from ListObject and add it to
	 * 'elementLookUp'.
	 * 
	 * @param xmlProcessor
	 *            the xml processor
	 */
	@Override
	public void checkForTimingElementsAndAppend(XmlProcessor xmlProcessor) {
		
		/*List<String> missingMeasurementPeriod = xmlProcessor.checkForTimingElements();
		
		if (missingMeasurementPeriod.isEmpty()) {
			logger.info("All timing elements present in the measure.");
			return;
		}
		logger.info("Found the following timing elements missing:" + missingMeasurementPeriod);
		
		//		List<String> missingOIDList = new ArrayList<String>();
		//		missingOIDList.add(missingMeasurementPeriod);
		
		QualityDataModelWrapper wrapper = getMeasureXMLDAO().createTimingElementQDMs(missingMeasurementPeriod);
		
		// Object to XML for elementLookUp
		ByteArrayOutputStream streamQDM = XmlProcessor.convertQualityDataDTOToXML(wrapper);
		
		String filteredString = removePatternFromXMLString(streamQDM.toString().substring(streamQDM.toString().indexOf("<measure>", 0)),
				"<measure>", "");
		filteredString = removePatternFromXMLString(filteredString, "</measure>", "");
		
		try {
			System.out.println("timing qdm String:"+filteredString);
			xmlProcessor.appendNode(filteredString, "qdm", "/measure/elementLookUp");
			String cqlValueSetString = filteredString.replaceAll("<qdm", "<valueset");
			System.out.println("timing cql valueset string:"+cqlValueSetString);
			xmlProcessor.appendNode(cqlValueSetString, "valueset", "/measure/cqlLookUp/valuesets");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
	}
	
	
	
	/**
	 * Check for default CQL code systems and append.
	 *
	 * @param processor the processor
	 */
	private void checkForDefaultCQLCodeSystemsAndAppend(XmlProcessor processor) {
		
		String codeSystemStr = getCqlService().getDefaultCodeSystems();
		
		NodeList defaultCQLCodeSystemsList = findDefaultCodeSystems(processor);
		
		if (defaultCQLCodeSystemsList.getLength() > 0) {
			logger.info("All Default codesystems elements present in the measure.");
			return;
		}
		
		try {
			processor.appendNode(codeSystemStr, "codeSystem", "/measure/cqlLookUp/codeSystems");
			
			NodeList defaultCodeSystemNodes = processor.findNodeList(processor.getOriginalDoc(), 
					"/measure/cqlLookUp/codeSystems/codeSystem[@codeSystemName='LOINC' or @codeSystemName='SNOMEDCT']");
			
			if(defaultCodeSystemNodes != null){
				System.out.println("suppl data elems..setting ids");
				for(int i=0;i<defaultCodeSystemNodes.getLength();i++){
					Node codeSystemNode = defaultCodeSystemNodes.item(i);
				    System.out.println("name:"+codeSystemNode.getAttributes().getNamedItem("codeSystemName").getNodeValue());
				    System.out.println("id:"+codeSystemNode.getAttributes().getNamedItem("id").getNodeValue());
				    codeSystemNode.getAttributes().getNamedItem("id").setNodeValue(UUIDUtilClient.uuid());
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check for default CQL codes and append.
	 *
	 * @param processor the processor
	 */
	private void checkForDefaultCQLCodesAndAppend(XmlProcessor processor) {
		
		String codeStr = getCqlService().getDefaultCodes();
		
		NodeList defaultCQLCodesList = findDefaultCodes(processor);
		
		if (defaultCQLCodesList.getLength() > 0) {
			logger.info("All Default code elements present in the measure.");
			return;
		}
		
		try {
			processor.appendNode(codeStr, "code", "/measure/cqlLookUp/codes");
			
			NodeList defaultCodeNodes = processor.findNodeList(processor.getOriginalDoc(), 
					"/measure/cqlLookUp/codes/code[@codeName='Birthdate' or @codeName='Dead']");
			
			if(defaultCodeNodes != null){
				System.out.println("suppl data elems..setting ids");
				for(int i=0;i<defaultCodeNodes.getLength();i++){
					Node codeNode = defaultCodeNodes.item(i);
					System.out.println("codename:"+codeNode.getAttributes().getNamedItem("codeName").getNodeValue());
				    System.out.println("codesystemname:"+codeNode.getAttributes().getNamedItem("codeSystemName").getNodeValue());
				    System.out.println("id:"+codeNode.getAttributes().getNamedItem("id").getNodeValue());
				    codeNode.getAttributes().getNamedItem("id").setNodeValue(UUIDUtilClient.uuid());
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Append cql parameters.
	 *
	 * @param xmlProcessor the xml processor
	 */
	public void checkForDefaultCQLDefinitionsAndAppend(XmlProcessor xmlProcessor) {
		
		NodeList defaultCQLDefNodeList = findDefaultDefinitions(xmlProcessor);
		
		if (defaultCQLDefNodeList != null && defaultCQLDefNodeList.getLength() == 4) {
			logger.info("All Default parameter elements present in the measure.");
			return;
		}
		
		String defStr = getCqlService().getSupplementalDefinitions();
		System.out.println("defStr:"+defStr);
		try {
			xmlProcessor.appendNode(defStr, "definition", "/measure/cqlLookUp/definitions");
			
			NodeList supplementalDefnNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), 
					"/measure/cqlLookUp/definitions/definition[@supplDataElement='true']");
			
			if(supplementalDefnNodes != null){
				System.out.println("suppl data elems..setting ids");
				for(int i=0;i<supplementalDefnNodes.getLength();i++){
					Node supplNode = supplementalDefnNodes.item(i);
				    System.out.println("name:"+supplNode.getAttributes().getNamedItem("name").getNodeValue());
				    System.out.println("id:"+supplNode.getAttributes().getNamedItem("id").getNodeValue());
					supplNode.getAttributes().getNamedItem("id").setNodeValue(UUIDUtilClient.uuid());
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This method will look into XPath "/measure/cqlLookUp/definitions/" and try and NodeList for Definitions with the following names;
	 * 'SDE Ethnicity','SDE Payer','SDE Race','SDE Sex'.
	 * @param xmlProcessor
	 * @return
	 */
	public NodeList findDefaultDefinitions(XmlProcessor xmlProcessor) {
		NodeList returnNodeList = null;
		Document originalDoc = xmlProcessor.getOriginalDoc();
		
		if (originalDoc != null) {
			try {				
				String defaultDefinitionsXPath = "/measure/cqlLookUp/definitions/definition[@name ='SDE Ethnicity' or @name='SDE Payer' or @name='SDE Race' or @name='SDE Sex']";
				returnNodeList = xmlProcessor.findNodeList(originalDoc, defaultDefinitionsXPath);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		return returnNodeList;
	}
	
	/**
	 * This method will look into XPath "/measure/cqlLookUp/codeSystems/" and try and NodeList for Definitions with the following names;
	 * @param xmlProcessor
	 * @return
	 */
	public NodeList findDefaultCodeSystems(XmlProcessor xmlProcessor) {
		NodeList returnNodeList = null;
		Document originalDoc = xmlProcessor.getOriginalDoc();
		
		if (originalDoc != null) {
			try {				
				String defaultCodeSystemsXPath = "/measure/cqlLookUp/codeSystems/codeSystem[@codeSystemName='LOINC' or @codeSystemName='SNOMEDCT']";
				returnNodeList = xmlProcessor.findNodeList(originalDoc, defaultCodeSystemsXPath);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		return returnNodeList;
	}
	
	/**
	 * This method will look into XPath "/measure/cqlLookUp/codes/" and try and NodeList for Definitions with the following names;
	 * @param xmlProcessor
	 * @return
	 */
	public NodeList findDefaultCodes(XmlProcessor xmlProcessor) {
		NodeList returnNodeList = null;
		Document originalDoc = xmlProcessor.getOriginalDoc();
		
		if (originalDoc != null) {
			try {				
				String defaultCodesXPath = "/measure/cqlLookUp/codes/code[@codeName='Birthdate' or @codeName='Dead']";
				returnNodeList = xmlProcessor.findNodeList(originalDoc, defaultCodesXPath);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		return returnNodeList;
	}
	
	/**
	 * Check for default cql parameters and append.
	 *
	 * @param xmlProcessor the xml processor
	 */
	public void checkForDefaultCQLParametersAndAppend(XmlProcessor xmlProcessor) {
		
		List<String> missingDefaultCQLParameters = xmlProcessor.checkForDefaultParameters();
		
		if (missingDefaultCQLParameters.isEmpty()) {
			logger.info("All Default parameter elements present in the measure.");
			return;
		}
		logger.info("Found the following Default parameter elements missing:" + missingDefaultCQLParameters);
		CQLParameter parameter = new  CQLParameter();
	
		parameter.setId(UUID.randomUUID().toString());
		parameter.setParameterName(CQLWorkSpaceConstants.CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_NAME);
		parameter.setParameterLogic(CQLWorkSpaceConstants.CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_LOGIC);
		parameter.setReadOnly(true);	
		String parStr = getCqlService().createParametersXML(parameter);
	
		try {
			xmlProcessor.appendNode(parStr, "parameter", "/measure/cqlLookUp/parameters");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Method called when Measure Details Clone operation is done or Drafting of
	 * a version measure is done. TODO: Sangeethaa This method will have to
	 * change when we get all the page items captued as XML 1) The
	 * MeasureDAO.clone() method should be re written in here
	 * 
	 * @param creatingDraft
	 *            the creating draft
	 * @param oldMeasureId
	 *            the old measure id
	 * @param clonedMeasureId
	 *            the cloned measure id
	 */
	@Override
	public final void cloneMeasureXml(final boolean creatingDraft, final String oldMeasureId, final String clonedMeasureId) {
		logger.info("In MeasureLibraryServiceImpl.cloneMeasureXml() method. Clonig for Measure: " + oldMeasureId);
		ManageMeasureDetailModel measureDetailModel = null;
		if (creatingDraft) {
			measureDetailModel = getMeasure(oldMeasureId);// get the
			// measureDetailsmodel object for which draft have to be created..
			Measure measure = getService().getById(clonedMeasureId);
			// get the Cloned Measure Revision Number reset to '000' when cloned.
			measure.setRevisionNumber("000");
			// Cloned version of the Measure.
			createMeasureDetailsModelFromMeasure(measureDetailModel, measure); // apply
			// measure values in the created MeasureDetailsModel.
		} else {
			measureDetailModel = getMeasure(clonedMeasureId);
		}
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		measureXmlModel.setMeasureId(measureDetailModel.getId());
		measureXmlModel.setXml(createXml(measureDetailModel).toString());
		measureXmlModel.setToReplaceNode(MEASURE_DETAILS);
		saveMeasureXml(measureXmlModel);
		logger.info("Clone of Measure_xml is Successful");
	}
	
	/**
	 * Adding additonal fields in model from measure table.
	 * 
	 * @param manageMeasureDetailModel
	 *            - {@link ManageMeasureDetailModel}.
	 * @param measure
	 *            - {@link Measure}.
	 */
	private void convertAddlXmlElementsToModel(final ManageMeasureDetailModel manageMeasureDetailModel, final Measure measure) {
		logger.info("In easureLibraryServiceImpl.convertAddlXmlElementsToModel()");
		manageMeasureDetailModel.setId(measure.getId());
		manageMeasureDetailModel.setCalenderYear(manageMeasureDetailModel.getPeriodModel().isCalenderYear());
		if (!manageMeasureDetailModel.getPeriodModel().isCalenderYear()){
			manageMeasureDetailModel.setMeasFromPeriod(manageMeasureDetailModel.getPeriodModel() != null ? manageMeasureDetailModel
					.getPeriodModel().getStartDate() : null);
			manageMeasureDetailModel.setMeasToPeriod(manageMeasureDetailModel.getPeriodModel() != null ? manageMeasureDetailModel
					.getPeriodModel().getStopDate() : null);
		}
		manageMeasureDetailModel.setEndorseByNQF((StringUtils.isNotBlank(
				manageMeasureDetailModel.getEndorsement()) ? true : false));
		manageMeasureDetailModel.setOrgVersionNumber(MeasureUtility.formatVersionText(measure.getRevisionNumber(),
				String.valueOf(measure.getVersionNumber())));
		manageMeasureDetailModel.setVersionNumber(MeasureUtility.getVersionText(manageMeasureDetailModel.getOrgVersionNumber(),
				measure.isDraft()));
		manageMeasureDetailModel.setFinalizedDate(DateUtility.convertDateToString(measure.getFinalizedDate()));
		manageMeasureDetailModel.setDraft(measure.isDraft());
		manageMeasureDetailModel.setValueSetDate(DateUtility.convertDateToStringNoTime(measure.getValueSetDate()));
		manageMeasureDetailModel.setNqfId(manageMeasureDetailModel.getNqfModel() != null ?
				manageMeasureDetailModel.getNqfModel()
				.getExtension() : null);
		manageMeasureDetailModel.seteMeasureId(measure.geteMeasureId());
		manageMeasureDetailModel.setMeasureOwnerId(measure.getOwner().getId());
		logger.info("Exiting easureLibraryServiceImpl.convertAddlXmlElementsToModel() method..");
	}
	
	/**
	 * Method un marshalls MeasureXML into ManageMeasureDetailModel object.
	 * 
	 * @param xmlModel
	 *            -MeasureXmlModel
	 * @param measure
	 *            - Measure
	 * @return ManageMeasureDetailModel
	 */
	private ManageMeasureDetailModel convertXmltoModel(final MeasureXmlModel xmlModel, final Measure measure) {
		logger.info("In MeasureLibraryServiceImpl.convertXmltoModel()");
		ManageMeasureDetailModel details = null;
		String xml = null;
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			xml = new XmlProcessor(xmlModel.getXml()).getXmlByTagName(MEASURE_DETAILS);
		}
		try {
			if (xml == null) { // TODO: This Check should be replaced when the
				// DataConversion is complete.
				logger.info("xml is null or xml doesn't contain measureDetails tag");
				details = new ManageMeasureDetailModel();
				createMeasureDetailsModelFromMeasure(details, measure);
			} else {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("MeasureDetailsModelMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(ManageMeasureDetailModel.class);
				unmar.setWhitespacePreserve(true);
				// logger.info("unmarshalling xml.. " + xml);
				details = (ManageMeasureDetailModel) unmar.unmarshal(new InputSource(new StringReader(xml)));
				// logger.info("unmarshalling complete.." + details.toString());
				convertAddlXmlElementsToModel(details, measure);
			}
			
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load MeasureDetailsModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		return details;
	}
	
	/**
	 * Convert xmlto quality data dto model.
	 * 
	 * @param xmlModel
	 *            the xml model
	 * @return the quality data model wrapper
	 */
	private QualityDataModelWrapper convertXmltoQualityDataDTOModel(final MeasureXmlModel xmlModel) {
		logger.info("In MeasureLibraryServiceImpl.convertXmltoQualityDataDTOModel()");
		QualityDataModelWrapper details = null;
		String xml = null;
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			xml = new XmlProcessor(xmlModel.getXml()).getXmlByTagName("measure");
			// logger.info("xml by tag name elementlookup" + xml);
		}
		try {
			if (xml == null) {// TODO: This Check should be replaced when the
				// DataConversion is complete.
				logger.info("xml is null or xml doesn't contain elementlookup tag");
				
			} else {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("QualityDataModelMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(QualityDataModelWrapper.class);
				unmar.setWhitespacePreserve(true);
				// logger.info("unmarshalling xml..elementlookup " + xml);
				details = (QualityDataModelWrapper) unmar.unmarshal(new InputSource(new StringReader(xml)));
				logger.info("unmarshalling complete..elementlookup" + details.getQualityDataDTO().get(0).getCodeListName());
			}
			
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load QualityDataModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		return details;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#createAndSaveElementLookUp(java.util.ArrayList, java.lang.String)
	 */
	@Override
	public final void createAndSaveElementLookUp(final List<QualityDataSetDTO> list, final String measureID,
			String expProfileToAllQDM) {
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		wrapper.setQualityDataDTO(list);
		if((expProfileToAllQDM!=null) && !expProfileToAllQDM.isEmpty()){
			wrapper.setVsacExpIdentifier(expProfileToAllQDM);
		}
		ByteArrayOutputStream stream = createQDMXML(wrapper);
		int startIndex = stream.toString().indexOf("<elementLookUp", 0);
		int lastIndex = stream.toString().indexOf("</measure>", startIndex);
		String xmlString = stream.toString().substring(startIndex, lastIndex);
		String nodeName = "elementLookUp";
		
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(measureID);
		exportModal.setParentNode("/measure");
		exportModal.setToReplaceNode("elementLookUp");
		System.out.println("XML " + xmlString);
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureID);
		if (((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) && ((nodeName != null) && StringUtils.isNotBlank(nodeName))) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			String result = xmlProcessor.replaceNode(xmlString, nodeName, "measure");
			System.out.println("result" + result);
			exportModal.setXml(result);
			getService().saveMeasureXml(exportModal);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#createAndSaveCQLLookUp(java.util.ArrayList, java.lang.String)
	 */
	@Override
	public final void createAndSaveCQLLookUp(final List<QualityDataSetDTO> list, final String measureID,
			String expProfileToAllQDM) {
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		wrapper.setQualityDataDTO(list);
		if((expProfileToAllQDM!=null) && !expProfileToAllQDM.isEmpty()){
			wrapper.setVsacExpIdentifier(expProfileToAllQDM);
		}
		ByteArrayOutputStream stream = createQDMXML(wrapper);
		int startIndex = stream.toString().indexOf("<cqlLookUp", 0);
		int lastIndex = stream.toString().indexOf("</measure>", startIndex);
		String xmlString = stream.toString().substring(startIndex, lastIndex);
		String nodeName = "cqlLookUp";
		
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(measureID);
		exportModal.setParentNode("/measure");
		exportModal.setToReplaceNode("cqlLookUp");
		System.out.println("XML " + xmlString);
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureID);
		if (((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) && ((nodeName != null) && StringUtils.isNotBlank(nodeName))) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			String result = xmlProcessor.replaceNode(xmlString, nodeName, "measure");
			System.out.println("result" + result);
			exportModal.setXml(result);
			getService().saveMeasureXml(exportModal);
		}
		
	}
	
	/**
	 * This should be removed when we do a batch save in Measure_XML on
	 * production.
	 * 
	 * @param model
	 *            the model
	 * @param measure
	 *            the measure
	 */
	private void createMeasureDetailsModelFromMeasure(final ManageMeasureDetailModel model, final Measure measure) {
		logger.info("In MeasureLibraryServiceImpl.createMeasureDetailsModelFromMeasure()");
		model.setId(measure.getId());
		model.setName(measure.getDescription());
		model.setShortName(measure.getaBBRName());
		model.setMeasScoring(measure.getMeasureScoring());
		model.setOrgVersionNumber(MeasureUtility.formatVersionText(measure.getRevisionNumber(),
				String.valueOf(measure.getVersionNumber())));
		model.setVersionNumber(MeasureUtility.getVersionText(model.getOrgVersionNumber(), measure.isDraft()));
		model.setFinalizedDate(DateUtility.convertDateToString(measure.getFinalizedDate()));
		model.setDraft(measure.isDraft());
		model.setMeasureSetId(measure.getMeasureSet().getId());
		model.setValueSetDate(DateUtility.convertDateToStringNoTime(measure.getValueSetDate()));
		model.seteMeasureId(measure.geteMeasureId());
		/*model.setMeasureStatus(measure.getMeasureStatus());*/
		model.setMeasureOwnerId(measure.getOwner().getId());
		logger.info("Exiting MeasureLibraryServiceImpl.createMeasureDetailsModelFromMeasure()");
	}
	
	/**
	 * Creates the measure details xml.
	 * 
	 * @param measureDetailModel
	 *            the measure detail model
	 * @param measure
	 *            the measure
	 * @return the string
	 */
	public final String createMeasureDetailsXml(final ManageMeasureDetailModel measureDetailModel, final Measure measure) {
		logger.info("In MeasureLibraryServiceImpl.createMeasureDetailsXml()");
		setAdditionalAttrsForMeasureXml(measureDetailModel, measure);
		logger.info("creating XML from Measure Details Model");
		ByteArrayOutputStream stream = createXml(measureDetailModel);
		logger.debug(stream.toString());
		return stream.toString();
	}
	
	/**
	 * Creates the measure xml model.
	 * 
	 * @param manageMeasureDetailModel
	 *            the manage measure detail model
	 * @param measure
	 *            the measure
	 * @param replaceNode
	 *            the replace node
	 * @param parentNode
	 *            the parent node
	 * @return the measure xml model
	 */
	private MeasureXmlModel createMeasureXmlModel(final ManageMeasureDetailModel manageMeasureDetailModel, final Measure measure,
			final String replaceNode, final String parentNode) {
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		measureXmlModel.setMeasureId(measure.getId());
		measureXmlModel.setXml(createMeasureDetailsXml(manageMeasureDetailModel, measure));
		measureXmlModel.setToReplaceNode(replaceNode);
		measureXmlModel.setParentNode(parentNode);
		return measureXmlModel;
	}
	
	/**
	 * Method to create XML from QualityDataModelWrapper object.
	 * 
	 * @param qualityDataSetDTO
	 *            - {@link QualityDataModelWrapper}.
	 * @return {@link ByteArrayOutputStream}.
	 * */
	private ByteArrayOutputStream createQDMXML(final QualityDataModelWrapper qualityDataSetDTO) {
		logger.info("In ManageCodeLiseServiceImpl.createXml()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("QualityDataModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(qualityDataSetDTO);
			logger.info("Marshalling of QualityDataSetDTO is successful..");
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load QualityDataModelMapping.xml" + e);
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
		logger.info("Exiting ManageCodeLiseServiceImpl.createXml()");
		return stream;
	}
	
	/**
	 * Method to create XML from CQLQualityDataModelWrapper object.
	 * 
	 * @param qualityDataSetDTO
	 *            - {@link CQLQualityDataModelWrapper}.
	 * @return {@link ByteArrayOutputStream}.
	 * */
	private ByteArrayOutputStream createQDMXML(final CQLQualityDataModelWrapper qualityDataSetDTO) {
		logger.info("In ManageCodeLiseServiceImpl.createXml()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLValueSetsMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(qualityDataSetDTO);
			logger.info("Marshalling of CQLQualityDataSetDTO is successful..");
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLValueSetsMapping.xml" + e);
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
		logger.info("Exiting ManageCodeLiseServiceImpl.createXml()");
		return stream;
	}
	
	/**
	 * Creates the xml.
	 * 
	 * @param measureDetailModel
	 *            the measure detail model
	 * @return the byte array output stream
	 */
	private ByteArrayOutputStream createXml(final ManageMeasureDetailModel measureDetailModel) {
		logger.info("In MeasureLibraryServiceImpl.createXml()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("MeasureDetailsModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(measureDetailModel);
			logger.info("Marshalling of ManageMeasureDetailsModel is successful..");
			logger.debug("Marshalling of ManageMeasureDetailsModel is successful.." + stream.toString());
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load MeasureDetailsModelMapping.xml" + e);
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
		logger.info("Exiting MeasureLibraryServiceImpl.createXml()");
		return stream;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#deleteMeasureNotes(mat.DTO.MeasureNoteDTO)
	 */
	@Override
	public final void deleteMeasureNotes(final MeasureNoteDTO measureNoteDTO) {
		MeasureNotesDAO measureNotesDAO = getMeasureNotesDAO();
		MeasureNotes measureNotes = measureNotesDAO.find(measureNoteDTO.getId());
		try {
			if(MatContextServiceUtil.get().isCurrentMeasureEditable(getMeasureDAO(),measureNoteDTO.getMeasureId())){
				getMeasureNotesService().deleteMeasureNote(measureNotes);
			}
			
			logger.info("MeasureNotes Deleted Successfully :: " + measureNotes.getId());
		} catch (Exception e) {
			logger.info("MeasureNotes not deleted. Exception occured. Measure notes Id :: " + measureNotes.getId());
		}
	}
	
	/**
	 * Extract measure search model detail.
	 * 
	 * @param currentUserId
	 *            - {@link String}
	 * @param isSuperUser
	 *            - {@link Boolean}
	 * @param dto
	 *            - {@link MeasureShareDTO}.
	 * @return {@link Result}.
	 */
	private ManageMeasureSearchModel.Result extractMeasureSearchModelDetail(final String currentUserId, final boolean isSuperUser,
			final MeasureShareDTO dto) {
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
		Measure measure = getMeasureDAO().find(dto.getMeasureId());
		detail.setName(dto.getMeasureName());
		detail.setShortName(dto.getShortName());
		detail.setScoringType(dto.getScoringType());
		detail.setStatus(dto.getStatus());
		detail.setId(dto.getMeasureId());
		detail.setStatus(dto.getStatus());
		detail.seteMeasureId(dto.geteMeasureId());
		
		String measureReleaseVersion = (measure.getReleaseVersion() == null)?"":measure.getReleaseVersion();
		if(measureReleaseVersion.length() == 0 || measureReleaseVersion.startsWith("v4") || measureReleaseVersion.startsWith("v3")){
			detail.setClonable(false);
		}else{
			detail.setClonable(isOwner || isSuperUser);
		}
		
		detail.setEditable((isOwner || isSuperUser || ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) && dto.isDraft());
		detail.setExportable(dto.isPackaged());
		detail.setHqmfReleaseVersion(measure.getReleaseVersion());
		detail.setSharable(isOwner || isSuperUser);
		detail.setMeasureLocked(dto.isLocked());
		detail.setLockedUserInfo(dto.getLockedUserInfo());
		User user = getUserService().getById(dto.getOwnerUserId());
		detail.setOwnerfirstName(user.getFirstName());
		detail.setOwnerLastName(user.getLastName());
		detail.setOwnerEmailAddress(user.getEmailAddress());
		detail.setDraft(dto.isDraft());
		String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(dto.getVersion(), dto.getRevisionNumber(), dto.isDraft());
		detail.setVersion(formattedVersion);
		detail.setFinalizedDate(dto.getFinalizedDate());
		detail.setMeasureSetId(dto.getMeasureSetId());
		return detail;
	}
	
	/**
	 * Find out maximum version number.
	 * 
	 * @param measureSetId
	 *            - {@link String}.
	 * @return {@link String}. *
	 */
	private String findOutMaximumVersionNumber(final String measureSetId) {
		String maxVerNum = getService().findOutMaximumVersionNumber(measureSetId);
		return maxVerNum;
	}
	
	/**
	 * Find out version number.
	 * 
	 * @param measureId
	 *            - {@link String}.
	 * @param measureSetId
	 *            - {@link String}.
	 * @return {@link String}. *
	 */
	private String findOutVersionNumber(final String measureId, final String measureSetId) {
		String maxVerNum = getService().findOutVersionNumber(measureId, measureSetId);
		return maxVerNum;
	}
	
	/**
	 * * Find All QDM's which are used in Clause Workspace tag's or in
	 * Supplemental Data Elements or in Attribute tags.
	 * 
	 * @param appliedQDMList
	 *            the applied qdm list
	 * @param measureXmlModel
	 *            the measure xml model
	 * @return the array list
	 */
	private ArrayList<QualityDataSetDTO> findUsedQDMs(final ArrayList<QualityDataSetDTO> appliedQDMList,
			final MeasureXmlModel measureXmlModel) {
		
		XmlProcessor processor = new XmlProcessor(measureXmlModel.getXml());
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		for (QualityDataSetDTO dataSetDTO : appliedQDMList) {
			String XPATH_EXPRESSION = "/measure//subTree//elementRef/@id=";
			XPATH_EXPRESSION = XPATH_EXPRESSION.concat("'").concat(dataSetDTO.getUuid()).
					concat("' or /measure//subTree//elementRef/attribute/@qdmUUID= '").concat(dataSetDTO.getUuid()).
					concat("' or /measure/supplementalDataElements//@id='").concat(dataSetDTO.getUuid())
					.concat("' or /measure/measureDetails/itemCount//@id='").concat(dataSetDTO.getUuid())
					.concat("' or /measure//measureGrouping//packageClause//elementRef/@id='").concat(dataSetDTO.getUuid())
					.concat("'");
			
			try {
				Boolean isUsed = (Boolean) xPath.evaluate(XPATH_EXPRESSION, processor.getOriginalDoc().getDocumentElement(),
						XPathConstants.BOOLEAN);
				dataSetDTO.setUsed(isUsed);
			} catch (XPathExpressionException e) {
				
				e.printStackTrace();
			}
		}
		
		return appliedQDMList;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#generateAndSaveMaxEmeasureId(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public final int generateAndSaveMaxEmeasureId(final ManageMeasureDetailModel measureModel) {
		if(measureModel.isEditable() && measureModel.geteMeasureId()==0){
			MeasurePackageService service = getService();
			Measure meas = service.getById(measureModel.getId());
			int eMeasureId = service.saveAndReturnMaxEMeasureId(meas);
			measureModel.seteMeasureId(eMeasureId);
			saveMaxEmeasureIdinMeasureXML(measureModel);
			return eMeasureId;	
		}
		return -1;
	}
	
	/**
	 * Save max emeasure idin measure xml.
	 *
	 * @param measureModel the measure model
	 */
	public void saveMaxEmeasureIdinMeasureXML(ManageMeasureDetailModel measureModel){
		
		MeasureXmlModel model = getMeasureXmlForMeasure(measureModel.getId());
		XmlProcessor xmlProcessor = new XmlProcessor(model.getXml());
		
		try {
			
			xmlProcessor.createEmeasureIdNode(measureModel.geteMeasureId());
			String newXml = xmlProcessor.transform(xmlProcessor.getOriginalDoc());
			model.setXml(newXml);
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		getService().saveMeasureXml(model);
	}
	
	/**
	 * Gets the all data type attributes.
	 * 
	 * @param qdmName
	 *            - {@link String}.
	 * @return {@link List} of {@link QDSAttributes}. *
	 */
	private List<QDSAttributes> getAllDataTypeAttributes(final String qdmName) {
		List<QDSAttributes> attrs = getAttributeDAO().findByDataType(qdmName, context);
		List<QDSAttributes> attrs1 = getAttributeDAO().getAllDataFlowAttributeName();
		Collections.sort(attrs, attributeComparator);
		Collections.sort(attrs1, attributeComparator);
		attrs.addAll(attrs1);
		// Collections.sort(attrs, attributeComparator);
		return attrs;
	}
	
	/**
	 * Check if qdm data type is present.
	 *
	 * @param dataTypeName the data type name
	 * @return true, if successful
	 */
	public boolean checkIfQDMDataTypeIsPresent(String dataTypeName){
		boolean checkIfDataTypeIsPresent = false;
		DataTypeDAO dataTypeDAO = (DataTypeDAO)context.getBean("dataTypeDAO");
		DataType dataType = dataTypeDAO.findByDataTypeName(dataTypeName);
		if(dataType!=null){
			checkIfDataTypeIsPresent = true;
		}
		return checkIfDataTypeIsPresent;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getAllMeasureNotesByMeasureID(java.lang.String)
	 */
	@Override
	public final MeasureNotesModel getAllMeasureNotesByMeasureID(final String measureID) {
		MeasureNotesModel measureNotesModel = new MeasureNotesModel();
		ArrayList<MeasureNoteDTO> data = new ArrayList<MeasureNoteDTO>();
		Measure measure = getMeasureDAO().find(measureID);
		if (measure != null) {
			List<MeasureNotes> measureNotesList = getMeasureNotesService().getAllMeasureNotesByMeasureID(measureID);
			if ((measureNotesList != null) && !measureNotesList.isEmpty()) {
				for (MeasureNotes measureNotes : measureNotesList) {
					if (measureNotes != null) {
						MeasureNoteDTO measureNoteDTO = new MeasureNoteDTO();
						measureNoteDTO.setMeasureId(measureID);
						measureNoteDTO.setId(measureNotes.getId());
						if (measureNotes.getModifyUser() != null) {
							measureNoteDTO.setLastModifiedByEmailAddress(
									measureNotes.getModifyUser().getEmailAddress());
						} else if (measureNotes.getCreateUser() != null) {
							measureNoteDTO.setLastModifiedByEmailAddress(
									measureNotes.getCreateUser().getEmailAddress());
						}
						measureNoteDTO.setNoteDesc(measureNotes.getNoteDesc());
						measureNoteDTO.setNoteTitle(measureNotes.getNoteTitle());
						Date lastModifiedDate = measureNotes.getLastModifiedDate();
						SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a z");
						if (lastModifiedDate != null) {
							measureNoteDTO.setLastModifiedDate(dateFormat.format(lastModifiedDate));
						}
						data.add(measureNoteDTO);
					}
				}
			}
		}
		measureNotesModel.setData(data);
		return measureNotesModel;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getAllRecentMeasureForUser(java.lang.String)
	 */
	/** Method to retrieve all Recently searched measure's for the logged in User from 'Recent_MSR_Activity_Log' table.
	 * @param userId - String logged in user id.
	 * @return {@link ManageMeasureSearchModel}. **/
	@Override
	public ManageMeasureSearchModel getAllRecentMeasureForUser(String userId) {
		// Call to fetch
		ArrayList<RecentMSRActivityLog> recentMeasureActivityList = (ArrayList<RecentMSRActivityLog>)
				recentMSRActivityLogDAO.getRecentMeasureActivityLog(userId);
		ManageMeasureSearchModel manageMeasureSearchModel = new ManageMeasureSearchModel();
		List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<ManageMeasureSearchModel.Result>();
		manageMeasureSearchModel.setData(detailModelList);
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		for (RecentMSRActivityLog activityLog : recentMeasureActivityList) {
			Measure measure = getMeasureDAO().find(activityLog.getMeasureId());
			ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
			detail.setName(measure.getDescription());
			detail.setShortName(measure.getaBBRName());
			detail.setId(measure.getId());
			detail.setDraft(measure.isDraft());
			detail.setExportable(measure.getExportedDate() != null); // to show export icon.
			detail.setHqmfReleaseVersion(measure.getReleaseVersion());
			/*detail.setStatus(measure.getMeasureStatus());*/
			String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(measure.getVersion(), measure.getRevisionNumber(),
					measure.isDraft());
			detail.setVersion(formattedVersion);
			detail.setFinalizedDate(measure.getFinalizedDate());
			detail.setOwnerfirstName(measure.getOwner().getFirstName());
			detail.setOwnerLastName(measure.getOwner().getLastName());
			detail.setOwnerEmailAddress(measure.getOwner().getEmailAddress());
			detail.setMeasureSetId(measure.getMeasureSet().getId());
			detail.setScoringType(measure.getMeasureScoring());
			boolean isLocked = getMeasureDAO().isMeasureLocked(measure.getId());
			detail.setMeasureLocked(isLocked);
			// Prod issue fixed - Measure Shared with Regular users not loaded as editable measures.
			List<MeasureShareDTO> measureShare = getMeasureDAO().
					getMeasureShareInfoForMeasureAndUser(currentUserId, measure.getId());
			if (measureShare.size() > 0) {
				detail.setEditable(((currentUserId.equals(measure.getOwner().getId()) || isSuperUser
						|| ShareLevel.MODIFY_ID.equals(
								measureShare.get(0).getShareLevel()))) && measure.isDraft());
			} else {
				detail.setEditable((currentUserId.equals(measure.getOwner().getId()) || isSuperUser)
						&& measure.isDraft());
			}
			if (isLocked && (measure.getLockedUser() != null)) {
				LockedUserInfo lockedUserInfo = new LockedUserInfo();
				lockedUserInfo.setUserId(measure.getLockedUser().getId());
				lockedUserInfo.setEmailAddress(measure.getLockedUser()
						.getEmailAddress());
				lockedUserInfo.setFirstName(measure.getLockedUser().getFirstName());
				lockedUserInfo.setLastName(measure.getLockedUser().getLastName());
				detail.setLockedUserInfo(lockedUserInfo);
			}
			detailModelList.add(detail);
		}
		return manageMeasureSearchModel;
	}
	
	/** Gets the and validate value set date.
	 * 
	 * @param valueSetDateStr - {@link String}.
	 * @return the and validate value set date
	 * @throws InvalidValueSetDateException - {@link Exception}. * */
	private void getAndValidateValueSetDate(final String valueSetDateStr) throws InvalidValueSetDateException {
		if (StringUtils.isNotBlank(valueSetDateStr)) {
			DateStringValidator dsv = new DateStringValidator();
			int validationCode = dsv.isValidDateString(valueSetDateStr);
			if (validationCode != DateStringValidator.VALID) {
				throw new InvalidValueSetDateException();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getAppliedQDMFromMeasureXml(java.lang.String, boolean)
	 */
	//	@Override
	//	public final ArrayList<QualityDataSetDTO> getAppliedQDMFromMeasureXml(final String measureId,
	//			final boolean checkForSupplementData) {
	//		MeasureXmlModel measureXmlModel = getMeasureXmlForMeasure(measureId);
	//		QualityDataModelWrapper details = convertXmltoQualityDataDTOModel(measureXmlModel);
	//		ArrayList<QualityDataSetDTO> finalList = new ArrayList<QualityDataSetDTO>();
	//		if (details != null) {
	//			if ((details.getQualityDataDTO() != null) && (details.getQualityDataDTO().size() != 0)) {
	//				logger.info(" details.getQualityDataDTO().size() :" + details.getQualityDataDTO().size());
	//				for (QualityDataSetDTO dataSetDTO : details.getQualityDataDTO()) {
	//					if ((dataSetDTO.getOccurrenceText() != null)
	//							&& StringUtils.isNotBlank(dataSetDTO.getOccurrenceText())
	//							&& StringUtils.isNotEmpty(dataSetDTO.getOccurrenceText())) {
	//						dataSetDTO.setSpecificOccurrence(true);
	//					}
	//					if (dataSetDTO.getCodeListName() != null) {
	//						if ((checkForSupplementData && dataSetDTO.isSuppDataElement())) {
	//							continue;
	//						} else {
	//							finalList.add(dataSetDTO);
	//						}
	//					}
	//				}
	//			}
	//			Collections.sort(finalList, new Comparator<QualityDataSetDTO>() {
	//				@Override
	//				public int compare(final QualityDataSetDTO o1, final QualityDataSetDTO o2) {
	//					return o1.getCodeListName().compareToIgnoreCase(o2.getCodeListName());
	//				}
	//			});
	//		}
	//
	//		finalList = findUsedQDMs(finalList, measureXmlModel);
	//		logger.info("finalList()of QualityDataSetDTO ::" + finalList.size());
	//		return finalList;
	//
	//	}
	
	@Override
	public final QualityDataModelWrapper getAppliedQDMFromMeasureXml(final String measureId,
			final boolean checkForSupplementData) {
		MeasureXmlModel measureXmlModel = getMeasureXmlForMeasure(measureId);
		QualityDataModelWrapper details = convertXmltoQualityDataDTOModel(measureXmlModel);
		//add expansion Profile if existing
		String expProfilestr = getDefaultExpansionIdentifier(measureId);
		if(expProfilestr != null){
			details.setVsacExpIdentifier(expProfilestr);
		}
		ArrayList<QualityDataSetDTO> finalList = new ArrayList<QualityDataSetDTO>();
		if (details != null) {
			if ((details.getQualityDataDTO() != null) && (details.getQualityDataDTO().size() != 0)) {
				logger.info(" details.getQualityDataDTO().size() :" + details.getQualityDataDTO().size());
				for (QualityDataSetDTO dataSetDTO : details.getQualityDataDTO()) {
					if ((dataSetDTO.getOccurrenceText() != null)
							&& StringUtils.isNotBlank(dataSetDTO.getOccurrenceText())
							&& StringUtils.isNotEmpty(dataSetDTO.getOccurrenceText())) {
						dataSetDTO.setSpecificOccurrence(true);
					}
					if (dataSetDTO.getCodeListName() != null) {
						if ((checkForSupplementData && dataSetDTO.isSuppDataElement())) {
							continue;
						} else {
							finalList.add(dataSetDTO);
						}
					}
				}
			}
			Collections.sort(finalList, new Comparator<QualityDataSetDTO>() {
				@Override
				public int compare(final QualityDataSetDTO o1, final QualityDataSetDTO o2) {
					return o1.getCodeListName().compareToIgnoreCase(o2.getCodeListName());
				}
			});
		}
		
		finalList = findUsedQDMs(finalList, measureXmlModel);
		details.setQualityDataDTO(finalList);
		logger.info("finalList()of QualityDataSetDTO ::" + finalList.size());
		return details;
		
	}
	
	@Override
	public final CQLQualityDataModelWrapper getCQLAppliedQDMFromMeasureXml(final String measureId,
			final boolean checkForSupplementData) {
		MeasureXmlModel measureXmlModel = getMeasureXmlForMeasure(measureId);
		CQLQualityDataModelWrapper details = convertXmltoCQLQualityDataDTOModel(measureXmlModel);
		//add expansion Profile if existing
		String expProfilestr = getDefaultExpansionIdentifier(measureId);
		if(expProfilestr != null){
			details.setVsacExpIdentifier(expProfilestr);
		}
		ArrayList<CQLQualityDataSetDTO> finalList = new ArrayList<CQLQualityDataSetDTO>();
		if (details != null) {
			if ((details.getQualityDataDTO() != null) && (details.getQualityDataDTO().size() != 0)) {
				logger.info(" details.getQualityDataDTO().size() :" + details.getQualityDataDTO().size());
				for (CQLQualityDataSetDTO dataSetDTO : details.getQualityDataDTO()) {
					if (dataSetDTO.getCodeListName() != null) {
						if ((checkForSupplementData && dataSetDTO.isSuppDataElement())) {
							continue;
						} else {
							//if(!dataSetDTO.getDataType().equalsIgnoreCase("Patient Characteristic Birthdate") && !dataSetDTO.getDataType().equalsIgnoreCase("Patient Characteristic Expired")) {
							finalList.add(dataSetDTO);
							//}
						}
					}
				}
			}
			Collections.sort(finalList, new Comparator<CQLQualityDataSetDTO>() {
				@Override
				public int compare(final CQLQualityDataSetDTO o1, final CQLQualityDataSetDTO o2) {
					return o1.getCodeListName().compareToIgnoreCase(o2.getCodeListName());
				}
			});
		}
		
		details.setQualityDataDTO(finalList);
		logger.info("finalList()of CQLQualityDataSetDTO ::" + finalList.size());
		return details;
		
	}
	
	@Override
	public final CQLQualityDataModelWrapper getDefaultCQLSDEFromMeasureXml(final String measureId) {
		logger.info("Inside MeasureLibraryServiceImp :: getDefaultCQLSDEFromMeasureXml :: Start");
		MeasureXmlModel measureXmlModel = getMeasureXmlForMeasure(measureId);
		CQLQualityDataModelWrapper details = convertXmltoCQLQualityDataDTOModel(measureXmlModel);
		ArrayList<CQLQualityDataSetDTO> finalList = new ArrayList<CQLQualityDataSetDTO>();
		if (details != null) {
			if ((details.getQualityDataDTO() != null) && (details.getQualityDataDTO().size() != 0)) {
				logger.info(" details.getQualityDataDTO().size() :" + details.getQualityDataDTO().size());
				for (CQLQualityDataSetDTO dataSetDTO : details.getQualityDataDTO()) {
					if (dataSetDTO.getCodeListName() != null) {
						if ((dataSetDTO.isSuppDataElement())) {
							finalList.add(dataSetDTO);
						}
					}
				}
			}
		}
		details.setQualityDataDTO(finalList);
		logger.info("finalList()of QualityDataSetDTO ::" + finalList.size());
		logger.info("Inside MeasureLibraryServiceImp :: getDefaultSDEFromMeasureXml :: END");
		return details;
	}
	
	private CQLQualityDataModelWrapper convertXmltoCQLQualityDataDTOModel(MeasureXmlModel measureXmlModel) {

		logger.info("In MeasureLibraryServiceImpl.convertXmltoCQLQualityDataDTOModel()");
		CQLQualityDataModelWrapper details = null;
		String xml = null;
		if ((measureXmlModel != null) && StringUtils.isNotBlank(measureXmlModel.getXml())) {
			xml = new XmlProcessor(measureXmlModel.getXml()).getXmlByTagName("cqlLookUp");
		}
		try {
			if (xml == null) {
				// TODO: This Check should be replaced when the
				// DataConversion is complete.
				logger.info("xml is null or xml doesn't contain valuesets tag");
				
			} else {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLValueSetsMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(CQLQualityDataModelWrapper.class);
				unmar.setWhitespacePreserve(true);
				details = (CQLQualityDataModelWrapper) unmar.unmarshal(new InputSource(new StringReader(xml)));
				logger.info("unmarshalling complete..valuesets" + details.getQualityDataDTO().get(0).getCodeListName());
			}
			
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLValueSetsMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		return details;
	
	}

	/**
	 * Gets the attribute dao.
	 * 
	 * @return the attribute dao
	 */
	private QDSAttributesDAO getAttributeDAO() {
		return ((QDSAttributesDAO) context.getBean("qDSAttributesDAO"));
		
	}
	
	/**
	 * Gets the context.
	 * 
	 * @return the context
	 */
	public ApplicationContext getContext() {
		return context;
	}
	
	/**
	 * Gets the locked user.
	 * 
	 * @param existingMeasure
	 *            the existing measure
	 * @return the locked user
	 */
	private User getLockedUser(final Measure existingMeasure) {
		return existingMeasure.getLockedUser();
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getMaxEMeasureId()
	 */
	@Override
	public final int getMaxEMeasureId() {
		MeasurePackageService service = getService();
		int emeasureId = service.getMaxEMeasureId();
		logger.info("**********Current Max EMeasure Id from DB ******************" + emeasureId);
		return emeasureId;
		// return 2012;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getMeasure(java.lang.String)
	 */
	@Override
	public final ManageMeasureDetailModel getMeasure(final String key) {
		logger.info("In MeasureLibraryServiceImpl.getMeasure() method..");
		logger.info("Loading Measure for MeasueId: " + key);
		Measure measure = getService().getById(key);
		MeasureXmlModel xml = getMeasureXmlForMeasure(key);
		return convertXmltoModel(xml, measure);
		
	}
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#updateComponentMeasuresOnDeletion(java.lang.String
	 */
	@Override
	public void updateMeasureXmlForDeletedComponentMeasureAndOrg(String measureId){
		logger.info("In MeasureLibraryServiceImpl. updateMeasureXmlForDeletedComponentMeasureAndOrg() method..");
		logger.info("Updating Measure for MeasueId: " + measureId);
		MeasureXmlModel xmlModel = getMeasureXmlForMeasure(measureId);
		if(xmlModel!=null){
			XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
			removeDeletedComponentMeasures(processor);
			removeDeletedSteward(processor);
			removeDeletedDevelopers(processor);
			xmlModel.setXml(processor.transform(processor.getOriginalDoc()));
			getService().saveMeasureXml(xmlModel);
		}
		
	}
	
	/**
	 * Update measure developers on deletion.
	 *
	 * @param processor the processor
	 */
	private void removeDeletedDevelopers(XmlProcessor processor) {
		try {
			NodeList developerParentNodeList = (NodeList) xPath.evaluate(
					XPATH_EXPRESSION_DEVELOPERS, processor.getOriginalDoc(),
					XPathConstants.NODESET);
			Node developerParentNode = developerParentNodeList.item(0);
			if (developerParentNode != null) {
				NodeList developerNodeList = developerParentNode
						.getChildNodes();
				for (int i = 0; i < developerNodeList.getLength(); i++) {
					Node newNode = developerNodeList.item(i);
					String developerId = newNode.getAttributes()
							.getNamedItem("id").getNodeValue();
					Organization org = organizationDAO.findById(developerId);
					if (org == null) {
						developerParentNode.removeChild(newNode);
						logger.info("Deleted MeasureDevelopers Deleted successFully From MeasureXml.");
					} else {
						newNode.setTextContent(org.getOrganizationName());
						logger.info("Developer's Name updated in MeasureXml.");
					}
				}
			}
		} catch (XPathExpressionException e) {
			logger.info("Failed to delete  MeasureDevelopers From MeasureXml. Exception occured.");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Update steward on deletion.
	 *
	 * @param processor the processor
	 */
	private void removeDeletedSteward(XmlProcessor processor) {
		try {
			// steward
			Node stewardParentNode = (Node) xPath.evaluate(
					XPATH_EXPRESSION_STEWARD, processor.getOriginalDoc(),
					XPathConstants.NODE);
			if (stewardParentNode != null) {
				String id = stewardParentNode.getAttributes()
						.getNamedItem("id").getNodeValue();
				Organization org = organizationDAO.findById(id);
				if (org == null) {
					removeNode(XPATH_EXPRESSION_STEWARD,
							processor.getOriginalDoc());
					logger.info("Deleted steward Deleted successFully From MeasureXml.");
				} else {
					stewardParentNode.setTextContent(org.getOrganizationName());
					logger.info("Steward Name updated in measure Xml.");
				}
			}
		} catch (XPathExpressionException e) {
			logger.info("Failed to delete  steward From MeasureXml. Exception occured.");
			e.printStackTrace();
		}
		
	}
	/**
	 * Update component measures on deletion.
	 *
	 * @param processor the processor
	 */
	private void removeDeletedComponentMeasures(XmlProcessor processor) {
		try {
			NodeList componentMeasureParentNodeList = (NodeList) xPath
					.evaluate(XPATH_EXPRESSION_COMPONENT_MEASURES,
							processor.getOriginalDoc(), XPathConstants.NODESET);
			Node componentMeasureParentNode = componentMeasureParentNodeList
					.item(0);
			if (componentMeasureParentNode != null) {
				NodeList nodeList = componentMeasureParentNode.getChildNodes();
				
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node newNode = nodeList.item(i);
					String id = newNode.getAttributes().getNamedItem("id")
							.getNodeValue();
					boolean isDeleted = getMeasureDAO().getMeasure(id);
					if (!isDeleted) {
						componentMeasureParentNode.removeChild(newNode);
					}
				}
			}
			logger.info("Deleted componentMeasures Deleted successFully From MeasureXml.");
		} catch (XPathExpressionException e) {
			logger.info("Failed to delete  componentMeasures From MeasureXml. Exception occured.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the measure dao.
	 * 
	 * @return the measure dao
	 */
	private MeasureDAO getMeasureDAO() {
		return ((MeasureDAO) context.getBean("measureDAO"));
	}
	
	/**
	 * Gets the measure notes dao.
	 * 
	 * @return the measure notes dao
	 */
	private MeasureNotesDAO getMeasureNotesDAO() {
		return ((MeasureNotesDAO) context.getBean("measureNotesDAO"));
	}
	
	/**
	 * Gets the measure notes service.
	 * 
	 * @return the measure notes service
	 */
	private MeasureNotesService getMeasureNotesService() {
		return ((MeasureNotesService) context.getBean("measureNotesService"));
		
	}
	
	/**
	 * Gets the measure package service.
	 * 
	 * @return {@link MeasurePackageService}. *
	 */
	public final MeasurePackageService getMeasurePackageService() {
		return (MeasurePackageService) context.getBean("measurePackageService");
	}
	
	/**
	 * Gets the measure xml dao.
	 * 
	 * @return the measure xml dao
	 */
	private MeasureXMLDAO getMeasureXMLDAO() {
		return ((MeasureXMLDAO) context.getBean("measureXMLDAO"));
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getMeasureXmlForMeasure(java.lang.String)
	 */
	@Override
	public final MeasureXmlModel getMeasureXmlForMeasure(final String measureId) {
		logger.info("In MeasureLibraryServiceImpl.getMeasureXmlForMeasure()");
		MeasureXmlModel measureXmlModel = getService().getMeasureXmlForMeasure(measureId);
		if (measureXmlModel == null) {
			logger.info("Measure XML is null");
		} else {
			logger.debug("XML ::: " + measureXmlModel.getXml());
		}
		return measureXmlModel;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getMeasureXmlForMeasureAndSortedSubTreeMap(java.lang.String)
	 */
	@Override
	public SortedClauseMapResult getMeasureXmlForMeasureAndSortedSubTreeMap(final String measureId){
		SortedClauseMapResult result = new SortedClauseMapResult();
		MeasureXmlModel model = getMeasureXmlForMeasure(measureId);
		LinkedHashMap<String, String> sortedSubTreeMap = getSortedClauseMap(measureId);
		result.setMeasureXmlModel(model);
		result.setClauseMap(sortedSubTreeMap);
		return result;
	}
	
	/**
	 * Gets the sorted clause map.
	 *
	 * @param measureId the measure id
	 * @return the sorted clause map
	 */
	@Override
	public LinkedHashMap<String, String> getSortedClauseMap(String measureId){
		
		logger.info("In MeasureLibraryServiceImpl.getSortedClauseMap()");
		MeasureXmlModel measureXmlModel = getService().getMeasureXmlForMeasure(
				measureId);
		
		LinkedHashMap<String, String> sortedMainClauseMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> mainClauseMap = new LinkedHashMap<String, String>();
		
		if ((measureXmlModel != null)
				&& StringUtils.isNotBlank(measureXmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(
					measureXmlModel.getXml());
			NodeList mainClauseLIst;
			NodeList instanceClauseList;
			try {
				mainClauseLIst = (NodeList) xPath.evaluate(
						"/measure//subTreeLookUp/subTree[not(@instanceOf )]",
						xmlProcessor.getOriginalDoc().getDocumentElement(),
						XPathConstants.NODESET);
				for (int i = 0; i < mainClauseLIst.getLength(); i++) {
					mainClauseMap.put(mainClauseLIst.item(i).getAttributes()
							.getNamedItem("displayName").getNodeValue(),
							mainClauseLIst.item(i).getAttributes()
							.getNamedItem("uuid").getNodeValue());
				}
				// sort the map alphabetically
				List<Entry<String, String>> mainClauses = new LinkedList<Map.Entry<String, String>>(
						mainClauseMap.entrySet());
				Collections.sort(mainClauses,
						new Comparator<Entry<String, String>>() {
					@Override
					public int compare(Entry<String, String> o1,
							Entry<String, String> o2) {
						return o1.getKey().toUpperCase()
								.compareTo(o2.getKey().toUpperCase());
					}
				});
				for (Entry<String, String> entry : mainClauses) {
					sortedMainClauseMap.put(entry.getValue(), entry.getKey());
					
					instanceClauseList = (NodeList) xPath.evaluate(
							"/measure//subTreeLookUp/subTree[@instanceOf='"
									+ entry.getValue() + "']", xmlProcessor
									.getOriginalDoc().getDocumentElement(),
									XPathConstants.NODESET);
					
					if (instanceClauseList.getLength() >= 1) {
						
						Map<String, String> instanceClauseMap = new HashMap<String, String>();
						for (int j = 0; j < instanceClauseList.getLength(); j++) {
							String uuid = instanceClauseList.item(j)
									.getAttributes().getNamedItem("uuid")
									.getNodeValue();
							String name = instanceClauseList.item(j)
									.getAttributes()
									.getNamedItem("displayName").getNodeValue();
							String instanceVal = instanceClauseList.item(j)
									.getAttributes().getNamedItem("instance")
									.getNodeValue().toUpperCase();
							String fname = "Occurrence "+ instanceVal +" of "
									+ name;
							instanceClauseMap.put(fname, uuid);
						}
						
						List<Entry<String, String>> instanceClauses = new LinkedList<Map.Entry<String, String>>(
								instanceClauseMap.entrySet());
						Collections.sort(instanceClauses,
								new Comparator<Entry<String, String>>() {
							@Override
							public int compare(
									Entry<String, String> o1,
									Entry<String, String> o2) {
								return o1
										.getKey()
										.toUpperCase()
										.compareTo(
												o2.getKey()
												.toUpperCase());
							}
						});
						for (Entry<String, String> entry1 : instanceClauses) {
							sortedMainClauseMap.put(entry1.getValue(),
									entry1.getKey());
						}
					}
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
		}
		return sortedMainClauseMap;
	}
	
	/** Gets the page count.
	 * 
	 * @param userId the user id
	 * @return {@link Integer}. * */
	/*private int getPageCount(final long totalRows, final int numberOfRows) {
		int pageCount = 0;
		int mod = (int) (totalRows % numberOfRows);
		pageCount = (int) (totalRows / numberOfRows);
		pageCount = (mod > 0) ? (pageCount + 1) : pageCount;
		return pageCount;
	}*/
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getRecentMeasureActivityLog(java.lang.String)
	 */
	@Override
	public List<RecentMSRActivityLog> getRecentMeasureActivityLog(String userId) {
		return recentMSRActivityLogDAO.getRecentMeasureActivityLog(userId);
	}
	
	/**
	 * Gets the service.
	 * 
	 * @return the service
	 */
	private MeasurePackageService getService() {
		return (MeasurePackageService) context.getBean("measurePackageService");
	}
	
	/**
	 * Gets the user service.
	 * 
	 * @return the user service
	 */
	private UserService getUserService() {
		return (UserService) context.getBean("userService");
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getUsersForShare(java.lang.String, int, int)
	 */
	@Override
	public final ManageMeasureShareModel getUsersForShare(final String measureId, final int startIndex, final int pageSize) {
		ManageMeasureShareModel model = new ManageMeasureShareModel();
		List<MeasureShareDTO> dtoList = getService().getUsersForShare(measureId, startIndex, pageSize);
		model.setResultsTotal(getService().countUsersForMeasureShare());
		List<MeasureShareDTO> dataList = new ArrayList<MeasureShareDTO>();
		for (MeasureShareDTO dto : dtoList) {
			dataList.add(dto);
		}
		// model.setData(dtoList); Directly setting dtoList causes the RPC
		// serialization exception(java.util.RandomaccessSubList) since we are
		// sublisting it.
		model.setData(dataList);
		model.setStartIndex(startIndex);
		model.setMeasureId(measureId);
		model.setPrivate(getService().getById(measureId).getIsPrivate());
		return model;
	}
	
	/**
	 * Increment version number and save.
	 * 
	 * @param maximumVersionNumber
	 *            - {@link String}.
	 * @param incrementBy
	 *            - {@link String}.
	 * @param mDetail
	 *            - {@link ManageMeasureDetailModel}.
	 * @param meas
	 *            - {@link Measure}.
	 * @return {@link SaveMeasureResult}. *
	 */
	private SaveMeasureResult incrementVersionNumberAndSave(final String maximumVersionNumber, final String incrementBy,
			final ManageMeasureDetailModel mDetail, final Measure meas) {
		BigDecimal mVersion = new BigDecimal(maximumVersionNumber);
		mVersion = mVersion.add(new BigDecimal(incrementBy));
		mDetail.setVersionNumber(mVersion.toString());
		Date currentDate = new Date();
		mDetail.setFinalizedDate(DateUtility.convertDateToString(currentDate));
		mDetail.setDraft(false);
		setValueFromModel(mDetail, meas);
		getService().save(meas);
		
		if(MATPropertiesService.get().getCurrentReleaseVersion().equals(meas.getReleaseVersion())) {
			MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(meas.getId());
			exportCQLibraryFromMeasure(meas, mDetail, xmlModel);
		}
				
		SaveMeasureResult result = new SaveMeasureResult();
		result.setSuccess(true);
		result.setId(meas.getId());
		String versionStr = meas.getMajorVersionStr() + "." + meas.getMinorVersionStr();
		result.setVersionStr(versionStr);
		logger.info("Result passed for Version Number " + versionStr);
		return result;
	}
	
	// TODO refactor this logic into a shared location: see MeasureDAO.
	/**
	 * Checks if Measure is locked.
	 * 
	 * @param m
	 *            the Measure
	 * @return true, if is locked
	 */
	private boolean isLocked(final Measure m) {
		if (m.getLockedOutDate() == null) {
			return false;
		}
		long lockTime = m.getLockedOutDate().getTime();
		long currentTime = System.currentTimeMillis();
		long threshold = 3 * 60 * 1000;
		boolean isLockExpired = (currentTime - lockTime) > threshold;
		return !isLockExpired;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#isMeasureLocked(java.lang.String)
	 */
	@Override
	public final boolean isMeasureLocked(final String id) {
		MeasurePackageService service = getService();
		boolean isLocked = service.isMeasureLocked(id);
		return isLocked;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#recordRecentMeasureActivity(java.lang.String, java.lang.String)
	 */
	@Override
	public void recordRecentMeasureActivity(String measureId, String userId) {
		recentMSRActivityLogDAO.recordRecentMeasureActivity(measureId, userId);
	}
	
	/**
	 * Removes the pattern from xml string.
	 * 
	 * @param xmlString
	 *            the xml string
	 * @param patternStart
	 *            the pattern start
	 * @param replaceWith
	 *            the replace with
	 * @return the string
	 */
	private String removePatternFromXMLString(final String xmlString, final String patternStart, final String replaceWith) {
		String newString = xmlString;
		if (patternStart != null) {
			newString = newString.replaceAll(patternStart, replaceWith);
		}
		return newString;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.service.MeasureService#resetLockedDate(java.lang.String
	 * , java.lang.String) This method has been added to release the Measure
	 * lock. It gets the existingMeasureLock and checks the loggedinUserId and
	 * the LockedUserid to release the lock.
	 */
	@Override
	public final SaveMeasureResult resetLockedDate(final String measureId, final String userId) {
		Measure existingMeasure = null;
		User lockedUser = null;
		SaveMeasureResult result = new SaveMeasureResult();
		if ((measureId != null) && (userId != null) && StringUtils.isNotBlank(measureId)) {
			existingMeasure = getService().getById(measureId);
			if (existingMeasure != null) {
				lockedUser = getLockedUser(existingMeasure);
				if ((lockedUser != null) && lockedUser.getId().equalsIgnoreCase(userId)) {
					// Only if the lockedUser and loggedIn User are same we can
					// allow the user to unlock the measure.
					if (existingMeasure.getLockedOutDate() != null) {
						// if it is not null then set it to null and save it.
						existingMeasure.setLockedOutDate(null);
						existingMeasure.setLockedUser(null);
						getService().updateLockedOutDate(existingMeasure);
						result.setSuccess(true);
					}
				}
			}
			result.setId(existingMeasure.getId());
		}
		
		return result;
	}
	
	/**
	 * Return failure reason.
	 * 
	 * @param rs
	 *            - {@link SaveMeasureResult}.
	 * @param failureReason
	 *            - {@link Integer}.
	 * @return {@link SaveMeasureResult}. *
	 */
	private SaveMeasureResult returnFailureReason(final SaveMeasureResult rs, final int failureReason) {
		rs.setFailureReason(failureReason);
		rs.setSuccess(false);
		return rs;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveMeasureAtPackage(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public SaveMeasureResult saveMeasureAtPackage(ManageMeasureDetailModel model) {
		Measure measure = getService().getById(model.getId());
		Integer revisionNumber = new Integer(000);
		if ((measure.getRevisionNumber() != null) && StringUtils.isNotEmpty(measure.getRevisionNumber())) {
			revisionNumber = Integer.parseInt(measure.getRevisionNumber());
			revisionNumber = revisionNumber + 1;
			measure.setRevisionNumber(String.format("%03d", revisionNumber));
			model.setRevisionNumber(String.format("%03d", revisionNumber));
		} else {
			revisionNumber = revisionNumber + 1;
			measure.setRevisionNumber(String.format("%03d", revisionNumber));
			model.setRevisionNumber(String.format("%03d", revisionNumber));
		}
		getService().save(measure);
		SaveMeasureResult result = save(model);
		return result;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#save(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public final SaveMeasureResult save(ManageMeasureDetailModel model) {
		// Scrubing out Mark Up.
		if(model != null){
			model.scrubForMarkUp();
		}
		ManageMeasureModelValidator manageMeasureModelValidator = new ManageMeasureModelValidator();
		List<String> message = manageMeasureModelValidator.isValidMeasure(model);
		if(message.size() ==0) {
			Measure pkg = null;
			MeasureSet measureSet = null;
			if (model.getId() != null) {
				setMeasureCreated(true);
				// editing an existing measure
				pkg = getService().getById(model.getId());
				model.setVersionNumber(pkg.getVersion());
				if (pkg.isDraft()) {
					model.setRevisionNumber(pkg.getRevisionNumber());
				} else {
					model.setRevisionNumber("000");
				}
				if (pkg.getMeasureSet().getId() != null) {
					measureSet = getService().findMeasureSet(pkg.getMeasureSet().getId());
				}
				if (!pkg.getMeasureScoring().equalsIgnoreCase(model.getMeasScoring())) {
					// US 194 User is changing the measure scoring. Make sure to
					// delete any groupings for that measure and save.
					getMeasurePackageService().deleteExistingPackages(pkg.getId());
				}
				
				//updateComponentMeasures(model);
				
			} else {
				// creating a new measure.
				setMeasureCreated(false);
				pkg = new Measure();
				/*model.setMeasureStatus("In Progress");*/
				pkg.setReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
				model.setRevisionNumber("000");
				measureSet = new MeasureSet();
				measureSet.setId(UUID.randomUUID().toString());				
				getService().save(measureSet);
			}
			pkg.setMeasureSet(measureSet);
			setValueFromModel(model, pkg);
			SaveMeasureResult result = new SaveMeasureResult();
			try {
				getAndValidateValueSetDate(model.getValueSetDate());
				pkg.setValueSetDate(DateUtility.addTimeToDate(pkg.getValueSetDate()));
				getService().save(pkg);
			} catch (InvalidValueSetDateException e) {
				result.setSuccess(false);
				result.setFailureReason(SaveMeasureResult.INVALID_VALUE_SET_DATE);
				result.setId(pkg.getId());
				return result;
			}
			result.setSuccess(true);
			result.setId(pkg.getId());
			saveMeasureXml(createMeasureXmlModel(model, pkg, MEASURE_DETAILS, MEASURE));
			return result;
		} else {
			logger.info("Validation Failed for measure :: Invalid Data Issues.");
			SaveMeasureResult result = new SaveMeasureResult();
			result.setSuccess(false);
			result.setFailureReason(SaveMeasureResult.INVALID_DATA);
			return result;
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveAndDeleteMeasure(java.lang.String)
	 */
	@Override
	public final void saveAndDeleteMeasure(final String measureID,  String loginUserId) {
		logger.info("MeasureLibraryServiceImpl: saveAndDeleteMeasure start : measureId:: " + measureID);
		MeasureDAO measureDAO = getMeasureDAO();
		Measure m = measureDAO.find(measureID);
		SecurityContext sc = SecurityContextHolder.getContext();
		MatUserDetails details = (MatUserDetails)sc.getAuthentication().getDetails();
		if(m.getOwner().getId().equalsIgnoreCase(details.getId())){
			logger.info("Measure Deletion Started for measure Id :: " + measureID);
			try {
				measureDAO.delete(m);
				logger.info("Measure Deleted Successfully :: " + measureID);
			} catch (Exception e) {
				logger.info("Measure not deleted.Something went wrong for measure Id :: " + measureID);
			}
		}
		
		logger.info("MeasureLibraryServiceImpl: saveAndDeleteMeasure End : measureId:: " + measureID);
}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveFinalizedVersion(java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public final SaveMeasureResult saveFinalizedVersion(final String measureId, final boolean isMajor, final String version) {
		

		logger.info("In MeasureLibraryServiceImpl.saveFinalizedVersion() method..");
		Measure m = getService().getById(measureId);
		logger.info("Measure Loaded for: " + measureId);
				
		boolean isMeasureVersionable = MatContextServiceUtil.get().isCurrentMeasureEditable(getMeasureDAO(),measureId);
		if(!isMeasureVersionable){
			SaveMeasureResult saveMeasureResult = new SaveMeasureResult();
			return returnFailureReason(saveMeasureResult, SaveMeasureResult.INVALID_DATA);
		}
						
		String versionNumber = null;
		if (isMajor) {
			versionNumber = findOutMaximumVersionNumber(m.getMeasureSet().getId());
			// For new measure's only draft entry will be
			// available.findOutMaximumVersionNumber will return null.
			if (versionNumber == null) {
				versionNumber = "0.000";
			}
			logger.info("Max Version Number loaded from DB: " + versionNumber);
		} else {
			int versionIndex = version.indexOf('v');
			logger.info("Min Version number passed from Page Model: " + versionIndex);
			String selectedVersion = version.substring(versionIndex + 1);
			logger.info("Min Version number after trim: " + selectedVersion);
			versionNumber = findOutVersionNumber(m.getMeasureSet().getId(), selectedVersion);
			
		}
		ManageMeasureDetailModel mDetail = getMeasure(measureId);
		// Need to check for logic when to mark a measure as completed.
		//mDetail.setMeasureStatus("Complete");
		SaveMeasureResult rs = new SaveMeasureResult();
		int endIndex = versionNumber.indexOf('.');
		String majorVersionNumber = versionNumber.substring(0, endIndex);
		if (!versionNumber.equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_VERSION)) {
			String[] versionArr = versionNumber.split("\\.");
			if (isMajor) {
				if (!versionArr[0].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MAJOR_VERSION)) {
					return incrementVersionNumberAndSave(majorVersionNumber, "1", mDetail, m);
				} else {
					return returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_MAJOR_VERSION);
				}
				
			} else {
				if (!versionArr[1].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MINOR_VERSION)) {
					versionNumber = versionArr[0]+"."+versionArr[1];
					return incrementVersionNumberAndSave(versionNumber, "0.001", mDetail, m);
				} else {
					return returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_MINOR_VERSION);
				}
			}
			
			
		} else {
			return returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_VERSION);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void exportCQLibraryFromMeasure(Measure measure, ManageMeasureDetailModel mDetail, MeasureXmlModel xmlModel) {
				
		// get simple xml for cql
		String cqlLibraryName = "";
		byte[] cqlByteArray = null; 
		if(!xmlModel.getXml().isEmpty()) {
			String xPathForCQLLookup = "/measure/cqlLookUp";
			String xPathForCQLLibraryName = "/measure/cqlLookUp/library";
			
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			Document document = xmlProcessor.getOriginalDoc();
			Node cqlLookUpNode = null;
			try {
				cqlLookUpNode = xmlProcessor.findNode(document, xPathForCQLLookup);
				Node cqlLibraryNode = xmlProcessor.findNode(document, xPathForCQLLibraryName);
				cqlLibraryName = cqlLibraryNode.getTextContent(); 
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
			if(cqlLookUpNode != null){
				String cqlXML = xmlProcessor.transform(cqlLookUpNode, true);
				cqlByteArray = cqlXML.getBytes();
			}
		}		
		
		String measureId = mDetail.getId();
		User owner = measure.getOwner(); 
		MeasureSet measureSet = measure.getMeasureSet();
		String version = mDetail.getVersionNumber();
		String releaseVersion = measure.getReleaseVersion();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:ss");
		Date date = null;
		try {
			date = dateFormat.parse(mDetail.getFinalizedDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time = date.getTime();
		Timestamp timestamp = new Timestamp(time);
		
		this.cqlLibraryService.save(cqlLibraryName, measureId, owner, measureSet, version, releaseVersion, timestamp, cqlByteArray);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveMeasureDetails(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public final SaveMeasureResult saveMeasureDetails(final ManageMeasureDetailModel model) {
		logger.info("In MeasureLibraryServiceImpl.saveMeasureDetails() method..");
		Measure measure = null;
		model.scrubForMarkUp();
		ManageMeasureModelValidator manageMeasureModelValidator = new ManageMeasureModelValidator();
		List<String> message = manageMeasureModelValidator.isValidMeasure(model);
		if(message.size() ==0) {
			if (model.getId() != null) {
				setMeasureCreated(true);
				measure = getService().getById(model.getId());
				/*if ((measure.getMeasureStatus() != null) && !measure.getMeasureStatus().
					equalsIgnoreCase(model.getMeasureStatus())) {
				measure.setMeasureStatus(model.getMeasureStatus());*/
				getService().save(measure);
				//}
			}
			model.setRevisionNumber(measure.getRevisionNumber());
			logger.info("Saving Measure_Xml");
			saveMeasureXml(createMeasureXmlModel(model, measure, MEASURE_DETAILS, MEASURE));
			SaveMeasureResult result = new SaveMeasureResult();
			result.setSuccess(true);
			logger.info("Saving of Measure Details Success");
			return result;
		} else {
			SaveMeasureResult result = new SaveMeasureResult();
			result.setSuccess(false);
			logger.info("Saving of Measure Details Failed. Invalid Data issue.");
			return result;
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveMeasureNote(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public final SaveMeasureNotesResult saveMeasureNote(MeasureNoteDTO model,
			final String measureId, final String userId) {
		model.scrubForMarkUp();
		ManageMeasureNotesModelValidator validator = new ManageMeasureNotesModelValidator();
		List<String> message = validator.validation(model);
		SaveMeasureNotesResult result = new SaveMeasureNotesResult();
		if(MatContextServiceUtil.get().isCurrentMeasureEditable(getMeasureDAO(),measureId)){
			if (message.size() == 0) {
				try {
					MeasureNotes measureNote = new MeasureNotes();
					measureNote.setNoteTitle(model.getNoteTitle());
					measureNote.setNoteDesc(model.getNoteDesc());
					Measure measure = getMeasureDAO().find(measureId);
					if (measure != null) {
						measureNote.setMeasure_id(measureId);
					}
					User user = getUserService().getById(userId);
					if (user != null) {
						measureNote.setCreateUser(user);
					}
					measureNote.setLastModifiedDate(new Date());
					getMeasureNotesService().saveMeasureNote(measureNote);
					logger.info("MeasureNotes Saved Successfully.");
					result.setSuccess(true);
				} catch (Exception e) {
					result.setSuccess(false);
					logger.info("Failed to save MeasureNotes. Exception occured.");
				}
			} else {
				result.setSuccess(false);
				result.setFailureReason(SaveMeasureNotesResult.INVALID_DATA);
			}
		}
		else{
			result.setSuccess(false);
			
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveMeasureXml(mat.client.clause.clauseworkspace.model.MeasureXmlModel)
	 */
	@Override
	public final void saveMeasureXml(final MeasureXmlModel measureXmlModel) {

		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			if(MatContextServiceUtil.get().isCurrentMeasureEditable(getMeasureDAO(),measureXmlModel.getMeasureId())){
				XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
				try{
					String scoringTypeBeforeNewXml = (String) xPath.evaluate(
							"/measure/measureDetails/scoring/@id",
							xmlProcessor.getOriginalDoc().getDocumentElement(), XPathConstants.STRING);
					String newXml = xmlProcessor.replaceNode(measureXmlModel.getXml(), measureXmlModel.getToReplaceNode(),
							measureXmlModel.getParentNode());
					String scoringTypeAfterNewXml = (String) xPath.evaluate(
							"/measure/measureDetails/scoring/@id",
							xmlProcessor.getOriginalDoc().getDocumentElement(), XPathConstants.STRING);
					xmlProcessor.checkForScoringType(MATPropertiesService.get().getQmdVersion());
					xmlProcessor.updateCQLLibraryName();
					//checkForTimingElementsAndAppend(xmlProcessor);
					checkForDefaultCQLParametersAndAppend(xmlProcessor);
					checkForDefaultCQLDefinitionsAndAppend(xmlProcessor);
					checkForDefaultCQLCodeSystemsAndAppend(xmlProcessor);
					checkForDefaultCQLCodesAndAppend(xmlProcessor);
					updateCQLVersion(xmlProcessor);
					if(! scoringTypeBeforeNewXml.equalsIgnoreCase(scoringTypeAfterNewXml)) {
						deleteExistingGroupings(xmlProcessor);
					}
					newXml = xmlProcessor.transform(xmlProcessor.getOriginalDoc());
					measureXmlModel.setXml(newXml);
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
				getService().saveMeasureXml(measureXmlModel);
			}
		} else {
			XmlProcessor processor = new XmlProcessor(measureXmlModel.getXml());
			processor.addParentNode(MEASURE);
			processor.checkForScoringType(MATPropertiesService.get().getQmdVersion());
			//checkForTimingElementsAndAppend(processor);
			checkForDefaultCQLParametersAndAppend(processor);
			checkForDefaultCQLDefinitionsAndAppend(processor);
			checkForDefaultCQLCodeSystemsAndAppend(processor);
			checkForDefaultCQLCodesAndAppend(processor);
			updateCQLVersion(processor);
			
			//Add QDM elements for Supplemental Definitions for Race, Payer, Sex, Ethnicity
			measureXmlModel.setXml(processor.transform(processor.getOriginalDoc()));
			CQLQualityDataModelWrapper wrapper = getMeasureXMLDAO().createSupplimentalQDM(
					measureXmlModel.getMeasureId(), false, null);
			
			ByteArrayOutputStream streamQDM = MeasureUtility.convertQualityDataDTOToXML(wrapper);
						
			String filteredString = removePatternFromXMLString(
					streamQDM.toString().substring(streamQDM.toString().indexOf("<measure>", 0)), "<measure>", "");
			filteredString = removePatternFromXMLString(filteredString, "</measure>", "");
			
			String result = callAppendNode(measureXmlModel, filteredString, "valueset", "/measure//valuesets");
			measureXmlModel.setXml(result);
			processor = new XmlProcessor(measureXmlModel.getXml());
			
		/*	String cqlFilteredString = filteredString.replaceAll("<qdm", "<valueset");
			try {
				processor.appendNode(cqlFilteredString, "valueset", "/measure/cqlLookUp/valuesets");
			} catch (SAXException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			
			Document measureXMLDocument = processor.getOriginalDoc();
			
			//find the "<supplementalDataElements>" tag.
			String supplementalDataElementsXPath = "/measure/supplementalDataElements";
			try {
				
				Node supplementalDataElementNode = processor.findNode(measureXMLDocument, supplementalDataElementsXPath);

				//create a tag "<supplementalDataElements>" under "<measure>" tag if "<supplementalDataElements>" is not present.
				if(supplementalDataElementNode == null){
					supplementalDataElementNode = measureXMLDocument.getFirstChild().appendChild(measureXMLDocument.createElement("supplementalDataElements"));
				}

				//append CQL definitions created for supplemental section to supplementalDataElement tag
				NodeList defaultCQLDefNodeList = findDefaultDefinitions(processor);

				//create "<cqldefinition>" tag with displayName and uuid pointing to the default CQL definitions and append it to "<supplementalDataElements>"
				for(int i=0;i<defaultCQLDefNodeList.getLength();i++){
					Node cqlDefNode = defaultCQLDefNodeList.item(i);
					Element cqlDefinitionRefNode = measureXMLDocument.createElement("cqldefinition");
					cqlDefinitionRefNode.setAttribute("displayName", cqlDefNode.getAttributes().getNamedItem("name").getNodeValue());
					cqlDefinitionRefNode.setAttribute("uuid", cqlDefNode.getAttributes().getNamedItem("id").getNodeValue());
					supplementalDataElementNode.appendChild(cqlDefinitionRefNode);
				}

				//processor.checkForStratificationAndAdd();
				measureXmlModel.setXml(processor.transform(processor.getOriginalDoc()));
				getService().saveMeasureXml(measureXmlModel);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * Update CQL version.
	 *
	 * @param processor the processor
	 */
	private void updateCQLVersion(XmlProcessor processor) {
		String cqlVersionXPath = "/measure/cqlLookUp/version";
		try {
			String version = (String) xPath.evaluate(
					"/measure/measureDetails/version/text()",
					processor.getOriginalDoc().getDocumentElement(), XPathConstants.STRING);
			Node node = (Node)xPath.evaluate(cqlVersionXPath, processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODE);
			if(node!=null){
				node.setTextContent(version);
			}
			
		} catch (XPathExpressionException e) {
			logger.error(e.getMessage());
		}
		
	}

	/**
	 * Deletes the existing groupings when scoring type selection is changed and saved.
	 *
	 * @param xmlProcessor the xml processor
	 */
	private void deleteExistingGroupings(XmlProcessor xmlProcessor) {
		NodeList measureGroupingList;
		try {
			measureGroupingList = (NodeList) xPath.evaluate("/measure/measureGrouping/group",
					xmlProcessor.getOriginalDoc().getDocumentElement(), XPathConstants.NODESET);
			for(int i = 0; i<measureGroupingList.getLength(); i++ ) {
				removeNode("/measure/measureGrouping/group",xmlProcessor.getOriginalDoc());
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#search(java.lang.String, int, int, int)
	 */
	@Override
	public final ManageMeasureSearchModel search(final String searchText,
			final int startIndex, final int pageSize, final int filter) {
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();
		
		if (SecurityRole.ADMIN_ROLE.equals(userRole)) {
			List<MeasureShareDTO> measureList = getService()
					.searchForAdminWithFilter(searchText, 1, Integer.MAX_VALUE,
							filter);
			List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<ManageMeasureSearchModel.Result>();
			List<MeasureShareDTO> measureTotalList = measureList;
			searchModel.setResultsTotal(measureTotalList.size());
			if (pageSize < measureTotalList.size()) {
				measureList = measureTotalList
						.subList(startIndex - 1, pageSize);
			} else if (pageSize > measureList.size()) {
				measureList = measureTotalList.subList(startIndex - 1,
						measureList.size());
			}
			searchModel.setStartIndex(startIndex);
			searchModel.setData(detailModelList);
			
			for (MeasureShareDTO dto : measureList) {
				ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
				detail.setName(dto.getMeasureName());
				detail.setId(dto.getMeasureId());
				detail.seteMeasureId(dto.geteMeasureId());
				detail.setDraft(dto.isDraft());
				String formattedVersion = MeasureUtility.getVersionText(
						dto.getVersion(), dto.isDraft());
				detail.setVersion(formattedVersion);
				detail.setFinalizedDate(dto.getFinalizedDate());
				detail.setStatus(dto.getStatus());
				User user = getUserService().getById(dto.getOwnerUserId());
				detail.setOwnerfirstName(user.getFirstName());
				detail.setOwnerLastName(user.getLastName());
				detail.setOwnerEmailAddress(user.getEmailAddress());
				detail.setMeasureSetId(dto.getMeasureSetId());
				detailModelList.add(detail);
			}
		} else {
			List<MeasureShareDTO> measureList = getService().searchWithFilter(
					searchText, 1, Integer.MAX_VALUE, filter);
			List<MeasureShareDTO> measureTotalList = measureList;
			
			searchModel.setResultsTotal(measureTotalList.size());
			if (pageSize <= measureTotalList.size()) {
				measureList = measureTotalList
						.subList(startIndex - 1, pageSize);
			} else if (pageSize > measureList.size()) {
				measureList = measureTotalList.subList(startIndex - 1,
						measureList.size());
			}
			searchModel.setStartIndex(startIndex);
			List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<ManageMeasureSearchModel.Result>();
			searchModel.setData(detailModelList);
			for (MeasureShareDTO dto : measureList) {
				ManageMeasureSearchModel.Result detail = extractMeasureSearchModelDetail(
						currentUserId, isSuperUser, dto);
				detailModelList.add(detail);
			}
			updateMeasureFamily(detailModelList);
		}
		
		return searchModel;
	}
	
	/**
	 * Update measure family.
	 *
	 * @param detailModelList the detail model list
	 */
	public void updateMeasureFamily(List<ManageMeasureSearchModel.Result> detailModelList){
		boolean isFamily=false;
		if((detailModelList!=null) & (detailModelList.size()>0)){
			for(int i=0;i<detailModelList.size();i++){
				if(i>0){
					if(detailModelList.get(i).getMeasureSetId().equalsIgnoreCase(
							detailModelList.get(i-1).getMeasureSetId())) {
						detailModelList.get(i).setMeasureFamily(!isFamily);
					} else {
						detailModelList.get(i).setMeasureFamily(isFamily);
					}
				}
				else{
					detailModelList.get(i).setMeasureFamily(isFamily);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#searchMeasuresForDraft(java.lang.String)
	 */
	@Override
	public final ManageMeasureSearchModel searchMeasuresForDraft(final String searchText) {
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();
		List<MeasureShareDTO> measureList = getService().searchMeasuresForDraft(searchText);
		searchModel.setResultsTotal((int) getService().countMeasuresForDraft(searchText));
		List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<ManageMeasureSearchModel.Result>();
		searchModel.setData(detailModelList);
		for (MeasureShareDTO dto : measureList) {
			setDTOtoModel(detailModelList, dto, currentUserId, isSuperUser);
		}
		return searchModel;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#searchMeasuresForVersion(java.lang.String)
	 */
	@Override
	public final ManageMeasureSearchModel searchMeasuresForVersion(final String searchText) {
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();
		List<MeasureShareDTO> measureList = getService().searchMeasuresForVersion(searchText);
		searchModel.setResultsTotal((int) getService().countMeasuresForVersion(searchText));
		List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<ManageMeasureSearchModel.Result>();
		searchModel.setData(detailModelList);
		
		for (MeasureShareDTO dto : measureList) {
			setDTOtoModel(detailModelList, dto, currentUserId, isSuperUser);
		}
		return searchModel;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#searchUsers(int, int)
	 */
	@Override
	public final TransferMeasureOwnerShipModel searchUsers(final String searchText, final int startIndex, final int pageSize) {
		UserService usersService = getUserService();
		List<User> searchResults;
		if(searchText.equals("")){
			searchResults = usersService.searchNonAdminUsers("", startIndex, pageSize);
		}
		else{
			searchResults = usersService.searchNonAdminUsers(searchText, startIndex, pageSize);
		}
		logger.info("User search returned " + searchResults.size());
		
		TransferMeasureOwnerShipModel result = new TransferMeasureOwnerShipModel();
		List<TransferMeasureOwnerShipModel.Result> detailList = new ArrayList<TransferMeasureOwnerShipModel.Result>();
		for (User user : searchResults) {
			TransferMeasureOwnerShipModel.Result r = new TransferMeasureOwnerShipModel.Result();
			r.setFirstName(user.getFirstName());
			r.setLastName(user.getLastName());
			r.setEmailId(user.getEmailAddress());
			r.setKey(user.getId());
			detailList.add(r);
		}
		result.setData(detailList);
		result.setStartIndex(startIndex);
		result.setResultsTotal(getUserService().countSearchResultsNonAdmin(""));
		
		return result;
		
	}
	
	/**
	 * Setting Additional Attributes for Measure Xml.
	 * 
	 * @param measureDetailModel
	 *            - {@link ManageMeasureDetailModel}.
	 * @param measure
	 *            - {@link Measure}.
	 */
	private void setAdditionalAttrsForMeasureXml(final ManageMeasureDetailModel measureDetailModel, final Measure measure) {
		logger.info("In MeasureLibraryServiceImpl.setAdditionalAttrsForMeasureXml()");
		measureDetailModel.setId(measure.getId());
		measureDetailModel.setMeasureSetId(measure.getMeasureSet() != null ? measure.getMeasureSet().getId() : null);
		measureDetailModel.setOrgVersionNumber(MeasureUtility.formatVersionText(
				measureDetailModel.getRevisionNumber(), String.valueOf(measure.getVersionNumber())));
		measureDetailModel.setVersionNumber(MeasureUtility.getVersionText(measureDetailModel.getOrgVersionNumber(),
				measureDetailModel.getRevisionNumber(), measure.isDraft()));
		measureDetailModel.setId(UuidUtility.idToUuid(measureDetailModel.getId())); // have
		// to
		// change
		// on
		// unmarshalling.
		/*if (StringUtils.isNotBlank(measureDetailModel.getMeasFromPeriod())
				|| StringUtils.isNotBlank(measureDetailModel.getMeasToPeriod())) {*/
		PeriodModel periodModel = new PeriodModel();
		periodModel.setUuid(UUID.randomUUID().toString());
		//for New measures checking Calender year to add Default Dates
		if(!isMeasureCreated()){
			measureDetailModel.setCalenderYear(true);
		}
		periodModel.setCalenderYear(measureDetailModel.isCalenderYear());
		if(!measureDetailModel.isCalenderYear()){
			periodModel.setStartDate(measureDetailModel.getMeasFromPeriod());
			periodModel.setStopDate(measureDetailModel.getMeasToPeriod());
		} else { // for Default Dates
			periodModel.setStartDate("01/01/20XX");
			periodModel.setStopDate("12/31/20XX");
		}
		//			if (StringUtils.isNotBlank(measureDetailModel.getMeasFromPeriod())) {
		//				periodModel.setStartDate(measureDetailModel.getMeasFromPeriod());
		//				//commented UUID as part of MAT-4613
		//				//periodModel.setStartDateUuid(UUID.randomUUID().toString());
		//			}
		//			if (StringUtils.isNotBlank(measureDetailModel.getMeasToPeriod())) {
		//				periodModel.setStopDate(measureDetailModel.getMeasToPeriod());
		//				//commented UUID as part of MAT-4613
		//				//periodModel.setStopDateUuid(UUID.randomUUID().toString());
		//			}
		measureDetailModel.setPeriodModel(periodModel);
		//}
		
		if (StringUtils.isNotBlank(measureDetailModel.getGroupName())) {
			measureDetailModel.setQltyMeasureSetUuid(UUID.randomUUID().toString());
		}
		//MAT-4898
		//setOrgIdInAuthor(measureDetailModel.getAuthorSelectedList());
		setMeasureTypeAbbreviation(measureDetailModel.getMeasureTypeSelectedList());
		measureDetailModel.setScoringAbbr(setScoringAbbreviation(measureDetailModel.getMeasScoring()));
		
		if ((measureDetailModel.getEndorseByNQF() != null) && measureDetailModel.getEndorseByNQF()) {
			measureDetailModel.setEndorsement("National Quality Forum");
			measureDetailModel.setEndorsementId("2.16.840.1.113883.3.560");
		} else {
			measureDetailModel.setEndorsement(null);
			measureDetailModel.setEndorsementId(null);
		}
		NqfModel nqfModel = new NqfModel();
		nqfModel.setExtension(measureDetailModel.getNqfId());
		nqfModel.setRoot("2.16.840.1.113883.3.560.1");
		measureDetailModel.setNqfModel(nqfModel);
		if (CollectionUtils.isEmpty(MeasureDetailsUtil.getTrimmedList(measureDetailModel.getReferencesList()))) {
			measureDetailModel.setReferencesList(null);
		}
		logger.info("Exiting MeasureLibraryServiceImpl.setAdditionalAttrsForMeasureXml()..");
	}
	
	
	/**
	 * Sets the context.
	 * 
	 * @param context
	 *            the new context
	 */
	public void setContext(ApplicationContext context) {
		this.context = context;
	}
	
	/**
	 * Sets the dt oto model.
	 * 
	 * @param detailModelList
	 *            - {@link Result}.
	 * @param dto
	 *            - {@link MeasureShareDTO}.
	 * @param currentUserId
	 *            - {@link String}.
	 * @param isSuperUser
	 *            - {@link Boolean}. *
	 */
	private void setDTOtoModel(final List<ManageMeasureSearchModel.Result> detailModelList, final MeasureShareDTO dto,
			final String currentUserId, final boolean isSuperUser) {
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
		detail.setName(dto.getMeasureName());
		detail.setShortName(dto.getShortName());
		detail.setStatus(dto.getStatus());
		detail.setId(dto.getMeasureId());
		detail.setStatus(dto.getStatus());
		detail.setClonable(isOwner || isSuperUser);
		detail.setEditable((isOwner || isSuperUser || ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) && dto.isDraft());
		detail.setMeasureLocked(dto.isLocked());
		detail.setExportable(dto.isPackaged());
		detail.setSharable(isOwner || isSuperUser);
		detail.setLockedUserInfo(dto.getLockedUserInfo());
		detail.setDraft(dto.isDraft());
		String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(dto.getVersion(), dto.getRevisionNumber(), dto.isDraft());
		detail.setVersion(formattedVersion);
		detail.setScoringType(dto.getScoringType());
		detail.setMeasureSetId(dto.getMeasureSetId());
		detailModelList.add(detail);
	}
	
	/**
	 * Sets the measure package service.
	 * 
	 * @param measurePackagerService
	 *            the new measure package service
	 */
	public final void setMeasurePackageService(final MeasurePackageService measurePackagerService) {
		measurePackageService = measurePackagerService;
	}
	
	/**
	 * Sets the measure type abbreviation.
	 * 
	 * @param measureTypeList
	 *            the new measure type abbreviation
	 */
	private void setMeasureTypeAbbreviation(final List<MeasureType> measureTypeList) {
		if (measureTypeList != null) {
			for (MeasureType measureType : measureTypeList) {
				measureType.setAbbrDesc(MeasureDetailsUtil.getMeasureTypeAbbr(measureType.getDescription()));
			}
		}
	}
	
	/**
	 * Sets the org id in author.
	 * 
	 * @param authors
	 *            the new org id in author
	 */
	private void setOrgIdInAuthor(final List<Author> authors) {
		if (CollectionUtils.isNotEmpty(authors)) {
			for (Author author : authors) {
				String oid = getService().retrieveStewardOID(author.getAuthorName().trim());
				author.setOrgId((oid != null) && !oid.equals("") ? oid : UUID.randomUUID().toString());
			}
		}
	}
	
	/**
	 * Sets the scoring abbreviation.
	 * 
	 * @param measScoring
	 *            the meas scoring
	 * @return the string
	 */
	private String setScoringAbbreviation(final String measScoring) {
		return MeasureDetailsUtil.getScoringAbbr(measScoring);
	}
	
	/**
	 * Sets the user service.
	 * 
	 * @param usersService
	 *            the new user service
	 */
	public final void setUserService(final UserService usersService) {
		userService = usersService;
	}
	
	/**
	 * Sets the value from model.
	 * 
	 * @param model
	 *            the model
	 * @param measure
	 *            the measure
	 */
	private void setValueFromModel(final ManageMeasureDetailModel model, final Measure measure) {
		measure.setDescription(model.getName());
		measure.setaBBRName(model.getShortName());
		// US 421. Scoring choice is not part of core measure.
		measure.setMeasureScoring(model.getMeasScoring());
		measure.setVersion(model.getVersionNumber());
		measure.setDraft(model.isDraft());
		measure.setRevisionNumber(model.getRevisionNumber());
		/*measure.setMeasureStatus(model.getMeasureStatus());*/
		measure.seteMeasureId(model.geteMeasureId());
		if ((model.getFinalizedDate() != null) && !model.getFinalizedDate().equals("")) {
			measure.setFinalizedDate(new Timestamp(DateUtility.convertStringToDate(model.getFinalizedDate()).getTime()));
		}
		if ((model.getValueSetDate() != null) && !model.getValueSetDate().equals("")) {
			measure.setValueSetDate(new Timestamp(DateUtility.convertStringToDate(model.getValueSetDate()).getTime()));
		}
	}
	
	/** This method updates MeasureXML - Attributes Nodes
	 * 
	 * *.
	 * 
	 * @param list the list
	 * @param toEmail the to email */
	
	/*
	 * private void updateAttributes(final XmlProcessor processor, final
	 * QualityDataSetDTO modifyWithDTO, final QualityDataSetDTO modifyDTO) {
	 * 
	 * logger.debug(" MeasureLibraryServiceImpl: updateAttributes Start :  ");
	 * 
	 * String XPATH_EXPRESSION_ATTRIBUTE =
	 * "/measure//clause//attribute[@qdmUUID='"
	 * +modifyDTO.getUuid()+"']";//XPath to find all elementRefs in
	 * supplementalDataElements for to be modified QDM.
	 * 
	 * try { NodeList nodesATTR = (NodeList)
	 * xPath.evaluate(XPATH_EXPRESSION_ATTRIBUTE, processor.getOriginalDoc(),
	 * XPathConstants.NODESET); for(int i=0 ;i<nodesATTR.getLength();i++){ Node
	 * newNode = nodesATTR.item(i); newNode
	 * .getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO
	 * .getDataType()); }
	 * 
	 * } catch (XPathExpressionException e) { e.printStackTrace(); }
	 * 
	 * logger.debug(" MeasureLibraryServiceImpl: updateAttributes End : "); }
	 */
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#transferOwnerShipToUser(java.util.List, java.lang.String)
	 */
	@Override
	public final void transferOwnerShipToUser(final List<String> list, final String toEmail) {
		MeasurePackageService service = getService();
		service.transferMeasureOwnerShipToUser(list, toEmail);
	}
	
	/**
	 * This method updates MeasureXML - QDM nodes under ElementLookUp.
	 * 
	 * *
	 * 
	 * @param processor
	 *            the processor
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param modifyDTO
	 *            the modify dto
	 */
	private void updateElementLookUp(final XmlProcessor processor, final QualityDataSetDTO modifyWithDTO,
			final QualityDataSetDTO modifyDTO) {
		
		logger.debug(" MeasureLibraryServiceImpl: updateElementLookUp Start :  ");
		// XPath Expression to find all elementRefs in elementLookUp for to be modified QDM.
		String XPATH_EXPRESSION_ELEMENTLOOKUP = "/measure/elementLookUp/qdm[@uuid='"
				+ modifyDTO.getUuid() + "']";
		try {
			NodeList nodesElementLookUp = (NodeList) xPath.evaluate(XPATH_EXPRESSION_ELEMENTLOOKUP, processor.getOriginalDoc(),
					XPathConstants.NODESET);
			if(nodesElementLookUp.getLength()>1){
				Node parentNode = nodesElementLookUp.item(0).getParentNode();
				if (parentNode.getAttributes().getNamedItem("vsacExpIdentifier") != null) {
					if (!StringUtils.isBlank(modifyWithDTO.getVsacExpIdentifier())) {
						parentNode.getAttributes().getNamedItem("vsacExpIdentifier").setNodeValue(
								modifyWithDTO.getExpansionIdentifier());
					} else {
						parentNode.getAttributes().removeNamedItem("vsacExpIdentifier");
					}
				} else {
					if (!StringUtils.isEmpty(modifyWithDTO.getExpansionIdentifier())) {
						Attr vsacExpIdentifierAttr = processor.getOriginalDoc().createAttribute("vsacExpIdentifier");
						vsacExpIdentifierAttr.setNodeValue(modifyWithDTO.getVsacExpIdentifier());
						parentNode.getAttributes().setNamedItem(vsacExpIdentifierAttr);
					}
				}
			}
			for (int i = 0; i < nodesElementLookUp.getLength(); i++) {
				Node newNode = nodesElementLookUp.item(i);
				newNode.getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO.getCodeListName());
				newNode.getAttributes().getNamedItem("id").setNodeValue(modifyWithDTO.getId());
				if ((newNode.getAttributes().getNamedItem("codeSystemName") == null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					Attr attrNode = processor.getOriginalDoc().createAttribute("codeSystemName");
					attrNode.setNodeValue(modifyWithDTO.getCodeSystemName());
					newNode.getAttributes().setNamedItem(attrNode);
				} else if ((newNode.getAttributes().getNamedItem("codeSystemName") != null)
						&& (modifyWithDTO.getCodeSystemName() == null)) {
					newNode.getAttributes().getNamedItem("codeSystemName").setNodeValue(null);
				} else if ((newNode.getAttributes().getNamedItem("codeSystemName") != null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					newNode.getAttributes().getNamedItem("codeSystemName").setNodeValue(
							modifyWithDTO.getCodeSystemName());
				}
				newNode.getAttributes().getNamedItem("datatype").setNodeValue(modifyWithDTO.getDataType());
				newNode.getAttributes().getNamedItem("oid").setNodeValue(modifyWithDTO.getOid());
				newNode.getAttributes().getNamedItem("taxonomy").setNodeValue(modifyWithDTO.getTaxonomy());
				newNode.getAttributes().getNamedItem("version").setNodeValue(modifyWithDTO.getVersion());
				if (modifyWithDTO.isSuppDataElement()) {
					newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("true");
				} else {
					newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("false");
				}
				if (newNode.getAttributes().getNamedItem("instance") != null) {
					if (!StringUtils.isBlank(modifyWithDTO.getOccurrenceText())) {
						newNode.getAttributes().getNamedItem("instance").setNodeValue(
								modifyWithDTO.getOccurrenceText());
					} else {
						newNode.getAttributes().removeNamedItem("instance");
					}
				} else {
					if (!StringUtils.isEmpty(modifyWithDTO.getOccurrenceText())) {
						Attr instance = processor.getOriginalDoc().createAttribute("instance");
						instance.setNodeValue(modifyWithDTO.getOccurrenceText());
						newNode.getAttributes().setNamedItem(instance);
					}
				}
				if (newNode.getAttributes().getNamedItem("effectiveDate") != null) {
					if (!StringUtils.isBlank(modifyWithDTO.getEffectiveDate())) {
						newNode.getAttributes().getNamedItem("effectiveDate").setNodeValue(
								modifyWithDTO.getEffectiveDate());
					} else {
						newNode.getAttributes().removeNamedItem("effectiveDate");
					}
				} else {
					if (!StringUtils.isEmpty(modifyWithDTO.getEffectiveDate())) {
						Attr effectiveDateAttr = processor.getOriginalDoc().createAttribute("effectiveDate");
						effectiveDateAttr.setNodeValue(modifyWithDTO.getEffectiveDate());
						newNode.getAttributes().setNamedItem(effectiveDateAttr);
					}
				}
				
				if (newNode.getAttributes().getNamedItem("expansionIdentifier") != null) {
					if (!StringUtils.isBlank(modifyWithDTO.getExpansionIdentifier())) {
						newNode.getAttributes().getNamedItem("expansionIdentifier").setNodeValue(
								modifyWithDTO.getExpansionIdentifier());
					} else {
						newNode.getAttributes().removeNamedItem("expansionIdentifier");
					}
				} else {
					if (!StringUtils.isEmpty(modifyWithDTO.getExpansionIdentifier())) {
						Attr expansionIdentifierAttr = processor.getOriginalDoc().createAttribute("expansionIdentifier");
						expansionIdentifierAttr.setNodeValue(modifyWithDTO.getExpansionIdentifier());
						newNode.getAttributes().setNamedItem(expansionIdentifierAttr);
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		logger.debug(" MeasureLibraryServiceImpl: updateElementLookUp End :  ");
	}
	
	
	@Override
	public void updateValueSetsInCQLLookUp(final CQLQualityDataSetDTO modifyWithDTO,
			final CQLQualityDataSetDTO modifyDTO, final String measureId) {
		
		MeasureXmlModel model = getMeasureXmlForMeasure(measureId);
		
		logger.debug(" MeasureLibraryServiceImpl: updateValueSetsInCQLLookUp Start :  ");
		
		if (model != null) {
			XmlProcessor processor = new XmlProcessor(model.getXml());
			
			// XPath Expression to find all elementRefs in elementLookUp for to be modified QDM.
			String XPATH_EXPRESSION_VALUESETS = "/measure/cqlLookUp/valuesets/valueset[@uuid='"
					+ modifyDTO.getUuid() + "']";
			try {
				NodeList nodesValuesets = (NodeList) xPath.evaluate(XPATH_EXPRESSION_VALUESETS, processor.getOriginalDoc(),
						XPathConstants.NODESET);
				if(nodesValuesets.getLength()>1){
					Node parentNode = nodesValuesets.item(0).getParentNode();
					if (parentNode.getAttributes().getNamedItem("vsacExpIdentifier") != null) {
						if (!StringUtils.isBlank(modifyWithDTO.getVsacExpIdentifier())) {
							parentNode.getAttributes().getNamedItem("vsacExpIdentifier").setNodeValue(
									modifyWithDTO.getExpansionIdentifier());
						} else {
							parentNode.getAttributes().removeNamedItem("vsacExpIdentifier");
						}
					} else {
						if (!StringUtils.isEmpty(modifyWithDTO.getExpansionIdentifier())) {
							Attr vsacExpIdentifierAttr = processor.getOriginalDoc().createAttribute("vsacExpIdentifier");
							vsacExpIdentifierAttr.setNodeValue(modifyWithDTO.getVsacExpIdentifier());
							parentNode.getAttributes().setNamedItem(vsacExpIdentifierAttr);
						}
					}
				}
				for (int i = 0; i < nodesValuesets.getLength(); i++) {
					Node newNode = nodesValuesets.item(i);
					newNode.getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO.getCodeListName());
					newNode.getAttributes().getNamedItem("id").setNodeValue(modifyWithDTO.getId());
					if ((newNode.getAttributes().getNamedItem("codeSystemName") == null)
							&& (modifyWithDTO.getCodeSystemName() != null)) {
						Attr attrNode = processor.getOriginalDoc().createAttribute("codeSystemName");
						attrNode.setNodeValue(modifyWithDTO.getCodeSystemName());
						newNode.getAttributes().setNamedItem(attrNode);
					} else if ((newNode.getAttributes().getNamedItem("codeSystemName") != null)
							&& (modifyWithDTO.getCodeSystemName() == null)) {
						newNode.getAttributes().getNamedItem("codeSystemName").setNodeValue(null);
					} else if ((newNode.getAttributes().getNamedItem("codeSystemName") != null)
							&& (modifyWithDTO.getCodeSystemName() != null)) {
						newNode.getAttributes().getNamedItem("codeSystemName").setNodeValue(
								modifyWithDTO.getCodeSystemName());
					}
					newNode.getAttributes().getNamedItem("oid").setNodeValue(modifyWithDTO.getOid());
					newNode.getAttributes().getNamedItem("taxonomy").setNodeValue(modifyWithDTO.getTaxonomy());
					newNode.getAttributes().getNamedItem("version").setNodeValue(modifyWithDTO.getVersion());
					if (modifyWithDTO.isSuppDataElement()) {
						newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("true");
					} else {
						newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("false");
					}
					
					if (newNode.getAttributes().getNamedItem("expansionIdentifier") != null) {
						if (!StringUtils.isBlank(modifyWithDTO.getExpansionIdentifier())) {
							newNode.getAttributes().getNamedItem("expansionIdentifier").setNodeValue(
									modifyWithDTO.getExpansionIdentifier());
						} else {
							newNode.getAttributes().removeNamedItem("expansionIdentifier");
						}
					} else {
						if (!StringUtils.isEmpty(modifyWithDTO.getExpansionIdentifier())) {
							Attr expansionIdentifierAttr = processor.getOriginalDoc().createAttribute("expansionIdentifier");
							expansionIdentifierAttr.setNodeValue(modifyWithDTO.getExpansionIdentifier());
							newNode.getAttributes().setNamedItem(expansionIdentifierAttr);
						}
					}
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			model.setXml(processor.transform(processor.getOriginalDoc()));
			getService().saveMeasureXml(model);
		}
		logger.debug(" MeasureLibraryServiceImpl: updateValueSetsInCQLLookUp End :  ");
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.service.MeasureService#updateLockedDate
	 * (java.lang.String, java.lang.String).This method has been added to update
	 * the measureLock Date. This method first gets the exisitingMeasure and
	 * then adds the lockedOutDate if it is not there.
	 */
	@Override
	public final SaveMeasureResult updateLockedDate(final String measureId, final String userId) {
		Measure existingmeasure = null;
		User user = null;
		SaveMeasureResult result = new SaveMeasureResult();
		if ((measureId != null) && (userId != null)) {
			existingmeasure = getService().getById(measureId);
			if (existingmeasure != null) {
				if (!isLocked(existingmeasure)) {
					user = getUserService().getById(userId);
					existingmeasure.setLockedUser(user);
					existingmeasure.setLockedOutDate(new Timestamp(new Date().getTime()));
					getService().save(existingmeasure);
					result.setSuccess(true);
				}
			}
		}
		
		result.setId(existingmeasure.getId());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#updateMeasureNotes(mat.DTO.MeasureNoteDTO, java.lang.String)
	 */
	@Override
	public final void updateMeasureNotes(final MeasureNoteDTO measureNoteDTO, final String userId) {
		try {
			if(MatContextServiceUtil.get()
					.isCurrentMeasureEditable(getMeasureDAO(),measureNoteDTO.getMeasureId())){
				MeasureNotesDAO measureNotesDAO = getMeasureNotesDAO();
				MeasureNotes measureNotes = measureNotesDAO.find(measureNoteDTO.getId());
				measureNotes.setNoteTitle(measureNoteDTO.getNoteTitle());
				measureNotes.setNoteDesc(measureNoteDTO.getNoteDesc());
				User user = getUserService().getById(userId);
				if (user != null) {
					measureNotes.setModifyUser(user);
				}
				measureNotes.setLastModifiedDate(new Date());
				getMeasureNotesService().saveMeasureNote(measureNotes);
				logger.info("Edited MeasureNotes Saved Successfully. Measure notes Id :: " + measureNoteDTO.getId());
			}
			
		} catch (Exception e) {
			logger.info("Edited MeasureNotes not saved. Exception occured. Measure notes Id :: " + measureNoteDTO.getId());
		}
	}
	
	/**
	 * This method updates MeasureXML - ElementLookUpNode,ElementRef's under
	 * Population Node and Stratification Node, SupplementDataElements. It also
	 * removes attributes nodes if there is mismatch in data types of newly
	 * selected QDM and already applied QDM.
	 * 
	 * *
	 * 
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param modifyDTO
	 *            the modify dto
	 * @param measureId
	 *            the measure id
	 */
	@Override
	public final void updateMeasureXML(final QualityDataSetDTO modifyWithDTO,
			final QualityDataSetDTO modifyDTO, final String measureId) {
		logger.debug(" MeasureLibraryServiceImpl: updateMeasureXML Start : Measure Id :: " + measureId);
		MeasureXmlModel model = getMeasureXmlForMeasure(measureId);
		
		if (model != null) {
			XmlProcessor processor = new XmlProcessor(model.getXml());
			if (modifyDTO.isUsed()) {
				if (modifyDTO.getDataType().equalsIgnoreCase("Attribute")) {
					// update All Attributes.
					// updateAttributes(processor, modifyWithDTO, modifyDTO);
				} else {
					// Update all elementRef's in Populations and Stratification
					//updatePopulationAndStratification(processor, modifyWithDTO, modifyDTO);
				}
				
				//Update all elementRef's in SubTreeLookUp
				updateSubTreeLookUp(processor, modifyWithDTO, modifyDTO);
				
				// update elementLookUp Tag
				updateElementLookUp(processor, modifyWithDTO, modifyDTO);
				// update cqlLookUp Tag
				updateCQLLookUp(processor, modifyWithDTO, modifyDTO);
				updateSupplementalDataElement(processor, modifyWithDTO, modifyDTO);
				model.setXml(processor.transform(processor.getOriginalDoc()));
				getService().saveMeasureXml(model);
				
				
			} else {
				// update elementLookUp Tag
				updateElementLookUp(processor, modifyWithDTO, modifyDTO);
				// update cqlLookUp Tag
				updateCQLLookUp(processor, modifyWithDTO, modifyDTO);
				model.setXml(processor.transform(processor.getOriginalDoc()));
				getService().saveMeasureXml(model);
			}
			
		}
		logger.debug(" MeasureLibraryServiceImpl: updateMeasureXML End : Measure Id :: " + measureId);
	}
	
	/**
	 * This method updates MeasureXML - ValueSet nodes under CQLLookUp.
	 * 
	 * *
	 * 
	 * @param processor
	 *            the processor
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param modifyDTO
	 *            the modify dto
	 */
	private void updateCQLLookUp(XmlProcessor processor, QualityDataSetDTO modifyWithDTO, QualityDataSetDTO modifyDTO) {

		
		logger.debug(" MeasureLibraryServiceImpl: updateElementLookUp Start :  ");
		// XPath Expression to find all elementRefs in elementLookUp for to be modified QDM.
		String XPATH_EXPRESSION_CQLLOOKUP = "/measure/cqlLookUp/valuesets/valueset[@uuid='"
				+ modifyDTO.getUuid() + "']";
		try {
			NodeList nodesElementLookUp = (NodeList) xPath.evaluate(XPATH_EXPRESSION_CQLLOOKUP, processor.getOriginalDoc(),
					XPathConstants.NODESET);
			if(nodesElementLookUp.getLength()>1){
				Node parentNode = nodesElementLookUp.item(0).getParentNode();
				if (parentNode.getAttributes().getNamedItem("vsacExpIdentifier") != null) {
					if (!StringUtils.isBlank(modifyWithDTO.getVsacExpIdentifier())) {
						parentNode.getAttributes().getNamedItem("vsacExpIdentifier").setNodeValue(
								modifyWithDTO.getExpansionIdentifier());
					} else {
						parentNode.getAttributes().removeNamedItem("vsacExpIdentifier");
					}
				} else {
					if (!StringUtils.isEmpty(modifyWithDTO.getExpansionIdentifier())) {
						Attr vsacExpIdentifierAttr = processor.getOriginalDoc().createAttribute("vsacExpIdentifier");
						vsacExpIdentifierAttr.setNodeValue(modifyWithDTO.getVsacExpIdentifier());
						parentNode.getAttributes().setNamedItem(vsacExpIdentifierAttr);
					}
				}
			}
			for (int i = 0; i < nodesElementLookUp.getLength(); i++) {
				Node newNode = nodesElementLookUp.item(i);
				newNode.getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO.getCodeListName());
				newNode.getAttributes().getNamedItem("id").setNodeValue(modifyWithDTO.getId());
				if ((newNode.getAttributes().getNamedItem("codeSystemName") == null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					Attr attrNode = processor.getOriginalDoc().createAttribute("codeSystemName");
					attrNode.setNodeValue(modifyWithDTO.getCodeSystemName());
					newNode.getAttributes().setNamedItem(attrNode);
				} else if ((newNode.getAttributes().getNamedItem("codeSystemName") != null)
						&& (modifyWithDTO.getCodeSystemName() == null)) {
					newNode.getAttributes().getNamedItem("codeSystemName").setNodeValue(null);
				} else if ((newNode.getAttributes().getNamedItem("codeSystemName") != null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					newNode.getAttributes().getNamedItem("codeSystemName").setNodeValue(
							modifyWithDTO.getCodeSystemName());
				}
				newNode.getAttributes().getNamedItem("datatype").setNodeValue(modifyWithDTO.getDataType());
				newNode.getAttributes().getNamedItem("oid").setNodeValue(modifyWithDTO.getOid());
				newNode.getAttributes().getNamedItem("taxonomy").setNodeValue(modifyWithDTO.getTaxonomy());
				newNode.getAttributes().getNamedItem("version").setNodeValue(modifyWithDTO.getVersion());
				if (modifyWithDTO.isSuppDataElement()) {
					newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("true");
				} else {
					newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("false");
				}
				if (newNode.getAttributes().getNamedItem("instance") != null) {
					if (!StringUtils.isBlank(modifyWithDTO.getOccurrenceText())) {
						newNode.getAttributes().getNamedItem("instance").setNodeValue(
								modifyWithDTO.getOccurrenceText());
					} else {
						newNode.getAttributes().removeNamedItem("instance");
					}
				} else {
					if (!StringUtils.isEmpty(modifyWithDTO.getOccurrenceText())) {
						Attr instance = processor.getOriginalDoc().createAttribute("instance");
						instance.setNodeValue(modifyWithDTO.getOccurrenceText());
						newNode.getAttributes().setNamedItem(instance);
					}
				}
				if (newNode.getAttributes().getNamedItem("effectiveDate") != null) {
					if (!StringUtils.isBlank(modifyWithDTO.getEffectiveDate())) {
						newNode.getAttributes().getNamedItem("effectiveDate").setNodeValue(
								modifyWithDTO.getEffectiveDate());
					} else {
						newNode.getAttributes().removeNamedItem("effectiveDate");
					}
				} else {
					if (!StringUtils.isEmpty(modifyWithDTO.getEffectiveDate())) {
						Attr effectiveDateAttr = processor.getOriginalDoc().createAttribute("effectiveDate");
						effectiveDateAttr.setNodeValue(modifyWithDTO.getEffectiveDate());
						newNode.getAttributes().setNamedItem(effectiveDateAttr);
					}
				}
				
				if (newNode.getAttributes().getNamedItem("expansionIdentifier") != null) {
					if (!StringUtils.isBlank(modifyWithDTO.getExpansionIdentifier())) {
						newNode.getAttributes().getNamedItem("expansionIdentifier").setNodeValue(
								modifyWithDTO.getExpansionIdentifier());
					} else {
						newNode.getAttributes().removeNamedItem("expansionIdentifier");
					}
				} else {
					if (!StringUtils.isEmpty(modifyWithDTO.getExpansionIdentifier())) {
						Attr expansionIdentifierAttr = processor.getOriginalDoc().createAttribute("expansionIdentifier");
						expansionIdentifierAttr.setNodeValue(modifyWithDTO.getExpansionIdentifier());
						newNode.getAttributes().setNamedItem(expansionIdentifierAttr);
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		logger.debug(" MeasureLibraryServiceImpl: updateCQLLookUp End :  ");
	
		
	}

	/**
	 * Update measure xml for qdm.
	 *
	 * @param modifyWithDTO the modify with dto
	 * @param xmlprocessor the xmlprocessor
	 * @param expansionIdentifier the expansion identifier
	 */
	private void updateMeasureXmlForQDM(final QualityDataSetDTO modifyWithDTO,
			XmlProcessor xmlprocessor, String expansionIdentifier){
		//if (!modifyWithDTO.getDataType().equalsIgnoreCase("Attribute")) {
		String XPATH_EXPRESSION_ELEMENTLOOKUP = "/measure/elementLookUp/qdm[@uuid='"
				+ modifyWithDTO.getUuid() + "']";
		NodeList nodesElementLookUp;
		try {
			nodesElementLookUp = (NodeList) xPath.evaluate(XPATH_EXPRESSION_ELEMENTLOOKUP, xmlprocessor.getOriginalDoc(),
					XPathConstants.NODESET);
			
			for (int i = 0; i < nodesElementLookUp.getLength(); i++) {
				Node newNode = nodesElementLookUp.item(i);
				newNode.getAttributes().getNamedItem("version").setNodeValue("1.0");
				if (newNode.getAttributes().getNamedItem("expansionIdentifier") != null) {
					if (!StringUtils.isBlank(modifyWithDTO.getExpansionIdentifier())) {
						newNode.getAttributes().getNamedItem("expansionIdentifier").setNodeValue(
								expansionIdentifier);
					} else {
						newNode.getAttributes().removeNamedItem("expansionIdentifier");
					}
				} else {
					if (!StringUtils.isEmpty(expansionIdentifier)) {
						Attr expansionIdentifierAttr = xmlprocessor.getOriginalDoc().createAttribute("expansionIdentifier");
						expansionIdentifierAttr.setNodeValue(expansionIdentifier);
						newNode.getAttributes().setNamedItem(expansionIdentifierAttr);
					}
				}
			}
		}  catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		//}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#updateMeasureXMLForExpansionIdentifier(java.util.List, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateMeasureXMLForExpansionIdentifier(List<QualityDataSetDTO> modifyWithDTOList, String measureId, String expansionIdentifier) {
		logger.debug(" MeasureLibraryServiceImpl: updateMeasureXMLForExpansionIdentifier Start : Measure Id :: " + measureId);
		MeasureXmlModel model = getMeasureXmlForMeasure(measureId);
		if (model != null) {
			XmlProcessor processor = new XmlProcessor(model.getXml());
			String XPATH_EXP_FOR_ELEMENTLOOKUP_ATTR = "/measure/elementLookUp";
			try {
				Node nodesElementLookUp = (Node)xPath.evaluate(XPATH_EXP_FOR_ELEMENTLOOKUP_ATTR, processor.getOriginalDoc(),
						XPathConstants.NODE);
				if(nodesElementLookUp!=null){
					if (nodesElementLookUp.getAttributes().getNamedItem("vsacExpIdentifier") != null) {
						if (!StringUtils.isBlank(expansionIdentifier)) {
							nodesElementLookUp.getAttributes().getNamedItem("vsacExpIdentifier").setNodeValue(
									expansionIdentifier);
						} else {
							nodesElementLookUp.getAttributes().removeNamedItem("vsacExpIdentifier");
						}
					} else {
						if (!StringUtils.isEmpty(expansionIdentifier)) {
							Attr vsacExpIdentifierAttr = processor.getOriginalDoc().createAttribute("vsacExpIdentifier");
							vsacExpIdentifierAttr.setNodeValue(expansionIdentifier);
							nodesElementLookUp.getAttributes().setNamedItem(vsacExpIdentifierAttr);
						}
					}
				}
				for(QualityDataSetDTO dto : modifyWithDTOList){
					updateMeasureXmlForQDM(dto, processor, expansionIdentifier);
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
			
			model.setXml(processor.transform(processor.getOriginalDoc()));
			getService().saveMeasureXml(model);
		}
		
	}
	/**
	 * Update sub tree look up.
	 *
	 * @param processor the processor
	 * @param modifyWithDTO the modify with dto
	 * @param modifyDTO the modify dto
	 */
	private void updateSubTreeLookUp(final XmlProcessor processor, final QualityDataSetDTO modifyWithDTO,
			final QualityDataSetDTO modifyDTO) {
		
		logger.debug(" MeasureLibraryServiceImpl: updateSubTreeLookUp Start :  ");
		// XPath to find All elementRef's under subTreeLookUp element nodes for to be modified QDM.
		String XPATH_EXPRESSION_SubTreeLookUp_ELEMENTREF = "/measure//subTreeLookUp//elementRef[@id='"
				+ modifyDTO.getUuid() + "']";
		try {
			NodeList nodesClauseWorkSpace = (NodeList) xPath.evaluate(XPATH_EXPRESSION_SubTreeLookUp_ELEMENTREF,
					processor.getOriginalDoc(),	XPathConstants.NODESET);
			for (int i = 0; i < nodesClauseWorkSpace.getLength(); i++) {
				Node newNode = nodesClauseWorkSpace.item(i);
				String displayName = new String();
				if (!StringUtils.isBlank(modifyWithDTO.getOccurrenceText())) {
					displayName = displayName.concat(modifyWithDTO.getOccurrenceText() + " of ");
				}
				displayName = displayName.concat(modifyWithDTO.getCodeListName() + " : " + modifyWithDTO.getDataType());
				
				newNode.getAttributes().getNamedItem("displayName").setNodeValue(displayName);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			
		}
		logger.debug(" MeasureLibraryServiceImpl: updateSubTreeLookUp End :  ");
	}
	
		
	/**
	 * This method updates MeasureXML - ElementRef's under Population and
	 * Stratification Node
	 * 
	 * *.
	 * 
	 * @param processor
	 *            the processor
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param modifyDTO
	 *            the modify dto
	 */
	private void updatePopulationAndStratification(final XmlProcessor processor, final QualityDataSetDTO modifyWithDTO,
			final QualityDataSetDTO modifyDTO) {
		
		logger.debug(" MeasureLibraryServiceImpl: updatePopulationAndStratification Start :  ");
		// XPath to find All elementRef's under clause element nodes for to be modified QDM.
		String XPATH_EXPRESSION_CLAUSE_ELEMENTREF = "/measure//subTreeLookUp//elementRef[@id='"
				+ modifyDTO.getUuid() + "']";
		try {
			NodeList nodesClauseWorkSpace = (NodeList) xPath.evaluate(XPATH_EXPRESSION_CLAUSE_ELEMENTREF,
					processor.getOriginalDoc(),	XPathConstants.NODESET);
			ArrayList<QDSAttributes> attr = (ArrayList<QDSAttributes>) getAllDataTypeAttributes(modifyWithDTO.getDataType());
			for (int i = 0; i < nodesClauseWorkSpace.getLength(); i++) {
				Node newNode = nodesClauseWorkSpace.item(i);
				String displayName = new String();
				if (!StringUtils.isBlank(modifyWithDTO.getOccurrenceText())) {
					displayName = displayName.concat(modifyWithDTO.getOccurrenceText() + " of ");
				}
				displayName = displayName.concat(modifyWithDTO.getCodeListName() + " : " + modifyWithDTO.getDataType());
				
				newNode.getAttributes().getNamedItem("displayName").setNodeValue(displayName);
				if (newNode.getChildNodes() != null) {
					NodeList childList = newNode.getChildNodes();
					for (int j = 0; j < childList.getLength(); j++) {
						Node childNode = childList.item(j);
						if (childNode.getAttributes().getNamedItem("qdmUUID") != null) {
							String childNodeAttrName = childNode.getAttributes().getNamedItem("name").
									getNodeValue();
							boolean isRemovable = true;
							for (QDSAttributes attributes : attr) {
								if (attributes.getName().equalsIgnoreCase(childNodeAttrName)) {
									isRemovable = false;
									break;
								}
							}
							if (isRemovable) {
								Node parentNode = childNode.getParentNode();
								parentNode.removeChild(childNode);
							}
						}
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			
		}
		logger.debug(" MeasureLibraryServiceImpl: updatePopulationAndStratification End :  ");
	}
	
		
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#updatePrivateColumnInMeasure(java.lang.String, boolean)
	 */
	@Override
	public final void updatePrivateColumnInMeasure(final String measureId, final boolean isPrivate) {
		getService().updatePrivateColumnInMeasure(measureId, isPrivate);
	}
	
	/** This method updates MeasureXML - ElementRef's under SupplementalDataElement Node.
	 * 
	 * @param processor the processor
	 * @param modifyWithDTO QualityDataSetDTO
	 * @param modifyDTO QualityDataSetDTO */
	private void updateSupplementalDataElement(final XmlProcessor processor, final QualityDataSetDTO modifyWithDTO,
			final QualityDataSetDTO modifyDTO) {
		
		logger.debug(" MeasureLibraryServiceImpl: updateSupplementalDataElement Start :  ");
		// XPath to find All elementRef's in supplementalDataElements for to be modified QDM.
		String XPATH_EXPRESSION_SDE_ELEMENTREF = "/measure/supplementalDataElements/elementRef[@id='"
				+ modifyDTO.getUuid() + "']";
		try {
			NodeList nodesSDE = (NodeList) xPath.evaluate(XPATH_EXPRESSION_SDE_ELEMENTREF, processor.getOriginalDoc(),
					XPathConstants.NODESET);
			for (int i = 0; i < nodesSDE.getLength(); i++) {
				Node newNode = nodesSDE.item(i);
				newNode.getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO.getCodeListName());
			}
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		logger.debug(" MeasureLibraryServiceImpl: updateSupplementalDataElement End :  ");
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#updateUsersShare(mat.client.measure.ManageMeasureShareModel)
	 */
	@Override
	public final void updateUsersShare(final ManageMeasureShareModel model) {
		getService().updateUsersShare(model);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.server.service.MeasureLibraryService#validateMeasureForExport(java.lang.String, java.util.ArrayList)
	 */
	@Override
	public final ValidateMeasureResult validateMeasureForExport(final String key, final List<MatValueSet> matValueSetList)
			throws MatException {
		try {
			return getService().validateMeasureForExport(key, matValueSetList);
		} catch (Exception exc) {
			logger.info("Exception validating export for " + key, exc);
			throw new MatException(exc.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getFormattedReleaseDate(java.lang.String)
	 */
	@Override
	public Date getFormattedReleaseDate(String releaseDate){
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		Date date = new Date();
		try {
			date = formatter.parse(releaseDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getHumanReadableForNode(java.lang.String, java.lang.String)
	 */
	@Override
	public String getHumanReadableForNode(final String measureId, final String populationSubXML){
		String humanReadableHTML = "";
		try {
			humanReadableHTML = getService().getHumanReadableForNode(measureId, populationSubXML);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return humanReadableHTML;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getComponentMeasures(java.util.List)
	 */
	@Override
	public ManageMeasureSearchModel getComponentMeasures(List<String> measureIds) {
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();
		List<Measure> measureList = getService().getComponentMeasuresInfo(measureIds);
		List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<ManageMeasureSearchModel.Result>();
		searchModel.setData(detailModelList);
		for (Measure measure : measureList) {
			ManageMeasureSearchModel.Result detail = extractManageMeasureSearchModelDetail(measure);
			detailModelList.add(detail);
		}
		return searchModel;
	}
	
	/**
	 * Extract manage measure search model detail.
	 *
	 * @param measure the measure
	 * @return the manage measure search model. result
	 */
	private ManageMeasureSearchModel.Result extractManageMeasureSearchModelDetail(Measure measure){
		ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
		detail.setName(measure.getDescription());
		detail.setId(measure.getId());
		String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(measure.getVersion(), measure.getRevisionNumber(), measure.isDraft());
		detail.setVersion(formattedVersion);
		detail.setFinalizedDate(measure.getFinalizedDate());
		return detail;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#validatePackageGrouping(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public ValidateMeasureResult validatePackageGrouping(ManageMeasureDetailModel model) {
		ValidateMeasureResult result = new ValidateMeasureResult();
		logger.debug(" MeasureLibraryServiceImpl: validatePackageGrouping Start :  ");
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(model.getId());
		if (((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml()))) {
			/*System.out.println("MEASURE_XML: "+xmlModel.getXml());*/
			result = validateMeasureXmlAtCreateMeasurePackager(xmlModel);
		} else {
			List<String> message = new ArrayList<String>();
			message.add(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			result.setValid(false);
			result.setValidationMessages(message);
		}
		
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#validateMeasureXmlAtCreateMeasurePackager(mat.client.clause.clauseworkspace.model.MeasureXmlModel)
	 */
	@Override
	public ValidateMeasureResult validateMeasureXmlAtCreateMeasurePackager(MeasureXmlModel measureXmlModel) {
		boolean isInvalid = false;
		ValidateMeasureResult result = new ValidateMeasureResult();
		result.setValid(true);
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		List<String> message = new ArrayList<String>();
		message.add(MatContext.get().getMessageDelegate().getINVALID_LOGIC_MEASURE_PACKAGER());
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			
			//validate only from MeasureGrouping
			String XAPTH_MEASURE_GROUPING="/measure/measureGrouping/ group/packageClause" +
					"[not(@uuid = preceding:: group/packageClause/@uuid)]";
			
			List<String> measureGroupingIDList = new ArrayList<String>();;
			
			try {
				NodeList measureGroupingNodeList = (NodeList) xPath.evaluate(XAPTH_MEASURE_GROUPING, xmlProcessor.getOriginalDoc(),
						XPathConstants.NODESET);
				
				for(int i=0 ; i<measureGroupingNodeList.getLength();i++){
					Node childNode = measureGroupingNodeList.item(i);
					String uuid = childNode.getAttributes().getNamedItem("uuid").getNodeValue();
					String type = childNode.getAttributes().getNamedItem("type").getNodeValue();
					if(type.equals("stratification")){
						List<String> stratificationClausesIDlist = getStratificationClasuesIDList(uuid, xmlProcessor);
						measureGroupingIDList.addAll(stratificationClausesIDlist);
						
					} else {
						measureGroupingIDList.add(uuid);
					}
				}
			} catch (XPathExpressionException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			String uuidXPathString = "";
			for (String uuidString: measureGroupingIDList) {
				uuidXPathString += "@uuid = '" + uuidString + "' or";
			}
			
			uuidXPathString = uuidXPathString.substring(0, uuidXPathString.lastIndexOf(" or"));
			System.out.println("UUID: "+uuidXPathString);
			
			String XPATH_POPULATION = "/measure//clause["+uuidXPathString+"]";
			
			try {
				NodeList measureGroupingNodeList = (NodeList) xPath.evaluate(XPATH_POPULATION, xmlProcessor.getOriginalDoc(),
						XPathConstants.NODESET);
				for(int i=0; i<measureGroupingNodeList.getLength(); i++){
					Node clauseNode = measureGroupingNodeList.item(i);
					if(clauseNode.hasChildNodes()) {
						Node childNode = clauseNode.getFirstChild();
						if(!childNode.getNodeName().equalsIgnoreCase("cqldefinition") && 
								!childNode.getNodeName().equalsIgnoreCase("cqlaggfunction") && 
								!childNode.getNodeName().equalsIgnoreCase("cqlfunction")) {
							result.setValid(false);
							result.setValidationMessages(message);
							break;
						} 
						//aggregate function should have user define function as a child
						if(childNode.getNodeName().equalsIgnoreCase("cqlaggfunction")){
							if(!childNode.hasChildNodes()){
								result.setValid(false);
								result.setValidationMessages(message);
								break;
							}
						}
					} else {
						result.setValid(false);
						result.setValidationMessages(message);
						break;
					}
				}
				//Parse CQL Data
				if(result.isValid()) {
					isInvalid = parseCQLFile(measureXmlModel.getXml(),
					measureXmlModel.getMeasureId());
					if(isInvalid) {
						result.setValid(false);
						result.setValidationMessages(message);
						}
					}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	
	/**
	 * Parses the CQL file.
	 *
	 * @param measureXML the measure XML
	 * @param measureId the measure id
	 * @return true, if successful
	 */
	private boolean parseCQLFile(String measureXML, String measureId){
		boolean isInvalid = false;
		MeasureXML newXml = getMeasureXMLDAO().findForMeasure(measureId);
		String cqlFileString = CQLUtilityClass.getCqlString(CQLUtilityClass.getCQLStringFromMeasureXML(measureXML,measureId), "").toString();
		MATCQLParser matcqlParser = new MATCQLParser();
		CQLFileObject cqlFileObject = matcqlParser.parseCQL(cqlFileString);
		List<String> message= new ArrayList<String>();
		String exportedXML = ExportSimpleXML.export(newXml, message, measureDAO,organizationDAO, cqlFileObject);
		CQLModel cqlModel = new CQLModel();
		cqlModel = CQLUtilityClass.getCQLStringFromMeasureXML(exportedXML,measureId);
		StringBuilder cqlString = getCqlService().getCqlString(cqlModel);
		if(!StringUtils.isBlank(cqlString.toString())){
			CQLtoELM cqlToElm = new CQLtoELM(cqlString.toString()); 
			cqlToElm.doTranslation(true, false, false);
			
			String elmSting = cqlToElm.getElmString();
			if(cqlToElm.getErrors() != null && cqlToElm.getErrors().size() > 0) {
				isInvalid = true; 
			}
		}
		
		return isInvalid;
	}	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#validateMeasureXmlInpopulationWorkspace(mat.client.clause.clauseworkspace.model.MeasureXmlModel)
	 */
	/**
	 * Validate measure xml atby create measure packager.
	 *
	 * @param measureXmlModel the measure xml model
	 * @return the validate measure result
	 */
	//@Override
	public ValidateMeasureResult validateMeasureXmlAtbyCreateMeasurePackager(MeasureXmlModel measureXmlModel) {
		boolean isInvalid = false;
		ValidateMeasureResult result = new ValidateMeasureResult();
		result.setValid(true);
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		List<String> message = new ArrayList<String>();
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			
			//validate only from MeasureGrouping
			String XAPTH_MEASURE_GROUPING="/measure/measureGrouping/ group/packageClause" +
					"[not(@uuid = preceding:: group/packageClause/@uuid)]";
			
			List<String> measureGroupingIDList = new ArrayList<String>();;
			
			try {
				NodeList measureGroupingNodeList = (NodeList) xPath.evaluate(XAPTH_MEASURE_GROUPING, xmlProcessor.getOriginalDoc(),
						XPathConstants.NODESET);
				
				for(int i=0 ; i<measureGroupingNodeList.getLength();i++){
					Node childNode = measureGroupingNodeList.item(i);
					String uuid = childNode.getAttributes().getNamedItem("uuid").getNodeValue();
					String type = childNode.getAttributes().getNamedItem("type").getNodeValue();
					if(type.equals("stratification")){
						List<String> stratificationClausesIDlist = getStratificationClasuesIDList(uuid, xmlProcessor);
						measureGroupingIDList.addAll(stratificationClausesIDlist);
						
					} else {
						measureGroupingIDList.add(uuid);
					}
				}
			} catch (XPathExpressionException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			String uuidXPathString = "";
			for (String uuidString: measureGroupingIDList) {
				uuidXPathString += "@uuid = '" + uuidString + "' or";
			}
			
			uuidXPathString = uuidXPathString.substring(0, uuidXPathString.lastIndexOf(" or"));
			
			String XPATH_POPULATION_TOP_LEVEL_LOGICAL_OP = "/measure//clause["+uuidXPathString+"]/logicalOp";
			
			String XPATH_POPULATION_LOGICALOP = "/measure//clause["+uuidXPathString+"]//logicalOp";
			
			String XPATH_POPULATION_QDMELEMENT = "/measure//clause["+uuidXPathString+"]//elementRef";
			
			String XPATH_POPULATION_TIMING_ELEMENT = "/measure//clause["+uuidXPathString+"]//relationalOp";
			
			String XPATH_POPULATION_FUNCTIONS ="/measure//clause["+uuidXPathString+"]//functionalOp";
			
			
			
			//get the Population Worspace Logic that are Used in Measure Grouping
			
			try {
				//list of LogicalOpNode inSide the PopulationWorkspace That are used in Grouping
				NodeList populationTopLevelLogicalOp = (NodeList) xPath.evaluate(XPATH_POPULATION_TOP_LEVEL_LOGICAL_OP, xmlProcessor.getOriginalDoc(),
						XPathConstants.NODESET);
				NodeList populationLogicalOp = (NodeList) xPath.evaluate(XPATH_POPULATION_LOGICALOP, xmlProcessor.getOriginalDoc(),
						XPathConstants.NODESET);
				//list of Qdemelement inSide the PopulationWorkspace That are used in Grouping
				NodeList populationQdemElement = (NodeList) xPath.evaluate(XPATH_POPULATION_QDMELEMENT, xmlProcessor.getOriginalDoc(),
						XPathConstants.NODESET);
				//list of TimingElement inSide the PopulationWorkspace That are used in Grouping
				NodeList populationTimingElement = (NodeList) xPath.evaluate(XPATH_POPULATION_TIMING_ELEMENT, xmlProcessor.getOriginalDoc(),
						XPathConstants.NODESET);
				//list of functionNode inSide the PopulationWorkspace That are used in Grouping
				NodeList populationFunctions = (NodeList) xPath.evaluate(XPATH_POPULATION_FUNCTIONS, xmlProcessor.getOriginalDoc(),
						XPathConstants.NODESET);
				// Validation for Logical Operators.
				String messageForLogicalOp = validateLogicalOpsInAllPopulations(populationTopLevelLogicalOp);
				if (messageForLogicalOp != null) {
					message.add(messageForLogicalOp);
					result.setValid(false);
					result.setValidationMessages(message);
				}
				// Validation for Stratums with No clause.
				String msg = validateStratumForAtleastOneClause(xmlModel);
				if (msg != null) {
					message.add(msg);
					result.setValid(false);
					result.setValidationMessages(message);
				}
				if ((result.getValidationMessages() != null)
						&& (result.getValidationMessages().size() > 0)) {
					return result;
				}
				// Other Validations.
				if(populationLogicalOp.getLength()>0){
					for (int i = 0; (i <populationLogicalOp.getLength()); i++) {
						Node childNode =populationLogicalOp.item(i);
						String type = childNode.getParentNode().getAttributes().getNamedItem("type").getNodeValue();
						if(type.equals("measureObservation")){
							result.setValid(false);
							message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
							result.setValidationMessages(message);
							return result;
						}
					}
				}
				
				if((populationQdemElement.getLength()>0)){
					result.setValid(false);
					message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
					result.setValidationMessages(message);
					return result;
				}
				
				if((populationTimingElement.getLength()>0)){
					result.setValid(false);
					message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
					result.setValidationMessages(message);
					return result;
				}
				if((populationFunctions.getLength()>0)){
					result.setValid(false);
					message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
					result.setValidationMessages(message);
					return result;
				}
				
				
			} catch (XPathExpressionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//start clause validation
			Map<String , List<String>> usedSubtreeRefIdsMap = getUsedSubtreeRefIds(xmlProcessor,measureGroupingIDList);
			//List<String> usedSubTreeIds = checkUnUsedSubTreeRef(xmlProcessor, usedSubtreeRefIds);
			List<String> usedSubTreeIds = checkUnUsedSubTreeRef(xmlProcessor, usedSubtreeRefIdsMap);
			//to get all Operators for validaiton during Package timing for Removed Operators
			List<String> operatorTypeList = getAllOperatorsTypeList();
			
			if(usedSubTreeIds.size()>0){
				for(int k=0; (k < usedSubTreeIds.size()); k++){
					String usedSubtreeRefId = usedSubTreeIds.get(k);
					
					String satisfyFunction = "@type='SATISFIES ALL' or @type='SATISFIES ANY'";
					String otherThanSatisfyfunction = "@type!='SATISFIES ALL' or @type!='SATISFIES ANY'";
					String dateTimeDiffFunction = "@type='DATETIMEDIFF'";
					String qdmVariable = "@qdmVariable='true'";
					//geting list of IDs
					String XPATH_QDMELEMENT = "/measure//subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']//elementRef/@id";
					//geting Unique Ids only
					//String XPATH_QDMELEMENT = "/measure//subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']//elementRef[not(@id =  preceding:: elementRef/@id)]";
					String XPATH_TIMING_ELEMENT = "/measure//subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']//relationalOp";
					String XPATH_SATISFY_ELEMENT = "/measure//subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']//functionalOp["+satisfyFunction+"]";
					String XPATH_FUNCTIONS ="/measure//subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']//functionalOp["+otherThanSatisfyfunction+"]";
					String XPATH_SETOPERATOR ="/measure//subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']//setOp";
					//for DateTimeDiff Validation
					String XPATH_DATE_TIME_DIFF_ELEMENT = "/measure//subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']//functionalOp["+dateTimeDiffFunction+"]";
					
					String XPATH_SUBTREE ="//subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']";
					String XPATH_QDMVARAIBLES = "/measure//subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"'and "+qdmVariable+"]";
					/*System.out.println("MEASURE_XML: "+xmlModel.getXml());*/
					try {
						
						
						NodeList nodesSDE_qdmElementId = (NodeList) xPath.evaluate(XPATH_QDMELEMENT, xmlProcessor.getOriginalDoc(),
								XPathConstants.NODESET);
						NodeList nodesSDE_timingElement = (NodeList) xPath.evaluate(XPATH_TIMING_ELEMENT, xmlProcessor.getOriginalDoc(),
								XPathConstants.NODESET);
						NodeList nodesSDE_satisfyElement = (NodeList) xPath.evaluate(XPATH_SATISFY_ELEMENT, xmlProcessor.getOriginalDoc(),
								XPathConstants.NODESET);
						NodeList nodesSDE_functions = (NodeList) xPath.evaluate(XPATH_FUNCTIONS, xmlProcessor.getOriginalDoc(),
								XPathConstants.NODESET);
						NodeList nodeSDE_setoperator =(NodeList) xPath.evaluate(XPATH_SETOPERATOR, xmlProcessor.getOriginalDoc(),
								XPathConstants.NODESET);
						NodeList nodeSDE_dateTimeDiffElement =(NodeList) xPath.evaluate(XPATH_DATE_TIME_DIFF_ELEMENT, xmlProcessor.getOriginalDoc(),
								XPathConstants.NODESET);
						NodeList nodesSDE_qdmVariables =(NodeList) xPath.evaluate(XPATH_QDMVARAIBLES, xmlProcessor.getOriginalDoc(),
								XPathConstants.NODESET);
						Node nodeSubTree =(Node) xPath.evaluate(XPATH_SUBTREE, xmlProcessor.getOriginalDoc(),
								XPathConstants.NODE);
						// Validation for Nested Clause Logic.
						/*if (nodeSubTreeRefList.getLength() > NESTED_CLAUSE_DEPTH) {
							flag = true;
							break;
						} else {*/
						// 1 is counter value for parent clause.
						int nestedClauseCounter = 0;
						isInvalid = validateNestedClauseLogic(nodeSubTree , nestedClauseCounter
								, isInvalid , xmlProcessor);
						if(isInvalid) {
							result.setValid(false);
							message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
							result.setValidationMessages(message);
							return result;
						}
						
						//}
						for (int n = 0; (n <nodesSDE_timingElement.getLength()); n++) {
							
							Node timingElementchildNode =nodesSDE_timingElement.item(n);
							isInvalid = validateTimingRelationshipNode(timingElementchildNode, operatorTypeList, isInvalid);
							if(isInvalid) {
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
								result.setValidationMessages(message);
								return result;
							}
							
						}
						for (int j = 0; (j < nodesSDE_satisfyElement.getLength()); j++) {
							
							Node satisfyElementchildNode = nodesSDE_satisfyElement.item(j);
							isInvalid = validateSatisfyNode(satisfyElementchildNode, isInvalid);
							if(isInvalid) {
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
								result.setValidationMessages(message);
								return result;
							}
							
						}
						
						for (int m = 0; (m <nodesSDE_qdmElementId.getLength()); m++) {
							String id = nodesSDE_qdmElementId.item(m).getNodeValue();
							String xpathForQdmWithAttributeList ="/measure//subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']//elementRef[@id='"+id+"']/attribute";
							String xpathForQdmWithOutAttributeList ="/measure//subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']//elementRef[@id='"+id+"'][not(attribute)]";
							String XPATH_QDMLOOKUP = "/measure/elementLookUp/qdm[@uuid='"+id+"']";
							Node qdmNode = (Node)xPath.evaluate(XPATH_QDMLOOKUP, xmlProcessor.getOriginalDoc(),XPathConstants.NODE);
							NodeList qdmWithAttributeNodeList = (NodeList)xPath.evaluate(xpathForQdmWithAttributeList, xmlProcessor.getOriginalDoc(),XPathConstants.NODESET);
							NodeList qdmWithOutAttributeList = (NodeList)xPath.evaluate(xpathForQdmWithOutAttributeList, xmlProcessor.getOriginalDoc(),XPathConstants.NODESET);
							//validation for QDMwithAttributeList
							//checking for all the Attribute That are used for The Id
							for(int n=0; n<qdmWithAttributeNodeList.getLength(); n++){
								String attributeName = qdmWithAttributeNodeList.item(n).getAttributes().getNamedItem("name").getNodeValue();
								isInvalid = !validateQdmNode(qdmNode, attributeName);
								if(isInvalid){
									result.setValid(false);
									message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
									result.setValidationMessages(message);
									return result;
								}
							}
							//validation for QDMwithOutAttributeList for the Id
							if((qdmWithOutAttributeList.getLength() >0)){
								String attributeName ="";
								isInvalid = !validateQdmNode(qdmNode, attributeName);
								if(isInvalid){
									result.setValid(false);
									message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
									result.setValidationMessages(message);
									return result;
								}
							}
							
						}
						
						for (int n = 0; (n < nodesSDE_functions.getLength()); n++) {
							
							Node functionsChildNode =nodesSDE_functions.item(n);
							isInvalid = validateFunctionNode(functionsChildNode, operatorTypeList, isInvalid);
							
							if(isInvalid || (usedSubtreeRefIdsMap.get("subTreeIDAtMO").contains(usedSubtreeRefId) &&
									validateFunctionNodeInMO(functionsChildNode))) {
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
								result.setValidationMessages(message);
								return result;
							}
						}
						
						for (int n = 0; (n < nodeSDE_setoperator.getLength()); n++) {
							
							Node setOperatorChildNode = nodeSDE_setoperator.item(n);
							isInvalid = validateSetOperatorNode(setOperatorChildNode, isInvalid);
							if(isInvalid) {
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
								result.setValidationMessages(message);
								return result;
							}
							
						}
						
						for (int n = 0; (n < nodeSDE_dateTimeDiffElement.getLength()); n++) {
							
							if(usedSubtreeRefIdsMap.get("subTreeIDAtPop").contains(usedSubtreeRefId) ||
									usedSubtreeRefIdsMap.get("subTreeIDAtStrat").contains(usedSubtreeRefId)){
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
								result.setValidationMessages(message);
								return result;
							}
							
							Node dateTimeDiffChildNode = nodeSDE_dateTimeDiffElement.item(n);
							if(dateTimeDiffChildNode.getChildNodes().getLength() < 2){
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
								result.setValidationMessages(message);
								return result;
							}
							
							//Verifying no datetime dif funcitons are not in riskAdjustmentVariables
							String RISK_ADJUSTMENT_DATETIMEDIF_RETRIVAL = "/measure/riskAdjustmentVariables/subTreeRef[@id='"+ usedSubtreeRefId +"']";
							NodeList riskAdjustmentNodes = (NodeList) xPath.evaluate(RISK_ADJUSTMENT_DATETIMEDIF_RETRIVAL, xmlProcessor.getOriginalDoc(),
									XPathConstants.NODESET);
							if(riskAdjustmentNodes.getLength() > 0){
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_RISK_ADJUSTMENT());
								result.setValidationMessages(message);
								return result;
							}
							
						}
						//verifying no qdm variables are in riskAjustmentVariables
						for(int p = 0; p<nodesSDE_qdmVariables.getLength(); p++){
							String nodeUUID = nodesSDE_qdmVariables.item(p).getAttributes().getNamedItem("uuid").getNodeValue();
							String RISK_ADJUSTMENT_QDM_RETRIVAL = "/measure/riskAdjustmentVariables/subTreeRef[@id='"+ nodeUUID +"']";
							NodeList riskAdjustmentNodes = (NodeList) xPath.evaluate(RISK_ADJUSTMENT_QDM_RETRIVAL, xmlProcessor.getOriginalDoc(),
									XPathConstants.NODESET);
							if(riskAdjustmentNodes.getLength()>0){
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_RISK_ADJUSTMENT());
								result.setValidationMessages(message);
								return result;
							}
						}
						
						
					} catch (XPathExpressionException e) {
						
						e.printStackTrace();
					}
				}
			}
			
		}
		return result;
	}
	
	/**
	 * Validate function node in measure observation.
	 *
	 * @param functionsChildNode the functions child node
	 * @return true, if successful
	 */
	private boolean validateFunctionNodeInMO(Node functionsChildNode) {
		
		String displayName = functionsChildNode.getAttributes().getNamedItem("displayName").getNodeValue();
		String type = functionsChildNode.getAttributes().getNamedItem("type").getNodeValue();
		if(!type.equalsIgnoreCase(displayName)) {
			return true;
		}
		return false;
	}
	
	/**
	 * This method will evaluate Logical Operators and checks if Logical operators has child nodes or not.
	 * Default Top Level Logical Operators are not considered for this validation.
	 * @param populationTopLevelLogicalOp - NodeList.
	 * @return String - message
	 */
	private String validateLogicalOpsInAllPopulations(NodeList populationTopLevelLogicalOp) {
		boolean isInvalid = false;
		String message = null;
		if (populationTopLevelLogicalOp.getLength() > 0) {
			for (int i = 0; i < populationTopLevelLogicalOp.getLength(); i++) {
				Node operatorNode = populationTopLevelLogicalOp.item(i);
				if (operatorNode.getParentNode().getAttributes().getNamedItem("type")
						.toString().equalsIgnoreCase("startum")) {
					if (operatorNode.hasChildNodes()) {
						isInvalid = findInvalidLogicalOperators(operatorNode.getChildNodes(), false);
					} else {
						isInvalid = true;
					}
					if (isInvalid) {
						message = MatContext.get().getMessageDelegate()
								.getCLAUSE_WORK_SPACE_INVALID_LOGICAL_OPERATOR();
						break;
					}
				} else {
					// Populations other than Stratification : dont check for default top level Logical Op.
					if (operatorNode.hasChildNodes() &&
							(operatorNode.getChildNodes().getLength()>1)) {
						isInvalid = findInvalidLogicalOperators(operatorNode.getChildNodes(), true);
						
						if (isInvalid) {
							message = MatContext.get().getMessageDelegate()
									.getCLAUSE_WORK_SPACE_INVALID_LOGICAL_OPERATOR();
							break;
						}
					}
				}
			}
		}
		return message;
	}
	
	
	/**
	 * Find invalid logical operators.
	 *
	 * @param populationTopLevelLogicalOp the population top level logical op
	 * @param isTopLevelLogicalOp the is top level logical op
	 * @return true, if successful
	 */
	private boolean findInvalidLogicalOperators(NodeList populationTopLevelLogicalOp, boolean isTopLevelLogicalOp) {
		boolean isInvalid = false;
		if((populationTopLevelLogicalOp != null)){
			for (int i=0; i < populationTopLevelLogicalOp.getLength();i++) {
				Node operatorNode = populationTopLevelLogicalOp.item(i);
				if (operatorNode.getNodeName().equalsIgnoreCase("comment")) {
					//ignore the comment for the top Level Logical Operator
					if(!isTopLevelLogicalOp) {
						isInvalid = true;
					} else {
						isTopLevelLogicalOp = false;
					}
					continue;
				}
				
				if (operatorNode.getNodeName().equalsIgnoreCase("subTreeRef")) {
					isInvalid = false;
				} else {
					if (operatorNode.hasChildNodes()) {
						isInvalid = findInvalidLogicalOperators(operatorNode.getChildNodes(), false);
					} else {
						if(!isTopLevelLogicalOp){
							isInvalid = true;
						} else {
							isTopLevelLogicalOp = false;
						}
					}
				}
				if(isInvalid){
					break;
				}
			}
		}
		return isInvalid;
	}
	
	
	/**
	 * Validate Clause should have depth up to 10 Levels.
	 *
	 * @param nodeSubTreeRef the node sub tree ref
	 * @param counter the counter
	 * @param flag the flag
	 * @param xmlProcessor the xml processor
	 * @return true, if successful
	 * @throws XPathExpressionException the x path expression exception
	 */
	private boolean validateNestedClauseLogic(Node nodeSubTreeRef, int counter, boolean flag
			, XmlProcessor xmlProcessor) throws XPathExpressionException {
		NodeList children = nodeSubTreeRef.getChildNodes();
		String subTreeNodeName = nodeSubTreeRef.getNodeName();
		if ((children.getLength() == 0) && subTreeNodeName.equalsIgnoreCase("subTree")
				&& (nodeSubTreeRef.getAttributes().getNamedItem("instanceOf")==null)) {
			flag = true;
			return flag;
		}
		for (int i = 0; ((i < children.getLength()) &&  !flag); i++) {
			int currentCounter = counter;
			System.out.println("Looping for counter -------" + counter);
			Node node = children.item(i);
			//String uuid = node.getAttributes().getNamedItem("id").getNodeValue();
			if (node.getNodeName().equalsIgnoreCase("subTreeRef")) {
				String uuid = node.getAttributes().getNamedItem("id").getNodeValue();
				if (node.getAttributes().getNamedItem("instance") != null) {
					uuid = node.getAttributes().getNamedItem("instanceOf").getNodeValue();
				}
				String XPATH_SUBTREE = "//subTreeLookUp/subTree[@uuid='"+uuid+"']";
				node = (Node) xPath.evaluate(XPATH_SUBTREE, xmlProcessor.getOriginalDoc(),
						XPathConstants.NODE);
				if(node.hasChildNodes()){
					node = node.getChildNodes().item(0);
				}
			}
			if (node.getChildNodes() != null && node.getChildNodes().getLength() > 0 
					&& !node.getNodeName().equalsIgnoreCase("subTree")) {
				currentCounter = currentCounter + 1;
			}
			if (currentCounter > NESTED_CLAUSE_DEPTH) {
				flag = true;
				System.out.println("Breaking for Node -------" + node.getAttributes().getNamedItem("displayName").getNodeValue());
				break;
			} else {
				flag = validateNestedClauseLogic(node , currentCounter, flag, xmlProcessor);
			}
			
		}
		return flag;
	}
	/**
	 * Check if qdm var instance is present.
	 *
	 * @param usedSubtreeRefId the used subtree ref id
	 * @param xmlProcessor the xml processor
	 * @return the string
	 */
	private String checkIfQDMVarInstanceIsPresent(String usedSubtreeRefId,
			XmlProcessor xmlProcessor){
		
		String XPATH_INSTANCE_QDM_VAR = "/measure/subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']/@instance";
		String XPATH_INSTANCE_OF_QDM_VAR = "/measure/subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']/@instanceOf";
		try {
			Node nodesSDE_SubTree = (Node) xPath.evaluate(XPATH_INSTANCE_QDM_VAR, xmlProcessor.getOriginalDoc(),
					XPathConstants.NODE);
			if(nodesSDE_SubTree!=null){
				Node nodesSDE_SubTreeInstance = (Node) xPath.evaluate(XPATH_INSTANCE_OF_QDM_VAR, xmlProcessor.getOriginalDoc(),
						XPathConstants.NODE);
				usedSubtreeRefId = nodesSDE_SubTreeInstance.getNodeValue();
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return usedSubtreeRefId;
	}
	
	/**
	 * Gets the filtered sub tree ids.
	 *
	 * @param xmlProcessor the xml processor
	 * @param usedSubTreeIdsMap the used sub tree ids map
	 * @return the filtered sub tree ids
	 */
	private List<String> checkUnUsedSubTreeRef(
			XmlProcessor xmlProcessor, Map<String, List<String>> usedSubTreeIdsMap) {
		
		List<String> subTreeIdsAtPop = new ArrayList<String>();
		List<String> subTreeIdsAtMO = new ArrayList<String>();
		List<String> subTreeIdsAtStrat = new ArrayList<String>();
		subTreeIdsAtPop.addAll(usedSubTreeIdsMap.get("subTreeIDAtPop"));
		subTreeIdsAtMO.addAll(usedSubTreeIdsMap.get("subTreeIDAtMO"));
		subTreeIdsAtStrat.addAll(usedSubTreeIdsMap.get("subTreeIDAtStrat"));
		List<String> subTreeIdsAtRAV = getUsedRiskAdjustmentVariables(xmlProcessor);
		subTreeIdsAtPop.removeAll(subTreeIdsAtMO);
		subTreeIdsAtMO.addAll(subTreeIdsAtPop);
		
		subTreeIdsAtMO.removeAll(subTreeIdsAtStrat);
		subTreeIdsAtStrat.addAll(subTreeIdsAtMO);
		
		//to get Used SubTreeRef form Risk Adjustment Variables
		subTreeIdsAtStrat.removeAll(subTreeIdsAtRAV);
		subTreeIdsAtRAV.addAll(subTreeIdsAtStrat);
		
		for(int i=0; i<subTreeIdsAtRAV.size();i++){
			System.out.println("SUBTREE NODES " + subTreeIdsAtRAV.get(i));
		}
		
		return subTreeIdsAtRAV;
	}
	
	
	/**
	 * Gets the used risk adjustment variables.
	 *
	 * @param xmlProcessor the xml processor
	 * @return the used risk adjustment variables
	 */
	private List<String> getUsedRiskAdjustmentVariables(XmlProcessor xmlProcessor){
		List<String> subTreeRefRAVList = new ArrayList<String>();
		
		String xpathforRiskAdjustmentVariables = "/measure/riskAdjustmentVariables/subTreeRef";
		try {
			NodeList subTreeRefIdsNodeListRAV = (NodeList) xPath.evaluate(xpathforRiskAdjustmentVariables,
					xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
			for(int i=0;i<subTreeRefIdsNodeListRAV.getLength();i++){
				Node childNode = subTreeRefIdsNodeListRAV.item(i);
				subTreeRefRAVList.add(childNode.getAttributes().
						getNamedItem("id").getNodeValue());
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return checkUnUsedSubTreeRef(xmlProcessor, subTreeRefRAVList);
	}
	
	
	/**
	 * Gets the stratification clasues id list.
	 *
	 * @param uuid the uuid
	 * @param xmlProcessor the xml processor
	 * @return the stratification clasues id list
	 */
	private List<String> getStratificationClasuesIDList(String uuid, XmlProcessor xmlProcessor) {
		
		String XPATH_MEASURE_GROUPING_STRATIFICATION_CLAUSES = "/measure/strata/stratification" +
				"[@uuid='"+uuid+"']/clause/@uuid";
		List<String> clauseList = new ArrayList<String>();
		try {
			NodeList stratificationClausesNodeList = (NodeList)xPath.evaluate(XPATH_MEASURE_GROUPING_STRATIFICATION_CLAUSES,
					xmlProcessor.getOriginalDoc(),XPathConstants.NODESET);
			for(int i=0;i<stratificationClausesNodeList.getLength();i++){
				clauseList.add(stratificationClausesNodeList.item(i).getNodeValue());
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clauseList;
	}
	
	/**
	 * Gets the used subtree ref ids.
	 *
	 * @param xmlProcessor the xml processor
	 * @param measureGroupingIDList the measure grouping id list
	 * @return the used subtree ref ids
	 */
	private Map<String , List<String>> getUsedSubtreeRefIds(XmlProcessor xmlProcessor, List<String> measureGroupingIDList) {
		
		List<String> usedSubTreeRefIdsPop = new ArrayList<String>();
		List<String> usedSubTreeRefIdsStrat = new ArrayList<String>();
		List<String> usedSubTreeRefIdsMO = new ArrayList<String>();
		Map<String , List<String>> usedSubTreeIdsMap = new HashMap<String, List<String>>();
		NodeList groupedSubTreeRefIdsNodeListPop;
		NodeList groupedSubTreeRefIdsNodeListMO;
		NodeList groupedSubTreeRefIdListStrat;
		String uuidXPathString = "";
		
		for(String uuidString: measureGroupingIDList){
			uuidXPathString += "@uuid = '"+uuidString + "' or";
		}
		
		uuidXPathString = uuidXPathString.substring(0,uuidXPathString.lastIndexOf(" or"));
		String XPATH_POPULATION_SUBTREEREF = "/measure/populations//clause["+uuidXPathString+"]//subTreeRef[not(@id = preceding:: populations//clause//subTreeRef/@id)]/@id";
		
		try {
			// Populations, MeasureObervations and Startification
			groupedSubTreeRefIdsNodeListPop = (NodeList) xPath.evaluate(XPATH_POPULATION_SUBTREEREF,
					xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
			
			for (int i = 0; i < groupedSubTreeRefIdsNodeListPop.getLength(); i++) {
				Node groupedSubTreeRefIdAttributeNodePop = groupedSubTreeRefIdsNodeListPop
						.item(i);
				String uuid = groupedSubTreeRefIdAttributeNodePop
						.getNodeValue();
				
				uuid = checkIfQDMVarInstanceIsPresent(uuid, xmlProcessor);
				if(!usedSubTreeRefIdsPop.contains(uuid)){
					usedSubTreeRefIdsPop.add(uuid);
				}
			}
			
			//to get the Used SubtreeIds from Population Tab.
			List<String> usedSubtreeIdsAtPop = checkUnUsedSubTreeRef(xmlProcessor, usedSubTreeRefIdsPop);
			
			// Measure Observations
			String measureObservationSubTreeRefID = "/measure/measureObservations//clause["+
					uuidXPathString+"]//subTreeRef[not(@id = preceding:: measureObservations//clause//subTreeRef/@id)]/@id";
			groupedSubTreeRefIdsNodeListMO = (NodeList) xPath.evaluate(measureObservationSubTreeRefID,
					xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
			
			for (int i = 0; i < groupedSubTreeRefIdsNodeListMO.getLength(); i++) {
				Node groupedSubTreeRefIdAttributeNodeMO = groupedSubTreeRefIdsNodeListMO
						.item(i);
				String uuid = groupedSubTreeRefIdAttributeNodeMO.getNodeValue();
				uuid = checkIfQDMVarInstanceIsPresent(uuid, xmlProcessor);
				if(!usedSubTreeRefIdsMO.contains(uuid)){
					usedSubTreeRefIdsMO.add(uuid);
				}
			}
			
			//used SubtreeIds at Measure Observation
			List<String> usedSubtreeIdsAtMO = checkUnUsedSubTreeRef(xmlProcessor, usedSubTreeRefIdsMO);
			
			// Stratifications
			
			String startSubTreeRefID = "/measure/strata//clause["+
					uuidXPathString+"]//subTreeRef[not(@id = preceding:: strata//clause//subTreeRef/@id)]/@id";
			groupedSubTreeRefIdListStrat = (NodeList) xPath.evaluate(startSubTreeRefID,
					xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
			
			for (int i = 0; i < groupedSubTreeRefIdListStrat.getLength(); i++) {
				Node groupedSubTreeRefIdAttributeNodeStrat = groupedSubTreeRefIdListStrat
						.item(i);
				String uuid = groupedSubTreeRefIdAttributeNodeStrat.getNodeValue();
				uuid = checkIfQDMVarInstanceIsPresent(uuid, xmlProcessor);
				if(!usedSubTreeRefIdsStrat.contains(uuid)) {
					usedSubTreeRefIdsStrat.add(uuid);
				}
			}
			
			//get used Subtreeids at Stratification
			List<String> usedSubtreeIdsAtStrat = checkUnUsedSubTreeRef(xmlProcessor, usedSubTreeRefIdsStrat);
			
			//			usedSubTreeRefIdsPop.removeAll(usedSubTreeRefIdsMO);
			//			usedSubTreeRefIdsMO.addAll(usedSubTreeRefIdsPop);
			//
			//			usedSubTreeRefIdsMO.removeAll(usedSubTreeRefIdsStrat);
			//			usedSubTreeRefIdsStrat.addAll(usedSubTreeRefIdsMO);
			
			usedSubTreeIdsMap.put("subTreeIDAtPop", usedSubtreeIdsAtPop);
			usedSubTreeIdsMap.put("subTreeIDAtMO", usedSubtreeIdsAtMO);
			usedSubTreeIdsMap.put("subTreeIDAtStrat", usedSubtreeIdsAtStrat);
			
		} catch (XPathExpressionException e) {
			
			e.printStackTrace();
		}
		
		return usedSubTreeIdsMap;
	}
	
	/**
	 * Check un used sub tree ref.
	 *
	 * @param xmlProcessor the xml processor
	 * @param usedSubTreeRefIds the used sub tree ref ids
	 * @return the list
	 */
	private  List<String> checkUnUsedSubTreeRef(XmlProcessor xmlProcessor, List<String> usedSubTreeRefIds){
		
		List<String> allSubTreeRefIds = new ArrayList<String>();
		NodeList subTreeRefIdsNodeList;
		try {
			subTreeRefIdsNodeList = (NodeList) xPath.evaluate(
					"/measure//subTreeRef/@id", xmlProcessor.getOriginalDoc(),
					XPathConstants.NODESET);
			
			
			for (int i = 0; i < subTreeRefIdsNodeList.getLength(); i++) {
				Node SubTreeRefIdAttributeNode = subTreeRefIdsNodeList.item(i);
				if (!allSubTreeRefIds.contains(SubTreeRefIdAttributeNode
						.getNodeValue())) {
					allSubTreeRefIds.add(SubTreeRefIdAttributeNode.getNodeValue());
				}
			}
			allSubTreeRefIds.removeAll(usedSubTreeRefIds);
			
			for (int i = 0; i < usedSubTreeRefIds.size(); i++) {
				for (int j = 0; j < allSubTreeRefIds.size(); j++) {
					Node usedSubTreeRefNode = (Node) xPath.evaluate(
							"/measure/subTreeLookUp/subTree[@uuid='"
									+ usedSubTreeRefIds.get(i)
									+ "']//subTreeRef[@id='"
									+ allSubTreeRefIds.get(j) + "']",
									xmlProcessor.getOriginalDoc(), XPathConstants.NODE);
					
					if (usedSubTreeRefNode != null) {
						
						String subTreeUUID = usedSubTreeRefNode.getAttributes().getNamedItem("id").getNodeValue();
						String XPATH_IS_INSTANCE_OF = "//subTree [boolean(@instanceOf)]/@uuid ='"
								+ subTreeUUID +"'";
						boolean isOccurrenceNode = (Boolean) xPath.evaluate(XPATH_IS_INSTANCE_OF, xmlProcessor.getOriginalDoc(), XPathConstants.BOOLEAN);
						if(isOccurrenceNode) {
							String XPATH_PARENT_UUID = "//subTree [@uuid ='"+subTreeUUID +"']/@instanceOf";
							String parentUUID = (String) xPath.evaluate(XPATH_PARENT_UUID, xmlProcessor.getOriginalDoc(), XPathConstants.STRING);
							if (!usedSubTreeRefIds.contains(parentUUID)) {
								usedSubTreeRefIds.add(parentUUID);
							}
							
						}
						if (!usedSubTreeRefIds.contains(allSubTreeRefIds.get(j))) {
							
							
							usedSubTreeRefIds.add(allSubTreeRefIds.get(j));
						}
					}
				}
				
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usedSubTreeRefIds;
	}
	/**
	 * Validate qdm node.
	 *
	 * @param qdmchildNode the qdmchild node
	 * @param attributeValue the attribute value
	 * @return true, if successful
	 */
	private boolean validateQdmNode(Node qdmchildNode, String attributeValue) {
		boolean flag = true;
		String dataTypeValue = qdmchildNode.getAttributes()
				.getNamedItem("datatype").getNodeValue();
		String qdmName = qdmchildNode.getAttributes().getNamedItem("name")
				.getNodeValue();
		String oidValue = qdmchildNode.getAttributes().getNamedItem("oid").getNodeValue();
		if (dataTypeValue.equalsIgnoreCase("timing element")) {
			if (qdmName.equalsIgnoreCase("Measurement End Date")
					|| qdmName.equalsIgnoreCase("Measurement Start Date")) {
				flag = false;
			}
		}
		else if(dataTypeValue.equalsIgnoreCase("Patient characteristic Birthdate") || dataTypeValue.equalsIgnoreCase("Patient characteristic Expired")){
			
			if(oidValue.equalsIgnoreCase("419099009") || oidValue.equalsIgnoreCase("21112-8")){
				//do nothing
			}else{
				flag = false;
			}
		}
		//
		else if (attributeValue.isEmpty()) {
			if (!checkIfQDMDataTypeIsPresent(dataTypeValue)) {
				flag = false;
			}
			
		}
		else if (!attributeValue.isEmpty() && (attributeValue.length() > 0)) {
			if (checkIfQDMDataTypeIsPresent(dataTypeValue)) {
				
				List<QDSAttributes> attlibuteList = getAllDataTypeAttributes(dataTypeValue);
				if ((attlibuteList.size() > 0) && (attlibuteList != null)) {
					List<String> attrList = new ArrayList<String>();
					for (int i = 0; i < attlibuteList.size(); i++) {
						attrList.add(attlibuteList.get(i).getName());
					}
					if (!attrList.contains(attributeValue)) {
						flag = false;
					}
				}
				
			} else {
				flag = false;
			}
		}
		return flag;
	}
	
	
	
	/**
	 * Validate Timing and Relationship node.
	 *
	 * @param timingElementchildNode the timing elementchild node
	 * @param operatorTypeList the operator type list
	 * @param flag the flag
	 * @return true, if successful
	 */
	private boolean validateTimingRelationshipNode(Node timingElementchildNode, List<String> operatorTypeList, boolean flag) {
		int childCount = timingElementchildNode.getChildNodes().getLength();
		String type = timingElementchildNode.getAttributes().getNamedItem("type").getNodeValue();
		if((childCount != 2) || !operatorTypeList.contains(type)){
			flag = true;
		}
		return flag;
	}
	
	
	/**
	 * Validate satisfy node.
	 *
	 * @param satisfyElementchildNode the satisfy elementchild node
	 * @param flag the flag
	 * @return true, if successful
	 */
	private boolean validateSatisfyNode(Node satisfyElementchildNode, boolean flag) {
		int childCount = satisfyElementchildNode.getChildNodes().getLength();
		if (childCount < 3) {
			flag = true;
		} else {
			Node firstQDMNode = satisfyElementchildNode.getChildNodes().item(0);
			if (firstQDMNode.hasChildNodes()
					&& firstQDMNode.getChildNodes().item(0).getNodeName().equalsIgnoreCase("attribute")) {
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * Validate function node.
	 *
	 * @param functionchildNode the functionchild node
	 * @param operatorTypeList the operator type list
	 * @param flag the flag
	 * @return true, if successful
	 */
	private boolean validateFunctionNode(Node functionchildNode,  List<String> operatorTypeList, boolean flag){
		int functionChildCount = functionchildNode.getChildNodes().getLength();
		String type = functionchildNode.getAttributes().getNamedItem("type").getNodeValue();
		if((functionChildCount< 1) || !operatorTypeList.contains(type)){
			flag = true;
		}
		return flag;
		
	}
	
	/**
	 * Validate set operator node.
	 *
	 * @param SetOperatorchildNode the set operatorchild node
	 * @param flag the flag
	 * @return true, if successful
	 */
	private boolean validateSetOperatorNode(Node SetOperatorchildNode, boolean flag){
		int setOperatorChildCount = SetOperatorchildNode.getChildNodes().getLength();
		if(setOperatorChildCount < 2) {
			flag = true;
		}
		return flag;
		
	}
	
	
	/**
	 * Validate date and time operators nodes.
	 *
	 * @param dateTimeDiffChildNode the date time diff child node
	 * @param flag the flag
	 * @return true, if successful
	 */
	private boolean validateDateTimeDiffNode(Node dateTimeDiffChildNode, boolean flag){
		int dateTimeChildCount = dateTimeDiffChildNode.getChildNodes().getLength();
		if(dateTimeChildCount < 2) {
			flag = true;
		}
		return flag;
		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#validateForGroup(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public ValidateMeasureResult validateForGroup(ManageMeasureDetailModel model) {
		
		logger.debug(" MeasureLibraryServiceImpl: validateGroup Start :  ");
		
		List<String> message = new ArrayList<String>();
		ValidateMeasureResult result = new ValidateMeasureResult();
		if(MatContextServiceUtil.get().isCurrentMeasureEditable(getMeasureDAO(),model.getId())){
			MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(model.getId());

			if (((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml()))) {
				XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
				/*System.out.println("MEASURE_XML: "+xmlModel.getXml());*/

				//validate for at least one grouping
				String XPATH_GROUP = "/measure/measureGrouping/group";

				NodeList groupSDE;
				try {
					groupSDE = (NodeList) xPath.evaluate(XPATH_GROUP, xmlProcessor.getOriginalDoc(),
							XPathConstants.NODESET);

					if(groupSDE.getLength()==0){
						message.add(MatContext.get().getMessageDelegate().getGroupingRequiredMessage());

					}else{
						for(int i=1; i<=groupSDE.getLength(); i++){
							NodeList numberOfStratificationPerGroup = (NodeList) xPath.evaluate("/measure/measureGrouping/group[@sequence='"+i+"']/packageClause[@type='stratification']", xmlProcessor.getOriginalDoc(),
									XPathConstants.NODESET);
							if(numberOfStratificationPerGroup.getLength()>1){
								message.add(MatContext.get().getMessageDelegate().getSTRATIFICATION_VALIDATION_FOR_GROUPING());
								break;
							}
						}

					}
				} catch (XPathExpressionException e2) {

					e2.printStackTrace();
				}
			}
		}
		result.setValid(message.size() == 0);
		result.setValidationMessages(message);
		return result;
	}
	
	/**
	 * Takes an XPath notation String for a particular tag and a Document object
	 * and finds and removes the tag from the document.
	 * @param nodeXPath the node x path
	 * @param originalDoc the original doc
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void removeNode(String nodeXPath, Document originalDoc) throws XPathExpressionException {
		Node node = (Node)xPath.evaluate(nodeXPath, originalDoc.getDocumentElement(), XPathConstants.NODE);
		if(node != null){
			Node parentNode = node.getParentNode();
			parentNode.removeChild(node);
		}
	}
	
	/**
	 * Update node.
	 *
	 * @param nodeXPath the node x path
	 * @param originalDoc the original doc
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void updateNode(String nodeXPath, Document originalDoc) throws XPathExpressionException {
		Node node = (Node)xPath.evaluate(nodeXPath, originalDoc.getDocumentElement(), XPathConstants.NODE);
		if(node != null){
			Node parentNode = node.getParentNode();
			parentNode.removeChild(node);
		}
	}
	
	
	
	
	/**
	 * Gets the all measure types.
	 *
	 * @return the all measure types
	 */
	@Override
	public final List<MeasureType> getAllMeasureTypes(){
		List<MeasureTypeDTO> measureTypeDTOList = measureTypeDAO.getAllMeasureTypes();
		List<MeasureType> measureTypeList = new ArrayList<MeasureType>();
		for(MeasureTypeDTO measureTypeDTO : measureTypeDTOList){
			MeasureType measureType = new MeasureType();
			measureType.setDescription(measureTypeDTO.getName());
			measureType.setAbbrDesc(MeasureDetailsUtil.getMeasureTypeAbbr(measureTypeDTO.getName()));
			measureTypeList.add(measureType);
		}
		return measureTypeList;
		
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getAllAuthors()
	 */
	@Override
	public List<Organization> getAllOrganizations(){
		List<Organization> organizationDTOList = organizationDAO.getAllOrganizations();
		return organizationDTOList;
	}
	
	/**
	 * Gets the all operators type list.
	 *
	 * @return the all operators type list
	 */
	private List<String> getAllOperatorsTypeList() {
		List<OperatorDTO> allOperatorsList = operatorDAO.getAllOperators();
		List<String> allOperatorsTypeList = new ArrayList<String>();
		for(int i = 0; i<allOperatorsList.size(); i ++){
			allOperatorsTypeList.add(allOperatorsList.get(i).getId());
		}
		return allOperatorsTypeList;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getUsedStewardAndDevelopersList(java.lang.String)
	 */
	@Override
	public MeasureDetailResult getUsedStewardAndDevelopersList(String measureId) {
		logger.info("In MeasureLibraryServiceImpl.getUsedStewardAndDevelopersList() method..");
		logger.info("Loading Measure for MeasueId: " + measureId);
		Measure measure = getService().getById(measureId);
		MeasureDetailResult usedStewardAndAuthorList = new MeasureDetailResult();
		MeasureXmlModel xml = getMeasureXmlForMeasure(measureId);
		usedStewardAndAuthorList.setUsedAuthorList(getAuthorsList(xml));
		usedStewardAndAuthorList.setUsedSteward(getSteward(xml));
		usedStewardAndAuthorList.setAllAuthorList(getAllAuthorList());
		usedStewardAndAuthorList.setAllStewardList(getAllStewardList());
		return usedStewardAndAuthorList;
		
	}
	
	/**
	 * Gets the all steward list.
	 *
	 * @return the all steward list
	 */
	private List<MeasureSteward> getAllStewardList() {
		List<MeasureSteward> stewardList = new ArrayList<MeasureSteward>();
		List<Organization> organizationList = getAllOrganizations();
		for(Organization org:organizationList){
			MeasureSteward steward= new MeasureSteward();
			steward.setId(Long.toString(org.getId()));
			steward.setOrgName(org.getOrganizationName());
			steward.setOrgOid(org.getOrganizationOID());
			stewardList.add(steward);
		}
		return stewardList;
	}
	
	/**
	 * Gets the all author list.
	 *
	 * @return the all author list
	 */
	private List<Author> getAllAuthorList() {
		List<Author> authorList = new ArrayList<Author>();
		List<Organization> organizationList = getAllOrganizations();
		for(Organization org:organizationList){
			Author author= new Author();
			author.setId(Long.toString(org.getId()));
			author.setAuthorName(org.getOrganizationName());
			author.setOrgId(org.getOrganizationOID());
			authorList.add(author);
		}
		
		return authorList;
	}
	
	/**
	 * Gets the authors list.
	 *
	 * @param xmlModel the xml model
	 * @return the authors list
	 */
	private List<Author> getAuthorsList(MeasureXmlModel xmlModel) {
		XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
		String XPATH_EXPRESSION_DEVELOPERS = "/measure//measureDetails//developers";
		List<Author> authorList = new ArrayList<Author>();
		List<Author> usedAuthorList = new ArrayList<Author>();
		List<Organization> allOrganization = getAllOrganizations();
		try {
			
			NodeList developerParentNodeList = (NodeList) xPath.evaluate(
					XPATH_EXPRESSION_DEVELOPERS, processor.getOriginalDoc(),
					XPathConstants.NODESET);
			Node developerParentNode = developerParentNodeList.item(0);
			if (developerParentNode != null) {
				NodeList developerNodeList = developerParentNode
						.getChildNodes();
				
				for (int i = 0; i < developerNodeList.getLength(); i++) {
					Author author = new Author();
					String developerId = developerNodeList.item(i).getAttributes()
							.getNamedItem("id").getNodeValue();
					String AuthorValue = developerNodeList.item(i).getTextContent();
					author.setId(developerId);
					author.setAuthorName(AuthorValue);
					authorList.add(author);
					
				}
				//if deleted, remove from the list
				for(Organization org:allOrganization){
					for(int i=0;i<authorList.size();i++){
						if(authorList.get(i).getId().equalsIgnoreCase(Long.toString(org.getId()))){
							usedAuthorList.add(authorList.get(i));
						}
					}
				}
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return usedAuthorList;
	}
	
	
	/**
	 * Gets the steward id.
	 *
	 * @param xmlModel the xml model
	 * @return the steward id
	 */
	private MeasureSteward getSteward(MeasureXmlModel xmlModel) {
		MeasureSteward measureSteward = new MeasureSteward();
		XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
		List<Organization> allOrganization = getAllOrganizations();
		String XPATH_EXPRESSION_STEWARD = "/measure//measureDetails//steward";
		
		try {
			Node stewardParentNode = (Node) xPath.evaluate(
					XPATH_EXPRESSION_STEWARD, processor.getOriginalDoc(),
					XPathConstants.NODE);
			if (stewardParentNode != null) {
				String id = stewardParentNode.getAttributes()
						.getNamedItem("id").getNodeValue();
				for(Organization org:allOrganization){
					if(id.equalsIgnoreCase(Long.toString(org.getId()))){
						measureSteward.setId(id);
						measureSteward.setOrgName(org.getOrganizationName());
						measureSteward.setOrgOid(org.getOrganizationOID());
					}
				}
				
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return measureSteward;
		
	}
	
	/**
	 * Checks if is measure created.
	 *
	 * @return true, if is measure created
	 */
	public boolean isMeasureCreated() {
		return isMeasureCreated;
	}
	
	/**
	 * Sets the measure created.
	 *
	 * @param isMeasureCreated the new measure created
	 */
	public void setMeasureCreated(boolean isMeasureCreated) {
		this.isMeasureCreated = isMeasureCreated;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getReleaseDate()
	 */
	/*@Override
	public String getReleaseDate() {
		return releaseDate;
	}*?
	
	/**
	 * Sets the release date.
	 *
	 * @param releaseDate the new release date
	 */
	/*public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}*/
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getDefaultSDEFromMeasureXml(java.lang.String)
	 */
	@Override
	public final QualityDataModelWrapper getDefaultSDEFromMeasureXml(final String measureId) {
		logger.info("Inside MeasureLibraryServiceImp :: getDefaultSDEFromMeasureXml :: Start");
		MeasureXmlModel measureXmlModel = getMeasureXmlForMeasure(measureId);
		QualityDataModelWrapper details = convertXmltoQualityDataDTOModel(measureXmlModel);
		ArrayList<QualityDataSetDTO> finalList = new ArrayList<QualityDataSetDTO>();
		if (details != null) {
			if ((details.getQualityDataDTO() != null) && (details.getQualityDataDTO().size() != 0)) {
				logger.info(" details.getQualityDataDTO().size() :" + details.getQualityDataDTO().size());
				for (QualityDataSetDTO dataSetDTO : details.getQualityDataDTO()) {
					if (dataSetDTO.getCodeListName() != null) {
						if ((dataSetDTO.isSuppDataElement())) {
							finalList.add(dataSetDTO);
						}
					}
				}
			}
		}
		details.setQualityDataDTO(finalList);
		logger.info("finalList()of QualityDataSetDTO ::" + finalList.size());
		logger.info("Inside MeasureLibraryServiceImp :: getDefaultSDEFromMeasureXml :: END");
		return details;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getMeasuresForMeasureOwner()
	 */
	@Override
	public List<MeasureOwnerReportDTO> getMeasuresForMeasureOwner() throws XPathExpressionException {
		Map<User, List<Measure>> map = new HashMap<User, List<Measure>>();
		List<User> nonAdminUserList = getUserService().getAllNonAdminActiveUsers();
		for (User user : nonAdminUserList) {
			List<Measure> measureList = getMeasureDAO().getMeasureListForMeasureOwner(user);
			if ((measureList != null) && (measureList.size() > 0)) {
				map.put(user, measureList);
			}
		}
		List<MeasureOwnerReportDTO> ownerReList = populateMeasureOwnerReport(map);
		return ownerReList;
	}
	
	/**
	 * Method to populate MeasureOwnerReport DTO.
	 * @param map - User and List of Measures for User.
	 * @return List<MeasureOwnerReportDTO>
	 * @throws XPathExpressionException - {@link XPathExpressionException}
	 */
	private List<MeasureOwnerReportDTO> populateMeasureOwnerReport(Map<User, List<Measure>> map) throws XPathExpressionException {
		List<MeasureOwnerReportDTO> measureOwnerReportDTOs = new ArrayList<MeasureOwnerReportDTO>();
		for (Entry<User, List<Measure>> entry : map.entrySet()) {
			User user = entry.getKey();
			List<Measure> measureList = entry.getValue();
			for (Measure measure : measureList) {
				logger.info("Start to evaluate measure id :::: " + measure.getId());
				MeasureXML measureXML = getMeasureXMLDAO().findForMeasure(measure.getId());
				if (measureXML != null) {
					String measureXmlString = measureXML.getMeasureXMLAsString();
					if (measureXmlString != null) {
						MeasureOwnerReportDTO ownerReportDTO = new MeasureOwnerReportDTO();
						ownerReportDTO.setFirstName(user.getFirstName());
						ownerReportDTO.setLastName(user.getLastName());
						ownerReportDTO.setOrganizationName(user.getOrganizationName());
						ownerReportDTO.setMeasureDescription(measure.getDescription());
						ownerReportDTO.setCmsNumber(measure.geteMeasureId());
						XmlProcessor processor = new XmlProcessor(measureXmlString);
						String xpathNqfId = "/measure/measureDetails/nqfid";
						String xpathGuid = "/measure/measureDetails/guid";
						Node nqfNode = processor.findNode(processor.getOriginalDoc(), xpathNqfId);
						if (nqfNode != null) {
							if (nqfNode.getAttributes().getNamedItem("extension") != null) {
								String nqfNumber = nqfNode.getAttributes().getNamedItem("extension")
										.getNodeValue();
								ownerReportDTO.setNqfId(nqfNumber);
							}
						}
						Node guidNode = processor.findNode(processor.getOriginalDoc(), xpathGuid);
						if (guidNode != null) {
							String guidNumber = guidNode.getTextContent();
							ownerReportDTO.setGuid(guidNumber);
						}
						measureOwnerReportDTOs.add(ownerReportDTO);
					}
				} else {
					logger.info("Measure xml not found for measure id :::: " + measure.getId());
				}
			}
		}
		return measureOwnerReportDTOs;
	}
	
	/**
	 * Gets the default expansion identifier.
	 *
	 * @param measureId the measure id
	 * @return the default expansion identifier
	 */
	@Override
	public String getDefaultExpansionIdentifier(String measureId){
		String defaultExpId = null;
		MeasureXmlModel model = getMeasureXmlForMeasure(measureId);
		if (model != null) {
			XmlProcessor processor = new XmlProcessor(model.getXml());
			
			String XPATH_VSAC_EXPANSION_IDENTIFIER = "/measure/elementLookUp/@vsacExpIdentifier";
			
			try {
				Node vsacExpIdAttr = (Node) xPath.evaluate(XPATH_VSAC_EXPANSION_IDENTIFIER, processor.getOriginalDoc(),
						XPathConstants.NODE);
				if(vsacExpIdAttr != null){
					defaultExpId = vsacExpIdAttr.getNodeValue();
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		
		return defaultExpId;
	}
	
	/**
	 * Scrub for mark up.
	 *
	 * @param model the model
	 */
	private void scrubForMarkUp(ManageMeasureDetailModel model) {
		String markupRegExp = "<[^>]+>";
		
		String noMarkupText = model.getName().trim().replaceAll(markupRegExp, "");
		System.out.println("measure name:"+noMarkupText);
		if(model.getName().trim().length() > noMarkupText.length()){
			model.setName(noMarkupText);
		}
		
		noMarkupText = model.getShortName().trim().replaceAll(markupRegExp, "");
		System.out.println("measure short-name:"+noMarkupText);
		if(model.getShortName().trim().length() > noMarkupText.length()){
			model.setShortName(noMarkupText);
		}
		
	}
	
	
	/**
	 * Validate stratum for atleast one clause.
	 * This validation is performed at the time of Measure Package Creation
	 * where if the a stratification is part of grouping then we need to check if
	 * stratum has atleast one clause. If stratum does'nt have atleast one
	 * clause then we throw a Warning Message.
	 *
	 * @param measureXmlModel the measure xml model
	 * @return the string
	 */
	private String validateStratumForAtleastOneClause(MeasureXmlModel measureXmlModel){
		String message = null;
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			
			//validate only for Stratification where each startum should have atleast one Clause
			String XAPTH_MEASURE_GROUPING_STRATIFICATION = "/measure/measureGrouping/group/packageClause[@type='stratification']"
					+ "[not(@uuid = preceding:: group/packageClause/@uuid)]/@uuid";
			
			List<String> stratificationClausesIDlist = null;
			try {
				NodeList startificationUuidList = (NodeList) xPath.evaluate(XAPTH_MEASURE_GROUPING_STRATIFICATION,
						xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
				
				for(int i=0 ; i<startificationUuidList.getLength();i++){
					String uuid = startificationUuidList.item(i).getNodeValue();
					stratificationClausesIDlist = getStratificationClasuesIDList(uuid, xmlProcessor);
				}
				if (stratificationClausesIDlist != null) {
					for(String clauseUUID: stratificationClausesIDlist){
						String XPATH_VALIDATE_STRATIFICATION_CLAUSE = "/measure/strata/stratification/clause[@uuid='"
								+clauseUUID+"']//subTreeRef/@id";
						NodeList strataClauseNodeList = (NodeList)xPath.evaluate(XPATH_VALIDATE_STRATIFICATION_CLAUSE,
								xmlProcessor.getOriginalDoc(),XPathConstants.NODESET);
						if((strataClauseNodeList != null) && (strataClauseNodeList.getLength()<=0)){
							message = MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_STRATA();
							break;
						}
					}
				}
			} catch (XPathExpressionException e2) {
				e2.printStackTrace();
			}
		}
		return message;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getCurrentReleaseVersion()
	 */
	@Override
	public String getCurrentReleaseVersion() {
		return currentReleaseVersion;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#setCurrentReleaseVersion(java.lang.String)
	 */
	@Override
	public void setCurrentReleaseVersion(String releaseVersion) {
		currentReleaseVersion = releaseVersion;
	}
	
	/**
	 * Checks if is measure owner.
	 *
	 * @param userId the user id
	 * @return true, if is measure owner
	 */
	private boolean isMeasureOwner(String userId){
		SecurityContext sc = SecurityContextHolder.getContext();
		MatUserDetails details = (MatUserDetails)sc.getAuthentication().getDetails();
		if(details.getId().equalsIgnoreCase(userId)){
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the cql service.
	 *
	 * @return the cql service
	 */
	public CQLService getCqlService() {
		return cqlService;
	}
	
	/**
	 * Sets the cql service.
	 *
	 * @param cqlService the new cql service
	 */
	public void setCqlService(CQLService cqlService) {
		this.cqlService = cqlService;
	}

	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#parseCQL(java.lang.String)
	 */
	@Override
	public CQLModel parseCQL(String cqlBuilder){
		return getCqlService().parseCQL(cqlBuilder);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getCQLData(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult getCQLData(String measureId) {
		return getCqlService().getCQLData(measureId);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getCQLData(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult getCQLFileData(String measureId) {
		return getCqlService().getCQLFileData(measureId);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#validateCQL(mat.model.cql.CQLModel)
	 */
	@Override
	public CQLValidationResult validateCQL(CQLModel cqlModel) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveAndModifyDefinitions(java.lang.String, mat.model.cql.CQLDefinition, mat.model.cql.CQLDefinition, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyDefinitions(String measureId, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList) {
		return getCqlService().saveAndModifyDefinitions(measureId, toBeModifiedObj, currentObj, definitionList);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveAndModifyParameters(java.lang.String, mat.model.cql.CQLParameter, mat.model.cql.CQLParameter, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyParameters(String measureId, CQLParameter toBeModifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList) {
		return getCqlService().saveAndModifyParameters(measureId, toBeModifiedObj, currentObj, parameterList);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveAndModifyFunctions(java.lang.String, mat.model.cql.CQLFunctions, mat.model.cql.CQLFunctions, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyFunctions(String measureId, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList) {
		return getCqlService().saveAndModifyFunctions(measureId, toBeModifiedObj, currentObj, functionsList);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveAndModifyCQLGeneralInfo(java.lang.String, java.lang.String)
	 */	@Override
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(
			String currentMeasureId, String context) {
		return getCqlService().saveAndModifyCQLGeneralInfo(currentMeasureId, context);
	}
	 
	 @Override
	 public SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj, CQLDefinition currentObj,
			 List<CQLDefinition> definitionList) {
		 return getCqlService().deleteDefinition(measureId, toBeDeletedObj, currentObj, definitionList);
	 }
	 
	 @Override
	 public SaveUpdateCQLResult deleteFunctions(String measureId, CQLFunctions toBeDeletedObj, CQLFunctions currentObj, 
			 List<CQLFunctions> functionsList) {
		 return getCqlService().deleteFunctions(measureId, toBeDeletedObj, currentObj, functionsList);
	 }
	 
	 @Override
	 public SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj, CQLParameter currentObj, 
			 List<CQLParameter> parameterList) {
		 return getCqlService().deleteParameter(measureId, toBeDeletedObj, currentObj, parameterList);
	 }
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getCQLDataTypeList()
	 */
	@Override
	public CQLKeywords getCQLKeywordsLists(){
		return getCqlService().getCQLKeyWords();
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getJSONObjectFromXML()
	 */
	@Override
	public String getJSONObjectFromXML(){
		return getCqlService().getJSONObjectFromXML();
	}
	
	@Override
	public GetUsedCQLArtifactsResult getUsedCqlArtifacts(String measureId) {
		return getCqlService().getUsedCQlArtifacts(measureId);
	}
	

	@Override
	public SaveUpdateCQLResult createAndSaveCQLElementLookUp(String toBeDelValueSetId, List<CQLQualityDataSetDTO> list, String measureID,
			String expProfileToAllQDM) {
		SaveUpdateCQLResult cqlResult = new SaveUpdateCQLResult();
		MeasureXmlModel exportModal = new MeasureXmlModel();
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureID);
		XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
		
		SaveUpdateCQLResult CQLFileData = getCQLFileData(measureID);
		if (CQLFileData.isSuccess()) {
			if ((CQLFileData.getCqlString() != null)
					&& !CQLFileData.getCqlString().isEmpty()) {
				SaveUpdateCQLResult ParseCQLFileData = parseCQLStringForError(CQLFileData.getCqlString());
				if(ParseCQLFileData.getCqlErrors().isEmpty()){
					try {
						String xpathforValueSet = "/measure/cqlLookUp//valueset[@id='"+ toBeDelValueSetId +"']";
						Node valueSetElements = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),xpathforValueSet);
						if (valueSetElements != null) {
							Node parentNode = valueSetElements.getParentNode();
							parentNode.removeChild(valueSetElements);
							exportModal.setXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
							exportModal.setMeasureId(measureID);
							getService().saveMeasureXml(exportModal);
						}else{
							logger.info("Unable to find the selected valueset element wit id : "+toBeDelValueSetId);
						}
					} catch (XPathExpressionException e) {
						logger.info("Error in method createAndSaveCQLElementLookUp: "+ e.getMessage());
					}
				}
				else{
					cqlResult.setCqlErrors(ParseCQLFileData.getCqlErrors());
				}
			}
		}
		return cqlResult;
	}

	@Override
	public SaveUpdateCQLResult parseCQLStringForError( String cqlFileString) {
		return this.getCqlService().parseCQLStringForError(cqlFileString);
	}

	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getCQLValusets(java.lang.String)
	 */
	@Override
	public CQLQualityDataModelWrapper getCQLValusets(String measureID) {
		CQLQualityDataModelWrapper cqlQualityDataModelWrapper = new CQLQualityDataModelWrapper();
		String expProfilestr = getDefaultExpansionIdentifier(measureID);
		if(expProfilestr != null){
			cqlQualityDataModelWrapper.setVsacExpIdentifier(expProfilestr);
		}
		
		return this.getCqlService().getCQLValusets(measureID, cqlQualityDataModelWrapper);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveCQLValueSettoMeasure(mat.model.CQLValueSetTransferObject)
	 */
	@Override
	public SaveUpdateCQLResult saveCQLValuesettoMeasure(CQLValueSetTransferObject valueSetTransferObject){
		SaveUpdateCQLResult result = getCqlService().saveCQLValuesettoMeasure(valueSetTransferObject);
		saveCQLValuesetInMeasureXml(result, valueSetTransferObject.getMeasureId());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveUserDefinedQDStoMeasure(mat.model.CQLValueSetTransferObject)
	 */
	@Override
	public SaveUpdateCQLResult saveCQLUserDefinedValuesettoMeasure(CQLValueSetTransferObject matValueSetTransferObject){
		SaveUpdateCQLResult result = getCqlService().saveCQLUserDefinedValuesettoMeasure(matValueSetTransferObject);
		saveCQLValuesetInMeasureXml(result, matValueSetTransferObject.getMeasureId());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#updateQDStoMeasure(mat.model.CQLValueSetTransferObject)
	 */
	@Override
	public SaveUpdateCQLResult updateCQLValueSetstoMeasure(
			CQLValueSetTransferObject matValueSetTransferObject) {
		
		SaveUpdateCQLResult result = getCqlService().updateCQLValueSetstoMeasure(matValueSetTransferObject);
		updateValueSetsInCQLLookUp(result.getCqlQualityDataSetDTO(), matValueSetTransferObject.getCqlQualityDataSetDTO(),
				matValueSetTransferObject.getMeasureId());
		return result;
	}
	
	/**
	 * Save CQL valueset in measure xml.
	 *
	 * @param result the result
	 */
	private void saveCQLValuesetInMeasureXml(SaveUpdateCQLResult result, String measureId){
		final String nodeName = "valueset";
		MeasureXmlModel xmlModal = new MeasureXmlModel();
		xmlModal.setMeasureId(measureId);
		xmlModal.setParentNode("/measure/cqlLookUp/valuesets");
		xmlModal.setToReplaceNode(nodeName);

		System.out.println("NEW XML " + result.getCqlString());
		xmlModal.setXml(result.getCqlString());
		
		appendAndSaveNode(xmlModal, nodeName);
	}
	
	@Override
	public SaveUpdateCQLResult saveAndModifyIncludeLibray(String measureId,
			CQLIncludeLibrary toBeModifiedObj, CQLIncludeLibrary currentObj,
			List<CQLIncludeLibrary> incLibraryList){
		return getCqlService().saveAndModifyIncludeLibray(measureId, toBeModifiedObj, currentObj, incLibraryList);
	}
}

