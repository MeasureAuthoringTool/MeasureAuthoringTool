package mat.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class LogoffEvent.
 */
public class LogoffEvent extends GwtEvent<LogoffEvent.Handler> {

    public static GwtEvent.Type<LogoffEvent.Handler> TYPE =
            new GwtEvent.Type<>();

    /**
     * The Interface Handler.
     */
    public interface Handler extends EventHandler {

        /**
         * On logoff.
         *
         * @param event the event
         */
        public void onLogoff(LogoffEvent event);
    }

    /**
     * Instantiates a new logoff event.
     */
    public LogoffEvent() {
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onLogoff(this);
    }

}
