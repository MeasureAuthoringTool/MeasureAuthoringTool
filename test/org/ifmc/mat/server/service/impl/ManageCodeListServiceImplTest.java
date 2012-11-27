package org.ifmc.mat.server.service.impl;

import org.ifmc.mat.client.codelist.ManageCodeListDetailModel;
import org.ifmc.mat.client.codelist.service.SaveUpdateCodeListResult;
import org.ifmc.mat.dao.CodeListDAO;
import org.ifmc.mat.dao.ListObjectDAO;
import org.ifmc.mat.dao.SpringInitializationTest;
import org.ifmc.mat.sprint1Testcase.TestCaseUtil;
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
 
