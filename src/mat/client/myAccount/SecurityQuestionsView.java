package mat.client.myAccount;

import java.util.List;

import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.NameValuePair;
import mat.client.shared.PasswordEditInfoWidget;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SecurityQuestionWithMaskedAnswerWidget;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

public class SecurityQuestionsView implements SecurityQuestionsPresenter.Display {
	private FlowPanel container = new FlowPanel();
	private PasswordTextBox password = new PasswordTextBox();
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	private ContentWithHeadingWidget headingPanel;
	protected SecurityQuestionWithMaskedAnswerWidget securityQuestionsWidget = new SecurityQuestionWithMaskedAnswerWidget();
	protected PasswordEditInfoWidget passwordEditInfoWidget = new PasswordEditInfoWidget();
	
	private SaveCancelButtonBar buttons = new SaveCancelButtonBar();
	
	public SecurityQuestionsView() {
		Label required = new Label("All fields are required");
		container.addStyleName("leftAligned");
		container.add(required);
		container.add(new SpacerWidget());
		container.add(errorMessages);
		container.add(successMessages);
		
		container.add(securityQuestionsWidget);
		container.add(new SpacerWidget());
		container.add (passwordEditInfoWidget);
		container.add(new SpacerWidget());
		buttons.getCancelButton().setText("Undo");
		buttons.getCancelButton().setTitle("Undo");
		buttons.getSaveButton().setTitle("Save");
		container.add(buttons);
		container.setStyleName("contentPanel");
		headingPanel = new ContentWithHeadingWidget(container, "Update Security Questions","SecurityInfo");
		
	}
	public Widget asWidget() {
		return headingPanel;
	}
	
	public PasswordTextBox getPasswordTextBox() {
		return password;
	}
	public void getPasswordTextBox(PasswordTextBox password) {
		this.password = password;
	}
	
	public void addQuestionTexts(List<NameValuePair> texts) {
		securityQuestionsWidget.getSecurityQuestion1().setDropdownOptions(texts);
		securityQuestionsWidget.getSecurityQuestion2().setDropdownOptions(texts);
		securityQuestionsWidget.getSecurityQuestion3().setDropdownOptions(texts);
	}

	@Override
	public HasValue<String> getQuestion1() {
		return securityQuestionsWidget.getSecurityQuestion1();
	}
	@Override
	public HasValue<String> getAnswer1() {
		return securityQuestionsWidget.getAnswer1();
	}
	@Override
	public HasValue<String> getQuestion2() {
		return securityQuestionsWidget.getSecurityQuestion2();
	}
	@Override
	public HasValue<String> getAnswer2() {
		return securityQuestionsWidget.getAnswer2();
	}
	@Override
	public HasValue<String> getQuestion3() {
		return securityQuestionsWidget.getSecurityQuestion3();
	}
	@Override
	public HasValue<String> getAnswer3() {
		return securityQuestionsWidget.getAnswer3();
	}
	@Override
	public HasClickHandlers getSaveButton() {
		return buttons.getSaveButton();
	}
	@Override
	public HasClickHandlers getCancelButton() {
		return buttons.getCancelButton();
	}
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	@Override
	public SecurityQuestionWithMaskedAnswerWidget getSecurityQuestionsWidget() {
		return securityQuestionsWidget;
	}
	
	
	@Override
	public PasswordEditInfoWidget getPasswordEditInfoWidget() {
		return passwordEditInfoWidget;
	}
		
	@Override
	public HasValue<String> getPassword() {
		return passwordEditInfoWidget.getPassword();
	}
	@Override
	public String getAnswerText1() {
		
		return  securityQuestionsWidget.getAnswerText1();
	}
	@Override
	public String getAnswerText2() {
		return  securityQuestionsWidget.getAnswerText2();
	}
	@Override
	public String getAnswerText3() {
		return  securityQuestionsWidget.getAnswerText3();
	}
	@Override
	public void setAnswerText1(String answerText1) {
		 securityQuestionsWidget.setAnswerText1(answerText1);
		
	}
	@Override
	public void setAnswerText2(String answerText2) {
		 securityQuestionsWidget.setAnswerText2(answerText2);
		
	}
	@Override
	public void setAnswerText3(String answerText3) {
		 securityQuestionsWidget.setAnswerText3(answerText3);
		
	}
	
}
