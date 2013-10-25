package mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class EditCodeListEvent.
 */
public class EditCodeListEvent extends GwtEvent<EditCodeListEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<EditCodeListEvent.Handler> TYPE = 
		new GwtEvent.Type<EditCodeListEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On edit code list.
		 * 
		 * @param event
		 *            the event
		 */
		public void onEditCodeList(EditCodeListEvent event);
	}

	
	/** The key. */
	private String key;
	
	/**
	 * Instantiates a new edits the code list event.
	 * 
	 * @param k
	 *            the k
	 */
	public EditCodeListEvent(String k) {
		key = k;
	}
	
	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
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
		handler.onEditCodeList(this);
	}

}
