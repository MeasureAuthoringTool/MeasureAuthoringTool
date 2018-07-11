package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class YesButton extends Button{

	public YesButton(String sectionName) {
		super();
		super.setType(ButtonType.PRIMARY);
		super.setTitle("Yes");
		super.setText("Yes");
		super.setSize(ButtonSize.SMALL);
		super.setId("Yes_"+sectionName);
	}
}
