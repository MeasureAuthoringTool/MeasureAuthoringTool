package mat.client.shared.editor;

import com.google.gwt.user.client.ui.RichTextArea;

public class RichTextEditor extends RichTextArea {
	
	public RichTextEditor(){
		super();
		this.setWidth("625px");
		this.setHeight("350px");
	}

	public String getFormattedText() {
		// if the trimmed plain text of the formatter is empty 
		// we want the formatted text to be empty
		if(this.getText() == null || this.getText().trim().isEmpty()) {
			return null;
		}
		return this.getHTML();
	}
	
	public void setEditorText(String text) {
		if(text != null) {
			text = text.replaceAll("\r\n", "<p>");
			text = text.replaceAll("\n", "<p>");
			if(!text.contains("<font face=\"Arial\">")) {
				text = "<font face=\"Arial\">" + text + "</font>";
			}
		}
		this.setHTML(text);
	}
	
	public void setReadOnly(Boolean readOnly) {
		this.setEnabled(readOnly);
	}
}
