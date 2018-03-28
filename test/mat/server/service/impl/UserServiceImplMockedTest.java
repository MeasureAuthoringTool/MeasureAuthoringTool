package mat.server.service.impl;

import java.util.Date;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import mat.dao.UserDAO;
import mat.dao.impl.StatusDAO;
import mat.model.Status;
import mat.model.User;
import mat.model.UserPassword;
import mat.server.service.UserService;

public class UserServiceImplMockedTest extends BaseServiceMockedTest {
	
	@Autowired
	protected UserService userService;
	@Autowired
	protected UserDAO userDAO;
	@Autowired
	protected StatusDAO statusDAO;
	
	@After
	public void teardown() {
		EasyMock.reset(userDAO, statusDAO);
	}
	
	@Test
	public void testResetPasswordUnlocksAccount() {
		User user = new User();
		String id = "1";
		UserPassword userPassword = new UserPassword();
		userPassword.setForgotPwdlockCounter(2);
		userPassword.setPasswordlockCounter(3);
		user.setPassword(userPassword);
		user.setEmailAddress(id);
		user.setLockedOutDate(new Date());
		
		EasyMock.expect(userDAO.find(id)).andReturn(user);
		userDAO.save(user);
		EasyMock.replay(userDAO);
		
		userService.requestResetLockedPassword(id);
		assertTrue(userPassword.getForgotPwdlockCounter() == 0);
		assertTrue(userPassword.getPasswordlockCounter() == 0);
		assertTrue("Locked out date is " + user.getLockedOutDate(),
				user.getLockedOutDate() == null);
	}
	

	@Test public void testInitialStatusIsActive() throws Exception {
		Status active = new Status();
		active.setDescription("Active");
		
		User user = new User();
		user.setEmailAddress("test");
		
		EasyMock.expect(statusDAO.find("1")).andReturn(active);
		EasyMock.expect(userDAO.userExists("test")).andReturn(Boolean.FALSE);
		userDAO.save(user);
		EasyMock.replay(statusDAO, userDAO);

		userService.saveNew(user);
		
		assertTrue(user.getStatus().equals(active));
	}
	
	@Test
	public void testForNonUniqueUserid() {
		User user = new User();
		user.setEmailAddress("Test");
		EasyMock.expect(userDAO.userExists("Test")).andReturn(Boolean.TRUE);
		userDAO.save(user);
		EasyMock.replay(userDAO);
		
		userService.saveNew(user);
		fail();
	}

}
