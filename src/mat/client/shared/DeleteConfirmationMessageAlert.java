package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class DeleteConfirmationMessageAlert extends MessageAlert implements WarningConfirmationAlertInterface{

	private Button yesButton = new Button();
	private Button noButton = new Button();
	
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
		
		yesButton.setType(ButtonType.PRIMARY);
		yesButton.setSize(ButtonSize.EXTRA_SMALL);
		yesButton.setTitle("Yes");
		yesButton.setText("Yes");
		yesButton.setId("Clear_Yes_Button");
		
		noButton.setType(ButtonType.PRIMARY);
		noButton.setSize(ButtonSize.EXTRA_SMALL);
		noButton.setMarginLeft(15);
		noButton.setTitle("Cancel");
		noButton.setText("Cancel");
		noButton.setId("Clear_No_Button");

		add(new SpacerWidget());
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		buttonToolBar.add(yesButton);
		buttonToolBar.add(noButton);
		add(buttonToolBar);
	}

}
