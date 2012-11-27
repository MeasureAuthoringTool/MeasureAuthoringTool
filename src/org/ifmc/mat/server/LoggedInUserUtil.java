package org.ifmc.mat.server;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ifmc.mat.server.model.MatUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedInUserUtil {
	private static final Log logger = LogFactory.getLog(LoggedInUserUtil.class);
	private static String loggedInUser;
	private static String loggedInUserEmail;
	
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
	
	public static void setLoggedInUser(String u) {
		loggedInUser = u;
	}
	
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
	public static void setLoggedInUserEmail(String e) {
		loggedInUserEmail = e;
	}
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
}
