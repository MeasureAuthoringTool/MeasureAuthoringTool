package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class ContinueButton extends Button {
	
	public ContinueButton(String sectionName) {
		super();
		super.setType(ButtonType.PRIMARY);
		super.setTitle("Continue");
		super.setText("Continue");
		super.setId("ContinueButton_"+sectionName);
	}

}
