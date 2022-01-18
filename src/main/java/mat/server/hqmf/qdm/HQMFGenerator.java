package mat.server.hqmf.qdm;

import mat.model.clause.MeasureExport;
import mat.server.hqmf.Generator;
import mat.server.humanreadable.qdm.HQMFHumanReadableGenerator;
import org.slf4j.LoggerFactory;
import mat.server.util.XmlProcessor;
import org.slf4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated this class is deprecated since it is an old version of QDM. It should not be modified. 
 */
public class HQMFGenerator implements Generator {
	
	private final Logger logger = LoggerFactory.getLogger(HQMFDataCriteriaGenerator.class);

	
	/**
	 * Generate hqm for measure.
	 *
	 * @param me the me
	 * @return the string
	 */
	@Override
	public String generate(MeasureExport me){
		String hqmfXML = "";
		try{
			String eMeasureDetailsXML = new HQMFMeasureDetailsGenerator().generate(me);
			// Inline comments are added after the end of last componentOf tag. This is removed in this method
			eMeasureDetailsXML = replaceInlineCommentFromEnd(eMeasureDetailsXML);
			hqmfXML += eMeasureDetailsXML;
			
			String dataCriteriaXML = new HQMFDataCriteriaGenerator().generate(me);
			hqmfXML= appendToHQMF(dataCriteriaXML, hqmfXML);
			
			XmlProcessor hqmfProcessor = new XmlProcessor(hqmfXML);
			me.setHQMFXmlProcessor(hqmfProcessor);
			
			generateNarrative(me);
			hqmfXML = finalCleanUp(me);			
		} catch(Exception e){
			logger.error("Unable to generate human readable. Exception Stack Strace is as followed : ");
			e.printStackTrace();
		}
		return hqmfXML;
	}
	
	/**
	 *  Inline comments are added after the end of last componentOf tag. This is removed in this method
	 * @param eMeasureDetailsXML - String eMeasureDetailsXML.
	 * @return  String eMeasureDetailsXML.
	 */
	private String replaceInlineCommentFromEnd(String eMeasureDetailsXML) {
		int indexOfComponentOf = eMeasureDetailsXML.lastIndexOf("</componentOf>");
		eMeasureDetailsXML = eMeasureDetailsXML.substring(0, indexOfComponentOf);
		eMeasureDetailsXML = eMeasureDetailsXML.concat("</componentOf></QualityMeasureDocument>");
		return eMeasureDetailsXML;
	}
	
