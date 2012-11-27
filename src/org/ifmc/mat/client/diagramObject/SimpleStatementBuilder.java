package org.ifmc.mat.client.diagramObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ifmc.mat.client.clause.AppController;
import org.ifmc.mat.client.clause.MeasurePhrases;
import org.ifmc.mat.model.QualityDataSetDTO;
import org.ifmc.mat.model.clause.Clause;
import org.ifmc.mat.shared.Attribute;
import org.ifmc.mat.shared.model.AttributeTerm;
import org.ifmc.mat.shared.model.Conditional;
import org.ifmc.mat.shared.model.Decision;
import org.ifmc.mat.shared.model.FunctionTerm;
import org.ifmc.mat.shared.model.MeasurementTerm;
import org.ifmc.mat.shared.model.QDSMeasurementTerm;
import org.ifmc.mat.shared.model.QDSTerm;
import org.ifmc.mat.shared.model.StatementTerm;
import org.ifmc.mat.shared.model.util.OperatorConverter;


public class SimpleStatementBuilder {

	private AppController appController;
	private List<QualityDataSetDTO> qds = null;
	private MeasurePhrases measurePhrases;

	public SimpleStatementBuilder(AppController appController, List<QualityDataSetDTO> qdsList, MeasurePhrases measurePhrases) {
		this.appController = appController;
		this.qds = qdsList;
		this.measurePhrases = measurePhrases;
	}
	
	public void handleAClause(Clause cl) {
		//insert the statement first before data mapping to retain position in the list.
		SimpleStatement stmt = null;
		if (cl.getContextId().equals(SimpleStatement.CLAUSE_TYPE.CLAUSE_LIBRARY.getClauseType()) && (cl.getClauseTypeId()==null)){
			stmt = new SimpleStatement(appController, SimpleStatement.CLAUSE_TYPE.CLAUSE_LIBRARY);
		}else {
			stmt = new SimpleStatement(appController, SimpleStatement.CLAUSE_TYPE.MEASURE_PHRASE);
		}
		
		stmt.setIdentity(cl.getName());
		//Fixing description persisting problem
		stmt.setDescription(cl.getDescription());
		stmt.setmPhraseUniqueIdentity(cl.getId());
		stmt.setChangedName(cl.getName());
		//US 601 retaining status value
		if(cl.getStatusId()!= null){
			stmt.setStatus(cl.getStatusId().getDescription());
		}
		Decision dummy = cl.getDecisions().get(0);
		
		if(dummy.getDecisions().size()<1) return;
		
		Decision child = dummy.getDecisions().get(0);

		//A clause always contains only a single decision
		handleDecision(dummy, child, stmt, 0);
		if (this.measurePhrases.getItem(cl.getName()) == null) {
				this.measurePhrases.insert(stmt.getIdentity(), stmt);
		}if(this.measurePhrases.getSavedItem(cl.getName()) == null){
				this.measurePhrases.insertSavedItem(stmt.getIdentity(), stmt);
		}
	}

	

	/**
	 * Handles clauses within clauses. If a clause is already handled then, 
	 * don't recreate a UI object. Simply add it like a QDM term-only clause 
	 * name.
	 **/
	public void buildOrAttachClause(Clause cl, SimpleStatement stmt, int pos) {
		
		if (this.measurePhrases.getItem(cl.getName()) == null) {
			Decision dummy = cl.getDecisions().get(0);
			//insert the statement first before data mapping to retain position in the list.
			SimpleStatement stmtnew = new SimpleStatement(appController);
			stmtnew.setIdentity(cl.getName());
			
			if (!dummy.getDecisions().isEmpty()) {

				Decision child = dummy.getDecisions().get(0);
				handleDecision(dummy, child, stmtnew, 0);
			}

			this.measurePhrases.insert(stmtnew.getIdentity(), stmtnew);
		} 
		//consider Clause name same as QDM ID.
		buildQDSTerm(cl.getName(), stmt, pos);
	}

	/**	 * Handles a function term. A function shall have a name, a decision, or a comparison quantity. 
	 **/
	public void buildFunctionTerm(org.ifmc.mat.shared.model.FunctionTerm function, SimpleStatement stmt) {
		stmt.function = function.getName();
		stmt.condition = SimpleStatement.NONE;		
		Decision dec = function.getTerm();
		handleDecision(function, dec, stmt, 0);
	}

