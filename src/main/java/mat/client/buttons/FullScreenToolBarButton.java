package mat.client.buttons;

import org.gwtbootstrap3.client.ui.constants.IconType;

public class FullScreenToolBarButton extends GenericToolbarButton {

    public FullScreenToolBarButton(String sectionName) {
        super("", sectionName);
        setTitle("Full Screen");
        setIcon();
        setSize();
    }

    @Override
    public void setIcon() {
        super.setIcon(IconType.ARROWS);
    }

    @Override
    public void setSize() {
        super.setSize("70px", "30px");
    }

}
