package org.ifmc.mat.client.shared;

import com.google.gwt.user.client.ui.TextBox;

public class EmailAddressTextBox extends TextBox {
	public EmailAddressTextBox() {
		super();
		setWidth("250px");
		setMaxLength(254);
	}
}
