package org.ifmc.mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class ChangePasswordWidget extends Composite {
	private PasswordTextBox password;
	private PasswordTextBox confirmPassword;
	
	public ChangePasswordWidget() {
		FlowPanel mainPanel = new FlowPanel();
		
		password = new PasswordTextBox();
		mainPanel.add(LabelBuilder.buildLabel(password, "New Password"));
		mainPanel.add(password);
		mainPanel.add(new SpacerWidget());
		
		confirmPassword = new PasswordTextBox();
		mainPanel.add(LabelBuilder.buildLabel(confirmPassword, "Confirm New Password"));
		mainPanel.add(confirmPassword);
		mainPanel.add(new SpacerWidget());
		
		password.setWidth("200px");
		confirmPassword.setWidth("200px");
		
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
