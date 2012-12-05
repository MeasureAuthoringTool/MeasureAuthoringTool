package mat.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import mat.client.myAccount.MyAccountModel;
import mat.client.myAccount.SecurityQuestionsModel;
import mat.client.myAccount.service.MyAccountService;
import mat.model.User;
import mat.model.UserSecurityQuestion;
import mat.server.service.UserService;
import mat.server.util.dictionary.CheckDictionaryWordInPassword;
import mat.shared.MyAccountModelValidator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MyAccountServiceImpl extends SpringRemoteServiceServlet implements
		MyAccountService {
	private static final Log logger = LogFactory.getLog(MyAccountServiceImpl.class);
	
	
	private MyAccountModel extractModel(User user) {
		MyAccountModel model = new MyAccountModel();
		model.setFirstName(user.getFirstName());
		model.setMiddleInitial(user.getMiddleInit());
		model.setLastName(user.getLastName());
		model.setEmailAddress(user.getEmailAddress());
		model.setPhoneNumber(user.getPhoneNumber());
		model.setTitle(user.getTitle());
		model.setOrganisation(user.getOrganizationName());
		model.setOid(user.getOrgOID());
		model.setRootoid(user.getRootOID());
		return model;
	}
	private void setModelFieldsOnUser(User user, MyAccountModel model) {
		user.setFirstName(model.getFirstName());
		user.setMiddleInit(model.getMiddleInitial());
		user.setLastName(model.getLastName());
		user.setEmailAddress(model.getEmailAddress());
		user.setPhoneNumber(model.getPhoneNumber());
		user.setTitle(model.getTitle());
		user.setOrganizationName(model.getOrganisation());
		user.setOrgOID(model.getOid());
		user.setRootOID(model.getRootoid());
		
	}
	private UserService getUserService() {
		return (UserService)context.getBean("userService");
	}
	
	public MyAccountModel getMyAccount() throws IllegalArgumentException {
		UserService userService = getUserService();
		User user = userService.getById(LoggedInUserUtil.getLoggedInUser());
		return extractModel(user);
	}
	
	public void saveMyAccount(MyAccountModel model) {		
		MyAccountModelValidator validator = new MyAccountModelValidator();
		List<String> messages = validator.validate(model);
		if(messages.size()!=0){
			for(String message: messages)
				System.out.println("Server-Side Validation for saveMyAccount"+ message);
		}else{
			//If there is no validation error messages then proceed to save.
			//TODO Add database constraint for OID to be non-nullable
			UserService userService = getUserService();
			User user = userService.getById(LoggedInUserUtil.getLoggedInUser());
			setModelFieldsOnUser(user, model);
			userService.saveExisting(user);
		}
	}
	
	public SecurityQuestionsModel getSecurityQuestions() {
		UserService userService = getUserService();
		User user = userService.getById(LoggedInUserUtil.getLoggedInUser());
		List<UserSecurityQuestion> secQuestions = user.getSecurityQuestions();
		
		SecurityQuestionsModel model = new SecurityQuestionsModel();
		if(secQuestions.size() > 0) {
			model.setQuestion1(secQuestions.get(0).getSecurityQuestion());
			model.setQuestion1Answer(secQuestions.get(0).getSecurityAnswer());
		}
		if(secQuestions.size() > 1) {
			model.setQuestion2(secQuestions.get(1).getSecurityQuestion());
			model.setQuestion2Answer(secQuestions.get(1).getSecurityAnswer());
		}
		if(secQuestions.size() > 2) {
			model.setQuestion3(secQuestions.get(2).getSecurityQuestion());
			model.setQuestion3Answer(secQuestions.get(2).getSecurityAnswer());
		}

		return model;
	}
	
	public void saveSecurityQuestions(SecurityQuestionsModel model) {
		logger.info("Saving security questions");
		UserService userService = getUserService();
		User user = userService.getById(LoggedInUserUtil.getLoggedInUser());
		List<UserSecurityQuestion> secQuestions = user.getSecurityQuestions();
		while(secQuestions.size() < 3) {
			UserSecurityQuestion newQuestion = new UserSecurityQuestion();
			secQuestions.add(newQuestion);
		}
		
		user.setSecurityQuestions(secQuestions);
		
		secQuestions.get(0).setSecurityQuestion(model.getQuestion1());
		secQuestions.get(0).setSecurityAnswer(model.getQuestion1Answer());

		secQuestions.get(1).setSecurityQuestion(model.getQuestion2());
		secQuestions.get(1).setSecurityAnswer(model.getQuestion2Answer());

		secQuestions.get(2).setSecurityQuestion(model.getQuestion3());
		secQuestions.get(2).setSecurityAnswer(model.getQuestion3Answer());
		
		userService.saveExisting(user);
	}
	
	public boolean changePassword(String password) {
		logger.info("Changing password to " + password);
		boolean result = false;
		
		try {
			result =  CheckDictionaryWordInPassword.containsDictionaryWords(password);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(result){
		
			UserService userService = getUserService();
			User user = userService.getById(LoggedInUserUtil.getLoggedInUser());

			userService.setUserPassword(user, password, false);
			userService.saveExisting(user);
		}
		return result;
	}
	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
	//US212
	@Override
	public void setUserSignInDate(String userid){
		UserService userService = getUserService();
		userService.setUserSignInDate(userid);
	}
	@Override
	public void setUserSignOutDate(String userid) {
		UserService userService = getUserService();
		userService.setUserSignOutDate(userid);
	}
}
