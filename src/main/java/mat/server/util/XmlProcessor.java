package mat.server.util;

import mat.server.logging.LogFactory;
import mat.server.service.impl.XMLUtility;
import mat.shared.ConstantMessages;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class XmlProcessor {
    private static final String COHORT = "COHORT";
    private static final String XPATH_POPULATIONS = "/measure/populations";
    private static final String XPATH_NUMERATORS = "/measure/populations/numerators";
    private static final String XPATH_DENOMINATOR = "/measure/populations/denominators";
    private static final String XPATH_NUMERATOR_EXCLUSIONS = "/measure/populations/numeratorExclusions";
    private static final String XPATH_MEASURE_OBSERVATIONS = "/measure/measureObservations";
    private static final String XPATH_MEASURE_STRATIFICATIONS = "/measure/strata";
    private static final String XPATH_MEASURE_SD_ELEMENTS = "/measure/supplementalDataElements";
    private static final String XPATH_MEASURE_RAV_ELEMENTS = "/measure/riskAdjustmentVariables";
    private static final String XPATH_MEASURE_ELEMENT_LOOKUP = "/measure/elementLookUp";
    private static final String XPATH_MEASURE_SUBTREE_LOOKUP = "/measure/subTreeLookUp";
    private static final String XPATH_MEASURE_POPULATIONS = "/measure/populations/measurePopulations";
    private static final String XPATH_MEASURE_POPULATION_EXCLUSIONS = "/measure/populations/measurePopulationExclusions";
    private static final String XPATH_DENOMINATOR_EXCEPTIONS = "/measure/populations/denominatorExceptions";
    private static final String XPATH_DENOMINATOR_EXCLUSIONS = "/measure/populations/denominatorExclusions";
    private static final String RATIO = "RATIO";
    private static final String PROPORTION = "PROPORTION";
    private static final String CONTINUOUS_VARIABLE = "CONTINUOUS VARIABLE";
    private static final String NUMERATOR_EXCLUSIONS = "numeratorExclusions";
    private static final String DENOMINATOR_EXCEPTIONS = "denominatorExceptions";
    private static final String DENOMINATOR_EXCLUSIONS = "denominatorExclusions";
    private static final String DENOMINATORS = "denominators";
    private static final String NUMERATORS = "numerators";
    private static final String MEASURE_POPULATIONS = "measurePopulations";
    private static final String MEASURE_POPULATION_EXCLUSIONS = "measurePopulationExclusions";
    private static final String INITIAL_POPULATIONS = "initialPopulations";
    public static final String XPATH_MEASURE_CLAUSE = "/measure/populations/*/clause | /measure/*/clause | /measure/strata/stratification | /measure/strata/Stratification";
    public static final String XPATH_MEASURE_GROUPING = "/measure/measureGrouping";
    public static final String XPATH_MEASURE_GROUPING_GROUP = "/measure/measureGrouping/group";
    public static final String XPATH_GROUP_SEQ_START = "/measure/measureGrouping/group[@sequence = '";
    public static final String XPATH_GROUP_SEQ_END = "' ] ";
    public static final String XPATH_FIND_GROUP_CLAUSE = "/measure/measureGrouping/group[packageClause[";
    public static final String XPATH_OLD_ALL_RELATIONALOP_SBOD = "/measure//*/relationalOp[@type='SBOD']";
    public static final String XPATH_OLD_ALL_RELATIONALOP_EBOD = "/measure//*/relationalOp[@type='EBOD']";
    public static final String STRATIFICATION = "stratification";
    private static final String STRATIFICATION_DISPLAYNAME = "Stratification 1";
    private static final String MEASURE_OBSERVATION = "measureObservations";
    private static final String AND = "and";
    private static final String OR_STRING = "or";
    private static final String UUID_STRING = "uuid";
    private static final String DISPLAY_NAME = "displayName";
    private static final String TYPE = "type";
    private static final String PATIENT = " Patient ";
    private static Map<String, String> constantsMap = new HashMap<String, String>();
    private static Map<String, String> topNodeOperatorMap = new HashMap<String, String>();
    private static final Log LOG = LogFactory.getLog(XmlProcessor.class);
    private String originalXml;
    private DocumentBuilder docBuilder;
    private Document originalDoc;
    private static final String PARAMETER_MEASUREMENT_PERIOD = "Measurement Period";
    private static final String XPATH_FOR_CODES = "//cqlLookUp/codes/code";
    private static final String XPATH_FOR_VALUESETS = "//cqlLookUp/valuesets/valueset";
    private static final String ATTRIBUTE_CODE_OID = "codeOID";
    private static final String ATTRIBUTE_CODE_NAME = "codeName";
    private static final String ATTRIBUTE_READ_ONLY = "readOnly";
    private static final String ATTRIBUTE_CODE_SYSTEM_OID = "codeSystemOID";
    private static final String[] POPULATIONS = {
            INITIAL_POPULATIONS, NUMERATORS, NUMERATOR_EXCLUSIONS, DENOMINATORS,
            DENOMINATOR_EXCLUSIONS, DENOMINATOR_EXCEPTIONS, MEASURE_POPULATIONS,
            MEASURE_POPULATION_EXCLUSIONS};

    static {
        constantsMap.put("populations", "Populations");
        constantsMap.put(MEASURE_OBSERVATION, "Measure Observations");
        constantsMap.put("strata", "Stratification");
        constantsMap.put("initialPopulations", "Initial Populations");
        constantsMap.put("numerators", "Numerators");
        constantsMap.put("denominators", "Denominators");
        constantsMap.put("denominatorExclusions", "Denominator Exclusions");
        constantsMap.put("denominatorExceptions", "Denominator Exceptions");
        constantsMap.put("measurePopulations", "Measure Populations");
        constantsMap.put("measurePopulationExclusions", "Measure Population Exclusions");
        constantsMap.put("numeratorExclusions", "Numerator Exclusions");
        constantsMap.put("Measure Observations", "Measure Observation");
        constantsMap.put("Stratum", "Stratum");
        constantsMap.put("Initial Populations", "Initial Population");
        constantsMap.put("Numerators", "Numerator");
        constantsMap.put("Denominators", "Denominator");
        constantsMap.put("Denominator Exclusions", "Denominator Exclusions");
        constantsMap.put("Denominator Exceptions", "Denominator Exceptions");
        constantsMap.put("Measure Populations", "Measure Population");
        constantsMap.put("Measure Population Exclusions", "Measure Population Exclusions");
        constantsMap.put("Numerator Exclusions", "Numerator Exclusions");
        topNodeOperatorMap.put("initialPopulations", AND);
        topNodeOperatorMap.put("numerators", AND);
        topNodeOperatorMap.put("denominators", AND);
        topNodeOperatorMap.put("measurePopulations", AND);
        topNodeOperatorMap.put("denominatorExclusions", OR_STRING);
        topNodeOperatorMap.put("numeratorExclusions", OR_STRING);
        topNodeOperatorMap.put("denominatorExceptions", OR_STRING);
        topNodeOperatorMap.put("measurePopulationExclusions", OR_STRING);
    }

    /**
     * Instantiates a new xml processor.
     *
     * @param originalXml the original xml
     */
    public XmlProcessor(String originalXml) {
        LOG.debug("In XmlProcessor() constructor");
        this.originalXml = originalXml;
        try {
            DocumentBuilderFactory documentBuilderFactory = XMLUtility.getInstance().buildDocumentBuilderFactory();
            docBuilder = documentBuilderFactory.newDocumentBuilder();
            InputSource oldXmlstream = new InputSource(new StringReader(originalXml));
            originalDoc = docBuilder.parse(oldXmlstream);
            LOG.debug("Document Object created successfully for the XML String");
        } catch (Exception e) {
            LOG.error("Exception thrown on XmlProcessor() constructor",e);
            caughtExceptions(e);
        }

    }

    /**
     * Instantiates a new xml processor for HQMFMeasureXml.
     *
     * @param file the file
     */
    public XmlProcessor(File file) {
        try {
            DocumentBuilderFactory documentBuilderFactory = XMLUtility.getInstance().buildDocumentBuilderFactory();
            docBuilder = documentBuilderFactory.newDocumentBuilder();
            originalDoc = docBuilder.parse(file);
            LOG.debug("Document Object created successfully for the XML String");
        } catch (ParserConfigurationException e) {
            LOG.error("Exception thrown on XmlProcessor() constructor", e);
        } catch (SAXException e) {
            LOG.error("Exception thrown on XmlProcessor() constructor", e);
        } catch (IOException e) {
            LOG.debug("Exception thrown on XmlProcessor() constructor", e);
            caughtExceptions(e);
        }

    }

    /**
     * Method to insert new Node under existing parent Node.
     *
     * @param newElement the new element
     * @param nodeName   the node name
     * @param parentNode the parent node
     * @return the string
     * @throws SAXException the sAX exception
     * @throws IOException  Signals that an I/O exception has occurred.
     */
    public String appendNode(String newElement, String nodeName,
                             String parentNode) throws SAXException, IOException {
        LOG.debug("In appendNode method with newElement ::: ");
        if ((originalDoc == null) || (newElement == null)) {
            return "";
        }
        try {
            Node parentTypeNode = findNode(originalDoc, parentNode);
            if (parentTypeNode != null) {
                InputSource newXmlstream = new InputSource(new StringReader(
                        newElement));
                // Parse the NewXml which should be replaced.
                Document newDoc = docBuilder.parse(newXmlstream);
                NodeList newNodeList = newDoc.getElementsByTagName(nodeName);
                for (int i = 0; i < newNodeList.getLength(); i++) {
                    Node newNode = newNodeList.item(i);
                    parentTypeNode.appendChild(originalDoc.importNode(newNode,
                            true));
                }

                LOG.debug("Document Object created successfully for the XML String.");
            } else {
                LOG.debug("parentNode:" + parentNode
                        + " not found. method appendNode exiting prematurely.");
            }
        } catch (XPathExpressionException e) {
            LOG.debug("Exception thrown on appendNode method");
            caughtExceptions(e);
        }
        return transform(originalDoc);
    }

    /**
     * Method with Replace/Insert Node into the Original Xml ------- REPLACE
     * ----- Example NewXml - <ABC>new text</ABC> OldXml - <AAA><ABC>old
     * Text</ABC><AAA> Result - <AAA><ABC>new text</ABC><AAA> ------- INSERT
     * ------ Example NewXml - <ABC>new text</ABC> OldXml -
     * <AAA><BBB>first</BBB><AAA> Result - <AAA><BBB>first</BBB><ABC>new
     * text</ABC><AAA>.
     *
     * @param newXml     the new xml
     * @param nodeName   the node name
     * @param parentName // this is optional, can be null or empty. if parentName not
     *                   null, the oldNode to be replaced will be retrieved based on
     *                   the parent Node, this is done to make sure we are replacing
     *                   the correct node.
     * @return the string
     */
    public String replaceNode(String newXml, String nodeName, String parentName) {
        try {
            LOG.debug("In replaceNode() method");
            InputSource newXmlstream = new InputSource(new StringReader(newXml));
            Document newDoc = docBuilder.parse(newXmlstream); // Parse the NewXml
            // which should
            // be replaced
            Node newNode = null;
            Node oldNode = null;
            NodeList newNodeList = newDoc.getElementsByTagName(nodeName);
            NodeList oldNodeList = originalDoc.getElementsByTagName(nodeName);
            if (oldNodeList.getLength() > 0) {
                if (StringUtils.isBlank(parentName)) {
                    oldNode = oldNodeList.item(0);
                } else {
                    for (int i = 0; i < oldNodeList.getLength(); i++) {
                        if (parentName.equals(oldNodeList.item(i)
                                .getParentNode().getNodeName())) { // get the old
                            // node with
                            // the
                            // matching
                            // Parent
                            // Node.
                            oldNode = oldNodeList.item(i);
                            break;
                        }
                    }
                }
            }
            if (newNodeList.getLength() > 0) {
                newNode = newNodeList.item(0);
                for (int i = 0; i < newNodeList.getLength(); i++) {
                    if (parentName.equals(newNodeList.item(i).getParentNode()
                            .getNodeName())) { // get the new node used to
                        // replace.
                        newNode = newNodeList.item(i);
                        break;
                    }
                }
                if (oldNode != null) { // check if the OriginalXml has the Node
                    // that should be replaced
                    Node nextSibling = oldNode.getNextSibling();
                    Node parentNode = oldNode.getParentNode();
                    parentNode.removeChild(oldNode); // Removing the old child
                    // node
                    if (nextSibling != null) {
                        // to maintain the order insert before the next sibling if exists
                        parentNode.insertBefore(
                                originalDoc.importNode(newNode, true),
                                nextSibling);
                    } else {
                        parentNode.appendChild(originalDoc.importNode(newNode,
                                true)); // insert the new child node to the old
                        // child's Parent node,.
                    }
                    LOG.debug("Replaced old Child Node with new Child Node "
                            + nodeName);
                } else { // if the Original Document doesnt have the Node, then
                    // insert the new Node under the first child
                    Node importNode = originalDoc.importNode(newNode, true);
                    originalDoc.getFirstChild().appendChild(importNode);
                    LOG.debug("Inserted new Child Node" + nodeName);
                }
                return transform(originalDoc);
            }

        } catch (Exception e) {
            LOG.debug("Exception thrown on replaceNode() method", e);
            caughtExceptions(e);
        }
        return originalXml; // not replaced returnig the original Xml;
    }

    /**
     * Update node text.
     *
     * @param nodeName  the node name
     * @param nodeValue the node value
     * @return the string
     */
    public String updateNodeText(String nodeName, String nodeValue) {
        try {
            LOG.debug("In updateNodeText() method");
            InputSource xmlStream = new InputSource(new StringReader(
                    originalXml));
            Document doc = docBuilder.parse(xmlStream);
            doc.getElementsByTagName(nodeName).item(0).setTextContent(nodeValue);
            LOG.debug("update NoedText");
            return transform(doc);
        } catch (Exception e) {
            LOG.error("Exception thrown on updateNodeText() method", e);
            caughtExceptions(e);
        }
        return null;
    }

    /**
     * Gets the original xml.
     *
     * @return the original xml
     */
    public String getOriginalXml() {
        return originalXml;
    }

    /**
     * Sets the original xml.
     *
     * @param originalXml the new original xml
     */
    public void setOriginalXml(String originalXml) {
        this.originalXml = originalXml;
    }

    /**
     * Gets the xml by tag name.
     *
     * @param tagName the tag name
     * @return the xml by tag name
     */
    public String getXmlByTagName(String tagName) {
        String returnVar = null;
        Node node = originalDoc.getElementsByTagName(tagName).getLength() > 0 ? originalDoc
                .getElementsByTagName(tagName).item(0) : null;
        if (null != node) {
            returnVar = transform(node);
        }
        return returnVar;
    }

    /**
     * Adds the parent node.
     *
     * @param parentTagName the parent tag name
     */
    public void addParentNode(String parentTagName) {
        if (originalDoc.hasChildNodes()) {
            Document newDoc = docBuilder.newDocument();
            Node parentNode = newDoc.appendChild(newDoc
                    .createElement(parentTagName));
            Node importedNode = newDoc.importNode(originalDoc.getFirstChild(),
                    true);
            parentNode.appendChild(importedNode);
            originalDoc = newDoc;
        }
    }

    /**
     * Caught exceptions.
     *
     * @param excp the exception that was caught
     */
    private void caughtExceptions(Exception excp) {
        if (excp instanceof ParserConfigurationException) {
            LOG.error("Document Builder Object creation failed:" + excp.getMessage(), excp);
        } else if (excp instanceof SAXException) {
            LOG.error("Xml parsing failed:" + excp.getMessage(), excp);
        } else if (excp instanceof IOException) {
            LOG.error("Conversion of String XML to InputSource failed" + excp.getMessage(), excp);
        } else {
            LOG.error("Generic Exception: " + excp.getMessage(), excp);
        }
    }

    /**
     * Check for scoring type.
     *
     * @return the string
     */
    public String checkForScoringType(String releaseVersion, String scoringType) {
        if (originalDoc == null) {
            return "";
        }
        try {
            removeNodesBasedOnScoring(scoringType);
            createNewNodesBasedOnScoring(scoringType, releaseVersion);
        } catch (XPathExpressionException e) {
            LOG.error(e.getMessage(), e);
        }
        return transform(originalDoc);
    }

    /**
     * This method looks at the Scoring Type for a measure and removes nodes
     * based on the value of Scoring Type.
     *
     * @param scoringType the scoring type
     * @throws XPathExpressionException the x path expression exception
     */
    public void removeNodesBasedOnScoring(String scoringType) throws XPathExpressionException {
        List<String> xPathList = new ArrayList<String>();

        if (RATIO.equalsIgnoreCase(scoringType)) {
            // Denominator Exceptions, Measure Populations
            xPathList.add(XPATH_DENOMINATOR_EXCEPTIONS);
            xPathList.add(XPATH_MEASURE_POPULATIONS);
            xPathList.add(XPATH_MEASURE_POPULATION_EXCLUSIONS);
        } else if (PROPORTION.equalsIgnoreCase(scoringType)) {
            // Measure Population Exlusions, Measure Populations
            xPathList.add(XPATH_MEASURE_POPULATIONS);
            xPathList.add(XPATH_MEASURE_POPULATION_EXCLUSIONS);
            xPathList.add(XPATH_MEASURE_OBSERVATIONS);
        } else if (CONTINUOUS_VARIABLE.equalsIgnoreCase(scoringType)) {
            // Numerators,Numerator Exclusions, Denominators, Denominator
            // Exceptions, Denominator Exclusions
            xPathList.add(XPATH_NUMERATORS);
            xPathList.add(XPATH_NUMERATOR_EXCLUSIONS);
            xPathList.add(XPATH_DENOMINATOR);
            xPathList.add(XPATH_DENOMINATOR_EXCEPTIONS);
            xPathList.add(XPATH_DENOMINATOR_EXCLUSIONS);
        } else if (COHORT.equalsIgnoreCase(scoringType)) {
            xPathList.add(XPATH_NUMERATORS);
            xPathList.add(XPATH_NUMERATOR_EXCLUSIONS);
            xPathList.add(XPATH_DENOMINATOR);
            xPathList.add(XPATH_DENOMINATOR_EXCEPTIONS);
            xPathList.add(XPATH_DENOMINATOR_EXCLUSIONS);
            xPathList.add(XPATH_MEASURE_POPULATIONS);
            xPathList.add(XPATH_MEASURE_POPULATION_EXCLUSIONS);
            xPathList.add(XPATH_MEASURE_OBSERVATIONS);
        }
        for (String xPathString : xPathList) {
            Node node = findNode(originalDoc, xPathString);
            removeFromParent(node);
        }
    }

    /**
     * Rename ip p_ to_ ip.
     *
     * @param document the document
     * @throws XPathExpressionException the x path expression exception
     */
    public void renameIPPToIP(Document document) throws XPathExpressionException {
        String clause = "clause";
        String initialPopulation = "initialPopulation";
        String initialPatientPopulation = "initialPatientPopulation";
        String xpathOldInitPatientPop = "/measure/populations/initialPatientPopulations";
        String xpathOldGroupingPackageClause = "/measure/measureGrouping/*/packageClause";

        if (document == null) {
            return;
        }

        //Find and Replace IPP to IP in measureGrouping/group/packageClause.
        javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodesPackageClauses = (NodeList) xPath.evaluate(xpathOldGroupingPackageClause,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);

        for (int i = 0; i < nodesPackageClauses.getLength(); i++) {
            Node childNode = nodesPackageClauses.item(i);
            String packageClauseType = childNode.getAttributes().getNamedItem(TYPE).getNodeValue();
            String packageClauseName = childNode.getAttributes().getNamedItem("name").getNodeValue();
            if (packageClauseType.equalsIgnoreCase(initialPatientPopulation)) {
                childNode.getAttributes().getNamedItem(TYPE).setNodeValue(initialPopulation);
                if (packageClauseName.indexOf(PATIENT) > 0) {
                    packageClauseName = packageClauseName.replaceAll(PATIENT, " ");
                }
                childNode.getAttributes().getNamedItem("name").setNodeValue(packageClauseName);
            }
        }

        //find initialPatientPopulations tag
        Node initialPopulationsNode = findNode(document, xpathOldInitPatientPop);
        if (initialPopulationsNode == null) {
            return;
        }

        //Also change the value of the 'displayName' attribute
        initialPopulationsNode.getAttributes().getNamedItem(DISPLAY_NAME).setNodeValue("Initial Populations");

        //within 'initialPopulations' tag, for all 'clause' tags, rename the 'displayName' attribute
        //from 'Initial Patient Population 1' to 'Initial Population 1'.
        //Also change 'type' attribute to 'initialPopulation'
        NodeList childNodes = initialPopulationsNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (clause.equals(childNode.getNodeName())) {
                childNode.getAttributes().getNamedItem(TYPE).setNodeValue(initialPopulation);

                String clauseDisplayName = childNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
                if (clauseDisplayName.indexOf(PATIENT) > 0) {
                    clauseDisplayName = clauseDisplayName.replaceAll(PATIENT, " ");
                }
                childNode.getAttributes().getNamedItem(DISPLAY_NAME).setNodeValue(clauseDisplayName);
            }
        }

        //rename 'initialPatientPopulations' to 'initialPopulations'.
        document.renameNode(initialPopulationsNode, "", INITIAL_POPULATIONS);
    }

    /**
     * Rename timing conventions.
     *
     * @param document the document
     * @throws XPathExpressionException the x path expression exception
     */
    public void renameTimingConventions(Document document) throws XPathExpressionException {
        String startsBeforeOrDuring = "Starts Before Or During";
        String endsBeforeOrDuring = "Ends Before Or During";
        String startBeforeEnd = "Starts Before End";
        String endsBeforeEnd = "Ends Before End";
        String sBE = "SBE";
        String eBE = "EBE";
        javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
        //replace relationalOp attribute values for displayName and type from SBOD to SBE
        NodeList nodesRelationalOpsSBOD = (NodeList) xPath.evaluate(XPATH_OLD_ALL_RELATIONALOP_SBOD,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);
        for (int i = 0; i < nodesRelationalOpsSBOD.getLength(); i++) {
            Node childNode = nodesRelationalOpsSBOD.item(i);
            String relationalOpDisplayName = childNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
            relationalOpDisplayName = relationalOpDisplayName.replace(startsBeforeOrDuring, startBeforeEnd);
            childNode.getAttributes().getNamedItem(DISPLAY_NAME).setNodeValue(relationalOpDisplayName);
            childNode.getAttributes().getNamedItem(TYPE).setNodeValue(sBE);
        }
        //replace relationalOp attribute values for displayName and type from EBOD to EBE
        NodeList nodesRelationalOpsEBOD = (NodeList) xPath.evaluate(XPATH_OLD_ALL_RELATIONALOP_EBOD,
                originalDoc.getDocumentElement(), XPathConstants.NODESET);
        for (int i = 0; i < nodesRelationalOpsEBOD.getLength(); i++) {
            Node childNode = nodesRelationalOpsEBOD.item(i);
            String relationalOpDisplayName = childNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
            relationalOpDisplayName = relationalOpDisplayName.replace(endsBeforeOrDuring, endsBeforeEnd);
            childNode.getAttributes().getNamedItem(DISPLAY_NAME).setNodeValue(relationalOpDisplayName);
            childNode.getAttributes().getNamedItem(TYPE).setNodeValue(eBE);
        }
    }


    /**
     * This method looks at the Scoring Type for a measure and adds nodes based
     * on the value of Scoring Type.
     *
     * @param scoringType    the scoring type
     * @param releaseVersion
     * @throws XPathExpressionException the x path expression exception
     */
    public void createNewNodesBasedOnScoring(String scoringType, String releaseVersion)
            throws XPathExpressionException {
        List<String> scoreBasedNodes = retrieveScoreBasedNodes(scoringType);
        Node populationsNode = findNode(originalDoc, XPATH_POPULATIONS);

        if (populationsNode == null) {
            populationsNode = addPopulationsNode();
        }
        boolean childAppended = calculateChildAppend(scoreBasedNodes,
                populationsNode);
        /**
         * Add Measure Observations node after Populations node if not present
         * and scoring type is Continuous Variable.
         */
        Node measureObservationsNode = findNode(originalDoc, XPATH_MEASURE_OBSERVATIONS);
        if ((CONTINUOUS_VARIABLE.equalsIgnoreCase(scoringType) || RATIO.equalsIgnoreCase(scoringType))
                && (measureObservationsNode == null)) {
            // Create a new measureObservations element.
            String nodeName = MEASURE_OBSERVATION;
            String displayName = constantsMap.get(nodeName);
            Element mainChildElem = createTemplateNode(nodeName,
                    displayName);
            measureObservationsNode = mainChildElem;
            // insert measureObservations element after populations element
            Node measureNode = populationsNode.getParentNode();
            Element measureElement = (Element) measureNode;
            measureElement.insertBefore(mainChildElem,
                    populationsNode.getNextSibling());
        }
        // Create stratifications node
        Node measureStratificationsNode = findNode(originalDoc,
                XPATH_MEASURE_STRATIFICATIONS);
        if (measureStratificationsNode == null && !RATIO.equalsIgnoreCase(scoringType)) {
            String stratificationsNodeName = "strata";
            String clauseDisplayName = "Stratum";
            Element stratificationElement = createTemplateNode(
                    stratificationsNodeName, clauseDisplayName);

            NodeList childs = stratificationElement.getChildNodes();
            Element stratificationEle = originalDoc
                    .createElement(STRATIFICATION);
            stratificationEle.setAttribute(DISPLAY_NAME, STRATIFICATION_DISPLAYNAME);
            stratificationEle.setAttribute(UUID_STRING, UUIDUtilClient.uuid());
            stratificationEle.setAttribute(TYPE, STRATIFICATION);
            List<Node> nCList = new ArrayList<Node>();
            for (int i = 0; i < childs.getLength(); i++) {
                nCList.add(childs.item(i));
                stratificationElement.removeChild(childs.item(i));
            }
            for (Node cNode : nCList) {
                stratificationEle.appendChild(cNode);
            }
            stratificationElement.appendChild(stratificationEle);

            measureStratificationsNode = stratificationElement;
            // insert measureObservations element after populations element
            Node measureNode = populationsNode.getParentNode();
            Element measureElement = (Element) measureNode;
            if (measureObservationsNode != null) {
                measureElement.insertBefore(stratificationElement,
                        measureObservationsNode.getNextSibling());
            } else {
                measureElement.insertBefore(stratificationElement,
                        populationsNode.getNextSibling());
            }
        } else if (measureStratificationsNode != null && RATIO.equalsIgnoreCase(scoringType)) {
            Node measureNode = measureStratificationsNode.getParentNode();
            Element measureElement = (Element) measureNode;

            measureElement.removeChild(measureStratificationsNode);
        }
        // Create supplementalDataElements node
        releaseVersion = releaseVersion.replaceAll("Draft ", "").trim();
        if (releaseVersion.startsWith("v")) {
            releaseVersion = releaseVersion.substring(1);
        }
        createSupplementalDataElementNode(measureStratificationsNode, releaseVersion);
        /*
         * All the adding and removing can put the children of 'populations' in
         * a random order. Arrange the population nodes in correct order.*/
        // If no children have been appended, dont go through the process of
        // re-arranging the
        // populations node children.
        if (!childAppended) {
            return;
        }
        // remove all the child nodes of populations node first
        NodeList childNodes = populationsNode.getChildNodes();
        List<Node> childNodesList = new ArrayList<Node>();
        for (int c = 0; c < childNodes.getLength(); c++) {
            childNodesList.add(childNodes.item(c));
        }
        for (int i = 0; i < childNodesList.size(); i++) {
            populationsNode
                    .removeChild(populationsNode.getChildNodes().item(0));
        }
        arrangeChildNodeList(populationsNode, childNodesList);

    }

    /**
     * Take the child nodeList & re-arrange it according to this order
     * "Initial Patient Populations", "Numerators", "Numerator Exclusions",
     * "Denominators", "Denominator Exclusions",
     * "Denominator Exceptions", "Measure Populations", "Measure Population Exclusions".
     *
     * @param populationsNode the population node
     * @param childNodesList  array list of nodes to be ordered
     */
    private void arrangeChildNodeList(Node populationsNode,
                                      List<Node> childNodesList) {
        // Take the child nodeList & re-arrange it according to this order
        // "Initial Patient Populations", "Numerators", "Numerator Exclusions",
        // "Denominators", "Denominator Exclusions",
        // "Denominator Exceptions", "Measure Populations", "Measure Population Exclusions"
        for (String populationsChild : XmlProcessor.POPULATIONS) {
            Node populationsChildNode = null;
            for (int j = 0; j < childNodesList.size(); j++) {
                Node child = childNodesList.get(j);
                if (child.getNodeName().equals(populationsChild)) {
                    populationsChildNode = child;
                    break;
                }
            }
            if (populationsChildNode != null) {
                populationsNode.appendChild(populationsChildNode);
            }
        }
    }

    /**
     * Creates the Supplemental Data Element Node.
     *
     * @param measureStratificationsNode stratifications Node for the measure
     * @param releaseVersion
     * @throws XPathExpressionException the x path expression exception
     */
    private void createSupplementalDataElementNode(
            Node measureStratificationsNode, String releaseVersion) throws XPathExpressionException {
        Node supplementaDataElementsElement = findNode(originalDoc,
                XPATH_MEASURE_SD_ELEMENTS);
        if (supplementaDataElementsElement == null) {
            supplementaDataElementsElement = originalDoc
                    .createElement("supplementalDataElements");
            ((Element) measureStratificationsNode.getParentNode())
                    .insertBefore(supplementaDataElementsElement,
                            measureStratificationsNode.getNextSibling());
        }
        // Create elementLookUp node
        if (findNode(originalDoc, XPATH_MEASURE_ELEMENT_LOOKUP) == null) {
            Element elementLookUpElement = originalDoc
                    .createElement("elementLookUp");
            ((Element) supplementaDataElementsElement.getParentNode())
                    .insertBefore(elementLookUpElement,
                            supplementaDataElementsElement.getNextSibling());
        }
        if (findNode(originalDoc, XPATH_MEASURE_SUBTREE_LOOKUP) == null) {
            Element subTreeLookUpElement = originalDoc
                    .createElement("subTreeLookUp");
            ((Element) supplementaDataElementsElement.getParentNode())
                    .insertBefore(subTreeLookUpElement,
                            supplementaDataElementsElement.getNextSibling());
        }
        // create Measure Grouping node
        if (findNode(originalDoc, XPATH_MEASURE_GROUPING) == null) {
            Element measureGroupingElement = originalDoc
                    .createElement("measureGrouping");
            ((Element) supplementaDataElementsElement.getParentNode())
                    .insertBefore(measureGroupingElement,
                            supplementaDataElementsElement.getNextSibling());
        }

        Node riskAdjustmentVariablesElement = findNode(originalDoc,
                XPATH_MEASURE_RAV_ELEMENTS);
        if (riskAdjustmentVariablesElement == null) {
            riskAdjustmentVariablesElement = originalDoc
                    .createElement("riskAdjustmentVariables");
            ((Element) supplementaDataElementsElement.getParentNode())
                    .insertBefore(riskAdjustmentVariablesElement,
                            supplementaDataElementsElement.getNextSibling());
        }

        LOG.debug("Original Doc: " + originalDoc.toString());
    }

    /**
     * Calculates whether we appended a child node or not.
     *
     * @param scoreBasedNodes the score Base Node
     * @param populationsNode the node for the measures population
     * @return Boolean
     * true: if we appended a child
     * false: if we didn't append a child
     */
    private boolean calculateChildAppend(List<String> scoreBasedNodes,
                                         Node populationsNode) {
        boolean childAppended = false;
        for (String nodeName : scoreBasedNodes) {
            boolean isNodePresent = false;
            NodeList childNodes = populationsNode.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                String childNodeName = childNode.getNodeName();
                if (childNodeName.equals(nodeName)) {
                    isNodePresent = true;
                    break;
                }
            }
            if (!isNodePresent) {
                String displayName = constantsMap.get(nodeName);
                Element mainChildElem = createTemplateNode(nodeName,
                        displayName);
                populationsNode.appendChild(mainChildElem);
                childAppended = true;
            }
        }
        return childAppended;
    }

    /**
     * Retrieves the Score Based nodes.
     *
     * @param scoringType the scoring type
     * @return List<String>
     * the score based Nodes
     */
    private List<String> retrieveScoreBasedNodes(String scoringType) {
        List<String> scoreBasedNodes = new ArrayList<String>();
        if (CONTINUOUS_VARIABLE.equalsIgnoreCase(scoringType)) {
            scoreBasedNodes.add(INITIAL_POPULATIONS);
            scoreBasedNodes.add(MEASURE_POPULATIONS);
            scoreBasedNodes.add(MEASURE_POPULATION_EXCLUSIONS);
        } else if (PROPORTION.equalsIgnoreCase(scoringType)) {
            scoreBasedNodes.add(INITIAL_POPULATIONS);
            scoreBasedNodes.add(NUMERATORS);
            scoreBasedNodes.add(NUMERATOR_EXCLUSIONS);
            scoreBasedNodes.add(DENOMINATORS);
            scoreBasedNodes.add(DENOMINATOR_EXCLUSIONS);
            scoreBasedNodes.add(DENOMINATOR_EXCEPTIONS);
        } else if (RATIO.equalsIgnoreCase(scoringType)) {
            scoreBasedNodes.add(INITIAL_POPULATIONS);
            scoreBasedNodes.add(NUMERATORS);
            scoreBasedNodes.add(NUMERATOR_EXCLUSIONS);
            scoreBasedNodes.add(DENOMINATORS);
            scoreBasedNodes.add(DENOMINATOR_EXCLUSIONS);
        } else if (COHORT.equalsIgnoreCase(scoringType)) {
            scoreBasedNodes.add(INITIAL_POPULATIONS);
        }
        return scoreBasedNodes;
    }

    /**
     * This method will add a 'populations' node to the XML. It assumes that the
     * 'measureDetails' node is already present in the XML Document and tries to
     * add the 'populations' node after the 'measureDetails' node.
     *
     * @return the node
     * @throws XPathExpressionException the x path expression exception
     */
    private Node addPopulationsNode() throws XPathExpressionException {
        Element populationsElem = originalDoc.createElement("populations");
        populationsElem.setAttribute(DISPLAY_NAME,
                XmlProcessor.constantsMap.get("populations"));
        Node measureNode = findNode(originalDoc,
                "/measure");
        Element measureElement = (Element) measureNode;
        measureElement.insertBefore(populationsElem,
                measureNode.getFirstChild());
        return populationsElem;
    }

    /**
     * This method creates blank nodes for Elements like 'child elements of
     * populations node, measureObservations node and stratifications node' The
     * method will return the newly created Element. The called needs to add
     * this Element to the appropriate parent node in the Document.
     *
     * @param nodeName          the node name
     * @param clauseDisplayName the clause display name
     * @return the element
     */
    private Element createTemplateNode(String nodeName, String clauseDisplayName) {
        Element mainChildElem = originalDoc.createElement(nodeName);
        mainChildElem.setAttribute(DISPLAY_NAME, constantsMap.get(nodeName));

        Element clauseChildElem = originalDoc.createElement("clause");
        String dispName = constantsMap.get(clauseDisplayName);
        clauseChildElem.setAttribute(DISPLAY_NAME, dispName + " 1");
        clauseChildElem.setAttribute(TYPE, toCamelCase(dispName));
        clauseChildElem.setAttribute(UUID_STRING, UUIDUtilClient.uuid());
        mainChildElem.appendChild(clauseChildElem);

        mainChildElem.appendChild(clauseChildElem);

        return mainChildElem;
    }

    /**
     * Removes the from parent.
     *
     * @param node the node
     */
    public void removeFromParent(Node node) {
        if (node != null) {
            Node parentNode = node.getParentNode();
            parentNode.removeChild(node);
        }
    }

    /**
     * Find node.
     *
     * @param document    the document
     * @param xPathString the x path string
     * @return the node
     * @throws XPathExpressionException the x path expression exception
     */
    public Node findNode(Document document, String xPathString) throws XPathExpressionException {
        javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
        Node node = (Node) xPath.evaluate(xPathString, document.getDocumentElement(), XPathConstants.NODE);
        return node;
    }

    /**
     * Find node list.
     *
     * @param document    the document
     * @param xPathString the x path string
     * @return the node list
     * @throws XPathExpressionException the x path expression exception
     */
    public NodeList findNodeList(Document document, String xPathString) throws XPathExpressionException {
        javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xPath.compile(xPathString);
        return (NodeList) expr.evaluate(document, XPathConstants.NODESET);
    }

    /**
     * Gets the node count.
     *
     * @param document    the document
     * @param xPathString the x path string
     * @return the node count
     * @throws XPathExpressionException the x path expression exception
     */
    public int getNodeCount(Document document, String xPathString) throws XPathExpressionException {
        javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xPath.compile(xPathString);
        return ((Double) expr.evaluate(document, XPathConstants.NUMBER)).intValue();
    }

    /**
     * Gets the original doc.
     *
     * @return the originalDoc
     */
    public Document getOriginalDoc() {
        return originalDoc;
    }

    /**
     * Sets the original doc.
     *
     * @param originalDoc the originalDoc to set
     */
    public void setOriginalDoc(Document originalDoc) {
        this.originalDoc = originalDoc;
    }

    /**
     * To camel case.
     *
     * @param name the name
     * @return the string
     */
    private static String toCamelCase(String name) {
        String nameNew = name.toLowerCase(Locale.US);
        String[] parts = nameNew.split(" ");
        StringBuffer camelCaseString = new StringBuffer(parts[0].substring(0, 1).toLowerCase()
                + parts[0].substring(1));
        for (int i = 1; i < parts.length; i++) {
            camelCaseString.append(toProperCase(parts[i]));
        }
        return camelCaseString.toString();
    }

    /**
     * To proper case.
     *
     * @param str the s
     * @return the string
     */
    private static String toProperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Transform.
     *
     * @param node the node
     * @return the string
     */
    public String transform(Node node) {
        return transform(node, false);
    }

    /**
     * Convert xml document to string.
     *
     * @param node        the node
     * @param isFormatted
     * @return the string
     */
    public String transform(Node node, boolean isFormatted) {
        LOG.debug("In transform() method");
        Transformer tf;
        Writer out = null;
        try {
            TransformerFactory transformerFactory = XMLUtility.getInstance().buildTransformerFactory();
            tf = transformerFactory.newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            if (isFormatted) {
                tf.setOutputProperty(OutputKeys.INDENT, "yes");
            }

            tf.setOutputProperty(OutputKeys.STANDALONE, "yes");
            out = new StringWriter();
            tf.transform(new DOMSource(node), new StreamResult(out));
        } catch (TransformerFactoryConfigurationError | TransformerException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.debug("Document object to ByteArray transformation complete");
        return out.toString();
    }

    /**
     * Takes the spaces out of the clauseName (attribute displayName in XML).
     *
     * @param xmlString   the xml string
     * @param xPathString the x path string
     * @return the string
     * @throws XPathExpressionException the x path expression exception
     */
    public static String normalizeNodeForSpaces(String xmlString, String xPathString) throws XPathExpressionException {
        XmlProcessor xmlProcessor = new XmlProcessor(xmlString);
        Node node = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xPathString);
        if (node != null) {
            String oldValue = node.getNodeValue();
            // Normalize by changing multiple spaces into one space
            String newValue = oldValue.replaceAll("( )+", " ");
            node.setNodeValue(newValue);
        }
        return xmlProcessor.transform(xmlProcessor.getOriginalDoc());
    }

    /**
     * Utility method to go through the Node and its children (upto nth level)
     * and remove all TEXT nodes.
     *
     * @param node the node
     */
    public static void clean(Node node) {
        NodeList childNodes = node.getChildNodes();

        for (int n = childNodes.getLength() - 1; n >= 0; n--) {
            Node child = childNodes.item(n);
            short nodeType = child.getNodeType();

            if (nodeType == Node.ELEMENT_NODE) {
                clean(child);
            } else if (nodeType == Node.TEXT_NODE) {
                String trimmedNodeVal = child.getNodeValue().trim();
                if (trimmedNodeVal.length() == 0) {
                    node.removeChild(child);
                } else {
                    child.setNodeValue(trimmedNodeVal);
                }
            } else if (nodeType == Node.COMMENT_NODE) {
                node.removeChild(child);
            }
        }
    }

    public void updateCQLLibraryName(String libraryName) throws XPathExpressionException {

        Node cqlLibraryNode = findNode(originalDoc, "//cqlLookUp/library");

        if (cqlLibraryNode != null) {
            libraryName = cleanString(libraryName);

            cqlLibraryNode.setTextContent(libraryName);
        }
    }

    /**
     * This method will take a String and remove all non-alphabet/non-numeric characters
     * except underscore ("_") characters.
     *
     * @param originalString
     * @return cleanedString
     */
    private String cleanString(String originalString) {
        originalString = originalString.replaceAll(" ", "");

        String cleanedString = "";
        for (int i = 0; i < originalString.length(); i++) {
            char c = originalString.charAt(i);
            int intc = (int) c;

            if (c == '_' || (intc >= 48 && intc <= 57) || (intc >= 65 && intc <= 90) || (intc >= 97 && intc <= 122)) {

                if (!(cleanedString.isEmpty() && Character.isDigit(c))) {
                    cleanedString = cleanedString + "" + c;
                }

            }

        }

        return cleanedString;
    }

    /**
     * Check for default parameter MeasurementPeriod.
     *
     * @return the list
     */
    public List<String> checkForDefaultParameters() {
        List<String> missingDefaultParameterList = new ArrayList<String>();

        if (originalDoc != null) {
            try {
                // Measurement Period
                Node measurementPeriodNode = this.findNode(originalDoc,
                        "/measure/cqlLookUp//parameter[@name='"
                                + PARAMETER_MEASUREMENT_PERIOD + "']");
                if (measurementPeriodNode == null) {
                    missingDefaultParameterList.add(PARAMETER_MEASUREMENT_PERIOD);
                }

            } catch (XPathExpressionException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return missingDefaultParameterList;
    }

    public void removeUnusedDefaultCodes(List<String> usedCodeList) {
        try {
            NodeList nodeList = findNodeList(getOriginalDoc(), XPATH_FOR_CODES);
            List<String> codeSystemOIDsToRemove = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node currentNode = nodeList.item(i);
                if (null != currentNode.getAttributes().getNamedItem(ATTRIBUTE_READ_ONLY)) {
                    String codeOID = currentNode.getAttributes().getNamedItem(ATTRIBUTE_CODE_OID).getNodeValue();
                    Boolean readOnly = Boolean.parseBoolean(currentNode.getAttributes().getNamedItem(ATTRIBUTE_READ_ONLY).getNodeValue());

                    if (readOnly && (codeOID.equals(ConstantMessages.BIRTHDATE_OID) || codeOID.equals(ConstantMessages.DEAD_OID))) {
                        String codeName = currentNode.getAttributes().getNamedItem(ATTRIBUTE_CODE_NAME).getNodeValue();
                        if (!usedCodeList.contains(codeName)) {
                            String codeSystemOID = currentNode.getAttributes().getNamedItem(ATTRIBUTE_CODE_SYSTEM_OID).getNodeValue();
                            codeSystemOIDsToRemove.add(codeSystemOID);
                            Node parentNode = currentNode.getParentNode();
                            parentNode.removeChild(currentNode);
                        }
                    }
                }
            }
            removeUnusedCodeSystems(codeSystemOIDsToRemove);
        } catch (XPathExpressionException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * When drafting or cloning a measure or cql library, the version attribute should become an empty string in the valueset tag.
     * <p>
     * It is an empty string because MAT still needs to support the version attribute because valuesets that had a version in an already
     * versioned measure should retain those values.
     */
    public void clearValuesetVersionAttribute() {
        try {
            NodeList nodeList = findNodeList(getOriginalDoc(), XPATH_FOR_VALUESETS);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node valuesetNode = nodeList.item(i);
                Node version = valuesetNode.getAttributes().getNamedItem("version");
                if (version != null) {
                    version.setNodeValue("");
                }
            }
        } catch (XPathExpressionException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void removeUnusedCodeSystems(List<String> codeSystemOIDsToRemove) {
        try {
            NodeList nodeList = findNodeList(getOriginalDoc(), XPATH_FOR_CODES);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node currentNode = nodeList.item(i);
                String codeSystemOID = currentNode.getAttributes().getNamedItem(ATTRIBUTE_CODE_SYSTEM_OID).getNodeValue();
                if (codeSystemOIDsToRemove.contains(codeSystemOID)) {
                    codeSystemOIDsToRemove.remove(codeSystemOIDsToRemove.indexOf(codeSystemOID));
                }
            }

            for (String codeSystemOID : codeSystemOIDsToRemove) {
                String codeSystemXPathString = "[@codeSystemOID =\"" + codeSystemOID + "\"]";
                Node codeSystemNode = findNode(getOriginalDoc(), codeSystemXPathString);
                Node parentNode = codeSystemNode.getParentNode();
                parentNode.removeChild(codeSystemNode);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
