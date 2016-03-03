package mat.client.shared;

/**
 * The Interface WarningAlertInterface.
 */
public interface SuccessAlertInterface {
	
	/**
	 * Clear the alert (messages/buttons).
	 */
	public void clearAlert();
	
	/**
	 * Create the alert (messages/buttons).
	 */	
	public void createAlert(String successMessage);
	


}
