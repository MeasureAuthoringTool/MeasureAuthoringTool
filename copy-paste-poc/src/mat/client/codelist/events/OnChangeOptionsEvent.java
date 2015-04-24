package mat.client.codelist.events;



import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class OnChangeOptionsEvent.
 */
public class OnChangeOptionsEvent extends GwtEvent<OnChangeOptionsEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<OnChangeOptionsEvent.Handler> TYPE = 
		new GwtEvent.Type<OnChangeOptionsEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On change options.
		 * 
		 * @param event
		 *            the event
		 */
		public void onChangeOptions(OnChangeOptionsEvent event);
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
		handler.onChangeOptions(this);
	}
}
