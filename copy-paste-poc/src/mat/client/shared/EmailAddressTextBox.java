package mat.client.shared;

import com.google.gwt.user.client.ui.TextBox;

/**
 * The Class EmailAddressTextBox.
 */
public class EmailAddressTextBox extends TextBox {
	
	/**
	 * Instantiates a new email address text box.
	 */
	public EmailAddressTextBox() {
		super();
		setWidth("250px");
		setMaxLength(254);
	}
}
