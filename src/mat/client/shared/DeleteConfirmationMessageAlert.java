package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

import mat.client.buttons.BlueButton;

public class DeleteConfirmationMessageAlert extends MessageAlert implements WarningConfirmationAlertInterface{

	private Button yesButton = new BlueButton("MessageAlertDeleteConfirm", "Yes");
	private Button noButton = new BlueButton("MessageAlertDeleteConfirm", "Cancel");
	
	@Override
	public void createAlert () {
		clear();
		createWarningAlert();
		setVisible(true);
	}
		
	public DeleteConfirmationMessageAlert() {
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
		getElement().setAttribute("id", "WarningConfirmationMessageAlert");
		super.setMessage(getMsgPanel(IconType.WARNING, MatContext.get().getMessageDelegate().getDELETE_WARNING_MESSAGE() + "?"));
		createButtons();
		setFocus();
		setVisible(true);
	}
	
	public void createWarningAlert(String message) {
		clear();
		getElement().setAttribute("id", "WarningConfirmationMessageAlert");
		super.setMessage(getMsgPanel(IconType.WARNING, message));
		createButtons(); 
		setFocus();
		setVisible(true); 
	}
	
		
	private void createButtons() {
		setType(AlertType.WARNING);
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
