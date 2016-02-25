package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.user.client.ui.HTML;

public class WarningMessageAlert extends MessageAlert {
	
	private Button yesButton = new Button();
	
	private Button noButton = new Button();
	
	public WarningMessageAlert() {
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
	
	public void turnOnWarningAlert () {
		clear();
		createWarningAlert();
		setVisible(true);
	}
	
	public void turnOffWarningAlert () {
		clear();
		setVisible(false);
	}
	
	/**
	 * Gets the msg panel.
	 *
	 * @param iconType the icon type
	 * @param message the message
	 * @return the msg panel
	 */
	private HTML getMsgPanel(IconType iconType, String message) {
		Icon checkIcon = new Icon(iconType);
		HTML msgHtml = new HTML(checkIcon + " <b>"+ message +"</b>");
		return msgHtml;
	}

}
