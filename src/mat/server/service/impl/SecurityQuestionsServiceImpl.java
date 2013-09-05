package mat.server.service.impl;

import java.util.List;

import mat.dao.SecurityQuestionsDAO;
import mat.model.SecurityQuestions;
import mat.server.service.SecurityQuestionsService;

import org.springframework.beans.factory.annotation.Autowired;



public class SecurityQuestionsServiceImpl implements SecurityQuestionsService {

	@Autowired
	private SecurityQuestionsDAO securityQuestionsDAO;
	
	@Override
	public List<SecurityQuestions> getSecurityQuestions() {
		return securityQuestionsDAO.getSecurityQuestions();
	}
	
	@Override
	public SecurityQuestions getSecurityQuestionObj(String question){
		SecurityQuestions secQuesObj = securityQuestionsDAO.getSecurityQuestionObj(question);
		return secQuesObj;
	}
}	