package mat.client.util;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;

public class MatTextBox extends TextBox {
	/**
	 * property for holding maximum length.
	 */
	private int minLength;
	
	/**
	 * Constructor.
	 *
	 * @param maxLen the max len
	 */
	public MatTextBox() {
		
		super(Document.get().createTextInputElement());
		removeStyleName("gwt-Text-box");
		setStyleName("form-control");
		sinkEvents(Event.ONPASTE | Event.ONKEYDOWN | Event.ONKEYPRESS | Event.ONKEYUP);
		MatTextBox.this.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				int keycode = event.getNativeKeyCode();

				if (event.isControlKeyDown()) {
					if(keycode == 86 || keycode == 88){
						Scheduler.get().scheduleDeferred(() -> execute());
					}
				} else if((keycode>=65 && keycode<=90) || (keycode>=48 && keycode<=57)
						|| (keycode==32) || (keycode==8)|| (keycode==59)
						|| (keycode==9) || (keycode==13) || (keycode==16) || (keycode==18)
						|| (keycode>=37 && keycode<=40) ){
					Scheduler.get().scheduleDeferred(() -> execute());
				}
			}
		});
	}
	/**
	 * Description: Takes the browser event.
	 *
	 * @param event
	 *            declared.
	 */
	@Override
	public void onBrowserEvent(Event event) {
		// Checking for paste event
		if (event.getTypeInt() == Event.ONPASTE) {
			Scheduler.get().scheduleDeferred(() -> execute());
			return;
		}

		if (event.getTypeInt() == Event.ONKEYDOWN) {
			NativeEvent nativeEvent = Document.get().createKeyUpEvent(false, false, false, false, event.getKeyCode());
			DomEvent.fireNativeEvent(nativeEvent, this);
		}
		// Checking for key Down event.
		if ((event.getTypeInt() == Event.ONKEYDOWN)) {
			if ((event.getKeyCode() != KeyCodes.KEY_LEFT)
					&& (event.getKeyCode() != KeyCodes.KEY_TAB)
					&& (event.getKeyCode() != KeyCodes.KEY_RIGHT)
					&& (event.getKeyCode() != KeyCodes.KEY_SHIFT)) {
				if (!event.getCtrlKey()) {
					Scheduler.get().scheduleDeferred(() -> execute());
				} 
			}
		}
	}
	/**
	 * Getter for maximum length.
	 * @return - int.
	 */
	public int getMinLength() {
		return minLength;
	}

	/**
	 * Setter for maximum length.
	 *
	 * @param maxLength the new max length
	 */
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	
	private void execute() {
		ValueChangeEvent.fire(MatTextBox.this, MatTextBox.this.getText());
	}
}