package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * The Class SaveCompleteCancelButtonBar.
 */
public class SaveCompleteCancelButtonBar extends Composite {
	
	/** The save complete button. */
	private Button saveCompleteButton = new PrimaryButton("Save As Complete","primaryButton");
	
	/** The save button. */
	private Button saveButton = new PrimaryButton("Save As Draft","primaryButton");
	
	/** The cancel button. */
	private Button cancelButton = new SecondaryButton("Cancel");
	
	/**
	 * Instantiates a new save complete cancel button bar.
	 */
	public SaveCompleteCancelButtonBar(){
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setId("buttonLayout_HorizontalPanel");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		saveCompleteButton.getElement().setId("saveCompleteButton_Button");
		saveButton.getElement().setId("saveButton_Button");
		cancelButton.getElement().setId("cancelButton_Button");
		buttonLayout.add(saveButton);
		buttonLayout.add(saveCompleteButton);
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
	
	/**
	 * Gets the save complete button.
	 * 
	 * @return the save complete button
	 */
	public Button getSaveCompleteButton() {
		return saveCompleteButton;
	}
}
