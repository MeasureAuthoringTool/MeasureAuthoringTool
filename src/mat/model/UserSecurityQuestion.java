package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;



public class UserSecurityQuestion  implements IsSerializable {

	private String securityQuestion;
	private String securityAnswer;
	
	public String getSecurityQuestion() {
		return securityQuestion;
	}
	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
	


}
