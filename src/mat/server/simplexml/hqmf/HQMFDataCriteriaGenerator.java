package mat.server.simplexml.hqmf;

import javax.xml.xpath.XPathExpressionException;
import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Class HQMFDataCriteriaGenerator.
 */
public class HQMFDataCriteriaGenerator implements Generator {
	
	/**
	 * Generate hqm for measure.
	 *
	 * @param me            the me
	 * @return the string
	 * @throws Exception the exception
	 */
	@Override
	public String generate(MeasureExport me) throws Exception{
		
		String dataCriteria = "";
		dataCriteria = getHQMFXmlString(me);
		dataCriteria = removeXmlTagNamespaceAndPreamble(dataCriteria);
		return dataCriteria;
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
		return dataCriteriaXMLProcessor.transform(dataCriteriaXMLProcessor.getOriginalDoc(), true);
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
		itemChild.setAttribute("extension", VERSIONID);
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
			generateQDMEntries(dataCriteriaXMLProcessor, simpleXmlprocessor,
					qdmNoAttributeNodeList);
			
			NodeList qdmAttributeNodeList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xPathForQDMAttributes);
			generateQDMAttributeEntries(dataCriteriaXMLProcessor, simpleXmlprocessor,
					qdmAttributeNodeList);
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generate qdm entries.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param qdmNoAttributeNodeList the qdm no attribute node list
	 * @throws XPathExpressionException the x path expression exception
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
			if(elementRefList.getLength() > 0){
				createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor, null);
			}
		}
		
	}
	
	/**
	 * Generate qdm attribute entries.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param qdmAttributeNodeList the qdm attribute node list
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateQDMAttributeEntries(
			XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, NodeList qdmAttributeNodeList) throws XPathExpressionException {
		
		//		if(qdmAttributeNodeList == null){
		//			return;
		//		}
		
		if(qdmAttributeNodeList != null){
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
		}
		//Generate entries for "Check if Present", attributes
		generateNonValuesetAttribEntries(dataCriteriaXMLProcessor,simpleXmlprocessor);
		generateDateTimeAttributeEntries(dataCriteriaXMLProcessor, simpleXmlprocessor);
	}
	
	/**
	 * Generate negation rationale entries.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 * @param qdmUUID the qdm uuid
	 * @throws XPathExpressionException the x path expression exception
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
			Node clonedAttributeQDMNode = attributeQDMNode.cloneNode(false);
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_NAME, NEGATION_RATIONALE, null);
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_MODE, VALUE_SET, null);
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_UUID, attributeNode.getAttributes().getNamedItem("attrUUID").getNodeValue(), null);
			
			createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor,clonedAttributeQDMNode);
			
		}
	}
	
	/**
	 * Generate value set attrib entries.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 * @param qdmUUID the qdm uuid
	 * @param modeValue the mode value
	 * @throws XPathExpressionException the x path expression exception
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
			Node clonedAttributeQDMNode = attributeQDMNode.cloneNode(false);
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_NAME, attributeNode.getAttributes().getNamedItem(NAME).getNodeValue(), null);
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_MODE, attributeNode.getAttributes().getNamedItem("mode").getNodeValue(), null);
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_UUID, attributeNode.getAttributes().getNamedItem("attrUUID").getNodeValue(), null);
			
			createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor,clonedAttributeQDMNode);
		}
	}
	
	/**
	 * This method will look for attributes of mode =
	 * 'Check if Present', 'Equal To', 'Less Than (or Equal)', Greater than (or Equal).
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateNonValuesetAttribEntries(
			XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor) throws XPathExpressionException {
		String xPathForAttributeUse = "/measure/subTreeLookUp/subTree//elementRef/attribute[@mode = 'Check if Present' or @mode='Equal To' or starts-with(@mode,'Less Than') or starts-with(@mode, 'Greater Than')]"
				+ "[@name != 'negation rationale' and @name != '"+START_DATETIME+"' and @name !='"+STOP_DATETIME+"' " +"" +
				"and @name != '"+FACILITY_LOCATION_ARRIVAL_DATETIME +"' and @name != '"+FACILITY_LOCATION_DEPARTURE_DATETIME
				+"' and  @name != '"+FACILITY_LOCATION+"']";
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
			Node clonedAttributeQDMNode = attributeNode.cloneNode(false);
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_NAME, attributeNode.getAttributes().getNamedItem(NAME).getNodeValue(), null);
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_MODE, attributeNode.getAttributes().getNamedItem("mode").getNodeValue(), null);
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_UUID, attributeNode.getAttributes().getNamedItem("attrUUID").getNodeValue(), null);
			
			createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor,clonedAttributeQDMNode);
		}
	}
	
	/**
	 * Generate date time attribute entries.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateDateTimeAttributeEntries(
			XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor) throws XPathExpressionException {
		
		String xPathForAttributeUse = "/measure/subTreeLookUp/subTree//elementRef/attribute"
				+ "[@name = '"+START_DATETIME+"' or @name='"+STOP_DATETIME +"' or @name = '"+FACILITY_LOCATION_ARRIVAL_DATETIME +"' or @name = '"+FACILITY_LOCATION_DEPARTURE_DATETIME
				+"' or @name = '"+FACILITY_LOCATION+"']"
				+ "[@mode='Equal To' or starts-with(@mode,'Less Than') or starts-with(@mode, 'Greater Than') or @mode='Check if Present']";
		
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
			Node clonedAttributeQDMNode = attributeNode.cloneNode(false);
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_NAME, attributeNode.getAttributes().getNamedItem(NAME).getNodeValue(), null);
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_MODE, attributeNode.getAttributes().getNamedItem("mode").getNodeValue(), null);
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_UUID, attributeNode.getAttributes().getNamedItem("attrUUID").getNodeValue(), null);
			String attrDate = "";
			if(attributeNode.getAttributes().getNamedItem("attrDate") != null){
				attrDate = attributeNode.getAttributes().getNamedItem("attrDate").getNodeValue();
			}
			clonedAttributeQDMNode.setUserData(ATTRIBUTE_DATE, attrDate, null);
			
			createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor,clonedAttributeQDMNode);
		}
	}
	
	/**
	 * Create xml for data criteria.
	 *
	 * @param qdmNode            the qdm node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 * @return the string
	 */
	private void createXmlForDataCriteria(Node qdmNode, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor, Node attributeQDMNode) {
		String dataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		
		XmlProcessor templateXMLProcessor = TemplateXMLSingleton.getTemplateXmlProcessor();
		String xPathForTemplate = "/templates/template[text()='"
				+ dataType.toLowerCase() + "']";
		String actNodeStr = "";
		try {
			
			Node templateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), xPathForTemplate);
			if (templateNode != null) {
				String attrClass = templateNode.getAttributes()
						.getNamedItem(CLASS).getNodeValue();
				String xpathForAct = "/templates/acts/act[@a_id='" + attrClass
						+ "']";
				Node actNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), xpathForAct);
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
		
		String qdmName = qdmNode.getAttributes()
				.getNamedItem(NAME).getNodeValue();
		String entryCommentText = dataType;
		// Local variable changes.
		//String qdmLocalVariableName = (qdmName + "_" + StringUtils.deleteWhitespace(dataType) + "_" + UUIDUtilClient.uuid());
		String qdmLocalVariableName = StringUtils.deleteWhitespace(qdmName + "_" + dataType);
		if(attributeQDMNode != null){
			if(attributeQDMNode.getUserData(ATTRIBUTE_UUID) != null){
				qdmLocalVariableName = (String)attributeQDMNode.getUserData(ATTRIBUTE_UUID);
			}
			if(attributeQDMNode.getUserData(ATTRIBUTE_NAME) != null){
				entryCommentText = entryCommentText+ " - " +attributeQDMNode.getUserData(ATTRIBUTE_NAME);
			}
			if(attributeQDMNode.getUserData(ATTRIBUTE_MODE) != null){
				entryCommentText = entryCommentText+ " With " +attributeQDMNode.getUserData(ATTRIBUTE_MODE);
			}
		}
		
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
		
		addCommentNode(dataCriteriaXMLProcessor, entryCommentText, entryElem);
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
		itemChild.setAttribute("extension", VERSIONID);
		templateId.appendChild(itemChild);
		Element idElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(ID);
		idElem.setAttribute(ROOT, rootValue);
		idElem.setAttribute("extension", qdmLocalVariableName);
		dataCriteriaElem.appendChild(idElem);
		
		String isAddCodeTag = templateNode.getAttributes().getNamedItem("addCodeTag").getNodeValue();
		if ("true".equalsIgnoreCase(isAddCodeTag)) { // Add Code Element to DataCriteria Element.
			addCodeElementToDataCriteriaElement(templateNode, dataCriteriaXMLProcessor
					, dataType, qdmOidValue, qdmName, qdmTaxonomy, dataCriteriaElem);
		}
		Element titleElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(TITLE);
		titleElem.setAttribute(VALUE, dataType);
		dataCriteriaElem.appendChild(titleElem);
		Element statusCodeElem = dataCriteriaXMLProcessor
				.getOriginalDoc().createElement("statusCode");
		statusCodeElem.setAttribute(CODE, statusValue);
		dataCriteriaElem.appendChild(statusCodeElem);
		// Add value tag in entry element.
		String addValueSetElement = templateNode.getAttributes().getNamedItem("addValueTag").getNodeValue();
		if ("true".equalsIgnoreCase(addValueSetElement)) {
			Element valueElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(VALUE);
			Node valueTypeAttr = templateNode.getAttributes().getNamedItem("valueType");
			if (valueTypeAttr != null) {
				valueElem.setAttribute(XSI_TYPE, valueTypeAttr.getNodeValue());
			}
			
			Node valueCodeSystem = templateNode.getAttributes().getNamedItem("valueCodeSystem");
			Node valueCode = templateNode.getAttributes().getNamedItem("valueCode");
			Node valueDisplayName = templateNode.getAttributes().getNamedItem("valueDisplayName");
			Node valueCodeSystemName = templateNode.getAttributes().getNamedItem("valueCodeSystemName");
			
			Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			
			if((valueCode != null) && (valueCodeSystem != null)){
				valueElem.setAttribute("code", valueCode.getNodeValue());
				valueElem.setAttribute("codeSystem", valueCodeSystem.getNodeValue());
				if(valueCodeSystemName!=null){
					valueElem.setAttribute("codeSystemName", valueCodeSystemName.getNodeValue());
				}
				if (valueDisplayName != null) {
					displayNameElem.setAttribute(VALUE, valueDisplayName.getNodeValue());
				}
			}else{
				valueElem.setAttribute("valueSet", qdmOidValue);
				displayNameElem.setAttribute(VALUE, qdmName+" "+qdmTaxonomy+" Value Set");
			}
			if(displayNameElem.hasAttribute(VALUE)){
				valueElem.appendChild(displayNameElem);
			}
			dataCriteriaElem.appendChild(valueElem);
		}
		if(templateNode.getAttributes().getNamedItem("includeSubTemplate") !=null){
			appendSubTemplateNode(templateNode, dataCriteriaXMLProcessor, templateXMLProcessor, dataCriteriaElem, qdmNode);
		}
		//checkForAttributes
		if (attributeQDMNode != null) {
			createDataCriteriaForAttributes(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
		}
		
	}
	
	/**
	 * Add Code Element To data Criteria Element based on condition.
	 * @param templateNode - Node
	 * @param dataCriteriaXMLProcessor - XmlProcessor
	 * @param dataType - String
	 * @param qdmOidValue - String
	 * @param qdmName - String
	 * @param qdmTaxonomy - String
	 * @param dataCriteriaElem - Element
	 */
	private void addCodeElementToDataCriteriaElement(Node templateNode, XmlProcessor dataCriteriaXMLProcessor, String dataType,
			String qdmOidValue, String qdmName, String qdmTaxonomy, Element dataCriteriaElem) {
		//Participant data type check in templates.xml.
		boolean isPart = templateNode.getAttributes().getNamedItem("isPart") != null;
		//Patient Characteristic data type - contains code tag with valueSetId attribute and no title and value set tag.
		boolean isPatientChar = templateNode.getAttributes().getNamedItem("valueSetId") != null;
		//Functional status data type - contains code tag with valueSetId attribute and no title and value set tag.
		boolean isFunctional = templateNode.getAttributes().getNamedItem("isFunctional") != null;
		//Functional status data type - contains code tag with valueSetId attribute and no title and value set tag.
		boolean isEncounter = templateNode.getAttributes().getNamedItem("isEncounter") != null;
		boolean isIntervention = ("Intervention, Order".equals(dataType) || "Intervention, Performed".equals(dataType) || "Intervention, Recommended".equals(dataType));
		boolean isLaboratoryTest = ("Laboratory Test, Order".equals(dataType) || "Laboratory Test, Performed".equals(dataType) || "Laboratory Test, Recommended".equals(dataType));
		
		if (isPart || isFunctional || isLaboratoryTest || isEncounter)  {
			Element codeElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(CODE);
			Node valueTypeAttr = templateNode.getAttributes().getNamedItem("valueType");
			if (valueTypeAttr != null) {
				codeElem.setAttribute(XSI_TYPE, valueTypeAttr.getNodeValue());
			}
			codeElem.setAttribute("valueSet", qdmOidValue);
			Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			Node codeDisplayName = templateNode.getAttributes().getNamedItem(CODE_SYSTEM_DISPLAY_NAME);
			String displayName = "";
			if(codeDisplayName!=null){
				displayName = codeDisplayName.getNodeValue();
			} else {
				displayName = qdmName+" "+qdmTaxonomy+" Value Set";
			}
			displayNameElem.setAttribute(VALUE, displayName);
			codeElem.appendChild(displayNameElem);
			dataCriteriaElem.appendChild(codeElem);
			
		} else if (isPatientChar) {
			Element codeElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(CODE);
			codeElem.setAttribute(templateNode.getAttributes().getNamedItem("valueSetId").getNodeValue(), qdmOidValue);
			Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			displayNameElem.setAttribute(VALUE, qdmName+" "+qdmTaxonomy+" Value Set");
			codeElem.appendChild(displayNameElem);
			dataCriteriaElem.appendChild(codeElem);
			/*Element titleElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(TITLE);
			titleElem.setAttribute(VALUE, dataType);
			dataCriteriaElem.appendChild(titleElem);*/
		} else if(isIntervention){
			Element codeElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(CODE);
			codeElem.setAttribute("valueSet", qdmOidValue);
			Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			displayNameElem.setAttribute(VALUE, qdmName+" "+qdmTaxonomy+" Value Set");
			codeElem.appendChild(displayNameElem);
			dataCriteriaElem.appendChild(codeElem);
			/*Element titleElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(TITLE);
			titleElem.setAttribute(VALUE, dataType);
			dataCriteriaElem.appendChild(titleElem);*/
		}else {
			Element codeElement = createCodeForDatatype(templateNode,
					dataCriteriaXMLProcessor);
			if (codeElement != null) {
				dataCriteriaElem.appendChild(codeElement);
			}
			/*Element titleElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(TITLE);
			titleElem.setAttribute(VALUE, dataType);
			dataCriteriaElem.appendChild(titleElem);*/
		}
	}
	
	/**
	 * Add SubTemplate defined in Template.xml to data criteria Element.
	 *
	 * @param templateNode - Node
	 * @param dataCriteriaXMLProcessor - XmlProcessor for Data Criteria
	 * @param templateXMLProcessor -XmlProcessor for Template Xml.
	 * @param dataCriteriaElem - Element
	 * @param qdmNode the qdm node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void appendSubTemplateNode(Node templateNode, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor templateXMLProcessor,
			Element dataCriteriaElem, Node qdmNode) throws XPathExpressionException {
		String subTemplateName = templateNode.getAttributes().getNamedItem("includeSubTemplate").getNodeValue();
		Node  subTemplateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/"
				+ subTemplateName);
		NodeList subTemplateNodeChilds = templateXMLProcessor.findNodeList(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/"
				+ subTemplateName + "/child::node()");
		String qdmOidValue = qdmNode.getAttributes().getNamedItem(OID)
				.getNodeValue();
		String qdmName = qdmNode.getAttributes().getNamedItem(NAME).getNodeValue();
		String qdmNameDataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		if(subTemplateNode.getAttributes().getNamedItem("changeAttribute") != null) {
			String[] attributeToBeModified = subTemplateNode.getAttributes().getNamedItem("changeAttribute").getNodeValue().split(",");
			
			for (String changeAttribute : attributeToBeModified) {
				Node  attributedToBeChangedInNode = null;
				attributedToBeChangedInNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/"
						+ subTemplateName+"//"+changeAttribute);
				
				if (changeAttribute.equalsIgnoreCase(ID)) {
					attributedToBeChangedInNode.getAttributes().getNamedItem("root").setNodeValue(UUIDUtilClient.uuid());
				} else if (changeAttribute.equalsIgnoreCase(CODE)) {
					attributedToBeChangedInNode.getAttributes().getNamedItem("valueSet").setNodeValue(qdmOidValue);
				} else if(changeAttribute.equalsIgnoreCase(DISPLAY_NAME)){
					attributedToBeChangedInNode.getAttributes().getNamedItem("value").setNodeValue(qdmName + " " + qdmNameDataType + " value set");
				}
			}
		}
		for (int i = 0; i < subTemplateNodeChilds.getLength(); i++) {
			Node childNode = subTemplateNodeChilds.item(i);
			Node nodeToAttach = dataCriteriaXMLProcessor.getOriginalDoc().importNode(childNode, true);
			dataCriteriaElem.appendChild(nodeToAttach);
		}
	}
	
	/**
	 * Method to append Facility Location attribute template to data type. This attribute can only have value ser
	 * and Check If present mode's and these are added to code tag.
	 *
	 * @param templateNode the template node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param templateXMLProcessor the template xml processor
	 * @param dataCriteriaElem the data criteria elem
	 * @param attrNode the attr node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void appendSubTemplateInFacilityAttribute(Node templateNode, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor templateXMLProcessor,
			Element dataCriteriaElem, Node attrNode) throws XPathExpressionException{
		String subTemplateName = templateNode.getAttributes().getNamedItem("includeSubTemplate").getNodeValue();
		Node  subTemplateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/"
				+ subTemplateName);
		NodeList subTemplateNodeChilds = templateXMLProcessor.findNodeList(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/"
				+ subTemplateName + "/child::node()");
		if(subTemplateNode.getAttributes().getNamedItem("changeAttribute") != null) {
			String[] tagToBeModified = subTemplateNode.getAttributes().getNamedItem("changeAttribute").getNodeValue().split(",");
			for (String changeAttribute : tagToBeModified) {
				Node  attributedToBeChangedInNode = null;
				attributedToBeChangedInNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/"
						+ subTemplateName+"//"+changeAttribute);
				if(changeAttribute.equalsIgnoreCase(ID)){
					attributedToBeChangedInNode.getAttributes().getNamedItem("root").setNodeValue(UUIDUtilClient.uuid());
				} else if(changeAttribute.equalsIgnoreCase(CODE)){
					String attrMode = (String) attrNode.getUserData(ATTRIBUTE_MODE);
					if(VALUE_SET.equals(attrMode)){
						if(attributedToBeChangedInNode.hasAttributes()){
							((Element)attributedToBeChangedInNode).removeAttribute("flavorId");
							((Element)attributedToBeChangedInNode).removeAttribute("xsi:type");
						}
						if(attributedToBeChangedInNode.hasChildNodes()){
							((Element)attributedToBeChangedInNode).removeChild(attributedToBeChangedInNode.getFirstChild());
						}
						checkIfSelectedModeIsValueSet(templateXMLProcessor, attrNode, false, subTemplateNode, (Element)attributedToBeChangedInNode);
					} else if(CHECK_IF_PRESENT.equals(attrMode)){
						if(attributedToBeChangedInNode.hasAttributes()){
							((Element)attributedToBeChangedInNode).removeAttribute("valueSet");
						}
						if(attributedToBeChangedInNode.hasChildNodes()){
							((Element)attributedToBeChangedInNode).removeChild(attributedToBeChangedInNode.getFirstChild());
						}
						checkIfSelectedModeIsPresent(templateXMLProcessor, attrNode, subTemplateNode, (Element)attributedToBeChangedInNode);
						((Element)attributedToBeChangedInNode).removeAttribute("xsi:type");
					}
				}
			}
			for (int i = 0; i < subTemplateNodeChilds.getLength(); i++) {
				Node childNode = subTemplateNodeChilds.item(i);
				Node nodeToAttach = dataCriteriaXMLProcessor.getOriginalDoc().importNode(childNode, true);
				dataCriteriaElem.appendChild(nodeToAttach);
			}
		}
	}
	
	/**
	 * This method will look for attributes used in the subTree logic and then generate appropriate data criteria entries.
	 *
	 * @param qdmNode the qdm node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void createDataCriteriaForAttributes(Node qdmNode, Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor, Node attributeQDMNode
			) throws XPathExpressionException {
		
		String attributeName = (String) attributeQDMNode.getUserData(ATTRIBUTE_NAME);
		String attributeMode = (String) attributeQDMNode.getUserData(ATTRIBUTE_MODE);
		if(NEGATION_RATIONALE.equals(attributeName)){
			generateNegationRationalEntries(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
		}else if(START_DATETIME.equals(attributeName) || STOP_DATETIME.equals(attributeName)){
			generateDateTimeAttributes(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
		}else if(FACILITY_LOCATION_ARRIVAL_DATETIME.equalsIgnoreCase(attributeName)
				|| FACILITY_LOCATION_DEPARTURE_DATETIME.equalsIgnoreCase(attributeName)) {
			generateFacilityLocationTypeAttributes(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
			
		}else if(DOSE.equalsIgnoreCase(attributeName)){
			generateDoseTypeAttributes(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
			
		}else if(VALUE_SET.equals(attributeMode) || CHECK_IF_PRESENT.equals(attributeMode) || attributeMode.startsWith(LESS_THAN) || attributeMode.startsWith(GREATER_THAN) || EQUAL_TO.equals(attributeMode)){
			//handle "Value Set", "Check If Present" and comparison(less than, greater than, equals) mode
			generateOtherAttributes(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
		}
	}
	
	/**
	 * Generate dose type attributes.
	 *
	 * @param qdmNode the qdm node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 */
	private void generateDoseTypeAttributes(Node qdmNode,
			Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node attributeQDMNode) {
		String attrMode = (String) attributeQDMNode.getUserData(ATTRIBUTE_MODE);
		Node attrOID = attributeQDMNode.getAttributes().getNamedItem("oid");
		Node participationElem = dataCriteriaElem.getElementsByTagName("participation").item(0);
		Node parentNode = participationElem.getParentNode();
		Element doseQuantityTag = dataCriteriaElem.getOwnerDocument().createElement(DOSE_QUANTITY);
		Node unitAttrib = attributeQDMNode.getAttributes().getNamedItem("unit");
		if(CHECK_IF_PRESENT.equals(attrMode)){
			doseQuantityTag.setAttribute(FLAVOR_ID, "ANY.NONNULL");
		}  else if(VALUE_SET.equals(attrMode)){
			doseQuantityTag.setAttribute(FLAVOR_ID, "UNK");
			Element translationNode = dataCriteriaElem.getOwnerDocument().createElement(TRANSLATION);
			translationNode.setAttribute("valueSet", attrOID.getNodeValue());
			Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			displayNameElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem(NAME).getNodeValue()
					+ " " + attributeQDMNode.getAttributes().getNamedItem(TAXONOMY).getNodeValue() + " Value Set");
			translationNode.appendChild(displayNameElem);
			doseQuantityTag.appendChild(translationNode);
		} else if(attrMode.startsWith(Generator.LESS_THAN) || attrMode.startsWith(Generator.GREATER_THAN) || attrMode.equals(Generator.EQUAL_TO)){
			if(attrMode.equals(Generator.EQUAL_TO)){
				doseQuantityTag.setAttribute("value", attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				if(unitAttrib!=null){
					doseQuantityTag.setAttribute("unit", unitAttrib.getNodeValue());
				}
			} else if(attrMode.startsWith(Generator.LESS_THAN)){
				Element uncertainRangeNode=  dataCriteriaElem.getOwnerDocument().createElement("uncertainRange");
				if(attrMode.equals(Generator.LESS_THAN)){
					uncertainRangeNode.setAttribute("highClosed", "false");
				}
				Element lowNode = dataCriteriaElem.getOwnerDocument().createElement(LOW);
				lowNode.setAttribute("nullFlavor", "NINF");
				Element highNode = dataCriteriaElem.getOwnerDocument().createElement(HIGH);
				highNode.setAttribute("xsi:type", "PQ");
				highNode.setAttribute("value", attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				if(unitAttrib!=null){
					highNode.setAttribute("unit", unitAttrib.getNodeValue());
				}
				uncertainRangeNode.appendChild(lowNode);
				uncertainRangeNode.appendChild(highNode);
				doseQuantityTag.appendChild(uncertainRangeNode);
				
			} else if(attrMode.startsWith(Generator.GREATER_THAN)){
				Element uncertainRangeNode=  dataCriteriaElem.getOwnerDocument().createElement("uncertainRange");
				if(attrMode.equals(Generator.GREATER_THAN)){
					uncertainRangeNode.setAttribute("lowClosed", "false");
				}
				Element lowNode = dataCriteriaElem.getOwnerDocument().createElement(LOW);
				lowNode.setAttribute("nullFlavor", "NINF");
				Element highNode = dataCriteriaElem.getOwnerDocument().createElement(HIGH);
				highNode.setAttribute("xsi:type", "PQ");
				highNode.setAttribute("value", attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				if(unitAttrib!=null){
					highNode.setAttribute("unit", unitAttrib.getNodeValue());
				}
				uncertainRangeNode.appendChild(lowNode);
				uncertainRangeNode.appendChild(highNode);
				doseQuantityTag.appendChild(uncertainRangeNode);
			}
		}
		parentNode.insertBefore(doseQuantityTag, participationElem);
		
	}
	
	/**
	 * Generate facility location type attributes.
	 *
	 * @param qdmNode the qdm node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateFacilityLocationTypeAttributes(Node qdmNode, Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor, Node attributeQDMNode) throws XPathExpressionException {
		String attributeName = (String) attributeQDMNode.getUserData(ATTRIBUTE_NAME);
		XmlProcessor templateXMLProcessor = TemplateXMLSingleton.getTemplateXmlProcessor();
		Node templateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/template[text()='"
				+ attributeName.toLowerCase() + "']");
		if(templateNode == null){
			return;
		}
		if(templateNode.getAttributes().getNamedItem("includeSubTemplate") !=null){
			appendSubTemplateNode(templateNode, dataCriteriaXMLProcessor, templateXMLProcessor, dataCriteriaElem, qdmNode);
		}
		generateDateTimeAttributes(qdmNode, dataCriteriaElem,
				dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
	}
	
	/**
	 * Generate negation rational entries.
	 *
	 * @param qdmNode the qdm node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateNegationRationalEntries(Node qdmNode, Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node attributeQDMNode) throws XPathExpressionException {
		if(attributeQDMNode.getAttributes().getLength() > 0) {
			
			String attrName = (String) attributeQDMNode.getUserData(ATTRIBUTE_NAME);
			String attribUUID = (String)attributeQDMNode.getUserData(ATTRIBUTE_UUID);
			
			XmlProcessor templateXMLProcessor = TemplateXMLSingleton.getTemplateXmlProcessor();
			Node templateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/template[text()='"
					+ attrName + "']");
			System.out.println("----------");
			System.out.println(attributeQDMNode.getNodeName());
			System.out.println(attributeQDMNode.getAttributes());
			System.out.println(simpleXmlprocessor.transform(attributeQDMNode));
			System.out.println("----------");
			String attributeValueSetName = attributeQDMNode.getAttributes()
					.getNamedItem(NAME).getNodeValue();
			String attributeOID = attributeQDMNode.getAttributes()
					.getNamedItem(OID).getNodeValue();
			String attributeTaxonomy = attributeQDMNode.getAttributes()
					.getNamedItem(TAXONOMY).getNodeValue();
			dataCriteriaElem.setAttribute("actionNegationInd", "true");
			
			Element outboundRelationshipElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(OUTBOUND_RELATIONSHIP);
			outboundRelationshipElem.setAttribute(TYPE_CODE, templateNode.getAttributes().getNamedItem(TYPE).getNodeValue());
			
			Element observationCriteriaElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(OBSERVATION_CRITERIA);
			observationCriteriaElem.setAttribute(CLASS_CODE, templateNode.getAttributes().getNamedItem(CLASS).getNodeValue());
			observationCriteriaElem.setAttribute(MOOD_CODE, templateNode.getAttributes().getNamedItem(MOOD).getNodeValue());
			
			outboundRelationshipElem.appendChild(observationCriteriaElem);
			
			Element templateId = dataCriteriaXMLProcessor
					.getOriginalDoc().createElement(TEMPLATE_ID);
			observationCriteriaElem.appendChild(templateId);
			
			Element itemChild = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(ITEM);
			itemChild.setAttribute(ROOT, templateNode.getAttributes().getNamedItem(OID).getNodeValue());
			itemChild.setAttribute("extension", VERSIONID);
			templateId.appendChild(itemChild);
			
			Element idElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(ID);
			idElem.setAttribute(ROOT, attribUUID);
			idElem.setAttribute("extension", StringUtils.deleteWhitespace(attributeValueSetName));
			observationCriteriaElem.appendChild(idElem);
			
			Element codeElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(CODE);
			codeElem.setAttribute(CODE, templateNode.getAttributes().getNamedItem(CODE).getNodeValue());
			codeElem.setAttribute(CODE_SYSTEM, templateNode.getAttributes().getNamedItem(CODE_SYSTEM).getNodeValue());
			
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
			valueElem.setAttribute(XSI_TYPE, templateNode.getAttributes().getNamedItem("valueType").getNodeValue());
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
	 * @param qdmNode the qdm node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateOtherAttributes(Node qdmNode, Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node attributeQDMNode) throws XPathExpressionException {
		
		String attrName = (String) attributeQDMNode.getUserData(ATTRIBUTE_NAME);
		String attrMode = (String) attributeQDMNode.getUserData(ATTRIBUTE_MODE);
		String attribUUID = (String)attributeQDMNode.getUserData(ATTRIBUTE_UUID);
		boolean isResultOrStatus = ("status".equalsIgnoreCase(attrName) || "result".equalsIgnoreCase(attrName));
		XmlProcessor templateXMLProcessor = TemplateXMLSingleton.getTemplateXmlProcessor();
		Node templateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/template[text()='"
				+ attrName.toLowerCase() + "']");
		
		if(templateNode == null){
			templateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/template[text()='"
					+ attrName.toLowerCase()+"-" +attrMode.toLowerCase() + "']");
			if(templateNode == null) {
				return;
			} else {
				if (ANATOMICAL_LOCATION_SITE.equalsIgnoreCase(attrName)) {
					addTargetSiteOrPriorityCodeOrRouteCodeElement(dataCriteriaElem, dataCriteriaXMLProcessor, attributeQDMNode, templateNode);
				} else if(LATERALITY.equalsIgnoreCase(attrName)){
					appendSubTemplateNode(templateNode, dataCriteriaXMLProcessor, templateXMLProcessor, dataCriteriaElem,null);
				} else if(ORDINALITY.equalsIgnoreCase(attrName)){
					addTargetSiteOrPriorityCodeOrRouteCodeElement(dataCriteriaElem, dataCriteriaXMLProcessor, attributeQDMNode, templateNode);
				}else if (ROUTE.equalsIgnoreCase(attrName)){
					addTargetSiteOrPriorityCodeOrRouteCodeElement(dataCriteriaElem, dataCriteriaXMLProcessor, attributeQDMNode, templateNode);
				} else if("method".equalsIgnoreCase(attrName)){
					addTargetSiteOrPriorityCodeOrRouteCodeElement(dataCriteriaElem, dataCriteriaXMLProcessor, attributeQDMNode, templateNode);
				}
				return;
			}
		}
		if (attrName.equalsIgnoreCase(FACILITY_LOCATION)) {
			if (templateNode.getAttributes().getNamedItem("includeSubTemplate") !=null) {
				appendSubTemplateInFacilityAttribute(templateNode, dataCriteriaXMLProcessor, templateXMLProcessor, dataCriteriaElem, attributeQDMNode);
			}
			return;
		}
		Element outboundRelationshipElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(OUTBOUND_RELATIONSHIP);
		outboundRelationshipElem.setAttribute(TYPE_CODE, templateNode.getAttributes().getNamedItem(TYPE).getNodeValue());
		
		Node invAttribNode = templateNode.getAttributes().getNamedItem("inv");
		if(invAttribNode != null){
			outboundRelationshipElem.setAttribute("inversionInd", invAttribNode.getNodeValue());
		}
		
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
			itemChild.setAttribute("extension", VERSIONID);
			templateId.appendChild(itemChild);
		}
		
		Element idElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(ID);
		idElem.setAttribute(ROOT, attribUUID);
		idElem.setAttribute("extension", StringUtils.deleteWhitespace(attrName));
		observationCriteriaElem.appendChild(idElem);
		Element codeElem = createCodeForDatatype(templateNode, dataCriteriaXMLProcessor);
		Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(DISPLAY_NAME);
		if(templateNode.getAttributes().getNamedItem("displayNameValue") != null){
			displayNameElem.setAttribute(VALUE, templateNode.getAttributes().getNamedItem("displayNameValue").getNodeValue());
		}else{
			displayNameElem.setAttribute(VALUE, attrName);
		}
		if (codeElem != null) {
			observationCriteriaElem.appendChild(codeElem);
			codeElem.appendChild(displayNameElem);
		}
		Element titleElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(TITLE);
		titleElem.setAttribute(VALUE, attrName);
		observationCriteriaElem.appendChild(titleElem);
		
		Element valueElem =  dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(VALUE);
		if(VALUE_SET.equals(attrMode)){
			checkIfSelectedModeIsValueSet(dataCriteriaXMLProcessor, attributeQDMNode, isResultOrStatus, templateNode,valueElem);
		} else if(CHECK_IF_PRESENT.equalsIgnoreCase(attrMode)){
			checkIfSelectedModeIsPresent(dataCriteriaXMLProcessor, attributeQDMNode, templateNode, valueElem);
		}else if(EQUAL_TO.equals(attrMode) || attrMode.startsWith(LESS_THAN) || attrMode.startsWith(GREATER_THAN)){
			checkIfSelectedModeIsArthimaticExpr(dataCriteriaXMLProcessor, attributeQDMNode, templateNode,valueElem);
		}
		
		observationCriteriaElem.appendChild(valueElem);
		dataCriteriaElem.appendChild(outboundRelationshipElem);
		
	}
	
	/**
	 * Check if selected mode is value set.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param attributeQDMNode the attribute qdm node
	 * @param isResultOrStatus the is result or status
	 * @param templateNode the template node
	 * @param valueElem the value elem
	 * @return the element
	 */
	private Element checkIfSelectedModeIsValueSet(XmlProcessor dataCriteriaXMLProcessor, Node attributeQDMNode, boolean isResultOrStatus,
			Node templateNode,Element valueElem) {
		String attributeValueSetName = attributeQDMNode.getAttributes()
				.getNamedItem(NAME).getNodeValue();
		String attributeOID = attributeQDMNode.getAttributes()
				.getNamedItem(OID).getNodeValue();
		String attributeTaxonomy = attributeQDMNode.getAttributes()
				.getNamedItem(TAXONOMY).getNodeValue();
		
		if(templateNode.getAttributes().getNamedItem("valueType") !=null) {
			valueElem.setAttribute(XSI_TYPE, templateNode.getAttributes().getNamedItem("valueType").getNodeValue());
		}
		
		valueElem.setAttribute("valueSet", attributeOID);
		if(!isResultOrStatus){
			Element valueDisplayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			valueDisplayNameElem.setAttribute(VALUE, attributeValueSetName+" "+attributeTaxonomy+" Value Set");
			
			valueElem.appendChild(valueDisplayNameElem);
		}
		
		return valueElem;
	}
	
	
	/**
	 * Check if selected mode is present.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param attributeQDMNode the attribute qdm node
	 * @param templateNode the template node
	 * @param valueElem the value elem
	 * @return the element
	 */
	private Element checkIfSelectedModeIsPresent(XmlProcessor dataCriteriaXMLProcessor, Node attributeQDMNode,
			Node templateNode,Element valueElem){
		valueElem.setAttribute(XSI_TYPE, "ANY");
		valueElem.setAttribute(FLAVOR_ID, "ANY.NONNULL");
		return valueElem;
	}
	
	/**
	 * Check if selected mode is arthimatic expr.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param attributeQDMNode the attribute qdm node
	 * @param templateNode the template node
	 * @param valueElem the value elem
	 * @return the element
	 */
	private Element checkIfSelectedModeIsArthimaticExpr(XmlProcessor dataCriteriaXMLProcessor, Node attributeQDMNode,
			Node templateNode, Element valueElem){
		String attrMode = (String) attributeQDMNode.getUserData(ATTRIBUTE_MODE);
		String nodeName = attributeQDMNode.getNodeName();
		
		if(nodeName.equals("attribute"))
		{
			valueElem.setAttribute(XSI_TYPE, "IVL_PQ");
			Node unitAttrib = attributeQDMNode.getAttributes().getNamedItem("unit");
			if(EQUAL_TO.equals(attrMode) ){
				Element lowElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(LOW);
				lowElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				
				Element highElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(HIGH);
				highElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				
				if(unitAttrib != null){
					String unitString = getUnitString(unitAttrib.getNodeValue());
					lowElem.setAttribute("unit", unitString);
					highElem.setAttribute("unit", unitString);
				}
				
				valueElem.appendChild(lowElem);
				valueElem.appendChild(highElem);
			}else if(attrMode.startsWith(GREATER_THAN)){
				if(attrMode.equals(GREATER_THAN)){
					valueElem.setAttribute("lowClosed", "false");
				}
				Element lowElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(LOW);
				lowElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				if(unitAttrib != null){
					String unitString = getUnitString(unitAttrib.getNodeValue());
					lowElem.setAttribute("unit", unitString);
				}
				
				valueElem.appendChild(lowElem);
			}else if(attrMode.startsWith(LESS_THAN)){
				if(attrMode.equals(LESS_THAN)){
					valueElem.setAttribute("highClosed", "false");
				}
				Element highElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(HIGH);
				highElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				if(unitAttrib != null){
					String unitString = getUnitString(unitAttrib.getNodeValue());
					highElem.setAttribute("unit", unitString);
				}
				
				valueElem.appendChild(highElem);
			}
		}
		
		
		return valueElem;
	}
	
	/**
	 * Adds the target site or priority code or route code element.
	 *
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param attributeQDMNode the attribute qdm node
	 * @param templateNode the template node
	 */
	private void addTargetSiteOrPriorityCodeOrRouteCodeElement(Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor,
			Node attributeQDMNode, Node templateNode) {
		String targetElementName = templateNode.getAttributes().getNamedItem("target").getNodeValue();
		Element targetSiteCodeElement = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(targetElementName);
		String insertBeforeNodeName = null;
		String insertAfterNodeName = null;
		if (templateNode.getAttributes().getNamedItem("insertBeforeNode") != null) {
			insertBeforeNodeName = templateNode.getAttributes().getNamedItem("insertBeforeNode").getNodeValue();
		} else if(templateNode.getAttributes().getNamedItem("insertAfterNode") != null){
			insertAfterNodeName = templateNode.getAttributes().getNamedItem("insertAfterNode").getNodeValue();
		}
		if (templateNode.getAttributes().getNamedItem("childTarget") != null) {
			String qdmOidValue = attributeQDMNode.getAttributes().getNamedItem(OID)
					.getNodeValue();
			Element valueElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(ITEM);
			valueElem.setAttribute("valueSet", qdmOidValue);
			Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			displayNameElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem(NAME).getNodeValue()
					+ " " + attributeQDMNode.getAttributes().getNamedItem(TAXONOMY).getNodeValue() + " Value Set");
			valueElem.appendChild(displayNameElem);
			targetSiteCodeElement.appendChild(valueElem);
			if (insertBeforeNodeName != null) {
				Node outBoundElement =  dataCriteriaElem.getElementsByTagName(insertBeforeNodeName).item(0);
				if (outBoundElement != null) {
					Node parentOfOutBoundElement = outBoundElement.getParentNode();
					parentOfOutBoundElement.insertBefore(targetSiteCodeElement, outBoundElement);
				} else {
					dataCriteriaElem.appendChild(targetSiteCodeElement);
				}
			} else if (insertAfterNodeName != null) {
				Node outBoundElement =  dataCriteriaElem.getElementsByTagName(insertAfterNodeName).item(0).getNextSibling();
				if (outBoundElement != null) {
					outBoundElement.getParentNode().insertBefore(targetSiteCodeElement, outBoundElement);
				} else {
					dataCriteriaElem.appendChild(targetSiteCodeElement);
				}
			} else {
				dataCriteriaElem.appendChild(targetSiteCodeElement);
			}
		} else if (templateNode.getAttributes().getNamedItem(FLAVOR_ID) != null) {
			String flavorIdValue = templateNode.getAttributes().getNamedItem(FLAVOR_ID).getNodeValue();
			targetSiteCodeElement.setAttribute(FLAVOR_ID, flavorIdValue);
			if (insertBeforeNodeName != null) {
				Node outBoundElement =  dataCriteriaElem.getElementsByTagName(insertBeforeNodeName).item(0);
				if (outBoundElement != null) {
					Node parentOfOutBoundElement = outBoundElement.getParentNode();
					parentOfOutBoundElement.insertBefore(targetSiteCodeElement, outBoundElement);
				} else {
					dataCriteriaElem.appendChild(targetSiteCodeElement);
				}
			} else if (insertAfterNodeName != null) {
				Node outBoundElement =  dataCriteriaElem.getElementsByTagName(insertAfterNodeName).item(0).getNextSibling();
				if (outBoundElement != null) {
					outBoundElement.getParentNode().insertBefore(targetSiteCodeElement, outBoundElement);
				} else {
					dataCriteriaElem.appendChild(targetSiteCodeElement);
				}
			} else {
				dataCriteriaElem.appendChild(targetSiteCodeElement);
			}
		} else if (templateNode.getAttributes().getNamedItem("addValueSet") != null) {
			String qdmOidValue = attributeQDMNode.getAttributes().getNamedItem(OID)
					.getNodeValue();
			targetSiteCodeElement.setAttribute("valueSet", qdmOidValue);
			Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			displayNameElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem(NAME).getNodeValue()
					+ " " + attributeQDMNode.getAttributes().getNamedItem(TAXONOMY).getNodeValue() + " Value Set");
			targetSiteCodeElement.appendChild(displayNameElem);
			if (insertBeforeNodeName != null) {
				Node outBoundElement =  dataCriteriaElem.getElementsByTagName(insertBeforeNodeName).item(0);
				if (outBoundElement != null) {
					Node parentOfOutBoundElement = outBoundElement.getParentNode();
					parentOfOutBoundElement.insertBefore(targetSiteCodeElement, outBoundElement);
				} else {
					dataCriteriaElem.appendChild(targetSiteCodeElement);
				}
			} else {
				dataCriteriaElem.appendChild(targetSiteCodeElement);
			}
		}
	}
	
	/**
	 * Method to generate HQMF XML for date time attributes.
	 *
	 * @param childNode the child node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 */
	private void generateDateTimeAttributes(Node childNode,
			Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node attributeQDMNode) {
		
		String attrName = (String) attributeQDMNode.getUserData(ATTRIBUTE_NAME);
		String attrMode = (String) attributeQDMNode.getUserData(ATTRIBUTE_MODE);
		String attrDate = (String) attributeQDMNode.getUserData(ATTRIBUTE_DATE);
		
		String timeTagName = "";
		if(attrName.equals(START_DATETIME) || attrName.equalsIgnoreCase(FACILITY_LOCATION_ARRIVAL_DATETIME)){
			timeTagName = LOW;
		}else if(attrName.equals(STOP_DATETIME) || attrName.equalsIgnoreCase(FACILITY_LOCATION_DEPARTURE_DATETIME)){
			timeTagName = HIGH;
		}
		
		Element effectiveTimeNode = dataCriteriaXMLProcessor.getOriginalDoc().createElement(EFFECTIVE_TIME);
		effectiveTimeNode.setAttribute(XSI_TYPE, "IVL_TS");
		
		if(CHECK_IF_PRESENT.equals(attrMode)){
			
			if(timeTagName.length() > 0){
				Element timeTagNode = dataCriteriaElem.getOwnerDocument().createElement(timeTagName);
				timeTagNode.setAttribute(FLAVOR_ID, "ANY.NONNULL");
				effectiveTimeNode.appendChild(timeTagNode);
			}
		}else if(attrMode.startsWith(Generator.LESS_THAN) || attrMode.startsWith(Generator.GREATER_THAN) || attrMode.equals(Generator.EQUAL_TO)){
			
			if(attrMode.equals(Generator.EQUAL_TO)){
				if(timeTagName.length() > 0){
					Element timeTagNode = dataCriteriaElem.getOwnerDocument().createElement(timeTagName);
					timeTagNode.setAttribute(VALUE, attrDate);
					effectiveTimeNode.appendChild(timeTagNode);
				}
			}else if(attrMode.startsWith(Generator.GREATER_THAN)){
				if(timeTagName.length() > 0){
					Element timeTagNode = dataCriteriaElem.getOwnerDocument().createElement(timeTagName);
					Element uncertainRangeNode = dataCriteriaElem.getOwnerDocument().createElement("uncertainRange");
					if(attrMode.equals(Generator.GREATER_THAN)){
						uncertainRangeNode.setAttribute("lowClosed", "false");
					}
					Element lowNode = dataCriteriaElem.getOwnerDocument().createElement(LOW);
					lowNode.setAttribute(XSI_TYPE, "TS");
					lowNode.setAttribute(VALUE, attrDate);
					
					Element highNode = dataCriteriaElem.getOwnerDocument().createElement(HIGH);
					highNode.setAttribute(XSI_TYPE, "TS");
					highNode.setAttribute("nullFlavor", "PINF");
					
					uncertainRangeNode.appendChild(lowNode);
					uncertainRangeNode.appendChild(highNode);
					timeTagNode.appendChild(uncertainRangeNode);
					effectiveTimeNode.appendChild(timeTagNode);
				}
			}else if(attrMode.startsWith(Generator.LESS_THAN)){
				if(timeTagName.length() > 0){
					Element timeTagNode = dataCriteriaElem.getOwnerDocument().createElement(timeTagName);
					Element uncertainRangeNode = dataCriteriaElem.getOwnerDocument().createElement("uncertainRange");
					if(attrMode.equals(Generator.LESS_THAN)){
						uncertainRangeNode.setAttribute("highClosed", "false");
					}
					Element lowNode = dataCriteriaElem.getOwnerDocument().createElement(LOW);
					lowNode.setAttribute(XSI_TYPE, "TS");
					lowNode.setAttribute("nullFlavor", "NINF");
					
					Element highNode = dataCriteriaElem.getOwnerDocument().createElement(HIGH);
					highNode.setAttribute(XSI_TYPE, "TS");
					highNode.setAttribute(VALUE, attrDate);
					
					uncertainRangeNode.appendChild(lowNode);
					uncertainRangeNode.appendChild(highNode);
					timeTagNode.appendChild(uncertainRangeNode);
					effectiveTimeNode.appendChild(timeTagNode);
				}
			}
		}
		
		/**
		 * If effectiveTimeNode has any child nodes then add it to the main dataCriteriaNode.
		 */
		if(effectiveTimeNode.hasChildNodes()){
			NodeList nodeList = dataCriteriaElem.getElementsByTagName("value");
			if ((nodeList != null) && (nodeList.getLength() > 0)) {
				dataCriteriaElem.insertBefore(effectiveTimeNode, nodeList.item(0));
			} else {
				
				if(attrName.contains("facility")){
					NodeList nodeListParticipation =  dataCriteriaElem.getElementsByTagName("role");
					if ((nodeListParticipation != null) && (nodeListParticipation.getLength() > 0)) {
						nodeListParticipation.item(0).getFirstChild().getParentNode().appendChild(effectiveTimeNode);
					}
				} else {
					NodeList nodeListParticipation = dataCriteriaElem.getElementsByTagName("participation");
					if ((nodeListParticipation != null) && (nodeListParticipation.getLength() > 0)) {
						dataCriteriaElem.insertBefore(effectiveTimeNode, nodeListParticipation.item(0));
					} else {
						dataCriteriaElem.appendChild(effectiveTimeNode);
					}
				}
			}
		}
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
	 * Add comment before specific Node.
	 *
	 * @param xmlProcessor the xml processor
	 * @param commentText the comment text
	 * @param insertBeforeNode the insert before node
	 */
	private void addCommentNode(XmlProcessor xmlProcessor, String commentText, Node insertBeforeNode) {
		Comment comment = xmlProcessor.getOriginalDoc().createComment(commentText);
		insertBeforeNode.getParentNode().insertBefore(comment, insertBeforeNode);
		
	}
	
	/**
	 * Creates the code for datatype.
	 *
	 * @param templateNode the template node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @return the element
	 */
	private Element createCodeForDatatype(Node templateNode,
			XmlProcessor dataCriteriaXMLProcessor) {
		Node codeAttr = templateNode.getAttributes().getNamedItem(CODE);
		Node codeSystemAttr = templateNode.getAttributes().getNamedItem(
				CODE_SYSTEM);
		Node codeSystemNameAttr = templateNode.getAttributes().getNamedItem(
				CODE_SYSTEM_NAME);
		Node codeDisplayNameAttr = templateNode.getAttributes().getNamedItem(
				CODE_SYSTEM_DISPLAY_NAME);
		Element codeElement = null;
		if ((codeAttr != null) || (codeSystemAttr != null) || (codeSystemNameAttr !=null) ||
				(codeDisplayNameAttr!=null)) {
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
			if(codeSystemNameAttr !=null){
				codeElement.setAttribute(CODE_SYSTEM_NAME,
						codeSystemNameAttr.getNodeValue());
			}
			if(codeDisplayNameAttr !=null){
				Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(DISPLAY_NAME);
				displayNameElem.setAttribute(VALUE,codeDisplayNameAttr.getNodeValue() );
				codeElement.appendChild(displayNameElem);
			}
		}
		return codeElement;
	}
	
	/**
	 * Gets the unit string.
	 *
	 * @param unitString the unit string
	 * @return the unit string
	 */
	private String getUnitString(String unitString){
		String returnString = unitString;
		
		if(unitString.equals("years") || unitString.equals("year")){
			returnString = "a";
		}else if(unitString.equals("month") || unitString.equals("months")){
			returnString = "mo";
		}else if(unitString.equals("day") || unitString.equals("days")){
			returnString = "d";
		}else if(unitString.equals("hours") || unitString.equals("hour")){
			returnString = "h";
		}else if(unitString.equals("week") || unitString.equals("weeks")){
			returnString = "wk";
		}else if(unitString.equals("minutes") || unitString.equals("minute")){
			returnString = "min";
		}else if(unitString.equals("quarter") || unitString.equals("quarters")){
			returnString = "[qtr]";
		}else if(unitString.equals("second") || unitString.equals("seconds")){
			returnString = "s";
		}else if(unitString.equals("bpm")){
			returnString = "{H.B}";
		}else if(unitString.equals("mmHG")){
			returnString = "mm[Hg]";
		}else if(unitString.equals("mEq")){
			returnString = "meq";
		}else if(unitString.equals("celsius")){
			returnString = "cel";
		}else if(unitString.equals("WBC/mm3")){
			returnString = "{WBC}/mm3";
		}else if(unitString.equals("WBC/hpf")){
			returnString = "{WBC}/[HPF]";
		}else if(unitString.equals("CFU/mL")){
			returnString = "{CFU}/mL";
		}else if(unitString.equals("per mm3")){
			returnString = "/mm3";
		}else if(unitString.equals("copies/mL")){
			returnString = "[copies]/mL";
		}
		
		return returnString;
	}
	
	
}
