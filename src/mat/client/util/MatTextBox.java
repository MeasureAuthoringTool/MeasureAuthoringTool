package mat.client.util;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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
		setStyleName("Text-Box");
		sinkEvents(Event.ONPASTE | Event.ONKEYDOWN | Event.ONKEYPRESS | Event.ONKEYUP);
		MatTextBox.this.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				int keycode = event.getNativeKeyCode();
				System.out.println("Keycode Pressed:" +keycode);
				if (event.isControlKeyDown()) {
					if(keycode == 86 || keycode == 88){
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							ValueChangeEvent.fire(MatTextBox.this,
									MatTextBox.this.getText());
						}
					});
				}
				} else if((keycode>=65 && keycode<=90) || (keycode>=48 && keycode<=57)
						|| (keycode==32) || (keycode==8)|| (keycode==59)
						|| (keycode==9) || (keycode==13) || (keycode==16) || (keycode==18)
						|| (keycode>=37 && keycode<=40) ){
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							ValueChangeEvent.fire(MatTextBox.this,
									MatTextBox.this.getText());
						}
					});
				}
			}
		});
//		MatTextBox.this
//		.addValueChangeHandler(new ValueChangeHandler<String>() {
//			
//			@Override
//			public void onValueChange(ValueChangeEvent<String> event) {
//				if (!MatTextBox.this.isReadOnly()) {
////					String commentAreaUpdatedText;
////					int pos = getCursorPos();
////					MatTextBox.this.setText(event.getValue());
////					try {
////						commentAreaUpdatedText = MatTextBox.this.getText();
////					} catch (Exception e) {
////						commentAreaUpdatedText = "";
////					}
////					if (commentAreaUpdatedText.length() >= minLength) {
////						String subStringText = commentAreaUpdatedText.substring(0,
////								minLength);
////						MatTextBox.this.setValue(subStringText);
////						setCursorPos(minLength);
////					} else {
////						MatTextBox.this.setValue(commentAreaUpdatedText);
////						setCursorPos(pos);
////					}
//					
//				}
//			}
//		});
	}
	/**
	 * Description: Takes the browser event.
	 *
	 * @param event
	 *            declared.
	 */
	@Override
	public void onBrowserEvent(Event event) {
		String commentAreaContent;
		try {
			commentAreaContent = MatTextBox.this.getText();
		} catch (Exception e) {
			commentAreaContent = "";
		}
		// Checking for paste event
		if (event.getTypeInt() == Event.ONPASTE) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					ValueChangeEvent.fire(MatTextBox.this,
							MatTextBox.this.getText());
				}
			});
			return;
		}
		
		if (event.getTypeInt() == Event.ONKEYDOWN) {
			NativeEvent nativeEvent = Document.get().createKeyUpEvent(false, false, false, false, event.getTypeInt());
			DomEvent.fireNativeEvent(nativeEvent, this);
		}
		// Checking for key Down event.
	    if ((event.getTypeInt() == Event.ONKEYDOWN)) {
			if ((event.getKeyCode() != KeyCodes.KEY_LEFT)
					&& (event.getKeyCode() != KeyCodes.KEY_TAB)
					&& (event.getKeyCode() != KeyCodes.KEY_RIGHT)
					&& (event.getKeyCode() != KeyCodes.KEY_SHIFT)) {
				if (!event.getCtrlKey()) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							ValueChangeEvent.fire(MatTextBox.this,
									MatTextBox.this.getText());
						}
					});
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
}