package mat.server.service.impl;

import java.util.List;

import mat.dao.SpringInitializationTest;
import mat.dao.clause.ClauseDAO;
import mat.model.clause.Clause;
import mat.server.clause.ClauseServiceImpl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class ClauseServiceImplTest extends SpringInitializationTest {
	@Autowired
	protected ClauseServiceImpl clauseServiceImpl;
	@Autowired
	protected ClauseDAO clauseDAO;
	
	@Test
	public void testLoadClause() {
		List<Clause> clauses = clauseDAO.find();
		
		if(!clauses.isEmpty()){
			Clause clause1 = clauses.get(0);
			String clauseId = clause1.getId();
			String measureId = clause1.getMeasureId();
			clauseServiceImpl.setContext(applicationContext);
			Clause clause2 = clauseServiceImpl.load(measureId, clauseId);
			
			assertTrue(clause1 != null);
			assertTrue(clause2 != null);
		}
	}


}
