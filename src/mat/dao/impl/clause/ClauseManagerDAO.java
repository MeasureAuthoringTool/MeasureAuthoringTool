package mat.dao.impl.clause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import mat.dao.service.DAOService;
import mat.model.DataType;
import mat.model.QualityDataSet;
import mat.model.clause.Clause;
import mat.model.clause.Decision;
import mat.model.clause.Measure;
import mat.model.clause.QDSAttributes;
import mat.server.util.MeasureUtility;
import mat.shared.ConstantMessages;
import mat.shared.StringUtility;
import mat.shared.model.AttributeTerm;
import mat.shared.model.Conditional;
import mat.shared.model.FunctionTerm;
import mat.shared.model.IQDSTerm;
import mat.shared.model.MeasurementTerm;
import mat.shared.model.QDSMeasurementTerm;
import mat.shared.model.QDSTerm;
import mat.shared.model.StatementTerm;
import mat.shared.model.util.OperatorConverter;
import mat.shared.model.util.TermUtil;

import org.springframework.context.ApplicationContext;

public class ClauseManagerDAO {

private final int lfOrder = 0;
private final int rtOrder = 1;
private OperatorConverter oc = new OperatorConverter();
private TermUtil tu = new TermUtil();
private final String QDS_TERM = "QDSTERM";
private final String MEASUREMENT_TERM = "MEASUREMENTTERM";
private final String QDS_ATTRIBUTE = "QDSATTRIBUTE";
private final String A_NOT_EMPTY = "A_NOT_EMPTY";
private final String CLAUSE = "CLAUSE";
private DecisionDAO decisionDAO = new DecisionDAO();
private QDSTermDAO qDSTermDAO = new QDSTermDAO();
private MeasurementTermDAO measurementTermDAO = new MeasurementTermDAO();
private DAOService dAOService = null;
private ClauseDAO clauseDAO = new ClauseDAO();
private MeasureDAO measureDAO = new MeasureDAO();
private ApplicationContext context = null;
private QDSAttributesDAO qDSAttributesDAO = new QDSAttributesDAO();
private QDSAttributeDetailsDAO qDSAttributeDetailsDAO = new QDSAttributeDetailsDAO();
private Measure measureLtForCloning = null;
private Measure measureLtForQDSTerm = null;

private final String SYSTEM_CLAUSE = "SYSTEM_CLAUSE";


public ClauseManagerDAO() {
}

public ClauseManagerDAO(DAOService dAOService) {
	//this constructor is used for testing or getting dao's via DAOService
	this.dAOService = dAOService;
}

public ClauseManagerDAO(ApplicationContext context) {
	this.context = context;
}

public mat.dao.QualityDataSetDAO getQdsDAO() {
	return (mat.dao.QualityDataSetDAO)context.getBean("qualityDataSetDAO");
}

public mat.dao.clause.QDSAttributeDetailsDAO getqDSAttributeDetailsDAO() {
	if (dAOService!=null) return dAOService.getqDSAttributeDetailsDAO();
	if (context!=null) return (mat.dao.clause.QDSAttributeDetailsDAO)context.getBean("qDSAttributeDetailsDAO");
	return qDSAttributeDetailsDAO;
}

public mat.dao.clause.QDSAttributesDAO getqDSAttributesDAO() {
	if (dAOService!=null) return dAOService.getqDSAttributesDAO();
	if (context!=null) return (mat.dao.clause.QDSAttributesDAO)context.getBean("qDSAttributesDAO");
	return qDSAttributesDAO;
}

public mat.dao.clause.QDSTermDAO getqDSTermDAO() {
	if (dAOService!=null) return dAOService.getqDSTermDAO();
	if (context!=null) return (mat.dao.clause.QDSTermDAO)context.getBean("qDSTermDAO");
	return qDSTermDAO;
}

public mat.dao.clause.MeasurementTermDAO getMeasurementTermDAO() {
	if (dAOService!=null) return dAOService.getMeasurementTermDAO();
	if (context!=null) return (mat.dao.clause.MeasurementTermDAO)context.getBean("measurementTermDAO");
	return measurementTermDAO;
}

public mat.dao.clause.DecisionDAO getDecisionDAO() {
	if (dAOService!=null) return dAOService.getDecisionDAO();
	if (context!=null) return (mat.dao.clause.DecisionDAO)context.getBean("decisionDAO");
	return decisionDAO;
}
public mat.dao.clause.ClauseDAO getClauseDAO() {
	if (dAOService!=null) return dAOService.getClauseDAO();
	if (context!=null) return (mat.dao.clause.ClauseDAO)context.getBean("clauseDAO");
	return clauseDAO;
}

public mat.dao.clause.MeasureDAO getMeasureDAO() {
	if (dAOService!=null) return dAOService.getMeasureDAO();
	if (context!=null) return (mat.dao.clause.MeasureDAO)context.getBean("measureDAO");
	return measureDAO;
}

public boolean isFunction(String symbol) {
	return tu.isFunctionTerm(symbol);
}
public boolean isStatementOperator(String symbol) {
	return tu.isStatementTermOperator(symbol);
}

public boolean isAttributeOperator(String symbol) {
	return tu.isAtttibuteTermOperator(symbol);
}

public boolean isQDSMeasurementOperator(String symbol) {
	return tu.isQDSMeasurementTermOperator(symbol);
}


public boolean isQDSTermOperation(String symbol) {
	return tu.isQDSTermOperation(symbol);
}
public boolean isMeasurementTermOperation(String symbol) {
	return tu.isMeasurementTermOperation(symbol);
}


private String getId() {
	return UUID.randomUUID().toString();
}

//****************************LOAD*********************************
	public mat.shared.model.Decision loadClause(String parentId)  {
		Decision topLevelDBDecision = new Decision();
		if (dAOService!=null) {
			topLevelDBDecision = dAOService.getDecisionDAO().find(parentId);
		} else {
			dAOService = (DAOService)context.getBean("daoService");
			topLevelDBDecision = dAOService.getDecisionDAO().find(parentId);
		}

		return preLoad(topLevelDBDecision);
	}
	
