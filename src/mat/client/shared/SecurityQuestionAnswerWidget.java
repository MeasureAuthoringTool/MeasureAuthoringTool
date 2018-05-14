package mat.client.shared;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.constants.InputType;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;

import mat.shared.StringUtility;

/**
 * The Class SecurityQuestionWithMaskedAnswerWidget.
 */
public class SecurityQuestionAnswerWidget extends Composite {

	/** The security question1. */
	private ListBoxMVP securityQuestion1 = new ListBoxMVP();
	
	/** The security question2. */
	private ListBoxMVP securityQuestion2 = new ListBoxMVP();
	
	/** The security question3. */
	private ListBoxMVP securityQuestion3 = new ListBoxMVP();
	private Input answer1 = new Input(InputType.TEXT);
	private Input answer2 = new Input(InputType.TEXT);
	private Input answer3 = new Input(InputType.TEXT);
	
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
	
	private boolean isCtrlKey = false;
	private Map<String, String> securityAnswers = new HashMap<String, String>();
	private List<Integer> validKeysList = new ArrayList<Integer>();
	/**
	 * Instantiates a new security question with masked answer widget.
	 */
	public SecurityQuestionAnswerWidget() {
		securityAnswers.put("answer1", "");
		securityAnswers.put("answer2", "");
		securityAnswers.put("answer3", "");
		buildValidKeysList();
		
		answer1.getElement().setId("answer1TextBox");
		answer2.getElement().setId("answer2TextBox");
		answer3.getElement().setId("answer3TextBox");
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
				securityAnswers.put("answer1", MessageDelegate.EMPTY_VALUE);
			}
		});
			
		getAnswer2().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				getAnswer2().setText(MessageDelegate.EMPTY_VALUE);
				securityAnswers.put("answer2", MessageDelegate.EMPTY_VALUE);
			}
		});

		getAnswer3().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				getAnswer3().setText(MessageDelegate.EMPTY_VALUE);
				securityAnswers.put("answer3", MessageDelegate.EMPTY_VALUE);
			}
		});
		
		getAnswer1().addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				if(StringUtility.isEmptyOrNull((getAnswer1().getValue())) && !StringUtility.isEmptyOrNull(getAnswer1Value())) {
					getAnswer1().setText(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE);
				}
				//Replacing the last character of the string with '*'
				String answer1Text = getAnswer1().getText().replaceAll(".", "*");
				getAnswer1().setText(answer1Text);
			}
		});
		
		getAnswer2().addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				if(StringUtility.isEmptyOrNull((getAnswer2().getValue())) && !StringUtility.isEmptyOrNull(getAnswer2Value())) {
					getAnswer2().setText(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE);
				}
				//Replacing the last character of the string with '*'
				String answer2Text = getAnswer2().getText().replaceAll(".", "*");
				getAnswer2().setText(answer2Text);
			}
		});
		
		getAnswer3().addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				if(StringUtility.isEmptyOrNull((getAnswer3().getValue())) && !StringUtility.isEmptyOrNull(getAnswer3Value())) {
					getAnswer3().setText(MessageDelegate.DEFAULT_SECURITY_QUESTION_VALUE);
				}
				//Replacing the last character of the string with '*'
				String answer3Text = getAnswer3().getText().replaceAll(".", "*");
				getAnswer3().setText(answer3Text);
			}
		});
		
		getAnswer1().addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				handleBackSpace("answer1", event.getNativeKeyCode());
				getAnswer1().setText(displayValueAsAsterix(getAnswer1().getText(), "answer1", event.getNativeKeyCode()));
			}
		});
		
		getAnswer2().addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				handleBackSpace("answer2", event.getNativeKeyCode());
				getAnswer2().setText(displayValueAsAsterix(getAnswer2().getText(), "answer2", event.getNativeKeyCode()));
			}
		});
		
		getAnswer3().addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				handleBackSpace("answer3", event.getNativeKeyCode());
				getAnswer3().setText(displayValueAsAsterix(getAnswer3().getText(), "answer3", event.getNativeKeyCode()));
			}
		});
		
		getAnswer1().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (KeyCodes.KEY_CTRL == event.getNativeKeyCode()) {
					isCtrlKey = true;
				}
			}
		});
		
		getAnswer2().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (KeyCodes.KEY_CTRL == event.getNativeKeyCode()) {
					isCtrlKey = true;
				}
			}
		});
		
		getAnswer3().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (KeyCodes.KEY_CTRL == event.getNativeKeyCode()) {
					isCtrlKey = true;
				}
			}
		});
	}
	
	private String displayValueAsAsterix(String temporaryValue, String currentField, int charCode) {
		String formattedValue = "";
		if (validKeysList.contains(charCode)) {
			if (isCtrlKey) {
				securityAnswers.put(currentField, temporaryValue);
				formattedValue = temporaryValue;
				isCtrlKey = false;
			}else {
				char newChar = temporaryValue.charAt(temporaryValue.length() - 1);
				String securityAnswer = securityAnswers.get(currentField); 
				securityAnswer += newChar;
				securityAnswers.put(currentField, securityAnswer);
				formattedValue = temporaryValue.length() == 2 ? "*" : "";
				if(temporaryValue.length() > 2) {
					formattedValue = temporaryValue.substring(0, securityAnswer.length()-2);
					formattedValue += "*";
				}
				formattedValue += newChar;
			}
		}else if (KeyCodes.KEY_RIGHT == charCode || KeyCodes.KEY_LEFT == charCode) {
			formattedValue = temporaryValue.substring(0, temporaryValue.length()-1).concat("*");
		}else if (KeyCodes.KEY_SHIFT == charCode) {
			formattedValue = temporaryValue;
		}else if (KeyCodes.KEY_CTRL == charCode || KeyCodes.KEY_SPACE == charCode) {
			formattedValue = temporaryValue.replaceAll(".", "*");
		}
		return formattedValue;
	}

	private void handleBackSpace(String currentField, int charCode) {
		if (KeyCodes.KEY_BACKSPACE == charCode) {
			securityAnswers.put(currentField, MessageDelegate.EMPTY_VALUE);
		}
	}
	
	private List<Integer> buildValidKeysList() {
		Integer[] keys = {KeyCodes.KEY_A, KeyCodes.KEY_B, KeyCodes.KEY_C, KeyCodes.KEY_D, KeyCodes.KEY_E, KeyCodes.KEY_F, KeyCodes.KEY_G, 
				KeyCodes.KEY_H, KeyCodes.KEY_I, KeyCodes.KEY_J, KeyCodes.KEY_K, KeyCodes.KEY_L, KeyCodes.KEY_M, KeyCodes.KEY_N, KeyCodes.KEY_O, 
				KeyCodes.KEY_P, KeyCodes.KEY_Q, KeyCodes.KEY_R, KeyCodes.KEY_S, KeyCodes.KEY_T, KeyCodes.KEY_U, KeyCodes.KEY_V, KeyCodes.KEY_W, 
				KeyCodes.KEY_X, KeyCodes.KEY_Y, KeyCodes.KEY_Z, KeyCodes.KEY_ONE, KeyCodes.KEY_TWO, KeyCodes.KEY_THREE, KeyCodes.KEY_FOUR, KeyCodes.KEY_FIVE, 
				KeyCodes.KEY_SIX, KeyCodes.KEY_SEVEN, KeyCodes.KEY_EIGHT, KeyCodes.KEY_NINE, KeyCodes.KEY_ZERO};
		
		for (int index=0; index< keys.length; index++) {
			validKeysList.add(keys[index]);
		}
		return validKeysList;
	}
	
	/**
	 * @return
	 */
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
	
	public Map<String, String> getSecurityAnswers(){
		return securityAnswers;
	}
}
