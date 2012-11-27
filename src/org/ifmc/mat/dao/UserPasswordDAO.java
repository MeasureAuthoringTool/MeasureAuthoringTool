package org.ifmc.mat.dao;

import org.ifmc.mat.model.UserPassword;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserPasswordDAO extends IDAO<UserPassword, String> {
	public UserPassword getUserPasswordInfo(String userId)throws UsernameNotFoundException;
}
