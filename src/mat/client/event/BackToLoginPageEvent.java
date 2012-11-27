package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class BackToLoginPageEvent extends GwtEvent<BackToLoginPageEvent.Handler> {
	public static GwtEvent.Type<BackToLoginPageEvent.Handler> TYPE = 
		new GwtEvent.Type<BackToLoginPageEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onLoginFailure(BackToLoginPageEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onLoginFailure(this);
	}

}
