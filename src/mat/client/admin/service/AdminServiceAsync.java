package mat.client.admin.service;

import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.ManageUsersSearchModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminServiceAsync {
	public void getUser(String key, AsyncCallback<ManageUsersDetailModel> callback);
	public void saveUpdateUser(ManageUsersDetailModel model, AsyncCallback<SaveUpdateUserResult> callback);

	public void searchUsers(String key, int startIndex, int pageSize, AsyncCallback<ManageUsersSearchModel> callback);

	public void resetUserPassword(String userid, AsyncCallback<Void> callback);
	public void deleteUser(String userid, AsyncCallback<Void> callback);
}
