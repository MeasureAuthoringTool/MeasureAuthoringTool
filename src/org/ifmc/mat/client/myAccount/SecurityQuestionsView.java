package org.ifmc.mat.client.myAccount;

import java.util.List;

import org.ifmc.mat.client.shared.ContentWithHeadingWidget;
import org.ifmc.mat.client.shared.ErrorMessageDisplay;
import org.ifmc.mat.client.shared.ErrorMessageDisplayInterface;
import org.ifmc.mat.client.shared.NameValuePair;
import org.ifmc.mat.client.shared.SaveCancelButtonBar;
import org.ifmc.mat.client.shared.SecurityQuestionsWidget;
import org.ifmc.mat.client.shared.SpacerWidget;
import org.ifmc.mat.client.shared.SuccessMessageDisplay;
import org.ifmc.mat.client.shared.SuccessMessageDisplayInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SecurityQuestionsView implements SecurityQuestionsPresenter.Display {
	private FlowPanel container = new FlowPanel();
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	private ContentWithHeadingWidget headingPanel;
	private SecurityQuestionsWidget securityQuestionsWidget =
		new SecurityQuestionsWidget();
	private SaveCancelButtonBar buttons = new SaveCancelButtonBar();
	
	public SecurityQuestionsView() {
		Label required = new Label("All fields are required");
		container.addStyleName("leftAligned");
		container.add(required);
		container.add(new SpacerWidget());
		container.add(errorMessages);
		container.add(successMessages);
		
		container.add(securityQuestionsWidget);
		
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
	
}
