package mat.client.umls.service;

import java.util.ArrayList;

import mat.model.MatValueSet;

import com.google.gwt.user.client.rpc.IsSerializable;


public class VsacApiResult implements IsSerializable {
	public  final static int UMLS_NOT_LOGGEDIN = 1;
	public  final static int OID_REQUIRED = 2;

	private int failureReason;
	private boolean isSuccess;
	private ArrayList<MatValueSet> vsacResponse;

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
	public ArrayList<MatValueSet> getVsacResponse() {
		return vsacResponse;
	}
	public void setVsacResponse(ArrayList<MatValueSet> vsacResponse) {
		this.vsacResponse = vsacResponse;
	}

}
