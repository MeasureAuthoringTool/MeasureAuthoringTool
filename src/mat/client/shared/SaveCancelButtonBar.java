package mat.client.shared;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * The Class SaveCancelButtonBar.
 */
public class SaveCancelButtonBar extends Composite {

	/** The save button. */
	private Button saveButton = new PrimaryButton("Save","primaryButton");
	
	/** The cancel button. */
	private Button cancelButton = new SecondaryButton("Cancel");
	
	/**
	 * Instantiates a new save cancel button bar.
	 */
	public SaveCancelButtonBar() {
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setId("buttonLayout_HorizontalPanel");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		saveButton.getElement().setId("saveButton_Button");
		cancelButton.getElement().setId("cancelButton_Button");
		saveButton.setTitle("Save");
		cancelButton.setTitle("Cancel");
		buttonLayout.add(saveButton);
		buttonLayout.add(cancelButton);
		
		initWidget(buttonLayout);
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
