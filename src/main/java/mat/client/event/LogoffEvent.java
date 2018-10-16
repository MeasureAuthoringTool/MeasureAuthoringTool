package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class LogoffEvent.
 */
public class LogoffEvent extends GwtEvent<LogoffEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<LogoffEvent.Handler> TYPE = 
		new GwtEvent.Type<LogoffEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On logoff.
		 * 
		 * @param event
		 *            the event
		 */
		public void onLogoff(LogoffEvent event);
	}
	
	/**
	 * Instantiates a new logoff event.
	 */
	public LogoffEvent() {
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(Handler handler) {
		handler.onLogoff(this);
	}

}
