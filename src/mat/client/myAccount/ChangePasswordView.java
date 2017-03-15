package mat.client.myAccount;

import mat.client.shared.ChangePasswordWidget;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.PasswordEditInfoWidget;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.PasswordRules;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ChangePasswordView.
 */
public class ChangePasswordView implements ChangePasswordPresenter.Display {
	
	/** The main panel. */
	private Panel mainPanel;
	
	/** The buttons. */
	private SaveCancelButtonBar buttons = new SaveCancelButtonBar("changePassword");
	
	/** The password widget. */
	private ChangePasswordWidget passwordWidget = new ChangePasswordWidget();
	
	/** The password edit info widget. */
	protected PasswordEditInfoWidget passwordEditInfoWidget = new  PasswordEditInfoWidget();
	
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The success messages. */
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
	/** The heading panel. */
	private ContentWithHeadingWidget headingPanel;
	
	/**
	 * Instantiates a new change password view.
	 */
	public ChangePasswordView() {
		HorizontalPanel horzPanel = new HorizontalPanel();
		VerticalPanel passwordPanel = new VerticalPanel();
		mainPanel = new FlowPanel();
		mainPanel.addStyleName("leftAligned");
		
		Label required = new Label("All fields are required");
		errorMessages.setStyleName("valueSetMarginLeft_7px");
		successMessages.setStyleName("valueSetMarginLeft_7px");
		mainPanel.add(errorMessages);
		mainPanel.add(successMessages);
		
		passwordPanel.add(required);
		passwordPanel.add(new SpacerWidget());
		passwordPanel.add(passwordWidget);
		passwordPanel.add(new SpacerWidget());
		
		passwordPanel.add(passwordEditInfoWidget);
		passwordPanel.add(new SpacerWidget());
		
		buttons.getCancelButton().setText("Undo");
		buttons.getCancelButton().setTitle("Undo");
		buttons.getSaveButton().setTitle("Save");
		buttons.addStyleName("floatLeft");
		//passwordPanel.add(buttons);
		passwordPanel.addStyleName("floatLeft");
		horzPanel.add(passwordPanel);
		
		PasswordRules rules = new PasswordRules();
		rules.addStyleName("leftAligned_small_text");
		rules.addStyleName("floatLeft");
		//rules.addStyleName("leftAligned");
		rules.addStyleName("myAccountPasswordRules");
		
		horzPanel.add(rules);
		mainPanel.add(horzPanel);
		mainPanel.add(buttons);
		SimplePanel clearPanel = new SimplePanel();
		clearPanel.addStyleName("clearBoth");
		mainPanel.add(clearPanel);
		mainPanel.addStyleName("contentPanel");
		headingPanel = new ContentWithHeadingWidget(mainPanel, "Change Password","ChangePassword");
		
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.ChangePasswordPresenter.Display#getPassword()
	 */
	@Override
	public HasValue<String> getPassword() {
		return passwordWidget.getPassword();
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.ChangePasswordPresenter.Display#getConfirmPassword()
	 */
	@Override
	public HasValue<String> getConfirmPassword() {
		return passwordWidget.getConfirmPassword();
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.ChangePasswordPresenter.Display#getCurrentPassword()
	 */
	@Override
	public HasValue<String> getCurrentPassword() {
		return passwordEditInfoWidget.getPassword();
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.ChangePasswordPresenter.Display#getChangePasswordWidget
	 */
	@Override
	public PasswordEditInfoWidget getPasswordEditInfoWidget() {
		return passwordEditInfoWidget;
	}

	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.ChangePasswordPresenter.Display#getSubmit()
	 */
	@Override
	public HasClickHandlers getSubmit() {
		return buttons.getSaveButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.ChangePasswordPresenter.Display#getReset()
	 */
	@Override
	public HasClickHandlers getReset() {
		return buttons.getCancelButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.ChangePasswordPresenter.Display#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return headingPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.ChangePasswordPresenter.Display#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.ChangePasswordPresenter.Display#getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	
}
