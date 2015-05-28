package mat.client.myAccount;

import mat.model.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class SecurityQuestionsModel.
 */
public class SecurityQuestionsModel implements IsSerializable , BaseModel {
	
	/** The question1. */
	private String question1;
	
	/** The question1 answer. */
	private String question1Answer;
	
	/** The question2. */
	private String question2;
	
	/** The question2 answer. */
	private String question2Answer;
	
	/** The question3. */
	private String question3;
	
	/** The question3 answer. */
	private String question3Answer;
	
	public SecurityQuestionsModel(String question1, String question1Answer, String question2, String question2Answer, String question3, String question3Answer) {
		this.question1 = question1;
		this.question2 = question2;
		this.question3 = question3;
		this.question1Answer = question1Answer;
		this.question2Answer = question2Answer;
		this.question3Answer = question3Answer;
		// TODO Auto-generated constructor stub
	}
	
	public SecurityQuestionsModel() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Gets the question1.
	 * 
	 * @return the question1
	 */
	public String getQuestion1() {
		return question1;
	}
	
	/**
	 * Sets the question1.
	 * 
	 * @param question1
	 *            the new question1
	 */
	public void setQuestion1(String question1) {
		this.question1 = question1;
	}
	
	/**
	 * Gets the question1 answer.
	 * 
	 * @return the question1 answer
	 */
	public String getQuestion1Answer() {
		return question1Answer;
	}
	
	/**
	 * Sets the question1 answer.
	 * 
	 * @param question1Answer
	 *            the new question1 answer
	 */
	public void setQuestion1Answer(String question1Answer) {
		this.question1Answer = question1Answer;
	}
	
	/**
	 * Gets the question2.
	 * 
	 * @return the question2
	 */
	public String getQuestion2() {
		return question2;
	}
	
	/**
	 * Sets the question2.
	 * 
	 * @param question2
	 *            the new question2
	 */
	public void setQuestion2(String question2) {
		this.question2 = question2;
	}
	
	/**
	 * Gets the question2 answer.
	 * 
	 * @return the question2 answer
	 */
	public String getQuestion2Answer() {
		return question2Answer;
	}
	
	/**
	 * Sets the question2 answer.
	 * 
	 * @param question2Answer
	 *            the new question2 answer
	 */
	public void setQuestion2Answer(String question2Answer) {
		this.question2Answer = question2Answer;
	}
	
	/**
	 * Gets the question3.
	 * 
	 * @return the question3
	 */
	public String getQuestion3() {
		return question3;
	}
	
	/**
	 * Sets the question3.
	 * 
	 * @param question3
	 *            the new question3
	 */
	public void setQuestion3(String question3) {
		this.question3 = question3;
	}
	
	/**
	 * Gets the question3 answer.
	 * 
	 * @return the question3 answer
	 */
	public String getQuestion3Answer() {
		return question3Answer;
	}
	
	/**
	 * Sets the question3 answer.
	 * 
	 * @param question3Answer
	 *            the new question3 answer
	 */
	public void setQuestion3Answer(String question3Answer) {
		this.question3Answer = question3Answer;
	}
	
	@Override
	public void scrubForMarkUp() {
		String markupRegExp = "<[^>]+>";
		
		String noMarkupText = this.getQuestion1Answer().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if (this.getQuestion1Answer().trim().length() > noMarkupText.length()) {
			this.setQuestion1Answer(noMarkupText);
		}
		noMarkupText = this.getQuestion2Answer().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if (this.getQuestion2Answer().trim().length() > noMarkupText.length()) {
			this.setQuestion2Answer(noMarkupText);
		}
		noMarkupText = this.getQuestion3Answer().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if (this.getQuestion3Answer().trim().length() > noMarkupText.length()) {
			this.setQuestion3Answer(noMarkupText);
		}
		
		
	}
	
	
}
