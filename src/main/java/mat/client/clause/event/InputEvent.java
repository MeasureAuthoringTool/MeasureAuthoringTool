package mat.client.clause.event;

import com.google.gwt.event.dom.client.DomEvent;

public class InputEvent extends DomEvent<InputHandler>{

	private static final Type<InputHandler> TYPE = new Type<InputHandler>("input", new InputEvent());

	public static Type<InputHandler> getType() {
		return TYPE;
	}

	protected InputEvent() {
	}

	@Override
	public Type<InputHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(InputHandler handler) {
		handler.onInput(this);
	}

}
