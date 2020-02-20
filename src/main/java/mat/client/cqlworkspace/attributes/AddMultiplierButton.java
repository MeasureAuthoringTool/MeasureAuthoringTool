package mat.client.cqlworkspace.attributes;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class AddMultiplierButton extends GenericMultiplierToolbarButton {

    public AddMultiplierButton(String sectionName) {
        super("Add", sectionName);
        setIcon();
    }

    @Override
    public void setIcon() {
        super.setIcon(IconType.PLUS_SQUARE);
    }

}
