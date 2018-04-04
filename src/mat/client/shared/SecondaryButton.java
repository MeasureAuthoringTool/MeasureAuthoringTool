package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;

/**
 * The Class SecondaryButton.
 */
public class SecondaryButton extends Button {
	
	/**
	 * Instantiates a new secondary button.
	 * 
	 * @param label
	 *            the label
	 */
	public SecondaryButton(String label) {
		super(label);
		setStylePrimaryName("secondaryButton");
	}
	
	/**
	 * Instantiates a new secondary button.
	 */
	public SecondaryButton() {
		super();
		setStylePrimaryName("secondaryButton");
	}
}
