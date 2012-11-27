package org.ifmc.mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ExternalViewerEvent extends GwtEvent<ExternalViewerEvent.Handler> {
	public static GwtEvent.Type<ExternalViewerEvent.Handler> TYPE = 
		new GwtEvent.Type<ExternalViewerEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onExternalView(ExternalViewerEvent event);
	}

	
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onExternalView(this);
	}

}
