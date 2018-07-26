package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.ButtonSize;

public class BlueButtonSmall extends BlueButton{

	public BlueButtonSmall(String sectionName, String title) {
		super(title+"_"+sectionName, title);
		super.setSize(ButtonSize.SMALL);
	}
}