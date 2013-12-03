package mat.client.admin.service;

import java.util.List;

import mat.DTO.OrganizationDTO;
import mat.client.admin.ManageOrganizationDetailModel;
import mat.client.admin.ManageOrganizationSearchModel;
import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.ManageUsersSearchModel;
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
	
	public void searchOrganization(String key, int startIndex, int pageSize, AsyncCallback<ManageOrganizationSearchModel> callback);
	/**
	 * Search users.
	 * 
	 * @param key
	 *            the key
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param callback
	 *            the callback
	 */
	public void searchUsers(String key, int startIndex, int pageSize, AsyncCallback<ManageUsersSearchModel> callback);
	
	void getAllOrganizations(AsyncCallback<List<OrganizationDTO>> callback);
}
