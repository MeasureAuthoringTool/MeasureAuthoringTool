package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MeasureEditEvent extends GwtEvent<MeasureEditEvent.Handler> {
	public static GwtEvent.Type<MeasureEditEvent.Handler> TYPE = 
		new GwtEvent.Type<MeasureEditEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onMeasureEdit(MeasureEditEvent event);
	}
	
	public MeasureEditEvent() {

	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onMeasureEdit(this);
	}
}
