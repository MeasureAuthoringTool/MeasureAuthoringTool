package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class ReturnToLoginEvent.
 */
public class ReturnToLoginEvent extends GwtEvent<ReturnToLoginEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<ReturnToLoginEvent.Handler> TYPE = 
		new GwtEvent.Type<ReturnToLoginEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On return to login.
		 * 
		 * @param event
		 *            the event
		 */
		public void onReturnToLogin(ReturnToLoginEvent event);
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
		handler.onReturnToLogin(this);
	}

}
