package mat.server.hibernate;


import mat.dao.UserDAO;
import mat.server.model.MatUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


public class HibernateUserDetailService implements UserDetailsService {
	
	
	@Autowired
	private UserDAO userDAO;
	
	
	public UserDetails loadUserByUsername(String userId) {
		return userDAO.getUser(userId);
	}
    
   
    public void saveUserDetails(MatUserDetails userdetails){
    	 userDAO.saveUserDetails(userdetails);
    }
    
    

}


