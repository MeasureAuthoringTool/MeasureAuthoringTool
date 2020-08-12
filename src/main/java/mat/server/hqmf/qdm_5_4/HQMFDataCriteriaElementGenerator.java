package mat.server.hqmf.qdm_5_4;

import mat.model.clause.MeasureExport;
import mat.server.hqmf.Generator;
import mat.server.hqmf.QDMTemplateProcessorFactory;
import mat.server.hqmf.qdm.HQMFDataCriteriaGenerator;
import mat.server.logging.LogFactory;
import mat.server.util.XmlProcessor;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HQMFDataCriteriaElementGenerator implements Generator {
	
	private static final String DATA_CRITERIA_EXTENSION = "2018-05-01";
	
	/** The occurrence map. */
	private Map<String, Node> occurrenceMap = new HashMap<String, Node>();
	
	protected String extensionValue = null;
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(HQMFDataCriteriaElementGenerator.class);
	
	/**
	 * Generate hqm for measure.
	 *
	 * @param me            the me
	 * @return the string
	 * @throws Exception the exception
	 */
	@Override
	public String generate(MeasureExport me) throws Exception {
		String dataCriteria = "";
		dataCriteria = getHQMFXmlString(me);
		return dataCriteria;
	}
	/**
	 * Gets the HQMF xml string.
	 * 
	 * @param me
	 *            the me
	 * @return the HQMF xml string
	 * @throws Exception 
	 */
	private String getHQMFXmlString(MeasureExport me) throws Exception {
		getExtensionValueBasedOnVersion(me);
		XmlProcessor dataCriteriaXMLProcessor = createDateCriteriaTemplate(me);
		me.setHQMFXmlProcessor(dataCriteriaXMLProcessor);
		
		String simpleXMLStr = me.getSimpleXML();
		XmlProcessor simpleXmlprocessor = new XmlProcessor(simpleXMLStr);
		me.setSimpleXMLProcessor(simpleXmlprocessor);
				
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
				"<root><component><dataCriteriaSection></dataCriteriaSection></component></root>");
		
		Node dataCriteriaElem = outputProcessor.getOriginalDoc()
				.getElementsByTagName("dataCriteriaSection").item(0);
		Element templateId = outputProcessor.getOriginalDoc()
				.createElement(TEMPLATE_ID);
		dataCriteriaElem.appendChild(templateId);
		Element itemChild = outputProcessor.getOriginalDoc()
				.createElement(ITEM);
		itemChild.setAttribute(ROOT, "2.16.840.1.113883.10.20.28.2.6");
		//itemChild.setAttribute("extension", getDataCriteriaExtValueBasedOnVersion(me));
		itemChild.setAttribute("extension", DATA_CRITERIA_EXTENSION);
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
		dataCriteriaElem.appendChild(textElem);
		
		return outputProcessor;
	}
	
	private String getDataCriteriaExtValueBasedOnVersion(MeasureExport me) {
		return VERSION_5_0_ID; 
	}
	/**
	 * Creates the data criteria for qdm elements.
	 *
	 * @param me            the me
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @return the string
	 * @throws Exception 
	 */
	private void createDataCriteriaForQDMELements(MeasureExport me, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor) throws Exception {
		String xPathForQDMNoAttribs = "/measure/elementLookUp/qdm[@datatype and @code ='false']";
		String xpathForOtherSupplementalQDMs = "/measure/supplementalDataElements/elementRef/@id";
		String xpathForMeasureGroupingItemCount = "/measure//itemCount/elementRef/@id";
		
		
		try {			
			NodeList qdmNoAttributeNodeList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xPathForQDMNoAttribs);
			generateCQLQDMNodeEntries(dataCriteriaXMLProcessor, simpleXmlprocessor,
					qdmNoAttributeNodeList);
			
			HQMFDataCriteriaElementGeneratorForCodes cqlBasedHQMFDataCriteriaElementGeneratorForCodes = new HQMFDataCriteriaElementGeneratorForCodes();
			cqlBasedHQMFDataCriteriaElementGeneratorForCodes.generate(me);
			
			//generating QDM Entries for other Supplemental Data Elements
			NodeList supplementalDataElements = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(),
					xpathForOtherSupplementalQDMs);
			generateOtherSupplementalDataQDMEntries(me, dataCriteriaXMLProcessor, supplementalDataElements);
			
			//generating QDM entries for measureGrouping ItemCountlist
			NodeList measureGroupingItemCountList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xpathForMeasureGroupingItemCount);
			generateMeasureGrpnItemCountQDMEntries(me, dataCriteriaXMLProcessor, measureGroupingItemCountList);
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	
	protected void getExtensionValueBasedOnVersion(MeasureExport me) {
		if(me!=null){
			extensionValue = getDataCriteriaExtValueBasedOnVersion(me);
		}
	}
	
	/**
	 * Generate measure grp item count qdm entries.
	 *
	 * @param me the me
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param measureGroupingItemCountList the measure grouping item count list
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateMeasureGrpnItemCountQDMEntries(MeasureExport me,
			XmlProcessor dataCriteriaXMLProcessor,
			NodeList measureGroupingItemCountList) throws XPathExpressionException {
		
		if((measureGroupingItemCountList==null)  ||
				(measureGroupingItemCountList.getLength()<1)){
			return;
		}
		List<String> itemCountIDList = new ArrayList<String>();
		for(int i=0; i<measureGroupingItemCountList.getLength();i++){
			if(!itemCountIDList.contains(measureGroupingItemCountList.item(i).getNodeValue())){
				itemCountIDList.add(measureGroupingItemCountList.item(i).getNodeValue());
			}
		}
		String xpathforElementLookUpElements="/measure/elementLookUp/qdm["+getUUIDString(itemCountIDList)+"]";
		
		NodeList measureGroupingElementRefNodeList = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(),
				xpathforElementLookUpElements);
		generateItemCountQDMEntries(me, dataCriteriaXMLProcessor, measureGroupingElementRefNodeList);
	}
	
	/**
	 * Generate supplemental data qdm entries.
	 *
	 * @param me the me
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param supplementalDataElements the supplemental data elements
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateOtherSupplementalDataQDMEntries(MeasureExport me, XmlProcessor dataCriteriaXMLProcessor,
			NodeList supplementalDataElements ) throws XPathExpressionException{
		if ((supplementalDataElements == null) ||
				(supplementalDataElements.getLength()<1)) {
			return;
		}
		List<String> supplementalElemenRefIds = new ArrayList<String>();
		for(int i=0; i<supplementalDataElements.getLength();i++){
			supplementalElemenRefIds.add(supplementalDataElements.item(i).getNodeValue());
		}
		
		String xpathforOtherSupplementalDataElements="/measure/elementLookUp/qdm["+getUUIDString(supplementalElemenRefIds)+"][@suppDataElement != 'true']";
		NodeList otherSupplementalQDMNodeList = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(),
				xpathforOtherSupplementalDataElements);
		
		generateSupplementalDataQDMEntries(me, dataCriteriaXMLProcessor, otherSupplementalQDMNodeList);
		
	}
	
	/**
	 * Generate supplemental data qdm entries.
	 *
	 * @param me the me
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param qdmNodeList the qdm node list
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateSupplementalDataQDMEntries(MeasureExport me, XmlProcessor dataCriteriaXMLProcessor,
			NodeList qdmNodeList) throws XPathExpressionException{
		for(int j=0; j<qdmNodeList.getLength(); j++){
			Node qdmNode = qdmNodeList.item(j);
			String qdmName = qdmNode.getAttributes().getNamedItem("name").getNodeValue();
			String qdmDatatype = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
			String qdmUUID = qdmNode.getAttributes().getNamedItem("uuid").getNodeValue();
			String qdmExtension = qdmName.replaceAll("\\s", "") +"_"+ qdmDatatype.replaceAll("\\s", "");
			String xpathForQDMEntry = "/root/component/dataCriteriaSection/entry/*/id[@root='"+
					qdmUUID+"'][@extension=\""+qdmExtension+"\"]";
			Node qmdEntryIDNode = dataCriteriaXMLProcessor.findNode(dataCriteriaXMLProcessor.getOriginalDoc(),
					xpathForQDMEntry);
			if (qmdEntryIDNode==null) {
				createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, me.getSimpleXMLProcessor());
			}
		}
	}
	
	/**
	 * Generate Item Count qdm entries.
	 *
	 * @param me the me
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param qdmNodeList the qdm node list
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateItemCountQDMEntries(MeasureExport me, XmlProcessor dataCriteriaXMLProcessor,
			NodeList qdmNodeList) throws XPathExpressionException{
		for(int j=0; j<qdmNodeList.getLength(); j++){
			Node qdmNode = qdmNodeList.item(j);
			String qdmName = qdmNode.getAttributes().getNamedItem("name").getNodeValue();
			String qdmDatatype = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
			String qdmUUID = qdmNode.getAttributes().getNamedItem("uuid").getNodeValue();
			String qdmExtension = qdmName.replaceAll("\\s", "") +"_"+ qdmDatatype.replaceAll("\\s", "");
			if(qdmNode.getAttributes().getNamedItem("instance") != null){
				String instanceOfValue = qdmNode.getAttributes().getNamedItem("instance").getNodeValue();
				String newExtension = instanceOfValue.replaceAll("\\s", "") + "_" + qdmExtension;
				qdmExtension = newExtension;
			}
			String xpathForQDMEntry = "/root/component/dataCriteriaSection/entry/*/id[@root='"+
					qdmUUID+"'][@extension=\""+qdmExtension+"\"]";
			Node qmdEntryIDNode = dataCriteriaXMLProcessor.findNode(dataCriteriaXMLProcessor.getOriginalDoc(),
					xpathForQDMEntry);
			if (qmdEntryIDNode==null) {
				createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, me.getSimpleXMLProcessor());
			}
		}
	}
	
	
	/**
	 * Gets the UUID string.
	 *
	 * @param uuidList the uuid list
	 * @return the UUID string
	 */
	private String getUUIDString(List<String> uuidList){
		String uuidXPathString = "";
		for (String uuidString: uuidList) {
			uuidXPathString += "@uuid = '" + uuidString + "' or";
		}
		
		uuidXPathString = uuidXPathString.substring(0, uuidXPathString.lastIndexOf(" or"));
		return uuidXPathString;
	}
	
	private void generateCQLQDMNodeEntries(XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, NodeList qdmNoAttributeNodeList)
					throws XPathExpressionException {
		
		if (qdmNoAttributeNodeList == null) {
			return;
		}
		
		for (int i = 0; i < qdmNoAttributeNodeList.getLength(); i++) {
			Node qdmNode = qdmNoAttributeNodeList.item(i);
			createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor);
		}
	}
	
	/**
	 * Generate qdm entry.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param qdmNode the qdm node
	 * @param forceGenerate the force generate
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateQDMEntry(XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node qdmNode, boolean forceGenerate)
					throws XPathExpressionException {
		String qdmUUID = qdmNode.getAttributes().getNamedItem(UUID).getNodeValue();
		
		String xPathForIndividualElementRefs = "/measure/subTreeLookUp//elementRef[@id='"+qdmUUID+"'][not(attribute)]";
		NodeList elementRefList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xPathForIndividualElementRefs);
		if(forceGenerate || (elementRefList.getLength() > 0)){
			createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor);
		}
	}
	
	/**
	 * Create xml for data criteria.
	 *
	 * @param qdmNode            the qdm node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 * @return void
	 */
	private void createXmlForDataCriteria(Node qdmNode, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor simpleXmlprocessor) {
		String dataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		
		XmlProcessor templateXMLProcessor = QDMTemplateProcessorFactory.getTemplateProcessor(5.4);
		String xPathForTemplate = "/templates/template[text()='" + dataType.toLowerCase() + "']";
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
				
				createDataCriteriaElementTag(actNodeStr, templateNode, qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor,templateXMLProcessor);
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
			simpleXmlprocessor, XmlProcessor templateXMLProcessor) throws XPathExpressionException {
		String oidValue = templateNode.getAttributes().getNamedItem(OID).getNodeValue();
		String classCodeValue = templateNode.getAttributes().getNamedItem(CLASS).getNodeValue();
		String moodValue = templateNode.getAttributes().getNamedItem(MOOD).getNodeValue();
		String statusValue = templateNode.getAttributes().getNamedItem("status").getNodeValue();
		String rootValue = qdmNode.getAttributes().getNamedItem(ID).getNodeValue();
		String dataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		String qdmOidValue = qdmNode.getAttributes().getNamedItem(OID).getNodeValue();

		String qdmName = qdmNode.getAttributes().getNamedItem(NAME).getNodeValue();
		Node actionNegInd = templateNode.getAttributes().getNamedItem("actionNegationInd");
		String entryCommentText = dataType;
		// Local variable changes.
		String qdmLocalVariableName = qdmName + "_" + dataType;
		String localVariableName = qdmLocalVariableName;

		qdmLocalVariableName = StringUtils.deleteWhitespace(qdmLocalVariableName);
		localVariableName = StringUtils.deleteWhitespace(localVariableName);

		Element dataCriteriaSectionElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc().getElementsByTagName("dataCriteriaSection").item(0);
		Element componentElem = (Element) dataCriteriaXMLProcessor.getOriginalDoc().getElementsByTagName("component").item(0);
		Attr nameSpaceAttr = dataCriteriaXMLProcessor.getOriginalDoc().createAttribute("xmlns:xsi");
		nameSpaceAttr.setNodeValue(nameSpace);
		componentElem.setAttributeNodeNS(nameSpaceAttr);
		Attr qdmNameSpaceAttr = dataCriteriaXMLProcessor.getOriginalDoc().createAttribute("xmlns:cql-ext");
		qdmNameSpaceAttr.setNodeValue("urn:hhs-cql:hqmf-n1-extensions:v1");
		componentElem.setAttributeNodeNS(qdmNameSpaceAttr);
		// creating Entry Tag
		Element entryElem = dataCriteriaXMLProcessor.getOriginalDoc().createElement("entry");
		entryElem.setAttribute(TYPE_CODE, "DRIV");
		// Local Variable Name Tag - Inside Entry tag.
		Element localVarElem = dataCriteriaXMLProcessor.getOriginalDoc().createElement("localVariableName");
		localVarElem.setAttribute(VALUE, localVariableName + "_" + UUIDUtilClient.uuid(5));
		entryElem.appendChild(localVarElem);
		Element dataCriteriaElem = dataCriteriaXMLProcessor.getOriginalDoc().createElement(actNodeStr);
		entryElem.appendChild(dataCriteriaElem);
		dataCriteriaElem.setAttribute(CLASS_CODE, classCodeValue);
		dataCriteriaElem.setAttribute(MOOD_CODE, moodValue);
		// adding actionNegationInd for Negative Datatypes
		if (actionNegInd != null) {
			dataCriteriaElem.setAttribute(ACTION_NEGATION_IND, actionNegInd.getNodeValue());
		}
		Element templateId = dataCriteriaXMLProcessor.getOriginalDoc().createElement(TEMPLATE_ID);
		dataCriteriaElem.appendChild(templateId);
		Element itemChild = dataCriteriaXMLProcessor.getOriginalDoc().createElement(ITEM);
		itemChild.setAttribute(ROOT, oidValue);
		if (templateNode.getAttributes().getNamedItem("specificExtensionValue") != null) {
			String specificExtensionValue = templateNode.getAttributes().getNamedItem("specificExtensionValue")
					.getNodeValue();
			itemChild.setAttribute("extension", specificExtensionValue);
		} else if (templateNode.getAttributes().getNamedItem("addExtensionInTemplate") == null) {
			itemChild.setAttribute("extension", extensionValue);
		}
		templateId.appendChild(itemChild);
		Element idElem = dataCriteriaXMLProcessor.getOriginalDoc().createElement(ID);
		idElem.setAttribute(ROOT, rootValue);
		idElem.setAttribute("extension", qdmLocalVariableName);
		dataCriteriaElem.appendChild(idElem);

		boolean appendEntryElem = false;
		String occurString = dataType + "-" + qdmOidValue;

		if (qdmNode.getAttributes().getNamedItem("instance") != null) {
			generateOutboundForOccur(templateNode, qdmNode, dataCriteriaElem, occurString, dataCriteriaXMLProcessor,
					simpleXmlprocessor);
			entryCommentText = qdmNode.getAttributes().getNamedItem("instance").getNodeValue() + " " + entryCommentText;
			appendEntryElem = true;
		} else if (!occurrenceMap.containsKey(occurString) /* || (attributeQDMNode != null) */) {

			String isAddCodeTag = templateNode.getAttributes().getNamedItem("addCodeTag").getNodeValue();
			if ("true".equalsIgnoreCase(isAddCodeTag)) { // Add Code Element to DataCriteria Element.
				addCodeElementToDataCriteriaElement(templateNode, dataCriteriaXMLProcessor, qdmNode, dataCriteriaElem);
			}
			Element titleElem = dataCriteriaXMLProcessor.getOriginalDoc().createElement(TITLE);
			titleElem.setAttribute(VALUE, dataType);
			dataCriteriaElem.appendChild(titleElem);
			Element statusCodeElem = dataCriteriaXMLProcessor.getOriginalDoc().createElement("statusCode");
			statusCodeElem.setAttribute(CODE, statusValue);
			dataCriteriaElem.appendChild(statusCodeElem);
			// Add value tag in entry element.
			String addValueSetElement = templateNode.getAttributes().getNamedItem("addValueTag").getNodeValue();
			if ("true".equalsIgnoreCase(addValueSetElement)) {
				Element valueElem = dataCriteriaXMLProcessor.getOriginalDoc().createElement(VALUE);
				Node valueTypeAttr = templateNode.getAttributes().getNamedItem("valueType");
				if (valueTypeAttr != null) {
					valueElem.setAttribute(XSI_TYPE, valueTypeAttr.getNodeValue());
				}

				Node valueCodeSystem = templateNode.getAttributes().getNamedItem("valueCodeSystem");
				Node valueCode = templateNode.getAttributes().getNamedItem("valueCode");
				Node valueDisplayName = templateNode.getAttributes().getNamedItem("valueDisplayName");
				Node valueCodeSystemName = templateNode.getAttributes().getNamedItem("valueCodeSystemName");
				if ((valueCode != null) && (valueCodeSystem != null)) {
					valueElem.setAttribute("code", valueCode.getNodeValue());
					valueElem.setAttribute("codeSystem", valueCodeSystem.getNodeValue());
					if (valueCodeSystemName != null) {
						valueElem.setAttribute("codeSystemName", valueCodeSystemName.getNodeValue());
					}
				} else {
					valueElem.setAttribute("valueSet", qdmOidValue);
					addValueSetVersion(qdmNode, valueElem);
				}

				dataCriteriaElem.appendChild(valueElem);
			}
			if (templateNode.getAttributes().getNamedItem("includeSubTemplate") != null) {
				appendSubTemplateNode(templateNode, dataCriteriaXMLProcessor, templateXMLProcessor, dataCriteriaElem,
						qdmNode);
			}
			// checkForAttributes

			appendEntryElem = true;
		}
		if (appendEntryElem) {
			dataCriteriaSectionElem.appendChild(entryElem);
		}

	}
	
	/**
	 * Method to add valueSetVersion attribute in value element tag.
	 *
	 * @param qdmNode the qdm node
	 * @param valueElem the value elem
	 */
	protected void addValueSetVersion(Node qdmNode, Element valueElem) {
		/**
		 * This is to be commented until we start getting value set versions from
		 * VSAC.
		 */
		boolean addVersionToValueTag = false;
			String valueSetVersion = qdmNode.getAttributes().getNamedItem("version").getNodeValue();

			if ("1.0".equals(valueSetVersion) || "1".equals(valueSetVersion) || StringUtils.isBlank(valueSetVersion)) {
				addVersionToValueTag = false;
			} else {
				valueSetVersion = qdmNode.getAttributes().getNamedItem("version").getNodeValue();
				addVersionToValueTag = true;
			}
			if (addVersionToValueTag) {
				valueElem.setAttribute("valueSetVersion", valueSetVersion);
			}
	}
	
	/**
	 * Generate outbound for occur.
	 *
	 * @param templateNode the template node
	 * @param qdmNode the qdm node
	 * @param dataCriteriaElem the data criteria elem
	 * @param occurString the occur string
	 */
	private void generateOutboundForOccur(Node templateNode, Node qdmNode,
			Element dataCriteriaElem, String occurString,XmlProcessor dataCriteriaXMLProcessor, XmlProcessor
			simpleXmlprocessor) {
		Node refNode = occurrenceMap.get(occurString);
		
		logger.info("In generateOutboundForOccur()..refNode:"+refNode);
		logger.info("----------Occurance map:"+occurrenceMap);
		
		if(refNode != null){
			
			try {
				Node cloneRefNode = refNode.cloneNode(true);
				String name = cloneRefNode.getAttributes().getNamedItem("name").getNodeValue();
				String occName = qdmNode.getAttributes().getNamedItem("instance").getNodeValue();
				cloneRefNode.getAttributes().getNamedItem("name").setNodeValue(occName + "_" + name);

				if (!occurrenceMap.containsKey(occName + occurString)) {
					occurrenceMap.remove(occurString);
					generateQDMEntry(dataCriteriaXMLProcessor, simpleXmlprocessor, cloneRefNode, true);
					occurrenceMap.put(occurString, refNode);
					occurrenceMap.put(occName + occurString, cloneRefNode);
				}

				String refRootValue = cloneRefNode.getAttributes().getNamedItem(ID).getNodeValue();

				String refDatatype = cloneRefNode.getAttributes().getNamedItem("datatype").getNodeValue();
				String refQdmName = cloneRefNode.getAttributes().getNamedItem("name").getNodeValue();
				String reExt = StringUtils.deleteWhitespace(refQdmName + "_" + refDatatype);

				Element outboundRelElem = dataCriteriaElem.getOwnerDocument().createElement("outboundRelationship");
				outboundRelElem.setAttribute("typeCode", "OCCR");

				Element criteriaRefElem = dataCriteriaElem.getOwnerDocument().createElement("criteriaReference");
				String refClassCodeValue = templateNode.getAttributes().getNamedItem(CLASS).getNodeValue();
				String refMoodValue = templateNode.getAttributes().getNamedItem(MOOD).getNodeValue();
				criteriaRefElem.setAttribute(CLASS_CODE, refClassCodeValue);
				criteriaRefElem.setAttribute(MOOD_CODE, refMoodValue);

				Element idRelElem = dataCriteriaElem.getOwnerDocument().createElement(ID);
				idRelElem.setAttribute(ROOT, refRootValue);
				idRelElem.setAttribute("extension", reExt);

				criteriaRefElem.appendChild(idRelElem);
				outboundRelElem.appendChild(criteriaRefElem);
				dataCriteriaElem.appendChild(outboundRelElem);
				Node templateIdNode = dataCriteriaElem.getElementsByTagName("templateId").item(0);
				dataCriteriaElem.removeChild(templateIdNode);
			}
			catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Add Code Element To data Criteria Element based on condition.
	 *
	 * @param templateNode - Node
	 * @param dataCriteriaXMLProcessor - XmlProcessor
	 * @param qdmNode the qdm node
	 * @param dataCriteriaElem - Element
	 */
	private void addCodeElementToDataCriteriaElement(Node templateNode, XmlProcessor dataCriteriaXMLProcessor, Node qdmNode, Element dataCriteriaElem) {
		String dataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		String qdmOidValue = qdmNode.getAttributes().getNamedItem(OID).getNodeValue();

		// Patient Characteristic data type - contains code tag with valueSetId
		// attribute and no title and value set tag.
		boolean isPatientChar = templateNode.getAttributes().getNamedItem("valueSetId") != null;
		boolean isAddValueSetInCodeTrue = templateNode.getAttributes().getNamedItem("addValueSetInCode") != null;
		boolean isIntervention = ("Intervention, Order".equalsIgnoreCase(dataType)
				|| "Intervention, Performed".equalsIgnoreCase(dataType)
				|| "Intervention, Recommended".equalsIgnoreCase(dataType)
				|| "Intervention, Not Ordered".equalsIgnoreCase(dataType)
				|| "Intervention, Not Performed".equalsIgnoreCase(dataType)
				|| "Intervention, Not Recommended".equalsIgnoreCase(dataType));
		if (isAddValueSetInCodeTrue) {
			Element codeElem = dataCriteriaXMLProcessor.getOriginalDoc().createElement(CODE);
			Node valueTypeAttr = templateNode.getAttributes().getNamedItem("valueType");
			if (valueTypeAttr != null) {
				codeElem.setAttribute(XSI_TYPE, valueTypeAttr.getNodeValue());
			}
			codeElem.setAttribute("valueSet", qdmOidValue);
			addValueSetVersion(qdmNode, codeElem);
			dataCriteriaElem.appendChild(codeElem);

		} else if (isPatientChar) {
			Element codeElem = dataCriteriaXMLProcessor.getOriginalDoc().createElement(CODE);
			codeElem.setAttribute(templateNode.getAttributes().getNamedItem("valueSetId").getNodeValue(), qdmOidValue);
			addValueSetVersion(qdmNode, codeElem);
			dataCriteriaElem.appendChild(codeElem);
		} else if (isIntervention) {
			Element codeElem = dataCriteriaXMLProcessor.getOriginalDoc().createElement(CODE);
			codeElem.setAttribute("valueSet", qdmOidValue);
			addValueSetVersion(qdmNode, codeElem);
			dataCriteriaElem.appendChild(codeElem);
		} else {
			Element codeElement = createCodeForDatatype(templateNode, dataCriteriaXMLProcessor);
			if (codeElement != null) {
				dataCriteriaElem.appendChild(codeElement);
			}
		}
	}
	
	/**
	 * This method is called for populating version/expansion Identifier of value set when value set attribute
	 * mode is applied. If version is most recent, 1.0 or 1 is returned.
	 * @param qdmNode
	 * @return version
	 */
	private String valueSetVersionStringValue(Node qdmNode){
		String version = qdmNode.getAttributes().getNamedItem("version").getNodeValue();
		if (!"1.0".equals(version) && !"1".equals(version) && StringUtils.isNotBlank(version)) {
			version = qdmNode.getAttributes().getNamedItem("version").getNodeValue();
		}  else {
			version = null;
		}
		return version;
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
		Node subTemplateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/" + subTemplateName);
		NodeList subTemplateNodeChilds = templateXMLProcessor.findNodeList(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/" + subTemplateName + "/child::node()");
		String qdmOidValue = qdmNode.getAttributes().getNamedItem(OID).getNodeValue();
		String qdmName = qdmNode.getAttributes().getNamedItem(NAME).getNodeValue();
		String qdmNameDataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		String qdmTaxonomy = qdmNode.getAttributes().getNamedItem(TAXONOMY).getNodeValue();
		
		if (subTemplateNode.getAttributes().getNamedItem("changeAttribute") != null) {
			String[] attributeToBeModified = subTemplateNode.getAttributes().getNamedItem("changeAttribute").getNodeValue().split(",");
			for (String changeAttribute : attributeToBeModified) {
				NodeList attributedToBeChangedInNode = null;
				attributedToBeChangedInNode = templateXMLProcessor.findNodeList(templateXMLProcessor.getOriginalDoc(),
						"/templates/subtemplates/" + subTemplateName + "//" + changeAttribute);
				if (changeAttribute.equalsIgnoreCase(ID)) {
					String rootId = qdmNode.getAttributes().getNamedItem("uuid").getNodeValue();
					attributedToBeChangedInNode.item(0).getAttributes().getNamedItem("root").setNodeValue(rootId);
					attributedToBeChangedInNode.item(0).getAttributes().getNamedItem("extension").setNodeValue(UUIDUtilClient.uuid());
				} else if (changeAttribute.equalsIgnoreCase(CODE)) {
					if (attributedToBeChangedInNode.item(0).getAttributes().getNamedItem("code") != null) {
						attributedToBeChangedInNode.item(0).getAttributes().removeNamedItem("code");
					}

					if (attributedToBeChangedInNode.item(0).getAttributes().getNamedItem("codeSystem") != null) {
						attributedToBeChangedInNode.item(0).getAttributes().removeNamedItem("codeSystem");
					}

					if (attributedToBeChangedInNode.item(0).getAttributes().getNamedItem("codeSystemName") != null) {
						attributedToBeChangedInNode.item(0).getAttributes().removeNamedItem("codeSystemName");
					}

					if (attributedToBeChangedInNode.item(0).getAttributes().getNamedItem("codeSystemVersion") != null) {
						attributedToBeChangedInNode.item(0).getAttributes().removeNamedItem("codeSystemVersion");
					}

					if (attributedToBeChangedInNode.item(0).getAttributes().getNamedItem("valueSetVersion") != null) {
						attributedToBeChangedInNode.item(0).getAttributes().removeNamedItem("valueSetVersion");
					}

					Attr attrNodeValueSet = attributedToBeChangedInNode.item(0).getOwnerDocument().createAttribute("valueSet");
					attrNodeValueSet.setNodeValue(qdmOidValue);
					attributedToBeChangedInNode.item(0).getAttributes().setNamedItem(attrNodeValueSet);
					String valueSetVersion = valueSetVersionStringValue(qdmNode);

					if (valueSetVersion != null) {
						Attr attrNode = attributedToBeChangedInNode.item(0).getOwnerDocument().createAttribute("valueSetVersion");
						attrNode.setNodeValue(valueSetVersion);
						attributedToBeChangedInNode.item(0).getAttributes().setNamedItem(attrNode);
					}

				} else if (changeAttribute.equalsIgnoreCase(DISPLAY_NAME)) {
					attributedToBeChangedInNode.item(0).getAttributes().getNamedItem("value").setNodeValue(HQMFDataCriteriaGenerator.removeOccurrenceFromName(qdmName) + " " + qdmTaxonomy + " value set");
				} else if (changeAttribute.equalsIgnoreCase(TITLE)) {
					attributedToBeChangedInNode.item(0).getAttributes().getNamedItem("value").setNodeValue(qdmNameDataType);
				} else if (changeAttribute.equalsIgnoreCase(ITEM)) {
					for (int count = 0; count < attributedToBeChangedInNode.getLength(); count++) {
						Node itemNode = attributedToBeChangedInNode.item(count);
						itemNode.getAttributes().getNamedItem("extension").setNodeValue(extensionValue);
					}

				} else if (changeAttribute.equalsIgnoreCase("value")) {	
					Attr attrNode = attributedToBeChangedInNode.item(0).getOwnerDocument().createAttribute("valueSet");
					attrNode.setNodeValue(qdmOidValue);
					attributedToBeChangedInNode.item(0).getAttributes().setNamedItem(attrNode);

				} 

			}
		}

		for (int i = 0; i < subTemplateNodeChilds.getLength(); i++) {
			Node childNode = subTemplateNodeChilds.item(i);
			Node nodeToAttach = dataCriteriaXMLProcessor.getOriginalDoc().importNode(childNode, true);
			XmlProcessor.clean(nodeToAttach);
			dataCriteriaElem.appendChild(nodeToAttach);
		}
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
	 * @param templateNode the template node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @return the element
	 */
	protected Element createCodeForDatatype(Node templateNode, XmlProcessor dataCriteriaXMLProcessor) {
		Node codeAttr = templateNode.getAttributes().getNamedItem(CODE);
		Node codeSystemAttr = templateNode.getAttributes().getNamedItem(CODE_SYSTEM);
		Node codeSystemNameAttr = templateNode.getAttributes().getNamedItem(CODE_SYSTEM_NAME);
		Node codeDisplayNameAttr = templateNode.getAttributes().getNamedItem(CODE_SYSTEM_DISPLAY_NAME);
		Element codeElement = null;
		if ((codeAttr != null) || (codeSystemAttr != null) || (codeSystemNameAttr != null)
				|| (codeDisplayNameAttr != null)) {
			codeElement = dataCriteriaXMLProcessor.getOriginalDoc().createElement(CODE);
			if (codeAttr != null) {
				codeElement.setAttribute(CODE, codeAttr.getNodeValue());
			}
			if (codeSystemAttr != null) {
				codeElement.setAttribute(CODE_SYSTEM, codeSystemAttr.getNodeValue());
			}
			if (codeSystemNameAttr != null) {
				codeElement.setAttribute(CODE_SYSTEM_NAME, codeSystemNameAttr.getNodeValue());
			}
			if (codeDisplayNameAttr != null) {
				Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc().createElement(DISPLAY_NAME);
				displayNameElem.setAttribute(VALUE, codeDisplayNameAttr.getNodeValue());
				codeElement.appendChild(displayNameElem);
			}
		}
		return codeElement;
	}
}

