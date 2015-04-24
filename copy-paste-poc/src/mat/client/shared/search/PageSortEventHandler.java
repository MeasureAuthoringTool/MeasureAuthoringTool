package mat.client.shared.search;

import com.google.gwt.event.shared.EventHandler;

/**
 * The Interface PageSortEventHandler.
 */
public interface PageSortEventHandler extends EventHandler {
	
	/**
	 * On page sort.
	 * 
	 * @param event
	 *            the event
	 */
	public void onPageSort(PageSortEvent event);
}
