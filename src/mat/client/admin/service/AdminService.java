/**
 * All the methods of this interface are to be called by a user with an 'Administrator' role.
 * The actual implementation of this interface on the server side should make sure to 
 * check the session and verify that the current logged in user has an Administrator role.
 * If not, then we throw a InCorrectUserRoleException. 
 */
package mat.client.admin.service;

import mat.client.admin.ManageUsersDetailModel;
import mat.client.admin.ManageUsersSearchModel;
import mat.shared.InCorrectUserRoleException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("admin")
public interface AdminService extends RemoteService {
	public ManageUsersDetailModel getUser(String key) throws InCorrectUserRoleException;
	public SaveUpdateUserResult saveUpdateUser(ManageUsersDetailModel model) throws InCorrectUserRoleException;
	
	public ManageUsersSearchModel searchUsers(String key, int startIndex, int pageSize) throws InCorrectUserRoleException;
	public void deleteUser(String userId) throws InCorrectUserRoleException;
	public void resetUserPassword(String userid) throws InCorrectUserRoleException;
}