	public mat.shared.model.Decision loadClause(String parentId, ApplicationContext ctx)  {
		this.context = ctx;
		return loadClause(parentId);
	}
	private mat.shared.model.Decision preLoad(Decision topLevelDBDecision)  {
		String op = topLevelDBDecision.getOperator();
		
		//this will determine what the top level shared pojo will be
		if (isStatementOperator(op)) {
			StatementTerm st = null;
			st = new StatementTerm();
			st.setOperator(oc.toStatementTermOp(op));
			loadAllChild(st, topLevelDBDecision);
			return st;
		} else if (isAttributeOperator(op)) {
			AttributeTerm at = new AttributeTerm();
			at.setOperator(oc.toAttributeTermOp(op));
			loadAllChild(at, topLevelDBDecision);
			return at;
		} else if (isQDSMeasurementOperator(op)) {
			QDSMeasurementTerm qmt = new QDSMeasurementTerm();
			qmt.setOperator(oc.toStatementTermOp(op));
			loadAllChild(qmt, topLevelDBDecision);
			return qmt;
		} else if (isFunction(op)) {
			FunctionTerm ft = new FunctionTerm();
			ft.setName(topLevelDBDecision.getOperator());
			loadAllChild(ft, topLevelDBDecision);
			return ft;
		} else if (tu.isQDSTermOperation(op)) {
			QDSTerm qt = getqDSTermDAO().findByDecisionId(topLevelDBDecision.getId());
			loadAllChild(qt, topLevelDBDecision);
			return qt;
		} else if (tu.isMeasurementTermOperation(op)) {
			MeasurementTerm mt = getMeasurementTermDAO().findByDecisionId(topLevelDBDecision.getId());
			return mt;
		} else if (tu.isConditional(op)) {
			Conditional conditional = new Conditional();
			loadAllChild(conditional, topLevelDBDecision);
			return conditional;
		} else if (tu.isClauseOperation(op)) {
			Clause clause = new Clause();
			loadAllChild(clause, topLevelDBDecision);
			return clause;
		}
		//Unrecognized decision type, return null
		return null;		
	}
	
	private void loadAllChild(mat.shared.model.Decision currentTopLevelCentralPojo, Decision topLevelDBDecision) {
		List<Decision> listOfChildren = dAOService.getDecisionDAO().findByParentId(topLevelDBDecision.getId());
		if (listOfChildren.isEmpty()) return;
		
		for (Decision dbChild: listOfChildren) {
			String dbChildOp = dbChild.getOperator();

			if (tu.isQDSTermOperation(dbChildOp)) {
				//QDS TERM
				QDSTerm qt = getqDSTermDAO().findByDecisionId(dbChild.getId());
				setChildToParent(currentTopLevelCentralPojo, topLevelDBDecision, dbChild, qt);
				loadAllChild(qt, dbChild);
			} else if (tu.isMeasurementTermOperation(dbChildOp)) {
				//MEASUREMENT TERM				
				MeasurementTerm mt = getMeasurementTermDAO().findByDecisionId(dbChild.getId());
				if (currentTopLevelCentralPojo instanceof QDSMeasurementTerm) {
					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder+"")) {
						//((QDSMeasurementTerm)d).setLfQDS(mt);
						((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfTerm(mt);
					} else {
						//((QDSMeasurementTerm)d).setRtQDS(mt);
						((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtTerm(mt);
					}
				} else if (currentTopLevelCentralPojo instanceof StatementTerm) {
					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder+"")) {
						((StatementTerm)currentTopLevelCentralPojo).setLfTerm(mt);
					} else {
						((StatementTerm)currentTopLevelCentralPojo).setRtTerm(mt);
					}
				} else if (currentTopLevelCentralPojo instanceof FunctionTerm) {
					((FunctionTerm)currentTopLevelCentralPojo).setTerm(mt);
				} else if (currentTopLevelCentralPojo instanceof AttributeTerm) {
					if (dbChild.getOrderNum().equalsIgnoreCase(rtOrder+"")) {
						((AttributeTerm)currentTopLevelCentralPojo).setRtTerm(mt);
					}
				}
				loadAllChild(mt, dbChild);
			} else if (isQDSMeasurementOperator(dbChildOp)) {
				//QDSMEASUREMENT TERM
				QDSMeasurementTerm qmt = new QDSMeasurementTerm();
				qmt.setQDSOperator(oc.toQDSMeasurementTermOp(dbChildOp));
				setChildToParent(currentTopLevelCentralPojo, topLevelDBDecision, dbChild, qmt);					
				loadAllChild(qmt, dbChild);

			} else if (isStatementOperator(dbChildOp)) {
				//STATEMENT TERM
				StatementTerm st = new StatementTerm();
				st.setOperator(oc.toStatementTermOp(dbChildOp));
				setChildToParent(currentTopLevelCentralPojo, topLevelDBDecision, dbChild, st);
				loadAllChild(st, dbChild);
				
			} else if (tu.isAtttibuteTermOperator(dbChildOp)) {
				//ATTRIBUTE TERM				
				AttributeTerm at = new AttributeTerm();
				at.setOperator(oc.toAttributeTermOp(dbChildOp));
				setChildToParent(currentTopLevelCentralPojo, topLevelDBDecision, dbChild, at);					
				loadAllChild(at, dbChild);
				
			} else if (tu.isAttributeTermOperation(dbChildOp)) {
				//ATTRIBUTETERM operation (the actual attribute name)			
				
				//db child row should contain the attribute id
				String attributeId = dbChild.getAttributeId();

				if (attributeId!=null) {
					QDSAttributes qa = getqDSAttributesDAO().find(attributeId);
					((AttributeTerm)currentTopLevelCentralPojo).setLfTerm(qa.getName());
				}
						
			} else if (tu.isConditional(dbChildOp)) {
				//CONDITIONAL
				Conditional cn = new Conditional();
				cn.setOperator(oc.toConditionalOp(dbChildOp));
				setChildToParent(currentTopLevelCentralPojo, topLevelDBDecision, dbChild, cn);
				loadAllChild(cn, dbChild);
				
			} else if (isFunction(dbChildOp)) {
				//FUNCTION TERM
				FunctionTerm ft = new FunctionTerm();
				ft.setName(dbChildOp);
				setChildToParent(currentTopLevelCentralPojo, topLevelDBDecision, dbChild, ft);
				loadAllChild(ft, dbChild);
				
			} else if (tu.isClauseOperation(dbChildOp) && dbChild.getClauseId()!=null) {
				//CLAUSE
				//use clauseid to get decisionid
				String clauseId = dbChild.getClauseId();
				Clause clause = getClauseDAO().find(clauseId);
				String decisionId = clause.getDecision().getId();
				
				Decision innerClauseDecision = getDecisionDAO().find(decisionId);
				mat.shared.model.Decision sd = preLoad(innerClauseDecision);
				List<mat.shared.model.Decision> decisions = new ArrayList<mat.shared.model.Decision>();
				decisions.add(sd);
				clause.setDecisions(decisions);
				//Stop here!
				if (currentTopLevelCentralPojo instanceof QDSMeasurementTerm) {
					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder+"")) {
						((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfQDS(clause);
					} else {
						((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtQDS(clause);
					}
				} else if (currentTopLevelCentralPojo instanceof StatementTerm) {
					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder+"")) {
						((StatementTerm)currentTopLevelCentralPojo).setLfTerm(clause);
					} else {
						((StatementTerm)currentTopLevelCentralPojo).setRtTerm(clause);
					}
				} else if (currentTopLevelCentralPojo instanceof FunctionTerm) {
					((FunctionTerm)currentTopLevelCentralPojo).setTerm(clause);
				} else if (currentTopLevelCentralPojo instanceof Conditional) {
					((Conditional)currentTopLevelCentralPojo).getDecisions().add(clause);
				} else if (currentTopLevelCentralPojo instanceof Clause) {
					((Clause)currentTopLevelCentralPojo).getDecisions().add(clause);
				}			
			}
		}
	}
	
