package mat.client.shared.search;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasSortHandler {
	public HandlerRegistration addPageSortHandler(PageSortEventHandler handler);
}
