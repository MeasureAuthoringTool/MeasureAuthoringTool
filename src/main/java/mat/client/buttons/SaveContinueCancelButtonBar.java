package mat.client.buttons;

import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;

public class SaveContinueCancelButtonBar extends Composite {

	private Button saveButton;
	private Button cancelButton;
	private ButtonToolBar buttonToolBar;

	public SaveContinueCancelButtonBar(String uniqueSection) {
		buttonToolBar = new ButtonToolBar();
		saveButton = new SaveAndContinueButton(uniqueSection);
		cancelButton = new CancelButton(uniqueSection);
		
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
