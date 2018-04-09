package mat.login;

import java.util.Date;
import mat.client.login.LoginModel;
import mat.dao.SpringInitializationTest;
import mat.model.SecurityRole;
import mat.model.User;
import mat.server.model.MatUserDetails;
import mat.server.service.UserIDNotUnique;
import org.junit.Test;

public class LoginTest extends SpringInitializationTest{
	String username = "logintest";
	String initialUsername = "logintest_initial";
	String tempUsername = "logintest_temp";
	String lockedUsername = "logintest_locked";
	String password = "logintest";
	
	private User createUser(String username) throws UserIDNotUnique {
		MatUserDetails userInfo = (MatUserDetails)getUserDetailService().loadUserByUsername(username);
		if(userInfo == null) {
			User user = new User();
			user.setFirstName("First");
			user.setLastName("Time");
			user.setPhoneNumber("123-123-1234");
			//user.setOrgOID("1");
			//user.setOrganizationName("Organization");
			user.setEmailAddress(username);
			SecurityRole sRole = new SecurityRole();
			sRole.setId("1");
			user.setSecurityRole(sRole);
			getUserService().setUserPassword(user, password, false);
			getUserService().saveNew(user);
			getUserService().setUserPassword(user, password, false);
			return user;
		}
		else {
			User user = getUserService().getById(userInfo.getId());
			getUserService().setUserPassword(user, password, false);
			return user;
		}
	}
	
	@Test
	public void initialSetup() {
		try {
			createUser(username);
			User initialUser = createUser(initialUsername);
			initialUser.getPassword().setInitial(true);
			User tempUser = createUser(tempUsername);
			tempUser.getPassword().setTemporaryPassword(true);
			User lockedUser = createUser(lockedUsername);
			lockedUser.setLockedOutDate(new Date());
			lockedUser.getPassword().setPasswordlockCounter(3);
		} catch (UserIDNotUnique e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testForLoginFailed()
	{
		LoginModel loginModel = new LoginModel();
		
		loginModel = getLoginService().isValidUser(username, password + "x","");
		assertTrue("Login Failed", loginModel.isLoginFailedEvent());
		System.out.println("Login Failed");
	}
	
	@Test
	public void testForLoginSuccess()
	{
		LoginModel loginModel = new LoginModel();
		loginModel = getLoginService().isValidUser(username, password,"");
		assertFalse("Login Successful", loginModel.isLoginFailedEvent());
		System.out.println("Login Success");
	}
	
	
	@Test
	public void testForInititalSignin(){
		LoginModel loginModel = new LoginModel();
		loginModel = getLoginService().isValidUser(initialUsername, password,"");
		assertTrue("User is signing in First",loginModel.isInitialPassword());
		System.out.println("User is signing in First");
	}
	
	@Test
	public void testForTempPasswordSignIn(){
		LoginModel loginModel = new LoginModel();
		loginModel = getLoginService().isValidUser(tempUsername, password,"");
		assertTrue("User is signing in with the temporary password",loginModel.isTemporaryPassword());
		System.out.println("User is signing in with the temporary password");
	}
	
	
	@Test
	public void testForLockedUser(){
		LoginModel loginModel = new LoginModel();
		loginModel = getLoginService().isValidUser(lockedUsername, password,"");
		assertTrue("Failed Attempt",loginModel.isLoginFailedEvent());
		System.out.println(loginModel.getErrorMessage());
	}
	
	@Test
	public void testForNormalUserSignin(){
		LoginModel loginModel = new LoginModel();
		loginModel = getLoginService().isValidUser(username, password,"");
		assertFalse("normal user has signed in successfully", loginModel.isLoginFailedEvent());
		System.out.println("User Role is:- "+loginModel.getRole().getDescription());
	}
}
