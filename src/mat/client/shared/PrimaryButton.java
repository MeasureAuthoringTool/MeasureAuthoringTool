package mat.client.shared;

import com.google.gwt.user.client.ui.Button;

public class PrimaryButton extends Button {
	public PrimaryButton(String label) {
		super(label);
		setStylePrimaryName("primaryGreyButton");
		getElement().setTitle(label);
	}
	public PrimaryButton() {
		super();
		setStylePrimaryName("primaryGreyButton");
	}
	public PrimaryButton(String label,String style) {
		super(label);
		setStylePrimaryName(style);
		getElement().setTitle(label);
	}
}
