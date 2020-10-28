package mat.client.buttons;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class CancelButton extends Button {

    public CancelButton() {
    }

    public CancelButton(String sectionName) {
        super();
        setAsCancel(sectionName);
    }

    public void setAsCancel(String sectionName) {
        setType(ButtonType.DANGER);
        setTitle("Cancel");
        setText("Cancel");
        setId("CancelButton_" + sectionName);
    }
}
