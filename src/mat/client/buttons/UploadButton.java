package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class UploadButton extends BlueButton{

	public UploadButton(String sectionName){
		super(sectionName, "Upload");
		super.setIcon(IconType.UPLOAD);
	}
}
