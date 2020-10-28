package mat.client.shared;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Input;

import java.util.List;

public interface SecurityQuestionsDisplay {
	
	/**
	 * Gets the submit.
	 * 
	 * @return the submit
	 */
	public HasClickHandlers getSubmit();
	
	/**
	 * Gets the reset.
	 * 
	 * @return the reset
	 */
	public HasClickHandlers getReset();
	
	/**
	 * Gets the question1 answer.
	 * 
	 * @return the question1 answer
	 */
	public HasValue<String> getQuestion1Answer();
	
	/**
	 * Gets the question2 answer.
	 * 
	 * @return the question2 answer
	 */
	public HasValue<String> getQuestion2Answer();
	
	/**
	 * Gets the question3 answer.
	 * 
	 * @return the question3 answer
	 */
	public HasValue<String> getQuestion3Answer();
	
	/**
	 * Gets the question1 text.
	 * 
	 * @return the question1 text
	 */
	public HasValue<String> getQuestion1Text();
	
	/**
	 * Gets the question2 text.
	 * 
	 * @return the question2 text
	 */
	public HasValue<String> getQuestion2Text();
	
	/**
	 * Gets the question3 text.
	 * 
	 * @return the question3 text
	 */
	public HasValue<String> getQuestion3Text();
	
	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public Input getPassword();
	
	/**
	 * Gets the confirm password.
	 * 
	 * @return the confirm password
	 */
	public Input getConfirmPassword();
	
	/**
	 * Gets the password error message display.
	 * 
	 * @return the password error message display
	 */
	public MessageAlert getPasswordErrorMessageDisplay();
	
	/**
	 * Gets the security error message display.
	 * 
	 * @return the security error message display
	 */
	public MessageAlert getSecurityErrorMessageDisplay();
	
	/**
	 * Gets the success message display.
	 * 
	 * @return the success message display
	 */
	public MessageAlert getSuccessMessageDisplay();
	
	/**
	 * Adds the security question texts.
	 * 
	 * @param texts
	 *            the texts
	 */
	public void addSecurityQuestionTexts(List<NameValuePair> texts);
	
	/**
	 * As widget.
	 * 
	 * @return the widget
	 */
	public Widget asWidget();
	
	/**
	 * Gets the answer text1.
	 * 
	 * @return the answer text1
	 */
	public String getAnswerText1();
	
	/**
	 * Gets the answer text2.
	 * 
	 * @return the answer text2
	 */
	public String getAnswerText2();
	
	/**
	 * Gets the answer text3.
	 * 
	 * @return the answer text3
	 */
	public String getAnswerText3();
	
	/**
	 * Gets the security questions widget.
	 * 
	 * @return the security questions widget
	 */
	SecurityQuestionAnswerWidget getSecurityQuestionsWidget();
	
	/**
	 * Gets the save button.
	 * 
	 * @return the save button
	 */
	HasClickHandlers getSaveButton();
	
	/**
	 * Gets the cancel button.
	 * 
	 * @return the cancel button
	 */
	HasClickHandlers getCancelButton();
}
