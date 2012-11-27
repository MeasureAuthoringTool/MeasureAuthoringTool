package org.ifmc.mat.server.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.ifmc.mat.client.codelist.ManageCodeListDetailModel;
import org.ifmc.mat.dao.CodeListDAO;
import org.ifmc.mat.dao.ListObjectDAO;
import org.ifmc.mat.model.Category;
import org.ifmc.mat.model.CodeList;
import org.ifmc.mat.model.GroupedCodeList;
import org.ifmc.mat.model.ListObject;
import org.ifmc.mat.server.LoggedInUserUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ManageCodeListServiceImplMockedTest extends BaseServiceMockedTest {
	
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
	public void testForNonUniqueCodeListExistingMatch() throws Exception {
		String name = "TestCodeList";
		String catId = "1";
		String codeSystemId = "2";
		String steward = "TestSteward";
		String stewardOther = null;
		String oid = "3";
		//
		// editing a code list so that it matches a different existing code list
		//
		ManageCodeListDetailModel model = new ManageCodeListDetailModel();
		model.setName(name);
		model.setCategory(catId);
		model.setSteward(steward);
		model.setCodeSystem(codeSystemId);
		CodeList codeList = new CodeList();
		
		ManageCodeListDetailModel model1 = new ManageCodeListDetailModel();
		model1.setName(name);
		model1.setCategory(catId);
		model1.setCodeSystem(codeSystemId);
		model1.setSteward(steward);
		model1.setStewardOther(stewardOther);
		model1.setOid(oid);
		
		CodeList existingCl = new CodeList();
		String id = "id";
		model.setID(id);
		model.setExistingCodeList(true);
		existingCl.setId(id);
		EasyMock.expect(codeListDAO.find(id)).andReturn(codeList);
		EasyMock.expect(codeListDAO.getCodeList(model1, LoggedInUserUtil.getLoggedInUser())).andReturn(existingCl);
		EasyMock.replay(codeListDAO);
		

		assertTrue(codeListService.isCodeListUnique(model));
		
	}
	
	@Test
	public void testForSaveRemovingGroupedCodeListsOfWrongCategory() throws Exception {
		Category c1 = new Category();
		c1.setId("1");
		Category c2 = new Category();
		c2.setId("2");
		
		ListObject listObject = new ListObject();
		listObject.setCategory(c2);
		
		CodeList cl1 = new CodeList();
		cl1.setCategory(c1);
		CodeList cl2 = new CodeList();
		cl2.setCategory(c2);
		
		Set<GroupedCodeList> groupCodeLists = new HashSet<GroupedCodeList>();
		GroupedCodeList gcl1 = new GroupedCodeList();
		gcl1.setCodeList(cl1);
		GroupedCodeList gcl2 = new GroupedCodeList();
		gcl2.setCodeList(cl2);
		groupCodeLists.add(gcl1);
		groupCodeLists.add(gcl2);
		listObject.setCodesLists(groupCodeLists);
		
		codeListService.filterCodeListsForCurrentCategory(listObject);
		assertTrue(listObject.getCodesLists().contains(gcl2));
		assertTrue(!listObject.getCodesLists().contains(gcl1));
		
	}

}
 
