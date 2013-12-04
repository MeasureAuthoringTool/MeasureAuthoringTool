package mat.server;

import java.util.ArrayList;
import java.util.List;

import mat.client.admin.ManageOrganizationDetailModel;
import mat.client.admin.ManageOrganizationSearchModel;
import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.ManageUsersSearchModel;
import mat.client.admin.service.AdminService;
import mat.client.admin.service.SaveUpdateOrganizationResult;
import mat.client.admin.service.SaveUpdateUserResult;
import mat.dao.OrganizationDAO;
import mat.model.Organization;
import mat.model.Status;
import mat.model.User;
import mat.server.service.UserService;
import mat.shared.AdminManageUserModelValidator;
import mat.shared.InCorrectUserRoleException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The Class AdminServiceImpl.
 */
@SuppressWarnings("serial")
public class AdminServiceImpl extends SpringRemoteServiceServlet implements AdminService{
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(AdminServiceImpl.class);
	/** The context. */
	/*
	 * @Autowired private ApplicationContext context;
	 */
	
	/**
	 * Check admin user.
	 * 
	 * @throws InCorrectUserRoleException
	 *             the in correct user role exception
	 */
	private void checkAdminUser() throws InCorrectUserRoleException{
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		logger.info("userRole actual:"+userRole);
		if(!("Administrator".equalsIgnoreCase(userRole))){
			SecurityContextHolder.clearContext();
			throw new mat.shared.InCorrectUserRoleException("Role not authorized to execute this function.");
		}
	}
	/* (non-Javadoc)
	 * @see mat.client.admin.service.AdminService#deleteUser(java.lang.String)
	 */
	@Override
	public void deleteUser(String userId) throws InCorrectUserRoleException  {
		checkAdminUser();
		getUserService().deleteUser(userId);
	}
	
	private ManageOrganizationDetailModel extractOrganizationModel(Organization organization) {
		ManageOrganizationDetailModel model = new ManageOrganizationDetailModel();
		if (organization != null) {
			model.setOid(organization.getOrganizationOID());
			model.setOrganization(organization.getOrganizationName());
			model.setExistingOrg(true);
		}
		return model;
	}
	
	
	/** Extract user model.
	 * 
	 * @param user the user
	 * @return the manage users detail model */
	private ManageUsersDetailModel extractUserModel(User user) {
		ManageUsersDetailModel model = new ManageUsersDetailModel();
		model.setUserID(user.getId());
		model.setFirstName(user.getFirstName());
		model.setLastName(user.getLastName());
		model.setMiddleInitial(user.getMiddleInit());
		model.setTitle(user.getTitle());
		model.setEmailAddress(user.getEmailAddress());
		model.setLoginId(user.getLoginId());
		model.setPhoneNumber(user.getPhoneNumber());
		model.setActive(getIsActive(user.getStatus()));
		model.setExistingUser(true);
		model.setLocked(user.getLockedOutDate() != null);
		model.setRole(user.getSecurityRole().getId());
		model.setOid(user.getOrgOID());
		// model.setRootOid(user.getRootOID());
		model.setOrganization(user.getOrganizationName());
		model.setOrganizationId(user.getOrganizationId());
		boolean v = isCurrentUserAdminForUser(user);
		model.setCurrentUserCanChangeAccountStatus(v);
		model.setCurrentUserCanUnlock(v);
		return model;
	}
	
	/**
	 * Gets the checks if is active.
	 * 
	 * @param status
	 *            the status
	 * @return the checks if is active
	 */
	private boolean getIsActive(Status status) {
		return status.getDescription().equalsIgnoreCase("Active");
	}
	
	@Override
	public ManageOrganizationDetailModel getOrganization(String key) {
		logger.info("Retrieving organization for OID " + key);
		Organization organization = getOrganizationDAO().findByOid(key);
		return extractOrganizationModel(organization);
	}
	
