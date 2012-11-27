package org.ifmc.mat.shared;

import java.util.ArrayList;
import java.util.List;

import org.ifmc.mat.client.shared.MatContext;

public class PasswordVerifier {
	private static final int MIN_LENGTH = 8;
	private static final int MAX_LENGTH = 16;
	
	private static final char[] SPECIAL_CHARS = 
		new char[] {'%', '#', '*', '+', '-', ',', ':', '=', '?', '_'};
	
	private boolean notUserid;
	private boolean noRepeatedChar;
	private boolean containsUpper;
	private boolean containsLower;
	private boolean containsSpecial;
	private boolean containsNumber;
	private boolean notTooLong;
	private boolean notTooShort;
	private boolean confirmed;
	
	private boolean validPassword;
	private List<String> message = new ArrayList<String>();
	
	public PasswordVerifier(String userId, String password, String confirmPassword) {
		validPassword = isPasswordValid(userId, password, confirmPassword);
	
		message.add(MatContext.get().getMessageDelegate().getDoesntFollowRulesMessage());
		if(!confirmed) {
			message.add(MatContext.get().getMessageDelegate().getPasswordMismatchMessage());
		}
		if(!notTooShort || !notTooLong) {
			message.add(MatContext.get().getMessageDelegate().getPasswordWrongLengthMessage());
		}
		if(!containsUpper) {
			message.add(MatContext.get().getMessageDelegate().getMustContainUpperMessage());
		}
		if(!containsLower) {
			message.add(MatContext.get().getMessageDelegate().getMustContainLowerMessage());
		}
		if(!containsNumber) {
			message.add(MatContext.get().getMessageDelegate().getMustContainNumberMessage());
		}
		if(!containsSpecial) {
			message.add(MatContext.get().getMessageDelegate().getMustContainSpecialMessage());
		}
		if(!notUserid) {
			message.add(MatContext.get().getMessageDelegate().getMustNotContainEmailMessage());
		}
		if(!noRepeatedChar) {
			message.add(MatContext.get().getMessageDelegate().getMustNotContainRunsMessage());
		}
	}
	
	public static char[] getAllowedSpecialChars() {
		return SPECIAL_CHARS;
	}
	public static int getMaxLength() {
		return MAX_LENGTH;
	}
	public static int getMinLength() {
		return MIN_LENGTH;
	}
	public List<String> getMessages() {
		return message;
	}
	public boolean isValid() {
		return validPassword;
	}
	
	private boolean isPasswordValid(String userid, String pwd, String confirm) {
		confirmed = pwd.equals(confirm);
		notTooLong = pwd.length() <= MAX_LENGTH;
		notTooShort = pwd.length() >= MIN_LENGTH;
		notUserid = pwd.toUpperCase().indexOf(userid.toUpperCase()) < 0;
		noRepeatedChar = checkForRepeatedChar(pwd);
		
		containsUpper = false;
		containsLower = false;
		containsSpecial = false;
		containsNumber = false;
		String specialChars = new String(SPECIAL_CHARS);
		
		for(int i = 0; i < pwd.length(); i++) {
			char c = pwd.charAt(i);
			containsUpper |= Character.isUpperCase(c);
			containsLower |= Character.isLowerCase(c);
			containsSpecial |= specialChars.indexOf(c) >= 0;
			containsNumber |= Character.isDigit(c);
		}
		
		return notTooLong && notTooShort && notUserid && containsUpper
				&& containsLower && containsSpecial && containsNumber
				&& noRepeatedChar && noRepeatedChar && confirmed;
		
	}
	
	private boolean checkForRepeatedChar(String pwd) {
		for(int i = 0; i < pwd.length() - 2; i++) {
			if(pwd.charAt(i) == pwd.charAt(i+1) &&
					pwd.charAt(i) == pwd.charAt(i + 2)) {
				return false;
			}
		}
		
		return true;
	}

	boolean isNotUserid() {
		return notUserid;
	}

	void setNotUserid(boolean notUserid) {
		this.notUserid = notUserid;
	}

	boolean isNoRepeatedChar() {
		return noRepeatedChar;
	}

	void setNoRepeatedChar(boolean noRepeatedChar) {
		this.noRepeatedChar = noRepeatedChar;
	}

	boolean isContainsUpper() {
		return containsUpper;
	}

	void setContainsUpper(boolean containsUpper) {
		this.containsUpper = containsUpper;
	}

	boolean isContainsLower() {
		return containsLower;
	}

	void setContainsLower(boolean containsLower) {
		this.containsLower = containsLower;
	}

	boolean isContainsSpecial() {
		return containsSpecial;
	}

	void setContainsSpecial(boolean containsSpecial) {
		this.containsSpecial = containsSpecial;
	}

	boolean isContainsNumber() {
		return containsNumber;
	}

	void setContainsNumber(boolean containsNumber) {
		this.containsNumber = containsNumber;
	}

	boolean isNotTooLong() {
		return notTooLong;
	}

	void setNotTooLong(boolean notTooLong) {
		this.notTooLong = notTooLong;
	}

	boolean isNotTooShort() {
		return notTooShort;
	}

	void setNotTooShort(boolean notTooShort) {
		this.notTooShort = notTooShort;
	}

	boolean isValidPassword() {
		return validPassword;
	}

	void setValidPassword(boolean validPassword) {
		this.validPassword = validPassword;
	}
	
	
}
