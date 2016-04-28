package mat.server.simplexml.cql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mat.model.cql.parser.CQLBaseStatementInterface;
import mat.model.cql.parser.CQLDefinitionModelObject;
import mat.model.cql.parser.CQLFileObject;
import mat.model.cql.parser.CQLFunctionModelObject;
import mat.model.cql.parser.CQLFunctionModelObject.FunctionArgument;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.w3c.dom.Node;

public class CQLHumanReadableHTMLCreator {
	
	private static final String[] keyWordListArray = {"library","version","using","include","called","public","private",
		"parameter","default","codesystem","valueset","codesystems","define",
		"function","with","without","in","from","where","return",
		"all","distinct","sort","by","asc","desc","is","not","cast","as","between",
		"difference","contains","and","or","xor","union","intersection","year","month",
		"day","hour","minute","second","millisecond","when","then","or","or less", 
		"before","after","or more","more","less","context","using", "QDM","Interval",
		"DateTime","Patient","Population","such that"};

	private static final String[] cqlFunctionsListArray = {"date","time","timezone","starts","ends",
			"occurs","overlaps","Interval",
			"Tuple","List","DateTime","AgeInYearsAt"};
	
	private static List<String> definitionsOrFunctionsAlreadyDisplayed = new ArrayList<String>();
	private static List<String> cqlObjects = new ArrayList<String>();

	public static String generateCQLHumanReadableForSinglePopulation(
			Node populationNode, CQLFileObject cqlFileObject) {

		definitionsOrFunctionsAlreadyDisplayed.clear();
		cqlObjects.clear();

		populateCQLObjectsList(cqlFileObject);

		Node cqlNode = populationNode.getFirstChild();
		String populationName = populationNode.getAttributes()
				.getNamedItem("displayName").getNodeValue();

		org.jsoup.nodes.Document htmlDocument = null;
		htmlDocument = createBaseHTMLDocument("Human Readable for "
				+ populationName);

		Element bodyElement = htmlDocument.body();

		generatePopulationCriteria(bodyElement, cqlFileObject, cqlNode,
				populationName);

		return htmlDocument.html();
	}

	private static void generatePopulationCriteria(Element bodyElement,
			CQLFileObject cqlFileObject, Node cqlNode, String populationName) {

		/*
		 * bodyElement.append(
		 * "<p id=\"nn-text\">Non-normative Example - Only CQL Logic.</p>");
		 * bodyElement.append(
		 * "<div><span id=\"d1e521\" class=\"section\">Population Criteria</span>\r\n"
		 * +
		 * "      <a href=\"#toc\" style=\"margin-top:-10px;\">Table of Contents</a></div>"
		 * );
		 */

		Element mainDivElement = bodyElement.appendElement("div");
		mainDivElement.attr("class", "treeview hover p-l-10");

		Element mainULElement = mainDivElement.appendElement("ul");
		mainULElement.attr("class", "list-unstyled");

		String cqlNodeType = cqlNode.getNodeName();
		String cqlName = cqlNode.getAttributes().getNamedItem("displayName").getNodeValue();
		
		System.out.println("Generating Human readable for:" + cqlNodeType + ":"
				+ cqlName);

		if ("cqldefinition".equals(cqlNodeType)) {
			generateHTMLForPopulation(mainULElement, cqlFileObject.getDefinitionsMap().get(cqlName),
					populationName, cqlName);
		} else if ("cqlfunction".equals(cqlNodeType)){
			generateHTMLForPopulation(mainULElement, cqlFileObject.getFunctionsMap().get(cqlName),
					populationName, cqlName);
		} else if("cqlaggfunction".equals(cqlNodeType)){
			CQLAggregateFunction cqlAggregateFunction = new CQLHumanReadableHTMLCreator().new CQLAggregateFunction();
			cqlAggregateFunction.setIdentifier(cqlName + " of:");
			
			Node childCQLNode = cqlNode.getChildNodes().item(0);
			String childCQLName = childCQLNode.getAttributes().getNamedItem("displayName").getNodeValue();
			CQLFunctionModelObject cqlFunctionModelObject = cqlFileObject.getFunctionsMap().get(childCQLName);
			
			cqlAggregateFunction.getReferredToFunctions().add(cqlFunctionModelObject);
			
			generateHTMLForPopulation(mainULElement, cqlAggregateFunction, populationName, cqlName);
		}

	}

