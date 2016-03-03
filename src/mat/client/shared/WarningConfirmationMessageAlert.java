package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class WarningConfirmationMessageAlert extends MessageAlert implements WarningAlertInterface  {
	
	private Button yesButton = new Button();
	
	private Button noButton = new Button();
	
	public void createAlert () {
		clear();
		createWarningAlert();
		setVisible(true);
	}
		
	public WarningConfirmationMessageAlert() {
		createWarningAlert();
	}
	
	public Button getYesButton() {
		return yesButton;
	}
	
	public Button getNoButton() {
		return noButton;
	}
	
	public void createWarningAlert() {
		super.setMessage(getMsgPanel(IconType.WARNING, MatContext.get().getMessageDelegate().getSaveErrorMsg()));
		createButtons();
		setFocus();
	}
		
	private void createButtons() {
		setType(AlertType.WARNING);
		add(new SpacerWidget());
		
		yesButton.setType(ButtonType.PRIMARY);
		yesButton.setSize(ButtonSize.EXTRA_SMALL);
		yesButton.setTitle("Yes");
		yesButton.setText("Yes");
		yesButton.setId("Clear_Yes_Button");
		
		noButton.setType(ButtonType.PRIMARY);
		noButton.setSize(ButtonSize.EXTRA_SMALL);
		noButton.setMarginLeft(15);
		noButton.setTitle("No");
		noButton.setText("No");
		noButton.setId("Clear_No_Button");

		add(new SpacerWidget());
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		buttonToolBar.add(yesButton);
		buttonToolBar.add(noButton);
		add(buttonToolBar);
	}

}
