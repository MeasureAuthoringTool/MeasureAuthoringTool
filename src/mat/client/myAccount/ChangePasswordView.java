package mat.client.myAccount;

import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Input;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.ChangePasswordWidget;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.PasswordEditInfoWidget;
import mat.client.shared.PasswordRules;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;

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
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The success messages. */
	private MessageAlert successMessages = new SuccessMessageAlert();
	
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
		
		Form form = new Form();
		FieldSet fieldSet = new FieldSet();
		
		fieldSet.add(passwordWidget.getPasswordGroup());
		fieldSet.add(passwordWidget.getConfirmPasswordGroup());
		fieldSet.add(passwordEditInfoWidget.getPasswordExistingGroup());
		form.add(fieldSet);
		Label required = new Label("All fields are required");
		//errorMessages.setStyleName("valueSetMarginLeft_7px");
		//successMessages.setStyleName("valueSetMarginLeft_7px");
		
		mainPanel.add(errorMessages);
		mainPanel.add(successMessages);
		passwordPanel.add(required);
		passwordPanel.add(new SpacerWidget());
		
		passwordPanel.add(form);
		
		passwordPanel.add(new SpacerWidget());
		
		/*passwordPanel.add(passwordEditInfoWidget.getPasswordExistingGroup());
		passwordPanel.add(new SpacerWidget());*/
		
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
	public Input getPassword() {
		return passwordWidget.getPassword();
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.ChangePasswordPresenter.Display#getConfirmPassword()
	 */
	@Override
	public Input getConfirmPassword() {
		return passwordWidget.getConfirmPassword();
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.ChangePasswordPresenter.Display#getCurrentPassword()
	 */
	@Override
	public Input getCurrentPassword() {
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
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.ChangePasswordPresenter.Display#getSuccessMessageDisplay()
	 */
	@Override
	public MessageAlert getSuccessMessageDisplay() {
		return successMessages;
	}
	
}
