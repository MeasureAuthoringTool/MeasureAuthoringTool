package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ReturnToLoginEvent extends GwtEvent<ReturnToLoginEvent.Handler> {
	public static GwtEvent.Type<ReturnToLoginEvent.Handler> TYPE = 
		new GwtEvent.Type<ReturnToLoginEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onReturnToLogin(ReturnToLoginEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onReturnToLogin(this);
	}

}
