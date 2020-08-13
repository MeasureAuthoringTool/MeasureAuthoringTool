package mat.client.admin.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import mat.client.admin.ManageOrganizationDetailModel;
import mat.client.admin.ManageOrganizationSearchModel;
import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.ManageUsersSearchModel;

import java.util.List;

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
	void deleteUser(String userid, AsyncCallback<Void> callback);
	
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
	void getUser(String key, AsyncCallback<ManageUsersDetailModel> callback);

	void activateUser(String userid, AsyncCallback<Void> callback);
	
	/**
	 * Save update user.
	 * 
	 * @param model
	 *            the model
	 * @param callback
	 *            the callback
	 */
	void saveUpdateUser(ManageUsersDetailModel model, AsyncCallback<SaveUpdateUserResult> callback);
	
	/** Search organization.
	 * 
	 * @param key the key
	 * @param callback the callback */
	void searchOrganization(String key, AsyncCallback<ManageOrganizationSearchModel> callback);
	
	/**
	 * Search users.
	 * 
	 * @param key
	 *            the key
	 * @param callback
	 *            the callback
	 */
	void searchUsers(String key, AsyncCallback<ManageUsersSearchModel> callback);
	
	void searchUsersWithActiveBonnie(String key, AsyncCallback<ManageUsersSearchModel> callback);
	
	/** Gets the all organizations.
	 * 
	 * @param callback the callback
	 * @return the all organizations */
	void getAllOrganizations(AsyncCallback<ManageOrganizationSearchModel> callback);
	
	void deleteOrganization(ManageOrganizationSearchModel.Result organization, AsyncCallback<Void> callback);
	
	void getUserByEmail(String emailId, AsyncCallback<ManageUsersDetailModel> callback);

	void getUsersByHarpId(String harpId, AsyncCallback<List<ManageUsersDetailModel>> callback);

	void saveOrganization(ManageOrganizationDetailModel updatedOrganizationDetailModel,
			AsyncCallback<SaveUpdateOrganizationResult> asyncCallback);

	void updateOrganization(Long currentOrganizationId,
			ManageOrganizationDetailModel updatedOrganizationDetailModel,
			AsyncCallback<SaveUpdateOrganizationResult> asyncCallback);
}
