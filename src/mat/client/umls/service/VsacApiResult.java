package mat.client.umls.service;

import java.util.ArrayList;

import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.rpc.IsSerializable;


public class VsacApiResult implements IsSerializable {
	public static final int UMLS_NOT_LOGGEDIN = 1;
	public static final int OID_REQUIRED = 2;
	
	private int failureReason;
	private boolean isSuccess;
	
	private ArrayList<MatValueSet> vsacResponse;
	private ArrayList<QualityDataSetDTO> appliedQDMList;
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
	public ArrayList<QualityDataSetDTO> getAppliedQDMList() {
		return appliedQDMList;
	}
	public void setAppliedQDMList(ArrayList<QualityDataSetDTO> appliedQDMList) {
		this.appliedQDMList = appliedQDMList;
	}
	

}
