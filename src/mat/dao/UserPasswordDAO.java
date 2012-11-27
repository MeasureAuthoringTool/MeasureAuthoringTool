package mat.dao;

import mat.model.UserPassword;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserPasswordDAO extends IDAO<UserPassword, String> {
	public UserPassword getUserPasswordInfo(String userId)throws UsernameNotFoundException;
}
