package mat.client.umls.service;

import mat.model.MatValueSet;

import com.google.gwt.user.client.rpc.IsSerializable;


public class VsacApiResult implements IsSerializable {
	public static final int UMLS_NOT_LOGGEDIN = 1;
	public static final int OID_REQUIRED = 2;
	
	private int failureReason;
	private boolean isSuccess;
	
	private MatValueSet vsacResponse;
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public int getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(int failureReason) {
		this.failureReason = failureReason;
	}
	
	public MatValueSet getVsacResponse() {
		return vsacResponse;
	}
	public void setVsacResponse(MatValueSet vsacResponse) {
		this.vsacResponse = vsacResponse;
	}

	

}
