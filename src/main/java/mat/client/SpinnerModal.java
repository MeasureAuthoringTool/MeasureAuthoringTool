package mat.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import mat.client.shared.MatContext;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

public class SpinnerModal extends Modal {

    private static SpinnerModal modal;
    private static ClickHandler handler;
    private static HandlerRegistration handlerRegistration;

    private static final String BODY_HTML = "<div class=\"spinner-wrapper\">" +
            "<div class=\"spinner-loading\">\n" +
            "  <span class=\"sr-only\">Loading...</span>\n" +
            "</div>" +
            "</div>";

    public SpinnerModal() {
        HTML body = new HTML(BODY_HTML);
        ModalBody modalBody = new ModalBody();
        modalBody.add(body);
        add(modalBody);

        setFade(true);
        setDataBackdrop(ModalBackdrop.STATIC);
        setSize(ModalSize.SMALL);
        setDataKeyboard(false);
        setClosable(false);
        setRemoveOnHide(true);
        getElement().getStyle().setZIndex(9999);
        getElement().setAttribute("id", "loadingSpinnerModalMessage");
        getElement().setAttribute("aria-role", "image");
        getElement().setAttribute("aria-labelledby", "LiveRegion");
        getElement().setAttribute("aria-live", "assertive");
        getElement().setAttribute("aria-atomic", "true");
        getElement().setAttribute("aria-relevant", "all");
        getElement().setAttribute("role", "alert");
        if (handler == null) {
            handler = MatContext.get().addClickHandlerToResetTimeoutWarning();
        }
        if (handlerRegistration != null) {
            handlerRegistration.removeHandler();
        }
        handlerRegistration = addDomHandler(handler, ClickEvent.getType());

        getElement().focus();
    }

    public static void showSpinnerWithTitle(String title) {
        if (modal == null) {
            modal = new SpinnerModal();
        }
        modal.setTitle(title);
        modal.show();
    }

    public static void hideSpinner() {
        if (modal != null) {
            modal.hide();
        }
    }

}
