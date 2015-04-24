package mat.client.shared.search;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * The Interface HasPageSelectionHandler.
 */
public interface HasPageSelectionHandler {
	
	/**
	 * Adds the page selection handler.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration
	 */
	public HandlerRegistration addPageSelectionHandler(PageSelectionEventHandler handler);
}
