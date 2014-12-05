package mat.server.simplexml.hqmf;

import javax.xml.xpath.XPathExpressionException;

import mat.model.clause.MeasureExport;
import mat.server.simplexml.HumanReadableGenerator;
import mat.server.util.XmlProcessor;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// TODO: Auto-generated Javadoc
/**
 * The Class HQMFGenerator.
 */
public class HQMFGenerator implements Generator {
	
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
			hqmfXML += eMeasureDetailsXML;
			
			String dataCriteriaXML = new HQMFDataCriteriaGenerator().generate(me);
			hqmfXML= appendToHQMF(dataCriteriaXML, hqmfXML);
			
			XmlProcessor hqmfProcessor = new XmlProcessor(hqmfXML);
			me.setHQMFXmlProcessor(hqmfProcessor);
			
			generateNarrative(me);
			hqmfXML = me.getHQMFXmlProcessor().transform(me.getHQMFXmlProcessor().getOriginalDoc(), true);
			
		} catch(Exception e){
			LOG.error("Unable to generate human readable. Exception Stack Strace is as followed : ");
			e.printStackTrace();
		}
		return hqmfXML;
	}

	private void generateNarrative(MeasureExport me) {
		String humanReadableHTML = HumanReadableGenerator.generateHTMLForMeasure(me.getMeasure().getId(), me.getSimpleXML());
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

	private void generateDataCritNarrative(MeasureExport me,
			XmlProcessor humanReadableProcessor)
			throws XPathExpressionException {
		
		//Get narrative for Data Criteria (QDM Variable) section
		Node dataCritQDMVarNode = generateNarrativeItem(me, humanReadableProcessor, "Data Criteria (QDM Variables)");
		
		//Get narrative for Data Criteria (QDM Data Elements) section.
		Node dataCritQDMNode = generateNarrativeItem(me, humanReadableProcessor, "Data criteria (QDM Data Elements)");
		
		//Get narrative for Supplemental Data Elements section
		Node dataCritSuppNode = generateNarrativeItem(me, humanReadableProcessor, "Supplemental Data Elements");
		
		XmlProcessor hqmfProcessor = me.getHQMFXmlProcessor();
		String xPathForDataCriteriaSection = "//dataCriteriaSection/text";
		Node dataCritTextNode = hqmfProcessor.findNode(hqmfProcessor.getOriginalDoc(), xPathForDataCriteriaSection);
		
		Element xmlNode = hqmfProcessor.getOriginalDoc().createElement("xml");
		Element itemNode = hqmfProcessor.getOriginalDoc().createElement("item");
		Element listNode = hqmfProcessor.getOriginalDoc().createElement("list");
		
		listNode.appendChild(dataCritQDMVarNode);
		listNode.appendChild(dataCritQDMNode);
		listNode.appendChild(dataCritSuppNode);
		
		itemNode.appendChild(listNode);
		xmlNode.appendChild(itemNode);
		dataCritTextNode.appendChild(xmlNode);
	}
	
	private void generatePopulationCritNarrative(MeasureExport me,
			XmlProcessor humanReadableProcessor) throws XPathExpressionException {
		
		//Get narrative for Population criteria section
		Node popCritNode = generateNarrativeItem(me, humanReadableProcessor, "Population criteria");
		
		XmlProcessor hqmfProcessor = me.getHQMFXmlProcessor();
		String xPathForPopCriteriaSection = "//populationCriteriaSection/text";
		Node popCritTextNode = hqmfProcessor.findNode(hqmfProcessor.getOriginalDoc(), xPathForPopCriteriaSection);
		
		Element xmlNode = hqmfProcessor.getOriginalDoc().createElement("xml");
		Element itemNode = hqmfProcessor.getOriginalDoc().createElement("item");
		Element listNode = hqmfProcessor.getOriginalDoc().createElement("list");
		
		listNode.appendChild(popCritNode);
		itemNode.appendChild(listNode);
		xmlNode.appendChild(itemNode);
		popCritTextNode.appendChild(xmlNode);
	}

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
		
		if(divNode != null && "div".equals(divNode.getNodeName())){
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
	
	private Node getNarrativeListNode(Node humanReadableNode, XmlProcessor xmlProcessor) {
		
		Node narrativeListNode = null;
		String nodeName = humanReadableNode.getNodeName();
		if(humanReadableNode.getNodeType() == humanReadableNode.TEXT_NODE){
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
		}
		
		return narrativeListNode;
	}

	private String appendToHQMF(String dataCriteriaXML, String hqmfXML) {
		int indexOfEnd = hqmfXML.indexOf("</QualityMeasureDocument>");
		if(indexOfEnd > -1){
			hqmfXML = hqmfXML.substring(0, indexOfEnd) + dataCriteriaXML + hqmfXML.substring(indexOfEnd);
		}
		return hqmfXML;
	}
	
}
