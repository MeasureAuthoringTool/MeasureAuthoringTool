package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class ChangePasswordWidget extends Composite {
	private PasswordTextBox password;
	private PasswordTextBox confirmPassword;
	
	public ChangePasswordWidget() {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.getElement().setId("mainPanel_FlowPanel");
		
		password = new PasswordTextBox();
		mainPanel.add(LabelBuilder.buildRequiredLabel(password, "New Password"));
		mainPanel.add(password);
		mainPanel.add(new SpacerWidget());
		
		confirmPassword = new PasswordTextBox();
		mainPanel.add(LabelBuilder.buildRequiredLabel(confirmPassword, "Confirm New Password"));
		mainPanel.add(confirmPassword);
		mainPanel.add(new SpacerWidget());
		
		password.setWidth("100px");
		confirmPassword.setWidth("100px");
		
		initWidget(mainPanel);
	}

	public PasswordTextBox getPassword() {
		return password;
	}

	public void setPassword(PasswordTextBox password) {
		this.password = password;
	}

	public PasswordTextBox getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(PasswordTextBox confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
