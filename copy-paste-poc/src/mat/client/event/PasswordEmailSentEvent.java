package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class PasswordEmailSentEvent.
 */
public class PasswordEmailSentEvent extends GwtEvent<PasswordEmailSentEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<PasswordEmailSentEvent.Handler> TYPE = 
		new GwtEvent.Type<PasswordEmailSentEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On password email sent.
		 * 
		 * @param event
		 *            the event
		 */
		public void onPasswordEmailSent(PasswordEmailSentEvent event);
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
		handler.onPasswordEmailSent(this);
	}

}
