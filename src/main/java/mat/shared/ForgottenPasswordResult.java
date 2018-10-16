package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class ForgottenPasswordResult.
 */
public class ForgottenPasswordResult implements IsSerializable {
	
	/** The Constant EMAIL_MISMATCH. */
	public static final int EMAIL_MISMATCH = 1;
	
	/** The Constant SECURITY_QUESTIONS_NOT_SET. */
	public static final int SECURITY_QUESTIONS_NOT_SET = 2;
	
	/** The Constant SECURITY_QUESTION_MISMATCH. */
	public static final int SECURITY_QUESTION_MISMATCH = 3;
	
	/** The Constant USER_NOT_FOUND. */
	public static final int USER_NOT_FOUND = 5;
	
	/** The Constant USER_ALREADY_LOGGED_IN. */
	public static final int USER_ALREADY_LOGGED_IN = 6;
		
	/** The email sent. */
	private boolean emailSent;
	
	/** The failure reason. */
	private int failureReason;
	
	/**
	 * Checks if is email sent.
	 * 
	 * @return true, if is email sent
	 */
	public boolean isEmailSent() {
		return emailSent;
	}
	
	/**
	 * Sets the email sent.
	 * 
	 * @param emailSent
	 *            the new email sent
	 */
	public void setEmailSent(boolean emailSent) {
		this.emailSent = emailSent;
	}
	
	/**
	 * Gets the failure reason.
	 * 
	 * @return the failure reason
	 */
	public int getFailureReason() {
		return failureReason;
	}
	
	/**
	 * Sets the failure reason.
	 * 
	 * @param failureReason
	 *            the new failure reason
	 */
	public void setFailureReason(int failureReason) {
		this.failureReason = failureReason;
	}

	

}
