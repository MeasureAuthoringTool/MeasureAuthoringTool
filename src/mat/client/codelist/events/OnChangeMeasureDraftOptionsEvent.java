package mat.client.codelist.events;



import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author aschmidt
 *
 */
public class OnChangeMeasureDraftOptionsEvent extends GwtEvent<OnChangeMeasureDraftOptionsEvent.Handler> {
	public static GwtEvent.Type<OnChangeMeasureDraftOptionsEvent.Handler> TYPE = 
		new GwtEvent.Type<OnChangeMeasureDraftOptionsEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onChangeOptions(OnChangeMeasureDraftOptionsEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onChangeOptions(this);
	}
}