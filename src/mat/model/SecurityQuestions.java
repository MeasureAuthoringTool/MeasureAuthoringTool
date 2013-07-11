package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;


public class SecurityQuestions  implements IsSerializable {

	private String questionId;
	private String question ;

	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
}
	



