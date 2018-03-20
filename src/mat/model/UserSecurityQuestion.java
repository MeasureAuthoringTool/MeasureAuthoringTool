package mat.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;



/**
 * The Class UserSecurityQuestion.
 */
public class UserSecurityQuestion implements IsSerializable, Serializable {
	private static final long serialVersionUID = 1467684755533301014L;

	private int id;

	/** The user */
	private String userId;
	
	/** The row id. */
	private String rowId;
	
	/** The security question id. */
	private String securityQuestionId;
	
	/** The security answer. */
	private String securityAnswer;
	
	/** The security questions. */
	private SecurityQuestions securityQuestions;
	
	private String salt;

	/**
	 * Gets the security question id.
	 * 
	 * @return the security question id
	 */
	public String getSecurityQuestionId() {
		return securityQuestionId;
	}
	
	/**
	 * Gets the security answer.
	 * 
	 * @return the security answer
	 */
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	
	/**
	 * Sets the security answer.
	 * 
	 * @param securityAnswer
	 *            the new security answer
	 */
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
	
	/**
	 * Gets the row id.
	 * 
	 * @return the row id
	 */
	public String getRowId() {
		return rowId;
	}
	
	/**
	 * Sets the row id.
	 * 
	 * @param rowId
	 *            the new row id
	 */
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	
	/**
	 * Gets the row id.
	 * 
	 * @return the row id
	 */
	public int getId() {
		return id;
	}
	
	public String getSalt() {
		return salt;
	}
	
	/**
	 * Sets the row id.
	 * 
	 * @param rowId
	 *            the new row id
	 */
	public void setId(int Id) {
		this.id = Id;
	}
	

	/**
	 * Sets the security question id.
	 * 
	 * @param securityQuestionId
	 *            the new security question id
	 */
	public void setSecurityQuestionId(String securityQuestionId) {
		this.securityQuestionId = securityQuestionId;
	}
	
	/**
	 * Gets the security questions.
	 * 
	 * @return the security questions
	 */
	public SecurityQuestions getSecurityQuestions() {
		return securityQuestions;
	}
	
	/**
	 * Sets the security questions.
	 * 
	 * @param securityQuestions
	 *            the new security questions
	 */
	public void setSecurityQuestions(SecurityQuestions securityQuestions) {
		this.securityQuestions = securityQuestions;
	}
	

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