	private static void generateHTMLForPopulation(Element mainElement,
			CQLBaseStatementInterface cqlBaseStatementObject, String populationName,
			String mainDefinitionName) {

		// create a base LI element
		Element mainliElement = mainElement.appendElement("li");
		mainliElement.attr("class", "list-unstyled");

		Element checkBoxElement = mainliElement.appendElement("input");
		checkBoxElement.attr("type", "checkbox");
		String id = "test-" + populationName + "_"
				+ (int) (Math.random() * 1000);
		checkBoxElement.attr("id", id);

		if (definitionsOrFunctionsAlreadyDisplayed.contains(populationName)) {
			checkBoxElement.attr("checked", "");
		} else {
			definitionsOrFunctionsAlreadyDisplayed.add(populationName);
		}

		Element definitionLabelElement = mainliElement.appendElement("label");
		definitionLabelElement.attr("for", id);
		definitionLabelElement.attr("class", "list-header");

		Element strongElement = definitionLabelElement.appendElement("strong");
		strongElement.appendText(populationName);
		
		definitionLabelElement.appendText(" (click to expand/collapse)");
		System.out.println(mainDefinitionName);
		generateHTMLForDefinitionOrFunction(cqlBaseStatementObject, mainliElement, true);
	}

	private static void generateHTMLForDefinitionOrFunction(
			CQLBaseStatementInterface cqlBaseStatementObject,
			Element mainElement, boolean isTopDefinition) {

		String statementIdentifier = cqlBaseStatementObject.getIdentifier();
		
		//If this cqlBaseStatementObject is a function, then we need to consider 
		//arguments to the function.
		String statementSignature = statementIdentifier;
		
		List<FunctionArgument> argumentList = cqlBaseStatementObject.getArguments();
		if(argumentList != null && argumentList.size() > 0){
			statementSignature += "(";
		}
		for(FunctionArgument functionArgument:argumentList){
			statementSignature += functionArgument.getArgumentName() + " " + functionArgument.getArgumentType() + ", ";
		}
		
		if(argumentList != null && argumentList.size() > 0){
			if(statementSignature.endsWith(", ")){
				statementSignature = statementSignature.substring(0, statementSignature.length()-2);
			}
			statementSignature += ")";
		}

		Element mainULElement = mainElement;
		if (isTopDefinition) {
			mainULElement = mainElement.appendElement("ul");
			mainULElement.attr("class", "code");
		}

		Element mainliElement = mainULElement;
		// create a base LI element
		if (isTopDefinition) {
			mainliElement = mainULElement.appendElement("li");
		}

		Element mainDivElement = mainliElement.appendElement("div");
		mainDivElement.attr("class", "treeview hover p-l-10");

		Element checkBoxElement = mainDivElement.appendElement("input");
		checkBoxElement.attr("type", "checkbox");
		String id = "test-" + statementIdentifier + "_"
				+ (int) (Math.random() * 1000);
		checkBoxElement.attr("id", id);

		if (definitionsOrFunctionsAlreadyDisplayed.contains(statementIdentifier)) {
			checkBoxElement.attr("checked", "");
		} else {
			definitionsOrFunctionsAlreadyDisplayed.add(statementIdentifier);
		}

		Element defnOrFuncLabelElement = mainDivElement.appendElement("label");
		defnOrFuncLabelElement.attr("for", id);
		defnOrFuncLabelElement.attr("class", "list-header");

		Element strongElement = defnOrFuncLabelElement.appendElement("strong");
		strongElement.appendText(statementIdentifier);
				
		defnOrFuncLabelElement.appendText(" (click to expand/collapse)");

		Element subULElement = mainDivElement.appendElement("ul");
		Element subLiElement = subULElement.appendElement("li");
		Element subDivElement = subLiElement.appendElement("div");
		
		if(!(cqlBaseStatementObject instanceof CQLAggregateFunction)){
			
			Element spanElem = getSpanElementWithClass(subDivElement, "cql_keyword");
			if(cqlBaseStatementObject instanceof CQLFunctionModelObject){
				spanElem.appendText("define function ");
			}else {
				spanElem.appendText("define ");
			}
	
			Element spanElemDefName = getSpanElementWithClass(subDivElement,
					"cql-class");
			spanElemDefName.appendText(statementSignature + ":");
			
			List<String> codeLineList = getDefnOrFuncLineList(cqlBaseStatementObject);
			
			if(codeLineList.size() > 0){
				subDivElement.append("&nbsp;" + codeLineList.get(0));
		
				subDivElement.appendElement("br");
		
				for (int i = 1; i < codeLineList.size(); i++) {
					Element spanElemDefBody = getSpanElementWithClass(subDivElement,
							"cql-definition-body");
					spanElemDefBody.append(codeLineList.get(i));
				}
			}
			subDivElement.appendElement("br");
		}
		

		List<CQLDefinitionModelObject> referredToDefinitionsModelObjectList = cqlBaseStatementObject
				.getReferredToDefinitions();
		
		for(CQLDefinitionModelObject referredTDefinitionModelObject : referredToDefinitionsModelObjectList){
			generateHTMLForDefinitionOrFunction(referredTDefinitionModelObject, subDivElement, false);
		}
		
		List<CQLFunctionModelObject> referredToFunctionsModelObjectList = cqlBaseStatementObject.getReferredToFunctions();
		
		for(CQLFunctionModelObject referredToFunctionModelObject : referredToFunctionsModelObjectList){
			generateHTMLForDefinitionOrFunction(referredToFunctionModelObject, subDivElement, false);
		}
		
	}

