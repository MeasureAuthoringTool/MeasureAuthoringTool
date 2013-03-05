package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ForgotLoginIDEvent extends GwtEvent<ForgotLoginIDEvent.Handler> {
	public static GwtEvent.Type<ForgotLoginIDEvent.Handler> TYPE = 
		new GwtEvent.Type<ForgotLoginIDEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onForgottenLoginID(ForgotLoginIDEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onForgottenLoginID(this);
	}

}

