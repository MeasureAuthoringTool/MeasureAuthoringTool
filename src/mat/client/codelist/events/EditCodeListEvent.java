package mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EditCodeListEvent extends GwtEvent<EditCodeListEvent.Handler> {
	public static GwtEvent.Type<EditCodeListEvent.Handler> TYPE = 
		new GwtEvent.Type<EditCodeListEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onEditCodeList(EditCodeListEvent event);
	}

	
	private String key;
	public EditCodeListEvent(String k) {
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
		handler.onEditCodeList(this);
	}

}
