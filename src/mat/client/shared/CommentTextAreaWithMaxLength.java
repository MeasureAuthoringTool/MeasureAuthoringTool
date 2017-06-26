package mat.client.shared;

import org.gwtbootstrap3.client.ui.TextArea;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;


public class CommentTextAreaWithMaxLength extends TextArea  {

	
	private int maxLength;
	
	
	/**
	 * Constructor.
	 *
	 * @param maxLen the max len
	 */
	public CommentTextAreaWithMaxLength(int maxLen) {
		
		super(Document.get().createTextAreaElement());
		maxLength = maxLen;
		//setStyleName("gwt-TextArea");
		sinkEvents(Event.ONPASTE | Event.ONKEYDOWN | Event.ONKEYPRESS);
		
		CommentTextAreaWithMaxLength.this
		.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (!CommentTextAreaWithMaxLength.this.isReadOnly()) {
					String commentAreaUpdatedText = getRestrictedString(event.getValue());
					int pos = getCursorPos();
					CommentTextAreaWithMaxLength.this.setText(commentAreaUpdatedText);
					/*try {
						commentAreaUpdatedText = CommentTextAreaWithMaxLength.this.getText();
					} catch (Exception e) {
						commentAreaUpdatedText = "";
					}*/
					if (commentAreaUpdatedText.length() >= maxLength) {
						String subStringText = commentAreaUpdatedText.substring(0,
								maxLength);
						CommentTextAreaWithMaxLength.this.setValue(subStringText);
						setCursorPos(maxLength);
					} else {
						CommentTextAreaWithMaxLength.this.setValue(commentAreaUpdatedText);
						setCursorPos(commentAreaUpdatedText.length());
					}
					
				}
			}
		});
	}
	
	private String getRestrictedString(String str) {
		StringBuilder sb = new StringBuilder();
		char prev = 0;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);

			if (prev == '*' && ch == '/' || prev == '/' && ch == '*') {
				return sb.toString();
			}

			prev = ch;
			sb.append(ch);
		}
		return sb.toString();
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
			commentAreaContent = CommentTextAreaWithMaxLength.this.getText();
		} catch (Exception e) {
			commentAreaContent = "";
		}
		// Checking for paste event
		if (event.getTypeInt() == Event.ONPASTE) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					ValueChangeEvent.fire(CommentTextAreaWithMaxLength.this,
							CommentTextAreaWithMaxLength.this.getText());
				}
			});
			return;
		}
		
		if (event.getTypeInt() == Event.ONKEYUP) {
			NativeEvent nativeEvent = Document.get().createKeyUpEvent(false, false, false, false, event.getTypeInt());
			DomEvent.fireNativeEvent(nativeEvent, this);
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
							ValueChangeEvent.fire(CommentTextAreaWithMaxLength.this,
									CommentTextAreaWithMaxLength.this.getText());
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
