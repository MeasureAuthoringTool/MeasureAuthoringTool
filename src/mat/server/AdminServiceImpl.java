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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("serial")
public class AdminServiceImpl extends SpringRemoteServiceServlet implements AdminService{
	private static final Log logger = LogFactory.getLog(AdminServiceImpl.class);
	

	@Override
	public ManageUsersDetailModel getUser(String key) {
		logger.info("Retrieving user " + key);
		User user= getUserService().getById(key);
		return extractUserModel(user);
	}

	@Override
	public SaveUpdateUserResult saveUpdateUser(ManageUsersDetailModel model) {
		SaveUpdateUserResult result = getUserService().saveUpdateUser(model);
		return result;
	}



	private boolean getIsActive(Status status) {
		return status.getDescription().equalsIgnoreCase("Active");
	}
	
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
		model.setRootOid(user.getRootOID());
		model.setOrganization(user.getOrganizationName());
		boolean v = isCurrentUserAdminForUser(user);
		model.setCurrentUserCanChangeAccountStatus(v);
		model.setCurrentUserCanUnlock(v);
		return model;
	}
	private boolean isCurrentUserAdminForUser(User user) {
		User adminUser = getUserService().getById(LoggedInUserUtil.getLoggedInUser());
		return getUserService().isAdminForUser(adminUser, user);
	}
	
	
	
	@Override
	public ManageUsersSearchModel searchUsers(String key, int startIndex, int pageSize) throws Exception {
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		logger.info("userRole actual:"+userRole);
		if(!("Adminstrator".equalsIgnoreCase(userRole))){
			logger.info("Non Administrator user tried to access Administrator data");
			throw new mat.shared.InCorrectUserRoleException("Non Administrator user tried to access Administrator data.");
			//return null;
		}
		
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
	
	public void resetUserPassword(String userid) {
		getUserService().requestResetLockedPassword(userid);
	}

	@Override
	public void deleteUser(String userId) {
		getUserService().deleteUser(userId);
	}

	private UserService getUserService() {
		return (UserService)context.getBean("userService");
	}
	
}
