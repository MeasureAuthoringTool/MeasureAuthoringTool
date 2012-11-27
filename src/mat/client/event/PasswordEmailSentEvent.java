package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class PasswordEmailSentEvent extends GwtEvent<PasswordEmailSentEvent.Handler> {
	public static GwtEvent.Type<PasswordEmailSentEvent.Handler> TYPE = 
		new GwtEvent.Type<PasswordEmailSentEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onPasswordEmailSent(PasswordEmailSentEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onPasswordEmailSent(this);
	}

}
