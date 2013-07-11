package mat.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;



public class UserSecurityQuestion implements IsSerializable, Serializable {

	private String userId;
	private String rowId;
	private String securityQuestionId;
	private String securityAnswer;
	private SecurityQuestions securityQuestions;
	
	public String getSecurityQuestionId() {
		return securityQuestionId;
	}
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public void setSecurityQuestionId(String securityQuestionId) {
		this.securityQuestionId = securityQuestionId;
	}
	public SecurityQuestions getSecurityQuestions() {
		return securityQuestions;
	}
	public void setSecurityQuestions(SecurityQuestions securityQuestions) {
		this.securityQuestions = securityQuestions;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
