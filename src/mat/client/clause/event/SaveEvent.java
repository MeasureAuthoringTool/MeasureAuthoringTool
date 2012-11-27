package mat.client.clause.event;

import com.google.gwt.event.shared.GwtEvent;

public class SaveEvent extends GwtEvent<SaveEventHandler> {
	public static Type<SaveEventHandler> TYPE = new Type<SaveEventHandler>();

	@Override
	public Type<SaveEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveEventHandler handler) {
		handler.onSave(this);
	}
}
