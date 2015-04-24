package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class ChangePaswwordSecurityQnsLoginPageEvent.
 */
public class ChangePaswwordSecurityQnsLoginPageEvent extends GwtEvent<ChangePaswwordSecurityQnsLoginPageEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<ChangePaswwordSecurityQnsLoginPageEvent.Handler> TYPE = 
		new GwtEvent.Type<ChangePaswwordSecurityQnsLoginPageEvent.Handler>();
		
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
		public void onFirstLogin(ChangePaswwordSecurityQnsLoginPageEvent event);
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
