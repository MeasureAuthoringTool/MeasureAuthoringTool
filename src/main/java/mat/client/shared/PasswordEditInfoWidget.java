package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.constants.InputType;

public class PasswordEditInfoWidget extends Composite {
	

	private Input password = new Input(InputType.PASSWORD);
	FormGroup passwordExistingGroup = new FormGroup();
	
	public PasswordEditInfoWidget() {
		
		
		FormLabel label = new FormLabel();
		label.setText("Enter existing password to confirm changes");
		label.setTitle("Enter existing password to confirm changes");
		label.setId("existingPasswordLabel");
		label.setFor("exisitingPasswordInput");
		label.setShowRequiredIndicator(true);
		
		password.setWidth("200px");
		password.setTitle("Enter existing password to confirm changes Required");
		password.setId("exisitingPasswordInput");
		passwordExistingGroup.add(label);
		passwordExistingGroup.add(password);
		
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
		this.password = password ;
	}
	
	public FormGroup getPasswordExistingGroup() {
		return passwordExistingGroup;
	}
	
}
