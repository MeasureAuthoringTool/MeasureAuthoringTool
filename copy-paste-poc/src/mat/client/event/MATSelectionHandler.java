package mat.client.event;


import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;

/**
 * Use this class to conditionally execute event behavior based on a check
 * delegated to MATEventHandler.
 * 
 * @param <Integer>
 *            the generic type
 * @author aschmidt
 */
public abstract class MATSelectionHandler<Integer> extends MATEventHandler implements SelectionHandler<Integer>{

	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.SelectionHandler#onSelection(com.google.gwt.event.logical.shared.SelectionEvent)
	 */
	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		handleEvent(event);
	}

}
