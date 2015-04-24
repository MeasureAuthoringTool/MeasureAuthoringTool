package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class TemporaryPasswordLoginEvent.
 */
public class TemporaryPasswordLoginEvent extends GwtEvent<TemporaryPasswordLoginEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<TemporaryPasswordLoginEvent.Handler> TYPE = 
		new GwtEvent.Type<TemporaryPasswordLoginEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On temp password login.
		 * 
		 * @param event
		 *            the event
		 */
		public void onTempPasswordLogin(TemporaryPasswordLoginEvent event);
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
		handler.onTempPasswordLogin(this);
	}
}
