package mat.client.codelist.events;



import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AddCodeToCodeListEvent extends GwtEvent<AddCodeToCodeListEvent.Handler> {
	public static GwtEvent.Type<AddCodeToCodeListEvent.Handler> TYPE = 
		new GwtEvent.Type<AddCodeToCodeListEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onAddCodeToCodeList(AddCodeToCodeListEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onAddCodeToCodeList(this);
	}
}