package mat.client.clause.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class SaveEvent.
 */
public class SaveEvent extends GwtEvent<SaveEventHandler> {
	
	/** The type. */
	public static Type<SaveEventHandler> TYPE = new Type<SaveEventHandler>();

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public Type<SaveEventHandler> getAssociatedType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(SaveEventHandler handler) {
		handler.onSave(this);
	}
}
