package mat.client.event;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;

public abstract class MATChangeHandler extends MATEventHandler implements ChangeHandler{

	@Override
	public void onChange(ChangeEvent event) {
		onEvent(event);
		
	}

}
