package mat.client.login;



import mat.model.SecurityRole;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LoginModel implements IsSerializable{

	private SecurityRole Role;
	
	private String userId;

	private String email;
	
	private String errorMessage;
	
	private boolean loginFailedEvent;
	
	private boolean initialPassword; 
	
	private boolean temporaryPassword; 

	private String password;
	
	private String question1;
	private String question1Answer;
	
	private String question2;
	private String question2Answer;
	
	private String question3;
	private String question3Answer;
	
	public boolean isInitialPassword() {
		return initialPassword;
	}

	public void setInitialPassword(boolean initialPassword) {
		this.initialPassword = initialPassword;
	}

	public SecurityRole getRole() {
		return Role;
	}

	public void setRole(SecurityRole role) {
		Role = role;
	}
	
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setLoginFailedEvent(boolean loginFailedEvent) {
		this.loginFailedEvent = loginFailedEvent;
	}

	public boolean isLoginFailedEvent() {
		return loginFailedEvent;
	}

	public void setTemporaryPassword(boolean temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}

	public boolean isTemporaryPassword() {
		return temporaryPassword;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}
	
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
	public void setPassword(String password) {
		this.password = password;
	}	
	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
