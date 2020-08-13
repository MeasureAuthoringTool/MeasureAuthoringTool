package mat.client.shared;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import org.gwtbootstrap3.client.ui.TextBox;


// TODO: Auto-generated Javadoc
/**
 * The Class CustomDateTextBox.
 */
public class CustomDateTimeTextBox extends TextBox {

	/** The max length. */
	private int maxLength;

	
	/**
	 * Instantiates a new custom date text box.
	 *
	 * @param maxLen the max len
	 */
	public CustomDateTimeTextBox(int maxLen) {
		
		super(Document.get().createTextInputElement());
		//setStyleName("gwt-TextArea");
		maxLength = maxLen;	
		sinkEvents(Event.ONPASTE | Event.ONKEYDOWN | Event.ONKEYPRESS);
		
		CustomDateTimeTextBox.this.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (!CustomDateTimeTextBox.this.isReadOnly()) {
					
					//String nameTextArea = event.getValue().replaceAll(" ", "").trim();
					
					/*					String nameTextArea= null;
					if(event.getValue().matches("[0-9]*")){
						nameTextArea = event.getValue();
					} else {
						CustomDateTextBox.this.setValue(nameTextArea);
						setCursorPos(nameTextArea.length()-1);
					}*/
					//String nameTextArea = event.getValue().trim();
					String nameTextArea = getNumerics(event.getValue());
					CustomDateTimeTextBox.this.setText(nameTextArea);
					
					if (nameTextArea.length() >= maxLength) {
						String subStringText = nameTextArea.substring(0, maxLength);
						CustomDateTimeTextBox.this.setValue(subStringText);
						setCursorPos(maxLength);
					} else {
						CustomDateTimeTextBox.this.setValue(nameTextArea);
						setCursorPos(nameTextArea.length());
					}

				}
			}
		});
	}
	
	
	
	/**
	 * Gets the numerics.
	 *
	 * @param s the s
	 * @return the numerics
	 */
	private String getNumerics(String s){

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
		    char ch = s.charAt(i);
		    if (ch =='0' || ch == '1' || ch == '2' || ch == '3'|| ch == '4'|| ch == '5'|| ch == '6'|| ch == '7'|| ch == '8'|| ch == '9') {
		    	sb.append(ch);
		    }
		}
		return sb.toString();
	}
	
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.ValueBoxBase#onBrowserEvent(com.google.gwt.user.client.Event)
	 */
	@Override
	public void onBrowserEvent(Event event) {
		
		String nameTextArea;
		try {
			nameTextArea = CustomDateTimeTextBox.this.getText();
		} catch (Exception e) {
			nameTextArea = "";
		}
		
		//Checking on Click Events
		if (event.getTypeInt() == Event.ONCLICK) {
			NativeEvent nativeEvent = Document.get().createClickEvent(1, 0, 0,
					0, 0, false, false, false, false);
			DomEvent.fireNativeEvent(nativeEvent, this);
		}
		
		if (event.getTypeInt() == Event.ONBLUR) {
			NativeEvent nativeEvent = Document.get().createBlurEvent();
			DomEvent.fireNativeEvent(nativeEvent, this);
		}
		
		
		// Checking for paste event
		if (event.getTypeInt() == Event.ONPASTE) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					ValueChangeEvent.fire(CustomDateTimeTextBox.this,
							CustomDateTimeTextBox.this.getText());
				}
			});
			return;
		}
		// Checking for key Down event.
		if ((event.getTypeInt() == Event.ONKEYDOWN) && (nameTextArea.length() > maxLength)
				&& (event.getKeyCode() != KeyCodes.KEY_LEFT) && (event.getKeyCode() != KeyCodes.KEY_TAB)
				&& (event.getKeyCode() != KeyCodes.KEY_RIGHT) && (event.getKeyCode() != KeyCodes.KEY_DELETE)
				&& (event.getKeyCode() != KeyCodes.KEY_BACKSPACE) && (event.getKeyCode() != KeyCodes.KEY_SHIFT)
				&& (event.getKeyCode() != KeyCodes.KEY_CTRL)) {
			event.preventDefault();
		} 
		else if ((event.getTypeInt() == Event.ONKEYDOWN) && (event.getKeyCode() == KeyCodes.KEY_SPACE)) {
			event.preventDefault();
		} 
		else if ((event.getTypeInt() == Event.ONKEYDOWN) && (nameTextArea.length() <= maxLength)) {
			if ((event.getKeyCode() != KeyCodes.KEY_LEFT) && (event.getKeyCode() != KeyCodes.KEY_TAB)
					&& (event.getKeyCode() != KeyCodes.KEY_RIGHT) && (event.getKeyCode() != KeyCodes.KEY_SHIFT)) {
				if (!event.getCtrlKey()) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							ValueChangeEvent.fire(CustomDateTimeTextBox.this,
									CustomDateTimeTextBox.this.getText());
						}
					});
				}
			}
		}
	}
	
	
	/**
	 * Gets the max length.
	 *
	 * @return the max length
	 */
	public int getMaxLength() {
		return maxLength;
	}
	
	
	/* (non-Javadoc)
	 * @see org.gwtbootstrap3.client.ui.base.ValueBoxBase#setMaxLength(int)
	 */
	@Override
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}
