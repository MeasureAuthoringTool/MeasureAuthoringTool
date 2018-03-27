package mat.server.service.impl;

import org.junit.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import mat.dao.SpringInitializationTest;
import mat.model.SecurityRole;
import mat.model.User;
import mat.model.UserPassword;
import mat.server.model.MatUserDetails;

public class LoginServiceImplTest extends SpringInitializationTest {
	String username = "firsttimeuser";
	String password     = "test";
	
	@org.junit.Before
	public void setUp() {
		System.out.println("\n\nTest\n\n");
		MatUserDetails userInfo = (MatUserDetails)getUserDetailService().loadUserByUsername(username);
		if(userInfo == null) {
			User user = new User();
			user.setPassword(new UserPassword());
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
			
		}
	}
	
	@Test
	public void testLoadUser(){
	MatUserDetails userInfo = (MatUserDetails)getUserDetailService().loadUserByUsername(username);
	if(userInfo != null){
		System.out.println("User Exists in the database");
	}else{
		throw new UsernameNotFoundException("UserID doesnot exist");}
	}

}
