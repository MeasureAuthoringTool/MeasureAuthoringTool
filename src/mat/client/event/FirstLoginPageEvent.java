package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FirstLoginPageEvent extends GwtEvent<FirstLoginPageEvent.Handler> {
	public static GwtEvent.Type<FirstLoginPageEvent.Handler> TYPE = 
		new GwtEvent.Type<FirstLoginPageEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onFirstLogin(FirstLoginPageEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onFirstLogin(this);
	}

}
