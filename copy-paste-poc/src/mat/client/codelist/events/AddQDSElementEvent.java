package mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class AddQDSElementEvent.
 */
public class AddQDSElementEvent extends GwtEvent<AddQDSElementEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<AddQDSElementEvent.Handler> TYPE = 
		new GwtEvent.Type<AddQDSElementEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On add qds element.
		 * 
		 * @param event
		 *            the event
		 */
		public void onAddQDSElement(AddQDSElementEvent event);
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
		handler.onAddQDSElement(this);
	}

}
