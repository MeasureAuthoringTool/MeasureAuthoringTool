package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class NoButton extends Button{

	public NoButton(String sectionName) {
		super();
		super.setType(ButtonType.DANGER);
		super.setTitle("No");
		super.setText("No");
		super.setSize(ButtonSize.SMALL);
		super.setId("No_"+sectionName);
	}
}
