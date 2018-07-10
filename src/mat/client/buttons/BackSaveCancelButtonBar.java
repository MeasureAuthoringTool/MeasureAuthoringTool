package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.user.client.ui.Composite;

public class BackSaveCancelButtonBar extends Composite {

	private Button saveButton;
	private Button cancelButton;
	private Button backButton;
	private ButtonToolBar buttonToolBar;

	public BackSaveCancelButtonBar(String uniqueSection) {
		buttonToolBar = new ButtonToolBar();
		saveButton = new SaveAndContinueButton(uniqueSection);
		cancelButton = new CancelButton(uniqueSection);
		cancelButton.setPull(Pull.RIGHT);
		backButton = new BackButton(uniqueSection);
		
		buttonToolBar.add(backButton);
		buttonToolBar.add(saveButton);
		buttonToolBar.add(cancelButton);
		
		initWidget(buttonToolBar);
	}
	
	public Button getBackButton() {
		return backButton;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}
}
