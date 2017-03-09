package mat.server;

import java.util.Iterator;

import mat.server.model.MatUserDetails;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The Class LoggedInUserUtil.
 */
public class LoggedInUserUtil {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(LoggedInUserUtil.class);
	
	/** The logged in user. */
	private static String loggedInUser;
	
	/** The logged in user email. */
	private static String loggedInUserEmail;
	
	/** The logged in login id. */
	private static String loggedInLoginId;
	
	/** The logged in username */
	private static String loggedInUserName;
	
	/**
	 * Gets the token.
	 * 
	 * @return the token
	 */
	private static UsernamePasswordAuthenticationToken getToken() {
		//re-factored to support Anonymous user US 439
		UsernamePasswordAuthenticationToken token = null;
		if(SecurityContextHolder.getContext() != null) {
			if(SecurityContextHolder.getContext().getAuthentication() != null) {
				try {
					if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
						token = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();						
					}
				}
				catch(Exception exc) {
					logger.info(exc);
				}
			}
		}	
		return token;
	}
	
	/**
	 * Sets the logged in user.
	 * 
	 * @param u
	 *            the new logged in user
	 */
	public static void setLoggedInUser(String u) {
		loggedInUser = u;
	}
	
	/**
	 * Gets the logged in user.
	 * 
	 * @return the logged in user
	 */
	public static String getLoggedInUser() {
		if(loggedInUser != null) {
			return loggedInUser;
		}
		else {
			UsernamePasswordAuthenticationToken token = getToken();
			String userName = null;
			if(token != null) {
				userName = token.getName();
			}
			return userName;
		}
	}
	
	/**
	 * Sets the logged in login id.
	 * 
	 * @param loginid
	 *            the new logged in login id
	 */
	public static void setLoggedInLoginId(String loginid) {
		loggedInLoginId = loginid;
	}
	
	/**
	 * Gets the logged in login id.
	 * 
	 * @return the logged in login id
	 */
	public static String getLoggedInLoginId() {
		if(loggedInLoginId != null) {
			return loggedInLoginId;
		}
		else {
			UsernamePasswordAuthenticationToken token = getToken();
			String loginId = null;
			if(token != null) {
				loginId = ((MatUserDetails) token.getDetails()).getLoginId();
			}
			return loginId;
		}
	}
	
	/**
	 * Sets the logged in user email.
	 * 
	 * @param e
	 *            the new logged in user email
	 */
	public static void setLoggedInUserEmail(String e) {
		loggedInUserEmail = e;
	}
	
	/**
	 * Gets the logged in user email address.
	 * 
	 * @return the logged in user email address
	 */
	public static String getLoggedInUserEmailAddress() {
		if(loggedInUserEmail != null) {
			return loggedInUserEmail;
		}
		UsernamePasswordAuthenticationToken token = getToken();
		String emailAddress = null;
		if(token != null) {
			emailAddress = ((MatUserDetails) token.getDetails()).getEmailAddress();
			
		}
		return emailAddress;
	}
	
	/**
	 * Gets the logged in user role.
	 * 
	 * @return the logged in user role
	 */
	public static String getLoggedInUserRole() {
		String role = null;
		UsernamePasswordAuthenticationToken token = getToken();
		if(token != null) {
			Iterator<GrantedAuthority> iter = token.getAuthorities().iterator();
			if(iter.hasNext()) {
				role = iter.next().getAuthority();
			}
		}
		return role;
	}

	public static void setLoggedInUserName(String e) {
	      loggedInUserName = e;
	}
	
	
	public static String getLoggedUserName() {
		if(loggedInUserName != null) {
			return loggedInUserName;
		}
		else {
			UsernamePasswordAuthenticationToken token = getToken();
			String firstName = null;
			String lastName = null;
			if(token != null) {
				firstName = ((MatUserDetails) token.getDetails()).getUsername();
				lastName = ((MatUserDetails) token.getDetails()).getUserLastName();
			}
			return firstName + " " + lastName;
		}
	}
}
