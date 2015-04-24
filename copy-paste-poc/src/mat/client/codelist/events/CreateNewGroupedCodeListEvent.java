package mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class CreateNewGroupedCodeListEvent.
 */
public class CreateNewGroupedCodeListEvent extends GwtEvent<CreateNewGroupedCodeListEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<CreateNewGroupedCodeListEvent.Handler> TYPE = 
		new GwtEvent.Type<CreateNewGroupedCodeListEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On create new grouped code list.
		 * 
		 * @param event
		 *            the event
		 */
		public void onCreateNewGroupedCodeList(CreateNewGroupedCodeListEvent event);
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
		handler.onCreateNewGroupedCodeList(this);
	}

}
