package mat.server.simplexml;

import javax.xml.xpath.XPathExpressionException;

import mat.server.util.XmlProcessor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HeaderHumanReadableGenerator {
	
	private static final String DETAILS_PATH = "/measure/measureDetails/";
	private static final String HTML_TR = "tr";
	private static final String HTML_TD = "td";
	private static Element row;
	private static Element column;
	
	public static String generateHeaderHTMLForMeasure(String measureId, String subXML,
			String measureXML) throws XPathExpressionException {
		org.jsoup.nodes.Document htmlDocument = null;
		System.out.println(measureXML);
		XmlProcessor measureXMLProcessor = new XmlProcessor(measureXML);
		
		htmlDocument = createBaseHTMLDocument(getInfo(measureXMLProcessor,"title"));
		Element bodyElement = htmlDocument.body();
		Element table = bodyElement.appendElement("table");
		table.attr("class", "header_table");
		Element tBody = table.appendElement("tbody");
		createDocumentGeneral(measureXMLProcessor, tBody);
		createRelatedDocument(measureXMLProcessor, tBody);
		createEmeasureBrief(measureXMLProcessor, tBody);
		createSubjectOf(measureXMLProcessor, tBody);
		
		System.out.println("HTML: " + htmlDocument.toString());
		return htmlDocument.toString();
	}
	private static void createDocumentGeneral(XmlProcessor processor,Element table) throws DOMException, XPathExpressionException{
		createRowAndColumns(table,"eMeasure Title");
		column.appendText(getShortTitle(processor));
		
		row = table.appendElement(HTML_TR);
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column,"20%");
		createSpan("eMeasure Identifier\n"+ "(Measure Authoring Tool)",column);
		column = row.appendElement(HTML_TD);
		setTDInfoAttributes(column,"30%", "");
		column.appendText(getInfo(processor, "emeasureid"));
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column,"20%");
		createSpan("eMeasure Version number",column);
		column = row.appendElement(HTML_TD);
		setTDInfoAttributes(column,"30%", "");
		column.appendText(getInfo(processor, "version"));
		
		row = table.appendElement(HTML_TR);
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column,"20%");
		createSpan("NQF Number",column);
		column = row.appendElement(HTML_TD);
		setTDInfoAttributes(column,"30%", "");
		column.appendText(getInfo(processor, "nqfid/@extension"));
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column,"20%");
		createSpan("GUID",column);
		column = row.appendElement(HTML_TD);
		column.appendText(getInfo(processor,"guid"));
		
		createRowAndColumns(table,"Measurement Period");
		column.appendText(getMeasurePeriod(processor));
		
		if(processor.findNode(processor.getOriginalDoc(), DETAILS_PATH + "cmsid") != null){
			createRowAndColumns(table,"CMS ID Number");
			column.appendText(getInfo(processor,"cmsid"));
		}
		
		createRowAndColumns(table,"Measure Steward");
		column.appendText(getInfo(processor,"steward"));
		
		getInfoNodes(table,processor,"developers/developer","Measure Developer",false);
		
		createRowAndColumns(table,"Endorsed By");
		column.appendText(getInfo(processor,"endorsement"));
	}
	
	private static void createRelatedDocument(XmlProcessor processor,Element table){
		//TODO Is this a thing in the MAT still? see 327 on eMeasure.xsl
	}
	private static void createEmeasureBrief(XmlProcessor processor,Element table) throws XPathExpressionException{
		createRowAndColumns(table,"Description");
		createDiv(getInfo(processor,"description"), column);
	}
	
	private static void createSubjectOf(XmlProcessor processor,Element table) throws XPathExpressionException{
		createRowAndColumns(table, "Copyright");
		createDiv(getInfo(processor,"copyright"), column);
		
		createRowAndColumns(table,"Disclaimer");
		createDiv(getInfo(processor,"disclaimer"), column);
		
		createRowAndColumns(table,"Measure Scoring");
		column.appendText(getInfo(processor,"scoring"));
		
		getInfoNodes(table,processor,"types/type","Measure Type",false);
		
		createRowAndColumns(table, "Stratification");
		createDiv(getInfo(processor,"stratification"), column);
		
		createRowAndColumns(table, "Risk Adjustment");
		createDiv(getInfo(processor,"riskAdjustment"), column);
		
		createRowAndColumns(table, "Rate Aggregation");
		createDiv(getInfo(processor,"aggregation"), column);
		
		createRowAndColumns(table, "Rationale");
		createDiv(getInfo(processor,"rationale"), column);
		
		createRowAndColumns(table, "Clinical Recommendation Statement");
		createDiv(getInfo(processor,"recommendations"), column);
		
		createRowAndColumns(table, "Improvement Notation");
		createDiv(getInfo(processor,"improvementNotations"), column);
		
		getInfoNodes(table,processor,"references/reference","Reference",true);
		
		getInfoNodes(table,processor,"definitions","Definition",true);
		
		createRowAndColumns(table, "Guidance");
		createDiv(getInfo(processor,"guidance"), column);
		
		createRowAndColumns(table, "Transmission Format");
		createDiv(getInfo(processor,"transmissionFormat"), column);
		
		createRowAndColumns(table, "Initial Patient Population");
		createDiv(getInfo(processor,"initialPopDescription"), column);
		
		createRowAndColumns(table, "Denominator");
		createDiv(getInfo(processor,"denominatorDescription"), column);
		
		createRowAndColumns(table, "Denominator Exclusions");
		createDiv(getInfo(processor,"denominatorExclusionsDescription"), column);
		
		createRowAndColumns(table, "Numerator");
		createDiv(getInfo(processor,"numeratorDescription"), column);
		
		createRowAndColumns(table, "Numerator Exclusions");
		createDiv(getInfo(processor,"numeratorExclusionsDescription"), column);
		
		createRowAndColumns(table, "Denominator Exceptions");
		createDiv(getInfo(processor,"denominatorExceptionsDescription"), column);
		
		createRowAndColumns(table, "Measure Population");
		createDiv(getInfo(processor,"measurePopulationDescription"), column);
		
		createRowAndColumns(table, "Measure Observations");
		createDiv(getInfo(processor,"measureObservationsDescription"), column);
		
		createRowAndColumns(table, "Supplemental Data Elements");
		createDiv(getInfo(processor,"supplementalData"), column);
	}

	private static String getShortTitle(XmlProcessor processor) throws DOMException, XPathExpressionException{
		String title = processor.findNode(processor.getOriginalDoc(), DETAILS_PATH + "title").getTextContent();
		if(title == null){
			title = "";
		}
		else{
			if(title.contains("(###)")){
				title = title.substring(0, title.indexOf("(###)"));
			}
		}
		return title;
	}
	
	private static String getMeasurePeriod(XmlProcessor processor){
		// TODO figure out how the time documentation works
		return "";
	}
	
	private static void getInfoNodes(Element table, XmlProcessor processor, String lookUp, String display,boolean addDiv) throws XPathExpressionException {
		NodeList developers = processor.findNodeList(processor.getOriginalDoc(), DETAILS_PATH + lookUp);
		
		if(developers.getLength() > 0){
			Node person;
			for(int i = 0; i < developers.getLength(); i++){
				person = developers.item(i);
				if(!addDiv){
					createRowAndColumns(table, display);
					if(person != null){
						column.appendText(person.getTextContent());
					}
				}
				else{
					if(person != null){
						createRowAndColumns(table, display);
						createDiv(person.getTextContent(), column);
					}
				}
			}
		}
		else{
			createRowAndColumns(table, display);
		}
	}
	
	private static String getInfo(XmlProcessor processor, String lookUp) throws XPathExpressionException {
		String returnVar = " ";
		Node node = processor.findNode(processor.getOriginalDoc(), DETAILS_PATH + lookUp);
		if(node != null){
			returnVar = node.getTextContent();
		}
		else if(lookUp.equalsIgnoreCase("endorsement") || lookUp.equalsIgnoreCase("nqfid/@extension")){
			returnVar = "None";
		}
		else if(lookUp.equalsIgnoreCase("guid")){
			returnVar = "Pending";
		}
		else if(lookUp.equalsIgnoreCase("title")){
			returnVar = "Health Quality Measure Document";
		}
		return returnVar;
	}
	
	private static void setTDHeaderAttributes(Element td, String width){
		td.attr("width", width);
		td.attr("bgcolor", "#656565");
		td.attr("style", "background-color:#656565");
	}
	private static void setTDInfoAttributes(Element td, String width, String span){
		td.attr("width", width);
		if(span.length()>0){
			td.attr("colspan", span);
		}
	}
	
	private static String nullValue(String value){
		String returnVar = "";
		if(value.equalsIgnoreCase("NI")){
			returnVar = "No Information";
		}
		else if(value.equalsIgnoreCase("INV")){
			returnVar = "Invalid";
		}
		else if(value.equalsIgnoreCase("MSK")){
			returnVar = "Masked";
		}
		else if(value.equalsIgnoreCase("NA")){
			returnVar = "Not applicable";
		}
		else if(value.equalsIgnoreCase("UNK")){
			returnVar = "Unknown";
		}
		else if(value.equalsIgnoreCase("OTH")){
			returnVar = "Other";
		}
		return returnVar;
	}
	
	private static void createRowAndColumns(Element table, String title){
		row = table.appendElement(HTML_TR);
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column,"20%");
		createSpan(title,column);
		column = row.appendElement(HTML_TD);
		setTDInfoAttributes(column,"80%", "3");
	}
	
	private static void createSpan(String message, Element column){
		Element span = column.appendElement("span");
		span.attr("class", "td_label");
		span.appendText(message);
	}
	private static void createDiv(String message, Element column){
		Element div = column.appendElement("div");
		div.attr("style", "width:660px;overflow-x:hidden;overflow-y:auto");
		Element pre = div.appendElement("pre");
		pre.appendText(message);
	}
	private static org.jsoup.nodes.Document createBaseHTMLDocument(String title) {
		org.jsoup.nodes.Document htmlDocument = org.jsoup.nodes.Document.createShell("");
		
		Element head = htmlDocument.head();
		Element meta = head.appendElement("meta");
		meta.attr("http-equiv", "Content-Type");
		meta.attr("content", "text/html; charset=ISO-8859-1");
		htmlDocument.title(title);
		appendStyleNode(head);
		return htmlDocument;
	}

	private static void appendStyleNode(Element head) {
		String styleTagString = MATCssUtil.getCSS();
		head.append(styleTagString);
	}
}
