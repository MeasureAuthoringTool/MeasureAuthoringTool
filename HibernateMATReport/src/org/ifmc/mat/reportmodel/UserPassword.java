package org.ifmc.mat.reportmodel;

import java.sql.Timestamp;
import java.util.Date;

public class UserPassword {
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
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public int getPasswordlockCounter() {
		return passwordlockCounter;
	}
	public void setPasswordlockCounter(int passwordlockCounter) {
		this.passwordlockCounter = passwordlockCounter;
	}
	public int getForgotPwdlockCounter() {
		return forgotPwdlockCounter;
	}
	public void setForgotPwdlockCounter(int forgotPwdlockCounter) {
		this.forgotPwdlockCounter = forgotPwdlockCounter;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isInitial() {
		return initial;
	}
	public void setInitial(boolean initial) {
		this.initial = initial;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setFirstFailedAttemptTime(Timestamp firstFailedAttemptTime) {
		this.firstFailedAttemptTime = firstFailedAttemptTime;
	}
	public Timestamp getFirstFailedAttemptTime() {
		return firstFailedAttemptTime;
	}
	public void setTemporaryPassword(boolean temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}
	public boolean isTemporaryPassword() {
		return temporaryPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
}