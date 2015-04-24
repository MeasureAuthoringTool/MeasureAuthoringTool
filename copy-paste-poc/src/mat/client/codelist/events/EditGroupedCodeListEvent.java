package mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class EditGroupedCodeListEvent.
 */
public class EditGroupedCodeListEvent extends GwtEvent<EditGroupedCodeListEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<EditGroupedCodeListEvent.Handler> TYPE = 
		new GwtEvent.Type<EditGroupedCodeListEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On edit grouped code list.
		 * 
		 * @param event
		 *            the event
		 */
		public void onEditGroupedCodeList(EditGroupedCodeListEvent event);
	}

	
	/** The key. */
	private String key;
	
	/**
	 * Instantiates a new edits the grouped code list event.
	 * 
	 * @param k
	 *            the k
	 */
	public EditGroupedCodeListEvent(String k) {
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
		handler.onEditGroupedCodeList(this);
	}

}
