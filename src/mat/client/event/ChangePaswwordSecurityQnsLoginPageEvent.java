package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ChangePaswwordSecurityQnsLoginPageEvent extends GwtEvent<ChangePaswwordSecurityQnsLoginPageEvent.Handler> {
	public static GwtEvent.Type<ChangePaswwordSecurityQnsLoginPageEvent.Handler> TYPE = 
		new GwtEvent.Type<ChangePaswwordSecurityQnsLoginPageEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onFirstLogin(ChangePaswwordSecurityQnsLoginPageEvent event);
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
