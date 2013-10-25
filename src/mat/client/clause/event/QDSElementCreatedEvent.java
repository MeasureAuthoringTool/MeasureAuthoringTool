package mat.client.clause.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class QDSElementCreatedEvent.
 */
public class QDSElementCreatedEvent extends GwtEvent<QDSElementCreatedEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<QDSElementCreatedEvent.Handler> TYPE = 
		new GwtEvent.Type<QDSElementCreatedEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On creation.
		 * 
		 * @param event
		 *            the event
		 */
		public void onCreation(QDSElementCreatedEvent event);
	}
	
	/** The name. */
	private String name;
	
	/**
	 * Instantiates a new qDS element created event.
	 * 
	 * @param name
	 *            the name
	 */
	public QDSElementCreatedEvent(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
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
		handler.onCreation(this);
	}

}
