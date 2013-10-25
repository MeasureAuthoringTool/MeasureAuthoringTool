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

/**
 * The Class ChangePasswordView.
 */
public class ChangePasswordView implements ChangePasswordPresenter.Display {
	
	/** The main panel. */
	private Panel mainPanel;
	
	/** The buttons. */
	private SaveCancelButtonBar buttons = new SaveCancelButtonBar();
	
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
