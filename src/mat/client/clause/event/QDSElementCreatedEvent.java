package mat.client.clause.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class QDSElementCreatedEvent extends GwtEvent<QDSElementCreatedEvent.Handler> {
	public static GwtEvent.Type<QDSElementCreatedEvent.Handler> TYPE = 
		new GwtEvent.Type<QDSElementCreatedEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onCreation(QDSElementCreatedEvent event);
	}
	
	private String name;
	public QDSElementCreatedEvent(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onCreation(this);
	}

}
