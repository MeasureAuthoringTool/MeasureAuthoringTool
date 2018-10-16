package mat.client.shared.search;

import com.google.gwt.event.shared.EventHandler;

/**
 * The Interface PageSizeSelectionEventHandler.
 */
public interface PageSizeSelectionEventHandler extends EventHandler {
	
	/**
	 * On page size selection.
	 * 
	 * @param event
	 *            the event
	 */
	public void onPageSizeSelection(PageSizeSelectionEvent event);
}
