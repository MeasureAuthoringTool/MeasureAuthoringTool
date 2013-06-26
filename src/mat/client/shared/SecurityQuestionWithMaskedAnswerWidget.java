package mat.client.shared;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class SecurityQuestionWithMaskedAnswerWidget extends Composite {
	private ListBoxMVP securityQuestion1 = new ListBoxMVP();
	private ListBoxMVP securityQuestion2 = new ListBoxMVP();
	private ListBoxMVP securityQuestion3 = new ListBoxMVP();
	
	private PasswordTextBox answer1 = new PasswordTextBox();
	private PasswordTextBox answer2 = new PasswordTextBox();
	private PasswordTextBox answer3 = new PasswordTextBox();
	
	
	public SecurityQuestionWithMaskedAnswerWidget() {
		FlowPanel container = new FlowPanel();
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
		answer1.setWidth("320px");
		answer2.setWidth("320px");
		answer3.setWidth("320px");
		
		answer1.setMaxLength(100);
		answer2.setMaxLength(100);
		answer3.setMaxLength(100);
		container.setStyleName("securityQuestions");
		initWidget(container);
	}
	private SimplePanel wrap(Widget widget) {
		SimplePanel p = new SimplePanel();
		p.add(widget);
		return p;
	}
	
	public ListBoxMVP getSecurityQuestion1() {
		return securityQuestion1;
	}
	public void setSecurityQuestion1(ListBoxMVP securityQuestion1) {
		this.securityQuestion1 = securityQuestion1;
	}
	public ListBoxMVP getSecurityQuestion2() {
		return securityQuestion2;
	}
	public void setSecurityQuestion2(ListBoxMVP securityQuestion2) {
		this.securityQuestion2 = securityQuestion2;
	}
	public ListBoxMVP getSecurityQuestion3() {
		return securityQuestion3;
	}
	public void setSecurityQuestion3(ListBoxMVP securityQuestion3) {
		this.securityQuestion3 = securityQuestion3;
	}
	public PasswordTextBox getAnswer1() {
		return answer1;
	}
	public void setAnswer1(PasswordTextBox answer1) {
		this.answer1 = answer1;
	}
	public PasswordTextBox getAnswer2() {
		return answer2;
	}
	public void setAnswer2(PasswordTextBox answer2) {
		this.answer2 = answer2;
	}
	public PasswordTextBox getAnswer3() {
		return answer3;
	}
	public void setAnswer3(PasswordTextBox answer3) {
		this.answer3 = answer3;
	}
	
	
	
}
