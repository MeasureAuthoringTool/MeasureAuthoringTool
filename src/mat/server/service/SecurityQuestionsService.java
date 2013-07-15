package mat.server.service;
import java.util.List;

import mat.model.SecurityQuestions;



public interface SecurityQuestionsService {

	public List<SecurityQuestions> getSecurityQuestions();
	public SecurityQuestions getSecurityQuestionObj(String question);
}	