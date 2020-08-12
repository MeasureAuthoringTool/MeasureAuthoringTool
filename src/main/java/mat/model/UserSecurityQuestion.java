package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "USER_SECURITY_QUESTIONS")
public class UserSecurityQuestion implements IsSerializable, Serializable {
	private static final long serialVersionUID = 1467684755533301014L;

	private int id;

	private String userId;
	
	private String rowId;
	
	private String securityQuestionId;
	
	private String securityAnswer;
	
	private SecurityQuestions securityQuestions;
	
	private String salt;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "USER_SECURITY_QUESTIONS_ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "USER_ID", nullable = false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String user) {
		this.userId = user;
	}

	@Column(name = "ROW_ID", nullable = false)
	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	@Column(name = "QUESTION_ID")
	public String getSecurityQuestionId() {
		return securityQuestionId;
	}

	public void setSecurityQuestionId(String securityQuestionId) {
		this.securityQuestionId = securityQuestionId;
	}

	@Column(name = "ANSWER", length = 100)
	public String getSecurityAnswer() {
		return securityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

	@ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.PERSIST)
	@JoinColumn(name = "QUESTION_ID", insertable=false,updatable=false)
	public SecurityQuestions getSecurityQuestions() {
		return securityQuestions;
	}

	public void setSecurityQuestions(SecurityQuestions securityQuestions) {
		this.securityQuestions = securityQuestions;
	}

	@Column(name = "SALT", length = 100)
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}



}
