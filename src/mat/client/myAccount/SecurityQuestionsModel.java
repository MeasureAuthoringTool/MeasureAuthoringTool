package mat.client.myAccount;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SecurityQuestionsModel implements IsSerializable {
	private String question1;
	private String question1Answer;
	
	private String question2;
	private String question2Answer;
	
	private String question3;
	private String question3Answer;
	public String getQuestion1() {
		return question1;
	}
	public void setQuestion1(String question1) {
		this.question1 = question1;
	}
	public String getQuestion1Answer() {
		return question1Answer;
	}
	public void setQuestion1Answer(String question1Answer) {
		this.question1Answer = question1Answer;
	}
	public String getQuestion2() {
		return question2;
	}
	public void setQuestion2(String question2) {
		this.question2 = question2;
	}
	public String getQuestion2Answer() {
		return question2Answer;
	}
	public void setQuestion2Answer(String question2Answer) {
		this.question2Answer = question2Answer;
	}
	public String getQuestion3() {
		return question3;
	}
	public void setQuestion3(String question3) {
		this.question3 = question3;
	}
	public String getQuestion3Answer() {
		return question3Answer;
	}
	public void setQuestion3Answer(String question3Answer) {
		this.question3Answer = question3Answer;
	}
	
	
}