	/**
	 * Handles a Statement term. A statement term may combine a QDSMeasurementTerm, 
	 * a QDM element, or a clause with a comparison quantity. 
	 **/
	public void buildStatementTerm(org.ifmc.mat.shared.model.StatementTerm quantityTerm, SimpleStatement stmt) {
		//Assumption: This class implementation tightly coupled to the 
		//functionality of Measure Phrases editor. so, if the right hand side term is
		//a measurement term then set the operator related to the measurement term else 
		//set the "main" condition operator. -Vasant.12/29/2010
//		if (quantityTerm.getRtTerm() instanceof MeasurementTerm) {
//			stmt.conditionalOperator = quantityTerm.getOperatorAsString();
//		} else {
//			stmt.condition = quantityTerm.getOperatorAsString();
//			stmt.mode = MODE.COMPARISON;
//		}

		int i=0;
		for(Decision stmtchild : quantityTerm.getDecisions()) {
			handleDecision(quantityTerm, stmtchild, stmt, i);
			i++;
		}
	}

	/**
	 * 
	 * Handles a conditional operator. A conditional may combine one to many
	 * clauses or QDM elements in any order.
	 **/
	public void buildConditional(org.ifmc.mat.shared.model.Conditional cond, SimpleStatement stmt) {
		stmt.condition = cond.getOperator().toString();
		stmt.mode = SimpleStatement.MODE.LOGICAL;
		processClauseORQDSDecisions(cond, stmt); 
	}
	/**
	 * 
	 * Handles a Timing relativity operator. A timing relativity relates a
	 * clause or QDM elements to another clause or a QDS elements.
	 **/
	public void buildTimingRelativity(org.ifmc.mat.shared.model.QDSMeasurementTerm relativity, SimpleStatement stmt) {
		stmt.condition = Rel.shortToLongIdentity(relativity.getQDSOperator());
		stmt.mode = SimpleStatement.MODE.COMPARISON;
		processClauseORQDSDecisions(relativity, stmt);
	}

	private void processClauseORQDSDecisions(Decision cond,
			SimpleStatement stmt) {
		int i=0;
		for (Decision dec: cond.getDecisions()) {
				handleAClauseOrDecision(stmt, dec,i);
				i++;
			}
	}

	private void buildMeasurementTerm(SimpleStatement stmt,
			Decision parent, MeasurementTerm measurement) {
		StatementTerm stmtTerm = (StatementTerm)parent;

		if (stmtTerm.getLfTerm() instanceof FunctionTerm) {
			stmt.functionQuantity = measurement.getQuantity();
			stmt.functionOperator =  new OperatorConverter().convertStatementOperatorTofunctionOperator(
					stmtTerm.getOperator().name());
			stmt.functionUnit = measurement.getUnit();
			stmt.conditionalOperator = null;
			stmt.quantity = null;
			stmt.unit = null; 
			FunctionTerm funcTerm = (FunctionTerm) stmtTerm.getLfTerm();
			if( funcTerm.getTerm() instanceof QDSMeasurementTerm) {
				stmt.mode = stmt.mode.COMPARISON;
			} else {
				stmt.mode = stmt.mode.FUNCTION;
			}
		} else {
			stmt.conditionalOperator = stmtTerm.getOperatorAsString();
			stmt.unit = measurement.getUnit();
			stmt.quantity = measurement.getQuantity();

			//If the phrase2 text box is not filled up or is not available,
			//then, the quantity box is filled using the "Change to quantity" button.
			if (stmt.phrase2 == null || stmt.phrase2.getText() == null) {
				stmt.mode = stmt.mode.SINGLE;
				stmt.singleSubMode = stmt.singleSubMode.COMPARISON;
			} else {
				stmt.mode = stmt.mode.COMPARISON;
			}
		}
	}

