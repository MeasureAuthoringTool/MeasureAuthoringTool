package mat.client.shared;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import org.gwtbootstrap3.client.ui.TextArea;


public class CustomTextAreaWithMaxLength extends TextArea  {
	private int maxLength;
	
	
	/**
	 * Constructor.
	 *
	 * @param maxLen the max len
	 */
	public CustomTextAreaWithMaxLength(int maxLen) {
		
		super(Document.get().createTextAreaElement());
		maxLength = maxLen;
		//setStyleName("gwt-TextArea");
		sinkEvents(Event.ONPASTE | Event.ONKEYDOWN | Event.ONKEYPRESS);
		
		CustomTextAreaWithMaxLength.this
		.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (!CustomTextAreaWithMaxLength.this.isReadOnly()) {
					String commentAreaUpdatedText;
					int pos = getCursorPos();
					CustomTextAreaWithMaxLength.this.setText(event.getValue());
					try {
						commentAreaUpdatedText = CustomTextAreaWithMaxLength.this.getText();
					} catch (Exception e) {
						commentAreaUpdatedText = "";
					}
					if (commentAreaUpdatedText.length() >= maxLength) {
						String subStringText = commentAreaUpdatedText.substring(0,
								maxLength);
						CustomTextAreaWithMaxLength.this.setValue(subStringText);
						setCursorPos(maxLength);
					} else {
						CustomTextAreaWithMaxLength.this.setValue(commentAreaUpdatedText);
						setCursorPos(pos);
					}
					
					//onTextAreaContentChanged(remainingCharsLabel);
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
		String commentAreaContent;
		try {
			commentAreaContent = CustomTextAreaWithMaxLength.this.getText();
		} catch (Exception e) {
			commentAreaContent = "";
		}
		// Checking for paste event
		if (event.getTypeInt() == Event.ONPASTE) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					ValueChangeEvent.fire(CustomTextAreaWithMaxLength.this,
							CustomTextAreaWithMaxLength.this.getText());
				}
			});
			return;
		}
		// Checking for key Down event.
		if ((event.getTypeInt() == Event.ONKEYDOWN)
				&& (commentAreaContent.length() > maxLength)
				&& (event.getKeyCode() != KeyCodes.KEY_LEFT)
				&& (event.getKeyCode() != KeyCodes.KEY_TAB)
				&& (event.getKeyCode() != KeyCodes.KEY_RIGHT)
				&& (event.getKeyCode() != KeyCodes.KEY_DELETE)
				&& (event.getKeyCode() != KeyCodes.KEY_BACKSPACE)
				&& (event.getKeyCode() != KeyCodes.KEY_SHIFT)
				&& (event.getKeyCode() != KeyCodes.KEY_CTRL)) {
			event.preventDefault();
		} else if ((event.getTypeInt() == Event.ONKEYDOWN)
				&& (commentAreaContent.length() <= maxLength)) {
			if ((event.getKeyCode() != KeyCodes.KEY_LEFT)
					&& (event.getKeyCode() != KeyCodes.KEY_TAB)
					&& (event.getKeyCode() != KeyCodes.KEY_RIGHT)
					&& (event.getKeyCode() != KeyCodes.KEY_SHIFT)) {
				if (!event.getCtrlKey()) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							ValueChangeEvent.fire(CustomTextAreaWithMaxLength.this,
									CustomTextAreaWithMaxLength.this.getText());
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
	public int getMaxLength() {
		return maxLength;
	}
	
	/**
	 * Setter for maximum length.
	 *
	 * @param maxLength the new max length
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

}
