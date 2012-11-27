package org.ifmc.mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CreateNewGroupedCodeListEvent extends GwtEvent<CreateNewGroupedCodeListEvent.Handler> {
	public static GwtEvent.Type<CreateNewGroupedCodeListEvent.Handler> TYPE = 
		new GwtEvent.Type<CreateNewGroupedCodeListEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onCreateNewGroupedCodeList(CreateNewGroupedCodeListEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onCreateNewGroupedCodeList(this);
	}

}
