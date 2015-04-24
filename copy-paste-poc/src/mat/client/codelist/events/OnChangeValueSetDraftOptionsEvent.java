package mat.client.codelist.events;



import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class OnChangeValueSetDraftOptionsEvent.
 * 
 * @author aschmidt
 */
public class OnChangeValueSetDraftOptionsEvent extends GwtEvent<OnChangeValueSetDraftOptionsEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<OnChangeValueSetDraftOptionsEvent.Handler> TYPE = 
		new GwtEvent.Type<OnChangeValueSetDraftOptionsEvent.Handler>();
		
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
		public void onChangeOptions(OnChangeValueSetDraftOptionsEvent event);
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
