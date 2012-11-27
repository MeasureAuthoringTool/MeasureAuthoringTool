package mat.shared;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mat.client.shared.MatContext;

public class SecurityQuestionVerifier {
	
	private List<String> message = new ArrayList<String>();
	
	//
	// use a value object for the parameters?
	//
	public SecurityQuestionVerifier(String question1, String answer1,
				String question2, String answer2, String question3, 
				String answer3) {
		
		if(answer1.trim().length() < 3) {
			message.add(MatContext.get().getMessageDelegate().getSecurityAnswerTooShortMessage(1));
		}
		if(answer2.trim().length() < 3) {
			message.add(MatContext.get().getMessageDelegate().getSecurityAnswerTooShortMessage(2));
		}
		if(answer3.trim().length() < 3) {
			message.add(MatContext.get().getMessageDelegate().getSecurityAnswerTooShortMessage(3));
		}
		

		Set<String> questionTexts = new HashSet<String>();
		questionTexts.add(question1);
		questionTexts.add(question2);
		questionTexts.add(question3);
		if(questionTexts.size() < 3) {
			message.add(MatContext.get().getMessageDelegate().getNotUniqueQuestions());
		}
				
		if(message.size() > 0) {
			message.add(0, MatContext.get().getMessageDelegate().getSecurityQuestionHeeaderMessage());
		}
		
		
	}
	
	public List<String> getMessages() {
		return message;
	}
	public boolean isValid() {
		return message.size() == 0;
	}
	
}
