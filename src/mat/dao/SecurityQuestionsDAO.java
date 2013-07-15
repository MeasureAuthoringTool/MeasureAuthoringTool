package mat.dao;

import java.util.List;

import mat.model.SecurityQuestions;
import mat.model.User;

public interface SecurityQuestionsDAO extends IDAO<SecurityQuestions, String>{
	public List<SecurityQuestions> getSecurityQuestions();
	public SecurityQuestions getSecurityQuestionObj(String question);
}
