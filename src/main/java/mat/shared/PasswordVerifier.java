package mat.shared;

import mat.client.shared.MatContext;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class PasswordVerifier.
 */
public class PasswordVerifier {
	
	/** The Constant MIN_LENGTH. */
	private static final int MIN_LENGTH = 15;
	
	/** The Constant MAX_LENGTH. */
	private static final int MAX_LENGTH = 128;
	
	/** The Constant SPECIAL_CHARS. */
	private static final char[] SPECIAL_CHARS = 
		new char[] {'%', '#', '*', '+', '-', ',', ':', '=', '?', '_'};

	/** The contains upper. */
	private boolean containsUpper;
	
	/** The contains lower. */
	private boolean containsLower;
	
	/** The contains special. */
	private boolean containsSpecial;
	
	/** The contains number. */
	private boolean containsNumber;
	
	/** The not too long. */
	private boolean notTooLong;
	
	/** The not too short. */
	private boolean notTooShort;
	
	/** The confirmed. */
	private boolean confirmed;
	
	/** The valid password. */
	private boolean validPassword;
	
	/** The message. */
	private List<String> message = new ArrayList<String>();
	
	/**
	 * Instantiates a new password verifier.
	 *
	 * @param password
	 *            the password
	 * @param confirmPassword
	 *            the confirm password
	 */
	public PasswordVerifier(String password, String confirmPassword) {
		validPassword = isPasswordValid(password, confirmPassword);
	
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
	}
	
	/**
	 * Gets the allowed special chars.
	 * 
	 * @return the allowed special chars
	 */
	public static char[] getAllowedSpecialChars() {
		return SPECIAL_CHARS;
	}
	
	/**
	 * Gets the max length.
	 * 
	 * @return the max length
	 */
	public static int getMaxLength() {
		return MAX_LENGTH;
	}
	
	/**
	 * Gets the min length.
	 * 
	 * @return the min length
	 */
	public static int getMinLength() {
		return MIN_LENGTH;
	}
	
	/**
	 * Gets the messages.
	 * 
	 * @return the messages
	 */
	public List<String> getMessages() {
		return message;
	}
	
	/**
	 * Checks if is valid.
	 * 
	 * @return true, if is valid
	 */
	public boolean isValid() {
		return validPassword;
	}
	
	/**
	 * Checks if is password valid.
	 *
	 * @param pwd
	 *            the pwd
	 * @param confirm
	 *            the confirm
	 * @return true, if is password valid
	 */
	private boolean isPasswordValid(String pwd, String confirm) {
	//private boolean isPasswordValid(String pwd, String confirm) {
		confirmed = pwd.equals(confirm);
		notTooLong = pwd.length() <= MAX_LENGTH;
		notTooShort = pwd.length() >= MIN_LENGTH;
		
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
		
		return notTooLong && notTooShort && containsUpper && containsLower && containsSpecial && containsNumber
                && confirmed;
		
		}

	/**
	 * Checks if is contains upper.
	 * 
	 * @return true, if is contains upper
	 */
	public boolean isContainsUpper() {
		return containsUpper;
	}

	/**
	 * Sets the contains upper.
	 * 
	 * @param containsUpper
	 *            the new contains upper
	 */
	void setContainsUpper(boolean containsUpper) {
		this.containsUpper = containsUpper;
	}

	/**
	 * Checks if is contains lower.
	 * 
	 * @return true, if is contains lower
	 */
	boolean isContainsLower() {
		return containsLower;
	}

	/**
	 * Sets the contains lower.
	 * 
	 * @param containsLower
	 *            the new contains lower
	 */
	void setContainsLower(boolean containsLower) {
		this.containsLower = containsLower;
	}

	/**
	 * Checks if is contains special.
	 * 
	 * @return true, if is contains special
	 */
	boolean isContainsSpecial() {
		return containsSpecial;
	}

	/**
	 * Sets the contains special.
	 * 
	 * @param containsSpecial
	 *            the new contains special
	 */
	void setContainsSpecial(boolean containsSpecial) {
		this.containsSpecial = containsSpecial;
	}

	/**
	 * Checks if is contains number.
	 * 
	 * @return true, if is contains number
	 */
	boolean isContainsNumber() {
		return containsNumber;
	}

	/**
	 * Sets the contains number.
	 * 
	 * @param containsNumber
	 *            the new contains number
	 */
	void setContainsNumber(boolean containsNumber) {
		this.containsNumber = containsNumber;
	}

	/**
	 * Checks if is not too long.
	 * 
	 * @return true, if is not too long
	 */
	boolean isNotTooLong() {
		return notTooLong;
	}

	/**
	 * Sets the not too long.
	 * 
	 * @param notTooLong
	 *            the new not too long
	 */
	void setNotTooLong(boolean notTooLong) {
		this.notTooLong = notTooLong;
	}

	/**
	 * Checks if is not too short.
	 * 
	 * @return true, if is not too short
	 */
	boolean isNotTooShort() {
		return notTooShort;
	}

	/**
	 * Sets the not too short.
	 * 
	 * @param notTooShort
	 *            the new not too short
	 */
	void setNotTooShort(boolean notTooShort) {
		this.notTooShort = notTooShort;
	}

	/**
	 * Checks if is valid password.
	 * 
	 * @return true, if is valid password
	 */
	boolean isValidPassword() {
		return validPassword;
	}

	/**
	 * Sets the valid password.
	 * 
	 * @param validPassword
	 *            the new valid password
	 */
	void setValidPassword(boolean validPassword) {
		this.validPassword = validPassword;
	}
	
	
}
