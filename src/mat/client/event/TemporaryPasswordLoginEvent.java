package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class TemporaryPasswordLoginEvent extends GwtEvent<TemporaryPasswordLoginEvent.Handler> {
	public static GwtEvent.Type<TemporaryPasswordLoginEvent.Handler> TYPE = 
		new GwtEvent.Type<TemporaryPasswordLoginEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onTempPasswordLogin(TemporaryPasswordLoginEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onTempPasswordLogin(this);
	}
}
