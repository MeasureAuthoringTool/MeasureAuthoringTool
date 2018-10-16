package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class UploadButton extends Button{

	public UploadButton(String sectionName){
		super();
		super.setTitle("Upload");
		super.setText("Upload");
		super.setType(ButtonType.PRIMARY);
		super.setIcon(IconType.UPLOAD);
	}
}
