package mat.client.bonnie;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;

import com.google.gwt.user.client.ui.Composite;

import mat.client.buttons.RedButton;
import mat.client.buttons.UploadButton;

public class BonnieUploadCancelButtonBar extends Composite {
	private Button uploadButton;
	private Button cancelButton;
	private ButtonToolBar buttonToolBar;

	public BonnieUploadCancelButtonBar(String uniqueSection) {
		buttonToolBar = new ButtonToolBar();
		uploadButton = new UploadButton(uniqueSection);
		uploadButton.setTitle("Upload to Bonnie");
		cancelButton = new RedButton(uniqueSection, "Cancel");
		
		buttonToolBar.add(uploadButton);
		buttonToolBar.add(cancelButton);
		
		initWidget(buttonToolBar);
	}
	
	public Button getUploadButton() {
		return uploadButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}
}
