package mat.server.simplexml.hqmf;

import java.io.StringWriter;
import java.io.Writer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// TODO: Auto-generated Javadoc
/**
 * The Class HQMFDataCriteriaGenerator.
 */
public class HQMFDataCriteriaGenerator implements Generator {
	
	private static final String RELATED_TO = "related to";
	
	private static final String CHECK_IF_PRESENT = "Check if Present";
	
	private static final String TYPE = "type";
	
	private static final String MOOD = "mood";
	
	private static final String CLASS = "class";
	
	private static final String XSI_TYPE = "xsi:type";
	
	private static final String VALUE = "value";
	
	private static final String TITLE = "title";
	
	private static final String DISPLAY_NAME = "displayName";
	
	private static final String CODE_SYSTEM = "codeSystem";
	
	private static final String ID = "id";
	
	private static final String ROOT = "root";
	
	private static final String ITEM = "item";
	
	private static final String TEMPLATE_ID = "templateId";
	
	private static final String MOOD_CODE = "moodCode";
	
	private static final String CLASS_CODE = "classCode";
	
	private static final String TYPE_CODE = "typeCode";
	
	private static final String OBSERVATION_CRITERIA = "observationCriteria";
	
	private static final String OUTBOUND_RELATIONSHIP = "outboundRelationship";
	
	private static final String UUID = "uuid";
	
	private static final String TAXONOMY = "taxonomy";
	
	private static final String OID = "oid";
	
	private static final String NAME = "name";
	
	private static final String CODE = "code";
	
	private static final String VALUE_SET = "Value Set";
	
	private static final String ATTRIBUTE_MODE = "attributeMode";
	
	private static final String ATTRIBUTE_NAME = "attributeName";
	
	private static final String NEGATION_RATIONALE = "negation rationale";
	
	/** The x path. */
	//static javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	
	/** The Constant logger. */
	private final Log LOG = LogFactory.getLog(HQMFDataCriteriaGenerator.class);
	
	/** The name space. */
	private final String nameSpace = "http://www.w3.org/2001/XMLSchema-instance";
	
	
	/**
	 * Generate hqm for measure.
	 * 
	 * @param me
	 *            the me
	 * @return the string
	 */
	@Override
	public String generate(MeasureExport me) {
		
		String dataCriteria = "";
		dataCriteria = getHQMFXmlString(me);
		dataCriteria = removeXmlTagNamespaceAndPreamble(dataCriteria);
		return dataCriteria;
		//return dataCriteria.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
	}
	
	/**
	 * Gets the HQMF xml string.
	 * 
	 * @param me
	 *            the me
	 * @return the HQMF xml string
	 */
	private String getHQMFXmlString(MeasureExport me) {
		XmlProcessor dataCriteriaXMLProcessor = createDateCriteriaTemplate(me);
		
		String simpleXMLStr = me.getSimpleXML();
		XmlProcessor simpleXmlprocessor = new XmlProcessor(simpleXMLStr);
		
		createDataCriteriaForQDMELements(me, dataCriteriaXMLProcessor, simpleXmlprocessor);
		addDataCriteriaComment(dataCriteriaXMLProcessor);
		return convertXMLDocumentToString(dataCriteriaXMLProcessor.getOriginalDoc());
	}
	
	
	/**
	 * Creates the date criteria template.
	 * 
	 * @param me
	 *            the me
	 * @return the string
	 */
	private XmlProcessor createDateCriteriaTemplate(MeasureExport me) {
		XmlProcessor outputProcessor = new XmlProcessor(
				"<component><dataCriteriaSection></dataCriteriaSection></component>");
		
		Node dataCriteriaElem = outputProcessor.getOriginalDoc()
				.getElementsByTagName("dataCriteriaSection").item(0);
		Element templateId = outputProcessor.getOriginalDoc()
				.createElement(TEMPLATE_ID);
		dataCriteriaElem.appendChild(templateId);
		Element itemChild = outputProcessor.getOriginalDoc()
				.createElement(ITEM);
		itemChild.setAttribute(ROOT, "2.16.840.1.113883.10.20.28.2.2");
		templateId.appendChild(itemChild);
		// creating Code Element for DataCriteria
		Element codeElem = outputProcessor.getOriginalDoc()
				.createElement(CODE);
		codeElem.setAttribute(CODE, "57025-9");
		codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.6.1");
		dataCriteriaElem.appendChild(codeElem);
		// creating title for DataCriteria
		Element titleElem = outputProcessor.getOriginalDoc()
				.createElement(TITLE);
		titleElem.setAttribute(VALUE, "Data Criteria Section");
		dataCriteriaElem.appendChild(titleElem);
		// creating text for DataCriteria
		Element textElem = outputProcessor.getOriginalDoc()
				.createElement("text");
		textElem.setAttribute(VALUE, "Data Criteria text");
		dataCriteriaElem.appendChild(textElem);
		
		return outputProcessor;
	}
	
