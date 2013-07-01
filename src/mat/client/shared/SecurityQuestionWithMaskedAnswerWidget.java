package mat.client.shared;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SecurityQuestionWithMaskedAnswerWidget extends Composite {
	private ListBoxMVP securityQuestion1 = new ListBoxMVP();
	private ListBoxMVP securityQuestion2 = new ListBoxMVP();
	private ListBoxMVP securityQuestion3 = new ListBoxMVP();
	
	/*private PasswordTextBox answer1 = new PasswordTextBox();
	private PasswordTextBox answer2 = new PasswordTextBox();
	private PasswordTextBox answer3 = new PasswordTextBox();*/
	private TextBox answer1 = new TextBox();
	private TextBox answer2 = new TextBox();
	private TextBox answer3 = new TextBox();
	private String answerText1;
	private String answerText2;
	private String answerText3;
	public SecurityQuestionWithMaskedAnswerWidget() {
		FlowPanel container = new FlowPanel();
		container.add(LabelBuilder.buildLabel(securityQuestion1, "Security Question 1"));
		container.add(wrap(securityQuestion1));
		container.add(LabelBuilder.buildLabel(answer1, "Security Answer 1"));
		container.add(wrap(answer1));
		answer1.setFocus(false);
		container.add(new SpacerWidget());
		
		container.add(LabelBuilder.buildLabel(securityQuestion2, "Security Question 2"));
		container.add(wrap(securityQuestion2));
		container.add(LabelBuilder.buildLabel(answer2, "Security Answer 2"));
		
		container.add(wrap(answer2));
		answer2.setFocus(false);
		container.add(new SpacerWidget());
		
		container.add(LabelBuilder.buildLabel(securityQuestion3, "Security Question 3"));
		container.add(wrap(securityQuestion3));
		container.add(LabelBuilder.buildLabel(answer3, "Security Answer 3"));
		
		container.add(wrap(answer3));
		answer3.setFocus(false);
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
	public TextBox getAnswer1() {
		return answer1;
	}
	public void setAnswer1(TextBox answer1) {
		this.answer1 = answer1;
	}
	public TextBox getAnswer2() {
		return answer2;
	}
	public void setAnswer2(TextBox answer2) {
		this.answer2 = answer2;
	}
	public TextBox getAnswer3() {
		return answer3;
	}
	public void setAnswer3(TextBox answer3) {
		this.answer3 = answer3;
	}
	public String getAnswerText1() {
		return answerText1;
	}
	public void setAnswerText1(String answerText1) {
		this.answerText1 = answerText1;
	}
	public String getAnswerText2() {
		return answerText2;
	}
	public void setAnswerText2(String answerText2) {
		this.answerText2 = answerText2;
	}
	public String getAnswerText3() {
		return answerText3;
	}
	public void setAnswerText3(String answerText3) {
		this.answerText3 = answerText3;
	}
	
	
	
}
