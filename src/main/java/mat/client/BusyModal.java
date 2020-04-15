package mat.client;

import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;

public class BusyModal extends Modal {
    private String bodyHtml = "<h1>Loading please wait ...</h1>";
    public BusyModal() {
        DialogBox d = new DialogBox();
        setClosable(false);
        ModalBody modalBody = new ModalBody();
        modalBody.add(new HTML(bodyHtml));
        add(modalBody);
        getElement().focus();
    }
    @Override
    public void show() {
        Scheduler.get().scheduleDeferred(() -> {
            center();
            super.show();
        });
    }
    public void center() {
        int left = Window.getClientWidth() - this.getOffsetWidth() >> 1;
        int top = Window.getClientHeight() - this.getOffsetHeight() >> 1;
        this.setPopupPosition(Math.max(Window.getScrollLeft() + left, 0), Math.max(Window.getScrollTop() + top, 0));
    }
    public void setPopupPosition(int left, int top) {
        Element elem = this.getElement();
        elem.getStyle().setPropertyPx("left", left);
        elem.getStyle().setPropertyPx("top", top);
    }
}
