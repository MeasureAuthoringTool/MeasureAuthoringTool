package mat.server.service.impl;

import java.util.List;

import mat.dao.SecurityQuestionsDAO;
import mat.model.SecurityQuestions;
import mat.server.service.SecurityQuestionsService;

import org.springframework.beans.factory.annotation.Autowired;



/**
 * The Class SecurityQuestionsServiceImpl.
 */
public class SecurityQuestionsServiceImpl implements SecurityQuestionsService {

	/** The security questions dao. */
	@Autowired
	private SecurityQuestionsDAO securityQuestionsDAO;
	
	/* (non-Javadoc)
	 * @see mat.server.service.SecurityQuestionsService#getSecurityQuestions()
	 */
	@Override
	public List<SecurityQuestions> getSecurityQuestions() {
		return securityQuestionsDAO.getSecurityQuestions();
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.SecurityQuestionsService#getSecurityQuestionObj(java.lang.String)
	 */
	@Override
	public SecurityQuestions getSecurityQuestionObj(String question){
		SecurityQuestions secQuesObj = securityQuestionsDAO.getSecurityQuestionObj(question);
		return secQuesObj;
	}
}	