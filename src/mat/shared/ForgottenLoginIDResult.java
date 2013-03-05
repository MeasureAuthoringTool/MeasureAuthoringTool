package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ForgottenLoginIDResult implements IsSerializable {
	public static final int SECURITY_QUESTIONS_NOT_SET = 1;
	public static final int SECURITY_QUESTION_MISMATCH = 2;
	public static final int SECURITY_QUESTIONS_LOCKED = 3;
	public static final int EMAIL_NOT_FOUND_MSG = 4;
	public static final int USER_ALREADY_LOGGED_IN = 5;
	
	private boolean emailSent;
	private int failureReason;

	public boolean isEmailSent() {
		return emailSent;
	}
	public void setEmailSent(boolean emailSent) {
		this.emailSent = emailSent;
	}
	
	public int getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(int failureReason) {
		this.failureReason = failureReason;
	}

	

}
