package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


public class EditCompositeMeasureEvent extends GwtEvent<EditCompositeMeasureEvent.Handler>{
	
	public static com.google.gwt.event.shared.GwtEvent.Type<Handler> TYPE =  new GwtEvent.Type<EditCompositeMeasureEvent.Handler>();
	
	public static interface Handler extends EventHandler {
			public void onFire(EditCompositeMeasureEvent event);
	}
	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(Handler handler) {
		handler.onFire(this);
		
	}
}