package mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CancelAddCodeEvent extends GwtEvent<CancelAddCodeEvent.Handler> {
	public static GwtEvent.Type<CancelAddCodeEvent.Handler> TYPE = 
		new GwtEvent.Type<CancelAddCodeEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onCancelAddCode(CancelAddCodeEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onCancelAddCode(this);
	}

}
