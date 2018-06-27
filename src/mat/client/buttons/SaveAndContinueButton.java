package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class SaveAndContinueButton extends Button {
	
	public SaveAndContinueButton(String sectionName) {
		super();
		super.setId("SaveAndContinueButton_"+sectionName);
		super.setType(ButtonType.PRIMARY);
		super.setTitle("Save and Continue");
		super.setText("Save and Continue");
	}

}
