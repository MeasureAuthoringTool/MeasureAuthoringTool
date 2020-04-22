package mat.client.shared;


import org.gwtbootstrap3.client.shared.event.ModalHideEvent;
import org.gwtbootstrap3.client.shared.event.ModalHideHandler;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import mat.client.buttons.NoButton;
import mat.client.buttons.YesButton;


public class ConfirmationDialogBox {
    protected final Button yesButton = new YesButton("ConfirmDialogBox");
    protected final Button noButton = new NoButton("ConfirmDialogBox");
    protected ConfirmationObserver observer;
    private MessagePanel messagePanel = new MessagePanel();
    protected Modal panel = new Modal();
    protected boolean isClosingOnContinue = false;
    protected boolean isError;
    private ClickHandler handler;

    public MessagePanel getMessageAlert() {
        return messagePanel;
    }

    public void setMessageAlert(MessagePanel messageAlert) {
        this.messagePanel = messageAlert;
    }

    public ConfirmationDialogBox() {

    }

    public ConfirmationDialogBox(String messageText, String yesButtonText, String noButtonText, ConfirmationObserver observer) {
        this(messageText, yesButtonText, noButtonText, observer, true);
    }

    public ConfirmationDialogBox(String messageText, String yesButtonText, String noButtonText, ConfirmationObserver observer, boolean isError) {
        this.observer = observer;
        this.isError = isError;
        if (isError) {
            this.messagePanel.getErrorMessageAlert().createAlert(messageText);
        } else {
            this.messagePanel.getWarningMessageAlert().createAlert(messageText);
        }

        getNoButton().setText(noButtonText);
        getNoButton().setTitle(noButtonText);
        getYesButton().setTitle(yesButtonText);
        getYesButton().setText(yesButtonText);
        getYesButton().setFocus(true);
    }

    public ConfirmationDialogBox(String messageText, String yesButtonText, String noButtonText) {
        this.observer = new ConfirmationObserver() {
            @Override
            public void onYesButtonClicked() {
                // No action
            }

            @Override
            public void onNoButtonClicked() {
                // No action
            }

            @Override
            public void onClose() {
                // No action
            }
        };
        getMessageAlert().getErrorMessageAlert().createAlert(messageText);
        setMargins(getMessageAlert().getErrorMessageAlert());
        getNoButton().setText(noButtonText);
        getNoButton().setTitle(noButtonText);
        getYesButton().setText(yesButtonText);
        getYesButton().setTitle(yesButtonText);
        getYesButton().setFocus(true);
    }

    public void show() {
        ModalBody modalBody = new ModalBody();

        modalBody.clear();
        panel.remove(modalBody);
        panel.setTitle("Warning");
        panel.setDataKeyboard(true);
        panel.setClosable(true);
        panel.setFade(true);
        panel.setDataBackdrop(ModalBackdrop.STATIC);
        panel.setSize(ModalSize.MEDIUM);
        panel.getElement().getStyle().setZIndex(9999);
        panel.setRemoveOnHide(true);

        if (handler == null) {
            handler = MatContext.get().addClickHandlerToResetTimeoutWarning();
        }

        panel.addDomHandler(handler, ClickEvent.getType());

        panel.addHideHandler(new ModalHideHandler() {
            @Override
            public void onHide(ModalHideEvent evt) {
                if (!isClosingOnContinue) {
                    observer.onClose();
                }
            }
        });

        getYesButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                isClosingOnContinue = true;
                observer.onYesButtonClicked();
            }
        });
        getNoButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                isClosingOnContinue = false;
                observer.onNoButtonClicked();
            }
        });

        if (isError) {
            setMargins(messagePanel.getErrorMessageAlert());
        } else {
            setMargins(messagePanel.getWarningMessageAlert());
        }

        modalBody.add(messagePanel);


        ModalFooter modalFooter = new ModalFooter();
        ButtonToolBar buttonToolBar = new ButtonToolBar();
        yesButton.setSize(ButtonSize.SMALL);
        noButton.setSize(ButtonSize.SMALL);
        yesButton.setDataDismiss(ButtonDismiss.MODAL);
        noButton.setDataDismiss(ButtonDismiss.MODAL);
        buttonToolBar.add(yesButton);
        buttonToolBar.add(noButton);

        modalFooter.add(buttonToolBar);

        panel.add(modalBody);

        panel.add(modalFooter);
        panel.getElement().focus();
        panel.show();
    }

    private void setMargins(MessageAlert messageAlert) {
        messageAlert.getElement().getStyle().setMarginTop(0.0, Style.Unit.PX);
        messageAlert.getElement().getStyle().setMarginBottom(0.0, Style.Unit.PX);
        messageAlert.getElement().setAttribute("role", "alert");
    }

    public void hide() {
        panel.hide();
    }


    public Button getYesButton() {
        return yesButton;
    }

    public Button getNoButton() {
        return noButton;
    }

    public void setObserver(ConfirmationObserver observer) {
        this.observer = observer;
    }

}
