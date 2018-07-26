package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import mat.client.buttons.BlueButtonSmall;

public class WarningConfirmationMessageAlert extends MessageAlert implements WarningConfirmationAlertInterface  {
	
	private Button yesButton = new BlueButtonSmall("warningConfirmationMessageAlert", "Yes");
	
	private Button noButton = new BlueButtonSmall("warningConfirmationMessageAlert", "Yes");
	
	@Override
	public void createAlert () {
		clear();
		createWarningAlert();
		setVisible(true);
	}
		
	public WarningConfirmationMessageAlert() {
		createWarningAlert();
	}
	
	@Override
	public Button getWarningConfirmationYesButton() {
		return yesButton;
	}
	
	@Override
	public Button getWarningConfirmationNoButton() {
		return noButton;
	}
	
	public void createWarningAlert() {
		clear();
		setStyleName("alert warning-alert");
		getElement().setAttribute("id", "WarningConfirmationMessageAlert");
		super.setMessage(getMsgPanel(IconType.WARNING, MatContext.get().getMessageDelegate().getSaveErrorMsg()));
		createButtons();
		setFocus();
		setVisible(true);
	}
	
	public void createConfirmationAlert(String message) {
		clear();
		setStyleName("alert alert-success");
		getElement().setAttribute("id", "ConfirmationMessageAlert");
		super.setMessage(getMsgPanel(IconType.BELL, message));
		createButtons();
		setFocus();
		setVisible(true);
	}
		
	private void createButtons() {
		add(new SpacerWidget());
		
		yesButton.setSize(ButtonSize.EXTRA_SMALL);
		
		noButton.setSize(ButtonSize.EXTRA_SMALL);
		noButton.setMarginLeft(15);

		add(new SpacerWidget());
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		buttonToolBar.add(yesButton);
		buttonToolBar.add(noButton);
		add(buttonToolBar);
	}


}
