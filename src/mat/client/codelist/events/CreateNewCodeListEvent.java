package mat.client.codelist.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CreateNewCodeListEvent extends GwtEvent<CreateNewCodeListEvent.Handler> {
	public static GwtEvent.Type<CreateNewCodeListEvent.Handler> TYPE = 
		new GwtEvent.Type<CreateNewCodeListEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onCreateNewCodeList(CreateNewCodeListEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onCreateNewCodeList(this);
	}

}