	/**
	 * Generate narrative.
	 *
	 * @param me the me
	 */
	private void generateNarrative(MeasureExport me) {
		String humanReadableHTML = HQMFHumanReadableGenerator.generateHTMLForMeasure(me.getMeasure().getId(), me.getSimpleXML());
		humanReadableHTML = tidyfyHTML(humanReadableHTML);
		humanReadableHTML = humanReadableHTML.substring(humanReadableHTML.indexOf(" <body>"),humanReadableHTML.indexOf("</body>")+"</body>".length());
		
		XmlProcessor humanReadableProcessor = new XmlProcessor(humanReadableHTML);
		
		try{
			Node mainNode = humanReadableProcessor.getOriginalDoc().getFirstChild();
			XmlProcessor.clean(mainNode);
			generateDataCritNarrative(me, humanReadableProcessor);
			generatePopulationCritNarrative(me, humanReadableProcessor);
			
		}  catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	private String tidyfyHTML(String humanReadableHTML) {
		
		Tidy tidy = new Tidy();
		tidy.setInputEncoding("UTF-8");		 
		tidy.setOutputEncoding("UTF-8");		 
		tidy.setWraplen(Integer.MAX_VALUE);		 
		tidy.setPrintBodyOnly(false);		 
		tidy.setXmlOut(true);		
		 
		ByteArrayInputStream inputStream;
		try {
			inputStream = new ByteArrayInputStream(humanReadableHTML.getBytes("UTF-8"));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			 
			tidy.parseDOM(inputStream, outputStream);
			 
			humanReadableHTML = outputStream.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return humanReadableHTML;	
		
	}

	/**
	 * Generate data crit narrative.
	 *
	 * @param me the me
	 * @param humanReadableProcessor the human readable processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateDataCritNarrative(MeasureExport me,
			XmlProcessor humanReadableProcessor)
					throws XPathExpressionException {
		
		//Get narrative for Data Criteria (QDM Variable) section
		Node dataCritQDMVarNode = generateNarrativeItem(me, humanReadableProcessor, "Data Criteria (QDM Variables)");
		
		//Get narrative for Data Criteria (QDM Data Elements) section.
		Node dataCritQDMNode = generateNarrativeItem(me, humanReadableProcessor, "Data Criteria (" + (me.isFhir() ? "FHIR Data Requirements" : "QDM Data Elements") + ")");
		
		//Get narrative for Supplemental Data Elements section
		Node dataCritSuppNode = generateNarrativeItem(me, humanReadableProcessor, "Supplemental Data Elements");
		
		//Get narrative for Risk Adjustment Data Elements section
		Node riskAdjustmentSuppNode = generateNarrativeItem(me, humanReadableProcessor, "Risk Adjustment Variables");
		
		XmlProcessor hqmfProcessor = me.getHQMFXmlProcessor();
		String xPathForDataCriteriaSection = "//dataCriteriaSection/text";
		Node dataCritTextNode = hqmfProcessor.findNode(hqmfProcessor.getOriginalDoc(), xPathForDataCriteriaSection);
		
		Element xmlNode = hqmfProcessor.getOriginalDoc().createElement("xml");
		Element itemNode = hqmfProcessor.getOriginalDoc().createElement("item");
		Element listNode = hqmfProcessor.getOriginalDoc().createElement("list");
		
		listNode.appendChild(dataCritQDMVarNode);
		listNode.appendChild(dataCritQDMNode);
		listNode.appendChild(dataCritSuppNode);
		listNode.appendChild(riskAdjustmentSuppNode);
		
		itemNode.appendChild(listNode);
		xmlNode.appendChild(itemNode);
		dataCritTextNode.appendChild(xmlNode);
	}
	
	/**
	 * Generate population crit narrative.
	 *
	 * @param me the me
	 * @param humanReadableProcessor the human readable processor
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generatePopulationCritNarrative(MeasureExport me,
			XmlProcessor humanReadableProcessor) throws XPathExpressionException {
		
		//Get narrative for Population criteria section
		//Node popCritNode = generateNarrativeItem(me, humanReadableProcessor, "Population criteria");
		
		XmlProcessor hqmfProcessor = me.getHQMFXmlProcessor();
		String xPathForPopCriteriaSection = "//populationCriteriaSection/text";
		NodeList popCritTextNodeList = hqmfProcessor.findNodeList(hqmfProcessor.getOriginalDoc(), xPathForPopCriteriaSection);
		Node msrObsXMLNode = hqmfProcessor.getOriginalDoc().createElement("xml");
//		for (int i=popCritTextNodeList.getLength()-1; i>=0; i--) {
		for(int i = 0; i<popCritTextNodeList.getLength(); i++){
			Element xmlNode = hqmfProcessor.getOriginalDoc().createElement("xml");
			Element itemNode = hqmfProcessor.getOriginalDoc().createElement("item");
			Element listNode = hqmfProcessor.getOriginalDoc().createElement("list");
			Element parentNodeElem = (Element)popCritTextNodeList.item(i).getParentNode(); 
			Node idNode = parentNodeElem.getElementsByTagName("id").item(0);
			String extension = idNode.getAttributes().getNamedItem("extension").getNodeValue();
			if(popCritTextNodeList.getLength()>1){
				listNode.appendChild(generatePopulationCriteriaNarrativeItem(me, humanReadableProcessor, "Population Criteria", i+1));
			} else {
				listNode.appendChild(generateNarrativeItem(me, humanReadableProcessor, "Population Criteria"));
			}
			itemNode.appendChild(listNode);
			xmlNode.appendChild(itemNode);
			popCritTextNodeList.item(i).appendChild(xmlNode);
			generateMsrObsNarrativeItem(extension, hqmfProcessor, msrObsXMLNode);
			}
		}
	
	
	/**
	 * Generate msr obs narrative item.
	 *
	 * @param sequence the sequence
	 * @param hqmfProcessor the hqmf processor
	 * @param msrObsXMLNode the msr obs xml node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void generateMsrObsNarrativeItem(String sequence, XmlProcessor hqmfProcessor, Node msrObsXMLNode) throws XPathExpressionException{
		Node msrObsItemNode = hqmfProcessor.getOriginalDoc().createElement("item");
		Node msrObsSectionContentNode = hqmfProcessor.getOriginalDoc().createElement("content"); 
		
		String xPathForMeasureObservation = "//measureObservationSection/text";
		Node msrObsTextNode = hqmfProcessor.findNode(hqmfProcessor.getOriginalDoc(), xPathForMeasureObservation);
		if (msrObsTextNode != null) {
			String xPathForMsrObsNarrative = "//populationCriteriaSection/id[@extension='"+sequence
					+"']/following-sibling::text//item[starts-with(content/text(), 'Measure Observation')]";
			Node msrObsNarrativeNode = hqmfProcessor.findNode(hqmfProcessor.getOriginalDoc(), xPathForMsrObsNarrative);
			Node msrObsNarrativeParentNode = msrObsNarrativeNode.getParentNode();
			msrObsNarrativeParentNode.removeChild(msrObsNarrativeNode);
			sequence = sequence.replaceAll("Population", "Population ").replaceAll("Criteria", "Criteria ");
			msrObsSectionContentNode.setTextContent(sequence);
			msrObsItemNode.appendChild(msrObsSectionContentNode);
			msrObsItemNode.appendChild(msrObsNarrativeNode);
			msrObsXMLNode.appendChild(msrObsItemNode);
			msrObsTextNode.appendChild(msrObsXMLNode);
			}	
	}
	
	/**
	 * Generate narrative item.
	 *
	 * @param me the me
	 * @param humanReadableProcessor the human readable processor
	 * @param searchText the search text
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node generateNarrativeItem(MeasureExport me,
			XmlProcessor humanReadableProcessor, String searchText)
					throws XPathExpressionException {
		Node dataCritItemNode = me.getHQMFXmlProcessor().getOriginalDoc().createElement("item");
		Node dataCritContentNode = me.getHQMFXmlProcessor().getOriginalDoc().createElement("content");
		((Element)dataCritContentNode).setAttribute("styleCode", "Bold");
		dataCritContentNode.setTextContent(searchText);
		dataCritItemNode.appendChild(dataCritContentNode);
		
		Node elementsNode = humanReadableProcessor.findNode(humanReadableProcessor.getOriginalDoc(), "/body/h3[a[text()='"+searchText+"']]");
		Node divNode = elementsNode.getNextSibling();
		if((divNode != null) && "div".equals(divNode.getNodeName())){
			if(divNode.hasChildNodes()){
				Node ulNode = divNode.getFirstChild();
				Node narrativeListNode = getNarrativeListNode(ulNode, me.getHQMFXmlProcessor());
				if(narrativeListNode != null){
					dataCritItemNode.appendChild(narrativeListNode);
				}
			}
		}
		return dataCritItemNode;
	}
	
	
	/**
	 * Generate population criteria narrative item.
	 *
	 * @param me the me
	 * @param humanReadableProcessor the human readable processor
	 * @param searchText the search text
	 * @param sequence the sequence
	 * @return the node
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Node generatePopulationCriteriaNarrativeItem(MeasureExport me,
			XmlProcessor humanReadableProcessor, String searchText, int sequence)
					throws XPathExpressionException {
		Node dataCritItemNode = me.getHQMFXmlProcessor().getOriginalDoc().createElement("item");
		Node dataCritContentNode = me.getHQMFXmlProcessor().getOriginalDoc().createElement("content");
		((Element)dataCritContentNode).setAttribute("styleCode", "Bold");
		dataCritContentNode.setTextContent(searchText);
		dataCritItemNode.appendChild(dataCritContentNode);
		String xpathForPOPNarrative = "/body/div/ul/li[contains(b/text(),'Population Criteria "+sequence+"')]";
		Node elementsNode = humanReadableProcessor.findNode(humanReadableProcessor.getOriginalDoc(), xpathForPOPNarrative);
		Node divNode = humanReadableProcessor.getOriginalDoc().createElement("ul");
		List<Node> popNodeList = new ArrayList<Node>(); 
		popNodeList = getPopulationNarrative(popNodeList, elementsNode);
		for(Node popNode : popNodeList ){
			divNode.appendChild(popNode);
		}
		Node narrativeListNode = getNarrativeListNode(divNode, me.getHQMFXmlProcessor());
		if(narrativeListNode != null){
			dataCritItemNode.appendChild(narrativeListNode);
		}		
		return dataCritItemNode;
	}
	
	/**
	 * Gets the population narrative.
	 *
	 * @param elementsNodeList the elements node list
	 * @param elementsNode the elements node
	 * @return the population narrative
	 */
	private List<Node> getPopulationNarrative(List<Node> elementsNodeList, Node elementsNode){
		Node nextSibling = elementsNode.getNextSibling();
		if(nextSibling!=null && !nextSibling.hasAttributes()){
		    elementsNodeList.add(nextSibling);
			elementsNodeList = getPopulationNarrative(elementsNodeList, nextSibling);
		} else {
			return elementsNodeList;
		}
		return elementsNodeList;
	}
	
	
	/**
	 * Gets the narrative list node.
	 *
	 * @param humanReadableNode the human readable node
	 * @param xmlProcessor the xml processor
	 * @return the narrative list node
	 */
	private Node getNarrativeListNode(Node humanReadableNode, XmlProcessor xmlProcessor) {		
		Node narrativeListNode = null;
		String nodeName = humanReadableNode.getNodeName();
		if(humanReadableNode.getNodeType() == Node.TEXT_NODE){
			narrativeListNode = xmlProcessor.getOriginalDoc().createTextNode(humanReadableNode.getNodeValue());
		}
		else if("ul".equals(nodeName)){
			narrativeListNode = xmlProcessor.getOriginalDoc().createElement("list");
			NodeList childNodeList = humanReadableNode.getChildNodes();
			
			if(childNodeList != null){
				for(int i=0;i<childNodeList.getLength();i++){
					Node child = getNarrativeListNode(childNodeList.item(i), xmlProcessor);
					if(child != null){
						narrativeListNode.appendChild(child);
					}
				}
			}
		}else if("li".equals(nodeName)){
			narrativeListNode = xmlProcessor.getOriginalDoc().createElement("item");
			
			if(humanReadableNode.hasChildNodes()){
				NodeList childNodeList = humanReadableNode.getChildNodes();
				for(int i=0;i<childNodeList.getLength();i++){
					Node child = getNarrativeListNode(childNodeList.item(i), xmlProcessor);
					if(child != null){
						narrativeListNode.appendChild(child);
					}
				}
			}else{
				narrativeListNode.setTextContent(humanReadableNode.getTextContent());
			}
		}else if("b".equals(nodeName)){
			narrativeListNode = xmlProcessor.getOriginalDoc().createElement("content");
			((Element)narrativeListNode).setAttribute("styleCode", "Bold");
			narrativeListNode.setTextContent(humanReadableNode.getTextContent());
		}else if("i".equals(nodeName)){
			narrativeListNode = xmlProcessor.getOriginalDoc().createElement("caption");
			narrativeListNode.setTextContent(humanReadableNode.getTextContent());
		}
		return narrativeListNode;
	}
	
	/**
	 * Final clean up.
	 *
	 * @param me the me
	 * @return the string
	 */
	private String finalCleanUp(MeasureExport me) {
		HQMFFinalCleanUp.cleanAndDeleteUnused(me);		
		return removeXmlTagNamespace(me.getHQMFXmlProcessor().transform(me.getHQMFXmlProcessor().getOriginalDoc(), true));
	}
	
	/**
	 * Removes the xml tag namespace.
	 *
	 * @param xmlString the xml string
	 * @return the string
	 */
	private String removeXmlTagNamespace(String xmlString) {
		xmlString = xmlString.replaceAll(" xmlns=\"\"", "");
		return xmlString;
	}
	
	/**
	 * Append to hqmf.
	 *
	 * @param dataCriteriaXML the data criteria xml
	 * @param hqmfXML the hqmf xml
	 * @return the string
	 */
	private String appendToHQMF(String dataCriteriaXML, String hqmfXML) {
		int indexOfEnd = hqmfXML.indexOf("</QualityMeasureDocument>");
		if(indexOfEnd > -1){
			hqmfXML = hqmfXML.substring(0, indexOfEnd) + dataCriteriaXML + hqmfXML.substring(indexOfEnd);
		}
		return hqmfXML;
	}
}
