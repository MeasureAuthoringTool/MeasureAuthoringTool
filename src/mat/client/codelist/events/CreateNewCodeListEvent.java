package mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class CreateNewCodeListEvent.
 */
public class CreateNewCodeListEvent extends GwtEvent<CreateNewCodeListEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<CreateNewCodeListEvent.Handler> TYPE = 
		new GwtEvent.Type<CreateNewCodeListEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On create new code list.
		 * 
		 * @param event
		 *            the event
		 */
		public void onCreateNewCodeList(CreateNewCodeListEvent event);
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
		handler.onCreateNewCodeList(this);
	}

}
