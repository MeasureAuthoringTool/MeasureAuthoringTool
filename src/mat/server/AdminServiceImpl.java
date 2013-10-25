package mat.server;

import java.util.ArrayList;
import java.util.List;

import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.ManageUsersSearchModel;
import mat.client.admin.service.AdminService;
import mat.client.admin.service.SaveUpdateUserResult;
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
	

	/* (non-Javadoc)
	 * @see mat.client.admin.service.AdminService#getUser(java.lang.String)
	 */
	@Override
	public ManageUsersDetailModel getUser(String key) throws InCorrectUserRoleException {
		checkAdminUser();
		logger.info("Retrieving user " + key);
		User user= getUserService().getById(key);
		return extractUserModel(user);
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
	
	/**
	 * Extract user model.
	 * 
	 * @param user
	 *            the user
	 * @return the manage users detail model
	 */
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
		//model.setRootOid(user.getRootOID());
		model.setOrganization(user.getOrganizationName());
		boolean v = isCurrentUserAdminForUser(user);
		model.setCurrentUserCanChangeAccountStatus(v);
		model.setCurrentUserCanUnlock(v);
		return model;
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
	
	/* (non-Javadoc)
	 * @see mat.client.admin.service.AdminService#resetUserPassword(java.lang.String)
	 */
	public void resetUserPassword(String userid) {
		getUserService().requestResetLockedPassword(userid);
	}

	/* (non-Javadoc)
	 * @see mat.client.admin.service.AdminService#deleteUser(java.lang.String)
	 */
	@Override
	public void deleteUser(String userId) throws InCorrectUserRoleException  {
		checkAdminUser();
		getUserService().deleteUser(userId);
	}
	
	/**
	 * Gets the user service.
	 * 
	 * @return the user service
	 */
	private UserService getUserService() {
		return (UserService)context.getBean("userService");
	}
	
}
