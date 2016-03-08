package mat.client.shared;

import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class SuccessMessageAlert extends MessageAlert  {
	
	@Override
	public void createAlert (String successMessage) {
		clear();
		createSuccessAlert(successMessage);
		setVisible(true);
	}
	
	public SuccessMessageAlert(String successMessage) {
		createSuccessAlert(successMessage);
	}
		
	public SuccessMessageAlert() {
		createSuccessAlert("");
	}

	public void createSuccessAlert(String successMessage) {
		setType(AlertType.SUCCESS);
		setMessage(getMsgPanel(IconType.CHECK_CIRCLE, successMessage));
		setFocus();
	}
		
}
