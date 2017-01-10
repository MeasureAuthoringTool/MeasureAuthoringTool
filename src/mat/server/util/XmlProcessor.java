package mat.server.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.model.QualityDataModelWrapper;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.shared.UUIDUtilClient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class XmlProcessor.
 */
public class XmlProcessor {
	
	/**
	 * The Constant COHORT.
	 */
	private static final String COHORT = "COHORT";
	
	/** The Constant MEASUREMENT_PERIOD_OID. */
	//private static final String MEASUREMENT_PERIOD_OID = "2.16.840.1.113883.3.67.1.101.1.53";
	
	/** The Constant PATIENT_CHARACTERISTIC_BIRTH_DATE_OID. */
	private static final String PATIENT_CHARACTERISTIC_BIRTH_DATE_OID = "21112-8";
	
	/** The Constant PATIENT_CHARACTERISTIC_EXPIRED_OID. */
	private static final String PATIENT_CHARACTERISTIC_EXPIRED_OID = "419099009";
	
	/** The Constant XPATH_POPULATIONS. */
	private static final String XPATH_POPULATIONS = "/measure/populations";
	
	/** The Constant XPATH_NUMERATORS. */
	private static final String XPATH_NUMERATORS = "/measure/populations/numerators";
	
	/** The Constant XPATH_DENOMINATOR. */
	private static final String XPATH_DENOMINATOR = "/measure/populations/denominators";
	
	/** The Constant XPATH_NUMERATOR_EXCLUSIONS. */
	private static final String XPATH_NUMERATOR_EXCLUSIONS = "/measure/populations/numeratorExclusions";
	
	/** The Constant XPATH_MEASURE_OBSERVATIONS. */
	private static final String XPATH_MEASURE_OBSERVATIONS = "/measure/measureObservations";
	
	/** The Constant XPATH_MEASURE_STRATIFICATIONS. */
	private static final String XPATH_MEASURE_STRATIFICATIONS = "/measure/strata";
	
	/** The Constant XPATH_MEASURE_SD_ELEMENTS. */
	private static final String XPATH_MEASURE_SD_ELEMENTS = "/measure/supplementalDataElements";
	
	/** The Constant XPATH_MEASURE_RAV_ELEMENTS. */
	private static final String XPATH_MEASURE_RAV_ELEMENTS = "/measure/riskAdjustmentVariables";
	
	/** The Constant XPATH_MEASURE_ELEMENT_LOOKUP. */
	private static final String XPATH_MEASURE_ELEMENT_LOOKUP = "/measure/elementLookUp";
	
	/** The Constant XPATH_MEASURE_SUBTREE_LOOKUP. */
	private static final String XPATH_MEASURE_SUBTREE_LOOKUP = "/measure/subTreeLookUp";
	
	/** The Constant XPATH_CQL_LOOKUP. */
	private static final String XPATH_CQL_LOOKUP = "/measure/cqlLookUp";
	
	/** The Constant XPATH_DETAILS_ITEM_COUNT. */
	//private static final String XPATH_DETAILS_ITEM_COUNT = "/measure/measureDetails/itemCount";
	
	/** The Constant XPATH_DTLS_COMPONENT_MEASURE. */
	private static final String XPATH_DTLS_COMPONENT_MEASURE = "/measure/measureDetails/componentMeasures";
	
	/** The Constant XPATH_DETAILS_EMEASUREID. */
	private static final String XPATH_DETAILS_EMEASUREID = "/measure/measureDetails/emeasureid";
	
	/** The Constant XPATH_DETAILS_FINALIZEDDATE. */
	private static final String XPATH_DETAILS_FINALIZEDDATE = "/measure/measureDetails/finalizedDate";
	
	/** The Constant XPATH_MEASURE_MEASURE_DETAILS_GUID. */
	private static final String XPATH_MEASURE_MEASURE_DETAILS_GUID = "/measure/measureDetails/guid";
	
	/** The Constant XPATH_MEASURE_MEASURE_DETAILS_MEASURETYPE. */
	
	private static final String XPATH_DETAILS_MEASURETYPE = "/measure/measureDetails/types";
	
	/** The Constant XPATH_MEASURE_MEASURE_DETAILS_SCORING. */
	private static final String XPATH_DETAILS_SCORING = "/measure/measureDetails/scoring";
	
	/** The Constant XPATH_MEASURE_ELEMENT_LOOKUP_QDM. */
	private static final String XPATH_MEASURE_ELEMENT_LOOKUP_QDM = "/measure/elementLookUp/qdm";
	
	/** The Constant XPATH_MEASURE_POPULATIONS. */
	private static final String XPATH_MEASURE_POPULATIONS = "/measure/populations/measurePopulations";
	
	/** The Constant XPATH_MEASURE_POPULATION_EXCLUSIONS. */
	private static final String XPATH_MEASURE_POPULATION_EXCLUSIONS = "/measure/populations/measurePopulationExclusions";
	
	/** The Constant XPATH_DENOMINATOR_EXCEPTIONS. */
	private static final String XPATH_DENOMINATOR_EXCEPTIONS = "/measure/populations/denominatorExceptions";
	
