package mat.server.service.impl;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.CQLService;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.MatContext;
import mat.client.shared.MatException;
import mat.client.shared.MessageDelegate;
import mat.dao.OrganizationDAO;
import mat.dao.UserDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureSetDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.model.Author;
import mat.model.Organization;
import mat.model.User;
import mat.model.clause.ComponentMeasure;
import mat.model.clause.Measure;
import mat.model.clause.MeasureDeveloperAssociation;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureTypeAssociation;
import mat.model.clause.MeasureXML;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLParameter;
import mat.server.CQLLibraryService;
import mat.server.LoggedInUserUtil;
import mat.server.logging.LogFactory;
import mat.server.service.MeasureCloningService;
import mat.server.util.CQLValidationUtil;
import mat.server.util.MATPropertiesService;
import mat.server.util.ManageMeasureDetailModelConversions;
import mat.server.util.MeasureUtility;
import mat.server.util.XmlProcessor;
import mat.shared.ConstantMessages;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.UUIDUtilClient;
import mat.shared.validator.measure.ManageMeasureModelValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MeasureCloningServiceImpl implements MeasureCloningService {

    private static final Log logger = LogFactory.getLog(MeasureCloningServiceImpl.class);

    private static final String QDM_BIRTHDATE_NON_DEFAULT = "birthdate";
    private static final String MEASURE_DETAILS = "measureDetails";
    private static final String MEASURE_GROUPING = "measureGrouping";
    private static final String UU_ID = "uuid";
    private static final String OID = "oid";
    private static final String DATATYPE = "datatype";
    private static final String VERSION = "version";
    private static final String SUPPLEMENTAL_DATA_ELEMENTS = "supplementalDataElements";
    private static final String RISK_ADJUSTMENT_VARIABLES = "riskAdjustmentVariables";
    private static final String VERSION_ZERO = "0.0";
    private static final String PATIENT_CHARACTERISTIC_BIRTH_DATE_OID = "21112-8";
    private static final String PATIENT_CHARACTERISTIC_EXPIRED_OID = "419099009";
    private static final String PATIENT_CHARACTERISTIC_BIRTH_DATE = "Patient Characteristic Birthdate";
    private static final String PATIENT_CHARACTERISTIC_EXPIRED = "Patient Characteristic Expired";
    private static final String TIMING_ELEMENT = "Timing ELement";
    private static final String ORIGINAL_NAME = "originalName";
    private static final String NAME = "name";
    private static final String PROGRAM = "program";
    private static final String RELEASE = "release";
    private static final String TYPE = "type";
    private static final String TAXONOMY = "taxonomy";
    private static final String GROUPING = "Grouping";
    private static final String EXTENSIONAL = "Extensional";

    private final CQLLibraryService cqlLibraryService;

    private final MeasureDAO measureDAO;

    private final MeasureXMLDAO measureXmlDAO;

    private final MeasureSetDAO measureSetDAO;

    private final OrganizationDAO organizationDAO;

    private final UserDAO userDAO;

    private final CQLService cqlService;

    private final MATPropertiesService propertiesService;

    public MeasureCloningServiceImpl(
            MATPropertiesService propertiesService,
            CQLLibraryService cqlLibraryService,
            MeasureDAO measureDAO,
            MeasureXMLDAO measureXmlDAO,
            MeasureSetDAO measureSetDAO,
            OrganizationDAO organizationDAO,
            UserDAO userDAO,
            CQLService cqlService) {
        this.propertiesService = propertiesService;
        this.cqlLibraryService = cqlLibraryService;
        this.measureDAO = measureDAO;
        this.measureXmlDAO = measureXmlDAO;
        this.measureSetDAO = measureSetDAO;
        this.organizationDAO = organizationDAO;
        this.userDAO = userDAO;
        this.cqlService = cqlService;
    }

    @Override
    public ManageMeasureSearchModel.Result clone(ManageMeasureDetailModel currentDetails, boolean creatingDraft) throws MatException {
        return clone(currentDetails, creatingDraft, false, false);
    }

    @Override
    public ManageMeasureSearchModel.Result cloneForFhir(ManageMeasureDetailModel currentDetails, boolean isQdmToFhir) throws MatException {
        return clone(currentDetails, true, true, isQdmToFhir);
    }

    private ManageMeasureSearchModel.Result clone(ManageMeasureDetailModel currentDetails,
                                                  boolean creatingDraft,
                                                  boolean creatingFhir,
                                                  boolean isQdmToFhir) throws MatException {
        logger.info("In MeasureCloningServiceImpl.clone() method..");

        validateMeasure(currentDetails);

        try {
            Measure measure = measureDAO.find(currentDetails.getId());

            if (checkNonCQLCloningValidation(measure, creatingDraft)) {
                MatException e = new MatException("Cannot clone this measure.");
                logger.error(e.getMessage(), e);
                throw e;
            }

            MeasureXML originalMeasureXml = measureXmlDAO.findForMeasure(currentDetails.getId());

            Measure clonedMeasure = new Measure();
            if (creatingFhir) {
                clonedMeasure.setSourceMeasureId(measure.getId());
            }
            String originalXml = originalMeasureXml.getMeasureXMLAsString();

            clonedMeasure.setaBBRName(currentDetails.getShortName());
            clonedMeasure.setDescription(currentDetails.getMeasureName());
            String newMeasureModel = creatingFhir ? ModelTypeHelper.FHIR : currentDetails.getMeasureModel();
            clonedMeasure.setMeasureModel(newMeasureModel);
            clonedMeasure.setCqlLibraryName(currentDetails.getCQLLibraryName());
            if (creatingFhir) {
                if (measure.getMeasurementPeriodFrom() == null || measure.getMeasurementPeriodTo() == null) {
                    clonedMeasure.setMeasurementPeriodFrom(getNextCalenderYearFromDate());
                    clonedMeasure.setMeasurementPeriodTo(getNextCalenderYearToDate());
                } else {
                    clonedMeasure.setMeasurementPeriodFrom(measure.getMeasurementPeriodFrom());
                    clonedMeasure.setMeasurementPeriodTo(measure.getMeasurementPeriodTo());
                }
                clonedMeasure.setFhirVersion(propertiesService.getFhirVersion());
            } else {
                clonedMeasure.setMeasurementPeriodFrom(getTimestampFromDateString(currentDetails.getMeasFromPeriod()));
                clonedMeasure.setMeasurementPeriodTo(getTimestampFromDateString(currentDetails.getMeasToPeriod()));
                clonedMeasure.setQdmVersion(propertiesService.getQdmVersion());
            }
            clonedMeasure.setReleaseVersion(measure.getReleaseVersion());
            clonedMeasure.setDraft(Boolean.TRUE);
            clonedMeasure.setPatientBased(currentDetails.isPatientBased());
            clonedMeasure.setReleaseVersion(propertiesService.getCurrentReleaseVersion());


            if (CollectionUtils.isNotEmpty(measure.getComponentMeasures()) && Boolean.TRUE.equals(measure.getIsCompositeMeasure())) {
                clonedMeasure.setIsCompositeMeasure(measure.getIsCompositeMeasure());
                clonedMeasure.setCompositeScoring(measure.getCompositeScoring());
                clonedMeasure.setComponentMeasures(cloneAndSetComponentMeasures(measure.getComponentMeasures(), clonedMeasure));
            }

            clonedMeasure.setMeasureScoring(currentDetails.getMeasScoring() != null ? currentDetails.getMeasScoring() : measure.getMeasureScoring());

            // when creating a draft of a shared version Measure then the Measure Owner should not change
            if (creatingDraft) {
                createDraftAndDetermineIfNonCQLAndPersist(isQdmToFhir, clonedMeasure, currentDetails, measure);
            } else {
                cloneMeasureAndPersist(clonedMeasure);
            }

            createMeasureXmlAndPersist(currentDetails, creatingDraft, creatingFhir, measure, clonedMeasure, originalXml);

            String formattedVersionWithText = MeasureUtility.getVersionTextWithRevisionNumber(clonedMeasure.getVersion(),
                    clonedMeasure.getRevisionNumber(), clonedMeasure.isDraft());

            ManageMeasureSearchModel.Result result = new ManageMeasureSearchModel.Result();
            result.setId(clonedMeasure.getId());
            result.setName(currentDetails.getMeasureName());
            result.setShortName(currentDetails.getShortName());
            result.setScoringType(currentDetails.getMeasScoring());
            result.setVersion(formattedVersionWithText);
            result.setMeasureModel(clonedMeasure.getMeasureModel());
            result.setEditable(Boolean.TRUE);
            result.setClonable(Boolean.TRUE);
            result.setPatientBased(clonedMeasure.getPatientBased());

            return result;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MatException(e.getMessage());
        }
    }

    private void createMeasureXmlAndPersist(ManageMeasureDetailModel currentDetails, boolean creatingDraft, boolean creatingFhir, Measure measure, Measure clonedMeasure, String originalXml) throws MatException {
        Document originalDoc = parseOriginalDocument(originalXml);
        Document clonedDoc = originalDoc;

        if (!creatingDraft) {
            clearChildNodes(clonedDoc, MEASURE_DETAILS);
        }

        String formattedVersion = MeasureUtility.formatVersionText(clonedMeasure.getRevisionNumber(), clonedMeasure.getVersion());

        SaveUpdateCQLResult saveUpdateCQLResult = cqlService.loadMeasureCql(measure, originalXml);


        // Create the measureGrouping tag
        clearChildNodes(clonedDoc, MEASURE_GROUPING);
        clearChildNodes(clonedDoc, SUPPLEMENTAL_DATA_ELEMENTS);
        clearChildNodes(clonedDoc, RISK_ADJUSTMENT_VARIABLES);

        String clonedXMLString = convertDocumenttoString(clonedDoc);
        MeasureXML clonedXml = new MeasureXML();
        clonedXml.setMeasureXMLAsByteArray(clonedXMLString);
        clonedXml.setMeasureId(clonedMeasure.getId());

        XmlProcessor xmlProcessor = new XmlProcessor(clonedXml.getMeasureXMLAsString());
        if (!creatingFhir) {
            // Don't do this for fhir. The converted CQL will still have these and they they can't be found when
            // generating the new mat xml.
            List<String> usedCodeList = saveUpdateCQLResult.getUsedCQLArtifacts().getUsedCQLcodes();
            xmlProcessor.removeUnusedDefaultCodes(usedCodeList);
        }

        removeMeasureDetailsNodes(xmlProcessor);

        updateScoring(currentDetails, measure, clonedMeasure, xmlProcessor);

        boolean isUpdatedForCQL = updateForCQLMeasure(xmlProcessor, clonedMeasure, creatingFhir);
        xmlProcessor.clearValuesetVersionAttribute();

        if (creatingDraft) {
            updateCQLLookUpVersionOnDraft(xmlProcessor, formattedVersion);
        } else {
            resetVersionOnCloning(xmlProcessor);
        }

        // this means this is a CQL Measure to CQL Measure draft/clone.
        if (!isUpdatedForCQL) {
            updateForCqlMeasureDraftOrClone(creatingFhir, xmlProcessor);
        }
        updateCqlLibraryName(clonedMeasure, xmlProcessor);

        clonedXml.setMeasureXMLAsByteArray(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));

        logger.info("Final XML after cloning/draft " + clonedXml.getMeasureXMLAsString());
        measureXmlDAO.save(clonedXml);
    }

    private void updateScoring(ManageMeasureDetailModel currentDetails, Measure measure, Measure clonedMeasure, XmlProcessor xmlProcessor) throws MatException {
        if (!measure.getMeasureScoring().equals(currentDetails.getMeasScoring()) || currentDetails.isPatientBased()) {
            String scoringTypeId = clonedMeasure.getMeasureScoring();
            try {
                xmlProcessor.removeNodesBasedOnScoring(scoringTypeId, currentDetails.isPatientBased());
            } catch (XPathExpressionException e) {
                throw new MatException(e);
            }
            createNewNodesBasedOnScoring(xmlProcessor, clonedMeasure, scoringTypeId);
        }
    }

    private void updateForCqlMeasureDraftOrClone(boolean creatingFhir, XmlProcessor xmlProcessor) throws MatException {
        //create the default 4 CMS supplemental definitions
        appendSupplementalDefinitions(xmlProcessor, false);
        // Always set latest model version.
        try {
            MeasureUtility.updateModelVersion(xmlProcessor, creatingFhir);
        } catch (XPathExpressionException e) {
            throw new MatException(e);
        }
    }

    private void updateCqlLibraryName(Measure clonedMeasure, XmlProcessor xmlProcessor) throws MatException {
        // Update CQL Library Name from the UI field
        try {
            xmlProcessor.updateCQLLibraryName(clonedMeasure.getCqlLibraryName());
        } catch (XPathExpressionException e) {
            throw new MatException(e);
        }
    }

    private Document parseOriginalDocument(String originalXml) throws MatException {
        try {
            InputSource oldXmlstream = new InputSource(new StringReader(originalXml));
            DocumentBuilderFactory documentBuilderFactory = XMLUtility.getInstance().buildDocumentBuilderFactory();
            DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
            return docBuilder.parse(oldXmlstream);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new MatException(e);
        }
    }

    private void validateMeasure(ManageMeasureDetailModel currentDetails) throws MatException {
        ManageMeasureModelValidator validator = new ManageMeasureModelValidator();
        List<String> messages = validator.validateMeasure(currentDetails);

        if (!messages.isEmpty()) {
            throw new MatException(MessageDelegate.GENERIC_ERROR_MESSAGE);
        }

        if (CQLValidationUtil.isCQLReservedWord(currentDetails.getCQLLibraryName())) {
            throw new MatException(MessageDelegate.LIBRARY_NAME_IS_CQL_KEYWORD_ERROR);
        }

        if (cqlService.checkIfLibraryNameExists(currentDetails.getCQLLibraryName(), currentDetails.getMeasureSetId())) {
            throw new MatException(MessageDelegate.DUPLICATE_LIBRARY_NAME);
        }
    }

    public void removeMeasureDetailsNodes(XmlProcessor xmlProcessor) throws MatException {
        removeNodeByPath(xmlProcessor, "/measure/measureDetails");
    }

    private Timestamp getTimestampFromDateString(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        try {
            SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                    "MM/dd/yyyy");
            Date fromDate = datetimeFormatter1.parse(date);
            return new Timestamp(fromDate.getTime());
        } catch (ParseException e) {
            logger.error("Cant convert measure date");
            return null;
        }

    }

    private boolean isOrgPresentCheckByOID(String stewardOID) {
        return organizationDAO.getAllOrganizations().stream().anyMatch(org -> String.valueOf(org.getOrganizationOID()).equals(stewardOID));
    }

    private boolean isOrgPresentCheckByID(String stewardID) {
        return organizationDAO.getAllOrganizations().stream().anyMatch(org -> String.valueOf(org.getId()).equals(stewardID));
    }

    private void createMeasureDevelopers(Measure clonedMeasure, ManageMeasureDetailModel currentDetails) {
        if (currentDetails.getAuthorSelectedList() != null) {
            List<MeasureDeveloperAssociation> developerAssociations = currentDetails.getAuthorSelectedList().stream()
                    .map(author -> createMeasureDetailsAssociation(author, clonedMeasure))
                    .collect(Collectors.toList());
            clonedMeasure.setMeasureDevelopers(developerAssociations);
        }
    }

    private MeasureDeveloperAssociation createMeasureDetailsAssociation(Author author, Measure measure) {
        String authorId = author.getId();
        if (isOrgPresentCheckByOID(authorId) || isOrgPresentCheckByID(authorId)) {
            Organization organization = organizationDAO.findByOidOrId(author.getId());
            if (organization != null) {
                return new MeasureDeveloperAssociation(measure, organization);
            }
        }
        return null;
    }

    private void createMeasureType(Measure clonedMeasure, ManageMeasureDetailModel currentDetails) {
        if (currentDetails.getMeasureTypeSelectedList() != null) {
            List<MeasureTypeAssociation> typesAssociations = currentDetails.getMeasureTypeSelectedList().stream()
                    .map(type -> new MeasureTypeAssociation(clonedMeasure, type))
                    .collect(Collectors.toList());
            clonedMeasure.setMeasureTypes(typesAssociations);
        }
    }

    private List<ComponentMeasure> cloneAndSetComponentMeasures(List<ComponentMeasure> componentMeasures, Measure clonedMeasure) {
        return componentMeasures.stream().map(f -> new ComponentMeasure(clonedMeasure, f.getComponentMeasure(), f.getAlias())).collect(Collectors.toList());
    }

    private boolean createDraftAndDetermineIfNonCQLAndPersist(boolean isQdmToFhir, Measure clonedMeasure, ManageMeasureDetailModel currentDetails, Measure measure) {

        copyMeasureDetails(isQdmToFhir, clonedMeasure, currentDetails);

        boolean isNonCQLtoCQLDraft = false;
        clonedMeasure.setOwner(measure.getOwner());
        clonedMeasure.setMeasureSet(measure.getMeasureSet());
        //MAT-9206 changes
        String draftVer;
        if (MatContext.get().isCQLMeasure(measure.getReleaseVersion())) {
            draftVer = measure.getVersion();
        } else {
            isNonCQLtoCQLDraft = true;
            draftVer = getVersionOnNonCQLDraft(currentDetails.getVersionNumber());
        }
        clonedMeasure.setVersion(draftVer);
        clonedMeasure.setRevisionNumber("000");
        clonedMeasure.seteMeasureId(measure.geteMeasureId());
        measureDAO.saveMeasure(clonedMeasure);

        return isNonCQLtoCQLDraft;
    }

    private void copyMeasureDetails(boolean isQdmToFhir, Measure clonedMeasure, ManageMeasureDetailModel currentDetails) {
        String stewardId = currentDetails.getStewardId();
        if (isOrgPresentCheckByID(stewardId) || isOrgPresentCheckByOID(stewardId)) {
            Organization organization = organizationDAO.findByOidOrId(currentDetails.getStewardId());
            if (organization != null) {
                clonedMeasure.setMeasureStewardId(String.valueOf(organization.getId()));
            }
        }
        clonedMeasure.setNqfNumber(currentDetails.getNqfId());
        ManageMeasureDetailModelConversions conversion = new ManageMeasureDetailModelConversions();
        conversion.createMeasureDetails(isQdmToFhir, clonedMeasure, currentDetails);
        createMeasureType(clonedMeasure, currentDetails);
        createMeasureDevelopers(clonedMeasure, currentDetails);
    }

    private void cloneMeasureAndPersist(Measure clonedMeasure) {
        // Clear the measureDetails tag
        if (LoggedInUserUtil.getLoggedInUser() != null) {
            User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
            clonedMeasure.setOwner(currentUser);
        }

        MeasureSet measureSet = new MeasureSet();
        measureSet.setId(UUID.randomUUID().toString());
        measureSetDAO.save(measureSet);
        clonedMeasure.setMeasureSet(measureSet);
        clonedMeasure.setVersion(VERSION_ZERO);
        clonedMeasure.setRevisionNumber("000");
        measureDAO.saveMeasure(clonedMeasure);
    }

    private boolean updateForCQLMeasure(XmlProcessor xmlProcessor, Measure clonedMeasure, boolean creatingFhir)
            throws MatException {

        Node cqlLookUpNode = findNode(xmlProcessor, "/measure/cqlLookUp");

        if (cqlLookUpNode != null) {

            // Update Model Version in Measure XMl for Draft and CQL Measures
            Node modelVersionNode = findNode(xmlProcessor, "/measure/cqlLookUp/usingModelVersion");
            if (modelVersionNode != null) {
                modelVersionNode.setTextContent(creatingFhir ? propertiesService.getFhirVersion() : propertiesService.getQdmVersion());
            }

            // Update Model Type
            Node modelNode = findNode(xmlProcessor, "/measure/cqlLookUp/usingModel");
            if (modelNode != null) {
                modelNode.setTextContent(creatingFhir ? ModelTypeHelper.FHIR : ModelTypeHelper.QDM);
            }

            return false;
        }

        removeNodeByPath(xmlProcessor, "/measure/populations");

        removeNodeByPath(xmlProcessor, "/measure/measureObservations");

        removeNodeByPath(xmlProcessor, "/measure/strata");

        String scoringTypeId = clonedMeasure.getMeasureScoring();

        createNewNodesBasedOnScoring(xmlProcessor, clonedMeasure, scoringTypeId);

        generateCqlLookupTag(xmlProcessor, clonedMeasure, creatingFhir);

        updateMeasureXmlWithQdm(xmlProcessor);

        checkForDefaultCQLParametersAndAppend(xmlProcessor);
        checkForDefaultCQLDefinitionsAndAppend(xmlProcessor);

        return true;
    }

    private void generateCqlLookupTag(XmlProcessor xmlProcessor, Measure clonedMeasure, boolean isFhir) {
        // This section generates CQL Look Up tag from CQLXmlTemplate.xml

        XmlProcessor cqlXmlProcessor = cqlLibraryService.loadCQLXmlTemplateFile(isFhir);
        String libraryName = clonedMeasure.getDescription();
        String version = clonedMeasure.getVersion();

        String cqlLookUpTag = cqlLibraryService.getCQLLookUpXml((MeasureUtility.cleanString(libraryName)),
                version,
                cqlXmlProcessor,
                "//measure",
                isFhir);

        if (cqlLookUpTag != null && StringUtils.isNotBlank(cqlLookUpTag)) {
            try {
                xmlProcessor.appendNode(cqlLookUpTag, "cqlLookUp", "/measure");
            } catch (SAXException | IOException e) {
                logger.debug("Measure lookup failed:" + e.getMessage(), e);
            }
        }
    }

    private void createNewNodesBasedOnScoring(XmlProcessor xmlProcessor, Measure clonedMeasure, String scoringTypeId) throws MatException {
        try {
            xmlProcessor.createNewNodesBasedOnScoring(scoringTypeId, propertiesService.getQdmVersion(), clonedMeasure.getPatientBased());
        } catch (XPathExpressionException e) {
            throw new MatException(e);
        }
    }

    private void removeNodeByPath(XmlProcessor xmlProcessor, String xPath) throws MatException {
        Node node = findNode(xmlProcessor, xPath);
        if (node != null) {
            Node parentNode = node.getParentNode();
            parentNode.removeChild(node);
        }
    }

    private Node findNode(XmlProcessor xmlProcessor, String xPath) throws MatException {
        try {
            return xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xPath);
        } catch (XPathExpressionException e) {
            throw new MatException(e);
        }
    }

    private NodeList findNodeList(XmlProcessor xmlProcessor, String xPath) throws MatException {
        try {
            return xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xPath);
        } catch (XPathExpressionException e) {
            throw new MatException(e);
        }
    }

    private void updateMeasureXmlWithQdm(XmlProcessor xmlProcessor) throws MatException {
        // copy qdm to cqlLookup/valuesets
        NodeList qdmNodes = findNodeList(xmlProcessor, "/measure/elementLookUp/qdm");
        Node cqlValuesetsNode = findNode(xmlProcessor, "/measure/cqlLookUp/valuesets");
        List<Node> qdmNodeList = new ArrayList<>();
        List<Node> cqlValuesetsNodeList = new ArrayList<>();

        if (cqlValuesetsNode != null) {
            populateQdmNodeList(xmlProcessor, qdmNodes, cqlValuesetsNode, qdmNodeList);
            populateCqlValueSets(xmlProcessor, cqlValuesetsNode, cqlValuesetsNodeList);
        }
        removeAllDuplicateValueSets(cqlValuesetsNodeList);
        removeAllUnclonableQdmElements(qdmNodeList);
    }

    private void populateQdmNodeList(XmlProcessor xmlProcessor, NodeList qdmNodes, Node cqlValuesetsNode, List<Node> qdmNodeList) {
        /**
         * We need to capture old "Patient Characteristic Expired"(oid=419099009) and "Patient Characteristic Birthdate"(oid=21112-8)
         * and remove them. Also we need to remove qdm with name="Birthdate' and 'Expired' which are non default qdms along with Occurrence
         * qdm elements.
         * Further below, when checkForTimingElementsAndAppend() is called, it will add back the above 2 elements with new properties.
         * For ex: "Patient Characteristic Expired" had an old name of "Expired", but the new name is "Dead".
         */

        for (int i = 0; i < qdmNodes.getLength(); i++) {
            Node qdmNode = qdmNodes.item(i);
            populateQdmNodeListWithNode(xmlProcessor, cqlValuesetsNode, qdmNodeList, qdmNode);
        }
    }

    private void populateQdmNodeListWithNode(XmlProcessor xmlProcessor, Node cqlValuesetsNode, List<Node> qdmNodeList, Node qdmNode) {
        String oid = qdmNode.getAttributes().getNamedItem(OID).getNodeValue();
        String qdmName = qdmNode.getAttributes().getNamedItem(NAME).getNodeValue();
        String dataType = qdmNode.getAttributes().getNamedItem(DATATYPE).getNodeValue();
        boolean isCloneable = calculateNodeCloneableAndPopulateToQdmNodeList(qdmNodeList, qdmNode, oid, qdmName, dataType);
        if (isCloneable) {
            adjustCqlValueSetsNodeOnClonable(xmlProcessor, cqlValuesetsNode, qdmNode);
        }
    }

    private boolean calculateNodeCloneableAndPopulateToQdmNodeList(List<Node> qdmNodeList, Node qdmNode, String oid, String qdmName, String dataType) {
        boolean isCloneable = true;
        if (oid.equals(PATIENT_CHARACTERISTIC_BIRTH_DATE_OID)) {
            isCloneable = false;
        } else if (oid.equals(PATIENT_CHARACTERISTIC_EXPIRED_OID) ||
                qdmName.equalsIgnoreCase(QDM_BIRTHDATE_NON_DEFAULT) ||
                dataType.equalsIgnoreCase(PATIENT_CHARACTERISTIC_BIRTH_DATE) ||
                dataType.equalsIgnoreCase(PATIENT_CHARACTERISTIC_EXPIRED) ||
                dataType.equalsIgnoreCase(TIMING_ELEMENT)) {
            qdmNodeList.add(qdmNode);
            isCloneable = false;
        }
        // Remove Specific Occurrence of QDM
        if (qdmNode.getAttributes().getNamedItem("instance") != null) {
            qdmNodeList.add(qdmNode);
            isCloneable = false;
        }
        return isCloneable;
    }

    private void adjustCqlValueSetsNodeOnClonable(XmlProcessor xmlProcessor, Node cqlValuesetsNode, Node qdmNode) {
        // MAT-8729 : Drafting of non-CQL to CQL measures - force version to be 1.0 if its value is 1.
        String qdmAppliedVersion = qdmNode.getAttributes().getNamedItem(VERSION).getNodeValue();
        if (qdmAppliedVersion.equals("1")) {
            qdmNode.getAttributes().getNamedItem(VERSION).setNodeValue("1.0");
        }
        Node clonedqdmNode = qdmNode.cloneNode(true);
        xmlProcessor.getOriginalDoc().renameNode(clonedqdmNode, null, "valueset");
        // MAT-8770
        if (clonedqdmNode.getAttributes().getNamedItem(DATATYPE) != null) {
            clonedqdmNode.getAttributes().removeNamedItem(DATATYPE);
        }
        cqlValuesetsNode.appendChild(clonedqdmNode);
    }

    private void populateCqlValueSets(XmlProcessor xmlProcessor, Node cqlValuesetsNode, List<Node> cqlValuesetsNodeList) {
        for (int i = 0; i < cqlValuesetsNode.getChildNodes().getLength(); i++) {
            Node valueSetNode = cqlValuesetsNode.getChildNodes().item(i);
            addOriginalNameAttributeIfNotPresent(valueSetNode, xmlProcessor);
            addProgramAttributeIfNotPresent(valueSetNode, xmlProcessor);
            addReleaseAttributeIfNotPresent(valueSetNode, xmlProcessor);
            addVSACTypeAttributeIfNotPresent(valueSetNode, xmlProcessor);
            cqlValuesetsNodeList.add(valueSetNode);
        }
    }

    private void removeAllDuplicateValueSets(List<Node> cqlValuesetsNodeList) {
        // Remove all duplicate value sets for new Value Sets workspace.
        if (cqlValuesetsNodeList != null && !cqlValuesetsNodeList.isEmpty()) {
            List<String> cqlVSACValueSets = new ArrayList<>();
            List<String> cqlUserDefValueSets = new ArrayList<>();
            for (int i = 0; i < cqlValuesetsNodeList.size(); i++) {
                Node cqlNode = cqlValuesetsNodeList.get(i);
                Node parentNode = cqlNode.getParentNode();
                String valuesetName = cqlNode.getAttributes().getNamedItem(NAME).getTextContent();
                processValueSet(cqlVSACValueSets, cqlUserDefValueSets, cqlNode, parentNode, valuesetName);
            }

            // Loop through user Defined and remove if it exists already in VSAC list
            removeValueSetsIfExistsInVsacList(cqlValuesetsNodeList, cqlVSACValueSets, cqlUserDefValueSets);

            cqlVSACValueSets.clear();
            cqlUserDefValueSets.clear();
        }
    }

    private void processValueSet(List<String> cqlVSACValueSets, List<String> cqlUserDefValueSets, Node cqlNode, Node parentNode, String valuesetName) {
        if (!isUserDefinedValueSet(cqlNode.getAttributes().getNamedItem(OID).getTextContent())) {
            if (!cqlVSACValueSets.contains(valuesetName)) {
                cqlVSACValueSets.add(valuesetName);
            } else {
                parentNode.removeChild(cqlNode);
            }
        } else {
            if (!cqlUserDefValueSets.contains(valuesetName)) {
                cqlUserDefValueSets.add(valuesetName);
            } else {
                parentNode.removeChild(cqlNode);
            }
        }
    }

    private void removeValueSetsIfExistsInVsacList(List<Node> cqlValuesetsNodeList, List<String> cqlVSACValueSets, List<String> cqlUserDefValueSets) {
        for (int i = 0; i < cqlValuesetsNodeList.size(); i++) {
            Node cqlNode = cqlValuesetsNodeList.get(i);
            Node parentNode = cqlNode.getParentNode();
            if (isUserDefinedValueSet(cqlNode.getAttributes().getNamedItem(OID).getTextContent())) {
                for (String userDefName : cqlUserDefValueSets) {
                    if (cqlVSACValueSets.contains(userDefName)) {
                        parentNode.removeChild(cqlNode);
                    }
                }
            }
        }
    }

    private void removeAllUnclonableQdmElements(List<Node> qdmNodeList) {
        // Remove all unclonable QDM's collected above in For Loop from elementLookUp tag.
        if (qdmNodeList != null && !qdmNodeList.isEmpty()) {
            for (int i = 0; i < qdmNodeList.size(); i++) {
                Node qNode = qdmNodeList.get(i);
                Node parentNode = qNode.getParentNode();
                parentNode.removeChild(qNode);
            }
        }
    }

    private boolean isUserDefinedValueSet(String valuesetOID) {
        return valuesetOID.equals(ConstantMessages.USER_DEFINED_QDM_OID);
    }

    private void addOriginalNameAttributeIfNotPresent(Node valueSetNode, XmlProcessor xmlProcessor) {
        addAttributeIfNotPresent(ORIGINAL_NAME, valueSetNode.getAttributes().getNamedItem(NAME).getNodeValue(), valueSetNode, xmlProcessor);
    }

    private void addProgramAttributeIfNotPresent(Node nodeToUpdate, XmlProcessor xmlProcessor) {
        addAttributeIfNotPresent(PROGRAM, StringUtils.EMPTY, nodeToUpdate, xmlProcessor);
    }

    private void addReleaseAttributeIfNotPresent(Node nodeToUpdate, XmlProcessor xmlProcessor) {
        addAttributeIfNotPresent(RELEASE, StringUtils.EMPTY, nodeToUpdate, xmlProcessor);
    }

    private void addVSACTypeAttributeIfNotPresent(Node valueSetNode, XmlProcessor xmlProcessor) {
        String valueSetType = getVSACTypeBasedOnTaxonomy(valueSetNode);
        addAttributeIfNotPresent(TYPE, valueSetType, valueSetNode, xmlProcessor);
    }

    private void addAttributeIfNotPresent(String attributeName, String value, Node nodeToUpdate, XmlProcessor xmlProcessor) {
        Node attribute = nodeToUpdate.getAttributes().getNamedItem(attributeName);
        if (attribute == null) {
            createDefaultAttr(attributeName, value, nodeToUpdate, xmlProcessor);
        }
    }

    private void createDefaultAttr(String name, String value, Node nodeToUpdate, XmlProcessor xmlProcessor) {
        Attr attr = xmlProcessor.getOriginalDoc().createAttribute(name);
        attr.setNodeValue(value);
        nodeToUpdate.getAttributes().setNamedItem(attr);
    }

    private String getVSACTypeBasedOnTaxonomy(Node valueset) {
        if (isUserDefinedValueSet(valueset.getAttributes().getNamedItem(OID).getNodeValue())) {
            return "";
        }
        return GROUPING.equals(valueset.getAttributes().getNamedItem(TAXONOMY).getNodeValue()) ? GROUPING : EXTENSIONAL;
    }

    /**
     * Append cql definitions.
     *
     * @param xmlProcessor the xml processor
     */
    private void checkForDefaultCQLDefinitionsAndAppend(XmlProcessor xmlProcessor) {

        NodeList defaultCQLDefNodeList = findDefaultDefinitions(xmlProcessor);

        if (defaultCQLDefNodeList != null && defaultCQLDefNodeList.getLength() == 4) {
            logger.info("All Default definition elements present in the measure while cloning.");
            logger.info("Check if SupplementalDataElement present in the measure while cloning.");
            // This checks if SDE holds child elements ,if not then append it.
            String defaultSDE = "/measure/supplementalDataElements";
            try {
                Node sdeNode = findNode(xmlProcessor, defaultSDE);
                if (sdeNode != null && !sdeNode.hasChildNodes()) {
                    appendSupplementalDefinitions(xmlProcessor, true);
                }
            } catch (MatException e) {
                logger.error("checkForDefaultCQLDefinitionsAndAppend:" + e.getMessage(), e);
            }
            return;
        }

        String defStr = cqlService.getSupplementalDefinitions();
        logger.info("defStr:" + defStr);
        try {
            xmlProcessor.appendNode(defStr, "definition", "/measure/cqlLookUp/definitions");

            appendSupplementalDefinitions(xmlProcessor, true);
        } catch (SAXException | IOException | MatException e) {
            logger.error("checkForDefaultCQLDefinitionsAndAppend:" + e.getMessage(), e);
        }
    }

    private void appendSupplementalDefinitions(XmlProcessor xmlProcessor, boolean createNewIds) throws MatException {
        Node supplementalDataNode = findNode(xmlProcessor, "/measure/supplementalDataElements");

        while (supplementalDataNode.hasChildNodes()) {
            supplementalDataNode.removeChild(supplementalDataNode.getFirstChild());
        }

        NodeList supplementalDefnNodes = findNodeList(xmlProcessor, "/measure/cqlLookUp/definitions/definition[@supplDataElement='true']");

        if (supplementalDefnNodes != null) {
            for (int i = 0; i < supplementalDefnNodes.getLength(); i++) {
                Node supplNode = supplementalDefnNodes.item(i);

                if (createNewIds) {
                    supplNode.getAttributes().getNamedItem("id").setNodeValue(UUIDUtilClient.uuid());
                }

                Element cqlDefinitionRefNode = xmlProcessor.getOriginalDoc().createElement("cqldefinition");
                cqlDefinitionRefNode.setAttribute("displayName", supplNode.getAttributes().getNamedItem(NAME).getNodeValue());
                cqlDefinitionRefNode.setAttribute(UU_ID, supplNode.getAttributes().getNamedItem("id").getNodeValue());
                supplementalDataNode.appendChild(cqlDefinitionRefNode);
            }
        }
    }

    /**
     * This method will look into XPath "/measure/cqlLookUp/definitions/" and try and NodeList for Definitions with the following names;
     * 'SDE Ethnicity','SDE Payer','SDE Race','SDE Sex'.
     *
     * @param xmlProcessor
     * @return
     */
    private NodeList findDefaultDefinitions(XmlProcessor xmlProcessor) {
        try {
            String defaultDefinitionsXPath = "/measure/cqlLookUp/definitions/definition[@name ='SDE Ethnicity' or @name='SDE Payer' or @name='SDE Race' or @name='SDE Sex']";
            return findNodeList(xmlProcessor, defaultDefinitionsXPath);
        } catch (MatException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Check for default cql parameters and append.
     *
     * @param xmlProcessor the xml processor
     */
    private void checkForDefaultCQLParametersAndAppend(XmlProcessor xmlProcessor) {

        List<String> missingDefaultCQLParameters = xmlProcessor.checkForDefaultParameters();

        if (missingDefaultCQLParameters.isEmpty()) {
            logger.info("All Default parameter elements present in the measure while cloning.");
            return;
        }
        logger.info("While cloning, found the following Default parameter elements missing:" + missingDefaultCQLParameters);
        CQLParameter parameter = new CQLParameter();

        parameter.setId(UUID.randomUUID().toString());
        parameter.setName(CQLWorkSpaceConstants.CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_NAME);
        parameter.setLogic(CQLWorkSpaceConstants.CQL_DEFAULT_MEASUREMENTPERIOD_PARAMETER_LOGIC);
        parameter.setReadOnly(true);
        String parStr = cqlService.createParametersXML(parameter);

        try {
            xmlProcessor.appendNode(parStr, "parameter", "/measure/cqlLookUp/parameters");
        } catch (SAXException | IOException e) {
            logger.debug("checkForDefaultCQLParametersAndAppend:" + e.getMessage());
        }

    }

    private void clearChildNodes(Document clonedDoc, String nodeName) {
        NodeList nodeList = clonedDoc.getElementsByTagName(nodeName);
        Node parentNode = nodeList.item(0);
        if (parentNode != null) {
            while (parentNode.hasChildNodes()) {
                parentNode.removeChild(parentNode.getFirstChild());
            }
        }
    }


    /**
     * Method to check If a user is trying to Clone a measure whose release version is not
     * v5.xx.
     * If the version is v4.xxx or v3.xxx or is NULL, then the method returns true, indicating cloning is not
     * allowed for this measure.
     *
     * @param measure
     * @param creatingDraft
     * @return
     */
    private boolean checkNonCQLCloningValidation(Measure measure,
                                                 boolean creatingDraft) {

        if (creatingDraft) {
            return false;
        }

        boolean returnValue = false;

        String measureReleaseVersion = StringUtils.trimToEmpty(measure.getReleaseVersion());
        if (measureReleaseVersion.length() == 0 || measureReleaseVersion.startsWith("v4") || measureReleaseVersion.startsWith("v3")) {
            returnValue = true;
        }

        return returnValue;
    }

    private String convertDocumenttoString(Document doc) throws MatException {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = XMLUtility.getInstance().buildTransformerFactory();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (TransformerException e) {
            throw new MatException(e);
        }

    }

    private String getVersionOnNonCQLDraft(String version) {

        String ver = StringUtils.substringAfterLast(version, "v");
        if (StringUtils.countMatches(ver, ".") == 2) {
            ver = StringUtils.substringBeforeLast(ver, ".");
        }
        String major = StringUtils.substringBeforeLast(ver, ".");
        String minor = StringUtils.substringAfterLast(ver, ".");

        return major + "." + StringUtils.leftPad(minor, 3, "0");
    }

    private void resetVersionOnCloning(XmlProcessor processor) {
        javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
        String cqlVersionXPath = "//cqlLookUp/version";
        try {
            Node node = (Node) xPath.evaluate(cqlVersionXPath, processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODE);
            if (node != null) {
                node.setTextContent("0.0.000");
            }
        } catch (XPathExpressionException e) {
            logger.error(e.getMessage());
        }

    }

    private void updateCQLLookUpVersionOnDraft(XmlProcessor processor, String version) {
        javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
        String cqlVersionXPath = "//cqlLookUp/version";
        try {
            Node node = (Node) xPath.evaluate(cqlVersionXPath, processor.getOriginalDoc().getDocumentElement(), XPathConstants.NODE);
            if (node != null) {
                node.setTextContent(version);
            }
        } catch (XPathExpressionException e) {
            logger.error(e.getMessage());
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
}
