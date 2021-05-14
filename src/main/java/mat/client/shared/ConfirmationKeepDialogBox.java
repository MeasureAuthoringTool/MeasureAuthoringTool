package mat.client.shared;

import mat.client.buttons.YesButton;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class ConfirmationKeepDialogBox extends ConfirmationDialogBox {
    private final Button keepButton = new YesButton("ConfirmDialogBox");
    private ConfirmationKeepObserver confirmationKeepObserver;

    public ConfirmationKeepDialogBox(String messageText,
                                     String yesButtonText) {
        super(messageText, yesButtonText, "Cancel", null, false);
    }

    public void setConfirmationKeepObserver(ConfirmationKeepObserver confirmationKeepObserver) {
        super.setObserver(confirmationKeepObserver);
        this.confirmationKeepObserver = confirmationKeepObserver;
        keepButton.setText("Keep");
    }

    @Override
    public void show() {
        keepButton.addClickHandler(event -> {
            isClosingOnContinue = true;
            confirmationKeepObserver.onKeepButtonClicked();
        });

        super.show();
    }

    @Override
    protected ButtonToolBar createButtonToolBar() {
        ButtonToolBar buttonToolBar = super.createButtonToolBar();

        keepButton.setSize(ButtonSize.SMALL);
        keepButton.setDataDismiss(ButtonDismiss.MODAL);
        keepButton.setType(ButtonType.INFO);

        buttonToolBar.insert(keepButton, 1);

        return buttonToolBar;
    }
}
