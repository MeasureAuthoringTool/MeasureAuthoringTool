package mat.client.login;



import com.google.gwt.user.client.rpc.IsSerializable;

import mat.dto.UserPreferenceDTO;
import mat.model.BaseModel;
import mat.model.SecurityRole;

/**
 * The Class LoginModel.
 */
public class LoginModel implements IsSerializable , BaseModel{
	
	/** The first name. */
	private String firstName;
	
	/** The Role. */
	private SecurityRole Role;
	
	/** The user id. */
	private String userId;
	
	/** The login id. */
	private String loginId;
	
	/** The email. */
	private String email;
	
	/** The error message. */
	private String errorMessage;
	
	/** The login failed event. */
	private boolean loginFailedEvent;
	
	/** The initial password. */
	private boolean initialPassword;
	
	/** The temporary password. */
	private boolean temporaryPassword;
	
	/** The password. */
	private String password;
	
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
	
	private UserPreferenceDTO userPreference;
	
	/**
	 * Checks if is initial password.
	 * 
	 * @return true, if is initial password
	 */
	public boolean isInitialPassword() {
		return initialPassword;
	}
	
	/**
	 * Sets the initial password.
	 * 
	 * @param initialPassword
	 *            the new initial password
	 */
	public void setInitialPassword(boolean initialPassword) {
		this.initialPassword = initialPassword;
	}
	
	/**
	 * Gets the role.
	 * 
	 * @return the role
	 */
	public SecurityRole getRole() {
		return Role;
	}
	
	/**
	 * Sets the role.
	 * 
	 * @param role
	 *            the new role
	 */
	public void setRole(SecurityRole role) {
		Role = role;
	}
	
	
	/**
	 * Sets the error message.
	 * 
	 * @param errorMessage
	 *            the new error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	/**
	 * Gets the error message.
	 * 
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	/**
	 * Sets the login failed event.
	 * 
	 * @param loginFailedEvent
	 *            the new login failed event
	 */
	public void setLoginFailedEvent(boolean loginFailedEvent) {
		this.loginFailedEvent = loginFailedEvent;
	}
	
	/**
	 * Checks if is login failed event.
	 * 
	 * @return true, if is login failed event
	 */
	public boolean isLoginFailedEvent() {
		return loginFailedEvent;
	}
	
	/**
	 * Sets the temporary password.
	 * 
	 * @param temporaryPassword
	 *            the new temporary password
	 */
	public void setTemporaryPassword(boolean temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}
	
	/**
	 * Checks if is temporary password.
	 * 
	 * @return true, if is temporary password
	 */
	public boolean isTemporaryPassword() {
		return temporaryPassword;
	}
	
	/**
	 * Sets the user id.
	 * 
	 * @param userId
	 *            the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * Sets the login id.
	 * 
	 * @param loginId
	 *            the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	/**
	 * Gets the login id.
	 * 
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
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
	
	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Gets the email.
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 * 
	 * @param email
	 *            the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
		noMarkupText = this.getEmail().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if (this.getEmail().trim().length() > noMarkupText.length()) {
			this.setEmail(noMarkupText);
		}
		
	}

	public UserPreferenceDTO getUserPreference() {
		return userPreference;
	}

	public void setUserPreference(UserPreferenceDTO userPreference) {
		this.userPreference = userPreference;
	}
	
}
