package mat.client.shared.search;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * The Interface HasSelectAllHandler.
 */
public interface HasSelectAllHandler {
	
	/**
	 * Adds the select all handler.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration
	 */
	public HandlerRegistration addSelectAllHandler(SelectAllEventHandler handler);
}
