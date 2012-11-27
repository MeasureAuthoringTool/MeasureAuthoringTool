package mat.server.service.impl;

import mat.client.codelist.ManageCodeListDetailModel;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.dao.CodeListDAO;
import mat.dao.ListObjectDAO;
import mat.dao.SpringInitializationTest;
import mat.server.service.impl.ManageCodeListServiceImpl;
import mat.sprint1Testcase.TestCaseUtil;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class ManageCodeListServiceImplTest extends SpringInitializationTest {
	
	@Autowired
	protected ManageCodeListServiceImpl codeListService;
	@Autowired
	protected CodeListDAO codeListDAO;
	@Autowired
	protected ListObjectDAO listObjectDAO;
	
	private TestCaseUtil tcu = new TestCaseUtil();
	
	@Test
	@Rollback
	public void insertTest() throws Exception {
		ManageCodeListDetailModel model = tcu.createManageCodeListDetailModel();
		SaveUpdateCodeListResult result = codeListService.saveorUpdateCodeList(model);
		assertEquals(true, result.isSuccess());
	}

	
}
 