	private void setChildToParent(mat.shared.model.Decision currentTopLevelCentralPojo, 
			Decision topLevelDBDecision, Decision dbChild, IQDSTerm currentChild) {

		String dbOp = topLevelDBDecision.getOperator();
		
		if (isQDSMeasurementOperator(dbOp)) {
			if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder+"")) {
				((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfQDS(currentChild);
			} else {
				((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtQDS(currentChild);
			}
			
		} else if (isStatementOperator(dbOp)) {
			if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder+"")) {
				((StatementTerm)currentTopLevelCentralPojo).setLfTerm(currentChild);
			} else {
				((StatementTerm)currentTopLevelCentralPojo).setRtTerm(currentChild);
			}
			
		} else if (isAttributeOperator(dbOp)) {
			//left side of AttributeTerm take a string, must set manually
			if (dbChild.getOrderNum().equalsIgnoreCase(rtOrder+"")) {
				((AttributeTerm)currentTopLevelCentralPojo).setRtTerm(currentChild);
			}			
		} else if (isFunction(dbOp)) {
			((FunctionTerm)currentTopLevelCentralPojo).setTerm(currentChild);
			
		} else if (isQDSTermOperation(dbOp)) {
			((QDSTerm)currentTopLevelCentralPojo).getAttributes().add(currentChild);
			
		} else if (tu.isConditional(dbOp)) {
			((Conditional)currentTopLevelCentralPojo).getDecisions().add(currentChild);
			
		} else if (tu.isClauseOperation(dbOp)) {
			Clause parentClause = ((Clause)currentTopLevelCentralPojo);
			if (parentClause.getDecisions()!=null) {
				parentClause.getDecisions().add(currentChild);
			} else {
				List<mat.shared.model.Decision> decisions = new ArrayList<mat.shared.model.Decision>();
				decisions.add(currentChild);
				parentClause.setDecisions(decisions);
			}
		}
	}
		
