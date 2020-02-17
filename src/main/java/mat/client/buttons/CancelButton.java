package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class CancelButton extends Button {

    public CancelButton(String sectionName) {
        super();
        super.setType(ButtonType.DANGER);
        super.setTitle("Cancel");
        super.setText("Cancel");
        super.setId("CancelButton_" + sectionName);
    }
}
