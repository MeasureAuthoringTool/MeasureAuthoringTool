package mat.server.simplexml;

import javax.xml.xpath.XPathExpressionException;

import mat.server.util.XmlProcessor;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HeaderHumanReadableGenerator {
	
	private static final String DETAILS_PATH = "/measure/measureDetails/";
	private static final String HTML_TR = "tr";
	private static final String HTML_TD = "td";
	private static final String TWENTY_PERCENT = "20%";
	private static Element row;
	private static Element column;
	

	public static org.jsoup.nodes.Document generateHeaderHTMLForMeasure(String measureXML) throws XPathExpressionException {
		org.jsoup.nodes.Document htmlDocument = null;
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
		
		return htmlDocument;
	}
	private static void createDocumentGeneral(XmlProcessor processor,Element table) throws DOMException, XPathExpressionException{
		createRowAndColumns(table,"eMeasure Title");
		column.appendText(getShortTitle(processor));
		
		row = table.appendElement(HTML_TR);
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column,TWENTY_PERCENT);
		createSpan("eMeasure Identifier\n"+ "(Measure Authoring Tool)",column);
		column = row.appendElement(HTML_TD);
		setTDInfoAttributes(column,"30%", "");
		column.appendText(getInfo(processor, "emeasureid"));
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column,TWENTY_PERCENT);
		createSpan("eMeasure Version number",column);
		column = row.appendElement(HTML_TD);
		setTDInfoAttributes(column,"30%", "");
		column.appendText(getInfo(processor, "version"));
		
		row = table.appendElement(HTML_TR);
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column,TWENTY_PERCENT);
		createSpan("NQF Number",column);
		column = row.appendElement(HTML_TD);
		setTDInfoAttributes(column,"30%", "");
		column.appendText(getInfo(processor, "nqfid/@extension"));
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column,TWENTY_PERCENT);
		createSpan("GUID",column);
		column = row.appendElement(HTML_TD);
		column.appendText(getInfo(processor,"guid"));
		
		/*createRowAndColumns(table,"Measurement Period");
		column.appendText(getMeasurePeriod(processor));*/
		
		getMeasurePeriod(processor,table);
		
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
		
		createInnerItemTable(processor, table);
		
		createComponentMeasureList(processor, table);		
		
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
		
		createRowAndColumns(table, "Initial Population");
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
		
		createRowAndColumns(table, "Measure Population Exclusions");
		createDiv(getInfo(processor,"measurePopulationExclusionsDescription"), column);
		
		createRowAndColumns(table, "Measure Observations");
		createDiv(getInfo(processor,"measureObservationsDescription"), column);
		
		createRowAndColumns(table, "Supplemental Data Elements");
		createDiv(getInfo(processor,"supplementalData"), column);
		
		createRowAndColumns(table, "Quality Measure Set");
		createDiv(getInfo(processor,"qualityMeasureSet"), column);
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
	
	private static void getMeasurePeriod(XmlProcessor processor, Element table) throws XPathExpressionException{
		String start = getInfo(processor,"period/startDate");
		String end = getInfo(processor,"period/stopDate");
		String newStart = " ";
		String newEnd = " ";
		if(start != " "){
			newStart = formatDate(start);
		}
		if(end != " "){
			newEnd = formatDate(end);
		}
		
		createRowAndColumns(table, "MeasurementPeriod");
		column.appendText(newStart + " through " + newEnd);
		
	}
	private static String formatDate(String date){
		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		String day = date.substring(6, 8);
		month = getMonth(month);
		if("0000".equals(year)){
			year = "20xx";
		}
		if("0".equals(day.charAt(0))){
			day = day.substring(1,2);
		}
		return month + day + ", " + year;
	}
	private static String getMonth(String month){
		String returnMonth = "";
		if("01".equals(month)){
			returnMonth = "January ";
		}
		else if("02".equals(month)){
			returnMonth = "February ";
		}
		else if("03".equals(month)){
			returnMonth = "March ";
		}
		else if("04".equals(month)){
			returnMonth = "April ";
		}
		else if("05".equals(month)){
			returnMonth = "May ";
		}
		else if("06".equals(month)){
			returnMonth = "June ";
		}
		else if("07".equals(month)){
			returnMonth = "July ";
		}
		else if("08".equals(month)){
			returnMonth = "August ";
		}
		else if("09".equals(month)){
			returnMonth = "September ";
		}
		else if("10".equals(month)){
			returnMonth = "October ";
		}
		else if("11".equals(month)){
			returnMonth = "November ";
		}
		else if("12".equals(month)){
			returnMonth = "December ";
		}
		return returnMonth;
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
	
	private static void createInnerItemTable(XmlProcessor processor, Element table) throws XPathExpressionException{
		NodeList list = processor.findNodeList(processor.getOriginalDoc(), DETAILS_PATH + "itemCount/elementRef");
		
		if(list.getLength() > 0){
			Node node;
			for(int i = 0; i < list.getLength(); i++){
				node = list.item(i);
				NamedNodeMap map = node.getAttributes();
				
				if(node != null){
					createRowAndColumns(table, "Measure Item Count");
					if(map.item(0) != null && map.item(2) != null){
						createDiv(map.item(0).getTextContent() + ":" + map.item(2).getTextContent(), column);
					}
					else{
						createDiv("",column);
					}
				}
			}
		}
		else{
			createRowAndColumns(table, "Item Count");
		}
	}
	
	private static void createComponentMeasureList(XmlProcessor processor,Element table) throws XPathExpressionException {
		createRowAndColumns(table, "Component Measure");
		
		NodeList list = processor.findNodeList(processor.getOriginalDoc(), DETAILS_PATH + "componentMeasures/measure");
		if(list.getLength() > 0){
			Node node;
			
			Element innerTable = column.appendElement("table");
			innerTable.attr("class", "inner_table");
			Element innerTableBody = innerTable.appendElement("tBody");
			row = innerTableBody.appendElement(HTML_TR);
			
			column = row.appendElement("th");
			setTDHeaderAttributes(column,"70%");
			createSpan("Measure Name",column);
			
			column = row.appendElement("th");
			setTDHeaderAttributes(column,"10%");
			createSpan("Version Number",column);
			
			column = row.appendElement("th");
			setTDHeaderAttributes(column,TWENTY_PERCENT);
			createSpan("GUID",column);
			
			for(int i = 0; i<list.getLength(); i++){
				node = list.item(i);
				
				NamedNodeMap map = node.getAttributes();
				
				row = innerTableBody.appendElement(HTML_TR);
				
				column = row.appendElement(HTML_TD);
				setTDInfoAttributes(column,"70%","");
				if(map.item(2) != null){
					column.appendText(map.item(2).getTextContent());
				}
				
				column = row.appendElement(HTML_TD);
				setTDInfoAttributes(column,"10%","");
				Element ver = column.appendElement("ver");
				if(map.item(3) != null){
					ver.appendText(map.item(3).getTextContent());
				}
				
				column = row.appendElement(HTML_TD);
				setTDInfoAttributes(column,TWENTY_PERCENT,"");
				if(map.item(0) != null){
					column.appendText(map.item(0).getTextContent());
				}
			}
		}

		
	}
	
	private static void setTDHeaderAttributes(Element col, String width){
		col.attr("width", width);
		col.attr("bgcolor", "#656565");
		col.attr("style", "background-color:#656565");
	}
	private static void setTDInfoAttributes(Element col, String width, String span){
		col.attr("width", width);
		if(span.length()>0){
			col.attr("colspan", span);
		}
	}
	
	private static void createRowAndColumns(Element table, String title){
		row = table.appendElement(HTML_TR);
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column,TWENTY_PERCENT);
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
		org.jsoup.nodes.Document htmlDocument = new org.jsoup.nodes.Document("");
				
		DocumentType doc = new DocumentType("html","-//W3C//DTD HTML 4.01//EN","http://www.w3.org/TR/html4/strict.dtd","");
		htmlDocument.appendChild(doc);
		Element html = htmlDocument.appendElement("html");
	    html.appendElement("head");
	    html.appendElement("body");
		
		Element head = htmlDocument.head();
		htmlDocument.title(title);
		appendStyleNode(head);
		return htmlDocument;
	}

	private static void appendStyleNode(Element head) {
		String styleTagString = MATCssUtil.getCSS();
		head.append(styleTagString);
	}
}
