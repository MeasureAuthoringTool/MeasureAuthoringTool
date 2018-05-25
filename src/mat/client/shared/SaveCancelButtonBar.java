package mat.client.shared;


import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

import com.google.gwt.user.client.ui.Composite;

/**
 * The Class SaveCancelButtonBar.
 */
public class SaveCancelButtonBar extends Composite {

	/** The save button. */
	private Button saveButton = new Button();
	
	/** The cancel button. */
	private Button cancelButton = new Button();
	
	/**
	 * Instantiates a new save cancel button bar.
	 */
	public SaveCancelButtonBar(String uniqueSection) {
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		saveButton.setId("ButtonPanel_SaveAndContinueButton_"+uniqueSection);
		saveButton.setType(ButtonType.PRIMARY);
		saveButton.setTitle("Save and Continue");
		saveButton.setText("Save and Continue");
		cancelButton.setType(ButtonType.DANGER);
		cancelButton.setTitle("Cancel");
		cancelButton.setText("Cancel");
		cancelButton.setId("ButtonPanel_CancelButton_"+uniqueSection);
		buttonToolBar.add(saveButton);
		buttonToolBar.add(cancelButton);
		
		
		initWidget(buttonToolBar);
	}
	
	/**
	 * Gets the save button.
	 * 
	 * @return the save button
	 */
	public Button getSaveButton() {
		return saveButton;
	}
	
	/**
	 * Gets the cancel button.
	 * 
	 * @return the cancel button
	 */
	public Button getCancelButton() {
		return cancelButton;
	}
}
