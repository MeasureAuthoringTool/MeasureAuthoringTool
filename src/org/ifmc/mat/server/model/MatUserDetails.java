package org.ifmc.mat.server.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ifmc.mat.model.SecurityRole;
import org.ifmc.mat.model.Status;
import org.ifmc.mat.model.UserPassword;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.gwt.user.client.rpc.IsSerializable;


public class MatUserDetails  implements IsSerializable, UserDetails {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String emailAddress;
    private UserPassword userPassword;
    private String username;
    private SecurityRole roles;
    private Status status;
    private Timestamp signInDate;
	private Timestamp signOutDate;
	private Timestamp lockedOutDate;
    private Timestamp terminationDate;
	private Timestamp activationDate;
    
  	private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    
    
    public MatUserDetails() {
    }


	


	public String getEmailAddress() {
		return emailAddress;
	}





	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}





	
	public SecurityRole getRoles() {
		return roles;
	}


	public void setRoles(SecurityRole roles) {
		this.roles = roles;
	}

	 public String getId() {
			return id;
	}


	public void setId(String id) {
			this.id = id;
	}
	
	public UserPassword getUserPassword() {
		return userPassword;
	}





	public void setUserPassword(UserPassword userPassword) {
		this.userPassword = userPassword;
	}




	  
    public Status getStatus() {
		return status;
	}





	public void setStatus(Status status) {
		this.status = status;
	}

	

	
    

    
    

	

	public void setUsername(String username) {
		this.username = username;
	}

 
	



public Collection<GrantedAuthority> getAuthorities() {
       List<GrantedAuthority> list = new ArrayList();
       list.add(new GrantedAuthorityImpl(roles.getDescription()));
       return list;
}


	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return false;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}
	
	@Override
	public boolean isEnabled() {
		return false;
	}





	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}





	public void setSignInDate(Timestamp signInDate) {
		this.signInDate = signInDate;
	}





	public Timestamp getSignInDate() {
		return signInDate;
	}





	public void setSignOutDate(Timestamp signOutDate) {
		this.signOutDate = signOutDate;
	}





	public Timestamp getSignOutDate() {
		return signOutDate;
	}





	public void setLockedOutDate(Timestamp lockedOutDate) {
		this.lockedOutDate = lockedOutDate;
	}





	public Timestamp getLockedOutDate() {
		return lockedOutDate;
	}





	/**
	 * @param terminationDate the terminationDate to set
	 */
	public void setTerminationDate(Timestamp terminationDate) {
		this.terminationDate = terminationDate;
	}





	/**
	 * @return the terminationDate
	 */
	public Timestamp getTerminationDate() {
		return terminationDate;
	}





	/**
	 * @param activationDate the activationDate to set
	 */
	public void setActivationDate(Timestamp activationDate) {
		this.activationDate = activationDate;
	}





	/**
	 * @return the activationDate
	 */
	public Timestamp getActivationDate() {
		return activationDate;
	}






}


