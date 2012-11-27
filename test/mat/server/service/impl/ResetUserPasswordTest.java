package mat.server.service.impl;

import junit.framework.TestCase;

import mat.model.User;
import mat.server.service.UserService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ResetUserPasswordTest extends TestCase {
	
	protected static ApplicationContext ctx;
	static {
		String[] locations = {
				"file:**/applicationContext-service.xml",
				"file:**/applicationContext-mail.xml",
				"file:**/mat-persistence.xml"
		};
		ctx = new FileSystemXmlApplicationContext(locations);
	}
	
	public void testResetPassword() {
		String userid = "Admin";
		String password = "Admin";
		boolean isTemporary = false;
		
		UserService userService = 
			(UserService)ctx.getBean("userService");
		User user = userService.getById(userid);
		if(user != null) {
			userService.setUserPassword(user, password, isTemporary);
			userService.saveExisting(user);
		}
	}
}
