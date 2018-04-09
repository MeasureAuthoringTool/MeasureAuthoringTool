package mat.server.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mat.model.SecurityRole;
import mat.model.Status;
import mat.model.UserPassword;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.gwt.user.client.rpc.IsSerializable;


// TODO: Auto-generated Javadoc
/**
 * The Class MatUserDetails.
 */
public class MatUserDetails  implements IsSerializable, UserDetails {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id. */
    private String id;
    
    /** The email address. */
    private String emailAddress;
    
    /** The user password. */
    private UserPassword userPassword;
    
    /** The username. */
    private String username;
    
    /** The roles. */
    private SecurityRole roles;
    
    /** The status. */
    private Status status;
    
    /** The sign in date. */
    private Timestamp signInDate;
	
	/** The sign out date. */
	private Timestamp signOutDate;
	
	/** The locked out date. */
	private Timestamp lockedOutDate;
    
    /** The termination date. */
    private Timestamp terminationDate;
	
	/** The activation date. */
	private Timestamp activationDate;
    
  	/** The account non expired. */
	  private boolean accountNonExpired;
    
    /** The account non locked. */
    private boolean accountNonLocked;
    
    /** The credentials non expired. */
    private boolean credentialsNonExpired;
    
    /** The enabled. */
    private boolean enabled;
    
    /** The login id. */
    private String loginId;
    
    /** The user last name. */
    private String userLastName;
    
    /**
	 * Instantiates a new mat user details.
	 */
    public MatUserDetails() {
    }


	


	/**
	 * Gets the email address.
	 * 
	 * @return the email address
	 */
	public String getEmailAddress() {
		return emailAddress;
	}





	/**
	 * Sets the email address.
	 * 
	 * @param emailAddress
	 *            the new email address
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}





	
	/**
	 * Gets the roles.
	 * 
	 * @return the roles
	 */
	public SecurityRole getRoles() {
		return roles;
	}


	/**
	 * Sets the roles.
	 * 
	 * @param roles
	 *            the new roles
	 */
	public void setRoles(SecurityRole roles) {
		this.roles = roles;
	}

	 /**
	 * Gets the id.
	 * 
	 * @return the id
	 */
 	public String getId() {
			return id;
	}


	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
			this.id = id;
	}
	
	/**
	 * Gets the user password.
	 * 
	 * @return the user password
	 */
	public UserPassword getUserPassword() {
		return userPassword;
	}





	/**
	 * Sets the user password.
	 * 
	 * @param userPassword
	 *            the new user password
	 */
	public void setUserPassword(UserPassword userPassword) {
		this.userPassword = userPassword;
	}




	  
    /**
	 * Gets the status.
	 * 
	 * @return the status
	 */
    public Status getStatus() {
		return status;
	}





	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	

	
    

    
    

	

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

 
	



/* (non-Javadoc)
 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
 */
public Collection<GrantedAuthority> getAuthorities() {
       List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
       list.add(new GrantedAuthorityImpl(roles.getDescription()));
       return list;
}


	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
	 */
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
	 */
	@Override
	public boolean isAccountNonLocked() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return false;
	}





	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}





	/**
	 * Sets the sign in date.
	 * 
	 * @param signInDate
	 *            the new sign in date
	 */
	public void setSignInDate(Timestamp signInDate) {
		this.signInDate = signInDate;
	}





	/**
	 * Gets the sign in date.
	 * 
	 * @return the sign in date
	 */
	public Timestamp getSignInDate() {
		return signInDate;
	}





	/**
	 * Sets the sign out date.
	 * 
	 * @param signOutDate
	 *            the new sign out date
	 */
	public void setSignOutDate(Timestamp signOutDate) {
		this.signOutDate = signOutDate;
	}





	/**
	 * Gets the sign out date.
	 * 
	 * @return the sign out date
	 */
	public Timestamp getSignOutDate() {
		return signOutDate;
	}





	/**
	 * Sets the locked out date.
	 * 
	 * @param lockedOutDate
	 *            the new locked out date
	 */
	public void setLockedOutDate(Timestamp lockedOutDate) {
		this.lockedOutDate = lockedOutDate;
	}





	/**
	 * Gets the locked out date.
	 * 
	 * @return the locked out date
	 */
	public Timestamp getLockedOutDate() {
		return lockedOutDate;
	}





	/**
	 * Sets the termination date.
	 * 
	 * @param terminationDate
	 *            the terminationDate to set
	 */
	public void setTerminationDate(Timestamp terminationDate) {
		this.terminationDate = terminationDate;
	}





	/**
	 * Gets the termination date.
	 * 
	 * @return the terminationDate
	 */
	public Timestamp getTerminationDate() {
		return terminationDate;
	}





	/**
	 * Sets the activation date.
	 * 
	 * @param activationDate
	 *            the activationDate to set
	 */
	public void setActivationDate(Timestamp activationDate) {
		this.activationDate = activationDate;
	}





	/**
	 * Gets the activation date.
	 * 
	 * @return the activationDate
	 */
	public Timestamp getActivationDate() {
		return activationDate;
	}





	/**
	 * Sets the login id.
	 * 
	 * @param loginId
	 *            the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}





	/**
	 * Gets the login id.
	 * 
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}


	
	/**
	 * Gets the user last name.
	 *
	 * @return the user last name
	 */
	public String getUserLastName() {
		return userLastName;
	}


	/**
	 * Sets the user last name.
	 *
	 * @param userLastName the new user last name
	 */
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}


}