	/** The Constant XPATH_MEASURE_DETAILS_DENOMINATOR. */
	private static final String XPATH_MEASURE_DETAILS_DENOMINATOR = "/measure/measureDetails/denominatorDescription";
	
	/** The Constant XPATH_MEASURE_DETAILS_DENOMINATOR_EXCEPTIONS. */
	private static final String XPATH_MEASURE_DETAILS_DENOMINATOR_EXCEPTIONS = "/measure/measureDetails/denominatorExceptionsDescription";
	
	/** The Constant XPATH_MEASURE_DETAILS_DENOMINATOR_EXCLUSIONS. */
	private static final String XPATH_MEASURE_DETAILS_DENOMINATOR_EXCLUSIONS = "/measure/measureDetails/denominatorExclusionsDescription";
	
	/** The Constant XPATH_MEASURE_DETAILS_MEASURE_POPULATION_EXCLUSIONS. */
	private static final String XPATH_MEASURE_DETAILS_MEASURE_POPULATION_EXCLUSIONS = "/measure/measureDetails/measurePopulationExclusionsDescription";
	
	/** The Constant XPATH_MEASURE_DETAILS_MEASURE_POPULATIONS. */
	private static final String XPATH_MEASURE_DETAILS_MEASURE_POPULATIONS = "/measure/measureDetails/measurePopulationDescription";
	
	/** The Constant XPATH_MEASURE_DETAILS_MEASURE_OBSERVATIONS. */
	private static final String XPATH_MEASURE_DETAILS_MEASURE_OBSERVATIONS = "/measure/measureDetails/measureObservationsDescription";
	
	/** The Constant XPATH_MEASURE_DETAILS_NUMERATOR. */
	private static final String XPATH_MEASURE_DETAILS_NUMERATOR = "/measure/measureDetails/numeratorDescription";
	
	/** The Constant XPATH_MEASURE_DETAILS_NUM_EXCLUSIONS. */
	private static final String XPATH_MEASURE_DETAILS_NUM_EXCLUSIONS = "/measure/measureDetails/numeratorExclusionsDescription";
	
	/** The Constant XPATH_DENOMINATOR_EXCLUSIONS. */
	private static final String XPATH_DENOMINATOR_EXCLUSIONS = "/measure/populations/denominatorExclusions";
	
	/** The Constant RATIO. */
	private static final String RATIO = "RATIO";
	
	/** The Constant PROPOR. */
	private static final String PROPOR = "PROPOR";
	
	/** The Constant SCORING_TYPE_CONTVAR. */
	private static final String SCORING_TYPE_CONTVAR = "CONTVAR";
	
	/** The Constant NUMERATOR_EXCLUSIONS. */
	private static final String NUMERATOR_EXCLUSIONS = "numeratorExclusions";
	
	/** The Constant DENOMINATOR_EXCEPTIONS. */
	private static final String DENOMINATOR_EXCEPTIONS = "denominatorExceptions";
	
	/** The Constant DENOMINATOR_EXCLUSIONS. */
	private static final String DENOMINATOR_EXCLUSIONS = "denominatorExclusions";
	
	/** The Constant DENOMINATORS. */
	private static final String DENOMINATORS = "denominators";
	
	/** The Constant NUMERATORS. */
	private static final String NUMERATORS = "numerators";
	
	/** The Constant MEASURE_POPULATIONS. */
	private static final String MEASURE_POPULATIONS = "measurePopulations";
	
	/** The Constant MEASURE_POPULATION_EXCLUSIONS. */
	private static final String MEASURE_POPULATION_EXCLUSIONS = "measurePopulationExclusions";
	
	/** The Constant INITIAL_PATIENT_POPULATIONS. */
	private static final String INITIAL_POPULATIONS = "initialPopulations";
	
	/** The Constant XPATH_MEASURE_CLAUSE. */
	//	public static final String XPATH_MEASURE_CLAUSE = "/measure/populations/*/clause | /measure/*/clause[@type !='stratum']";
	public static final String XPATH_MEASURE_CLAUSE = "/measure/populations/*/clause | /measure/*/clause | /measure/strata/stratification | /measure/strata/Stratification";
	
	/** The Constant XPATH_MEASURE_GROUPING. */
	public static final String XPATH_MEASURE_GROUPING = "/measure/measureGrouping";
	
	/** The Constant XPATH_MEASURE_GROUPING_GROUP. */
	public static final String XPATH_MEASURE_GROUPING_GROUP = "/measure/measureGrouping/group";
	
	/** The Constant XPATH_GROUP_SEQ_START. */
	public static final String XPATH_GROUP_SEQ_START = "/measure/measureGrouping/group[@sequence = '";
	
	/** The Constant XPATH_GROUP_SEQ_END. */
	public static final String XPATH_GROUP_SEQ_END = "' ] ";
	
	/** The Constant XPATH_FIND_GROUP_CLAUSE. */
	public static final String XPATH_FIND_GROUP_CLAUSE = "/measure/measureGrouping/group[packageClause[";
	
	/** The Constant XPATH_OLD_ALL_RELATIONALOP_SBOD. */
	public static final String XPATH_OLD_ALL_RELATIONALOP_SBOD = "/measure//*/relationalOp[@type='SBOD']";
	
