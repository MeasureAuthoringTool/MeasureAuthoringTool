package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class ForgotLoginIDEmailSentEvent.
 */
public class ForgotLoginIDEmailSentEvent extends GwtEvent<ForgotLoginIDEmailSentEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<ForgotLoginIDEmailSentEvent.Handler> TYPE = 
		new GwtEvent.Type<ForgotLoginIDEmailSentEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On forgot login id email sent.
		 * 
		 * @param event
		 *            the event
		 */
		public void onForgotLoginIdEmailSent(ForgotLoginIDEmailSentEvent event);
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
		handler.onForgotLoginIdEmailSent(this);
	}

}
