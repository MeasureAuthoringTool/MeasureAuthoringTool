package org.ifmc.mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CancelEditCodeListEvent extends GwtEvent<CancelEditCodeListEvent.Handler> {
	public static GwtEvent.Type<CancelEditCodeListEvent.Handler> TYPE = 
		new GwtEvent.Type<CancelEditCodeListEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onCancelEditCodeList(CancelEditCodeListEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onCancelEditCodeList(this);
	}

}
