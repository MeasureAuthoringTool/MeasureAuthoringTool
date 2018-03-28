package mat.dao;

import org.junit.Test;
/*import mat.model.clause.Clause;
import mat.model.clause.Decision;
*/

public class ClauseDAOTest extends SpringInitializationTest {

	@Test
	public void testCreate() {
//		testParentChild();
	}
	@Test
	public void testLoad() {
//		testLoad1();
	}
//*************************************************************************//	
	@Test
	public void testDelete() {
		//example of clause delete by id
		//String[] ids = {"8ae454132c73d437012c73d44daf0001"};
		//getService().getClauseDAO().delete(ids);

		//example of a clause/decision delete
//		Clause clause = new Clause();
//		ConditionalDAO c = new ConditionalDAO(getService());
//		c.deleteDecisions(clause.getDecisionId());
//		getService().getClauseDAO().delete(clause);
		
		
		
		//delete decision only
		//ConditionalDAO c = new ConditionalDAO(getService());
		//c.deleteDecisions("");

	}
	
	private void testLoad1() {
//		String clauseId = "8ae454132c75fa5a012c75fa6f1e0001";
//		String measureId = "f5c48c2f-0187-44e6-8922-a1f962fedaf6";
//		ClauseDAO clauseDAO = new ClauseDAO(getService());
//		Clause clause = clauseDAO.loadClause(measureId, clauseId);
//		System.out.println();
	}
	@Test
	public void testSave() {
//		testParentChild();
		testSave1();
	}
	
	private void testSave1() {		
//		//original
//		String clauseId = "8ae454132c7e3e8f012c7e3ea9640001";
//		String measureId = "994b437a-ecce-4261-9ded-ecb0d7b605a6";
//		ClauseDAO clauseDAO = new ClauseDAO(getService());
//		Clause clause = clauseDAO.loadClause(measureId, clauseId);
//		System.out.println();
//		
//		//save clause only
//		clause.setMeasureId(getId());
//		getService().getClauseDAO().save(clause);
//		
//		//save 
//		ClauseManagerDAO cm = new ClauseManagerDAO(getService());
//		cm.saveClause(clause);
//		
	}
	
//	private void testSave1() {
//		
//		List<Decision> dList = getService().getDecisionDAO().findByOperator("CLAUSE");
//		//put key of clause from mocked up code
//		String decisionId = dList.get(0).getId();
//		
//		//load mocked up data from tables to memory objects
//		ConditionalDAO conditionalDAO = new ConditionalDAO(getService());		
//		Decision decision = getService().getDecisionDAO().find(decisionId);
//		Conditional conditional = conditionalDAO.load(decision.getId());
//		//conditionalDAO.save(conditional);
//		
//		Clause clause = new Clause();
//		clause.setDecisions(conditional.getDecisions());
//		clause.setName("AMA_1_POPULATION_1");
//		clause.setContextId("System");
//		clause.setDecisionId(decisionId);
//		
//		System.out.println();
//		
//		clause.setMeasureId(getId());
//		getService().getClauseDAO().save(clause);
//		
//		ConditionalDAO c = new ConditionalDAO(getService());
//		c.save(clause);
//	}
//////////////////////////create rows////////////////////////
	
	
	
