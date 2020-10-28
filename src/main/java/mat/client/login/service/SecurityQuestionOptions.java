package mat.client.login.service;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.shared.NameValuePair;

import java.util.List;

/**
 * The Class SecurityQuestionOptions.
 */
public class SecurityQuestionOptions implements IsSerializable {
	
	/** The security questions. */
	private List<NameValuePair> securityQuestions;
	
	/** The user found. */
	private boolean userFound;
	
	/**
	 * Gets the security questions.
	 * 
	 * @return the security questions
	 */
	public List<NameValuePair> getSecurityQuestions() {
		return securityQuestions;
	}
	
	/**
	 * Sets the security questions.
	 * 
	 * @param securityQuestions
	 *            the new security questions
	 */
	public void setSecurityQuestions(List<NameValuePair> securityQuestions) {
		this.securityQuestions = securityQuestions;
	}
	
	/**
	 * Checks if is user found.
	 * 
	 * @return true, if is user found
	 */
	public boolean isUserFound() {
		return userFound;
	}
	
	/**
	 * Sets the user found.
	 * 
	 * @param userFound
	 *            the new user found
	 */
	public void setUserFound(boolean userFound) {
		this.userFound = userFound;
	}
	
	
}
