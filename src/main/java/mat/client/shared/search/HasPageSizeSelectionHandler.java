package mat.client.shared.search;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * The Interface HasPageSizeSelectionHandler.
 */
public interface HasPageSizeSelectionHandler {
	
	/**
	 * Adds the page size selection handler.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration
	 */
	public HandlerRegistration addPageSizeSelectionHandler(PageSizeSelectionEventHandler handler);
}
