package mat.client.umls.service;

import java.util.ArrayList;

import mat.model.MatValueSet;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The Class VsacApiResult.
 */
public class VsacApiResult implements IsSerializable {
	
	/** The Constant UMLS_NOT_LOGGEDIN. */
	public  final static int UMLS_NOT_LOGGEDIN = 1;
	
	/** The Constant OID_REQUIRED. */
	public  final static int OID_REQUIRED = 2;

	/** The failure reason. */
	private int failureReason;
	
	/** The is success. */
	private boolean isSuccess;
	
	/** The vsac response. */
	private ArrayList<MatValueSet> vsacResponse;

	/**
	 * Checks if is success.
	 * 
	 * @return true, if is success
	 */
	public boolean isSuccess() {
		return isSuccess;
	}
	
	/**
	 * Sets the success.
	 * 
	 * @param isSuccess
	 *            the new success
	 */
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
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
	
	/**
	 * Gets the vsac response.
	 * 
	 * @return the vsac response
	 */
	public ArrayList<MatValueSet> getVsacResponse() {
		return vsacResponse;
	}
	
	/**
	 * Sets the vsac response.
	 * 
	 * @param vsacResponse
	 *            the new vsac response
	 */
	public void setVsacResponse(ArrayList<MatValueSet> vsacResponse) {
		this.vsacResponse = vsacResponse;
	}

}
