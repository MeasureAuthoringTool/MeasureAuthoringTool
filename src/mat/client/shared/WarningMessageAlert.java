package mat.client.shared;

import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class WarningMessageAlert extends MessageAlert implements MessageAlertInterface  {
	
	@Override
	public void createAlert (String warningMessage) {
		clear();
		createWarningAlert(warningMessage);
		setVisible(true);
	}
	
	
	public WarningMessageAlert(String warningMessage) {
		createWarningAlert(warningMessage);
	}
		
	public WarningMessageAlert() {
		createWarningAlert("");
	}

	public void createWarningAlert(String warningMessage) {
		setType(AlertType.SUCCESS);
		getElement().setAttribute("id", "WarningMessageAlert");
		setMessage(getMsgPanel(IconType.CHECK_CIRCLE, warningMessage));
		setFocus();
	}
		
}
