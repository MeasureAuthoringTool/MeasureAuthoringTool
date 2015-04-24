package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class BackToLoginPageEvent.
 */
public class BackToLoginPageEvent extends GwtEvent<BackToLoginPageEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<BackToLoginPageEvent.Handler> TYPE = 
		new GwtEvent.Type<BackToLoginPageEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On login failure.
		 * 
		 * @param event
		 *            the event
		 */
		public void onLoginFailure(BackToLoginPageEvent event);
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
		handler.onLoginFailure(this);
	}

}
