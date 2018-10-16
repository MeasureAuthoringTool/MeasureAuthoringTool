package mat.client.shared.search;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * The Interface HasSortHandler.
 */
public interface HasSortHandler {
	
	/**
	 * Adds the page sort handler.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration
	 */
	public HandlerRegistration addPageSortHandler(PageSortEventHandler handler);
}
