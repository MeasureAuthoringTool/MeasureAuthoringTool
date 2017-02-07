package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class MeasureEditEvent.
 */
public class CQLLibraryEditEvent extends GwtEvent<CQLLibraryEditEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<CQLLibraryEditEvent.Handler> TYPE = 
		new GwtEvent.Type<CQLLibraryEditEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On measure edit.
		 * 
		 * @param event
		 *            the event
		 */
		public void onCQLLibraryEdit(CQLLibraryEditEvent event);
	}
	
	/**
	 * Instantiates a new measure edit event.
	 */
	public CQLLibraryEditEvent() {

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
		handler.onCQLLibraryEdit(this);
	}
}
