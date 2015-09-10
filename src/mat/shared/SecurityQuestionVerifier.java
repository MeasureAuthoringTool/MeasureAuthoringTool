package mat.shared;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//import java.util.logging.Logger;

import mat.client.shared.MatContext;

/**
 * The Class SecurityQuestionVerifier.
 */
public class SecurityQuestionVerifier {
	
	
	/** The Constant logger. */
//	private final Logger fLogger=Logger.getLogger("SecurityQuestionVerifier.class");
	//private static final Log logger = LogFactory.getLog(SecurityQuestionVerifier.class);

	/** The message. */
	private List<String> message = new ArrayList<String>();
	
	//
	// use a value object for the parameters?
	//
	/**
	 * Instantiates a new security question verifier.
	 * 
	 * @param question1
	 *            the question1
	 * @param answer1
	 *            the answer1
	 * @param question2
	 *            the question2
	 * @param answer2
	 *            the answer2
	 * @param question3
	 *            the question3
	 * @param answer3
	 *            the answer3
	 */
	public SecurityQuestionVerifier(String question1, String answer1,
			String question2, String answer2, 
			String question3, String answer3) {
		if(!checkForMarkUp(answer1, answer2, answer3)){
			message.add(MatContext.get().getMessageDelegate().getNoMarkupAllowedMessage());
		}
		if(answer1.trim().length() < 3) {
			message.add(MatContext.get().getMessageDelegate().getSecurityAnswerTooShortMessage(1));
		}
		if(answer2.trim().length() < 3) {
			message.add(MatContext.get().getMessageDelegate().getSecurityAnswerTooShortMessage(2));
		}
		if(answer3.trim().length() < 3) {
			message.add(MatContext.get().getMessageDelegate().getSecurityAnswerTooShortMessage(3));
		}
		
/*		fLogger.info(question1);
		fLogger.info(answer1);
		fLogger.info(question2);
		fLogger.info(answer2);
		fLogger.info(question3);
		fLogger.info(answer3);
*/
		
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
	
	private boolean checkForMarkUp(String answer1, String answer2, String answer3) {
		String markupRegExp = "<[^>]+>";
		
		String noMarkupText = answer1.trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(answer1.trim().length() > noMarkupText.length()){
			return false;
		}
		noMarkupText = answer2.trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(answer2.trim().length() > noMarkupText.length()){
			return false;
		}
		noMarkupText = answer3.trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(answer3.trim().length() > noMarkupText.length()){
			return false;
		}
		return true;
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
		return message.size() == 0;
	}
	
}
