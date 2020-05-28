package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class SwitchUserEvent extends GwtEvent<SwitchUserEvent.Handler> {

    public static Type<SwitchUserEvent.Handler> TYPE = new Type<>();

    private String newUserId;

    public SwitchUserEvent(String newUserId) {
        this.newUserId = newUserId;
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onLogoff(this);
    }

    public interface Handler extends EventHandler {
        void onLogoff(SwitchUserEvent event);
    }

    public String getNewUserId() {
        return newUserId;
    }

}
