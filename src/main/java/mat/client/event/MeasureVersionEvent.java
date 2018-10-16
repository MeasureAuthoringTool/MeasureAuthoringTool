package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


public class MeasureVersionEvent extends GwtEvent<MeasureVersionEvent.Handler> {
	
	/** The is versioned. */
	private boolean isVersioned; 
	
	/** The version name. */
	private String versionName;
	
	/** The message. */
	private String message;

	/** The type. */
	public static com.google.gwt.event.shared.GwtEvent.Type<Handler> TYPE = 
			new GwtEvent.Type<MeasureVersionEvent.Handler>();
	
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
		public void onVersioned(MeasureVersionEvent event);
	}
	
	/**
	 * Instantiates a new measure delete event.
	 * 
	 * @param isDeleted
	 *            the is deleted
	 * @param message
	 *            the message
	 */
	public MeasureVersionEvent(boolean isVersioned, String versionName, String message){
		this.isVersioned=isVersioned;
		this.versionName = versionName;
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
		handler.onVersioned(this);
		
	}

	/**
	 * @return the isVersioned
	 */
	public boolean isVersioned() {
		return isVersioned;
	}

	/**
	 * @param isVersioned the isVersioned to set
	 */
	public void setVersioned(boolean isVersioned) {
		this.isVersioned = isVersioned;
	}

	/**
	 * @return the versionName
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * @param versionName the versionName to set
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
