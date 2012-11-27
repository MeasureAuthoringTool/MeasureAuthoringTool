package org.ifmc.mat.dao.impl.clause;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.ifmc.mat.dao.service.DAOService;
import org.ifmc.mat.model.clause.Clause;
import org.ifmc.mat.model.clause.Decision;
import org.ifmc.mat.shared.model.Conditional;
import org.ifmc.mat.shared.model.FunctionTerm;
import org.ifmc.mat.shared.model.IQDSTerm;
import org.ifmc.mat.shared.model.MeasurementTerm;
import org.ifmc.mat.shared.model.QDSMeasurementTerm;
import org.ifmc.mat.shared.model.QDSTerm;
import org.ifmc.mat.shared.model.StatementTerm;
import org.ifmc.mat.shared.model.util.OperatorConverter;
import org.ifmc.mat.shared.model.util.TermUtil;

public class ConditionalDAO {

private String lfOrder = "0";
private String rtOrder = "1";
private OperatorConverter oc = new OperatorConverter();
private TermUtil tu = new TermUtil();
private String QDS_TERM = "QDSTERM";
private String MEASUREMENT_TERM = "MEASUREMENTTERM";
private DecisionDAO decisionDAO = new DecisionDAO();
private QDSTermDAO qDSTermDAO = new QDSTermDAO();
private MeasurementTermDAO measurementTermDAO = new MeasurementTermDAO();
private DAOService dAOService = null;
private ClauseDAO clauseDAO = new ClauseDAO();

public ConditionalDAO () {
}

public ConditionalDAO (DAOService dAOService) {
	//this constructor is used for testing or getting dao's via DAOService
	this.dAOService = dAOService;
}

public org.ifmc.mat.dao.clause.QDSTermDAO getqDSTermDAO() {
	if (dAOService!=null) return dAOService.getqDSTermDAO();
	return qDSTermDAO;
}

public org.ifmc.mat.dao.clause.MeasurementTermDAO getMeasurementTermDAO() {
	if (dAOService!=null) return dAOService.getMeasurementTermDAO();
	return measurementTermDAO;
}

public org.ifmc.mat.dao.clause.DecisionDAO getDecisionDAO() {
	if (dAOService!=null) return dAOService.getDecisionDAO();
	return decisionDAO;
}
public org.ifmc.mat.dao.clause.ClauseDAO getClauseDAO() {
	if (dAOService!=null) return dAOService.getClauseDAO();
	return clauseDAO;
}
public boolean isFunction(String symbol) {
	return tu.isFunctionTerm(symbol);
}
public boolean isStatementOperator(String symbol) {
	return tu.isStatementTermOperator(symbol);
}
public boolean isQDSMeasurementOperator(String symbol) {
	return tu.isQDSMeasurementTermOperator(symbol);
}


public boolean isQDSTermOperator(String symbol) {
	return tu.isQDSTermOperation(symbol);
}
public boolean isMeasurementTermOperator(String symbol) {
	return tu.isMeasurementTermOperation(symbol);
}


private String getId() {
	return UUID.randomUUID().toString();
}

//this load does not work for correctly, keep for now
////****************************load*********************************
////load top level parent
//public Conditional load(String parentId)  {
//	Decision decision = new Decision();
//	if (dAOService!=null) {
//		decision = dAOService.getDecisionDAO().find(parentId);
//	} else {
//		DecisionDAO decisionDAO = new DecisionDAO();
//		decision = decisionDAO.find(parentId);
//	}
//
//	return load(decision);
//}
//
//private Conditional load(Decision topLevelDBDecision)  {		
//	Conditional topLevelCentralPojo = new Conditional();
//	
//	String op = topLevelDBDecision.getOperator();
//	if (isStatementOperator(op)) {
//		StatementTerm st = null;
//		st = new StatementTerm();
//		st.setOperator(oc.toStatementTermOp(op));
//		topLevelCentralPojo.getDecisions().add(st);
//		createChild(st, topLevelDBDecision);
//	} else if (isQDSMeasurementOperator(op)) {
//		QDSMeasurementTerm qmt = new QDSMeasurementTerm();
//		qmt.setOperator(oc.toStatementTermOp(op));
//		topLevelCentralPojo.getDecisions().add(qmt);
//		createChild(qmt, topLevelDBDecision);
//	} else if (isFunction(op)) {
//		FunctionTerm ft = new FunctionTerm();
//		ft.setName(topLevelDBDecision.getOperator());
//		topLevelCentralPojo.getDecisions().add(ft);
//		createChild(ft, topLevelDBDecision);
//	} else if (tu.isQDSTermOperator(op)) {
//		QDSTerm qt = getqDSTermDAO().findByDecisionId(topLevelDBDecision.getId());
//		topLevelCentralPojo.getDecisions().add(qt);
//	} else if (tu.isMeasurementTermOperator(op)) {
//		MeasurementTerm mt = getMeasurementTermDAO().findByDecisionId(topLevelDBDecision.getId());
//		topLevelCentralPojo.getDecisions().add(mt);
//	} else if (tu.isConditional(op)) {
//		Conditional conditional = new Conditional();
//		topLevelCentralPojo.getDecisions().add(conditional);
//		createChild(conditional, topLevelDBDecision);
//	} else if (tu.isClause(op)) {
//		Clause clause = new Clause();
//		topLevelCentralPojo.getDecisions().add(clause);
//		createChild(clause, topLevelDBDecision);
//	}  	 		
//	return topLevelCentralPojo;
//}
//
////load child
//private void createChild(org.ifmc.mat.shared.model.Decision currentTopLevelCentralPojo, Decision topLevelDBDecision) {
//	List<Decision> listOfChildren = getDecisionDAO().findByParentId(topLevelDBDecision.getId());
//	
//	if (listOfChildren.isEmpty()) return;
//		for (Decision dbChild: listOfChildren) {
//			String dbChildOp = dbChild.getOperator();
//			if (tu.isQDSTermOperator(dbChildOp)) {
//				//QDS TERM
//				QDSTerm qt = getqDSTermDAO().findByDecisionId(dbChild.getId());
//				if (currentTopLevelCentralPojo instanceof QDSMeasurementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfQDS(qt);
//					} else {
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtQDS(qt);
//					}
//				} else if (currentTopLevelCentralPojo instanceof StatementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((StatementTerm)currentTopLevelCentralPojo).setLfTerm(qt);
//					} else {
//						((StatementTerm)currentTopLevelCentralPojo).setRtTerm(qt);
//					}
//				} else if (currentTopLevelCentralPojo instanceof FunctionTerm) {
//					((FunctionTerm)currentTopLevelCentralPojo).setTerm(qt);
//				}
//			} else if (tu.isMeasurementTermOperator(dbChildOp)) {
//				//MEASUREMENT TERM
//				MeasurementTerm mt = getMeasurementTermDAO().findByDecisionId(dbChild.getId());
//				if (currentTopLevelCentralPojo instanceof QDSMeasurementTerm) {
////why is MeasureTerm cannot be set?						
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						//((QDSMeasurementTerm)d).setLfQDS(mt);
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfTerm(mt);
//					} else {
//						//((QDSMeasurementTerm)d).setRtQDS(mt);
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtTerm(mt);
//					}
//				} else if (currentTopLevelCentralPojo instanceof StatementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((StatementTerm)currentTopLevelCentralPojo).setLfTerm(mt);
//					} else {
//						((StatementTerm)currentTopLevelCentralPojo).setRtTerm(mt);
//					}
//				} else if (currentTopLevelCentralPojo instanceof FunctionTerm) {
//					((FunctionTerm)currentTopLevelCentralPojo).setTerm(mt);
//				}
//			} else if (isQDSMeasurementOperator(dbChildOp)) {
//				//QDSMEASUREMENT TERM
//				QDSMeasurementTerm qmt = new QDSMeasurementTerm();
//				qmt.setQDSOperator(oc.toQDSMeasurementTermOp(dbChildOp));
//				
//				if (currentTopLevelCentralPojo instanceof QDSMeasurementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfQDS(qmt);
//					} else {
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtQDS(qmt);
//					}
//				} else if (currentTopLevelCentralPojo instanceof StatementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((StatementTerm)currentTopLevelCentralPojo).setLfTerm(qmt);
//					} else {
//						((StatementTerm)currentTopLevelCentralPojo).setRtTerm(qmt);
//					}
//				} else if (currentTopLevelCentralPojo instanceof FunctionTerm) {
//					((FunctionTerm)currentTopLevelCentralPojo).setTerm(qmt);
//				} else if (currentTopLevelCentralPojo instanceof Conditional) {
//					((Conditional)currentTopLevelCentralPojo).getDecisions().add(qmt);
//				}
//
//				createChild(qmt, dbChild);
//
//			} else if (isStatementOperator(dbChildOp)) {
//				//STATEMENT TERM
//				StatementTerm st = new StatementTerm();
//				st.setOperator(oc.toStatementTermOp(dbChildOp));
//				
//				if (currentTopLevelCentralPojo instanceof QDSMeasurementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfQDS(st);
//					} else {
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtQDS(st);
//					}
//				} else if (currentTopLevelCentralPojo instanceof StatementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((StatementTerm)currentTopLevelCentralPojo).setLfTerm(st);
//					} else {
//						((StatementTerm)currentTopLevelCentralPojo).setRtTerm(st);
//					}
//				} else if (currentTopLevelCentralPojo instanceof FunctionTerm) {
//					((FunctionTerm)currentTopLevelCentralPojo).setTerm(st);
//				} else if (currentTopLevelCentralPojo instanceof Conditional) {
//					((Conditional)currentTopLevelCentralPojo).getDecisions().add(st);
//				}
//				
//				createChild(st, dbChild);
//			
//			} else if (tu.isConditional(dbChildOp)) {
//				//CONDITIONAL
//				Conditional cn = new Conditional();
//				cn.setOperator(oc.toConditionalOp(dbChildOp));
//				
//				if (currentTopLevelCentralPojo instanceof QDSMeasurementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfQDS(cn);
//					} else {
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtQDS(cn);
//					}
//				} else if (currentTopLevelCentralPojo instanceof StatementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((StatementTerm)currentTopLevelCentralPojo).setLfTerm(cn);
//					} else {
//						((StatementTerm)currentTopLevelCentralPojo).setRtTerm(cn);
//					}
//				} else if (currentTopLevelCentralPojo instanceof FunctionTerm) {
//					((FunctionTerm)currentTopLevelCentralPojo).setTerm(cn);
//				} else if (currentTopLevelCentralPojo instanceof Conditional) {
//					((Conditional)currentTopLevelCentralPojo).getDecisions().add(cn);
//				}
//				
//				createChild(cn, dbChild);
//			
//			} else if (isFunction(dbChildOp)) {
//				//FUNCTION TERM
//				FunctionTerm ft = new FunctionTerm();
//				//ft.setOperator(oc.toFunctionTermOp(dbChildOp));
//				
//				if (currentTopLevelCentralPojo instanceof QDSMeasurementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfQDS(ft);
//					} else {
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtQDS(ft);
//					}
//				} else if (currentTopLevelCentralPojo instanceof StatementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((StatementTerm)currentTopLevelCentralPojo).setLfTerm(ft);
//					} else {
//						((StatementTerm)currentTopLevelCentralPojo).setRtTerm(ft);
//					}
//				} else if (currentTopLevelCentralPojo instanceof FunctionTerm) {
//					((FunctionTerm)currentTopLevelCentralPojo).setTerm(ft);
//				} else if (currentTopLevelCentralPojo instanceof Conditional) {
//					((Conditional)currentTopLevelCentralPojo).getDecisions().add(ft);
//				}
//				
//				createChild(ft, dbChild);
//				
//			} else if (tu.isClause(dbChildOp) && dbChild.getClauseId()!=null) {
//				System.out.println();
//				//CLAUSE
//				//use clauseid to get decisionid
//				String clauseId = dbChild.getClauseId();
//				Clause clause = getClauseDAO().find(clauseId);
//				String decisionId = clause.getDecisionId();
//				
//				Decision innerClauseDecision = getDecisionDAO().find(decisionId);
//				Conditional cn = load(innerClauseDecision);
//				List<org.ifmc.mat.shared.model.Decision> decisions = new ArrayList<org.ifmc.mat.shared.model.Decision>();
//				decisions.add(cn);
//				clause.setDecisions(decisions);
//				
//				if (currentTopLevelCentralPojo instanceof QDSMeasurementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfQDS(clause);
//					} else {
//						((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtQDS(clause);
//					}
//				} else if (currentTopLevelCentralPojo instanceof StatementTerm) {
//					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
//						((StatementTerm)currentTopLevelCentralPojo).setLfTerm(clause);
//					} else {
//						((StatementTerm)currentTopLevelCentralPojo).setRtTerm(clause);
//					}
//				} else if (currentTopLevelCentralPojo instanceof FunctionTerm) {
//					((FunctionTerm)currentTopLevelCentralPojo).setTerm(clause);
//				} else if (currentTopLevelCentralPojo instanceof Conditional) {
//					((Conditional)currentTopLevelCentralPojo).getDecisions().add(clause);
//				}			
//			}
//		}
//}

//****************************SAVE*********************************
//save - process top level parent
public void save(Clause clause) {
	String decisionId = clause.getDecision().getId();
	
	if (decisionId!=null) {
		//delete all existing decisions
		deleteDecisions(decisionId);
	}
	
	Decision parent = new Decision();
	
	//create clause op row in decision table
	parent.setId(getId());
	parent.setOperator("CLAUSE");
	
	clause.setDecision(parent);

//	Decision nextParent = new Decision();
//	nextParent.setId(getId());
//	nextParent.setParentId(parent.getId());
//	parent.getChildDecisions().add(nextParent);
	
	getClauseDAO().save(clause);
	
	List<Object> terms = new ArrayList<Object>();
	
	saveDecisions(clause.getDecisions(), parent, terms);
	
    //SAVE TO DB//
	//save parent first
	getDecisionDAO().save(parent);
	//save children
	for (Object obj : terms) {
		if (obj instanceof QDSTerm) {
			getqDSTermDAO().save((QDSTerm)obj);
		} else if (obj instanceof MeasurementTerm) {
			getMeasurementTermDAO().save((MeasurementTerm)obj);
		}
	}
}

private void saveDecisions(List<org.ifmc.mat.shared.model.Decision> listOfDecisions, Decision parent, List<Object> terms) {
	for (org.ifmc.mat.shared.model.Decision term : listOfDecisions) {				
		if (term instanceof QDSMeasurementTerm) {
			QDSMeasurementTerm qDSMeasurementTerm = (QDSMeasurementTerm) term;
			parent.setOperator(oc.qDSMeasureTermToString(qDSMeasurementTerm.getQDSOperator()));
			parent.setOrderNum(lfOrder);
			
			setTerm(qDSMeasurementTerm.getLfTerm(), parent, terms, lfOrder);
			setTerm(qDSMeasurementTerm.getRtTerm(), parent, terms, rtOrder);
			
		} else if (term instanceof StatementTerm) {
			StatementTerm statementTerm = (StatementTerm) term;
			parent.setOperator(oc.statementTermToString(statementTerm.getOperator()));
			parent.setOrderNum(lfOrder);
			
			setTerm(statementTerm.getLfTerm(), parent, terms, lfOrder);
			setTerm(statementTerm.getRtTerm(), parent, terms, rtOrder);
			
		} else if (term instanceof FunctionTerm) {
			FunctionTerm functionTerm = (FunctionTerm) term;
			parent.setOperator(functionTerm.getName());
			parent.setOrderNum(lfOrder);
			
			setTerm(functionTerm.getTerm(), parent, terms, lfOrder);
			
		} else if (term instanceof QDSTerm) {
			QDSTerm qDSTerm = (QDSTerm) term;
			parent.setOperator(QDS_TERM);
			parent.setOrderNum(lfOrder);
			
			qDSTerm.setId(getId());
			qDSTerm.setDecisionId(parent.getId());
			terms.add(qDSTerm);

		} else if (term instanceof MeasurementTerm) {
			MeasurementTerm measurementTerm = (MeasurementTerm) term;
			parent.setOperator(MEASUREMENT_TERM);
			parent.setOrderNum(lfOrder);
			
			measurementTerm.setId(getId());
			measurementTerm.setDecisionId(parent.getId());
			terms.add(measurementTerm);			
		} else if (term instanceof Conditional) {
			Conditional conditional = (Conditional) term;
			if (conditional.getOperator()!=null) {
				parent.setOperator(oc.conditionalToString(conditional.getOperator()));				
			} else {
				parent.setOperator("CLAUSE");
			}
			parent.setOrderNum(lfOrder);
			setTerm(conditional, parent, terms, lfOrder);				
		} else if (term instanceof Clause) {
			Clause clause = (Clause) term;
			
			Decision decision = new Decision();
			decision.setId(getId());
			decision.setParentId(parent.getId());
			if (clause.getId()!=null) { 
				decision.setClauseId(clause.getId());//this makes it a dead end
			}
			decision.setOperator("CLAUSE");
			decision.setOrderNum(lfOrder);
			parent.getChildDecisions().add(decision);			
		}		
	}	
}

//save - process child
private void setTerm(org.ifmc.mat.shared.model.Decision term, Decision parent, List<Object> terms, String orderNum) {
	if (term==null) return;
	
	//handle all child terms/conditionals recursively
		if (term instanceof QDSMeasurementTerm) {
			QDSMeasurementTerm qDSMeasurementTerm = (QDSMeasurementTerm) term;
			Decision decision = new Decision();
			decision.setId(getId());
			decision.setParentId(parent.getId());
			decision.setOperator(oc.qDSMeasureTermToString(qDSMeasurementTerm.getQDSOperator()));
			decision.setOrderNum(orderNum);
			parent.setOrderNum(lfOrder);//new
			parent.getChildDecisions().add(decision);
			
			setTerm(qDSMeasurementTerm.getLfTerm(), decision, terms, lfOrder);//handle children
			setTerm(qDSMeasurementTerm.getRtTerm(), decision, terms, rtOrder);//handle children
			
		} else if (term instanceof StatementTerm) {
			StatementTerm statementTerm = (StatementTerm) term;
			Decision decision = new Decision();
			decision.setId(getId());
			decision.setParentId(parent.getId());
			decision.setOperator(oc.statementTermToString(statementTerm.getOperator()));
			decision.setOrderNum(orderNum);
			parent.getChildDecisions().add(decision);
			
			setTerm(statementTerm.getLfTerm(), decision, terms, lfOrder);//handle children
			setTerm(statementTerm.getRtTerm(), decision, terms, rtOrder);//handle children
			
		} else if (term instanceof FunctionTerm) {
			FunctionTerm functionTerm = (FunctionTerm) term;
			Decision decision = new Decision();
			decision.setId(getId());
			decision.setParentId(parent.getId());
			decision.setOperator(functionTerm.getName());
			decision.setOrderNum(orderNum);
			parent.getChildDecisions().add(decision);
			
			setTerm(functionTerm.getTerm(), decision, terms, lfOrder);//handle children
			
		} else if (term instanceof QDSTerm) {
			QDSTerm qDSTerm = (QDSTerm) term;
			Decision decision = new Decision();
			decision.setId(getId());
			decision.setParentId(parent.getId());
			decision.setOperator(QDS_TERM);
			decision.setOrderNum(orderNum);
			parent.getChildDecisions().add(decision);			
			
			qDSTerm.setId(getId());
			qDSTerm.setDecisionId(decision.getId());
			terms.add(qDSTerm);//this makes it a dead end

		} else if (term instanceof MeasurementTerm) {
			MeasurementTerm measurementTerm = (MeasurementTerm) term;
			Decision decision = new Decision();
			decision.setId(getId());
			decision.setParentId(parent.getId());
			decision.setOperator(MEASUREMENT_TERM);
			decision.setOrderNum(orderNum);
			parent.getChildDecisions().add(decision);			
			
			measurementTerm.setId(getId());
			measurementTerm.setDecisionId(decision.getId());
			terms.add(measurementTerm);//this makes it a dead end
		} 
		else if (term instanceof Conditional) {			
			Conditional conditional = (Conditional) term;
			Decision decision = new Decision();
			decision.setId(getId());
			decision.setParentId(parent.getId());
			if (conditional.getOperator()!=null) {
				decision.setOperator(oc.conditionalToString(conditional.getOperator()));
			} else {
				decision.setOperator("CLAUSE");
			}
			decision.setOrderNum(lfOrder);
			parent.getChildDecisions().add(decision);			
			for (org.ifmc.mat.shared.model.Decision d : conditional.getDecisions()) {
				setTerm(d, parent, terms, lfOrder);
			}
		} else if (term instanceof Clause) {
			Clause clause = (Clause) term;
			Decision decision = new Decision();
			decision.setId(getId());
			decision.setParentId(parent.getId());
			decision.setClauseId(clause.getId());//this makes it a dead end
			decision.setOperator("CLAUSE");
			decision.setOrderNum(lfOrder);
			parent.getChildDecisions().add(decision);			
		}
	}

//****************************DELETE*********************************
	public void deleteDecisions(String parentId) {
		List<String> listOfIds = new ArrayList<String>();

		//get parent
		Decision parent = getDecisionDAO().find(parentId);
		if (parent!=null) {
			listOfIds.add(parentId);
			deleteChild(parentId, listOfIds);
		}
		String[] ids = new String[listOfIds.size()];
		listOfIds.toArray(ids);
				
		getqDSTermDAO().delete(ids);
		getMeasurementTermDAO().delete(ids);
		getDecisionDAO().delete(ids);
	}
	
