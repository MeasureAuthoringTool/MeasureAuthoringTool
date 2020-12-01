package mat.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;

/**
 * Allows disabling oc anchors:
 * https://stackoverflow.com/questions/8470561/gwt-how-to-disable-the-anchor-link-event-when-clicked
 */
public class MatAnchor extends Anchor {
    @Override
    public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
            case Event.ONDBLCLICK:
            case Event.ONFOCUS:
            case Event.ONCLICK:
                if (!isEnabled()) {
                    return;
                }
                break;
        }
        super.onBrowserEvent(event);
    }
}
