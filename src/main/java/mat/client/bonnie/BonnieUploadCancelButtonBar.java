package mat.client.bonnie;

import com.google.gwt.user.client.ui.Composite;
import mat.client.buttons.CancelButton;
import mat.client.buttons.UploadButton;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;

public class BonnieUploadCancelButtonBar extends Composite {
	private Button uploadButton;
	private Button cancelButton;
	private ButtonToolBar buttonToolBar;

	public BonnieUploadCancelButtonBar(String uniqueSection) {
		buttonToolBar = new ButtonToolBar();
		uploadButton = new UploadButton(uniqueSection);
		uploadButton.setTitle("Upload to Bonnie");
		cancelButton = new CancelButton(uniqueSection);
		
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
