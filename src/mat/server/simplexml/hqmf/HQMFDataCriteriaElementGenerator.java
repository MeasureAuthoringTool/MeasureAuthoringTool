package mat.server.simplexml.hqmf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPathExpressionException;
import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// TODO: Auto-generated Javadoc
/**
 * The Class HQMFDataCriteriaGenerator.
 */
public class HQMFDataCriteriaElementGenerator implements Generator {
	
	
	/** The occurrence map. */
	private Map<String, Node> occurrenceMap = new HashMap<String, Node>();
	
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
	public String generate(MeasureExport me) throws Exception{
		
		String dataCriteria = "";
		dataCriteria = getHQMFXmlString(me);
		/*dataCriteria = removeXmlTagNamespaceAndPreamble(dataCriteria);*/
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
		me.setHQMFXmlProcessor(dataCriteriaXMLProcessor);
		
		String simpleXMLStr = me.getSimpleXML();
		XmlProcessor simpleXmlprocessor = new XmlProcessor(simpleXMLStr);
		me.setSimpleXMLProcessor(simpleXmlprocessor);
		
		prepHQMF(me);
		
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
		//textElem.setAttribute(VALUE, "Data Criteria text");
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
		String xPathForOccurQDMNoAttribs = "/measure/elementLookUp/qdm[@datatype != 'attribute'][@instance]";
		String xPathForQDMNoAttribs = "/measure/elementLookUp/qdm[@datatype != 'attribute']";
		String xPathForQDMAttributes = "/measure/elementLookUp/qdm[@datatype = 'attribute']";
		String xpathForSupplementalQDMs = "/measure/elementLookUp/qdm[@suppDataElement = 'true']";
		String xpathForOtherSupplementalQDMs = "/measure/supplementalDataElements/elementRef/@id";
		String xpathForMeasureGroupingItemCount = "/measure/measureGrouping//itemCount/elementRef/@id";
		
		try {
			
			NodeList occurQdmNoAttributeNodeList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xPathForOccurQDMNoAttribs);
			generateOccurrenceQDMEntries(simpleXmlprocessor, dataCriteriaXMLProcessor, occurQdmNoAttributeNodeList);
			
			NodeList qdmNoAttributeNodeList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xPathForQDMNoAttribs);
			generateQDMEntries(dataCriteriaXMLProcessor, simpleXmlprocessor,
					qdmNoAttributeNodeList);
			
