package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class MeasureDeleteEvent.
 */
public class MeasureDeleteEvent extends GwtEvent<MeasureDeleteEvent.Handler> {
	
	/** The is deleted. */
	private boolean isDeleted; 
	
	/** The message. */
	private String message;

	/** The type. */
	public static com.google.gwt.event.shared.GwtEvent.Type<Handler> TYPE = 
			new GwtEvent.Type<MeasureDeleteEvent.Handler>();
	
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On deletion.
		 * 
		 * @param event
		 *            the event
		 */
		public void onDeletion(MeasureDeleteEvent event);
	}
	
	/**
	 * Instantiates a new measure delete event.
	 * 
	 * @param isDeleted
	 *            the is deleted
	 * @param message
	 *            the message
	 */
	public MeasureDeleteEvent(boolean isDeleted, String message){
		this.isDeleted=isDeleted;
		this.message = message;
		
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
		handler.onDeletion(this);
		
	}

	/**
	 * Checks if is deleted.
	 * 
	 * @return true, if is deleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * Sets the deleted.
	 * 
	 * @param isDeleted
	 *            the new deleted
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
