package mat.client.admin.service;

import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.ManageUsersSearchModel;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("admin")
public interface AdminService extends RemoteService {
	public ManageUsersDetailModel getUser(String key);
	public SaveUpdateUserResult saveUpdateUser(ManageUsersDetailModel model);
	
	public ManageUsersSearchModel searchUsers(String key, int startIndex, int pageSize) throws Exception;
	public void deleteUser(String userId);
	public void resetUserPassword(String userid);
}
