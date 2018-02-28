package mat.client.admin.service;

import mat.client.admin.ManageOrganizationDetailModel;
import mat.client.admin.ManageOrganizationSearchModel;
import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.ManageUsersSearchModel;
import mat.model.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface AdminServiceAsync.
 */
public interface AdminServiceAsync {
	
	/**
	 * Delete user.
	 * 
	 * @param userid
	 *            the userid
	 * @param callback
	 *            the callback
	 */
	public void deleteUser(String userid, AsyncCallback<Void> callback);
	
	/** Gets the organization.
	 * 
	 * @param key the key
	 * @param callback the callback
	 * @return the organization */
	void getOrganization(String key, AsyncCallback<ManageOrganizationDetailModel> callback);
	
	/**
	 * Gets the user.
	 * 
	 * @param key
	 *            the key
	 * @param callback
	 *            the callback
	 * @return the user
	 */
	public void getUser(String key, AsyncCallback<ManageUsersDetailModel> callback);
	/**
	 * Reset user password.
	 * 
	 * @param userid
	 *            the userid
	 * @param callback
	 *            the callback
	 */
	public void resetUserPassword(String userid, AsyncCallback<Void> callback);
	
	/**
	 * Save update user.
	 * 
	 * @param model
	 *            the model
	 * @param callback
	 *            the callback
	 */
	public void saveUpdateUser(ManageUsersDetailModel model, AsyncCallback<SaveUpdateUserResult> callback);
	
	/** Search organization.
	 * 
	 * @param key the key
	 * @param callback the callback */
	public void searchOrganization(String key, AsyncCallback<ManageOrganizationSearchModel> callback);
	
	/**
	 * Search users.
	 * 
	 * @param key
	 *            the key
	 * @param callback
	 *            the callback
	 */
	public void searchUsers(String key, AsyncCallback<ManageUsersSearchModel> callback);
	
	/** Gets the all organizations.
	 * 
	 * @param callback the callback
	 * @return the all organizations */
	void getAllOrganizations(AsyncCallback<ManageOrganizationSearchModel> callback);
	
	void deleteOrganization(ManageOrganizationSearchModel.Result organization, AsyncCallback<Void> callback);
	
	void getUserByEmail(String emailId, AsyncCallback<ManageUsersDetailModel> callback);

	public void saveOrganization(ManageOrganizationDetailModel updatedOrganizationDetailModel,
			AsyncCallback<SaveUpdateOrganizationResult> asyncCallback);

	public void updateOrganization(ManageOrganizationDetailModel currentOrganizationDetails,
			ManageOrganizationDetailModel updatedOrganizationDetailModel,
			AsyncCallback<SaveUpdateOrganizationResult> asyncCallback);
}
