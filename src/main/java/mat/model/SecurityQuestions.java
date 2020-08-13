package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SECURITY_QUESTIONS")
public class SecurityQuestions  implements IsSerializable {

	private String questionId;
	
	private String question ;


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "QUESTION_ID", unique = true, nullable = false)
	public String getQuestionId() {
		return questionId;
	}
	
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
	@Column(name = "QUESTION", nullable = false, length = 100)
	public String getQuestion() {
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
}
	



