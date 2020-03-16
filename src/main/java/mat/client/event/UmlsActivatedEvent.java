package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class UmlsActivatedEvent extends GwtEvent<UmlsActivatedEvent.Handler> {

    public static Type<UmlsActivatedEvent.Handler> TYPE = new Type<UmlsActivatedEvent.Handler>();

    public interface Handler extends EventHandler {
        void onSuccessfulLogin(UmlsActivatedEvent event);
    }

    public UmlsActivatedEvent() {
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onSuccessfulLogin(this);
    }

}
