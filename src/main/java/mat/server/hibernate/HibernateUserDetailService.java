package mat.server.hibernate;


import mat.dao.UserDAO;
import mat.server.model.MatUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * The Class HibernateUserDetailService.
 */
public class HibernateUserDetailService implements UserDetailsService {
	
	
	/** The user dao. */
	@Autowired
	private UserDAO userDAO;
	
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String loginId) {
		return userDAO.getUser(loginId);
	}

	public UserDetails loadUserByHarpId(String harpId) {
		return userDAO.getUserDetailsByHarpId(harpId);
	}
    
   
    /**
	 * Save user details.
	 * 
	 * @param userdetails
	 *            the userdetails
	 */
    public void saveUserDetails(MatUserDetails userdetails){
    	 userDAO.saveUserDetails(userdetails);
    }
    
    

}


