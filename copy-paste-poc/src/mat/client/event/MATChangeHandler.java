package mat.client.event;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;

/**
 * The Class MATChangeHandler.
 */
public abstract class MATChangeHandler extends MATEventHandler implements ChangeHandler{

	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.ChangeHandler#onChange(com.google.gwt.event.dom.client.ChangeEvent)
	 */
	@Override
	public void onChange(ChangeEvent event) {
		onEvent(event);
		
	}

}