	/**
	 * This method will go through definition body and try to format it in
	 * series of lines for easier reading.
	 * 
	 * @param cqlBaseStatementObject
	 * @return
	 */
	private static List<String> getDefnOrFuncLineList(
			CQLBaseStatementInterface cqlBaseStatementObject) {

		List<String> lineList = new ArrayList<String>();
		
		if(cqlBaseStatementObject instanceof CQLFunctionModelObject){
			lineList.add("");
		}
		
		int tokenCounter = 0;

		List<String> childTokens = cqlBaseStatementObject.getChildTokens();
		
		if(childTokens.size() == 0){
			return new ArrayList<String>();
		}

		// find the first line
		if (childTokens.get(0).trim().equals("["))// Try to check for something
													// like "["Encounter,
													// Performed": "Ambulatory/ED
													// Visit"] E"
		{
			String tokenString = "[";
			// look further until you find ']'
			for (tokenCounter = 1; tokenCounter < childTokens.size(); tokenCounter++) {
				if (childTokens.get(tokenCounter).equals("]")) {
					if (childTokens.size() > (tokenCounter + 1)) {
						String nextToken = childTokens.get(tokenCounter + 1)
								.trim();
						if (nextToken.length() == 1
								&& Character.isLetter(nextToken.charAt(0))) {
							lineList.add(tokenString + "] "
									+ nextToken);
							tokenCounter += 2;
						} else { // check for something like " ["Diagnosis,
									// Active": "Acute
									// Pharyngitis"] union ["Diagnosis,
									// Active": "Acute Tonsillitis"]"
							lineList.add(" ");
							tokenCounter = 0;
						}
					} else {// check for something like " ["Diagnosis,
							// Active": "Acute Pharyngitis"] union ["Diagnosis,
							// Active": "Acute Tonsillitis"]"
						lineList.add(" ");
						lineList.add(tokenString + "] ");
						tokenCounter += 1;
					}
					break;
				} else {
					tokenString += " "
							+ wrapWithCssClass(childTokens.get(tokenCounter));
				}
			}
		} else if (childTokens.size() > 1
				&& childTokens.get(1).trim().length() == 1
				&& Character.isLetter(childTokens.get(1).trim().charAt(0)))// check
																			// for
																			// something
																			// like
																			// "MeasurementPeriodEncounters E"
		{
			tokenCounter = 2;
			lineList.add(childTokens.get(0) + " "
					+ wrapWithCssClass(childTokens.get(1)));
		}

		String tokenString = "";
		List<String> breakAtKeywords = new ArrayList<String>();
		breakAtKeywords.add("where");
		breakAtKeywords.add("with");
		breakAtKeywords.add("and");
		// breakAtKeywords.add("such that");

		for (; tokenCounter < childTokens.size(); tokenCounter++) {
			if (breakAtKeywords.contains(childTokens.get(tokenCounter).trim()
					.toLowerCase())
					&& tokenString.length() > 0) {
				lineList.add(tokenString + " "
						+ wrapWithCssClass(childTokens.get(tokenCounter)));
				tokenString = "";
			} else {
				String fillerSpace = " ";

				List<String> noSpaceTokens = new ArrayList<String>();
				noSpaceTokens.add(".");
				noSpaceTokens.add("(");
				// noSpaceTokens.add(")");
				// noSpaceTokens.add("[");
				// noSpaceTokens.add("]");
				String token = childTokens.get(tokenCounter);
				// String lastToken = tokenString.length() > 0 ?
				// tokenString.charAt(tokenString.length()-1)+"" : "";

				String lastToken = (tokenCounter > 0) ? childTokens
						.get(tokenCounter - 1) : "";
				
				System.out.println("token:"+token);
				System.out.println("lastToken:"+lastToken);
				
				if (noSpaceTokens.contains(token)
						|| noSpaceTokens.contains(lastToken)) {
					fillerSpace = "";
				}
				System.out.println("filler:"+fillerSpace+"!");
				tokenString += fillerSpace
						+ wrapWithCssClass(childTokens.get(tokenCounter));
				System.out.println("tokenString:" + tokenString);
				System.out.println();
			}
		}

		if (tokenString.length() > 0) {
			lineList.add(tokenString);
		}

		return lineList;
	}

