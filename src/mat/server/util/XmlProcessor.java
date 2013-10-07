package mat.server.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import mat.shared.UUIDUtilClient;
import net.sf.saxon.TransformerFactoryImpl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlProcessor {

	private static final String MEASUREMENT_END_DATE_OID = "2.16.840.1.113883.3.67.1.101.1.55";

	private static final String MEASUREMENT_START_DATE_OID = "2.16.840.1.113883.3.67.1.101.1.54";

	private static final String MEASUREMENT_PERIOD_OID = "2.16.840.1.113883.3.67.1.101.1.53";

	private static final String XPATH_POPULATIONS = "/measure/populations";

	private static final String XPATH_NUMERATORS = "/measure/populations/numerators";

	private static final String XPATH_DENOMINATOR = "/measure/populations/denominators";

	private static final String XPATH_NUMERATOR_EXCLUSIONS = "/measure/populations/numeratorExclusions";

	private static final String XPATH_MEASURE_OBSERVATIONS = "/measure/measureObservations";

	private static final String XPATH_MEASURE_STRATIFICATIONS = "/measure/strata";

	private static final String XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS = "/measure/supplementalDataElements";

	private static final String XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_ELEMENTREF = "/measure/supplementalDataElements/elementRef";

	private static final String XPATH_MEASURE_ELEMENT_LOOKUP = "/measure/elementLookUp";

	private static final String XPATH_MEASURE_ELEMENT_LOOKUP_QDM = "/measure/elementLookUp/qdm";

	private static final String XPATH_MEASURE_POPULATIONS = "/measure/populations/measurePopulations";

	private static final String XPATH_DENOMINATOR_EXCEPTIONS = "/measure/populations/denominatorExceptions";

	private static final String XPATH_DENOMINATOR_EXCLUSIONS = "/measure/populations/denominatorExclusions";

	private static final String RATIO = "RATIO";

	private static final String PROPOR = "PROPOR";

	private static final String SCORING_TYPE_CONTVAR = "CONTVAR";

	private static final String NUMERATOR_EXCLUSIONS = "numeratorExclusions";

	private static final String DENOMINATOR_EXCEPTIONS = "denominatorExceptions";

	private static final String DENOMINATOR_EXCLUSIONS = "denominatorExclusions";

	private static final String DENOMINATORS = "denominators";

	private static final String NUMERATORS = "numerators";

	private static final String MEASURE_POPULATIONS = "measurePopulations";

	private static final String INITIAL_PATIENT_POPULATIONS = "initialPatientPopulations";

	public static final String XPATH_MEASURE_CLAUSE = "/measure/populations/*/clause | /measure/*/clause[@type !='stratum']";

	public static final String XPATH_MEASURE_GROUPING = "/measure/measureGrouping";

	public static final String XPATH_MEASURE_GROUPING_GROUP = "/measure/measureGrouping/group";

	public static final String XPATH_GROUP_SEQ_START = "/measure/measureGrouping/group[@sequence = '";

	public static final String XPATH_GROUP_SEQ_END = "' ] ";

	public static final String XPATH_FIND_GROUP_CLAUSE = "/measure/measureGrouping/group[packageClause[";

	private static Map<String, String> constantsMap = new HashMap<String, String>();

	private static final Log logger = LogFactory.getLog(XmlProcessor.class);

	private String originalXml;

	private DocumentBuilder docBuilder;

	private Document originalDoc;

	private static final String[] POPULATIONS = { INITIAL_PATIENT_POPULATIONS,
			NUMERATORS, NUMERATOR_EXCLUSIONS, DENOMINATORS,
			DENOMINATOR_EXCLUSIONS, DENOMINATOR_EXCEPTIONS, MEASURE_POPULATIONS };

	static {
		constantsMap.put("populations", "Populations");
		constantsMap.put("measureObservations", "Measure Observations");
		constantsMap.put("strata", "Stratification");
		constantsMap.put("measureObservations", "Measure Observations");
		constantsMap.put("initialPatientPopulations",
				"Initial Patient Populations");
		constantsMap.put("numerators", "Numerators");
		constantsMap.put("denominators", "Denominators");
		constantsMap.put("denominatorExclusions", "Denominator Exclusions");
		constantsMap.put("denominatorExceptions", "Denominator Exceptions");
		constantsMap.put("measurePopulations", "Measure Populations");
		constantsMap.put("numeratorExclusions", "Numerator Exclusions");

		constantsMap.put("Measure Observations", "Measure Observation");
		constantsMap.put("Stratum", "Stratum");
		constantsMap.put("Initial Patient Populations",
				"Initial Patient Population");
		constantsMap.put("Numerators", "Numerator");
		constantsMap.put("Denominators", "Denominator");
		constantsMap.put("Denominator Exclusions", "Denominator Exclusions");
		constantsMap.put("Denominator Exceptions", "Denominator Exceptions");
		constantsMap.put("Measure Populations", "Measure Population");
		constantsMap.put("Numerator Exclusions", "Numerator Exclusions");

	}

	public XmlProcessor(String originalXml) {
		logger.info("In XmlProcessor() constructor");
		this.originalXml = originalXml;
		try {
			docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputSource oldXmlstream = new InputSource(new StringReader(
					originalXml));
			originalDoc = docBuilder.parse(oldXmlstream);
			logger.info("Document Object created successfully for the XML String");
		} catch (Exception e) {
			logger.info("Exception thrown on XmlProcessor() costructor");
			caughtExceptions(e);
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Method to insert new Node under existing parent Node.
	 * 
	 * */
	public String appendNode(String newElement, String nodeName,
			String parentNode) throws SAXException, IOException {
		logger.info("In appendNode method with newElement ::: " + newElement);
		if (this.originalDoc == null || newElement == null) {
			return "";
		}
		try {
			Node parentTypeNode = findNode(originalDoc, parentNode);
			InputSource newXmlstream = new InputSource(new StringReader(
					newElement));
			Document newDoc = docBuilder.parse(newXmlstream);// Parse the NewXml
																// which should
																// be replaced
			NodeList newNodeList = newDoc.getElementsByTagName(nodeName);
			for (int i = 0; i < newNodeList.getLength(); i++) {
				Node newNode = newNodeList.item(i);
				parentTypeNode.appendChild(originalDoc
						.importNode(newNode, true));
			}

			logger.info("Document Object created successfully for the XML String"
					+ originalDoc.toString());
		} catch (XPathExpressionException e) {
			logger.info("Exception thrown on appendNode method");
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
	 * text</ABC><AAA>
	 * 
	 * @param newXml
	 * @param nodeName
	 * @param parentName
	 *            // this is optional, can be null or empty. if parentName not
	 *            null, the oldNode to be replaced will be retrieved based on
	 *            the parent Node, this is done to make sure we are replacing
	 *            the correct node.
	 * 
	 * 
	 * @return
	 */
	public String replaceNode(String newXml, String nodeName, String parentName) {
		try {
			logger.info("In replaceNode() method");
			InputSource newXmlstream = new InputSource(new StringReader(newXml));
			Document newDoc = docBuilder.parse(newXmlstream);// Parse the NewXml
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
								.getParentNode().getNodeName())) {// get the old
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
							.getNodeName())) {// get the new node used to
												// replace.
						newNode = newNodeList.item(i);
						break;
					}
				}

				if (oldNode != null) {// check if the OriginalXml has the Node
										// that should be replaced
					Node nextSibling = oldNode.getNextSibling();
					Node parentNode = oldNode.getParentNode();
					parentNode.removeChild(oldNode);// Removing the old child
													// node
					if (nextSibling != null) {
						parentNode.insertBefore(
								originalDoc.importNode(newNode, true),
								nextSibling);// to maintain the order insert
												// before the next sibling if
												// exists
					} else {
						parentNode.appendChild(originalDoc.importNode(newNode,
								true));// insert the new child node to the old
										// child's Parent node,.
					}
					logger.info("Replaced old Child Node with new Child Node "
							+ nodeName);
				} else {// if the Original Document doesnt have the Node, then
						// insert the new Node under the first child
					Node importNode = originalDoc.importNode(newNode, true);
					originalDoc.getFirstChild().appendChild(importNode);
					logger.info("Inserted new Child Node" + nodeName);
				}
				return transform(originalDoc);
			}

		} catch (Exception e) {
			logger.info("Exception thrown on replaceNode() method");
			caughtExceptions(e);
			e.printStackTrace();
		}
		return originalXml;// not replaced returnig the original Xml;
	}

	public String updateNodeText(String nodeName, String nodeValue) {
		try {
			logger.info("In updateNodeText() method");
			InputSource xmlStream = new InputSource(new StringReader(
					originalXml));
			Document doc = docBuilder.parse(xmlStream);
			doc.getElementsByTagName(nodeName).item(0)
					.setTextContent(nodeValue);
			logger.info("update NoedText");
			return transform(doc);
		} catch (Exception e) {
			logger.info("Exception thrown on updateNodeText() method");
			caughtExceptions(e);
			e.printStackTrace();
		}
		return null;
	}

	public String transform(Node node) {
		logger.info("In transform() method");
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		TransformerFactory transformerFactory = TransformerFactoryImpl
				.newInstance();
		DOMSource source = new DOMSource(node);
		StreamResult result = new StreamResult(arrayOutputStream);

		try {
			transformerFactory.newTransformer().transform(source, result);
		} catch (TransformerException e) {
			logger.info("Document object to ByteArray transformation failed "
					+ e.getStackTrace());
			e.printStackTrace();
		}
		logger.info("Document object to ByteArray transformation complete");
		System.out.println(arrayOutputStream.toString());
		return arrayOutputStream.toString();
	}

	public String getOriginalXml() {
		return originalXml;
	}

	public void setOriginalXml(String originalXml) {
		this.originalXml = originalXml;
	}

	public String getXmlByTagName(String tagName) {
		Node node = originalDoc.getElementsByTagName(tagName).getLength() > 0 ? originalDoc
				.getElementsByTagName(tagName).item(0) : null;
		if (null != node) {
			return transform(node);
		}
		return null;
	}

	public void addParentNode(String parentTagName) {
		if (originalDoc.hasChildNodes()) {
			Document newDoc = docBuilder.newDocument();
			Node parentNode = newDoc.appendChild(newDoc
					.createElement(parentTagName));
			Node importedNode = newDoc.importNode(originalDoc.getFirstChild(),
					true);
			parentNode.appendChild(importedNode);
			this.originalDoc = newDoc;
		}
	}

	private void caughtExceptions(Exception e) {
		if (e instanceof ParserConfigurationException) {
			logger.info("Document Builder Object creation failed"
					+ e.getStackTrace());
		} else if (e instanceof SAXException) {
			logger.info("Xml parsing failed:" + e.getStackTrace());
		} else if (e instanceof IOException) {
			logger.info("Conversion of String XML to InputSource failed"
					+ e.getStackTrace());
		} else {
			logger.info("Generic Exception: " + e.getStackTrace());
		}
	}

	public String checkForScoringType() {
		if (this.originalDoc == null) {
			return "";
		}
		// Get the scoring type from originalDoc
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			String scoringType = (String) xPath.evaluate(
					"/measure/measureDetails/scoring/@id",
					originalDoc.getDocumentElement(), XPathConstants.STRING);
			System.out.println("scoringType:" + scoringType);

			removeNodesBasedOnScoring(scoringType);
			createNewNodesBasedOnScoring(scoringType);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return transform(originalDoc);
	}

	public Map<String, ArrayList<QualityDataSetDTO>> sortSDEAndQDMsForMeasurePackager() {
		Map<String, ArrayList<QualityDataSetDTO>> map = new HashMap<String, ArrayList<QualityDataSetDTO>>();
		ArrayList<QualityDataSetDTO> qdmList = new ArrayList<QualityDataSetDTO>();
		ArrayList<QualityDataSetDTO> masterList = new ArrayList<QualityDataSetDTO>();
		ArrayList<QualityDataSetDTO> supplementalDataList = new ArrayList<QualityDataSetDTO>();
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();

		if (this.originalDoc == null) {
			return map;
		}

		try {
			NodeList nodesElementLookUpAll = (NodeList) xPath.evaluate(
					XPATH_MEASURE_ELEMENT_LOOKUP_QDM,
					originalDoc.getDocumentElement(), XPathConstants.NODESET);

			// Master List of Element Look Up QDM's. This list is used to
			// populate QDM properties in SDE and QDM List.
			for (int i = 0; i < nodesElementLookUpAll.getLength(); i++) {
				Node newNode = nodesElementLookUpAll.item(i);
				QualityDataSetDTO dataSetDTO = new QualityDataSetDTO();
				dataSetDTO.setId(newNode.getAttributes().getNamedItem("id")
						.getNodeValue().toString());
				dataSetDTO.setDataType(newNode.getAttributes()
						.getNamedItem("datatype").getNodeValue().toString());
				if (newNode.getAttributes().getNamedItem("instance") != null)
					dataSetDTO
							.setOccurrenceText(newNode.getAttributes()
									.getNamedItem("instance").getNodeValue()
									.toString());
				else
					dataSetDTO.setOccurrenceText("");
				dataSetDTO.setCodeListName(newNode.getAttributes()
						.getNamedItem("name").getNodeValue().toString());
				dataSetDTO.setOid(newNode.getAttributes().getNamedItem("oid")
						.getNodeValue().toString());
				dataSetDTO.setTaxonomy(newNode.getAttributes()
						.getNamedItem("taxonomy").getNodeValue().toString());
				dataSetDTO.setUuid(newNode.getAttributes().getNamedItem("uuid")
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
					XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS_ELEMENTREF,
					originalDoc.getDocumentElement(), XPathConstants.NODESET);
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
						originalDoc.getDocumentElement(),
						XPathConstants.NODESET);
				// populate QDM List
				for (int i = 0; i < nodesFinal.getLength(); i++) {
					Node newNode = nodesFinal.item(i);
					String nodeID = newNode.getAttributes()
							.getNamedItem("uuid").getNodeValue();
					String dataType = newNode.getAttributes()
							.getNamedItem("datatype").getNodeValue();
					boolean isOccurrenceText = false;
					if (newNode.getAttributes().getNamedItem("instance") != null) {
						isOccurrenceText = true;
					}
					// Check to Filter Occurrences
					if (!isOccurrenceText) {
						// Check to filter Attributes and Timing data types.
						if (!dataType
								.equalsIgnoreCase(ConstantMessages.TIMING_ELEMENT)
								&& !dataType
										.equalsIgnoreCase(ConstantMessages.ATTRIBUTE)) {
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
			} else {
				for (int i = 0; i < nodesElementLookUpAll.getLength(); i++) {
					Node newNode = nodesElementLookUpAll.item(i);
					String nodeID = newNode.getAttributes()
							.getNamedItem("uuid").getNodeValue();
					String dataType = newNode.getAttributes()
							.getNamedItem("datatype").getNodeValue();
					boolean isOccurrenceText = false;
					if (newNode.getAttributes().getNamedItem("instance") != null) {
						isOccurrenceText = true;
					}
					// Check to Filter Occurrences
					if (!isOccurrenceText) {
						// Check to filter Attributes and Timing data types.
						if (!dataType
								.equalsIgnoreCase(ConstantMessages.TIMING_ELEMENT)
								&& !dataType
										.equalsIgnoreCase(ConstantMessages.ATTRIBUTE)) {
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
	 * This method looks at the Scoring Type for a measure and removes nodes
	 * based on the value of Scoring Type.
	 * 
	 * @param scoringType
	 * @throws XPathExpressionException
	 */
	public void removeNodesBasedOnScoring(String scoringType)
			throws XPathExpressionException {
		List<String> xPathList = new ArrayList<String>();

		if (RATIO.equals(scoringType.toUpperCase())) {
			// Denominator Exceptions, Measure Populations
			xPathList.add(XPATH_DENOMINATOR_EXCEPTIONS);
			xPathList.add(XPATH_MEASURE_POPULATIONS);
			xPathList.add(XPATH_MEASURE_OBSERVATIONS);
		} else if (PROPOR.equals(scoringType.toUpperCase())) {
			// Numerator Exclusions, Measure Populations
			xPathList.add(XPATH_NUMERATOR_EXCLUSIONS);
			xPathList.add(XPATH_MEASURE_POPULATIONS);
			xPathList.add(XPATH_MEASURE_OBSERVATIONS);
		} else if (SCORING_TYPE_CONTVAR.equals(scoringType.toUpperCase())) {
			// Numerators,Numerator Exclusions, Denominators, Denominator
			// Exceptions, Denominator Exclusions
			xPathList.add(XPATH_NUMERATORS);
			xPathList.add(XPATH_NUMERATOR_EXCLUSIONS);
			xPathList.add(XPATH_DENOMINATOR);
			xPathList.add(XPATH_DENOMINATOR_EXCEPTIONS);
			xPathList.add(XPATH_DENOMINATOR_EXCLUSIONS);
		}

		for (String xPathString : xPathList) {
			Node node = findNode(this.originalDoc, xPathString);
			removeFromParent(node);
		}
	}

	/**
	 * This method looks at the Scoring Type for a measure and adds nodes based
	 * on the value of Scoring Type.
	 * 
	 * @param scoringType
	 * @throws XPathExpressionException
	 */
	public void createNewNodesBasedOnScoring(String scoringType)
			throws XPathExpressionException {

		List<String> scoreBasedNodes = new ArrayList<String>();
		if (SCORING_TYPE_CONTVAR.equals(scoringType)) {
			scoreBasedNodes.add(INITIAL_PATIENT_POPULATIONS);
			scoreBasedNodes.add(MEASURE_POPULATIONS);
		} else if (PROPOR.equals(scoringType)) {
			scoreBasedNodes.add(INITIAL_PATIENT_POPULATIONS);
			scoreBasedNodes.add(NUMERATORS);
			scoreBasedNodes.add(DENOMINATORS);
			scoreBasedNodes.add(DENOMINATOR_EXCLUSIONS);
			scoreBasedNodes.add(DENOMINATOR_EXCEPTIONS);
		} else if (RATIO.equals(scoringType)) {
			scoreBasedNodes.add(INITIAL_PATIENT_POPULATIONS);
			scoreBasedNodes.add(NUMERATORS);
			scoreBasedNodes.add(NUMERATOR_EXCLUSIONS);
			scoreBasedNodes.add(DENOMINATORS);
			scoreBasedNodes.add(DENOMINATOR_EXCLUSIONS);
		}

		Node populationsNode = findNode(this.originalDoc, XPATH_POPULATIONS);
		if (populationsNode == null) {
			populationsNode = addPopulationsNode();
		}
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

		/**
		 * Add Measure Observations node after Populations node if not present
		 * and scoring type is Continuous Variable.
		 */
		Node measureObservationsNode = findNode(this.originalDoc,
				XPATH_MEASURE_OBSERVATIONS);
		if (SCORING_TYPE_CONTVAR.equals(scoringType)) {
			if (measureObservationsNode == null) {
				// Create a new measureObservations element.
				String nodeName = "measureObservations";
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
		}

		// Create stratifications node
		Node measureStratificationsNode = findNode(this.originalDoc,
				XPATH_MEASURE_STRATIFICATIONS);
		if (measureStratificationsNode == null) {
			String stratificationsNodeName = "strata";
			String clauseDisplayName = "Stratum";
			Element stratificationElement = createTemplateNode(
					stratificationsNodeName, clauseDisplayName);
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
		Node supplementaDataElements_Element = findNode(originalDoc,
				XPATH_MEASURE_SUPPLEMENTAL_DATA_ELEMENTS);
		if (supplementaDataElements_Element == null) {
			supplementaDataElements_Element = originalDoc
					.createElement("supplementalDataElements");
			((Element) measureStratificationsNode.getParentNode())
					.insertBefore(supplementaDataElements_Element,
							measureStratificationsNode.getNextSibling());
		}

		// Create elementLookUp node
		if (findNode(originalDoc, XPATH_MEASURE_ELEMENT_LOOKUP) == null) {
			Element elementLookUp_Element = originalDoc
					.createElement("elementLookUp");
			((Element) supplementaDataElements_Element.getParentNode())
					.insertBefore(elementLookUp_Element,
							supplementaDataElements_Element.getNextSibling());
		}

		// create Measure Grouping node
		if (findNode(originalDoc, XPATH_MEASURE_GROUPING) == null) {
			Element measureGrouping_Element = originalDoc
					.createElement("measureGrouping");
			((Element) supplementaDataElements_Element.getParentNode())
					.insertBefore(measureGrouping_Element,
							supplementaDataElements_Element.getNextSibling());
		}

		/*
		 * All the adding and removing can put the children of 'populations' in
		 * a random order. Arrange the population nodes in correct order.
		 */

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

		// Take the child nodeList & re-arrange it according to this order
		// "Initial Patient Populations", "Numerators", "Numerator Exclusions",
		// "Denominators", "Denominator Exclusions",
		// "Denominator Exceptions", "Measure Populations"
		for (int i = 0; i < XmlProcessor.POPULATIONS.length; i++) {
			String populationsChild = XmlProcessor.POPULATIONS[i];
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
	 * This method will add a 'populations' node to the XML. It assumes that the
	 * 'measureDetails' node is already present in the XML Document and tries to
	 * add the 'populations' node after the 'measureDetails' node.
	 * 
	 * @throws XPathExpressionException
	 */
	private Node addPopulationsNode() throws XPathExpressionException {
		Element populationsElem = originalDoc.createElement("populations");
		populationsElem.setAttribute("displayName",
				XmlProcessor.constantsMap.get("populations"));
		Node measureDetailsNode = findNode(this.originalDoc,
				"/measure/measureDetails");
		Node measureNode = measureDetailsNode.getParentNode();
		Element measureElement = (Element) measureNode;
		measureElement.insertBefore(populationsElem,
				measureDetailsNode.getNextSibling());
		return populationsElem;
	}

	/**
	 * This method creates blank nodes for Elements like 'child elements of
	 * populations node, measureObservations node and stratifications node' The
	 * method will return the newly created Element. The called needs to add
	 * this Element to the appropriate parent node in the Document.
	 * 
	 * @param nodeName
	 * @param displayName
	 * @return
	 */
	private Element createTemplateNode(String nodeName, String clauseDisplayName) {
		Element mainChildElem = originalDoc.createElement(nodeName);
		mainChildElem.setAttribute("displayName", constantsMap.get(nodeName));

		Element clauseChildElem = originalDoc.createElement("clause");
		String dispName = constantsMap.get(clauseDisplayName);
		clauseChildElem.setAttribute("displayName", dispName + " 1");
		clauseChildElem.setAttribute("type", toCamelCase(dispName));
		clauseChildElem.setAttribute("uuid", UUIDUtilClient.uuid());
		mainChildElem.appendChild(clauseChildElem);

		Element logicalOpElem = originalDoc.createElement("logicalOp");
		logicalOpElem.setAttribute("displayName", "AND");
		logicalOpElem.setAttribute("type", "and");

		clauseChildElem.appendChild(logicalOpElem);
		mainChildElem.appendChild(clauseChildElem);

		return mainChildElem;
	}

	private void removeFromParent(Node node) {
		if (node != null) {
			Node parentNode = node.getParentNode();
			parentNode.removeChild(node);
		}
	}

	public Node findNode(Document document, String xPathString)
			throws XPathExpressionException {
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		Node node = (Node) xPath.evaluate(xPathString,
				document.getDocumentElement(), XPathConstants.NODE);
		return node;
	}

	public NodeList findNodeList(Document document, String xPathString)
			throws XPathExpressionException {
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xPath.compile(xPathString);
		return (NodeList) expr.evaluate(document, XPathConstants.NODESET);
	}

	public NodeList getNodeCount(Document document, String xPathString)
			throws XPathExpressionException {
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xPath.compile(xPathString);
		return (NodeList) expr.evaluate(document, XPathConstants.NODESET);
	}

	/**
	 * @return the originalDoc
	 */
	public Document getOriginalDoc() {
		return originalDoc;
	}

	/**
	 * @param originalDoc
	 *            the originalDoc to set
	 */
	public void setOriginalDoc(Document originalDoc) {
		this.originalDoc = originalDoc;
	}

	private static String toCamelCase(String name) {
		name = name.toLowerCase();
		String[] parts = name.split(" ");
		String camelCaseString = parts[0].substring(0, 1).toLowerCase()
				+ parts[0].substring(1);
		for (int i = 1; i < parts.length; i++) {
			camelCaseString = camelCaseString + toProperCase(parts[i]);
		}
		return camelCaseString;
	}

	private static String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	/**
	 * Method to create XML from QualityDataModelWrapper object for
	 * elementLookUp.
	 * */
	public static org.apache.commons.io.output.ByteArrayOutputStream convertQualityDataDTOToXML(
			QualityDataModelWrapper qualityDataSetDTO) {
		logger.info("In MeasureLibraryServiceImpl.convertQualityDataDTOToXML()");
		Mapping mapping = new Mapping();
		org.apache.commons.io.output.ByteArrayOutputStream stream = new org.apache.commons.io.output.ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader()
					.getResourceAsURL("QualityDataModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(
					stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(qualityDataSetDTO);
			logger.info("Marshalling of QualityDataSetDTO is successful.."
					+ stream.toString());
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load QualityDataModelMapping.xml in convertQualityDataDTOToXML method"
						+ e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed in convertQualityDataDTOToXML method"
						+ e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed in convertQualityDataDTOToXML method"
						+ e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception in convertQualityDataDTOToXML method"
						+ e);
			} else {
				logger.info("Other Exception in convertQualityDataDTOToXML method "
						+ e);
			}
		}
		logger.info("Exiting MeasureLibraryServiceImpl.convertQualityDataDTOToXML()");
		return stream;
	}

	/**
	 * Method to create XML from QualityDataModelWrapper object for
	 * supplementalDataElement .
	 * */
	public static org.apache.commons.io.output.ByteArrayOutputStream convertQDMOToSuppleDataXML(
			QualityDataModelWrapper qualityDataSetDTO) {
		logger.info("In MeasureLibraryServiceImpl.convertQDMOToSuppleDataXML()");
		Mapping mapping = new Mapping();
		org.apache.commons.io.output.ByteArrayOutputStream stream = new org.apache.commons.io.output.ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader()
					.getResourceAsURL("QDMToSupplementDataMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(
					stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(qualityDataSetDTO);
			logger.info("Marshalling of QualityDataSetDTO is successful in convertQDMOToSuppleDataXML()"
					+ stream.toString());
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load QualityDataModelMapping.xml in convertQDMOToSuppleDataXML()"
						+ e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed in convertQDMOToSuppleDataXML()"
						+ e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed in convertQDMOToSuppleDataXML()"
						+ e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception in convertQDMOToSuppleDataXML()"
						+ e);
			} else {
				logger.info("Other Exception in convertQDMOToSuppleDataXML()"
						+ e);
			}
		}
		logger.info("Exiting MeasureLibraryServiceImpl.convertQDMOToSuppleDataXML()");
		return stream;
	}

	/**
	 * This method will check for the 3 timing elements to be present in
	 * 'elementLookUp/qdm' tag based on their OID's. Those 3 elements are,
	 * 
	 * 2.16.840.1.113883.3.67.1.101.1.53 : Measurement Period
	 * 2.16.840.1.113883.3.67.1.101.1.54 : Measurement Start Date
	 * 2.16.840.1.113883.3.67.1.101.1.55 : Measurement End Date
	 * 
	 * And will return a list of timing element OID's missing from
	 * 'elementLookUp/qdm'
	 * 
	 * @return List<String>
	 */
	public List<String> checkForTimingElements() {
		List<String> missingTimingElementList = new ArrayList<String>();

		if (this.originalDoc != null) {
			try {
				// Measurement Period
				Node measurementPeriodNode = this.findNode(originalDoc,
						"/measure/elementLookUp/qdm[@oid='"
								+ MEASUREMENT_PERIOD_OID + "']");
				if (measurementPeriodNode == null) {
					missingTimingElementList.add(MEASUREMENT_PERIOD_OID);
				}
				// Measurement Start Date
				Node measurementStartDateNode = this.findNode(originalDoc,
						"/measure/elementLookUp/qdm[@oid='"
								+ MEASUREMENT_START_DATE_OID + "']");
				if (measurementStartDateNode == null) {
					missingTimingElementList.add(MEASUREMENT_START_DATE_OID);
				}
				// Measurement End Date
				Node measurementEndDateNode = this.findNode(originalDoc,
						"/measure/elementLookUp/qdm[@oid='"
								+ MEASUREMENT_END_DATE_OID + "']");
				if (measurementEndDateNode == null) {
					missingTimingElementList.add(MEASUREMENT_END_DATE_OID);
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return missingTimingElementList;
	}
}
