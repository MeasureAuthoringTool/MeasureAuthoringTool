package mat.server;

import mat.client.umls.service.VSACAPIService;
import mat.client.umls.service.VsacApiResult;

// TODO: Auto-generated Javadoc
/** VSACAPIServiceImpl class. **/
@SuppressWarnings("static-access")
public class VSACAPIServiceImpl extends SpringRemoteServiceServlet implements VSACAPIService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * MeasureLibrary Service Object.
	 * @return MeasureLibraryService.
	 * */
	public final VSACApiServImpl getVsacApiService() {
		return (VSACApiServImpl) context.getBean("vsacapi");
	}
	
	
	/**
	 * Method to retrieve value set from VSAC based on OID and version.
	 *
	 * @param oid            - String.
	 * @param version            - String.
	 * @param expansionId the expansion id
	 * @return VsacApiResult - VsacApiResult.
	 * *
	 */
	@Override
	public final VsacApiResult getValueSetByOIDAndVersionOrExpansionId (final String oid, final String version,
			final String expansionId) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.getVsacApiService().getValueSetByOIDAndVersionOrExpansionId(oid, version, expansionId, sessionId);
	}
	/**
	 *Method to invalidate VSAC user session by removing HTTP session Id from UMLSSessionMap.
	 * **/
	@Override
	public final void inValidateVsacUser() {
		String sessionId = getThreadLocalRequest().getSession().getId();
		this.getVsacApiService().inValidateVsacUser(sessionId);
	}
	/**
	 *Method to check if User already signed in at VSAC.
	 *@return Boolean.
	 ***/
	@Override
	public final boolean isAlreadySignedIn() {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.getVsacApiService().isAlreadySignedIn(sessionId);
	}
	
	
	/***
	 * Method to update valueset's without versions from VSAC in Measure XML.
	 * Skip Timing elements, Expired, Birthdate and User defined QDM. Supplemental Data Elements are considered here.
	 *
	 * @param measureId - Selected Measure Id.
	 * @return VsacApiResult - Result.
	 * */
	@Override
	public final VsacApiResult updateAllVSACValueSetsAtPackage(final String measureId) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.getVsacApiService().updateAllVSACValueSetsAtPackage(measureId, sessionId);
	}
	
	
	
	/**
	 * Method to retrive All Profile List from VSAC.
	 *
	 * @return the all profile list
	 */
	@Override
	public final VsacApiResult getAllExpIdentifierList() {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.getVsacApiService().getAllExpIdentifierList(sessionId);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.umls.service.VSACAPIService#getAllVersionListByOID(java.lang.String)
	 */
	@Override
	public final VsacApiResult getAllVersionListByOID(String oid) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.getVsacApiService().getAllVersionListByOID(oid,sessionId);
	}
	
	
	/**
	 * *
	 * Method to update valueset's without versions from VSAC in Measure XML.
	 * Skip supplemental Data Elements and Timing elements, Expired, Birth date and User defined QDM.
	 *
	 * @param measureId            - Selected Measure Id.
	 * @param defaultExpId the default exp id
	 * @return VsacApiResult - Result.
	 */
	@Override
	public final VsacApiResult updateVSACValueSets(final String measureId, String defaultExpId) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.getVsacApiService().updateVSACValueSets(measureId, defaultExpId,sessionId);
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
		return this.getVsacApiService().validateVsacUser(userName, password,sessionId);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.umls.service.VSACAPIService#getMostRecentValueSetByOID(java.lang.String, java.lang.String)
	 */
	@Override
	public final VsacApiResult getMostRecentValueSetByOID(final String oid, String expansionId) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.getVsacApiService().getMostRecentValueSetByOID(oid, expansionId,sessionId);
	}


	@Override
	public VsacApiResult updateCQLVSACValueSets(String measureId, String defaultExpId) {
		String sessionId = getThreadLocalRequest().getSession().getId();
		return this.getVsacApiService().updateCQLVSACValueSets(measureId, defaultExpId, sessionId);
	}
	
	
	
}
