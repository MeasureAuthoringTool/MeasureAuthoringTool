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
	
	/** The Constant SECURITY_QUESTIONS_LOCKED. */
	public static final int SECURITY_QUESTIONS_LOCKED = 4;
	
	/** The Constant USER_NOT_FOUND. */
	public static final int USER_NOT_FOUND = 5;
	
	/** The Constant USER_ALREADY_LOGGED_IN. */
	public static final int USER_ALREADY_LOGGED_IN = 6;
	
	/** The Constant SECURITY_QUESTIONS_LOCKED_SECOND_ATTEMPT. */
	public static final int SECURITY_QUESTIONS_LOCKED_SECOND_ATTEMPT = 7;
	
	/** The email sent. */
	private boolean emailSent;
	
	/** The failure reason. */
	private int failureReason;
	
	/** The counter. */
	private int counter;

	/**
	 * Gets the counter.
	 * 
	 * @return the counter
	 */
	public int getCounter() {
		return counter;
	}
	
	/**
	 * Sets the counter.
	 * 
	 * @param counter
	 *            the new counter
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}
	
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
