package mat.server;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import mat.DTO.MeasureTypeDTO;
import mat.DTO.OperatorDTO;
import mat.client.clause.clauseworkspace.model.MeasureDetailResult;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.NqfModel;
import mat.client.measure.PeriodModel;
import mat.client.measure.TransferOwnerShipModel;
import mat.client.measure.service.CQLService;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.GenericResult;
import mat.client.shared.MatContext;
import mat.client.shared.MatException;
import mat.client.umls.service.VsacApiResult;
import mat.dao.DataTypeDAO;
import mat.dao.MeasureTypeDAO;
import mat.dao.OrganizationDAO;
import mat.dao.RecentMSRActivityLogDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.ComponentMeasuresDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dao.clause.OperatorDAO;
import mat.dao.clause.QDSAttributesDAO;
import mat.model.Author;
import mat.model.CQLValueSetTransferObject;
import mat.model.ComponentMeasureTabObject;
import mat.model.DataType;
import mat.model.LockedUserInfo;
import mat.model.MatCodeTransferObject;
import mat.model.MatValueSet;
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
import mat.model.clause.ComponentMeasure;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.MeasureXML;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLCodeWrapper;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.humanreadable.cql.CQLHumanReadableGenerator;
import mat.server.humanreadable.cql.HumanReadableComponentMeasureModel;
import mat.server.humanreadable.cql.HumanReadableMeasureInformationModel;
import mat.server.humanreadable.cql.HumanReadableModel;
import mat.server.model.MatUserDetails;
import mat.server.service.InvalidValueSetDateException;
import mat.server.service.MeasureLibraryService;
import mat.server.service.MeasurePackageService;
import mat.server.service.UserService;
import mat.server.service.impl.MatContextServiceUtil;
import mat.server.service.impl.MeasureAuditServiceImpl;
import mat.server.service.impl.PatientBasedValidator;
import mat.server.service.impl.XMLMarshalUtil;
import mat.server.util.CQLUtil;
import mat.server.util.ExportSimpleXML;
import mat.server.util.MATPropertiesService;
import mat.server.util.MeasureUtility;
import mat.server.util.UuidUtility;
import mat.server.util.XmlProcessor;
import mat.server.validator.measure.CompositeMeasureValidator;
import mat.shared.CQLValidationResult;
import mat.shared.CompositeMeasureValidationResult;
import mat.shared.ConstantMessages;
import mat.shared.DateStringValidator;
import mat.shared.DateUtility;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.MeasureSearchModel;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.StringUtility;
import mat.shared.UUIDUtilClient;
import mat.shared.cql.error.InvalidLibraryException;
import mat.shared.error.AuthenticationException;
import mat.shared.error.measure.DeleteMeasureException;
import mat.shared.model.util.MeasureDetailsUtil;
import mat.shared.validator.measure.ManageCompositeMeasureModelValidator;
import mat.shared.validator.measure.ManageMeasureModelValidator;

@Service
public class MeasureLibraryServiceImpl implements MeasureLibraryService {

	private static final int NESTED_CLAUSE_DEPTH = 10;

	private static final Log logger = LogFactory.getLog(MeasureLibraryServiceImpl.class);

	private static final String MEASURE = "measure";

	private static final String MEASURE_DETAILS = "measureDetails";

	private static final String XPATH_EXPRESSION_COMPONENT_MEASURES = "/measure//measureDetails//componentMeasures";

	private static final String XPATH_EXPRESSION_STEWARD = "/measure//measureDetails//steward";

	private static final String XPATH_EXPRESSION_DEVELOPERS = "/measure//measureDetails//developers";

	private static final String INSTANCE = "instance";

	private static final String INSTANCE_OF = "instanceOf";

	private static final String NAME = "name";

	private static final String DATATYPE = "datatype";

	private static final String ID = "id";

	private static final String OID = "oid";

	private static final String TAXONOMY = "taxonomy";

	private static final String VERSION = "version";

	private static final String CODE_SYSTEM_NAME = "codeSystemName";

	private static final String SUPP_DATA_ELEMENT = "suppDataElement";

	private static final String EFFECTIVE_DATE = "effectiveDate";

	private static final String EXPANSION_IDENTIFIER = "expansionIdentifier";

	private static final String VSAC_EXP_IDENTIFIER = "vsacExpIdentifier";

	private static final String MAPPING_FAILED = "Mapping Failed";

	private static final String UNMARSHALLING_FAILED = "Unmarshalling Failed";

	private static final String OTHER_EXCEPTION = "Other Exception";

	private static final String CQL_LOOKUP = "cqlLookUp";
	
	private static final String QDM_MAPPING = "QualityDataModelMapping.xml";

	private String currentReleaseVersion;

	private boolean isMeasureCreated;

	private Comparator<QDSAttributes> attributeComparator = (arg0, arg1) -> arg0.getName().toLowerCase().compareTo(arg1.getName().toLowerCase());

	@Autowired
	private ApplicationContext context;

	@Autowired
	private MeasureDAO measureDAO;

	@Autowired
	private RecentMSRActivityLogDAO recentMSRActivityLogDAO;

	@Autowired
	private MeasureTypeDAO measureTypeDAO;

	@Autowired
	private OperatorDAO operatorDAO;

	@Autowired
	private OrganizationDAO organizationDAO;

	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;

	@Autowired
	private CQLLibraryService cqlLibraryService;
	
	@Autowired
	private MeasureExportDAO measureExportDAO; 

	javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();

	@Autowired
	private CQLService cqlService;
	
	@Autowired
	private MeasureAuditServiceImpl auditService; 
	
	@Autowired
	private PackageServiceImpl packageService;
	
	@Autowired
	private CompositeMeasureValidator compositeMeasureValidator;
	
	@Autowired
	private MeasurePackageService measurePackageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private ComponentMeasuresDAO componentMeasuresDAO;
	
	@Autowired
	private LoginServiceImpl loginService; 
	
	@Autowired
	private CQLHumanReadableGenerator humanReadableGenerator;
	
