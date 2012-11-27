package mat.client.shared.search;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasPageSelectionHandler {
	public HandlerRegistration addPageSelectionHandler(PageSelectionEventHandler handler);
}