	/** The Constant XPATH_OLD_ALL_RELATIONALOP_EBOD. */
	public static final String XPATH_OLD_ALL_RELATIONALOP_EBOD = "/measure//*/relationalOp[@type='EBOD']";
	
	/** The Constant XPATH_STRATA. */
	private static final String XPATH_STRATA = "/measure/strata";
	
	/** The Constant STRATIFICATION. */
	public static final String  STRATIFICATION = "stratification";
	
	/** The Constant STRATIFICATION_DISPLAYNAME. */
	private static final String  STRATIFICATION_DISPLAYNAME = "Stratification 1";
	
	/** The Constant MEASURE_OBSERVATION. */
	private static final String MEASURE_OBSERVATION = "measureObservations";
	
	/** The Constant AND. */
	private static final String AND = "and";
	
	/** The Constant OR. */
	private static final String OR_STRING = "or";
	
	/** The Constant UUID. */
	private static final String UUID_STRING = "uuid";
	
	/** The Constant DISPLAY_NAME. */
	private static final String DISPLAY_NAME = "displayName";
	
	/** The Constant TYPE. */
	private static final String TYPE = "type";
	
	
	/** The Constant PATIENT. */
	private static final String PATIENT = " Patient ";
	
	/** The constants map. */
	private static Map<String, String> constantsMap = new HashMap<String, String>();
	/** The constants map. */
	private static Map<String, String> topNodeOperatorMap = new HashMap<String, String>();
	
	/** The Constant logger. */
	private static final Log LOG = LogFactory.getLog(XmlProcessor.class);
	
	/** The original xml. */
	private String originalXml;
	
	/** The doc builder. */
	private DocumentBuilder docBuilder;
	
	/** The original doc. */
	private Document originalDoc;
	
	/** The Constant PARAMETER_MEASUREMENT_PERIOD. */
	private static final String PARAMETER_MEASUREMENT_PERIOD = "Measurement Period";
	
