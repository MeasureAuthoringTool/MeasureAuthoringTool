package mat.client.shared;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

public class InvalidEMeasureIdGeneratorDialogBox {

    private Button confirmButton;
    private Modal panel;
    private FocusPanel focusPanel = new FocusPanel();
    private ClickHandler handler;

    public void displayInvalidEMeasureIdMessage(String message) {
        focusPanel.clear();
        panel = new Modal();
        confirmButton = new Button("Continue");
        ModalBody modalBody = new ModalBody();
        ErrorMessageAlert messageAlert = new ErrorMessageAlert();

        modalBody.clear();
        messageAlert.clear();
        modalBody.remove(messageAlert);
        panel.remove(modalBody);
        panel.setTitle("Warning");
        panel.setDataKeyboard(true);
        panel.setClosable(true);
        panel.setFade(true);
        panel.setDataBackdrop(ModalBackdrop.STATIC);
        panel.setSize(ModalSize.MEDIUM);
        panel.getElement().getStyle().setZIndex(9999);
        panel.setRemoveOnHide(true);

        if(handler == null) {
            handler = MatContext.get().addClickHandlerToResetTimeoutWarning();
        }

        panel.addDomHandler(handler, ClickEvent.getType());

        messageAlert.getElement().getStyle().setMarginTop(0.0, Style.Unit.PX);
        messageAlert.getElement().getStyle().setMarginBottom(0.0, Style.Unit.PX);
        messageAlert.createAlert(message);

        ModalFooter modalFooter = new ModalFooter();

        confirmButton.setType(ButtonType.PRIMARY);
        confirmButton.setSize(ButtonSize.DEFAULT);
        confirmButton.setEnabled(true);

        modalFooter.add(confirmButton);
        VerticalPanel vp = new VerticalPanel();
        vp.add(messageAlert);
        focusPanel.add(vp);
        modalBody.add(focusPanel);
        panel.add(modalBody);

        panel.add(modalFooter);
        panel.getElement().focus();
        panel.show();
    }

    public void closeDialogBox() {
        panel.hide();
    }

    public Button getConfirmButton() {
        return confirmButton;
    }
}
