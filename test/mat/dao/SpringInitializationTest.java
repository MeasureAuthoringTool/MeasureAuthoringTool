package mat.dao;



import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.TestCase;
import mat.dao.service.DAOService;
import mat.model.User;
import mat.server.LoggedInUserUtil;
import mat.server.hibernate.HibernateUserDetailService;
import mat.server.service.CodeListService;
import mat.server.service.LoginCredentialService;
import mat.server.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
//ApplicationContext will be loaded from "/applicationContext.xml" and "/applicationContext-test.xml"
//in the root of the classpath
@ContextConfiguration({"file:**/applicationContext-mail.xml", "file:**/war/WEB-INF/mat-persistence.xml",
	"file:**/applicationContext-service.xml"})
@Rollback(false)
@Transactional(transactionManager="txManager")
public abstract class SpringInitializationTest extends TestCase {
	
	@Autowired
	protected ApplicationContext applicationContext;

	protected String[] getConfigLocations() {
		return new String[] {
				"file:**/applicationContext*.xml",
				"file:**/mat-persistence.xml"
		};
	}
	
	@Before
	public void setLoggedInUser() {
		User user = getUserService().searchForUsersByName("").get(0);
		LoggedInUserUtil.setLoggedInUser(user.getId());
		LoggedInUserUtil.setLoggedInUserEmail(user.getEmailAddress());
	}
	public DAOService getService() {
		return (DAOService)applicationContext.getBean("daoService");
	}
	
	public HibernateUserDetailService getUserDetailService(){
	   return (HibernateUserDetailService)applicationContext.getBean("hibernateuserService");
	}

	public UserService getUserService(){
		return (UserService) applicationContext.getBean("userService");
	}
	public LoginCredentialService getLoginService(){
		return (LoginCredentialService) applicationContext.getBean("loginService");
	}
	
	public CodeListService getCodeListService(){
		return (CodeListService) applicationContext.getBean("codeListService");
	}
	
	
}
