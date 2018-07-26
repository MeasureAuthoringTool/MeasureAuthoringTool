package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class SaveButton extends BlueButton{

	public SaveButton(String sectionName){
		super(sectionName, "Save");
		super.setIcon(IconType.SAVE);
	}
}
