package mat.server.service.impl;

import java.util.HashSet;
import java.util.Set;

import mat.client.codelist.ManageCodeListDetailModel;
import mat.dao.CodeListDAO;
import mat.dao.ListObjectDAO;
import mat.model.Category;
import mat.model.CodeList;
import mat.model.GroupedCodeList;
import mat.model.ListObject;
import mat.server.LoggedInUserUtil;
import mat.server.service.impl.ManageCodeListServiceImpl;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ManageCodeListServiceImpl2Test extends BaseServiceMockedTest {
	
	@Autowired
	protected ManageCodeListServiceImpl codeListService;
	@Autowired
	protected CodeListDAO codeListDAO;
	@Autowired
	protected ListObjectDAO listObjectDAO;
	
	@After
	public void teardown() {
		EasyMock.reset(codeListDAO, listObjectDAO);
	}
	
	@Before
	public void setup() {
//		EasyMock.resetToNice(codeListDAO, listObjectDAO, categoryDAO);
	}

	@Test
	public void testForNonUniqueCodeListExistingNotMatch() {
		String name = "TestCodeList";
		String catId = "1";
		String codeSystemId = "2";
		String steward = "TestSteward";
		String stewardOther = null;
		String oid = "3";
		
		ManageCodeListDetailModel model1 = new ManageCodeListDetailModel();
		model1.setName(name);
		model1.setCategory(catId);
		model1.setCodeSystem(codeSystemId);
		model1.setSteward(steward);
		model1.setStewardOther(stewardOther);
		model1.setOid(oid);
		//
		// editing a code list so that it matches a different existing code list
		//
		ManageCodeListDetailModel model = new ManageCodeListDetailModel();
		model.setName(name);
		model.setCategory(catId);
		model.setSteward(steward);
		model.setCodeSystem(codeSystemId);
		
		
		
		CodeList existingCl = new CodeList();
		String id = "id";
		model.setID(id);
		model.setExistingCodeList(true);
		existingCl.setId("xxx");
		EasyMock.expect(codeListDAO.getCodeList(model1, LoggedInUserUtil.getLoggedInUser())).andReturn(existingCl);
		EasyMock.replay(codeListDAO);
		
		assertFalse(codeListService.isCodeListUnique(model));
	}
	
	@Test
	public void testForNonUniqueCodeListNotExisting() {
		String name = "TestCodeList";
		String catId = "1";
		String codeSystemId = "2";
		String steward = "TestSteward";
		String stewardOther = null;
		String oid = "3";
		
		ManageCodeListDetailModel model1 = new ManageCodeListDetailModel();
		model1.setName(name);
		model1.setCategory(catId);
		model1.setCodeSystem(codeSystemId);
		model1.setSteward(steward);
		model1.setStewardOther(stewardOther);
		model1.setOid(oid);
		//
		// editing a code list so that it matches a different existing code list
		//
		ManageCodeListDetailModel model = new ManageCodeListDetailModel();
		model.setName(name);
		model.setCategory(catId);
		model.setSteward(steward);
		model.setCodeSystem(codeSystemId);
		
		CodeList existingCl = new CodeList();
		String id = "id";
		model.setExistingCodeList(false);
		existingCl.setId(id);
		EasyMock.expect(codeListDAO.getCodeList(model1, LoggedInUserUtil.getLoggedInUser())).andReturn(existingCl);
		EasyMock.replay(codeListDAO);
		

		assertFalse(codeListService.isCodeListUnique(model));
	}
	
	
	@Test
	public void testForNonUniqueGroupedCodeListExistingNotMatch() {
		String name = "TestCodeList";
		String catId = "1";
		String steward = "TestSteward";
		//
		// editing a code list so that it matches a different existing code list
		//
		ManageCodeListDetailModel model = new ManageCodeListDetailModel();
		model.setName(name);
		model.setCategory(catId);
		model.setSteward(steward);
		
		ListObject existingCl = new ListObject();
		String id = "id";
		model.setID(id);
		model.setExistingCodeList(true);
		existingCl.setId("xxx");
		EasyMock.expect(listObjectDAO.getListObject(name, steward, catId, LoggedInUserUtil.getLoggedInUser())).andReturn(existingCl);
		EasyMock.replay(listObjectDAO);
		

		assertFalse(codeListService.isGroupedCodeListUnique(model));
	}
	@Test
	public void testForNonUniqueGroupedCodeListExistingMatch() throws Exception {
		String name = "TestCodeList";
		String catId = "1";
		String steward = "TestSteward";
		//
		// editing a code list so that it matches a different existing code list
		//
		ManageCodeListDetailModel model = new ManageCodeListDetailModel();
		model.setName(name);
		model.setCategory(catId);
		model.setSteward(steward);
		ListObject codeList = new ListObject();
		
		ListObject existingCl = new ListObject();
		String id = "id";
		model.setID(id);
		model.setExistingCodeList(true);
		existingCl.setId(id);
		EasyMock.expect(listObjectDAO.find(id)).andReturn(codeList);
		EasyMock.expect(listObjectDAO.getListObject(name, steward, catId, LoggedInUserUtil.getLoggedInUser())).andReturn(existingCl);
		EasyMock.replay(listObjectDAO);
		

		assertTrue(codeListService.isGroupedCodeListUnique(model));
		
	}
	@Test
	public void testForNonUniqueGroupedCodeListNotExisting() {
		String name = "TestCodeList";
		String catId = "1";
		String steward = "TestSteward";
		//
		// editing a code list so that it matches a different existing code list
		//
		ManageCodeListDetailModel model = new ManageCodeListDetailModel();
		model.setName(name);
		model.setCategory(catId);
		model.setSteward(steward);
		
		ListObject existingCl = new ListObject();
		String id = "id";
		model.setExistingCodeList(false);
		existingCl.setId(id);
		EasyMock.expect(listObjectDAO.getListObject(name, steward, catId, LoggedInUserUtil.getLoggedInUser())).andReturn(existingCl);
		EasyMock.replay(listObjectDAO);
		

		assertFalse(codeListService.isGroupedCodeListUnique(model));
	}
	
}
 
