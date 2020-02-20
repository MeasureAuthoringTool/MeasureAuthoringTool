package mat.client.cqlworkspace.attributes;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class RemoveMultiplierButton extends GenericMultiplierToolbarButton {

    public RemoveMultiplierButton(String sectionName) {
        super("Remove", sectionName);
        setIcon();
    }

    @Override
    public void setIcon() {
        super.setIcon(IconType.MINUS_SQUARE);
    }

}