	private void deleteChild(String parentId, List<String> listOfIds) {
		List<Decision> listOfDecisions = getDecisionDAO().findByParentId(parentId);
		for (Decision child : listOfDecisions) {
			//every row in decision must be collected
			listOfIds.add(child.getId());
			if (isQDSTermOperator(child.getOperator())) {
				QDSTerm qDSTerm = getqDSTermDAO().findByDecisionId(child.getId());
				listOfIds.add(qDSTerm.getId());
			} else if (isMeasurementTermOperator(child.getOperator())) {
				MeasurementTerm measurementTerm = getMeasurementTermDAO().findByDecisionId(child.getId());
				listOfIds.add(measurementTerm.getId());
			} else {
				//go thru the next child's decisions
				deleteChild(child.getId(), listOfIds);
			}
		}		
	}
	
	////////////////////load///////////////////////////////////////////
	public org.ifmc.mat.shared.model.Decision load(String parentId)  {
		Decision topLevelDBDecision = new Decision();
		if (dAOService!=null) {
			topLevelDBDecision = dAOService.getDecisionDAO().find(parentId);
		} else {
			DecisionDAO decisionDAO = new DecisionDAO();
			topLevelDBDecision = decisionDAO.find(parentId);
		}

		return preLoad(topLevelDBDecision);
	}
	