	//****************************SAVE*********************************
	//save - process top level parent
      public void saveClause(Clause clausePojo) {
		String clauseName = clausePojo.getName();
		String clauseMeasureId = clausePojo.getMeasureId();
		String changedName = clausePojo.getChangedName();
		String clauseId = clausePojo.getId();
		Clause foundClause  = null;
		
		if(clausePojo.getContextId().equalsIgnoreCase(ConstantMessages.MEASURE_PHRASE_CONTEXT_ID)){
			 //look for existing Phrase
			if(clauseId != null){
			    foundClause = getClauseDAO().findByClauseIdAndMeasureId(clauseId, clauseMeasureId);
			}else{
				foundClause = getClauseDAO().findByNameAndMeasureId(clauseName,clauseMeasureId);//This is for the first time insert
			}
			
			if(foundClause != null){
//				foundClause.setStatusId(clausePojo.getStatusId());//US 601 persisting status value//MAT-286 - Removing Status Field from Measure Phrase. 01/2013		
				if(changedName != null && !changedName.equalsIgnoreCase(clauseName))//Change the name to the new Changed Name
					foundClause.setName(changedName);
			}
			
		}else{
			//look for existing System Clause.
			foundClause = getClauseDAO().findByNameAndMeasureId(clauseName, clauseMeasureId);
		}
		//create a new parent row in decision table
		Decision dbTopLevelParent = new Decision();
		dbTopLevelParent.setId(getId());//create a new id		
		dbTopLevelParent.setOperator(CLAUSE);
		
		String formattedClauseVersion = "";
		
		//Find the measure version to store the version number to the clause
		if(clauseMeasureId != null && !clauseMeasureId.equalsIgnoreCase("")){
			Measure clauseMeas = getMeasureDAO().find(clauseMeasureId);
			formattedClauseVersion = MeasureUtility.getClauseLibraryVersionPrefix(clauseMeas.getVersion(), clauseMeas.isDraft());
		}
		
		clausePojo.setVersion(formattedClauseVersion);
		if (foundClause!=null) {
			
			foundClause.setDescription(clausePojo.getDescription());
			foundClause.setVersion(clausePojo.getVersion());
			//delete all existing decisions/QDSTerms/MeasurementTerms
			//will rebuild with new keys using clause object
			//this ensures updates to occur correctly
			if(foundClause.getDecision()!= null){
				deleteDecisions(foundClause.getDecision().getId());
			}
			//update existing clause - set realClause with the new decision id
			foundClause.setDecision(dbTopLevelParent);
			//update existing clause row with new decision id
			getClauseDAO().save(foundClause);
		} else {
			//saving new clause
			clausePojo.setDecision(dbTopLevelParent);
			//create a new row in clause table
			getClauseDAO().save(clausePojo);
		}
		
		//container for qdsterms and measurementterms
		List<Object> terms = new ArrayList<Object>();
		
		//added child to parent
		//this will add child rows into decision table
		for (mat.shared.model.Decision decisionPojo : clausePojo.getDecisions()) {
			saveChildDecision(decisionPojo, dbTopLevelParent, terms, lfOrder, null);			
		}
		
	    //SAVE TO DB//
		getDecisionDAO().save(dbTopLevelParent);
		//save qdsterms and measurementterms
		for (Object obj : terms) {
			if (obj instanceof QDSTerm) {
				getqDSTermDAO().save((QDSTerm)obj);
			} else if (obj instanceof MeasurementTerm) {
				getMeasurementTermDAO().save((MeasurementTerm)obj);
			}
		}
	}


	
	//save - process child
	private void saveChildDecision(mat.shared.model.Decision decisionPojo, Decision dbTopLevelParent, List<Object> terms, int orderNum, String dataTypeName) {
		if (decisionPojo==null) return;
		
		//handle all child terms/conditionals recursively
		if (decisionPojo instanceof QDSMeasurementTerm) {
			QDSMeasurementTerm qDSMeasurementTerm = (QDSMeasurementTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(oc.qDSMeasureTermToString(qDSMeasurementTerm.getQDSOperator()));
			newDbChild.setOrderNum(orderNum+"");
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			saveChildDecision(qDSMeasurementTerm.getLfTerm(), newDbChild, terms, lfOrder, null);//handle children
			saveChildDecision(qDSMeasurementTerm.getRtTerm(), newDbChild, terms, rtOrder, null);//handle children
			
		} else if (decisionPojo instanceof StatementTerm) {
			StatementTerm statementTerm = (StatementTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(oc.statementTermToString(statementTerm.getOperator()));
			newDbChild.setOrderNum(orderNum+"");
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			saveChildDecision(statementTerm.getLfTerm(), newDbChild, terms, lfOrder, null);//handle children
			saveChildDecision(statementTerm.getRtTerm(), newDbChild, terms, rtOrder, null);//handle children
			
		} else if (decisionPojo instanceof QDSTerm) {
			QDSTerm qDSTerm = (QDSTerm) decisionPojo;
			
			String qdmid = qDSTerm.getqDSRef();
			String dtid = getDataTypeId(qdmid);			
			
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(QDS_TERM);
			newDbChild.setOrderNum(orderNum+"");
			//for Decision table
			dbTopLevelParent.getChildDecisions().add(newDbChild);			
			
			if (measureLtForCloning!=null) {
				QualityDataSet qds = getQdsDAO().find(qDSTerm.getqDSRef());
				String listObjectId = qds.getListObject().getId();
				java.util.List<QualityDataSet> listOfQDS = getQdsDAO().getQDSElementsFor(measureLtForCloning.getId(), listObjectId, qds.getDataType().getId(), qds.getOccurrence());
				qDSTerm.setqDSRef(listOfQDS.get(0).getId());
			}
			//for QDS_Term table
			qDSTerm.setId(getId());
			qDSTerm.setDecisionId(newDbChild.getId());
			terms.add(qDSTerm);//this makes it a dead end
			
			//PROCESS ATTRIBUTES
			List<mat.shared.model.Decision> attrs = qDSTerm.getAttributes();
			if(attrs!=null && !attrs.isEmpty()) {
				int ord = -1;
				for (mat.shared.model.Decision d : attrs) {
					ord++;
					saveChildDecision(d, newDbChild, terms, ord, dtid);
				}
			}
		} else if (decisionPojo instanceof AttributeTerm) {
			AttributeTerm attributeTerm = (AttributeTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			
			if (attributeTerm.getOperator()!=null) {
				newDbChild.setOperator(oc.attributeTermToString(attributeTerm.getOperator()));
			} else {
				//this means there is only the attribute (left hand value)
				//and an operator needs to be set so that the Decision table's
				//not nullable can be satisfied
				newDbChild.setOperator(A_NOT_EMPTY);
			}
			
			newDbChild.setOrderNum(orderNum+"");
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			saveLfChildAttr(attributeTerm.getLfTerm(), newDbChild, dataTypeName);//handle left child - string
			saveChildDecision(attributeTerm.getRtTerm(), newDbChild, terms, rtOrder, null);//handle right child - IQDSterm
		} else if (decisionPojo instanceof MeasurementTerm) {
			MeasurementTerm measurementTerm = (MeasurementTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(MEASUREMENT_TERM);
			newDbChild.setOrderNum(orderNum+"");
			//for DECISION table
			dbTopLevelParent.getChildDecisions().add(newDbChild);			
			
			//for MEASUREMENT_TERM table
			measurementTerm.setId(getId());
			measurementTerm.setDecisionId(newDbChild.getId());
			terms.add(measurementTerm);//this makes it a dead end
			
		} else if (decisionPojo instanceof FunctionTerm) {
			FunctionTerm functionTerm = (FunctionTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(functionTerm.getName());
			newDbChild.setOrderNum(orderNum+"");
			//for DECISION table
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			//function term may have other terms, send to saveDecision() to process
			saveChildDecision(functionTerm.getTerm(), newDbChild, terms, lfOrder, null);//handle children
			
		} else if (decisionPojo instanceof Conditional) {			
			Conditional conditional = (Conditional) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			if (conditional.getOperator()!=null) {
				newDbChild.setOperator(oc.conditionalToString(conditional.getOperator()));
			} else {
				//default all conditional's to "AND"
				//this code should never be hit
				newDbChild.setOperator("AND");
			}
			newDbChild.setOrderNum(orderNum+"");

			//row for DECISION table
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			//Conditional must retain the order of it's children
			//order number can be >1 (lf=0, rt=1)
			//being a child under conditional, your order number is
			//just an order number, not lf or rt
			int order = -1;
			for (mat.shared.model.Decision d : conditional.getDecisions()) {
				order++;
				saveChildDecision(d, newDbChild, terms, order, null);
			}
		} else if (decisionPojo instanceof Clause) {
			//this section will handle a reference to another clause
			//the new row in decision table will contain the clause id
			//see newDbChild.setClauseId(clauseId);
			Clause clause = (Clause) decisionPojo;
			
			String clauseName = clause.getName();
			String clauseMeasureId = clause.getMeasureId();
			
			//look for existing clause
			Clause foundClause = getClauseDAO().findByNameAndMeasureId(clauseName, clauseMeasureId);
			
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(CLAUSE);
			newDbChild.setOrderNum(orderNum+"");
			
			//if realClause is not null, then newDbChild (a clause),
			//represents a reference to realClause
			if (foundClause!=null) {
				if (measureLtForCloning!=null) {
					//if your are in here, you are doing the cloning process
					//look for existing cloned clause
					String measureId =  measureLtForCloning.getId();
					String clonedClauseName = clauseName;
					Clause foundClonedClause = getClauseDAO().findByNameAndMeasureId(clonedClauseName, measureId);
					String foundClonedClauseId = null;
					if (foundClonedClause!=null) {
						foundClonedClauseId = foundClonedClause.getId();
					} else {
						//create a new clause row
						//get its id
						Clause clonedClause = cloner(foundClause, measureId, clonedClauseName, true, true,false);
						
						
						//create a new parent row in decision table
						Decision referencedClause = new Decision();
						referencedClause.setId(getId());//create a new id		
						referencedClause.setOperator(CLAUSE);
								
						clonedClause.setDecision(referencedClause);
						//create a new row in clause table
						getClauseDAO().save(clonedClause);
						foundClonedClauseId = clonedClause.getId();

						
						//remove an extra layer of clause
						Clause temp = (Clause)clonedClause.getDecisions().get(0);
						
						for (mat.shared.model.Decision d : temp.getDecisions()) {
							saveChildDecision(d, referencedClause, terms, lfOrder, null);
						}
						dbTopLevelParent.getChildDecisions().add(referencedClause);
					}
					newDbChild.setClauseId(foundClonedClauseId);//this makes it a dead end
					dbTopLevelParent.getChildDecisions().add(newDbChild);
					
				} else {
					//normal clause saving process
					newDbChild.setClauseId(foundClause.getId());//this makes it a dead end
					dbTopLevelParent.getChildDecisions().add(newDbChild);
				}
			}
			else {
				dbTopLevelParent.getChildDecisions().add(newDbChild);			
				for (mat.shared.model.Decision d : clause.getDecisions()) {
					saveChildDecision(d, newDbChild, terms, lfOrder, null);
				}
			}
		}
	}
	
	private void saveLfChildAttr(String attributeName, Decision dbTopLevelParent, String dataTypeName) {
		QDSAttributes qa = getqDSAttributesDAO().findByNameAndDataType(attributeName, dataTypeName);
		if (qa==null) return;
		
		Decision newDbChild = new Decision();
		newDbChild.setId(getId());
		newDbChild.setParentId(dbTopLevelParent.getId());
		newDbChild.setOperator(QDS_ATTRIBUTE);
		newDbChild.setOrderNum(lfOrder+"");
		newDbChild.setAttributeId(qa.getId());
		dbTopLevelParent.getChildDecisions().add(newDbChild);
	}
		
	//****************************DELETE*********************************
	public void deleteDecisions(String parentId) {
		//since each table uses unique ids, ids from all
		//tables will be combined into the below list
		
		List<String> decisionIds = new ArrayList<String>();
		List<String> qdsTermIds = new ArrayList<String>();
		List<String> mTermIds = new ArrayList<String>();

		//get parent
		Decision parent = getDecisionDAO().find(parentId);
		if (parent!=null) {
			decisionIds.add(parentId);
			deleteChild(parentId, decisionIds, qdsTermIds, mTermIds);
			//Need to loop the above and get the list of parentids();
		}
		String[] dIds = new String[decisionIds.size()];
		decisionIds.toArray(dIds);

		String[] qIds = new String[qdsTermIds.size()];
		qdsTermIds.toArray(qIds);

		String[] mIds = new String[mTermIds.size()];
		mTermIds.toArray(mIds);

		//delete using the list ids
		getqDSTermDAO().delete(qIds);
		getMeasurementTermDAO().delete(mIds);
		getDecisionDAO().deleteAndUpdateParent(dIds);

		qIds = null;
		mIds = null;
		dIds = null;
		decisionIds = null;
		qdsTermIds = null;
		mTermIds = null;
	}
	
	private void deleteChild(String parentId, List<String> decisionIds, List<String> qdsTermIds, List<String> mTermIds) {
		List<Decision> listOfDecisions = getDecisionDAO().findByParentId(parentId);
		for (Decision child : listOfDecisions) {
			//every row in decision must be collected
			decisionIds.add(child.getId());
			
			if (isQDSTermOperation(child.getOperator())) {
				QDSTerm qDSTerm = getqDSTermDAO().findByDecisionId(child.getId());
				//TODO: check the code. why is qdsTerm null??- Vasant.
				if (qDSTerm != null) {
					qdsTermIds.add(qDSTerm.getId());
				}
			} else if (isMeasurementTermOperation(child.getOperator())) {
				MeasurementTerm measurementTerm = getMeasurementTermDAO().findByDecisionId(child.getId());
				if(measurementTerm!=null) mTermIds.add(measurementTerm.getId());
			}
		    //Fix for the constraint issue liquibase 14
			deleteChild(child.getId(),decisionIds,qdsTermIds, mTermIds);
		}		
	}
	
	//****************************CLONE A CLAUSE AND ALL TERMS/PROPERTIES*********************************
	/*
	 * this method will clone one clause --> clausePojo
	 * and make it a child of the measure --> clonedMeasure
	 * newMeasureName will give the clause a new prefix name
	 * 
	 * clone system clause should go thru here
	 */
	public void cloneClause(Clause clausePojo, Measure clonedMeasure, String newName, boolean keepContext) {
		cloneClause(clausePojo, clonedMeasure, newName, true, keepContext, false);
	}
	
	/*
	 * set the "replaceName" to true if you want to clone the clause with a completely new name 
	 */
	public void cloneClause(Clause clausePojo, Measure clonedMeasure, 
			String newName, boolean replaceName, boolean keepContext, boolean isMeasureCloning) {
		measureLtForCloning = new Measure();
		measureLtForCloning.setId(clonedMeasure.getId());
		measureLtForCloning.setDescription(newName);
		
		Clause clonedClause = cloner(clausePojo, clonedMeasure.getId(), newName, replaceName, keepContext,isMeasureCloning);
		
		//create a new parent row in decision table
		Decision dbTopLevelParent = new Decision();
		dbTopLevelParent.setId(getId());//create a new id		
		dbTopLevelParent.setOperator(CLAUSE);
				
		clonedClause.setDecision(dbTopLevelParent);
		//create a new row in clause table
		getClauseDAO().save(clonedClause);
		
		//container for qdsterms and measurementterms
		List<Object> terms = new ArrayList<Object>();
		
		
		mat.shared.model.Decision dec = null;
		
		if (isMeasureCloning) {
			dec = clonedClause;
		} else {
			//cloning system Clause has an extra layer of clause, this will strip the clause out
			dec = clonedClause.getDecisions().get(0);
		}

		for (mat.shared.model.Decision decisionPojo : dec.getDecisions()) {
			saveChildDecision(decisionPojo, dbTopLevelParent, terms, lfOrder, null);			
		}

//		//do not strip if it is conditional
//		if (dec instanceof Conditional) {
//			Conditional temp = (Conditional)clonedClause.getDecisions().get(0);
//			for (mat.shared.model.Decision decisionPojo : temp.getDecisions()) {
//				saveChildDecision(decisionPojo, dbTopLevelParent, terms, lfOrder);			
//			}
//		} else if (dec instanceof Clause) {
//			Clause temp = (Clause)clonedClause.getDecisions().get(0);
//			//add child to parent
//			//this will add child rows into decision table
//			//cloning only works off of the save, new child Id's are created during the save
//			for (mat.shared.model.Decision decisionPojo : temp.getDecisions()) {
//				saveChildDecision(decisionPojo, dbTopLevelParent, terms, lfOrder);			
//			}
//		}	

		
	    //SAVE TO DB//
		getDecisionDAO().save(dbTopLevelParent);
		//save qdsterms and measurementterms
		for (Object obj : terms) {
			if (obj instanceof QDSTerm) {
				getqDSTermDAO().save((QDSTerm)obj);
			} else if (obj instanceof MeasurementTerm) {
				getMeasurementTermDAO().save((MeasurementTerm)obj);
			}
		}
		measureLtForCloning = null;
	}
	/*private Clause cloner(Clause originalClause, String measureId,
			String newName, boolean replaceName, boolean keepContext) {
		Clause clone = new Clause();
		
		if (originalClause.getClauseTypeId()!=null) clone.setClauseTypeId(originalClause.getClauseTypeId());
		if (originalClause.getDescription()!=null) clone.setDescription(originalClause.getDescription());
		if (originalClause.getDecisions()!=null) clone.setDecisions(originalClause.getDecisions());
		if (measureId!=null) clone.setMeasureId(measureId);
		
		String name = newName;
		if (!replaceName) {
			name = clauseNamer(newName, originalClause.getName());
		} else {
			//cloning with replaceName=true is cloning from clause to measure phrase
			//cloning at clause level
			//9=Measure Phrase
			clone.setContextId("9");
		}
		clone.setName(name);

		//this keepContext is true
		//must copy from original
		if (keepContext) {
			String newContextId = correctContextId(clone.getName());
			if (newContextId!=null) {
				if (originalClause.getContextId()!=null) clone.setContextId(newContextId);
			} else {
				//This is for measure phrase.
				if (originalClause.getContextId()!=null) clone.setContextId(originalClause.getContextId());
			}
			
			//only set this field when cloning from clause library
			//this is to distinguish the difference between User-defined clones as a system clause
			//or if User-defined is a measure phrase
			if (clone.getContextId().equalsIgnoreCase("8")) {
				//make it a system clause
				clone.setClauseTypeId(SYSTEM_CLAUSE);				
			}
		}

		if (originalClause.getProperty()!=null) clone.setProperties(originalClause.getProperty());
		if (originalClause.getStatusId()!=null) clone.setStatusId(originalClause.getStatusId());
		if (originalClause.getVersion()!=null) clone.setVersion(originalClause.getVersion());
		return clone;
	}*/
	
	private boolean isSystemClause(Clause originalClause){
		return !originalClause.getContextId().equalsIgnoreCase(ConstantMessages.MEASURE_PHRASE_CONTEXT_ID); 
		
	}
	
	private Clause cloner(Clause originalClause, String measureId,
			String newName, boolean replaceName, boolean keepContext, boolean isMeasureCloning) {
		//Refactored this method to not use replaceName and keepContext since it is always set to true.
		//But kept the flags in the method arg, TODO:- If there is no problem then it is ok to remove those.
		
		Clause clone = new Clause();
		
		if (originalClause.getClauseTypeId()!=null) clone.setClauseTypeId(originalClause.getClauseTypeId());
		if (originalClause.getDescription()!=null) clone.setDescription(originalClause.getDescription());
		if (originalClause.getDecisions()!=null) clone.setDecisions(originalClause.getDecisions());
		if (measureId!=null) clone.setMeasureId(measureId);
		
		boolean sysClause = isSystemClause(originalClause);
		if(sysClause && isMeasureCloning){ //This if block has been added to fix the Measure cloning issue
			int underscoreindex  = originalClause.getName().lastIndexOf("_");
			clone.setName(newName +"_" + originalClause.getName().substring(underscoreindex+1));//Need to change this name.
		}
		
		else if(originalClause.getContextId().equalsIgnoreCase(ConstantMessages.MEASURE_PHRASE_CONTEXT_ID)){
			clone.setName(originalClause.getName());
		}
		else{
			clone.setName(newName);
		}
		
		if(originalClause.getContextId().equalsIgnoreCase(ConstantMessages.MEASURE_PHRASE_CONTEXT_ID))
			clone.setContextId(originalClause.getContextId());
		else{
			String newContextId = correctContextId(clone.getName());
			if (newContextId!=null) {
				 clone.setContextId(newContextId);
			}
		}
		
		//only set this field when cloning from clause library
		//this is to distinguish the difference between User-defined clones as a system clause
		//or if User-defined is a measure phrase
		if (clone.getContextId()!= null && clone.getContextId().equalsIgnoreCase(ConstantMessages.USER_DEFINED_CONTEXT_ID)) {
			//make it a system clause
			clone.setClauseTypeId(SYSTEM_CLAUSE);				
		}
		if (originalClause.getProperty()!=null) clone.setProperties(originalClause.getProperty());
//		if (originalClause.getStatusId()!=null) clone.setStatusId(originalClause.getStatusId());//MAT-286 - Removing Status Field from Measure Phrase. 01/2013		
		if (originalClause.getVersion()!=null) clone.setVersion(originalClause.getVersion());
		return clone;
	}
	
	private String clauseNamer(String newMeasureName, String originalClauseName) {
		//how to handle new clause/phrase name when cloned		
//		Measure: ASM1
//		Clauses: ASM1_population1
//		         ASM1_population2
//		         ASM1_Numerator1
//
//		Cloned Meaure name: ASM2	
//		-----------------------------
//		Clauses: ASM2_population1
//		         ASM2_population2
//		         ASM2_Numerator1
		String returnVal = "";
		try {
			String[] temp = originalClauseName.split("_");
			if (temp.length==1) {
				returnVal = originalClauseName;
			} else if (temp.length==2) {
				returnVal = newMeasureName+"_"+temp[1];				
			} else if (temp.length==3) {
				returnVal = newMeasureName+"_"+temp[1]+"_"+temp[2];				
			} else {
				//unknown
				returnVal = originalClauseName;
			}
		} catch (Exception e) {
			returnVal = originalClauseName;
		}
		return returnVal;
	}
	
	private String correctContextId(String newCloneClauseName) {
		//TODO:consolidate this with AppController and context table
		StringUtility su = new StringUtility();
		String clauseNameWithoutNumber = su.stripOffNumber(newCloneClauseName);
		
		
		if (clauseNameWithoutNumber.endsWith(ConstantMessages.MEASURE_POPULATION_CONTEXT_DESC)) {
			return ConstantMessages.MEASURE_POPULATION_CONTEXT_ID;//Test for Measure Population first to avoid getting into population category.
		} else if (clauseNameWithoutNumber.endsWith(ConstantMessages.NUMERATOR_CONTEXT_DESC)) {
			return ConstantMessages.NUMERATOR_CONTEXT_ID;
		} else if (clauseNameWithoutNumber.endsWith(ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_DESC)) {
			return ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID;
		} else if (clauseNameWithoutNumber.endsWith(ConstantMessages.DENOMINATOR_CONTEXT_DESC)) {
			return ConstantMessages.DENOMINATOR_CONTEXT_ID;
		} else if (clauseNameWithoutNumber.endsWith(ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_DESC)) {
			return ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID;
		} else if (clauseNameWithoutNumber.endsWith(ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_DESC)) {
			return ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID;
		}else if (clauseNameWithoutNumber.endsWith(ConstantMessages.POPULATION_CONTEXT_DESC)) {
			return ConstantMessages.POPULATION_CONTEXT_ID; 
		}else if (clauseNameWithoutNumber.endsWith(ConstantMessages.MEASURE_OBSERVATION_CONTEXT_DESC)) {
			return ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID;
		}else if (clauseNameWithoutNumber.endsWith(ConstantMessages.STRAT_CONTEXT_DESC)) {
			return ConstantMessages.STRATIFICATION_CONTEXT_ID;
		}else if (clauseNameWithoutNumber.endsWith(ConstantMessages.USER_DEFINED_CONTEXT_DESC)) {
			return ConstantMessages.USER_DEFINED_CONTEXT_ID;
		}
		return null;
	}
	
	
//***********************QDSTerm collector*********************
	public List<QDSTerm> getQDSTermsFromClause(Clause clausePojo) {
		//TODO:- Need to refactor this method since no sense to have a random UUID for measureLTForQDSTerm global variable.
		measureLtForQDSTerm = new Measure();
		measureLtForQDSTerm.setId(getId());
		
		Decision dbTopLevelParent = new Decision();
		dbTopLevelParent.setId(getId());	
		dbTopLevelParent.setOperator(CLAUSE);

		List<Object> terms = new ArrayList<Object>();
		
		for (mat.shared.model.Decision decisionPojo : clausePojo.getDecisions()) {
			getFromChild(decisionPojo, dbTopLevelParent, terms, lfOrder);			
		}
				
		dbTopLevelParent = null;
		
		//putting QDSTerm in a map to prevent duplicates
		Map<String, QDSTerm> qDSTermMap = new HashMap<String, QDSTerm>();

		for (Object obj : terms) {
			if (obj instanceof QDSTerm) {
				QDSTerm qt = ((QDSTerm)obj);
				qDSTermMap.put(qt.getqDSRef(), qt);
			}
		}

		Set<String> keys = qDSTermMap.keySet();
		
		List<QDSTerm> qDSTermList = new ArrayList<QDSTerm>();
		
		//convert map to list
		for (String key : keys) {
			qDSTermList.add(qDSTermMap.get(key));
		}
		
		measureLtForQDSTerm = null;
		return qDSTermList;
	}
	
	/* this method is used purely to extract QDSTerms from a Clause
	 * the code is the same as "saveChildDecision" method, except for
	 * 1. nothing is saved to the tables
	 * 2. referenced clause are traversed(to get QDSTerms from them), 
	 * this logic is located where the global object "measureLtForQDSTerm" is used
	 * 
	 * extra code were not removed due to the risk of introducing bugs -vince
	 */		
	private void getFromChild(mat.shared.model.Decision decisionPojo, Decision dbTopLevelParent, List<Object> terms, int orderNum) {
		if (decisionPojo==null) return;
		
		//handle all child terms/conditionals recursively
		if (decisionPojo instanceof QDSMeasurementTerm) {
			QDSMeasurementTerm qDSMeasurementTerm = (QDSMeasurementTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(oc.qDSMeasureTermToString(qDSMeasurementTerm.getQDSOperator()));
			newDbChild.setOrderNum(orderNum+"");
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			getFromChild(qDSMeasurementTerm.getLfTerm(), newDbChild, terms, lfOrder);//handle children
			getFromChild(qDSMeasurementTerm.getRtTerm(), newDbChild, terms, rtOrder);//handle children
			
		} else if (decisionPojo instanceof StatementTerm) {
			StatementTerm statementTerm = (StatementTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(oc.statementTermToString(statementTerm.getOperator()));
			newDbChild.setOrderNum(orderNum+"");
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			getFromChild(statementTerm.getLfTerm(), newDbChild, terms, lfOrder);//handle children
			getFromChild(statementTerm.getRtTerm(), newDbChild, terms, rtOrder);//handle children
			
		} else if (decisionPojo instanceof QDSTerm) {
			QDSTerm qDSTerm = (QDSTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(QDS_TERM);
			newDbChild.setOrderNum(orderNum+"");
			//for Decision table
			dbTopLevelParent.getChildDecisions().add(newDbChild);			
			
//			if (measureLtForCloning!=null) {
//				QualityDataSet qds = getQdsDAO().find(qDSTerm.getqDSRef());
//				String listObjectId = qds.getListObject().getId();
//				java.util.List<QualityDataSet> listOfQDS = getQdsDAO().getQDSElementsFor(measureLtForCloning.getId(), listObjectId, qds.getDataType().getId());
//				qDSTerm.setqDSRef(listOfQDS.get(0).getId());
//			}
//			//for QDS_Term table
//			qDSTerm.setId(getId());
			qDSTerm.setDecisionId(newDbChild.getId());
			terms.add(qDSTerm);//this makes it a dead end
			
			//PROCESS ATTRIBUTES
			List<mat.shared.model.Decision> attrs = qDSTerm.getAttributes();
			if(attrs!=null) {
				int ord = -1;
				for (mat.shared.model.Decision d : attrs) {
					ord++;
					getFromChild(d, newDbChild, terms, ord);
				}
			}
		} else if (decisionPojo instanceof AttributeTerm) {
			AttributeTerm attributeTerm = (AttributeTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			
			if (attributeTerm.getOperator()!=null) {
				newDbChild.setOperator(oc.attributeTermToString(attributeTerm.getOperator()));
			} else {
				//this means there is only the attribute (left hand value)
				//and an operator needs to be set so that the Decision table's
				//not nullable can be satisfied
				newDbChild.setOperator(A_NOT_EMPTY);
			}
			
			newDbChild.setOrderNum(orderNum+"");
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
//			saveLfChildAttr(attributeTerm.getLfTerm(), newDbChild);//handle left child - string
			getFromChild(attributeTerm.getRtTerm(), newDbChild, terms, rtOrder);//handle right child - IQDSterm
		} else if (decisionPojo instanceof MeasurementTerm) {
			MeasurementTerm measurementTerm = (MeasurementTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(MEASUREMENT_TERM);
			newDbChild.setOrderNum(orderNum+"");
			//for DECISION table
			dbTopLevelParent.getChildDecisions().add(newDbChild);			
			
			//for MEASUREMENT_TERM table
			measurementTerm.setId(getId());
			measurementTerm.setDecisionId(newDbChild.getId());
			terms.add(measurementTerm);//this makes it a dead end
			
		} else if (decisionPojo instanceof FunctionTerm) {
			FunctionTerm functionTerm = (FunctionTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(functionTerm.getName());
			newDbChild.setOrderNum(orderNum+"");
			//for DECISION table
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			//function term may have other terms, send to saveDecision() to process
			getFromChild(functionTerm.getTerm(), newDbChild, terms, lfOrder);//handle children
			
		} else if (decisionPojo instanceof Conditional) {			
			Conditional conditional = (Conditional) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			if (conditional.getOperator()!=null) {
				newDbChild.setOperator(oc.conditionalToString(conditional.getOperator()));
			} else {
				//default all conditional's to "AND"
				//this code should never be hit
				newDbChild.setOperator("AND");
			}
			newDbChild.setOrderNum(orderNum+"");

			//row for DECISION table
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			//Conditional must retain the order of it's children
			//order number can be >1 (lf=0, rt=1)
			//being a child under conditional, your order number is
			//just an order number, not lf or rt
			int order = -1;
			for (mat.shared.model.Decision d : conditional.getDecisions()) {
				order++;
				getFromChild(d, newDbChild, terms, order);
			}
		} else if (decisionPojo instanceof Clause) {
			//this section will handle a reference to another clause
			//the new row in decision table will contain the clause id
			//see newDbChild.setClauseId(clauseId);
			Clause clause = (Clause) decisionPojo;
			
			String clauseName = clause.getName();
			String clauseMeasureId = clause.getMeasureId();
			
			//look for existing clause
			Clause foundClause = getClauseDAO().findByNameAndMeasureId(clauseName, clauseMeasureId);
			
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(CLAUSE);
			newDbChild.setOrderNum(orderNum+"");
			
			//if realClause is not null, then newDbChild (a clause),
			//represents a reference to realClause
			if (foundClause!=null) {
				if (measureLtForQDSTerm!=null) {
					//if your are in here, you are doing the cloning process
					//look for existing cloned clause
					String measureId =  measureLtForQDSTerm.getId();
					String clonedClauseName = clauseName;
					Clause foundClonedClause = getClauseDAO().findByNameAndMeasureId(clonedClauseName, measureId);
					String foundClonedClauseId = null;
					if (foundClonedClause!=null) {
						foundClonedClauseId = foundClonedClause.getId();
					} else {
						//create a new clause row
						//get its id
						Clause clonedClause = cloner(foundClause, measureId, clonedClauseName, true, true, false);
						
						//create a new parent row in decision table
						Decision referencedClause = new Decision();
						referencedClause.setId(getId());//create a new id		
						referencedClause.setOperator(CLAUSE);
								
						clonedClause.setDecision(referencedClause);
						//create a new row in clause table
//						getClauseDAO().save(clonedClause);
						foundClonedClauseId = clonedClause.getId();

						
						//remove an extra layer of clause
						Clause temp = (Clause)clonedClause.getDecisions().get(0);
						
						for (mat.shared.model.Decision d : temp.getDecisions()) {
							getFromChild(d, referencedClause, terms, lfOrder);
						}
						dbTopLevelParent.getChildDecisions().add(referencedClause);
					}
					newDbChild.setClauseId(foundClonedClauseId);//this makes it a dead end
					dbTopLevelParent.getChildDecisions().add(newDbChild);
					
				} else {
					//normal clause saving process
					newDbChild.setClauseId(foundClause.getId());//this makes it a dead end
					dbTopLevelParent.getChildDecisions().add(newDbChild);
				}
			}
			else {
				dbTopLevelParent.getChildDecisions().add(newDbChild);			
				for (mat.shared.model.Decision d : clause.getDecisions()) {
					getFromChild(d, newDbChild, terms, lfOrder);
				}
			}
		}
	}

	private String getDataTypeId(String qdmid){
		List<String> qdmids = new ArrayList<String>();
		qdmids.add(qdmid);
		List<QualityDataSet> qdms = getQdsDAO().getQDMsById(qdmids);
		DataType dt = qdms.get(0).getDataType();
		return dt.getId();
	}

	public ArrayList<String> getDependencyTree(mat.shared.model.Decision decisionPojo){
		ArrayList<String> clauseNames = new ArrayList<String>();
		if (decisionPojo instanceof QDSMeasurementTerm) {
			QDSMeasurementTerm q = (QDSMeasurementTerm)decisionPojo;
				addAllUnique(clauseNames, getDependencyTree(q.getLfTerm()));
				addAllUnique(clauseNames, getDependencyTree(q.getRtTerm()));
			
		} else if (decisionPojo instanceof StatementTerm) {
			StatementTerm s = (StatementTerm)decisionPojo;
				addAllUnique(clauseNames, getDependencyTree(s.getLfTerm()));
				addAllUnique(clauseNames, getDependencyTree(s.getRtTerm()));
			
		}  else if (decisionPojo instanceof FunctionTerm) {
			FunctionTerm f = (FunctionTerm)decisionPojo;
				addAllUnique(clauseNames, getDependencyTree(f.getTerm()));
			
		} else if (decisionPojo instanceof Conditional) {			
			Conditional conditional = (Conditional) decisionPojo;
			for (mat.shared.model.Decision d : conditional.getDecisions()) {
					addAllUnique(clauseNames, getDependencyTree(d));
			}
		} else if (decisionPojo instanceof Clause) {
			Clause clause = (Clause) decisionPojo;
			clauseNames.add(clause.getName());
			for (mat.shared.model.Decision d : clause.getDecisions()) {
					addAllUnique(clauseNames,getDependencyTree(d));
			}
		} 
		return clauseNames;
	}
	
	public void addAllUnique(ArrayList<String> to, ArrayList<String> from){
		for(String s : from){
			if(s != null){
				if(to.contains(s)){
					to.remove(s);
				}
				to.add(s);
			}
		}
	}
}


