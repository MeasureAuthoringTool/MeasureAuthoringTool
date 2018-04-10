package mat.server.simplexml;

import java.nio.charset.StandardCharsets;

import javax.xml.xpath.XPathExpressionException;

import mat.server.simplexml.cql.MATCssCQLUtil;
import mat.server.util.XmlProcessor;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The class HeaderHumanREadableGenerator It creates and returns the header with
 * all the measure details in html document
 * 
 */
public class HeaderHumanReadableGenerator {
	
	/**
	 * The beginning of measure details x-path
	 */
	private static final String DETAILS_PATH = "/measure/measureDetails/";
	
	/**
	 * The constant HTML_TR
	 */
	private static final String HTML_TR = "tr";
	
	/**
	 * The constant HTML_TD
	 */
	private static final String HTML_TD = "td";
	
	/**
	 * The constant TWENTY_PERCENT
	 */
	private static final String TWENTY_PERCENT = "20%";
	
	/**
	 * The element row used to create the header table
	 */
	private static Element row;
	
	/**
	 * the element column used to create the header table
	 */
	private static Element column;
	
	/**
	 * Creates the html document with the human readable header in it
	 * 
	 * @param measureXML
	 *            String containing the measures xml
	 * @return The created html document
	 * @throws XPathExpressionException
	 */
	public static org.jsoup.nodes.Document generateHeaderHTMLForMeasure(
			String measureXML) throws XPathExpressionException {
		org.jsoup.nodes.Document htmlDocument = null;
		XmlProcessor measureXMLProcessor = new XmlProcessor(measureXML);
		
		htmlDocument = createBaseHTMLDocument(getInfo(measureXMLProcessor,
				"title"));
		Element bodyElement = htmlDocument.body();
		Element table = bodyElement.appendElement("table");
		table.attr("class", "header_table");
		Element tBody = table.appendElement("tbody");
		createDocumentGeneral(measureXMLProcessor, tBody);
		createEmeasureBrief(measureXMLProcessor, tBody);
		createSubjectOf(measureXMLProcessor, tBody);
		
		return htmlDocument;
	}
	
	/**
	 * Creates the html document with the human readable header in it
	 * 
	 * @param measureXML
	 *            String containing the measures xml
	 * @return The created html document
	 * @throws XPathExpressionException
	 */
	public static org.jsoup.nodes.Document generateHeaderHTMLForCQLMeasure(
			String measureXML) throws XPathExpressionException {
		org.jsoup.nodes.Document htmlDocument = null;
		XmlProcessor measureXMLProcessor = new XmlProcessor(measureXML);
		
		htmlDocument = createBaseHTMLDocumentCQL(getInfo(measureXMLProcessor, "title"));
		Element bodyElement = htmlDocument.body();
		Element table = bodyElement.appendElement("table");
		table.attr("class", "header_table");
		Element tBody = table.appendElement("tbody");
		createDocumentGeneral(measureXMLProcessor, tBody);
		createEmeasureBrief(measureXMLProcessor, tBody);
		createSubjectOf(measureXMLProcessor, tBody);
		
		return htmlDocument;
	}
	
	public static void addMeasureSet(XmlProcessor processor,
			org.jsoup.nodes.Document html) throws XPathExpressionException {
		Element body = html.body();
		
		body.append("<div style=\"float:left; background:teal; height:3px; width:80%\"></div><pre><br/></pre>");
		
		Element table = body.appendElement("table");
		table.attr("class", "header_table");
		Element tBody = table.appendElement("tbody");
		
		createRowAndColumns(tBody, "Measure Set");
		createDiv(getInfo(processor, "qualityMeasureSet"), column);
	}
	
