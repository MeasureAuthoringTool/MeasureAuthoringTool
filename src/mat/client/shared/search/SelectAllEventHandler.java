package mat.client.shared.search;

import com.google.gwt.event.shared.EventHandler;

/**
 * The Interface SelectAllEventHandler.
 */
public interface SelectAllEventHandler  extends EventHandler {
	
	/**
	 * On select all.
	 * 
	 * @param event
	 *            the event
	 */
	public void onSelectAll(SelectAllEvent event);
}
