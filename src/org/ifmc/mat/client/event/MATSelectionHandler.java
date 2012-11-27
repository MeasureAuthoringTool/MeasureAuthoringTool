package org.ifmc.mat.client.event;


import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;

/**
 * Use this class to conditionally execute event behavior based on a check delegated to MATEventHandler
 * @author aschmidt
 *
 * @param <Integer>
 */
public abstract class MATSelectionHandler<Integer> extends MATEventHandler implements SelectionHandler<Integer>{

	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		handleEvent(event);
	}

}
