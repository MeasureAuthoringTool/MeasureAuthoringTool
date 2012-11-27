package org.ifmc.mat.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SuccessFailureHolder implements IsSerializable {
	private boolean success;
	private int failureReason;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(int failureReason) {
		this.failureReason = failureReason;
	}
	
	
}
