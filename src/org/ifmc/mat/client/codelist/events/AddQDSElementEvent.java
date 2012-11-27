package org.ifmc.mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AddQDSElementEvent extends GwtEvent<AddQDSElementEvent.Handler> {
	public static GwtEvent.Type<AddQDSElementEvent.Handler> TYPE = 
		new GwtEvent.Type<AddQDSElementEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onAddQDSElement(AddQDSElementEvent event);
	}

	
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onAddQDSElement(this);
	}

}
