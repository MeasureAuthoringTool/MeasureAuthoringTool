package mat.client.codelist.events;



import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author aschmidt
 *
 */
public class OnChangeMeasureVersionOptionsEvent extends GwtEvent<OnChangeMeasureVersionOptionsEvent.Handler> {
	public static GwtEvent.Type<OnChangeMeasureVersionOptionsEvent.Handler> TYPE = 
		new GwtEvent.Type<OnChangeMeasureVersionOptionsEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onChangeOptions(OnChangeMeasureVersionOptionsEvent event);
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