			NodeList qdmAttributeNodeList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xPathForQDMAttributes);
			generateQDMAttributeEntries(dataCriteriaXMLProcessor, simpleXmlprocessor,
					qdmAttributeNodeList);
			//generating QDM Entries for default Supplemental Data Elements
			NodeList supplementalQDMNodeList = simpleXmlprocessor.findNodeList(simpleXmlprocessor.getOriginalDoc(), xpathForSupplementalQDMs);
			generateSupplementalDataQDMEntries(simpleXmlprocessor, dataCriteriaXMLProcessor, supplementalQDMNodeList);
			
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
		generateSupplementalDataQDMEntries(me, dataCriteriaXMLProcessor, measureGroupingElementRefNodeList);
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
					qdmUUID+"'][@extension='"+qdmExtension+"']";
			Node qmdEntryIDNode = dataCriteriaXMLProcessor.findNode(dataCriteriaXMLProcessor.getOriginalDoc(),
					xpathForQDMEntry);
			if (qmdEntryIDNode==null) {
				createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, me.getSimpleXMLProcessor(), null);
			}
		}
	}
	
	
	/**
	 * Generate default supplemental data qdm entries.
	 *
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param supplementalQDMNodeList the supplemental qdm node list
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateSupplementalDataQDMEntries(XmlProcessor simpleXmlprocessor, XmlProcessor dataCriteriaXMLProcessor,
			NodeList supplementalQDMNodeList) throws XPathExpressionException{
		
		if (supplementalQDMNodeList == null) {
			return;
		}
		
		for (int i = 0; i < supplementalQDMNodeList.getLength(); i++) {
			Node qdmNode = supplementalQDMNodeList.item(i);
			//generateQDMEntry(dataCriteriaXMLProcessor, simpleXmlprocessor, qdmNode);
			createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor, null);
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
	
	/**
	 * This method will populate a map of all reference elements for the Occurrence elements.
	 *
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param occurQdmNoAttributeNodeList the occur qdm no attribute node list
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateOccurrenceQDMEntries(XmlProcessor simpleXmlprocessor, XmlProcessor dataCriteriaXMLProcessor,
			NodeList occurQdmNoAttributeNodeList) throws XPathExpressionException {
		
		Map<String, Node> map = new HashMap<String, Node>();
		
		for(int i=0;i<occurQdmNoAttributeNodeList.getLength();i++){
			Node occurQdmNode = occurQdmNoAttributeNodeList.item(i);
			String datatype = occurQdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
			String oid = occurQdmNode.getAttributes().getNamedItem(OID).getNodeValue();
			
			if(!map.containsKey(datatype+"-"+oid)){
				map.put(datatype+"-"+oid, occurQdmNode);
			}
		}
		
		for(Node occurNode:map.values()){
			String datatype = occurNode.getAttributes().getNamedItem("datatype").getNodeValue();
			String oid = occurNode.getAttributes().getNamedItem(OID).getNodeValue();
			
			String xpath = "/measure/elementLookUp/qdm[@datatype != 'attribute'][not(@instance)][@oid = '"+oid+"'][@datatype = '"+datatype+"']";
			Node nodeToUse = simpleXmlprocessor.findNode(simpleXmlprocessor.getOriginalDoc(), xpath);
			boolean forceGenerate = true;
			if(nodeToUse == null){
				nodeToUse = occurNode.cloneNode(true);
				nodeToUse.getAttributes().removeNamedItem("instance");
				forceGenerate = false;
			}
			
			generateQDMEntry(dataCriteriaXMLProcessor, simpleXmlprocessor, nodeToUse, forceGenerate);
			occurrenceMap.put(datatype+"-"+oid, nodeToUse);
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
			generateQDMEntry(dataCriteriaXMLProcessor, simpleXmlprocessor, qdmNode);
		}
	}
	
	/**
	 * Generate qdm entry.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param qdmNode the qdm node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateQDMEntry(XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node qdmNode)
					throws XPathExpressionException {
		generateQDMEntry(dataCriteriaXMLProcessor, simpleXmlprocessor, qdmNode, false);
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
			createXmlForDataCriteria(qdmNode, dataCriteriaXMLProcessor, simpleXmlprocessor, null);
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
				+"' and  @name != '"+FACILITY_LOCATION+"' and @name != '"+ADMISSION_DATETIME
				+"' and @name != '"+DISCHARGE_DATETIME +"' and @name != '"+ACTIVE_DATETIME+"' and @name != '"+INCISION_DATETIME+"' and @name != '"+DATE
				+"' and @name != '"+TIME+"' and @name != '"+REMOVAL_DATETIME+"' and @name != '"+SIGNED_DATETIME+"']";
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
				+"' or @name = '"+FACILITY_LOCATION+"' or @name ='"+ADMISSION_DATETIME+"' or @name ='"+DISCHARGE_DATETIME+"'" +
				"or @name ='"+ACTIVE_DATETIME+"' or @name ='"+INCISION_DATETIME+"' or @name ='"+DATE+"' or @name ='"+TIME+"'" +
				"or @name ='"+REMOVAL_DATETIME+"' or @name ='"+SIGNED_DATETIME+"']"
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
	 * @return void
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
		String qdmTaxonomy = qdmNode.getAttributes()
				.getNamedItem(TAXONOMY).getNodeValue();
		String entryCommentText = dataType;
		// Local variable changes.
		String qdmLocalVariableName = qdmName + "_" + dataType;
		String localVariableName = qdmLocalVariableName;
		if(qdmNode.getAttributes().getNamedItem("instance") != null){
			qdmLocalVariableName = qdmNode.getAttributes().getNamedItem("instance").getNodeValue() +"_" + qdmLocalVariableName;
			localVariableName = qdmNode.getAttributes().getNamedItem("instance").getNodeValue() + "of" + localVariableName;
		}
		qdmLocalVariableName = StringUtils.deleteWhitespace(qdmLocalVariableName);
		if(attributeQDMNode != null){
			if(attributeQDMNode.getUserData(ATTRIBUTE_UUID) != null){
				qdmLocalVariableName = (String)attributeQDMNode.getUserData(ATTRIBUTE_UUID);
			}
			if(attributeQDMNode.getUserData(ATTRIBUTE_NAME) != null){
				entryCommentText = entryCommentText+ " - " +attributeQDMNode.getUserData(ATTRIBUTE_NAME);
				localVariableName = localVariableName + "_" +attributeQDMNode.getUserData(ATTRIBUTE_NAME);
			}
			if(attributeQDMNode.getUserData(ATTRIBUTE_MODE) != null){
				entryCommentText = entryCommentText+ " With " +attributeQDMNode.getUserData(ATTRIBUTE_MODE);
				localVariableName = localVariableName + "_" +attributeQDMNode.getUserData(ATTRIBUTE_MODE);
			}
		}
		localVariableName = StringUtils.deleteWhitespace(localVariableName);
		
		Element dataCriteriaSectionElem = (Element) dataCriteriaXMLProcessor
				.getOriginalDoc().getElementsByTagName("dataCriteriaSection")
				.item(0);
		Element componentElem = (Element) dataCriteriaXMLProcessor
				.getOriginalDoc().getElementsByTagName("component")
				.item(0);
		Attr nameSpaceAttr = dataCriteriaXMLProcessor.getOriginalDoc()
				.createAttribute("xmlns:xsi");
		nameSpaceAttr.setNodeValue(nameSpace);
		componentElem.setAttributeNodeNS(nameSpaceAttr);
		//xmlns:qdm="urn:hhs-qdm:hqmf-r2-extensions:v1"
		Attr qdmNameSpaceAttr = dataCriteriaXMLProcessor.getOriginalDoc().createAttribute("xmlns:qdm");
		qdmNameSpaceAttr.setNodeValue("urn:hhs-qdm:hqmf-r2-extensions:v1");
		componentElem.setAttributeNodeNS(qdmNameSpaceAttr);
		// creating Entry Tag
		Element entryElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement("entry");
		entryElem.setAttribute(TYPE_CODE, "DRIV");
		//Local Variable Name Tag - Inside Entry tag.
		Element localVarElem = dataCriteriaXMLProcessor.getOriginalDoc().createElement("localVariableName");
		localVarElem.setAttribute(VALUE, localVariableName + "_" + UUIDUtilClient.uuid(5));
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
		if (templateNode.getAttributes().getNamedItem("addExtensionInTemplate") == null) {
			itemChild.setAttribute("extension", VERSIONID);
		}
		templateId.appendChild(itemChild);
		Element idElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(ID);
		idElem.setAttribute(ROOT, rootValue);
		idElem.setAttribute("extension", qdmLocalVariableName);
		dataCriteriaElem.appendChild(idElem);
		
		boolean appendEntryElem = false;
		String occurString = dataType + "-" + qdmOidValue;
		
		if(qdmNode.getAttributes().getNamedItem("instance") != null){
			generateOutboundForOccur(templateNode, qdmNode, dataCriteriaElem, occurString, dataCriteriaXMLProcessor, simpleXmlprocessor);
			entryCommentText = qdmNode.getAttributes().getNamedItem("instance").getNodeValue() + " " + entryCommentText;
			appendEntryElem = true;
		}else if(!occurrenceMap.containsKey(occurString) || (attributeQDMNode != null)){
			
			String isAddCodeTag = templateNode.getAttributes().getNamedItem("addCodeTag").getNodeValue();
			if ("true".equalsIgnoreCase(isAddCodeTag)) { // Add Code Element to DataCriteria Element.
				addCodeElementToDataCriteriaElement(templateNode, dataCriteriaXMLProcessor
						, qdmNode, dataCriteriaElem);
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
					addValueSetVersion(qdmNode, valueElem);
					displayNameElem.setAttribute(VALUE, qdmName+" "+qdmTaxonomy+" Value Set");
				}
				if(displayNameElem.hasAttribute(VALUE)){
					valueElem.appendChild(displayNameElem);
				}
				dataCriteriaElem.appendChild(valueElem);
			}
			if(templateNode.getAttributes().getNamedItem("includeSubTemplate") !=null){
				appendSubTemplateNode(templateNode, dataCriteriaXMLProcessor, templateXMLProcessor, dataCriteriaElem, qdmNode,attributeQDMNode);
			}
			//checkForAttributes
			
			appendEntryElem = true;
		}
		if(appendEntryElem){
			if (attributeQDMNode != null) {
				createDataCriteriaForAttributes(qdmNode, dataCriteriaElem,
						dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
			}
			dataCriteriaSectionElem.appendChild(entryElem);
			addCommentNode(dataCriteriaXMLProcessor, entryCommentText, entryElem);
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
		String valueSetVersion = qdmNode.getAttributes().getNamedItem("version").getNodeValue();
		boolean addVersionToValueTag = false;
		if ("1.0".equals(valueSetVersion) || "1".equals(valueSetVersion)) {
			if (qdmNode.getAttributes().getNamedItem("expansionIdentifier") != null) {
				valueSetVersion = "vsac:profile:" + qdmNode.getAttributes().getNamedItem("expansionIdentifier").getNodeValue();
				addVersionToValueTag = true;
			} else {
				addVersionToValueTag = false;
			}
		} else {
			valueSetVersion = "vsac:version:" + qdmNode.getAttributes().getNamedItem("version").getNodeValue();
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
				cloneRefNode.getAttributes().getNamedItem("name").setNodeValue(occName+"_"+name);
				
				if(!occurrenceMap.containsKey(occName+occurString)){
					occurrenceMap.remove(occurString);
					generateQDMEntry(dataCriteriaXMLProcessor, simpleXmlprocessor, cloneRefNode, true);
					occurrenceMap.put(occurString, refNode);
					occurrenceMap.put(occName+occurString,cloneRefNode);
				}
				
				String refRootValue = cloneRefNode.getAttributes().getNamedItem(ID).getNodeValue();
				
				String refDatatype = cloneRefNode.getAttributes().getNamedItem("datatype").getNodeValue();
				String refQdmName = cloneRefNode.getAttributes().getNamedItem("name").getNodeValue();
				String reExt = StringUtils.deleteWhitespace(refQdmName + "_" + refDatatype);
				
				Element outboundRelElem = dataCriteriaElem.getOwnerDocument().createElement("outboundRelationship");
				outboundRelElem.setAttribute("typeCode", "OCCR");
				
				Element criteriaRefElem = dataCriteriaElem.getOwnerDocument().createElement("criteriaReference");
				String refClassCodeValue = templateNode.getAttributes().getNamedItem(CLASS)
						.getNodeValue();
				String refMoodValue = templateNode.getAttributes().getNamedItem(MOOD)
						.getNodeValue();
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
				// TODO Auto-generated catch block
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
		String dataType = qdmNode.getAttributes().getNamedItem("datatype")
				.getNodeValue();
		String qdmOidValue = qdmNode.getAttributes().getNamedItem(OID)
				.getNodeValue();
		
		String qdmName = qdmNode.getAttributes()
				.getNamedItem(NAME).getNodeValue();
		String qdmTaxonomy = qdmNode.getAttributes()
				.getNamedItem(TAXONOMY).getNodeValue();
		
		//Patient Characteristic data type - contains code tag with valueSetId attribute and no title and value set tag.
		boolean isPatientChar = templateNode.getAttributes().getNamedItem("valueSetId") != null;
		boolean isAddValueSetInCodeTrue =templateNode.getAttributes().getNamedItem("addValueSetInCode")!=null;
		boolean isIntervention = ("Intervention, Order".equals(dataType) || "Intervention, Performed".equals(dataType) || "Intervention, Recommended".equals(dataType));
		
		
		if (isAddValueSetInCodeTrue)  {
			Element codeElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(CODE);
			Node valueTypeAttr = templateNode.getAttributes().getNamedItem("valueType");
			if (valueTypeAttr != null) {
				codeElem.setAttribute(XSI_TYPE, valueTypeAttr.getNodeValue());
			}
			codeElem.setAttribute("valueSet", qdmOidValue);
			addValueSetVersion(qdmNode, codeElem);
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
		} else if(isIntervention){
			Element codeElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(CODE);
			codeElem.setAttribute("valueSet", qdmOidValue);
			Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			displayNameElem.setAttribute(VALUE, qdmName+" "+qdmTaxonomy+" Value Set");
			codeElem.appendChild(displayNameElem);
			dataCriteriaElem.appendChild(codeElem);
		}else {
			Element codeElement = createCodeForDatatype(templateNode,
					dataCriteriaXMLProcessor);
			if (codeElement != null) {
				dataCriteriaElem.appendChild(codeElement);
			}
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
	 * @param attributeQDMNode the attribute qdm node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void appendSubTemplateNode(Node templateNode, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor templateXMLProcessor,
			Element dataCriteriaElem, Node qdmNode , Node attributeQDMNode) throws XPathExpressionException {
		String subTemplateName = templateNode.getAttributes().getNamedItem("includeSubTemplate").getNodeValue();
		Node  subTemplateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/"
				+ subTemplateName);
		NodeList subTemplateNodeChilds = templateXMLProcessor.findNodeList(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/"
				+ subTemplateName + "/child::node()");
		String qdmOidValue = qdmNode.getAttributes().getNamedItem(OID)
				.getNodeValue();
		String version = qdmNode.getAttributes().getNamedItem("version")
				.getNodeValue();
		boolean addVersionToValueTag = false;
		if ("1.0".equals(version) || "1".equals(version)) {
			if (qdmNode.getAttributes().getNamedItem("expansionIdentifier") != null) {
				version = "vsac:profile:" + qdmNode.getAttributes().getNamedItem("expansionIdentifier").getNodeValue();
				addVersionToValueTag = true;
			} else {
				addVersionToValueTag = false;
			}
		} else {
			addVersionToValueTag = true;
			version = "vsac:version:" + qdmNode.getAttributes().getNamedItem("version")
					.getNodeValue();
		}
		String qdmName = qdmNode.getAttributes().getNamedItem(NAME).getNodeValue();
		String qdmNameDataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		String qdmTaxonomy = qdmNode.getAttributes().getNamedItem(TAXONOMY).getNodeValue();
		if (subTemplateNode.getAttributes().getNamedItem("changeAttribute") != null) {
			String[] attributeToBeModified = subTemplateNode.getAttributes().
					getNamedItem("changeAttribute").getNodeValue().split(",");
			for (String changeAttribute : attributeToBeModified) {
				Node  attributedToBeChangedInNode = null;
				attributedToBeChangedInNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc()
						, "/templates/subtemplates/" + subTemplateName + "//" + changeAttribute);
				if (changeAttribute.equalsIgnoreCase(ID)) {
					String rootId = qdmNode.getAttributes().getNamedItem("uuid").getNodeValue();
					attributedToBeChangedInNode.getAttributes().getNamedItem("root").
					setNodeValue(rootId);
					attributedToBeChangedInNode.getAttributes().getNamedItem("extension").
					setNodeValue(UUIDUtilClient.uuid());
				} else if (changeAttribute.equalsIgnoreCase(CODE)) {
					attributedToBeChangedInNode.getAttributes().getNamedItem("valueSet").setNodeValue(qdmOidValue);
					if(addVersionToValueTag){
						Attr attrNode = attributedToBeChangedInNode.getOwnerDocument().createAttribute("valueSetVersion");
						attrNode.setNodeValue(version);
						attributedToBeChangedInNode.getAttributes().setNamedItem(attrNode);
					}
					
				} else if (changeAttribute.equalsIgnoreCase(DISPLAY_NAME)) {
					attributedToBeChangedInNode.getAttributes().getNamedItem("value").
					setNodeValue(qdmName + " " + qdmTaxonomy + " value set");
				} else if (changeAttribute.equalsIgnoreCase(TITLE)) {
					attributedToBeChangedInNode.getAttributes().getNamedItem("value").setNodeValue(qdmNameDataType);
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
			Node  attributedToBeChangedInNode = null;
			String[] tagToBeModified = subTemplateNode.getAttributes().getNamedItem("changeAttribute").getNodeValue().split(",");
			for (String changeAttribute : tagToBeModified) {
				attributedToBeChangedInNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/"
						+ subTemplateName+"//"+changeAttribute);
				if (changeAttribute.equalsIgnoreCase(ID)) {
					Node childNodes = attributedToBeChangedInNode.getFirstChild().getNextSibling();
					String rootId = (String) attrNode.getUserData(ATTRIBUTE_UUID);
					childNodes.getAttributes().getNamedItem("root").
					setNodeValue(rootId);
					childNodes.getAttributes().getNamedItem("extension").
					setNodeValue(StringUtils.deleteWhitespace((String) attrNode.getUserData(ATTRIBUTE_NAME)));
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
						checkIfSelectedModeIsValueSet(templateXMLProcessor, attrNode, subTemplateNode, (Element)attributedToBeChangedInNode);
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
		}
		for (int i = 0; i < subTemplateNodeChilds.getLength(); i++) {
			Node childNode = subTemplateNodeChilds.item(i);
			Node nodeToAttach = dataCriteriaXMLProcessor.getOriginalDoc().importNode(childNode, true);
			XmlProcessor.clean(nodeToAttach);
			dataCriteriaElem.appendChild(nodeToAttach);
		}
	}
	
	
	/**
	 * Append sub template with order attribute.
	 *
	 * @param templateNode the template node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param templateXMLProcessor the template xml processor
	 * @param dataCriteriaElem the data criteria elem
	 * @param qdmNode the qdm node
	 * @param attrNode the attr node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void appendSubTemplateWithOrderAttribute(Node templateNode, XmlProcessor dataCriteriaXMLProcessor, XmlProcessor templateXMLProcessor,
			Element dataCriteriaElem,Node qdmNode, Node attrNode) throws XPathExpressionException{
		String subTemplateName = templateNode.getAttributes().getNamedItem("includeOtherSubTemplate").getNodeValue();
		Node  subTemplateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/"
				+ subTemplateName);
		NodeList subTemplateNodeChilds = templateXMLProcessor.findNodeList(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/"
				+ subTemplateName + "/child::node()");
		boolean isOrder = templateNode.getAttributes().getNamedItem("typeOrder").getNodeValue()!=null;
		if(subTemplateNode.getAttributes().getNamedItem("changeAttribute") != null) {
			Node  attributedToBeChangedInNode = null;
			String[] tagToBeModified = subTemplateNode.getAttributes().getNamedItem("changeAttribute").getNodeValue().split(",");
			for (String changeAttribute : tagToBeModified) {
				attributedToBeChangedInNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/subtemplates/"
						+ subTemplateName+"//"+changeAttribute);
				if (changeAttribute.equalsIgnoreCase(ID)) {
					Node childNodes = attributedToBeChangedInNode.getFirstChild().getNextSibling();
					String rootId = (String) attrNode.getUserData(ATTRIBUTE_UUID);
					childNodes.getAttributes().getNamedItem("root").
					setNodeValue(rootId);
					childNodes.getAttributes().getNamedItem("extension").
					setNodeValue(StringUtils.deleteWhitespace((String) attrNode.getUserData(ATTRIBUTE_NAME)));
				}
			}
		}
		
		for (int i = 0; i < subTemplateNodeChilds.getLength(); i++) {
			Node childNode = subTemplateNodeChilds.item(i);
			Node nodeToAttach = dataCriteriaXMLProcessor.getOriginalDoc().importNode(childNode, true);
			XmlProcessor.clean(nodeToAttach);
			dataCriteriaElem.appendChild(nodeToAttach);
		}
		Element timeNode = dataCriteriaXMLProcessor.getOriginalDoc().createElement(TIME);
		generateDateTimeAttributesTag(timeNode, attrNode, dataCriteriaElem, dataCriteriaXMLProcessor, isOrder);
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
		if(NEGATION_RATIONALE.equals(attributeName)) {
			generateNegationRationalEntries(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
		}else if (START_DATETIME.equals(attributeName) || STOP_DATETIME.equals(attributeName)
				|| SIGNED_DATETIME.equals(attributeName)) {
			generateOrderTypeAttributes(qdmNode, dataCriteriaElem, dataCriteriaXMLProcessor,
					simpleXmlprocessor, attributeQDMNode);
			//		} else if(SIGNED_DATETIME.equals(attributeName)){
			//			Element timeNode = dataCriteriaXMLProcessor.getOriginalDoc().createElement(TIME);
			//			generateDateTimeAttributesTag(timeNode, attributeQDMNode, dataCriteriaElem, dataCriteriaXMLProcessor, true);
		}else if(ADMISSION_DATETIME.equalsIgnoreCase(attributeName)
				|| DISCHARGE_DATETIME.equalsIgnoreCase(attributeName)
				|| REMOVAL_DATETIME.equalsIgnoreCase(attributeName)
				|| ACTIVE_DATETIME.equalsIgnoreCase(attributeName)
				|| TIME.equalsIgnoreCase(attributeName)
				|| DATE.equalsIgnoreCase(attributeName)) {
			generateDateTimeAttributes(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
		} else if (FACILITY_LOCATION_ARRIVAL_DATETIME.equalsIgnoreCase(attributeName)
				|| FACILITY_LOCATION_DEPARTURE_DATETIME.equalsIgnoreCase(attributeName)) {
			generateFacilityLocationTypeAttributes(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
		} else if (DOSE.equalsIgnoreCase(attributeName)
				|| LENGTH_OF_STAY.equalsIgnoreCase(attributeName)) {
			generateDoseTypeAttributes(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
		} else if (attributeName.equalsIgnoreCase(REFILLS)) {
			generateRepeatNumber(qdmNode, dataCriteriaXMLProcessor, dataCriteriaElem, attributeQDMNode);
		} else if (attributeName.equalsIgnoreCase(DISCHARGE_STATUS)) {
			generateDischargeStatus(qdmNode, dataCriteriaXMLProcessor, dataCriteriaElem, attributeQDMNode);
		} else if (attributeName.equalsIgnoreCase(INCISION_DATETIME)) {
			generateIncisionDateTimeTypeAttributes(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
		}else if (VALUE_SET.equals(attributeMode)
				|| CHECK_IF_PRESENT.equals(attributeMode)
				|| attributeMode.startsWith(LESS_THAN)
				|| attributeMode.startsWith(GREATER_THAN)
				|| EQUAL_TO.equals(attributeMode)) {
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
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateDoseTypeAttributes(Node qdmNode,
			Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node attributeQDMNode) throws XPathExpressionException {
		
		String attrName = (String) attributeQDMNode.getUserData(ATTRIBUTE_NAME);
		String attrMode = (String) attributeQDMNode.getUserData(ATTRIBUTE_MODE);
		Node attrOID = attributeQDMNode.getAttributes().getNamedItem(OID);
		Node attrVersion = attributeQDMNode.getAttributes().getNamedItem("version");
		boolean isLengthOfStayValueSet = false;
		XmlProcessor templateXMLProcessor = TemplateXMLSingleton.getTemplateXmlProcessor();
		Node templateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/template[text()='"
				+ attrName.toLowerCase() + "']");
		Node targetNode = templateNode.getAttributes().getNamedItem("target");
		Element targetQuantityTag = null;
		if (targetNode!=null) {
			targetQuantityTag = dataCriteriaElem.getOwnerDocument().createElement(targetNode.getNodeValue());
		}
		Node unitAttrib = attributeQDMNode.getAttributes().getNamedItem("unit");
		if (CHECK_IF_PRESENT.equals(attrMode)) {
			targetQuantityTag.setAttribute(FLAVOR_ID, "ANY.NONNULL");
		}  else if (VALUE_SET.equals(attrMode)) {
			if (attrName.equalsIgnoreCase(LENGTH_OF_STAY)) {
				isLengthOfStayValueSet = true;
			} else {
				targetQuantityTag.setAttribute(NULL_FLAVOR, "UNK");
				Element translationNode = dataCriteriaElem.getOwnerDocument().createElement(TRANSLATION);
				translationNode.setAttribute("valueSet", attrOID.getNodeValue());
				String version = attrVersion.getNodeValue();
				boolean addVersionToValueTag = false;
				if ("1.0".equals(version) || "1".equals(version)) {
					if (qdmNode.getAttributes().getNamedItem("expansionIdentifier") != null) {
						version = "vsac:profile:" + qdmNode.getAttributes().getNamedItem("expansionIdentifier")
								.getNodeValue();
						addVersionToValueTag = true;
					} else {
						addVersionToValueTag = false;
					}
				} else {
					addVersionToValueTag = true;
					version = "vsac:version:" + attrVersion.getNodeValue();
				}
				if (addVersionToValueTag) {
					translationNode.setAttribute("valueSetVersion", version);
				}
				Element displayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(DISPLAY_NAME);
				displayNameElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem(NAME).getNodeValue()
						+ " " + attributeQDMNode.getAttributes().getNamedItem(TAXONOMY).getNodeValue() + " Value Set");
				translationNode.appendChild(displayNameElem);
				targetQuantityTag.appendChild(translationNode);
			}
		} else if(attrMode.startsWith(Generator.LESS_THAN) || attrMode.startsWith(Generator.GREATER_THAN) || attrMode.equals(Generator.EQUAL_TO)){
			if(attrMode.equals(Generator.EQUAL_TO)){
				targetQuantityTag.setAttribute("value", attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				if(unitAttrib!=null){
					targetQuantityTag.setAttribute("unit", getUnitString(unitAttrib.getNodeValue()));
				}
			} else if(attrMode.startsWith(Generator.LESS_THAN)){
				Element uncertainRangeNode=  dataCriteriaElem.getOwnerDocument().createElement("uncertainRange");
				if(attrMode.equals(Generator.LESS_THAN)){
					uncertainRangeNode.setAttribute("highClosed", "false");
				}
				Element lowNode = dataCriteriaElem.getOwnerDocument().createElement(LOW);
				if(attrName.equalsIgnoreCase(LENGTH_OF_STAY)){
					lowNode.setAttribute("xsi:type", "PQ");
				}
				lowNode.setAttribute("nullFlavor", "NINF");
				Element highNode = dataCriteriaElem.getOwnerDocument().createElement(HIGH);
				highNode.setAttribute("xsi:type", "PQ");
				highNode.setAttribute("value", attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				if(unitAttrib!=null){
					highNode.setAttribute("unit",  getUnitString(unitAttrib.getNodeValue()));
				}
				
				uncertainRangeNode.appendChild(lowNode);
				uncertainRangeNode.appendChild(highNode);
				targetQuantityTag.appendChild(uncertainRangeNode);
				
			} else if(attrMode.startsWith(Generator.GREATER_THAN)){
				Element uncertainRangeNode=  dataCriteriaElem.getOwnerDocument().createElement("uncertainRange");
				if(attrMode.equals(Generator.GREATER_THAN)){
					uncertainRangeNode.setAttribute("lowClosed", "false");
				}
				Element lowNode = dataCriteriaElem.getOwnerDocument().createElement(LOW);
				lowNode.setAttribute("xsi:type", "PQ");
				lowNode.setAttribute("value", attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				if(unitAttrib!=null){
					lowNode.setAttribute("unit", getUnitString(unitAttrib.getNodeValue()));
				}
				Element highNode = dataCriteriaElem.getOwnerDocument().createElement(HIGH);
				if(attrName.equalsIgnoreCase(LENGTH_OF_STAY)){
					highNode.setAttribute("xsi:type", "PQ");
				}
				highNode.setAttribute("nullFlavor", "PINF");
				uncertainRangeNode.appendChild(lowNode);
				uncertainRangeNode.appendChild(highNode);
				targetQuantityTag.appendChild(uncertainRangeNode);
			}
		}
		String insertAfterNodeName = templateNode.getAttributes().getNamedItem("insertAfterNode").getNodeValue();
		if (insertAfterNodeName != null) {
			if(dataCriteriaElem.getElementsByTagName(insertAfterNodeName).item(0) != null) {
				Node outBoundElement =  dataCriteriaElem.getElementsByTagName(insertAfterNodeName).item(0).getNextSibling();
				if (outBoundElement != null) {
					outBoundElement.getParentNode().insertBefore(targetQuantityTag, outBoundElement);
				} else {
					if(!isLengthOfStayValueSet){
						dataCriteriaElem.appendChild(targetQuantityTag);
					}
				}
			} else {
				dataCriteriaElem.appendChild(targetQuantityTag);
			}
		} else {
			if(!isLengthOfStayValueSet){
				dataCriteriaElem.appendChild(targetQuantityTag);
			}
		}
		
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
			appendSubTemplateInFacilityAttribute(templateNode, dataCriteriaXMLProcessor, templateXMLProcessor, dataCriteriaElem, attributeQDMNode);
		}
		generateDateTimeAttributes(qdmNode, dataCriteriaElem,
				dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
	}
	
	/**
	 * Generate incision date time type attributes.
	 *
	 * @param qdmNode the qdm node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateIncisionDateTimeTypeAttributes(Node qdmNode, Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node attributeQDMNode) throws XPathExpressionException {
		String attributeName = (String) attributeQDMNode.getUserData(ATTRIBUTE_NAME);
		XmlProcessor templateXMLProcessor = TemplateXMLSingleton.getTemplateXmlProcessor();
		Node templateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/template[text()='"
				+ attributeName.toLowerCase() + "']");
		if(templateNode == null){
			return;
		}
		if(templateNode.getAttributes().getNamedItem("includeSubTemplate") !=null){
			appendSubTemplateNode(templateNode, dataCriteriaXMLProcessor, templateXMLProcessor, dataCriteriaElem, qdmNode,attributeQDMNode);
		}
		generateDateTimeAttributes(qdmNode, dataCriteriaElem,
				dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
	}
	
	/**
	 * Generate order type attributes.
	 *
	 * @param qdmNode the qdm node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param simpleXmlprocessor the simple xmlprocessor
	 * @param attributeQDMNode the attribute qdm node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateOrderTypeAttributes(Node qdmNode, Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor,
			XmlProcessor simpleXmlprocessor, Node attributeQDMNode) throws XPathExpressionException {
		String qdmName = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		String attrName = (String)attributeQDMNode.getUserData(ATTRIBUTE_NAME);
		XmlProcessor templateXMLProcessor = TemplateXMLSingleton.getTemplateXmlProcessor();
		Node templateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/template[text()='"
				+ qdmName.toLowerCase() + "']");
		if(templateNode == null){
			return;
		}
		if((templateNode.getAttributes().getNamedItem("includeOtherSubTemplate") !=null)
				|| attrName.equalsIgnoreCase(SIGNED_DATETIME)){
			appendSubTemplateWithOrderAttribute(templateNode, dataCriteriaXMLProcessor, templateXMLProcessor, dataCriteriaElem, qdmNode,attributeQDMNode);
		} else {
			generateDateTimeAttributes(qdmNode, dataCriteriaElem,
					dataCriteriaXMLProcessor, simpleXmlprocessor, attributeQDMNode);
		}
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
			logger.info("----------");
			logger.info(attributeQDMNode.getNodeName());
			logger.info(attributeQDMNode.getAttributes());
			logger.info(simpleXmlprocessor.transform(attributeQDMNode));
			logger.info("----------");
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
			addValueSetVersion(attributeQDMNode, valueElem);
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
		String attribUUID = (String) attributeQDMNode.getUserData(ATTRIBUTE_UUID);
		String qdmName = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		boolean isResult = "result".equalsIgnoreCase(attrName);
		
		boolean isResultNotOutBound = isResult && ("Diagnostic Study, Performed".equalsIgnoreCase(qdmName)
				|| "Laboratory Test, Performed".equalsIgnoreCase(qdmName)
				|| "Functional Status, Performed".equalsIgnoreCase(qdmName)
				|| "Risk Category Assessment".equalsIgnoreCase(qdmName));
		
		XmlProcessor templateXMLProcessor = TemplateXMLSingleton.getTemplateXmlProcessor();
		Node templateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/template[text()='"
				+ attrName.toLowerCase() + "']");
		boolean isRadiation = false;
		if (templateNode == null) {
			templateNode = templateXMLProcessor.findNode(templateXMLProcessor.getOriginalDoc(), "/templates/template[text()='"
					+ attrName.toLowerCase() + "-" + attrMode.toLowerCase() + "']");
			if (templateNode == null) {
				return;
			} else {
				if (ANATOMICAL_LOCATION_SITE.equalsIgnoreCase(attrName)
						|| ORDINALITY.equalsIgnoreCase(attrName)
						|| ROUTE.equalsIgnoreCase(attrName)
						|| "method".equalsIgnoreCase(attrName)
						|| ANATOMICAL_APPROACH_SITE.equalsIgnoreCase(attrName)) {
					addTargetSiteOrPriorityCodeOrRouteCodeElement(dataCriteriaElem,
							dataCriteriaXMLProcessor, attributeQDMNode, templateNode);
				} else if (LATERALITY.equalsIgnoreCase(attrName)) {
					appendSubTemplateNode(templateNode, dataCriteriaXMLProcessor
							, templateXMLProcessor, dataCriteriaElem, qdmNode, attributeQDMNode);
				}
				return;
			}
		}
		//flag to add statusCode for Radiation Dosage and Radiation Duration attributes
		if (templateNode.getAttributes().getNamedItem("isRadiation") != null) {
			isRadiation = templateNode.getAttributes().getNamedItem("isRadiation").getNodeValue() != null;
		}
		if (attrName.equalsIgnoreCase(FACILITY_LOCATION)) {
			if (templateNode.getAttributes().getNamedItem("includeSubTemplate") !=null) {
				appendSubTemplateInFacilityAttribute(templateNode, dataCriteriaXMLProcessor, templateXMLProcessor, dataCriteriaElem, attributeQDMNode);
			}
			return;
		}
		Element outboundRelationshipElem = null;
		Element observationCriteriaElem = null;
		if (!isResultNotOutBound) { //result attribute with specific Datatypes does'nt add OutBoundRelationShip
			outboundRelationshipElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(OUTBOUND_RELATIONSHIP);
			outboundRelationshipElem.setAttribute(TYPE_CODE, templateNode.getAttributes().getNamedItem(TYPE).getNodeValue());
			
			Node invAttribNode = templateNode.getAttributes().getNamedItem("inv");
			if(invAttribNode != null){
				outboundRelationshipElem.setAttribute("inversionInd", invAttribNode.getNodeValue());
			}
			
			observationCriteriaElem = dataCriteriaXMLProcessor.getOriginalDoc()
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
				if (templateNode.getAttributes().getNamedItem("addExtensionInTemplate") == null) {
					itemChild.setAttribute("extension", VERSIONID);
				}
				templateId.appendChild(itemChild);
			}
			
			Element idElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(ID);
			idElem.setAttribute(ROOT, attribUUID);
			idElem.setAttribute("extension", StringUtils.deleteWhitespace(attrName));
			observationCriteriaElem.appendChild(idElem);
			Element codeElem = createCodeForDatatype(templateNode, dataCriteriaXMLProcessor);
			if((isRadiation || isResult) && (codeElem!=null)){
				observationCriteriaElem.appendChild(codeElem);
			} else {
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
			}
			if(!isRadiation){
				Element titleElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(TITLE);
				titleElem.setAttribute(VALUE, attrName);
				observationCriteriaElem.appendChild(titleElem);
			}  if(isRadiation){//statusCode is added for Radiation Duration and Dosage
				Element statusCodeElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(STATUS_CODE);
				if(templateNode.getAttributes().getNamedItem("status") != null){
					statusCodeElem.setAttribute(CODE, templateNode.getAttributes().getNamedItem("status").getNodeValue());
				}
				observationCriteriaElem.appendChild(statusCodeElem);
			}
		}
		Element valueElem =  dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(VALUE);
		if(VALUE_SET.equals(attrMode)){
			checkIfSelectedModeIsValueSet(dataCriteriaXMLProcessor, attributeQDMNode, templateNode,valueElem);
		} else if(CHECK_IF_PRESENT.equalsIgnoreCase(attrMode)){
			checkIfSelectedModeIsPresent(dataCriteriaXMLProcessor, attributeQDMNode, templateNode, valueElem);
		}else if(EQUAL_TO.equals(attrMode) || attrMode.startsWith(LESS_THAN) || attrMode.startsWith(GREATER_THAN)){
			checkIfSelectedModeIsArthimaticExpr(dataCriteriaXMLProcessor, attributeQDMNode, templateNode,valueElem);
		}
		if((outboundRelationshipElem!=null) && (observationCriteriaElem!=null)){
			observationCriteriaElem.appendChild(valueElem);
			dataCriteriaElem.appendChild(outboundRelationshipElem);
		} else {
			NodeList outboundRelationshipList = dataCriteriaElem.getElementsByTagName("outboundRelationship");
			if((outboundRelationshipList != null) && (outboundRelationshipList.getLength() > 0)){
				Node outboundRelationshipNode = outboundRelationshipList.item(0);
				dataCriteriaElem.insertBefore(valueElem, outboundRelationshipNode);
			}else{
				dataCriteriaElem.appendChild(valueElem);
			}
		}
	}
	
	/**
	 * Refills Attribute tags.
	 *
	 * @param templateNode the template node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param dataCriteriaElem the data criteria elem
	 * @param attributeQDMNode the attribute qdm node
	 */
	private void generateRepeatNumber(Node templateNode, XmlProcessor dataCriteriaXMLProcessor,
			Element dataCriteriaElem, Node attributeQDMNode) {
		String attrMode = (String) attributeQDMNode.getUserData(ATTRIBUTE_MODE);
		Element repeatNumberElement =  dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement("repeatNumber");
		if (CHECK_IF_PRESENT.equalsIgnoreCase(attrMode)) {
			repeatNumberElement.setAttribute(FLAVOR_ID, "ANY.NONNULL");
			dataCriteriaElem.appendChild(repeatNumberElement);
		}  else if (EQUAL_TO.equals(attrMode) || attrMode.startsWith(LESS_THAN) || attrMode.startsWith(GREATER_THAN)) {
			if (EQUAL_TO.equals(attrMode)) {
				Element lowElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(LOW);
				lowElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				
				Element highElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(HIGH);
				highElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				repeatNumberElement.appendChild(lowElem);
				repeatNumberElement.appendChild(highElem);
				dataCriteriaElem.appendChild(repeatNumberElement);
			} else if(attrMode.startsWith(GREATER_THAN)) {
				if (attrMode.equals(GREATER_THAN)) {
					repeatNumberElement.setAttribute("lowClosed", "false");
				}
				Element lowElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(LOW);
				lowElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				repeatNumberElement.appendChild(lowElem);
				dataCriteriaElem.appendChild(repeatNumberElement);
			}else if(attrMode.startsWith(LESS_THAN)){
				if(attrMode.equals(LESS_THAN)){
					repeatNumberElement.setAttribute("highClosed", "false");
				}
				Element highElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(HIGH);
				highElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				repeatNumberElement.appendChild(highElem);
				dataCriteriaElem.appendChild(repeatNumberElement);
			}
		}
	}
	
	/**
	 * Discharge Status Attribute tags.
	 *
	 * @param templateNode the template node
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param dataCriteriaElem the data criteria elem
	 * @param attributeQDMNode the attribute qdm node
	 */
	private void generateDischargeStatus(Node templateNode, XmlProcessor dataCriteriaXMLProcessor,
			Element dataCriteriaElem, Node attributeQDMNode) {
		String attrMode = (String) attributeQDMNode.getUserData(ATTRIBUTE_MODE);
		Element dischargeDispositionElement =  dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement("dischargeDispositionCode");
		if (CHECK_IF_PRESENT.equalsIgnoreCase(attrMode)) {
			dischargeDispositionElement.setAttribute(FLAVOR_ID, "ANY.NONNULL");
			dataCriteriaElem.appendChild(dischargeDispositionElement);
		}  else if (VALUE_SET.equalsIgnoreCase(attrMode)) {
			String attributeValueSetName = attributeQDMNode.getAttributes()
					.getNamedItem(NAME).getNodeValue();
			
			String attributeOID = attributeQDMNode.getAttributes()
					.getNamedItem(OID).getNodeValue();
			String attributeTaxonomy = attributeQDMNode.getAttributes()
					.getNamedItem(TAXONOMY).getNodeValue();
			dischargeDispositionElement.setAttribute("valueSet", attributeOID);
			addValueSetVersion(attributeQDMNode, dischargeDispositionElement);
			Element valueDisplayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(DISPLAY_NAME);
			valueDisplayNameElem.setAttribute(VALUE, attributeValueSetName+" "+attributeTaxonomy+" Value Set");
			dischargeDispositionElement.appendChild(valueDisplayNameElem);
			dataCriteriaElem.appendChild(dischargeDispositionElement);
		}
	}
	
	/**
	 * Check if selected mode is value set.
	 *
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param attributeQDMNode the attribute qdm node
	 * @param templateNode the template node
	 * @param valueElem the value elem
	 * @return the element
	 */
	private Element checkIfSelectedModeIsValueSet(XmlProcessor dataCriteriaXMLProcessor, Node attributeQDMNode,
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
		addValueSetVersion(attributeQDMNode, valueElem);
		Element valueDisplayNameElem = dataCriteriaXMLProcessor.getOriginalDoc()
				.createElement(DISPLAY_NAME);
		valueDisplayNameElem.setAttribute(VALUE, attributeValueSetName+" "+attributeTaxonomy+" Value Set");
		valueElem.appendChild(valueDisplayNameElem);
		
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
		String attrName = (String) attributeQDMNode.getUserData(ATTRIBUTE_NAME);
		String nodeName = attributeQDMNode.getNodeName();
		boolean isRadiation = false;
		boolean isTargetOutCome = false;
		boolean isResult = "result".equalsIgnoreCase(attrName);
		if(templateNode.getAttributes().getNamedItem("isRadiation")!=null){
			isRadiation = templateNode.getAttributes().getNamedItem("isRadiation").getNodeValue()!=null;
		}
		if(templateNode.getAttributes().getNamedItem("isTargetOutcome")!=null){
			isTargetOutCome = templateNode.getAttributes().getNamedItem("isTargetOutcome").getNodeValue()!=null;
		}
		
		if(nodeName.equals("attribute"))
		{
			valueElem.setAttribute(XSI_TYPE, "IVL_PQ");
			Node unitAttrib = attributeQDMNode.getAttributes().getNamedItem("unit");
			if(EQUAL_TO.equals(attrMode) ){
				if(isRadiation){ //for radiation dosage and radiation duration
					valueElem.getAttributes().getNamedItem(XSI_TYPE).setNodeValue("PQ");
					valueElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
					if (unitAttrib != null) {
						String unitString = getUnitString(unitAttrib.getNodeValue());
						valueElem.setAttribute("unit", unitString);
					}
				} else { //for attributes other than radiation duration and radiation dosage
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
					if(isResult){
						lowElem.setAttribute(XSI_TYPE, "PQ");
						highElem.setAttribute(XSI_TYPE, "PQ");
					}
					valueElem.appendChild(lowElem);
					valueElem.appendChild(highElem);
				}
				
			}else if(attrMode.startsWith(GREATER_THAN)){
				if(attrMode.equals(GREATER_THAN)){
					valueElem.setAttribute("lowClosed", "false");
				}
				Element lowElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(LOW);
				if(isResult){
					lowElem.setAttribute(XSI_TYPE, "PQ");
				}
				lowElem.setAttribute(VALUE, attributeQDMNode.getAttributes().getNamedItem("comparisonValue").getNodeValue());
				if(unitAttrib != null){
					String unitString = getUnitString(unitAttrib.getNodeValue());
					lowElem.setAttribute("unit", unitString);
				}
				valueElem.appendChild(lowElem);
				if(isRadiation || isResult || isTargetOutCome){//for radiation dosage and radiation duration
					Element highElem = dataCriteriaXMLProcessor.getOriginalDoc()
							.createElement(HIGH);
					highElem.setAttribute(NULL_FLAVOR, "PINF");
					valueElem.appendChild(highElem);
				}
			}else if(attrMode.startsWith(LESS_THAN)){
				if(attrMode.equals(LESS_THAN)){
					valueElem.setAttribute("highClosed", "false");
				}
				if(isRadiation || isResult || isTargetOutCome){//for radiation dosage and radiation duration
					Element highElem = dataCriteriaXMLProcessor.getOriginalDoc()
							.createElement(LOW);
					highElem.setAttribute(NULL_FLAVOR, "NINF");
					valueElem.appendChild(highElem);
				}
				Element highElem = dataCriteriaXMLProcessor.getOriginalDoc()
						.createElement(HIGH);
				if(isResult){
					highElem.setAttribute(XSI_TYPE, "PQ");
				}
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
		} else if (templateNode.getAttributes().getNamedItem("insertAfterNode") != null) {
			insertAfterNodeName = templateNode.getAttributes().getNamedItem("insertAfterNode").getNodeValue();
		}
		if (templateNode.getAttributes().getNamedItem("childTarget") != null) {
			String qdmOidValue = attributeQDMNode.getAttributes().getNamedItem(OID)
					.getNodeValue();
			//			String version = attributeQDMNode.getAttributes().getNamedItem("version")
			//					.getNodeValue();
			Element valueElem = dataCriteriaXMLProcessor.getOriginalDoc()
					.createElement(ITEM);
			valueElem.setAttribute("valueSet", qdmOidValue);
			addValueSetVersion(attributeQDMNode, valueElem);
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
			addValueSetVersion(attributeQDMNode, targetSiteCodeElement);
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
		
		Element effectiveTimeNode = dataCriteriaXMLProcessor.getOriginalDoc().createElement(EFFECTIVE_TIME);
		effectiveTimeNode.setAttribute(XSI_TYPE, "IVL_TS");
		generateDateTimeAttributesTag(effectiveTimeNode, attributeQDMNode, dataCriteriaElem, dataCriteriaXMLProcessor, false);
	}
	
	
	/**
	 * Generate date time attributes tag.
	 *
	 * @param dateTimeNode the effective time node
	 * @param attributeQDMNode the attribute qdm node
	 * @param dataCriteriaElem the data criteria elem
	 * @param dataCriteriaXMLProcessor the data criteria xml processor
	 * @param isOrder the is order
	 */
	private void generateDateTimeAttributesTag(Node dateTimeNode, Node attributeQDMNode,
			Element dataCriteriaElem, XmlProcessor dataCriteriaXMLProcessor, boolean isOrder) {
		
		String attrName = (String) attributeQDMNode.getUserData(ATTRIBUTE_NAME);
		String attrMode = (String) attributeQDMNode.getUserData(ATTRIBUTE_MODE);
		String attrDate = (String) attributeQDMNode.getUserData(ATTRIBUTE_DATE);
		
		String timeTagName = "";
		if (attrName.equals(START_DATETIME)
				|| attrName.equalsIgnoreCase(FACILITY_LOCATION_ARRIVAL_DATETIME)
				|| ADMISSION_DATETIME.equalsIgnoreCase(attrName)
				|| ACTIVE_DATETIME.equalsIgnoreCase(attrName)
				|| DATE.equalsIgnoreCase(attrName)
				|| TIME.equalsIgnoreCase(attrName)
				|| INCISION_DATETIME.equalsIgnoreCase(attrName)) {
			timeTagName = LOW;
		} else if (attrName.equals(STOP_DATETIME)
				|| attrName.equalsIgnoreCase(FACILITY_LOCATION_DEPARTURE_DATETIME)
				|| attrName.equalsIgnoreCase(DISCHARGE_DATETIME)
				|| attrName.equalsIgnoreCase(REMOVAL_DATETIME)
				|| attrName.equalsIgnoreCase(SIGNED_DATETIME)) {
			timeTagName = HIGH;
		}
		
		if(CHECK_IF_PRESENT.equals(attrMode)){
			
			if(timeTagName.length() > 0){
				Element timeTagNode = dataCriteriaElem.getOwnerDocument().createElement(timeTagName);
				timeTagNode.setAttribute(FLAVOR_ID, "ANY.NONNULL");
				dateTimeNode.appendChild(timeTagNode);
			}
		}else if(attrMode.startsWith(Generator.LESS_THAN) || attrMode.startsWith(Generator.GREATER_THAN) || attrMode.equals(Generator.EQUAL_TO)){
			
			if(attrMode.equals(Generator.EQUAL_TO)){
				if(timeTagName.length() > 0){
					Element timeTagNode = dataCriteriaElem.getOwnerDocument().createElement(timeTagName);
					timeTagNode.setAttribute(VALUE, attrDate);
					dateTimeNode.appendChild(timeTagNode);
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
					dateTimeNode.appendChild(timeTagNode);
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
					dateTimeNode.appendChild(timeTagNode);
				}
			}
		}
		
		/**
		 * If effectiveTimeNode has any child nodes then add it to the main dataCriteriaNode.
		 */
		if(dateTimeNode.hasChildNodes()){
			
			if(attrName.equalsIgnoreCase(START_DATETIME)
					||attrName.equalsIgnoreCase(STOP_DATETIME)
					||attrName.equalsIgnoreCase(SIGNED_DATETIME)){
				NodeList nodeList = dataCriteriaElem.getElementsByTagName("participation");
				if ((nodeList != null) && (nodeList.getLength() > 0) && isOrder) {
					if(nodeList.getLength()>1){
						nodeList.item(1).insertBefore(dateTimeNode, dataCriteriaElem.getElementsByTagName("role").item(1));
					} else {
						nodeList.item(0).insertBefore(dateTimeNode, dataCriteriaElem.getElementsByTagName("role").item(0));
					}
				} else {
					NodeList valueNodeList = dataCriteriaElem.getElementsByTagName("value");
					if ((valueNodeList != null) && (valueNodeList.getLength() > 0)) {
						dataCriteriaElem.insertBefore(dateTimeNode, valueNodeList.item(0));
					} else {
						NodeList statusCodeNodeList =  dataCriteriaElem.getElementsByTagName("statusCode");
						if ((statusCodeNodeList != null) && (statusCodeNodeList.getLength() > 0)) {
							dataCriteriaElem.insertBefore(dateTimeNode
									, statusCodeNodeList.item(0).getNextSibling());
						} else {
							dataCriteriaElem.appendChild(dateTimeNode);
						}
					}
				}
			} else{
				NodeList nodeList = dataCriteriaElem.getElementsByTagName("value");
				if ((nodeList != null) && (nodeList.getLength() > 0)) {
					dataCriteriaElem.insertBefore(dateTimeNode, nodeList.item(0));
				} else {
					
					if (attrName.contains("facility")) {
						NodeList nodeListParticipation =  dataCriteriaElem.getElementsByTagName("role");
						if ((nodeListParticipation != null) && (nodeListParticipation.getLength() > 0)) {
							nodeListParticipation.item(0).getFirstChild().getParentNode().appendChild(dateTimeNode);
						}
					} else if(attrName.equalsIgnoreCase(INCISION_DATETIME)) { //for Incision Datetime Attribute effective Time is Added inside
						NodeList nodeListProcedureCriteria =  dataCriteriaElem.getElementsByTagName("procedureCriteria");
						if ((nodeListProcedureCriteria != null) && (nodeListProcedureCriteria.getLength() > 0)) {
							nodeListProcedureCriteria.item(0).getFirstChild().getParentNode().appendChild(dateTimeNode);
						}
					} else {
						NodeList nodeListParticipation = dataCriteriaElem.getElementsByTagName("participation");
						if ((nodeListParticipation != null) && (nodeListParticipation.getLength() > 0)) {
							dataCriteriaElem.insertBefore(dateTimeNode, nodeListParticipation.item(0));
						} else {
							dataCriteriaElem.appendChild(dateTimeNode);
						}
					}
				}
			}
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
	protected Element createCodeForDatatype(Node templateNode,
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
	 * This method is called before we start generating HQMF code for various data criteria elements.
	 * It will perform the task of prepping the Simple XML for the clause generation process.
	 * It will do the following,
	 * 
	 * 1) Call prepForAGE_AT(MeasureExport me) which will,
	 *    Look for <functionalOp type="AGE AT"...../> and
	 *    replace it with <relationalOp type="SBS" ..../>
	 *    The first child of this new relationalOp will be the elementRef
	 *    for Birthdate QDM element.
	 *    The 2nd child will be the first child of the original
	 *    <functionalOp type="AGE AT"...../>.
	 */
	private void prepHQMF(MeasureExport me) {
		logger.info("Prepping for HQMF Clause generation..............");
		prepForUUID(me);
		prepForAGE_AT(me);
		prepForSatisfiesAll_Any(me);
		//		System.out.println("Done prepping for HQMF Clause generation.");
		//		System.out.println(me.getSimpleXMLProcessor().transform(me.getSimpleXMLProcessor().getOriginalDoc(), true));
		logger.info("Done prepping for HQMF Clause generation.");
	}
	
	private void prepForUUID(MeasureExport me) {
		
		String xPathForFunctionalOp = "/measure/subTreeLookUp//functionalOp";
		String xPathForRelationalOp = "/measure/subTreeLookUp//relationalOp";
		String xPathForSetOp = "/measure/subTreeLookUp//setOp";
		
		try{
			NodeList nodeList = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(), xPathForFunctionalOp);
			addUUIDToNodes(nodeList);
			
			nodeList = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(), xPathForRelationalOp);
			addUUIDToNodes(nodeList);
			
			nodeList = me.getSimpleXMLProcessor().findNodeList(me.getSimpleXMLProcessor().getOriginalDoc(), xPathForSetOp);
			addUUIDToNodes(nodeList);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void addUUIDToNodes(NodeList nodeList){
		if((nodeList != null) && (nodeList.getLength() > 0)){
			for(int i=0;i<nodeList.getLength();i++){
				Node node = nodeList.item(i);
				((Element)node).setAttribute("uuid", UUIDUtilClient.uuid());
			}
		}
	}
	
	/**
	 * This method will be called by prepHQMF(MeasureExport me) method to prep
	 * functionalOp for AGE AT functions. This methiod will do the following,
	 * 
	 *    Look for <functionalOp type="AGE AT"...../> and
	 *    replace it with <relationalOp type="SBS" ..../>
	 *    The first child of this new relationalOp will be the elementRef
	 *    for Birthdate QDM element.
	 *    The 2nd child will be the first child of the original
	 *    <functionalOp type="AGE AT"...../>.
	 * @param me
	 */
	
	private void prepForAGE_AT(MeasureExport me) {
		XmlProcessor xmlProcessor = me.getSimpleXMLProcessor();
		logger.info("Prepping for HQMF Clause generation for AGE AT functionalOps.");
		
		try {
			
			//find <qdm> for Birthdate QDM element in elementLookUp
			String xPathForBirthdate = "/measure/elementLookUp/qdm[@name='Birthdate'][@datatype='Patient Characteristic Birthdate']";
			Node birthDateQDM = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xPathForBirthdate);
			if(birthDateQDM == null){
				logger.info("**********   Could not find QDM for Birthdate. No changes done for AGE AT. ***************");
				return;
			}
			
			String xPathForAGE_AT = "/measure/subTreeLookUp//functionalOp[@type='AGE AT']";
			Node ageAtFuncNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xPathForAGE_AT);
			
			logger.info(".......found AGE AT functionalOps");
			
			while(ageAtFuncNode != null){
				logger.info("Changing "+ageAtFuncNode.toString() + " to relational SBS node.");
				Node cloneAgeAtRelNode = ageAtFuncNode.cloneNode(true);
				
				//hold on to the first child of Age At
				Node firstChild = cloneAgeAtRelNode.getFirstChild();
				//Node firstChild = ageAtFuncNode.getFirstChild();
				NamedNodeMap attribMap = ageAtFuncNode.getAttributes();
				Element newRelationalOp = xmlProcessor.getOriginalDoc().createElement("relationalOp");
				
				for(int j=0;j<attribMap.getLength();j++){
					Node attrib = attribMap.item(j);
					newRelationalOp.setAttribute(attrib.getNodeName(), attrib.getNodeValue());
				}
				
				//set the type attribute to SBS
				newRelationalOp.getAttributes().getNamedItem("type").setNodeValue("SBS");
				
				//create a new <elementRef for birthDateQDM
				Element birthDateElementRef = xmlProcessor.getOriginalDoc().createElement("elementRef");
				birthDateElementRef.setAttribute("id", birthDateQDM.getAttributes().getNamedItem("uuid").getNodeValue());
				birthDateElementRef.setAttribute("type", "qdm");
				birthDateElementRef.setAttribute("displayName", "Birthdate : Patient Characteristic Birthdate");
				
				newRelationalOp.appendChild(birthDateElementRef);
				newRelationalOp.appendChild(firstChild);
				
				Node parentNode = ageAtFuncNode.getParentNode();
				parentNode.insertBefore(newRelationalOp, ageAtFuncNode);
				parentNode.removeChild(ageAtFuncNode);
				logger.info("Change done.");
				
				ageAtFuncNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xPathForAGE_AT);
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will be called by prepHQMF(MeasureExport me) method to prep
	 * functionalOp for SATISFIES ALL/SATISFIES ANY functions.
	 * This methiod will do the following,
	 * 
	 * This method will look for all functionalOp's with names,
	 * SATISFIES ALL/SATISFIES ANY renames the <functionalOp> tag with
	 * <setOp> tag.
	 * 
	 * @param me
	 */
	private void prepForSatisfiesAll_Any(MeasureExport me) {
		XmlProcessor xmlProcessor = me.getSimpleXMLProcessor();
		logger.info("Prepping for HQMF Clause generation for Satisfies All/Satisfies Any functionalOps.");
		String xPathForSatisfiesAllAny = "/measure/subTreeLookUp//functionalOp[@type='SATISFIES ALL' or @type='SATISFIES ANY']";
		try {
			Node satisfiesFuncNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xPathForSatisfiesAllAny);
			logger.info(".......found Satisfies All/Satisfies Any functionalOps");
			while(satisfiesFuncNode != null){
				logger.info("Changing functionaOp "+satisfiesFuncNode.getAttributes().getNamedItem("displayName").getNodeValue() + " to relationalOp node.");
				
				NamedNodeMap attribMap = satisfiesFuncNode.getAttributes();
				Element newSetOp = xmlProcessor.getOriginalDoc().createElement("setOp");
				
				for(int j=0;j<attribMap.getLength();j++){
					Node attrib = attribMap.item(j);
					newSetOp.setAttribute(attrib.getNodeName(), attrib.getNodeValue());
				}
				
				NodeList childNodeList = satisfiesFuncNode.getChildNodes();
				/**
				 * Ignore the first element of SATISFIES ALL/ANY.
				 * i.e. Start counter at 1 isntead of 0.
				 */
				for(int j=1;j<childNodeList.getLength();j++){
					Node childNode = childNodeList.item(j).cloneNode(true);
					newSetOp.appendChild(childNode);
				}
				
				Node parentNode = satisfiesFuncNode.getParentNode();
				parentNode.insertBefore(newSetOp, satisfiesFuncNode);
				parentNode.removeChild(satisfiesFuncNode);
				logger.info("Change done.");
				satisfiesFuncNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xPathForSatisfiesAllAny);
			}
		}catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the unit string.
	 *
	 * @param unitString the unit string
	 * @return the unit string
	 */
	protected String getUnitString(String unitString){
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
			returnString = "{H.B}/min";
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
			returnString = "[CFU]/mL";
		}else if(unitString.equals("per mm3")){
			returnString = "/mm3";
		}else if(unitString.equals("copies/mL")){
			returnString = "[copies]/mL";
		}
		
		return returnString;
	}
}
