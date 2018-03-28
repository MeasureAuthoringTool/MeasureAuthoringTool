package mat.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import mat.client.admin.ManageOrganizationDetailModel;
import mat.client.admin.ManageOrganizationSearchModel;
import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.ManageUsersSearchModel;
import mat.client.admin.service.AdminService;
import mat.client.admin.service.SaveUpdateOrganizationResult;
import mat.client.admin.service.SaveUpdateUserResult;
import mat.dao.OrganizationDAO;
import mat.dao.UserDAO;
import mat.model.Organization;
import mat.model.Status;
import mat.model.User;
import mat.server.model.MatUserDetails;
import mat.server.service.UserService;
import mat.shared.AdminManageOrganizationModelValidator;
import mat.shared.AdminManageUserModelValidator;
import mat.shared.InCorrectUserRoleException;


/**
 * The Class AdminServiceImpl.
 */
@SuppressWarnings("serial")
public class AdminServiceImpl extends SpringRemoteServiceServlet implements AdminService{
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(AdminServiceImpl.class);
	
	/**
	 * Check admin user.
	 *
	 * @throws InCorrectUserRoleException             the in correct user role exception
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
	
	/* (non-Javadoc)
	 * @see mat.client.admin.service.AdminService#deleteOrganization(mat.client.admin.ManageOrganizationSearchModel.Result)
	 */
	@Override
	public void deleteOrganization(ManageOrganizationSearchModel.Result organization) {
		Organization org = getOrganizationDAO().findByOid(organization.getOid());
		getOrganizationDAO().deleteOrganization(org);
	}
	
	
	/** Extract organization model.
	 * 
	 * @param organization the organization
	 * @return the manage organization detail model */
	private ManageOrganizationDetailModel extractOrganizationModel(Organization organization) {
		ManageOrganizationDetailModel model = new ManageOrganizationDetailModel();
		if (organization != null) {
			model.setId(organization.getId());
			model.setOid(organization.getOrganizationOID());
			model.setOrganization(organization.getOrganizationName());
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
		model.setOrganization(user.getOrganizationName());
		model.setOrganizationId(user.getOrganizationId());
		boolean v = isCurrentUserAdminForUser(user);
		model.setCurrentUserCanChangeAccountStatus(v);
		model.setRevokeDate(getUserRevokeDate(user));
		model.setCurrentUserCanUnlock(v);
		model.setPasswordExpirationMsg(getUserPwdCreationMsg(user.getLoginId()));
		model.setLastSuccessFullLoginDateTimeMessage(getUserSuccessfulLogonMsg(user.getSignInDate()));
		return model;
	}
	/**
	 * This method is used to generate message for user last successful 
	 * sign in date time. In case if no sign in date is set for user (new user),
	 * no message is returned.
	 * 
	 * @param Date signInDate - User object signInDate.
	 * 
	 * @return String - Message.
	 * 
	 * **/
	private String getUserSuccessfulLogonMsg(Date signInDate) {
		if(signInDate != null) {
			SimpleDateFormat isoFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			String date = isoFormat.format(signInDate);
			String message ="Last Successful logon : " + date + " CDT";
			return message;
		} else {
			return "";
		}
		
	}
	private String getUserRevokeDate(User user) {
		String revokedDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar calendar = Calendar.getInstance();
		if (user.getStatus().getId().equals("2")) {
			calendar.setTime(user.getTerminationDate());
			revokedDate = "(" + dateFormat.format(calendar.getTime()) + ")";
		} else {
			if (user.getSignInDate() != null) {
				calendar.setTime(user.getSignInDate());
				calendar.add(Calendar.DATE, 180);
				revokedDate = "(" + dateFormat.format(calendar.getTime()) + ")";
			} else {  
				// since user has never signed in yet, put "(Not Activated)"
				revokedDate = "(Not Activated)";
			}
		}
		return revokedDate;
	}
	
	/**
	 * Gets the user pwd creation msg.
	 *
	 * @param userID the user id
	 * @return the user pwd creation msg
	 */
	private String getUserPwdCreationMsg(String userID){
		UserDAO userDAO = (UserDAO) context.getBean("userDAO");
		MatUserDetails userDetails = (MatUserDetails) userDAO.getUser(userID);
		Date creationDate = userDetails.getUserPassword().getCreatedDate();
		boolean tempPwd = userDetails.getUserPassword().isTemporaryPassword();
		boolean initialPwd = userDetails.getUserPassword().isInitial();
		Date currentDate = new Date();
		String passwordExpiryMsg = "";
		
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(creationDate);
		if(tempPwd || initialPwd){
			calendar.add(Calendar.DATE, 4);
		} else {
			calendar.add(Calendar.DATE, 59);
		}
		SimpleDateFormat currentDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		currentDate = getFormattedDate(currentDateFormat, currentDate);
		Date pwdExpiryDate = getFormattedDate(currentDateFormat, calendar.getTime());
		if(currentDate.before(pwdExpiryDate) ||
				currentDate.equals(pwdExpiryDate)) {
			passwordExpiryMsg = "Password Expiration Date: " +
					currentDateFormat.format(calendar.getTime())+" 23:59  ";
		} else {
			passwordExpiryMsg = "Password Expired on "+currentDateFormat.format(calendar.getTime());
		}
		
		return  passwordExpiryMsg;
	}
	
	/**
	 * Gets the formatted date.
	 *
	 * @param currentDate the current date
	 * @return the formatted date
	 */
	private Date getFormattedDate(SimpleDateFormat currentDateFormat,
			Date currentDate){
		
		try {
			currentDate = currentDateFormat.parse(currentDateFormat.format(currentDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currentDate;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.admin.service.AdminService#getAllOrganizations()
	 */
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
	
	/* (non-Javadoc)
	 * @see mat.client.admin.service.AdminService#getOrganization(java.lang.String)
	 */
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
	
	
	/* (non-Javadoc)
	 * @see mat.client.admin.service.AdminService#saveUpdateUser(mat.client.admin.ManageUsersDetailModel)
	 */
	@Override
	public SaveUpdateUserResult saveUpdateUser(ManageUsersDetailModel model) throws InCorrectUserRoleException {
		checkAdminUser();
		model.scrubForMarkUp();
		AdminManageUserModelValidator test = new AdminManageUserModelValidator();
		List<String>  messages = test.isValidUsersDetail(model);
		SaveUpdateUserResult result = new SaveUpdateUserResult();
		if (messages.size() != 0) {
			for (String message: messages) {
				logger.info("Server-Side Validation failed for SaveUpdateUserResult for Login ID: "
						+ model.getLoginId() + " And failure Message is :" + message);
			}
			result.setSuccess(false);
			result.setMessages(messages);
			result.setFailureReason(SaveUpdateUserResult.SERVER_SIDE_VALIDATION);
		} else {
			result = getUserService().saveUpdateUser(model);
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see mat.client.admin.service.AdminService#searchOrganization(java.lang.String)
	 */
	@Override
	public ManageOrganizationSearchModel searchOrganization(String key)	{
		List<Organization> searchResults = getOrganizationDAO().searchOrganization(key);
		UserService userService = getUserService();
		HashMap<String, Organization> usedOrganizationsMap = userService.searchForUsedOrganizations();
		logger.info("Organization search returned " + searchResults.size());
		ManageOrganizationSearchModel model = new ManageOrganizationSearchModel();
		List<ManageOrganizationSearchModel.Result> detailList = new ArrayList<ManageOrganizationSearchModel.Result>();
		for (Organization org : searchResults) {
			ManageOrganizationSearchModel.Result r = new ManageOrganizationSearchModel.Result();
			r.setOrgName(org.getOrganizationName());
			r.setOid(org.getOrganizationOID());
			r.setId(Long.toString(org.getId()));
			r.setUsed(usedOrganizationsMap.get(Long.toString(org.getId()))!=null);
			detailList.add(r);
		}
		model.setData(detailList);
		//model.setStartIndex(startIndex);
		//model.setResultsTotal(getOrganizationDAO().countSearchResults(key));
		logger.info("Searching Organization on " + key);
		return model;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.service.AdminService#searchUsers(java.lang.String)
	 */
	@Override
	public ManageUsersSearchModel searchUsers(String key) throws InCorrectUserRoleException {
		checkAdminUser();
		
		UserService userService = getUserService();
		List<User> searchResults = userService.searchForUsersByName(key);
		logger.info("User search returned " + searchResults.size());
		
		ManageUsersSearchModel model = new  ManageUsersSearchModel();
		List<ManageUsersSearchModel.Result> detailList = new ArrayList<ManageUsersSearchModel.Result>();
		for (User user : searchResults) {
			ManageUsersSearchModel.Result r = new ManageUsersSearchModel.Result();
			r.setFirstName(user.getFirstName());
			r.setLastName(user.getLastName());
			r.setOrgName(user.getOrganizationName());
			r.setUserRole(user.getSecurityRole().getDescription());
			r.setKey(user.getId());
			r.setLoginId(user.getLoginId());
			if (user.getStatus() != null) {
				r.setStatus(user.getStatus().getDescription());
			}
			detailList.add(r);
		}
		model.setData(detailList);
		
		//model.setStartIndex(startIndex);
		//model.setResultsTotal(getUserService().countSearchResults(key));
		//logger.info("Searching users on " + key + " with page size " + pageSize);
		logger.info("Searching users on " + key);
		
		return model;
	}
	
	@Override
	public ManageUsersDetailModel getUserByEmail(String emailId) throws InCorrectUserRoleException {
		checkAdminUser();
		logger.info("Retrieving user " + emailId);
		User user = getUserService().findByEmailID(emailId);
		return extractUserModel(user);
	}
	
	@Override
	public SaveUpdateOrganizationResult saveOrganization(ManageOrganizationDetailModel updatedOrganizationDetailModel) {
		AdminManageOrganizationModelValidator organizationValidator = new AdminManageOrganizationModelValidator();
		SaveUpdateOrganizationResult result = new SaveUpdateOrganizationResult();
		if(organizationValidator.isManageOrganizationDetailModelValid(updatedOrganizationDetailModel)) { 
			if (organizationAlreadyExists(updatedOrganizationDetailModel)) {
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateOrganizationResult.OID_NOT_UNIQUE);
			} else {
				try {
					Organization organization = new Organization();
					organization.setOrganizationName(updatedOrganizationDetailModel.getOrganization());
					organization.setOrganizationOID(updatedOrganizationDetailModel.getOid());
					getOrganizationDAO().saveOrganization(organization);
					result.setSuccess(true);
				} catch (Exception exception) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateOrganizationResult.OID_NOT_UNIQUE);
				}
			}
		} else {
			result.setSuccess(false);
			result.setMessages(organizationValidator.getValidationErrors(updatedOrganizationDetailModel));
			result.setFailureReason(SaveUpdateUserResult.SERVER_SIDE_VALIDATION);
		}
		return result;
	}
	
	@Override
	public SaveUpdateOrganizationResult updateOrganization(Long currentOrganizationId, ManageOrganizationDetailModel updatedOrganizationDetailModel) {
		SaveUpdateOrganizationResult result = new SaveUpdateOrganizationResult();
		AdminManageOrganizationModelValidator organizationValidator = new AdminManageOrganizationModelValidator();
		if(organizationValidator.isManageOrganizationDetailModelValid(updatedOrganizationDetailModel)) { 
			try {
				Organization organization = getOrganizationDAO().find(currentOrganizationId);
				if(organization != null && !organizationAlreadyExists(updatedOrganizationDetailModel) || organizationsHaveTheSameOID(organization.getOrganizationOID(), updatedOrganizationDetailModel)) {
					organization.setOrganizationName(updatedOrganizationDetailModel.getOrganization());
					organization.setOrganizationOID(updatedOrganizationDetailModel.getOid());
					getOrganizationDAO().updateOrganization(organization);
					result.setSuccess(true);
				} else {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateOrganizationResult.OID_NOT_UNIQUE);
				}
			} catch (Exception exception) {
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateOrganizationResult.OID_NOT_UNIQUE);
			}
		} else {
			result.setSuccess(false);
			result.setMessages(organizationValidator.getValidationErrors(updatedOrganizationDetailModel));
			result.setFailureReason(SaveUpdateUserResult.SERVER_SIDE_VALIDATION);
		}
		return result;
	}
	
	private boolean organizationsHaveTheSameOID(String currentOid, ManageOrganizationDetailModel updatedOrganizationDetailModel) {
		boolean result = false;
		if(currentOid != null) {
			result = currentOid.equals(updatedOrganizationDetailModel.getOid());
		}
		return result;
	}
	
	private boolean organizationAlreadyExists(ManageOrganizationDetailModel model) {
		return (getOrganizationDAO().findByOid(model.getOid()) != null);
	}
}
