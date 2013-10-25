package mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class CancelAddCodeEvent.
 */
public class CancelAddCodeEvent extends GwtEvent<CancelAddCodeEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<CancelAddCodeEvent.Handler> TYPE = 
		new GwtEvent.Type<CancelAddCodeEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On cancel add code.
		 * 
		 * @param event
		 *            the event
		 */
		public void onCancelAddCode(CancelAddCodeEvent event);
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
		handler.onCancelAddCode(this);
	}

}
