package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;

/**
 * The Interface WarningAlertInterface.
 */
public interface WarningConfirmationAlertInterface {
	
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
	public Button getWarningConfirmationYesButton();
	
	/**
	 * Gets the No Button.
	 * 
	 */
	public Button getWarningConfirmationNoButton();
	

}
