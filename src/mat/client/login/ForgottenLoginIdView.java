package mat.client.login;

import mat.client.shared.EmailAddressTextBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ForgottenLoginIdView implements ForgottenLoginIdPresenter.Display {
	private Panel mainPanel;
	private TextBox email;
	
	private SaveCancelButtonBar buttonBar;
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	public ForgottenLoginIdView() {
		mainPanel = new VerticalPanel();
		mainPanel.addStyleName("centered");
		
		
		SimplePanel titleHolder = new SimplePanel();
		Label titlePanel = new Label("Request your User ID");
		titleHolder.add(titlePanel);
		titleHolder.setStylePrimaryName("loginBlueTitleHolder");
		titleHolder.setWidth("100%");
		titlePanel.setStylePrimaryName("loginBlueTitle");
		mainPanel.add(titleHolder);
		
		VerticalPanel bluePanel = new VerticalPanel();
		bluePanel.setStylePrimaryName("loginContentPanel");
		
		Label instructions = new Label("Enter the E-mail address associated with your MAT account:");
		instructions.setStylePrimaryName("loginForgotInstructions");
		bluePanel.add(instructions);
		bluePanel.add(new SpacerWidget());
		
		bluePanel.add(errorMessages);
		
		email = new EmailAddressTextBox();
		email.setTitle("Enter Email Address");
		Label emailAddressLabel = (Label)LabelBuilder.buildLabel(email, "Email Address");
		HorizontalPanel hPanel = new HorizontalPanel();
		HTML required = new HTML(RequiredIndicator.get());
		hPanel.add(emailAddressLabel);
		hPanel.add(required);
		bluePanel.add(hPanel);
		bluePanel.add(new SpacerWidget());
		bluePanel.add(email);
		bluePanel.add(new SpacerWidget());
		bluePanel.add(new SpacerWidget());
		
		buttonBar = new SaveCancelButtonBar();
		buttonBar.getSaveButton().setText("Submit");
		bluePanel.add(buttonBar);
		
		mainPanel.add(bluePanel);
		
	}	

	@Override
	public HasValue<String> getEmail() {
		return email;
	}

	@Override
	public HasClickHandlers getSubmit() {
		return buttonBar.getSaveButton();
	}

	@Override
	public HasClickHandlers getReset() {
		return buttonBar.getCancelButton();
	}


	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
}
