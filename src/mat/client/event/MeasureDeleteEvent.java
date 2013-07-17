package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MeasureDeleteEvent extends GwtEvent<MeasureDeleteEvent.Handler> {
	
	private boolean isDeleted; 
	private String message;

	public static com.google.gwt.event.shared.GwtEvent.Type<Handler> TYPE = 
			new GwtEvent.Type<MeasureDeleteEvent.Handler>();
	
	public static interface Handler extends EventHandler {
		public void onDeletion(MeasureDeleteEvent event);
	}
	
	public MeasureDeleteEvent(boolean isDeleted, String message){
		this.isDeleted=isDeleted;
		this.message = message;
		
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onDeletion(this);
		
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