	public void handleDecision(Decision parent, Decision child, SimpleStatement stmt, int pos) {
		//handle functions
		//Handle statement Term
		//Handle QDS measurement Term.
		//Handle QDS Term.
		//Handle ClauseToo

		if (child instanceof Conditional) {
			buildConditional(((Conditional)child), stmt);

		} else if (child instanceof QDSMeasurementTerm) {
			buildTimingRelativity((QDSMeasurementTerm)child, stmt);
		
		} else if (child instanceof StatementTerm) {
			buildStatementTerm((StatementTerm)child, stmt);
		
		} else if (child instanceof FunctionTerm) {
//			if (parent instanceof StatementTerm) {
//				StatementTerm funcParent = ((StatementTerm)parent);
//				stmt.functionOperator = new OperatorConverter().convertStatementOperatorTofunctionOperator(funcParent.getOperator().name());
//				stmt.conditionalOperator = "";
//				if( funcParent.getRtTerm() instanceof MeasurementTerm) {
//					MeasurementTerm mTerm = (MeasurementTerm)funcParent.getRtTerm();
//					stmt.functionQuantity = mTerm.getQuantity();
//				}
//			}
			buildFunctionTerm((FunctionTerm)child, stmt);
		
		} else if (child instanceof Clause || child instanceof QDSTerm) {
			handleAClauseOrDecision(stmt, child, pos);
		
		} else if (child instanceof MeasurementTerm) {
			buildMeasurementTerm(stmt, parent, (MeasurementTerm)child);
		}

	}

	private void handleAClauseOrDecision(SimpleStatement stmt, Decision dec, int pos) {
		if ( dec instanceof QDSTerm) {
			QualityDataSetDTO dtoTemp = new QualityDataSetDTO();
			dtoTemp.setId(((QDSTerm)dec).getqDSRef());
			int ind = -1;
			if ( (ind = this.qds.indexOf(dtoTemp)) >= 0) {
				buildQDSTerm(this.qds.get(ind).toString(), stmt, pos);
				buildQDSTermAttributes((QDSTerm)dec, stmt, pos);				
			}
		} else if (dec instanceof Clause) {
			buildOrAttachClause((Clause)dec, stmt, pos);
		}
	}

	private void buildQDSTermAttributes(QDSTerm qdsTerm , SimpleStatement stmt, int pos) {
		try {
			//get constants from central location
			String COMPARISON = "Comparison";
			String QDS_ELEMENT = "Value Set";
			String PRESENT = "Check if Present";
			String A_NOT_EMPTY = "A_NOT_EMPTY";
			
			List<Attribute> attributes = new ArrayList<Attribute>();
			List<Decision> dList = qdsTerm.getAttributes();
			
			for (Decision d : dList) {
				AttributeTerm at = (AttributeTerm)d;
				Attribute attribute = new Attribute(null, null, null, null, null, null);
				
				//left term always exists
				attribute.setAttribute(at.getLfTerm());
			
				//either a QDSTerm or MeasurementTerm
				//if operator exists
				if (at.getOperator()!=null) {
					OperatorConverter oc = new OperatorConverter();
					attribute.setComparisonOperator(oc.attributeTermToClientString(at.getOperator()));
				}
				
				Decision decision = at.getRtTerm();
				if (decision!=null) {				
					if (decision instanceof QDSTerm) {
						QDSTerm q = (QDSTerm)decision;
						QualityDataSetDTO dto = new QualityDataSetDTO();
						dto.setId(q.getqDSRef());
						//TODO://if should always be true.
						if (qds.indexOf(dto) >0 ) {
							attribute.setTerm(qds.get(qds.indexOf(dto)).toString());
							attribute.setType(QDS_ELEMENT);
						}
					} else if (decision instanceof MeasurementTerm) {
						MeasurementTerm m = (MeasurementTerm)decision;
						attribute.setQuantity(m.getQuantity());
						attribute.setUnit(m.getUnit());
						attribute.setType(COMPARISON);
					}
				} else {
					attribute.setType(PRESENT);
					attribute.setComparisonOperator(A_NOT_EMPTY);
				}
				attributes.add(attribute);
			}
			
			if (pos == 0) {
				stmt.phrase1.setAttributes(attributes);
			}else if (pos == 1) {
				stmt.phrase2.setAttributes(attributes);
			} else {
				stmt.additionalPhraseList.get(pos-2).setAttributes(attributes);
			}
		} catch (Exception e) {
			//let process continue if exception
			e.printStackTrace();
		}
	}
	
	public void buildQDSTerm(String qdsId, SimpleStatement stmt, int pos) {
		if (pos == 0) {
			stmt.phrase1.setText(qdsId);
		}else if (pos == 1) {
			stmt.phrase2.setText(qdsId);
		} else {
			stmt.additionalPhraseList.add(new Phrase(appController, qdsId));
		}
		
	}
}
