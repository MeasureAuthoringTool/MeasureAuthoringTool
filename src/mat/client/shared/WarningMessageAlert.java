package mat.client.shared;

import java.util.List;

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
		setStyleName("alert warning-alert");
		getElement().setAttribute("id", "WarningMessageAlert");
		setMessage(getMsgPanel(IconType.WARNING, warningMessage));
		setFocus();
	}
		
	public void createWarningMultiLineAlert(List<String> messageList) {
		clear();
		setStyleName("alert warning-alert");
		for(int i=0;i< messageList.size();i++){
			if (i==0)
				setMessage(getMsgPanel(IconType.WARNING, messageList.get(i)));
			else if (i==1)
				setMessage(getMsgPanel(null, messageList.get(i)));
			else
				setMessage(getMsgPanel(messageList.get(i)));
		}
		getElement().setAttribute("id", "WarningMessageAlert");
		setFocus();
		setVisible(true);
	}
}
