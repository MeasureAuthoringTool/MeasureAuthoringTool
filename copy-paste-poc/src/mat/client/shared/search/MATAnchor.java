package mat.client.shared.search;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;

/**
 * The Class MATAnchor.
 * 
 * @author aschmidt
 */
public class MATAnchor extends Anchor{
	
	
	/**
	 * Instantiates a new mAT anchor.
	 * 
	 * @param label
	 *            the label
	 */
	public MATAnchor(String label) {
		super(label);
	}

	
	/**
	 * ensure events are not propagated if not enabled.
	 * 
	 * @param event
	 *            the event
	 */
	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
			case Event.ONDBLCLICK:
			case Event.ONFOCUS:
			case Event.ONCLICK:
				if (!isEnabled()) {
					return;
				}
				break;
		}
		super.onBrowserEvent(event);
	}
}
