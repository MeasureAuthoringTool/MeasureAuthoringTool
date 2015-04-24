package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The Class SecurityQuestions.
 */
public class SecurityQuestions  implements IsSerializable {

	/** The question id. */
	private String questionId;
	
	/** The question. */
	private String question ;

	/**
	 * Gets the question id.
	 * 
	 * @return the question id
	 */
	public String getQuestionId() {
		return questionId;
	}
	
	/**
	 * Sets the question id.
	 * 
	 * @param questionId
	 *            the new question id
	 */
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
	/**
	 * Gets the question.
	 * 
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	
	/**
	 * Sets the question.
	 * 
	 * @param question
	 *            the new question
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	
}
	



