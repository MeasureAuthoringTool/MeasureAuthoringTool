package mat.client.admin.service;

import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.ManageUsersSearchModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface AdminServiceAsync.
 */
public interface AdminServiceAsync {
	
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
	 * Save update user.
	 * 
	 * @param model
	 *            the model
	 * @param callback
	 *            the callback
	 */
	public void saveUpdateUser(ManageUsersDetailModel model, AsyncCallback<SaveUpdateUserResult> callback);

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
	 * Delete user.
	 * 
	 * @param userid
	 *            the userid
	 * @param callback
	 *            the callback
	 */
	public void deleteUser(String userid, AsyncCallback<Void> callback);
}
