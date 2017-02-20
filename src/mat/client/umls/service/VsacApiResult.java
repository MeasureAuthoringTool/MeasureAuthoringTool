package mat.client.umls.service;

import java.util.ArrayList;
import java.util.List;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.model.VSACExpansionProfile;
import mat.model.VSACVersion;
import mat.model.cql.CQLQualityDataSetDTO;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The Class VsacApiResult.
 */
public class VsacApiResult implements IsSerializable {
	
	/** The Constant OID_REQUIRED. */
	public  final static int OID_REQUIRED = 2;
	
	/** The Constant UMLS_NOT_LOGGEDIN. */
	public  final static int UMLS_NOT_LOGGEDIN = 1;
	
	/** The Constant VSAC REQUEST TIMED OUT. */
	public  final static int VSAC_REQUEST_TIMEOUT = 3;
	
	/** The failure reason. */
	private int failureReason;
	
	/** The is success. */
	private boolean isSuccess;
	
	private List<String> retrievalFailedOIDs;
	
	/** The QualityDataSetDTO List. */
	private List<QualityDataSetDTO> updatedQualityDataDTOLIst;
	
	/** The CQLQualityDataSetDTO List. */
	private List<CQLQualityDataSetDTO> updatedCQLQualityDataDTOLIst;
	
	/** The vsac response. */
	private List<MatValueSet> vsacResponse;
	
	private List<VSACExpansionProfile> vsacExpProfileResp;
	
	private List<VSACVersion> vsacVersionResp;
	
	/**
	 * Gets the failure reason.
	 * 
	 * @return the failure reason
	 */
	public int getFailureReason() {
		return failureReason;
	}
	
	public List<String> getRetrievalFailedOIDs() {
		return retrievalFailedOIDs;
	}
	
	/**
	 * Gets the vsac response.
	 * 
	 * @return the vsac response
	 */
	public List<MatValueSet> getVsacResponse() {
		return vsacResponse;
	}
	
	/**
	 * Checks if is success.
	 * 
	 * @return true, if is success
	 */
	public boolean isSuccess() {
		return isSuccess;
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
	
	public void setRetrievalFailedOIDs(List<String> retrievalFailedOIDs) {
		this.retrievalFailedOIDs = retrievalFailedOIDs;
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
	 * Sets the vsac response.
	 * 
	 * @param vsacResponse
	 *            the new vsac response
	 */
	public void setVsacResponse(ArrayList<MatValueSet> vsacResponse) {
		this.vsacResponse = vsacResponse;
	}

	/**
	 * @return the updatedQualityDataDTOLIst
	 */
	public List<QualityDataSetDTO> getUpdatedQualityDataDTOLIst() {
		return updatedQualityDataDTOLIst;
	}

	/**
	 * @param updatedQualityDataDTOLIst the updatedQualityDataDTOLIst to set
	 */
	public void setUpdatedQualityDataDTOLIst(List<QualityDataSetDTO> updatedQualityDataDTOLIst) {
		this.updatedQualityDataDTOLIst = updatedQualityDataDTOLIst;
	}

	/**
	 * @return the updatedCQLQualityDataDTOLIst
	 */
	public List<CQLQualityDataSetDTO> getUpdatedCQLQualityDataDTOLIst() {
		return updatedCQLQualityDataDTOLIst;
	}

	/**
	 * @param updatedCQLQualityDataDTOLIst the updatedCQLQualityDataDTOLIst to set
	 */
	public void setUpdatedCQLQualityDataDTOLIst(List<CQLQualityDataSetDTO> updatedCQLQualityDataDTOLIst) {
		this.updatedCQLQualityDataDTOLIst = updatedCQLQualityDataDTOLIst;
	}

	/**
	 * @return the vsacProfileResp
	 */
	public List<VSACExpansionProfile> getVsacExpProfileResp() {
		return vsacExpProfileResp;
	}

	/**
	 * @param vsacProfileResp the vsacProfileResp to set
	 */
	public void setVsacExpProfileResp(List<VSACExpansionProfile> vsacExpProfileResp) {
		this.vsacExpProfileResp = vsacExpProfileResp;
	}

	public List<VSACVersion> getVsacVersionResp() {
		return vsacVersionResp;
	}

	public void setVsacVersionResp(List<VSACVersion> vsacVersionResp) {
		this.vsacVersionResp = vsacVersionResp;
	}
	
}
