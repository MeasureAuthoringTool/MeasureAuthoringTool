package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class ForgottenPasswordEvent.
 */
public class ForgottenPasswordEvent extends GwtEvent<ForgottenPasswordEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<ForgottenPasswordEvent.Handler> TYPE = 
		new GwtEvent.Type<ForgottenPasswordEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On forgotten password.
		 * 
		 * @param event
		 *            the event
		 */
		public void onForgottenPassword(ForgottenPasswordEvent event);
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
		handler.onForgottenPassword(this);
	}

}
