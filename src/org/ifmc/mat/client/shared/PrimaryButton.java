package org.ifmc.mat.client.shared;

import com.google.gwt.user.client.ui.Button;

public class PrimaryButton extends Button {
	public PrimaryButton(String label) {
		super(label);
		setStylePrimaryName("primaryButton");
	}
	public PrimaryButton() {
		super();
		setStylePrimaryName("primaryButton");
	}
}
