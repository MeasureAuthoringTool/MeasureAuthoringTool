package mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class CancelEditCodeListEvent.
 */
public class CancelEditCodeListEvent extends GwtEvent<CancelEditCodeListEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<CancelEditCodeListEvent.Handler> TYPE = 
		new GwtEvent.Type<CancelEditCodeListEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On cancel edit code list.
		 * 
		 * @param event
		 *            the event
		 */
		public void onCancelEditCodeList(CancelEditCodeListEvent event);
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
		handler.onCancelEditCodeList(this);
	}

}
