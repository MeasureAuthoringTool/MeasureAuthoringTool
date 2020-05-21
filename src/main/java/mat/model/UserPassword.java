package mat.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "USER_PASSWORD", uniqueConstraints = @UniqueConstraint(columnNames = "USER_ID"))
public class UserPassword {

	public static final int PASSWORD_LOCKED_CNT = 3;
	private String id;
	
	private User user;
	
	private int passwordlockCounter;
	
	private Timestamp firstFailedAttemptTime;
	
	private int forgotPwdlockCounter;
	
	private String password;
	
	private String salt;
	
	private boolean initial;
	
	private boolean temporaryPassword;
	
	private Date createdDate;
	
	public UserPassword() {
		
	}
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "USER_PASSWORD_ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", unique = true, nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "PWD_LOCK_COUNTER")
	public int getPasswordlockCounter() {
		return passwordlockCounter;
	}
	
	public void setPasswordlockCounter(int passwordlockCounter) {
		this.passwordlockCounter = passwordlockCounter;
	}
	
	@Column(name = "FORGOT_PWD_LOCK_COUNTER")
	public int getForgotPwdlockCounter() {
		return forgotPwdlockCounter;
	}
	
	public void setForgotPwdlockCounter(int forgotPwdlockCounter) {
		this.forgotPwdlockCounter = forgotPwdlockCounter;
	}
	
	@Column(name = "PASSWORD", nullable = false, length = 100)
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "INITIAL_PWD")
	public boolean isInitial() {
		return initial;
	}
	
	public void setInitial(boolean initial) {
		this.initial = initial;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE", nullable = false, length = 10)
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public void setFirstFailedAttemptTime(Timestamp firstFailedAttemptTime) {
		this.firstFailedAttemptTime = firstFailedAttemptTime;
	}
	
	@Column(name = "FIRST_FAILED_ATTEMPT_TIME", length = 19)
	public Timestamp getFirstFailedAttemptTime() {
		return firstFailedAttemptTime;
	}
	
	public void setTemporaryPassword(boolean temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}
	
	@Column(name = "TEMP_PWD")
	public boolean isTemporaryPassword() {
		return temporaryPassword;
	}

	@Column(name = "SALT", nullable = false, length = 100)
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Transient
	public boolean isPasswordLocked() {
		return getPasswordlockCounter() >= PASSWORD_LOCKED_CNT;
	}
	
}