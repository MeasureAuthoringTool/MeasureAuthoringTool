package mat.client.harp;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class SuccessfulHarpLoginEvent extends GwtEvent<SuccessfulHarpLoginEvent.Handler> {
    /** The type. */
    public static GwtEvent.Type<SuccessfulHarpLoginEvent.Handler> TYPE =
            new GwtEvent.Type<>();

    /**
     * The Interface Handler.
     */
    public interface Handler extends EventHandler {

        /**
         * On successful login.
         *
         * @param event
         *            the event
         */
        void onSuccesfulHarpLogin(SuccessfulHarpLoginEvent event);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SuccessfulHarpLoginEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
     */
    @Override
    protected void dispatch(SuccessfulHarpLoginEvent.Handler handler) {
        handler.onSuccesfulHarpLogin(this);
    }
}
