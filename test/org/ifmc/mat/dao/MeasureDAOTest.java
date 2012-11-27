package org.ifmc.mat.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mat.dao.impl.clause.ConditionalDAO;
import mat.dao.impl.clause.MeasureDAO;
import mat.model.clause.Clause;
import mat.model.clause.Decision;
import mat.model.clause.Measure;
import mat.shared.model.MeasurementTerm;
import mat.shared.model.QDSTerm;

import org.junit.Test;

public class MeasureDAOTest extends SpringInitializationTest {
	
	@Test
	public void testCreate() {
		testParentChild();
	}
	@Test
	public void testSave() {
		testSave1();
	}
		
	private void testSave1() {
		//load mocked up data using java code into decsion/term tables
		testParentChild();
		
		//put key of clause from mocked up code
		String decisionId = "4d3d72bd-2c3d-4aba-aab6-8bc8d9e37b7d";
		
		//load mocked up data from tables to memory objects
		ConditionalDAO conditionalDAO = new ConditionalDAO(getService());		
		Decision decision = getService().getDecisionDAO().find(decisionId);
		
		
		mat.shared.model.Decision sharedDecision = conditionalDAO.load(decision.getId());
		List<mat.shared.model.Decision> decisions = new ArrayList<mat.shared.model.Decision>();
		decisions.add(sharedDecision);

		//conditionalDAO.save(conditional);
		
		
		//let's try to save from meaure object
		Measure measure = new Measure();
		
		measure.setaBBRName("AMA");
		measure.setDescription("ama measure");
		measure.setVersion("22.33");
		measure.setMeasureStatus("pending");
		Clause clause = new Clause();
//		clause.setDecisions(conditional.getDecisions());
		clause.setName("AMA_1_POPULATION_1");
		clause.setContextId("System");
		clause.setDecision(decision);
		
		List<Clause> clauses = new ArrayList<Clause>();
		clauses.add(clause);
		measure.setClauses(clauses);
		
		MeasureDAO measureDAO = new MeasureDAO(getService());
		measureDAO.saveMeasure(measure);
		System.out.println();
	}
//////////////////////////create rows////////////////////////
	private void testParentChild() {
		
		List<Object> terms = new ArrayList<Object>();
		
		///////////PARENT//////////////////
		Decision parent = new Decision();
		parent.setOperator("CLAUSE");
		
		String parentId = getId();
		
		parent.setId(parentId);
		parent.setOrderNum("0");
		//////////////////////////////////
		//////000 level children////////
		Decision child0 = new Decision();
		child0.setOperator("GREATER_THAN_OR_EQUAL_TO");
		child0.setParentId(parentId);
		String child0Id = getId();
		child0.setId(child0Id);
		child0.setOrderNum("0");
		
		
		parent.getChildDecisions().add(child0);
	
		//////first level children////////
		Decision child1 = new Decision();
		child1.setOperator("DURING");
		child1.setParentId(child0Id);
		String child1Id = getId();
		child1.setId(child1Id);
		child1.setOrderNum("0");
		
		
		//down to atomic level
		Decision child2 = new Decision();
		child2.setOperator("MEASUREMENTTERM");
		child2.setParentId(child0Id);
		String child2Id = getId();
		child2.setId(child2Id);
		child2.setOrderNum("1");
		
		MeasurementTerm mt = new MeasurementTerm();
		mt.setQuantity("5");
		mt.setUnit("years");
		mt.setId(getId());
		mt.setDecisionId(child2.getId());
		terms.add(mt);

		child0.getChildDecisions().add(child1);
		child0.getChildDecisions().add(child2);
		//////////////////////////////////

		
		//////second level children for child1////
		Decision child11 = new Decision();
		child11.setOperator("SBOD");
		child11.setParentId(child1Id);
		child11.setId(getId());
		child11.setOrderNum("0");

		Decision child12 = new Decision();
		child12.setOperator("QDSTERM");
		child12.setParentId(child1Id);
		child12.setId(getId());
		child12.setOrderNum("1");
		
		QDSTerm qt12 = new QDSTerm();
		qt12.setqDSRef("3");
		qt12.setId(getId());
		qt12.setDecisionId(child12.getId());
		terms.add(qt12);
		
		child1.getChildDecisions().add(child11);
		child1.getChildDecisions().add(child12);
		//////////////////////////////////
		
		
		///////third level children for child11//////
		Decision child21 = new Decision();
		child21.setOperator("QDSTERM");
		child21.setParentId(child11.getId());
		child21.setId(getId());
		child21.setOrderNum("0");

		QDSTerm qt21 = new QDSTerm();
		qt21.setqDSRef("1");
		qt21.setId(getId());
		qt21.setDecisionId(child21.getId());
		terms.add(qt21);

		Decision child22 = new Decision();
		child22.setOperator("QDSTERM");
		child22.setParentId(child11.getId());
		child22.setId(getId());
		child22.setOrderNum("1");

		QDSTerm qt22 = new QDSTerm();
		qt22.setqDSRef("2");
		qt22.setId(getId());
		qt22.setDecisionId(child22.getId());
		terms.add(qt22);

		child11.getChildDecisions().add(child21);
		child11.getChildDecisions().add(child22);
		//////////////////////////////////

		getService().getDecisionDAO().save(parent);
		for (Object obj : terms) {
			if (obj instanceof QDSTerm) {
				getService().getqDSTermDAO().save((QDSTerm)obj);
			} else if (obj instanceof MeasurementTerm) {
				getService().getMeasurementTermDAO().save((MeasurementTerm)obj);
			}
		}
	}

/////////////////////////////////////////////////////////////	
	private String getId() {
		return UUID.randomUUID().toString();
	}
	
}
