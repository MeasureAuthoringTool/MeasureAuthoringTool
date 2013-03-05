package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ForgotLoginIDEmailSentEvent extends GwtEvent<ForgotLoginIDEmailSentEvent.Handler> {
	public static GwtEvent.Type<ForgotLoginIDEmailSentEvent.Handler> TYPE = 
		new GwtEvent.Type<ForgotLoginIDEmailSentEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onForgotLoginIdEmailSent(ForgotLoginIDEmailSentEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onForgotLoginIdEmailSent(this);
	}

}