	/**
	 * Creates the first part of the header table
	 * 
	 * @param processor
	 *            XmlProcessor that contains the measures xml
	 * @param table
	 *            The table element to put the info in
	 * @throws DOMException
	 * @throws XPathExpressionException
	 */
	private static void createDocumentGeneral(XmlProcessor processor,
			Element table) throws DOMException, XPathExpressionException {
		
		// eCQM Title
		createRowAndColumns(table, "eCQM Title");
		column.append("<h1 style=\"font-size:10px\">" + getShortTitle(processor) +"</h1>");
		
		// eCQM Identifier and Version number
		row = table.appendElement(HTML_TR);
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column, TWENTY_PERCENT);
		createSpan("eCQM Identifier\n" + "(Measure Authoring Tool)", column);
		column = row.appendElement(HTML_TD);
		setTDInfoAttributes(column, "30%", "");
		column.appendText(getInfo(processor, "emeasureid"));
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column, TWENTY_PERCENT);
		createSpan("eCQM Version number", column);
		column = row.appendElement(HTML_TD);
		setTDInfoAttributes(column, "30%", "");
		column.appendText(getInfo(processor, "version"));
		
		// NQF number and GUID
		row = table.appendElement(HTML_TR);
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column, TWENTY_PERCENT);
		createSpan("NQF Number", column);
		column = row.appendElement(HTML_TD);
		setTDInfoAttributes(column, "30%", "");
		column.appendText(getInfo(processor, "nqfid/@extension"));
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column, TWENTY_PERCENT);
		createSpan("GUID", column);
		column = row.appendElement(HTML_TD);
		column.appendText(getInfo(processor, "guid"));
		
		// Calls getMeasurePeriod to produce the Measurement Period
		getMeasurePeriod(processor, table);
		
		// CMS ID number
		if (processor.findNode(processor.getOriginalDoc(), DETAILS_PATH + "cmsid") != null) {
			createRowAndColumns(table, "CMS ID Number");
			column.appendText(getInfo(processor, "cmsid"));
		}
		
		// Measure Steward
		createRowAndColumns(table, "Measure Steward");
		column.appendText(getInfo(processor, "steward"));
		
		// Measure Developers
		getInfoNodes(table, processor, "developers/developer",
				"Measure Developer", false);
		
		// Endorsed By
		createRowAndColumns(table, "Endorsed By");
		column.appendText(getInfo(processor, "endorsement"));
	}
	
	/**
	 * Creates the "Description section of the header table
	 * 
	 * @param processor
	 *            XmlProcessor that contains the measures xml
	 * @param table
	 *            The table element to put the info in
	 * @throws XPathExpressionException
	 */
	private static void createEmeasureBrief(XmlProcessor processor,
			Element table) throws XPathExpressionException {
		// Description
		createRowAndColumns(table, "Description");
		createDiv(getInfo(processor, "description"), column);
	}
	
	/**
	 * Creates the rest of the header table
	 * 
	 * @param processor
	 *            XmlProcessor that contains the measures xml
	 * @param table
	 *            The table element to put the info in
	 * @throws XPathExpressionException
	 */
	private static void createSubjectOf(XmlProcessor processor, Element table)
			throws XPathExpressionException {
		
		String expansionId = getExpansionIdentifier(processor);
		// add expansionId if set
		if (expansionId != null && !expansionId.isEmpty() && !expansionId.equals("")){
			// Expansion Profile
			createRowAndColumns(table, "VSAC Value Set Expansion Identifier");
			column.appendText(expansionId);
			createDiv(getInfo(processor, "vsacExpIdentifier"), column);  
		} 
		
		// Copyright
		createRowAndColumns(table, "Copyright");
		createDiv(getInfo(processor, "copyright"), column);
		
		// Disclaimer
		createRowAndColumns(table, "Disclaimer");
		createDiv(getInfo(processor, "disclaimer"), column);
		
		// Measure Scoring
		createRowAndColumns(table, "Measure Scoring");
		String measureScoring = getInfo(processor, "scoring");
		String patientbasedIndicator = getInfo(processor, "patientBasedIndicator");
		column.appendText(measureScoring);
		
		// Measure Type
		getInfoNodes(table, processor, "types/type", "Measure Type", false);
		
		// Measure Item Count
		createItemCount(processor, table);
		
		// Component Measures
		createComponentMeasureList(processor, table);
		
		// Stratification
		createRowAndColumns(table, "Stratification");
		createDiv(getInfo(processor, "stratification"), column);
		
		// Risk Adjustment
		createRowAndColumns(table, "Risk Adjustment");
		createDiv(getInfo(processor, "riskAdjustment"), column);
		
		// Rate Aggregation
		createRowAndColumns(table, "Rate Aggregation");
		createDiv(getInfo(processor, "aggregation"), column);
		
		// Rationale
		createRowAndColumns(table, "Rationale");
		createDiv(getInfo(processor, "rationale"), column);
		
		// Clinical Recommendation Statement
		createRowAndColumns(table, "Clinical Recommendation Statement");
		createDiv(getInfo(processor, "recommendations"), column);
		
		// Improvement Notation
		createRowAndColumns(table, "Improvement Notation");
		createDiv(getInfo(processor, "improvementNotations"), column);
		
		// References
		getInfoNodes(table, processor, "references/reference", "Reference", true);
		
		// Definitions
		getInfoNodes(table, processor, "definitions", "Definition", true);
		
		// Guidance
		createRowAndColumns(table, "Guidance");
		createDiv(getInfo(processor, "guidance"), column);
		
		// Transmission Format
		createRowAndColumns(table, "Transmission Format");
		createDiv(getInfo(processor, "transmissionFormat"), column);
		
		// Initial Population
		createRowAndColumns(table, "Initial Population");
		createDiv(getInfo(processor, "initialPopDescription"), column);
		
		//MAT 5014 Start - Dynamically show/hide populations based on scoring type.
		if (measureScoring.equalsIgnoreCase("Proportion") || measureScoring.equalsIgnoreCase("Ratio")) {
			// Denominator
			createRowAndColumns(table, "Denominator");
			createDiv(getInfo(processor, "denominatorDescription"), column);
			
			// Denominator Exclusions
			createRowAndColumns(table, "Denominator Exclusions");
			createDiv(getInfo(processor, "denominatorExclusionsDescription"), column);
			
			// Numerator
			createRowAndColumns(table, "Numerator");
			createDiv(getInfo(processor, "numeratorDescription"), column);
			
			// Numerator Exclusions
			createRowAndColumns(table, "Numerator Exclusions");
			createDiv(getInfo(processor, "numeratorExclusionsDescription"), column);
		}
		
		if (measureScoring.equalsIgnoreCase("Proportion")) {
			// Denominator Exceptions
			createRowAndColumns(table, "Denominator Exceptions");
			createDiv(getInfo(processor, "denominatorExceptionsDescription"),
					column);
		}
		
		if (measureScoring.equalsIgnoreCase("Continuous Variable")) {
			// Measure Population
			createRowAndColumns(table, "Measure Population");
			createDiv(getInfo(processor, "measurePopulationDescription"), column);
			
			// Measure Population Exclusions
			createRowAndColumns(table, "Measure Population Exclusions");
			createDiv(getInfo(processor, "measurePopulationExclusionsDescription"), column);
		}
		
		if (measureScoring.equalsIgnoreCase("Continuous Variable")
				|| (measureScoring.equalsIgnoreCase("Ratio") && !"true".equalsIgnoreCase(patientbasedIndicator))) {
			// Measure Observations
			createRowAndColumns(table, "Measure Observations");
			createDiv(getInfo(processor, "measureObservationsDescription"), column);
		}
		
		//MAT 5014 End - Dynamically show/hide populations based on scoring type.
		// Supplemental Data Elements
		createRowAndColumns(table, "Supplemental Data Elements");
		createDiv(getInfo(processor, "supplementalData"), column);
	}
	
	/**
	 * Gets the short title of the measure
	 * 
	 * @param processor
	 *            XmlProcessor that contains the measures xml
	 * @return Sting containing the measure's short title
	 * @throws DOMException
	 * @throws XPathExpressionException
	 */
	private static String getShortTitle(XmlProcessor processor) throws DOMException, XPathExpressionException {
		
		String title = processor.findNode(processor.getOriginalDoc(), DETAILS_PATH + "title").getTextContent();
		if (title == null) {
			title = "";
		} else {
			if (title.contains("(###)")) {
				title = title.substring(0, title.indexOf("(###)"));
			}
		}
		return title;
	}
	
	/**
	 * Gets the measures measurement period
	 * 
	 * @param processor
	 *            XmlProcessor that contains the measures xml
	 * @param table
	 *            The table element to put the info in
	 * @throws XPathExpressionException
	 */
	private static void getMeasurePeriod(XmlProcessor processor, Element table) throws XPathExpressionException {
		
		boolean calenderYear = Boolean.valueOf(getInfo(processor, "period/@calenderYear"));
		String start = getInfo(processor, "period/startDate");
		String end = getInfo(processor, "period/stopDate");
		String newStart = " ";
		String newEnd = " ";
		String through = " through ";
		// if start or end are not null format the date
		if(!calenderYear){
			if (!" ".equals(start)) {
				newStart = formatDate(start);
			}
			if (!" ".equals(end)) {
				newEnd = formatDate(end);
			}
		} else {
			if (!" ".equals(start)) {
				newStart = formatDate("20XX0101");
			}
			if (!" ".equals(end)) {
				newEnd = formatDate("20XX1231");
			}
		}
		// if the ending date is null we don't want through to display
		if (" ".equals(newEnd)) {
			through = "";
		}
		createRowAndColumns(table, "Measurement Period");
		column.appendText(newStart + through + newEnd);
		
	}
	
	/**
	 * Formats the date for measurement period
	 * 
	 * @param date
	 *            An eight digit string representing a date formated like
	 *            YYYYMMDD
	 * @return A string
	 */
	private static String formatDate(String date) {
		String returnDate = " ";
		// The string should be 8 characters long if not return " "
		if (date.length() == 8) {
			// Separate the string into year, month, and dat
			String year = date.substring(0, 4);
			String month = date.substring(4, 6);
			String day = date.substring(6, 8);
			// Get the string version of the month
			month = getMonth(month);
			// if the year equals 0000 we display 20xx
			if ("0000".equals(year)) {
				year = "20XX";
			}
			// if the day starts with a zero only display the second digit
			if (day.charAt(0) == '0') {
				day = day.substring(1, 2);
			}
			returnDate = month + day + ", " + year;
		}
		return returnDate;
	}
	
	/**
	 * Returns the string representation of a month
	 * 
	 * @param month
	 *            String containing the month numbers
	 * @return String representation of the given month
	 */
	private static String getMonth(String month) {
		String returnMonth = "";
		if ("01".equals(month)) {
			returnMonth = "January ";
		} else if ("02".equals(month)) {
			returnMonth = "February ";
		} else if ("03".equals(month)) {
			returnMonth = "March ";
		} else if ("04".equals(month)) {
			returnMonth = "April ";
		} else if ("05".equals(month)) {
			returnMonth = "May ";
		} else if ("06".equals(month)) {
			returnMonth = "June ";
		} else if ("07".equals(month)) {
			returnMonth = "July ";
		} else if ("08".equals(month)) {
			returnMonth = "August ";
		} else if ("09".equals(month)) {
			returnMonth = "September ";
		} else if ("10".equals(month)) {
			returnMonth = "October ";
		} else if ("11".equals(month)) {
			returnMonth = "November ";
		} else if ("12".equals(month)) {
			returnMonth = "December ";
		}
		return returnMonth;
	}
	
	/**
	 * Used to generate the table rows of elements that will potentially have
	 * multiple rows.
	 * 
	 * @param table
	 *            The table element to put the info in
	 * @param processor
	 *            XmlProcessor that contains the measures xml
	 * @param lookUp
	 *            The last part of the xPath to locate the nodes in the xml
	 * @param display
	 *            What gets displayed in the header column
	 * @param addDiv
	 *            Boolean, tells whether to add a div formating element
	 * @throws XPathExpressionException
	 */
	private static void getInfoNodes(Element table, XmlProcessor processor,
			String lookUp, String display, boolean addDiv)
					throws XPathExpressionException {
		// Gets all nodes that are in the given path
		NodeList developers = processor.findNodeList(
				processor.getOriginalDoc(), DETAILS_PATH + lookUp);
		
		if (developers.getLength() > 0) {
			Node person;
			// loop through each node
			for (int i = 0; i < developers.getLength(); i++) {
				person = developers.item(i);
				// Not adding a div create the row and column and append text
				if (!addDiv) {
					createRowAndColumns(table, display);
					if (person != null) {
						column.appendText(person.getTextContent());
					}
				}
				// Adding a div, create the rows and then create the div Element
				else {
					if (person != null) {
						createRowAndColumns(table, display);
						createDiv(person.getTextContent(), column);
					}
				}
			}
		}
		// Even if there are no nodes still display an empty row
		else {
			createRowAndColumns(table, display);
		}
	}
	
	/**
	 * Retreves the text content from the node on the given xPath
	 * 
	 * @param processor
	 *            XmlProcessor that contains the measures xml
	 * @param lookUp
	 *            The last part of the xPath to locate the nodes in the xml
	 * @return The string content of the node on the given xPath
	 * @throws XPathExpressionException
	 */
	private static String getInfo(XmlProcessor processor, String lookUp)
			throws XPathExpressionException {
		String returnVar = " ";
		// Finds the node
		Node node = processor.findNode(processor.getOriginalDoc(), DETAILS_PATH
				+ lookUp);
		// If the node exists return the text value
		if ((node != null) && (node.getTextContent()!=null)) {
			returnVar = node.getTextContent();
		}
		// else the node does not exist
		// if were looking for "endorsement return None
		else if (lookUp.equalsIgnoreCase("endorsement")
				|| lookUp.equalsIgnoreCase("nqfid/@extension")) {
			returnVar = "None";
		}
		// if we were looking for guid return Pending
		else if (lookUp.equalsIgnoreCase("guid")) {
			returnVar = "Pending";
		}
		// if we were looking for title return Health Quality Measure Document
		else if (lookUp.equalsIgnoreCase("title")) {
			returnVar = "Health Quality Measure Document";
		}
		return returnVar;
	}
	
	private static String getExpansionIdentifier(XmlProcessor processor) throws XPathExpressionException{
		String vsacExpIdentifier = "";
		// Finds the node
		Node node = processor.findNode(processor.getOriginalDoc(), "/measure/elementLookUp/@vsacExpIdentifier");
		
		// If the node exists return the text value
		if ((node != null) && (node.getTextContent()!=null)) {
			vsacExpIdentifier = node.getTextContent();
		}
		return vsacExpIdentifier;
	}

	
	/**
	 * Creates the Item Count section of the header
	 * 
	 * @param processor
	 *            XmlProcessor that contains the measures xml
	 * @param table
	 *            The table element to put the info in
	 * @throws XPathExpressionException
	 */
	private static void createItemCount(XmlProcessor processor, Element table) throws XPathExpressionException {
		NodeList list = processor.findNodeList(processor.getOriginalDoc(),
				DETAILS_PATH + "itemCount/elementRef");
		
		if (list.getLength() > 0) {
			Node node;
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				// Create a NamedNodeMap of the nodes attributes
				NamedNodeMap map = node.getAttributes();
				
				if (node != null) {
					createRowAndColumns(table, "Measure Item Count");
					if ((map.getNamedItem("dataType") != null)
							&& (map.getNamedItem("name") != null)) {
						if(map.getNamedItem("instance")!=null){
							createDiv(map.getNamedItem("instance").getNodeValue()+" of "+map.getNamedItem("dataType").getNodeValue()
									+ ": "
									+ map.getNamedItem("name").getNodeValue(),
									column);
							
						} else{
							createDiv(map.getNamedItem("dataType").getNodeValue()
									+ ": "
									+ map.getNamedItem("name").getNodeValue(),
									column);
						}
						
					} else {
						createDiv("", column);
					}
				}
			}
		}
	}
	
	/**
	 * Creates a table in the header table to display the component measures
	 * 
	 * @param processor
	 *            XmlProcessor that contains the measures xml
	 * @param table
	 *            The table element to put the info in
	 * @throws XPathExpressionException
	 */
	private static void createComponentMeasureList(XmlProcessor processor,
			Element table) throws XPathExpressionException {
		// Create the row in the main table
		
		// find the list of nodes
		NodeList list = processor.findNodeList(processor.getOriginalDoc(),
				DETAILS_PATH + "componentMeasures/measure");
		if (list.getLength() > 0) {
			createRowAndColumns(table, "Component Measure");
			Node node;
			
			// Create the inner table
			Element innerTable = column.appendElement("table");
			innerTable.attr("class", "inner_table");
			Element innerTableBody = innerTable.appendElement("tBody");
			row = innerTableBody.appendElement(HTML_TR);
			
			// create the headers for the inner table
			// Measure Name
			column = row.appendElement("th");
			setTDHeaderAttributes(column, "60%");
			createSpan("Measure Name", column);
			
			// Version Number
			column = row.appendElement("th");
			setTDHeaderAttributes(column, "10%");
			createSpan("Version Number", column);
			
			// GUID
			column = row.appendElement("th");
			setTDHeaderAttributes(column, "30%");
			createSpan("GUID", column);
			
			// for each node append a row with all information
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				
				NamedNodeMap map = node.getAttributes();
				
				row = innerTableBody.appendElement(HTML_TR);
				
				column = row.appendElement(HTML_TD);
				setTDInfoAttributes(column, "60%", "");
				if (map.getNamedItem("name") != null) {
					column.appendText(map.getNamedItem("name").getNodeValue());
				}
				
				column = row.appendElement(HTML_TD);
				setTDInfoAttributes(column, "10%", "");
				Element div = column.appendElement("div");
				div.attr("class", "ver");
				
				if (map.getNamedItem("versionNo") != null) {
					div.appendText(map.getNamedItem("versionNo").getNodeValue());
				}
				
				column = row.appendElement(HTML_TD);
				setTDInfoAttributes(column, "30%", "");
				// map.item(0) pulls the GUID from the NamedNodeMap
				if (map.getNamedItem("measureSetId") != null) {
					column.appendText(map.getNamedItem("measureSetId").getNodeValue());
				}
			}
		}
		
	}
	
	/**
	 * Sets the formating for the td headers
	 * 
	 * @param col
	 *            The column Element to format
	 * @param width
	 *            The width we want the column to take up
	 */
	private static void setTDHeaderAttributes(Element col, String width) {
		col.attr("style", "background-color:#656565; width:"+width);
	}
	
	/**
	 * Sets the formating for the information columns
	 * 
	 * @param col
	 *            The column Element to format
	 * @param width
	 *            The width we want the column to take up
	 * @param span
	 *            The number of columns to span if blank no span is added
	 */
	private static void setTDInfoAttributes(Element col, String width, String span) {
		col.attr("style", "width:"+width);
		if (span.length() > 0) {
			col.attr("colspan", span);
		}
	}
	
	/**
	 * Creates the row and columns for the table
	 * 
	 * @param table
	 *            The table Element to add the row and columns to
	 * @param title
	 *            The title to be put in the header column
	 */
	private static void createRowAndColumns(Element table, String title) {
		row = table.appendElement(HTML_TR);
		column = row.appendElement(HTML_TD);
		setTDHeaderAttributes(column, TWENTY_PERCENT);
		createSpan(title, column);
		column = row.appendElement(HTML_TD);
		setTDInfoAttributes(column, "80%", "3");
	}
	/**
	 * Used to format and make a span element
	 * 
	 * @param message
	 *            The message to be displayed in the span
	 * @param column
	 *            The column to append the span to
	 */
	private static void createSpan(String message, Element column) {
		Element span = column.appendElement("span");
		span.attr("class", "td_label");
		span.appendText(message);
	}
	
	/**
	 * Used to format and make a div element
	 * 
	 * @param message
	 *            The message to be displayed in the div element
	 * @param column
	 *            The column to append the div to
	 */
	private static void createDiv(String message, Element column) {
		Element div = column.appendElement("div");
		div.attr("style", "width:660px;overflow-x:hidden;overflow-y:auto");
		Element pre = div.appendElement("pre");
		pre.appendText(message);
	}
	
	/**
	 * Creates the initial html jsoup document
	 * 
	 * @param title
	 *            The title of the document
	 * @return jsoup html document that has been initialized
	 */
	private static org.jsoup.nodes.Document createBaseHTMLDocument(String title) {
		org.jsoup.nodes.Document htmlDocument = new org.jsoup.nodes.Document("");
		
		// Must be added first for proper formating and styling
		DocumentType doc = new DocumentType("html","-//W3C//DTD HTML 4.01//EN","http://www.w3.org/TR/html4/strict.dtd");
		htmlDocument.appendChild(doc);
		
		Element html = htmlDocument.appendElement("html");
		// POC - Added language attribute in html tag as asked by Matt.
		html.attributes().put(new Attribute("lang", "en"));
		html.appendElement("head");
		html.appendElement("body");
		
		Element head = htmlDocument.head();
		htmlDocument.title(title);
		appendStyleNode(head);
		return htmlDocument;
	}
	
	private static org.jsoup.nodes.Document createBaseHTMLDocumentCQL(String title) {
		org.jsoup.nodes.Document htmlDocument = new org.jsoup.nodes.Document("");
		htmlDocument.charset(StandardCharsets.UTF_8);
		
		// Must be added first for proper formating and styling
		DocumentType doc = new DocumentType("html",
				"-//W3C//DTD HTML 4.01 Transitional//EN",
				"http://www.w3.org/TR/html4/loose.dtd");
		htmlDocument.appendChild(doc);

		Element html = htmlDocument.appendElement("html");
		// POC - Added language attribute in html tag as asked by Matt.
		html.attributes().put(new Attribute("lang", "en"));
		
		Element headElement = html.appendElement("head");
		headElement.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
		
		html.appendElement("body");

		Element head = htmlDocument.head();
		htmlDocument.title(title);
		appendCQLStyleNode(head);
		return htmlDocument;
	}
	
	private static void appendCQLStyleNode(Element head) {
		String styleTagString = MATCssCQLUtil.getCSS();
		head.append(styleTagString);
	}
	
	/**
	 * Puts the style section into the html document
	 * 
	 * @param head
	 */
	private static void appendStyleNode(Element head) {
		String styleTagString = MATCssUtil.getCSS();
		head.append(styleTagString);
	}
}
