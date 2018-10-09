package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class BackButton extends Button {
	public BackButton(String sectionName) {
		super();
		super.setId("BackButton_"+sectionName);
		super.setType(ButtonType.PRIMARY);
		super.setTitle("Go Back");
		super.setText("Go Back");
	}
}