	@Override
	public final String appendAndSaveNode(final MeasureXmlModel measureXmlModel, final String nodeName) {
		String result = "";
		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())
				&& StringUtils.isNotBlank(nodeName)) {
			 result = callAppendNode(xmlModel, measureXmlModel.getXml(), nodeName,
					measureXmlModel.getParentNode());
			xmlModel.setXml(result);
			measurePackageService.saveMeasureXml(xmlModel);
		}
		return result;
	}

	@Override
	public HashMap<String, String> checkAndDeleteSubTree(String measureId, String subTreeUUID) {
		logger.info("Inside checkAndDeleteSubTree Method for measure Id " + measureId);
		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
		HashMap<String, String> removeUUIDMap = new HashMap<>();
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
			if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
				XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
				try {
					NodeList subTreeRefNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
							"//subTreeRef[@id='" + subTreeUUID + "']");
					if (subTreeRefNodeList.getLength() > 0) {
						xmlModel.setXml(null);
						return removeUUIDMap;
					}

					Node subTreeNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
							"/measure/subTreeLookUp/subTree[@uuid='" + subTreeUUID + "']");
					NodeList subTreeOccNode = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
							"/measure/subTreeLookUp/subTree[@instanceOf='" + subTreeUUID + "']");
					if (subTreeNode != null) {
						Node parentNode = subTreeNode.getParentNode();
						String name = subTreeNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME).getNodeValue();
						String uuid = subTreeNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
						String mapValue = name + "~" + uuid;
						removeUUIDMap.put(uuid, mapValue);
						parentNode.removeChild(subTreeNode);
					}
					if (subTreeOccNode.getLength() > 0) {
						Set<Node> targetOccurenceElements = new HashSet<>();
						for (int i = 0; i < subTreeOccNode.getLength(); i++) {
							Node node = subTreeOccNode.item(i);
							targetOccurenceElements.add(node);
						}

						for (Node occNode : targetOccurenceElements) {
							String name = "Occurrence "
									+ occNode.getAttributes().getNamedItem(INSTANCE).getNodeValue() + " of ";
							name = name + occNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME).getNodeValue();
							String uuid = occNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
							String mapValue = name + "~" + uuid;
							removeUUIDMap.put(uuid, mapValue);
							occNode.getParentNode().removeChild(occNode);
						}
					}
					xmlModel.setXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
					measurePackageService.saveMeasureXml(xmlModel);
				} catch (XPathExpressionException e) {
					logger.debug("checkAndDeleteSubTree:" + e.getMessage());
				}
			}
		}
		return removeUUIDMap;
	}

	@Override
	public boolean isSubTreeReferredInLogic(String measureId, String subTreeUUID) {
		logger.info("Inside isSubTreeReferredInLogic Method for measure Id " + measureId);
		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
		if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			try {
				NodeList subTreeRefNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
						"//subTreeRef[@id='" + subTreeUUID + "']");
				NodeList subTreeOccNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
						"//subTree[@instanceOf='" + subTreeUUID + "']");
				boolean isOccurrenceUsed = false;
				for (int i = 0; i < subTreeOccNodeList.getLength(); i++) {
					Node node = subTreeOccNodeList.item(i);
					if (node.hasAttributes()) {
						String occNodeUUID = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
						NodeList subTreeOccRefNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
								"//subTreeRef[@id='" + occNodeUUID + "']");
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
				logger.debug("isSubTreeReferredInLogic:" + e.getMessage());
			}
		}

		return false;
	}

	@Override
	public boolean isQDMVariableEnabled(String measureId, String subTreeUUID) {
		logger.info("Inside isQDMVariableEnabled Method for measure Id " + measureId);

		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
		if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			try {
				NodeList subTreeOccNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
						"//subTree[@instanceOf='" + subTreeUUID + "']");
				if ((subTreeOccNodeList.getLength() > 0)) {
					return true;
				}
			} catch (XPathExpressionException e) {
				logger.debug("isQDMVariableEnabled:" + e.getMessage());
			}
		}

		return false;
	}

	@Override
	public SortedClauseMapResult saveSubTreeInMeasureXml(MeasureXmlModel measureXmlModel, String nodeNameWithSpaces,
			String nodeUUID) {

		// change multiple spaces into one space for the nodeName
		String nodeName = nodeNameWithSpaces.replaceAll("( )+", " ");

		logger.info("Inside saveSubTreeInMeasureXml Method for measure Id " + measureXmlModel.getMeasureId() + " .");
		SortedClauseMapResult clauseMapResult = new SortedClauseMapResult();
		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureXmlModel.getMeasureId());

		if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			try {
				// take the spaces out of the xml for displayNme xml attribute
				String normalizedXml = XmlProcessor.normalizeNodeForSpaces(measureXmlModel.getXml(),
						"//subTree/@displayName");
				measureXmlModel.setXml(normalizedXml);

				Node subTreeLookUpNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
						measureXmlModel.getParentNode());
				// Add subTreeLookUp node if not available in MeasureXml.
				if (subTreeLookUpNode == null) {
					logger.info(
							"Adding subTreeNodeLookUp Node for measure Id " + measureXmlModel.getMeasureId() + " .");
					String xPathSupplementalDataElement = "/measure/supplementalDataElements";
					Node supplementaDataElementsElement = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
							xPathSupplementalDataElement);
					String tagNameSubTeeLookUp = "subTreeLookUp";
					Element subTreeLookUpElement = xmlProcessor.getOriginalDoc().createElement(tagNameSubTeeLookUp);
					((Element) supplementaDataElementsElement.getParentNode()).insertBefore(subTreeLookUpElement,
							supplementaDataElementsElement.getNextSibling());
					xmlProcessor.setOriginalXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
				}
				// If Node already exist's and its a update then existing node
				// will be removed from Parent Node and updated node will be added.
				String xPathForSubTree = "/measure/subTreeLookUp/subTree[@uuid='" + nodeUUID + "']";
				NodeList subTreeNodeForUUID = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForSubTree);
				if (subTreeNodeForUUID.getLength() == 0) {
					xmlProcessor.appendNode(measureXmlModel.getXml(), measureXmlModel.getToReplaceNode(),
							measureXmlModel.getParentNode());
				} else {
					Node newNode = subTreeNodeForUUID.item(0);
					if (newNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue().equals(nodeUUID)) {
						logger.info("Replacing SubTreeNode for UUID " + nodeUUID + " .");
						xmlProcessor.removeFromParent(newNode);
						xmlProcessor.appendNode(measureXmlModel.getXml(), measureXmlModel.getToReplaceNode(),
								measureXmlModel.getParentNode());

						// In case the name of the subTree has changed we need
						// to make sure to find all the subTreeRef tags and
						// change the name in them as well.
						NodeList subTreeRefNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
								"//subTreeRef[@id='" + nodeUUID + "']");
						NodeList subTreeOccRefNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
								"//subTreeRef[@instanceOf='" + nodeUUID + "']");
						String xPathForSubTreeOcc = "/measure/subTreeLookUp/subTree[@instanceOf='" + nodeUUID + "']";
						NodeList subTreeOccNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
								xPathForSubTreeOcc);
						if (subTreeRefNodeList.getLength() > 0) {
							for (int k = 0; k < subTreeRefNodeList.getLength(); k++) {
								Node subTreeRefNode = subTreeRefNodeList.item(k);
								subTreeRefNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME).setNodeValue(nodeName);
							}
						}
						if (subTreeOccRefNodeList.getLength() > 0) {
							for (int k = 0; k < subTreeOccRefNodeList.getLength(); k++) {
								Node subTreeRefOccNode = subTreeOccRefNodeList.item(k);
								subTreeRefOccNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME).setNodeValue(nodeName);
							}
						}

						if (subTreeOccNodeList.getLength() > 0) {
							for (int k = 0; k < subTreeOccNodeList.getLength(); k++) {
								Node subTreeOccNode = subTreeOccNodeList.item(k);
								subTreeOccNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME).setNodeValue(nodeName);
							}
						}
					}
				}
				xmlProcessor.setOriginalXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));

				measureXmlModel.setXml(xmlProcessor.getOriginalXml());
				clauseMapResult.setMeasureXmlModel(measureXmlModel);
				measurePackageService.saveMeasureXml(measureXmlModel);
			} catch (XPathExpressionException | SAXException | IOException e) {
				logger.debug("saveSubTreeInMeasureXml:" + e.getMessage());
			}
		}
		logger.info("End saveSubTreeInMeasureXml Method for measure Id " + measureXmlModel.getMeasureId() + " .");
		clauseMapResult.setClauseMap(getSortedClauseMap(measureXmlModel.getMeasureId()));
		return clauseMapResult;
	}

	@Override
	public SortedClauseMapResult saveSubTreeOccurrence(MeasureXmlModel measureXmlModel, String nodeName,
			String nodeUUID) {
		logger.info("Inside saveSubTreeOccurrence Method for measure Id " + measureXmlModel.getMeasureId() + " .");
		SortedClauseMapResult sortedClauseMapResult = new SortedClauseMapResult();
		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		int codeASCIIStart = 65;
		int codeASCIIEnd = 90;
		int occurrenceCount = codeASCIIStart;
		if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			try {
				String xPathForSubTree = "/measure/subTreeLookUp/subTree[@instanceOf='" + nodeUUID + "']";
				NodeList subTreeNodeForUUID = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPathForSubTree);
				if (subTreeNodeForUUID.getLength() == 0) {
					XmlProcessor processor = new XmlProcessor(measureXmlModel.getXml());
					Node subTreeNode = processor.findNode(processor.getOriginalDoc(), "/subTree");
					Attr instanceAttrNode = processor.getOriginalDoc().createAttribute(INSTANCE);
					instanceAttrNode.setNodeValue("" + (char) occurrenceCount);
					subTreeNode.getAttributes().setNamedItem(instanceAttrNode);
					Attr instanceAttrNodeOfAttrNode = processor.getOriginalDoc().createAttribute(INSTANCE_OF);
					instanceAttrNodeOfAttrNode.setNodeValue(nodeUUID);
					subTreeNode.getAttributes().setNamedItem(instanceAttrNodeOfAttrNode);
					measureXmlModel.setXml(processor.transform(subTreeNode));
					xmlProcessor.appendNode(measureXmlModel.getXml(), measureXmlModel.getToReplaceNode(),
							measureXmlModel.getParentNode());
				} else {
					for (int i = 0; i < subTreeNodeForUUID.getLength(); i++) {
						Node node = subTreeNodeForUUID.item(i);
						String instanceValue = node.getAttributes().getNamedItem(INSTANCE).getNodeValue();
						Character text = instanceValue.charAt(0);
						int newOcc = (text);
						if (newOcc >= occurrenceCount) {
							occurrenceCount = ++newOcc;
						}
					}
					if (occurrenceCount < codeASCIIEnd) {
						XmlProcessor processor = new XmlProcessor(measureXmlModel.getXml());
						Node subTreeNode = processor.findNode(processor.getOriginalDoc(), "/subTree");
						Attr instanceAttrNode = processor.getOriginalDoc().createAttribute(INSTANCE);
						instanceAttrNode.setNodeValue("" + (char) occurrenceCount);
						subTreeNode.getAttributes().setNamedItem(instanceAttrNode);
						Attr instanceAttrNodeOfAttrNode = processor.getOriginalDoc().createAttribute(INSTANCE_OF);
						instanceAttrNodeOfAttrNode.setNodeValue(nodeUUID);
						subTreeNode.getAttributes().setNamedItem(instanceAttrNodeOfAttrNode);
						measureXmlModel.setXml(processor.transform(subTreeNode));
						xmlProcessor.appendNode(measureXmlModel.getXml(), measureXmlModel.getToReplaceNode(),
								measureXmlModel.getParentNode());
					}
				}
				xmlProcessor.setOriginalXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
				measureXmlModel.setXml(xmlProcessor.getOriginalXml());
				sortedClauseMapResult.setMeasureXmlModel(measureXmlModel);
				measurePackageService.saveMeasureXml(measureXmlModel);
			} catch (XPathExpressionException | SAXException | IOException e) {
				logger.debug("saveSubTreeOccurrence:" + e.getMessage());
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
		} catch (SAXException |IOException e) {
			logger.debug("callAppendNode:" + e.getMessage());
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
		// empty method body
	}


	/**
	 * Append cql parameters.
	 *
	 * @param xmlProcessor
	 *            the xml processor
	 */
	public void checkForDefaultCQLDefinitionsAndAppend(XmlProcessor xmlProcessor) {

		NodeList defaultCQLDefNodeList = findDefaultDefinitions(xmlProcessor);

		if (defaultCQLDefNodeList != null && defaultCQLDefNodeList.getLength() == 4) {
			logger.info("All Default parameter elements present in the measure.");
			return;
		}

		String defStr = getCqlService().getSupplementalDefinitions();
		try {
			xmlProcessor.appendNode(defStr, "definition", "/measure/cqlLookUp/definitions");

			NodeList supplementalDefnNodes = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
					"/measure/cqlLookUp/definitions/definition[@supplDataElement='true']");

			if (supplementalDefnNodes != null) {
				for (int i = 0; i < supplementalDefnNodes.getLength(); i++) {
					Node supplNode = supplementalDefnNodes.item(i);
					supplNode.getAttributes().getNamedItem(ID).setNodeValue(UUIDUtilClient.uuid());
				}
			}
		} catch (SAXException | IOException| XPathExpressionException e) {
			logger.debug("checkForDefaultCQLDefinitionsAndAppend:" + e.getMessage());
		}
	}

	/**
	 * This method will look into XPath "/measure/cqlLookUp/definitions/" and
	 * try and NodeList for Definitions with the following names; 'SDE
	 * Ethnicity','SDE Payer','SDE Race','SDE Sex'.
	 * 
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
				logger.debug("findDefaultDefinitions:" + e.getMessage());
			}
		}
		return returnNodeList;
	}


	/**
	 * Check for default cql parameters and append.
	 *
	 * @param xmlProcessor
	 *            the xml processor
	 */
	public void checkForDefaultCQLParametersAndAppend(XmlProcessor xmlProcessor) {

		List<String> missingDefaultCQLParameters = xmlProcessor.checkForDefaultParameters();

		if (missingDefaultCQLParameters.isEmpty()) {
			logger.info("All Default parameter elements present in the measure.");
			return;
		}
		logger.info("Found the following Default parameter elements missing:" + missingDefaultCQLParameters);
		CQLParameter parameter = new CQLParameter();

		parameter.setId(UUID.randomUUID().toString());
		parameter.setName(CQLWorkSpaceConstants.CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_NAME);
		parameter.setLogic(CQLWorkSpaceConstants.CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_LOGIC);
		parameter.setReadOnly(true);
		String parStr = getCqlService().createParametersXML(parameter);

		try {
			xmlProcessor.appendNode(parStr, "parameter", "/measure/cqlLookUp/parameters");
		} catch (SAXException | IOException e) {
			logger.debug("checkForDefaultCQLParametersAndAppend:" + e.getMessage());
		}

	}

	/**
	 * Method called when Measure Details Clone operation is done or Drafting of
	 * a version measure is done.
	 * 
	 * @param creatingDraft
	 *            the creating draft
	 * @param oldMeasureId
	 *            the old measure id
	 * @param clonedMeasureId
	 *            the cloned measure id
	 */
	@Override
	public final void cloneMeasureXml(final boolean creatingDraft, final String oldMeasureId,
			final String clonedMeasureId) {
		logger.info("In MeasureLibraryServiceImpl.cloneMeasureXml() method. Clonig for Measure: " + oldMeasureId);
		ManageMeasureDetailModel measureDetailModel = null;
		if (creatingDraft) {
			measureDetailModel = getMeasure(oldMeasureId);// get the
			// measureDetailsmodel object for which draft have to be created..
			Measure measure = measurePackageService.getById(clonedMeasureId);
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
		measureXmlModel.setXml(createXml(measureDetailModel));
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
	private void convertAddlXmlElementsToModel(final ManageMeasureDetailModel manageMeasureDetailModel,
			final Measure measure) {
		logger.info("In easureLibraryServiceImpl.convertAddlXmlElementsToModel()");
		manageMeasureDetailModel.setId(measure.getId());
		manageMeasureDetailModel.setCalenderYear(manageMeasureDetailModel.getPeriodModel().isCalenderYear());
		if (!manageMeasureDetailModel.getPeriodModel().isCalenderYear()) {
			manageMeasureDetailModel.setMeasFromPeriod(manageMeasureDetailModel.getPeriodModel() != null
					? manageMeasureDetailModel.getPeriodModel().getStartDate() : null);
			manageMeasureDetailModel.setMeasToPeriod(manageMeasureDetailModel.getPeriodModel() != null
					? manageMeasureDetailModel.getPeriodModel().getStopDate() : null);
		}
		manageMeasureDetailModel.setEndorseByNQF(StringUtils.isNotBlank(manageMeasureDetailModel.getEndorsement()));
		manageMeasureDetailModel.setOrgVersionNumber(MeasureUtility.formatVersionText(measure.getRevisionNumber(),
				String.valueOf(measure.getVersionNumber())));
		manageMeasureDetailModel.setVersionNumber(
				MeasureUtility.getVersionText(manageMeasureDetailModel.getOrgVersionNumber(), measure.isDraft()));
		manageMeasureDetailModel.setFinalizedDate(DateUtility.convertDateToString(measure.getFinalizedDate()));
		manageMeasureDetailModel.setDraft(measure.isDraft());
		manageMeasureDetailModel.setValueSetDate(DateUtility.convertDateToStringNoTime(measure.getValueSetDate()));
		manageMeasureDetailModel.setNqfId(manageMeasureDetailModel.getNqfModel() != null
				? manageMeasureDetailModel.getNqfModel().getExtension() : null);
		manageMeasureDetailModel.seteMeasureId(measure.geteMeasureId());
		manageMeasureDetailModel.setMeasureOwnerId(measure.getOwner().getId());
		logger.info("Exiting easureLibraryServiceImpl.convertAddlXmlElementsToModel() method..");
	}
	
	private ManageMeasureDetailModel convertXMLToModel(String xml, Measure measure) {

		logger.info("In MeasureLibraryServiceImpl.convertXMLToModel()");

		ManageMeasureDetailModel details = null;
	
		try {
			if (StringUtils.isNotBlank(xml)) {
				xml = new XmlProcessor(xml).getXmlByTagName(MEASURE_DETAILS);
			}

			details = (xml == null) ? createModelForNoXML(measure) : createModelFromXML(xml, measure);
			
		} catch (Exception e) {
			logger.error("Exception in convertXMLToModel: " + e);
		}
		return details;
	
	}

	private ManageMeasureDetailModel createModelForNoXML(final Measure measure) {
		ManageMeasureDetailModel details = null;
		if(BooleanUtils.isTrue(measure.getIsCompositeMeasure())) {
			details = new ManageCompositeMeasureDetailModel();
			createMeasureDetailsModelForCompositeMeasure((ManageCompositeMeasureDetailModel) details, measure);
		} else {
			details = new ManageMeasureDetailModel();
			createMeasureDetailsModelFromMeasure(details, measure);
		}
		
		return details;	
	}
	
	private void createMeasureDetailsModelForCompositeMeasure(final ManageCompositeMeasureDetailModel model, final Measure measure) {
		createMeasureDetailsModelFromMeasure(model, measure);
		model.setCompositeScoringMethod(measure.getCompositeScoring());
		model.setQdmVersion(measure.getQdmVersion());
		List<ManageMeasureSearchModel.Result> componentMeasuresSelectedList = new ArrayList<>();
		Map<String, String> aliasMapping = new HashMap<>();
		boolean isSuperUser = LoggedInUserUtil.getLoggedInUserRole().equalsIgnoreCase(SecurityRole.SUPER_USER_ROLE);
		for(ComponentMeasure component : measure.getComponentMeasures()) {
			componentMeasuresSelectedList.add(buildSearchModelResultObjectFromMeasureId(component.getComponentMeasure().getId(), isSuperUser));			
		}
		model.setAppliedComponentMeasures(componentMeasuresSelectedList);
		model.setComponentMeasuresSelectedList(componentMeasuresSelectedList);
		
		for(ComponentMeasure component : measure.getComponentMeasures() ) {
			aliasMapping.put(component.getComponentMeasure().getId(), component.getAlias());
		}
		
		model.setAliasMapping(aliasMapping);
	}
	
	private ManageMeasureDetailModel createModelFromXML(String xml, final Measure measure) {
		ManageMeasureDetailModel result = null;
		try {
			if(BooleanUtils.isTrue(measure.getIsCompositeMeasure())) {
				result = createObjectFromXMLMapping(xml, "CompositeMeasureDetailsModelMapping.xml", ManageCompositeMeasureDetailModel.class);
				createMeasureDetailsModelForCompositeMeasure((ManageCompositeMeasureDetailModel) result, measure);
			} else {
				result = createObjectFromXMLMapping(xml, "MeasureDetailsModelMapping.xml", ManageMeasureDetailModel.class);
			}
			convertAddlXmlElementsToModel(result, measure);
		} catch(Exception e) {
			logger.error("Exception in createModelFromXML: " + e);
		}
		return result;
	}

	private ManageMeasureDetailModel createObjectFromXMLMapping(String xml, String xmlMapping, Class<?> clazz) {

		Object result = null;
		XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
		try {
			result = xmlMarshalUtil.convertXMLToObject(xmlMapping, xml, clazz);
			
		} catch (MarshalException | ValidationException | MappingException | IOException e) {
			logger.error("Failed to load MeasureDetailsModelMapping.xml" + e.getMessage());
			e.printStackTrace();
		}
		return (ManageMeasureDetailModel) result;

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
		if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
			xml = new XmlProcessor(xmlModel.getXml()).getXmlByTagName(MEASURE);
		}
		try {
			if (xml != null) {
				XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
				details = (QualityDataModelWrapper) xmlMarshalUtil.convertXMLToObject(QDM_MAPPING, xml, QualityDataModelWrapper.class);
				logger.info("unmarshalling complete..elementlookup" + details.getQualityDataDTO().get(0).getCodeListName());
			}

		} catch (IOException e) {
			logger.info("Failed to load QualityDataModelMapping.xml" + e.getMessage());
		} catch (MappingException e) {
			logger.info(MAPPING_FAILED + e.getMessage());
		} catch(MarshalException e) {
			logger.info(UNMARSHALLING_FAILED + e.getMessage());
		}  catch(Exception e) {
			logger.info(OTHER_EXCEPTION + e.getMessage());
		}
		return details;
	}

	@Override
	public final void createAndSaveElementLookUp(final List<QualityDataSetDTO> list, final String measureID,
			String expProfileToAllQDM) {
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		wrapper.setQualityDataDTO(list);
		if ((expProfileToAllQDM != null) && !expProfileToAllQDM.isEmpty()) {
			wrapper.setVsacExpIdentifier(expProfileToAllQDM);
		}
		
		String xmlString = null;
		String stream = createNewXML(QDM_MAPPING, wrapper);

		if (StringUtils.isNotBlank(stream)) {
			int startIndex = stream.indexOf("<elementLookUp", 0);
			int lastIndex = stream.indexOf("</measure>", startIndex);
			xmlString = stream.substring(startIndex, lastIndex);
		} 
		
		String nodeName = "elementLookUp";

		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(measureID);
		exportModal.setParentNode("/measure");
		exportModal.setToReplaceNode("elementLookUp");

		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureID);
		if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())
				&& StringUtils.isNotBlank(nodeName)) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			String result = xmlProcessor.replaceNode(xmlString, nodeName, MEASURE);
			exportModal.setXml(result);
			measurePackageService.saveMeasureXml(exportModal);
		}

	}

	@Override
	public final void createAndSaveCQLLookUp(final List<QualityDataSetDTO> list, final String measureID,
			String expProfileToAllQDM) {
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		wrapper.setQualityDataDTO(list);
		if ((expProfileToAllQDM != null) && !expProfileToAllQDM.isEmpty()) {
			wrapper.setVsacExpIdentifier(expProfileToAllQDM);
		}

		String xmlString = null;
		String stream = createNewXML(QDM_MAPPING, wrapper);

		if (StringUtils.isNotBlank(stream)) {
			int startIndex = stream.indexOf("<elementLookUp", 0);
			int lastIndex = stream.indexOf("</measure>", startIndex);
			xmlString = stream.substring(startIndex, lastIndex);
		} 
		
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(measureID);
		exportModal.setParentNode("/measure");
		exportModal.setToReplaceNode(CQL_LOOKUP);

		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureID);
		if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			String result = xmlProcessor.replaceNode(xmlString, CQL_LOOKUP, MEASURE);
			exportModal.setXml(result);
			measurePackageService.saveMeasureXml(exportModal);
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
	public final String createMeasureDetailsXml(final ManageMeasureDetailModel measureDetailModel,
			final Measure measure) {
		logger.info("In MeasureLibraryServiceImpl.createMeasureDetailsXml()");
		setAdditionalAttrsForMeasureXml(measureDetailModel, measure);
		logger.info("creating XML from Measure Details Model");
		return createXml(measureDetailModel);
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
	private MeasureXmlModel createMeasureXmlModel(final ManageMeasureDetailModel manageMeasureDetailModel,
			final Measure measure, final String replaceNode, final String parentNode) {
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		measureXmlModel.setMeasureId(measure.getId());
		measureXmlModel.setXml(createMeasureDetailsXml(manageMeasureDetailModel, measure));
		measureXmlModel.setToReplaceNode(replaceNode);
		measureXmlModel.setParentNode(parentNode);
		return measureXmlModel;
	}
	
	private String createNewXML(String mapping, Object object) {
		String stream = null;
		try {
			final XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
			stream = xmlMarshalUtil.convertObjectToXML(mapping, object);
			
		} catch (MarshalException | ValidationException | IOException | MappingException e) {
			logger.info("Exception in createExpressionXML: " + e);
			e.printStackTrace();
		}

		logger.info("Exiting ManageCodeListServiceImpl.createXml()");
		return stream;
	}

	/**
	 * Creates the xml.
	 * 
	 * @param measureDetailModel
	 *            the measure detail model
	 * @return the byte array output stream
	 */
	private String createXml(final ManageMeasureDetailModel measureDetailModel) {
		logger.info("In MeasureLibraryServiceImpl.createXml()");
		String mapping = "MeasureDetailsModelMapping.xml";
		if(measureDetailModel instanceof ManageCompositeMeasureDetailModel) {
			mapping = "CompositeMeasureDetailsModelMapping.xml";
		}
		return createNewXML(mapping, measureDetailModel);
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
	 * @return {@link ManageMeasureSearchModel.Result}.
	 */
	private ManageMeasureSearchModel.Result extractMeasureSearchModelDetail(final String currentUserId,
			final boolean isSuperUser, final MeasureShareDTO dto) {
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
		Measure measure = measureDAO.find(dto.getMeasureId());
		detail.setName(dto.getMeasureName());
		detail.setShortName(dto.getShortName());
		detail.setScoringType(dto.getScoringType());
		detail.setStatus(dto.getStatus());
		detail.setId(dto.getMeasureId());
		detail.setStatus(dto.getStatus());
		detail.seteMeasureId(dto.geteMeasureId());
		detail.setPatientBased(dto.isPatientBased());
		detail.setQdmVersion(measure.getQdmVersion());
		detail.setIsComposite(measure.getIsCompositeMeasure());
		
		String measureReleaseVersion = StringUtils.trimToEmpty(measure.getReleaseVersion());
		
		detail.setClonable((isOwner || isSuperUser) && !measure.getIsCompositeMeasure() && !(measureReleaseVersion.length() == 0 || measureReleaseVersion.startsWith("v4")
				|| measureReleaseVersion.startsWith("v3")));

		detail.setEditable(MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, dto.getMeasureId()));
		
		detail.setExportable(dto.isPackaged());
		detail.setHqmfReleaseVersion(measure.getReleaseVersion());
		detail.setSharable(isOwner || isSuperUser);
		detail.setMeasureLocked(dto.isLocked());
		detail.setLockedUserInfo(dto.getLockedUserInfo());
		User user = userService.getById(dto.getOwnerUserId());
		detail.setOwnerfirstName(user.getFirstName());
		detail.setOwnerLastName(user.getLastName());
		detail.setOwnerEmailAddress(user.getEmailAddress());
		detail.setDraft(dto.isDraft());
		String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(dto.getVersion(),
				dto.getRevisionNumber(), dto.isDraft());
		detail.setVersion(formattedVersion);
		detail.setFinalizedDate(dto.getFinalizedDate());
		detail.setMeasureSetId(dto.getMeasureSetId());
		detail.setDraftable(dto.isDraftable());
		detail.setVersionable(dto.isVersionable());
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
		return measurePackageService.findOutMaximumVersionNumber(measureSetId);
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
		return measurePackageService.findOutVersionNumber(measureId, measureSetId);
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
			XPATH_EXPRESSION = XPATH_EXPRESSION.concat("'").concat(dataSetDTO.getUuid())
					.concat("' or /measure//subTree//elementRef/attribute/@qdmUUID= '").concat(dataSetDTO.getUuid())
					.concat("' or /measure/supplementalDataElements//@id='").concat(dataSetDTO.getUuid())
					.concat("' or /measure/measureDetails/itemCount//@id='").concat(dataSetDTO.getUuid())
					.concat("' or /measure//measureGrouping//packageClause//elementRef/@id='")
					.concat(dataSetDTO.getUuid()).concat("'");

			try {
				Boolean isUsed = (Boolean) xPath.evaluate(XPATH_EXPRESSION,
						processor.getOriginalDoc().getDocumentElement(), XPathConstants.BOOLEAN);
				dataSetDTO.setUsed(isUsed);
			} catch (XPathExpressionException e) {
				logger.debug("findUsedQDMs:" + e.getMessage());
			}
		}

		return appliedQDMList;
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
		return attrs;
	}

	/**
	 * Check if qdm data type is present.
	 *
	 * @param dataTypeName
	 *            the data type name
	 * @return true, if successful
	 */
	public boolean checkIfQDMDataTypeIsPresent(String dataTypeName) {
		boolean checkIfDataTypeIsPresent = false;
		DataTypeDAO dataTypeDAO = (DataTypeDAO) context.getBean("dataTypeDAO");
		DataType dataType = dataTypeDAO.findByDataTypeName(dataTypeName);
		if (dataType != null) {
			checkIfDataTypeIsPresent = true;
		}
		return checkIfDataTypeIsPresent;
	}

	/**
	 * Method to retrieve all Recently searched measure's for the logged in User
	 * from 'Recent_MSR_Activity_Log' table.
	 * 
	 * @param userId
	 *            - String logged in user id.
	 * @return {@link ManageMeasureSearchModel}.
	 **/
	@Override
	public ManageMeasureSearchModel getAllRecentMeasureForUser(String userId) {
		// Call to fetch
		ArrayList<RecentMSRActivityLog> recentMeasureActivityList = (ArrayList<RecentMSRActivityLog>) recentMSRActivityLogDAO
				.getRecentMeasureActivityLog(userId);
		ManageMeasureSearchModel manageMeasureSearchModel = new ManageMeasureSearchModel();
		List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<>();
		manageMeasureSearchModel.setData(detailModelList);

		boolean isSuperUser = LoggedInUserUtil.getLoggedInUserRole().equalsIgnoreCase(SecurityRole.SUPER_USER_ROLE);
		for (RecentMSRActivityLog activityLog : recentMeasureActivityList) {
			ManageMeasureSearchModel.Result detail = buildSearchModelResultObjectFromMeasureId(activityLog.getMeasureId(), isSuperUser);			
			detailModelList.add(detail);
		}
		return manageMeasureSearchModel;
	}
	
	private ManageMeasureSearchModel.Result buildSearchModelResultObjectFromMeasureId(String measureId, boolean isSuperUser){
		Measure measure = measureDAO.find(measureId);
		List<Measure> measureList = new ArrayList<>();
		measureList.add(measure);
		List<Measure> measuresInSet = measureDAO.getAllMeasuresInSet(measureList);
		ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
		detail.setName(measure.getDescription());
		detail.setShortName(measure.getaBBRName());
		detail.setId(measure.getId());
		detail.setDraft(measure.isDraft());
		detail.setIsComposite(measure.getIsCompositeMeasure());
		detail.setQdmVersion(measure.getQdmVersion());
		detail.setExportable(measure.getExportedDate() != null); // to show export icon.
		detail.setHqmfReleaseVersion(measure.getReleaseVersion());
		String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(measure.getVersion(),
				measure.getRevisionNumber(), measure.isDraft());
		detail.setVersion(formattedVersion);
		detail.setFinalizedDate(measure.getFinalizedDate());
		detail.setOwnerfirstName(measure.getOwner().getFirstName());
		detail.setOwnerLastName(measure.getOwner().getLastName());
		detail.setOwnerEmailAddress(measure.getOwner().getEmailAddress());
		detail.setMeasureSetId(measure.getMeasureSet().getId());
		detail.setScoringType(measure.getMeasureScoring());
		boolean isLocked = measureDAO.isMeasureLocked(measure.getId());
		detail.setMeasureLocked(isLocked);
		boolean isEditable = MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measure.getId());
		detail.setEditable(isEditable);
		detail.setPatientBased(measure.getPatientBased());
		boolean isOwner = measure.getOwner().getId().equals(LoggedInUserUtil.getLoggedInUser());
		String measureReleaseVersion = StringUtils.trimToEmpty(measure.getReleaseVersion());
		boolean isCLonable = (isOwner || isSuperUser) && !measure.getIsCompositeMeasure() && !(measureReleaseVersion.length() == 0 || measureReleaseVersion.startsWith("v4")
				|| measureReleaseVersion.startsWith("v3"));
		detail.setClonable(isCLonable);
		detail.setSharable(isOwner || isSuperUser);
		boolean isEditableForVersion = MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measure.getId(), false);
		if(isEditableForVersion && !isLocked) {
			caclulateVersionAndDraft(detail, measuresInSet);
		}
		if (isLocked && (measure.getLockedUser() != null)) {
			LockedUserInfo lockedUserInfo = new LockedUserInfo();
			lockedUserInfo.setUserId(measure.getLockedUser().getId());
			lockedUserInfo.setEmailAddress(measure.getLockedUser().getEmailAddress());
			lockedUserInfo.setFirstName(measure.getLockedUser().getFirstName());
			lockedUserInfo.setLastName(measure.getLockedUser().getLastName());
			detail.setLockedUserInfo(lockedUserInfo);
		}
		
		return detail;

	}
	
	private void caclulateVersionAndDraft(Result detail, List<Measure> measuresInSet) {
		if(detail.isDraft()) {
			detail.setVersionable(detail.isDraft());
			detail.setDraftable(!detail.isDraft());
			return;
		}
		detail.setVersionable(false);
		boolean isDraftable = measuresInSet.stream().filter(measure -> measure.isDraft()).count() == 0;
		detail.setDraftable(isDraftable);
	}

	/**
	 * Gets the and validate value set date.
	 * 
	 * @param valueSetDateStr
	 *            - {@link String}.
	 * @return the and validate value set date
	 * @throws InvalidValueSetDateException
	 *             - {@link Exception}. *
	 */
	private void getAndValidateValueSetDate(final String valueSetDateStr) throws InvalidValueSetDateException {
		if (StringUtils.isNotBlank(valueSetDateStr)) {
			DateStringValidator dsv = new DateStringValidator();
			int validationCode = dsv.isValidDateString(valueSetDateStr);
			if (validationCode != DateStringValidator.VALID) {
				throw new InvalidValueSetDateException();
			}
		}
	}

	
	@Override
	public final QualityDataModelWrapper getAppliedQDMFromMeasureXml(final String measureId,
			final boolean checkForSupplementData) {
		MeasureXmlModel measureXmlModel = getMeasureXmlForMeasure(measureId);
		QualityDataModelWrapper details = convertXmltoQualityDataDTOModel(measureXmlModel);
		ArrayList<QualityDataSetDTO> finalList = new ArrayList<>();
		if (details != null) {
			if (details.getQualityDataDTO() != null && !details.getQualityDataDTO().isEmpty()) {
				logger.info(" details.getQualityDataDTO().size() :" + details.getQualityDataDTO().size());
				for (QualityDataSetDTO dataSetDTO : details.getQualityDataDTO()) {
					if (StringUtils.isNotBlank(dataSetDTO.getOccurrenceText())) {
						dataSetDTO.setSpecificOccurrence(true);
					}
					if (dataSetDTO.getCodeListName() != null && !(checkForSupplementData && dataSetDTO.isSuppDataElement())) {
						finalList.add(dataSetDTO);
					}
				}
			}
			Collections.sort(finalList, (o1, o2) -> o1.getCodeListName().compareToIgnoreCase(o2.getCodeListName()));

			finalList = findUsedQDMs(finalList, measureXmlModel);
			details.setQualityDataDTO(finalList);
		}

		logger.info("finalList()of QualityDataSetDTO ::" + finalList.size());
		return details;

	}

	@Override
	public final CQLQualityDataModelWrapper getCQLAppliedQDMFromMeasureXml(final String measureId,
			final boolean checkForSupplementData) {
		return getCqlService().getCQLValusets(measureId, new CQLQualityDataModelWrapper());
	}

	@Override
	public final CQLQualityDataModelWrapper getDefaultCQLSDEFromMeasureXml(final String measureId) {
		logger.info("Inside MeasureLibraryServiceImp :: getDefaultCQLSDEFromMeasureXml :: Start");
		MeasureXmlModel measureXmlModel = getMeasureXmlForMeasure(measureId);
		CQLQualityDataModelWrapper details = convertXmltoCQLQualityDataDTOModel(measureXmlModel);
		ArrayList<CQLQualityDataSetDTO> finalList = new ArrayList<>();
		if (details != null) {
			if (details.getQualityDataDTO() != null && !details.getQualityDataDTO().isEmpty()) {
				logger.info(" details.getQualityDataDTO().size() :" + details.getQualityDataDTO().size());
				for (CQLQualityDataSetDTO dataSetDTO : details.getQualityDataDTO()) {
					if (dataSetDTO.getName() != null && dataSetDTO.isSuppDataElement()) {
						finalList.add(dataSetDTO);
					}
				}
			}
			details.setQualityDataDTO(finalList);
		}
		logger.info("finalList()of QualityDataSetDTO ::" + finalList.size());
		logger.info("Inside MeasureLibraryServiceImp :: getDefaultSDEFromMeasureXml :: END");
		return details;
	}

	private CQLQualityDataModelWrapper convertXmltoCQLQualityDataDTOModel(MeasureXmlModel measureXmlModel) {

		logger.info("In MeasureLibraryServiceImpl.convertXmltoCQLQualityDataDTOModel()");
		CQLQualityDataModelWrapper details = null;
		String xml = null;
		if ((measureXmlModel != null) && StringUtils.isNotBlank(measureXmlModel.getXml())) {
			xml = new XmlProcessor(measureXmlModel.getXml()).getXmlByTagName(CQL_LOOKUP);
		}
		try {
			if (xml != null) {
				XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
				details = (CQLQualityDataModelWrapper) xmlMarshalUtil.convertXMLToObject("CQLValueSetsMapping.xml", xml, CQLQualityDataModelWrapper.class);
				logger.info("unmarshalling complete..valuesets" + details.getQualityDataDTO().get(0).getName());
			}

		} catch (IOException e) {
			logger.info("Failed to load CQLValueSetsMapping.xml" + e);
		} catch (MappingException e) {
			logger.info(MAPPING_FAILED + e);
		} catch (MarshalException e) {
			logger.info(UNMARSHALLING_FAILED + e);
		} catch (Exception e) {
			logger.info(OTHER_EXCEPTION + e);
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

	@Override
	public final int getMaxEMeasureId() {
		MeasurePackageService service = measurePackageService;
		int emeasureId = service.getMaxEMeasureId();
		logger.info("**********Current Max EMeasure Id from DB ******************" + emeasureId);
		return emeasureId;
	}

	@Override
	public final ManageMeasureDetailModel getMeasure(final String key) {
		logger.info("In MeasureLibraryServiceImpl.getMeasure() method..");
		logger.info("Loading Measure for MeasueId: " + key);
		Measure measure = measurePackageService.getById(key);
		if(!measure.getIsCompositeMeasure()) {
			MeasureXmlModel xmlModel = getMeasureXmlForMeasure(key);
			MeasureDetailResult measureDetailResult = getUsedStewardAndDevelopersList(measure.getId());
			String xmlString = new XmlProcessor(xmlModel.getXml()).getXmlByTagName(MEASURE_DETAILS);
			ManageMeasureDetailModel manageMeasureDetailModel = convertXMLToModel(xmlString, measure);
			manageMeasureDetailModel.setMeasureDetailResult(measureDetailResult);
			manageMeasureDetailModel.setQdmVersion(measure.getQdmVersion());
		
			return manageMeasureDetailModel;
		}
		else {
			return getCompositeMeasure(key);
		}
		

	}

	@Override
	public ManageCompositeMeasureDetailModel getCompositeMeasure(String measureId) {
		Measure measure = measurePackageService.getById(measureId);
		MeasureXmlModel xmlModel = getMeasureXmlForMeasure(measureId);
		MeasureDetailResult measureDetailResult = getUsedStewardAndDevelopersList(measure.getId());
		String xmlString = new XmlProcessor(xmlModel.getXml()).getXmlByTagName(MEASURE_DETAILS);
		ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel = (ManageCompositeMeasureDetailModel) convertXMLToModel(xmlString, measure);
		manageCompositeMeasureDetailModel.setMeasureDetailResult(measureDetailResult);
		manageCompositeMeasureDetailModel.setQdmVersion(measure.getQdmVersion());
		
		return manageCompositeMeasureDetailModel;
	}

	@Override
	public ManageCompositeMeasureDetailModel getCompositeMeasure(String measureId, String simpleXML) {
		Measure measure = measurePackageService.getById(measureId);
		MeasureDetailResult measureDetailResult = getUsedStewardAndDevelopersList(measure.getId());	
		ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel = (ManageCompositeMeasureDetailModel) convertXMLToModel(simpleXML, measure);
		manageCompositeMeasureDetailModel.setMeasureDetailResult(measureDetailResult);
		
		return manageCompositeMeasureDetailModel;
	}
	
	@Override
	public void updateMeasureXmlForDeletedComponentMeasureAndOrg(String measureId) {
		logger.info("In MeasureLibraryServiceImpl. updateMeasureXmlForDeletedComponentMeasureAndOrg() method..");
		logger.info("Updating Measure for MeasueId: " + measureId);
		MeasureXmlModel xmlModel = getMeasureXmlForMeasure(measureId);
		if (xmlModel != null) {
			XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
			removeDeletedComponentMeasures(processor);
			removeDeletedSteward(processor);
			removeDeletedDevelopers(processor);
			xmlModel.setXml(processor.transform(processor.getOriginalDoc()));
			measurePackageService.saveMeasureXml(xmlModel);
		}

	}

	/**
	 * Update measure developers on deletion.
	 *
	 * @param processor
	 *            the processor
	 */
	private void removeDeletedDevelopers(XmlProcessor processor) {
		try {
			NodeList developerParentNodeList = (NodeList) xPath.evaluate(XPATH_EXPRESSION_DEVELOPERS,
					processor.getOriginalDoc(), XPathConstants.NODESET);
			Node developerParentNode = developerParentNodeList.item(0);
			if (developerParentNode != null) {
				NodeList developerNodeList = developerParentNode.getChildNodes();
				for (int i = 0; i < developerNodeList.getLength(); i++) {
					Node newNode = developerNodeList.item(i);
					String developerId = newNode.getAttributes().getNamedItem(ID).getNodeValue();
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
			logger.debug("Failed to delete MeasureDevelopers From MeasureXml. Exception occured." + e);
		}

	}

	/**
	 * Update steward on deletion.
	 *
	 * @param processor
	 *            the processor
	 */
	private void removeDeletedSteward(XmlProcessor processor) {
		try {
			// steward
			Node stewardParentNode = (Node) xPath.evaluate(XPATH_EXPRESSION_STEWARD, processor.getOriginalDoc(),
					XPathConstants.NODE);
			if (stewardParentNode != null) {
				String id = stewardParentNode.getAttributes().getNamedItem(ID).getNodeValue();
				Organization org = organizationDAO.findById(id);
				if (org == null) {
					removeNode(XPATH_EXPRESSION_STEWARD, processor.getOriginalDoc());
					logger.info("Deleted steward Deleted successFully From MeasureXml.");
				} else {
					stewardParentNode.setTextContent(org.getOrganizationName());
					logger.info("Steward Name updated in measure Xml.");
				}
			}
		} catch (XPathExpressionException e) {
			logger.debug("Failed to delete steward From MeasureXml. Exception occured." + e);
		}

	}

	/**
	 * Update component measures on deletion.
	 *
	 * @param processor
	 *            the processor
	 */
	private void removeDeletedComponentMeasures(XmlProcessor processor) {
		try {
			NodeList componentMeasureParentNodeList = (NodeList) xPath.evaluate(XPATH_EXPRESSION_COMPONENT_MEASURES,
					processor.getOriginalDoc(), XPathConstants.NODESET);
			Node componentMeasureParentNode = componentMeasureParentNodeList.item(0);
			if (componentMeasureParentNode != null) {
				NodeList nodeList = componentMeasureParentNode.getChildNodes();

				for (int i = 0; i < nodeList.getLength(); i++) {
					Node newNode = nodeList.item(i);
					String id = newNode.getAttributes().getNamedItem(ID).getNodeValue();
					boolean isDeleted = measureDAO.getMeasure(id);
					if (!isDeleted) {
						componentMeasureParentNode.removeChild(newNode);
					}
				}
			}
			logger.info("Deleted componentMeasures Deleted successFully From MeasureXml.");
		} catch (XPathExpressionException e) {
			logger.debug("Failed to delete  componentMeasures From MeasureXml. Exception occured." + e);
		}
	}


	/**
	 * Gets the measure xml dao.
	 * 
	 * @return the measure xml dao
	 */
	private MeasureXMLDAO getMeasureXMLDAO() {
		return ((MeasureXMLDAO) context.getBean("measureXMLDAO"));
	}

	@Override
	public final MeasureXmlModel getMeasureXmlForMeasure(final String measureId) {
		logger.info("In MeasureLibraryServiceImpl.getMeasureXmlForMeasure()");
		MeasureXmlModel measureXmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
		if (measureXmlModel == null) {
			logger.info("Measure XML is null");
		} else {
			logger.debug("XML ::: " + measureXmlModel.getXml());
		}
		return measureXmlModel;
	}

	@Override
	public SortedClauseMapResult getMeasureXmlForMeasureAndSortedSubTreeMap(final String measureId) {
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
	 * @param measureId
	 *            the measure id
	 * @return the sorted clause map
	 */
	@Override
	public LinkedHashMap<String, String> getSortedClauseMap(String measureId) {

		logger.info("In MeasureLibraryServiceImpl.getSortedClauseMap()");
		MeasureXmlModel measureXmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);

		LinkedHashMap<String, String> sortedMainClauseMap = new LinkedHashMap<>();
		LinkedHashMap<String, String> mainClauseMap = new LinkedHashMap<>();

		if ((measureXmlModel != null) && StringUtils.isNotBlank(measureXmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(measureXmlModel.getXml());
			NodeList mainClauseLIst;
			NodeList instanceClauseList;
			try {
				mainClauseLIst = (NodeList) xPath.evaluate("/measure//subTreeLookUp/subTree[not(@instanceOf )]",
						xmlProcessor.getOriginalDoc().getDocumentElement(), XPathConstants.NODESET);
				for (int i = 0; i < mainClauseLIst.getLength(); i++) {
					mainClauseMap.put(mainClauseLIst.item(i).getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME).getNodeValue(),
							mainClauseLIst.item(i).getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue());
				}
				// sort the map alphabetically
				List<Entry<String, String>> mainClauses = new LinkedList<>(
						mainClauseMap.entrySet());
				Collections.sort(mainClauses, (o1, o2) -> o1.getKey().toUpperCase().compareTo(o2.getKey().toUpperCase()));
				for (Entry<String, String> entry : mainClauses) {
					sortedMainClauseMap.put(entry.getValue(), entry.getKey());

					instanceClauseList = (NodeList) xPath.evaluate(
							"/measure//subTreeLookUp/subTree[@instanceOf='" + entry.getValue() + "']",
							xmlProcessor.getOriginalDoc().getDocumentElement(), XPathConstants.NODESET);

					if (instanceClauseList.getLength() >= 1) {

						Map<String, String> instanceClauseMap = new HashMap<>();
						for (int j = 0; j < instanceClauseList.getLength(); j++) {
							String uuid = instanceClauseList.item(j).getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID)
									.getNodeValue();
							String name = instanceClauseList.item(j).getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME)
									.getNodeValue();
							String instanceVal = instanceClauseList.item(j).getAttributes().getNamedItem(INSTANCE)
									.getNodeValue().toUpperCase();
							String fname = "Occurrence " + instanceVal + " of " + name;
							instanceClauseMap.put(fname, uuid);
						}

						List<Entry<String, String>> instanceClauses = new LinkedList<>(
								instanceClauseMap.entrySet());
						Collections.sort(instanceClauses, (o1, o2) -> o1.getKey().toUpperCase().compareTo(o2.getKey().toUpperCase()));
						for (Entry<String, String> entry1 : instanceClauses) {
							sortedMainClauseMap.put(entry1.getValue(), entry1.getKey());
						}
					}
				}
			} catch (XPathExpressionException e) {
				logger.debug("getSortedClauseMap:" + e);
			}

		}
		return sortedMainClauseMap;
	}

	@Override
	public List<RecentMSRActivityLog> getRecentMeasureActivityLog(String userId) {
		return recentMSRActivityLogDAO.getRecentMeasureActivityLog(userId);
	}

	/**
	 * Gets the vsac service.
	 * 
	 * @return the service
	 */
	private VSACApiServImpl getVsacService() {
		return (VSACApiServImpl) context.getBean("vsacapi");
	}

	@Override
	public final ManageMeasureShareModel getUsersForShare(final String userName, final String measureId, final int startIndex,
			final int pageSize) {
		ManageMeasureShareModel model = new ManageMeasureShareModel();
		List<MeasureShareDTO> dtoList = measurePackageService.getUsersForShare(userName, measureId, startIndex, pageSize);
		model.setResultsTotal(measurePackageService.countUsersForMeasureShare());
		List<MeasureShareDTO> dataList = new ArrayList<>();
		for (MeasureShareDTO dto : dtoList) {
			dataList.add(dto);
		}
		// model.setData(dtoList); Directly setting dtoList causes the RPC
		// serialization exception(java.util.RandomaccessSubList) since we are
		// sublisting it
		model.setData(dataList);
		model.setStartIndex(startIndex);
		model.setMeasureId(measureId);
		Measure measure = measurePackageService.getById(measureId);
		model.setMeasureName(measure.getDescription());
		model.setPrivate(measure.getIsPrivate());
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
	 * @param measure
	 *            - {@link Measure}.O
	 * @return {@link SaveMeasureResult}. *
	 */
	private SaveMeasureResult incrementVersionNumberAndSave(final String maximumVersionNumber, final String incrementBy,
			final Measure measure) {
		ManageMeasureDetailModel mDetail = getMeasure(measure.getId());
		BigDecimal mVersion = new BigDecimal(maximumVersionNumber);
		mVersion = mVersion.add(new BigDecimal(incrementBy));
		mDetail.setVersionNumber(mVersion.toString());
		Date currentDate = new Date();
		mDetail.setFinalizedDate(DateUtility.convertDateToString(currentDate));
		mDetail.setDraft(false);
		setValueFromModel(mDetail, measure);
		measurePackageService.save(measure);
		String versionStr = mVersion.toString();
		// Divide the number by 1 and check for a remainder.
		// Any whole number should always have a remainder of 0 when divided by
		// 1.
		// For major versions, there may be case the minor version value is
		// zero.
		// THis makes the BigDecimal as Integer value causing issue while
		// formatVersionText method.
		// To fix that we are explicitly appending .0 in versionString.
		if (mVersion.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
			versionStr = versionStr.concat(".0");
		}
		
		versionStr = versionStr.concat(".000");

		
		// This code is added to update version value for both measureDetails
		// version tag and cqlLookUp version tag.
		setVersionInMeasureDetailsAndCQLLookUp(measure, versionStr);

		if (MatContext.get().isCQLMeasure(measure.getReleaseVersion())) {
			MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measure.getId());
			exportCQLibraryFromMeasure(measure, mDetail, xmlModel);
		}

		SaveMeasureResult result = new SaveMeasureResult();
		result.setSuccess(true);
		result.setId(measure.getId());
		versionStr = MeasureUtility.formatVersionText(versionStr);
		result.setVersionStr(versionStr);
		logger.info("Result passed for Version Number " + versionStr);
		return result;
	}

    /**
    * Method to set version in measureDetail/version tag and cqlLookUp/version
    * tag when measure is versioned.
    * 
     * @param meas
    *            - Measure
    * @param versionStr
    */
    private void setVersionInMeasureDetailsAndCQLLookUp(final Measure meas, String versionStr) {
           MeasureXmlModel measureXmlModel = measurePackageService.getMeasureXmlForMeasure(meas.getId());
           if ((measureXmlModel != null) && StringUtils.isNotBlank(measureXmlModel.getXml())) {
                  String measureXML = measureXmlModel.getXml();
                  String updatedXML;
                  try {
                        updatedXML = replaceVersionInXMLString(measureXML, versionStr, meas.getId());
                        measureXmlModel.setXml(updatedXML);
                        measurePackageService.saveMeasureXml(measureXmlModel);
                  } catch (XPathExpressionException e) {
                        e.printStackTrace();
                  }
           }
    }

    private String replaceVersionInXMLString(String measureXML, String versionStr, String measureId) throws XPathExpressionException {
    	XmlProcessor xmlProcessor = new XmlProcessor(measureXML);
    	String cqlVersionXPath = "//cqlLookUp/version";
    	String measureVersionXPath = "//measureDetails/version";
    	Node measureVersionNode = (Node) xPath.evaluate(measureVersionXPath,
    			xmlProcessor.getOriginalDoc().getDocumentElement(), XPathConstants.NODE);
    	if (measureVersionNode != null) {
    		measureVersionNode.setTextContent(MeasureUtility.formatVersionText(versionStr));
    	} else {
    		logger.info("measureDetails Node not found. This is in measure : " + measureId);
    	}

    	Node cqlVersionNode = (Node) xPath.evaluate(cqlVersionXPath,
    			xmlProcessor.getOriginalDoc().getDocumentElement(), XPathConstants.NODE);
    	if (cqlVersionNode != null) {
    		cqlVersionNode.setTextContent(MeasureUtility.formatVersionText(versionStr));
    	} else {
    		logger.info("CQLLookUp Node not found. This is in measure : " + measureId);
    	}
    	return xmlProcessor.transform(xmlProcessor.getOriginalDoc());
    }


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

	@Override
	public final boolean isMeasureLocked(final String id) {
		return measurePackageService.isMeasureLocked(id);
	}

	@Override
	public void recordRecentMeasureActivity(String measureId, String userId) {
		recentMSRActivityLogDAO.recordRecentMeasureActivity(measureId, userId);
	}

	/** 
	 * This method has been added to release the Measure lock. 
	 * It gets the existingMeasureLock and checks the loggedinUserId and the LockedUserid to release the lock.
	 */
	@Override
	public final SaveMeasureResult resetLockedDate(final String measureId, final String userId) {
		Measure existingMeasure = null;
		User lockedUser = null;
		SaveMeasureResult result = new SaveMeasureResult();
		if ((measureId != null) && (userId != null) && StringUtils.isNotBlank(measureId)) {
			existingMeasure = measurePackageService.getById(measureId);
			if (existingMeasure != null) {
				lockedUser = getLockedUser(existingMeasure);
				if ((lockedUser != null) && lockedUser.getId().equalsIgnoreCase(userId)) {
					// Only if the lockedUser and loggedIn User are same we can
					// allow the user to unlock the measure.
					if (existingMeasure.getLockedOutDate() != null) {
						// if it is not null then set it to null and save it.
						existingMeasure.setLockedOutDate(null);
						existingMeasure.setLockedUser(null);
						measurePackageService.updateLockedOutDate(existingMeasure);
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

	@Override
	public SaveMeasureResult saveMeasureAtPackage(ManageMeasureDetailModel model) {
		Measure measure = measurePackageService.getById(model.getId());
		Integer revisionNumber = NumberUtils.INTEGER_ZERO;
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
		measurePackageService.save(measure);
		SaveMeasureResult result = save(model);
		return result;
	}

	@Override
	public final SaveMeasureResult save(ManageMeasureDetailModel model) {
		// Scrubbing out Mark Up.
		if (model != null) {
			model.scrubForMarkUp();
		}
		ManageMeasureModelValidator manageMeasureModelValidator = new ManageMeasureModelValidator();
		List<String> message = manageMeasureModelValidator.validateMeasure(model);
		if (message.isEmpty()) {
			Measure pkg = null;
			MeasureSet measureSet = null;
			if (model.getId() != null) {
				setMeasureCreated(true);
				// editing an existing measure
				pkg = measurePackageService.getById(model.getId());
				model.setVersionNumber(pkg.getVersion());
				if (pkg.isDraft()) {
					model.setRevisionNumber(pkg.getRevisionNumber());
				} else {
					model.setRevisionNumber("000");
				}
				if (pkg.getMeasureSet().getId() != null) {
					measureSet = measurePackageService.findMeasureSet(pkg.getMeasureSet().getId());
				}
				if (!pkg.getMeasureScoring().equalsIgnoreCase(model.getMeasScoring())) {
					// US 194 User is changing the measure scoring. Make sure to
					// delete any groupings for that measure and save.
					measurePackageService.deleteExistingPackages(pkg.getId());
				}
			} else {
				// creating a new measure.
				setMeasureCreated(false);
				pkg = new Measure();
				pkg.setReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
				pkg.setQdmVersion(MATPropertiesService.get().getQmdVersion());
				model.setRevisionNumber("000");
				measureSet = new MeasureSet();
				measureSet.setId(UUID.randomUUID().toString());
				measurePackageService.save(measureSet);
			}
			pkg.setMeasureSet(measureSet);
			setValueFromModel(model, pkg);
			SaveMeasureResult result = new SaveMeasureResult();
			try {
				getAndValidateValueSetDate(model.getValueSetDate());
				pkg.setValueSetDate(DateUtility.addTimeToDate(pkg.getValueSetDate()));
				measurePackageService.save(pkg);
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
	
	@Override
	public void deleteMeasure(String measureId, String loggedInUserId, String password) throws DeleteMeasureException, AuthenticationException {
		logger.info("Measure Deletion Started for measure Id :: " + measureId);		
		boolean isValidPassword = loginService.isValidPassword(loggedInUserId, password);
		if(!isValidPassword) {
			logger.error("Failed to delete measure: " + MatContext.get().getMessageDelegate().getMeasureDeletionInvalidPwd());
			throw new AuthenticationException(MatContext.get().getMessageDelegate().getMeasureDeletionInvalidPwd());
		}
		
		GenericResult isUsedAsComponentMeasureResult = checkIfMeasureIsUsedAsComponentMeasure(measureId);
		if(!isUsedAsComponentMeasureResult.isSuccess()) {
			logger.error("Failed to delete measure: "  + isUsedAsComponentMeasureResult.getMessages().get(0));
			throw new DeleteMeasureException(isUsedAsComponentMeasureResult.getMessages().get(0));
		}
		
		deleteMeasure(measureId);
		logger.info("Measure Deleted Successfully: " + measureId);
	}

	private void deleteMeasure(String measureId) {
		Measure m = measureDAO.find(measureId);
		SecurityContext sc = SecurityContextHolder.getContext();
		MatUserDetails details = (MatUserDetails) sc.getAuthentication().getDetails();
		if (m.getOwner().getId().equalsIgnoreCase(details.getId())) {
			if (m.getIsCompositeMeasure()) {
				measurePackageService.updateComponentMeasures(m.getId(), new ArrayList<>());
			}
			m.setOwner(null);
			m.setLastModifiedBy(null);
			m.setLockedUser(null);
			measureDAO.delete(m);
		}
	}

	private void removeUnusedLibraries(MeasureXmlModel measureXmlModel, SaveUpdateCQLResult cqlResult, Measure measure) {
		String measureXml = measureXmlModel.getXml();
		XmlProcessor processor = new XmlProcessor(measureXml);
		try {
			CQLUtil.removeUnusedIncludes(processor.getOriginalDoc(), cqlResult.getUsedCQLArtifacts().getUsedCQLLibraries(), cqlResult.getCqlModel());
		} catch (XPathExpressionException e) {
			logger.error(e.getStackTrace());
		}

		String updatedMeasureXml = new String(processor.transform(processor.getOriginalDoc()).getBytes());
		measureXmlModel.setXml(updatedMeasureXml);
		measurePackageService.saveMeasureXml(measureXmlModel);
	}
	
	private void removeUnusedComponents(MeasureXmlModel measureXmlModel, Measure measure) {
		XmlProcessor processor = new XmlProcessor(measureXmlModel.getXml());
		logger.info("Remove Unused Component Measures");

		String componentMeasuresXPath = "/measure/measureDetails/componentMeasures/measure";
		try {
			NodeList componentMeasureNodeList = (NodeList) xPath.evaluate(componentMeasuresXPath,
					processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODESET);

			String includedLibraryXPath = "/measure/cqlLookUp/includeLibrarys/includeLibrary";
			NodeList includedLibraryNodeList = (NodeList) xPath.evaluate(includedLibraryXPath,
					processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODESET);

			Set<String> usedIncludedLibraryIds = new HashSet<>();
			if (includedLibraryNodeList != null) {
				for (int i = 0; i < includedLibraryNodeList.getLength(); i++) {
					Node current = includedLibraryNodeList.item(i);
					if (current.getAttributes().getNamedItem("measureId") != null) {
						usedIncludedLibraryIds.add(current.getAttributes().getNamedItem("measureId").getNodeValue());
					}
				}
			}

			for (int i = 0; i < componentMeasureNodeList.getLength(); i++) {
				Node current = componentMeasureNodeList.item(i);
				String id = current.getAttributes().getNamedItem("id").getNodeValue();
				if (!usedIncludedLibraryIds.contains(id)) {
					current.getParentNode().removeChild(current);
					componentMeasuresDAO.delete(String.valueOf((measure.getComponentMeasures().stream().filter(m -> m.getComponentMeasure().getId().equals(id)).findFirst().get().getId())));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Finished Removing Unused Component Measures");
	}

	@Override
	public final SaveMeasureResult saveFinalizedVersion(final String measureId, final boolean isMajor, final String version, boolean shouldPackage, boolean ignoreUnusedLibraries) {

		logger.info("In MeasureLibraryServiceImpl.saveFinalizedVersion() method..");
		Measure m = measurePackageService.getById(measureId);
		logger.info("Measure Loaded for: " + measureId);

		boolean isMeasureVersionable = MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId);
		if (!isMeasureVersionable) {
			SaveMeasureResult saveMeasureResult = new SaveMeasureResult();
			return returnFailureReason(saveMeasureResult, SaveMeasureResult.INVALID_DATA);
		}

		SaveUpdateCQLResult cqlResult = getMeasureCQLFileData(measureId);
		if(cqlResult.getCqlErrors().size() > 0 || !cqlResult.isDatatypeUsedCorrectly()){
			SaveMeasureResult saveMeasureResult = new SaveMeasureResult();
			return returnFailureReason(saveMeasureResult, SaveMeasureResult.INVALID_CQL_DATA);
		}

		// return error if there are unused libraries in the measure
		MeasureXmlModel measureXmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
		String measureXml = measureXmlModel.getXml();
		if(!ignoreUnusedLibraries && CQLUtil.checkForUnusedIncludes(measureXml, cqlResult.getUsedCQLArtifacts().getUsedCQLLibraries())) {
			SaveMeasureResult saveMeasureResult = new SaveMeasureResult();
			saveMeasureResult.setFailureReason(SaveMeasureResult.UNUSED_LIBRARY_FAIL);
			logger.info("Measure Package and Version Failed for measure with id " + measureId + " because there are libraries that are unused.");
			return saveMeasureResult;
		}
		
	
		if(shouldPackage) {
			SaveMeasureResult validatePackageResult = validateAndPackage(getMeasure(measureId), false);
			if(!validatePackageResult.isSuccess()) {
				SaveMeasureResult saveMeasureResult = new SaveMeasureResult(); 
				return returnFailureReason(saveMeasureResult, SaveMeasureResult.PACKAGE_FAIL);
			}
		}
		
		removeUnusedLibraries(measureXmlModel, cqlResult, m);
		removeUnusedComponents(measureXmlModel, m);

		return updateVersionAndExports(isMajor, version, m);
		
	}

	private SaveMeasureResult updateVersionAndExports(final boolean isMajor, final String version, Measure measure) {
		String versionNumber = createVersionNumber(isMajor, version, measure.getMeasureSet().getId());

		// Need to check for logic when to mark a measure as completed.
		SaveMeasureResult rs = new SaveMeasureResult();

		if (!versionNumber.equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_VERSION)) {
			String[] versionArr = versionNumber.split("\\.");
			if (isMajor) {
				rs = createMajorVersion(versionNumber, versionArr, measure, rs);
			} else {
				rs = createMinorVersion(versionArr, measure, rs);
			}

		} else {
			returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_VERSION);
		}

		if (rs.isSuccess()) {
			updateVersionInSimpleXMLAndGenerateArtifacts(measure);
		}

		return rs;
	}

	private SaveMeasureResult createMajorVersion(String versionNumber, String[] versionArr, Measure measure,
			SaveMeasureResult rs) {
		if (!versionArr[0].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MAJOR_VERSION)) {
			String majorVersionNumber = StringUtils.substringBefore(versionNumber, ".");
			rs = incrementVersionNumberAndSave(majorVersionNumber, "1", measure);
		} else {
			returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_MAJOR_VERSION);
		}
		return rs;
	}

	private SaveMeasureResult createMinorVersion(String[] versionArr, Measure measure, SaveMeasureResult rs) {
		if (!versionArr[1].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MINOR_VERSION)) {
			String minorVersionNumber = versionArr[0] + "." + versionArr[1];
			rs = incrementVersionNumberAndSave(minorVersionNumber, "0.001", measure);
		} else {
			returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_MINOR_VERSION);
		}
		return rs;
	}

	private String createVersionNumber(boolean isMajor, final String version, final String measureSetId) {
		String versionNumber = null;
		if (isMajor) {
			versionNumber = findOutMaximumVersionNumber(measureSetId);
			// For new measure's only draft entry will be
			// available.findOutMaximumVersionNumber will return null.
			if (versionNumber == null) {
				versionNumber = "0.000";
			}
			logger.info("Max Version Number loaded from DB: " + versionNumber);
		} else {
			String selectedVersion = StringUtils.substringAfter(version, "v");
			logger.info("Min Version number after trim: " + selectedVersion);
			versionNumber = findOutVersionNumber(measureSetId, selectedVersion);
		}
		return versionNumber;
	}

	private void updateVersionInSimpleXMLAndGenerateArtifacts(Measure measure) {
		MeasureExport measureExport = measureExportDAO.findByMeasureId(measure.getId());
		if (measureExport != null) {
			String simpleXML = measureExport.getSimpleXML();
			String finalVersion = measure.getMajorVersionInt() + "." + measure.getMinorVersionInt() + "."
					+ measure.getRevisionNumber();

			String updatedSimpleXML = "";
			try {
				updatedSimpleXML = replaceVersionInXMLString(simpleXML, finalVersion, measure.getId());

			} catch (XPathExpressionException e) {
				logger.debug("updateVersionInSimpleXMLAndGenerateArtifacts:" + e);
			}

			measureExport.setSimpleXML(updatedSimpleXML);
			measureExportDAO.save(measureExport);
			measurePackageService.createPackageArtifacts(measure.getId(), measure.getReleaseVersion(), measureExport);
		}
	}

	/**
	 * This method exports cql when measure is versioned. Entry in CQL_Library table
	 * is made.
	 * 
	 * @param measure
	 * @param mDetail
	 * @param xmlModel
	 */
	private void exportCQLibraryFromMeasure(Measure measure, ManageMeasureDetailModel mDetail,
			MeasureXmlModel xmlModel) {
		logger.info("exportCQLibraryFromMeasure method :: Start");

		String cqlLibraryName = "";
		String cqlQdmVersion = "";
		byte[] cqlByteArray = null;
		CQLLibrary cqlLibrary = new CQLLibrary();
		if (!xmlModel.getXml().isEmpty()) {
			String xPathForCQLLookup = "//cqlLookUp";
			String xPathForCQLLibraryName = "//cqlLookUp/library";
			String xPathForCQLQdmVersion = "//cqlLookUp/usingModelVersion";

			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			Document document = xmlProcessor.getOriginalDoc();
			Node cqlLookUpNode = null;

			try {
				cqlLookUpNode = xmlProcessor.findNode(document, xPathForCQLLookup);
				if (cqlLookUpNode != null) {
					Node cqlLibraryNode = xmlProcessor.findNode(document, xPathForCQLLibraryName);
					Node cqlQdmVersionNode = xmlProcessor.findNode(document, xPathForCQLQdmVersion);
					cqlLibraryName = cqlLibraryNode.getTextContent();
					cqlQdmVersion = cqlQdmVersionNode.getTextContent();
					if (cqlLibraryName.length() > 200) {
						cqlLibraryName = cqlLibraryName.substring(0, 199);
					}
					cqlLibrary.setName(MeasureUtility.cleanString(cqlLibraryName));

					String cqlXML = xmlProcessor.transform(cqlLookUpNode, true);
					cqlByteArray = cqlXML.getBytes();

					SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:ss");
					Date date = null;
					try {
						date = dateFormat.parse(mDetail.getFinalizedDate());
					} catch (ParseException e) {
						logger.debug("Exception in exportCQLibraryFromMeasure when formatting date: " + e);
					}
					long time = date.getTime();
					Timestamp timestamp = new Timestamp(time);

					cqlLibrary.setMeasureId(mDetail.getId());
					cqlLibrary.setOwnerId(measure.getOwner());
					cqlLibrary.setSet_id(measure.getMeasureSet().getId());
					cqlLibrary.setVersion(mDetail.getVersionNumber());
					cqlLibrary.setReleaseVersion(measure.getReleaseVersion());
					cqlLibrary.setQdmVersion(cqlQdmVersion);
					cqlLibrary.setFinalizedDate(timestamp);
					cqlLibrary.setDraft(false);
					cqlLibrary.setRevisionNumber(mDetail.getRevisionNumber());
					cqlLibrary.setCQLByteArray(cqlByteArray);
					cqlLibraryService.save(cqlLibrary);
					cqlLibraryService.saveCQLLibraryExport(cqlLibrary, cqlXML);
					logger.info("Versioned CQL successfull.");
				} else {
					logger.info("Versioning of cql failed as no cqlLookUpNode available");
				}

			} catch (XPathExpressionException e) {
				logger.debug("exportCQLibraryFromMeasure:" + e);
			}
		}
		logger.info("exportCQLibraryFromMeasure method :: END");
	}

	@Override
	public final SaveMeasureResult saveMeasureDetails(final ManageMeasureDetailModel model) {
		logger.info("In MeasureLibraryServiceImpl.saveMeasureDetails() method..");
		Measure measure = null;
		model.scrubForMarkUp();
		ManageMeasureModelValidator manageMeasureModelValidator = new ManageMeasureModelValidator();
		List<String> message = manageMeasureModelValidator.validateMeasure(model);
		if (message.isEmpty()) {
			if (model.getId() != null) {
				setMeasureCreated(true);
				measure = measurePackageService.getById(model.getId());
				measure.setDescription(model.getName());
				String shortName = buildMeasureShortName(model);

				model.setShortName(shortName);
				measure.setaBBRName(shortName);
				measure.setMeasureScoring(model.getMeasScoring());
				measure.setPatientBased(model.isPatientBased());
				measurePackageService.save(measure);
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
			result.setMessages(message);
			logger.info("Saving of Measure Details Failed. Invalid Data issue.");
			return result;
		}
	}

	private String buildMeasureShortName(final ManageMeasureDetailModel model) {
		String shortName = "";
		if(!StringUtility.isEmptyOrNull(model.getShortName())) {
			shortName = model.getShortName();
			if(shortName.length() > 32) {
				shortName = shortName.substring(0,  32);
			}
		}
		return shortName;
	}

	@Override
	public final void saveMeasureXml(final MeasureXmlModel measureXmlModel) {

		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureXmlModel.getMeasureId())) {
				XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
				try {
					String scoringTypeBeforeNewXml = (String) xPath.evaluate("/measure/measureDetails/scoring/@id",
							xmlProcessor.getOriginalDoc().getDocumentElement(), XPathConstants.STRING);
					String newXml = xmlProcessor.replaceNode(measureXmlModel.getXml(),
							measureXmlModel.getToReplaceNode(), measureXmlModel.getParentNode());
					String scoringTypeAfterNewXml = (String) xPath.evaluate("/measure/measureDetails/scoring/@id",
							xmlProcessor.getOriginalDoc().getDocumentElement(), XPathConstants.STRING);
					xmlProcessor.checkForScoringType(MATPropertiesService.get().getQmdVersion());
					xmlProcessor.updateCQLLibraryName();
					checkForDefaultCQLParametersAndAppend(xmlProcessor);
					checkForDefaultCQLDefinitionsAndAppend(xmlProcessor);
					updateCQLVersion(xmlProcessor);
					if (!scoringTypeBeforeNewXml.equalsIgnoreCase(scoringTypeAfterNewXml)) {
						deleteExistingGroupings(xmlProcessor);
					}
					newXml = xmlProcessor.transform(xmlProcessor.getOriginalDoc());
					measureXmlModel.setXml(newXml);
				} catch (XPathExpressionException e) {
					logger.debug("Exception in saveMeasureXml while updating scoring :" + e);
				}
				measurePackageService.saveMeasureXml(measureXmlModel);
			}
		} else {
			XmlProcessor processor = new XmlProcessor(measureXmlModel.getXml());
			processor.addParentNode(MEASURE);
			processor.checkForScoringType(MATPropertiesService.get().getQmdVersion());
			processor.transform(processor.getOriginalDoc());
			try {
				String libraryName = (String) xPath.evaluate("/measure/measureDetails/title/text()",
						processor.getOriginalDoc().getDocumentElement(), XPathConstants.STRING);
				String version = (String) xPath.evaluate("/measure/measureDetails/version/text()",
						processor.getOriginalDoc().getDocumentElement(), XPathConstants.STRING);

				XmlProcessor cqlXmlProcessor = cqlLibraryService.loadCQLXmlTemplateFile();
				String cqlLookUpTag = cqlLibraryService.getCQLLookUpXml((MeasureUtility.cleanString(libraryName)),
						version, cqlXmlProcessor, "//measure");
				if (StringUtils.isNotBlank(cqlLookUpTag)) {
					processor.appendNode(cqlLookUpTag, CQL_LOOKUP, "/measure");

					Document measureXMLDocument = processor.getOriginalDoc();

					String supplementalDataElementsXPath = "/measure/supplementalDataElements";
					try {

						Node supplementalDataElementNode = processor.findNode(measureXMLDocument,
								supplementalDataElementsXPath);

						// create a tag "<supplementalDataElements>" under
						// "<measure>" tag if "<supplementalDataElements>" is
						// not present.
						if (supplementalDataElementNode == null) {
							supplementalDataElementNode = measureXMLDocument.getFirstChild()
									.appendChild(measureXMLDocument.createElement("supplementalDataElements"));
						}

						// append CQL definitions created for supplemental
						// section to supplementalDataElement tag
						NodeList defaultCQLDefNodeList = findDefaultDefinitions(processor);
						if (defaultCQLDefNodeList != null) {
							// create "<cqldefinition>" tag with displayName and
							// uuid pointing to the default CQL definitions and
							// append it to "<supplementalDataElements>"
							for (int i = 0; i < defaultCQLDefNodeList.getLength(); i++) {
								Node cqlDefNode = defaultCQLDefNodeList.item(i);
								Element cqlDefinitionRefNode = measureXMLDocument.createElement("cqldefinition");
								cqlDefinitionRefNode.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME,
										cqlDefNode.getAttributes().getNamedItem(NAME).getNodeValue());
								cqlDefinitionRefNode.setAttribute(PopulationWorkSpaceConstants.UUID,
										cqlDefNode.getAttributes().getNamedItem(ID).getNodeValue());
								supplementalDataElementNode.appendChild(cqlDefinitionRefNode);
							}
						}
						measureXmlModel.setXml(processor.transform(processor.getOriginalDoc()));
						measurePackageService.saveMeasureXml(measureXmlModel);
					} catch (XPathExpressionException e) {
						logger.debug("Exception in saveMeasureXml while updating cqldefinition :" + e);
					}

					measureXmlModel.setXml(processor.transform(processor.getOriginalDoc()));
				} else {
					logger.info("Measure Xml save failed for measure " + measureXmlModel.getMeasureId());
				}

			} catch (Exception e) {
				logger.debug("Exception in saveMeasureXml :" + e);
			}
		}
	}

	/**
	 * Update CQL version.
	 *
	 * @param processor
	 *            the processor
	 */
	private void updateCQLVersion(XmlProcessor processor) {
		String cqlVersionXPath = "//cqlLookUp/version";
		try {
			String version = (String) xPath.evaluate("/measure/measureDetails/version/text()",
					processor.getOriginalDoc().getDocumentElement(), XPathConstants.STRING);
			Node node = (Node) xPath.evaluate(cqlVersionXPath, processor.getOriginalDoc().getDocumentElement(),
					XPathConstants.NODE);
			if (node != null) {
				node.setTextContent(version);
			}

		} catch (XPathExpressionException e) {
			logger.debug("updateCQLVersion:" + e.getMessage());
		}

	}

	/**
	 * Deletes the existing groupings when scoring type selection is changed and
	 * saved.
	 *
	 * @param xmlProcessor
	 *            the xml processor
	 */
	private void deleteExistingGroupings(XmlProcessor xmlProcessor) {
		NodeList measureGroupingList;
		try {
			measureGroupingList = (NodeList) xPath.evaluate("/measure/measureGrouping/group",
					xmlProcessor.getOriginalDoc().getDocumentElement(), XPathConstants.NODESET);
			for (int i = 0; i < measureGroupingList.getLength(); i++) {
				removeNode("/measure/measureGrouping/group", xmlProcessor.getOriginalDoc());
			}
		} catch (XPathExpressionException e) {
			logger.debug("deleteExistingGroupings:" + e.getMessage());
		}
	}

	@Override
	public final ManageMeasureSearchModel search(MeasureSearchModel measureSearchModel) {
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();
		if (SecurityRole.ADMIN_ROLE.equals(userRole)) {
			List<MeasureShareDTO> measureList = measurePackageService.searchForAdminWithFilter(
					measureSearchModel.getSearchTerm(), 1, Integer.MAX_VALUE, measureSearchModel.getIsMyMeasureSearch());

			List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<>();
			List<MeasureShareDTO> measureTotalList = measureList;
			searchModel.setResultsTotal(measureTotalList.size());
			if (measureSearchModel.getPageSize() < measureTotalList.size()) {
				measureList = measureTotalList.subList(measureSearchModel.getStartIndex() - 1,
						measureSearchModel.getPageSize());
			} else if (measureSearchModel.getPageSize() > measureList.size()) {
				measureList = measureTotalList.subList(measureSearchModel.getStartIndex() - 1, measureList.size());
			}
			searchModel.setStartIndex(measureSearchModel.getStartIndex());
			searchModel.setData(detailModelList);

			for (MeasureShareDTO dto : measureList) {
				ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
				detail.setName(dto.getMeasureName());
				detail.setId(dto.getMeasureId());
				detail.seteMeasureId(dto.geteMeasureId());
				detail.setDraft(dto.isDraft());
				String formattedVersion = MeasureUtility.getVersionText(dto.getVersion(), dto.isDraft());
				detail.setVersion(formattedVersion);
				detail.setFinalizedDate(dto.getFinalizedDate());
				detail.setStatus(dto.getStatus());
				User user = userService.getById(dto.getOwnerUserId());
				detail.setOwnerfirstName(user.getFirstName());
				detail.setOwnerLastName(user.getLastName());
				detail.setOwnerEmailAddress(user.getEmailAddress());
				detail.setMeasureSetId(dto.getMeasureSetId());
				detail.setPatientBased(dto.isPatientBased());
				detailModelList.add(detail);
			}
		} else {
			List<MeasureShareDTO> measureList = measurePackageService.searchWithFilter(measureSearchModel);
			List<MeasureShareDTO> measureTotalList = measureList;

			searchModel.setResultsTotal(measureTotalList.size());
			if (measureSearchModel.getPageSize() <= measureTotalList.size()) {
				measureList = measureTotalList.subList(measureSearchModel.getStartIndex() - 1,
						measureSearchModel.getPageSize());
			} else if (measureSearchModel.getPageSize() > measureList.size()) {
				measureList = measureTotalList.subList(measureSearchModel.getStartIndex() - 1, measureList.size());
			}
			searchModel.setStartIndex(measureSearchModel.getStartIndex());
			List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<>();
			searchModel.setData(detailModelList);
			for (MeasureShareDTO dto : measureList) {
				ManageMeasureSearchModel.Result detail = extractMeasureSearchModelDetail(currentUserId, isSuperUser,
						dto);
				detailModelList.add(detail);
			}
			updateMeasureFamily(detailModelList);
		}
		
		return searchModel;
	}

	/**
	 * Update measure family.
	 *
	 * @param detailModelList
	 *            the detail model list
	 */
	public void updateMeasureFamily(List<ManageMeasureSearchModel.Result> detailModelList) {
		boolean isFamily = false;
		if (detailModelList != null && !detailModelList.isEmpty()) {
			for (int i = 0; i < detailModelList.size(); i++) {
				if (i > 0) {
					if (detailModelList.get(i).getMeasureSetId()
							.equalsIgnoreCase(detailModelList.get(i - 1).getMeasureSetId())) {
						detailModelList.get(i).setMeasureFamily(!isFamily);
					} else {
						detailModelList.get(i).setMeasureFamily(isFamily);
					}
				} else {
					detailModelList.get(i).setMeasureFamily(isFamily);
				}
			}
		}
	}

	@Override
	public final TransferOwnerShipModel searchUsers(final String searchText, final int startIndex, final int pageSize) {
		UserService usersService = userService;
		List<User> searchResults;
		if (searchText.equals("")) {
			searchResults = usersService.searchNonAdminUsers("", startIndex, pageSize);
		} else {
			searchResults = usersService.searchNonAdminUsers(searchText, startIndex, pageSize);
		}
		logger.info("User search returned " + searchResults.size());

		TransferOwnerShipModel result = new TransferOwnerShipModel();
		List<TransferOwnerShipModel.Result> detailList = new ArrayList<>();
		for (User user : searchResults) {
			TransferOwnerShipModel.Result r = new TransferOwnerShipModel.Result();
			r.setFirstName(user.getFirstName());
			r.setLastName(user.getLastName());
			r.setEmailId(user.getEmailAddress());
			r.setKey(user.getId());
			detailList.add(r);
		}
		result.setData(detailList);
		result.setStartIndex(startIndex);
		result.setResultsTotal(searchResults.size());

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
	private void setAdditionalAttrsForMeasureXml(final ManageMeasureDetailModel measureDetailModel,
			final Measure measure) {
		logger.info("In MeasureLibraryServiceImpl.setAdditionalAttrsForMeasureXml()");
		measureDetailModel.setId(measure.getId());
		measureDetailModel.setMeasureSetId(measure.getMeasureSet() != null ? measure.getMeasureSet().getId() : null);
		measureDetailModel.setOrgVersionNumber(MeasureUtility.formatVersionText(measureDetailModel.getRevisionNumber(),
				String.valueOf(measure.getVersionNumber())));
		measureDetailModel.setVersionNumber(MeasureUtility.getVersionText(measureDetailModel.getOrgVersionNumber(),
				measureDetailModel.getRevisionNumber(), measure.isDraft()));
		measureDetailModel.setId(UuidUtility.idToUuid(measureDetailModel.getId())); // have to change on unmarshalling.
		PeriodModel periodModel = new PeriodModel();
		periodModel.setUuid(UUID.randomUUID().toString());
		// for New measures checking Calender year to add Default Dates
		if (!isMeasureCreated()) {
			measureDetailModel.setCalenderYear(true);
		}
		periodModel.setCalenderYear(measureDetailModel.isCalenderYear());
		if (!measureDetailModel.isCalenderYear()) {
			periodModel.setStartDate(measureDetailModel.getMeasFromPeriod());
			periodModel.setStopDate(measureDetailModel.getMeasToPeriod());
		} else { // for Default Dates
			periodModel.setStartDate("01/01/20XX");
			periodModel.setStopDate("12/31/20XX");
		}

		measureDetailModel.setPeriodModel(periodModel);

		if (StringUtils.isNotBlank(measureDetailModel.getGroupName())) {
			measureDetailModel.setQltyMeasureSetUuid(UUID.randomUUID().toString());
		}

		measureDetailModel.setScoringAbbr(setScoringAbbreviation(measureDetailModel.getMeasScoring()));

		if (measureDetailModel instanceof ManageCompositeMeasureDetailModel) {
			((ManageCompositeMeasureDetailModel) measureDetailModel)
					.setCompositeScoringAbbreviation(getCompositeScoringAbbreviation(
							((ManageCompositeMeasureDetailModel) measureDetailModel).getCompositeScoringMethod()));
		}

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
	 * Sets the measure package service.
	 * 
	 * @param measurePackagerService
	 *            the new measure package service
	 */
	public final void setMeasurePackageService(final MeasurePackageService measurePackagerService) {
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

	private String getCompositeScoringAbbreviation(final String compositeMeasureScoring) {
		return MeasureDetailsUtil.getCompositeScoringAbbreviation(compositeMeasureScoring);
	}

	/**
	 * Sets the user service.
	 * 
	 * @param usersService
	 *            the new user service
	 */
	public final void setUserService(final UserService usersService) {
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
		measure.setPatientBased(model.isPatientBased());
		measure.setVersion(model.getVersionNumber());
		measure.setDraft(model.isDraft());
		measure.setRevisionNumber(model.getRevisionNumber());
		measure.seteMeasureId(model.geteMeasureId());

		if ((model.getFinalizedDate() != null) && !model.getFinalizedDate().equals("")) {
			measure.setFinalizedDate(
					new Timestamp(DateUtility.convertStringToDate(model.getFinalizedDate()).getTime()));
		}
		if ((model.getValueSetDate() != null) && !model.getValueSetDate().equals("")) {
			measure.setValueSetDate(new Timestamp(DateUtility.convertStringToDate(model.getValueSetDate()).getTime()));
		}
	}

	@Override
	public final void transferOwnerShipToUser(final List<String> list, final String toEmail) {
		MeasurePackageService service = measurePackageService;
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
		// XPath Expression to find all elementRefs in elementLookUp for to be
		// modified QDM.
		String XPATH_EXPRESSION_ELEMENTLOOKUP = "/measure/elementLookUp/qdm[@uuid='" + modifyDTO.getUuid() + "']";
		try {
			NodeList nodesElementLookUp = (NodeList) xPath.evaluate(XPATH_EXPRESSION_ELEMENTLOOKUP,
					processor.getOriginalDoc(), XPathConstants.NODESET);
			if (nodesElementLookUp.getLength() > 1) {
				Node parentNode = nodesElementLookUp.item(0).getParentNode();
				if (parentNode.getAttributes().getNamedItem(VSAC_EXP_IDENTIFIER) != null) {
					if (StringUtils.isNotBlank(modifyWithDTO.getVsacExpIdentifier())) {
						parentNode.getAttributes().getNamedItem(VSAC_EXP_IDENTIFIER)
								.setNodeValue(modifyWithDTO.getExpansionIdentifier());
					} else {
						parentNode.getAttributes().removeNamedItem(VSAC_EXP_IDENTIFIER);
					}
				} else {
					if (StringUtils.isNotEmpty(modifyWithDTO.getExpansionIdentifier())) {
						Attr vsacExpIdentifierAttr = processor.getOriginalDoc().createAttribute(VSAC_EXP_IDENTIFIER);
						vsacExpIdentifierAttr.setNodeValue(modifyWithDTO.getVsacExpIdentifier());
						parentNode.getAttributes().setNamedItem(vsacExpIdentifierAttr);
					}
				}
			}
			for (int i = 0; i < nodesElementLookUp.getLength(); i++) {
				Node newNode = nodesElementLookUp.item(i);
				newNode.getAttributes().getNamedItem(NAME).setNodeValue(modifyWithDTO.getCodeListName());
				newNode.getAttributes().getNamedItem(ID).setNodeValue(modifyWithDTO.getId());
				if ((newNode.getAttributes().getNamedItem(CODE_SYSTEM_NAME) == null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					Attr attrNode = processor.getOriginalDoc().createAttribute(CODE_SYSTEM_NAME);
					attrNode.setNodeValue(modifyWithDTO.getCodeSystemName());
					newNode.getAttributes().setNamedItem(attrNode);
				} else if ((newNode.getAttributes().getNamedItem(CODE_SYSTEM_NAME) != null)
						&& (modifyWithDTO.getCodeSystemName() == null)) {
					newNode.getAttributes().getNamedItem(CODE_SYSTEM_NAME).setNodeValue(null);
				} else if ((newNode.getAttributes().getNamedItem(CODE_SYSTEM_NAME) != null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					newNode.getAttributes().getNamedItem(CODE_SYSTEM_NAME)
							.setNodeValue(modifyWithDTO.getCodeSystemName());
				}
				newNode.getAttributes().getNamedItem(DATATYPE).setNodeValue(modifyWithDTO.getDataType());
				newNode.getAttributes().getNamedItem(OID).setNodeValue(modifyWithDTO.getOid());
				newNode.getAttributes().getNamedItem(TAXONOMY).setNodeValue(modifyWithDTO.getTaxonomy());
				newNode.getAttributes().getNamedItem(VERSION).setNodeValue(modifyWithDTO.getVersion());
				if (modifyWithDTO.isSuppDataElement()) {
					newNode.getAttributes().getNamedItem(SUPP_DATA_ELEMENT).setNodeValue("true");
				} else {
					newNode.getAttributes().getNamedItem(SUPP_DATA_ELEMENT).setNodeValue("false");
				}
				if (newNode.getAttributes().getNamedItem(INSTANCE) != null) {
					if (StringUtils.isNotBlank(modifyWithDTO.getOccurrenceText())) {
						newNode.getAttributes().getNamedItem(INSTANCE).setNodeValue(modifyWithDTO.getOccurrenceText());
					} else {
						newNode.getAttributes().removeNamedItem(INSTANCE);
					}
				} else {
					if (StringUtils.isNotEmpty(modifyWithDTO.getOccurrenceText())) {
						Attr instance = processor.getOriginalDoc().createAttribute(INSTANCE);
						instance.setNodeValue(modifyWithDTO.getOccurrenceText());
						newNode.getAttributes().setNamedItem(instance);
					}
				}
				if (newNode.getAttributes().getNamedItem(EFFECTIVE_DATE) != null) {
					if (StringUtils.isNotBlank(modifyWithDTO.getEffectiveDate())) {
						newNode.getAttributes().getNamedItem(EFFECTIVE_DATE)
								.setNodeValue(modifyWithDTO.getEffectiveDate());
					} else {
						newNode.getAttributes().removeNamedItem(EFFECTIVE_DATE);
					}
				} else {
					if (StringUtils.isNotEmpty(modifyWithDTO.getEffectiveDate())) {
						Attr effectiveDateAttr = processor.getOriginalDoc().createAttribute(EFFECTIVE_DATE);
						effectiveDateAttr.setNodeValue(modifyWithDTO.getEffectiveDate());
						newNode.getAttributes().setNamedItem(effectiveDateAttr);
					}
				}

				if (newNode.getAttributes().getNamedItem(EXPANSION_IDENTIFIER) != null) {
					if (StringUtils.isNotBlank(modifyWithDTO.getExpansionIdentifier())) {
						newNode.getAttributes().getNamedItem(EXPANSION_IDENTIFIER)
								.setNodeValue(modifyWithDTO.getExpansionIdentifier());
					} else {
						newNode.getAttributes().removeNamedItem(EXPANSION_IDENTIFIER);
					}
				} else {
					if (StringUtils.isNotEmpty(modifyWithDTO.getExpansionIdentifier())) {
						Attr expansionIdentifierAttr = processor.getOriginalDoc().createAttribute(EXPANSION_IDENTIFIER);
						expansionIdentifierAttr.setNodeValue(modifyWithDTO.getExpansionIdentifier());
						newNode.getAttributes().setNamedItem(expansionIdentifierAttr);
					}
				}
			}
		} catch (XPathExpressionException e) {
			logger.debug("updateElementLookUp: " + e);
		}
		logger.debug(" MeasureLibraryServiceImpl: updateElementLookUp End :  ");
	}

	@Override
	public void updateCQLLookUpTagWithModifiedValueSet(final CQLQualityDataSetDTO modifyWithDTO,
			final CQLQualityDataSetDTO modifyDTO, final String measureId) {
		MeasureXmlModel model = getMeasureXmlForMeasure(measureId);
		logger.debug(" MeasureLibraryServiceImpl: updateCQLLookUpTagWithModifiedValueSet Start :  ");

		if (model != null) {
			SaveUpdateCQLResult result = cqlService.updateCQLLookUpTag(model.getXml(), modifyWithDTO, modifyDTO);
			if (result != null && result.isSuccess()) {
				model.setXml(result.getXml());
				measurePackageService.saveMeasureXml(model);
			}
		}
		logger.debug(" MeasureLibraryServiceImpl: updateCQLLookUpTagWithModifiedValueSet End :  ");
	}

	/**
	 * This method has been added to update the measureLock Date. This method first
	 * gets the exisitingMeasure and then adds the lockedOutDate if it is not there.
	 */
	@Override
	public final SaveMeasureResult updateLockedDate(final String measureId, final String userId) {
		Measure existingmeasure = null;
		User user = null;
		SaveMeasureResult result = new SaveMeasureResult();
		if ((measureId != null) && (userId != null)) {
			existingmeasure = measurePackageService.getById(measureId);
			if (existingmeasure != null) {
				if (!isLocked(existingmeasure)) {
					user = userService.getById(userId);
					existingmeasure.setLockedUser(user);
					existingmeasure.setLockedOutDate(new Timestamp(new Date().getTime()));
					measurePackageService.save(existingmeasure);
					result.setSuccess(true);
				}
			}
		}

		result.setId(existingmeasure.getId());
		return result;
	}

	/**
	 * This method updates MeasureXML - ElementLookUpNode,ElementRef's under
	 * Population Node and Stratification Node, SupplementDataElements. It also
	 * removes attributes nodes if there is mismatch in data types of newly selected
	 * QDM and already applied QDM.
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
	public final void updateMeasureXML(final QualityDataSetDTO modifyWithDTO, final QualityDataSetDTO modifyDTO,
			final String measureId) {
		logger.debug(" MeasureLibraryServiceImpl: updateMeasureXML Start : Measure Id :: " + measureId);
		MeasureXmlModel model = getMeasureXmlForMeasure(measureId);

		if (model != null) {
			XmlProcessor processor = new XmlProcessor(model.getXml());
			if (modifyDTO.isUsed()) {
				// Update all elementRef's in SubTreeLookUp
				updateSubTreeLookUp(processor, modifyWithDTO, modifyDTO);

				// update elementLookUp Tag
				updateElementLookUp(processor, modifyWithDTO, modifyDTO);
				// update cqlLookUp Tag
				updateCQLLookUp(processor, modifyWithDTO, modifyDTO);
				updateSupplementalDataElement(processor, modifyWithDTO, modifyDTO);
				model.setXml(processor.transform(processor.getOriginalDoc()));
				measurePackageService.saveMeasureXml(model);

			} else {
				// update elementLookUp Tag
				updateElementLookUp(processor, modifyWithDTO, modifyDTO);
				// update cqlLookUp Tag
				updateCQLLookUp(processor, modifyWithDTO, modifyDTO);
				model.setXml(processor.transform(processor.getOriginalDoc()));
				measurePackageService.saveMeasureXml(model);
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
		// XPath Expression to find all elementRefs in elementLookUp for to be
		// modified QDM.
		String XPATH_EXPRESSION_CQLLOOKUP = "/measure/cqlLookUp/valuesets/valueset[@uuid='" + modifyDTO.getUuid()
				+ "']";
		try {
			NodeList nodesElementLookUp = (NodeList) xPath.evaluate(XPATH_EXPRESSION_CQLLOOKUP,
					processor.getOriginalDoc(), XPathConstants.NODESET);
			if (nodesElementLookUp.getLength() > 1) {
				Node parentNode = nodesElementLookUp.item(0).getParentNode();
				if (parentNode.getAttributes().getNamedItem(VSAC_EXP_IDENTIFIER) != null) {
					if (StringUtils.isNotBlank(modifyWithDTO.getVsacExpIdentifier())) {
						parentNode.getAttributes().getNamedItem(VSAC_EXP_IDENTIFIER)
								.setNodeValue(modifyWithDTO.getExpansionIdentifier());
					} else {
						parentNode.getAttributes().removeNamedItem(VSAC_EXP_IDENTIFIER);
					}
				} else {
					if (StringUtils.isNotEmpty(modifyWithDTO.getExpansionIdentifier())) {
						Attr vsacExpIdentifierAttr = processor.getOriginalDoc().createAttribute(VSAC_EXP_IDENTIFIER);
						vsacExpIdentifierAttr.setNodeValue(modifyWithDTO.getVsacExpIdentifier());
						parentNode.getAttributes().setNamedItem(vsacExpIdentifierAttr);
					}
				}
			}
			for (int i = 0; i < nodesElementLookUp.getLength(); i++) {
				Node newNode = nodesElementLookUp.item(i);
				newNode.getAttributes().getNamedItem(NAME).setNodeValue(modifyWithDTO.getCodeListName());
				newNode.getAttributes().getNamedItem(ID).setNodeValue(modifyWithDTO.getId());
				if ((newNode.getAttributes().getNamedItem(CODE_SYSTEM_NAME) == null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					Attr attrNode = processor.getOriginalDoc().createAttribute(CODE_SYSTEM_NAME);
					attrNode.setNodeValue(modifyWithDTO.getCodeSystemName());
					newNode.getAttributes().setNamedItem(attrNode);
				} else if ((newNode.getAttributes().getNamedItem(CODE_SYSTEM_NAME) != null)
						&& (modifyWithDTO.getCodeSystemName() == null)) {
					newNode.getAttributes().getNamedItem(CODE_SYSTEM_NAME).setNodeValue(null);
				} else if ((newNode.getAttributes().getNamedItem(CODE_SYSTEM_NAME) != null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					newNode.getAttributes().getNamedItem(CODE_SYSTEM_NAME)
							.setNodeValue(modifyWithDTO.getCodeSystemName());
				}
				newNode.getAttributes().getNamedItem(DATATYPE).setNodeValue(modifyWithDTO.getDataType());
				newNode.getAttributes().getNamedItem(OID).setNodeValue(modifyWithDTO.getOid());
				newNode.getAttributes().getNamedItem(TAXONOMY).setNodeValue(modifyWithDTO.getTaxonomy());
				newNode.getAttributes().getNamedItem(VERSION).setNodeValue(modifyWithDTO.getVersion());
				if (modifyWithDTO.isSuppDataElement()) {
					newNode.getAttributes().getNamedItem(SUPP_DATA_ELEMENT).setNodeValue("true");
				} else {
					newNode.getAttributes().getNamedItem(SUPP_DATA_ELEMENT).setNodeValue("false");
				}
				if (newNode.getAttributes().getNamedItem(INSTANCE) != null) {
					if (StringUtils.isNotBlank(modifyWithDTO.getOccurrenceText())) {
						newNode.getAttributes().getNamedItem(INSTANCE).setNodeValue(modifyWithDTO.getOccurrenceText());
					} else {
						newNode.getAttributes().removeNamedItem(INSTANCE);
					}
				} else {
					if (StringUtils.isNotEmpty(modifyWithDTO.getOccurrenceText())) {
						Attr instance = processor.getOriginalDoc().createAttribute(INSTANCE);
						instance.setNodeValue(modifyWithDTO.getOccurrenceText());
						newNode.getAttributes().setNamedItem(instance);
					}
				}
				if (newNode.getAttributes().getNamedItem(EFFECTIVE_DATE) != null) {
					if (StringUtils.isNotBlank(modifyWithDTO.getEffectiveDate())) {
						newNode.getAttributes().getNamedItem(EFFECTIVE_DATE)
								.setNodeValue(modifyWithDTO.getEffectiveDate());
					} else {
						newNode.getAttributes().removeNamedItem(EFFECTIVE_DATE);
					}
				} else {
					if (StringUtils.isNotEmpty(modifyWithDTO.getEffectiveDate())) {
						Attr effectiveDateAttr = processor.getOriginalDoc().createAttribute(EFFECTIVE_DATE);
						effectiveDateAttr.setNodeValue(modifyWithDTO.getEffectiveDate());
						newNode.getAttributes().setNamedItem(effectiveDateAttr);
					}
				}

				if (newNode.getAttributes().getNamedItem(EXPANSION_IDENTIFIER) != null) {
					if (StringUtils.isNotBlank(modifyWithDTO.getExpansionIdentifier())) {
						newNode.getAttributes().getNamedItem(EXPANSION_IDENTIFIER)
								.setNodeValue(modifyWithDTO.getExpansionIdentifier());
					} else {
						newNode.getAttributes().removeNamedItem(EXPANSION_IDENTIFIER);
					}
				} else {
					if (StringUtils.isNotEmpty(modifyWithDTO.getExpansionIdentifier())) {
						Attr expansionIdentifierAttr = processor.getOriginalDoc().createAttribute(EXPANSION_IDENTIFIER);
						expansionIdentifierAttr.setNodeValue(modifyWithDTO.getExpansionIdentifier());
						newNode.getAttributes().setNamedItem(expansionIdentifierAttr);
					}
				}
			}
		} catch (XPathExpressionException e) {
			logger.debug("Exception in updateCQLLookUp: " + e);
		}
		logger.debug(" MeasureLibraryServiceImpl: updateCQLLookUp End :  ");

	}

	/**
	 * Update measure xml for qdm.
	 *
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param xmlprocessor
	 *            the xmlprocessor
	 * @param expansionIdentifier
	 *            the expansion identifier
	 */
	private void updateMeasureXmlForQDM(final QualityDataSetDTO modifyWithDTO, XmlProcessor xmlprocessor,
			String expansionIdentifier) {
		String XPATH_EXPRESSION_ELEMENTLOOKUP = "/measure/elementLookUp/qdm[@uuid='" + modifyWithDTO.getUuid() + "']";
		NodeList nodesElementLookUp;
		try {
			nodesElementLookUp = (NodeList) xPath.evaluate(XPATH_EXPRESSION_ELEMENTLOOKUP,
					xmlprocessor.getOriginalDoc(), XPathConstants.NODESET);

			for (int i = 0; i < nodesElementLookUp.getLength(); i++) {
				Node newNode = nodesElementLookUp.item(i);
				newNode.getAttributes().getNamedItem(VERSION).setNodeValue("1.0");
				if (newNode.getAttributes().getNamedItem(EXPANSION_IDENTIFIER) != null) {
					if (StringUtils.isNotBlank(modifyWithDTO.getExpansionIdentifier())) {
						newNode.getAttributes().getNamedItem(EXPANSION_IDENTIFIER).setNodeValue(expansionIdentifier);
					} else {
						newNode.getAttributes().removeNamedItem(EXPANSION_IDENTIFIER);
					}
				} else {
					if (StringUtils.isNotEmpty(expansionIdentifier)) {
						Attr expansionIdentifierAttr = xmlprocessor.getOriginalDoc()
								.createAttribute(EXPANSION_IDENTIFIER);
						expansionIdentifierAttr.setNodeValue(expansionIdentifier);
						newNode.getAttributes().setNamedItem(expansionIdentifierAttr);
					}
				}
			}
		} catch (XPathExpressionException e) {
			logger.debug("Exception in updateMeasureXmlForQDM: " + e);
		}
	}

	/**
	 * Update measure xml for qdm.
	 *
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param xmlprocessor
	 *            the xmlprocessor
	 * @param expansionIdentifier
	 *            the expansion identifier
	 */
	private void updateCQLMeasureXmlForQDM(final CQLQualityDataSetDTO modifyWithDTO, XmlProcessor xmlprocessor,
			String expansionIdentifier) {
		String XPATH_EXPRESSION_ELEMENTLOOKUP = "/measure/cqlLookUp/valuesets/valueset[@uuid='"
				+ modifyWithDTO.getUuid() + "']";
		NodeList nodesElementLookUp;
		try {
			nodesElementLookUp = (NodeList) xPath.evaluate(XPATH_EXPRESSION_ELEMENTLOOKUP,
					xmlprocessor.getOriginalDoc(), XPathConstants.NODESET);

			for (int i = 0; i < nodesElementLookUp.getLength(); i++) {
				Node newNode = nodesElementLookUp.item(i);
				newNode.getAttributes().getNamedItem(VERSION).setNodeValue("1.0");
			}
		} catch (XPathExpressionException e) {
			logger.debug("Exception in updateCQLMeasureXmlForQDM: " + e);
		}
	}

	@Override
	public void updateMeasureXMLForExpansionIdentifier(List<QualityDataSetDTO> modifyWithDTOList, String measureId,
			String expansionIdentifier) {
		logger.debug(" MeasureLibraryServiceImpl: updateMeasureXMLForExpansionIdentifier Start : Measure Id :: "
				+ measureId);
		MeasureXmlModel model = getMeasureXmlForMeasure(measureId);
		if (model != null) {
			XmlProcessor processor = new XmlProcessor(model.getXml());
			String XPATH_EXP_FOR_ELEMENTLOOKUP_ATTR = "/measure/elementLookUp";
			try {
				Node nodesElementLookUp = (Node) xPath.evaluate(XPATH_EXP_FOR_ELEMENTLOOKUP_ATTR,
						processor.getOriginalDoc(), XPathConstants.NODE);
				if (nodesElementLookUp != null) {
					if (nodesElementLookUp.getAttributes().getNamedItem(VSAC_EXP_IDENTIFIER) != null) {
						if (StringUtils.isNotBlank(expansionIdentifier)) {
							nodesElementLookUp.getAttributes().getNamedItem(VSAC_EXP_IDENTIFIER)
									.setNodeValue(expansionIdentifier);
						} else {
							nodesElementLookUp.getAttributes().removeNamedItem(VSAC_EXP_IDENTIFIER);
						}
					} else {
						if (StringUtils.isNotEmpty(expansionIdentifier)) {
							Attr vsacExpIdentifierAttr = processor.getOriginalDoc()
									.createAttribute(VSAC_EXP_IDENTIFIER);
							vsacExpIdentifierAttr.setNodeValue(expansionIdentifier);
							nodesElementLookUp.getAttributes().setNamedItem(vsacExpIdentifierAttr);
						}
					}
				}
				for (QualityDataSetDTO dto : modifyWithDTOList) {
					updateMeasureXmlForQDM(dto, processor, expansionIdentifier);
				}
			} catch (XPathExpressionException e) {
				logger.debug("Exception in updateMeasureXMLForExpansionIdentifier: " + e);
			}

			model.setXml(processor.transform(processor.getOriginalDoc()));
			measurePackageService.saveMeasureXml(model);
		}

	}

	@Override
	public void updateCQLMeasureXMLForExpansionIdentifier(List<CQLQualityDataSetDTO> modifyWithDTOList,
			String measureId, String expansionIdentifier) {
		logger.debug(" MeasureLibraryServiceImpl: updateMeasureXMLForExpansionIdentifier Start : Measure Id :: "
				+ measureId);
		MeasureXmlModel model = getMeasureXmlForMeasure(measureId);
		if (model != null) {
			XmlProcessor processor = new XmlProcessor(model.getXml());
			String XPATH_EXP_FOR_ELEMENTLOOKUP_ATTR = "/measure/cqlLookUp/valuesets";
			try {
				Node nodesElementLookUp = (Node) xPath.evaluate(XPATH_EXP_FOR_ELEMENTLOOKUP_ATTR,
						processor.getOriginalDoc(), XPathConstants.NODE);
				if (nodesElementLookUp != null) {
					if (nodesElementLookUp.getAttributes().getNamedItem(VSAC_EXP_IDENTIFIER) != null) {
						if (StringUtils.isNotBlank(expansionIdentifier)) {
							nodesElementLookUp.getAttributes().getNamedItem(VSAC_EXP_IDENTIFIER)
									.setNodeValue(expansionIdentifier);
						} else {
							nodesElementLookUp.getAttributes().removeNamedItem(VSAC_EXP_IDENTIFIER);
						}
					} else {
						if (StringUtils.isNotEmpty(expansionIdentifier)) {
							Attr vsacExpIdentifierAttr = processor.getOriginalDoc()
									.createAttribute(VSAC_EXP_IDENTIFIER);
							vsacExpIdentifierAttr.setNodeValue(expansionIdentifier);
							nodesElementLookUp.getAttributes().setNamedItem(vsacExpIdentifierAttr);
						}
					}
				}
				for (CQLQualityDataSetDTO dto : modifyWithDTOList) {
					updateCQLMeasureXmlForQDM(dto, processor, expansionIdentifier);
				}
			} catch (XPathExpressionException e) {
				logger.debug("Exception in updateCQLMeasureXMLForExpansionIdentifier: " + e);
			}

			model.setXml(processor.transform(processor.getOriginalDoc()));
			measurePackageService.saveMeasureXml(model);
		}

	}

	/**
	 * Update sub tree look up.
	 *
	 * @param processor
	 *            the processor
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param modifyDTO
	 *            the modify dto
	 */
	private void updateSubTreeLookUp(final XmlProcessor processor, final QualityDataSetDTO modifyWithDTO,
			final QualityDataSetDTO modifyDTO) {

		logger.debug(" MeasureLibraryServiceImpl: updateSubTreeLookUp Start :  ");
		// XPath to find All elementRef's under subTreeLookUp element nodes for
		// to be modified QDM.
		String XPATH_EXPRESSION_SubTreeLookUp_ELEMENTREF = "/measure//subTreeLookUp//elementRef[@id='"
				+ modifyDTO.getUuid() + "']";
		try {
			NodeList nodesClauseWorkSpace = (NodeList) xPath.evaluate(XPATH_EXPRESSION_SubTreeLookUp_ELEMENTREF,
					processor.getOriginalDoc(), XPathConstants.NODESET);
			for (int i = 0; i < nodesClauseWorkSpace.getLength(); i++) {
				Node newNode = nodesClauseWorkSpace.item(i);
				String displayName = "";
				if (StringUtils.isNotBlank(modifyWithDTO.getOccurrenceText())) {
					displayName = displayName.concat(modifyWithDTO.getOccurrenceText() + " of ");
				}
				displayName = displayName.concat(modifyWithDTO.getCodeListName() + " : " + modifyWithDTO.getDataType());

				newNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME)
						.setNodeValue(displayName);
			}
		} catch (XPathExpressionException e) {
			logger.debug("Exception in updateSubTreeLookUp: " + e);

		}
		logger.debug(" MeasureLibraryServiceImpl: updateSubTreeLookUp End :  ");
	}

	@Override
	public final void updatePrivateColumnInMeasure(final String measureId, final boolean isPrivate) {
		measurePackageService.updatePrivateColumnInMeasure(measureId, isPrivate);
	}

	/**
	 * This method updates MeasureXML - ElementRef's under SupplementalDataElement
	 * Node.
	 * 
	 * @param processor
	 *            the processor
	 * @param modifyWithDTO
	 *            QualityDataSetDTO
	 * @param modifyDTO
	 *            QualityDataSetDTO
	 */
	private void updateSupplementalDataElement(final XmlProcessor processor, final QualityDataSetDTO modifyWithDTO,
			final QualityDataSetDTO modifyDTO) {

		logger.debug(" MeasureLibraryServiceImpl: updateSupplementalDataElement Start :  ");
		// XPath to find All elementRef's in supplementalDataElements for to be
		// modified QDM.
		String XPATH_EXPRESSION_SDE_ELEMENTREF = "/measure/supplementalDataElements/elementRef[@id='"
				+ modifyDTO.getUuid() + "']";
		try {
			NodeList nodesSDE = (NodeList) xPath.evaluate(XPATH_EXPRESSION_SDE_ELEMENTREF, processor.getOriginalDoc(),
					XPathConstants.NODESET);
			for (int i = 0; i < nodesSDE.getLength(); i++) {
				Node newNode = nodesSDE.item(i);
				newNode.getAttributes().getNamedItem(NAME).setNodeValue(modifyWithDTO.getCodeListName());
			}

		} catch (XPathExpressionException e) {
			logger.debug("updateSupplementalDataElement: " + e);
		}
		logger.debug(" MeasureLibraryServiceImpl: updateSupplementalDataElement End :  ");
	}

	@Override
	public final void updateUsersShare(final ManageMeasureShareModel model) {
		measurePackageService.updateUsersShare(model);
	}	
	
	@Override
	public final ValidateMeasureResult createExports(final String key, final List<MatValueSet> matValueSetList, boolean shouldCreateArtifacts) throws MatException {
		try {
			return measurePackageService.createExports(key, matValueSetList, shouldCreateArtifacts);
		} catch (Exception exc) {
			logger.info("Exception validating export for " + key, exc);
			throw new MatException(exc.getMessage());
		}
	}

	@Override
	public Date getFormattedReleaseDate(String releaseDate) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		Date date = new Date();
		try {
			date = formatter.parse(releaseDate);
		} catch (ParseException e) {
			logger.info("Exception in getFormattedReleaseDate: " + e);
		}
		return date;
	}

	@Override
	public String getHumanReadableForNode(final String measureId, final String populationSubXML) {
		String humanReadableHTML = "";
		try {
			humanReadableHTML = measurePackageService.getHumanReadableForNode(measureId, populationSubXML);
		} catch (Exception e) {
			logger.info("Exception in getHumanReadableForNode: " + e);
		}
		return humanReadableHTML;
	}

	@Override
	public ManageMeasureSearchModel getComponentMeasures(String measureId) {
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();
		List<Measure> componentMeasures = measurePackageService.getComponentMeasuresInfo(measureId);
		List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<>();
		searchModel.setData(detailModelList);
		for (Measure componentMeasure : componentMeasures) {
			ManageMeasureSearchModel.Result detail = extractManageMeasureSearchModelDetail(componentMeasure);
			detailModelList.add(detail);
		}

		return searchModel;
	}

	@Override
	public List<ComponentMeasureTabObject> getCQLLibraryInformationForComponentMeasure(String compositeMeasureId) {
		List<ComponentMeasureTabObject> componentMeasureInformationList = new ArrayList<>();
		Measure compositeMeasure = measureDAO.find(compositeMeasureId);

		List<ComponentMeasure> componentMeasures = compositeMeasure.getComponentMeasures();

		for (ComponentMeasure componentMeasure : componentMeasures) {
			Measure measure = measureDAO.find(componentMeasure.getComponentMeasure().getId());
			String measureName = measure.getDescription();
			String ownerName = measure.getOwner().getFullName();
			String alias = componentMeasure.getAlias();
			String componentId = measure.getId();

			CQLLibrary library = cqlLibraryDAO.getLibraryByMeasureId(componentId);
			String version = library.getVersionNumber() + ".000";
			
			String libraryName = library.getName();
			String data = "";
			try {
				data = new String(library.getCqlXML().getBytes(1, (int) library.getCqlXML().length()));
			} catch (SQLException e) {
				e.printStackTrace();
			}

			CQLModel libraryCQLModel = CQLUtilityClass.getCQLModelFromXML(data);
			String libraryContent = CQLUtilityClass.getCqlString(libraryCQLModel, "");

			ComponentMeasureTabObject o = new ComponentMeasureTabObject(measureName, alias, libraryName, version, ownerName, libraryContent, componentId);
			componentMeasureInformationList.add(o);
		}

		return componentMeasureInformationList;

	}

	/**
	 * Extract manage measure search model detail.
	 *
	 * @param measure
	 *            the measure
	 * @return the manage measure search model. result
	 */
	private ManageMeasureSearchModel.Result extractManageMeasureSearchModelDetail(Measure measure) {
		ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
		detail.setName(measure.getDescription());
		detail.setId(measure.getId());
		String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(measure.getVersion(),
				measure.getRevisionNumber(), measure.isDraft());
		detail.setVersion(formattedVersion);
		detail.setFinalizedDate(measure.getFinalizedDate());
		if (measure.getPatientBased() != null) {
			detail.setPatientBased(measure.getPatientBased());
		}
		return detail;
	}

	@Override
	public ValidateMeasureResult validatePackageGrouping(ManageMeasureDetailModel model) {
		ValidateMeasureResult result = new ValidateMeasureResult();
		logger.debug(" MeasureLibraryServiceImpl: validatePackageGrouping Start :  ");
		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(model.getId());
		if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
			result = validateMeasureXmlAtCreateMeasurePackager(xmlModel);
		} else {
			List<String> message = new ArrayList<>();
			message.add(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			result.setValid(false);
			result.setValidationMessages(message);
		}

		return result;
	}

	@Override
	public ValidateMeasureResult validateMeasureXmlAtCreateMeasurePackager(MeasureXmlModel measureXmlModel) {
		boolean isInvalid = false;
		ValidateMeasureResult result = new ValidateMeasureResult();
		result.setValid(true);
		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		List<String> message = new ArrayList<>();
		message.add(MatContext.get().getMessageDelegate().getINVALID_LOGIC_MEASURE_PACKAGER());
		if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());

			// validate only from MeasureGrouping
			String XPATH_MEASURE_GROUPING = "/measure/measureGrouping/ group/packageClause"
					+ "[not(@uuid = preceding:: group/packageClause/@uuid)]";

			List<String> measureGroupingIDList = new ArrayList<>();

			try {
				NodeList measureGroupingNodeList = (NodeList) xPath.evaluate(XPATH_MEASURE_GROUPING,
						xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);

				for (int i = 0; i < measureGroupingNodeList.getLength(); i++) {
					Node childNode = measureGroupingNodeList.item(i);
					String uuid = childNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID)
							.getNodeValue();
					String type = childNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.TYPE)
							.getNodeValue();
					if (type.equals("stratification")) {
						List<String> stratificationClausesIDlist = getStratificationClasuesIDList(uuid, xmlProcessor);
						measureGroupingIDList.addAll(stratificationClausesIDlist);

					} else {
						measureGroupingIDList.add(uuid);
					}
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

			String uuidXPathString = "";
			for (String uuidString : measureGroupingIDList) {
				uuidXPathString += "@uuid = '" + uuidString + "' or";
			}

			uuidXPathString = uuidXPathString.substring(0, uuidXPathString.lastIndexOf(" or"));
			String XPATH_POPULATION = "/measure//clause[" + uuidXPathString + "]";

			try {
				NodeList measureGroupingNodeList = (NodeList) xPath.evaluate(XPATH_POPULATION,
						xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
				for (int i = 0; i < measureGroupingNodeList.getLength(); i++) {
					Node clauseNode = measureGroupingNodeList.item(i);
					if (clauseNode.hasChildNodes()) {
						Node childNode = clauseNode.getFirstChild();
						if (!childNode.getNodeName().equalsIgnoreCase("cqldefinition")
								&& !childNode.getNodeName().equalsIgnoreCase("cqlaggfunction")
								&& !childNode.getNodeName().equalsIgnoreCase("cqlfunction")) {
							result.setValid(false);
							result.setValidationMessages(message);
							break;
						}
						// aggregate function should have user define function
						// as a child
						if ((childNode.getNodeName().equalsIgnoreCase("cqlaggfunction")
								|| childNode.getNodeName().equalsIgnoreCase("cqlfunction"))
								&& !childNode.hasChildNodes()) {
							result.setValid(false);
							message.clear();
							message.add(
									"Measure Observations within a Measure Grouping must contain both an Aggregate Function and a valid User Defined Function.");
							result.setValidationMessages(message);
							break;
						}
					} else {
						result.setValid(false);
						result.setValidationMessages(message);
						break;
					}
				}
				// Parse CQL Data
				if (result.isValid()) {
					isInvalid = parseCQLFile(measureXmlModel.getXml(), measureXmlModel.getMeasureId());
					if (isInvalid) {
						result.setValid(false);
						result.setValidationMessages(message);
					} else {
						Map<Integer, MeasurePackageDetail> seqDetailMap = checkTypeCheckValidationInGroupings(
								measureXmlModel, xmlProcessor);
						if (!seqDetailMap.isEmpty()) {
							List<String> typeCheckErrorMessage = new ArrayList<>();
							typeCheckErrorMessage
									.add(MatContext.get().getMessageDelegate().getCreatePackageTypeCheckError());
							String allMessages = "";
							boolean isTypeCheckInValid = false;
							try {
								for (Map.Entry<Integer, MeasurePackageDetail> entry : seqDetailMap.entrySet()) {
									List<String> messages = PatientBasedValidator.checkPatientBasedValidations(
											xmlModel.getXml(), entry.getValue(), cqlLibraryDAO);
									if (!messages.isEmpty()) {
										allMessages = allMessages + "Grouping " + entry.getKey() + ", ";
										isTypeCheckInValid = true;
									}
								}
								if (isTypeCheckInValid) {
									result.setValid(false);
									int position = allMessages.lastIndexOf(",");
									if (position != -1)
										allMessages = allMessages.substring(0, position);
									typeCheckErrorMessage.add(allMessages
											+ ". Please re-save your measure grouping(s) for additional information.");
									result.setValidationMessages(typeCheckErrorMessage);
								}

							} catch (XPathExpressionException e) {
								typeCheckErrorMessage.add(
										"Unexpected error encountered while doing Group Validations. Please contact HelpDesk.");
							}
						}
					}
				}
			} catch (XPathExpressionException e) {
				logger.debug("validateMeasureXmlAtCreateMeasurePackager: " + e);
			}
		}

		return result;
	}

	/**
	 * @param measureXmlModel
	 * @param xmlProcessor
	 * @return
	 * @throws XPathExpressionException
	 */
	public Map<Integer, MeasurePackageDetail> checkTypeCheckValidationInGroupings(MeasureXmlModel measureXmlModel,
			XmlProcessor xmlProcessor) throws XPathExpressionException {
		// XPath to get all measure Groupings
		NodeList measureGroups = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
				XmlProcessor.XPATH_MEASURE_GROUPING_GROUP);
		Map<Integer, MeasurePackageDetail> seqDetailMap = new HashMap<>();
		// iterate through the measure groupings and get the sequence number
		// attribute and insert in a map with sequence as key and
		// MeasurePackageDetail as value.
		if ((measureGroups != null) && (measureGroups.getLength() > 0)) {
			for (int i = 0; i < measureGroups.getLength(); i++) {
				NamedNodeMap groupAttrs = measureGroups.item(i).getAttributes();
				Integer seq = Integer.parseInt(groupAttrs.getNamedItem("sequence").getNodeValue());
				MeasurePackageDetail detail = seqDetailMap.get(seq);
				if (detail == null) {
					detail = new MeasurePackageDetail();
					detail.setSequence(Integer.toString(seq));
					detail.setMeasureId(measureXmlModel.getMeasureId());
					seqDetailMap.put(seq, detail);
				}
				NodeList pkgClauses = measureGroups.item(i).getChildNodes();
				// Iterate through the PACKAGECLAUSE nodes and
				// convert it into MeasurePackageClauseDetail add it to the list
				// in MeasurePackageDetail
				for (int j = 0; j < pkgClauses.getLength(); j++) {

					if (!PopulationWorkSpaceConstants.PACKAGE_CLAUSE_NODE.equals(pkgClauses.item(j).getNodeName())) {
						// group node can contain tab or new lines which can be counted as it's
						// child.Those should be filtered.
						continue;
					}

					NamedNodeMap pkgClauseMap = pkgClauses.item(j).getAttributes();
					Node associatedClauseNode = pkgClauseMap.getNamedItem("associatedPopulationUUID");
					String associatedClauseNodeUuid = null;
					if (associatedClauseNode != null) {
						associatedClauseNodeUuid = associatedClauseNode.getNodeValue();
					}
					detail.getPackageClauses()
							.add(createMeasurePackageClauseDetail(
									pkgClauseMap.getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue(),
									pkgClauseMap.getNamedItem(NAME).getNodeValue(),
									pkgClauseMap.getNamedItem(PopulationWorkSpaceConstants.TYPE).getNodeValue(),
									associatedClauseNodeUuid));
				}
			}
		}
		return seqDetailMap;
	}

	private MeasurePackageClauseDetail createMeasurePackageClauseDetail(String id, String name, String type,
			String associatedPopulationUUID) {
		MeasurePackageClauseDetail detail = new MeasurePackageClauseDetail();
		detail.setId(id);
		detail.setName(name);
		detail.setType(type);
		detail.setAssociatedPopulationUUID(associatedPopulationUUID);
		return detail;
	}

	/**
	 * Parses the CQL file.
	 *
	 * @param measureXML
	 *            the measure XML
	 * @param measureId
	 *            the measure id
	 * @return true, if successful
	 */
	private boolean parseCQLFile(String measureXML, String measureId) {
		boolean isInvalid = false;
		MeasureXML newXml = getMeasureXMLDAO().findForMeasure(measureId);

		CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureXML);

		SaveUpdateCQLResult cqlResult = CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, null);

		if (cqlResult.getCqlErrors() != null && cqlResult.getCqlErrors().isEmpty()) {
			String exportedXML = ExportSimpleXML.export(newXml, measureDAO, organizationDAO, cqlLibraryDAO, cqlModel);

			CQLModel model = CQLUtilityClass.getCQLModelFromXML(exportedXML);

			SaveUpdateCQLResult result = CQLUtil.parseCQLLibraryForErrors(model, cqlLibraryDAO,
					getExpressionListFromCqlModel(model));

			if (result.getCqlErrors() != null && !result.getCqlErrors().isEmpty()) {
				isInvalid = true;
			} else {
				isInvalid = !CQLUtil.validateDatatypeCombinations(model,
						result.getUsedCQLArtifacts().getValueSetDataTypeMap(),
						result.getUsedCQLArtifacts().getCodeDataTypeMap());
			}
		} else {
			isInvalid = true;
		}
		return isInvalid;
	}

	private List<String> getExpressionListFromCqlModel(CQLModel cqlModel) {
		List<String> expressionList = new ArrayList<>();

		for (CQLDefinition cqlDefinition : cqlModel.getDefinitionList()) {
			expressionList.add(cqlDefinition.getName());
		}

		for (CQLFunctions cqlFunction : cqlModel.getCqlFunctions()) {
			expressionList.add(cqlFunction.getName());
		}

		return expressionList;
	}

	/**
	 * Validate measure xml atby create measure packager.
	 *
	 * @param measureXmlModel
	 *            the measure xml model
	 * @return the validate measure result
	 */
	public ValidateMeasureResult validateMeasureXmlAtbyCreateMeasurePackager(MeasureXmlModel measureXmlModel) {
		boolean isInvalid = false;
		ValidateMeasureResult result = new ValidateMeasureResult();
		result.setValid(true);
		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		List<String> message = new ArrayList<>();
		if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());

			// validate only from MeasureGrouping
			String XAPTH_MEASURE_GROUPING = "/measure/measureGrouping/ group/packageClause"
					+ "[not(@uuid = preceding:: group/packageClause/@uuid)]";

			List<String> measureGroupingIDList = new ArrayList<>();

			try {
				NodeList measureGroupingNodeList = (NodeList) xPath.evaluate(XAPTH_MEASURE_GROUPING,
						xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);

				for (int i = 0; i < measureGroupingNodeList.getLength(); i++) {
					Node childNode = measureGroupingNodeList.item(i);
					String uuid = childNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID)
							.getNodeValue();
					String type = childNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.TYPE)
							.getNodeValue();
					if (type.equals("stratification")) {
						List<String> stratificationClausesIDlist = getStratificationClasuesIDList(uuid, xmlProcessor);
						measureGroupingIDList.addAll(stratificationClausesIDlist);

					} else {
						measureGroupingIDList.add(uuid);
					}
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

			String uuidXPathString = "";
			for (String uuidString : measureGroupingIDList) {
				uuidXPathString += "@uuid = '" + uuidString + "' or";
			}

			uuidXPathString = uuidXPathString.substring(0, uuidXPathString.lastIndexOf(" or"));

			String XPATH_POPULATION_TOP_LEVEL_LOGICAL_OP = "/measure//clause[" + uuidXPathString + "]/logicalOp";

			String XPATH_POPULATION_LOGICALOP = "/measure//clause[" + uuidXPathString + "]//logicalOp";

			String XPATH_POPULATION_QDMELEMENT = "/measure//clause[" + uuidXPathString + "]//elementRef";

			String XPATH_POPULATION_TIMING_ELEMENT = "/measure//clause[" + uuidXPathString + "]//relationalOp";

			String XPATH_POPULATION_FUNCTIONS = "/measure//clause[" + uuidXPathString + "]//functionalOp";

			// get the Population Worspace Logic that are Used in Measure
			// Grouping

			try {
				// list of LogicalOpNode inSide the PopulationWorkspace That are
				// used in Grouping
				NodeList populationTopLevelLogicalOp = (NodeList) xPath.evaluate(XPATH_POPULATION_TOP_LEVEL_LOGICAL_OP,
						xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
				NodeList populationLogicalOp = (NodeList) xPath.evaluate(XPATH_POPULATION_LOGICALOP,
						xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
				// list of Qdemelement inSide the PopulationWorkspace That are
				// used in Grouping
				NodeList populationQdemElement = (NodeList) xPath.evaluate(XPATH_POPULATION_QDMELEMENT,
						xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
				// list of TimingElement inSide the PopulationWorkspace That are
				// used in Grouping
				NodeList populationTimingElement = (NodeList) xPath.evaluate(XPATH_POPULATION_TIMING_ELEMENT,
						xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
				// list of functionNode inSide the PopulationWorkspace That are
				// used in Grouping
				NodeList populationFunctions = (NodeList) xPath.evaluate(XPATH_POPULATION_FUNCTIONS,
						xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
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
				if ((result.getValidationMessages() != null) && (result.getValidationMessages().size() > 0)) {
					return result;
				}
				// Other Validations.
				if (populationLogicalOp.getLength() > 0) {
					for (int i = 0; (i < populationLogicalOp.getLength()); i++) {
						Node childNode = populationLogicalOp.item(i);
						String type = childNode.getParentNode().getAttributes()
								.getNamedItem(PopulationWorkSpaceConstants.TYPE).getNodeValue();
						if (type.equals("measureObservation")) {
							result.setValid(false);
							message.add(MatContext.get().getMessageDelegate()
									.getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
							result.setValidationMessages(message);
							return result;
						}
					}
				}

				if ((populationQdemElement.getLength() > 0)) {
					result.setValid(false);
					message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
					result.setValidationMessages(message);
					return result;
				}

				if ((populationTimingElement.getLength() > 0)) {
					result.setValid(false);
					message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
					result.setValidationMessages(message);
					return result;
				}
				if ((populationFunctions.getLength() > 0)) {
					result.setValid(false);
					message.add(MatContext.get().getMessageDelegate().getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
					result.setValidationMessages(message);
					return result;
				}

			} catch (XPathExpressionException e) {
				logger.debug("validateMeasureXmlAtbyCreateMeasurePackager: " + e);
			}

			// start clause validation
			Map<String, List<String>> usedSubtreeRefIdsMap = getUsedSubtreeRefIds(xmlProcessor, measureGroupingIDList);
			List<String> usedSubTreeIds = checkUnUsedSubTreeRef(xmlProcessor, usedSubtreeRefIdsMap);
			// to get all Operators for validaiton during Package timing for
			// Removed Operators
			List<String> operatorTypeList = getAllOperatorsTypeList();

			if (usedSubTreeIds.size() > 0) {
				for (int k = 0; (k < usedSubTreeIds.size()); k++) {
					String usedSubtreeRefId = usedSubTreeIds.get(k);

					String satisfyFunction = "@type='SATISFIES ALL' or @type='SATISFIES ANY'";
					String otherThanSatisfyfunction = "@type!='SATISFIES ALL' or @type!='SATISFIES ANY'";
					String dateTimeDiffFunction = "@type='DATETIMEDIFF'";
					String qdmVariable = "@qdmVariable='true'";
					// geting list of IDs
					String XPATH_QDMELEMENT = "/measure//subTreeLookUp/subTree[@uuid='" + usedSubtreeRefId
							+ "']//elementRef/@id";
					// geting Unique Ids only
					// String XPATH_QDMELEMENT =
					// "/measure//subTreeLookUp/subTree[@uuid='"+usedSubtreeRefId+"']//elementRef[not(@id
					// = preceding:: elementRef/@id)]";
					String XPATH_TIMING_ELEMENT = "/measure//subTreeLookUp/subTree[@uuid='" + usedSubtreeRefId
							+ "']//relationalOp";
					String XPATH_SATISFY_ELEMENT = "/measure//subTreeLookUp/subTree[@uuid='" + usedSubtreeRefId
							+ "']//functionalOp[" + satisfyFunction + "]";
					String XPATH_FUNCTIONS = "/measure//subTreeLookUp/subTree[@uuid='" + usedSubtreeRefId
							+ "']//functionalOp[" + otherThanSatisfyfunction + "]";
					String XPATH_SETOPERATOR = "/measure//subTreeLookUp/subTree[@uuid='" + usedSubtreeRefId
							+ "']//setOp";
					// for DateTimeDiff Validation
					String XPATH_DATE_TIME_DIFF_ELEMENT = "/measure//subTreeLookUp/subTree[@uuid='" + usedSubtreeRefId
							+ "']//functionalOp[" + dateTimeDiffFunction + "]";

					String XPATH_SUBTREE = "//subTreeLookUp/subTree[@uuid='" + usedSubtreeRefId + "']";
					String XPATH_QDMVARAIBLES = "/measure//subTreeLookUp/subTree[@uuid='" + usedSubtreeRefId + "'and "
							+ qdmVariable + "]";
					try {

						NodeList nodesSDE_qdmElementId = (NodeList) xPath.evaluate(XPATH_QDMELEMENT,
								xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
						NodeList nodesSDE_timingElement = (NodeList) xPath.evaluate(XPATH_TIMING_ELEMENT,
								xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
						NodeList nodesSDE_satisfyElement = (NodeList) xPath.evaluate(XPATH_SATISFY_ELEMENT,
								xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
						NodeList nodesSDE_functions = (NodeList) xPath.evaluate(XPATH_FUNCTIONS,
								xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
						NodeList nodeSDE_setoperator = (NodeList) xPath.evaluate(XPATH_SETOPERATOR,
								xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
						NodeList nodeSDE_dateTimeDiffElement = (NodeList) xPath.evaluate(XPATH_DATE_TIME_DIFF_ELEMENT,
								xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
						NodeList nodesSDE_qdmVariables = (NodeList) xPath.evaluate(XPATH_QDMVARAIBLES,
								xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
						Node nodeSubTree = (Node) xPath.evaluate(XPATH_SUBTREE, xmlProcessor.getOriginalDoc(),
								XPathConstants.NODE);
						// Validation for Nested Clause Logic.

						// 1 is counter value for parent clause.
						int nestedClauseCounter = 0;
						isInvalid = validateNestedClauseLogic(nodeSubTree, nestedClauseCounter, isInvalid,
								xmlProcessor);
						if (isInvalid) {
							result.setValid(false);
							message.add(MatContext.get().getMessageDelegate()
									.getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
							result.setValidationMessages(message);
							return result;
						}

						for (int n = 0; (n < nodesSDE_timingElement.getLength()); n++) {

							Node timingElementchildNode = nodesSDE_timingElement.item(n);
							isInvalid = validateTimingRelationshipNode(timingElementchildNode, operatorTypeList,
									isInvalid);
							if (isInvalid) {
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate()
										.getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
								result.setValidationMessages(message);
								return result;
							}

						}
						for (int j = 0; (j < nodesSDE_satisfyElement.getLength()); j++) {

							Node satisfyElementchildNode = nodesSDE_satisfyElement.item(j);
							isInvalid = validateSatisfyNode(satisfyElementchildNode, isInvalid);
							if (isInvalid) {
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate()
										.getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
								result.setValidationMessages(message);
								return result;
							}

						}

						for (int m = 0; (m < nodesSDE_qdmElementId.getLength()); m++) {
							String id = nodesSDE_qdmElementId.item(m).getNodeValue();
							String xpathForQdmWithAttributeList = "/measure//subTreeLookUp/subTree[@uuid='"
									+ usedSubtreeRefId + "']//elementRef[@id='" + id + "']/attribute";
							String xpathForQdmWithOutAttributeList = "/measure//subTreeLookUp/subTree[@uuid='"
									+ usedSubtreeRefId + "']//elementRef[@id='" + id + "'][not(attribute)]";
							String XPATH_QDMLOOKUP = "/measure/elementLookUp/qdm[@uuid='" + id + "']";
							Node qdmNode = (Node) xPath.evaluate(XPATH_QDMLOOKUP, xmlProcessor.getOriginalDoc(),
									XPathConstants.NODE);
							NodeList qdmWithAttributeNodeList = (NodeList) xPath.evaluate(xpathForQdmWithAttributeList,
									xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
							NodeList qdmWithOutAttributeList = (NodeList) xPath.evaluate(
									xpathForQdmWithOutAttributeList, xmlProcessor.getOriginalDoc(),
									XPathConstants.NODESET);
							// validation for QDMwithAttributeList
							// checking for all the Attribute That are used for
							// The Id
							for (int n = 0; n < qdmWithAttributeNodeList.getLength(); n++) {
								String attributeName = qdmWithAttributeNodeList.item(n).getAttributes()
										.getNamedItem(NAME).getNodeValue();
								isInvalid = !validateQdmNode(qdmNode, attributeName);
								if (isInvalid) {
									result.setValid(false);
									message.add(MatContext.get().getMessageDelegate()
											.getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
									result.setValidationMessages(message);
									return result;
								}
							}
							// validation for QDMwithOutAttributeList for the Id
							if ((qdmWithOutAttributeList.getLength() > 0)) {
								String attributeName = "";
								isInvalid = !validateQdmNode(qdmNode, attributeName);
								if (isInvalid) {
									result.setValid(false);
									message.add(MatContext.get().getMessageDelegate()
											.getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
									result.setValidationMessages(message);
									return result;
								}
							}

						}

						for (int n = 0; (n < nodesSDE_functions.getLength()); n++) {

							Node functionsChildNode = nodesSDE_functions.item(n);
							isInvalid = validateFunctionNode(functionsChildNode, operatorTypeList, isInvalid);

							if (isInvalid || (usedSubtreeRefIdsMap.get("subTreeIDAtMO").contains(usedSubtreeRefId)
									&& validateFunctionNodeInMO(functionsChildNode))) {
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate()
										.getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
								result.setValidationMessages(message);
								return result;
							}
						}

						for (int n = 0; (n < nodeSDE_setoperator.getLength()); n++) {

							Node setOperatorChildNode = nodeSDE_setoperator.item(n);
							isInvalid = validateSetOperatorNode(setOperatorChildNode, isInvalid);
							if (isInvalid) {
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate()
										.getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
								result.setValidationMessages(message);
								return result;
							}

						}

						for (int n = 0; (n < nodeSDE_dateTimeDiffElement.getLength()); n++) {

							if (usedSubtreeRefIdsMap.get("subTreeIDAtPop").contains(usedSubtreeRefId)
									|| usedSubtreeRefIdsMap.get("subTreeIDAtStrat").contains(usedSubtreeRefId)) {
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate()
										.getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
								result.setValidationMessages(message);
								return result;
							}

							Node dateTimeDiffChildNode = nodeSDE_dateTimeDiffElement.item(n);
							if (dateTimeDiffChildNode.getChildNodes().getLength() < 2) {
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate()
										.getWARNING_MEASURE_PACKAGE_CREATION_GENERIC());
								result.setValidationMessages(message);
								return result;
							}

							// Verifying no datetime dif funcitons are not in
							// riskAdjustmentVariables
							String RISK_ADJUSTMENT_DATETIMEDIF_RETRIVAL = "/measure/riskAdjustmentVariables/subTreeRef[@id='"
									+ usedSubtreeRefId + "']";
							NodeList riskAdjustmentNodes = (NodeList) xPath.evaluate(
									RISK_ADJUSTMENT_DATETIMEDIF_RETRIVAL, xmlProcessor.getOriginalDoc(),
									XPathConstants.NODESET);
							if (riskAdjustmentNodes.getLength() > 0) {
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate()
										.getWARNING_MEASURE_PACKAGE_CREATION_RISK_ADJUSTMENT());
								result.setValidationMessages(message);
								return result;
							}

						}
						// verifying no qdm variables are in
						// riskAjustmentVariables
						for (int p = 0; p < nodesSDE_qdmVariables.getLength(); p++) {
							String nodeUUID = nodesSDE_qdmVariables.item(p).getAttributes()
									.getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
							String RISK_ADJUSTMENT_QDM_RETRIVAL = "/measure/riskAdjustmentVariables/subTreeRef[@id='"
									+ nodeUUID + "']";
							NodeList riskAdjustmentNodes = (NodeList) xPath.evaluate(RISK_ADJUSTMENT_QDM_RETRIVAL,
									xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
							if (riskAdjustmentNodes.getLength() > 0) {
								result.setValid(false);
								message.add(MatContext.get().getMessageDelegate()
										.getWARNING_MEASURE_PACKAGE_CREATION_RISK_ADJUSTMENT());
								result.setValidationMessages(message);
								return result;
							}
						}

					} catch (XPathExpressionException e) {
						logger.debug("Exception in validateMeasureXmlAtbyCreateMeasurePackager: " + e);
					}
				}
			}

		}
		return result;
	}

	/**
	 * Validate function node in measure observation.
	 *
	 * @param functionsChildNode
	 *            the functions child node
	 * @return true, if successful
	 */
	private boolean validateFunctionNodeInMO(Node functionsChildNode) {

		String displayName = functionsChildNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME)
				.getNodeValue();
		String type = functionsChildNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.TYPE).getNodeValue();
		return (!type.equalsIgnoreCase(displayName));
	}

	/**
	 * This method will evaluate Logical Operators and checks if Logical operators
	 * has child nodes or not. Default Top Level Logical Operators are not
	 * considered for this validation.
	 * 
	 * @param populationTopLevelLogicalOp
	 *            - NodeList.
	 * @return String - message
	 */
	private String validateLogicalOpsInAllPopulations(NodeList populationTopLevelLogicalOp) {
		boolean isInvalid = false;
		String message = null;
		if (populationTopLevelLogicalOp.getLength() > 0) {
			for (int i = 0; i < populationTopLevelLogicalOp.getLength(); i++) {
				Node operatorNode = populationTopLevelLogicalOp.item(i);
				if (operatorNode.getParentNode().getAttributes().getNamedItem(PopulationWorkSpaceConstants.TYPE)
						.toString().equalsIgnoreCase("startum")) {
					if (operatorNode.hasChildNodes()) {
						isInvalid = findInvalidLogicalOperators(operatorNode.getChildNodes(), false);
					} else {
						isInvalid = true;
					}
					if (isInvalid) {
						message = MatContext.get().getMessageDelegate().getCLAUSE_WORK_SPACE_INVALID_LOGICAL_OPERATOR();
						break;
					}
				} else {
					// Populations other than Stratification : dont check for
					// default top level Logical Op.
					if (operatorNode.hasChildNodes() && (operatorNode.getChildNodes().getLength() > 1)) {
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
	 * @param populationTopLevelLogicalOp
	 *            the population top level logical op
	 * @param isTopLevelLogicalOp
	 *            the is top level logical op
	 * @return true, if successful
	 */
	private boolean findInvalidLogicalOperators(NodeList populationTopLevelLogicalOp, boolean isTopLevelLogicalOp) {
		boolean isInvalid = false;
		if ((populationTopLevelLogicalOp != null)) {
			for (int i = 0; i < populationTopLevelLogicalOp.getLength(); i++) {
				Node operatorNode = populationTopLevelLogicalOp.item(i);
				if (operatorNode.getNodeName().equalsIgnoreCase("comment")) {
					// ignore the comment for the top Level Logical Operator
					if (!isTopLevelLogicalOp) {
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
						if (!isTopLevelLogicalOp) {
							isInvalid = true;
						} else {
							isTopLevelLogicalOp = false;
						}
					}
				}
				if (isInvalid) {
					break;
				}
			}
		}
		return isInvalid;
	}

	/**
	 * Validate Clause should have depth up to 10 Levels.
	 *
	 * @param nodeSubTreeRef
	 *            the node sub tree ref
	 * @param counter
	 *            the counter
	 * @param flag
	 *            the flag
	 * @param xmlProcessor
	 *            the xml processor
	 * @return true, if successful
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private boolean validateNestedClauseLogic(Node nodeSubTreeRef, int counter, boolean flag, XmlProcessor xmlProcessor)
			throws XPathExpressionException {
		NodeList children = nodeSubTreeRef.getChildNodes();
		String subTreeNodeName = nodeSubTreeRef.getNodeName();
		if ((children.getLength() == 0) && subTreeNodeName.equalsIgnoreCase("subTree")
				&& (nodeSubTreeRef.getAttributes().getNamedItem(INSTANCE_OF) == null)) {
			flag = true;
			return flag;
		}
		for (int i = 0; ((i < children.getLength()) && !flag); i++) {
			int currentCounter = counter;
			Node node = children.item(i);
			if (node.getNodeName().equalsIgnoreCase("subTreeRef")) {
				String uuid = node.getAttributes().getNamedItem(ID).getNodeValue();
				if (node.getAttributes().getNamedItem(INSTANCE) != null) {
					uuid = node.getAttributes().getNamedItem(INSTANCE_OF).getNodeValue();
				}
				String XPATH_SUBTREE = "//subTreeLookUp/subTree[@uuid='" + uuid + "']";
				node = (Node) xPath.evaluate(XPATH_SUBTREE, xmlProcessor.getOriginalDoc(), XPathConstants.NODE);
				if (node.hasChildNodes()) {
					node = node.getChildNodes().item(0);
				}
			}
			if (node.getChildNodes() != null && node.getChildNodes().getLength() > 0
					&& !node.getNodeName().equalsIgnoreCase("subTree")) {
				currentCounter = currentCounter + 1;
			}
			if (currentCounter > NESTED_CLAUSE_DEPTH) {
				flag = true;
				logger.info("Breaking for Node -------"
						+ node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME).getNodeValue());
				break;
			} else {
				flag = validateNestedClauseLogic(node, currentCounter, flag, xmlProcessor);
			}

		}
		return flag;
	}

	/**
	 * Check if qdm var instance is present.
	 *
	 * @param usedSubtreeRefId
	 *            the used subtree ref id
	 * @param xmlProcessor
	 *            the xml processor
	 * @return the string
	 */
	private String checkIfQDMVarInstanceIsPresent(String usedSubtreeRefId, XmlProcessor xmlProcessor) {

		String XPATH_INSTANCE_QDM_VAR = "/measure/subTreeLookUp/subTree[@uuid='" + usedSubtreeRefId + "']/@instance";
		String XPATH_INSTANCE_OF_QDM_VAR = "/measure/subTreeLookUp/subTree[@uuid='" + usedSubtreeRefId
				+ "']/@instanceOf";
		try {
			Node nodesSDE_SubTree = (Node) xPath.evaluate(XPATH_INSTANCE_QDM_VAR, xmlProcessor.getOriginalDoc(),
					XPathConstants.NODE);
			if (nodesSDE_SubTree != null) {
				Node nodesSDE_SubTreeInstance = (Node) xPath.evaluate(XPATH_INSTANCE_OF_QDM_VAR,
						xmlProcessor.getOriginalDoc(), XPathConstants.NODE);
				usedSubtreeRefId = nodesSDE_SubTreeInstance.getNodeValue();
			}

		} catch (XPathExpressionException e) {
			logger.debug("checkIfQDMVarInstanceIsPresent: " + e);
		}

		return usedSubtreeRefId;
	}

	/**
	 * Gets the filtered sub tree ids.
	 *
	 * @param xmlProcessor
	 *            the xml processor
	 * @param usedSubTreeIdsMap
	 *            the used sub tree ids map
	 * @return the filtered sub tree ids
	 */
	private List<String> checkUnUsedSubTreeRef(XmlProcessor xmlProcessor, Map<String, List<String>> usedSubTreeIdsMap) {

		List<String> subTreeIdsAtPop = new ArrayList<>();
		List<String> subTreeIdsAtMO = new ArrayList<>();
		List<String> subTreeIdsAtStrat = new ArrayList<>();
		subTreeIdsAtPop.addAll(usedSubTreeIdsMap.get("subTreeIDAtPop"));
		subTreeIdsAtMO.addAll(usedSubTreeIdsMap.get("subTreeIDAtMO"));
		subTreeIdsAtStrat.addAll(usedSubTreeIdsMap.get("subTreeIDAtStrat"));
		List<String> subTreeIdsAtRAV = getUsedRiskAdjustmentVariables(xmlProcessor);
		subTreeIdsAtPop.removeAll(subTreeIdsAtMO);
		subTreeIdsAtMO.addAll(subTreeIdsAtPop);

		subTreeIdsAtMO.removeAll(subTreeIdsAtStrat);
		subTreeIdsAtStrat.addAll(subTreeIdsAtMO);

		// to get Used SubTreeRef form Risk Adjustment Variables
		subTreeIdsAtStrat.removeAll(subTreeIdsAtRAV);
		subTreeIdsAtRAV.addAll(subTreeIdsAtStrat);

		return subTreeIdsAtRAV;
	}

	/**
	 * Gets the used risk adjustment variables.
	 *
	 * @param xmlProcessor
	 *            the xml processor
	 * @return the used risk adjustment variables
	 */
	private List<String> getUsedRiskAdjustmentVariables(XmlProcessor xmlProcessor) {
		List<String> subTreeRefRAVList = new ArrayList<>();

		String xpathforRiskAdjustmentVariables = "/measure/riskAdjustmentVariables/subTreeRef";
		try {
			NodeList subTreeRefIdsNodeListRAV = (NodeList) xPath.evaluate(xpathforRiskAdjustmentVariables,
					xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
			for (int i = 0; i < subTreeRefIdsNodeListRAV.getLength(); i++) {
				Node childNode = subTreeRefIdsNodeListRAV.item(i);
				subTreeRefRAVList.add(childNode.getAttributes().getNamedItem(ID).getNodeValue());
			}
		} catch (XPathExpressionException e) {
			logger.debug("getUsedRiskAdjustmentVariables: " + e);
		}

		return checkUnUsedSubTreeRef(xmlProcessor, subTreeRefRAVList);
	}

	/**
	 * Gets the stratification clasues id list.
	 *
	 * @param uuid
	 *            the uuid
	 * @param xmlProcessor
	 *            the xml processor
	 * @return the stratification clasues id list
	 */
	private List<String> getStratificationClasuesIDList(String uuid, XmlProcessor xmlProcessor) {

		String XPATH_MEASURE_GROUPING_STRATIFICATION_CLAUSES = "/measure/strata/stratification" + "[@uuid='" + uuid
				+ "']/clause/@uuid";
		List<String> clauseList = new ArrayList<>();
		try {
			NodeList stratificationClausesNodeList = (NodeList) xPath.evaluate(
					XPATH_MEASURE_GROUPING_STRATIFICATION_CLAUSES, xmlProcessor.getOriginalDoc(),
					XPathConstants.NODESET);
			for (int i = 0; i < stratificationClausesNodeList.getLength(); i++) {
				clauseList.add(stratificationClausesNodeList.item(i).getNodeValue());
			}
		} catch (XPathExpressionException e) {
			logger.debug("getStratificationClasuesIDList: " + e);
		}
		return clauseList;
	}

	/**
	 * Gets the used subtree ref ids.
	 *
	 * @param xmlProcessor
	 *            the xml processor
	 * @param measureGroupingIDList
	 *            the measure grouping id list
	 * @return the used subtree ref ids
	 */
	private Map<String, List<String>> getUsedSubtreeRefIds(XmlProcessor xmlProcessor,
			List<String> measureGroupingIDList) {

		List<String> usedSubTreeRefIdsPop = new ArrayList<>();
		List<String> usedSubTreeRefIdsStrat = new ArrayList<>();
		List<String> usedSubTreeRefIdsMO = new ArrayList<>();
		Map<String, List<String>> usedSubTreeIdsMap = new HashMap<>();
		NodeList groupedSubTreeRefIdsNodeListPop;
		NodeList groupedSubTreeRefIdsNodeListMO;
		NodeList groupedSubTreeRefIdListStrat;
		String uuidXPathString = "";

		for (String uuidString : measureGroupingIDList) {
			uuidXPathString += "@uuid = '" + uuidString + "' or";
		}

		uuidXPathString = uuidXPathString.substring(0, uuidXPathString.lastIndexOf(" or"));
		String XPATH_POPULATION_SUBTREEREF = "/measure/populations//clause[" + uuidXPathString
				+ "]//subTreeRef[not(@id = preceding:: populations//clause//subTreeRef/@id)]/@id";

		try {
			// Populations, MeasureObervations and Startification
			groupedSubTreeRefIdsNodeListPop = (NodeList) xPath.evaluate(XPATH_POPULATION_SUBTREEREF,
					xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);

			for (int i = 0; i < groupedSubTreeRefIdsNodeListPop.getLength(); i++) {
				Node groupedSubTreeRefIdAttributeNodePop = groupedSubTreeRefIdsNodeListPop.item(i);
				String uuid = groupedSubTreeRefIdAttributeNodePop.getNodeValue();

				uuid = checkIfQDMVarInstanceIsPresent(uuid, xmlProcessor);
				if (!usedSubTreeRefIdsPop.contains(uuid)) {
					usedSubTreeRefIdsPop.add(uuid);
				}
			}

			// to get the Used SubtreeIds from Population Tab.
			List<String> usedSubtreeIdsAtPop = checkUnUsedSubTreeRef(xmlProcessor, usedSubTreeRefIdsPop);

			// Measure Observations
			String measureObservationSubTreeRefID = "/measure/measureObservations//clause[" + uuidXPathString
					+ "]//subTreeRef[not(@id = preceding:: measureObservations//clause//subTreeRef/@id)]/@id";
			groupedSubTreeRefIdsNodeListMO = (NodeList) xPath.evaluate(measureObservationSubTreeRefID,
					xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);

			for (int i = 0; i < groupedSubTreeRefIdsNodeListMO.getLength(); i++) {
				Node groupedSubTreeRefIdAttributeNodeMO = groupedSubTreeRefIdsNodeListMO.item(i);
				String uuid = groupedSubTreeRefIdAttributeNodeMO.getNodeValue();
				uuid = checkIfQDMVarInstanceIsPresent(uuid, xmlProcessor);
				if (!usedSubTreeRefIdsMO.contains(uuid)) {
					usedSubTreeRefIdsMO.add(uuid);
				}
			}

			// used SubtreeIds at Measure Observation
			List<String> usedSubtreeIdsAtMO = checkUnUsedSubTreeRef(xmlProcessor, usedSubTreeRefIdsMO);

			// Stratifications
			String startSubTreeRefID = "/measure/strata//clause[" + uuidXPathString
					+ "]//subTreeRef[not(@id = preceding:: strata//clause//subTreeRef/@id)]/@id";
			groupedSubTreeRefIdListStrat = (NodeList) xPath.evaluate(startSubTreeRefID, xmlProcessor.getOriginalDoc(),
					XPathConstants.NODESET);

			for (int i = 0; i < groupedSubTreeRefIdListStrat.getLength(); i++) {
				Node groupedSubTreeRefIdAttributeNodeStrat = groupedSubTreeRefIdListStrat.item(i);
				String uuid = groupedSubTreeRefIdAttributeNodeStrat.getNodeValue();
				uuid = checkIfQDMVarInstanceIsPresent(uuid, xmlProcessor);
				if (!usedSubTreeRefIdsStrat.contains(uuid)) {
					usedSubTreeRefIdsStrat.add(uuid);
				}
			}

			// get used Subtreeids at Stratification
			List<String> usedSubtreeIdsAtStrat = checkUnUsedSubTreeRef(xmlProcessor, usedSubTreeRefIdsStrat);

			usedSubTreeIdsMap.put("subTreeIDAtPop", usedSubtreeIdsAtPop);
			usedSubTreeIdsMap.put("subTreeIDAtMO", usedSubtreeIdsAtMO);
			usedSubTreeIdsMap.put("subTreeIDAtStrat", usedSubtreeIdsAtStrat);

		} catch (XPathExpressionException e) {
			logger.debug("getUsedSubtreeRefIds: " + e);
		}

		return usedSubTreeIdsMap;
	}

	/**
	 * Check un used sub tree ref.
	 *
	 * @param xmlProcessor
	 *            the xml processor
	 * @param usedSubTreeRefIds
	 *            the used sub tree ref ids
	 * @return the list
	 */
	private List<String> checkUnUsedSubTreeRef(XmlProcessor xmlProcessor, List<String> usedSubTreeRefIds) {

		List<String> allSubTreeRefIds = new ArrayList<>();
		NodeList subTreeRefIdsNodeList;
		try {
			subTreeRefIdsNodeList = (NodeList) xPath.evaluate("/measure//subTreeRef/@id", xmlProcessor.getOriginalDoc(),
					XPathConstants.NODESET);

			for (int i = 0; i < subTreeRefIdsNodeList.getLength(); i++) {
				Node SubTreeRefIdAttributeNode = subTreeRefIdsNodeList.item(i);
				if (!allSubTreeRefIds.contains(SubTreeRefIdAttributeNode.getNodeValue())) {
					allSubTreeRefIds.add(SubTreeRefIdAttributeNode.getNodeValue());
				}
			}
			allSubTreeRefIds.removeAll(usedSubTreeRefIds);

			for (int i = 0; i < usedSubTreeRefIds.size(); i++) {
				for (int j = 0; j < allSubTreeRefIds.size(); j++) {
					Node usedSubTreeRefNode = (Node) xPath.evaluate(
							"/measure/subTreeLookUp/subTree[@uuid='" + usedSubTreeRefIds.get(i) + "']//subTreeRef[@id='"
									+ allSubTreeRefIds.get(j) + "']",
							xmlProcessor.getOriginalDoc(), XPathConstants.NODE);

					if (usedSubTreeRefNode != null) {

						String subTreeUUID = usedSubTreeRefNode.getAttributes().getNamedItem(ID).getNodeValue();
						String XPATH_IS_INSTANCE_OF = "//subTree [boolean(@instanceOf)]/@uuid ='" + subTreeUUID + "'";
						boolean isOccurrenceNode = (Boolean) xPath.evaluate(XPATH_IS_INSTANCE_OF,
								xmlProcessor.getOriginalDoc(), XPathConstants.BOOLEAN);
						if (isOccurrenceNode) {
							String XPATH_PARENT_UUID = "//subTree [@uuid ='" + subTreeUUID + "']/@instanceOf";
							String parentUUID = (String) xPath.evaluate(XPATH_PARENT_UUID,
									xmlProcessor.getOriginalDoc(), XPathConstants.STRING);
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
			logger.debug("checkUnUsedSubTreeRef: " + e);
		}
		return usedSubTreeRefIds;
	}

	/**
	 * Validate qdm node.
	 *
	 * @param qdmchildNode
	 *            the qdmchild node
	 * @param attributeValue
	 *            the attribute value
	 * @return true, if successful
	 */
	private boolean validateQdmNode(Node qdmchildNode, String attributeValue) {
		boolean flag = true;
		String dataTypeValue = qdmchildNode.getAttributes().getNamedItem(DATATYPE).getNodeValue();
		String qdmName = qdmchildNode.getAttributes().getNamedItem(NAME).getNodeValue();
		String oidValue = qdmchildNode.getAttributes().getNamedItem(OID).getNodeValue();
		if (dataTypeValue.equalsIgnoreCase("timing element")) {
			if (qdmName.equalsIgnoreCase("Measurement End Date")
					|| qdmName.equalsIgnoreCase("Measurement Start Date")) {
				flag = false;
			}
		} else if (dataTypeValue.equalsIgnoreCase("Patient characteristic Birthdate")
				|| dataTypeValue.equalsIgnoreCase("Patient characteristic Expired")) {

			if (oidValue.equalsIgnoreCase("419099009") || oidValue.equalsIgnoreCase("21112-8")) {
				// do nothing
			} else {
				flag = false;
			}
		} else if (attributeValue.isEmpty()) {
			if (!checkIfQDMDataTypeIsPresent(dataTypeValue)) {
				flag = false;
			}

		} else if (!attributeValue.isEmpty() && (attributeValue.length() > 0)) {
			if (checkIfQDMDataTypeIsPresent(dataTypeValue)) {

				List<QDSAttributes> attributeList = getAllDataTypeAttributes(dataTypeValue);
				if (!attributeList.isEmpty()) {
					List<String> attrList = new ArrayList<>();
					for (int i = 0; i < attributeList.size(); i++) {
						attrList.add(attributeList.get(i).getName());
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
	 * @param timingElementchildNode
	 *            the timing elementchild node
	 * @param operatorTypeList
	 *            the operator type list
	 * @param flag
	 *            the flag
	 * @return true, if successful
	 */
	private boolean validateTimingRelationshipNode(Node timingElementchildNode, List<String> operatorTypeList,
			boolean flag) {
		int childCount = timingElementchildNode.getChildNodes().getLength();
		String type = timingElementchildNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.TYPE)
				.getNodeValue();
		if ((childCount != 2) || !operatorTypeList.contains(type)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * Validate satisfy node.
	 *
	 * @param satisfyElementchildNode
	 *            the satisfy elementchild node
	 * @param flag
	 *            the flag
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
	 * @param functionchildNode
	 *            the functionchild node
	 * @param operatorTypeList
	 *            the operator type list
	 * @param flag
	 *            the flag
	 * @return true, if successful
	 */
	private boolean validateFunctionNode(Node functionchildNode, List<String> operatorTypeList, boolean flag) {
		int functionChildCount = functionchildNode.getChildNodes().getLength();
		String type = functionchildNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.TYPE).getNodeValue();
		if ((functionChildCount < 1) || !operatorTypeList.contains(type)) {
			flag = true;
		}
		return flag;

	}

	/**
	 * Validate set operator node.
	 *
	 * @param setOperatorchildNode
	 *            the set operatorchild node
	 * @param flag
	 *            the flag
	 * @return true, if successful
	 */
	private boolean validateSetOperatorNode(Node setOperatorchildNode, boolean flag) {
		int setOperatorChildCount = setOperatorchildNode.getChildNodes().getLength();
		if (setOperatorChildCount < 2) {
			flag = true;
		}
		return flag;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.server.service.MeasureLibraryService#validateForGroup(mat.client.
	 * measure.ManageMeasureDetailModel)
	 */
	@Override
	public ValidateMeasureResult validateForGroup(ManageMeasureDetailModel model) {

		logger.debug(" MeasureLibraryServiceImpl: validateGroup Start :  ");

		List<String> message = new ArrayList<>();
		ValidateMeasureResult result = new ValidateMeasureResult();
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, model.getId())) {
			MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(model.getId());

			if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
				XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());

				// validate for at least one grouping
				String XPATH_GROUP = "/measure/measureGrouping/group";

				NodeList groupSDE;
				try {
					groupSDE = (NodeList) xPath.evaluate(XPATH_GROUP, xmlProcessor.getOriginalDoc(),
							XPathConstants.NODESET);

					if (groupSDE.getLength() == 0) {
						message.add(MatContext.get().getMessageDelegate().getGroupingRequiredMessage());

					} else {
						for (int i = 1; i <= groupSDE.getLength(); i++) {
							NodeList numberOfStratificationPerGroup = (NodeList) xPath.evaluate(
									"/measure/measureGrouping/group[@sequence='" + i
											+ "']/packageClause[@type='stratification']",
									xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
							if (numberOfStratificationPerGroup.getLength() > 1) {
								message.add(MatContext.get().getMessageDelegate()
										.getSTRATIFICATION_VALIDATION_FOR_GROUPING());
								break;
							}
						}

					}
				} catch (XPathExpressionException e) {
					logger.debug("validateForGroup: " + e);
				}
			}
		}
		result.setValid(message.isEmpty());
		result.setValidationMessages(message);
		return result;
	}

	/**
	 * Takes an XPath notation String for a particular tag and a Document object and
	 * finds and removes the tag from the document.
	 * 
	 * @param nodeXPath
	 *            the node x path
	 * @param originalDoc
	 *            the original doc
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private void removeNode(String nodeXPath, Document originalDoc) throws XPathExpressionException {
		Node node = (Node) xPath.evaluate(nodeXPath, originalDoc.getDocumentElement(), XPathConstants.NODE);
		if (node != null) {
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
	public final List<MeasureType> getAllMeasureTypes() {
		List<MeasureTypeDTO> measureTypeDTOList = measureTypeDAO.getAllMeasureTypes();
		List<MeasureType> measureTypeList = new ArrayList<>();
		for (MeasureTypeDTO measureTypeDTO : measureTypeDTOList) {
			MeasureType measureType = new MeasureType();
			measureType.setDescription(measureTypeDTO.getName());
			measureType.setAbbrName(measureTypeDTO.getAbbrName());
			measureTypeList.add(measureType);
		}
		return measureTypeList;

	}

	@Override
	public List<Organization> getAllOrganizations() {
		return organizationDAO.getAllOrganizations();
	}

	/**
	 * Gets the all operators type list.
	 *
	 * @return the all operators type list
	 */
	private List<String> getAllOperatorsTypeList() {
		List<OperatorDTO> allOperatorsList = operatorDAO.getAllOperators();
		List<String> allOperatorsTypeList = new ArrayList<>();
		for (int i = 0; i < allOperatorsList.size(); i++) {
			allOperatorsTypeList.add(allOperatorsList.get(i).getId());
		}
		return allOperatorsTypeList;
	}

	@Override
	public MeasureDetailResult getUsedStewardAndDevelopersList(String measureId) {
		logger.info("In MeasureLibraryServiceImpl.getUsedStewardAndDevelopersList() method..");
		logger.info("Loading Measure for MeasueId: " + measureId);
		MeasureDetailResult usedStewardAndAuthorList = new MeasureDetailResult();
		MeasureXmlModel xml = getMeasureXmlForMeasure(measureId);
		
		List<Organization> allOrganization = getAllOrganizations();
		usedStewardAndAuthorList.setUsedAuthorList(getAuthorsList(xml, allOrganization));
		usedStewardAndAuthorList.setUsedSteward(getSteward(xml, allOrganization));
		usedStewardAndAuthorList.setAllAuthorList(getAllAuthorList(allOrganization));
		usedStewardAndAuthorList.setAllStewardList(getAllStewardList(allOrganization));

		return usedStewardAndAuthorList;
	}

	/**
	 * Gets the all steward list.
	 *
	 * @return the all steward list
	 */
	private List<MeasureSteward> getAllStewardList(List<Organization> organizationList) {
		
		return organizationList.stream().map(
				org -> new MeasureSteward(String.valueOf(org.getId()), org.getOrganizationName(), org.getOrganizationOID())).collect(Collectors.toList());
	}

	/**
	 * Gets the all author list.
	 *
	 * @return the all author list
	 */
	private List<Author> getAllAuthorList(List<Organization> organizationList) {
		
		return organizationList.stream().map(
				org -> new Author(String.valueOf(org.getId()), org.getOrganizationName(), org.getOrganizationOID())).collect(Collectors.toList());
	}

	/**
	 * Gets the authors list.
	 *
	 * @param xmlModel
	 *            the xml model
	 * @return the authors list
	 */
	private List<Author> getAuthorsList(MeasureXmlModel xmlModel, List<Organization> allOrganization) {
		XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
		String XPATH_EXPRESSION_DEVELOPERS = "/measure//measureDetails//developers";
		List<Author> usedAuthorList = new ArrayList<>();

		try {
			NodeList developerParentNodeList = (NodeList) xPath.evaluate(XPATH_EXPRESSION_DEVELOPERS, processor.getOriginalDoc(), XPathConstants.NODESET);
			Node developerParentNode = developerParentNodeList.item(0);

			if (developerParentNode != null) {
				NodeList developerNodeList = developerParentNode.getChildNodes();

				int childNodes =  developerNodeList.getLength();

				LinkedHashSet<Long> authorList = new LinkedHashSet<>();

				for (int i = 0; i < childNodes; i++) {
					authorList.add(Long.parseLong(developerNodeList.item(i).getAttributes().getNamedItem(ID).getNodeValue()));
				}

				if(CollectionUtils.isNotEmpty(authorList)) {
					authorList.forEach(id -> addAuthorsToList(allOrganization, usedAuthorList, id));
				}
			}

		} catch (XPathExpressionException e) {
			logger.debug("getAuthorsList: " + e);
		}

		return usedAuthorList;
	}

	private void addAuthorsToList(List<Organization> allOrganization, List<Author> usedAuthorList, Long id) {
		Author author = getAuthorFromOrganization(id, allOrganization);
		if(author != null) {
			usedAuthorList.add(author);
		}
	}

	private Author getAuthorFromOrganization(Long id, List<Organization> allOrganization){
		Optional<Organization> organization = allOrganization.stream().filter(o -> o.getId().equals(id)).findFirst();
		if(organization.isPresent()) {
			Organization org = organization.get();
			return new Author(String.valueOf(org.getId()), org.getOrganizationName(), org.getOrganizationOID());
		}
		return null;
	}
	
	/**
	 * Gets the steward id.
	 *
	 * @param xmlModel
	 *            the xml model
	 * @return the steward id
	 */
	private MeasureSteward getSteward(MeasureXmlModel xmlModel, List<Organization> allOrganization) {
		MeasureSteward measureSteward = new MeasureSteward();
		XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
		String XPATH_EXPRESSION_STEWARD = "/measure//measureDetails//steward";

		try {
			Node stewardParentNode = (Node) xPath.evaluate(XPATH_EXPRESSION_STEWARD, processor.getOriginalDoc(), XPathConstants.NODE);
			
			if (stewardParentNode != null) {
				String id = stewardParentNode.getAttributes().getNamedItem(ID).getNodeValue();

				Organization organization = allOrganization.stream().filter(org -> id.equalsIgnoreCase(Long.toString(org.getId()))).findAny().orElse(new Organization());
				measureSteward.setId(id);
				measureSteward.setOrgName(organization.getOrganizationName());
				measureSteward.setOrgOid(organization.getOrganizationOID());
			}

		} catch (XPathExpressionException e) {
			logger.debug("getSteward: " + e);
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
	 * @param isMeasureCreated
	 *            the new measure created
	 */
	public void setMeasureCreated(boolean isMeasureCreated) {
		this.isMeasureCreated = isMeasureCreated;
	}

	@Override
	public final QualityDataModelWrapper getDefaultSDEFromMeasureXml(final String measureId) {
		logger.info("Inside MeasureLibraryServiceImp :: getDefaultSDEFromMeasureXml :: Start");
		MeasureXmlModel measureXmlModel = getMeasureXmlForMeasure(measureId);
		QualityDataModelWrapper details = convertXmltoQualityDataDTOModel(measureXmlModel);
		ArrayList<QualityDataSetDTO> finalList = new ArrayList<>();
		if (details != null) {
			if (details.getQualityDataDTO() != null && !details.getQualityDataDTO().isEmpty()) {
				logger.info(" details.getQualityDataDTO().size() :" + details.getQualityDataDTO().size());
				for (QualityDataSetDTO dataSetDTO : details.getQualityDataDTO()) {
					if (dataSetDTO.getCodeListName() != null && dataSetDTO.isSuppDataElement()) {
						finalList.add(dataSetDTO);
					}
				}
			}
			details.setQualityDataDTO(finalList);
		}
		logger.info("finalList()of QualityDataSetDTO ::" + finalList.size());
		logger.info("Inside MeasureLibraryServiceImp :: getDefaultSDEFromMeasureXml :: END");
		return details;
	}

	@Override
	public List<MeasureOwnerReportDTO> getMeasuresForMeasureOwner() throws XPathExpressionException {
		Map<User, List<Measure>> map = new HashMap<>();
		List<User> nonAdminUserList = userService.getAllNonAdminActiveUsers();
		for (User user : nonAdminUserList) {
			List<Measure> measureList = measureDAO.getMeasureListForMeasureOwner(user);
			if ((measureList != null) && (measureList.size() > 0)) {
				map.put(user, measureList);
			}
		}
		List<MeasureOwnerReportDTO> ownerReList = populateMeasureOwnerReport(map);
		return ownerReList;
	}

	/**
	 * Method to populate MeasureOwnerReport DTO.
	 * 
	 * @param map
	 *            - User and List of Measures for User.
	 * @return List<MeasureOwnerReportDTO>
	 * @throws XPathExpressionException
	 *             - {@link XPathExpressionException}
	 */
	private List<MeasureOwnerReportDTO> populateMeasureOwnerReport(Map<User, List<Measure>> map)
			throws XPathExpressionException {
		List<MeasureOwnerReportDTO> measureOwnerReportDTOs = new ArrayList<>();
		for (Entry<User, List<Measure>> entry : map.entrySet()) {
			User user = entry.getKey();
			List<Measure> measureList = entry.getValue();
			for (Measure measure : measureList) {
				logger.info("Start to evaluate measure id :::: " + measure.getId());
				MeasureXML measureXML = getMeasureXMLDAO().findForMeasure(measure.getId());
				if (measureXML != null) {
					String measureXmlString = measureXML.getMeasureXMLAsString();
					if (measureXmlString != null) {
						String firstName = user.getFirstName();
						String lastName = user.getLastName();
						String organizationName = user.getOrganizationName();
						String measureDescription = measure.getDescription();
						int cmsNumber = measure.geteMeasureId();
						XmlProcessor processor = new XmlProcessor(measureXmlString);
						String xpathNqfId = "/measure/measureDetails/nqfid";
						String xpathGuid = "/measure/measureDetails/guid";
						Node nqfNode = processor.findNode(processor.getOriginalDoc(), xpathNqfId);
						String nqfId = "";
						if (nqfNode != null) {
							if (nqfNode.getAttributes().getNamedItem("extension") != null) {
								String nqfNumber = nqfNode.getAttributes().getNamedItem("extension").getNodeValue();
								nqfId = nqfNumber;
							}
						}
						Node guidNode = processor.findNode(processor.getOriginalDoc(), xpathGuid);
						String guid = "";
						if (guidNode != null) {
							String guidNumber = guidNode.getTextContent();
							guid = guidNumber;
						}

						MeasureOwnerReportDTO ownerReportDTO = new MeasureOwnerReportDTO(firstName, lastName,
								organizationName, measureDescription, cmsNumber, nqfId, guid);
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
	 * Validate stratum for atleast one clause. This validation is performed at the
	 * time of Measure Package Creation where if the a stratification is part of
	 * grouping then we need to check if stratum has atleast one clause. If stratum
	 * does'nt have atleast one clause then we throw a Warning Message.
	 *
	 * @param measureXmlModel
	 *            the measure xml model
	 * @return the string
	 */
	private String validateStratumForAtleastOneClause(MeasureXmlModel measureXmlModel) {
		String message = null;

		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureXmlModel.getMeasureId());

		if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());

			// validate only for Stratification where each startum should have at least one
			// Clause
			String XPATH_MEASURE_GROUPING_STRATIFICATION = "/measure/measureGrouping/group/packageClause[@type='stratification']"
					+ "[not(@uuid = preceding:: group/packageClause/@uuid)]/@uuid";

			List<String> stratificationClausesIDlist = null;
			try {
				NodeList startificationUuidList = (NodeList) xPath.evaluate(XPATH_MEASURE_GROUPING_STRATIFICATION,
						xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);

				for (int i = 0; i < startificationUuidList.getLength(); i++) {
					String uuid = startificationUuidList.item(i).getNodeValue();
					stratificationClausesIDlist = getStratificationClasuesIDList(uuid, xmlProcessor);
				}
				if (stratificationClausesIDlist != null) {
					for (String clauseUUID : stratificationClausesIDlist) {
						String XPATH_VALIDATE_STRATIFICATION_CLAUSE = "/measure/strata/stratification/clause[@uuid='"
								+ clauseUUID + "']//subTreeRef/@id";
						NodeList strataClauseNodeList = (NodeList) xPath.evaluate(XPATH_VALIDATE_STRATIFICATION_CLAUSE,
								xmlProcessor.getOriginalDoc(), XPathConstants.NODESET);
						if ((strataClauseNodeList != null) && (strataClauseNodeList.getLength() <= 0)) {
							message = MatContext.get().getMessageDelegate()
									.getWARNING_MEASURE_PACKAGE_CREATION_STRATA();
							break;
						}
					}
				}
			} catch (XPathExpressionException e) {
				logger.debug("validateStratumForAtleastOneClause: " + e);
			}
		}
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.server.service.MeasureLibraryService#getCurrentReleaseVersion()
	 */
	@Override
	public String getCurrentReleaseVersion() {
		return currentReleaseVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.server.service.MeasureLibraryService#setCurrentReleaseVersion(java.
	 * lang.String)
	 */
	@Override
	public void setCurrentReleaseVersion(String releaseVersion) {
		currentReleaseVersion = releaseVersion;
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
	 * @param cqlService
	 *            the new cql service
	 */
	public void setCqlService(CQLService cqlService) {
		this.cqlService = cqlService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.server.service.MeasureLibraryService#parseCQL(java.lang.String)
	 */
	@Override
	public CQLModel parseCQL(String cqlBuilder) {
		return getCqlService().parseCQL(cqlBuilder);
	}

	@Override
	public SaveUpdateCQLResult getMeasureCQLFileData(String measureId) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureId);

		if (model != null && StringUtils.isNotBlank(model.getXml())) {
			String xmlString = model.getXml();
			result = cqlService.getCQLFileData(xmlString);
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}

		return result;
	}

	@Override
	public SaveUpdateCQLResult getMeasureCQLLibraryData(String measureId) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureId);

		if (model != null && StringUtils.isNotBlank(model.getXml())) {
			String xmlString = model.getXml();
			result = cqlService.getCQLLibraryData(xmlString);
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}

		return result;
	}

	@Override
	public CQLValidationResult validateCQL(CQLModel cqlModel) {
		return null;
	}

	@Override
	public SaveUpdateCQLResult saveAndModifyDefinitions(String measureId, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
			MeasureXmlModel measureXMLModel = measurePackageService.getMeasureXmlForMeasure(measureId);
			if (measureXMLModel != null) {
				MatContextServiceUtil.get().setMeasure(true);
				result = getCqlService().saveAndModifyDefinitions(measureXMLModel.getXml(), toBeModifiedObj, currentObj,
						definitionList, isFormatable);
				if (result.isSuccess()) {
					measureXMLModel.setXml(result.getXml());
					measurePackageService.saveMeasureXml(measureXMLModel);
				}
			}
		}
		MatContextServiceUtil.get().setMeasure(false);
		return result;
	}

	@Override
	public SaveUpdateCQLResult saveAndModifyParameters(String measureId, CQLParameter toBeModifiedObj,
			CQLParameter currentObj, List<CQLParameter> parameterList, boolean isFormatable) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
			MeasureXmlModel measureXMLModel = measurePackageService.getMeasureXmlForMeasure(measureId);
			if (measureXMLModel != null) {
				result = getCqlService().saveAndModifyParameters(measureXMLModel.getXml(), toBeModifiedObj, currentObj,
						parameterList, isFormatable);
				if (result.isSuccess()) {
					measureXMLModel.setXml(result.getXml());
					measurePackageService.saveMeasureXml(measureXMLModel);
				}
			}
		}

		return result;
	}

	@Override
	public SaveUpdateCQLResult saveAndModifyFunctions(String measureId, CQLFunctions toBeModifiedObj,
			CQLFunctions currentObj, List<CQLFunctions> functionsList, boolean isFormatable) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
			MeasureXmlModel measureXMLModel = measurePackageService.getMeasureXmlForMeasure(measureId);
			if (measureXMLModel != null) {
				MatContextServiceUtil.get().setMeasure(true);
				result = getCqlService().saveAndModifyFunctions(measureXMLModel.getXml(), toBeModifiedObj, currentObj,
						functionsList, isFormatable);
				if (result.isSuccess()) {
					measureXMLModel.setXml(result.getXml());
					measurePackageService.saveMeasureXml(measureXMLModel);
				}
			}
		}
		MatContextServiceUtil.get().setMeasure(false);
		return result;

	}

	@Override
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String currentMeasureId, String libraryName,
			String comments) {

		MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(currentMeasureId);
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		if (xmlModel != null) {
			result = getCqlService().saveAndModifyCQLGeneralInfo(xmlModel.getXml(), libraryName, comments);
			if (result.isSuccess()) {
				xmlModel.setXml(result.getXml());
				measurePackageService.saveMeasureXml(xmlModel);
			}

		}

		return result;
	}

	@Override
	public SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj,
			List<CQLDefinition> definitionList) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
			MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
			if (xmlModel != null) {
				result = getCqlService().deleteDefinition(xmlModel.getXml(), toBeDeletedObj, definitionList);
				if (result.isSuccess()) {
					xmlModel.setXml(result.getXml());
					cleanPopulationsAndGroups(toBeDeletedObj, xmlModel);
					measurePackageService.saveMeasureXml(xmlModel);
				}
			}
		}
		return result;
	}

	private void cleanPopulationsAndGroups(CQLDefinition toBeDeletedObj, MeasureXmlModel xmlModel) {

		List<String> deletedClauseUUIDs = new ArrayList<>();

		XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
		try {

			// find all <populations with this definition
			String populationXPath = "/measure/populations//cqldefinition[@uuid='" + toBeDeletedObj.getId() + "']";
			NodeList cqlDefinitionNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), populationXPath);

			for (int i = 0; i < cqlDefinitionNodeList.getLength(); i++) {
				Node node = cqlDefinitionNodeList.item(i);
				Node parentClauseNode = node.getParentNode();
				String clauseUUID = parentClauseNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID)
						.getNodeValue();

				parentClauseNode.removeChild(node);
				deletedClauseUUIDs.add(clauseUUID);
			}

			// find all <stratification with this definition
			String stratificationXPath = "/measure/strata/stratification/clause[cqldefinition/@uuid='"
					+ toBeDeletedObj.getId() + "']";
			NodeList stratiClauseNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(),
					stratificationXPath);

			for (int i = 0; i < stratiClauseNodeList.getLength(); i++) {
				Node clauseNode = stratiClauseNodeList.item(i);
				Node stratiNode = clauseNode.getParentNode();

				clauseNode.removeChild(clauseNode.getFirstChild());

				String stratiUUID = stratiNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID)
						.getNodeValue();
				deletedClauseUUIDs.add(stratiUUID);
			}

			String supplementalXPath = "/measure/supplementalDataElements/cqldefinition[@uuid='"
					+ toBeDeletedObj.getId() + "']";
			String riskAdjXPath = "/measure/riskAdjustmentVariables/cqldefinition[@uuid='" + toBeDeletedObj.getId()
					+ "']";

			Node suppDefNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), supplementalXPath);
			if (suppDefNode != null) {
				suppDefNode.getParentNode().removeChild(suppDefNode);
			}

			Node riskDefNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), riskAdjXPath);
			if (riskDefNode != null) {
				riskDefNode.getParentNode().removeChild(riskDefNode);
			}

			// remove groupings if they contain deleted populations/strtifications
			if (deletedClauseUUIDs.size() > 0) {
				for (String clauseUUID : deletedClauseUUIDs) {
					String groupingXPath = "/measure/measureGrouping/group[packageClause/@uuid='" + clauseUUID + "']";

					NodeList groupNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), groupingXPath);

					for (int i = 0; i < groupNodeList.getLength(); i++) {
						Node groupNode = groupNodeList.item(i);
						groupNode.getParentNode().removeChild(groupNode);
					}
				}
			}

			if ((deletedClauseUUIDs.size() > 0) || (suppDefNode != null) || (riskDefNode != null)) {
				xmlModel.setXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public SaveUpdateCQLResult deleteFunctions(String measureId, CQLFunctions toBeDeletedObj,
			List<CQLFunctions> functionsList) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
			MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);

			if (xmlModel != null) {
				result = getCqlService().deleteFunctions(xmlModel.getXml(), toBeDeletedObj, functionsList);
				if (result.isSuccess()) {
					xmlModel.setXml(result.getXml());
					cleanMsrObservationsAndGroups(toBeDeletedObj, xmlModel);
					measurePackageService.saveMeasureXml(xmlModel);
				}
			}
		}
		return result;
	}

	private void cleanMsrObservationsAndGroups(CQLFunctions toBeDeletedObj, MeasureXmlModel xmlModel) {

		List<String> deletedClauseUUIDs = new ArrayList<>();

		XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
		boolean isUpdated = false;

		try {

			// find all <measureObservations with this function
			String msrObsFuncXPath = "/measure/measureObservations/clause//cqlfunction[@uuid='" + toBeDeletedObj.getId()
					+ "']";
			NodeList msrObsFuncNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), msrObsFuncXPath);

			for (int i = 0; i < msrObsFuncNodeList.getLength(); i++) {
				Node funcNode = msrObsFuncNodeList.item(i);

				Node parentNode = funcNode.getParentNode();

				if (parentNode.getNodeName().equals("clause")) {
					String uuid = parentNode.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID)
							.getNodeValue();
					deletedClauseUUIDs.add(uuid);
				} else {
					String uuid = parentNode.getParentNode().getAttributes()
							.getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
					deletedClauseUUIDs.add(uuid);
				}

				parentNode.removeChild(funcNode);
				isUpdated = true;
			}

			// remove groupings if they contain deleted measureObservations
			if (isUpdated) {
				for (String clauseUUID : deletedClauseUUIDs) {
					String groupingXPath = "/measure/measureGrouping/group[packageClause/@uuid='" + clauseUUID + "']";

					NodeList groupNodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), groupingXPath);

					for (int i = 0; i < groupNodeList.getLength(); i++) {
						Node groupNode = groupNodeList.item(i);
						groupNode.getParentNode().removeChild(groupNode);
					}
				}

				xmlModel.setXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
			}

		} catch (XPathExpressionException e) {
			logger.debug("cleanMsrObservationsAndGroups: " + e);
		}
	}

	@Override
	public SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj,
			List<CQLParameter> parameterList) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
			MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);

			if (xmlModel != null) {
				result = getCqlService().deleteParameter(xmlModel.getXml(), toBeDeletedObj, parameterList);
				if (result.isSuccess()) {
					xmlModel.setXml(result.getXml());
					measurePackageService.saveMeasureXml(xmlModel);
				}
			}
		}
		return result;
	}

	@Override
	public CQLKeywords getCQLKeywordsLists() {
		return getCqlService().getCQLKeyWords();
	}

	@Override
	public String getJSONObjectFromXML() {
		return getCqlService().getJSONObjectFromXML();
	}

	@Override
	public GetUsedCQLArtifactsResult getUsedCqlArtifacts(String measureId) {
		MeasureXmlModel measureXML = measurePackageService.getMeasureXmlForMeasure(measureId);
		return getCqlService().getUsedCQlArtifacts(measureXML.getXml());
	}

	@Override
	public SaveUpdateCQLResult deleteValueSet(String toBeDelValueSetId, String measureID) {

		SaveUpdateCQLResult cqlResult = null;
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureID)) {
			MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureID);
			if (model != null && model.getXml() != null) {
				cqlResult = getCqlService().deleteValueSet(model.getXml(), toBeDelValueSetId);
				if (cqlResult != null && cqlResult.isSuccess()) {
					model.setXml(cqlResult.getXml());
					measurePackageService.saveMeasureXml(model);
				}
			}
		}
		return cqlResult;
	}

	@Override
	public SaveUpdateCQLResult deleteCode(String toBeDeletedId, String measureID) {

		SaveUpdateCQLResult cqlResult = new SaveUpdateCQLResult();
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureID)) {
			MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureID);
			if (model != null && model.getXml() != null) {
				cqlResult = getCqlService().deleteCode(model.getXml(), toBeDeletedId);
				if (cqlResult != null && cqlResult.isSuccess()) {
					model.setXml(cqlResult.getXml());
					measurePackageService.saveMeasureXml(model);
					cqlResult.setCqlCodeList(getCQLCodes(measureID).getCqlCodeList());
				}
			}
		}
		return cqlResult;
	}

	@Override
	public SaveUpdateCQLResult parseCQLStringForError(String cqlFileString) {
		return getCqlService().parseCQLStringForError(cqlFileString);
	}

	@Override
	public CQLQualityDataModelWrapper getCQLValusets(String measureID) {
		CQLQualityDataModelWrapper cqlQualityDataModelWrapper = new CQLQualityDataModelWrapper();
		return getCqlService().getCQLValusets(measureID, cqlQualityDataModelWrapper);
	}

	@Override
	public CQLQualityDataModelWrapper saveValueSetList(List<CQLValueSetTransferObject> transferObjectList,
			List<CQLQualityDataSetDTO> appliedValueSetList, String measureId) {

		StringBuilder finalXmlString = new StringBuilder("<valuesets>");
		SaveUpdateCQLResult finalResult = new SaveUpdateCQLResult();
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
			for (CQLValueSetTransferObject transferObject : transferObjectList) {
				SaveUpdateCQLResult result = null;
				transferObject.setAppliedQDMList(appliedValueSetList);
				if (transferObject.getCqlQualityDataSetDTO().getOid().equals(ConstantMessages.USER_DEFINED_QDM_OID)) {
					result = getCqlService().saveCQLUserDefinedValueset(transferObject);
				} else {
					result = getCqlService().saveCQLValueset(transferObject);
				}

				if (result != null && result.isSuccess() && StringUtils.isNotBlank(result.getXml())) {
					finalXmlString = finalXmlString.append(result.getXml());
				}
			}

			finalXmlString.append("</valuesets>");
			finalResult.setXml(finalXmlString.toString());
			logger.info(finalXmlString);
			saveCQLValuesetInMeasureXml(finalResult, measureId);
			wrapper = getCQLValusets(measureId);
		}
		return wrapper;
	}

	@Override
	public SaveUpdateCQLResult saveCQLValuesettoMeasure(CQLValueSetTransferObject valueSetTransferObject) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, valueSetTransferObject.getMeasureId())) {
			result = getCqlService().saveCQLValueset(valueSetTransferObject);
			if (result != null && result.isSuccess()) {
				saveCQLValuesetInMeasureXml(result, valueSetTransferObject.getMeasureId());
			}
		}
		return result;
	}

	@Override
	public SaveUpdateCQLResult saveCQLUserDefinedValuesettoMeasure(
			CQLValueSetTransferObject matValueSetTransferObject) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO,
				matValueSetTransferObject.getMeasureId())) {
			result = getCqlService().saveCQLUserDefinedValueset(matValueSetTransferObject);
			if (result != null && result.isSuccess()) {
				saveCQLValuesetInMeasureXml(result, matValueSetTransferObject.getMeasureId());
			}
		}
		return result;
	}

	@Override
	public SaveUpdateCQLResult modifyCQLValueSetstoMeasure(CQLValueSetTransferObject matValueSetTransferObject) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO,
				matValueSetTransferObject.getMeasureId())) {
			result = getCqlService().modifyCQLValueSets(matValueSetTransferObject);
			if (result != null && result.isSuccess()) {
				updateCQLLookUpTagWithModifiedValueSet(result.getCqlQualityDataSetDTO(),
						matValueSetTransferObject.getCqlQualityDataSetDTO(), matValueSetTransferObject.getMeasureId());
			}
		}
		return result;
	}

	@Override
	public SaveUpdateCQLResult saveCQLCodestoMeasure(MatCodeTransferObject transferObject) {

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, transferObject.getId())) {
			MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(transferObject.getId());
			if (xmlModel != null && !xmlModel.getXml().isEmpty()) {
				result = getCqlService().saveCQLCodes(xmlModel.getXml(), transferObject);
				if (result != null && result.isSuccess()) {
					String newSavedXml = saveCQLCodesInMeasureXml(result, transferObject.getId());
					if (StringUtils.isNotBlank(newSavedXml)) {

						CQLCodeSystem codeSystem = new CQLCodeSystem();
						codeSystem.setCodeSystem(transferObject.getCqlCode().getCodeSystemOID());
						codeSystem.setCodeSystemName(transferObject.getCqlCode().getCodeSystemName());
						codeSystem.setCodeSystemVersion(transferObject.getCqlCode().getCodeSystemVersion());
						SaveUpdateCQLResult updatedResult = getCqlService().saveCQLCodeSystem(newSavedXml, codeSystem);
						if (updatedResult.isSuccess()) {
							newSavedXml = saveCQLCodeSystemInMeasureXml(updatedResult, transferObject.getId());
						}
						CQLCodeWrapper wrapper = getCqlService().getCQLCodes(newSavedXml);
						if (wrapper != null && !wrapper.getCqlCodeList().isEmpty()) {
							result.setCqlCodeList(wrapper.getCqlCodeList());
							CQLModel cqlModel = cqlService.getCQLData(result.getXml())
									.getCqlModel();
							result.setCqlModel(cqlModel);
						}
					}
				}

			}
		}
		return result;
	}

	@Override
	public SaveUpdateCQLResult saveCQLCodeListToMeasure(List<CQLCode> codeList, String measureId) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		result.setSuccess(true);
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
			MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
			if (xmlModel != null && !xmlModel.getXml().isEmpty()) {
				for (CQLCode cqlCode : codeList) {
					MatCodeTransferObject transferObject = new MatCodeTransferObject();
					transferObject.setCqlCode(cqlCode);
					transferObject.setId(measureId);
					SaveUpdateCQLResult codeResult = getCqlService().saveCQLCodes(xmlModel.getXml(), transferObject);
					if (codeResult != null && codeResult.isSuccess()) {
						String newXml = appendCQLCodesInMeasureXml(codeResult, measureId, xmlModel);
						if (StringUtils.isNotBlank(newXml)) {
							xmlModel.setXml(newXml);

							CQLCodeSystem codeSystem = new CQLCodeSystem();
							codeSystem.setCodeSystem(cqlCode.getCodeSystemOID());
							codeSystem.setCodeSystemName(cqlCode.getCodeSystemName());
							codeSystem.setCodeSystemVersion(cqlCode.getCodeSystemVersion());
							SaveUpdateCQLResult updatedResult = getCqlService().saveCQLCodeSystem(xmlModel.getXml(),
									codeSystem);
							if (updatedResult.isSuccess()) {
								newXml = appendCQLCodeSystemInMeasureXml(updatedResult, measureId, xmlModel);
								if (StringUtils.isNotBlank(newXml)) {
									xmlModel.setXml(newXml);
								}
							}
						}
						result.setXml(xmlModel.getXml());
					} else {
						result.setSuccess(false);
						break;
					}
				}
				if (result.isSuccess()) {
					measurePackageService.saveMeasureXml(xmlModel);
					CQLCodeWrapper wrapper = getCqlService().getCQLCodes(xmlModel.getXml());
					if (wrapper != null && !wrapper.getCqlCodeList().isEmpty()) {
						result.setCqlCodeList(wrapper.getCqlCodeList());
						CQLModel cqlModel = cqlService.getCQLData(result.getXml())
								.getCqlModel();
						result.setCqlModel(cqlModel);
					}
				}
			}
		}

		return result;
	}

	@Override
	public SaveUpdateCQLResult modifyCQLCodeInMeasure(CQLCode codeToReplace, CQLCode replacementCode,
			String measureId) {
		SaveUpdateCQLResult cqlResult = new SaveUpdateCQLResult();
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
			MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
			if (xmlModel != null && !xmlModel.getXml().isEmpty()) {
				cqlResult = getCqlService().deleteCode(xmlModel.getXml(), codeToReplace.getId());
				if (cqlResult != null && cqlResult.isSuccess()) {
					xmlModel.setXml(cqlResult.getXml());
					MatCodeTransferObject transferObject = new MatCodeTransferObject();
					transferObject.setCqlCode(replacementCode);
					transferObject.setId(measureId);

					SaveUpdateCQLResult codeResult = getCqlService().saveCQLCodes(xmlModel.getXml(), transferObject);
					if (codeResult != null && codeResult.isSuccess()) {
						String newXml = appendCQLCodesInMeasureXml(codeResult, measureId, xmlModel);
						if (StringUtils.isNotBlank(newXml)) {
							xmlModel.setXml(newXml);

							CQLCodeSystem codeSystem = new CQLCodeSystem();
							codeSystem.setCodeSystem(replacementCode.getCodeSystemOID());
							codeSystem.setCodeSystemName(replacementCode.getCodeSystemName());
							codeSystem.setCodeSystemVersion(replacementCode.getCodeSystemVersion());
							SaveUpdateCQLResult updatedResult = getCqlService().saveCQLCodeSystem(xmlModel.getXml(),
									codeSystem);
							if (updatedResult.isSuccess()) {
								newXml = appendCQLCodeSystemInMeasureXml(updatedResult, measureId, xmlModel);
								if (StringUtils.isNotBlank(newXml)) {
									xmlModel.setXml(newXml);
								}
							}
						}
						measurePackageService.saveMeasureXml(xmlModel);
						CQLCodeWrapper wrapper = getCqlService().getCQLCodes(xmlModel.getXml());
						if (wrapper != null && !wrapper.getCqlCodeList().isEmpty()) {
							cqlResult.setCqlCodeList(wrapper.getCqlCodeList());
							CQLModel cqlModel = cqlService.getCQLData(cqlResult.getXml())
									.getCqlModel();
							cqlResult.setCqlModel(cqlModel);
						}
					}
					cqlResult.setXml(xmlModel.getXml());
				} else {
					cqlResult.setSuccess(false);
				}

			}
		}

		return cqlResult;
	}

	@Override
	public CQLCodeWrapper getCQLCodes(String measureID) {
		CQLCodeWrapper cqlCodeWrapper = new CQLCodeWrapper();
		MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureID);
		if (model != null) {
			String xmlString = model.getXml();
			cqlCodeWrapper = getCqlService().getCQLCodes(xmlString);
		}

		return cqlCodeWrapper;
	}

	/**
	 * Save CQL codes in measure xml.
	 *
	 * @param result
	 *            the result
	 */
	private String saveCQLCodesInMeasureXml(SaveUpdateCQLResult result, String measureId) {
		final String nodeName = "code";
		MeasureXmlModel xmlModal = new MeasureXmlModel();
		xmlModal.setMeasureId(measureId);
		xmlModal.setParentNode("//cqlLookUp/codes");
		xmlModal.setToReplaceNode(nodeName);
		xmlModal.setXml(result.getXml());

		return appendAndSaveNode(xmlModal, nodeName);

	}

	/**
	 * append CQL codes in measure xml.
	 *
	 * @param saveUpdateCQLResult
	 *            the result
	 */
	private String appendCQLCodesInMeasureXml(SaveUpdateCQLResult saveUpdateCQLResult, String measureId,
			MeasureXmlModel xmlModel) {
		String result = "";
		String nodeName = "code";
		MeasureXmlModel codeXmlModel = new MeasureXmlModel();
		codeXmlModel.setMeasureId(measureId);
		codeXmlModel.setParentNode("//cqlLookUp/codes");
		codeXmlModel.setToReplaceNode(nodeName);
		codeXmlModel.setXml(saveUpdateCQLResult.getXml());

		if ((xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) && StringUtils.isNotBlank(nodeName)) {
			result = callAppendNode(xmlModel, codeXmlModel.getXml(), nodeName, codeXmlModel.getParentNode());
		}
		return result;
	}

	/**
	 * append CQL code system in measure xml.
	 *
	 * @param saveUpdateCQLResult
	 *            the result
	 */
	private String appendCQLCodeSystemInMeasureXml(SaveUpdateCQLResult saveUpdateCQLResult, String measureId,
			MeasureXmlModel xmlModel) {
		String result = "";
		final String systemNodeName = "codeSystem";
		MeasureXmlModel codeSystemXmlModel = new MeasureXmlModel();
		codeSystemXmlModel.setMeasureId(measureId);
		codeSystemXmlModel.setParentNode("//cqlLookUp/codeSystems");
		codeSystemXmlModel.setToReplaceNode(systemNodeName);
		codeSystemXmlModel.setXml(saveUpdateCQLResult.getXml());

		result = callAppendNode(xmlModel, codeSystemXmlModel.getXml(), systemNodeName,
				codeSystemXmlModel.getParentNode());
		return result;
	}

	private String saveCQLCodeSystemInMeasureXml(SaveUpdateCQLResult result, String measureId) {
		final String nodeName = "codeSystem";
		MeasureXmlModel xmlModal = new MeasureXmlModel();
		xmlModal.setMeasureId(measureId);
		xmlModal.setParentNode("//cqlLookUp/codeSystems");
		xmlModal.setToReplaceNode(nodeName);
		xmlModal.setXml(result.getXml());

		return appendAndSaveNode(xmlModal, nodeName);

	}

	/**
	 * Save CQL valueset in measure xml.
	 *
	 * @param result
	 *            the result
	 */
	private void saveCQLValuesetInMeasureXml(SaveUpdateCQLResult result, String measureId) {
		final String nodeName = "valueset";
		MeasureXmlModel xmlModal = new MeasureXmlModel();
		xmlModal.setMeasureId(measureId);
		xmlModal.setParentNode("/measure/cqlLookUp/valuesets");
		xmlModal.setToReplaceNode(nodeName);

		xmlModal.setXml(result.getXml());

		appendAndSaveNode(xmlModal, nodeName);
	}

	@Override
	public SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String measureId, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) throws InvalidLibraryException {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {

			MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
			if (xmlModel != null) {
				int numberOfAssociations = cqlService.countNumberOfAssociation(measureId);
				if (numberOfAssociations < 10) {
					result = getCqlService().saveAndModifyIncludeLibrayInCQLLookUp(xmlModel.getXml(), toBeModifiedObj, currentObj, incLibraryList);

					if (result.isSuccess()) {
						xmlModel.setXml(result.getXml());
						measurePackageService.saveMeasureXml(xmlModel);
						getCqlService().saveCQLAssociation(currentObj, measureId);
						if (toBeModifiedObj != null) {
							getCqlService().deleteCQLAssociation(toBeModifiedObj, measureId);
						}
					}
				} else {
					logger.info("Already 10 associations exists for this measure.");
				}

			}
		}
		return result;
	}

	@Override
	public SaveUpdateCQLResult getMeasureCQLData(String measureId) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureId);
		Measure measure = measurePackageService.getById(measureId);

		if (model != null && StringUtils.isNotBlank(model.getXml())) {
			String xmlString = model.getXml();
			result = cqlService.getCQLData(xmlString);
			result.setSetId(measure.getMeasureSet().getId());
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}

		return result;
	}

	@Override
	public SaveUpdateCQLResult getMeasureCQLDataForLoad(String measureId) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureId);
		Measure measure = measurePackageService.getById(measureId);

		if (model != null && StringUtils.isNotBlank(model.getXml())) {
			String xmlString = model.getXml();
			result = cqlService.getCQLDataForLoad(xmlString);
			result.setSetId(measure.getMeasureSet().getId());
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}

		return result;
	}

	@Override
	public SaveUpdateCQLResult deleteInclude(String currentMeasureId, CQLIncludeLibrary toBeModifiedIncludeObj,
			List<CQLIncludeLibrary> viewIncludeLibrarys) {

		SaveUpdateCQLResult result = null;
		if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, currentMeasureId)) {

			MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(currentMeasureId);
			if (xmlModel != null) {
				result = getCqlService().deleteInclude(xmlModel.getXml(), toBeModifiedIncludeObj, viewIncludeLibrarys);
				if (result.isSuccess()) {
					xmlModel.setXml(result.getXml());
					measurePackageService.saveMeasureXml(xmlModel);
					getCqlService().deleteCQLAssociation(toBeModifiedIncludeObj, currentMeasureId);
				}
			}
		}
		return result;

	}

	@Override
	public VsacApiResult updateCQLVSACValueSets(String measureId, String expansionId, String sessionId) {
		CQLQualityDataModelWrapper details = getCQLAppliedQDMFromMeasureXml(measureId, false);
		List<CQLQualityDataSetDTO> appliedQDMList = details.getQualityDataDTO();
		VsacApiResult result = getVsacService().updateCQLVSACValueSets(appliedQDMList, expansionId, sessionId);
		if (result.isSuccess()) {
			updateAllCQLInMeasureXml(result.getCqlQualityDataSetMap(), measureId);
		}
		return result;
	}

	/**
	 * Method to Iterate through Map of Quality Data set DTO(modify With) as key and
	 * Quality Data Set DTO (modifiable) as Value and update Measure XML by calling
	 * {@link MeasureLibraryServiceImpl} method 'updateMeasureXML'.
	 * 
	 * @param map
	 *            - HaspMap
	 * @param measureId
	 *            - String
	 */
	private void updateAllCQLInMeasureXml(HashMap<CQLQualityDataSetDTO, CQLQualityDataSetDTO> map, String measureId) {
		logger.info("Start VSACAPIServiceImpl updateAllInMeasureXml :");
		Iterator<Entry<CQLQualityDataSetDTO, CQLQualityDataSetDTO>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<CQLQualityDataSetDTO, CQLQualityDataSetDTO> entrySet = it.next();
			logger.info("Calling updateMeasureXML for : " + entrySet.getKey().getOid());
			updateCQLLookUpTagWithModifiedValueSet(entrySet.getKey(), entrySet.getValue(), measureId);
			logger.info("Successfully updated Measure XML for  : " + entrySet.getKey().getOid());
		}
		logger.info("End VSACAPIServiceImpl updateAllInMeasureXml :");
	}

	@Override
	public SaveMeasureResult validateAndPackage(ManageMeasureDetailModel model, boolean shouldCreateArtifacts) {
		SaveMeasureResult result = new SaveMeasureResult();
		String measureId = model.getId();
		try {
			ValidateMeasureResult validateGroupResult = validateForGroup(model);
			if (!validateGroupResult.isValid()) {
				result.setSuccess(false);
				result.setValidateResult(validateGroupResult);
				result.setFailureReason(SaveMeasureResult.INVALID_GROUPING);
				return result;
			}

			ValidateMeasureResult validatePackageGroupingResult = validatePackageGrouping(model);
			if (!validatePackageGroupingResult.isValid()) {
				result.setSuccess(false);
				result.setValidateResult(validateGroupResult);
				result.setFailureReason(SaveMeasureResult.INVALID_PACKAGE_GROUPING);
				return result;
			}

			updateMeasureXmlForDeletedComponentMeasureAndOrg(measureId);			
			ValidateMeasureResult validateExports = validateExports(measureId);
			if(!validateExports.isValid()) {
				result.setSuccess(false);
				result.setValidateResult(validateExports);
				result.setFailureReason(SaveMeasureResult.INVALID_EXPORTS);
				return result; 
			}
			
			result = saveMeasureAtPackage(model);
			if (!result.isSuccess()) {
				return result;
			}
			
			ValidateMeasureResult measureExportValidation = createExports(measureId, null, shouldCreateArtifacts);
			if (!measureExportValidation.isValid()) {
				result.setSuccess(false);
				result.setValidateResult(measureExportValidation);
				result.setFailureReason(SaveMeasureResult.INVALID_CREATE_EXPORT);
				return result; 
			}

			auditService.recordMeasureEvent(measureId, "Measure Package Created", "", false);
			return result;
		} catch (Exception e) {
			logger.debug("validateAndPackage: " + e);
			result.setSuccess(false);
			return result;
		}
	}
	
	@Override
	public ValidateMeasureResult validateExports(final String measureId) throws Exception {
		return measurePackageService.validateExportsForCompositeMeasures(measureId);
	}

	@Override
	public ManageMeasureSearchModel searchComponentMeasures(MeasureSearchModel measureSearchModel) {
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();

		List<MeasureShareDTO> measureList = measurePackageService.searchComponentMeasuresWithFilter(measureSearchModel);
		List<MeasureShareDTO> measureTotalList = measureList;

		searchModel.setResultsTotal(measureTotalList.size());
		if (measureSearchModel.getPageSize() <= measureTotalList.size()) {
			measureList = measureTotalList.subList(measureSearchModel.getStartIndex() - 1,
					measureSearchModel.getPageSize());
		} else if (measureSearchModel.getPageSize() > measureList.size()) {
			measureList = measureTotalList.subList(measureSearchModel.getStartIndex() - 1, measureList.size());
		}
		searchModel.setStartIndex(measureSearchModel.getStartIndex());
		List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<>();
		searchModel.setData(detailModelList);
		for (MeasureShareDTO dto : measureList) {
			ManageMeasureSearchModel.Result detail = extractMeasureSearchModelDetail(currentUserId, isSuperUser, dto);
			detailModelList.add(detail);
		}

		updateMeasureFamily(detailModelList);
		
		return searchModel;
	}

	@Override
	public ManageCompositeMeasureDetailModel buildCompositeMeasure(
			ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
		Map<String, MeasurePackageOverview> packageMap = packageService
				.getClausesAndPackagesForMeasures(manageCompositeMeasureDetailModel.getAppliedComponentMeasures());
		manageCompositeMeasureDetailModel.setPackageMap(packageMap);
		return manageCompositeMeasureDetailModel;
	}

	@Override
	public CompositeMeasureValidationResult validateCompositeMeasure(
			ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
		return compositeMeasureValidator.validateCompositeMeasure(manageCompositeMeasureDetailModel);
	}

	private List<MeasureType> getMeasureTypeForComposite(List<MeasureType> list) {
		List<MeasureType> measureTypeList = new ArrayList<>();
		if(CollectionUtils.isEmpty(list)) {
			MeasureType measureType = new MeasureType();
			measureType.setAbbrName("COMPOSITE");
			measureType.setDescription("Composite");
			measureTypeList.add(measureType);
		} else {
			measureTypeList.addAll(list);
		}
		
		return measureTypeList;
	}

	@Override
	public SaveMeasureResult saveCompositeMeasure(ManageCompositeMeasureDetailModel model) {
		// Scrubbing out Mark Up.
		if (model != null) {
			model.scrubForMarkUp();
		}
		ManageCompositeMeasureModelValidator manageCompositeMeasureModelValidator = new ManageCompositeMeasureModelValidator();
		List<String> message = manageCompositeMeasureModelValidator.validateMeasure(model);
		if (message.isEmpty()) {
			String shortName = buildMeasureShortName(model);
			model.setShortName(shortName);
			Measure pkg = null;
			MeasureSet measureSet = null;
			if (model.getId() != null) {
				setMeasureCreated(true);
				// editing an existing measure
				pkg = measurePackageService.getById(model.getId());
				model.setVersionNumber(pkg.getVersion());
				if (pkg.isDraft()) {
					model.setRevisionNumber(pkg.getRevisionNumber());
				} else {
					model.setRevisionNumber("000");
				}
				if (pkg.getMeasureSet().getId() != null) {
					measureSet = measurePackageService.findMeasureSet(pkg.getMeasureSet().getId());
				}
				if (!pkg.getMeasureScoring().equalsIgnoreCase(model.getMeasScoring())) {
					// US 194 User is changing the measure scoring. Make sure to
					// delete any groupings for that measure and save.
					measurePackageService.deleteExistingPackages(pkg.getId());
				}

			} else {
				// creating a new measure.
				setMeasureCreated(false);
				pkg = new Measure();
				pkg.setReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
				pkg.setQdmVersion(MATPropertiesService.get().getQmdVersion());
				pkg.setIsCompositeMeasure(Boolean.TRUE);
				model.setRevisionNumber("000");
				measureSet = new MeasureSet();
				measureSet.setId(UUID.randomUUID().toString());
				measurePackageService.save(measureSet);
			}
			pkg.setCompositeScoring(model.getCompositeScoringMethod());
			pkg.setMeasureSet(measureSet);
			setValueFromModel(model, pkg);
			SaveMeasureResult result = new SaveMeasureResult();
			try {
				getAndValidateValueSetDate(model.getValueSetDate());
				pkg.setValueSetDate(DateUtility.addTimeToDate(pkg.getValueSetDate()));
				measurePackageService.save(pkg);
				if (isMeasureCreated()) {
					if (null != model.getAppliedComponentMeasures()) {
						deleteAndSaveComponentMeasures(model);
					}
				} else {
					saveComponentMeasures(pkg.getId(), model);
				}

			} catch (InvalidValueSetDateException e) {
				result.setSuccess(false);
				result.setFailureReason(SaveMeasureResult.INVALID_VALUE_SET_DATE);
				result.setId(pkg.getId());
				return result;
			}
			result.setSuccess(true);
			result.setId(pkg.getId());
			model.setMeasureTypeSelectedList(getMeasureTypeForComposite(model.getMeasureTypeSelectedList()));
			saveMeasureXml(createMeasureXmlModel(model, pkg, MEASURE_DETAILS, MEASURE));
			createComponentMeasureAsLibraryInMeasureXML(pkg.getId(), model);
			return result;
		} else {
			logger.info("Validation Failed for measure :: Invalid Data Issues.");
			SaveMeasureResult result = new SaveMeasureResult();
			result.setSuccess(false);
			result.setFailureReason(SaveMeasureResult.INVALID_DATA);
			return result;
		}
	}

	private void saveComponentMeasures(String compositeMeasureId, ManageCompositeMeasureDetailModel model) {
		List<ComponentMeasure> componentMeasuresList = buildComponentMeasuresList(compositeMeasureId, model);
		measurePackageService.saveComponentMeasures(componentMeasuresList);
	}

	private void deleteAndSaveComponentMeasures(ManageCompositeMeasureDetailModel model) {
		List<ComponentMeasure> componentMeasuresList = buildComponentMeasuresList(model.getId(), model);
		measurePackageService.updateComponentMeasures(model.getId(), componentMeasuresList);
	}

	private List<ComponentMeasure> buildComponentMeasuresList(String measureId,
			ManageCompositeMeasureDetailModel model) {
		return model.getAppliedComponentMeasures().stream().map(
				result -> new ComponentMeasure(measureDAO.find(measureId), measureDAO.find(result.getId()), model.getAliasMapping().get(result.getId())))
				.collect(Collectors.toList());
	}

	private void createComponentMeasureAsLibraryInMeasureXML(String measureId,
			ManageCompositeMeasureDetailModel model) {
		List<CQLIncludeLibrary> cqlIncludeLibraryList = new ArrayList<>();
		for (ManageMeasureSearchModel.Result measure : model.getAppliedComponentMeasures()) {
			CQLLibrary cqlLibrary = cqlLibraryDAO.getLibraryByMeasureId(measure.getId());
			CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(new String(cqlLibrary.getCQLByteArray()));
			CQLIncludeLibrary cqlIncludedLibraryCreatedFromComponentMeasure = new CQLIncludeLibrary();
			cqlIncludedLibraryCreatedFromComponentMeasure.setId(UUID.randomUUID().toString());
			cqlIncludedLibraryCreatedFromComponentMeasure.setAliasName(model.getAliasMapping().get(measure.getId()));
			cqlIncludedLibraryCreatedFromComponentMeasure.setCqlLibraryId(cqlLibrary.getId());
			cqlIncludedLibraryCreatedFromComponentMeasure.setVersion(cqlModel.getVersionUsed());
			cqlIncludedLibraryCreatedFromComponentMeasure.setCqlLibraryName(cqlLibrary.getName());
			cqlIncludedLibraryCreatedFromComponentMeasure.setQdmVersion(measure.getQdmVersion());
			cqlIncludedLibraryCreatedFromComponentMeasure.setSetId(measure.getMeasureSetId());
			cqlIncludedLibraryCreatedFromComponentMeasure.setIsComponent("true");
			cqlIncludedLibraryCreatedFromComponentMeasure.setMeasureId(measure.getId());
			cqlIncludeLibraryList.add(cqlIncludedLibraryCreatedFromComponentMeasure);
		}

		updateComponentMeasuresAsIncludedLibrariesInMeasureXML(measureId, cqlIncludeLibraryList);
	}

	private void updateComponentMeasuresAsIncludedLibrariesInMeasureXML(String measureId,
			List<CQLIncludeLibrary> cqlIncludeLibraryList) {
		try {
			MeasureXmlModel xmlModel = getMeasureXmlForMeasure(measureId);
			XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
			String XPATH_EXPRESSION_INCLUDES = "//cqlLookUp/includeLibrarys";

			removeAllIncludedComponentMeasuresInMeasureXML(processor);
			for (CQLIncludeLibrary library : cqlIncludeLibraryList) {
				String cqlString = getCqlService().createIncludeLibraryXML(library);
				processor.appendNode(cqlString, "includeLibrary", XPATH_EXPRESSION_INCLUDES);
				processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
			}
			xmlModel.setXml(processor.transform(processor.getOriginalDoc()));
			measurePackageService.saveMeasureXml(xmlModel);
		} catch (SAXException | IOException | MarshalException | MappingException | ValidationException e) {
			logger.error("Exception in createIncludedMeasureAsLibrary: " + e);
		}
	}

	private void removeAllIncludedComponentMeasuresInMeasureXML(XmlProcessor processor) {
		String XPATH_EXPRESSION_EXISTING_COMPONENTS = "//cqlLookUp/includeLibrarys/*";
		try {
			NodeList componentNodesList = processor.findNodeList(processor.getOriginalDoc(),
					XPATH_EXPRESSION_EXISTING_COMPONENTS);
			if (componentNodesList != null && componentNodesList.getLength() > 0) {
				int length = componentNodesList.getLength();
				for (int i = 0; i < length; i++) {
					Node componentNode = componentNodesList.item(i);
					if (isComponentNode(componentNode))
						processor.removeFromParent(componentNode);
				}
			}
		} catch (XPathExpressionException e) {
			logger.error("Exception in removeIncludedComponentMeasuresInMeasureXML: " + e);
		}
	}

	private boolean isComponentNode(Node componentNode) {
		return (componentNode.getAttributes().getNamedItem("isComponent") != null
				&& ("true").equals(componentNode.getAttributes().getNamedItem("isComponent").getNodeValue()));
	}

	@Override
	public GenericResult checkIfMeasureIsUsedAsComponentMeasure(String currentMeasureId) {
		GenericResult result = new GenericResult();
		List<ComponentMeasure> componentMeasures = componentMeasuresDAO.findByComponentMeasureId(currentMeasureId);
		if(CollectionUtils.isNotEmpty(componentMeasures)) {
			result.setSuccess(false);
			Measure measure = measureDAO.find(currentMeasureId);
			StringBuilder errorMessageBuilder = new StringBuilder();
			errorMessageBuilder.append(measure.getDescription() + " can not be deleted as it has been used as a component measure in ");
			for(int i = 0; i<componentMeasures.size(); i++) {
				ComponentMeasure componentMeasure = componentMeasures.get(i);
				if(i > 0) {
					errorMessageBuilder.append(",");
				}
				Measure compositeMeasure = measureDAO.find(componentMeasure.getCompositeMeasure().getId());
				errorMessageBuilder.append(" " + compositeMeasure.getDescription());
			}
			List<String> errorMessages = new ArrayList<>();
			errorMessages.add(errorMessageBuilder.toString());
			result.setMessages(errorMessages);
		} else {
			result.setSuccess(true);
		}
		return result;
	}

	@Override
	public Boolean isCompositeMeasure(String currentMeasureId) {
		Measure currentMeasure = measureDAO.find(currentMeasureId);
		return currentMeasure.getIsCompositeMeasure();
	}

	@Override
	public int generateAndSaveMaxEmeasureId(boolean isEditable, String measureId) {
		if (isEditable) {
			MeasurePackageService service = measurePackageService;
			Measure measure = service.getById(measureId);
			int eMeasureId = service.saveAndReturnMaxEMeasureId(measure);
			saveMaxEmeasureIdinMeasureXML(measureId, eMeasureId);
			return eMeasureId;
		}
		return -1;
	}
	
	public void saveMaxEmeasureIdinMeasureXML(String measureId, int eMeasureId) {
		MeasureXmlModel model = getMeasureXmlForMeasure(measureId);
		XmlProcessor xmlProcessor = new XmlProcessor(model.getXml());
		try {
			xmlProcessor.createEmeasureIdNode(eMeasureId);
			String newXml = xmlProcessor.transform(xmlProcessor.getOriginalDoc());
			model.setXml(newXml);

		} catch (XPathExpressionException e) {
			logger.debug("saveMaxEmeasureIdinMeasureXML:" + e.getMessage());
		}

		measurePackageService.saveMeasureXml(model);
	}

	@Override
	public String getHumanReadableForMeasureDetails(String measureId) {
		String humanReadableHTML = "";
		try {
			MeasureXmlModel measureXML = getMeasureXmlForMeasure(measureId);
			
			XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
			HumanReadableModel model = (HumanReadableModel) xmlMarshalUtil.convertXMLToObject("SimpleXMLHumanReadableModelMapping.xml", measureXML.getXml(), HumanReadableModel.class);

			if(model.getMeasureInformation().getComponentMeasures() != null) {
				for(HumanReadableComponentMeasureModel componentModel: model.getMeasureInformation().getComponentMeasures()) {
					ManageMeasureDetailModel manageComponentMeasureDetailModel = getMeasure(componentModel.getId());
					componentModel.setMeasureSetId(manageComponentMeasureDetailModel.getMeasureSetId());
					componentModel.setName(manageComponentMeasureDetailModel.getName());
					componentModel.setVersion(manageComponentMeasureDetailModel.getVersionNumber());
				}
			}

			HumanReadableMeasureInformationModel measureInformationModel = model.getMeasureInformation();

			standardizeStartAndEndDate(measureInformationModel);
			humanReadableHTML = humanReadableGenerator.generate(measureInformationModel);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getHumanReadableForMeasureDetails: " + e);
		}
		return humanReadableHTML;
	}

	private void standardizeStartAndEndDate(HumanReadableMeasureInformationModel measureInformationModel) {
		if((measureInformationModel.getMeasurementPeriodStartDate() == null && measureInformationModel.getMeasurementPeriodEndDate() == null) ||
				(measureInformationModel.getMeasurementPeriodStartDate().equals("01/01/20XX") && measureInformationModel.getMeasurementPeriodEndDate().equals("12/31/20XX"))) {
			measureInformationModel.setMeasurementPeriodStartDate("00000101");
			measureInformationModel.setMeasurementPeriodEndDate("00001231");
		} else {
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
			String startDate = LocalDate.parse(measureInformationModel.getMeasurementPeriodStartDate(), dateFormat).format(targetFormat);
			String endDate = LocalDate.parse(measureInformationModel.getMeasurementPeriodEndDate(), dateFormat).format(targetFormat);
			measureInformationModel.setMeasurementPeriodStartDate(startDate);
			measureInformationModel.setMeasurementPeriodEndDate(endDate);
		}
	}
}
