package mat.server;

import mat.client.clause.clauseworkspace.model.MeasureDetailResult;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.TransferOwnerShipModel;
import mat.client.measure.service.CQLService;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.client.shared.GenericResult;
import mat.client.shared.MatContext;
import mat.client.shared.MatException;
import mat.client.shared.MatRuntimeException;
import mat.client.umls.service.VsacApiResult;
import mat.dao.DataTypeDAO;
import mat.dao.MeasureTypeDAO;
import mat.dao.OrganizationDAO;
import mat.dao.RecentMSRActivityLogDAO;
import mat.dao.UserDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.ComponentMeasuresDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureDetailsReferenceDAO;
import mat.dao.clause.MeasureDeveloperDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dao.clause.OperatorDAO;
import mat.dao.clause.QDSAttributesDAO;
import mat.dto.MeasureTypeDTO;
import mat.dto.OperatorDTO;
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
import mat.model.clause.MeasureDetails;
import mat.model.clause.MeasureDeveloperAssociation;
import mat.model.clause.MeasureExport;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.MeasureTypeAssociation;
import mat.model.clause.MeasureXML;
import mat.model.clause.ModelTypeHelper;
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
import mat.server.cqlparser.CQLLinter;
import mat.server.cqlparser.CQLLinterConfig;
import mat.server.humanreadable.cql.CQLHumanReadableGenerator;
import mat.server.humanreadable.cql.HumanReadableMeasureInformationModel;
import mat.server.humanreadable.cql.HumanReadableModel;
import mat.server.logging.LogFactory;
import mat.server.model.MatUserDetails;
import mat.server.service.InvalidValueSetDateException;
import mat.server.service.MeasureDetailsService;
import mat.server.service.MeasureLibraryService;
import mat.server.service.MeasurePackageService;
import mat.server.service.UserService;
import mat.server.service.cql.ValidationRequest;
import mat.server.service.impl.MatContextServiceUtil;
import mat.server.service.impl.MeasureAuditServiceImpl;
import mat.server.service.impl.PatientBasedValidator;
import mat.server.service.impl.XMLMarshalUtil;
import mat.server.util.CQLUtil;
import mat.server.util.CQLValidationUtil;
import mat.server.util.ExportSimpleXML;
import mat.server.util.MATPropertiesService;
import mat.server.util.ManageMeasureDetailModelConversions;
import mat.server.util.MeasureUtility;
import mat.server.util.XmlProcessor;
import mat.server.validator.measure.CompositeMeasureValidator;
import mat.shared.CompositeMeasureValidationResult;
import mat.shared.ConstantMessages;
import mat.shared.DateStringValidator;
import mat.shared.DateUtility;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.MeasureSearchModel;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.StringUtility;
import mat.shared.cql.error.InvalidLibraryException;
import mat.shared.error.AuthenticationException;
import mat.shared.error.measure.DeleteMeasureException;
import mat.shared.validator.measure.ManageCompositeMeasureModelValidator;
import mat.shared.validator.measure.ManageMeasureModelValidator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class MeasureLibraryServiceImpl implements MeasureLibraryService {

    private static final int NESTED_CLAUSE_DEPTH = 10;

    private static final String MEASURE = "measure";

    private static final String MEASURE_DETAILS = "measureDetails";

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

    private static final Log log = LogFactory.getLog(MeasureLibraryServiceImpl.class);
    javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
    @Value("${mat.measure.current.release.version}")
    private String currentReleaseVersion;
    private Comparator<QDSAttributes> attributeComparator = (arg0, arg1) -> arg0.getName().toLowerCase().compareTo(arg1.getName().toLowerCase());
    @Autowired
    private ApplicationContext context;
    @Autowired
    private MeasureDAO measureDAO;
    @Autowired
    private MeasureXMLDAO measureXMLDAO;
    @Autowired
    private MeasureDetailsReferenceDAO measureDetailsReferenceDAO;
    @Autowired
    private MeasureDeveloperDAO measureDeveloperDAO;
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
    @Autowired
    private UserDAO userDAO;
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
    private MeasureDetailsService measureDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ComponentMeasuresDAO componentMeasuresDAO;

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private CQLHumanReadableGenerator humanReadableGenerator;

    @Autowired
    private MATPropertiesService propertiesService;

    @Autowired
    private PatientBasedValidator patientBasedValidator;

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
        log.info("Inside checkAndDeleteSubTree Method for measure Id " + measureId);
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
                    log.error("checkAndDeleteSubTree:" + e.getMessage(), e);
                }
            }
        }
        return removeUUIDMap;
    }

    @Override
    public boolean isSubTreeReferredInLogic(String measureId, String subTreeUUID) {
        log.info("Inside isSubTreeReferredInLogic Method for measure Id " + measureId);
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
                log.error("isSubTreeReferredInLogic:" + e.getMessage(), e);
            }
        }

        return false;
    }

    @Override
    public boolean isQDMVariableEnabled(String measureId, String subTreeUUID) {
        log.info("Inside isQDMVariableEnabled Method for measure Id " + measureId);

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
                log.error("isQDMVariableEnabled:" + e.getMessage(), e);
            }
        }

        return false;
    }

    @Override
    public SortedClauseMapResult saveSubTreeInMeasureXml(MeasureXmlModel measureXmlModel, String nodeNameWithSpaces,
                                                         String nodeUUID) {

        // change multiple spaces into one space for the nodeName
        String nodeName = nodeNameWithSpaces.replaceAll("( )+", " ");

        log.info("Inside saveSubTreeInMeasureXml Method for measure Id " + measureXmlModel.getMeasureId() + " .");
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
                    log.info(
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
                        log.info("Replacing SubTreeNode for UUID " + nodeUUID + " .");
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
                log.error("saveSubTreeInMeasureXml:" + e.getMessage(), e);
            }
        }
        log.info("End saveSubTreeInMeasureXml Method for measure Id " + measureXmlModel.getMeasureId() + " .");
        clauseMapResult.setClauseMap(getSortedClauseMap(measureXmlModel.getMeasureId()));
        return clauseMapResult;
    }

    @Override
    public SortedClauseMapResult saveSubTreeOccurrence(MeasureXmlModel measureXmlModel, String nodeName,
                                                       String nodeUUID) {
        log.info("Inside saveSubTreeOccurrence Method for measure Id " + measureXmlModel.getMeasureId() + " .");
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
                log.error("saveSubTreeOccurrence:" + e.getMessage(), e);
            }
        }
        sortedClauseMapResult.setClauseMap(getSortedClauseMap(measureXmlModel.getMeasureId()));
        return sortedClauseMapResult;
    }

    /**
     * Method to call XMLProcessor appendNode method to append new xml nodes
     * into existing xml.
     *
     * @param measureXmlModel the measure xml model
     * @param newXml          the new xml
     * @param nodeName        the node name
     * @param parentNodeName  the parent node name
     * @return the string
     */
    private String callAppendNode(final MeasureXmlModel measureXmlModel, final String newXml, final String nodeName,
                                  final String parentNodeName) {
        XmlProcessor xmlProcessor = new XmlProcessor(measureXmlModel.getXml());
        String result = null;
        try {
            result = xmlProcessor.appendNode(newXml, nodeName, parentNodeName);
        } catch (SAXException | IOException e) {
            log.error("callAppendNode:" + e.getMessage(), e);
        }

        return result;
    }

    /**
     * This method will use XMLProcessor to determine if which of the 3 Timing
     * Elements are missing from the measure xml 'elementLookUp' tag. Based on
     * which one is missing it will fetch it from ListObject and add it to
     * 'elementLookUp'.
     *
     * @param xmlProcessor the xml processor
     */
    @Override
    public void checkForTimingElementsAndAppend(XmlProcessor xmlProcessor) {
        // empty method body
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
                log.error("findDefaultDefinitions:" + e.getMessage(), e);
            }
        }
        return returnNodeList;
    }

    private void convertAddlXmlElementsToModel(final ManageMeasureDetailModel manageMeasureDetailModel,
                                               final Measure measure) {
        log.info("In easureLibraryServiceImpl.convertAddlXmlElementsToModel()");
        manageMeasureDetailModel.setId(measure.getId());
        manageMeasureDetailModel.setCalenderYear(manageMeasureDetailModel.getPeriodModel().isCalenderYear());
        if (!manageMeasureDetailModel.getPeriodModel().isCalenderYear()) {
            manageMeasureDetailModel.setMeasFromPeriod(manageMeasureDetailModel.getPeriodModel() != null
                    ? manageMeasureDetailModel.getPeriodModel().getStartDate() : null);
            manageMeasureDetailModel.setMeasToPeriod(manageMeasureDetailModel.getPeriodModel() != null
                    ? manageMeasureDetailModel.getPeriodModel().getStopDate() : null);
        }
        manageMeasureDetailModel.setEndorseByNQF(StringUtils.isNotBlank(manageMeasureDetailModel.getEndorsement()));
        manageMeasureDetailModel.setFormattedVersion(MeasureUtility.getVersionTextWithRevisionNumber(measure.getVersion(), measure.getRevisionNumber(), measure.isDraft()));
        manageMeasureDetailModel.setOrgVersionNumber(MeasureUtility.formatVersionText(measure.getRevisionNumber(), String.valueOf(measure.getVersionNumber())));
        manageMeasureDetailModel.setVersionNumber(MeasureUtility.getVersionText(manageMeasureDetailModel.getOrgVersionNumber(), measure.isDraft()));
        manageMeasureDetailModel.setFinalizedDate(DateUtility.convertDateToString(measure.getFinalizedDate()));
        manageMeasureDetailModel.setDraft(measure.isDraft());
        manageMeasureDetailModel.setValueSetDate(DateUtility.convertDateToStringNoTime(measure.getValueSetDate()));
        manageMeasureDetailModel.setNqfId(manageMeasureDetailModel.getNqfModel() != null
                ? manageMeasureDetailModel.getNqfModel().getExtension() : null);
        manageMeasureDetailModel.seteMeasureId(measure.geteMeasureId());
        manageMeasureDetailModel.setMeasureOwnerId(measure.getOwner().getId());
        log.info("Exiting easureLibraryServiceImpl.convertAddlXmlElementsToModel() method..");
    }

    private ManageMeasureDetailModel convertXMLToModel(String xml, Measure measure) {

        log.info("In MeasureLibraryServiceImpl.convertXMLToModel()");

        ManageMeasureDetailModel details = null;

        try {
            if (StringUtils.isNotBlank(xml)) {
                xml = new XmlProcessor(xml).getXmlByTagName(MEASURE_DETAILS);
            }

            details = (xml == null) ? createModelForNoXML(measure) : createModelFromXML(xml, measure);

        } catch (Exception e) {
            log.error("Exception in convertXMLToModel: " + e.getMessage(), e);
        }
        return details;

    }

    private ManageMeasureDetailModel createModelFromXML(String xml, final Measure measure) {
        ManageMeasureDetailModel result = null;
        try {
            if (BooleanUtils.isTrue(measure.getIsCompositeMeasure())) {
                result = createObjectFromXMLMapping(xml, "CompositeMeasureDetailsModelMapping.xml", ManageCompositeMeasureDetailModel.class);
                createMeasureDetailsModelForCompositeMeasure((ManageCompositeMeasureDetailModel) result, measure);
            } else {
                result = createObjectFromXMLMapping(xml, "MeasureDetailsModelMapping.xml", ManageMeasureDetailModel.class);
            }
            convertAddlXmlElementsToModel(result, measure);
        } catch (Exception e) {
            log.error("Exception in createModelFromXML: " + e.getMessage(), e);
        }
        return result;
    }

    private ManageMeasureDetailModel createObjectFromXMLMapping(String xml, String xmlMapping, Class<?> clazz) {

        Object result = null;
        XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
        try {
            result = xmlMarshalUtil.convertXMLToObject(xmlMapping, xml, clazz);

        } catch (MarshalException | ValidationException | MappingException | IOException e) {
            log.error("Failed to load MeasureDetailsModelMapping.xml" + e.getMessage(), e);
        }
        return (ManageMeasureDetailModel) result;

    }

    private ManageMeasureDetailModel createModelForNoXML(final Measure measure) {
        ManageMeasureDetailModel details = null;
        if (BooleanUtils.isTrue(measure.getIsCompositeMeasure())) {
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
        for (ComponentMeasure component : measure.getComponentMeasures()) {
            componentMeasuresSelectedList.add(buildSearchModelResultObjectFromMeasureId(component.getComponentMeasure().getId(), isSuperUser));
        }
        model.setAppliedComponentMeasures(componentMeasuresSelectedList);
        model.setComponentMeasuresSelectedList(componentMeasuresSelectedList);

        for (ComponentMeasure component : measure.getComponentMeasures()) {
            aliasMapping.put(component.getComponentMeasure().getId(), component.getAlias());
        }

        model.setAliasMapping(aliasMapping);
    }

    /**
     * Convert xmlto quality data dto model.
     *
     * @param xmlModel the xml model
     * @return the quality data model wrapper
     */
    private QualityDataModelWrapper convertXmltoQualityDataDTOModel(final MeasureXmlModel xmlModel) {
        log.info("In MeasureLibraryServiceImpl.convertXmltoQualityDataDTOModel()");
        QualityDataModelWrapper details = null;
        String xml = null;
        if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
            xml = new XmlProcessor(xmlModel.getXml()).getXmlByTagName(MEASURE);
        }
        try {
            if (xml != null) {
                XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
                details = (QualityDataModelWrapper) xmlMarshalUtil.convertXMLToObject(QDM_MAPPING, xml, QualityDataModelWrapper.class);
                log.info("unmarshalling complete..elementlookup" + details.getQualityDataDTO().get(0).getCodeListName());
            }

        } catch (IOException e) {
            log.info("Failed to load QualityDataModelMapping.xml" + e.getMessage(), e);
        } catch (MappingException e) {
            log.info(MAPPING_FAILED + e.getMessage(), e);
        } catch (MarshalException e) {
            log.info(UNMARSHALLING_FAILED + e.getMessage(), e);
        } catch (Exception e) {
            log.info(OTHER_EXCEPTION + e.getMessage(), e);
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

        MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureID);

        if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())
                && StringUtils.isNotBlank(nodeName)) {
            XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
            String result = xmlProcessor.replaceNode(xmlString, nodeName, MEASURE);

            MeasureXmlModel exportModal = new MeasureXmlModel();
            exportModal.setMeasureId(measureID);
            exportModal.setParentNode("/measure");
            exportModal.setToReplaceNode("elementLookUp");
            exportModal.setXml(result);
            exportModal.setMeasureModel(xmlModel.getMeasureModel());
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

        MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureID);
        if (xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) {
            XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
            String result = xmlProcessor.replaceNode(xmlString, CQL_LOOKUP, MEASURE);

            MeasureXmlModel exportModal = new MeasureXmlModel();
            exportModal.setMeasureId(measureID);
            exportModal.setParentNode("/measure");
            exportModal.setToReplaceNode(CQL_LOOKUP);
            exportModal.setXml(result);
            exportModal.setMeasureModel(xmlModel.getMeasureModel());

            measurePackageService.saveMeasureXml(exportModal);
        }

    }

    /**
     * This should be removed when we do a batch save in Measure_XML on
     * production.
     *
     * @param model   the model
     * @param measure the measure
     */
    private void createMeasureDetailsModelFromMeasure(final ManageMeasureDetailModel model, final Measure measure) {
        log.info("In MeasureLibraryServiceImpl.createMeasureDetailsModelFromMeasure()");
        model.setId(measure.getId());
        model.setMeasureName(measure.getDescription());
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
        log.info("Exiting MeasureLibraryServiceImpl.createMeasureDetailsModelFromMeasure()");
    }

    public final String createMeasureDetailsXml(final ManageMeasureDetailModel measureDetailModel) {
        log.info("creating XML from Measure Details Model");
        return createXml(measureDetailModel);
    }

    /**
     * Creates the measure xml model.
     *
     * @param measure    the measure
     * @param parentNode the parent node
     * @return the measure xml model
     */
    private MeasureXmlModel createMeasureXmlModel(final Measure measure, final String parentNode) {
        MeasureXmlModel measureXmlModel = new MeasureXmlModel();
        measureXmlModel.setMeasureId(measure.getId());
        measureXmlModel.setParentNode(parentNode);
        measureXmlModel.setMeasureModel(measure.getMeasureModel());
        return measureXmlModel;
    }

    private String createNewXML(String mapping, Object object) {
        String stream = null;
        try {
            final XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
            stream = xmlMarshalUtil.convertObjectToXML(mapping, object);

        } catch (MarshalException | ValidationException | IOException | MappingException e) {
            log.error("Exception in createExpressionXML: " + e.getMessage(), e);
        }

        log.info("Exiting ManageCodeListServiceImpl.createXml()");
        return stream;
    }

    /**
     * Creates the xml.
     *
     * @param measureDetailModel the measure detail model
     * @return the byte array output stream
     */
    private String createXml(final ManageMeasureDetailModel measureDetailModel) {
        log.info("In MeasureLibraryServiceImpl.createXml()");
        String mapping = "MeasureDetailsModelMapping.xml";
        if (measureDetailModel instanceof ManageCompositeMeasureDetailModel) {
            mapping = "CompositeMeasureDetailsModelMapping.xml";
        }
        return createNewXML(mapping, measureDetailModel);
    }

    /**
     * Extract measure search model detail.
     *
     * @param currentUserId - {@link String}
     * @param isSuperUser   - {@link Boolean}
     * @param dto           - {@link MeasureShareDTO}.
     * @return {@link ManageMeasureSearchModel.Result}.
     */
    private ManageMeasureSearchModel.Result extractMeasureSearchModelDetail(final String currentUserId,
                                                                            final boolean isSuperUser,
                                                                            final MeasureShareDTO dto,
                                                                            final String shareLevelId) {
        final StopWatch stopwatch = new StopWatch();
        stopwatch.start();

        boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
        ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
        Measure measure = measureDAO.find(dto.getMeasureId());
        detail.setName(dto.getMeasureName());
        detail.setShortName(dto.getShortName());
        detail.setMeasureModel(dto.getMeasureModel());
        detail.setCqlLibraryName(StringUtils.defaultString(measure.getCqlLibraryName()));
        detail.setScoringType(dto.getScoringType());
        detail.setStatus(dto.getStatus());
        detail.setId(dto.getMeasureId());
        detail.setStatus(dto.getStatus());
        detail.seteMeasureId(dto.geteMeasureId());
        detail.setPatientBased(dto.isPatientBased());
        detail.setQdmVersion(measure.getQdmVersion());
        detail.setFhirVersion(measure.getFhirVersion());
        detail.setIsComposite(measure.getIsCompositeMeasure());

        String measureReleaseVersion = StringUtils.trimToEmpty(measure.getReleaseVersion());

        boolean isClonable = (isOwner || isSuperUser) && !measure.getIsCompositeMeasure() && !(measureReleaseVersion.length() == 0 || measureReleaseVersion.startsWith("v4")
                || measureReleaseVersion.startsWith("v3")) && !measure.isFhirMeasure();

        detail.setClonable(isClonable);

        detail.setEditable(MatContextServiceUtil.get().isCurrentMeasureEditable(measure, currentUserId, shareLevelId, true));
        detail.setMeasureEditOrViewable(MatContextServiceUtil.get().isCurrentMeasureViewable(measure));
        detail.setExportable(dto.isPackaged());
        detail.setFhirConvertible(MatContextServiceUtil.get().isMeasureConvertible(measure));
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
        detail.setValidatable(MatContextServiceUtil.get().isValidatable(measure));
        detail.setFinalizedDate(dto.getFinalizedDate());
        detail.setMeasureSetId(dto.getMeasureSetId());
        detail.setDraftable(dto.isDraftable());
        detail.setVersionable(dto.isVersionable());

        stopwatch.stop();
        log.debug("MeasureLibraryService::extractMeasureSearchModelDetail took " + stopwatch.getTime(TimeUnit.MILLISECONDS) + "ms.");

        return detail;
    }

    /**
     * Find out maximum version number.
     *
     * @param measureSetId - {@link String}.
     * @return {@link String}. *
     */
    private String findOutMaximumVersionNumber(final String measureSetId) {
        return measurePackageService.findOutMaximumVersionNumber(measureSetId);
    }

    /**
     * Find out version number.
     *
     * @param measureId    - {@link String}.
     * @param measureSetId - {@link String}.
     * @return {@link String}. *
     */
    private String findOutVersionNumber(final String measureId, final String measureSetId) {
        return measurePackageService.findOutVersionNumber(measureId, measureSetId);
    }

    /**
     * * Find All QDM's which are used in Clause Workspace tag's or in
     * Supplemental Data Elements or in Attribute tags.
     *
     * @param appliedQDMList  the applied qdm list
     * @param measureXmlModel the measure xml model
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
                    .concat("' or /measure//measureGrouping//packageClause//elementRef/@id='")
                    .concat(dataSetDTO.getUuid()).concat("'");

            try {
                Boolean isUsed = (Boolean) xPath.evaluate(XPATH_EXPRESSION,
                        processor.getOriginalDoc().getDocumentElement(), XPathConstants.BOOLEAN);
                dataSetDTO.setUsed(isUsed);
            } catch (XPathExpressionException e) {
                log.debug("findUsedQDMs:" + e.getMessage());
            }
        }

        return appliedQDMList;
    }

    /**
     * Gets the all data type attributes.
     *
     * @param qdmName - {@link String}.
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
     * @param dataTypeName the data type name
     * @return true, if successful
     */
    public boolean checkIfQDMDataTypeIsPresent(String dataTypeName) {
        boolean checkIfDataTypeIsPresent = false;
        DataTypeDAO dataTypeDAO = context.getBean(DataTypeDAO.class);
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
     * @param userId - String logged in user id.
     * @return {@link ManageMeasureSearchModel}.
     **/
    @Override
    public ManageMeasureSearchModel getAllRecentMeasureForUser(String userId, boolean isFhirEnabled) {
        // Call to fetch
        ArrayList<RecentMSRActivityLog> recentMeasureActivityList = (ArrayList<RecentMSRActivityLog>) recentMSRActivityLogDAO
                .getRecentMeasureActivityLog(userId);
        ManageMeasureSearchModel manageMeasureSearchModel = new ManageMeasureSearchModel();
        List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<>();
        manageMeasureSearchModel.setData(detailModelList);

        boolean isSuperUser = LoggedInUserUtil.getLoggedInUserRole().equalsIgnoreCase(SecurityRole.SUPER_USER_ROLE);
        for (RecentMSRActivityLog activityLog : recentMeasureActivityList) {
            ManageMeasureSearchModel.Result detail = buildSearchModelResultObjectFromMeasureId(activityLog.getMeasureId(), isSuperUser);
            if (isFhirEnabled) {
                detailModelList.add(detail);
            } else if (!ModelTypeHelper.isFhir(detail.getMeasureModel())) {
                detailModelList.add(detail);
            }

        }
        return manageMeasureSearchModel;
    }

    private ManageMeasureSearchModel.Result buildSearchModelResultObjectFromMeasureId(String measureId, boolean isSuperUser) {
        Measure measure = measureDAO.find(measureId);
        List<Measure> measureList = new ArrayList<>();
        measureList.add(measure);
        List<Measure> measuresInSet = measureDAO.getAllMeasuresInSet(measureList);
        ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
        detail.setName(measure.getDescription());
        detail.setMeasureModel(measure.getMeasureModel());
        detail.setShortName(measure.getaBBRName());
        detail.setCqlLibraryName(StringUtils.defaultString(measure.getCqlLibraryName()));
        detail.setId(measure.getId());
        detail.setDraft(measure.isDraft());
        detail.setIsComposite(measure.getIsCompositeMeasure());
        detail.setQdmVersion(measure.getQdmVersion());
        detail.setFhirVersion(measure.getFhirVersion());
        detail.setExportable(measure.getExportedDate() != null); // to show export icon.
        detail.setFhirConvertible(MatContextServiceUtil.get().isMeasureConvertible(measure));
        detail.setHqmfReleaseVersion(measure.getReleaseVersion());
        String formattedVersion = MeasureUtility.getVersionTextWithRevisionNumber(measure.getVersion(),
                measure.getRevisionNumber(), measure.isDraft());
        detail.setVersion(formattedVersion);
        detail.setValidatable(MatContextServiceUtil.get().isValidatable(measure));
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
        detail.setMeasureEditOrViewable(MatContextServiceUtil.get().isCurrentMeasureViewable(measure));
        detail.setPatientBased(measure.getPatientBased());
        boolean isOwner = measure.getOwner().getId().equals(LoggedInUserUtil.getLoggedInUser());
        String measureReleaseVersion = StringUtils.trimToEmpty(measure.getReleaseVersion());

        boolean isClonable = (isOwner || isSuperUser) && !measure.getIsCompositeMeasure() && !(measureReleaseVersion.length() == 0 || measureReleaseVersion.startsWith("v4")
                || measureReleaseVersion.startsWith("v3")) && !ModelTypeHelper.FHIR.equals(measure.getMeasureModel());

        detail.setClonable(isClonable);
        detail.setSharable(isOwner || isSuperUser);
        // Pass measure
        boolean isEditableForVersion = MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measure.getId(), false);
        if (isEditableForVersion && !isLocked) {
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
        if (detail.isDraft()) {
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
     * @param valueSetDateStr - {@link String}.
     * @return the and validate value set date
     * @throws InvalidValueSetDateException - {@link Exception}. *
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
                log.info(" details.getQualityDataDTO().size() :" + details.getQualityDataDTO().size());
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

        log.info("finalList()of QualityDataSetDTO ::" + finalList.size());
        return details;

    }

    @Override
    public final CQLQualityDataModelWrapper getCQLAppliedQDMFromMeasureXml(final String measureId,
                                                                           final boolean checkForSupplementData) {
        return getCqlService().getCQLValusets(measureId, new CQLQualityDataModelWrapper());
    }

    @Override
    public final CQLQualityDataModelWrapper getDefaultCQLSDEFromMeasureXml(final String measureId) {
        log.info("Inside MeasureLibraryServiceImp :: getDefaultCQLSDEFromMeasureXml :: Start");
        MeasureXmlModel measureXmlModel = getMeasureXmlForMeasure(measureId);
        CQLQualityDataModelWrapper details = convertXmltoCQLQualityDataDTOModel(measureXmlModel);
        ArrayList<CQLQualityDataSetDTO> finalList = new ArrayList<>();
        if (details != null) {
            if (details.getQualityDataDTO() != null && !details.getQualityDataDTO().isEmpty()) {
                log.info(" details.getQualityDataDTO().size() :" + details.getQualityDataDTO().size());
                for (CQLQualityDataSetDTO dataSetDTO : details.getQualityDataDTO()) {
                    if (dataSetDTO.getName() != null && dataSetDTO.isSuppDataElement()) {
                        finalList.add(dataSetDTO);
                    }
                }
            }
            details.setQualityDataDTO(finalList);
        }
        log.info("finalList()of QualityDataSetDTO ::" + finalList.size());
        log.info("Inside MeasureLibraryServiceImp :: getDefaultSDEFromMeasureXml :: END");
        return details;
    }

    private CQLQualityDataModelWrapper convertXmltoCQLQualityDataDTOModel(MeasureXmlModel measureXmlModel) {

        log.info("In MeasureLibraryServiceImpl.convertXmltoCQLQualityDataDTOModel()");
        CQLQualityDataModelWrapper details = null;
        String xml = null;
        if ((measureXmlModel != null) && StringUtils.isNotBlank(measureXmlModel.getXml())) {
            xml = new XmlProcessor(measureXmlModel.getXml()).getXmlByTagName(CQL_LOOKUP);
        }
        try {
            if (xml != null) {
                XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();
                details = (CQLQualityDataModelWrapper) xmlMarshalUtil.convertXMLToObject("CQLValueSetsMapping.xml", xml, CQLQualityDataModelWrapper.class);
                log.info("unmarshalling complete..valuesets" + details.getQualityDataDTO().get(0).getName());
            }

        } catch (IOException e) {
            log.info("Failed to load CQLValueSetsMapping.xml" + e);
        } catch (MappingException e) {
            log.info(MAPPING_FAILED + e);
        } catch (MarshalException e) {
            log.info(UNMARSHALLING_FAILED + e);
        } catch (Exception e) {
            log.info(OTHER_EXCEPTION + e);
        }
        return details;

    }

    private QDSAttributesDAO getAttributeDAO() {
        return context.getBean(QDSAttributesDAO.class);

    }

    public ApplicationContext getContext() {
        return context;
    }

    /**
     * Sets the context.
     *
     * @param context the new context
     */
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    private User getLockedUser(final Measure existingMeasure) {
        return existingMeasure.getLockedUser();
    }

    @Override
    public final int getMaxEMeasureId() {
        MeasurePackageService service = measurePackageService;
        int emeasureId = service.getMaxEMeasureId();
        log.info("**********Current Max EMeasure Id from DB ******************" + emeasureId);
        return emeasureId;
    }

    @Override
    public final ManageMeasureDetailModel getMeasure(final String measureId) {
        log.info("In MeasureLibraryServiceImpl.getMeasure() method..");
        log.info("Loading Measure for MeasureId: " + measureId);
        Measure measure = measurePackageService.getById(measureId);
        if (!measure.getIsCompositeMeasure()) {
            MeasureXmlModel xmlModel = getMeasureXmlForMeasure(measureId);
            String xmlString = new XmlProcessor(xmlModel.getXml()).getXmlByTagName(MEASURE_DETAILS);

            ManageMeasureDetailModel manageMeasureDetailModel;
            MeasureDetailResult measureDetailResult;
            if (xmlString == null || measure.getMeasureDetails() != null) {
                manageMeasureDetailModel = setManageMeasureDetailModelFromDatabaseData(measure);
                measureDetailResult = getUsedStewardAndDevelopersList(measure.getId());
            } else {
                manageMeasureDetailModel = convertXMLToModel(xmlString, measure);
                measureDetailResult = getUsedStewardAndDeveloperFromXml(measure.getId());
                addIdsToMeasureType(manageMeasureDetailModel);
            }

            setCQLLibraryName(measure, manageMeasureDetailModel);
            manageMeasureDetailModel.setQdmVersion(measure.getQdmVersion());
            manageMeasureDetailModel.setFhirVersion(measure.getFhirVersion());
            manageMeasureDetailModel.setMeasureDetailResult(measureDetailResult);
            manageMeasureDetailModel.setExperimental(measure.isExperimental());
            manageMeasureDetailModel.setPopulationBasis(measure.getPopulationBasis());

            return manageMeasureDetailModel;
        } else {
            return getCompositeMeasure(measureId);
        }
    }

    private void setCQLLibraryName(Measure measure, ManageMeasureDetailModel manageMeasureDetailModel) {
        if (StringUtils.isNotBlank(measure.getCqlLibraryName())) {
            manageMeasureDetailModel.setCQLLibraryName(measure.getCqlLibraryName());
        } else {
            manageMeasureDetailModel.setCQLLibraryName(MeasureUtility.cleanString(measure.getDescription()));
        }
    }

    private void addIdsToMeasureType(ManageMeasureDetailModel model) {
        if (model.getMeasureTypeSelectedList() != null) {
            model.getMeasureTypeSelectedList().forEach(mt -> mt.setId(measureTypeDAO.getMeasureTypeByName(mt.getDescription()).getId()));
        }
    }

    private ManageMeasureDetailModel setManageMeasureDetailModelFromDatabaseData(Measure measure) {
        ManageMeasureDetailModelConversions converter = new ManageMeasureDetailModelConversions();
        if (BooleanUtils.isTrue(measure.getIsCompositeMeasure())) {
            return converter.createManageCompositeMeasureDetailModel(measure, organizationDAO, measureTypeDAO);
        } else {
            return converter.createManageMeasureDetailModel(measure, organizationDAO, measureTypeDAO);
        }
    }

    @Override
    public ManageCompositeMeasureDetailModel getCompositeMeasure(String measureId) {
        Measure measure = measurePackageService.getById(measureId);
        MeasureXmlModel xmlModel = getMeasureXmlForMeasure(measureId);
        MeasureDetailResult measureDetailResult = new MeasureDetailResult();
        String xmlString = new XmlProcessor(xmlModel.getXml()).getXmlByTagName(MEASURE_DETAILS);

        ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel = new ManageCompositeMeasureDetailModel();
        if (xmlString == null || measure.getMeasureDetails() != null) {
            manageCompositeMeasureDetailModel = (ManageCompositeMeasureDetailModel) setManageMeasureDetailModelFromDatabaseData(measure);
            measureDetailResult = getUsedStewardAndDevelopersList(measure.getId());
        } else {
            manageCompositeMeasureDetailModel = (ManageCompositeMeasureDetailModel) convertXMLToModel(xmlString, measure);
            measureDetailResult = getUsedStewardAndDeveloperFromXml(measure.getId());
            addIdsToMeasureType(manageCompositeMeasureDetailModel);
        }

        setCQLLibraryName(measure, manageCompositeMeasureDetailModel);
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

    /**
     * Gets the measure xml dao.
     *
     * @return the measure xml dao
     */
    private MeasureXMLDAO getMeasureXMLDAO() {
        return context.getBean(MeasureXMLDAO.class);
    }

    @Override
    public final MeasureXmlModel getMeasureXmlForMeasure(final String measureId) {
        log.info("In MeasureLibraryServiceImpl.getMeasureXmlForMeasure()");
        MeasureXmlModel measureXmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
        if (measureXmlModel == null) {
            log.info("Measure XML is null");
        } else {
            log.debug("XML ::: " + measureXmlModel.getXml());
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
     * @param measureId the measure id
     * @return the sorted clause map
     */
    @Override
    public LinkedHashMap<String, String> getSortedClauseMap(String measureId) {

        log.info("In MeasureLibraryServiceImpl.getSortedClauseMap()");
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
                log.debug("getSortedClauseMap:" + e);
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
        return context.getBean(VSACApiServImpl.class);
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

    private void rollbackMeasureVersioning(String toVersion, String toRevision, boolean toDraftState, Measure measure) {
        ManageMeasureDetailModel mDetail = getMeasure(measure.getId());
        mDetail.setVersionNumber(toVersion);
        mDetail.setRevisionNumber(toRevision);
        mDetail.setDraft(toDraftState);
        setValueFromModel(mDetail, measure);
        measurePackageService.save(measure);
    }

    private SaveMeasureResult incrementVersionNumberAndSave(final String maximumVersionNumber, final String incrementBy,
                                                            final Measure measure) {
        ManageMeasureDetailModel mDetail = getMeasure(measure.getId());
        BigDecimal mVersion = new BigDecimal(maximumVersionNumber);
        mVersion = mVersion.add(new BigDecimal(incrementBy));
        mDetail.setVersionNumber(mVersion.toString());
        mDetail.setRevisionNumber("000");
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
            exportCQLibraryFromMeasure(measure, mDetail, xmlModel, false);
        }

        SaveMeasureResult result = new SaveMeasureResult();
        result.setSuccess(true);
        result.setId(measure.getId());
        versionStr = MeasureUtility.formatVersionText(versionStr);
        result.setVersionStr(versionStr);
        log.info("Result passed for Version Number " + versionStr);
        return result;
    }

    /**
     * Method to set version in measureDetail/version tag and cqlLookUp/version
     * tag when measure is versioned.
     *
     * @param meas       - Measure
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
                log.error("Error setVersionInMeasureDetailsAndCQLLookUp:" + e.getMessage(), e);
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
            log.info("measureDetails Node not found. This is in measure : " + measureId);
        }

        Node cqlVersionNode = (Node) xPath.evaluate(cqlVersionXPath,
                xmlProcessor.getOriginalDoc().getDocumentElement(), XPathConstants.NODE);
        if (cqlVersionNode != null) {
            cqlVersionNode.setTextContent(MeasureUtility.formatVersionText(versionStr));
        } else {
            log.info("CQLLookUp Node not found. This is in measure : " + measureId);
        }
        return xmlProcessor.transform(xmlProcessor.getOriginalDoc());
    }

    /**
     * Checks if Measure is locked.
     *
     * @param m the Measure
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
     * @param rs            - {@link SaveMeasureResult}.
     * @param failureReason - {@link Integer}.
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
        SaveMeasureResult result = saveOrUpdateMeasure(model);
        return result;
    }

    @Override
    public final SaveMeasureResult saveOrUpdateMeasure(ManageMeasureDetailModel model) {
        // Scrubbing out Mark Up.
        if (model != null) {
            model.scrubForMarkUp();
        }

        boolean isFhir = ModelTypeHelper.isFhir(model.getMeasureModel());

        SaveMeasureResult result = new SaveMeasureResult();
        if (libraryNameExists(model.getCQLLibraryName(), model.getMeasureSetId())) {
            result.setFailureReason(SaveUpdateCQLResult.DUPLICATE_LIBRARY_NAME);
            result.setSuccess(false);
            return result;
        }

        if (CQLValidationUtil.isCQLReservedWord(model.getCQLLibraryName())) {
            result.setFailureReason(SaveUpdateCQLResult.DUPLICATE_CQL_KEYWORD);
            result.setSuccess(false);
            return result;
        }

        ManageMeasureModelValidator manageMeasureModelValidator = new ManageMeasureModelValidator();
        List<String> message = manageMeasureModelValidator.validateMeasure(model);
        String existingMeasureScoringType = "";
        String existingMeasurePopulationBasis = "";
        if (message.isEmpty()) {
            Measure measure = null;
            MeasureSet measureSet = null;
            if (model.getId() != null) {
                // editing an existing measure
                measure = measurePackageService.getById(model.getId());
                model.setVersionNumber(measure.getVersion());
                if (measure.isDraft()) {
                    model.setRevisionNumber(measure.getRevisionNumber());
                } else {
                    model.setRevisionNumber("000");
                }
                if (measure.getMeasureSet().getId() != null) {
                    measureSet = measurePackageService.findMeasureSet(measure.getMeasureSet().getId());
                }
                if (!measure.getMeasureScoring().equalsIgnoreCase(model.getMeasScoring())) {
                    // US 194 User is changing the measure scoring. Make sure to
                    // delete any groupings for that measure and save.
                    measurePackageService.deleteExistingPackages(measure.getId());
                }
                existingMeasureScoringType = measure.getMeasureScoring();
                existingMeasurePopulationBasis = measure.getPopulationBasis();
            } else {
                // creating a new measure.
                measure = new Measure();
                measure.setReleaseVersion(propertiesService.getCurrentReleaseVersion());
                if (!isFhir) {
                    measure.setQdmVersion(propertiesService.getQdmVersion());
                } else {
                    measure.setFhirVersion(propertiesService.getFhirVersion());
                    measure.setPopulationBasis("boolean");//todo MAT-1546 add boolean as default value
                    measure.setMeasurementPeriodFrom(getNextCalenderYearFromDate());
                    measure.setMeasurementPeriodTo(getNextCalenderYearToDate());
                }
                measure.setCqlLibraryName(model.getCQLLibraryName());
                model.setRevisionNumber("000");
                measureSet = new MeasureSet();
                measureSet.setId(UUID.randomUUID().toString());
                measurePackageService.save(measureSet);
            }

            if (measure.getMeasureDetails() == null) {
                MeasureDetails measureDetails = new MeasureDetails();
                if (isFhir) {
                    //Defaulted on new measures for fhir.
                    measureDetails.setImprovementNotation("increase");
                }
                measureDetails.setMeasure(measure);
                measure.setMeasureDetails(measureDetails);
            }

            measure.setMeasureSet(measureSet);
            setValueFromModel(model, measure);

            try {
                getAndValidateValueSetDate(model.getValueSetDate());
                measure.setValueSetDate(DateUtility.addTimeToDate(measure.getValueSetDate()));
                measurePackageService.save(measure);
            } catch (InvalidValueSetDateException e) {
                result.setSuccess(false);
                result.setFailureReason(SaveMeasureResult.INVALID_VALUE_SET_DATE);
                result.setId(measure.getId());
                return result;
            }
            result.setVersionStr(MeasureUtility.formatVersionText(measure.getRevisionNumber(), measure.getVersion()));
            result.setSuccess(true);
            result.setId(measure.getId());
            saveMeasureXml(createMeasureXmlModel(measure, MEASURE), measure.getId(), StringUtils.equals("FHIR", model.getMeasureModel()));
            // Adds population nodes to new measures
            updateMeasureXml(model, measure, existingMeasureScoringType, existingMeasurePopulationBasis);
            return result;
        } else {
            log.info("Validation Failed for measure :: Invalid Data Issues.");
            result.setSuccess(false);
            result.setFailureReason(SaveMeasureResult.INVALID_DATA);
            return result;
        }
    }

    private Timestamp getNextCalenderYearFromDate() {
        Timestamp timestamp = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            int year = Calendar.getInstance().get(Calendar.YEAR);
            Date fromDate = dateFormat.parse("01/01/" + ++year);
            timestamp = new java.sql.Timestamp(fromDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    private Timestamp getNextCalenderYearToDate() {
        Timestamp timestamp = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            int year = Calendar.getInstance().get(Calendar.YEAR);
            Date fromDate = dateFormat.parse("12/31/" + ++year);
            timestamp = new java.sql.Timestamp(fromDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    @Override
    public void deleteMeasure(String measureId, String loggedInUserId) throws DeleteMeasureException, AuthenticationException {
        log.info("Measure Deletion Started for measure Id :: " + measureId);

        GenericResult isUsedAsComponentMeasureResult = checkIfMeasureIsUsedAsComponentMeasure(measureId);
        if (!isUsedAsComponentMeasureResult.isSuccess()) {
            log.error("Failed to delete measure: " + isUsedAsComponentMeasureResult.getMessages().get(0));
            throw new DeleteMeasureException(isUsedAsComponentMeasureResult.getMessages().get(0));
        }

        deleteMeasure(measureId);
        log.info("Measure Deleted Successfully: " + measureId);
    }

    private void deleteMeasure(String measureId) {
        Measure m = measureDAO.find(measureId);
        CQLLibrary cqlLibrary = cqlLibraryDAO.getLibraryByMeasureId(measureId);

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
            if (cqlLibrary != null) {
                cqlLibraryDAO.delete(cqlLibrary.getId());
            }
        }
    }

    private void removeUnusedLibraries(MeasureXmlModel measureXmlModel, SaveUpdateCQLResult cqlResult) {
        String measureXml = measureXmlModel.getXml();
        XmlProcessor processor = new XmlProcessor(measureXml);
        try {
            CQLUtil.removeUnusedIncludes(processor.getOriginalDoc(), cqlResult.getUsedCQLArtifacts().getUsedCQLLibraries(), cqlResult.getCqlModel());
        } catch (XPathExpressionException e) {
            log.error("Error in removeUnusedLibraries:" + e.getMessage(), e);
        }

        String updatedMeasureXml = new String(processor.transform(processor.getOriginalDoc()).getBytes());
        measureXmlModel.setXml(updatedMeasureXml);
        measurePackageService.saveMeasureXml(measureXmlModel);
    }

    private void removeUnusedComponents(MeasureXmlModel measureXmlModel, Measure measure) {
        XmlProcessor processor = new XmlProcessor(measureXmlModel.getXml());
        log.info("Remove Unused Component Measures");

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

        log.info("Finished Removing Unused Component Measures");
    }

    @Override
    public final SaveMeasureResult saveFinalizedVersion(final String measureId, final boolean isMajor, final String version, boolean shouldPackage, boolean ignoreUnusedLibraries) {
        log.info("In MeasureLibraryServiceImpl.saveFinalizedVersion() method..");
        Measure m = measurePackageService.getById(measureId);
        log.info("Measure Loaded for: " + measureId);

        SaveMeasureResult saveMeasureResult = new SaveMeasureResult();

        boolean isMeasureVersionable = MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId);
        if (!isMeasureVersionable) {
            log.error("Measure Package and Version Failed for measure with id " + measureId + " -> INVALID_DATA");
            return returnFailureReason(saveMeasureResult, SaveMeasureResult.INVALID_DATA);
        }

        SaveUpdateCQLResult cqlResult = getMeasureCQLFileData(measureId);

        if (cqlResult.getFailureReason() == SaveUpdateCQLResult.DUPLICATE_LIBRARY_NAME) {
            log.error("Measure Package and Version Failed for measure with id " + measureId + " -> DUPLICATE_LIBRARY_NAME DATA");
            return returnFailureReason(saveMeasureResult, SaveUpdateCQLResult.DUPLICATE_LIBRARY_NAME);
        }

        if (!cqlResult.getCqlErrors().isEmpty() || !cqlResult.getLinterErrors().isEmpty() || !cqlResult.isDatatypeUsedCorrectly()) {
            log.error("Measure Package and Version Failed for measure with id " + measureId + " -> INVALID_CQL_DATA");
            log.error("CQL Errors: " + cqlResult.getCqlErrors());
            log.error("CQL Linter Errors: " + cqlResult.getLinterErrors());
            log.error("CQL Datatype Used Correctly: " + cqlResult.isDatatypeUsedCorrectly());
            return returnFailureReason(saveMeasureResult, SaveMeasureResult.INVALID_CQL_DATA);
        }

        // return error if there are unused libraries in the measure
        MeasureXmlModel measureXmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
        String measureXml = measureXmlModel.getXml();
        if (!m.isFhirMeasure() &&
                !ignoreUnusedLibraries &&
                CQLUtil.checkForUnusedIncludes(measureXml, cqlResult.getUsedCQLArtifacts().getUsedCQLLibraries())) {
            // Currently fhir doesn't check for unused libraries or cql structures. A future enhancement will add it.
            saveMeasureResult.setFailureReason(SaveMeasureResult.UNUSED_LIBRARY_FAIL);
            log.info("Measure Package and Version Failed for measure with id " + measureId + " because there are libraries that are unused.");
            return saveMeasureResult;
        }

        if (shouldPackage) {
            SaveMeasureResult validatePackageResult = validateAndPackage(getMeasure(measureId), false);
            if (!validatePackageResult.isSuccess()) {
                saveMeasureResult.setValidateResult(validatePackageResult.getValidateResult());
                log.error("Validate Package Result" + validatePackageResult);
                log.error("Measure Package and Version Failed for measure with id " + measureId + " -> PACKAGE_FAIL");
                return returnFailureReason(saveMeasureResult, SaveMeasureResult.PACKAGE_FAIL);
            }
        }

        if (!m.isFhirMeasure()) {
            removeUnusedLibraries(measureXmlModel, cqlResult);
            removeUnusedComponents(measureXmlModel, m);
        }

        return updateVersionAndExports(isMajor, version, m);
    }

    private SaveMeasureResult updateVersionAndExports(final boolean isMajor, final String version, Measure measure) {
        String priorVersion = measure.getVersion();
        String priorRevision = measure.getRevisionNumber();
        boolean priorDraft = measure.isDraft();

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
            try {
                //With the intro of FHIR, microservice calls are being made here which can fail.
                //Add logic to roll back the version if it fails.
                updateVersionInSimpleXMLAndGenerateArtifacts(measure);
            } catch (RuntimeException re) {
                log.warn("updateVersionInSimpleXMLAndGenerateArtifacts failed with exception. " +
                        "Rolling back versioning. measure=" + measure.getId() +
                        " priorVersion=" + priorVersion +
                        " priorRevision=" + priorRevision +
                        " priorDraft=" + priorDraft);
                try {
                    rollbackMeasureVersioning(priorVersion, priorRevision, priorDraft, measure);
                } catch (RuntimeException re2) {
                    log.error("Error rolling back measure.", re2);
                    log.error("Measure versioned with no way to roll back!. " +
                            "measure=" + measure.getId() +
                            " priorVersion=" + priorVersion +
                            " priorRevision=" + priorRevision +
                            " priorDraft=" + priorDraft);
                }
                throw re;
            }
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
            log.info("Max Version Number loaded from DB: " + versionNumber);
        } else {
            String selectedVersion = StringUtils.substringAfter(version, "v");
            log.info("Min Version number after trim: " + selectedVersion);
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
                log.error("updateVersionInSimpleXMLAndGenerateArtifacts:" + e.getMessage(), e);
            }

            measureExport.setSimpleXML(updatedSimpleXML);
            measureExportDAO.save(measureExport);
            measurePackageService.createPackageArtifacts(measure, measureExport);
        }
    }

    public void exportDraftCQLLibraryForMeasure(Measure measure) {
        MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measure.getId());
        ManageMeasureDetailModel mDetail = getMeasure(measure.getId());
        exportCQLibraryFromMeasure(measure, mDetail, xmlModel, true);
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
                                            MeasureXmlModel xmlModel, boolean isDraft) {
        log.info("exportCQLibraryFromMeasure method :: Start");

        String cqlLibraryName = "";
        String cqlQdmVersion = "";
        byte[] cqlByteArray = null;
        CQLLibrary cqlLibrary = cqlLibraryDAO.getLibraryByMeasureId(measure.getId());
        if (cqlLibrary == null) {
            cqlLibrary = new CQLLibrary();
        }

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

                    long time;
                    if (isDraft && mDetail.getFinalizedDate() == null) {
                        time = System.currentTimeMillis();
                    } else {
                        Date date = null;
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:ss");
                            date = dateFormat.parse(mDetail.getFinalizedDate());
                            time = date.getTime();
                        } catch (ParseException e) {
                            log.error("Exception in exportCQLibraryFromMeasure when formatting date: " + e.getMessage(), e);
                            throw new MatRuntimeException("If !isDraft, mDetail.getFinalizedDate can not be null!");
                        }
                    }
                    Timestamp timestamp = new Timestamp(time);

                    cqlLibrary.setMeasureId(mDetail.getId());
                    cqlLibrary.setOwnerId(measure.getOwner());
                    cqlLibrary.setSetId(measure.getMeasureSet().getId());
                    cqlLibrary.setVersion(new BigDecimal(measure.getVersionNumber()).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                    cqlLibrary.setReleaseVersion(measure.getReleaseVersion());
                    cqlLibrary.setQdmVersion(cqlQdmVersion);
                    cqlLibrary.setFinalizedDate(timestamp);
                    cqlLibrary.setDraft(isDraft);
                    cqlLibrary.setRevisionNumber(mDetail.getRevisionNumber());
                    cqlLibrary.setCQLByteArray(cqlByteArray);
                    cqlLibrary.setDescription(measure.getDescription());
                    cqlLibrary.setStewardId(measure.getMeasureStewardId());
                    cqlLibrary.setExperimental(false);
                    cqlLibraryService.save(cqlLibrary);
                    cqlLibraryService.saveQdmCQLLibraryExport(cqlLibrary, cqlXML);
                    log.info("Versioned CQL successfull.");
                } else {
                    log.info("Versioning of cql failed as no cqlLookUpNode available");
                }

            } catch (XPathExpressionException e) {
                log.error("exportCQLibraryFromMeasure:" + e.getMessage(), e);
            }
        }
        log.info("exportCQLibraryFromMeasure method :: END");
    }

    @Override
    public final SaveMeasureResult saveMeasureDetails(final ManageMeasureDetailModel model) throws MatException {
        log.info("In MeasureLibraryServiceImpl.saveMeasureDetails() method..");
        Measure measure = measurePackageService.getById(model.getId());
        setCQLLibraryName(measure, model);
        model.scrubForMarkUp();
        ManageMeasureModelValidator manageMeasureModelValidator = new ManageMeasureModelValidator();
        List<String> message = manageMeasureModelValidator.validateMeasure(model);
        if ((CollectionUtils.isNotEmpty(model.getMeasureTypeSelectedList()) && isDuplicateMeasureType(model.getMeasureTypeSelectedList())) || (CollectionUtils.isNotEmpty(model.getAuthorSelectedList()) && isDuplicateMeasureDeveloper(model.getAuthorSelectedList()))) {
            throw new MatException();
        }

        String existingMeasureScoringType = "";
        String existingMeasurePopulationBasis = "";
        if (message.isEmpty()) {
            if (model.getId() != null) {
                existingMeasureScoringType = measure.getMeasureScoring();
                existingMeasurePopulationBasis = measure.getPopulationBasis();

                measure.setDescription(model.getMeasureName());
                String shortName = buildMeasureShortName(model);

                model.setShortName(shortName);
                measure.setaBBRName(shortName);
                measure.setMeasureScoring(model.getMeasScoring());
                measure.setPatientBased(model.isPatientBased());

                MeasureDetails measureDetails = generateMeasureDetailsFromDatabaseData(model, measure);
                measure.setMeasureDetails(measureDetails);
                measure.setMeasureTypes(getSelectedMeasureTypes(measure, model));
                measure.setMeasureDevelopers(getSelectedDeveloperList(measure, model));
                measure.setMeasureStewardId(model.getStewardId());

                measure.seteMeasureId(model.geteMeasureId());
                measure.setNqfNumber(model.getNqfId());
                measure.setExperimental(model.isExperimental());

                if (model.getPopulationBasis() == null) {
                    measure.setPopulationBasis(null);
                } else {
                    measure.setPopulationBasis(model.getPopulationBasis().equalsIgnoreCase("Boolean") ? "boolean" : model.getPopulationBasis());
                }
                calculateCalendarYearForMeasure(model, measure);

                measurePackageService.save(measure);
            }
            model.setRevisionNumber(measure.getRevisionNumber());

            updateMeasureXml(model, measure, existingMeasureScoringType, existingMeasurePopulationBasis);

            SaveMeasureResult result = new SaveMeasureResult();
            result.setSuccess(true);
            log.info("Saving of Measure Details Success");
            return result;
        } else {
            SaveMeasureResult result = new SaveMeasureResult();
            result.setSuccess(false);
            result.setMessages(message);
            log.info("Saving of Measure Details Failed. Invalid Data issue.");
            return result;
        }
    }

    private boolean isDuplicateMeasureType(List<MeasureType> measureTypes) {
        Set<MeasureType> allItems = new HashSet<>();
        Set<MeasureType> duplicates = measureTypes.stream()
                .filter(n -> !allItems.add(n)) //Set.add() returns false if the item was already in the set.
                .collect(Collectors.toSet());
        return !duplicates.isEmpty();
    }

    private boolean isDuplicateMeasureDeveloper(List<Author> measureDevelopers) {
        Set<Author> allItems = new HashSet<>();
        Set<Author> duplicates = measureDevelopers.stream()
                .filter(n -> !allItems.add(n)) //Set.add() returns false if the item was already in the set.
                .collect(Collectors.toSet());
        return !duplicates.isEmpty();
    }

    private void updateMeasureXml(final ManageMeasureDetailModel model, Measure measure,
                                  String existingMeasureScoringType, String existingMeasurePopulationBasis) {
        // update measure xml biased off of measure details changed
        MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measure.getId());
        XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());

        xmlProcessor.checkForScoringType(propertiesService.getQdmVersion(), model.getMeasScoring(), model.isPatientBased());
        if (!existingMeasureScoringType.equalsIgnoreCase(model.getMeasScoring()) || (model.getPopulationBasis() != null && !model.getPopulationBasis().equalsIgnoreCase(existingMeasurePopulationBasis))) {
            deleteExistingGroupings(xmlProcessor);
            MatContext.get().setCurrentMeasureScoringType(model.getMeasScoring());
        }

        String newXml = xmlProcessor.transform(xmlProcessor.getOriginalDoc());
        xmlModel.setXml(newXml);
        measurePackageService.saveMeasureXml(xmlModel);
    }

    private void calculateCalendarYearForMeasure(final ManageMeasureDetailModel model, Measure measure) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        int year = Calendar.getInstance().get(Calendar.YEAR);
        try {
            if (model.isCalenderYear()) {
                if (ModelTypeHelper.isFhir(model.getMeasureModel())) {
                    Date fromDate = dateFormat.parse("01/01/" + ++year);
                    Timestamp fromTimestamp = new java.sql.Timestamp(fromDate.getTime());
                    measure.setMeasurementPeriodFrom(fromTimestamp);

                    Date toDate = dateFormat.parse("12/31/" + year);
                    Timestamp toTimestamp = new java.sql.Timestamp(toDate.getTime());
                    measure.setMeasurementPeriodTo(toTimestamp);
                } else {
                    measure.setMeasurementPeriodFrom(null);
                    measure.setMeasurementPeriodTo(null);
                }
            } else {
                Date fromDate = dateFormat.parse(model.getMeasFromPeriod());
                Timestamp fromTimestamp = new java.sql.Timestamp(fromDate.getTime());
                measure.setMeasurementPeriodFrom(fromTimestamp);

                Date toDate = dateFormat.parse(model.getMeasToPeriod());
                Timestamp toTimestamp = new java.sql.Timestamp(toDate.getTime());
                measure.setMeasurementPeriodTo(toTimestamp);
            }
        } catch (Exception e) {
            // look the origin of exception
            log.debug("failed to convert date", e);
        }
    }

    private MeasureDetails generateMeasureDetailsFromDatabaseData(final ManageMeasureDetailModel model,
                                                                  Measure measure) {
        //delete existing references
        if (measure.getMeasureDetails() != null) {
            measureDetailsReferenceDAO.deleteAllByMeasureDetailsId(measure.getMeasureDetails().getId());
        }
        //delete existing developer associations
        measureDeveloperDAO.deleteAllDeveloperAssociationsByMeasureId(measure.getId());
        //delete existing measure type associations
        measureTypeDAO.deleteAllMeasureTypeAssociationsByMeasureId(measure.getId());
        MeasureDetails measureDetails = measureDetailsService.getMeasureDetailFromManageMeasureDetailsModel(measure.getMeasureDetails(), model);
        return measureDetails;
    }

    private List<MeasureDeveloperAssociation> getSelectedDeveloperList(Measure measure,
                                                                       ManageMeasureDetailModel model) {
        List<MeasureDeveloperAssociation> mdaList = new ArrayList<>();
        List<Author> mdList = model.getAuthorSelectedList();
        if (mdList != null) {
            mdList.forEach(typ -> mdaList.add(new MeasureDeveloperAssociation(measure, getOrganizationFromOrgOID(typ.getOrgId()))));
        }
        return mdaList;
    }

    private Organization getOrganizationFromOrgOID(String orgOID) {
        return organizationDAO.findByOid(orgOID);
    }

    private List<MeasureTypeAssociation> getSelectedMeasureTypes(Measure measure, ManageMeasureDetailModel model) {
        List<MeasureTypeAssociation> measureTypeAssociationList = new ArrayList<>();
        List<MeasureType> measureTypeList = model.getMeasureTypeSelectedList();
        if (CollectionUtils.isNotEmpty(measureTypeList)) {
            measureTypeList.forEach(typ -> measureTypeAssociationList.add(new MeasureTypeAssociation(measure, findMeasureTypeById(typ.getId()))));
        }
        return measureTypeAssociationList;
    }

    private MeasureType findMeasureTypeById(String measureTypeId) {
        return measureTypeDAO.find(measureTypeId);
    }

    private String buildMeasureShortName(final ManageMeasureDetailModel model) {
        String shortName = "";
        if (!StringUtility.isEmptyOrNull(model.getShortName())) {
            shortName = model.getShortName();
            if (shortName.length() > 32) {
                shortName = shortName.substring(0, 32);
            }
        }
        return shortName;
    }

    @Override
    public final void saveMeasureXml(final MeasureXmlModel measureXmlModel, String measureId, boolean isFhir) {

        Measure measure = measureDAO.find(measureId);
        String libraryName = measure.getCqlLibraryName();
        String version = MeasureUtility.formatVersionText(measure.getRevisionNumber(), measure.getVersion());

        MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
        if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
            saveExistingNonBlankMeasureXml(measureXmlModel, version, xmlModel);
        } else {
            saveNewMeasureXml(measureXmlModel, libraryName, version, isFhir);
        }
    }

    private void saveNewMeasureXml(MeasureXmlModel measureXmlModel, String libraryName, String version,
                                   boolean isFhir) {
        XmlProcessor processor = new XmlProcessor("<measure> </measure>");
        processor.transform(processor.getOriginalDoc());
        try {
            String cqlLookUpTag = createNewCqlLookupTag(libraryName, version, isFhir);
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

                    MeasureUtility.updateCQLVersion(processor, version);
                    MeasureUtility.updateModelVersion(processor, ModelTypeHelper.isFhir(measureXmlModel.getMeasureModel()));


                    measureXmlModel.setXml(processor.transform(processor.getOriginalDoc()));
                    measurePackageService.saveMeasureXml(measureXmlModel);
                } catch (XPathExpressionException e) {
                    log.error("Exception in saveMeasureXml while updating cqldefinition :" + e.getMessage(), e);
                }

                measureXmlModel.setXml(processor.transform(processor.getOriginalDoc()));
            } else {
                log.info("Measure Xml save failed for measure " + measureXmlModel.getMeasureId());
            }

        } catch (Exception e) {
            log.debug("Exception in saveMeasureXml :" + e);
        }
    }

    private void saveExistingNonBlankMeasureXml(MeasureXmlModel measureXmlModel, String version, MeasureXmlModel
            xmlModel) {
        if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureXmlModel.getMeasureId())) {
            XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
            try {
                MeasureUtility.updateCQLVersion(xmlProcessor, version);
                MeasureUtility.updateModelVersion(xmlProcessor, ModelTypeHelper.isFhir(measureXmlModel.getMeasureModel()));
                String newXml = xmlProcessor.transform(xmlProcessor.getOriginalDoc());
                measureXmlModel.setXml(newXml);
                measurePackageService.saveMeasureXml(measureXmlModel);
            } catch (XPathExpressionException e) {
                log.debug("Exception in saveMeasureXml :" + e.getMessage(), e);
            }

        }
    }

    private String createNewCqlLookupTag(String libraryName, String version, boolean isFhir) {
        XmlProcessor cqlXmlProcessor = cqlLibraryService.loadCQLXmlTemplateFile(isFhir);
        return cqlLibraryService.getCQLLookUpXml((MeasureUtility.cleanString(libraryName)),
                version,
                cqlXmlProcessor,
                "//measure",
                isFhir);
    }

    /**
     * Deletes the existing groupings when scoring type selection is changed and
     * saved.
     *
     * @param xmlProcessor the xml processor
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
            log.error("deleteExistingGroupings:" + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ManageMeasureSearchModel search(MeasureSearchModel measureSearchModel) {
        log.debug("MeasureLibraryServiceImpl::search - enter");
        final StopWatch searchStopwatch = new StopWatch();
        searchStopwatch.start();
        String currentUserId = LoggedInUserUtil.getLoggedInUser();
        String userRole = LoggedInUserUtil.getLoggedInUserRole();
        boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
        ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();

        if (SecurityRole.ADMIN_ROLE.equals(userRole)) {
            searchForAdmin(measureSearchModel, searchModel);
        } else {
            searchForUser(measureSearchModel, currentUserId, isSuperUser, searchModel);
        }

        searchStopwatch.stop();
        log.info("MeasureLibraryServiceImpl::search took " + searchStopwatch.getTime(TimeUnit.MILLISECONDS) + "ms.");
        log.debug("MeasureLibraryServiceImpl::search - exit");
        return searchModel;
    }

    private void searchForAdmin(MeasureSearchModel measureSearchModel, ManageMeasureSearchModel searchModel) {
        List<MeasureShareDTO> measureTotalList = measurePackageService.searchForAdminWithFilter(
                measureSearchModel.getSearchTerm(), 1, Integer.MAX_VALUE, measureSearchModel.getIsMyMeasureSearch());

        List<Result> detailModelList = new ArrayList<>();
        searchModel.setResultsTotal(measureTotalList.size());

        List<MeasureShareDTO> measureList = getSublist(measureSearchModel, measureTotalList);
        searchModel.setStartIndex(measureSearchModel.getStartIndex());
        searchModel.setData(detailModelList);

        for (MeasureShareDTO dto : measureList) {
            Result detail = new Result();
            detail.setName(dto.getMeasureName());
            detail.setId(dto.getMeasureId());
            detail.setMeasureModel(dto.getMeasureModel());
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
    }

    private void searchForUser(MeasureSearchModel measureSearchModel, String currentUserId,
                               boolean isSuperUser, ManageMeasureSearchModel searchModel) {
        log.debug("MeasureLibraryServiceImpl::searchForUser - enter");
        List<MeasureShareDTO> measureTotalList = measurePackageService.searchWithFilter(measureSearchModel);

        searchModel.setResultsTotal(measureTotalList.size());
        searchModel.setStartIndex(measureSearchModel.getStartIndex());

        List<MeasureShareDTO> measureList = getSublist(measureSearchModel, measureTotalList);

        List<Result> detailModelList = extractModelDetailList(currentUserId, isSuperUser, measureList);

        searchModel.setData(detailModelList);
        log.debug("MeasureLibraryServiceImpl::searchForUser - exit");
    }

    private List<MeasureShareDTO> getSublist(MeasureSearchModel
                                                     measureSearchModel, List<MeasureShareDTO> measureTotalList) {
        log.debug("MeasureLibraryServiceImpl::getSublist - enter");
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<MeasureShareDTO> measureList;
        if (measureSearchModel.getPageSize() <= measureTotalList.size()) {
            measureList = measureTotalList.subList(measureSearchModel.getStartIndex() - 1,
                    measureSearchModel.getPageSize());
        } else {
            measureList = measureTotalList.subList(measureSearchModel.getStartIndex() - 1, measureTotalList.size());
        }
        log.debug("MeasureLibraryServiceImpl::getSublist took " + stopWatch.getTime(TimeUnit.MILLISECONDS) + "ms.");
        log.debug("MeasureLibraryServiceImpl::getSublist - exit");
        return measureList;
    }

    private List<Result> extractModelDetailList(String currentUserId, boolean isSuperUser, List<
            MeasureShareDTO> measureList) {
        log.debug("MeasureLibraryServiceImpl::extractModelDetailList - enter");
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Set<String> measureSetIds = measureList.stream().map(m -> m.getMeasureSetId()).collect(Collectors.toSet());
        Map<String, String> shareLevels = measureDAO.findShareLevelsForUser(currentUserId, measureSetIds);

        List<Result> detailModelList = measureList.stream()
                .map(m -> extractMeasureSearchModelDetail(currentUserId, isSuperUser, m, shareLevels.get(m.getMeasureSetId())))
                .collect(Collectors.toList());

        updateMeasureFamily(detailModelList);

        stopWatch.stop();
        log.info("MeasureLibraryServiceImpl::extractModelDetailList took " + stopWatch.getTime(TimeUnit.MILLISECONDS) + "ms.");
        log.debug("MeasureLibraryServiceImpl::extractModelDetailList - exit");
        return detailModelList;
    }

    /**
     * Update measure family.
     *
     * @param detailModelList the detail model list
     */
    public void updateMeasureFamily(List<ManageMeasureSearchModel.Result> detailModelList) {
        boolean isFamily = false;
        if (detailModelList != null && !detailModelList.isEmpty()) {
            for (int i = 0; i < detailModelList.size(); i++) {
                Result currentMeasure = detailModelList.get(i);
                if (i > 0) {
                    Result previousMeasure = detailModelList.get(i - 1);
                    if (currentMeasure.getMeasureSetId().equalsIgnoreCase(previousMeasure.getMeasureSetId())) {
                        currentMeasure.setMeasureFamily(!isFamily);
                    } else {
                        currentMeasure.setMeasureFamily(isFamily);
                    }
                } else {
                    currentMeasure.setMeasureFamily(isFamily);
                }
            }
        }
    }

    @Override
    public final TransferOwnerShipModel searchUsers(final String searchText, final int startIndex,
                                                    final int pageSize) {
        UserService usersService = userService;
        List<User> searchResults;
        if (searchText.equals("")) {
            searchResults = usersService.searchNonAdminUsers("", startIndex, pageSize);
        } else {
            searchResults = usersService.searchNonAdminUsers(searchText, startIndex, pageSize);
        }
        log.info("User search returned " + searchResults.size());

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

    private void setValueFromModel(final ManageMeasureDetailModel model, final Measure measure) {
        measure.setDescription(model.getMeasureName());
        measure.setMeasureModel(model.getMeasureModel());
        measure.setCqlLibraryName(model.getCQLLibraryName());
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
        if (!StringUtils.isEmpty(model.getValueSetDate())) {
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
     * <p>
     * *
     *
     * @param processor     the processor
     * @param modifyWithDTO the modify with dto
     * @param modifyDTO     the modify dto
     */
    private void updateElementLookUp(final XmlProcessor processor, final QualityDataSetDTO modifyWithDTO,
                                     final QualityDataSetDTO modifyDTO) {

        log.debug(" MeasureLibraryServiceImpl: updateElementLookUp Start :  ");
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
            log.error("updateElementLookUp: " + e.getMessage(), e);
        }
        log.debug("MeasureLibraryServiceImpl: updateElementLookUp End :  ");
    }

    @Override
    public void updateCQLLookUpTagWithModifiedValueSet(final CQLQualityDataSetDTO modifyWithDTO,
                                                       final CQLQualityDataSetDTO modifyDTO, final String measureId) {
        MeasureXmlModel model = getMeasureXmlForMeasure(measureId);
        log.debug(" MeasureLibraryServiceImpl: updateCQLLookUpTagWithModifiedValueSet Start :  ");

        if (model != null) {
            SaveUpdateCQLResult result = cqlService.updateCQLLookUpTag(model.getXml(), modifyWithDTO, modifyDTO);
            if (result != null && result.isSuccess()) {
                model.setXml(result.getXml());
                measurePackageService.saveMeasureXml(model);
            }
        }
        log.debug(" MeasureLibraryServiceImpl: updateCQLLookUpTagWithModifiedValueSet End :  ");
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
     * <p>
     * *
     *
     * @param modifyWithDTO the modify with dto
     * @param modifyDTO     the modify dto
     * @param measureId     the measure id
     */
    @Override
    public final void updateMeasureXML(final QualityDataSetDTO modifyWithDTO, final QualityDataSetDTO modifyDTO,
                                       final String measureId) {
        log.debug(" MeasureLibraryServiceImpl: updateMeasureXML Start : Measure Id :: " + measureId);
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
        log.debug(" MeasureLibraryServiceImpl: updateMeasureXML End : Measure Id :: " + measureId);
    }

    /**
     * This method updates MeasureXML - ValueSet nodes under CQLLookUp.
     * <p>
     * *
     *
     * @param processor     the processor
     * @param modifyWithDTO the modify with dto
     * @param modifyDTO     the modify dto
     */
    private void updateCQLLookUp(XmlProcessor processor, QualityDataSetDTO modifyWithDTO, QualityDataSetDTO
            modifyDTO) {

        log.debug(" MeasureLibraryServiceImpl: updateElementLookUp Start :  ");
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
            log.error("Exception in updateCQLLookUp: " + e.getMessage(), e);
        }
        log.debug(" MeasureLibraryServiceImpl: updateCQLLookUp End :  ");

    }

    /**
     * Update measure xml for qdm.
     *
     * @param modifyWithDTO       the modify with dto
     * @param xmlprocessor        the xmlprocessor
     * @param expansionIdentifier the expansion identifier
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
            log.error("Exception in updateMeasureXmlForQDM: " + e.getMessage(), e);
        }
    }

    /**
     * Update measure xml for qdm.
     *
     * @param modifyWithDTO       the modify with dto
     * @param xmlprocessor        the xmlprocessor
     * @param expansionIdentifier the expansion identifier
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
            log.error("Exception in updateCQLMeasureXmlForQDM: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateMeasureXMLForExpansionIdentifier(List<QualityDataSetDTO> modifyWithDTOList, String
            measureId,
                                                       String expansionIdentifier) {
        log.debug(" MeasureLibraryServiceImpl: updateMeasureXMLForExpansionIdentifier Start : Measure Id :: "
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
                log.error("Exception in updateMeasureXMLForExpansionIdentifier: " + e.getMessage(), e);
            }

            model.setXml(processor.transform(processor.getOriginalDoc()));
            measurePackageService.saveMeasureXml(model);
        }

    }

    @Override
    public void updateCQLMeasureXMLForExpansionIdentifier(List<CQLQualityDataSetDTO> modifyWithDTOList,
                                                          String measureId, String expansionIdentifier) {
        log.debug(" MeasureLibraryServiceImpl: updateMeasureXMLForExpansionIdentifier Start : Measure Id :: "
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
                log.error("Exception in updateCQLMeasureXMLForExpansionIdentifier: " + e.getMessage(), e);
            }

            model.setXml(processor.transform(processor.getOriginalDoc()));
            measurePackageService.saveMeasureXml(model);
        }

    }

    /**
     * Update sub tree look up.
     *
     * @param processor     the processor
     * @param modifyWithDTO the modify with dto
     * @param modifyDTO     the modify dto
     */
    private void updateSubTreeLookUp(final XmlProcessor processor, final QualityDataSetDTO modifyWithDTO,
                                     final QualityDataSetDTO modifyDTO) {

        log.debug(" MeasureLibraryServiceImpl: updateSubTreeLookUp Start :  ");
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
            log.error("Exception in updateSubTreeLookUp: " + e.getMessage(), e);

        }
        log.debug(" MeasureLibraryServiceImpl: updateSubTreeLookUp End :  ");
    }

    @Override
    public final void updatePrivateColumnInMeasure(final String measureId, final boolean isPrivate) {
        measurePackageService.updatePrivateColumnInMeasure(measureId, isPrivate);
    }

    /**
     * This method updates MeasureXML - ElementRef's under SupplementalDataElement
     * Node.
     *
     * @param processor     the processor
     * @param modifyWithDTO QualityDataSetDTO
     * @param modifyDTO     QualityDataSetDTO
     */
    private void updateSupplementalDataElement(final XmlProcessor processor,
                                               final QualityDataSetDTO modifyWithDTO,
                                               final QualityDataSetDTO modifyDTO) {

        log.debug(" MeasureLibraryServiceImpl: updateSupplementalDataElement Start :  ");
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
            log.error("updateSupplementalDataElement: " + e.getMessage(), e);
        }
        log.debug(" MeasureLibraryServiceImpl: updateSupplementalDataElement End :  ");
    }

    @Override
    public final void updateUsersShare(final ManageMeasureShareModel model) {
        measurePackageService.updateUsersShare(model);
    }

    @Override
    public final ValidateMeasureResult createExports(final String key, final List<MatValueSet> matValueSetList,
                                                     boolean shouldCreateArtifacts) throws MatException {
        try {
            return measurePackageService.createExports(key, matValueSetList, shouldCreateArtifacts);
        } catch (Exception exc) {
            log.error("Exception validating export for " + key, exc);
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
            log.error("Exception in getFormattedReleaseDate: " + e.getMessage(), e);
        }
        return date;
    }

    @Override
    public String getHumanReadableForNode(final String measureId, final String populationSubXML) {
        String humanReadableHTML = "";
        try {
            humanReadableHTML = measurePackageService.getHumanReadableForNode(measureId, populationSubXML);
        } catch (Exception e) {
            log.error("Exception in getHumanReadableForNode: " + e.getMessage(), e);
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
                log.error("Cannot get read library's xml:" + e.getMessage(), e);
            }

            CQLModel libraryCQLModel = CQLUtilityClass.getCQLModelFromXML(data);
            String libraryContent = CQLUtilityClass.getCqlString(libraryCQLModel, "").getLeft();

            ComponentMeasureTabObject o = new ComponentMeasureTabObject(measureName, alias, libraryName, version, ownerName, libraryContent, componentId);
            componentMeasureInformationList.add(o);
        }

        return componentMeasureInformationList;

    }

    /**
     * Extract manage measure search model detail.
     *
     * @param measure the measure
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
        log.debug(" MeasureLibraryServiceImpl: validatePackageGrouping Start :  ");
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
                log.error("validateMeasureXmlAtCreateMeasurePackager: " + e.getMessage(), e);
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
                                    Measure measure = measureDAO.find(entry.getValue().getMeasureId());
                                    List<String> messages = patientBasedValidator.checkPatientBasedValidations(
                                            xmlModel.getXml(), entry.getValue(), cqlLibraryDAO, measure);
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
                                log.warn("validateMeasureXmlAtCreateMeasurePackager: " + e.getMessage(), e);
                                typeCheckErrorMessage.add(
                                        "Unexpected error encountered while doing Group Validations. Please contact HelpDesk.");
                            }
                        }
                    }
                }
            } catch (XPathExpressionException e) {
                log.error("validateMeasureXmlAtCreateMeasurePackager: " + e.getMessage(), e);
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

    private boolean parseCQLFile(String measureXML, String measureId) {
        boolean isInvalid = false;
        MeasureXML newXml = getMeasureXMLDAO().findForMeasure(measureId);

        log.debug("Parsing CQLModel from measure XML");
        CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureXML);

        if (cqlModel.isFhir()) {
            String cql = CQLUtilityClass.getCqlString(cqlModel, "").getLeft();
            SaveUpdateCQLResult cqlResult = getCqlService().parseFhirCQLForErrors(cqlModel, cql);
            return cqlResult.getCqlErrors().size() != 0;
        } else {
            SaveUpdateCQLResult cqlResult = CQLUtil.parseQDMCQLLibraryForErrors(cqlModel, cqlLibraryDAO, null);

            if (cqlResult.getCqlErrors() != null && cqlResult.getCqlErrors().isEmpty()) {

                log.debug("Parsing CQLModel from Export XML");
                String exportedXML = ExportSimpleXML.export(newXml, measureDAO, organizationDAO, cqlLibraryDAO, cqlModel, measureTypeDAO);

                CQLModel model = CQLUtilityClass.getCQLModelFromXML(exportedXML);

                log.debug("Parsing for QDM CQL errors");
                SaveUpdateCQLResult result = CQLUtil.parseQDMCQLLibraryForErrors(model, cqlLibraryDAO,
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
     * Find invalid logical operators.
     *
     * @param populationTopLevelLogicalOp the population top level logical op
     * @param isTopLevelLogicalOp         the is top level logical op
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
     * Check if qdm var instance is present.
     *
     * @param usedSubtreeRefId the used subtree ref id
     * @param xmlProcessor     the xml processor
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
            log.error("checkIfQDMVarInstanceIsPresent: " + e.getMessage(), e);
        }

        return usedSubtreeRefId;
    }

    /**
     * Gets the filtered sub tree ids.
     *
     * @param xmlProcessor      the xml processor
     * @param usedSubTreeIdsMap the used sub tree ids map
     * @return the filtered sub tree ids
     */
    private List<String> checkUnUsedSubTreeRef(XmlProcessor
                                                       xmlProcessor, Map<String, List<String>> usedSubTreeIdsMap) {

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
     * @param xmlProcessor the xml processor
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
            log.error("getUsedRiskAdjustmentVariables: " + e.getMessage(), e);
        }

        return checkUnUsedSubTreeRef(xmlProcessor, subTreeRefRAVList);
    }

    /**
     * Gets the stratification clasues id list.
     *
     * @param uuid         the uuid
     * @param xmlProcessor the xml processor
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
            log.error("getStratificationClasuesIDList: " + e.getMessage(), e);
        }
        return clauseList;
    }

    /**
     * Gets the used subtree ref ids.
     *
     * @param xmlProcessor          the xml processor
     * @param measureGroupingIDList the measure grouping id list
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
            log.error("getUsedSubtreeRefIds: " + e.getMessage(), e);
        }

        return usedSubTreeIdsMap;
    }

    /**
     * Check un used sub tree ref.
     *
     * @param xmlProcessor      the xml processor
     * @param usedSubTreeRefIds the used sub tree ref ids
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
            log.error("checkUnUsedSubTreeRef: " + e.getMessage(), e);
        }
        return usedSubTreeRefIds;
    }

    /**
     * Validate qdm node.
     *
     * @param qdmchildNode   the qdmchild node
     * @param attributeValue the attribute value
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
     * @param timingElementchildNode the timing elementchild node
     * @param operatorTypeList       the operator type list
     * @param flag                   the flag
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
     * @param satisfyElementchildNode the satisfy elementchild node
     * @param flag                    the flag
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
     * @param operatorTypeList  the operator type list
     * @param flag              the flag
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
     * @param setOperatorchildNode the set operatorchild node
     * @param flag                 the flag
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

        log.debug(" MeasureLibraryServiceImpl: validateGroup Start :  ");

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
                    log.error("validateForGroup: " + e.getMessage(), e);
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
     * @param nodeXPath   the node x path
     * @param originalDoc the original doc
     * @throws XPathExpressionException the x path expression exception
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
            measureType.setId(measureTypeDTO.getId());
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
        log.info("In MeasureLibraryServiceImpl.getUsedStewardAndDevelopersList() method..");
        log.info("Loading Measure for MeasueId: " + measureId);
        MeasureDetailResult usedStewardAndAuthorList = new MeasureDetailResult();

        Measure measure = measureDAO.find(measureId);

        List<Organization> allOrganization = getAllOrganizations();
        usedStewardAndAuthorList.setUsedAuthorList(getAuthorsList(measure, allOrganization));
        usedStewardAndAuthorList.setUsedSteward(getSteward(measure, allOrganization));
        usedStewardAndAuthorList.setAllAuthorList(getAllAuthorList(allOrganization));
        usedStewardAndAuthorList.setAllStewardList(getAllStewardList(allOrganization));

        return usedStewardAndAuthorList;
    }

    private MeasureDetailResult getUsedStewardAndDeveloperFromXml(String measureId) {
        log.info("In MeasureLibraryServiceImpl.getUsedStewardAndDevelopersList() method..");
        log.info("Loading Measure for MeasueId: " + measureId);
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

    private void addAuthorsToList(List<Organization> allOrganization, List<Author> usedAuthorList, Long id) {
        Author author = getAuthorFromOrganization(id, allOrganization);
        if (author != null) {
            usedAuthorList.add(author);
        }
    }

    private List<Author> getAuthorsList(MeasureXmlModel xmlModel, List<Organization> allOrganization) {
        XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
        String XPATH_EXPRESSION_DEVELOPERS = "/measure//measureDetails//developers";
        List<Author> usedAuthorList = new ArrayList<>();

        try {
            NodeList developerParentNodeList = (NodeList) xPath.evaluate(XPATH_EXPRESSION_DEVELOPERS, processor.getOriginalDoc(), XPathConstants.NODESET);
            Node developerParentNode = developerParentNodeList.item(0);

            if (developerParentNode != null) {
                NodeList developerNodeList = developerParentNode.getChildNodes();

                int childNodes = developerNodeList.getLength();

                LinkedHashSet<Long> authorList = new LinkedHashSet<>();

                for (int i = 0; i < childNodes; i++) {
                    authorList.add(Long.parseLong(developerNodeList.item(i).getAttributes().getNamedItem(ID).getNodeValue()));
                }

                if (CollectionUtils.isNotEmpty(authorList)) {
                    authorList.forEach(id -> addAuthorsToList(allOrganization, usedAuthorList, id));
                }
            }

        } catch (XPathExpressionException e) {
            log.error("getAuthorsList: " + e.getMessage(), e);
        }

        return usedAuthorList;
    }

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
            log.error("getSteward: " + e.getMessage(), e);
        }

        return measureSteward;
    }

    /**
     * Gets the authors list.
     *
     * @return the authors list
     */
    private List<Author> getAuthorsList(Measure measure, List<Organization> allOrganizations) {
        List<Author> usedAuthor = new ArrayList<>();
        if (measure.getMeasureDevelopers() != null) {
            usedAuthor = measure.getMeasureDevelopers()
                    .stream()
                    .map(association -> getAuthorFromOrganization(association.getOrganization().getId(), allOrganizations))
                    .collect(Collectors.toList());
        }
        return usedAuthor;
    }

    private Author getAuthorFromOrganization(Long id, List<Organization> allOrganization) {
        Optional<Organization> organization = allOrganization.stream().filter(o -> o.getId().equals(id)).findFirst();
        if (organization.isPresent()) {
            Organization org = organization.get();
            return new Author(String.valueOf(org.getId()), org.getOrganizationName(), org.getOrganizationOID());
        }

        return null;
    }

    /**
     * Gets the steward id.
     *
     * @param measure the xml model
     * @return the steward id
     */
    private MeasureSteward getSteward(Measure measure, List<Organization> allOrganization) {
        if (measure.getMeasureStewardId() != null) {
            MeasureSteward measureSteward = new MeasureSteward();
            Organization organization = allOrganization.stream().filter(org -> measure.getMeasureStewardId().equalsIgnoreCase(Long.toString(org.getId()))).findAny().orElse(new Organization());
            measureSteward.setId(String.valueOf(organization.getId()));
            measureSteward.setOrgName(organization.getOrganizationName());
            measureSteward.setOrgOid(organization.getOrganizationOID());

            return measureSteward;
        }
        return null;
    }

    @Override
    public final QualityDataModelWrapper getDefaultSDEFromMeasureXml(final String measureId) {
        log.info("Inside MeasureLibraryServiceImp :: getDefaultSDEFromMeasureXml :: Start");
        MeasureXmlModel measureXmlModel = getMeasureXmlForMeasure(measureId);
        QualityDataModelWrapper details = convertXmltoQualityDataDTOModel(measureXmlModel);
        ArrayList<QualityDataSetDTO> finalList = new ArrayList<>();
        if (details != null) {
            if (details.getQualityDataDTO() != null && !details.getQualityDataDTO().isEmpty()) {
                log.info(" details.getQualityDataDTO().size() :" + details.getQualityDataDTO().size());
                for (QualityDataSetDTO dataSetDTO : details.getQualityDataDTO()) {
                    if (dataSetDTO.getCodeListName() != null && dataSetDTO.isSuppDataElement()) {
                        finalList.add(dataSetDTO);
                    }
                }
            }
            details.setQualityDataDTO(finalList);
        }
        log.info("finalList()of QualityDataSetDTO ::" + finalList.size());
        log.info("Inside MeasureLibraryServiceImp :: getDefaultSDEFromMeasureXml :: END");
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
     * @param map - User and List of Measures for User.
     * @return List<MeasureOwnerReportDTO>
     * @throws XPathExpressionException - {@link XPathExpressionException}
     */
    private List<MeasureOwnerReportDTO> populateMeasureOwnerReport(Map<User, List<Measure>> map)
            throws XPathExpressionException {
        List<MeasureOwnerReportDTO> measureOwnerReportDTOs = new ArrayList<>();
        for (Entry<User, List<Measure>> entry : map.entrySet()) {
            User user = entry.getKey();
            List<Measure> measureList = entry.getValue();
            for (Measure measure : measureList) {
                log.info("Start to evaluate measure id :::: " + measure.getId());
                MeasureXML measureXML = getMeasureXMLDAO().findForMeasure(measure.getId());
                if (measureXML != null) {
                    String measureXmlString = measureXML.getMeasureXMLAsString();
                    if (measureXmlString != null) {
                        String firstName = user.getFirstName();
                        String lastName = user.getLastName();
                        String organizationName = user.getOrganizationName();
                        String measureDescription = measure.getDescription();
                        int cmsNumber = measure.geteMeasureId();
                        String nqfId = measure.getNqfNumber();
                        String guid = measure.getMeasureSet().getId();

                        MeasureOwnerReportDTO ownerReportDTO = new MeasureOwnerReportDTO(firstName, lastName,
                                organizationName, measureDescription, cmsNumber, nqfId, guid);
                        measureOwnerReportDTOs.add(ownerReportDTO);
                    }
                } else {
                    log.info("Measure xml not found for measure id :::: " + measure.getId());
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
     * @param measureXmlModel the measure xml model
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
                log.error("validateStratumForAtleastOneClause: " + e.getMessage(), e);
            }
        }
        return message;
    }

    @Override
    public String getCurrentReleaseVersion() {
        return currentReleaseVersion;
    }

    @Override
    public void setCurrentReleaseVersion(String releaseVersion) {
        currentReleaseVersion = releaseVersion;
    }

    public CQLService getCqlService() {
        return cqlService;
    }

    public void setCqlService(CQLService cqlService) {
        this.cqlService = cqlService;
    }


    @Override
    public SaveUpdateCQLResult getMeasureCQLFileData(String measureId) {
        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureId);
        Measure measure = measureDAO.find(measureId);

        if (model != null && StringUtils.isNotBlank(model.getXml())) {
            String xmlString = model.getXml();
            result = cqlService.getCQLFileData(measureId, true, xmlString);
            lintAndAddToResult(measureId, result);
            result.setSuccess(true);
            if (measure.isDraft() && libraryNameExists(result.getCqlModel().getLibraryName(), measure.getMeasureSet().getId())) {
                result.setFailureReason(SaveUpdateCQLResult.DUPLICATE_LIBRARY_NAME);
            }

            result.setMeasureStewardId(measure.getMeasureStewardId());
            if (measure.getMeasureDetails() != null ) {
                result.setMeasureDescription(measure.getMeasureDetails().getDescription());
            }
            result.setPopulationBasis(measure.getPopulationBasis());
            if (CollectionUtils.isNotEmpty(measure.getMeasureTypes())) {
                result.setMeasureTypes(new ArrayList<>());
                var resultMts = result.getMeasureTypes();
                measure.getMeasureTypes().forEach(mt -> resultMts.add(mt.getMeasureTypes().getAbbrName()));
            }
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
            result = cqlService.loadMeasureCql(measureDAO.find(measureId),
                    xmlString);
            lintAndAddToResult(measureId, result);

            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }

        return result;
    }

    private void lintAndAddToResult(String measureId, SaveUpdateCQLResult result) {
        Measure measure = measureDAO.find(measureId);
        if (measure.isDraft()) {
            String model = measure.isFhirMeasure() ? ModelTypeHelper.FHIR : ModelTypeHelper.QDM;

            CQLLinterConfig config = new CQLLinterConfig(result.getCqlModel().getLibraryName(),
                    MeasureUtility.formatVersionText(measure.getRevisionNumber(), measure.getVersion()),
                    model, measure.getModelVersion());
            config.setPreviousCQLModel(result.getCqlModel());

            CQLLinter linter = CQLUtil.lint(result.getCqlString(), config);
            if (result.getCqlModel().isFhir()) {
                SaveUpdateCQLResult cqlResult = getCqlService().parseFhirCQLForErrors(result.getCqlModel(), result.getCqlString());
                result.setCqlErrors(cqlResult.getCqlErrors());
                result.setCqlWarnings(cqlResult.getCqlWarnings());
            }

            result.getLinterErrors().addAll(linter.getErrors());
            result.getLinterErrorMessages().addAll(linter.getErrorMessages());
            result.setDoesMeasureHaveIncludedLibraries(checkIfIncludedLibrariesAreCoposite(result.getCqlModel().getIncludedLibrarys(), measure.getIsCompositeMeasure()));
            result.setMeasureComposite(measure.getIsCompositeMeasure());
        } else {
            result.resetErrors();
        }
    }

    private boolean checkIfIncludedLibrariesAreCoposite
            (Map<CQLIncludeLibrary, CQLModel> includedLibrarys, Boolean compositeMeasure) {
        if (!compositeMeasure) {
            return includedLibrarys.size() > 0;
        }
        return includedLibrarys.keySet().stream().anyMatch(library -> !"true".equals(library.getIsComponent()));
    }

    @Override
    public SaveUpdateCQLResult saveCQLFile(String measureId, String cql) {
        SaveUpdateCQLResult result = null;

        if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
            MeasureXmlModel measureXMLModel = measurePackageService.getMeasureXmlForMeasure(measureId);
            Measure measure = measureDAO.find(measureId);

            CQLModel previousModel = CQLUtilityClass.getCQLModelFromXML(measureXMLModel.getXml());

            CQLLinterConfig config = new CQLLinterConfig(previousModel.getLibraryName(),
                    MeasureUtility.formatVersionText(measure.getRevisionNumber(), measure.getVersion()),
                    ModelTypeHelper.defaultTypeIfBlank(measure.getMeasureModel()), measure.isFhirMeasure() ? measure.getFhirVersion() : measure.getQdmVersion());

            config.setPreviousCQLModel(previousModel);

            result = getCqlService().saveCQLFile(measureXMLModel.getXml(), cql, config, measure.getMeasureModel());

            if (!result.isSevereError()) {
                XmlProcessor processor = new XmlProcessor(measureXMLModel.getXml());
                processor.replaceNode(result.getXml(), CQL_LOOKUP, MEASURE);
                measureXMLModel.setXml(processor.transform(processor.getOriginalDoc()));

                if (result.isSuccess()) {
                    // need to clean definitions from populations and groupings.
                    // go through all of the definitions in the previous model and check if they are in the new model
                    // if the old definition is not in the new model, clean the groupings
                    for (CQLDefinition previousDefinition : previousModel.getDefinitionList()) {
                        Optional<CQLDefinition> previousDefinitionInNewModel = result.getCqlModel().getDefinitionList().stream().filter(d -> d.getId().equals((previousDefinition.getId()))).findFirst();
                        if (!previousDefinitionInNewModel.isPresent()) {
                            cleanPopulationsAndGroups(previousDefinition, measureXMLModel);
                        }
                    }

                    // do the same thing for functions
                    for (CQLFunctions previousFunction : previousModel.getCqlFunctions()) {
                        Optional<CQLFunctions> previousFunctionInNewModel = result.getCqlModel().getCqlFunctions().stream().filter(f -> f.getId().equals((previousFunction.getId()))).findFirst();
                        if (!previousFunctionInNewModel.isPresent()) {
                            cleanMeasureObservationAndGroups(previousFunction, measureXMLModel);
                        }
                    }
                }
                measurePackageService.saveMeasureXml(measureXMLModel);
            }

            if (result.isSuccess()) {
                measure.setCqlLibraryHistory(cqlService.createCQLLibraryHistory(measure.getCqlLibraryHistory(), result.getCqlString(), null, measure));

                if (result.getCqlModel().isFhir()) {
                    result = handleSaveFhirSevereErrors(result, measure, cql);
                }
                measureDAO.save(measure);
            }
        }

        return result;
    }

    private SaveUpdateCQLResult handleSaveFhirSevereErrors(SaveUpdateCQLResult result, Measure measure, String cql) {
        if (result.getCqlModel().isFhir()) {
            MeasureXML xml = measureXMLDAO.findForMeasure(measure.getId());
            if (xml == null) {
                throw new MatRuntimeException("Can't find measureXml for " + measure.getId());
            }
            if (result.isSevereError()) {
                String userId = LoggedInUserUtil.getLoggedInUser();
                if (userId == null) {
                    throw new MatRuntimeException("Can't find a logged in userId.");
                }
                User user = userDAO.find(userId);
                if (user == null) {
                    throw new MatRuntimeException("User not found: " + userId);
                }
                if (user.getUserPreference().isFreeTextEditorEnabled()) {
                    xml.setSevereErrorCql(result.getCqlString());
                } else {
                    result.setSevereError(true);
                    result.setSuccess(false);
                }
            } else {
                xml.setSevereErrorCql(null);
            }
            measureXMLDAO.save(xml);
        }
        return result;
    }

    @Override
    public SaveUpdateCQLResult saveAndModifyDefinitions(String measureId, CQLDefinition toBeModifiedObj,
                                                        CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable) {

        SaveUpdateCQLResult result = null;
        if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
            Measure measure = measureDAO.find(measureId);
            MeasureXmlModel measureXMLModel = measurePackageService.getMeasureXmlForMeasure(measureId);
            if (measureXMLModel != null) {
                result = getCqlService().saveAndModifyDefinitions(measureXMLModel.getXml(), toBeModifiedObj, currentObj, definitionList, isFormatable, measure.getMeasureModel());
                XmlProcessor processor = new XmlProcessor(measureXMLModel.getXml());
                if (result.isSuccess()) {
                    updateRiskAdjustmentVariables(processor, toBeModifiedObj, currentObj);
                    processor.replaceNode(result.getXml(), "cqlLookUp", "measure");
                    measureXMLModel.setXml(processor.transform(processor.getOriginalDoc()));
                    measurePackageService.saveMeasureXml(measureXMLModel);
                }
            }
        }
        return result;
    }

    private void updateRiskAdjustmentVariables(XmlProcessor processor, CQLDefinition toBeModifiedObj, CQLDefinition
            currentObj) {
        if (toBeModifiedObj != null) {
            String XPATH_EXPRESSION_SDE_ELEMENTREF = "/measure//cqldefinition[@uuid='" + toBeModifiedObj.getId() + "']";
            try {
                NodeList nodesSDE = processor.findNodeList(processor.getOriginalDoc(), XPATH_EXPRESSION_SDE_ELEMENTREF);
                for (int i = 0; i < nodesSDE.getLength(); i++) {
                    Node newNode = nodesSDE.item(i);
                    newNode.getAttributes().getNamedItem("displayName").setNodeValue(currentObj.getName());
                }

            } catch (XPathExpressionException e) {
                log.error("Error in  updateRiskAdjustmentVariables: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public SaveUpdateCQLResult saveAndModifyParameters(String measureId, CQLParameter toBeModifiedObj,
                                                       CQLParameter currentObj, List<CQLParameter> parameterList, boolean isFormatable) {

        SaveUpdateCQLResult result = null;
        if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
            Measure measure = measureDAO.find(measureId);
            MeasureXmlModel measureXMLModel = measurePackageService.getMeasureXmlForMeasure(measureId);
            if (measureXMLModel != null) {
                result = getCqlService().saveAndModifyParameters(measureXMLModel.getXml(), toBeModifiedObj, currentObj,
                        parameterList, isFormatable, measure.getMeasureModel());
                if (result.isSuccess()) {
                    XmlProcessor processor = new XmlProcessor(measureXMLModel.getXml());
                    processor.replaceNode(result.getXml(), "cqlLookUp", "measure");
                    measureXMLModel.setXml(processor.transform(processor.getOriginalDoc()));
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
            Measure measure = measureDAO.find(measureId);
            MeasureXmlModel measureXMLModel = measurePackageService.getMeasureXmlForMeasure(measureId);
            if (measureXMLModel != null) {
                result = getCqlService().saveAndModifyFunctions(measureXMLModel.getXml(), toBeModifiedObj, currentObj,
                        functionsList, isFormatable, measure.getMeasureModel());

                XmlProcessor processor = new XmlProcessor(measureXMLModel.getXml());
                if (result.isSuccess()) {
                    updateFunctionDisplayName(processor, toBeModifiedObj, currentObj);
                    processor.replaceNode(result.getXml(), "cqlLookUp", "measure");
                    measureXMLModel.setXml(processor.transform(processor.getOriginalDoc()));
                    measurePackageService.saveMeasureXml(measureXMLModel);
                }
            }
        }
        return result;

    }

    private void updateFunctionDisplayName(XmlProcessor processor, CQLFunctions toBeModifiedObj, CQLFunctions
            currentObj) {
        // XPath to find All cqlfunction in populations to be
        // modified functions.
        if (toBeModifiedObj != null) {
            String XPATH_EXPRESSION_FUNCTION = "/measure//cqlfunction[@uuid='" + toBeModifiedObj.getId() + "']";
            try {
                NodeList nodesSDE = processor.findNodeList(processor.getOriginalDoc(), XPATH_EXPRESSION_FUNCTION);
                for (int i = 0; i < nodesSDE.getLength(); i++) {
                    Node newNode = nodesSDE.item(i);
                    newNode.getAttributes().getNamedItem("displayName").setNodeValue(currentObj.getName());
                }

            } catch (XPathExpressionException e) {
                log.error("Error in  updateFunctionDisplayName: " + e.getMessage(), e);
            }
        }

        log.debug(" CQLServiceImpl: updateFunctionDisplayName End :  ");
    }

    @Override
    public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String currentMeasureId, String libraryName, String
            comments) {
        SaveUpdateCQLResult result = new SaveUpdateCQLResult();
        if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, currentMeasureId)) {
            Measure measure = measureDAO.find(currentMeasureId);
            if (libraryNameExists(libraryName, measure.getMeasureSet().getId())) {
                result.setFailureReason(SaveUpdateCQLResult.DUPLICATE_LIBRARY_NAME);
                result.setSuccess(false);
            } else {
                MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(currentMeasureId);
                if (xmlModel != null) {
                    result = getCqlService().saveAndModifyCQLGeneralInfo(xmlModel.getXml(), libraryName, comments);
                    if (result.isSuccess()) {
                        measure.setCqlLibraryName(libraryName);
                        measureDAO.save(measure);
                        xmlModel.setXml(result.getXml());
                        measurePackageService.saveMeasureXml(xmlModel);
                    }
                }
            }

        }

        return result;
    }

    @Override
    public SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj) {

        SaveUpdateCQLResult result = null;
        if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
            MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
            if (xmlModel != null) {
                result = getCqlService().deleteDefinition(xmlModel.getXml(), toBeDeletedObj);
                if (result.isSuccess()) {
                    XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
                    processor.replaceNode(result.getXml(), "cqlLookUp", "measure");
                    xmlModel.setXml(processor.transform(processor.getOriginalDoc()));
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
            log.error("Error in  cleanPopulationsAndGroups: " + e.getMessage(), e);
        }
    }

    @Override
    public SaveUpdateCQLResult deleteFunction(String measureId, CQLFunctions toBeDeletedObj) {

        SaveUpdateCQLResult result = null;
        if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
            MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);

            if (xmlModel != null) {
                result = getCqlService().deleteFunction(xmlModel.getXml(), toBeDeletedObj);
                if (result.isSuccess()) {
                    XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
                    processor.replaceNode(result.getXml(), "cqlLookUp", "measure");
                    xmlModel.setXml(processor.transform(processor.getOriginalDoc()));
                    cleanMeasureObservationAndGroups(toBeDeletedObj, xmlModel);
                    measurePackageService.saveMeasureXml(xmlModel);
                }
            }
        }

        return result;
    }

    private void cleanMeasureObservationAndGroups(CQLFunctions toBeDeletedObj, MeasureXmlModel xmlModel) {

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
            log.error("cleanMsrObservationsAndGroups: " + e.getMessage(), e);
        }
    }

    @Override
    public SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj) {

        SaveUpdateCQLResult result = null;
        if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
            MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);

            if (xmlModel != null) {
                result = getCqlService().deleteParameter(xmlModel.getXml(), toBeDeletedObj);
                if (result.isSuccess()) {
                    XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
                    processor.replaceNode(result.getXml(), "cqlLookUp", "measure");
                    xmlModel.setXml(processor.transform(processor.getOriginalDoc()));
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
        Measure measure = measureDAO.find(measureId);

        if (ModelTypeHelper.FHIR.equalsIgnoreCase(measure.getMeasureModel())) {
            CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureXML.getXml());
            String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, "").getLeft();
            SaveUpdateCQLResult cqlResult = getCqlService().parseFhirCQLForErrors(cqlModel,
                    cqlFileString,
                    ValidationRequest.builder().validateReturnType(true).build());
            return getCqlService().generateUsedCqlArtifactsResult(cqlModel, measureXML.getXml(), cqlResult);
        } else {
            return getCqlService().getUsedCQlArtifacts(measureXML.getXml());
        }
    }

    @Override
    public SaveUpdateCQLResult deleteValueSet(String toBeDelValueSetId, String measureID) {

        SaveUpdateCQLResult cqlResult = null;
        if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureID)) {
            MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureID);
            if (model != null && model.getXml() != null) {
                cqlResult = getCqlService().deleteValueSet(model.getXml(), toBeDelValueSetId);
                if (cqlResult != null && cqlResult.isSuccess()) {
                    XmlProcessor processor = new XmlProcessor(model.getXml());
                    processor.replaceNode(cqlResult.getXml(), "cqlLookUp", "measure");
                    model.setXml(processor.transform(processor.getOriginalDoc()));
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
                cqlResult = getCqlService().deleteCode(measureID, true, model.getXml(), toBeDeletedId);
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
    public CQLQualityDataModelWrapper getCQLValusets(String measureID) {
        CQLQualityDataModelWrapper cqlQualityDataModelWrapper = new CQLQualityDataModelWrapper();
        return getCqlService().getCQLValusets(measureID, cqlQualityDataModelWrapper);
    }

    @Override
    public CQLQualityDataModelWrapper saveValueSetList
            (List<CQLValueSetTransferObject> transferObjectList, List<CQLQualityDataSetDTO> appliedValueSetList, String
                    measureId) {
        CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
        MeasureXmlModel measureXMLModel = measurePackageService.getMeasureXmlForMeasure(measureId);

        if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {
            for (CQLValueSetTransferObject transferObject : transferObjectList) {
                SaveUpdateCQLResult result = null;
                transferObject.setAppliedQDMList(appliedValueSetList);
                result = getCqlService().saveCQLValueset(measureXMLModel.getXml(), transferObject);

                if (result.isSuccess()) {
                    XmlProcessor processor = new XmlProcessor(measureXMLModel.getXml());
                    processor.replaceNode(result.getXml(), "cqlLookUp", "measure");
                    measureXMLModel.setXml(processor.transform(processor.getOriginalDoc()));
                    measurePackageService.saveMeasureXml(measureXMLModel);
                }
            }

            wrapper = getCQLValusets(measureId);
        }

        return wrapper;
    }

    @Override
    public SaveUpdateCQLResult saveCQLValuesettoMeasure(CQLValueSetTransferObject valueSetTransferObject) {
        SaveUpdateCQLResult result = new SaveUpdateCQLResult();

        MeasureXmlModel measureXMLModel = measurePackageService.getMeasureXmlForMeasure(valueSetTransferObject.getMeasureId());

        if (measureXMLModel != null) {
            result = getCqlService().saveCQLValueset(measureXMLModel.getXml(), valueSetTransferObject);
            if (result.isSuccess()) {
                XmlProcessor processor = new XmlProcessor(measureXMLModel.getXml());
                processor.replaceNode(result.getXml(), "cqlLookUp", "measure");
                measureXMLModel.setXml(processor.transform(processor.getOriginalDoc()));
                measurePackageService.saveMeasureXml(measureXMLModel);
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
                if (result.isSuccess()) {
                    XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
                    processor.replaceNode(result.getXml(), "cqlLookUp", "measure");
                    xmlModel.setXml(processor.transform(processor.getOriginalDoc()));

                    CQLCodeSystem codeSystem = new CQLCodeSystem();
                    codeSystem.setCodeSystem(transferObject.getCqlCode().getCodeSystemOID());
                    codeSystem.setCodeSystemName(transferObject.getCqlCode().getCodeSystemName());
                    codeSystem.setCodeSystemVersion(transferObject.getCqlCode().getCodeSystemVersion());

                    SaveUpdateCQLResult saveCodesystemResult = getCqlService().saveCQLCodeSystem(xmlModel.getXml(), codeSystem);
                    if (saveCodesystemResult.isSuccess()) {
                        processor.replaceNode(saveCodesystemResult.getXml(), "cqlLookUp", "measure");
                        xmlModel.setXml(processor.transform(processor.getOriginalDoc()));
                        result = saveCodesystemResult;
                    }

                    measurePackageService.saveMeasureXml(xmlModel);
                    result.setCqlCodeList(result.getCqlModel().getCodeList());
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
                        XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
                        processor.replaceNode(codeResult.getXml(), "cqlLookUp", "measure");
                        xmlModel.setXml(processor.transform(processor.getOriginalDoc()));

                        CQLCodeSystem codeSystem = new CQLCodeSystem();
                        codeSystem.setCodeSystem(cqlCode.getCodeSystemOID());
                        codeSystem.setCodeSystemName(cqlCode.getCodeSystemName());
                        codeSystem.setCodeSystemVersion(cqlCode.getCodeSystemVersion());

                        SaveUpdateCQLResult updatedResult = getCqlService().saveCQLCodeSystem(xmlModel.getXml(), codeSystem);
                        if (updatedResult.isSuccess()) {
                            processor.replaceNode(updatedResult.getXml(), "cqlLookUp", "measure");
                            xmlModel.setXml(processor.transform(processor.getOriginalDoc()));
                        }
                        result.setXml(xmlModel.getXml());
                    } else {
                        result.setSuccess(false);
                        break;
                    }
                }
                if (result.isSuccess()) {
                    measurePackageService.saveMeasureXml(xmlModel);
                    CQLCodeWrapper wrapper = getCqlService().getCQLCodes(measureId, true, xmlModel.getXml());
                    if (wrapper != null && !wrapper.getCqlCodeList().isEmpty()) {
                        result.setCqlCodeList(wrapper.getCqlCodeList());
                        CQLModel cqlModel = cqlService.getCQLData(measureId, true, result.getXml()).getCqlModel();
                        result.setCqlModel(cqlModel);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public CQLCodeWrapper getCQLCodes(String measureID) {
        CQLCodeWrapper cqlCodeWrapper = new CQLCodeWrapper();
        MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureID);
        if (model != null) {
            String xmlString = model.getXml();
            cqlCodeWrapper = getCqlService().getCQLCodes(measureID, true, xmlString);
        }

        return cqlCodeWrapper;
    }

    @Override
    public SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String measureId, CQLIncludeLibrary toBeModifiedObj,
                                                            CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) throws InvalidLibraryException {

        SaveUpdateCQLResult result = null;
        Measure measure = measureDAO.find(measureId);
        if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, measureId)) {

            MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(measureId);
            if (xmlModel != null) {
                int numberOfAssociations = cqlService.countNumberOfAssociation(measureId);
                if (numberOfAssociations < 10) {
                    result = getCqlService().saveAndModifyIncludeLibrayInCQLLookUp(xmlModel.getXml(),
                            toBeModifiedObj, currentObj, incLibraryList, measure.getMeasureModel());

                    if (result.isSuccess()) {
                        XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
                        if (result.isSuccess()) {
                            processor.replaceNode(result.getXml(), "cqlLookUp", "measure");
                            xmlModel.setXml(processor.transform(processor.getOriginalDoc()));
                            measurePackageService.saveMeasureXml(xmlModel);
                        }

                        getCqlService().saveCQLAssociation(currentObj, measureId);
                        if (toBeModifiedObj != null) {
                            getCqlService().deleteCQLAssociation(toBeModifiedObj, measureId);
                        }
                    }
                } else {
                    log.info("Already 10 associations exists for this measure.");
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
            result = cqlService.getCQLData(measureId, true, xmlString);
            result.setSetId(measure.getMeasureSet().getId());
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }

        return result;
    }

    @Override
    public SaveUpdateCQLResult getMeasureCQLDataForLoad(String measureId) {
        try {
            SaveUpdateCQLResult result = new SaveUpdateCQLResult();
            MeasureXmlModel model = measurePackageService.getMeasureXmlForMeasure(measureId);
            Measure measure = measureDAO.find(measureId);

            if (model != null && StringUtils.isNotBlank(model.getXml())) {
                String xmlString = model.getXml();
                result = cqlService.getCQLDataForLoad(xmlString);
                result.setSetId(measure.getMeasureSet().getId());
                result.setSuccess(true);
                if (measure.isDraft() && libraryNameExists(result.getCqlModel().getLibraryName(), measure.getMeasureSet().getId())) {
                    result.setFailureReason(SaveUpdateCQLResult.DUPLICATE_LIBRARY_NAME);
                }
            } else {
                result.setSuccess(false);
            }
            return result;
        } catch (RuntimeException re) {
            log.error("Error in getMeasureCQLDataForLoad:" + re.getMessage(), re);
            throw re;
        }
    }

    @Override
    public SaveUpdateCQLResult deleteInclude(String currentMeasureId, CQLIncludeLibrary toBeModifiedIncludeObj) {

        SaveUpdateCQLResult result = null;
        if (MatContextServiceUtil.get().isCurrentMeasureEditable(measureDAO, currentMeasureId)) {

            MeasureXmlModel xmlModel = measurePackageService.getMeasureXmlForMeasure(currentMeasureId);
            if (xmlModel != null) {
                result = getCqlService().deleteInclude(xmlModel.getXml(), toBeModifiedIncludeObj);
                if (result.isSuccess()) {
                    XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
                    processor.replaceNode(result.getXml(), "cqlLookUp", "measure");
                    xmlModel.setXml(processor.transform(processor.getOriginalDoc()));
                    measurePackageService.saveMeasureXml(xmlModel);
                    getCqlService().deleteCQLAssociation(toBeModifiedIncludeObj, currentMeasureId);
                    result.setUsedCQLArtifacts(cqlService.getUsedCQlArtifacts(xmlModel.getXml()));
                }
            }
        }
        return result;

    }

    @Override
    public VsacApiResult updateCQLVSACValueSets(String measureId, String expansionId, String sessionId) {
        CQLQualityDataModelWrapper details = getCQLAppliedQDMFromMeasureXml(measureId, false);
        List<CQLQualityDataSetDTO> appliedQDMList = details.getQualityDataDTO();
        appliedQDMList = appliedQDMList.stream().filter(v -> !StringUtils.isEmpty(v.getOriginalCodeListName())).collect(Collectors.toList());
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
     * @param map       - HaspMap
     * @param measureId - String
     */
    private void updateAllCQLInMeasureXml(HashMap<CQLQualityDataSetDTO, CQLQualityDataSetDTO> map, String
            measureId) {
        log.info("Start VSACAPIServiceImpl updateAllInMeasureXml :");
        Iterator<Entry<CQLQualityDataSetDTO, CQLQualityDataSetDTO>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<CQLQualityDataSetDTO, CQLQualityDataSetDTO> entrySet = it.next();
            log.info("Calling updateMeasureXML for : " + entrySet.getKey().getOid());
            updateCQLLookUpTagWithModifiedValueSet(entrySet.getKey(), entrySet.getValue(), measureId);
            log.info("Successfully updated Measure XML for  : " + entrySet.getKey().getOid());
        }
        log.info("End VSACAPIServiceImpl updateAllInMeasureXml :");
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
                log.error("MeasureLibraryService::validateAndPackage - Invalid Grouping: " + validateGroupResult.getValidationMessages());
                return result;
            }

            ValidateMeasureResult validatePackageGroupingResult = validatePackageGrouping(model);
            if (!validatePackageGroupingResult.isValid()) {
                result.setSuccess(false);
                result.setValidateResult(validateGroupResult);
                result.setFailureReason(SaveMeasureResult.INVALID_PACKAGE_GROUPING);
                log.error("MeasureLibraryService::validateAndPackage - Invalid Package Grouping: " + validatePackageGroupingResult.getValidationMessages());
                return result;
            }

            ValidateMeasureResult validateExports = validateExports(measureId);
            if (!validateExports.isValid()) {
                result.setSuccess(false);
                result.setValidateResult(validateExports);
                result.setFailureReason(SaveMeasureResult.INVALID_EXPORTS);
                log.error("MeasureLibraryService::validateAndPackage - Invalid Exports: " + validateExports.getValidationMessages());
                return result;
            }

            result = saveMeasureAtPackage(model);
            if (!result.isSuccess()) {
                log.error("MeasureLibraryService::validateAndPackage - Save Measure At Package: " + result.getFailureReason());
                return result;
            }

            ValidateMeasureResult measureExportValidation = createExports(measureId, null, shouldCreateArtifacts);
            if (!measureExportValidation.isValid()) {
                result.setSuccess(false);
                result.setValidateResult(measureExportValidation);
                result.setFailureReason(SaveMeasureResult.INVALID_CREATE_EXPORT);
                log.warn("MeasureLibraryService::validateAndPackage - Invalid Create Exports: " + measureExportValidation.getValidationMessages());
                return result;
            }

            auditService.recordMeasureEvent(measureId, "Measure Package Created", "", false);
            return result;
        } catch (Exception e) {
            log.error("MeasureLibraryService::validateAndPackage -> Error: " + e.getMessage(), e);
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

        List<MeasureShareDTO> measureTotalList = measurePackageService.searchComponentMeasuresWithFilter(measureSearchModel);
        searchModel.setResultsTotal(measureTotalList.size());

        List<MeasureShareDTO> measureList = getSublist(measureSearchModel, measureTotalList);
        searchModel.setStartIndex(measureSearchModel.getStartIndex());


        List<ManageMeasureSearchModel.Result> detailModelList = extractModelDetailList(currentUserId, isSuperUser, measureList);
        searchModel.setData(detailModelList);

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
        if (CollectionUtils.isEmpty(list)) {
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
    public SaveMeasureResult saveCompositeMeasure(ManageCompositeMeasureDetailModel model) throws MatException {

        if ((CollectionUtils.isNotEmpty(model.getMeasureTypeSelectedList()) && isDuplicateMeasureType(model.getMeasureTypeSelectedList())) || (CollectionUtils.isNotEmpty(model.getAuthorSelectedList()) && isDuplicateMeasureDeveloper(model.getAuthorSelectedList()))) {
            throw new MatException();
        }

        boolean isExisting = false;
        // Scrubbing out Mark Up.
        if (model != null) {
            model.scrubForMarkUp();
        }
        Measure pkg = null;


        if (model.getId() != null) {
            pkg = measurePackageService.getById(model.getId());
            setCQLLibraryName(pkg, model);
            isExisting = true;
        }


        SaveMeasureResult result = new SaveMeasureResult();
        if (!isExisting && libraryNameExists(model.getCQLLibraryName(), model.getMeasureSetId())) {
            result.setFailureReason(SaveUpdateCQLResult.DUPLICATE_LIBRARY_NAME);
            result.setSuccess(false);
            return result;
        }

        ManageCompositeMeasureModelValidator manageCompositeMeasureModelValidator = new ManageCompositeMeasureModelValidator();
        List<String> message = manageCompositeMeasureModelValidator.validateMeasure(model);
        if (message.isEmpty()) {
            String shortName = buildMeasureShortName(model);
            model.setShortName(shortName);

            MeasureSet measureSet = null;
            String existingMeasureScoringType = "";
            String existingMeasurePopulationBasis = "";
            final boolean existingMeasure;

            if (model.getId() != null) {

                // editing an existing measure
                existingMeasure = true;

                existingMeasureScoringType = pkg.getMeasureScoring();
                existingMeasurePopulationBasis = pkg.getPopulationBasis();
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
                existingMeasure = false;
                pkg = new Measure();
                pkg.setReleaseVersion(propertiesService.getCurrentReleaseVersion());
                pkg.setQdmVersion(propertiesService.getQdmVersion());
                pkg.setIsCompositeMeasure(Boolean.TRUE);
                pkg.setCqlLibraryName(model.getCQLLibraryName());
                List<MeasureTypeAssociation> measureTypes = new ArrayList<>(1);
                MeasureType composite = findMeasureTypeById("1");
                measureTypes.add(new MeasureTypeAssociation(pkg, composite));
                pkg.setMeasureTypes(measureTypes);

                model.setRevisionNumber("000");
                measureSet = new MeasureSet();
                measureSet.setId(UUID.randomUUID().toString());
                measurePackageService.save(measureSet);
            }
            pkg.setCompositeScoring(model.getCompositeScoringMethod());
            pkg.setMeasureSet(measureSet);

            MeasureDetails measureDetails = generateMeasureDetailsFromDatabaseData(model, pkg);
            pkg.setMeasureDetails(measureDetails);
            if (isExisting) {
                pkg.setMeasureTypes(getSelectedMeasureTypes(pkg, model));
            }
            pkg.setMeasureDevelopers(getSelectedDeveloperList(pkg, model));
            pkg.setMeasureStewardId(model.getStewardId());

            pkg.seteMeasureId(model.geteMeasureId());
            pkg.setNqfNumber(model.getNqfId());

            calculateCalendarYearForMeasure(model, pkg);

            setValueFromModel(model, pkg);
            try {
                getAndValidateValueSetDate(model.getValueSetDate());
                pkg.setValueSetDate(DateUtility.addTimeToDate(pkg.getValueSetDate()));
                measurePackageService.save(pkg);
                if (existingMeasure) {
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

            saveMeasureXml(createMeasureXmlModel(pkg, MEASURE),
                    pkg.getId(),
                    StringUtils.equals("FHIR", model.getMeasureModel()));

            updateMeasureXml(model, pkg, existingMeasureScoringType, existingMeasurePopulationBasis);

            result.setSuccess(true);
            result.setId(pkg.getId());
            model.setMeasureTypeSelectedList(getMeasureTypeForComposite(model.getMeasureTypeSelectedList()));
            createComponentMeasureAsLibraryInMeasureXML(pkg.getId(), model);

            if (isExisting) {
                result.setCompositeMeasureDetailModel(getCompositeMeasure(model.getId()));
            }

            return result;
        } else {
            log.info("Validation Failed for measure :: Invalid Data Issues.");
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
            log.error("Exception in createIncludedMeasureAsLibrary: " + e.getMessage(), e);
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
            log.error("Exception in removeIncludedComponentMeasuresInMeasureXML: " + e.getMessage(), e);
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
        if (CollectionUtils.isNotEmpty(componentMeasures)) {
            result.setSuccess(false);
            Measure measure = measureDAO.find(currentMeasureId);
            StringBuilder errorMessageBuilder = new StringBuilder();
            errorMessageBuilder.append(measure.getDescription() + " can not be deleted as it has been used as a component measure in ");
            for (int i = 0; i < componentMeasures.size(); i++) {
                ComponentMeasure componentMeasure = componentMeasures.get(i);
                if (i > 0) {
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
            return eMeasureId;
        }
        return -1;
    }

    @Override
    public String getHumanReadableForMeasureDetails(String measureId, String measureModel) {
        String humanReadableHTML = "";
        try {
            ManageMeasureDetailModel measureDetailsModel = getMeasure(measureId);
            HumanReadableModel model = new HumanReadableModel();
            HumanReadableMeasureInformationModel measureInformationModel = new HumanReadableMeasureInformationModel(measureDetailsModel);
            model.setMeasureInformation(measureInformationModel);
            if (!ModelTypeHelper.isFhir(measureModel)) {
                standardizeStartAndEndDateForQdm(measureInformationModel);
            }
            humanReadableHTML = humanReadableGenerator.generate(measureInformationModel, measureModel);
        } catch (Exception e) {
            log.error("Exception in getHumanReadableForMeasureDetails: " + e.getMessage(), e);
        }
        return humanReadableHTML;
    }


    private void standardizeStartAndEndDateForQdm(HumanReadableMeasureInformationModel measureInformationModel) {

        if ((measureInformationModel.getMeasurementPeriodStartDate() == null && measureInformationModel.getMeasurementPeriodEndDate() == null) ||
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

    @Override
    public boolean libraryNameExists(String libraryName, String setId) {
        return getCqlService().checkIfLibraryNameExists(libraryName, setId);
    }

}
