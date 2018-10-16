package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class ForgotLoginIDEvent.
 */
public class ForgotLoginIDEvent extends GwtEvent<ForgotLoginIDEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<ForgotLoginIDEvent.Handler> TYPE = 
		new GwtEvent.Type<ForgotLoginIDEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On forgotten login id.
		 * 
		 * @param event
		 *            the event
		 */
		public void onForgottenLoginID(ForgotLoginIDEvent event);
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
		handler.onForgottenLoginID(this);
	}

}

