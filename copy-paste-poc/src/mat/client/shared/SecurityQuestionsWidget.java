package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class SecurityQuestionsWidget.
 */
public class SecurityQuestionsWidget extends Composite {
	
	/** The security question1. */
	private ListBoxMVP securityQuestion1 = new ListBoxMVP();
	
	/** The security question2. */
	private ListBoxMVP securityQuestion2 = new ListBoxMVP();
	
	/** The security question3. */
	private ListBoxMVP securityQuestion3 = new ListBoxMVP();
	
	/** The answer1. */
	private TextBox answer1 = new TextBox();
	
	/** The answer2. */
	private TextBox answer2 = new TextBox();
	
	/** The answer3. */
	private TextBox answer3 = new TextBox();
	
	/**
	 * Instantiates a new security questions widget.
	 */
	public SecurityQuestionsWidget() {
		answer1.getElement().setId("answer1_TextBox");
		answer2.getElement().setId("answer2_TextBox");
		answer3.getElement().setId("answer3_TextBox");
		securityQuestion1.getElement().setId("securityQuestion1_ListBoxMVP");
		securityQuestion2.getElement().setId("securityQuestion2_ListBoxMVP");
		securityQuestion3.getElement().setId("securityQuestion3_ListBoxMVP");
		
		FlowPanel container = new FlowPanel();
		container.getElement().setId("container_FlowPanel");
		container.add(LabelBuilder.buildLabel(securityQuestion1, "Security Question 1"));
		container.add(wrap(securityQuestion1));
		container.add(LabelBuilder.buildLabel(answer1, "Security Answer 1"));
		container.add(wrap(answer1));
		container.add(new SpacerWidget());
		
		container.add(LabelBuilder.buildLabel(securityQuestion2, "Security Question 2"));
		container.add(wrap(securityQuestion2));
		container.add(LabelBuilder.buildLabel(answer2, "Security Answer 2"));
		container.add(wrap(answer2));
		container.add(new SpacerWidget());
		
		container.add(LabelBuilder.buildLabel(securityQuestion3, "Security Question 3"));
		container.add(wrap(securityQuestion3));
		container.add(LabelBuilder.buildLabel(answer3, "Security Answer 3"));
		container.add(wrap(answer3));
		container.add(new SpacerWidget());
		
		securityQuestion1.setWidth("320px");
		securityQuestion2.setWidth("320px");
		securityQuestion3.setWidth("320px");
		answer1.setWidth("400px");
		answer2.setWidth("400px");
		answer3.setWidth("400px");
		
		answer1.setMaxLength(100);
		answer2.setMaxLength(100);
		answer3.setMaxLength(100);
		container.setStyleName("securityQuestions");
		initWidget(container);
	}
	
	/**
	 * Wrap.
	 * 
	 * @param widget
	 *            the widget
	 * @return the simple panel
	 */
	private SimplePanel wrap(Widget widget) {
		SimplePanel p = new SimplePanel();
		p.getElement().setId("p_SimplePanel");
		p.add(widget);
		return p;
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
	public TextBox getAnswer1() {
		return answer1;
	}
	
	/**
	 * Sets the answer1.
	 * 
	 * @param answer1
	 *            the new answer1
	 */
	public void setAnswer1(TextBox answer1) {
		this.answer1 = answer1;
	}
	
	/**
	 * Gets the answer2.
	 * 
	 * @return the answer2
	 */
	public TextBox getAnswer2() {
		return answer2;
	}
	
	/**
	 * Sets the answer2.
	 * 
	 * @param answer2
	 *            the new answer2
	 */
	public void setAnswer2(TextBox answer2) {
		this.answer2 = answer2;
	}
	
	/**
	 * Gets the answer3.
	 * 
	 * @return the answer3
	 */
	public TextBox getAnswer3() {
		return answer3;
	}
	
	/**
	 * Sets the answer3.
	 * 
	 * @param answer3
	 *            the new answer3
	 */
	public void setAnswer3(TextBox answer3) {
		this.answer3 = answer3;
	}
	
}
