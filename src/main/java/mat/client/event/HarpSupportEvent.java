package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class HarpSupportEvent extends GwtEvent<HarpSupportEvent.Handler> {

    public static Type<HarpSupportEvent.Handler> TYPE = new Type<>();

    public interface Handler extends EventHandler {
        void onHarpSupportEvent(HarpSupportEvent event);
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onHarpSupportEvent(this);
    }

}
