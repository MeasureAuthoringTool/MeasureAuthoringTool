package mat.client.shared;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class SaveCancelButtonBar extends Composite {

	private Button saveButton = new PrimaryButton("Save","primaryButton");
	private Button cancelButton = new SecondaryButton("Cancel");
	
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
	
	public Button getSaveButton() {
		return saveButton;
	}
	public Button getCancelButton() {
		return cancelButton;
	}
}