	/*private void testParentChild() {
		
		List<Object> terms = new ArrayList<Object>();
		
		
		//test of (((1 SBOD 2)DURING 3)>=(5,YEARS)) OR testClause
		//testClause = (((4 SBOD 5)DURING 6)>=(10,YEARS))
				
		///////////PARENT//////////////////
		//a CLAUSE row WITHOUT clauseId means it is a parent row of the clause
		Decision parent = new Decision();
		parent.setOperator("CLAUSE");
		String parentId = getId();
		
		parent.setId(parentId);
		parent.setOrderNum("0");
		//////////////////////////////////
		
		///////////PARENT//////////////////
		Decision nextParent = new Decision();
		nextParent.setOperator("OR");
		
		String nextParentId = getId();
		
		nextParent.setId(nextParentId);
		nextParent.setParentId(parentId);
		nextParent.setOrderNum("0");

		parent.getChildDecisions().add(nextParent);
		//////////////////////////////////

		//////zero level children////////
		Decision child01 = new Decision();
		child01.setOperator("GREATER_THAN_OR_EQUAL_TO");
		child01.setParentId(nextParentId);
		String child01Id = getId();
		child01.setId(child01Id);
		child01.setOrderNum("0");
		
		
		//a CLAUSE row with clauseId means it will reference an exisiting clause via clauseId
		//down to atomic level
		Decision child02 = new Decision();
		child02.setOperator("CLAUSE");
		child02.setParentId(nextParentId);
		String child02Id = getId();
		child02.setId(child02Id);
		child02.setOrderNum("1");
		child02.setClauseId("clauseId");

		nextParent.getChildDecisions().add(child01);
		nextParent.getChildDecisions().add(child02);		
		//////////////////////////////////
		
		
		//////first level children////////
		Decision child1 = new Decision();
		child1.setOperator("DURING");
		child1.setParentId(child01Id);
		String child1Id = getId();
		child1.setId(child1Id);
		child1.setOrderNum("0");
		
		
		//down to atomic level
		Decision child2 = new Decision();
		child2.setOperator("MEASUREMENTTERM");
		child2.setParentId(child01Id);
		String child2Id = getId();
		child2.setId(child2Id);
		child2.setOrderNum("1");
		
		MeasurementTerm mt = new MeasurementTerm();
		mt.setQuantity("5");
		mt.setUnit("years");
		mt.setId(getId());
		mt.setDecisionId(child2.getId());
		terms.add(mt);

		parent.getChildDecisions().add(child1);
		parent.getChildDecisions().add(child2);
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

		//save to db using non shared pojo
		getService().getDecisionDAO().save(parent);
		for (Object obj : terms) {
			if (obj instanceof QDSTerm) {
				getService().getqDSTermDAO().save((QDSTerm)obj);
			} else if (obj instanceof MeasurementTerm) {
				getService().getMeasurementTermDAO().save((MeasurementTerm)obj);
			}
		}*/
		
		
		//load mocked up data from tables to memory objects
//		ConditionalDAO conditionalDAO = new ConditionalDAO(getService());		
//		Decision decision = getService().getDecisionDAO().find(parent.getId());
//		Conditional conditional = conditionalDAO.load(decision.getId());
		//conditionalDAO.save(conditional);
		
