package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class InAppHelpButton extends GenericToolbarButton{

	public InAppHelpButton() {
		super("", "InAppHelp");
		setIcon();
		setSize();
		super.setTitle("Need Help?");
		super.getElement().setAttribute("aria-label", "Need Help");
		super.setToggleCaret(false);
	}

	@Override
	public void setIcon() {
		super.setIcon(IconType.QUESTION_CIRCLE);
	}

	@Override
	public void setSize() {
		super.setSize("30px", "30px");
		super.setMarginTop(3);
	}

}
