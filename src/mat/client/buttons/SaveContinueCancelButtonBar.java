package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import com.google.gwt.user.client.ui.Composite;

public class SaveContinueCancelButtonBar extends Composite {

	private Button saveButton;
	private Button cancelButton;
	private ButtonToolBar buttonToolBar;

	public SaveContinueCancelButtonBar(String uniqueSection) {
		buttonToolBar = new ButtonToolBar();
		saveButton = new BlueButton(uniqueSection, "Save and Continue");
		cancelButton = new RedButton(uniqueSection, "Cancel");
		
		buttonToolBar.add(saveButton);
		buttonToolBar.add(cancelButton);
		
		initWidget(buttonToolBar);
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}
}
