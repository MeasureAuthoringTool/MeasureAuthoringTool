package org.ifmc.mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class TimedOutEvent extends GwtEvent<TimedOutEvent.Handler> {
	public static GwtEvent.Type<TimedOutEvent.Handler> TYPE = 
		new GwtEvent.Type<TimedOutEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onTimedOut(TimedOutEvent event);
	}
	
	public TimedOutEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onTimedOut(this);
	}

}
