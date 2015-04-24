package mat.client.measure.metadata.events;



import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class ContinueToMeasurePackageEvent.
 */
public class ContinueToMeasurePackageEvent extends GwtEvent<ContinueToMeasurePackageEvent.Handler>{
	
	/** The type. */
	public static GwtEvent.Type<ContinueToMeasurePackageEvent.Handler> TYPE = 
		new GwtEvent.Type<ContinueToMeasurePackageEvent.Handler>();
		
	/**
	 * The Interface Handler.
	 */
	public static interface Handler extends EventHandler {
		
		/**
		 * On continue to measure package.
		 * 
		 * @param event
		 *            the event
		 */
		public void onContinueToMeasurePackage(ContinueToMeasurePackageEvent event);
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
		handler.onContinueToMeasurePackage(this);
	}
	
	
}
