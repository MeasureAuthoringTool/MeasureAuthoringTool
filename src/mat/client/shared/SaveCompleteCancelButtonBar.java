package mat.client.shared;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class SaveCompleteCancelButtonBar extends Composite {
	private Button saveCompleteButton = new PrimaryButton("Save As Complete","primaryButton");
	private Button saveButton = new PrimaryButton("Save As Draft","primaryButton");
	private Button cancelButton = new SecondaryButton("Cancel");
	
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
	
	public Button getSaveButton() {
		return saveButton;
	}
	public Button getCancelButton() {
		return cancelButton;
	}
	public Button getSaveCompleteButton() {
		return saveCompleteButton;
	}
}
