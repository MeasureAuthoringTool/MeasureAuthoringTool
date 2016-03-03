package mat.client.shared;

import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class ErrorMessageAlert extends MessageAlert implements MessageAlertInterface  {
	
	@Override
	public void createAlert (String errorMessage) {
		clear();
		createErrorAlert(errorMessage);
		setVisible(true);
	}
		
	public ErrorMessageAlert(String errorMessage) {
		createErrorAlert(errorMessage);
	}
		
	public ErrorMessageAlert() {
		createErrorAlert("");
	}

	public void createErrorAlert(String errorMessage) {
		setType(AlertType.DANGER);
		setMessage(getMsgPanel(IconType.EXCLAMATION_CIRCLE, errorMessage));
		setFocus();
	}
		
}
