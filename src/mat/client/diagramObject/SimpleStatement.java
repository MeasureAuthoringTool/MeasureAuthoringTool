package mat.client.diagramObject;

import gwt.g2d.client.math.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import mat.client.clause.AppController;
import mat.client.clause.MeasurePhrases;
import mat.client.clause.diagram.TraversalTree;
import mat.client.clause.view.DiagramView;
import mat.client.codelist.HasListBox;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MATRadioButton;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.shared.ConstantMessages;
import mat.shared.IllegalRecursionException;
import mat.shared.StringUtility;
import mat.shared.TopologicalSort;
import mat.shared.Vertex;
import mat.shared.diagramObject.InProgress_Complete;
import mat.shared.model.StatementTerm;
import mat.shared.model.StatementTerm.Operator;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SimpleStatement extends DiagramObject {
	private static final String LINE_SEPARATOR = "\r\n"; 
	private static final String RECURSION_TEST_STATEMENT = "__TEST__";
	private static final int TAB_SIZE = 4;
	protected static final int NAME_PANEL_ROW = 0;
	protected static final int NAME_PANEL_COL = 0;
	protected static final int DESCRIPTION_PANEL_ROW = 1;
	protected static final int DESCRIPTION_PANEL_COL = 0;
	protected static final int FUNCTION_PANEL_ROW = 2;
	protected static final int FUNCTION_PANEL_COL = 0;
	protected static final int PHRASE_RADIO_ROW = 4;
	protected static final int PHRASE_LABEL_ROW = 3;
	protected static final int PHRASE_LIST_ROW = 5;
	protected static final int PHRASE1_COL = 0;
	protected static final int CONDITION_COL = 1;
	protected static final int COMPARISON_COL = 1;
	protected static final int PHRASE2_COL = 2;
	protected static final int SIMPLE_COMPARISON_QUANTITY_COL = 2;
	protected static final int  SIMPLE_COMPARISON_UNIT_COL = 3;
	protected static final int TOGGLE_BUTTON_COL = 2;
	protected static final int ADD_BUTTON_ROW = PHRASE_LIST_ROW;
	protected static final int FIRST_ADDITIONAL_PHRASE_ROW = 7;
	protected static final int ADD_REMOVE_BUTTON_COL = 3;	
	public static final int SAVE_BUTTON_ROW = 7;

	public static enum CLAUSE_TYPE {
		CLAUSE_LIBRARY("10"), MEASURE_PHRASE("11");
		private String type;

		private CLAUSE_TYPE(String type) {
			this.type = type;
		}

		public String getClauseType() {
			return type;
		}
	}

	protected List<String> qdmElements;
	protected List<String> qdmElementsWithAttrib;
	
	protected Set<String> phraseElements;
	private List<String> functions = Arrays.asList(new String[]  {
			NONE,
			"ABS","ADDDATE","ADDTIME","AVG","COUNTDISTINCT","COUNT","CURDATE","CURTIME","DATEDIFF","DAYOFMONTH","DAYOFWEEK","DAYOFYEAR","HOUR","MAX",
			"MEDIAN","MIN","MINUTE","MONTH","NOW","POSITION","ROUND","SEC","STDDEV","SUBDATE","SUBTIME","SUM","TIME",
			"TIMEDIFF","VARIANCE","WEEK"," WEEKDAY","WEEKOFYEAR","YEAR","YEARWEEK","FIRST","SECOND",
			"THIRD","FOURTH","FIFTH","LAST","RELATIVEFIRST","RELATIVESECOND", "NOT"});
	protected static final String NONE = " ";
	protected String[] conditions;
	protected List<String> statusArray;
	protected List<String> timingConditions;
	public static final String[] comparisonOperators = {
		"Less Than",
		"Greater Than",
		"Less Than or Equal To", 
		"Greater Than or Equal To",  
		"Equal To"
	};
	
	public static final String[] useCompOperators = {
		"Added To", 
		"Subtracted From", 
	};
	
	private String[] comparisonUnits;
	private String[] functionUnits;
	
	private String[] usePhraseConditionOpr;
	
	protected String[] children = {};
	protected String[] siblings = {};

	protected SimpleStatement me;
	protected CLAUSE_TYPE clauseType;
	protected AppController appController;
	
	protected Phrase phrase1;
	protected String condition;
	protected Phrase phrase2;
	protected List<Phrase> additionalPhraseList;
	protected String conditionalOperator;
	protected String quantity;	
    protected String status;
	private MATRadioButton phrasesRadio1 = new MATRadioButton("phrase1ElementType", "Measure Phrases");
	private MATRadioButton qdmElementsRadio1 = new MATRadioButton("phrase1ElementType", "QDM Elements");
	private MATRadioButton phrasesRadio2 = new MATRadioButton("phrase2ElementType", "Measure Phrases");
	private MATRadioButton qdmElementsRadio2 = new MATRadioButton("phrase2ElementType", "QDM Elements");
	
	protected String unit;
	protected String function;
	protected String functionOperator;
	protected String functionQuantity;
	protected String functionUnit;

	protected TextBox nameTextBox;
	protected TextBox descriptionTextBox;
	protected ListBox inProgressCompleteListBox;
	protected ListBox functionListBox;
	protected ListBox functionOperatorListBox;
	protected TextBox functionQuantityTextBox;
	protected ListBox functionUnitListBox;
	protected ListBox conditionListBox;
	protected FlexTable simpleComparisonGrid;
	protected Button toggleSimplePhrase2AndComparisonButton;
	protected HandlerRegistration toggleSimplePhrase2AndComparisonHandlerRegistration;
	protected ListBox conditionalOperatorListBox;
	protected TextBox quantityTextBox;
	protected ListBox unitsListBox;

	protected enum MODE {SINGLE, LOGICAL, COMPARISON, FUNCTION};
	protected MODE mode = MODE.SINGLE;
	protected enum SINGLE_SUBMODE {PHRASE, COMPARISON};//Use Phrase or Use Comparison
	protected SINGLE_SUBMODE singleSubMode = SINGLE_SUBMODE.PHRASE;

	private List<String> errorMsgList = new ArrayList<String>();
	private List<String> successMsgList = new ArrayList<String>();
	
	
	public List<String> getErrorMsgList(){
		return errorMsgList;
	}
	
	public SimpleStatement(){}
	
	public SimpleStatement(AppController appController) {	
		super();
        this.description = "";
        timingConditions = getTimingConditionsList();
		this.clauseType = CLAUSE_TYPE.MEASURE_PHRASE;
		this.appController = appController;
		this.additionalPhraseList = new ArrayList<Phrase>();
		me = this;
		phrase1 = new Phrase(appController);
		phrase2 = new Phrase(appController);
		
		phrasesRadio1.addFocusHandler(
				new SimpleStatementFocusHandler());
		
		qdmElementsRadio1.addFocusHandler(
				new SimpleStatementFocusHandler());
				
		phrasesRadio2.addFocusHandler(
				new SimpleStatementFocusHandler());
		
		qdmElementsRadio2.addFocusHandler(
				new SimpleStatementFocusHandler());
		
		//US 171. Initialize Condition DropDown for Use Phrase.
		MatContext.get().getListBoxCodeProvider().getUsePhraseConditions(new AsyncCallback<List<? extends HasListBox>>() {			
			
			@Override
			public void onFailure(Throwable caught) {
				errorMsgList.add(MessageDelegate.s_ERR_RETRIEVE_OPERATOR);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {				
				ArrayList<String> operatorList = new ArrayList<String>();
				for(HasListBox operator: result){
					operatorList.add(operator.getItem());
				}
				usePhraseConditionOpr = new String[result.size()];
				operatorList.toArray(usePhraseConditionOpr);
			}
       });
			
		//US 62. Initialize Units for comparison.
		MatContext.get().getListBoxCodeProvider().getUnitMatrixListByCategory(ConstantMessages.UNIT_COMPARISON, new AsyncCallback<List<? extends HasListBox>>() {			
			
			@Override
			public void onFailure(Throwable caught) {
				errorMsgList.add(MessageDelegate.s_ERR_RETRIEVE_UNITS);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {				
				ArrayList<String> unitList = new ArrayList<String>();
				for(HasListBox unit: result){
					unitList.add(unit.getItem());
				}
				comparisonUnits = new String[result.size()];
				unitList.toArray(comparisonUnits);
				
			}
       });

		//US 62. Initialize units for functions
		MatContext.get().getListBoxCodeProvider().getUnitMatrixListByCategory(ConstantMessages.UNIT_FUNCTION, new AsyncCallback<List<? extends HasListBox>>() {			
			
			@Override
			public void onFailure(Throwable caught) {
				errorMsgList.add(MessageDelegate.s_ERR_RETRIEVE_UNITS);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {				
				ArrayList<String> unitList = new ArrayList<String>();	
				
				for(HasListBox unit: result){
					unitList.add(unit.getItem());
				}
				functionUnits = new String[result.size()];
				unitList.toArray(functionUnits);
				
			}
       });

		//US 601 Initialize Status Dropdown From the ObjectStatus Table
		MatContext.get().getListBoxCodeProvider().getStatusList(new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				errorMsgList.add(MessageDelegate.s_ERR_RETRIEVE_STATUS);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				statusArray = new ArrayList<String>();
				for(HasListBox status: result){
					statusArray.add(status.getItem());
				}
			}
		});
		
	}

	class SimpleStatementFocusHandler implements FocusHandler{

		@Override
		public void onFocus(FocusEvent event) {
			MatContext.get().clearDVIMessages();
			
		}
		
	}

	public SimpleStatement(AppController appController, CLAUSE_TYPE clauseType) {
		this(appController);
		this.clauseType = clauseType;
	}

	public SimpleStatement(AppController appController, String identity) {		
		super(identity);
		this.clauseType = CLAUSE_TYPE.MEASURE_PHRASE;
		this.appController = appController;
		me = this;
		this.description = "";
		this.phrase1 = new Phrase(appController);
		this.phrase2 = new Phrase(appController);
	}

	public SimpleStatement(AppController appController, CLAUSE_TYPE clauseType, String identity) {
		this(appController, identity);
		this.clauseType = clauseType;
	}

	public SimpleStatement(AppController appController, CLAUSE_TYPE clauseType, String identity, InProgress_Complete inProgressComplete, String description) {
		this(appController, identity);
		this.clauseType = clauseType;
		this.description = description;
	}

	@Override
	public FlexTable start(DiagramView<?> view, List<String> elements, FlexTable g, String top, 
			ClickHandler okClickHandler, ClickHandler cancelClickHandler) {
		this.view = view;
		phrase1.setAppController(appController);
		phrase2.setAppController(appController);
		this.qdmElementsWithAttrib = elements;
		this.qdmElements= stripAttrib(this.qdmElementsWithAttrib);
		this.phraseElements = getMeasurePhraseList();
		this.okClickHandler = okClickHandler;
		this.cancelClickHandler = cancelClickHandler;
		init(view.getPropertyEditor(), g);
		action = ACTION.INSERT;

		switch (mode) {
		case SINGLE:
			initSingleMode();
			break;
		case LOGICAL:
			initLogicalMode();
			break;
		case COMPARISON:
			initComparisonMode();
			break;
		}
		setupNameTextBox(top, false);
		return grid;
	}

	private List<String> stripAttrib(List<String> elements){
		List<String> filtered = new ArrayList<String>();
		for(String str: elements){
			String tmp = MatContext.get().getTextSansOid(str);
			if(!tmp.trim().endsWith(": attribute")){
				filtered.add(str);
			}
		}
		return filtered;
	}
	
	@Override
	public FlexTable select(DiagramView<?> view, List<String> elements, FlexTable g, String top, 
			ClickHandler okClickHandler, ClickHandler deleteClickHandler, ClickHandler cancelClickHandler) {
		this.view = view;
		phrase1.setAppController(appController);
		if (phrase2 != null)
			phrase2.setAppController(appController);
		for (Phrase phrase : additionalPhraseList)
			phrase.setAppController(appController);
		this.qdmElementsWithAttrib = elements;
		this.qdmElements = stripAttrib(this.qdmElementsWithAttrib);
		this.phraseElements = getMeasurePhraseList();//
		this.okClickHandler = okClickHandler;
		this.deleteClickHandler = deleteClickHandler;
		this.cancelClickHandler = cancelClickHandler;
		init(view.getPropertyEditor(), g);
		action = ACTION.UPDATE;

		switch (mode) {
		case SINGLE:
			initSingleMode();
			break;
		case FUNCTION:
			//TODO: make sure comparison works when a function has been used.
			condition = this.getCondition();
			if (condition.equals("AND") || condition.equals("OR")) {
				mode = MODE.LOGICAL;
				initLogicalMode();
			}else
				initSingleMode();
			break;
		case LOGICAL:
			initLogicalMode();
			break;
		case COMPARISON:
			initComparisonMode();
			break;
		}
		nameTextBox.setText(identity);
		descriptionTextBox.setText(description);
		setItemText(inProgressCompleteListBox,status);
		phrase1.setText();
		setItemText(conditionListBox, condition);

		switch (mode) {
		case SINGLE:
			if (singleSubMode == SINGLE_SUBMODE.PHRASE)
				phrase2.setText();
			else {
				quantityTextBox.setText(quantity);	
				setItemText(unitsListBox, unit);
			}				
			break;
		case LOGICAL:
			int i = FIRST_ADDITIONAL_PHRASE_ROW;
			drawAdditionalPhrases();
			setupFunction();
			setLinkVisibility();
			break;
		case COMPARISON:
			if (conditionalOperator != null && !getRelAssociationsList().contains(condition)) {
				addComparison();
				setItemText(conditionalOperatorListBox, conditionalOperator);
				quantityTextBox.setText(quantity);
				setItemText(unitsListBox, unit);
			}
			setupFunction();
			break;
		case FUNCTION:
			if (quantity != null){
			quantityTextBox.setText(quantity);
			}

			if (unit != null) {				
				setItemText(unitsListBox, unit);
			}
			if(functionUnit != null){
				setItemText(functionUnitListBox, functionUnit);
			}
		default:
			break;
		}

		setupNameTextBox(top, true);
		return grid;
	}
	
	private Set<String> getMeasurePhraseList() {
		MeasurePhrases measurePhrases = appController.getMeasurePhrases().clone();
		List<String> phraseNames = appController.getMeasurePhraseList();
		Set<String> availablePhrases = new LinkedHashSet<String>();
		
		for(String name:phraseNames){
			String currentPhraseName = this.identity;
//			System.out.println("looking for cycle for:"+this.identity);
//			System.out.println("evaluating:"+name);
			if(name.equals(this.identity)){
				continue;
			}
			SimpleStatement measurePhrase = (SimpleStatement)measurePhrases.getItem(name);
			
//			System.out.println(name + " has measurePhrase.getPhrase1Text():"+measurePhrase.getPhrase1Text());
			if(measurePhrase.getPhrase1Text().equals(currentPhraseName)){
				continue;
			}else if(measurePhrase.getPhrase2() != null && measurePhrase.getPhrase2Text() != null && measurePhrase.getPhrase2Text().equals(currentPhraseName)){
				continue;
			}
			boolean isContinue=false;
			for (Phrase additionalPhrase : measurePhrase.getAdditionalPhraseList()) {
				if(additionalPhrase.getText().equals(currentPhraseName)){
					isContinue=true;
					break;
				}
			}
			if(isContinue){
				continue;
			}
			availablePhrases.add(measurePhrase.getIdentity());
		}
		
		if (availablePhrases.size() > 0) {
			Object[] objArray = availablePhrases.toArray();
			String[] strArray = new String[objArray.length];
			int index = 0;
			for (Object obj : objArray){
				strArray[index++] = (String)obj;
			}
			Arrays.sort(strArray);
			availablePhrases.addAll(Arrays.asList(strArray));
		}
		
		return availablePhrases;
	}
	
	private Set<String> getMeasurePhraseList_old() {
		  long startTime = System.currentTimeMillis();
		  MeasurePhrases measurePhrases = appController.getMeasurePhrases().clone();
		  List<String> phraseNames = appController.getMeasurePhraseList();
		  Set<String> availablePhrases = new LinkedHashSet<String>();
		 
		  // need to use phrase1's text for recursion testing; save it and restore later
		  String phrase1Text = phrase1.text; 
		  
		  SimpleStatement test = new SimpleStatement(appController);
		  test.setIdentity(this.identity);
		  test.setDescription(this.description);
		  test.phrase1 = new Phrase(appController);
		  
//		  long t1=0;
//		  long t0=0;
		  int i=0;
		  TopologicalSort g = null;
		  List<Vertex> vertexes = new ArrayList<Vertex>();
		  for(String nam:phraseNames){
			  vertexes.add(new Vertex(nam));
		  }
		  	  
//		  long start = System.currentTimeMillis();
		  
		  for (String name : phraseNames) {
			//phrase1.setText(name);
			phrase1.text = name;
			measurePhrases.put(RECURSION_TEST_STATEMENT, test);
			
//			long s1 = System.currentTimeMillis();
			if(i==0){
				g = new TopologicalSort(measurePhrases, phraseNames);
			}else{
				Vertex vertexList[] = new Vertex[phraseNames.size()];
				vertexes.toArray(vertexList);
//		    	int[][] matrix = new int[phraseNames.size()][phraseNames.size()];
								  
//		    	Although the declaration of the 2D array above is enough to initialize the array
//		    	with 0's, I am not sure how this would play when GWT converts this into Javascript.
//		    	So writing a for in for to initialize this with 0's.
//		    	for(int row=0;row<phraseNames.size();row++){
//				  vertexList[row] = new Vertex(phraseNames.get(row));
//				  for(int col=0;col<phraseNames.size();col++){
//					  matrix[row][col] = 0;
//				  }
//				}
//				g.setMatrix(matrix);
				g.setNumVerts(phraseNames.size());
		    	g.setSortedArray(new String[phraseNames.size()]);
		    	g.resetMatrix();
		    	g.setVertexList(vertexList);
		    	g.setMeasurePhrases(measurePhrases);
		    	g.addEdges();
		    }
//			g = new TopologicalSort(measurePhrases, phraseNames);
		    int ret = g.topologicalSort();
//		    long e1 = System.currentTimeMillis();
//		    t1 += e1-s1;
//		    if(i == 0){
//		    	t0 = t1;
//		    }
		    if(ret > 0){
		    	availablePhrases.add(name);
		    }
		    i++;
		  }
		  
//		  long end = System.currentTimeMillis();
//		  Window.alert("Time taken:"+(end-start)+" and topo sort took:"+t1);
		 
//		  if (availablePhrases.size() > 0) {
//			   Object[] objArray = availablePhrases.toArray();
//			   String[] strArray = new String[objArray.length];
//			   int index = 0;
//			   for (Object obj : objArray){
//			    strArray[index++] = (String)obj;
//			   }
//			   Arrays.sort(strArray);
//			   availablePhrases.addAll(Arrays.asList(strArray));
//		  }
		  
		  if (availablePhrases.size() > 0) {
			 Collections.sort(new ArrayList<String>(availablePhrases));
		  }

		 
		  phrase1.setText(phrase1Text);
		  long endTime = System.currentTimeMillis();
		  System.out.println("total time for simpleStatement.getMeasurePhraseList():"+(endTime - startTime));
		  return availablePhrases;
		 }
		 
	protected void setupNameTextBox(String top, boolean objectExists) {
		final boolean isNewPhrase = ((top == null || top.equalsIgnoreCase(this.EMPTY)) && !objectExists);
		if (isNewPhrase)
			nameTextBox.setFocus(true);
		else {					
			nameTextBox.setText(top);
			//nameTextBox.setEnabled(false);//This is the fix for 320 to make the Name field editable
			phrase1.getListBox().setFocus(true);
		}
	}

	protected void setupFunction() {
		setItemText(functionListBox, function);
		setItemText(functionOperatorListBox, functionOperator);
		functionQuantityTextBox.setText(functionQuantity);
		setItemText(functionUnitListBox,functionUnit);
	}

	public void switchSubmode() {
		//US 202 Fix for the data loss.
		//identity = nameTextBox.getText().trim();
		changedName = nameTextBox.getText().trim();
		description = descriptionTextBox.getText().trim();
	}
	
	public boolean update() {
		String name = nameTextBox.getText().trim();		
		if (name.length() == 0) {
			//This is getting delayed and firing after the message renders
			// since setting focus calls an event handler clearing messages
			// this is an issue. Only seems to be a problem in IE.
			//nameTextBox.setFocus(true);
			errorMsgList.add(MatContext.get().getMessageDelegate().getEnterNameMessage());
			return false;
		}
		//identity = name;//do not do this, instead change it to the changedName
		changedName = name;
		
		description = descriptionTextBox.getText().trim();
		switch(mode) {
		case SINGLE:
			return updateSingle();
		case LOGICAL:
			return updateLogical();
		case COMPARISON:
			if(phrase1.listBox.getItemCount() == 0 ){
				errorMsgList.add(MatContext.get().getMessageDelegate().getNoElementsMessage());
				return false;
			}else if(phrase2.getText() == null || phrase2.getText().isEmpty() || noneComparator(phrase2.getText(),NONE)){
				errorMsgList.add(MatContext.get().getMessageDelegate().getNeedSecondMessage());
				phrase2.getListBox().setFocus(true);
				return false;
			}else
				return updateComparison();
		case FUNCTION:
			return updateFunction();
		default:
			return false;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	protected boolean updateSingle() {
		if(phrase1.listBox.getItemCount() == 0 ){
			errorMsgList.add(MatContext.get().getMessageDelegate().getNoPhraseElementsMessage());
			return false;
		}else{
			phrase1.setText();
			condition = getItemText(conditionListBox);
			if (singleSubMode == SINGLE_SUBMODE.PHRASE) {
				if (phrase2 != null)
					phrase2.setText();
				if ((noneComparator(condition,NONE)) && !(noneComparator(phrase2.getText(),NONE))) {
					errorMsgList.add(MatContext.get().getMessageDelegate().getNoConditionMessage());
					phrase2.getListBox().setFocus(true);
					return false;
				}
			}
			else {
				phrase2 = null;
				if (!getComparison())
					return false;
			}
		}
	
	   return true;
		
	}

	private boolean noneComparator(String condition1,String condition2){
		return condition1.trim().equals(condition2.trim());
	}
	
	protected boolean updateLogical() {
		//need to set a alert message when there is no phrase element, but there is a condition AND/OR.
		if(phrase1.listBox.getItemCount() == 0 ){
			errorMsgList.add(MatContext.get().getMessageDelegate().getNoElementsMessage());
			return false;
		}else if(phrase2.getText() == null || phrase2.getText().isEmpty() || noneComparator(phrase2.getText(),NONE)){
			errorMsgList.add(MatContext.get().getMessageDelegate().getNeedSecondMessage());
			phrase2.getListBox().setFocus(true);
			return false;
		}else{
			phrase1.setText();
			condition = getItemText(conditionListBox);
			phrase2.setText();
			return updateFunction();
		}
	}

	protected boolean updateComparison() {
		if (!getComparison())
			return false;

		phrase1.setText();
		condition = getItemText(conditionListBox);
		phrase2.setText();

		return updateFunction();
	}

	protected void unupdateFunction() {
		//TODO Should this be "" by default???
		function = functionOperator = functionQuantity = functionUnit= null;
		
	}

	protected boolean updateFunction() {
		function = getItemText(functionListBox);
		functionUnit = getItemText(functionUnitListBox);
		if (function == null || function.trim().length() == 0)
			function = NONE;
		functionOperator = getItemText(functionOperatorListBox);
		if (functionOperator == null || functionOperator.trim().length() == 0)
			functionOperator = NONE;

		if ((noneComparator(function,NONE)) && !(noneComparator(functionOperator,NONE))) {
			unupdateFunction();
			return alert("Functions must be defined with operators.");
		}

		functionQuantity = functionQuantityTextBox.getText().trim();
		if ((noneComparator(function,NONE)) && !functionQuantity.equals("")) {
			unupdateFunction();
			return alert("If there is no function, it cannot have a quantity.");
		}

		if (!functionQuantity.equals("")) {
			try {
				//US211
				new Double(functionQuantity);
				if(functionQuantity.length()>100)
					throw new Exception();
//				new Integer(functionQuantity);
			}
			catch (Exception e) {
				unupdateFunction();
				errorMsgList.add(MatContext.get().getMessageDelegate().getQuantityNumMessage());
				return false;
			}
		}

		return true;
	}

	private boolean alert(String msg) {
		errorMsgList.add(MatContext.get().getMessageDelegate().getIdentityMessage(msg));
		return false;
	}

	protected void initSingleMode() {
		if (singleSubMode == SINGLE_SUBMODE.PHRASE)
			if (function != null && !function.trim().isEmpty())
				mode = MODE.FUNCTION;
			else
				mode = MODE.SINGLE;			
		initSingleTable();    
		redraw(view.getPropertyEditor(), grid);
	}

	protected void changeToSingleMode() {
		//US175 1. change name 2. remove AND or OR 3. remove RHS 4. save
		changedName = nameTextBox.getText().trim();
		
		description = descriptionTextBox.getText().trim();
		initSingleMode();
		removeAttributesFromPhrase(phrase2);//Removing the attributes from the second phrase element. Since SingleMode will not have second phrase so zeroout the attributes.
	}

	protected void initLogicalMode() {	
		mode = MODE.LOGICAL;
		//US175 1. change name 2. change to AND or OR 3. add RHS 4. save
		if(descriptionTextBox != null){
			description = descriptionTextBox.getText().trim();
		}
		initTable();
		insertAddAdditionalPhraseButton(grid, ADD_BUTTON_ROW, ADD_REMOVE_BUTTON_COL);
		redraw(view.getPropertyEditor(), grid);
	}

	protected Button insertAddAdditionalPhraseButton(FlexTable grid, int row, int col) {
		Button button = new Button();
		MatContext.get().setAriaHidden(button, "false");
		button.setText("Add");	
		button.setEnabled(view.isEditable());
		button.setTitle("Add a phrase at the end.");
		button.addClickHandler(addLogicalHandler);
		grid.setWidget(row, col, button);
		return button;
	}

	protected void addAddComparisonButton(FlexTable grid, int row, int col) {
		Button button = new Button();
		MatContext.get().setAriaHidden(button, "false");
		button.setText("Add");
		button.setTitle("Add Comparison");
		button.setEnabled(view.isEditable());
		button.addClickHandler(addComparisonHandler);
		grid.setWidget(row, col, button);
	}
	
	 //US 171, Removing Add comparison button when the relative associations operator has been used.
	protected void removeAddComparisonButton(FlexTable grid,int row, int col){
		try{
			if(grid.isCellPresent(row, col)){
				grid.removeCell(row, col);
			}
			if(grid.isCellPresent(SAVE_BUTTON_ROW, COMPARISON_COL)){
				grid.removeCell(grid.getRowCount() - 1, CONDITION_COL);
				emptyOutComparisionOperators();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			moveFocusToPropertyEditor();
		}
	}	
	
	protected void changeToLogicalMode() {
		//US?? 1. change name 2. change to AND or OR 3. add RHS 4. save
//		identity = nameTextBox.getText().trim();
		changedName = nameTextBox.getText().trim();
		
		description = descriptionTextBox.getText().trim();
		initLogicalMode();
	}

	protected void initComparisonMode() {
		mode = MODE.COMPARISON;			
		initTable();
		if(!getRelAssociationsList().contains(condition)){//US 171 Add the Add comparison button only for Relative timing
			addAddComparisonButton(grid, ADD_BUTTON_ROW, ADD_REMOVE_BUTTON_COL);
		}
		redraw(view.getPropertyEditor(), grid);	
	}

	protected void changeToComparisonMode() {
		//US?? 1. change name 2. change to SBOD 3. add RHS 4. save
		//identity = nameTextBox.getText().trim();
		changedName = nameTextBox.getText().trim();
		description = descriptionTextBox.getText().trim();
		initComparisonMode();	
	}

	private final String phrase1ID = "Phrase 1";
	private final String phrase2ID = "Phrase 2";
	private final String conditionID = "Condition";
	private final String quantityID = "Quantity";
	private final String unitID = "Unit";
	
	private final String conditionOperatorID = "Condition Operator";
	private final String conditionQuantityID = "Condition Quantity";
	private final String conditionUnitID = "Condition Unit";
	
	protected void initSingleTable() {
		if (view.getPropertyEditor().getWidgetCount() > 1)
			view.getPropertyEditor().remove(1);
		grid = new FlexTable();

		addNamePanel();
		addDescriptionPanel();
		addFunctionPanel();

		addLabel(grid, "Phrase Element", PHRASE_LABEL_ROW, PHRASE1_COL, phrase1ID);
		if (singleSubMode == SINGLE_SUBMODE.PHRASE) {
			addLabel(grid, "Condition", PHRASE_LABEL_ROW, CONDITION_COL, conditionID);
			addLabel(grid, "Phrase Element", PHRASE_LABEL_ROW, PHRASE2_COL, phrase2ID);
		}
		else {
			addLabel(grid, "Condition", PHRASE_LABEL_ROW, CONDITION_COL, conditionID);
			addLabel(grid, "Quantity", PHRASE_LABEL_ROW, SIMPLE_COMPARISON_QUANTITY_COL, quantityID);
			addLabel(grid, "Unit", PHRASE_LABEL_ROW, SIMPLE_COMPARISON_UNIT_COL, unitID);
		}
		addPhraseRadios(1, grid);
		addPhrase1();

		if (singleSubMode == SINGLE_SUBMODE.PHRASE) {
			addSimplePhraseConditions();
			//US202 comment
			//if (phrase2 != null){
				addPhraseRadios(2, grid);
				addPhrase2();
				
			//}
			
		}
		else
			addComparisonPhraseConditions();

		addSaveCancelPhrase(grid, SAVE_BUTTON_ROW);
		toggleSimplePhrase2AndComparisonButton = new Button();
		toggleSimplePhrase2AndComparisonButton.setEnabled(view.isEditable());	
		grid.setWidget(SAVE_BUTTON_ROW, TOGGLE_BUTTON_COL, toggleSimplePhrase2AndComparisonButton);

		if (singleSubMode == SINGLE_SUBMODE.PHRASE)
			usePhrase();
		else {
			addSimpleComparison();
			grid.setWidget(SAVE_BUTTON_ROW, TOGGLE_BUTTON_COL, toggleSimplePhrase2AndComparisonButton);
			useComparison();
		}
	}

	private void addPhraseRadios(int phraseIndex, FlexTable grid){
		MATRadioButton qdmElementsRadio = phraseIndex == 1 ? qdmElementsRadio1 : qdmElementsRadio2;
		MATRadioButton phrasesRadio = phraseIndex == 1 ? phrasesRadio1 : phrasesRadio2;
		Phrase phrase = phraseIndex == 1 ? phrase1 : phrase2;
		int col = phraseIndex == 1 ? PHRASE1_COL : PHRASE2_COL;
		
		if(isMeasurePhrase(phrase))
			phrasesRadio.setValue(true);
		else
			qdmElementsRadio.setValue(true);
		HorizontalPanel hp = new HorizontalPanel();
		qdmElementsRadio.setStylePrimaryName("padLeft5px");
		phrasesRadio.setStylePrimaryName("padLeft5px");
		hp.add(qdmElementsRadio);
		hp.add(phrasesRadio);
		phrase.setQdmElementsRadio(qdmElementsRadio);
		phrase.setPhrasesRadio(phrasesRadio);
		grid.setWidget(PHRASE_RADIO_ROW, col, hp);
		
		qdmElementsRadio.addClickHandler(phraseValueClickHandler);
		phrasesRadio.addClickHandler(phraseValueClickHandler);
		qdmElementsRadio.addClickHandler(phraseValueClickHandler);
		phrasesRadio.addClickHandler(phraseValueClickHandler);
		
		//US 430. Disable the radios for read only mode
		phrasesRadio.setEnabled(view.isEditable());
		qdmElementsRadio.setEnabled(view.isEditable());
	}
	
	private boolean isMeasurePhrase(Phrase phrase){
		StringUtility su = new StringUtility();
		if(su.isEmptyOrNull(phrase.getText()) || qdmElements.contains(phrase.getText()))
			return false;
		else return phraseElements.contains(phrase.getText());
	}
	
	
	class PhraseClickHandler implements ClickHandler{
		@Override
		public void onClick(ClickEvent event){
			MatContext.get().clearDVIMessages();
			
				if(event.getSource().equals(qdmElementsRadio1)){
					removeAttributesFromPhrase(phrase1);
					//updateListBoxStripAttrib(phrase1, qdmElements.toArray(new String[qdmElements.size()]));
					updateListBox(phrase1, qdmElements.toArray(new String[qdmElements.size()]));
					
					phrase1.getLink().setVisible(true);
					phrase1.getLink().getElement().setAttribute("role", "alert");
				}else if(event.getSource().equals(phrasesRadio1)){
					removeAttributesFromPhrase(phrase1);
					updateListBox(phrase1, phraseElements.toArray(new String[phraseElements.size()]));
					phrase1.getLink().setVisible(false);
					phrase1.getLink().getElement().setAttribute("role", "alert");
				}else if(event.getSource().equals(qdmElementsRadio2)){
					removeAttributesFromPhrase(phrase2);
					List<String> tempQdmElements = new ArrayList<String>();
					tempQdmElements.addAll(qdmElements);
					tempQdmElements.add(0,NONE);
					//updateListBoxStripAttrib(phrase2, tempQdmElements.toArray(new String[tempQdmElements.size()]));
					updateListBox(phrase2, tempQdmElements.toArray(new String[tempQdmElements.size()]));
					
					phrase2.getLink().setVisible(true);
					phrase2.getLink().getElement().setAttribute("role", "alert");
				}else if(event.getSource().equals(phrasesRadio2)){
					removeAttributesFromPhrase(phrase2);
					List<String> tempPhraseElements = new ArrayList<String>();
					tempPhraseElements.addAll(phraseElements);
					tempPhraseElements.add(0, NONE);
					updateListBox(phrase2, tempPhraseElements.toArray(new String[tempPhraseElements.size()]));
					phrase2.getLink().setVisible(false);
					phrase2.getLink().getElement().setAttribute("role", "alert");
				}
				setLinkVisibility();
				view.firePropertyEditorAlert();
		}
	}

	/**
	 * @param phrase
	 */
	private void removeAttributesFromPhrase(Phrase phrase) {
		phrase.getAttributes().removeAll(phrase.getAttributes());
		phrase.setAttributesLinkCaption();
	}
	PhraseClickHandler phraseValueClickHandler = new PhraseClickHandler();
	
	@Override
	protected void initTable() {
		if (view.getPropertyEditor().getWidgetCount() > 1)
			view.getPropertyEditor().remove(1);
		grid = new FlexTable();

		addNamePanel();
		addDescriptionPanel();
		addFunctionPanel();

		addLabel(grid, "Phrase Element", PHRASE_LABEL_ROW, PHRASE1_COL, phrase1ID);
		addLabel(grid, "Condition", PHRASE_LABEL_ROW, CONDITION_COL, conditionID);
		addLabel(grid, "Phrase Element", PHRASE_LABEL_ROW, PHRASE2_COL, phrase2ID);
		addPhraseRadios(1, grid);
		addPhrase1();//addid
		addConditions();
		addPhraseRadios(2, grid);
		addPhrase2();//addid

		addSaveCancelPhrase(grid, SAVE_BUTTON_ROW);
		moveFocusToPropertyEditor();
		
	}

	protected ListBox addPhrase(final Phrase phrase, int col, boolean setQDMs) {
		return addPhrase(phrase, PHRASE_LIST_ROW, col, setQDMs);
	}

	protected ListBox addPhrase(final Phrase phrase, int row, int col, boolean setQDMs) {
		FlowPanel phrasePanel = new FlowPanel();

		ListBox listBox = phrase.getListBox();
		listBox.setWidth("25em");
		
		if(setQDMs)
			addAndSortListToListBox(phrasePanel, listBox, row, col, new ArrayList(qdmElements));
		else
			addAndSortListToListBox(phrasePanel, listBox, row, col, new ArrayList(phraseElements));
		
		if (phrase.getText() != null)
			phrase.setText(phrase.getText());
		else
			phrase.setText();

		Anchor link = phrase.getLink();
		link.addStyleName("link");
		link.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				/*EDIT ME*/
				//private MATRadioButton phrasesRadio1 = new MATRadioButton("phrase1ElementType", "Measure Phrases");
				//private MATRadioButton qdmElementsRadio1 = new MATRadioButton("phrase1ElementType", "QDM Elements");
				//private MATRadioButton phrasesRadio2 = new MATRadioButton("phrase2ElementType", "Measure Phrases");
				//private MATRadioButton qdmElementsRadio2 = new MATRadioButton("phrase2ElementType", "QDM Elements");
				
				MatContext.get().clearDVIMessages();
			
				String selectedText = phrase.getListBox().getValue() == null ? "" : 
					phrase.getListBox().getValue().trim();
				
				if (qdmElements.contains(selectedText)) {
					view.hidePropertyEditor();
					view.showAttributeEditor(phrase);
					moveFocusToPropertyEditor();
				}
				else{
					view.getPropEditErrorMessages().setMessage(MatContext.get().getMessageDelegate().getAttributesNotAllowedMessage());
				}
			}
		});
		phrase.setAttributesLinkCaption();

		grid.setWidget(row, col, phrasePanel);
		grid.setWidget(row+1, col, link);
		MatContext.get().setAriaHidden(grid.getWidget(row, col),"false");
		MatContext.get().setAriaHidden(grid.getWidget(row+1, col),"false");
		
		
		int i = listBox.getSelectedIndex();
		if(i>=0){
		String s = listBox.getItemText(i);
		boolean clearAttrib = s.startsWith(ConstantMessages.MEASUREMENT_PERIOD);
		clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_START_DATE);
		clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_END_DATE);	
		if(clearAttrib)
				link.setVisible(false);
		else 
			link.setVisible(true);
		}
		
		
     	return listBox;
	}

	protected ListBoxMVP addPhrase1() {
		phrase1.getListBox().addChangeHandler(phraseChangeHandler);
		addPhrase(phrase1, PHRASE1_COL, qdmElementsRadio1.getValue());
		DOM.setElementAttribute(phrase1.getListBox().getElement(), "id", phrase1ID);
		
		
		int i = phrase1.getListBox().getSelectedIndex();
		if(i>=0){
			String s = phrase1.getListBox().getItemText(i);
			boolean clearAttrib = s.startsWith(ConstantMessages.MEASUREMENT_PERIOD);
			clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_START_DATE);
			clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_END_DATE);	
			if(clearAttrib)
				phrase1.getLink().setVisible(false);
			else{
				if(qdmElementsRadio1.isEnabled())
					phrase1.getLink().setVisible(true);
			}
		}
		
		return phrase1.getListBox();
	}

	protected ListBox addPhrase2() {
		String text = phrase2 == null || phrase2.getText() == null ? "" : phrase2.getText();
		ListBox listBox = addPhrase(phrase2, PHRASE2_COL, qdmElementsRadio2.getValue());
		DOM.setElementAttribute(listBox.getElement(), "id", phrase2ID);
		listBox.insertItem(NONE, 0);
		if (text.trim().isEmpty()) {
			listBox.setSelectedIndex(0);
			phrase2.setText();
		}
		listBox.addChangeHandler(phraseChangeHandler);
		
		
		
		int i = phrase2.getListBox().getSelectedIndex();
		String s = phrase2.getListBox().getItemText(i);
		boolean clearAttrib = s.startsWith(ConstantMessages.MEASUREMENT_PERIOD);
		clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_START_DATE);
		clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_END_DATE);	
		if(clearAttrib)
			phrase2.getLink().setVisible(false);
		else{
			if(qdmElementsRadio2.isEnabled())
				phrase2.getLink().setVisible(true);
		}
		
		return listBox;	
	}

	protected void addConditions() {
		FlowPanel panel = new FlowPanel();
		createConditionListBox();
		addListBox(panel, conditionListBox, 0, 0, Arrays.asList(usePhraseConditionOpr));
		if (condition != null)
			setItemText(conditionListBox, condition);
		conditionListBox.addBlurHandler(conditionBlurHandler);
		conditionListBox.setEnabled(view.isEditable());
		grid.setWidget(PHRASE_LIST_ROW, CONDITION_COL, panel);
	}

	protected void addSimplePhraseConditions() {
		VerticalPanel panel = new VerticalPanel();

		List<String> simpleConditionList = new ArrayList<String>();
		simpleConditionList.add(NONE);
		simpleConditionList.addAll(Arrays.asList(usePhraseConditionOpr));

		addSimpleConditions(panel, simpleConditionList);
	}

	protected void addComparisonPhraseConditions() {
		VerticalPanel panel = new VerticalPanel();

		List<String> simpleConditionList = new ArrayList<String>();
		simpleConditionList.addAll(Arrays.asList(comparisonOperators));

		addSimpleConditions(panel, simpleConditionList);
	}

	private void addSimpleConditions(VerticalPanel panel,
			List<String> simpleConditionList) {
		createConditionListBox();
		addListBox(panel, conditionListBox, 0, 0, simpleConditionList);
		conditionListBox.setSelectedIndex(0);
		conditionListBox.addBlurHandler(conditionBlurHandler);
		conditionListBox.setEnabled(view.isEditable());
		grid.setWidget(PHRASE_LIST_ROW, CONDITION_COL, panel);
	}

	protected void addNamePanel() {
		VerticalPanel namePanel = new VerticalPanel();
		nameTextBox = new TextBox();
		nameTextBox.addFocusHandler(
				new SimpleStatementFocusHandler());

		Widget nameLabel = LabelBuilder.buildRequiredLabel(nameTextBox,"Name\t\t");
		nameLabel.addStyleName("gridLabel");	
		namePanel.add(nameLabel);
		
		nameTextBox.setMaxLength(100);
		nameTextBox.setWidth("50em");
		//US175 1. change name 2. change to AND or OR, or to Comparison, or no AND or Or or Comparison 3. save
		/*
		 * set name text and text title as follows:
		 * 1) nameChanged if nameChanged is not empty 
		 * 2) identity if identity is not empty
		 * 3) empty
		 */
		String nameTxt = (changedName != null && !changedName.equals(DiagramObject.EMPTY)) ? changedName :
			(identity == null || identity.equals(DiagramObject.EMPTY)) ? "" : identity;
		nameTextBox.setText(nameTxt);
		nameTextBox.setTitle(nameTxt);
		
		nameTextBox.setEnabled(view.isEditable());
		namePanel.add(nameTextBox);
		grid.setWidget(NAME_PANEL_ROW, NAME_PANEL_COL, namePanel);
		grid.getFlexCellFormatter().setColSpan(NAME_PANEL_ROW, NAME_PANEL_COL, 3);
	}

	private void addDescriptionPanel() {
		VerticalPanel descriptionPanel = new VerticalPanel();

		descriptionTextBox = new TextBox();
		descriptionTextBox.addFocusHandler(new SimpleStatementFocusHandler());
		
		Widget descriptionLabel = LabelBuilder.buildLabel(descriptionTextBox,"Description\t\t");
		descriptionLabel.addStyleName("gridLabel");	
		descriptionPanel.add(descriptionLabel);

		descriptionTextBox.setMaxLength(100);
		descriptionTextBox.setWidth("50em");
		descriptionTextBox.setText(description);
		descriptionTextBox.setTitle("Description for this simple expression" + ((identity == null) ? " is blank" : identity));
		descriptionTextBox.setEnabled(view.isEditable());
		descriptionPanel.add(descriptionTextBox);

		inProgressCompleteListBox = new ListBox();
		inProgressCompleteListBox.addFocusHandler(new SimpleStatementFocusHandler());
		/*
		 * MAT-286 - Removing Status Field from Measure Phrase. 01/2013		
  		descriptionPanel.add(LabelBuilder.buildRequiredLabel(inProgressCompleteListBox, "Status"));
		addListBox(descriptionPanel, inProgressCompleteListBox, statusArray);
		 */
		inProgressCompleteListBox.setSelectedIndex(1);//default select inProgress for New Phrase.
		inProgressCompleteListBox.setEnabled(view.isEditable());
		grid.setWidget(DESCRIPTION_PANEL_ROW, DESCRIPTION_PANEL_COL, descriptionPanel);
		grid.getFlexCellFormatter().setColSpan(DESCRIPTION_PANEL_ROW, DESCRIPTION_PANEL_COL, 4);
	}

	protected void addFunctionPanel() {
		FlexTable functionGrid = new FlexTable();

		functionListBox = new ListBox();
		functionListBox.addFocusHandler(new SimpleStatementFocusHandler());
		Widget functionLabel = LabelBuilder.buildLabel(functionListBox,"Function");
		functionLabel.addStyleName("gridLabel");
		functionGrid.setWidget(0, 0, functionLabel);
		addListBox(functionGrid, functionListBox,1, 0, functions);
		functionListBox.setSelectedIndex((functions.indexOf(function)>= -1 ?functions.indexOf(function):0));
		functionListBox.addChangeHandler(functionChangeHandler);
		functionListBox.setEnabled(view.isEditable());

		functionOperatorListBox = new ListBox();
		functionOperatorListBox.addFocusHandler(new SimpleStatementFocusHandler());
		Widget comparisonLabel = LabelBuilder.buildLabel(functionOperatorListBox,"Operator");
		comparisonLabel.setTitle("Operator");
		comparisonLabel.addStyleName("gridLabel");
		functionGrid.setWidget(0, 1, comparisonLabel);
		addListBox(functionGrid, functionOperatorListBox, 1, 1, comparisonOperators);
		functionOperatorListBox.setEnabled(view.isEditable());
		functionOperatorListBox.insertItem(NONE, 0);
		List<String> temp = Arrays.asList(comparisonOperators);
		functionOperatorListBox.setSelectedIndex(
				(temp.indexOf(functionOperator) >= -1 ? 
						(temp.indexOf(functionOperator)+1):0));
		functionOperatorListBox.addChangeHandler(functionChangeHandler);

		functionQuantityTextBox = new TextBox();
		functionQuantityTextBox.setMaxLength(100);
		functionQuantityTextBox.addFocusHandler(new SimpleStatementFocusHandler());
		Widget quantityLabel = LabelBuilder.buildLabel(functionQuantityTextBox,"Quantity");
		quantityLabel.setTitle("Quantity");
		quantityLabel.addStyleName("gridLabel");
		functionGrid.setWidget(0, 2, quantityLabel);
		addTextBox(functionGrid, functionQuantityTextBox, "FunctionComparisonQuantity", 1, 2);
		functionQuantityTextBox.setText(functionQuantity);
		functionQuantityTextBox.addChangeHandler(functionChangeHandler);
		functionQuantityTextBox.setEnabled(view.isEditable());
		//Fix for US 150 (Adding function unit dropdown as part of US 150).
		functionUnitListBox = new ListBox();
		functionUnitListBox.addFocusHandler(new SimpleStatementFocusHandler());
		Widget functionUnitsLabel = LabelBuilder.buildLabel(functionUnitListBox, "Unit");
		functionUnitsLabel.setTitle("Unit");
		functionUnitsLabel.addStyleName("gridLabel");
		functionGrid.setWidget(0, 3, functionUnitsLabel);
		addListBox(functionGrid, functionUnitListBox,1,3,functionUnits);
		functionUnitListBox.addChangeHandler(functionChangeHandler);
		functionUnitListBox.setEnabled(view.isEditable());
		functionUnitListBox.insertItem(NONE,0);
		List<String> unitsList = Arrays.asList(functionUnits);
		functionUnitListBox.setSelectedIndex(
				(unitsList.indexOf(functionUnit) >= -1 ? 
						(unitsList.indexOf(functionUnit)+1):0));
		grid.setWidget(FUNCTION_PANEL_ROW, FUNCTION_PANEL_COL, functionGrid);
		grid.getFlexCellFormatter().setColSpan(FUNCTION_PANEL_ROW, FUNCTION_PANEL_COL, 3);	
	}

	ClickHandler addLogicalHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			MatContext.get().clearDVIMessages();
			addAdditionalPhrase();
		}
	};

	
	private void setSecondLinkVisiblity(){
		boolean visibility = true;
		 if(phrasesRadio2.getValue())
			 visibility = false;
		 if(qdmElementsRadio2.getValue()){
			 int i = phrase2.getListBox().getSelectedIndex();
			 if(i>=0){
				 String s = phrase2.getListBox().getItemText(i);
				 boolean clearAttrib = s.startsWith(ConstantMessages.MEASUREMENT_PERIOD);
				 clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_START_DATE);
				 clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_END_DATE);	
				 if(clearAttrib)
					visibility = false;
			 }
		 }
		phrase2.getLink().setVisible(visibility);
	}
	
	private void setFirstLinkVisiblity(){
		boolean visibility = true;
		 if(phrasesRadio1.getValue())
			 visibility = false;
		 if(qdmElementsRadio1.getValue()){
			 int i = phrase1.getListBox().getSelectedIndex();
			 if(i>=0){
				 String s = phrase1.getListBox().getItemText(i);
				 boolean clearAttrib = s.startsWith(ConstantMessages.MEASUREMENT_PERIOD);
				 clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_START_DATE);
				 clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_END_DATE);	
				 if(clearAttrib)
					visibility = false;
			 }
		 }
		phrase1.getLink().setVisible(visibility);
	}
	
	
	private void setAdditionalLinkVisiblity(){
		
		for(int j=0; j<additionalPhraseList.size();j++){
		
			additionalPhraseList.get(j).getLink().setVisible(true);
			boolean visibility = true;
			
			if(additionalPhraseList.get(j).getPhrasesRadio().getValue())
				visibility = false;
			if(additionalPhraseList.get(j).getQdmElementsRadio().getValue()){
				visibility = true;
				int i = additionalPhraseList.get(j).getListBox().getSelectedIndex();
				if(i>=0){
					String s = additionalPhraseList.get(j).getItemText(i);
					boolean clearAttrib = s.startsWith(ConstantMessages.MEASUREMENT_PERIOD);
					clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_START_DATE);
					clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_END_DATE);	
					if(clearAttrib)
						visibility = false;
				}
			}
			additionalPhraseList.get(j).getLink().setVisible(visibility);
		}
	}
	
	public void setLinkVisibility(){
		setFirstLinkVisiblity();	
		setSecondLinkVisiblity();
		setAdditionalLinkVisiblity();	
	}
	
	protected void addAdditionalPhrase() {
		additionalPhraseList.add(new Phrase(appController));
		initLogicalMode();
		drawAdditionalPhrases();
		setLinkVisibility();
	}
	
	ClickHandler removeLogicalHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			MatContext.get().clearDVIMessages();
			removeAdditionalPhrase(event);
		}
	};

	protected void removeAdditionalPhrase(ClickEvent event) {
		final Button removeButton = (Button)event.getSource();
		try {
			String title = removeButton.getTitle();
			int index = (new Integer((title.split(" "))[3]));
			removeAdditionalPhrase(index);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void removeAdditionalPhrase(int index) {
		additionalPhraseList.remove(index);
		initLogicalMode();
		drawAdditionalPhrases();
		setLinkVisibility();
	}

	protected void drawAdditionalPhrases() {
		int row = FIRST_ADDITIONAL_PHRASE_ROW;
		int size = additionalPhraseList.size();
		for (int i = 0; i < size; ++i) {
			addAdditionalPhraseRadios(row, grid, i);
			Phrase phrase = additionalPhraseList.get(i);
			boolean isQdm = !isMeasurePhrase(phrase);
			addPhrase(phrase, row+1, PHRASE2_COL, isQdm);
			phrase.getListBox().addChangeHandler(additionalPhraseChangeHandler);
			addLabel(grid, condition, row+1, CONDITION_COL);
			insertRemoveAdditionalPhraseButton(i, grid, row+1, ADD_REMOVE_BUTTON_COL);
			row = row+3;
		}
	}

	private void addAdditionalPhraseRadios(int row, FlexTable grid, final int i){
		final MATRadioButton qdmElementsRadio = new MATRadioButton("phrase"+row+"ElementType", "QDM Elements");
		final MATRadioButton phrasesRadio = new MATRadioButton("phrase"+row+"ElementType", "Measure Phrases");
		additionalPhraseList.get(i).setQdmElementsRadio(qdmElementsRadio);
		additionalPhraseList.get(i).setPhrasesRadio(phrasesRadio);
		
		
		int col = PHRASE2_COL;
		
		if(isMeasurePhrase(additionalPhraseList.get(i))){
			phrasesRadio.setValue(true);
			additionalPhraseList.get(i).getLink().setVisible(false);
		}else{
			qdmElementsRadio.setValue(true);
			ListBox listBox = additionalPhraseList.get(i).getListBox();
			int j = listBox.getSelectedIndex();
			if(j>=0){
				String s = listBox.getItemText(j);
				boolean clearAttrib = s.startsWith(ConstantMessages.MEASUREMENT_PERIOD);
				clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_START_DATE);
				clearAttrib = clearAttrib || s.startsWith(ConstantMessages.MEASUREMENT_END_DATE);	
				if(clearAttrib)
					additionalPhraseList.get(i).getLink().setVisible(false);
			}
			
		}
		HorizontalPanel hp = new HorizontalPanel();
		qdmElementsRadio.setStylePrimaryName("padLeft5px");
		phrasesRadio.setStylePrimaryName("padLeft5px");
		hp.add(qdmElementsRadio);
		hp.add(phrasesRadio);
		grid.setWidget(row, col, hp);
		ClickHandler additionalPhraseClickHandler =  new ClickHandler(){
			@Override
			public void onClick(ClickEvent event){
				MatContext.get().clearDVIMessages();
				if(event.getSource().equals(qdmElementsRadio)){
					removeAttributesFromPhrase(additionalPhraseList.get(i));
					updateListBoxStripAttrib(additionalPhraseList.get(i), qdmElements.toArray(new String[qdmElements.size()]));
				}else if(event.getSource().equals(phrasesRadio)){
					removeAttributesFromPhrase(additionalPhraseList.get(i));
					additionalPhraseList.get(i).getLink().setVisible(false);
					updateListBox(additionalPhraseList.get(i), phraseElements.toArray(new String[phraseElements.size()]));
				}
				setLinkVisibility();
				view.firePropertyEditorAlert();
			}
		};
		qdmElementsRadio.addClickHandler(additionalPhraseClickHandler);
		phrasesRadio.addClickHandler(additionalPhraseClickHandler);
	}
	private void updateListBoxStripAttrib(Phrase phrase, String[] strArr){
		//ArrayList<String> filtered = new ArrayList<String>(); 
		
		int count=0;
		for(String str : strArr){
			String tmp = MatContext.get().getTextSansOid(str);
			if(!tmp.trim().endsWith(": attribute")){
				count++;
			}
		}
		String[] filtered = new String[count];
		
		count=0;
		for(String str : strArr){
			String tmp = MatContext.get().getTextSansOid(str);
			if(!tmp.trim().endsWith(": attribute")){
				filtered[count]=str;
				count++;
			}
		}
		updateListBox(phrase, filtered);
	}
	
	private void updateListBox(Phrase phrase, String[] strArr){
		phrase.getListBox().clear();
		addAndSortElementsToListBox(phrase.getListBox(), strArr);
		phrase.setText();
		phrase.getListBox().getElement().setAttribute("role", "alert");
		phrase.getListBox().setFocus(true);
	}
	
	protected Button insertRemoveAdditionalPhraseButton(int i, FlexTable grid, int row, int col) {
		Button button = new Button();
		button.setText("Remove");	
		button.setTitle("Remove additional phrase " + i);
		button.setEnabled(view.isEditable());
		button.addClickHandler(removeLogicalHandler);
		grid.setWidget(row, col, button);
		MatContext.get().setAriaHidden(grid.getWidget(row, col),"false");
		return button;
	}

	ChangeHandler phraseChangeHandler = new ChangeHandler() {
		@Override
		public void onChange(ChangeEvent event) {
			ListBox listBox = (ListBox)(event.getSource());
			int selectedIndex = listBox.getSelectedIndex();
			if (getItemText(listBox, selectedIndex).startsWith("-"))
				listBox.setSelectedIndex(--selectedIndex);
			Phrase phrase = (listBox == phrase1.getListBox()) ? phrase1 : phrase2;
			//Need to null out the value of the attribute whenever the changes in the listbox has been made.
			removeAttributesFromPhrase(phrase);
			
			String text = listBox.getValue(selectedIndex);
			phrase.setText(text);
			listBox.setTitle(text);
		}	
	};

	ChangeHandler additionalPhraseChangeHandler = new ChangeHandler() {
		@Override
		public void onChange(ChangeEvent event) {
			ListBox listBox = (ListBox)(event.getSource());
			int selectedIndex = listBox.getSelectedIndex();
			if (getItemText(listBox, selectedIndex).startsWith("-"))
				listBox.setSelectedIndex(--selectedIndex);
			for (int i = 0; i < additionalPhraseList.size(); ++i) {
				Phrase phrase = additionalPhraseList.get(i);
				if (listBox == phrase.listBox) {
					//Need to null out the value of the attribute whenever the changes in the listbox has been made.
					removeAttributesFromPhrase(phrase);
					
					String text = listBox.getValue(selectedIndex);
					phrase.setText(text);
					listBox.setTitle(text);
					additionalPhraseList.set(i, phrase);
					return;
				}
			}
			throw new IllegalArgumentException("additionalPhraseChangeHandler - can't find Phrase in additionalPhraseList!");
		}	
	};

	private BlurHandler conditionBlurHandler = new BlurHandler(){

		@Override
		public void onBlur(BlurEvent event) {
			doConditionBlurBehavior();
		}
		
	};
	private void doConditionBlurBehavior(){
		condition = getSelectedCondition();
		if (condition.equals("AND") || condition.equals("OR")) {
			if (mode == MODE.LOGICAL){
				refreshLogicalConditions();
			}else{
				changeToLogicalMode();
			}
		}
		else if (timingConditions.contains(condition)) {
			if (mode != MODE.COMPARISON) {
				changeToComparisonMode();
			}else if(getRelAssociationsList().contains(condition)){//US 171, Removing Add button when relative Associations has been selected.
				removeAddComparisonButton(grid, ADD_BUTTON_ROW, ADD_REMOVE_BUTTON_COL);
			}else if(!getRelAssociationsList().contains(condition)){
				addAddComparisonButton(grid, ADD_BUTTON_ROW, ADD_REMOVE_BUTTON_COL);
			}
		}
		else {
			if (mode != MODE.SINGLE){
				changeToSingleMode();
			}
		}
	}

	ChangeHandler functionChangeHandler = new ChangeHandler() {
		@Override
		public void onChange(ChangeEvent event) {
			function = getSelectedFunction();
			functionOperator = getSelectedFunctionOperator();
			functionUnit = getSelectedFunctionUnit();
			//if conditions are NOT blanks, then the mode has to be a comparison, so don't bother 
			//changing the mode in that case.
			if( condition == null || condition.trim().isEmpty()) {
				if (function != null || (functionOperator != null && !functionOperator.trim().isEmpty())) {

					if (!function.trim().isEmpty()) {
						mode = MODE.FUNCTION;
					} else {
						mode = MODE.SINGLE;
					}
				}
			}
			functionQuantity = functionQuantityTextBox.getText();
		}

		private String getSelectedFunctionOperator() {
			return getItemText(functionOperatorListBox);
		}

		private String getSelectedFunction() {
			return getItemText(functionListBox);
		}
		
		//Introduce unit for function
		private String getSelectedFunctionUnit(){
			return getItemText(functionUnitListBox);
		}
	};
	
	

	protected void refreshLogicalConditions() {
		for (int i = 0, row = FIRST_ADDITIONAL_PHRASE_ROW+1; i < additionalPhraseList.size(); ++i) {
			Label label = (Label)(grid.getWidget(row, CONDITION_COL));
			label.setText(condition);
			label.setTitle(condition);
			MatContext.get().setAriaHidden(label, "false");
			row = row + 3;
		}
	}

	protected ClickHandler addComparisonHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			MatContext.get().clearDVIMessages();
			addComparison();
		}
	};

	protected void addSimpleComparison() {
		addListBox(grid, conditionalOperatorListBox = new ListBox(), PHRASE_LIST_ROW, CONDITION_COL, useCompOperators);
		
		conditionalOperatorListBox.addFocusHandler(new SimpleStatementFocusHandler());
		
		setSelectedIndex(conditionalOperatorListBox, conditionalOperator, useCompOperators);
		
		grid.getCellFormatter().setVerticalAlignment(PHRASE_LIST_ROW, CONDITION_COL, HasVerticalAlignment.ALIGN_TOP);
		conditionalOperatorListBox.setEnabled(view.isEditable());

		quantityTextBox = new TextBox();
		quantityTextBox.setMaxLength(100);
		addTextBox(grid, quantityTextBox, "ComparisonQuantity", PHRASE_LIST_ROW, SIMPLE_COMPARISON_QUANTITY_COL);
		
		quantityTextBox.addFocusHandler(new SimpleStatementFocusHandler());
		grid.getCellFormatter().setVerticalAlignment(PHRASE_LIST_ROW, SIMPLE_COMPARISON_QUANTITY_COL, HasVerticalAlignment.ALIGN_TOP);
		quantityTextBox.setEnabled(view.isEditable());

		unitsListBox = new ListBox();
		unitsListBox.addFocusHandler(new SimpleStatementFocusHandler());
		
		DOM.setElementAttribute(unitsListBox.getElement(), "id", unitID);
		unitsListBox.setEnabled(view.isEditable());
		addListBox(grid, unitsListBox, PHRASE_LIST_ROW, SIMPLE_COMPARISON_UNIT_COL, comparisonUnits);
		grid.getCellFormatter().setVerticalAlignment(PHRASE_LIST_ROW, SIMPLE_COMPARISON_UNIT_COL, HasVerticalAlignment.ALIGN_TOP);
		setSelectedIndex(unitsListBox, unit, comparisonUnits);
		unitsListBox.setEnabled(view.isEditable());
	}

	private void setSelectedIndex(ListBox listbox, String value, String[] items) {
		int index = -1;
		if (value == null || value.trim().equals("")){
			index = Arrays.asList(items).indexOf(ConstantMessages.COMPARISON_UNIT_DEFAULT);
			listbox.setSelectedIndex(index);
		}else {
			index = Arrays.asList(items).indexOf(value);
			if (index < 0)
				index = 0;
			listbox.setSelectedIndex(index);
		}
	}

	protected void addComparison() {
		int row = SAVE_BUTTON_ROW;

		try {
			grid.removeCell(PHRASE_LIST_ROW, ADD_REMOVE_BUTTON_COL);
			addRemoveComparisonButton(grid, PHRASE_LIST_ROW, ADD_REMOVE_BUTTON_COL);

			FlexTable comparisonGrid = new FlexTable();
			addLabel(comparisonGrid, "Operator", 0, 0, conditionOperatorID);
			addLabel(comparisonGrid, "Quantity", 0, 1, conditionQuantityID);
			addLabel(comparisonGrid, "Unit", 0, 2, conditionUnitID);
			
			conditionalOperatorListBox = new ListBox();
			conditionalOperatorListBox.addFocusHandler(
					new FocusHandler() {
						@Override
						public void onFocus(FocusEvent event) {
							MatContext.get().clearDVIMessages();	
						}
					});

			DOM.setElementAttribute(conditionalOperatorListBox.getElement(), "id", conditionOperatorID);
			addListBox(comparisonGrid, conditionalOperatorListBox, 1, 0, comparisonOperators);
			quantityTextBox = new TextBox();
			quantityTextBox.setMaxLength(100);
			quantityTextBox.addFocusHandler(new SimpleStatementFocusHandler());
			DOM.setElementAttribute(quantityTextBox.getElement(), "id", conditionQuantityID);
			addTextBox(comparisonGrid, quantityTextBox, "ComparisonQuantity", 1, 1);
			conditionalOperatorListBox.setEnabled(view.isEditable());
			quantityTextBox.setEnabled(view.isEditable());
			unitsListBox = new ListBox();
			unitsListBox.addFocusHandler(new SimpleStatementFocusHandler());
			DOM.setElementAttribute(unitsListBox.getElement(), "id", conditionUnitID);
			unitsListBox.setEnabled(view.isEditable());
			addListBox(comparisonGrid, unitsListBox, 1, 2, comparisonUnits);
			unitsListBox.setSelectedIndex(2);
            MatContext.get().setAriaHidden(comparisonGrid, "false");
			grid.setWidget(row, COMPARISON_COL, comparisonGrid);
			grid.getFlexCellFormatter().setColSpan(row, COMPARISON_COL, 2);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			moveFocusToPropertyEditor();
		}
	}

	protected void addRemoveComparisonButton(FlexTable grid, int row, int col) {
		Button button = new Button();
		button.setText("Remove");
		button.setTitle("Remove Comparison");
		button.setEnabled(view.isEditable());
		button.addClickHandler(removeComparisonHandler);
		grid.setWidget(row, col, button);
		MatContext.get().setAriaHidden(grid.getWidget(row,col),"false");
	}

	ClickHandler removeComparisonHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			MatContext.get().clearDVIMessages();
			removeComparison();
		}
	};

	protected void removeComparison() {
		try {
			grid.removeCell(PHRASE_LIST_ROW, ADD_REMOVE_BUTTON_COL);
			addAddComparisonButton(grid, PHRASE_LIST_ROW, ADD_REMOVE_BUTTON_COL);
			grid.removeCell(grid.getRowCount() - 1, CONDITION_COL);
			emptyOutComparisionOperators();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			moveFocusToPropertyEditor();
		}
	}

	protected void emptyOutComparisionOperators(){
		conditionalOperatorListBox = null;
		conditionalOperator = null;
		quantity = null;	
		unit = null;
		quantityTextBox = null;
	}
	protected void addTextBox(FlexTable comparisonGrid, TextBox textBox, String title, int row, int col) {
		textBox.setTitle(title);
		comparisonGrid.setWidget(row, col, textBox);
	}

	protected String getSelectedCondition() {
		return getItemText(conditionListBox);
	}

	protected void toggleSimplePhrase2ToComparison() {
		switchSubmode();
		singleSubMode = SINGLE_SUBMODE.COMPARISON;
		initSingleMode();
	}

	protected void toggleSimpleComparisonToPhrase2() {
		switchSubmode();
		singleSubMode = SINGLE_SUBMODE.PHRASE;
		emptyOutComparisionOperators();
		initSingleMode();
	}

	protected void usePhrase() {
		toggleSimplePhrase2AndComparisonButton.setText("Use comparison");
		toggleSimplePhrase2AndComparisonButton.setTitle("Use comparison");
		if (toggleSimplePhrase2AndComparisonHandlerRegistration != null)
			toggleSimplePhrase2AndComparisonHandlerRegistration.removeHandler();
		toggleSimplePhrase2AndComparisonHandlerRegistration = toggleSimplePhrase2AndComparisonButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				toggleSimplePhrase2ToComparison();
			}
		});
		moveFocusToPropertyEditor();
	}

	protected void useComparison() {
		toggleSimplePhrase2AndComparisonButton.setText("Use phrase");
		toggleSimplePhrase2AndComparisonButton.setTitle("Use phrase");
		if (toggleSimplePhrase2AndComparisonHandlerRegistration != null)
			toggleSimplePhrase2AndComparisonHandlerRegistration.removeHandler();
		toggleSimplePhrase2AndComparisonHandlerRegistration = toggleSimplePhrase2AndComparisonButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				toggleSimpleComparisonToPhrase2();
			}
		});
		moveFocusToPropertyEditor();
	}

	public DiagramObject clone() throws IllegalArgumentException {
		return clone(this);
	}

	public DiagramObject clone(SimpleStatement src) throws IllegalArgumentException {
		SimpleStatement dest = new SimpleStatement(appController);

		dest.identity = src.identity;
		dest.description = src.description;
		dest.status = src.status;
		dest.mode = src.mode;
		dest.singleSubMode = src.singleSubMode;
		dest.clauseType = src.clauseType;
		dest.phrase1 = src.phrase1.clone();
		dest.condition = src.condition;
		dest.changedName = src.identity;
		dest.mPhraseUniqueIdentity = src.mPhraseUniqueIdentity;
		if (dest.condition == null || dest.condition.trim().length() == 0)
			dest.condition = NONE;
		dest.phrase2 = src.phrase2 == null ? null : src.phrase2.clone();
		dest.additionalPhraseList = new ArrayList<Phrase>();
		if (src.additionalPhraseList != null)
			for (Phrase phrase : src.additionalPhraseList)
				dest.additionalPhraseList.add(phrase.clone());
		dest.conditionalOperator = src.conditionalOperator;
		dest.quantity = src.quantity;
		dest.unit = src.unit;

		dest.function = src.function;
		dest.functionOperator = src.functionOperator;
		dest.functionQuantity = src.functionQuantity;
		dest.functionUnit = src.functionUnit;

		return dest;
	}

	public String getPrettyPrintedFullText() {
		return getPrettyPrintedFullText(1);
	}

	public String getPrettyPrintedFullText(int indentation) {
		StringBuilder sb = new StringBuilder();

		switch (mode) {
		case SINGLE:
			getFullTextForSingle(sb, indentation, "\n");
			break;
		case LOGICAL:
			getFullTextForLogical(sb, indentation, "\n");
			break;
		case COMPARISON:
			getFullTextForComparison(sb, indentation, "\n");
			break;
		case FUNCTION:
			getFullTextForComparison(sb, indentation, "\n");
			break;
		default:
			return "";
		}		

		return sb.toString();
	}

	public String getFullText() {
		StringBuilder sb = new StringBuilder();

		switch (mode) {
		case SINGLE:
			getFullTextForSingle(sb, 1, "");
			break;
		case LOGICAL:
			getFullTextForLogical(sb, 1, "");
			break;
		case COMPARISON:
			getFullTextForComparison(sb, 1, "");
			break;
		default:
			return "";
		}		

		return sb.toString();
	}

	public String getFirstLine() {
		String[] lines = getFullText().split(LINE_SEPARATOR);
		if (lines == null)
			return "";
		else
			return lines[0].trim();
	}

	@Override
	public void draw(TraversalTree tree, MeasurePhrases measurePhrases, ZOOM zoom) {
		this.measurePhrases = measurePhrases;
		this.zoom = zoom;

		switch (mode) {
		case SINGLE:
			drawSingle(tree);
			break;
		case LOGICAL:
			drawLogical(tree);
			break;
		case COMPARISON:
			drawComparison(tree);
			break;
		default:
			break;
		}
	}

	protected void drawSingle(TraversalTree tree) {	
		StringBuilder sb = new StringBuilder();
		tree.addDiagramObject(new Qdsel(sb.toString()));
	}

	protected String createTab(int indentation) {
		if (indentation <= 0)
			return "";
		char[] tabs = new char[indentation * TAB_SIZE];
		Arrays.fill(tabs, ' ');
		return new String(tabs);	
	}

	protected String expandText(Phrase phrase, int indentation, String newline) {
		if (phrase == null)
			return "";
		String s = expandText(phrase.getTextSansOid(), indentation, newline);
		if(s== null || s.indexOf("null")>=0){
			if(s==null)
				s="";
		}
		
		return s;
	}

	private String expandText(String text, int indentation, String newline) {
		if (text == null || text.trim().length() == 0)
			return text;

		String tab = createTab(indentation - 1);
		StringBuilder sb = new StringBuilder();

		if (view.isMeasurePhrase(text)) {
			SimpleStatement simpleStatement = (SimpleStatement)(view.getMeasurePhrase(text));
			simpleStatement.setView(this.getView());
			simpleStatement.getPhrase1().setAppController(appController);

			if (simpleStatement.getPhrase2() != null && simpleStatement.getPhrase2().getText() != null) {
				simpleStatement.getPhrase2().setAppController(appController);
			}

			// do same for additional phrases...
			for (Phrase phrase : simpleStatement.getAdditionalPhraseList())
				phrase.setAppController(appController);

			sb.append(tab).append("("). /*append( text);*/
			/*sb.append("=").*/append(newline);
			sb.append(simpleStatement.getPrettyPrintedFullText(indentation + 2));
			sb.append(tab).append(")").append(newline).append(tab);
		}
		else {
			sb.append(tab).append(text);
		}

		return sb.toString();
	}

	protected void getFullTextForSingle(StringBuilder sb, int indentation, String newline) {
		String tab = createTab(indentation);

		
		
		sb.append(expandText(phrase1, indentation, newline));
		
		
		if (singleSubMode == SINGLE_SUBMODE.PHRASE) {
			if (phrase2 != null)
				sb.append(tab).append(condition).append(" ").append(expandText(phrase2, 0, newline));
		}
		else
			if (quantity != null)
				sb.append(tab).append(conditionalOperator).append(" ").append(quantity).append(" ").append(unit);
	}

	protected void drawLogical(TraversalTree tree) {
		StringBuilder sb = new StringBuilder();
		tree.addDiagramObject(new Qdsel(sb.toString()));
	}

	protected void getFullTextForLogical(StringBuilder sb, int indentation, String newline) {
		
		String tab = createTab(indentation);
		String functionTab = "";
		boolean hasFunction = function != null && !function.trim().equals("");

		String leftSpace = "";
		if (hasFunction) {
			functionTab = createTab(indentation - 1);
			sb.append(functionTab).append(function).append("(").append(newline);
			leftSpace = tab;
		}

		sb.append(leftSpace).append(expandText(phrase1, indentation, newline));
		sb.append(" ").append(condition).append(" ").append(expandText(phrase2, 0, newline)).append(newline);

		leftSpace = (tab.contains("\t")) ? tab : " ";
		if (additionalPhraseList != null)
			for (Phrase phrase : additionalPhraseList)
				sb.append(leftSpace).append(condition).append(" ").append(expandText(phrase, indentation, newline)).append(newline);

		
		
		String s1 = functionTab;
		if(s1==null)
			s1="";
		String s2 = functionOperator;
		if(s2==null)
			s2="";
		String s3 = functionQuantity;
		if(s3==null)
			s3="";
		String s4 = functionUnit;
		if(s4==null)
			s4="";
		
		if (hasFunction)
			addFunction(sb, functionTab, newline);
			//sb.append(s1).append(") ").append(s2).append(" ").append(s3).append(" ").append(s4);
	}

	protected void drawComparison(TraversalTree tree) {
		StringBuilder sb = new StringBuilder();
		getFullTextForComparison(sb, 1, "");
		tree.addDiagramObject(new Qdsel(sb.toString()));
	}

	protected void getFullTextForComparison(StringBuilder sb, int indentation, String newline) {
		
		String tab = createTab(indentation);
		String functionTab = "";
		boolean hasFunction = function != null && !function.trim().equals("");

		String leftSpace = "";
		if (hasFunction) {
			functionTab = createTab(indentation - 1);
			sb.append(functionTab).append(function).append("(").append(newline);
			leftSpace = tab;
		}

		sb.append(leftSpace).append(expandText(phrase1, indentation, newline)).append(" ").append(condition).append(" ");
		sb.append(expandText(phrase2, 0, newline)).append(newline);

		if (quantity != null)
			sb.append(tab.contains("\t") ? tab : " ").append(conditionalOperator).append(" ").append(quantity).append(" ").append(unit);

		if (hasFunction){
			addFunction(sb, functionTab, newline);
		}
	}

	private void drawPhrases(TraversalTree tree) {
		drawPhrase(tree, phrase1.getText());
		drawPhrase(tree, phrase2.getText());
	}

	private void drawPhrase(TraversalTree tree, String phrase) {
		if (measurePhrases.getList().contains(phrase)) {
			DiagramObject diagramObject = measurePhrases.getItem(phrase);
			if (diagramObject != null) {
				if (zoom == ZOOM.EXPANDED) {
					TraversalTree child = new TraversalTree();
					diagramObject.draw(child, measurePhrases, zoom);
					tree.addChild(child);
				}
				else
					tree.addDiagramObject(new PlaceHolder((SimpleStatement)diagramObject));
			}
		}
		else
			tree.addDiagramObject(new Qdsel(phrase));
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getFunctionOperator() {
		return functionOperator;
	}
	
	public void setFunctionUnit(String funit){
		this.functionUnit = funit;
	}
	
	public String getFunctionUnit(){
		return functionUnit;
	}
	
	public void setFunctionOperator(String functionOperator) {
		this.functionOperator = functionOperator;
	}
	

	public String getFunctionQuantity() {
		return functionQuantity;
	}

	public void setFunctionQuantity(String functionQuantity) {
		this.functionQuantity = functionQuantity;
	}

	public Phrase getPhrase1() {
		return phrase1;
	}

	public String getPhrase1Text() {
		return phrase1.getText();
	}

	public Phrase getPhrase2() {
		return phrase2;
	}
	public MODE getMode() {
		return mode;
	}

	public void setMode(MODE mode) {
		this.mode = mode;
	}


	public String getPhrase2Text() {
		return phrase2.getText();
	}

	public List<Phrase> getAdditionalPhraseList() {	
		return additionalPhraseList;
	}	

	private boolean getComparison() {
		String q = (quantityTextBox == null) ? null : quantityTextBox.getText();
		if (q != null && !q.trim().isEmpty())
			try {
				for(char c : q.toCharArray()){
					new Integer(c+"");
				}
				if(q.length()>100)
					throw new Exception();
				quantity = q;
				conditionalOperator = conditionalOperatorListBox == null ? null : getItemText(conditionalOperatorListBox);	// MODE.COMPARISON
				if (conditionalOperator == null)
					conditionalOperator = conditionListBox == null ? "" : getItemText(conditionListBox);		// MODE.SINGLE
				unit = unitsListBox == null ? "" : getItemText(unitsListBox);
			}
		catch (Exception e) {
			errorMsgList.add(MatContext.get().getMessageDelegate().getQuantityIntMessage());
			return false;
		}
		return true;
	}	

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Operator getComparisonAsStatementTermOperator() {
		return StatementTerm.statementTermOperators[Arrays.asList(comparisonOperators).indexOf(conditionalOperator)];
	}

	public static Operator getComparisonAsStatementTermOperator(String comparison) {
		return StatementTerm.statementTermOperators[Arrays.asList(comparisonOperators).indexOf(comparison)];
	}

	public void setComparison(String comparison) {
		conditionalOperator = comparison;
	}

	public void setComparison(Operator operator) {
		conditionalOperator = StatementTerm.getOperatorAsString(operator);
	}

	public SimpleStatement transformQdselToSimpleStatement(DiagramObject diagramObject, String newName) {
		SimpleStatement simpleStatement = new SimpleStatement(null);
		simpleStatement.identity = newName;
		simpleStatement.mode = MODE.SINGLE;
		simpleStatement.singleSubMode = SINGLE_SUBMODE.PHRASE;
		simpleStatement.phrase1 = new Phrase(appController);
		simpleStatement.phrase1.setText(diagramObject.getIdentity());
		simpleStatement.condition = NONE;
		simpleStatement.phrase2.setText(NONE);
		simpleStatement.additionalPhraseList = new ArrayList<Phrase>();
		return simpleStatement;
	}

	public DiagramObject createSecondOrderSimpleStatement(String newName) {
		this.mode = MODE.SINGLE;
		this.singleSubMode = SINGLE_SUBMODE.PHRASE;
		this.phrase1 = new Phrase(appController);
		this.phrase1.setText(this.identity);
		this.condition = NONE;
		this.phrase2.setText(NONE);
		this.additionalPhraseList = new ArrayList<Phrase>();
		this.identity = newName;	// do this last, so that phrase1 will contain the old name
		return this;
	}

	public CLAUSE_TYPE getClauseType() {
		return clauseType;
	}

	public void setClauseType(CLAUSE_TYPE clauseType) {
		this.clauseType = clauseType;
	}

	public List<String> getChildMeasurePhrases(DiagramView<?> view) {
		List<String>childMeasurePhrases = new ArrayList<String>();
		if (view.isMeasurePhrase(phrase1.getText()))
			childMeasurePhrases.add(phrase1.getText());
		if (view.isMeasurePhrase(phrase2.getText()))
			childMeasurePhrases.add(phrase1.getText());		
		for (Phrase phrase : additionalPhraseList) {
			String phraseName = phrase.getText();
			if (view.isMeasurePhrase(phraseName))
				childMeasurePhrases.add(phraseName);
		}
		return childMeasurePhrases;
	}

	@Override
	public boolean hasPropertyEditor() {
		return true;
	}	

	@Override
	public boolean canHaveChildren() {
		return false;
	}
	
	public String getStatusSelectedValue(){
	 if("InProgress".equalsIgnoreCase(getItemText(inProgressCompleteListBox))){
			 return "1";
	 }else
			 return "2";
	}
	public String getStatusItemText(){
		return getItemText(inProgressCompleteListBox);
	}
	private void createConditionListBox(){
		conditionListBox = new ListBox();
		conditionListBox.addFocusHandler(new SimpleStatementFocusHandler());
		DOM.setElementAttribute(conditionListBox.getElement(), "id", conditionID);
	}
	
	private void moveFocusToPropertyEditor(){
		view.moveFocusToPropertyEditor();
	}
	
	public void clearRadioButtonSelection(){
		phrasesRadio1.setValue(false);
		qdmElementsRadio1.setValue(false);
		phrasesRadio2.setValue(false);
		qdmElementsRadio2.setValue(false);
	}
	
	private void addFunction(StringBuilder sb, String functionTab, String newline){
		sb.append(functionTab).append(newline).append(functionTab).append(") ").append(functionOperator!=null?functionOperator:"").append(" ").append(functionQuantity!=null?functionQuantity:"").append(" ").append(functionUnit!=null?functionUnit:"");
	}
	
	private List<String> getTimingConditionsList(){
		return new ArrayList(MatContext.get().getListBoxCodeProvider().getTimingConditionsMap().keySet());
	}
	
	private List<String> getRelAssociationsList(){
		 return  MatContext.get().getListBoxCodeProvider().getRelAssociationOperatorList(); 
	}
}