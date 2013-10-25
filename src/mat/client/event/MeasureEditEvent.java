package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class MeasureEditEvent.
 */
public class MeasureEditEvent extends GwtEvent<MeasureEditEvent.Handler> {
	
	/** The type. */
	public static GwtEvent.Type<MeasureEditEvent.Handler> TYPE = 
		new GwtEvent.Type<MeasureEditEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On measure edit.
		 * 
		 * @param event
		 *            the event
		 */
		public void onMeasureEdit(MeasureEditEvent event);
	}
	
	/**
	 * Instantiates a new measure edit event.
	 */
	public MeasureEditEvent() {

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
		handler.onMeasureEdit(this);
	}
}
