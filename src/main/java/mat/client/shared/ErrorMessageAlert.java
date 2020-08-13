package mat.client.shared;

import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.List;

public class ErrorMessageAlert extends MessageAlert implements MessageAlertInterface  {
	
	@Override
	public void createAlert (String errorMessage) {
		clear();
		createErrorAlert(errorMessage);
		setVisible(true);
	}
	
	@Override
	public void createAlert (List<String> errorMessage) {
		clear();
		createErrorAlert(errorMessage);
		setVisible(true);
	}
		
	public void createMultiLineAlert (List<String> errorMessage) {
		clear();
		createErrorMultiLineAlert(errorMessage);
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
		getElement().setAttribute("id", "ErrorMessageAlert");
		setFocus();
	}
	
	public void createErrorAlert(List<String> errorMessage) {
		setType(AlertType.DANGER);
		for(int i=0;i< errorMessage.size();i++){
			setMessage(getMsgPanel(IconType.EXCLAMATION_CIRCLE, errorMessage.get(i)));
		}
		getElement().setAttribute("id", "ErrorMessageAlert");
		setFocus();
	}
		
	public void createErrorMultiLineAlert(List<String> errorMessage) {
		setType(AlertType.DANGER);
		for(int i=0;i< errorMessage.size();i++){
			if (i==0)
				setMessage(getMsgPanel(IconType.EXCLAMATION_CIRCLE, errorMessage.get(i)));
			else if (i==1)
				setMessage(getMsgPanel(null, errorMessage.get(i)));
			else
				setMessage(getMsgPanel(errorMessage.get(i)));
		}
		getElement().setAttribute("id", "ErrorMessageAlert");
		setFocus();
	}
		
}