	private org.ifmc.mat.shared.model.Decision preLoad(Decision topLevelDBDecision)  {
		String op = topLevelDBDecision.getOperator();
		//this will determine what the top level shared pojo will be
		if (isStatementOperator(op)) {
			StatementTerm st = null;
			st = new StatementTerm();
			st.setOperator(oc.toStatementTermOp(op));
			loadAll(st, topLevelDBDecision);
			return st;
		} else if (isQDSMeasurementOperator(op)) {
			QDSMeasurementTerm qmt = new QDSMeasurementTerm();
			qmt.setOperator(oc.toStatementTermOp(op));
			loadAll(qmt, topLevelDBDecision);
			return qmt;
		} else if (isFunction(op)) {
			FunctionTerm ft = new FunctionTerm();
			ft.setName(topLevelDBDecision.getOperator());
			loadAll(ft, topLevelDBDecision);
			return ft;
		} else if (tu.isQDSTermOperation(op)) {
			QDSTerm qt = getqDSTermDAO().findByDecisionId(topLevelDBDecision.getId());
			return qt;
		} else if (tu.isMeasurementTermOperation(op)) {
			MeasurementTerm mt = getMeasurementTermDAO().findByDecisionId(topLevelDBDecision.getId());
			return mt;
		} else if (tu.isConditional(op)) {
			Conditional conditional = new Conditional();
			loadAll(conditional, topLevelDBDecision);
			return conditional;
		} else if (tu.isClauseOperation(op)) {
			Clause clause = new Clause();
			loadAll(clause, topLevelDBDecision);
			return clause;
		}
		//Unrecognized decision type, return null
		return null;		
	}
	
	
	private void setToParent(org.ifmc.mat.shared.model.Decision currentTopLevelCentralPojo, 
			Decision topLevelDBDecision, Decision dbChild, IQDSTerm currentChild) {

		String dbOp = topLevelDBDecision.getOperator();
		
		if (isQDSMeasurementOperator(dbOp)) {
			if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
				((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfQDS(currentChild);
			} else {
				((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtQDS(currentChild);
			}
			
		} else if (isStatementOperator(dbOp)) {
			if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
				((StatementTerm)currentTopLevelCentralPojo).setLfTerm(currentChild);
			} else {
				((StatementTerm)currentTopLevelCentralPojo).setRtTerm(currentChild);
			}
			
		} else if (isFunction(dbOp)) {
			((FunctionTerm)currentTopLevelCentralPojo).setTerm(currentChild);
			
		} else if (tu.isConditional(dbOp)) {
			((Conditional)currentTopLevelCentralPojo).getDecisions().add(currentChild);
			
		} else if (tu.isClauseOperation(dbOp)) {
			Clause parentClause = ((Clause)currentTopLevelCentralPojo);
			if (parentClause.getDecisions()!=null) {
				parentClause.getDecisions().add(currentChild);
			} else {
				List<org.ifmc.mat.shared.model.Decision> decisions = new ArrayList<org.ifmc.mat.shared.model.Decision>();
				decisions.add(currentChild);
				parentClause.setDecisions(decisions);
			}
		}
	}
	
	private void loadAll(org.ifmc.mat.shared.model.Decision currentTopLevelCentralPojo, Decision topLevelDBDecision) {
		List<Decision> listOfChildren = dAOService.getDecisionDAO().findByParentId(topLevelDBDecision.getId());
		if (listOfChildren.isEmpty()) return;
		
		for (Decision dbChild: listOfChildren) {
			String dbChildOp = dbChild.getOperator();
			if (tu.isQDSTermOperation(dbChildOp)) {
				//QDS TERM
				QDSTerm qt = getqDSTermDAO().findByDecisionId(dbChild.getId());
				setToParent(currentTopLevelCentralPojo, topLevelDBDecision, dbChild, qt);					
			} else if (tu.isMeasurementTermOperation(dbChildOp)) {
				//MEASUREMENT TERM				
				MeasurementTerm mt = getMeasurementTermDAO().findByDecisionId(dbChild.getId());
				if (currentTopLevelCentralPojo instanceof QDSMeasurementTerm) {
					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
						//((QDSMeasurementTerm)d).setLfQDS(mt);
						((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfTerm(mt);
					} else {
						//((QDSMeasurementTerm)d).setRtQDS(mt);
						((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtTerm(mt);
					}
				} else if (currentTopLevelCentralPojo instanceof StatementTerm) {
					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
						((StatementTerm)currentTopLevelCentralPojo).setLfTerm(mt);
					} else {
						((StatementTerm)currentTopLevelCentralPojo).setRtTerm(mt);
					}
				} else if (currentTopLevelCentralPojo instanceof FunctionTerm) {
					((FunctionTerm)currentTopLevelCentralPojo).setTerm(mt);
				}
				
			} else if (isQDSMeasurementOperator(dbChildOp)) {
				//QDSMEASUREMENT TERM
				QDSMeasurementTerm qmt = new QDSMeasurementTerm();
				qmt.setQDSOperator(oc.toQDSMeasurementTermOp(dbChildOp));
				setToParent(currentTopLevelCentralPojo, topLevelDBDecision, dbChild, qmt);					
				loadAll(qmt, dbChild);

			} else if (isStatementOperator(dbChildOp)) {
				//STATEMENT TERM
				StatementTerm st = new StatementTerm();
				st.setOperator(oc.toStatementTermOp(dbChildOp));
				setToParent(currentTopLevelCentralPojo, topLevelDBDecision, dbChild, st);
				loadAll(st, dbChild);
			
			} else if (tu.isConditional(dbChildOp)) {
				//CONDITIONAL
				Conditional cn = new Conditional();
				cn.setOperator(oc.toConditionalOp(dbChildOp));
				setToParent(currentTopLevelCentralPojo, topLevelDBDecision, dbChild, cn);
				loadAll(cn, dbChild);
				
			} else if (isFunction(dbChildOp)) {
				//FUNCTION TERM
				FunctionTerm ft = new FunctionTerm();
				setToParent(currentTopLevelCentralPojo, topLevelDBDecision, dbChild, ft);
				loadAll(ft, dbChild);
				
			} else if (tu.isClauseOperation(dbChildOp) && dbChild.getClauseId()!=null) {
				//CLAUSE
				//use clauseid to get decisionid
				String clauseId = dbChild.getClauseId();
				Clause clause = getClauseDAO().find(clauseId);
				String decisionId = clause.getDecision().getId();
				
				Decision innerClauseDecision = getDecisionDAO().find(decisionId);
				org.ifmc.mat.shared.model.Decision sd = preLoad(innerClauseDecision);
				List<org.ifmc.mat.shared.model.Decision> decisions = new ArrayList<org.ifmc.mat.shared.model.Decision>();
				decisions.add(sd);
				clause.setDecisions(decisions);
				//Stop here!
				if (currentTopLevelCentralPojo instanceof QDSMeasurementTerm) {
					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
						((QDSMeasurementTerm)currentTopLevelCentralPojo).setLfQDS(clause);
					} else {
						((QDSMeasurementTerm)currentTopLevelCentralPojo).setRtQDS(clause);
					}
				} else if (currentTopLevelCentralPojo instanceof StatementTerm) {
					if (dbChild.getOrderNum().equalsIgnoreCase(lfOrder)) {
						((StatementTerm)currentTopLevelCentralPojo).setLfTerm(clause);
					} else {
						((StatementTerm)currentTopLevelCentralPojo).setRtTerm(clause);
					}
				} else if (currentTopLevelCentralPojo instanceof FunctionTerm) {
					((FunctionTerm)currentTopLevelCentralPojo).setTerm(clause);
				} else if (currentTopLevelCentralPojo instanceof Conditional) {
					((Conditional)currentTopLevelCentralPojo).getDecisions().add(clause);
				}			
			}
		}
	}
	
	//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$test of refactoring$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

	////////////////////////////////////////////////save///////////////////////////////////////////////
	//****************************SAVE*********************************
	//save - process top level parent
	public void saveClause(Clause clausePojo) {
		
		String decisionId = clausePojo.getDecision().getId();
		
		if (decisionId!=null) {
			//delete all existing decisions/QDSTerms/MeasurementTerms
			//will rebuild with new keys using clause object
			//this ensures updates to occur correctly
			deleteDecisions(decisionId);
		}
		
		Decision dbTopLevelParent = new Decision();
		
		//create clause op row in decision table
		dbTopLevelParent.setId(getId());
		dbTopLevelParent.setOperator("CLAUSE");
		
		clausePojo.setDecision(dbTopLevelParent);

		//update any changes, ie, new decisionId
		getClauseDAO().save(clausePojo);
		
		List<Object> terms = new ArrayList<Object>();
		
		for (org.ifmc.mat.shared.model.Decision decisionPojo : clausePojo.getDecisions()) {
			saveDecision(decisionPojo, dbTopLevelParent, terms, lfOrder);			
		}
		
	    //SAVE TO DB//
		//save parent first
		getDecisionDAO().save(dbTopLevelParent);
		//save children
		for (Object obj : terms) {
			if (obj instanceof QDSTerm) {
				getqDSTermDAO().save((QDSTerm)obj);
			} else if (obj instanceof MeasurementTerm) {
				getMeasurementTermDAO().save((MeasurementTerm)obj);
			}
		}
	}

	//save - process child
	private void saveDecision(org.ifmc.mat.shared.model.Decision decisionPojo, Decision dbTopLevelParent, List<Object> terms, String orderNum) {
		if (decisionPojo==null) return;
		
		//handle all child terms/conditionals recursively
		if (decisionPojo instanceof QDSMeasurementTerm) {
			QDSMeasurementTerm qDSMeasurementTerm = (QDSMeasurementTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(oc.qDSMeasureTermToString(qDSMeasurementTerm.getQDSOperator()));
			newDbChild.setOrderNum(orderNum);
			//dbTopLevelParent.setOrderNum(lfOrder);//new
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			saveDecision(qDSMeasurementTerm.getLfTerm(), newDbChild, terms, lfOrder);//handle children
			saveDecision(qDSMeasurementTerm.getRtTerm(), newDbChild, terms, rtOrder);//handle children
			
		} else if (decisionPojo instanceof StatementTerm) {
			StatementTerm statementTerm = (StatementTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(oc.statementTermToString(statementTerm.getOperator()));
			newDbChild.setOrderNum(orderNum);
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			saveDecision(statementTerm.getLfTerm(), newDbChild, terms, lfOrder);//handle children
			saveDecision(statementTerm.getRtTerm(), newDbChild, terms, rtOrder);//handle children
			
		} else if (decisionPojo instanceof QDSTerm) {
			QDSTerm qDSTerm = (QDSTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(QDS_TERM);
			newDbChild.setOrderNum(orderNum);
			//for Decision table
			dbTopLevelParent.getChildDecisions().add(newDbChild);			
			
			//for QDS_Term table
			qDSTerm.setId(getId());
			qDSTerm.setDecisionId(newDbChild.getId());
			terms.add(qDSTerm);//this makes it a dead end

		} else if (decisionPojo instanceof MeasurementTerm) {
			MeasurementTerm measurementTerm = (MeasurementTerm) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setOperator(MEASUREMENT_TERM);
			newDbChild.setOrderNum(orderNum);
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
			newDbChild.setOrderNum(orderNum);
			//for DECISION table
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			//function term may have other terms, send to saveDecision() to process
			saveDecision(functionTerm.getTerm(), newDbChild, terms, lfOrder);//handle children
			
		} else if (decisionPojo instanceof Conditional) {			
			Conditional conditional = (Conditional) decisionPojo;
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			if (conditional.getOperator()!=null) {
				newDbChild.setOperator(oc.conditionalToString(conditional.getOperator()));
			}
			newDbChild.setOrderNum(lfOrder);
			
			//row for DECISION table
			dbTopLevelParent.getChildDecisions().add(newDbChild);
			
			for (org.ifmc.mat.shared.model.Decision d : conditional.getDecisions()) {
				saveDecision(d, newDbChild, terms, lfOrder);
			}
		} else if (decisionPojo instanceof Clause) {
			Clause clause = (Clause) decisionPojo;
			
			Decision newDbChild = new Decision();
			newDbChild.setId(getId());
			newDbChild.setParentId(dbTopLevelParent.getId());
			newDbChild.setClauseId(clause.getId());//this makes it a dead end
			newDbChild.setOperator("CLAUSE");
			newDbChild.setOrderNum(lfOrder);
			if (clause.getId()!=null) {
				newDbChild.setClauseId(clause.getId());//this makes it a dead end
				dbTopLevelParent.getChildDecisions().add(newDbChild);			
			} else {
				dbTopLevelParent.getChildDecisions().add(newDbChild);			
				for (org.ifmc.mat.shared.model.Decision d : clause.getDecisions()) {
					saveDecision(d, newDbChild, terms, lfOrder);
				}
			}
		}
	}
	//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$test of refactoring$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
	
}

