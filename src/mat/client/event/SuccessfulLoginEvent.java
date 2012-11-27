package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class SuccessfulLoginEvent extends GwtEvent<SuccessfulLoginEvent.Handler> {
	public static GwtEvent.Type<SuccessfulLoginEvent.Handler> TYPE = 
		new GwtEvent.Type<SuccessfulLoginEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onSuccessfulLogin(SuccessfulLoginEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onSuccessfulLogin(this);
	}

}
