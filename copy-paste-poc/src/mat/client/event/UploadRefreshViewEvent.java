package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class UploadRefreshViewEvent.
 */
public class UploadRefreshViewEvent extends GwtEvent<UploadRefreshViewEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<UploadRefreshViewEvent.Handler> TYPE = 
		new GwtEvent.Type<UploadRefreshViewEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On upload refresh view.
		 * 
		 * @param event
		 *            the event
		 */
		public void onUploadRefreshView(UploadRefreshViewEvent event);
	}
	
	/**
	 * Instantiates a new upload refresh view event.
	 */
	public UploadRefreshViewEvent() {

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
		handler.onUploadRefreshView(this);
	}

}
