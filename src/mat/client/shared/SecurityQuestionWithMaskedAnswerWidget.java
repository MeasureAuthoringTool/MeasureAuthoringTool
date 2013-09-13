package mat.client.shared;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SecurityQuestionWithMaskedAnswerWidget extends Composite {
	private ListBoxMVP securityQuestion1 = new ListBoxMVP();
	private ListBoxMVP securityQuestion2 = new ListBoxMVP();
	private ListBoxMVP securityQuestion3 = new ListBoxMVP();
	
	private TextBox answer1 = new TextBox();
	private TextBox answer2 = new TextBox();
	private TextBox answer3 = new TextBox();
	private String answerText1;
	private String answerText2;
	private String answerText3;
	public SecurityQuestionWithMaskedAnswerWidget() {
		answer1.getElement().setId("answer1_TextBox");
		answer2.getElement().setId("answer2_TextBox");
		answer3.getElement().setId("answer3_TextBox");
		securityQuestion1.getElement().setId("securityQuestion1_ListBoxMVP");
		securityQuestion2.getElement().setId("securityQuestion2_ListBoxMVP");
		securityQuestion3.getElement().setId("securityQuestion3_ListBoxMVP");
		
		FlowPanel container = new FlowPanel();
		container.getElement().setId("container_FlowPanel");
		FlowPanel fp = new FlowPanel();
		fp.getElement().setId("fp_FlowPanel");
		
		HTML b1 = new HTML("<img src='images/bullet.png'/><span style='font-size:1.5 em;'> You must select three questions and enter an answer for each question.</span>");
		HTML b2 = new HTML("<img src='images/bullet.png'/> <span style='font-size:1.5 em;'> You cannot use the same question more than once.</span>");
		HTML b3 = new HTML("<img src='images/bullet.png'/> <span style='font-size:1.5 em;'> Answers are NOT case sensitive (caps or no caps are OK).</span>");
		
		fp.add(b1);
		fp.add(b2);
		fp.add(b3);
		container.add(fp);
		
		container.add(new SpacerWidget());
		
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
		p.getElement().setId("p_SimplePanel");
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
	
	public String maskAnswers(String answer){
		String maskedAnswer = new String();
		for(int i=0;i<answer.length();i++){
			maskedAnswer=maskedAnswer.concat("*");
		}
		return maskedAnswer;
	}	
}