	/**
	 * Gets the measure xml dao.
	 * 
	 * @return the measure xml dao
	 */
	private OrganizationDAO getOrganizationDAO() {
		return ((OrganizationDAO) context.getBean("organizationDAO"));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.service.AdminService#getUser(java.lang.String)
	 */
	@Override
	public ManageUsersDetailModel getUser(String key) throws InCorrectUserRoleException {
		checkAdminUser();
		logger.info("Retrieving user " + key);
		User user = getUserService().getById(key);
		return extractUserModel(user);
	}
	/**
	 * Gets the user service.
	 * 
	 * @return the user service
	 */
	private UserService getUserService() {
		return (UserService)context.getBean("userService");
	}
	
	/**
	 * Checks if is current user admin for user.
	 * 
	 * @param user
	 *            the user
	 * @return true, if is current user admin for user
	 */
	private boolean isCurrentUserAdminForUser(User user) {
		User adminUser = getUserService().getById(LoggedInUserUtil.getLoggedInUser());
		return getUserService().isAdminForUser(adminUser, user);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.service.AdminService#resetUserPassword(java.lang.String)
	 */
	@Override
	public void resetUserPassword(String userid) {
		getUserService().requestResetLockedPassword(userid);
	}
	
	/** @param model */
	@Override
	public SaveUpdateOrganizationResult saveUpdateOrganization(ManageOrganizationDetailModel currentModel,
			ManageOrganizationDetailModel updatedModel) {
		SaveUpdateOrganizationResult saveUpdateOrganizationResult = new SaveUpdateOrganizationResult();
		Organization organization = null;
		if (currentModel.isExistingOrg()) {
			organization = getOrganizationDAO().findByOid(currentModel.getOid());
			organization.setOrganizationName(updatedModel.getOrganization());
			organization.setOrganizationOID(updatedModel.getOid());
		} else {
			organization = new Organization();
			if (getOrganizationDAO().findByOid(updatedModel.getOid()) != null) {
				saveUpdateOrganizationResult.setSuccess(false);
				saveUpdateOrganizationResult.setFailureReason(SaveUpdateOrganizationResult.OID_NOT_UNIQUE);
				return saveUpdateOrganizationResult;
			}
			organization.setOrganizationName(updatedModel.getOrganization());
			organization.setOrganizationOID(updatedModel.getOid());
		}
		try {
			getOrganizationDAO().saveOrganization(organization);
			saveUpdateOrganizationResult.setSuccess(true);
		} catch (Exception exception) {
			saveUpdateOrganizationResult.setSuccess(false);
			saveUpdateOrganizationResult.setFailureReason(SaveUpdateOrganizationResult.OID_NOT_UNIQUE);
		}
		return saveUpdateOrganizationResult;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.service.AdminService#saveUpdateUser(mat.client.admin.ManageUsersDetailModel)
	 */
	@Override
	public SaveUpdateUserResult saveUpdateUser(ManageUsersDetailModel model) throws InCorrectUserRoleException {
		checkAdminUser();
		AdminManageUserModelValidator test = new AdminManageUserModelValidator();
		List<String>  messages= test.isValidUsersDetail(model);
		SaveUpdateUserResult result = new SaveUpdateUserResult();
		if(messages.size()!=0){
			for(String message: messages){
				logger.info("Server-Side Validation failed for SaveUpdateUserResult for Login ID: "+model.getLoginId()+ " And failure Message is :"+ message);
			}
			result.setSuccess(false);
			result.setMessages(messages);
			result.setFailureReason(SaveUpdateUserResult.SERVER_SIDE_VALIDATION);
		}else{
			result = getUserService().saveUpdateUser(model);
		}
		return result;
	}
	@Override
	public ManageOrganizationSearchModel searchOrganization(String key, int startIndex, int pageSize)
	{
		List<Organization> searchResults = getOrganizationDAO().searchOrganization(key, startIndex - 1, pageSize);
		logger.info("Organization search returned " + searchResults.size());
		ManageOrganizationSearchModel model = new ManageOrganizationSearchModel();
		List<ManageOrganizationSearchModel.Result> detailList = new ArrayList<ManageOrganizationSearchModel.Result>();
		for (Organization org : searchResults) {
			ManageOrganizationSearchModel.Result r = new ManageOrganizationSearchModel.Result();
			r.setOrgName(org.getOrganizationName());
			r.setOid(org.getOrganizationOID());
			r.setId(Long.toString(org.getId()));
			detailList.add(r);
		}
		model.setData(detailList);
		model.setStartIndex(startIndex);
		model.setResultsTotal(getOrganizationDAO().countSearchResults(key));
		logger.info("Searching Organization on " + key + " with page size " + pageSize);
		return model;
	}
	/* (non-Javadoc)
	 * @see mat.client.admin.service.AdminService#searchUsers(java.lang.String, int, int)
	 */
	@Override
	public ManageUsersSearchModel searchUsers(String key, int startIndex, int pageSize) throws InCorrectUserRoleException {
		checkAdminUser();
		
		UserService userService = getUserService();
		List<User> searchResults = userService.searchForUsersByName(key, startIndex, pageSize);
		logger.info("User search returned " + searchResults.size());
		
		ManageUsersSearchModel model = new  ManageUsersSearchModel();
		List<ManageUsersSearchModel.Result> detailList = new ArrayList<ManageUsersSearchModel.Result>();
		for(User user : searchResults) {
			ManageUsersSearchModel.Result r = new ManageUsersSearchModel.Result();
			r.setFirstName(user.getFirstName());
			r.setLastName(user.getLastName());
			r.setOrgName(user.getOrganizationName());
			r.setKey(user.getId());
			r.setLoginId(user.getLoginId());
			detailList.add(r);
		}
		model.setData(detailList);
		
		model.setStartIndex(startIndex);
		model.setResultsTotal(getUserService().countSearchResults(key));
		logger.info("Searching users on " + key + " with page size " + pageSize);
		
		return model;
	}
	
	@Override
	public ManageOrganizationSearchModel getAllOrganizations() {
		ManageOrganizationSearchModel model = new ManageOrganizationSearchModel();
		List<ManageOrganizationSearchModel.Result> results = new ArrayList<ManageOrganizationSearchModel.Result>();
		
		List<Organization> organizations = getOrganizationDAO().getAllOrganizations();		
		for (Organization organization : organizations) {
			ManageOrganizationSearchModel.Result result = new ManageOrganizationSearchModel.Result();
			result.setId(String.valueOf(organization.getId()));
			result.setOrgName(organization.getOrganizationName());
			result.setOid(organization.getOrganizationOID());
			results.add(result);
		}
		model.setData(results);		
		logger.info("Getting all organizations.");
		
		return model;
	}
}
