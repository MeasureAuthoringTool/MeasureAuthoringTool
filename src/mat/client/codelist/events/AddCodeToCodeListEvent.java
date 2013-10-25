package mat.client.codelist.events;



import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class AddCodeToCodeListEvent.
 */
public class AddCodeToCodeListEvent extends GwtEvent<AddCodeToCodeListEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<AddCodeToCodeListEvent.Handler> TYPE = 
		new GwtEvent.Type<AddCodeToCodeListEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On add code to code list.
		 * 
		 * @param event
		 *            the event
		 */
		public void onAddCodeToCodeList(AddCodeToCodeListEvent event);
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
		handler.onAddCodeToCodeList(this);
	}
}
