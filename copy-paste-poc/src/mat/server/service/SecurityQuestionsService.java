package mat.server.service;
import java.util.List;

import mat.model.SecurityQuestions;



/**
 * The Interface SecurityQuestionsService.
 */
public interface SecurityQuestionsService {

	/**
	 * Gets the security questions.
	 * 
	 * @return the security questions
	 */
	public List<SecurityQuestions> getSecurityQuestions();
	
	/**
	 * Gets the security question obj.
	 * 
	 * @param question
	 *            the question
	 * @return the security question obj
	 */
	public SecurityQuestions getSecurityQuestionObj(String question);
}	