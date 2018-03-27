package mat.server.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import mat.dao.SecurityRoleDAO;
import mat.dao.SpringInitializationTest;
import mat.dao.UserDAO;
import mat.model.SecurityRole;
import mat.model.User;
import mat.server.service.UserService;
import mat.shared.PasswordVerifier;

public class UserServiceImplTest extends SpringInitializationTest {
	
	@Autowired
	protected UserService userService;
	@Autowired
	protected UserDAO userDAO;
	
	@Test
	public void testGenerateRandomPassword() {
		
		for(int i = 0; i < 500; i++) {
			String pwd = userService.generateRandomPassword();
			PasswordVerifier verifier = new PasswordVerifier("test", pwd, pwd);
			assertTrue(pwd, verifier.isValid());
		}
	}
	

	@Test
	public void testGenerateRandomPasswordExplicitTest() {
		String password = userService.generateRandomPassword();
		assertTrue(password.length() >= 8);
		assertTrue(password.length() <= 16);
		assertTrue(!Character.isDigit(password.charAt(0)));
		boolean upperFound = false;
		boolean lowerFound = false;
		boolean numberFound = false;
		boolean specialFound = false;
		for(int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			upperFound |= Character.isUpperCase(c);
			lowerFound |= Character.isLowerCase(c);
			numberFound |= Character.isDigit(c);
			specialFound |= isSpecial(c);
		}
		assertTrue(upperFound);
		assertTrue(lowerFound);
		assertTrue(numberFound);
		assertTrue(specialFound);
	}
	private boolean isSpecial(char c) {
		return c == '%' ||
			c == '#' ||
			c == '*' ||
			c == '+' ||
			c == '-' ||
			c == ',' ||
			c == ':' ||
			c == '=' ||
			c == '?' || 
			c == '_';
	}
	
	@Test
	public void testIsAdminNotInRole() {
		User user1 = new User();
		user1.setId("1");
		user1.setEmailAddress("1");
		User user2 = new User();
		user2.setEmailAddress("2");
		user2.setId("2");
		
		for(SecurityRole role : getService().getSecurityRoleDAO().find()) {
			user1.setSecurityRole(role);
			if(role.getId().equals("1")) {
				assertTrue(userService.isAdminForUser(user1, user2));
			}
			else {
				assertFalse(userService.isAdminForUser(user1, user2));
			}
		}
	}
	
	@Test
	public void testIsAdminNotAdminForSelf() {
		SecurityRoleDAO roleDAO = getService().getSecurityRoleDAO();
		User user1 = new User();
		user1.setEmailAddress("1");
		user1.setId("id1");
		user1.setSecurityRole(roleDAO.find("1"));
		assertFalse(userService.isAdminForUser(user1, user1));
	}


}
