package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class TimedOutEvent.
 */
public class TimedOutEvent extends GwtEvent<TimedOutEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<TimedOutEvent.Handler> TYPE = 
		new GwtEvent.Type<TimedOutEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On timed out.
		 * 
		 * @param event
		 *            the event
		 */
		public void onTimedOut(TimedOutEvent event);
	}
	
	/**
	 * Instantiates a new timed out event.
	 */
	public TimedOutEvent() {
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
		handler.onTimedOut(this);
	}

}
