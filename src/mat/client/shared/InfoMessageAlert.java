package mat.client.shared;

import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class InfoMessageAlert extends MessageAlert implements MessageAlertInterface  {
	
	@Override
	public void createAlert (String infoMessage) {
		clear();
		createInfoAlert(infoMessage);
		setVisible(true);
	}
	
	
	public InfoMessageAlert(String infoMessage) {
		createInfoAlert(infoMessage);
	}
		
	public InfoMessageAlert() {
		createInfoAlert("");
	}

	public void createInfoAlert(String infoMessage) {
		setType(AlertType.INFO);
		getElement().setAttribute("id", "InfoMessageAlert");
		super.setMessage(getMsgPanel(IconType.INFO, infoMessage));
		setFocus();
	}
		
}
