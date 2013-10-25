package mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class ExternalViewerEvent.
 */
public class ExternalViewerEvent extends GwtEvent<ExternalViewerEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<ExternalViewerEvent.Handler> TYPE = 
		new GwtEvent.Type<ExternalViewerEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On external view.
		 * 
		 * @param event
		 *            the event
		 */
		public void onExternalView(ExternalViewerEvent event);
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
		handler.onExternalView(this);
	}

}
