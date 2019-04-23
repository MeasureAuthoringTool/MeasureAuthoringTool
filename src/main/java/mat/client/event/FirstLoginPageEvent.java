package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class FirstLoginPageEvent.
 */
public class FirstLoginPageEvent extends GwtEvent<FirstLoginPageEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<FirstLoginPageEvent.Handler> TYPE = 
		new GwtEvent.Type<FirstLoginPageEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On first login.
		 * 
		 * @param event
		 *            the event
		 */
		public void onFirstLogin(FirstLoginPageEvent event);
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
		handler.onFirstLogin(this);
	}

}
