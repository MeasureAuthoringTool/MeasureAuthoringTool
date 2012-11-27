package mat.sprint20.junit;

import java.util.UUID;

import mat.dao.SpringInitializationTest;
import mat.model.clause.Measure;
import mat.model.clause.MeasureSet;
import mat.server.MeasureLibraryServiceImpl;
import mat.server.service.MeasurePackageService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;

public class V1_0_2_US4_MeasureLibraryServiceImplTest extends SpringInitializationTest{


	private MeasureLibraryServiceImpl measureLibService = new MeasureLibraryServiceImpl();
	
	@Autowired
	private MeasurePackageService measurePackService;
	
	@Autowired
	protected ApplicationContext applicationContext;
	
	private Measure m = new Measure();
	@Before
	public void setUp(){
		m.setDescription("Test for Supplimental Data Elements");
		m.setaBBRName("SDE");
		m.setDraft(true);
		m.setMeasureStatus("In Progress");
		
	}
	
	@Test
	@Rollback
	public void test_v1_0_2_US4_CreateSDEForNewMeasure() throws Exception {
		MeasureSet measureSet = new MeasureSet();
		measureSet.setId(UUID.randomUUID().toString());
		measurePackService.save(measureSet);
		m.setMeasureSet(measureSet);
		measurePackService.save(m);
		measureLibService.setContext(applicationContext);
		measureLibService.createSupplimentalQDM(m);
	}
	
	
}
