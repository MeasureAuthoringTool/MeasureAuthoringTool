package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;

import com.google.gwt.user.client.ui.Composite;

public class BackSaveCancelButtonBar extends Composite {

	private Button saveButton;
	private Button cancelButton;
	private Button backButton;
	private ButtonToolBar buttonToolBar;

	public BackSaveCancelButtonBar(String uniqueSection) {
		buttonToolBar = new ButtonToolBar();
		saveButton = new BlueButton(uniqueSection, "Save and Continue");
		cancelButton = new RedButton(uniqueSection, "Cancel");
		backButton = new BlueButton(uniqueSection, "Back");
		
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