		/*Clause clause = new Clause();
//		clause.setDecisions(conditional.getDecisions());
		clause.setName("AMA_1_POPULATION_1");
		clause.setContextId("System");
		clause.setDecision(parent);
		
		System.out.println();
		
		clause.setMeasureId(getId());
		getService().getClauseDAO().save(clause);*/
		

//		mat.shared.model.Decision sharedDecision = new mat.shared.model.Decision();
//		
//		sharedDecision.
//		List<mat.shared.model.Decision> decisions = new ArrayList<mat.shared.model.Decision>();
//		decisions.add(parent);
//		Clause clause = new Clause();
//		clause.setDecisions(decisions);
//		clause.setName("AMA_1_POPULATION_1");
//		clause.setContextId("System");
//		clause.setDecisionId(parent.getId());

		
		/*createReferenceClause();*/
	//}
	
	
	
/*	private void createReferenceClause() {
		//test of (((4 SBOD 5)DURING 6)>=(10,YEARS))
		
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
		mt.setQuantity("10");
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
		qt12.setqDSRef("6");
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
		qt21.setqDSRef("4");
		qt21.setId(getId());
		qt21.setDecisionId(child21.getId());
		terms.add(qt21);

		Decision child22 = new Decision();
		child22.setOperator("QDSTERM");
		child22.setParentId(child11.getId());
		child22.setId(getId());
		child22.setOrderNum("1");

		QDSTerm qt22 = new QDSTerm();
		qt22.setqDSRef("5");
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
		
		Clause clause = new Clause();
//		clause.setDecisions(conditional.getDecisions());
		clause.setName("AMA_1_REFERENCE_CLAUSE");
		clause.setContextId("System");
		clause.setDecision(parent);
		
		System.out.println();
		
		clause.setMeasureId(getId());
		getService().getClauseDAO().save(clause);

	}*/


//	private void testParentChild() {
//		//test of (((1 SBOD 2)DURING 3)>=(5,YEARS))
//		
//		List<Object> terms = new ArrayList<Object>();
//		
//		///////////PARENT//////////////////
//		Decision parent = new Decision();
//		parent.setOperator("CLAUSE");
//		
//		String parentId = getId();
//		
//		parent.setId(parentId);
//		parent.setOrderNum("0");
//		//////////////////////////////////
//		//////000 level children////////
//		Decision child0 = new Decision();
//		child0.setOperator("GREATER_THAN_OR_EQUAL_TO");
//		child0.setParentId(parentId);
//		String child0Id = getId();
//		child0.setId(child0Id);
//		child0.setOrderNum("0");
//		
//		
//		parent.getChildDecisions().add(child0);
//	
//		//////first level children////////
//		Decision child1 = new Decision();
//		child1.setOperator("DURING");
//		child1.setParentId(child0Id);
//		String child1Id = getId();
//		child1.setId(child1Id);
//		child1.setOrderNum("0");
//		
//		
//		//down to atomic level
//		Decision child2 = new Decision();
//		child2.setOperator("MEASUREMENTTERM");
//		child2.setParentId(child0Id);
//		String child2Id = getId();
//		child2.setId(child2Id);
//		child2.setOrderNum("1");
//		
//		MeasurementTerm mt = new MeasurementTerm();
//		mt.setQuantity("5");
//		mt.setUnit("years");
//		mt.setId(getId());
//		mt.setDecisionId(child2.getId());
//		terms.add(mt);
//
//		child0.getChildDecisions().add(child1);
//		child0.getChildDecisions().add(child2);
//		//////////////////////////////////
//
//		
//		//////second level children for child1////
//		Decision child11 = new Decision();
//		child11.setOperator("SBOD");
//		child11.setParentId(child1Id);
//		child11.setId(getId());
//		child11.setOrderNum("0");
//
//		Decision child12 = new Decision();
//		child12.setOperator("QDSTERM");
//		child12.setParentId(child1Id);
//		child12.setId(getId());
//		child12.setOrderNum("1");
//		
//		QDSTerm qt12 = new QDSTerm();
//		qt12.setqDSRef("3");
//		qt12.setId(getId());
//		qt12.setDecisionId(child12.getId());
//		terms.add(qt12);
//		
//		child1.getChildDecisions().add(child11);
//		child1.getChildDecisions().add(child12);
//		//////////////////////////////////
//		
//		
//		///////third level children for child11//////
//		Decision child21 = new Decision();
//		child21.setOperator("QDSTERM");
//		child21.setParentId(child11.getId());
//		child21.setId(getId());
//		child21.setOrderNum("0");
//
//		QDSTerm qt21 = new QDSTerm();
//		qt21.setqDSRef("1");
//		qt21.setId(getId());
//		qt21.setDecisionId(child21.getId());
//		terms.add(qt21);
//
//		Decision child22 = new Decision();
//		child22.setOperator("QDSTERM");
//		child22.setParentId(child11.getId());
//		child22.setId(getId());
//		child22.setOrderNum("1");
//
//		QDSTerm qt22 = new QDSTerm();
//		qt22.setqDSRef("2");
//		qt22.setId(getId());
//		qt22.setDecisionId(child22.getId());
//		terms.add(qt22);
//
//		child11.getChildDecisions().add(child21);
//		child11.getChildDecisions().add(child22);
//		//////////////////////////////////
//
//		getService().getDecisionDAO().save(parent);
//		for (Object obj : terms) {
//			if (obj instanceof QDSTerm) {
//				getService().getqDSTermDAO().save((QDSTerm)obj);
//			} else if (obj instanceof MeasurementTerm) {
//				getService().getMeasurementTermDAO().save((MeasurementTerm)obj);
//			}
//		}
//	}
	
/////////////////////////////////////////////////////////////	
	
}
