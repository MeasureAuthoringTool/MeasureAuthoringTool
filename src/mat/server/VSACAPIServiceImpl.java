package mat.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import mat.client.umls.service.VSACAPIService;
import mat.client.umls.service.VsacApiResult;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.service.VSACApiService;

/** VSACAPIServiceImpl class. **/
public class VSACAPIServiceImpl extends SpringRemoteServiceServlet implements VSACAPIService {

	private static final long serialVersionUID = 1L;
	
	@Autowired VSACApiService vsacapi;
	
	/**
	 *	Method to invalidate VSAC user session by removing HTTP session Id from UMLSSessionMap.
	 * **/
	@Override
	public final void inValidateVsacUser() {
		String sessionId = getThreadLocalRequest().getSession().getId();
		this.vsacapi.inValidateVsacUser(sessionId);
	}
	
	/**
	 *	Method to check if User already signed in at VSAC.
	 *	@return Boolean.
	 **/
	@Override
	public final boolean isAlreadySignedIn() {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.isAlreadySignedIn(sessionId);
	}
	
	
	/**
	 *Method to authenticate user at VSAC and save eightHourTicket into UMLSSessionMap for valid user.
	 *@param userName - String.
	 *@param password - String.
	 *@return Boolean.
	 * **/
	@Override
	public final boolean validateVsacUser(final String userName, final String password) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.validateVsacUser(userName, password,sessionId);
	}

	@Override
	public final VsacApiResult getMostRecentValueSetByOID(final String oid, String expansionId) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.getMostRecentValueSetByOID(oid, expansionId,sessionId);
	}

	@Override
	public VsacApiResult updateCQLVSACValueSets(List<CQLQualityDataSetDTO> appliedQDMList, String defaultExpId) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.updateCQLVSACValueSets(appliedQDMList, defaultExpId, sessionId);
	}

	@Override
	public VsacApiResult getDirectReferenceCode(String url) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.getDirectReferenceCode(url, sessionId);
	}

	@Override
	public VsacApiResult getVSACProgramsAndReleases() {
		return this.vsacapi.getVSACProgramsAndReleases();
	}
}
