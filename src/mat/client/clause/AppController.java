package mat.client.clause;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import mat.client.Mat;
import mat.client.clause.diagram.Diagram;
import mat.client.clause.diagram.TraversalTree;
import mat.client.clause.presenter.DiagramPresenter;
import mat.client.clause.view.DiagramViewImpl;
import mat.client.clause.view.shape.TextRectangle;
import mat.client.codelist.service.CodeListService;
import mat.client.codelist.service.CodeListServiceAsync;
import mat.client.diagramObject.Conditional;
import mat.client.diagramObject.Criterion;
import mat.client.diagramObject.CriterionParent;
import mat.client.diagramObject.DiagramObject;
import mat.client.diagramObject.PlaceHolder;
import mat.client.diagramObject.Qdsel;
import mat.client.diagramObject.Rel;
import mat.client.diagramObject.SimpleStatement;
import mat.client.diagramObject.SimpleStatementBuilder;
import mat.client.diagramObject.SimpleStatementToCentralPojoFactory;
import mat.client.diagramObject.clickHandler.PlaceholderClickHandler;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.MatContext;
import mat.model.QualityDataSetDTO;
import mat.model.clause.Clause;
import mat.model.clause.Context;
import mat.shared.ConstantMessages;
import mat.shared.StringUtility;
import mat.shared.TopologicalSort;
import mat.shared.model.Decision;
import mat.shared.model.IQDSTerm;
import mat.shared.model.QDSMeasurementTerm;
import mat.shared.model.QDSTerm;
import mat.shared.model.Conditional.Operator;
import mat.shared.model.QDSMeasurementTerm.QDSOperator;
import mat.shared.model.util.OperatorConverter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController {
	//field used to indicate source of invoking class (JavaScript translation does not support this.getClass().getName())
	private static final String CLASS_NAME = "AppController";
	
	private final AppController me;
	private final HandlerManager eventBus;
	private final ClauseServiceAsync rpcService; 
	private CodeListServiceAsync codeListService;
	private List<Context> allContexts = new ArrayList<Context>();
	//private Map<String, QualityDataSetDTO> qdsMap = new HashMap<String, QualityDataSetDTO>();
	private List<QualityDataSetDTO> qdsList = new ArrayList<QualityDataSetDTO>();
	ErrorMessageDisplay saveErrorMessages = new ErrorMessageDisplay();
	public ErrorMessageDisplay getSaveErrorMessages() {
		return saveErrorMessages;
	}

	public void setSaveErrorMessages(ErrorMessageDisplay saveErrorMessages) {
		this.saveErrorMessages = saveErrorMessages;
	}

	public final static List<String> criterionNames = Arrays.asList(
			new String[] {ConstantMessages.POPULATION_CONTEXT_DESC,
					ConstantMessages.NUMERATOR_CONTEXT_DESC,
					ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_DESC,
					ConstantMessages.DENOMINATOR_CONTEXT_DESC,
					ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_DESC,
					ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_DESC,
					ConstantMessages.MEASURE_POPULATION_CONTEXT_DESC,
					ConstantMessages.MEASURE_OBSERVATION_CONTEXT_DESC,
					ConstantMessages.STRAT_CONTEXT_DESC,
					ConstantMessages.USER_DEFINED_CONTEXT_DESC,
					ConstantMessages.MEASURE_PHRASE_CONTEXT_DESC});
	
	
	private Diagram[] diagrams = null;
	
	private MeasurePhrases measurePhrases;
	
	private DiagramViewImpl<Diagram> diagramView = null;
	private Diagram currentDiagram = null;

	
	private String measureName;
	private DiagramObject currentDiagramObject = null;
	private ClauseController clauseController = new ClauseController();
	private Clause criterionModel = null;
	private String measureId;
	private Map<String, Clause> clauses = new HashMap<String, Clause>();
	private boolean editable;
	private List<Clause> usersOtherSystemClauses = new ArrayList<Clause>();
	
	private boolean isLoaded = false;
	
	public AppController(ClauseServiceAsync rpcService,HandlerManager eventBus) {
		me = this;
		measurePhrases = new MeasurePhrases(this);
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		diagramView = new DiagramViewImpl<Diagram>(this, clauseController);
		saveErrorMessages.clear();
		diagramView.getSaveErrorMessageHolder().add(saveErrorMessages);
	}

	public String[] getQDSElements() {
		String[] ts =  new String[qdsList.size()];
		int i=0;
//		String[] ts = new String[qdsMap.keySet().size()];
//		int i=0;
//	
//		for (String t: qdsMap.keySet()) {
//			ts[i++]=qdsMap.get(t).toString();
//		}
//		Arrays.sort(ts);
//		return ts;	
		for(QualityDataSetDTO qds: qdsList){
			ts[i++]= qds.toString();
		}
		Arrays.sort(ts);
		return ts;
	}
	
	
	public void loadQDSElements(final boolean load) {
		codeListService= (CodeListServiceAsync) GWT.create(CodeListService.class);
		codeListService.getQDSElements(MatContext.get().getCurrentMeasureId(), null, new AsyncCallback<List<QualityDataSetDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				System.out.println("Error caught" + caught.getMessage());
			}

			@Override
			public void onSuccess(List<QualityDataSetDTO> result) {
				/*for (QualityDataSetDTO dto: result) {
					qdsMap.put(dto.getOid(), dto);
				}*/
				
				qdsList = result;

				if(load) {
					loadRules();					
				}
			}
		});
	}
	
	void open(String measureID,final HasWidgets container) { 
		saveErrorMessages.clear();
		new DiagramPresenter(this, eventBus, diagramView).go(container);
		
		clauseController.displaySearch();
		showLoadingMessage();
		//control selection of phrase and clause libraries
		disableLibraries();
		//US201 select phrase library by default when navigating to Clause Workspace
		selectPhraseLibrary();
	//	if(isNewMeasureLoaded())
			initDiagrams();
		diagramView.selectStack();
		//editable = MatContext.get().isCurrentMeasureEditable()|| !MatContext.get().isCurrentMeasureLocked();
		editable = MatContext.get().getMeasureLockService().checkForEditPermission();
		diagramView.setEditable(editable);
		try {
//				if(isNewMeasureLoaded()) {
					resetAllData();
					this.measureName = MatContext.get().getCurrentMeasureName();
					this.measureId = MatContext.get().getCurrentMeasureId();
					loadAllContexts();
//				}else{
//					Mat.hideLoadingMessage();
//				}
		}catch (Exception e) {
			hideLoadingMessage();
			e.printStackTrace();
			//Window.alert(e.getMessage());
			Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
		}
	}

	private void resetAllData() {
		initDiagrams();
		this.measurePhrases.reset();
		this.measurePhrases.resetSavedDiagramObjects();
		this.qdsList.clear();
		this.clauses.clear();
		usersOtherSystemClauses.clear();
	}
	
	public void resetSavedDiagramObjects(){
		this.measurePhrases.resetSavedDiagramObjects();
	}
	
	public void refreshSystemClauses(List<String> systemClauses){
		//To reset all the System Clauses and load all contexts
		resetAllData();
		loadAllContexts();
	}
	
	public boolean setDiagramDirty(String criterion) {
		int diagramIndex = criterionNames.indexOf(criterion);
		if (diagramIndex < 0 || diagramIndex > (ConstantMessages.CONTEXT_ID_COUNT-2))//return false if the criterion is Measure Phrase.
			return false;
		diagrams[diagramIndex].setDirty(true);
		return true;
	}
	
	private void setCriterionHelper(int i){
		String s = criterionNames.get(i);
		diagrams[i].getTree().addDiagramObject(new CriterionParent(s));
		addCriterion(s);
	}
	
	private void initDiagrams() {
		diagrams = null;
		diagrams = new Diagram[criterionNames.size()-1];//Ignore Measure Phrase
		for (int i = 0; i < diagrams.length; ++i) {
			diagrams[i] = new Diagram();
			diagrams[i].setDirty(false);
			setCriterionHelper(i);
		}
		/*setCriterionHelper(0);
		setCriterionHelper(1);
		setCriterionHelper(2);
		setCriterionHelper(3);
		setCriterionHelper(4);
		setCriterionHelper(5);
		setCriterionHelper(6);
		setCriterionHelper(7);
		setCriterionHelper(8);*/
		/*
		diagrams[0].getTree().addDiagramObject(new CriterionParent(POPULATION));
		addCriterion(POPULATION);
		diagrams[1].getTree().addDiagramObject(new CriterionParent(NUMERATOR));
		addCriterion(NUMERATOR);
		diagrams[2].getTree().addDiagramObject(new CriterionParent(NUMERATOR_EXCL));
		addCriterion(NUMERATOR_EXCL);
		diagrams[3].getTree().addDiagramObject(new CriterionParent(DENOMINATOR));
		addCriterion(DENOMINATOR);
		diagrams[4].getTree().addDiagramObject(new CriterionParent(EXCLUSIONS));
		addCriterion(EXCLUSIONS);
		diagrams[5].getTree().addDiagramObject(new CriterionParent(EXCEPTIONS));
		addCriterion(EXCEPTIONS);
		diagrams[6].getTree().addDiagramObject(new CriterionParent(MEASURE_POPULATION));
		addCriterion(MEASURE_POPULATION);
		diagrams[7].getTree().addDiagramObject(new CriterionParent(MEASURE_OBSERVATION));
		addCriterion(MEASURE_OBSERVATION);
		diagrams[8].getTree().addDiagramObject(new CriterionParent(USER_DEFINED));
		addCriterion(USER_DEFINED);
		*/
	}
	
	public void addConditional(String currentCriterion, Criterion currentDiagramObject) {
		String condition = "AND";
		DiagramObject newConditional = (DiagramObject)(new Conditional(condition));
		addDiagramObject(currentCriterion, currentDiagramObject, newConditional);
	}
	
	private boolean isNewMeasureLoaded() {
		return (this.measureId == null || !MatContext.get().getCurrentMeasureId().equals(this.measureId));
	}

	public void loadAllContexts() {
		rpcService.getAllContexts(new AsyncCallback<List<Context>>() {
			@Override
			public void onSuccess(List<Context> ctxs) {
				allContexts = ctxs;
				loadQDSElements(true);
			}	

			public void onFailure(Throwable e) {
				e.printStackTrace();
			}
		});
	}
	
	public boolean isLoaded() {
		return isLoaded;
	}
	private void loadRules() {
		isLoaded = false;
		//US201
		disableLibraries();
		rpcService.loadAll(MatContext.get().getCurrentMeasureId(), null, new AsyncCallback<List<Clause>>() {
			@Override
			public void onSuccess(List<Clause> clauses) {
				//diagramView.refreshMeasurePhrases(measurePhrases.getList());
				SimpleStatementBuilder builder = new SimpleStatementBuilder(me, qdsList, measurePhrases);
				ListIterator<Clause> clausesIterator = clauses.listIterator(); 
				
				//we have to load phrases first
				while(clausesIterator.hasNext()){
				    Clause cl = clausesIterator.next();
					AppController.this.clauses.put(cl.getName(), cl);

					//if a measure phrase
					boolean isMeasurePhrase = cl.getContextId().equals(SimpleStatement.CLAUSE_TYPE.MEASURE_PHRASE.getClauseType());
					if (isMeasurePhrase) {
						builder.handleAClause(cl);//building the measure phrases
						clausesIterator.remove();//Removing the measure phrase from the list to prevent reiterating while loading non-phrases.
					}
				}				
				//load non-phrases
				for (Clause cl : clauses) {
					   if(!cl.getContextId().equalsIgnoreCase(ConstantMessages.MEASURE_PHRASE_CONTEXT_ID))
						   setClauseToDiagram(cl);//building the system clauses
					   else
						   if((cl.getClauseTypeId()!=null && cl.getClauseTypeId().equalsIgnoreCase("SYSTEM_CLAUSE"))){
							   setClauseToDiagram(cl);//Load System Clause for User-defined.  
						   }
				}
				addTopLevelAnds();
				isLoaded = true;
				//US201 refresh measure phrase library only
				//loadSystemClauseNames();
				diagramView.refreshMeasurePhrases(measurePhrases.getList());
				
				unSetDiagramDirty();
				if(getCurrentDiagramView().equalsIgnoreCase("TEXT_VIEW")){//if the user was previously in Text view
					reDrawTextView();
				}else{
					diagramView.drawDiagram();
				}
				clearClauseWorkSpaceMessages();
				hideLoadingMessage();
				//US201
				enableLibraries();
			}	

			public void onFailure(Throwable e) {
				hideLoadingMessage();
				//US201
				enableLibraries();
				e.printStackTrace();
			}
		});
	}
	private void addTopLevelAnds() {
		boolean doSave = false;
		for(int i = 0; i < diagrams.length-1; i++){
			Diagram d = diagrams[i];
			if(!d.getTree().isLeaf()) {
				String currentCriterion = d.getTree().getName();
				if(d.getTree().getChildren().get(0).isLeaf()){
					TraversalTree tt = d.getTree().getChildren().get(0);
					Criterion crit = (Criterion) tt.getDiagramObject();
					addConditional(currentCriterion, crit);
					doSave = true;
				}
			}
		}
		if(doSave)
			saveMainPhrases();
	}
	/*
	 * after loading we may inadvertently
	 * set the dirty flags to true, this
	 * method will reset all dirty flags
	 * to false
	 */
	public void unSetDiagramDirty() {
		int length = diagrams.length;
		for (int i=0; i<length; i++) {
			diagrams[i].setDirty(false);
		}
	}
	/*
	private void loadRule(String clauseName) {
		rpcService.loadClauseByName(MatContext.get().getCurrentMeasureId(), clauseName, new AsyncCallback<Clause>() {
			@Override
			public void onSuccess(Clause cl) {
				//diagramView.refreshMeasurePhrases(measurePhrases.getList());
				SimpleStatementBuilder builder = new SimpleStatementBuilder(me, qdsList, measurePhrases);

				AppController.this.clauses.put(cl.getName(), cl);
				if (cl.getContextId().equals(SimpleStatement.CLAUSE_TYPE.CLAUSE_LIBRARY.getClauseType()) ||
						cl.getContextId().equals(SimpleStatement.CLAUSE_TYPE.MEASURE_PHRASE.getClauseType())) {
					builder.handleAClause(cl);
				}
				loadSystemClauseNames();
				//refreshLibaryAndPhrases();
			}	

			public void onFailure(Throwable e) {
				e.printStackTrace();
			}
		});
	}
	*/
	
	private long start;
	/*
	 * This method loads all the available system clauses for the loggedinUser in the 
	 * Clause Library Tab.
	 */
	//US201 making public to be invoked from DiagramViewImpl
	public void loadSystemClauseNames() {
		showLoadingMessage();
		usersOtherSystemClauses.clear();
		//control selection of phrase and clause libraries
		disableLibraries();
		final long start = System.currentTimeMillis();
		rpcService.loadSystemClauseNames(MatContext.get().getLoggedinUserId(), 
				MatContext.get().getLoggedInUserRole(), new AsyncCallback<List<Clause>>() {
			@Override
			public void onSuccess(List<Clause> clauses) {
				
				long end = System.currentTimeMillis();
				long dur = (end-start)/1000;
				System.out.println("client loadSystemClauseNames took "+dur+" seconds to load "+clauses.size()+" clauses");
				
				List<String> mPhrases = measurePhrases.getList();
				
				for(Clause c : clauses) {
					boolean found = false;
					for (String m : mPhrases) {
						//filter out the current meassure's system clauses
						if (c.getName().equalsIgnoreCase(m)) {
							found = true;
						}
					}
					if (!found) {
						usersOtherSystemClauses.add(c);
					}
				}
				//US201 refresh only the system clauses
				diagramView.refreshSystemClauses( usersOtherSystemClauses);
//				refreshLibaryAndPhrases();
				enableLibraries();
				hideLoadingMessage();
			}	

			public void onFailure(Throwable e) {
				e.printStackTrace();
				hideLoadingMessage();
			}
		});
	}

	private void setClauseToDiagram(Clause clause) {
		StringUtility su = new StringUtility();
		String criterion = null;
		int criterionPos = 0;
		int currentDiagPos = 0;//population1, numerator1, etc...
		String name =clause.getName();
		String contextId = clause.getContextId();
		
		if (contextId.equalsIgnoreCase(ConstantMessages.POPULATION_CONTEXT_ID)) {
			criterion = ConstantMessages.POPULATION_CONTEXT_DESC;
			criterionPos = 0;
			currentDiagPos = su.getPos(name);
		} else if (contextId.equalsIgnoreCase(ConstantMessages.NUMERATOR_CONTEXT_ID)) {
			criterion = ConstantMessages.NUMERATOR_CONTEXT_DESC;
			criterionPos = 1;
			currentDiagPos = su.getPos(name);
		} else if(contextId.equalsIgnoreCase(ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID)) {
			criterion = ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_DESC;
			criterionPos = 2;
			currentDiagPos = su.getPos(name);
		} else if(contextId.equalsIgnoreCase(ConstantMessages.DENOMINATOR_CONTEXT_ID)) {
			criterion = ConstantMessages.DENOMINATOR_CONTEXT_DESC;
			criterionPos = 3;
			currentDiagPos = su.getPos(name);
		} else if(contextId.equalsIgnoreCase(ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID)) {
			criterion = ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_DESC;
			criterionPos = 4;
			currentDiagPos = su.getPos(name);
		} else if(contextId.equalsIgnoreCase(ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID)) {
			criterion = ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_DESC;
			criterionPos = 5;
			currentDiagPos = su.getPos(name);
		}else if(contextId.equalsIgnoreCase(ConstantMessages.MEASURE_POPULATION_CONTEXT_ID)) {
			criterion = ConstantMessages.MEASURE_POPULATION_CONTEXT_DESC;
			criterionPos = 6;
			currentDiagPos = su.getPos(name);
		}else if(contextId.equalsIgnoreCase(ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID)) {
			criterion = ConstantMessages.MEASURE_OBSERVATION_CONTEXT_DESC;
			criterionPos = 7;
			currentDiagPos = su.getPos(name);
		}else if(contextId.equalsIgnoreCase(ConstantMessages.STRATIFICATION_CONTEXT_ID)) {
			criterion = ConstantMessages.STRAT_CONTEXT_DESC;
			criterionPos = 8;
			currentDiagPos = su.getPos(name);
		}else if(contextId.equalsIgnoreCase(ConstantMessages.USER_DEFINED_CONTEXT_ID)) {
			criterion = ConstantMessages.USER_DEFINED_CONTEXT_DESC;
			criterionPos = 9;
			currentDiagPos = su.getPos(name);
		}

		//Add a new clause diagram object.
		if (currentDiagPos>0) {
			diagrams[criterionPos].getTree().addDiagramObject(new Criterion(name, clause.getCustomName(), currentDiagPos));
		}

		//TODO: This if check needs to be removed later. This is to facilitate testers' by 
		//removing phrases that were not supposed to be there These unwanted phrases e.g. "Population"
		//were not supposed to saved in the first place. Remove it in next build..-Vasant.
		if (currentDiagPos>=0) {
			if(diagrams[criterionPos].getTree().getChildren().size()>currentDiagPos){
				TraversalTree treeClause = diagrams[criterionPos].getTree().getChildren().get(currentDiagPos);
				for (Decision decision : clause.getDecisions()) {
					populateChild(decision, criterion, treeClause.getDiagramObject());
				}
			}else{
				//an error should be thrown here index out of bounds
				System.out.println("INDEX OUT OF BOUNDS AppController.setClauseToDiagram treesize:"+diagrams[criterionPos].getTree().getChildren().size());
			}
		}
	}
	
	private void populateChild(Decision decision, String currentCriterion, DiagramObject currentDiagramObject) {
		
		//CONDITIONAL
		if(decision instanceof mat.shared.model.Conditional) {
			mat.shared.model.Conditional c = (mat.shared.model.Conditional) decision;
			OperatorConverter oc = new OperatorConverter();
			String identity = oc.conditionalToString(c.getOperator());

			DiagramObject childDiagramObject = new Conditional(identity);
			loadDiagramObject(currentCriterion, currentDiagramObject, childDiagramObject);
			
			//process this Conditional's children
			List<Decision> nextChildren = c.getDecisions();
			if (nextChildren==null) return;
			for (Decision d : nextChildren) {
				populateChild(d, currentCriterion, childDiagramObject);
			}
		
		//QDS MEASUREMENT TERM
		} else if(decision instanceof QDSMeasurementTerm) {
			QDSMeasurementTerm qDSMeasurementTerm = (QDSMeasurementTerm) decision;
			String identity = Rel.shortToLongIdentity(qDSMeasurementTerm.getQDSOperator());
			
			DiagramObject childDiagramObject = new Rel(identity);
				
			loadDiagramObject(currentCriterion, currentDiagramObject, childDiagramObject);
			
			//process this QDSMeasurementTerm left and right children
			populateChild(qDSMeasurementTerm.getLfTerm(), currentCriterion, childDiagramObject);
			populateChild(qDSMeasurementTerm.getRtTerm(), currentCriterion, childDiagramObject);
			
		//CLAUSE
		} else if(decision instanceof Clause) {
			Clause c = (Clause)decision;
			String clauseName = c.getName();
			
			List<Decision> nextChildren = c.getDecisions();
			DiagramObject childDiagramObject = null;
			if (clauseName!=null) {
				SimpleStatement simpleStatement = (SimpleStatement)(getMeasurePhrase(c.getName()));
				if(simpleStatement != null){
					simpleStatement.setView(diagramView);
					childDiagramObject = new PlaceHolder(simpleStatement);
					loadDiagramObject(currentCriterion, currentDiagramObject, childDiagramObject);
				}
					
				//process this Clause's children with new parent
				for (Decision d : nextChildren) {
					populateChild(d, currentCriterion, childDiagramObject);
				}
			} 
			else {
				//process this Clause's children with previous parent
				for (Decision d : nextChildren) {
					populateChild(d, currentCriterion, currentDiagramObject);
				}
			}

			//QDS TERM	
		} else if(decision instanceof QDSTerm) {
			QDSTerm qDSTerm = (QDSTerm)decision;
			loadDiagramObject(currentCriterion, currentDiagramObject, new Qdsel(getQDSStrForQDSTerm(qDSTerm)));
		}
	}
	
	private String getQDSStrForQDSTerm(QDSTerm qDSTerm) {
		QualityDataSetDTO dtoTemp = new QualityDataSetDTO();
		dtoTemp.setId(qDSTerm.getqDSRef());
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<QualityDataSetDTO> qds = new ArrayList(qdsList);
		
		int ind = -1;
		String qdsStr = "";
		if ( (ind = qds.indexOf(dtoTemp)) >= 0) {
			qdsStr = qds.get(ind).toString();
		}
		return qdsStr;
	}
	
	/*
	 * save all phrases (simple phrases and population, 
	 * numerator, denominator, exclusions, exceptions, others)
	 */
	public void save() {
		SimpleStatementToCentralPojoFactory sstcpf = new SimpleStatementToCentralPojoFactory(clauses);
		sstcpf.setDataResolver(qdsList);

		List<Clause> clauses = new ArrayList<Clause>();
		try {
			diagramToModel(sstcpf, clauses, null);
			criterionToModel(sstcpf, clauses);
			saveClauses(clauses, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * save only main phrases (population, numerator, num exclusions, denominator, exclusions, exceptions, others)
	 */
	public void saveMainPhrases() {
		showLoadingMessage();
		SimpleStatementToCentralPojoFactory sstcpf = new SimpleStatementToCentralPojoFactory(clauses);
		sstcpf.setDataResolver(qdsList);

		List<Clause> clauses = new ArrayList<Clause>();
		try {
			criterionToModel(sstcpf, clauses);
			if (!clauses.isEmpty()) {
				saveClauses(clauses, 0);
			}else{
				MatContext.get().getSynchronizationDelegate().setSavingClauses(false);
				diagramView.setSaveStatus(true);
				hideLoadingMessage();
			}
		} catch (Exception e) {
			hideLoadingMessage();
			e.printStackTrace();
		} 
	}
	/**
	 * Method to identify if canvas has any changes.
	 * */
	public boolean isCanvasModified(){
		boolean isCanvasChanged =false;
		Diagram[] criteriaTypes = getDiagrams();
		for (Diagram criterion: criteriaTypes) {
			if (criterion.isDirty()) {
				isCanvasChanged = true;
				break;
			}
			
		}
		return isCanvasChanged;
		
	}
	
	
	/*
	 * save one phrase at a time
	 */
	public void save(String phraseName) {
		List<String> phraseNames = new ArrayList<String>();
		phraseNames.add(phraseName);
		save(phraseNames);
	}
	/*
	 * save one or more phrases at a time
	 */
	public void save(List<String> phraseNames) {
		SimpleStatementToCentralPojoFactory sstcpf = new SimpleStatementToCentralPojoFactory(clauses);
		sstcpf.setDataResolver(qdsList);

		List<Clause> clauses = new ArrayList<Clause>();
		try {
			diagramToModel(sstcpf, clauses, phraseNames);
			saveClauses(clauses, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Clause getClause(String phraseName) {
	  Clause outerLayerClause =clauses.get(phraseName);
	  Clause innerTrueClause = null;
	  if (outerLayerClause.getDecisions().get(0) instanceof Clause) {
	   innerTrueClause = (Clause)outerLayerClause.getDecisions().get(0);
	   innerTrueClause.setContextId(outerLayerClause.getContextId());
	   innerTrueClause.setName(outerLayerClause.getName());
	   innerTrueClause.setDescription(outerLayerClause.getDescription());
	   return innerTrueClause;
	  } else {
	   return outerLayerClause;
	  }
	}
	
	
	
	private void criterionToModel(SimpleStatementToCentralPojoFactory sstcpf, List<Clause> clauses) throws Exception {
		Diagram[] criteriaTypes = getDiagrams();
		for (Diagram criterion: criteriaTypes) {
			
			//do not process if dirty flag is not set
			//we do not want to re-save any diagram that was 
			//not modified
			if (criterion.isDirty()) {
				TraversalTree systemClauseTree = criterion.getTree();
				if (systemClauseTree.getDiagramObject() != null) {
					String contextName = systemClauseTree.getDiagramObject().getIdentity();
					//This is an instance of a system clause (numerator, denominator etc.)
					// each system clause gets mapped to a clause model object.
					List<TraversalTree> systemClauses = systemClauseTree.getChildren();
					
					if (contextName.equalsIgnoreCase("Measure Phrase")) {
						return;
					}
	
					for (TraversalTree systemClause: systemClauses) {
						criterionModel = new Clause();
						criterionModel.setName(systemClause.getName());
						criterionModel.setDescription(systemClause.getDescription());
						criterionModel.setCustomName(systemClause.getDiagramObject().getCustomName());
						criterionModel.setMeasureId(MatContext.get().getCurrentMeasureId());						
						Context ctxTest = new Context();
						ctxTest.setDescription(contextName);
						int pos = allContexts.indexOf(ctxTest);
						if(pos<0){
							 System.out.println("ERROR Pos "+pos);
								continue;
						}
						Context fork = allContexts.get(pos);
						String bork = fork.getId();
						System.out.println(bork);
						criterionModel.setContextId(allContexts.get(pos).getId());

						//used only to identify "User-defined" as a "system clause" or a measure phrase
						if (systemClause.isSystemClause() && criterionModel.getContextId().equalsIgnoreCase(ConstantMessages.USER_DEFINED_CONTEXT_ID)) {
							criterionModel.setClauseTypeId("SYSTEM_CLAUSE");
						}

						List<TraversalTree> rules = systemClause.getChildren();
						for (TraversalTree rule: rules) {
							//map this system clause to the backend model.
							criterionModel.getDecisions().add(createDecision(rule, sstcpf));
						}
						clauses.add(criterionModel);
					}
				}
			}
			//we processed all of the dirty flag criterion's
			//now lets reset each of them to false
			criterion.setDirty(false);
		}

	}

	private mat.shared.model.Decision createDecision(
			TraversalTree rule, SimpleStatementToCentralPojoFactory sstcpf) throws Exception {

		mat.shared.model.Decision childDec = null;

		if(rule.getDiagramObject() instanceof Conditional) {
			Conditional uiCond = (Conditional)rule.getDiagramObject();
			mat.shared.model.Conditional cond = new mat.shared.model.Conditional();
			childDec = cond;

			if (uiCond.getIdentity().equalsIgnoreCase("And")) {
				cond.setOperator(Operator.AND);
			} else if (uiCond.getIdentity().equalsIgnoreCase("Or")) {
					cond.setOperator(Operator.OR);
			}
			
		} else if(rule.getDiagramObject() instanceof Rel) {
			
			Rel rel = (Rel)rule.getDiagramObject();
			mat.shared.model.QDSMeasurementTerm qdsMeasure = new QDSMeasurementTerm();
			childDec = qdsMeasure;
			qdsMeasure.setQDSOperator(QDSOperator.valueOf(Rel.longToShortIdentity(rel.getIdentity())));

		} else if(rule.getDiagramObject() instanceof PlaceHolder) {
		
			SimpleStatement phrase = ((PlaceHolder)rule.getDiagramObject()).getSimpleStatement();			
			Clause cl = sstcpf.getClauseFor(phrase);
			childDec = cl;

			cl.setMeasureId(MatContext.get().getCurrentMeasureId());
			if (this.measurePhrases.getItem(phrase.getIdentity())  == null) {
				this.measurePhrases.insert(phrase.getIdentity(), phrase );
			}
		} else if(rule.getDiagramObject() instanceof Qdsel) {
			Qdsel qdsel = (Qdsel)rule.getDiagramObject();
			QDSTerm qdsTerm = new QDSTerm();
			QualityDataSetDTO qds = null;
			for(QualityDataSetDTO qdsT : qdsList){
				if(qdsel.getIdentity().equalsIgnoreCase(qdsT.toString())){
					qds = qdsT;
				}
			}
			childDec = qdsTerm;
			if(qds != null)
				qdsTerm.setqDSRef(qds.getId());
		}

		List<TraversalTree> subs = rule.getChildren();
		for (TraversalTree sub: subs) {
			addChildDecision(childDec, createDecision(sub, sstcpf));
		}

		return childDec;
	}


	private void addChildDecision(mat.shared.model.Decision dec,
			mat.shared.model.Decision child) {

		if (dec instanceof QDSMeasurementTerm) {
			QDSMeasurementTerm qdst = (QDSMeasurementTerm)dec;
			if (qdst.getLfTerm() == null) {
				qdst.setLfQDS((IQDSTerm)child);
			} else {
				qdst.setRtQDS((IQDSTerm)child);
			}
		} else if (dec instanceof mat.shared.model.Conditional) {
			dec.getDecisions().add(child);
		}

		
	}

	public void saveClauses(final List<Clause> clauses, final int index) {
		MatContext.get().getSynchronizationDelegate().setSavingClauses(true);
		rpcService.save(clauses, new AsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				MatContext.get().getSynchronizationDelegate().setSavingClauses(false);
				diagramView.setSaveStatus(true);
				hideLoadingMessage();
			}
			public void onFailure(Throwable e) {
				MatContext.get().getSynchronizationDelegate().setSavingClauses(false);
				e.printStackTrace();
				if(e instanceof StatusCodeException) {
					StatusCodeException sce = (StatusCodeException)e;
					if(sce.getStatusCode() == 0) {
						return;
					}
				}
				diagramView.setSaveStatus(false);
				hideLoadingMessage();
			}	
		});
	}
	
	
	public void clearClauseWorkSpaceMessages(){
		diagramView.clearMessages();
	}
	
	public DiagramViewImpl<Diagram> getDiagramView(){
		return diagramView;
	}
	
	
	
	public void setSavingStatus(){
		diagramView.setSaveStatus(false);
	}
	
	public void reDrawTextView(){
		//This will redraw the diagramTree when we switch between the measures. To prevent the diagramTree being cached.
		diagramView.drawDiagramTree();
	}
	
	public String getCurrentDiagramView(){
		return diagramView.getCurrentDiagramView();
	}
	
	private void diagramToModel(SimpleStatementToCentralPojoFactory sstcpf, List<Clause> clauses, List<String> listOfPhrases) throws Exception {
		String[] phraseNames = null;
		if (listOfPhrases!=null) {
			String[] pns = new String[listOfPhrases.size()];
			listOfPhrases.toArray(pns);
			phraseNames = pns;
		} else  {
			phraseNames = topoligicalSort(measurePhrases.getList());
		}
						
		for (String phraseName: phraseNames) {
			SimpleStatement measurePhrase = (SimpleStatement)(getMeasurePhrase(phraseName));

			Clause cl = sstcpf.getClauseFor(measurePhrase);
			cl.setMeasureId(MatContext.get().getCurrentMeasureId());
			cl.setContextId(measurePhrase.getClauseType() == SimpleStatement.CLAUSE_TYPE.MEASURE_PHRASE
					? SimpleStatement.CLAUSE_TYPE.MEASURE_PHRASE.getClauseType()
					: SimpleStatement.CLAUSE_TYPE.CLAUSE_LIBRARY.getClauseType());

			clauses.add(cl);
		}
	}
			
	
	protected String[] topoligicalSort(List<String> phraseNames) throws Exception {
		TopologicalSort g = new TopologicalSort(getMeasurePhrases(), phraseNames);
		g.topologicalSort();
		return g.getSortedArray();
	}

	public Criterion addCriterion(String criterion) {
		int diagramIndex = criterionNames.indexOf(criterion);
		Diagram diagram = diagrams[diagramIndex];
		diagram.setDirty(true);
		
		String criterionIdentity = MatContext.get().getCurrentShortName() + "_" + criterion;
		
		int count = diagram.getTree().getChildren().size()+1;
		Criterion criterionDiagramObject = new Criterion(criterionIdentity, count);
//		Criterion criterionDiagramObject = new Criterion(criterion, count));
//		criterionDiagramObject.setCustomName(criterionIdentity);
		diagram.getTree().addDiagramObject(criterionDiagramObject);
		return criterionDiagramObject;
	}
	
	public void addCriterion(String criterion, TraversalTree criterionTree) {
		// Necessary to CLONE the criterionTree  (clone of a clone!) - if the user pastes the same criterionTree twice, it
		// must not be the same criterionTree, otherwise the two criteria will have the same children.
		TraversalTree clone = criterionTree.clone(this);
		
		int diagramIndex = criterionNames.indexOf(criterion);
		String diagramName = MatContext.get().getCurrentShortName() + "_" + criterionNames.get(diagramIndex);
		Diagram diagram = diagrams[diagramIndex];
		diagram.setDirty(true);
		
		DiagramObject diagramObject = clone.getDiagramObject();
		int count = diagram.getTree().getChildren().size()+1;
		diagramName = diagramName + count;
		clone.setName(diagramName);
		diagramObject.setIdentity(diagramName);
		
		TraversalTree tree = findCriterion(criterion);
		tree.addChild(clone);
		diagramView.drawDiagram();
		
	}
	
	public boolean deleteCriterion(String criterion, DiagramObject diagramObject) {
		/**
		 * TODO Do not execute if saved in the database 
		 */
		int diagramIndex = criterionNames.indexOf(criterion);	
		Diagram diagram = diagrams[diagramIndex];
		diagram.setDirty(false);
		
		TraversalTree child = diagram.getTree().findDiagramObject(diagramObject);
		if (child == null) return false;
		
		TraversalTree parent = child.getParent();
		if (parent == null) return false;
		
		child.deleteChildren();	
		parent.deleteChild(child);
		
		int index = 1;
		for (TraversalTree c : parent.getChildren()) {
			Criterion criterionObject = (Criterion)(c.getDiagramObject());
			criterionObject.setIndex(index);
			criterionObject.setIdentity(MatContext.get().getCurrentShortName() + "_" + criterion + index);
			++index;
		}
		
		diagramView.drawDiagram();	
		return true;
	}

	public boolean criterionHasOnlyOneChild(String criterion) {
		int diagramIndex = criterionNames.indexOf(criterion);
		return diagrams[diagramIndex].getTree().getChildren().size() == 1;
	}
	
	public String getCriterionFor(DiagramObject diagramObject) {
		if (diagramObject instanceof CriterionParent) {
			return diagramObject.getIdentity();	
		}
		TraversalTree tree = findDiagramObject(diagramObject);
		for (TraversalTree parent = tree.getParent(); ; parent = parent.getParent())  {
			if (parent.getParent() == null) {
				DiagramObject rootDiagramObject = parent.getDiagramObject(); 
				return rootDiagramObject.getIdentity();	
			}
		}
	}
	
	public TraversalTree findDiagramObject(DiagramObject diagramObject) {
		for (int diagramIndex = 0; diagramIndex < criterionNames.size(); ++diagramIndex) {
			TraversalTree tree = findDiagramObject(diagramIndex, diagramObject);
			if (tree != null)
				return tree;
		}
		return null;
	}
	
	public TraversalTree findCriterion(String criterion) {
		int diagramIndex = criterionNames.indexOf(criterion);
		if (diagramIndex < 0)
			return null;
		Diagram diagram = diagrams[diagramIndex];
		return diagram.getTree();
	}
	
	public TraversalTree findDiagramObject(String criterion, DiagramObject diagramObject) {
		return findDiagramObject(criterionNames.indexOf(criterion), diagramObject);
	}
	
	public TraversalTree findDiagramObject(int diagramIndex, DiagramObject diagramObject) {
		//return null if this is trying to acces a measure phrase or invalid index
		if(diagramIndex >= diagrams.length || diagramIndex < 0)
			return null;
		Diagram diagram = diagrams[diagramIndex];
		TraversalTree tree = diagram.getTree().findDiagramObject(diagramObject);
		return tree;
	}
	
	public boolean isDiagramObject(String criterion, DiagramObject diagramObject) {
		return isDiagramObject(criterionNames.indexOf(criterion), diagramObject);
	}
	
	public boolean isDiagramObject(int diagramIndex, DiagramObject diagramObject) {
		Diagram diagram = diagrams[diagramIndex];
		TraversalTree tree = diagram.getTree().findDiagramObject(diagramObject);
		return (tree != null);
	}
	
	// use this for UI, not RPC
	public DiagramObject addDiagramObject(String criterion, DiagramObject parent, DiagramObject diagramObject) {
		int diagramIndex = criterionNames.indexOf(criterion);
		if (diagramIndex < 0)
			return null;
		diagrams[diagramIndex].setDirty(true);
		
		TraversalTree tree = getTreeForDiagramObject(criterion, parent);
		if (tree == null) return null;
		tree.addDiagramObject(diagramObject);
		diagramView.drawDiagram();
		return diagramObject;
	}
	
	// use this for RPC, not UI
	public void loadDiagramObject(String criterion, DiagramObject parent, DiagramObject diagramObject) {
		TraversalTree tree = getTreeForDiagramObject(criterion, parent); 		// e.g. criterion = "Population", parent = population1
		if (tree == null) return;																// throw an exception?
		tree.addDiagramObject(diagramObject); 											// e.g. diagramObject is a Conditional("And")
	}
	
	public void updateDiagramObject(String criterion, DiagramObject currentDiagramObject, DiagramObject newDiagramObject) {
		if (!setDiagramDirty(criterion))
			return;
		
		TraversalTree tree = getTreeForDiagramObject(criterion, currentDiagramObject);
		if (tree == null) return;
		tree.setDiagramObject(newDiagramObject);
		diagramView.drawDiagram();
	}
	
	public void copy(String criterion, DiagramObject diagramObject, String newName) {
		if (!setDiagramDirty(criterion))
			return;
		
		TraversalTree tree = getTreeForDiagramObject(criterion, diagramObject);
		
		SimpleStatement s1 = (diagramObject instanceof SimpleStatement) 
			? (SimpleStatement)diagramObject
			: ((PlaceHolder)diagramObject).getSimpleStatement();
		
		SimpleStatement simpleStatement = (SimpleStatement)(new SimpleStatement(this).clone(s1));
		simpleStatement.setIdentity(newName);
		if (tree == null)
			addTransformedMeasurePhrase(simpleStatement);
		else
			addTransformedMeasurePhrase(tree, simpleStatement, newName);
	}

	public DiagramObject createSecondOrderSimpleStatement(String criterion, DiagramObject diagramObject, String newName) {
		if (!setDiagramDirty(criterion))
			return null;
		
		TraversalTree tree = getTreeForDiagramObject(criterion, diagramObject);

		SimpleStatement s1 = (diagramObject instanceof SimpleStatement) 
			? (SimpleStatement)diagramObject
			: ((PlaceHolder)diagramObject).getSimpleStatement();
		SimpleStatement s2 = (SimpleStatement)(s1.clone());
		s2.createSecondOrderSimpleStatement(newName);
		if (tree == null)
			addTransformedMeasurePhrase(s2);
		else
			addTransformedMeasurePhrase(tree, s2, newName);
		
		return s2;
	}

	private void addTransformedMeasurePhrase(SimpleStatement simpleStatement) {
		addMeasurePhrase(simpleStatement);
		diagramView.refreshMeasurePhrases(getMeasurePhraseList());
	}
	private void addTransformedMeasurePhrase(TraversalTree tree, SimpleStatement simpleStatement, String newName) {
		addTransformedMeasurePhrase(simpleStatement);
		
		PlaceHolder placeholder = new PlaceHolder(simpleStatement);
		TextRectangle textRectangle = (TextRectangle) tree.getDiagramShape();
		textRectangle.setDiagramObject(placeholder);
		textRectangle.setCaption(newName);
		textRectangle.getRectangle().addClickHandler(new PlaceholderClickHandler(diagramView, simpleStatement).getClickHandler());
		tree.setDiagramObject(placeholder);
		tree.setDiagramShape(textRectangle);
		diagramView.drawDiagram();
	}
	
	public DiagramObject transformQdselToMeasurePhrase(String criterion, DiagramObject diagramObject, String newName) {
		TraversalTree tree = getTreeForDiagramObject(criterion, diagramObject);
		
		SimpleStatement simpleStatement = new SimpleStatement(this).transformQdselToSimpleStatement(diagramObject, newName);
		simpleStatement.setView(diagramView);
		if (tree == null)
			addTransformedMeasurePhrase(simpleStatement);
		else
			addTransformedMeasurePhrase(tree, simpleStatement, newName);
		
		return simpleStatement;
	}

	private TraversalTree getTreeForDiagramObject(String criterion, DiagramObject diagramObject) {
			int diagramIndex = criterionNames.indexOf(criterion);
			Diagram diagram = diagrams[diagramIndex];
			TraversalTree tree = diagram.getTree().findDiagramObject(diagramObject);
			if (tree == null)
				System.out.println("Can't find tree for " + diagramObject.getIdentity()
						+" "+ new Throwable().getStackTrace()[1].getFileName()
						+" line "+ new Throwable().getStackTrace()[1].getLineNumber());
			return tree;
	}
		
	public boolean deleteDiagramObject(String criterion, DiagramObject diagramObject) {
		int diagramIndex = criterionNames.indexOf(criterion);	
		Diagram diagram = diagrams[diagramIndex];
		if (!setDiagramDirty(criterion))
			return false;
		
		TraversalTree child = diagram.getTree().findDiagramObject(diagramObject);
		if (child == null) return false;
		
		TraversalTree parent = child.getParent();
		if (parent == null) return false;
		
		child.deleteChildren();
		parent.deleteChild(child);
		diagramView.drawDiagram();
		return true;
	}
	
	public void addMeasurePhrase(DiagramObject diagramObject) {
		SimpleStatement simpleStatement = (SimpleStatement)diagramObject;
		measurePhrases.insert(diagramObject.getIdentity(), diagramObject);
		measurePhrases.insertSavedItem(diagramObject.getIdentity(), diagramObject);
	}
	
	public Integer getCriterionIndex(String criterion) {
		return criterionNames.indexOf(criterion);
	}	
	
	public MeasurePhrases getMeasurePhrases() {
		return measurePhrases;
	}
	
	public DiagramObject getMeasurePhrase(String identity) {
		return measurePhrases.getItem(identity);
	}
	
	public DiagramObject getSavedMeasurePhrase(String identity) {
		return measurePhrases.getSavedItem(identity);
	}
	
	public boolean isMeasurePhrase(String identity) {
		return measurePhrases.isMeasurePhrase(identity);
	}
	
	public List<String> getMeasurePhraseList() {
		return measurePhrases.getList();
	}
     
	public MeasurePhrases getCurrentMeasurePhrases() {
		return measurePhrases;
	}
	
	public void updateSavedMeasurePhrase(String identity,DiagramObject diagObj){
		 measurePhrases.addToSavedList(identity, diagObj);
	}
	
	public void updateMeasurePhraseMap(String identity,DiagramObject diagObj){
		measurePhrases.updateMPMap(identity, diagObj);
	}
	
	public String getMeasureName() {	
		return measureName;
	}
	
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}
	
	public Diagram[] getDiagrams() {
		return diagrams;
	}
	
	public Diagram getCurrentDiagram(String criterion) {
		return (currentDiagram = diagrams[criterionNames.indexOf(criterion)]);
	}
	
	public Diagram getNewDiagram() {
		return (currentDiagram = new Diagram());
	}
	
	public DiagramObject getCurrentDiagramObject() {
		return currentDiagramObject;
	}

	public void setCurrentDiagramObject(DiagramObject currentDiagramObject) {
		this.currentDiagramObject = currentDiagramObject;
	}	

	public void copyToClauseLibrary(DiagramObject diagramObject, String newName) {
		SimpleStatement simpleStatement = (SimpleStatement) ((SimpleStatement)diagramObject).clone();
		simpleStatement.setIdentity(newName);
		simpleStatement.setClauseType(SimpleStatement.CLAUSE_TYPE.CLAUSE_LIBRARY);
		addMeasurePhrase(simpleStatement);
	}
	
	private ClauseCloningServiceAsync createClauseCloningService() {
		return (ClauseCloningServiceAsync) GWT.create(ClauseCloningService.class);
	}

    //TODO:- Need to remove the following method ,since it is not used. This invloves removing of another clone service method from the clausecloningservice
	//--vasugi 08/21/2011
	/*public void copyToMeasurePhraseLibrary(DiagramObject diagramObject, String newName) {
		final String clauseName = newName;
		createClauseCloningService().clone(MatContext.get().getCurrentMeasureId(), 
				getClause(diagramObject.getIdentity()), newName, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				System.out.println("Error caught" + caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result.booleanValue()) {
					loadRule(clauseName);
				}
			}
		});
	}*/
	
	public void cloneSystemClause(String clauseNameToBeCloned, String criterionNameToClone, String cloneFromMeasureId) {
		String identity = null;
		identity = getTrueIdentity(clauseNameToBeCloned);
//		String cloneFromMeasureId = getCloneFromMeasureId(identity);
		String cloneToMeasureId = MatContext.get().getCurrentMeasureId();
		final String clonedClauseName = getNewClauseName(criterionNameToClone);
		
		createClauseCloningService().clone(cloneFromMeasureId, cloneToMeasureId, 
				identity, clonedClauseName, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				System.out.println("Error caught" + caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result.booleanValue()) {
					resetAllData();
					loadAllContexts();
					//saveMainPhrases();
				}
			}
		});
	}
	
	private String getTrueIdentity(String identity) {
		for(Clause c : usersOtherSystemClauses) {
			if (c.getName().equalsIgnoreCase(identity)) {
				return c.getName();
			}else if(c.getCustomName()!=null && c.getCustomName().equalsIgnoreCase(identity)) {
				return c.getName();
			}
		}
		return null;
	}
	
	public String getNewClauseName(String criterionNameToClone) {
		String shortName =MatContext.get().getCurrentShortName();
		String nextVal = getNextCriterionValue(criterionNameToClone)+"";

		return shortName + "_" + criterionNameToClone + nextVal;
	}
	
	private int getNextCriterionValue(String criterionName) {
		Diagram[] criteriaTypes = getDiagrams();
		int nextValue = 1;
		for (Diagram criterion: criteriaTypes) {			
			TraversalTree systemClauseTree = criterion.getTree();
			if (systemClauseTree.getDiagramObject() != null) {
				List<TraversalTree> systemClauses = systemClauseTree.getChildren();
				if (systemClauseTree.getDiagramObject().getIdentity().equalsIgnoreCase(criterionName)) {
//					boolean lastSystemClauseIsEmpty = false;
					for (TraversalTree systemClause: systemClauses) {
//						List<TraversalTree> rules = systemClause.getChildren();
//						if (rules.isEmpty()) {
//							lastSystemClauseIsEmpty = true;
//						}
						nextValue++;
					}
//					if (lastSystemClauseIsEmpty) {
//						nextValue = nextValue - 1;
//					}
					break;
				}
			}
		}
		return nextValue;

	}

	
	
	private String getCloneFromMeasureId(String clauseNameToBeCloned) {
		for (Clause c : usersOtherSystemClauses) {
			if (c.getName().equalsIgnoreCase(clauseNameToBeCloned)) {
				return c.getMeasureId();
			}
		}
		return null;
	}
	
	/**
	 * api to control phrase and clause library selection and handlers
	 */
	private void disableLibraries(){
		diagramView.disableLibraries(CLASS_NAME);
	}
	//US201 exposing access to the Phrase Library tab
	private void selectPhraseLibrary(){
		diagramView.selectPhraseLibrary();
	}
	/**
	 * api to control phrase and clause library selection and handlers
	 */
	private void enableLibraries(){
		diagramView.enableLibraries("AppController");
	}
	
	private void refreshLibaryAndPhrases() {
		
		diagramView.refreshSystemClauses( usersOtherSystemClauses);
		diagramView.refreshMeasurePhrases(measurePhrases.getList());
	}
	
	//A Clause should only start with only one AND/OR
	public boolean canAddConditional(DiagramObject parent) {
		if (parent instanceof Criterion)  {	
			TraversalTree tree = findDiagramObject(parent); 
			for (TraversalTree c : tree.getChildren())
				if (c.getDiagramObject() instanceof Conditional)
					return false;
		}
		return true;
	}

	public boolean isEditable() {
		return editable;
	}

	
	public void addClauseToLibList(String clauseName, String measureId, String customName) {
		Clause cl = new Clause();
		cl.setName(clauseName);
		if (clauseName.contains("User-defined")) {
			cl.setName(clauseName);
			cl.setCustomName(customName);
		}
		cl.setMeasureId(measureId);
		
		boolean found = false;
		for(Clause c : usersOtherSystemClauses) {
			if (c.getName().equalsIgnoreCase(clauseName)) {
				found = true;
				break;
			}
		}
		if (!found) {
			usersOtherSystemClauses.add(cl);
		}
		refreshLibaryAndPhrases();
	}
	
	public boolean canDeleteClause(Criterion criterion){
		if(this.clauses.get(criterion.getIdentity()) == null)
			return true;
		return false;
	}
	
	/**
	 * loading message hub to better track where showLoadingMessage is invoked locally
	 */
	public void showLoadingMessage(){
		Mat.showLoadingMessage();
	}
	/**
	 * hide message hub to better track where hideLoadingMessage is invoked locally
	 */
	public void hideLoadingMessage(){
		Mat.hideLoadingMessage();
	}
	
	public ClauseController getClauseController() {
		return clauseController;
	}
}	



