package org.ifmc.mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ForgottenPasswordResult implements IsSerializable {
	public static final int EMAIL_MISMATCH = 1;
	public static final int SECURITY_QUESTIONS_NOT_SET = 2;
	public static final int SECURITY_QUESTION_MISMATCH = 3;
	public static final int SECURITY_QUESTIONS_LOCKED = 4;
	public static final int USER_NOT_FOUND = 5;
	public static final int USER_ALREADY_LOGGED_IN = 6;
	
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
