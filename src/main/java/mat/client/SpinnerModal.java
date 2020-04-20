package mat.client;

import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

import com.google.gwt.user.client.ui.HTML;

public class SpinnerModal extends Modal {

    private static final String BODY_HTML = "<div class=\"spinner-wrapper\">" +
            "<div class=\"spinner-loading\">\n" +
            "  <span class=\"sr-only\">Loading...</span>\n" +
            "</div>" +
            "</div>";


    public SpinnerModal() {
        ModalBody modalBody = new ModalBody();
        HTML body = new HTML(BODY_HTML);
        modalBody.add(body);
        add(modalBody);
    }

    public void showSpinnerWithTitle(String title) {
        setTitle(title);
        setClosable(false);
        setFade(true);
        setDataBackdrop(ModalBackdrop.STATIC);
        setSize(ModalSize.SMALL);
        getElement().getStyle().setZIndex(9999);
        getElement().setAttribute("id", "loadingSpinnerModalMessage");
        getElement().setAttribute("aria-role", "image");
        getElement().setAttribute("aria-labelledby", "LiveRegion");
        getElement().setAttribute("aria-live", "assertive");
        getElement().setAttribute("aria-atomic", "true");
        getElement().setAttribute("aria-relevant", "all");
        getElement().setAttribute("role", "alert");
        getElement().focus();
        show();
    }
}
