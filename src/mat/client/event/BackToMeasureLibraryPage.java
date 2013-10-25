package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class BackToMeasureLibraryPage.
 */
public class BackToMeasureLibraryPage extends GwtEvent<BackToMeasureLibraryPage.Handler> {

	/** The type. */
	public static com.google.gwt.event.shared.GwtEvent.Type<Handler> TYPE = 
			new GwtEvent.Type<BackToMeasureLibraryPage.Handler>();
	
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On deleted.
		 * 
		 * @param event
		 *            the event
		 */
		public void onDeleted(BackToMeasureLibraryPage event);
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
		handler.onDeleted(this);
	}

}
