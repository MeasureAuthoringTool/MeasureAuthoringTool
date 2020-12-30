package mat.server;

import mat.client.umls.service.VSACAPIService;
import mat.client.umls.service.VsacApiResult;
import mat.client.umls.service.VsacTicketInformation;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.service.VSACApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/** VSACAPIServiceImpl class. **/
@Configurable
@Service
public class VSACAPIServiceImpl extends SpringRemoteServiceServlet implements VSACAPIService {

	private static final long serialVersionUID = 1L;
	
	@Autowired VSACApiService vsacapi;

	@Override
	public String getSessionId() {
		HttpServletRequest thread = getThreadLocalRequest();
		return thread.getSession().getId();
	}
	
	/**
	 *	Method to invalidate VSAC user session by removing HTTP session Id from UMLSSessionMap.
	 * **/
	@Override
	public final void inValidateVsacUser() {
	    try {
            String sessionId = getThreadLocalRequest().getSession().getId();
            this.vsacapi.inValidateVsacUser(sessionId);
        } catch (Exception e) {
	        log("VSACAPIServiceImpl::inValidateVsacUser " + e.getMessage(), e);
	        throw e;
        }
	}
	
	/**
	 *	Method to check if User already signed in at VSAC.
	 *	@return Boolean.
	 **/
	@Override
	public final boolean isAlreadySignedIn() {
		HttpServletRequest thread = getThreadLocalRequest();
		String sessionId = thread.getSession().getId();
		return this.vsacapi.isAlreadySignedIn(sessionId);
	}
	
	@Override
	public final VsacTicketInformation getTicketGrantingToken() {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.getTicketGrantingTicket(sessionId);
	}
	
	
	/**
	 *Method to authenticate user at VSAC and save eightHourTicket into UMLSSessionMap for valid user.
	 *@param userName - String.
	 *@param password - String.
	 *@return Boolean.
	 * **/
	@Override
	public final boolean validateVsacUser(final String apiKey) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.validateVsacUser(apiKey,sessionId);
	}

	@Override
	public final VsacApiResult getMostRecentValueSetByOID(final String oid, final String release, String expansionId) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.vsacapi.getMostRecentValueSetByOID(oid, release, expansionId, sessionId);
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
	public VsacApiResult getVSACProgramsReleasesAndProfiles() {
		return this.vsacapi.getVSACProgramsReleasesAndProfiles();
	}
}