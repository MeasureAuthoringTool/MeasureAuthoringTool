package mat.client.shared.search;

import com.google.gwt.event.shared.EventHandler;

/**
 * The Interface PageSelectionEventHandler.
 */
public interface PageSelectionEventHandler extends EventHandler {
	
	/**
	 * On page selection.
	 * 
	 * @param event
	 *            the event
	 */
	public void onPageSelection(PageSelectionEvent event);
}