	/** The Constant POPULATIONS. */
	private static final String[] POPULATIONS = {
		INITIAL_POPULATIONS, NUMERATORS, NUMERATOR_EXCLUSIONS, DENOMINATORS,
		DENOMINATOR_EXCLUSIONS, DENOMINATOR_EXCEPTIONS, MEASURE_POPULATIONS,
		MEASURE_POPULATION_EXCLUSIONS };
	static {
		constantsMap.put("populations", "Populations");
		constantsMap.put(MEASURE_OBSERVATION, "Measure Observations");
		constantsMap.put("strata", "Stratification");
		constantsMap.put(MEASURE_OBSERVATION, "Measure Observations");
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
		//commented for MAT-4426 in sprint 44
		//topNodeOperatorMap.put(MEASURE_OBSERVATION, AND);
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
	 * @param originalXml
	 *            the original xml
	 */
	public XmlProcessor(String originalXml) {
		LOG.info("In XmlProcessor() constructor");
		this.originalXml = originalXml;
		try {
			docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputSource oldXmlstream = new InputSource(new StringReader(
					originalXml));
			originalDoc = docBuilder.parse(oldXmlstream);
			LOG.info("Document Object created successfully for the XML String");
		} catch (Exception e) {
			LOG.info("Exception thrown on XmlProcessor() costructor");
			caughtExceptions(e);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Instantiates a new xml processor for HQMFMeasureXml.
	 *
	 * @param file the file
	 */
	public XmlProcessor(File file) {
		try {
			docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			originalDoc = docBuilder.parse(file);
			LOG.info("Document Object created successfully for the XML String");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			LOG.info("Exception thrown on XmlProcessor() costructor");
			caughtExceptions(e);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method to insert new Node under existing parent Node.
	 * 
	 * @param newElement
	 *            the new element
	 * @param nodeName
	 *            the node name
	 * @param parentNode
	 *            the parent node
	 * @return the string
	 * @throws SAXException
	 *             the sAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String appendNode(String newElement, String nodeName,
			String parentNode) throws SAXException, IOException {
		LOG.info("In appendNode method with newElement ::: ");
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
				
				LOG.info("Document Object created successfully for the XML String.");
			} else {
				LOG.info("parentNode:" + parentNode
						+ " not found. method appendNode exiting prematurely.");
			}
		} catch (XPathExpressionException e) {
			LOG.info("Exception thrown on appendNode method");
			caughtExceptions(e);
			e.printStackTrace();
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
	 * @param newXml
	 *            the new xml
	 * @param nodeName
	 *            the node name
	 * @param parentName
	 *            // this is optional, can be null or empty. if parentName not
	 *            null, the oldNode to be replaced will be retrieved based on
	 *            the parent Node, this is done to make sure we are replacing
	 *            the correct node.
	 * @return the string
	 */
	public String replaceNode(String newXml, String nodeName, String parentName) {
		try {
			LOG.info("In replaceNode() method");
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
					LOG.info("Replaced old Child Node with new Child Node "
							+ nodeName);
				} else { // if the Original Document doesnt have the Node, then
					// insert the new Node under the first child
					Node importNode = originalDoc.importNode(newNode, true);
					originalDoc.getFirstChild().appendChild(importNode);
					LOG.info("Inserted new Child Node" + nodeName);
				}
				return transform(originalDoc);
			}
			
		} catch (Exception e) {
			LOG.info("Exception thrown on replaceNode() method");
			caughtExceptions(e);
			e.printStackTrace();
		}
		return originalXml; // not replaced returnig the original Xml;
	}
	
	/**
	 * Update node text.
	 * 
	 * @param nodeName
	 *            the node name
	 * @param nodeValue
	 *            the node value
	 * @return the string
	 */
	public String updateNodeText(String nodeName, String nodeValue) {
		try {
			LOG.info("In updateNodeText() method");
			InputSource xmlStream = new InputSource(new StringReader(
					originalXml));
			Document doc = docBuilder.parse(xmlStream);
			doc.getElementsByTagName(nodeName).item(0)
			.setTextContent(nodeValue);
			LOG.info("update NoedText");
			return transform(doc);
		} catch (Exception e) {
			LOG.info("Exception thrown on updateNodeText() method");
			caughtExceptions(e);
			e.printStackTrace();
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
	 * @param originalXml
	 *            the new original xml
	 */
	public void setOriginalXml(String originalXml) {
		this.originalXml = originalXml;
	}
	
	/**
	 * Gets the xml by tag name.
	 * 
	 * @param tagName
	 *            the tag name
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
	 * @param parentTagName
	 *            the parent tag name
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
	 * @param excp
	 *            the exception that was caught
	 */
	private void caughtExceptions(Exception excp) {
		if (excp instanceof ParserConfigurationException) {
			LOG.info("Document Builder Object creation failed"
					+ excp.getStackTrace());
		} else if (excp instanceof SAXException) {
			LOG.info("Xml parsing failed:" + excp.getStackTrace());
		} else if (excp instanceof IOException) {
			LOG.info("Conversion of String XML to InputSource failed"
					+ excp.getStackTrace());
		} else {
			LOG.info("Generic Exception: " + excp.getStackTrace());
		}
	}
	
	/**
	 * Check for scoring type.
	 * 
	 * @return the string
	 */
	public String checkForScoringType(String releaseVersion) {
		if (originalDoc == null) {
			return "";
		}
		// Get the scoring type from originalDoc
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			String scoringType = (String) xPath.evaluate(
					"/measure/measureDetails/scoring/@id",
					originalDoc.getDocumentElement(), XPathConstants.STRING);
			LOG.info("scoringType:" + scoringType);
			
			removeNodesBasedOnScoring(scoringType);
			createNewNodesBasedOnScoring(scoringType,releaseVersion);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return transform(originalDoc);
	}
	/**
	 * This method looks at the Scoring Type for a measure and removes nodes
	 * based on the value of Scoring Type.
	 * 
	 * @param scoringType
	 *            the scoring type
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	public void removeNodesBasedOnScoring(String scoringType)
			throws XPathExpressionException {
		List<String> xPathList = new ArrayList<String>();
		
		if (RATIO.equalsIgnoreCase(scoringType)) {
			// Denominator Exceptions, Measure Populations
			xPathList.add(XPATH_DENOMINATOR_EXCEPTIONS);
			xPathList.add(XPATH_MEASURE_POPULATIONS);
			xPathList.add(XPATH_MEASURE_POPULATION_EXCLUSIONS);
			
			xPathList.add(XPATH_MEASURE_DETAILS_MEASURE_POPULATIONS);
			xPathList.add(XPATH_MEASURE_DETAILS_MEASURE_POPULATION_EXCLUSIONS);
			xPathList.add(XPATH_MEASURE_DETAILS_DENOMINATOR_EXCEPTIONS);
			/*xPathList.add(XPATH_MEASURE_OBSERVATIONS);*/
		} else if (PROPOR.equalsIgnoreCase(scoringType)) {
			// Measure Population Exlusions, Measure Populations
			//xPathList.add(XPATH_NUMERATOR_EXCLUSIONS);
			xPathList.add(XPATH_MEASURE_POPULATIONS);
			xPathList.add(XPATH_MEASURE_POPULATION_EXCLUSIONS);
			xPathList.add(XPATH_MEASURE_OBSERVATIONS);
			
			xPathList.add(XPATH_MEASURE_DETAILS_MEASURE_POPULATIONS);
			xPathList.add(XPATH_MEASURE_DETAILS_MEASURE_POPULATION_EXCLUSIONS);
			xPathList.add(XPATH_MEASURE_DETAILS_MEASURE_OBSERVATIONS);
			
		} else if (SCORING_TYPE_CONTVAR.equalsIgnoreCase(scoringType)) {
			// Numerators,Numerator Exclusions, Denominators, Denominator
			// Exceptions, Denominator Exclusions
			xPathList.add(XPATH_NUMERATORS);
			xPathList.add(XPATH_NUMERATOR_EXCLUSIONS);
			xPathList.add(XPATH_DENOMINATOR);
			xPathList.add(XPATH_DENOMINATOR_EXCEPTIONS);
			xPathList.add(XPATH_DENOMINATOR_EXCLUSIONS);
			
			xPathList.add(XPATH_MEASURE_DETAILS_NUMERATOR);
			xPathList.add(XPATH_MEASURE_DETAILS_NUM_EXCLUSIONS);
			xPathList.add(XPATH_MEASURE_DETAILS_DENOMINATOR);
			xPathList.add(XPATH_MEASURE_DETAILS_DENOMINATOR_EXCEPTIONS);
			xPathList.add(XPATH_MEASURE_DETAILS_DENOMINATOR_EXCLUSIONS);
			
		} else if (COHORT.equalsIgnoreCase(scoringType)) {
			xPathList.add(XPATH_NUMERATORS);
			xPathList.add(XPATH_NUMERATOR_EXCLUSIONS);
			xPathList.add(XPATH_DENOMINATOR);
			xPathList.add(XPATH_DENOMINATOR_EXCEPTIONS);
			xPathList.add(XPATH_DENOMINATOR_EXCLUSIONS);
			xPathList.add(XPATH_MEASURE_POPULATIONS);
			xPathList.add(XPATH_MEASURE_POPULATION_EXCLUSIONS);
			xPathList.add(XPATH_MEASURE_OBSERVATIONS);
			
			xPathList.add(XPATH_MEASURE_DETAILS_NUMERATOR);
			xPathList.add(XPATH_MEASURE_DETAILS_NUM_EXCLUSIONS);
			xPathList.add(XPATH_MEASURE_DETAILS_DENOMINATOR);
			xPathList.add(XPATH_MEASURE_DETAILS_DENOMINATOR_EXCEPTIONS);
			xPathList.add(XPATH_MEASURE_DETAILS_DENOMINATOR_EXCLUSIONS);
			xPathList.add(XPATH_MEASURE_DETAILS_MEASURE_POPULATIONS);
			xPathList.add(XPATH_MEASURE_DETAILS_MEASURE_POPULATION_EXCLUSIONS);
			xPathList.add(XPATH_MEASURE_DETAILS_MEASURE_OBSERVATIONS);
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
		String xpathOldMSRDetailsPatientPop = "/measure/measureDetails/initialPatientPopDescription";
		String xpathOldGroupingPackageClause = "/measure/measureGrouping/*/packageClause";
		
		if (document == null) {
			return;
		}
		
		//replace the <initialPatientPopDescription> tag in <measureDetails> with <initialPopDescription>
		Node initialPatientPopDescription = findNode(document, xpathOldMSRDetailsPatientPop);
		if (initialPatientPopDescription != null) {
			document.renameNode(initialPatientPopDescription, "", "initialPopDescription");
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
			Node childNode =  nodesRelationalOpsSBOD.item(i);
			String relationalOpDisplayName = childNode.getAttributes().getNamedItem(DISPLAY_NAME).getNodeValue();
			relationalOpDisplayName = relationalOpDisplayName.replace(startsBeforeOrDuring, startBeforeEnd);
			childNode.getAttributes().getNamedItem(DISPLAY_NAME).setNodeValue(relationalOpDisplayName);
			childNode.getAttributes().getNamedItem(TYPE).setNodeValue(sBE);
		}
		//replace relationalOp attribute values for displayName and type from EBOD to EBE
		NodeList nodesRelationalOpsEBOD = (NodeList) xPath.evaluate(XPATH_OLD_ALL_RELATIONALOP_EBOD,
				originalDoc.getDocumentElement(), XPathConstants.NODESET);
		for (int i = 0; i < nodesRelationalOpsEBOD.getLength(); i++) {
			Node childNode =  nodesRelationalOpsEBOD.item(i);
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
	 * @param scoringType
	 *            the scoring type
	 * @param releaseVersion 
	 * @throws XPathExpressionException
	 *             the x path expression exception
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
		Node measureObservationsNode = findNode(originalDoc,
				XPATH_MEASURE_OBSERVATIONS);
		if ((SCORING_TYPE_CONTVAR.equals(scoringType)
				|| RATIO.equals(scoringType)) && (measureObservationsNode == null)) {
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
		if (measureStratificationsNode == null) {
			String stratificationsNodeName = "strata";
			String clauseDisplayName = "Stratum";
			Element stratificationElement = createTemplateNode(
					stratificationsNodeName, clauseDisplayName);
			
			NodeList childs  = stratificationElement.getChildNodes();
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
		}
		// Create supplementalDataElements node
		releaseVersion = releaseVersion.replaceAll("Draft ", "").trim();
		if(releaseVersion.startsWith("v")){
			releaseVersion = releaseVersion.substring(1);
		}
		createSupplementalDataElementNode(measureStratificationsNode,releaseVersion);
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
	 * @param childNodesList array list of nodes to be ordered
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
		
		if (findNode(originalDoc, XPATH_DTLS_COMPONENT_MEASURE) == null) {
			Element componentMeasureElement = originalDoc
					.createElement("componentMeasures");
			if (findNode(originalDoc, XPATH_DETAILS_MEASURETYPE) == null) {
				Node scoringElement = findNode(originalDoc, XPATH_DETAILS_SCORING);
				if (scoringElement != null) {
					((Element) scoringElement.getParentNode())
					.insertBefore(componentMeasureElement,
							scoringElement.getNextSibling());
				}
			} else {
				Node measureTypeElement = findNode(originalDoc, XPATH_DETAILS_MEASURETYPE);
				((Element) measureTypeElement.getParentNode())
				.insertBefore(componentMeasureElement,
						measureTypeElement.getNextSibling());
			}
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
		if (findNode(originalDoc, XPATH_CQL_LOOKUP) == null) {
			Element cqlLookUpElement = originalDoc
					.createElement("cqlLookUp");
			((Element) supplementaDataElementsElement.getParentNode())
			.appendChild(cqlLookUpElement
					);
			
			createCQLLookUpElements(releaseVersion);
		}
		System.out.println("Original Doc: "+originalDoc.toString());
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
		if (SCORING_TYPE_CONTVAR.equals(scoringType)) {
			scoreBasedNodes.add(INITIAL_POPULATIONS);
			scoreBasedNodes.add(MEASURE_POPULATIONS);
			scoreBasedNodes.add(MEASURE_POPULATION_EXCLUSIONS);
		} else if (PROPOR.equals(scoringType)) {
			scoreBasedNodes.add(INITIAL_POPULATIONS);
			scoreBasedNodes.add(NUMERATORS);
			scoreBasedNodes.add(NUMERATOR_EXCLUSIONS);
			scoreBasedNodes.add(DENOMINATORS);
			scoreBasedNodes.add(DENOMINATOR_EXCLUSIONS);
			scoreBasedNodes.add(DENOMINATOR_EXCEPTIONS);
		} else if (RATIO.equals(scoringType)) {
			scoreBasedNodes.add(INITIAL_POPULATIONS);
			scoreBasedNodes.add(NUMERATORS);
			scoreBasedNodes.add(NUMERATOR_EXCLUSIONS);
			scoreBasedNodes.add(DENOMINATORS);
			scoreBasedNodes.add(DENOMINATOR_EXCLUSIONS);
		} else if (COHORT.equals(scoringType)) {
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
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	private Node addPopulationsNode() throws XPathExpressionException {
		Element populationsElem = originalDoc.createElement("populations");
		populationsElem.setAttribute(DISPLAY_NAME,
				XmlProcessor.constantsMap.get("populations"));
		Node measureDetailsNode = findNode(originalDoc,
				"/measure/measureDetails");
		Node measureNode = measureDetailsNode.getParentNode();
		Element measureElement = (Element) measureNode;
		measureElement.insertBefore(populationsElem,
				measureDetailsNode.getNextSibling());
		return populationsElem;
	}
	
	/**
	 * Creates the emeasure id node.
	 *
	 * @param emeasureId the emeasure id
	 * @throws XPathExpressionException the x path expression exception
	 * @throws DOMException the dOM exception
	 */
	public void createEmeasureIdNode(int emeasureId) throws XPathExpressionException, DOMException{
		
		if (findNode(originalDoc, XPATH_DETAILS_EMEASUREID) == null) {
			Element emeasureIDElement = originalDoc
					.createElement("emeasureid");
			emeasureIDElement.appendChild(originalDoc.createTextNode(Integer.toString(emeasureId)));
			if (findNode(originalDoc, XPATH_DETAILS_FINALIZEDDATE) == null) {
				Node guidElement = findNode(originalDoc, XPATH_MEASURE_MEASURE_DETAILS_GUID);
				((Element) guidElement.getParentNode())
				.insertBefore(emeasureIDElement,
						guidElement);
			} else {
				Node finalizedDateElement = findNode(originalDoc, XPATH_DETAILS_FINALIZEDDATE);
				((Element) finalizedDateElement.getParentNode())
				.insertBefore(emeasureIDElement,
						finalizedDateElement);
			}
		}
	}
	
	/**
	 * Creates the general information node.
	 */
	public void createGeneralInformationNode(){
		
	}
	
	/**
	 * This method creates blank nodes for Elements like 'child elements of
	 * populations node, measureObservations node and stratifications node' The
	 * method will return the newly created Element. The called needs to add
	 * this Element to the appropriate parent node in the Document.
	 * 
	 * @param nodeName
	 *            the node name
	 * @param clauseDisplayName
	 *            the clause display name
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
		
		/**Commenting this code for MAT-7076*/
		
		//logical AND is not required by stratification clause at the
		//time of creation of new Measure But Population and Measure Observations
		// clauses will have Logical AND by Default.
		
		/**if (!nodeName.equalsIgnoreCase("strata")&& (topNodeOperatorMap.containsKey(nodeName))) {
			String nodeTopLogicalOperator = topNodeOperatorMap.get(nodeName);
			if (nodeTopLogicalOperator != null) {
				Element logicalOpElem = originalDoc.createElement("logicalOp");
				logicalOpElem.setAttribute(DISPLAY_NAME, nodeTopLogicalOperator.toUpperCase(Locale.US));
				logicalOpElem.setAttribute(TYPE, nodeTopLogicalOperator);
				clauseChildElem.appendChild(logicalOpElem);
			}
		}*/
		
		/**Commenting for MAT-7076 ends.*/
		mainChildElem.appendChild(clauseChildElem);
		
		return mainChildElem;
	}
	
	/**
	 * Removes the from parent.
	 * 
	 * @param node
	 *            the node
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
	 * @param document
	 *            the document
	 * @param xPathString
	 *            the x path string
	 * @return the node
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	public Node findNode(Document document, String xPathString)
			throws XPathExpressionException {
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		Node node = (Node) xPath.evaluate(xPathString,
				document.getDocumentElement(), XPathConstants.NODE);
		return node;
	}
	
	/**
	 * Find node list.
	 * 
	 * @param document
	 *            the document
	 * @param xPathString
	 *            the x path string
	 * @return the node list
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	public NodeList findNodeList(Document document, String xPathString)
			throws XPathExpressionException {
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xPath.compile(xPathString);
		return (NodeList) expr.evaluate(document, XPathConstants.NODESET);
	}
	
	/**
	 * Gets the node count.
	 * 
	 * @param document
	 *            the document
	 * @param xPathString
	 *            the x path string
	 * @return the node count
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	public int getNodeCount(Document document, String xPathString)
			throws XPathExpressionException {
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xPath.compile(xPathString);
		return ((Double) expr.evaluate(document, XPathConstants.NUMBER)).intValue();
	}
	
	//	public void addEmptyItemCountNode(){
	//		NodeList nodes=originalDoc.getElementsByTagName("scoring");
	//		Element itemCountElement=originalDoc.createElement("itemCount");
	//		nodes.item(0).getParentNode().insertBefore(itemCountElement,null);
	//	}
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
	 * @param originalDoc
	 *            the originalDoc to set
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
	 * @param str
	 *            the s
	 * @return the string
	 */
	private static String toProperCase(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}
	
	
	
	/**
	 * This method will check for the timing element to be present in
	 * 'elementLookUp/qdm' tag based on its OID's. That element is,
	 * 
	 * 2.16.840.1.113883.3.67.1.101.1.53 : Measurement Period
	 * 
	 * And will return a string of the timing element OID's missing from
	 * 'elementLookUp/qdm'
	 * 
	 * @return String
	 */
	/*public List<String> checkForTimingElements() {
		List<String> missingTimingElementList = new ArrayList<String>();
		
		if (originalDoc != null) {
			try {
				// Measurement Period commented - MAT-7104.
				// Default Measurement Period is created in Parameter section so it is not required in Elementlookup.
				Node measurementPeriodNode = this.findNode(originalDoc,
						"/measure/elementLookUp/qdm[@oid='"
								+ MEASUREMENT_PERIOD_OID + "']");
				if (measurementPeriodNode == null) {
					missingTimingElementList.add(MEASUREMENT_PERIOD_OID);
				}
				
				//Patient Characteristic Birth Data
				Node patientCharacteristicBirthDateNode = this.findNode(originalDoc,
						"/measure/elementLookUp/qdm[@oid='"
								+ PATIENT_CHARACTERISTIC_BIRTH_DATE_OID + "']");
				if (patientCharacteristicBirthDateNode == null) {
					missingTimingElementList.add(PATIENT_CHARACTERISTIC_BIRTH_DATE_OID);
				}
				
				//Patient Characteristic Expired
				Node patientCharacteristicExpiredNode = this.findNode(originalDoc,
						"/measure/elementLookUp/qdm[@oid='"
								+ PATIENT_CHARACTERISTIC_EXPIRED_OID + "']");
				if (patientCharacteristicExpiredNode == null) {
					missingTimingElementList.add(PATIENT_CHARACTERISTIC_EXPIRED_OID);
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return missingTimingElementList;
	}
	
	*//**
	 * Check for qdm id and update.
	 *
	 * @return the string
	 *//*
	public String checkForQdmIDAndUpdate() {
		if (originalDoc == null) {
			return "";
		}
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			
			NodeList nodesElementLookUpAll = (NodeList) xPath.evaluate(
					XPATH_MEASURE_ELEMENT_LOOKUP_QDM,
					originalDoc.getDocumentElement(), XPathConstants.NODESET);
			List<String> idList = new ArrayList<String>();
			for (int i = 0; i < nodesElementLookUpAll.getLength(); i++) {
				Node newNode = nodesElementLookUpAll.item(i);
				
				String id = newNode.getAttributes().getNamedItem("id").getNodeValue().toString();
				
				if(idList.contains(id))
				{
					newNode.getAttributes().getNamedItem("id").setNodeValue(UUID.randomUUID().toString().replaceAll("-", ""));
				}
				else
				{
					idList.add(id);
				}
			}
		}catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return transform(originalDoc);
	}*/
	
	/**
	 * Transform.
	 * 
	 * @param node
	 *            the node
	 * @return the string
	 */
	public String transform(Node node) {
		return transform(node,false);
	}
	
	/**
	 * Convert xml document to string.
	 *
	 * @param node the node
	 * @param isFormatted TODO
	 * @return the string
	 */
	public String transform(Node node, boolean isFormatted) {
		LOG.info("In transform() method");
		Transformer tf;
		Writer out = null;
		try {
			tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
			if(isFormatted){
				tf.setOutputProperty(OutputKeys.INDENT, "yes");
			}
			
			tf.setOutputProperty(OutputKeys.STANDALONE, "yes");
			out = new StringWriter();
			tf.transform(new DOMSource(node), new StreamResult(out));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		LOG.info("Document object to ByteArray transformation complete");
		return out.toString();
	}
	
	/**
	 * Takes the spaces out of the clauseName (attribute displayName in XML).
	 *
	 * @param xmlString the xml string
	 * @param xPathString the x path string
	 * @return the string
	 * @throws XPathExpressionException the x path expression exception
	 */
	public static String normalizeNodeForSpaces (String xmlString, String xPathString) throws XPathExpressionException {
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
	public static void clean(Node node)
	{
		NodeList childNodes = node.getChildNodes();
		
		for (int n = childNodes.getLength() - 1; n >= 0; n--)
		{
			Node child = childNodes.item(n);
			short nodeType = child.getNodeType();
			
			if (nodeType == Node.ELEMENT_NODE) {
				clean(child);
			} else if (nodeType == Node.TEXT_NODE)
			{
				String trimmedNodeVal = child.getNodeValue().trim();
				if (trimmedNodeVal.length() == 0) {
					node.removeChild(child);
				} else {
					child.setNodeValue(trimmedNodeVal);
				}
			}
			else if (nodeType == Node.COMMENT_NODE) {
				node.removeChild(child);
			}
		}
	}
	
	/**
	 * Creates the cql general info.
	 * @param releaseVersion 
	 *
	 * @return the string
	 */
	public String createCQLLookUpElements(String releaseVersion) {
		
		if (originalDoc == null) {
			return "";
		}
		// Get the title from originalDoc
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			
			Node cqlNode = findNode(originalDoc, XPATH_CQL_LOOKUP);
			if(cqlNode!=null){
				
				String libraryName = (String) xPath.evaluate(
						"/measure/measureDetails/title/text()",
						originalDoc.getDocumentElement(), XPathConstants.STRING);
				
				String version = (String) xPath.evaluate(
						"/measure/measureDetails/version/text()",
						originalDoc.getDocumentElement(), XPathConstants.STRING);
				
				Element libraryChildElem = originalDoc.createElement("library");
				libraryChildElem.setTextContent(cleanString(libraryName));
				
				Element versionChildElem = originalDoc.createElement("version");
				versionChildElem.setTextContent(version);
				
				Element usingChildElem = originalDoc.createElement("usingModel");
				usingChildElem.setTextContent("QDM");
				
				Element usingVerChildElem = originalDoc.createElement("usingModelVersion");
				usingVerChildElem.setTextContent(releaseVersion);
				
				Element contextChildElem = originalDoc.createElement("cqlContext");
				contextChildElem.setTextContent("Patient");
				
				Element codeSystemsChildElem = originalDoc.createElement("codeSystems");
				Element valueSetsChildElem = originalDoc.createElement("valuesets");
				Element codeChildElem = originalDoc.createElement("codes");
				Element parametersChildElem = originalDoc.createElement("parameters");
				
				Element definitionsChildElem = originalDoc.createElement("definitions");
				Element functionsChildElem = originalDoc.createElement("functions");
				
				
				cqlNode.appendChild(libraryChildElem);
				cqlNode.appendChild(versionChildElem);
				cqlNode.appendChild(usingChildElem);
				cqlNode.appendChild(usingVerChildElem);
				cqlNode.appendChild(contextChildElem);
				cqlNode.appendChild(codeSystemsChildElem);
				cqlNode.appendChild(valueSetsChildElem);
				cqlNode.appendChild(codeChildElem);
				cqlNode.appendChild(parametersChildElem);
				cqlNode.appendChild(definitionsChildElem);
				cqlNode.appendChild(functionsChildElem);
			}
				
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return transform(originalDoc);
		
	}
	
	public void updateCQLLibraryName() throws XPathExpressionException{
		
		Node cqlLibraryNode = findNode(originalDoc, "/measure/cqlLookUp/library");
		
		if(cqlLibraryNode != null){
			
			Node measureTitleNode = findNode(originalDoc, "/measure/measureDetails/title");
			String libraryName = measureTitleNode.getTextContent();
			libraryName = cleanString(libraryName);
			
			cqlLibraryNode.setTextContent(libraryName);
		}
	}
	
	/**
	 * This method will take a String and remove all non-alphabet/non-numeric characters 
	 * except underscore ("_") characters.
	 * @param originalString
	 * @return cleanedString
	 */
	private String cleanString(String originalString) {
		originalString = originalString.replaceAll(" ", "");
		
		String cleanedString = "";
				
		for(int i=0;i<originalString.length();i++){
			char c = originalString.charAt(i);
			int intc = (int)c;
			if(c == '_' || (intc >= 48 && intc <= 57) || (intc >= 65 && intc <= 90) || (intc >= 97 && intc <= 122)){
				cleanedString = cleanedString + "" + c;
			}
		}
		
		return cleanedString;
	}
	
	/**
	 * Check for default parameter MeasurementPeriod.
	 *
	 * @return the list
	 */
	public List<String> checkForDefaultParameters(){
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
				e.printStackTrace();
			}
		}
		return missingDefaultParameterList;
	}
}
