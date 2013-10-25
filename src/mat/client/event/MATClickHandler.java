package mat.client.event;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * The Class MATClickHandler.
 */
public abstract class MATClickHandler extends MATEventHandler implements ClickHandler{

	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event) {
		handleEvent(event);
	}

}
