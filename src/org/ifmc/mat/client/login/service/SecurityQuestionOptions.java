package org.ifmc.mat.client.login.service;

import java.util.List;

import org.ifmc.mat.client.shared.NameValuePair;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SecurityQuestionOptions implements IsSerializable {
	
	private List<NameValuePair> securityQuestions;
	private boolean userFound;
	
	public List<NameValuePair> getSecurityQuestions() {
		return securityQuestions;
	}
	public void setSecurityQuestions(List<NameValuePair> securityQuestions) {
		this.securityQuestions = securityQuestions;
	}
	public boolean isUserFound() {
		return userFound;
	}
	public void setUserFound(boolean userFound) {
		this.userFound = userFound;
	}
	
	
}
