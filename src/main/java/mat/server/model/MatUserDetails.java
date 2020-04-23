package mat.server.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.SecurityRole;
import mat.model.Status;
import mat.model.UserPassword;
import mat.model.UserPreference;

@Entity
@Table(name = "USER", uniqueConstraints = @UniqueConstraint(columnNames = "LOGIN_ID"))
public class MatUserDetails  implements IsSerializable, UserDetails {
    
	private static final long serialVersionUID = -6183578219682706613L;

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
    
    private String loginId;
    
    private String userLastName;
    
    private String sessionId;
    
    private UserPreference userPreference;
    
    public MatUserDetails() {
    	
    }

	@Column(name = "SESSION_ID", length = 64)
	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	@Column(name = "EMAIL_ADDRESS", nullable = false, length = 254)
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SECURITY_ROLE_ID", nullable = false)
	public SecurityRole getRoles() {
		return roles;
	}

	public void setRoles(SecurityRole roles) {
		this.roles = roles;
	}

	@Id
	@Column(name = "USER_ID", unique = true, nullable = false, length = 40)
 	public String getId() {
			return id;
	}

	public void setId(String id) {
			this.id = id;
	}
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
	public UserPassword getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(UserPassword userPassword) {
		this.userPassword = userPassword;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable = false)
    public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 */
	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
	       List<GrantedAuthority> list = new ArrayList<>();
	       list.add(new SimpleGrantedAuthority(roles.getDescription()));
	       return list;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@Override
	@Column(name="FIRST_NAME")
	public String getUsername() {
		return username;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
	 */
	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
	 */
	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@Override
	@Transient
	public boolean isEnabled() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Override
	@Transient
	public String getPassword() {
		return null;
	}

	public void setSignInDate(Timestamp signInDate) {
		this.signInDate = signInDate;
	}

	@Column(name = "SIGN_IN_DATE", length = 19)
	public Timestamp getSignInDate() {
		return signInDate;
	}

	public void setSignOutDate(Timestamp signOutDate) {
		this.signOutDate = signOutDate;
	}

	@Column(name = "SIGN_OUT_DATE", length = 19)
	public Timestamp getSignOutDate() {
		return signOutDate;
	}

	public void setLockedOutDate(Timestamp lockedOutDate) {
		this.lockedOutDate = lockedOutDate;
	}

	@Column(name = "LOCKED_OUT_DATE", length = 19)
	public Timestamp getLockedOutDate() {
		return lockedOutDate;
	}

	public void setTerminationDate(Timestamp terminationDate) {
		this.terminationDate = terminationDate;
	}

	@Column(name = "TERMINATION_DATE", length = 10)
	public Timestamp getTerminationDate() {
		return terminationDate;
	}

	public void setActivationDate(Timestamp activationDate) {
		this.activationDate = activationDate;
	}

	@Column(name = "ACTIVATION_DATE", nullable = false, length = 10)
	public Timestamp getActivationDate() {
		return activationDate;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@Column(name = "LOGIN_ID", unique = true, length = 45)
	public String getLoginId() {
		return loginId;
	}
	
	@Column(name = "LAST_NAME", nullable = false, length = 100)
	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade=CascadeType.ALL)
	public UserPreference getUserPreference() {
		return userPreference;
	}

	public void setUserPreference(UserPreference userPreference) {
		this.userPreference = userPreference;
	}
}


