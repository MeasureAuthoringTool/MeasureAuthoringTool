package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;

/**
 * The Class PrimaryButton.
 */
public class PrimaryButton extends Button {
	
	/**
	 * Instantiates a new primary button.
	 * 
	 * @param label
	 *            the label
	 */
	public PrimaryButton(String label) {
		super(label);
		setStylePrimaryName("primaryGreyButton");
		getElement().setTitle(label);
	}
	
	/**
	 * Instantiates a new primary button.
	 */
	public PrimaryButton() {
		super();
		setStylePrimaryName("primaryGreyButton");
	}
	
	/**
	 * Instantiates a new primary button.
	 * 
	 * @param label
	 *            the label
	 * @param style
	 *            the style
	 */
	public PrimaryButton(String label,String style) {
		super(label);
		setStylePrimaryName(style);
		getElement().setTitle(label);
	}
}