	/**
	 * Creates the data criteria for qdm elements.
	 *
	 * @param me            the me
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @return the string
	 */
	private void createDataCriteriaForQDMELements(MeasureExport me, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor) {
		//XPath String for only QDM's.
		String xPathForQDMNoAttribs = "/measure/elementLookUp/qdm[@datatype != 'attribute']";
		String xPathForQDMAttributes = "/measure/elementLookUp/qdm[@datatype = 'attribute']";
		
		try {
			
			NodeList qdmNoAttributeNodeList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xPathForQDMNoAttribs);
			//NodeList qdmNoAttributeNodeList = (NodeList) xPath.evaluate(
			//xPathForQDMNoAttribs, simpleXmlprocessor.getOriginalDoc(),
			//	XPathConstants.NODESET);
			
			generateQDMEntries(dataCriteriaXMLProcessor, simpleXmlprocessor,
					qdmNoAttributeNodeList);
			
			NodeList qdmAttributeNodeList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xPathForQDMAttributes);
			/*NodeList qdmAttributeNodeList = (NodeList) xPath.evaluate(
					xPathForQDMAttributes, simpleXmlprocessor.getOriginalDoc(),
					XPathConstants.NODESET);*/
			generateQDMAttributeEntries(dataCriteriaXMLProcessor, simpleXmlprocessor,
					qdmAttributeNodeList);
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param dataCriteriaXMLProcessor
	 * @param simpleXmlprocessor
	 * @param qdmNoAttributeNodeList
	 * @throws XPathExpressionException
	 */
	private void generateQDMEntries(XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, NodeList qdmNoAttributeNodeList)
					throws XPathExpressionException {
		
		if (qdmNoAttributeNodeList == null) {
			return;
		}
		
		for (int i = 0; i < qdmNoAttributeNodeList.getLength(); i++) {
			Node qdmNode = qdmNoAttributeNodeList.item(i);
			String qdmUUID = qdmNode.getAttributes().getNamedItem(UUID).getNodeValue();
			
			String xPathForIndividualElementRefs = "/measure/subTreeLookUp//elementRef[@id='"+qdmUUID+"'][not(attribute)]";
			NodeList elementRefList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xPathForIndividualElementRefs);
			/*	NodeList elementRefList = (NodeList) xPath.evaluate(
					xPathForIndividualElementRefs, simpleXmlprocessor.getOriginalDoc(),
					XPathConstants.NODESET);*/
			if(elementRefList.getLength() > 0){
				createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor, null);
			}
		}
		
	}
	
	/**
	 * @param dataCriteriaXMLProcessor
	 * @param simpleXmlprocessor
	 * @param qdmAttributeNodeList
	 * @throws XPathExpressionException
	 */
	private void generateQDMAttributeEntries(
			XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, NodeList qdmAttributeNodeList) throws XPathExpressionException {
		
		if(qdmAttributeNodeList == null){
			return;
		}
		
		for(int i=0;i<qdmAttributeNodeList.getLength();i++){
			Node attributeQDMNode = qdmAttributeNodeList.item(i);
			String qdmUUID = attributeQDMNode.getAttributes().getNamedItem(UUID).getNodeValue();
			
			//Generate entries for Negation Rationale
			generateNegationRationaleEntries(dataCriteriaXMLProcessor,
					simpleXmlprocessor, attributeQDMNode, qdmUUID);
			
			//Generate entries for "Value Set" attributes
			generateValueSetAttribEntries(dataCriteriaXMLProcessor,
					simpleXmlprocessor, attributeQDMNode, qdmUUID, "Value Set");
		}
		//Generate entries for "Check if Present" attributes
		generateCheckIfPresentAttribEntries(dataCriteriaXMLProcessor,simpleXmlprocessor);
		
	}
	
	/**
	 * @param dataCriteriaXMLProcessor
	 * @param simpleXmlprocessor
	 * @param attributeQDMNode
	 * @param qdmUUID
	 * @throws XPathExpressionException
	 */
	private void generateNegationRationaleEntries(
			XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node attributeQDMNode,
			String qdmUUID) throws XPathExpressionException {
		String xPathForAttributeUse = "/measure/subTreeLookUp/subTree//elementRef/attribute[@qdmUUID='"+qdmUUID+"'][@name='negation rationale']";
		NodeList usedAttributeNodeList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xPathForAttributeUse);
		if(usedAttributeNodeList == null){
			return;
		}
		
		for(int j=0;j<usedAttributeNodeList.getLength();j++){
			Node attributeNode = usedAttributeNodeList.item(j);
			Node parentElementRefNode = attributeNode.getParentNode();
			String qdmNodeUUID = parentElementRefNode.getAttributes().getNamedItem(ID).getNodeValue();
			
			String xPathForQDM = "/measure/elementLookUp/qdm[@uuid='"+qdmNodeUUID+"']";
			Node qdmNode = simpleXmlprocessor.findNode(simpleXmlprocessor.getOriginalDoc(), xPathForQDM);
			
			if(qdmNode == null){
				continue;
			}
			
			//We need some way of letting the methods downstream know that this is a "negation rationale" attribute w/o sending the <attribute> tag node.
			Node clonedAttributeNode = attributeQDMNode.cloneNode(false);
			clonedAttributeNode.setUserData(ATTRIBUTE_NAME, NEGATION_RATIONALE, null);
			
			createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor,clonedAttributeNode);
			
		}
	}
	
	/**
	 * @param dataCriteriaXMLProcessor
	 * @param simpleXmlprocessor
	 * @param attributeQDMNode
	 * @param qdmUUID
	 * @throws XPathExpressionException
	 */
	private void generateValueSetAttribEntries(
			XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node attributeQDMNode,
			String qdmUUID, String modeValue) throws XPathExpressionException {
		String xPathForAttributeUse = "/measure/subTreeLookUp/subTree//elementRef/attribute[@qdmUUID='"+qdmUUID+"'][@name != 'negation rationale'][@mode = '"+modeValue+"']";
		NodeList usedAttributeNodeList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xPathForAttributeUse);
		
		if(usedAttributeNodeList == null){
			return;
		}
		
		for(int j=0;j<usedAttributeNodeList.getLength();j++){
			Node attributeNode = usedAttributeNodeList.item(j);
			Node parentElementRefNode = attributeNode.getParentNode();
			String qdmNodeUUID = parentElementRefNode.getAttributes().getNamedItem(ID).getNodeValue();
			
			String xPathForQDM = "/measure/elementLookUp/qdm[@uuid='"+qdmNodeUUID+"']";
			Node qdmNode = simpleXmlprocessor.findNode(simpleXmlprocessor.getOriginalDoc(), xPathForQDM);
			
			if(qdmNode == null){
				continue;
			}
			
			//We need some way of letting the methods downstream know the name of this attribute w/o sending the <attribute> tag node.
			Node clonedAttributeNode = attributeQDMNode.cloneNode(false);
			clonedAttributeNode.setUserData(ATTRIBUTE_NAME, attributeNode.getAttributes().getNamedItem(NAME).getNodeValue(), null);
			clonedAttributeNode.setUserData(ATTRIBUTE_MODE, attributeNode.getAttributes().getNamedItem("mode").getNodeValue(), null);
			
			createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor,clonedAttributeNode);
		}
	}
	
	private void generateCheckIfPresentAttribEntries(
			XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor) throws XPathExpressionException {
		String xPathForAttributeUse = "/measure/subTreeLookUp/subTree//elementRef/attribute[@mode = 'Check if Present']";
		NodeList usedAttributeNodeList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xPathForAttributeUse);
		
		if(usedAttributeNodeList == null){
			return;
		}
		
		for(int j=0;j<usedAttributeNodeList.getLength();j++){
			Node attributeNode = usedAttributeNodeList.item(j);
			Node parentElementRefNode = attributeNode.getParentNode();
			String qdmNodeUUID = parentElementRefNode.getAttributes().getNamedItem(ID).getNodeValue();
			
			String xPathForQDM = "/measure/elementLookUp/qdm[@uuid='"+qdmNodeUUID+"']";
			Node qdmNode = simpleXmlprocessor.findNode(simpleXmlprocessor.getOriginalDoc(), xPathForQDM);
			
			if(qdmNode == null){
				continue;
			}
			
			//We need some way of letting the methods downstream know the name of this attribute w/o sending the <attribute> tag node.
			Node clonedAttributeNode = attributeNode.cloneNode(false);
			clonedAttributeNode.setUserData(ATTRIBUTE_NAME, attributeNode.getAttributes().getNamedItem(NAME).getNodeValue(), null);
			clonedAttributeNode.setUserData(ATTRIBUTE_MODE, attributeNode.getAttributes().getNamedItem("mode").getNodeValue(), null);
			
			createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor,clonedAttributeNode);
		}
	}
	
	/**
	 * Create xml for data criteria.
	 *
	 * @param qdmNode            the qdm node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode
	 * @return the string
	 */
	private void createXmlForDataCriteria(Node qdmNode, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor, Node attributeQDMNode) {
		String dataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		
		XmlProcessor templateXMLProcessor = TemplateXMLSingleton.getTemplateXmlProcessor();
		String xPathForTemplate = "/templates/template[text()='"
				+ dataType.toLowerCase() + "']";
		String actNodeStr = "";
		try {
			/*Node templateNode = (Node) xPath.evaluate(xPathForTemplate,
					templateXMLProcessor.getOriginalDoc(), XPathConstants.NODE)*/;
					Node templateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), xPathForTemplate);
					if (templateNode != null) {
						String attrClass = templateNode.getAttributes()
								.getNamedItem(CLASS).getNodeValue();
						String xpathForAct = "/templates/acts/act[@a_id='" + attrClass
								+ "']";
						Node actNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), xpathForAct);
						/*Node actNode = (Node) xPath.evaluate(xpathForAct,
								templateXMLProcessor.getOriginalDoc(), XPathConstants.NODE);*/
						if (actNode != null) {
							actNodeStr = actNode.getTextContent();
						}
						
						createDataCriteriaElementTag(actNodeStr, templateNode, qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor,templateXMLProcessor, attributeQDMNode);
					}
					
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the creates the data create element tag.
	 *
	 * @param actNodeStr            the act node str
	 * @param templateNode            the template node
	 * @param qdmNode            the qdm node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param templateXMLProcessor - templateXmlProcessor
	 * @param attributeQDMNode - Attribute QDM Node.
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void createDataCriteriaElementTag(String actNodeStr, Node templateNode,
			Node qdmNode, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor
			simpleXmlprocessor, XmlProcessor templateXMLProcessor, Node attributeQDMNode) throws XPathExpressionException {
		String oidValue = templateNode.getAttributes().getNamedItem(OID)
				.getNodeValue();
		String classCodeValue = templateNode.getAttributes().getNamedItem(CLASS)
				.getNodeValue();
		String moodValue = templateNode.getAttributes().getNamedItem(MOOD)
				.getNodeValue();
		String statusValue = templateNode.getAttributes().getNamedItem("status")
				.getNodeValue();
		String rootValue = qdmNode.getAttributes().getNamedItem(ID)
				.getNodeValue();
		String dataType = qdmNode.getAttributes().getNamedItem("datatype")
				.getNodeValue();
		String qdmOidValue = qdmNode.getAttributes().getNamedItem(OID)
				.getNodeValue();
		// Local variable changes.
		String qdmLocalVariableName = (StringUtils.deleteWhitespace(dataType) + "_" + UUIDUtilClient.uuid());
		String qdmName = qdmNode.getAttributes()
				.getNamedItem(NAME).getNodeValue();
		String qdmTaxonomy = qdmNode.getAttributes()
				.getNamedItem(TAXONOMY).getNodeValue();
		Element dataCriteriaSectionElem = (Element) dataCriteriaXMLProcessor
				.getOriginalDoc().getElementsByTagName("dataCriteriaSection")
				.item(0);
		Attr nameSpaceAttr = dataCriteriaXMLProcessor.getOriginalDoc()
				.createAttribute("xmlns:xsi");
		nameSpaceAttr.setNodeValue(nameSpace);
		dataCriteriaSectionElem.setAttributeNodeNS(nameSpaceAttr);
		// creating Entry Tag
		Element entryElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement("entry");
		entryElem.setAttribute(TYPE_CODE, "DRIV");
		dataCriteriaSectionElem.appendChild(entryElem);
		// creating LocalVariableName Tag
		Element localVarElem = dataCriteriaXMLProcessor
				.getOriginalDoc().createElement("localVariableName");
		localVarElem.setAttribute(VALUE, qdmLocalVariableName);
		entryElem.appendChild(localVarElem);
		Element dataCriteriaElem = dataCriteriaXMLProcessor
				.getOriginalDoc().createElement(actNodeStr);
		entryElem.appendChild(dataCriteriaElem);
		dataCriteriaElem.setAttribute(CLASS_CODE, classCodeValue);
		dataCriteriaElem.setAttribute(MOOD_CODE, moodValue);
		Element templateId = dataCriteriaXMLProcessor
				.getOriginalDoc().createElement(TEMPLATE_ID);
		dataCriteriaElem.appendChild(templateId);
		Element itemChild = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(ITEM);
		itemChild.setAttribute(ROOT, oidValue);
		templateId.appendChild(itemChild);
		Element idElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(ID);
		idElem.setAttribute(ROOT, rootValue);
		idElem.setAttribute("extension", StringUtils.deleteWhitespace(qdmName+"_"+qdmLocalVariableName));
		dataCriteriaElem.appendChild(idElem);
		//Participant attribute check in templates.xml.
		boolean isPart = templateNode.getAttributes().getNamedItem("isPart") != null;
		if (!isPart) {
			Element codeElement = createCodeForDatatype(templateNode,
					dataCriteriaXMLProcessor);
			if (codeElement != null) {
				dataCriteriaElem.appendChild(codeElement);
			}
		} else  {
			Element codeElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(CODE);
			Node valueTypeAttr = templateNode.getAttributes().getNamedItem("valueType");
			if (valueTypeAttr != null) {
				codeElem.setAttribute(XSI_TYPE, valueTypeAttr.getNodeValue());
			}
			codeElem.setAttribute("valueSet", qdmOidValue);
			Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			displayNameElem.setAttribute(VALUE, qdmName+" "+qdmTaxonomy+" Value Set");
			codeElem.appendChild(displayNameElem);
			dataCriteriaElem.appendChild(codeElem);
		}
		Element titleElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(TITLE);
		titleElem.setAttribute(VALUE, dataType);
		dataCriteriaElem.appendChild(titleElem);
		Element statusCodeElem = dataCriteriaXMLProcessor
				.getOriginalDoc().createElement("statusCode");
		statusCodeElem.setAttribute(CODE, statusValue);
		dataCriteriaElem.appendChild(statusCodeElem);
		if (!isPart) {
			Element valueElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(VALUE);
			Node valueTypeAttr = templateNode.getAttributes().getNamedItem("valueType");
			if (valueTypeAttr != null) {
				valueElem.setAttribute(XSI_TYPE, valueTypeAttr.getNodeValue());
			}
			valueElem.setAttribute("valueSet", qdmOidValue);
			Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			displayNameElem.setAttribute(VALUE, qdmName+" "+qdmTaxonomy+" Value Set");
			valueElem.appendChild(displayNameElem);
			dataCriteriaElem.appendChild(valueElem);
		} else {
			String subTemplateName = templateNode.getAttributes().getNamedItem("isPart").getNodeValue();
			NodeList subTemplateNode = templateXMLProcessor.findNodeList(templateXMLProcessor.getOriginalDoc(), "/templates/"
					+ subTemplateName + "/child::node()");
			for (int i = 0; i < subTemplateNode.getLength(); i++) {
				Node childNode = subTemplateNode.item(i);
				Node participantNode = dataCriteriaXMLProcessor.getOriginalDoc().importNode(childNode, true);
				dataCriteriaElem.appendChild(participantNode);
			}
		}
		//checkForAttributes
		if (attributeQDMNode != null) {
			createDataCriteriaForAttributes(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
		}
	}
	
	
	
	/**
	 * This method will look for attributes used in the subTree logic and then generate appropriate data criteria entries.
	 *
	 * @param childNode the child node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void createDataCriteriaForAttributes(Node childNode, Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor, Node attributeQDMNode) throws XPathExpressionException {
		
		String attributeName = (String) attributeQDMNode.getUserData(ATTRIBUTE_NAME);
		
		if(NEGATION_RATIONALE.equals(attributeName)){
			
			generateNegationRationalEntries(childNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
		}else{
			String attributeMode = (String) attributeQDMNode.getUserData(ATTRIBUTE_MODE);
			
			if(VALUE_SET.equals(attributeMode) || CHECK_IF_PRESENT.equals(attributeMode)){
				//handle "Value Set" and "Check If Present" mode
				generateOtherAttributes(childNode, dataCriteriaElem,
						dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
			}else {
				//TODO:handle other modes
			}
		}
	}
	
	/**
	 * Generate negation rational entries.
	 *
	 * @param qdmNode the qdm node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateNegationRationalEntries(Node qdmNode, Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node attributeQDMNode) throws XPathExpressionException {
		if(attributeQDMNode.getAttributes().getLength() > 0) {
			String attributeValueSetName = attributeQDMNode.getAttributes()
					.getNamedItem(NAME).getNodeValue();
			String attributeOID = attributeQDMNode.getAttributes()
					.getNamedItem(OID).getNodeValue();
			String attributeTaxonomy = attributeQDMNode.getAttributes()
					.getNamedItem(TAXONOMY).getNodeValue();
			String attribUUID = attributeQDMNode.getAttributes()
					.getNamedItem(UUID).getNodeValue();
			
			dataCriteriaElem.setAttribute("actionNegationInd", "true");
			
			Element outboundRelationshipElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(OUTBOUND_RELATIONSHIP);
			outboundRelationshipElem.setAttribute(TYPE_CODE, "RSON");
			
			Element observationCriteriaElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(OBSERVATION_CRITERIA);
			observationCriteriaElem.setAttribute(CLASS_CODE, "OBS");
			observationCriteriaElem.setAttribute(MOOD_CODE, "EVN");
			
			outboundRelationshipElem.appendChild(observationCriteriaElem);
			
			Element templateId = dataCriteriaXMLProcessor
					.getOriginalDoc().createElement(TEMPLATE_ID);
			observationCriteriaElem.appendChild(templateId);
			
			Element itemChild = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(ITEM);
			itemChild.setAttribute(ROOT, "2.16.840.1.113883.10.20.28.3.88");
			templateId.appendChild(itemChild);
			
			Element idElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(ID);
			idElem.setAttribute(ROOT, attribUUID);
			idElem.setAttribute("extension", StringUtils.deleteWhitespace(attributeValueSetName));
			observationCriteriaElem.appendChild(idElem);
			
			Element codeElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(CODE);
			codeElem.setAttribute(CODE, "410666004");
			codeElem.setAttribute(CODE_SYSTEM, "2.16.840.1.113883.6.96");
			
			Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			displayNameElem.setAttribute(VALUE, "Reason");
			
			observationCriteriaElem.appendChild(codeElem);
			codeElem.appendChild(displayNameElem);
			
			Element titleElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(TITLE);
			titleElem.setAttribute(VALUE, "Reason");
			observationCriteriaElem.appendChild(titleElem);
			
			Element valueElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(VALUE);
			valueElem.setAttribute(XSI_TYPE, "CD");
			valueElem.setAttribute("valueSet", attributeOID);
			
			Element valueDisplayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			valueDisplayNameElem.setAttribute(VALUE, attributeValueSetName+" "+attributeTaxonomy+" Value Set");
			
			valueElem.appendChild(valueDisplayNameElem);
			observationCriteriaElem.appendChild(valueElem);
			
			dataCriteriaElem.appendChild(outboundRelationshipElem);
		}
	}
	
	/**
	 * Generate other attribute entries.
	 *
	 * @param childNode the child node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode
	 * @throws XPathExpressionException
	 */
	private void generateOtherAttributes(Node qdmNode, Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node attributeQDMNode) throws XPathExpressionException {
		
		String attrName = (String) attributeQDMNode.getUserData(ATTRIBUTE_NAME);
		String attrMode = (String) attributeQDMNode.getUserData(ATTRIBUTE_MODE);
		
		XmlProcessor templateXMLProcessor = TemplateXMLSingleton.getTemplateXmlProcessor();
		Node templateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/template[text()='"
				+ attrName + "']");
		
		if(templateNode == null){
			return;
		}
		
		Element outboundRelationshipElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(OUTBOUND_RELATIONSHIP);
		outboundRelationshipElem.setAttribute(TYPE_CODE, templateNode.getAttributes().getNamedItem(TYPE).getNodeValue());
		
		Element observationCriteriaElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(OBSERVATION_CRITERIA);
		observationCriteriaElem.setAttribute(CLASS_CODE, templateNode.getAttributes().getNamedItem(CLASS).getNodeValue());
		observationCriteriaElem.setAttribute(MOOD_CODE, templateNode.getAttributes().getNamedItem(MOOD).getNodeValue());
		
		outboundRelationshipElem.appendChild(observationCriteriaElem);
		
		if(templateNode.getAttributes().getNamedItem(OID) != null){
			Element templateId = dataCriteriaXMLProcessor
					.getOriginalDoc().createElement(TEMPLATE_ID);
			observationCriteriaElem.appendChild(templateId);
			
			Element itemChild = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(ITEM);
			itemChild.setAttribute(ROOT, templateNode.getAttributes().getNamedItem(OID).getNodeValue());
			templateId.appendChild(itemChild);
		}
		
		Element idElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(ID);
		if(attributeQDMNode.getAttributes().getNamedItem(UUID) != null){
			idElem.setAttribute(ROOT, attributeQDMNode.getAttributes().getNamedItem(UUID).getNodeValue());
		}else if(attributeQDMNode.getAttributes().getNamedItem("attrUUID") != null){
			idElem.setAttribute(ROOT, attributeQDMNode.getAttributes().getNamedItem("attrUUID").getNodeValue());
		}
		String extensionId = idElem.getAttribute(ROOT);
		idElem.setAttribute("extension", StringUtils.deleteWhitespace(extensionId+"_"+attrName));
		observationCriteriaElem.appendChild(idElem);
		
		Element codeElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(CODE);
		codeElem.setAttribute(CODE, templateNode.getAttributes().getNamedItem(CODE).getNodeValue());
		codeElem.setAttribute(CODE_SYSTEM, templateNode.getAttributes().getNamedItem("codesystem").getNodeValue());
		
		Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(DISPLAY_NAME);
		if(templateNode.getAttributes().getNamedItem("displayNameValue") != null){
			displayNameElem.setAttribute(VALUE, templateNode.getAttributes().getNamedItem("displayNameValue").getNodeValue());
		}else{
			displayNameElem.setAttribute(VALUE, attrName);
		}
		
		observationCriteriaElem.appendChild(codeElem);
		codeElem.appendChild(displayNameElem);
		
		Element titleElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(TITLE);
		titleElem.setAttribute(VALUE, attrName);
		observationCriteriaElem.appendChild(titleElem);
		
		Element valueElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(VALUE);
		
		if(VALUE_SET.equals(attrMode)){
			String attributeValueSetName = attributeQDMNode.getAttributes()
					.getNamedItem(NAME).getNodeValue();
			String attributeOID = attributeQDMNode.getAttributes()
					.getNamedItem(OID).getNodeValue();
			String attributeTaxonomy = attributeQDMNode.getAttributes()
					.getNamedItem(TAXONOMY).getNodeValue();
			
			
			valueElem.setAttribute(XSI_TYPE, templateNode.getAttributes().getNamedItem("valueType").getNodeValue());
			valueElem.setAttribute("valueSet", attributeOID);
			
			Element valueDisplayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			valueDisplayNameElem.setAttribute(VALUE, attributeValueSetName+" "+attributeTaxonomy+" Value Set");
			
			valueElem.appendChild(valueDisplayNameElem);
		}else if(CHECK_IF_PRESENT.equals(attrMode)){
			valueElem.setAttribute(XSI_TYPE, "ANY");
			valueElem.setAttribute("flavorId", "ANY.NONNULL");
		}
		
		observationCriteriaElem.appendChild(valueElem);
		
		dataCriteriaElem.appendChild(outboundRelationshipElem);
		
	}
	
	/**

	 * Convert xml document to string.
	 * 
	 * @param document
	 *            the document
	 * @return the string
	 */
	private String convertXMLDocumentToString(Document document) {
		Transformer tf;
		Writer out = null;
		try {
			tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty(OutputKeys.STANDALONE, "yes");
			out = new StringWriter();
			tf.transform(new DOMSource(document), new StreamResult(out));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return out.toString();
	}
	
	/**
	 * This method removes top xml tag and xmlns from data critiera xml.
	 * @param xmlString - xml String.
	 * @return String.
	 */
	private String removeXmlTagNamespaceAndPreamble(String xmlString) {
		xmlString = xmlString.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim().
				replaceAll("(<\\?[^<]*\\?>)?", "")./* remove preamble */
				replaceAll("xmlns.*?(\"|\').*?(\"|\')", "") /* remove xmlns declaration */
				.replaceAll("(<)(\\w+:)(.*?>)", "$1$3") /* remove opening tag prefix */
				.replaceAll("(</)(\\w+:)(.*?>)", "$1$3"); /* remove closing tags prefix */
		return xmlString;
	}
	/**
	 * Adds the data criteria comment.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 */
	private void addDataCriteriaComment(XmlProcessor dataCriteriaXMLProcessor) {
		Element element = dataCriteriaXMLProcessor.getOriginalDoc().getDocumentElement();
		Comment comment = dataCriteriaXMLProcessor.getOriginalDoc().createComment(
				"Data Criteria Section");
		element.getParentNode().insertBefore(comment, element);
	}
	
	/**
	 * Creates the code for datatype.
	 *
	 * @param childNode the child node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @return the element
	 */
	private Element createCodeForDatatype(Node templateNode,
			XmlProcessor dataCriteriaXMLProcessor) {
		Node codeAttr = templateNode.getAttributes().getNamedItem(CODE);
		Node codeSystemAttr = templateNode.getAttributes().getNamedItem(
				CODE_SYSTEM);
		Element codeElement = null;
		if ((codeAttr != null) || (codeSystemAttr != null)) {
			codeElement = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(CODE);
			if (codeAttr != null) {
				codeElement.setAttribute(CODE,
						codeAttr.getNodeValue());
			}
			if (codeSystemAttr != null) {
				codeElement.setAttribute(CODE_SYSTEM,
						codeSystemAttr.getNodeValue());
			}
		}
		return codeElement;
	}
	
	
}
