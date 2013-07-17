package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class BackToMeasureLibraryPage extends GwtEvent<BackToMeasureLibraryPage.Handler> {

	public static com.google.gwt.event.shared.GwtEvent.Type<Handler> TYPE = 
			new GwtEvent.Type<BackToMeasureLibraryPage.Handler>();
	
	public static interface Handler extends EventHandler {
		public void onDeleted(BackToMeasureLibraryPage event);
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onDeleted(this);
	}

}
