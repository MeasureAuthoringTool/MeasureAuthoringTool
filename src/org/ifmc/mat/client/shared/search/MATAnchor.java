package org.ifmc.mat.client.shared.search;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;

/**
 * 
 * @author aschmidt
 *
 */
public class MATAnchor extends Anchor{
	
	
	public MATAnchor(String label) {
		super(label);
	}

	
	/**
	 * ensure events are not propagated if not enabled
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
