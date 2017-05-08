package mat.server.service.impl;

import java.util.List;

import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.service.SaveUpdateUserResult;
import mat.dao.ListObjectDAO;
import mat.dao.SpringInitializationTest;
import mat.dao.UserDAO;
import mat.model.CodeListSearchDTO;
import mat.model.User;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminServiceImplTest extends SpringInitializationTest {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private ListObjectDAO listObjectDAO;
	
	private static ManageUsersDetailModel userDetails = new ManageUsersDetailModel() ;
	

	public  void  setUp(){
		System.out.println("Setting HardCoded values for User creation.");
		userDetails.setExistingUser(false);//Change this accordingly.
		userDetails.setFirstName("Test User");
		userDetails.setLastName("Test User");
		userDetails.setEmailAddress("test@telligen.org");
		userDetails.setPhoneNumber("515-440-8572");
		userDetails.setOrganization("IFMC");
		userDetails.setOid("1234");
		userDetails.setRole("2");
		userDetails.setActive(false);
	}
	
	
	@Test
	public void testForNewUserCreationAndDefaultCodeList(){
		setUp();
		//This test case will test for new user creation and default codeList creation.
		SaveUpdateUserResult result =  getUserService().saveUpdateUser(userDetails);
		if(result.isSuccess()){
			System.out.println("A new user " + userDetails.getFirstName() + " has been successfully created and default codeList has been created for the new user");
		}
	}

	
	/*@Test
	public void testForDefaultCodeList(){
		User usr = userDAO.findByEmail(userDetails.getEmailAddress());
		List<CodeListSearchDTO> defaultCodeList = listObjectDAO.searchByUser("", usr.getId(), 0, 1, "name", true, true);
		if(defaultCodeList.size() > 0){
			for(CodeListSearchDTO defaultCdlst: defaultCodeList){
				System.out.println("Default CodeList:"+ defaultCdlst.getName());
			}
		}
	}*/
	
	
	@Test
	public void searchForAUser(){
		System.out.println("Searching for the user");
		List<User> searchResults =  getUserService().searchForUsersByName(userDetails.getFirstName());
		
		if(searchResults.size()> 0){
			System.out.println("More than one user exist with the same name"+ userDetails.getFirstName());
			for(User user : searchResults){
				System.out.println("Following are the details of that user"+ user.getLastName() + ","+ user.getOrganizationName()+","
						+ user.getEmailAddress()+"," + user.getPhoneNumber());
			}
		}else if (searchResults.size() == 1){
			User user = searchResults.get(0);
			System.out.println("Only one user exist with the same name"+ userDetails.getFirstName());
			System.out.println("Following are the details of that user"+ user.getLastName() + ","+ user.getOrganizationName()+","
					+ user.getEmailAddress()+"," + user.getPhoneNumber());
		}
		
		tearDown();
	}
	
	
	
	public void tearDown(){
		System.out.println("Testing new user creation is complete... ");
		System.out.println("Deleting the newly Created user: "+ userDetails.getFirstName());
		User usr = userDAO.findByEmail(userDetails.getEmailAddress());
		getUserService().deleteUser(usr.getId());
	}
	
}
