package mat.client.event;


import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;

@SuppressWarnings("hiding")
public abstract class MATSelectionHandler<Integer> extends MATEventHandler implements SelectionHandler<Integer>{

	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		handleEvent(event);
	}

}
