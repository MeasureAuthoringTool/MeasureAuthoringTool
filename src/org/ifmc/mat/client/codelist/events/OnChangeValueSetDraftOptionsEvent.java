package org.ifmc.mat.client.codelist.events;



import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author aschmidt
 *
 */
public class OnChangeValueSetDraftOptionsEvent extends GwtEvent<OnChangeValueSetDraftOptionsEvent.Handler> {
	public static GwtEvent.Type<OnChangeValueSetDraftOptionsEvent.Handler> TYPE = 
		new GwtEvent.Type<OnChangeValueSetDraftOptionsEvent.Handler>();
		
	public static interface Handler extends EventHandler {
		public void onChangeOptions(OnChangeValueSetDraftOptionsEvent event);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onChangeOptions(this);
	}
}