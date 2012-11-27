package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ForgottenPasswordEvent extends GwtEvent<ForgottenPasswordEvent.Handler> {
	public static GwtEvent.Type<ForgottenPasswordEvent.Handler> TYPE = 
		new GwtEvent.Type<ForgottenPasswordEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onForgottenPassword(ForgottenPasswordEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onForgottenPassword(this);
	}

}
