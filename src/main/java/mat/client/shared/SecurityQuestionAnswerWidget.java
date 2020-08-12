package mat.client.shared;


import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import mat.shared.StringUtility;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.constants.InputType;

public class SecurityQuestionAnswerWidget extends Composite {

	private ListBoxMVP securityQuestion1 = new ListBoxMVP();
	
	private ListBoxMVP securityQuestion2 = new ListBoxMVP();
	
	private ListBoxMVP securityQuestion3 = new ListBoxMVP();
	private Input answer1 = new Input(InputType.PASSWORD);
	private Input answer2 = new Input(InputType.PASSWORD);
	private Input answer3 = new Input(InputType.PASSWORD);
	
	private Hidden answer1Value = new Hidden();
	private Hidden answer2Value = new Hidden();
	private Hidden answer3Value = new Hidden();

	FormGroup rulesGroup = new FormGroup();
	FormGroup question1FormGroup = new FormGroup();
	FormGroup answer1FormGroup = new FormGroup();
	FormGroup question2FormGroup = new FormGroup();
	FormGroup answer2FormGroup = new FormGroup();
	FormGroup question3FormGroup = new FormGroup();
	FormGroup answer3FormGroup = new FormGroup();
	private FlowPanel rulesPanel = new FlowPanel();

	public SecurityQuestionAnswerWidget() {
		answer1.getElement().setId("answer1TextBox");
		answer1.setTitle("Security Question Answer One Required");
		answer2.getElement().setId("answer2TextBox");
		answer2.setTitle("Security Question Answer Two Required");
		answer3.getElement().setId("answer3TextBox");
		answer3.setTitle("Security Question Answer Three Required");
		answer1Value.getElement().setId("answer1Value");
		answer2Value.getElement().setId("answer2Value");
		answer3Value.getElement().setId("answer3Value");
		securityQuestion1.getElement().setId("securityQuestion1ListBoxMVP");
		securityQuestion2.getElement().setId("securityQuestion2ListBoxMVP");
		securityQuestion3.getElement().setId("securityQuestion3ListBoxMVP");
		
		rulesGroup.add(addSecurityQuestionAnsertRules());
		
		FormLabel labelQuestion1 = new FormLabel();
		labelQuestion1.setText("Security Question 1");
		labelQuestion1.setTitle("Security Question 1");
		labelQuestion1.setId("SecurityQnsLabel1");
		labelQuestion1.setFor("securityQuestion1ListBoxMVP");
		labelQuestion1.setShowRequiredIndicator(true);
		
		FormLabel labelQuestion2 = new FormLabel();
		labelQuestion2.setText("Security Question 2");
		labelQuestion2.setTitle("Security Question 2");
		labelQuestion2.setId("SecurityQnsLabel2");
		labelQuestion2.setFor("securityQuestion2ListBoxMVP");
		labelQuestion2.setShowRequiredIndicator(true);
		
		FormLabel labelQuestion3 = new FormLabel();
		labelQuestion3.setText("Security Question 3");
		labelQuestion3.setTitle("Security Question 3");
		labelQuestion3.setId("SecurityQnsLabel3");
		labelQuestion3.setFor("securityQuestion3ListBoxMVP");
		labelQuestion3.setShowRequiredIndicator(true);

		FormLabel labelAnswer1 = new FormLabel();
		labelAnswer1.setText("Security Answer 1");
		labelAnswer1.setTitle("Security Answer 1");
		labelAnswer1.setId("SecurityAnswerLabel1");
		labelAnswer1.setFor("answer1TextBox");
		labelAnswer1.setShowRequiredIndicator(true);
		
		FormLabel labelAnswer2 = new FormLabel();
		labelAnswer2.setText("Security Answer 2");
		labelAnswer2.setTitle("Security Answer 2");
		labelAnswer2.setId("SecurityAnswerLabel2");
		labelAnswer2.setFor("answer2TextBox");
		labelAnswer2.setShowRequiredIndicator(true);
		
		FormLabel labelAnswer3 = new FormLabel();
		labelAnswer3.setText("Security Answer 3");
		labelAnswer3.setTitle("Security Answer 3");
		labelAnswer3.setId("SecurityAnswerLabel3");
		labelAnswer3.setFor("answer3TextBox");
		labelAnswer3.setShowRequiredIndicator(true);
		
		securityQuestion1.setWidth("320px");
		securityQuestion2.setWidth("320px");
		securityQuestion3.setWidth("320px");
		answer1.setWidth("320px");
		answer1.setMaxLength(100);
		answer2.setWidth("320px");
		answer2.setMaxLength(100);
		answer3.setWidth("320px");
		answer3.setMaxLength(100);
		
		question1FormGroup.add(labelQuestion1);
		question1FormGroup.add(securityQuestion1);
		labelAnswer1.setMarginTop(10.00);
		answer1FormGroup.add(answer1Value);
		answer1FormGroup.add(labelAnswer1);
		answer1FormGroup.add(answer1);
		
		question2FormGroup.add(labelQuestion2);
		question2FormGroup.add(securityQuestion2);
		labelAnswer2.setMarginTop(10.00);
		answer2FormGroup.add(answer2Value);
		answer2FormGroup.add(labelAnswer2);
		answer2FormGroup.add(answer2);
		
		question3FormGroup.add(labelQuestion3);
		question3FormGroup.add(securityQuestion3);
		labelAnswer3.setMarginTop(10.00);
		answer3FormGroup.add(answer3Value);
		answer3FormGroup.add(labelAnswer3);
		answer3FormGroup.add(answer3);
		
		getAnswer1().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				getAnswer1().setText(MessageDelegate.EMPTY_VALUE);
			}
		});
			
		getAnswer2().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				getAnswer2().setText(MessageDelegate.EMPTY_VALUE);
			}
		});

		getAnswer3().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				getAnswer3().setText(MessageDelegate.EMPTY_VALUE);
			}
		});
		
		getAnswer1().addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				if(StringUtility.isEmptyOrNull((getAnswer1().getValue())) && !StringUtility.isEmptyOrNull(getAnswer1Value())) {
					getAnswer1().setText(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE);
				}
			}
		});
		
		getAnswer2().addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				if(StringUtility.isEmptyOrNull((getAnswer2().getValue())) && !StringUtility.isEmptyOrNull(getAnswer2Value())) {
					getAnswer2().setText(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE);
				}
			}
		});
		
		getAnswer3().addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				if(StringUtility.isEmptyOrNull((getAnswer3().getValue())) && !StringUtility.isEmptyOrNull(getAnswer3Value())) {
					getAnswer3().setText(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE);
				}
			}
		});
	}

	public FlowPanel addSecurityQuestionAnsertRules() {
		rulesPanel.clear();
		rulesPanel.getElement().setId("fp_FlowPanel");
		
		HTML b1 = new HTML("<img src='images/bullet.png'/><span style='font-size:1.5 em;'> Each security question must have an entered answer in order to save.</span>");
		HTML b2 = new HTML("<img src='images/bullet.png'/> <span style='font-size:1.5 em;'> Each security question may only be used once.</span>");
		HTML b3 = new HTML("<img src='images/bullet.png'/> <span style='font-size:1.5 em;'> Answers are not case sensitive and must contain at least three characters.</span>");
		
		rulesPanel.add(b1);
		rulesPanel.add(b2);
		rulesPanel.add(b3);
		return rulesPanel;
	}
	
	public void prependRule(String ruleHTML) {
		HTML b1 = new HTML(ruleHTML);
		rulesPanel.insert(b1, 0);
	}
	
	/**
	 * Gets the security question1.
	 * 
	 * @return the security question1
	 */
	public ListBoxMVP getSecurityQuestion1() {
		return securityQuestion1;
	}
	
	/**
	 * Sets the security question1.
	 * 
	 * @param securityQuestion1
	 *            the new security question1
	 */
	public void setSecurityQuestion1(ListBoxMVP securityQuestion1) {
		this.securityQuestion1 = securityQuestion1;
	}
	
	/**
	 * Gets the security question2.
	 * 
	 * @return the security question2
	 */
	public ListBoxMVP getSecurityQuestion2() {
		return securityQuestion2;
	}
	
	/**
	 * Sets the security question2.
	 * 
	 * @param securityQuestion2
	 *            the new security question2
	 */
	public void setSecurityQuestion2(ListBoxMVP securityQuestion2) {
		this.securityQuestion2 = securityQuestion2;
	}
	
	/**
	 * Gets the security question3.
	 * 
	 * @return the security question3
	 */
	public ListBoxMVP getSecurityQuestion3() {
		return securityQuestion3;
	}
	
	/**
	 * Sets the security question3.
	 * 
	 * @param securityQuestion3
	 *            the new security question3
	 */
	public void setSecurityQuestion3(ListBoxMVP securityQuestion3) {
		this.securityQuestion3 = securityQuestion3;
	}
	
	/**
	 * Gets the answer1.
	 * 
	 * @return the answer1
	 */
	public Input getAnswer1() {
		return answer1;
	}
	
	/**
	 * Sets the answer1.
	 * 
	 * @param answer1
	 *            the new answer1
	 */
	public void setAnswer1(Input answer1) {
		this.answer1 = answer1;
	}
	
	/**
	 * Gets the answer2.
	 * 
	 * @return the answer2
	 */
	public Input getAnswer2() {
		return answer2;
	}
	
	/**
	 * Sets the answer2.
	 * 
	 * @param answer2
	 *            the new answer2
	 */
	public void setAnswer2(Input answer2) {
		this.answer2 = answer2;
	}
	
	/**
	 * Gets the answer3.
	 * 
	 * @return the answer3
	 */
	public Input getAnswer3() {
		return answer3;
	}
	
	public FormGroup getQuestionAns1FormGroup() {
		return question1FormGroup;
	}

	public FormGroup getQuestionAns2FormGroup() {
		return question2FormGroup;
	}

	public FormGroup getQuestionAns3FormGroup() {
		return question3FormGroup;
	}	
	public FormGroup getAns1FormGroup() {
		return answer1FormGroup;
	}

	public FormGroup getAns2FormGroup() {
		return answer2FormGroup;
	}

	public FormGroup getAns3FormGroup() {
		return answer3FormGroup;
	}

	public FormGroup getRulesGroup() {
		return rulesGroup;
	}
	public String getAnswer1Value() {
		return answer1Value.getValue();
	}

	public void setAnswer1Value(String answer1Value) {
		this.answer1Value.setValue(answer1Value);
	}
	public String getAnswer2Value() {
		return answer2Value.getValue();
	}

	public void setAnswer2Value(String answer2Value) {
		this.answer2Value.setValue(answer2Value);
	}
	public String getAnswer3Value() {
		return answer3Value.getValue();
	}

	public void setAnswer3Value(String answer3Value) {
		this.answer3Value.setValue(answer3Value);
	}
}
