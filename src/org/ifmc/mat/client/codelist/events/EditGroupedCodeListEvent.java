package org.ifmc.mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EditGroupedCodeListEvent extends GwtEvent<EditGroupedCodeListEvent.Handler> {
	public static GwtEvent.Type<EditGroupedCodeListEvent.Handler> TYPE = 
		new GwtEvent.Type<EditGroupedCodeListEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onEditGroupedCodeList(EditGroupedCodeListEvent event);
	}

	
	private String key;
	public EditGroupedCodeListEvent(String k) {
		key = k;
	}
	public String getKey() {
		return key;
	}
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onEditGroupedCodeList(this);
	}

}
