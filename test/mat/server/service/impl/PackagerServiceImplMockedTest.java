package mat.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.dao.clause.ClauseDAO;
import mat.dao.clause.PackagerDAO;
import mat.model.clause.Clause;
import mat.server.service.impl.PackagerServiceImpl;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PackagerServiceImplMockedTest extends BaseServiceMockedTest {
	
	@Autowired
	protected PackagerServiceImpl packagerService;
	@Autowired
	protected ClauseDAO clauseDAO;
	@Autowired
	private PackagerDAO packagerDAO;
	
	@After
	public void teardown() {
		EasyMock.reset(clauseDAO, packagerDAO);
	}
	
	
	
	@Test
	public void testFilterClausesByContextId() {
		String measureId = "measureId";
			
		List<Clause> clauseList = new ArrayList<Clause>();
		Clause c1 = new Clause();
		c1.setContextId("1");
		c1.setName("Clause1");
		clauseList.add(c1);
		
		Clause c2 = new Clause();
		c2.setContextId("2");
		c2.setName("Clause2");
		clauseList.add(c2);
		
		Clause c3 = new Clause();
		c3.setContextId("3");
		c3.setName("Clause3");
		clauseList.add(c3);
		
		Clause c4 = new Clause();
		c4.setContextId("4");
		c4.setName("Clause4");
		clauseList.add(c4);
		
		Clause c5 = new Clause();
		c5.setContextId("5");
		c5.setName("Clause5");
		clauseList.add(c5);
		
		Clause c6 = new Clause();
		c6.setContextId("6");
		c6.setName("Clause6");
		clauseList.add(c6);
		
		Clause c7 = new Clause();
		c7.setContextId("9");
		c7.setName("Clause7");
		clauseList.add(c7);
		
		EasyMock.expect(clauseDAO.findByMeasureId(measureId, null)).andReturn(clauseList);
		EasyMock.replay(clauseDAO);
		
		MeasurePackageOverview overview = 
			packagerService.buildOverviewForMeasure(measureId);
		
		assertTrue(clauseFound(overview, c1));
		assertTrue(clauseFound(overview, c2));
		assertFalse(clauseFound(overview, c3));
		assertTrue(clauseFound(overview, c4));
		assertTrue(clauseFound(overview, c5));
		assertFalse(clauseFound(overview, c6));
		assertFalse(clauseFound(overview, c7));
		
	}
	
	private boolean clauseFound(MeasurePackageOverview overview, Clause clause) {
		for(MeasurePackageClauseDetail nvp : overview.getClauses()) {
			if(nvp.getName().equals(clause.getName())) {
				return true;
			}
		}
		return false;
	}
	


}
