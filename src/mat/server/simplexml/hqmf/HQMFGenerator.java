package mat.server.simplexml.hqmf;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;


import mat.model.clause.MeasureExport;
import mat.server.simplexml.HumanReadableGenerator;
import mat.server.util.XmlProcessor;

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
			getHumanReadable(me);
			
		} catch(Exception e){
			LOG.error("Unable to generate human readable. Exception Stack Strace is as followed : ");
			e.printStackTrace();
		}
		return hqmfXML;
	}

	private void getHumanReadable(MeasureExport me) {
		HQMFDataCriteriaElementGenerator hQMFDataElementGenerator= new HQMFDataCriteriaElementGenerator();
		String humanReadableHTML = HumanReadableGenerator.generateHTMLForMeasure(me.getMeasure().getId(), me.getSimpleXML());
		//humanReadableHTML = humanReadableHTML.substring(humanReadableHTML.indexOf("<style type="),humanReadableHTML.indexOf("</style>")+"</style>".length());
		humanReadableHTML = humanReadableHTML.substring(humanReadableHTML.indexOf(" <body>"),humanReadableHTML.indexOf("</body>")+"</body>".length());		
		XmlProcessor humanReadableProcessor = new XmlProcessor(humanReadableHTML);
		me.setHumanReadableProcessor(humanReadableProcessor);	
		try{
			XmlProcessor processor = me.getHumanReadableProcessor();
			
			Node mainNode = me.getHumanReadableProcessor().findNode(
					processor.getOriginalDoc(),
					"/body");
			hQMFDataElementGenerator.clean(mainNode);
		Node qDMDataElementsNode = me.getHumanReadableProcessor().findNode(
				processor.getOriginalDoc(),
				"/body/h3[a[text()='Data criteria (QDM Data Elements)']]");
		
		Node childNode = qDMDataElementsNode.getNextSibling();
		Node duplicateNode = childNode.cloneNode(true);
		childNode.getParentNode().appendChild(duplicateNode);
		me.setHumanReadableProcessor(processor);		
		
		}  catch (XPathExpressionException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	private String appendToHQMF(String dataCriteriaXML, String hqmfXML) {
		int indexOfEnd = hqmfXML.indexOf("</QualityMeasureDocument>");
		if(indexOfEnd > -1){
			hqmfXML = hqmfXML.substring(0, indexOfEnd) + dataCriteriaXML + hqmfXML.substring(indexOfEnd);
		}
		return hqmfXML;
	}
	
	
}
