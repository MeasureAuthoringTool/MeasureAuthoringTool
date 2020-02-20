package mat.client.cqlworkspace.attributes;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;


public abstract class GenericMultiplierToolbarButton extends Button {

    public GenericMultiplierToolbarButton(String name, String sectionName) {
        super.setType(ButtonType.LINK);
        super.getElement().setId((name.toLowerCase().replace(" ", "")) + "Button_" + sectionName);
        super.setSize("40px", "40px");
        super.setPaddingLeft(5);
        super.setPaddingRight(5);
        super.setTitle(name);
        super.setIconSize(IconSize.TIMES2);
        super.setColor("#0964A2");
        super.getElement().setAttribute("aria-label", name);
    }

    public abstract void setIcon();

}
