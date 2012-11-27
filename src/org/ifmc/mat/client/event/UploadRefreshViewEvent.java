package org.ifmc.mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class UploadRefreshViewEvent extends GwtEvent<UploadRefreshViewEvent.Handler> {
	public static GwtEvent.Type<UploadRefreshViewEvent.Handler> TYPE = 
		new GwtEvent.Type<UploadRefreshViewEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onUploadRefreshView(UploadRefreshViewEvent event);
	}
	
	public UploadRefreshViewEvent() {

	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onUploadRefreshView(this);
	}

}
