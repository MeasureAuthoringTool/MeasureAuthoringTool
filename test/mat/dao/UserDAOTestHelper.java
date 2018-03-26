package mat.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mat.model.AuditLog;
import mat.model.SecurityRole;
import mat.model.Status;
import mat.model.User;
import mat.model.UserPassword;
import mat.model.UserSecurityQuestion;

public class UserDAOTestHelper{

	public User getUserForTestUserDAO(Status s, SecurityRole sr) {
		AuditLog log = createAuditLog();
		User user = new User();
		user.setActivationDate(new Date());
		user.setAuditLog(log);
		user.setEmailAddress("test email");
		user.setFirstName("Admin");
		user.setLastName("user");
		user.setPhoneNumber("phone no");
		//user.setOrganizationName("organizationName");
		//user.setOrgOID("org OID");
		user.setStatus(s);
		user.setSecurityRole(sr);
		user.setSignInDate(new Date());		
		
		UserPassword password = new UserPassword();
		password.setCreatedDate(new Date());
		password.setPassword("a04d6d2c52daf0cd14af246a57562ac5");
		password.setInitial(true);
		user.setPassword(password);
		
		UserSecurityQuestion secQues = new UserSecurityQuestion();
		//secQues.getSecurityQuestions().setQuestion("Test Question");
		secQues.setSecurityAnswer("test answer");
		List<UserSecurityQuestion> securityQuestions = new ArrayList<UserSecurityQuestion>();
		securityQuestions.add(secQues);
		user.setUserSecurityQuestions(securityQuestions);
		return user;
	}
	
	private AuditLog createAuditLog() {
		AuditLog log = new AuditLog();
		log.setCreateDate(new Date());
		log.setCreatedBy("Admin");
		return log;
	}

}
