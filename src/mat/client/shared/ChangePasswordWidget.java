package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * The Class ChangePasswordWidget.
 */
public class ChangePasswordWidget extends Composite {
	
	/** The password. */
	private PasswordTextBox password;
	
	/** The confirm password. */
	private PasswordTextBox confirmPassword;
	
	/**
	 * Instantiates a new change password widget.
	 */
	public ChangePasswordWidget() {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.getElement().setId("mainPanel_FlowPanel");
		
		password = new PasswordTextBox();
		password.getElement().setId("password_PasswordTextBox");
		password.setTitle("Enter New Password");
		password.getElement().setAttribute("aria-describedby", "passwordRulesLabel descLabel b1Label b2Label b3Label b4Label");
		mainPanel.add(LabelBuilder.buildRequiredLabel(password, "New Password"));
		mainPanel.add(password);
		mainPanel.add(new SpacerWidget());
		
		confirmPassword = new PasswordTextBox();
		confirmPassword.getElement().setId("confirmPassword_PasswordTextBox");
		confirmPassword.setTitle("Enter New Password again to confirm");
		mainPanel.add(LabelBuilder.buildRequiredLabel(confirmPassword, "Confirm New Password"));
		mainPanel.add(confirmPassword);
		mainPanel.add(new SpacerWidget());
		
		password.setWidth("100px");
		confirmPassword.setWidth("100px");
		
		initWidget(mainPanel);
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public PasswordTextBox getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(PasswordTextBox password) {
		this.password = password;
	}

	/**
	 * Gets the confirm password.
	 * 
	 * @return the confirm password
	 */
	public PasswordTextBox getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * Sets the confirm password.
	 * 
	 * @param confirmPassword
	 *            the new confirm password
	 */
	public void setConfirmPassword(PasswordTextBox confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
