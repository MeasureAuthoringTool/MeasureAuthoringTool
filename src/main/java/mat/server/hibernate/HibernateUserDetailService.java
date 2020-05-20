package mat.server.hibernate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import mat.dao.UserDAO;
import mat.server.model.MatUserDetails;


/**
 * The Class HibernateUserDetailService.
 */
@Service
public class HibernateUserDetailService implements UserDetailsService {
	
	/** The user dao. */
	@Autowired
	private UserDAO userDAO;

	public MatUserDetails loadUserByUsername(String loginId) {
		return userDAO.getUserDetailsByLoginId(loginId);
	}

	public MatUserDetails loadUserById(String id) {
		return userDAO.getUserDetailsById(id);
	}

	public MatUserDetails loadUserByHarpId(String harpId) {
		return userDAO.getUserDetailsByHarpId(harpId);
	}
    
    public void saveUserDetails(MatUserDetails userdetails){
    	 userDAO.saveUserDetails(userdetails);
    }

}


