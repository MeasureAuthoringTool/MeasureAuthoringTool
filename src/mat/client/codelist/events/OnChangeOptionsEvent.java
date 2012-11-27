package mat.client.codelist.events;



import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class OnChangeOptionsEvent extends GwtEvent<OnChangeOptionsEvent.Handler> {
	public static GwtEvent.Type<OnChangeOptionsEvent.Handler> TYPE = 
		new GwtEvent.Type<OnChangeOptionsEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onChangeOptions(OnChangeOptionsEvent event);
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