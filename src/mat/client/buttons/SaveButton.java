package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class SaveButton extends Button{

	public SaveButton(String sectionName){
		super();
		super.setTitle("Save");
		super.setText("Save");
		super.setType(ButtonType.PRIMARY);
		super.setIcon(IconType.SAVE);
	}
}
