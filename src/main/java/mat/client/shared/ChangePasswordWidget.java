package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.constants.InputType;


public class ChangePasswordWidget extends Composite {
	
	private Input password = new Input(InputType.PASSWORD);
	
	private Input confirmPassword = new Input(InputType.PASSWORD);
	
	private FormGroup passwordGroup = new FormGroup();
	
	private FormGroup confirmPasswordGroup = new FormGroup();
	
	public ChangePasswordWidget() {
		FormLabel label = new FormLabel();
		label.setText("New Password");
		label.setTitle("New Password");
		label.setId("newPasswordLabel");
		label.setFor("newPasswordInput");
		label.setShowRequiredIndicator(true);
		
		password.setWidth("200px");
		password.setId("newPasswordInput");
		password.setTitle("Enter New Password Required");
		password.setPlaceholder("Enter New Password here.");
		password.getElement().setAttribute("aria-describedby", "passwordRulesLabel descLabel b1Label b2Label b3Label b4Label");
		
		passwordGroup.add(label);
		passwordGroup.add(password);
		
		FormLabel confirmPwdlabel = new FormLabel();
		confirmPwdlabel.setText("Confirm New Password");
		confirmPwdlabel.setTitle("Confirm New Password");
		confirmPwdlabel.setId("confirmPasswordLabel");
		confirmPwdlabel.setFor("confirmPasswordInput");
		confirmPwdlabel.setShowRequiredIndicator(true);
		
		confirmPassword.setWidth("200px");
		confirmPassword.setId("confirmPasswordInput");
		confirmPassword.setTitle("Enter New Password again to confirm Required");
		confirmPassword.setPlaceholder("Confirm Password here.");
		confirmPasswordGroup.add(confirmPwdlabel);
		confirmPasswordGroup.add(confirmPassword);

		password.getElement().setId("password_PasswordTextBox");
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public Input getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(Input password) {
		this.password = password;
	}

	/**
	 * Gets the confirm password.
	 * 
	 * @return the confirm password
	 */
	public Input getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * Sets the confirm password.
	 * 
	 * @param confirmPassword
	 *            the new confirm password
	 */
	public void setConfirmPassword(Input confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public FormGroup getPasswordGroup() {
		return passwordGroup;
	}

	public FormGroup getConfirmPasswordGroup() {
		return confirmPasswordGroup;
	}
	
}