	private static String wrapWithCssClass(String string) {

		String cssClass = "";
		if (string.trim().startsWith("\"") && string.endsWith("\"")) {
			cssClass = "cql_string";

		} else if (string.trim().length() == 1) {
			cssClass = "cql_identifier";
		} else if (contains(keyWordListArray, string.trim())) {
			cssClass = "cql_keyword";
		} else if (contains(cqlFunctionsListArray, string.trim())) {
			cssClass = "cql_function";
		} else if (cqlObjects.contains(string)) {
			cssClass = "cql-object";
		}

		string = "<span class=\"" + cssClass + "\">" + string + "</span>";
		return string;
	}

	private static boolean contains(String[] stringArray, String tokenString) {

		for (int i = 0; i < stringArray.length; i++) {
			if (tokenString.equals(stringArray[i])) {
				return true;
			}
		}
		return false;
	}

	private static Element getSpanElementWithClass(Element subLiElement,
			String cssClassName) {
		Element spanElem = subLiElement.appendElement("span");
		spanElem.attr("class", cssClassName);
		return spanElem;
	}

	private static org.jsoup.nodes.Document createBaseHTMLDocument(String title) {
		org.jsoup.nodes.Document htmlDocument = new org.jsoup.nodes.Document("");

		// Must be added first for proper formating and styling
		DocumentType doc = new DocumentType("html",
				"-//W3C//DTD HTML 4.01 Transitional//EN",
				"http://www.w3.org/TR/html4/loose.dtd", "");
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

	private static void appendStyleNode(Element head) {
		String styleTagString = MATCssCQLUtil.getCSS();
		head.append(styleTagString);
	}
	
	private static void populateCQLObjectsList(CQLFileObject cqlFileObject) {
		
		Map<String, CQLDefinitionModelObject> cqlDefinitionMap = cqlFileObject.getDefinitionsMap();
		cqlObjects.addAll(cqlDefinitionMap.keySet());
		
		Map<String, CQLFunctionModelObject> cqlFunctionsMap = cqlFileObject.getFunctionsMap();
		cqlObjects.addAll(cqlFunctionsMap.keySet());
	}
	
	class CQLAggregateFunction implements CQLBaseStatementInterface {

		private String identifier = "";
		private List<CQLFunctionModelObject> referredToFunctions = new ArrayList<CQLFunctionModelObject>();
		
		@Override
		public String getIdentifier() {
			return identifier;
		}
		
		public void setIdentifier(String identifierName) {
			identifier = identifierName;
		}

		@Override
		public List<String> getChildTokens() {
			return new ArrayList<String>();
		}

		@Override
		public List<FunctionArgument> getArguments() {
			return new ArrayList<FunctionArgument>();
		}

		@Override
		public List<CQLDefinitionModelObject> getReferredToDefinitions() {
			return new ArrayList<CQLDefinitionModelObject>();
		}

		@Override
		public List<CQLDefinitionModelObject> getReferredByDefinitions() {
			return new ArrayList<CQLDefinitionModelObject>();
		}

		@Override
		public List<CQLFunctionModelObject> getReferredToFunctions() {
			return referredToFunctions;
		}
		
		@Override
		public List<CQLFunctionModelObject> getReferredByFunctions() {
			return new ArrayList<CQLFunctionModelObject>();
		}
		
	}
}
