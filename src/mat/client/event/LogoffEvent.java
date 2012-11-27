package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class LogoffEvent extends GwtEvent<LogoffEvent.Handler> {
	public static GwtEvent.Type<LogoffEvent.Handler> TYPE = 
		new GwtEvent.Type<LogoffEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onLogoff(LogoffEvent event);
	}
	
	public LogoffEvent() {
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onLogoff(this);
	}

}
