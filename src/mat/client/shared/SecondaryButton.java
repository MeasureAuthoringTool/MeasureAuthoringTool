package mat.client.shared;

import com.google.gwt.user.client.ui.Button;

public class SecondaryButton extends Button {
	public SecondaryButton(String label) {
		super(label);
		setStylePrimaryName("secondaryButton");
	}
	public SecondaryButton() {
		super();
		setStylePrimaryName("secondaryButton");
	}
}
