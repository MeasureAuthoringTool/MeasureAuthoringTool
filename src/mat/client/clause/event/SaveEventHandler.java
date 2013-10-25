package mat.client.clause.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * The Interface SaveEventHandler.
 */
public interface SaveEventHandler extends EventHandler {
	
	/**
	 * On save.
	 * 
	 * @param event
	 *            the event
	 */
	void onSave(SaveEvent event);
}
