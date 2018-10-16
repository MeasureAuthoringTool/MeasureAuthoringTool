package mat.client.shared;

import java.util.List;

import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class InformationMessageAlert extends MessageAlert implements MessageAlertInterface  {
	
	@Override
	public void createAlert (String errorMessage) {
		clear();
		createInfoAlert(errorMessage);
		setVisible(true);
	}
	
	@Override
	public void createAlert (List<String> errorMessage) {
		clear();
		createInfoAlert(errorMessage);
		setVisible(true);
	}
		
	public InformationMessageAlert(String errorMessage) {
		createInfoAlert(errorMessage);
	}
		
	public InformationMessageAlert() {
		createInfoAlert("");
	}

	public void createInfoAlert(String errorMessage) {
		setType(AlertType.INFO);
		setMessage(getMsgPanel(IconType.INFO_CIRCLE, errorMessage));
		getElement().setAttribute("id", "InfoMessageAlert");
		setFocus();
	}
	
	public void createInfoAlert(List<String> errorMessage) {
		setType(AlertType.INFO);
		for(int i=0;i< errorMessage.size();i++){
			if(i==0) {
				setMessage(getMsgPanel(IconType.INFO_CIRCLE, errorMessage.get(i)));
			} else {
				setMessage(getMsgPanel(errorMessage.get(i)));
			}
			
		}
		getElement().setAttribute("id", "InfoMessageAlert");
		setFocus();
	}
		
}
