package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;

/**
 * The Interface WarningAlertInterface.
 */
public interface WarningAlertInterface {
	
	/**
	 * Clear the alert (messages/buttons).
	 */
	public void clearAlert();
	
	/**
	 * Create the alert (messages/buttons).
	 */	
	public void createAlert();
	
	
	/**
	 * Gets the Yes Button.
	 * 
	 */
	public Button getYesButton();
	
	/**
	 * Gets the No Button.
	 * 
	 */
	public Button getNoButton();
	
	/**
	 * Sets the focus.
	 */
	//public void setFocus();
	
}
