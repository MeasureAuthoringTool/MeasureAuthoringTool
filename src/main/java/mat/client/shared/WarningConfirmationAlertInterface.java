package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;

/**
 * The Interface WarningAlertInterface.
 */
public interface WarningConfirmationAlertInterface {

    /**
     * Clear the alert (messages/buttons).
     */
    void clearAlert();

    /**
     * Create the alert (messages/buttons).
     */
    void createAlert();


    /**
     * Gets the Yes Button.
     */
    Button getWarningConfirmationYesButton();

    /**
     * Gets the No Button.
     */
    Button getWarningConfirmationNoButton();


}
