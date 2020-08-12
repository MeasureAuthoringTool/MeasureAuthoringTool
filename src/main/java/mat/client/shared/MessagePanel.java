package mat.client.shared;

import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.Button;

public class MessagePanel extends VerticalPanel {
	private MessageAlert successMessageAlert = new SuccessMessageAlert();
	private MessageAlert warningMessageAlert = new WarningMessageAlert();
	private MessageAlert errorMessageAlert = new ErrorMessageAlert();
	private WarningConfirmationMessageAlert warningConfirmationMessageAlert = new WarningConfirmationMessageAlert();
	private WarningConfirmationMessageAlert globalWarningConfirmationMessageAlert = new WarningConfirmationMessageAlert();
	
	public MessagePanel() {
		add(successMessageAlert);
		add(errorMessageAlert);
		add(warningMessageAlert);
		add(warningConfirmationMessageAlert);
		add(globalWarningConfirmationMessageAlert);
		clearAlerts();
	}
	
	public MessageAlert getSuccessMessageAlert() {
		return successMessageAlert;
	}
	public void setSuccessMessageAlert(MessageAlert successMessageAlert) {
		this.successMessageAlert = successMessageAlert;
	}
	public MessageAlert getWarningMessageAlert() {
		return warningMessageAlert;
	}
	public void setWarningMessageAlert(MessageAlert warningMessageAlert) {
		this.warningMessageAlert = warningMessageAlert;
	}
	public MessageAlert getErrorMessageAlert() {
		return errorMessageAlert;
	}
	public void setErrorMessageAlert(MessageAlert errorMessageAlert) {
		this.errorMessageAlert = errorMessageAlert;
	}
	
	public WarningConfirmationMessageAlert getWarningConfirmationMessageAlert() {
		return warningConfirmationMessageAlert;
	}

	public void setWarningConfirmationMessageAlert(WarningConfirmationMessageAlert warningConfirmationMessageAlert) {
		this.warningConfirmationMessageAlert = warningConfirmationMessageAlert;
	}
	
	public WarningConfirmationMessageAlert getGlobalWarningConfirmationMessageAlert() {
		return globalWarningConfirmationMessageAlert;
	}

	public void setGlobalWarningConfirmationMessageAlert(
			WarningConfirmationMessageAlert globalWarningConfirmationMessageAlert) {
		this.globalWarningConfirmationMessageAlert = globalWarningConfirmationMessageAlert;
	}
	
	/**
	 * Gets the warning confirmation yes button.
	 *
	 * @return the warning confirmation yes button
	 */
	public Button getWarningConfirmationYesButton() {
		return warningConfirmationMessageAlert.getWarningConfirmationYesButton();
	}
	
	/**
	 * Gets the warning confirmation no button.
	 *
	 * @return the warning confirmation no button
	 */
	public Button getWarningConfirmationNoButton() {
		return warningConfirmationMessageAlert.getWarningConfirmationNoButton();
	}
	
	/**
	 * Gets the global warning confirmation yes button.
	 *
	 * @return the global warning confirmation yes button
	 */
	public Button getGlobalWarningConfirmationYesButton() {
		return globalWarningConfirmationMessageAlert.getWarningConfirmationYesButton();
	}

	/**
	 * Gets the global warning confirmation no button.
	 *
	 * @return the global warning confirmation no button
	 */
	public Button getGlobalWarningConfirmationNoButton() {
		return globalWarningConfirmationMessageAlert.getWarningConfirmationNoButton();
	}
	
	public void clearAlerts() {
		successMessageAlert.clearAlert();
		warningMessageAlert.clearAlert();
		errorMessageAlert.clearAlert();
		warningConfirmationMessageAlert.clearAlert();
		globalWarningConfirmationMessageAlert.clearAlert();
	}

}
