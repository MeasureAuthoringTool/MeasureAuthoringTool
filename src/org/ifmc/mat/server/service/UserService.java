package org.ifmc.mat.server.service;

import java.util.List;

import org.ifmc.mat.client.admin.ManageUsersDetailModel;
import org.ifmc.mat.client.admin.service.SaveUpdateUserResult;
import org.ifmc.mat.client.login.service.SecurityQuestionOptions;
import org.ifmc.mat.model.User;
import org.ifmc.mat.shared.ForgottenPasswordResult;

public interface UserService {
	public List<User> searchForUsersByName(String orgId, int startIndex, int numResults);
	public int countSearchResults(String text);
	
	public User getById(String id);
	public void saveNew(User user) throws UserIDNotUnique;
	public void saveExisting(User user);
    public SaveUpdateUserResult saveUpdateUser(ManageUsersDetailModel model);
	public ForgottenPasswordResult requestForgottenPassword(String email, 
			String securityQuestion, String securityAnswer);
	public void requestResetLockedPassword(String userid);
	
	public void setUserPassword(User user, String clearTextPassword, boolean isTemporary);
	public String getPasswordHash(String userId, String password);
	public boolean isAdminForUser(User admin, User user);
	public String generateRandomPassword();
	public void deleteUser(String userid);
	public SecurityQuestionOptions getSecurityQuestionOptions(String email);
	//US212
	public void setUserSignInDate(String userid);
	public void setUserSignOutDate(String userid);
}
