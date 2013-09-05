package mat.client.myAccount;

import mat.client.shared.ChangePasswordWidget;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.PasswordEditInfoWidget;
import mat.client.shared.PasswordRules;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ChangePasswordView implements ChangePasswordPresenter.Display {
	private Panel mainPanel;
	private SaveCancelButtonBar buttons = new SaveCancelButtonBar();
	private ChangePasswordWidget passwordWidget = new ChangePasswordWidget();
	protected PasswordEditInfoWidget passwordEditInfoWidget = new  PasswordEditInfoWidget();
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	private ContentWithHeadingWidget headingPanel;
	
	public ChangePasswordView() {
		FlowPanel hPanel = new FlowPanel();
		
		mainPanel = new FlowPanel();
		mainPanel.addStyleName("leftAligned");
		
		Label required = new Label("All fields are required");
		mainPanel.add(required);
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(errorMessages);
		mainPanel.add(successMessages);
		
		mainPanel.add(passwordWidget);
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(passwordEditInfoWidget);
		mainPanel.add(new SpacerWidget());
		
		buttons.getCancelButton().setText("Undo");
		buttons.getCancelButton().setTitle("Undo");
		buttons.getSaveButton().setTitle("Save");
		mainPanel.add(buttons);
		mainPanel.addStyleName("floatLeft");
		hPanel.add(mainPanel);
		
		PasswordRules rules = new PasswordRules();
		rules.addStyleName("floatLeft");
		rules.addStyleName("leftAligned");
		rules.addStyleName("myAccountPasswordRules");
		hPanel.add(rules);
		
		SimplePanel clearPanel = new SimplePanel();
		clearPanel.addStyleName("clearBoth");
		hPanel.add(clearPanel);
		hPanel.addStyleName("contentPanel");
		headingPanel = new ContentWithHeadingWidget(hPanel, "Change Password","ChangePassword");
		
	}

	@Override
	public HasValue<String> getPassword() {
		return passwordWidget.getPassword();
	}

	@Override
	public HasValue<String> getConfirmPassword() {
		return passwordWidget.getConfirmPassword();
	}

	@Override
	public HasValue<String> getCurrentPassword() {
		return passwordEditInfoWidget.getPassword();
	}
	
	@Override
	public HasClickHandlers getSubmit() {
		return buttons.getSaveButton();
	}

	@Override
	public HasClickHandlers getReset() {
		return buttons.getCancelButton();
	}

	@Override
	public Widget asWidget() {
		return headingPanel;
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
