package mat.client.harp;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class HarpUserVerificationEvent extends GwtEvent<HarpUserVerificationEvent.Handler> {

    /** The type. */
    public static GwtEvent.Type<HarpUserVerificationEvent.Handler> TYPE =
            new GwtEvent.Type<HarpUserVerificationEvent.Handler>();

    /**
     * The Interface Handler.
     */
    public interface Handler extends EventHandler {

        /**
         * On forgotten password.
         *
         * @param event
         *            the event
         */
        void verifyUser(HarpUserVerificationEvent event);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<HarpUserVerificationEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
     */
    @Override
    protected void dispatch(HarpUserVerificationEvent.Handler handler) {
        handler.verifyUser(this);
    }
}
