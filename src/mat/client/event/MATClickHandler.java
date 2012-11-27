package mat.client.event;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public abstract class MATClickHandler extends MATEventHandler implements ClickHandler{

	@Override
	public void onClick(ClickEvent event) {
		handleEvent(event);
	}

}
