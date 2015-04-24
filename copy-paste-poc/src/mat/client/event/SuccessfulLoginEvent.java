package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class SuccessfulLoginEvent.
 */
public class SuccessfulLoginEvent extends GwtEvent<SuccessfulLoginEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<SuccessfulLoginEvent.Handler> TYPE = 
		new GwtEvent.Type<SuccessfulLoginEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On successful login.
		 * 
		 * @param event
		 *            the event
		 */
		public void onSuccessfulLogin(SuccessfulLoginEvent event);
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
		handler.onSuccessfulLogin(this);
	}

}
