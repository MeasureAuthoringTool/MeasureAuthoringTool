package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class ExpressionBuilderButton extends GenericToolbarButton {
	
	public ExpressionBuilderButton(String sectionName) {
		super("Expression Builder", sectionName);
		setIcon();
		setSize();
	}
	
	@Override
	public void setIcon() {
		super.setIcon(IconType.WRENCH);
	}

	@Override
	public void setSize() {
		super.setSize("150px", "30px");
	}
}
