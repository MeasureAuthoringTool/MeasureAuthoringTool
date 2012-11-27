package mat.sprint1Testcase;

import java.util.List;

import junit.framework.AssertionFailedError;

import mat.client.codelist.ManageCodeListDetailModel;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.dao.CodeListDAO;
import mat.dao.ListObjectDAO;
import mat.dao.SpringInitializationTest;
import mat.dao.UserDAO;
import mat.model.ListObject;
import mat.model.SecurityRole;
import mat.model.User;
import mat.server.LoggedInUserUtil;
import mat.server.service.CodeListOidNotUniqueException;
import mat.server.service.impl.ManageCodeListServiceImpl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class ManageCodeListServiceImplOidTest extends SpringInitializationTest {
	
	@Autowired
	protected ManageCodeListServiceImpl codeListService;
	@Autowired
	protected CodeListDAO codeListDAO;
	@Autowired
	protected ListObjectDAO listObjectDAO;
	@Autowired
	protected UserDAO userDAO;
	
	ManageCodeListDetailModel model1 = null;
	ManageCodeListDetailModel model2 = null;
	private TestCaseUtil tcu = new TestCaseUtil();
	
	/**
	 * insert code list
	 * attempt to insert a code list with the same oid
	 * expect an error
	 * 
	 * @throws Exception
	 */
	@Test
	@Rollback(true)
	public void insertTest() throws Exception {
		boolean caught = false;
		model1 = tcu.createManageCodeListDetailModel("testforcodeList1", "1", "1", "1", "1", "191.292.393.494");
		model2 = tcu.createManageCodeListDetailModel("testforcodeList2", "2", "2", "2", "2", "191.292.393.494");
		
		SaveUpdateCodeListResult result = codeListService.saveorUpdateCodeList(model1);
		try{
			result = codeListService.saveorUpdateCodeList(model2);
		}catch(CodeListOidNotUniqueException e){
			caught = true;
		}
		
		teardownInsertTest();

		if(!caught){
			throw new AssertionFailedError("Duplicate LIST_OBJECT.OID values were allowed.");
		}
		
	}

	/**
	 * (1) get any non-admin user from dbase
	 * (2) set logged in user
	 * (3) get unique oid
	 * (4) verify oid not null and not empty
	 * (5) verify oid not equal to any existing ListObject.oid 
	 * (6) verify oid has root of user.rootOid
	 * @throws Exception
	 */
	@Test
	public void generateUniqueOidTest() throws Exception {
		//(1)
		User user = getUser();
		if(user == null)
			throw new AssertionFailedError("Test could not be performed, no user exists in the database");
		//(2)
		LoggedInUserUtil.setLoggedInUser(user.getId());
		//(3)
		String uniqueoid = codeListService.generateUniqueOid(null);
		//(4)
		if(uniqueoid == null || uniqueoid.isEmpty())
			throw new AssertionFailedError("Unique oid generated was null or empty");
		//(5)
		List<ListObject> lobjs = listObjectDAO.find();
		for(ListObject lobj : lobjs)
			if(lobj.getOid().equalsIgnoreCase(uniqueoid)){
				throw new AssertionFailedError("Unique oid generated was not unique");
			}
		//(6)
		if(!uniqueoid.startsWith(user.getRootOID()+"."))
			throw new AssertionFailedError("Unique oid generated does not start with the user oid");
	}
	
	@Test
	public void countListObjectsByOidAndNotIdTest() throws Exception {
		List<ListObject> lobjs = listObjectDAO.find();
		for(ListObject lobj : lobjs) {
			if(listObjectDAO.countListObjectsByOidAndNotId(lobj.getOid(), lobj.getId()) != 0)
				throw new AssertionFailedError("There is more than one ListObject with the same oid.");
		}
		if(lobjs.size() > 1) {
			ListObject first = lobjs.get(0);
			ListObject last = lobjs.get(lobjs.size()-1);
			if(first.getId().equalsIgnoreCase(last.getId()))
				throw new AssertionFailedError("There are two ListObjects with the same id.");

			if(listObjectDAO.countListObjectsByOidAndNotId(first.getOid(), last.getId()) == 0)
				throw new AssertionFailedError("countListObjectsByOidAndNotId failed its contract.");
		}
	}
	
	private User getUser(){
		List<User> users = userDAO.find();
		for(User u : users)
			if(!(u.getSecurityRole().getId().equalsIgnoreCase(SecurityRole.ADMIN_ROLE_ID)))
				return u;
		return null;
	}
	
	private void teardownInsertTest(){
		try{
			if(model1 !=null && model1.getID() != null)
				codeListDAO.delete(model1.getID());
			if(model2 !=null && model2.getID() != null)
				codeListDAO.delete(model2.getID());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
 
